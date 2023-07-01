package com.technokratos.studysoapstand.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;


    public StatusJwt validateToken(String token) {

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return StatusJwt.OK;
        } catch (MalformedJwtException e) {
            log.error(e.toString());
            return StatusJwt.MALFORMEDJWT;
        } catch (SignatureException e) {
            log.error(e.toString());
            return StatusJwt.SIGNATURE;
        }
    }

    public String getDataFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public enum StatusJwt{
        OK,
        MALFORMEDJWT,
        SIGNATURE
    }
}