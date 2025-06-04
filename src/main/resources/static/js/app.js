// Real-time data updates
class DKXApp {
    constructor() {
        this.initCharts();
        this.startDataUpdates();
        this.initScrollEffects();
    }

    // Initialize price charts
    initCharts() {
        // Price Chart
        const priceCtx = document.getElementById('priceChart');
        if (priceCtx) {
            this.priceChart = new Chart(priceCtx, {
                type: 'line',
                data: {
                    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
                    datasets: [{
                        label: 'DKX Price',
                        data: [0.008, 0.009, 0.011, 0.010, 0.012, 0.0125],
                        borderColor: '#6366f1',
                        backgroundColor: 'rgba(99, 102, 241, 0.1)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        x: {
                            grid: {
                                color: 'rgba(255, 255, 255, 0.1)'
                            },
                            ticks: {
                                color: '#a1a1aa'
                            }
                        },
                        y: {
                            grid: {
                                color: 'rgba(255, 255, 255, 0.1)'
                            },
                            ticks: {
                                color: '#a1a1aa'
                            }
                        }
                    }
                }
            });
        }

        // Tokenomics Chart
        const tokenomicsCtx = document.getElementById('tokenomicsChart');
        if (tokenomicsCtx) {
            this.tokenomicsChart = new Chart(tokenomicsCtx, {
                type: 'doughnut',
                data: {
                    labels: ['Public Sale', 'Development', 'Marketing', 'Team', 'Reserve'],
                    datasets: [{
                        data: [40, 25, 15, 10, 10],
                        backgroundColor: [
                            '#6366f1',
                            '#8b5cf6',
                            '#06b6d4',
                            '#10b981',
                            '#f59e0b'
                        ],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }
            });
        }
    }

    // Start real-time data updates
    startDataUpdates() {
        this.updateTokenStats();
        this.updateTransactions();
        
        // Update every 30 seconds
        setInterval(() => {
            this.updateTokenStats();
            this.updateTransactions();
        }, 30000);
    }

    // Update token statistics
    async updateTokenStats() {
        try {
            const response = await fetch('/api/token-stats');
            const data = await response.json();
            
            const currentPrice = document.getElementById('current-price');
            const priceChange = document.getElementById('price-change');
            const volume = document.getElementById('volume');
            
            if (currentPrice) {
                currentPrice.textContent = `$${data.price}`;
            }
            
            if (priceChange) {
                priceChange.textContent = `${data.change24h}%`;
                priceChange.className = data.change24h.startsWith('+') ? 'positive' : 'negative';
            }
            
            if (volume) {
                volume.textContent = `$${this.formatNumber(data.volume24h)}`;
            }
            
        } catch (error) {
            console.error('Error updating token stats:', error);
        }
    }

    // Update recent transactions
    async updateTransactions() {
        try {
            const response = await fetch('/api/transactions');
            const transactions = await response.json();
            
            const container = document.getElementById('recent-transactions');
            if (container) {
                container.innerHTML = transactions.map(tx => `
                    <div class="transaction-item">
                        <div>
                            <div style="font-weight: 500;">${tx.amount} DKX</div>
                            <div style="font-size: 0.8rem; color: #a1a1aa;">${this.formatAddress(tx.hash)}</div>
                        </div>
                        <div style="text-align: right;">
                            <div style="font-size: 0.8rem; color: #a1a1aa;">${this.formatTime(tx.timestamp)}</div>
                        </div>
                    </div>
                `).join('');
            }
            
        } catch (error) {
            console.error('Error updating transactions:', error);
        }
    }

    // Utility functions
    formatNumber(num) {
        if (num >= 1000000) {
            return (num / 1000000).toFixed(1) + 'M';
        } else if (num >= 1000) {
            return (num / 1000).toFixed(1) + 'K';
        }
        return num.toString();
    }

    formatAddress(address) {
        return `${address.substring(0, 6)}...${address.substring(address.length - 4)}`;
    }

    formatTime(timestamp) {
        const now = Date.now();
        const diff = Math.floor((now - timestamp) / 1000);
        
        if (diff < 60) return `${diff}s ago`;
        if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
        if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
        return `${Math.floor(diff / 86400)}d ago`;
    }

    // Initialize scroll effects
    initScrollEffects() {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                }
            });
        });

        // Observe elements for scroll animations
        document.querySelectorAll('.data-card, .about-text, .token-detail').forEach(el => {
            el.style.opacity = '0';
            el.style.transform = 'translateY(20px)';
            el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
            observer.observe(el);
        });
    }
}

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new DKXApp();
});

// Mobile navigation toggle
document.addEventListener('DOMContentLoaded', () => {
    const navToggle = document.querySelector('.nav-toggle');
    const navMenu = document.querySelector('.nav-menu');
    
    if (navToggle && navMenu) {
        navToggle.addEventListener('click', () => {
            navMenu.classList.toggle('active');
        });
    }
});

// Smooth scrolling for navigation links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
}); 