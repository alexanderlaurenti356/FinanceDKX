package com.financedkx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class FinanceDkxApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceDkxApplication.class, args);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

@Controller
class WebController {
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalSupply", "1,000,000,000");
        model.addAttribute("currentPrice", "$0.0125");
        model.addAttribute("marketCap", "$12,500,000");
        return "index";
    }
    
    @GetMapping("/platforms")
    public String platforms(Model model) {
        // Add traditional financial platforms data
        model.addAttribute("topPlatforms", getTopFinancialPlatforms());
        model.addAttribute("categories", getFinancialCategories());
        return "platforms";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add user dashboard data
        model.addAttribute("userBalance", "1,250.75");
        model.addAttribute("portfolioValue", "$15.63");
        return "dashboard";
    }
    
    @GetMapping("/whitepaper")
    public String whitepaper() {
        return "whitepaper";
    }
    
    @GetMapping("/roadmap")
    public String roadmap() {
        return "roadmap";
    }
    
    @GetMapping("/test-login")
    public String testLogin() {
        return "login";  // This should definitely use our template
    }
    
    private List<Map<String, Object>> getTopFinancialPlatforms() {
        List<Map<String, Object>> platforms = new ArrayList<>();
        
        // Investment Platforms
        platforms.add(Map.of(
            "rank", 1,
            "name", "Fidelity Investments",
            "category", "Investment",
            "aum", "$11.8T",
            "fees", "0.00%",
            "rating", 4.8,
            "minInvestment", "$0",
            "affiliateLink", "https://www.fidelity.com/?refcode=financedkx",
            "logo", "/images/platforms/fidelity.png",
            "description", "Leading investment platform with zero-fee mutual funds and excellent research tools"
        ));
        
        platforms.add(Map.of(
            "rank", 2,
            "name", "Charles Schwab",
            "category", "Investment",
            "aum", "$8.1T",
            "fees", "0.00%",
            "rating", 4.7,
            "minInvestment", "$0",
            "affiliateLink", "https://www.schwab.com/?refid=financedkx",
            "logo", "/images/platforms/schwab.png",
            "description", "Full-service brokerage with excellent customer service and research"
        ));
        
        // Banks
        platforms.add(Map.of(
            "rank", 3,
            "name", "Chase Bank",
            "category", "Banking",
            "aum", "$3.7T",
            "fees", "APY: 0.50%",
            "rating", 4.3,
            "minInvestment", "$0",
            "affiliateLink", "https://www.chase.com/?jp_cmp=rb/financedkx",
            "logo", "/images/platforms/chase.png",
            "description", "Largest bank in the US with extensive ATM network and digital banking"
        ));
        
        // Robo-Advisors
        platforms.add(Map.of(
            "rank", 4,
            "name", "Betterment",
            "category", "Robo-Advisor",
            "aum", "$33B",
            "fees", "0.25%",
            "rating", 4.6,
            "minInvestment", "$0",
            "affiliateLink", "https://www.betterment.com/?referrer=financedkx",
            "logo", "/images/platforms/betterment.png",
            "description", "Leading robo-advisor with automated investing and tax-loss harvesting"
        ));
        
        // Trading Platforms
        platforms.add(Map.of(
            "rank", 5,
            "name", "E*TRADE",
            "category", "Trading",
            "aum", "$691B",
            "fees", "$0 trades",
            "rating", 4.5,
            "minInvestment", "$0",
            "affiliateLink", "https://us.etrade.com/home?referrer=financedkx",
            "logo", "/images/platforms/etrade.png",
            "description", "Advanced trading platform with powerful tools for active traders"
        ));
        
        // Credit Cards
        platforms.add(Map.of(
            "rank", 6,
            "name", "Chase Sapphire Preferred",
            "category", "Credit Card",
            "aum", "N/A",
            "fees", "$95 annual",
            "rating", 4.9,
            "minInvestment", "Good Credit",
            "affiliateLink", "https://creditcards.chase.com/rewards-credit-cards/sapphire/preferred?CELL=6RRW&jp_cmp=cc/financedkx",
            "logo", "/images/platforms/sapphire.png",
            "description", "Premium travel rewards card with 2x points on travel and dining"
        ));
        
        // High-Yield Savings
        platforms.add(Map.of(
            "rank", 7,
            "name", "Marcus by Goldman Sachs",
            "category", "Savings",
            "aum", "$110B",
            "fees", "4.50% APY",
            "rating", 4.4,
            "minInvestment", "$0",
            "affiliateLink", "https://www.marcus.com/us/en/savings?referrer=financedkx",
            "logo", "/images/platforms/marcus.png",
            "description", "High-yield online savings account with competitive rates and no fees"
        ));
        
        // Insurance
        platforms.add(Map.of(
            "rank", 8,
            "name", "GEICO",
            "category", "Insurance",
            "aum", "N/A",
            "fees", "Save 15%+",
            "rating", 4.2,
            "minInvestment", "$0",
            "affiliateLink", "https://www.geico.com/?referrer=financedkx",
            "logo", "/images/platforms/geico.png",
            "description", "Leading auto insurance with competitive rates and excellent digital experience"
        ));
        
        return platforms;
    }
    
    private List<Map<String, Object>> getFinancialCategories() {
        return List.of(
            Map.of("name", "Investment", "count", 25, "icon", "fas fa-chart-line"),
            Map.of("name", "Banking", "count", 18, "icon", "fas fa-university"),
            Map.of("name", "Credit Card", "count", 30, "icon", "fas fa-credit-card"),
            Map.of("name", "Trading", "count", 12, "icon", "fas fa-exchange-alt"),
            Map.of("name", "Robo-Advisor", "count", 8, "icon", "fas fa-robot"),
            Map.of("name", "Savings", "count", 15, "icon", "fas fa-piggy-bank"),
            Map.of("name", "Insurance", "count", 20, "icon", "fas fa-shield-alt"),
            Map.of("name", "Loans", "count", 22, "icon", "fas fa-hand-holding-usd")
        );
    }
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
class ApiController {
    
    @GetMapping("/token-stats")
    public ResponseEntity<Map<String, Object>> getTokenStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("price", "0.0125");
        stats.put("change24h", "+5.67");
        stats.put("volume24h", "1,234,567");
        stats.put("marketCap", "12500000");
        stats.put("totalSupply", "1000000000");
        stats.put("circulatingSupply", "750000000");
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<List<Map<String, Object>>> getTransactions() {
        List<Map<String, Object>> transactions = new ArrayList<>();
        
        Map<String, Object> tx1 = new HashMap<>();
        tx1.put("hash", "0x1a2b3c4d5e6f...");
        tx1.put("from", "0x742d35cc6434c532532a15c9c0c1b57d");
        tx1.put("to", "0x8ba1f109551bd432803012645hac136c");
        tx1.put("amount", "1000.50");
        tx1.put("timestamp", System.currentTimeMillis() - 300000);
        transactions.add(tx1);
        
        Map<String, Object> tx2 = new HashMap<>();
        tx2.put("hash", "0x2b3c4d5e6f7a...");
        tx2.put("from", "0x8ba1f109551bd432803012645hac136c");
        tx2.put("to", "0x999888777666555444333222111000aa");
        tx2.put("amount", "2500.75");
        tx2.put("timestamp", System.currentTimeMillis() - 600000);
        transactions.add(tx2);
        
        return ResponseEntity.ok(transactions);
    }
}

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
class AuthController {
    
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
            
            // Verify Google token (you'll need to implement this)
            Map<String, Object> googleUser = verifyGoogleToken(token);
            
            if (googleUser != null) {
                String email = (String) googleUser.get("email");
                String firstName = (String) googleUser.get("given_name");
                String lastName = (String) googleUser.get("family_name");
                
                // Check if user exists or create new one
                Optional<User> existingUser = userService.findByEmail(email);
                User user;
                
                if (existingUser.isPresent()) {
                    user = existingUser.get();
                } else {
                    // Create new user with Google data
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
        // Implement Google token verification
        // For now, return mock data for testing
        Map<String, Object> mockUser = new HashMap<>();
        mockUser.put("email", "user@gmail.com");
        mockUser.put("given_name", "John");
        mockUser.put("family_name", "Doe");
        return mockUser;
    }
}
