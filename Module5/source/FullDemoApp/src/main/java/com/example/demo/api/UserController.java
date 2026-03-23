package com.example.demo.api;

import com.example.demo.model.UserVO;
import com.example.demo.service.ServiceLayerException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
   
    @Autowired
    UserService userService;

    @GetMapping
    public List<UserVO> getAllUsers() {
        return userService.findAllUsers();

    }

    @PostMapping("/signup")
    public String createUser(@RequestBody UserVO userVO) throws ServiceLayerException  {
        // TODO: should handle this exception in a better way.
        userService.saveUser(userVO);
        return "User created successfully";
    }
}