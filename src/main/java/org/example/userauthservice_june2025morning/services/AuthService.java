package org.example.userauthservice_june2025morning.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_june2025morning.exceptions.PasswordMismatchException;
import org.example.userauthservice_june2025morning.exceptions.UserAlreadySignedUpException;
import org.example.userauthservice_june2025morning.exceptions.UserNotRegisteredException;
import org.example.userauthservice_june2025morning.models.User;
import org.example.userauthservice_june2025morning.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//ToDo : Define Exception Handlers and Controller Advisor for Exceptions
@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String name, String email, String password, String phoneNumber) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isPresent()) {
           throw new UserAlreadySignedUpException("Please login directly...");
        }

        User user = new User();
        user.setEmail(email);
        //user.setPassword(password);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        return userRepo.save(user);
     }

    @Override
    public Pair<User,String> login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()) {
           throw new UserNotRegisteredException("Please signup first...");
        }

        User user = userOptional.get();

        //if(!user.getPassword().equals(password)) {
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())) {
            throw new PasswordMismatchException("Please add correct password...");
        }

        //token generation

//        String message = "{\n" +
//                "   \"email\": \"anurag@gmail.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"ta\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2026\"\n" +
//                "}";
//
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);

        Map<String,Object> claims = new HashMap<String,Object>();
        claims.put("iss","scaler");
        claims.put("userId",user.getId());
        Long currentTimeInMillis = System.currentTimeMillis();
        claims.put("gen",currentTimeInMillis);
        claims.put("exp",currentTimeInMillis+100000);
        claims.put("access",user.getRoles());


        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
//        String token = Jwts.builder().content(content).compact();
        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        return new Pair<User,String>(user,token);
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        return null;
    }
}
