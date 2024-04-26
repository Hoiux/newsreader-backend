package org.hoiux.newsreader.controller;

import org.hoiux.newsreader.entity.User;
import org.hoiux.newsreader.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@RestController
@RequestMapping("/api/v1/newsreader")
public class UserController {

    @Autowired
    UserService userService;

    // Converts objects to json strings.
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    // --------------------------------------------------------------------------------------------
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@RequestBody User user) {

        User u = userService.createUser(user);
        if (u == null) {
            return ResponseEntity.badRequest().body("{ \"Error\": \"User already exists.\" }");
        }

        return ResponseEntity.ok(u);
    }

    // --------------------------------------------------------------------------------------------
    @DeleteMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccount(@PathVariable("username") String username) {

        String uname = userService.deleteUser(username);
        if (uname == null) {
            return ResponseEntity.badRequest().body("{ \"Error\": \"User does not exist.\" }");
        }

        return ResponseEntity.ok(null);
    }

    // --------------------------------------------------------------------------------------------
    @GetMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccount(@PathVariable("username") String username) {

        User user = userService.getUser(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("{ \"Error\": \"User does not exist.\" }");
        }

        return ResponseEntity.ok(user);
    }

}