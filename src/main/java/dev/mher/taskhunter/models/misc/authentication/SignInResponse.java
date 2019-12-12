package dev.mher.taskhunter.models.misc.authentication;

import dev.mher.taskhunter.models.misc.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 5:48 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.responses.
 */

@Getter
@Setter
public class SignInResponse extends Response {
    private HashMap<String, String> user;
    private CharSequence accessToken;

    public SignInResponse() {

    }
    public SignInResponse(HashMap<String, String> user, CharSequence accessToken) {
        this.setError(false);
        this.setUser(user);
        this.setAccessToken(accessToken);
    }
}
