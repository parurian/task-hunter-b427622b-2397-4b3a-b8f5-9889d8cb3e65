package dev.mher.taskhunter.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User: MheR
 * Date: 12/4/19.
 * Time: 2:54 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.services.
 */

@Service
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jsonWebToken.secret}")
    private String secret;

    @Value("${jsonWebToken.lifetime}")
    private int lifetime;

    public CharSequence encode(String s, long lifetime) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withExpiresAt(getLifetime(lifetime))
                    .withSubject(s)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            logger.error(e.getMessage());
            logger.info(e.getMessage(), e);
        }
        return null;
    }

    public String decode(CharSequence token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token.toString());
            return jwt.getSubject();
        } catch (JWTVerificationException e){
            logger.error(e.getMessage());
            logger.info(e.getMessage(), e);
        }
        return null;
    }

    private Date getLifetime(long lifetime){
        return new Date(System.currentTimeMillis() + lifetime * 1000);
    }

}
