package com.financedkx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalSupply", "1,000,000,000");
        model.addAttribute("currentPrice", "$0.0125");
        model.addAttribute("marketCap", "$12,500,000");
        return "index";
    }

    @GetMapping("/platforms")
    public String platforms(Model model) {
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
        return "login";
    }

    private List<Map<String, Object>> getTopFinancialPlatforms() {
        List<Map<String, Object>> platforms = new ArrayList<>();

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
