package dev.mher.taskhunter.models.misc.authentication;

import dev.mher.taskhunter.models.misc.Response;

import java.util.HashMap;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 5:48 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.responses.
 */
public class SignInResponse extends Response {
    private HashMap<String, String> user;

    public HashMap<String, String> getUser() {
        return user;
    }

    public void setUser(HashMap<String, String> user) {
        this.user = user;
    }

    public CharSequence getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(CharSequence accessToken) {
        this.accessToken = accessToken;
    }

    private CharSequence accessToken;

    public SignInResponse() {

    }
    public SignInResponse(HashMap<String, String> user, CharSequence accessToken) {
        this.setError(false);
        this.setUser(user);
        this.setAccessToken(accessToken);
    }
}
