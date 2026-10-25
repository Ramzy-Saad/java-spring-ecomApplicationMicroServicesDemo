package com.ecomMicroservices.user.controllers;

import com.ecomMicroservices.user.dto.UserRequest;
import com.ecomMicroservices.user.dto.UserResponse;
import com.ecomMicroservices.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
//    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
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
}
