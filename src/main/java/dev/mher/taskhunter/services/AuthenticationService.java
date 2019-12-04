package dev.mher.taskhunter.services;

import dev.mher.taskhunter.models.UserModel;
import dev.mher.taskhunter.models.responses.SignInResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 11:52 AM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.services.
 */
@Service
public class AuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final EmailService emailService;
    private final UserModel userModel;
    private final CryptoService cryptoService;
    private final JwtService jwtService;


    @Value("${app.frontend.baseUrl}")
    private String frontendBaseUrl;

    @Value("${auth.confirmationToken.lifetime}")
    private int confirmationTokenLifetimeDays;

    @Value("${auth.signInToken.lifetime}")
    private long signInTokenLifetimeSecs;

    @Value("${jsonWebToken.lifetime}")
    private long jwtTokenLifetime;


    @Autowired
    public AuthenticationService(EmailService emailService, UserModel userModel, CryptoService cryptoService, JwtService jwtService) {
        this.emailService = emailService;
        this.userModel = userModel;
        this.cryptoService = cryptoService;
        this.jwtService = jwtService;
    }


    public boolean signUp(String email, CharSequence password, String firstName, String lastName, String ipAddress) {
        // hash password
        CharSequence passwordHash = cryptoService.generateHash(password);

        // create unique confirmation token
        String confirmationToken = UUID.randomUUID().toString();

        // create registration time
        Timestamp confirmationSentAt = new Timestamp(System.currentTimeMillis());
        try {
            // add user record in db
            userModel.setEmail(email);
            userModel.setPassword(passwordHash);
            userModel.setConfirmationToken(confirmationToken);
            userModel.setConfirmationSentAt(confirmationSentAt);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);
            if (!userModel.signUp()) {
                return false;
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
            return false;
        }
        try {
            // send confirmation email
            String fullName = firstName + ' ' + lastName;
            String date = confirmationSentAt.toString();
            String link = String.format("%s/confirm/%s", frontendBaseUrl, confirmationToken);
            emailService.sendSignUpMail(email, fullName, link, date, ipAddress);
        } catch (Exception e) {
            logger.info(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return true;
    }


    public boolean userConfirm(String token) {
        userModel.setConfirmationToken(token);
        userModel.setActive(true);

        LocalDate daysBehind = LocalDate.now().minusDays(confirmationTokenLifetimeDays);
        Timestamp interval = Timestamp.valueOf(daysBehind.atStartOfDay());

        return userModel.userConfirm(interval);
    }

    public SignInResponse signIn(String email, String password, Boolean rememberMe) {
        UserModel user = userModel.findByEmail(email);

        SignInResponse signInResponse = new SignInResponse();

        // invalid email
        if (user == null) {
            signInResponse.setError(true);
            signInResponse.setMessage("INVALID_EMAIL");
            return signInResponse;
        }

        if (!cryptoService.compare(password, user.getPassword())) {
            signInResponse.setError(true);
            signInResponse.setMessage("PASSWORD_DOES_NOT_MATCH");
            return signInResponse;
        }
        long lifetime = rememberMe != null && rememberMe ? signInTokenLifetimeSecs : jwtTokenLifetime;

        String subject = String.valueOf(user.getUserId());
        CharSequence accessToken = jwtService.encode(subject, lifetime);

        if (accessToken == null) {
            signInResponse.setError(true);
            signInResponse.setMessage("INTERNAL_ERROR");
            return signInResponse;
        }

        signInResponse.setError(false);
        signInResponse.setMessage("OK");
        signInResponse.setAccessToken(accessToken);

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        userMap.put("email", user.getEmail());
        signInResponse.setUser(userMap);

        return signInResponse;
    }

}
