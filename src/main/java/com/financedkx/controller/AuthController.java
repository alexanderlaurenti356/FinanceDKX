package com.financedkx.controller;

import com.financedkx.User;
import com.financedkx.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String firstName = request.get("firstName");
            String lastName = request.get("lastName");

            User user = userService.registerUser(email, password, firstName, lastName);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registration successful!");
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isPresent() && userService.validatePassword(userOpt.get(), password)) {
                User user = userOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful!");
                response.put("user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "dkxBalance", user.getDkxBalance()
                ));
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Long userId) {
        try {
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> profile = new HashMap<>();
                profile.put("id", user.getId());
                profile.put("email", user.getEmail());
                profile.put("firstName", user.getFirstName());
                profile.put("lastName", user.getLastName());
                profile.put("dkxBalance", user.getDkxBalance());
                profile.put("walletAddress", user.getWalletAddress());
                profile.put("verified", user.isVerified());
                profile.put("memberSince", user.getCreatedAt());
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<Map<String, Object>> googleLogin(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");

            Map<String, Object> googleUser = verifyGoogleToken(token);

            if (googleUser != null) {
                String email = (String) googleUser.get("email");
                String firstName = (String) googleUser.get("given_name");
                String lastName = (String) googleUser.get("family_name");

                Optional<User> existingUser = userService.findByEmail(email);
                User user;

                if (existingUser.isPresent()) {
                    user = existingUser.get();
                } else {
                    user = userService.registerUser(email, "google-oauth", firstName, lastName);
                }

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Google login successful!");
                response.put("user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "firstName", user.getFirstName(),
                        "lastName", user.getLastName(),
                        "dkxBalance", user.getDkxBalance()
                ));
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid Google token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Google login failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> verifyGoogleToken(String token) {
        Map<String, Object> mockUser = new HashMap<>();
        mockUser.put("email", "user@gmail.com");
        mockUser.put("given_name", "John");
        mockUser.put("family_name", "Doe");
        return mockUser;
    }
}
