package dev.mher.taskhunter.models.misc;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 5:48 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.responses.
 */
public class Response {
    private boolean error;
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
