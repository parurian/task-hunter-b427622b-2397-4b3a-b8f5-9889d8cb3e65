package dev.mher.taskhunter.models.misc;

import lombok.Getter;
import lombok.Setter;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 5:48 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.models.responses.
 */
@Getter
@Setter
public class Response {
    private boolean error;
    private String message;
}
