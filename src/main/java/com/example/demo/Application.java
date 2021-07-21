package com.example.demo;

import model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@SpringBootApplication
public class Application {

    static final String users = "http://91.241.64.178:7081/api/users";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        User newUser = new User(3L, "James", "Brown", (byte) 35);
        User newUser1 = new User(3L, "Thomas", "Shelby", (byte) 31);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(users, HttpMethod.GET, entity, String.class);

        ResponseEntity<String> forEntity = restTemplate.getForEntity(users, String.class);
        Objects.requireNonNull(forEntity.getHeaders().get("Set-Cookie")).forEach(System.out::println);
        List<String> cookies = response.getHeaders().get("Set-Cookie");

        assert cookies != null;
        headers.set("Cookie", String.join(";", cookies));
        HttpEntity<User> entity1 = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> response1 = restTemplate.exchange(users, HttpMethod.POST, entity1, String.class);
        System.out.println(response1.getBody());

        headers.set("Cookie", String.join(";", cookies));
        HttpEntity<User> entity2 = new HttpEntity<>(newUser1, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(users, HttpMethod.PUT, entity2, String.class);
        System.out.println(response2.getBody());

        headers.set("Cookie", String.join(";", cookies));
        HttpEntity<User> entity3 = new HttpEntity<>(headers);
        ResponseEntity<String> response3 = restTemplate.exchange(users + "/" + "3", HttpMethod.DELETE, entity3, String.class);
        System.out.println(response3.getBody());
    }
}