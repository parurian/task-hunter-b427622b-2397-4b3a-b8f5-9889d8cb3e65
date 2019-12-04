package dev.mher.taskhunter.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * User: MheR
 * Date: 12/3/19.
 * Time: 11:23 AM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.services.
 */

@Service
public class CryptoService {

    @Value("${security.password.saltStrength}")
    int saltStrength;


    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(saltStrength);
    }

    public String generateHash(CharSequence s) {
        final PasswordEncoder encoder = new BCryptPasswordEncoder(saltStrength);

        return encoder.encode(s);
    }

    public boolean compare(CharSequence s, CharSequence hash) {
        final PasswordEncoder encoder = new BCryptPasswordEncoder(saltStrength);
        return encoder.matches(s.toString(), hash.toString());
    }
}
