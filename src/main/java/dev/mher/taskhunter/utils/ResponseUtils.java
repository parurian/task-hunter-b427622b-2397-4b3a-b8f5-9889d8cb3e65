package dev.mher.taskhunter.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 4:01 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.utils.
 */

@Getter
@Setter
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

    @Override
    public String toString() {
        return "ResponseUtils{" +
                "error=" + error +
                ", errorMessage='" + errorMessage + '\'' +
                ", result=" + result +
                '}';
    }
}
