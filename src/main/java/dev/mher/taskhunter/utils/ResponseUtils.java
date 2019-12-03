package dev.mher.taskhunter.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 4:01 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.utils.
 */

@Component
public class ResponseUtils {

    private boolean error;
    private String errorMessage;
    private Object result;

    public ResponseUtils() {

    }

    public ResponseUtils(boolean error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public ResponseUtils(Object result) {
        this.error = false;
        this.result = result;
    }

    public ResponseUtils(String key, Object value) {
        Map<String, Object> response = new HashMap<>();
        response.put(key, value);
        this.result = response;
        this.error = false;
    }


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseUtils{" +
                "error=" + error +
                ", errorMessage='" + errorMessage + '\'' +
                ", result=" + result +
                '}';
    }
}
