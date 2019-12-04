package dev.mher.taskhunter.services;

import dev.mher.taskhunter.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
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


    @Value("${app.frontend.baseUrl}")
    private String frontendBaseUrl;

    @Value("${auth.confirmationToken.lifetime}")
    private int tokenLifetime;

    @Autowired
    public AuthenticationService(EmailService emailService, UserModel userModel, CryptoService cryptoService) {
        this.emailService = emailService;
        this.userModel = userModel;
        this.cryptoService = cryptoService;
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

        LocalDate daysBehind = LocalDate.now().minusDays(tokenLifetime);
        Timestamp interval = Timestamp.valueOf(daysBehind.atStartOfDay());

        return userModel.userConfirm(interval);
    }

    public boolean signIn(String email, String password, Boolean rememberMe) {

        UserModel user = userModel.findByEmail(email);

        if (user == null) {
            // invalid email
            return false;
        }

        if (!cryptoService.compare(user.getPassword(), password)) {
            // password does not match
            return false;
        }


        // use remember me for session duration setup




        return true;
    }

}
