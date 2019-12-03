package dev.mher.taskhunter.controllers.v1;

import dev.mher.taskhunter.utils.RequestUtils;
import dev.mher.taskhunter.services.AuthenticationService;
import dev.mher.taskhunter.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RequestUtils requestUtils;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, RequestUtils requestUtils) {
        this.authenticationService = authenticationService;
        this.requestUtils = requestUtils;
    }

    @PostMapping("sign-up")
    public ResponseEntity signUp(
            @RequestBody String email,
            @RequestBody CharSequence password,
            @RequestBody String firstName,
            @RequestBody String lastName
    ) {

        String ipAddress = requestUtils.getClientIp();
        boolean isSent = authenticationService.signUp(email, password, firstName, lastName, ipAddress);

        if (isSent) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("isSent", true);
            return ResponseEntity.ok(new ResponseUtils(response));
        }
        return ResponseEntity.ok(new ResponseUtils(true, "USER_ALREADY_EXISTS"));
    }


    @GetMapping("confirm/{token}")
    public ResponseEntity confirmUser(
            @PathVariable("token") String token
    ) {
        boolean isUpdated = authenticationService.userConfirm(token);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUpdated", isUpdated);
        return ResponseEntity.ok(new ResponseUtils(response));
    }

    @PostMapping("sign-in")
    public ResponseEntity signIn(
            @RequestBody String email,
            @RequestBody String password,
            @RequestBody Boolean rememberMe
    ) {
        boolean isAuthenticated = authenticationService.signIn(email, password, rememberMe);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAuthenticated", isAuthenticated);
        return ResponseEntity.ok(new ResponseUtils(response));
    }

}
