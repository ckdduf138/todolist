package com.example.todolist.controller;

import com.example.todolist.model.User;
import com.example.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
	    @PostMapping("/register")
	    public ResponseEntity<User> registerUser(@RequestBody User user) {
	        user.setIndate(LocalDateTime.now());
	
	        System.out.println("회원가입 시작");
	        
	        System.out.println("회원가입 아이디: " + user.getUserId());
	        System.out.println("회원가입 아이디: " + user.getPassword());
	        
	        User savedUser = userService.saveOrUpdate(user);
	
	        System.out.println("회원가입 성공");
	        return ResponseEntity.ok(savedUser);
	    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String userId, @RequestParam String password) {

    	System.out.println("로그인 시작");
        User user = userService.findByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        
        if (user == null || !user.getPassword().equals(password)) {
            response.put("message", "Invalid username or password");
            response.put("userId", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("message", "로그인 성공 후 index 페이지로 이동하도록 처리해야 합니다.");
        response.put("userId", userId);
        
        System.out.println("아이디: " + userId);
        System.out.println("패스워드: " + password);
        
        System.out.println("로그인 성공");
        
        return ResponseEntity.ok(response);
    }

}
