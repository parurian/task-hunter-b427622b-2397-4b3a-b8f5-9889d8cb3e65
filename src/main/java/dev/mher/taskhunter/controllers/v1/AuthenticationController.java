package dev.mher.taskhunter.controllers.v1;

import dev.mher.taskhunter.models.misc.authentication.SignInResponse;
import dev.mher.taskhunter.services.AuthenticationService;
import dev.mher.taskhunter.utils.RequestUtils;
import dev.mher.taskhunter.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 11:26 AM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.controllers.
 */
@RestController
@RequestMapping("/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RequestUtils requestUtils;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, RequestUtils requestUtils) {
        this.authenticationService = authenticationService;
        this.requestUtils = requestUtils;
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(
            @RequestBody SignUpParams params
    ) {

        String ipAddress = requestUtils.getClientIp();
        boolean isSent = authenticationService.signUp(params.getEmail(), params.getPassword(), params.getFirstName(), params.getLastName(), ipAddress);

        if (isSent) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("isSent", true);
            return ResponseEntity.ok(new ResponseUtils(response));
        }
        return ResponseEntity.ok(new ResponseUtils(true, "USER_ALREADY_EXISTS"));
    }


    @GetMapping("/confirm/{token}")
    public ResponseEntity confirmUser(
            @PathVariable("token") String token
    ) {
        boolean isUpdated = authenticationService.userConfirm(token);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUpdated", isUpdated);
        return ResponseEntity.ok(new ResponseUtils(response));
    }

    @PostMapping("/sign-in")
    public ResponseEntity signIn(
            @RequestBody SignInParams params
    ) {
        SignInResponse signInResponse = authenticationService.signIn(params.getEmail(), params.getPassword(), params.getRememberMe());

        if (signInResponse.isError()) {
            return ResponseEntity.ok(new ResponseUtils(signInResponse.isError(), signInResponse.getMessage()));
        }

        CharSequence accessToken = signInResponse.getAccessToken();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Access-Token", accessToken.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(new ResponseUtils(signInResponse.getUser()));
    }

}

class SignInParams {
    private String email;
    private String password;
    private Boolean rememberMe;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }
}

class SignUpParams {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}