package com.alex.security.auth.service;

import com.alex.security.common.exceptions.BadRequestException;
import com.alex.security.common.exceptions.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service    // transform to a managed @Bean of Spring (Inject)
public class JwtService {

    @Value("${app.security.jwt.secret}")
    private String SECRET_KEY;  // solo private como modificador de acceso para q no de error

    @Value("${app.security.jwt.expiration}")
    private Long JWT_EXPIRATION_HOURS;


    public String extractUsername(String jwt) {
        // el Subject deberia ser el   (email,uuid,username)    q vamos a setear en la construccion del JWT
        return extractClaim(jwt, Claims::getSubject);
    }

    // genericos para extraer cualquier claim q nos interese
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);    // extra TODOS los claims del JWT q se le pase
    }


    // // // generate JWT  --  sobrecarga de methods - Polymorphism
    public String generateJwt(Map<String, Object> extraClaims, UserDetails userDetails) {
        // extracalims es lo extra q quiero pasarle al payload del jwt

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())  // payload q se va a codificar (email, uuid, usrname)
                .setIssuedAt(new Date(System.currentTimeMillis()))    // when this jwt was created - to calculate the expiration date
                .setExpiration(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * JWT_EXPIRATION_HOURS)))    // setear el tiempo de validez del jwt
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)   // hashcode redomendado
                .compact();
    }

    // Polymorphism: Without any extraClaims
    public String generateJwt(UserDetails userDetails) {
        return generateJwt(new HashMap<>(), userDetails);
    }


    // // Validate JWT: valida jwt y q el (email,uuid,username) del subject pertenezca a ese usuario <-- en este contexto username=email
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);

        return (
                username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(jwt)
        );
    }


    // // validate uri and bearer token
    public String validateJwtRequest(String authHeader, String uri) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        // // we need to handle like this 'cause of the filterChain behavior (says that this endpoint exists but need auth, another just 404 <- EntryPoint)
        // regex for any api version
        String regex = "/api/v\\d+/auth/renew-token";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uri);

        if (!StringUtils.hasText(authHeader) && matcher.find()) {
            throw new UnauthorizedException("Unauthorized");
        }

        return null;
    }


    private Claims extractAllClaims(String jwt) {
        Claims claims;

        try {
            claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey()) // JWT Secret
                    .build()    // xq es 1 builder
                    .parseClaimsJws(jwt)    // parse el JWT para extraer los claims
                    .getBody();     // cuando hace el parse puede obtener los claims y en este caso queremos el body
        } catch (SecurityException ex) {    // invalid signature
            throw new BadRequestException("Invalid Token Signature");
        } catch (MalformedJwtException | UnsupportedJwtException ex) {
            throw new BadRequestException("Invalid Token");
        } catch (ExpiredJwtException ex) {
            throw new BadRequestException("Expired Token");
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid Token Claims");
        }

        return claims;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);   // jwt lo requiere en b64
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date()); // before 'cause the expiration is the SUM of now and JWT_EXPIRATION_HOURS
    }

}
