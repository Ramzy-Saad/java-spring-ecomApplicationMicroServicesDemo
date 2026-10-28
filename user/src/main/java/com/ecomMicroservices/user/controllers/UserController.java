package com.ecomMicroservices.user.controllers;

import com.ecomMicroservices.user.dto.UserRequest;
import com.ecomMicroservices.user.dto.UserResponse;
import com.ecomMicroservices.user.services.UserService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
//    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    @RateLimiter(name = "ratebreaker" , fallbackMethod = "fallbackRetryMessage")
    public ResponseEntity<List<UserResponse>> getAllUSer(){
        return new ResponseEntity<>(userService.fetchAllUSer(), HttpStatus.OK);
    //        return ResponseEntity.ok(userService.fetchAllUSer());
    }

    @PostMapping
    public ResponseEntity<String> createUSer(@RequestBody UserRequest userRequest){
        userService.addUSer(userRequest);
        return ResponseEntity.ok("user added successfully");
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> fetchUser(@PathVariable Long id){
        log.debug("fetchUser called to user {}", id);

        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUSer(@PathVariable Long id, @RequestBody UserRequest updatedUserRequest){
        boolean updated =  userService.updateUser(id,updatedUserRequest);
        if (updated)
            return ResponseEntity.ok("user updated successfully");
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<UserResponse>> fallbackRetryMessage(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Collections.emptyList());
    }
}
