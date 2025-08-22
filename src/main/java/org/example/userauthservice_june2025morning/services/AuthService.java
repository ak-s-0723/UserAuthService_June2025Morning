package org.example.userauthservice_june2025morning.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_june2025morning.clients.KafkaClient;
import org.example.userauthservice_june2025morning.dtos.EmailDto;
import org.example.userauthservice_june2025morning.exceptions.PasswordMismatchException;
import org.example.userauthservice_june2025morning.exceptions.UserAlreadySignedUpException;
import org.example.userauthservice_june2025morning.exceptions.UserNotRegisteredException;
import org.example.userauthservice_june2025morning.models.Status;
import org.example.userauthservice_june2025morning.models.User;
import org.example.userauthservice_june2025morning.models.UserSession;
import org.example.userauthservice_june2025morning.repos.SessionRepo;
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


    @Autowired
    private SessionRepo sessionRepo;


    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaClient kafkaClient;


    @Autowired
    private ObjectMapper objectMapper;

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


        //sent message via Kafka
        try {
            EmailDto emailDto = new EmailDto();
            emailDto.setTo(email);
            emailDto.setFrom("anuragonhiring@gmail.com");
            emailDto.setSubject("Welcome to Scaler");
            emailDto.setBody("Have a good learning experience");
            kafkaClient.sendMessage("signup", objectMapper.writeValueAsString(emailDto));
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception.getMessage());
        }

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

//        Declared as a bean in class 4 of Auth
//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();

//        String token = Jwts.builder().content(content).compact();
        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        UserSession userSession = new UserSession();
        userSession.setToken(token);
        userSession.setUser(user);
        userSession.setStatus(Status.ACTIVE);
        sessionRepo.save(userSession);

        return new Pair<User,String>(user,token);
    }

    //validate if token exists in Db
    //check for expiry
    @Override
    public Boolean validateToken(String token, Long userId) {
        Optional<UserSession> optionalUserSession = sessionRepo.findByTokenAndUser_Id(token,userId);

        if(optionalUserSession.isEmpty()) {
            return false;
        }


        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long tokenExpiry = (Long)claims.get("exp");
        Long currentTime = System.currentTimeMillis();

        System.out.println("tokenExpiry = "+tokenExpiry);
        System.out.println("currentTime = "+currentTime);

        if(currentTime > tokenExpiry) {
            System.out.println("TOKEN HAS EXPIRED");
            UserSession userSession = optionalUserSession.get();
            userSession.setStatus(Status.INACTIVE);
            sessionRepo.save(userSession);
            return false;
        }

        return true;
    }
}
