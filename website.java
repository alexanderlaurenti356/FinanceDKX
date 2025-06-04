package com.financedkx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@SpringBootApplication
public class FinanceDkxApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceDkxApplication.class, args);
    }
}

@Controller
public class WebController {
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalSupply", "1,000,000,000");
        model.addAttribute("currentPrice", "$0.0125");
        model.addAttribute("marketCap", "$12,500,000");
        return "index";
    }
    
    @GetMapping("/whitepaper")
    public String whitepaper() {
        return "whitepaper";
    }
    
    @GetMapping("/roadmap")
    public String roadmap() {
        return "roadmap";
    }
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {
    
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
