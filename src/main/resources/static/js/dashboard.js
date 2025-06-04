class DashboardManager {
    constructor() {
        this.initNavigation();
        this.initCharts();
        this.loadUserData();
        this.loadTransactions();
    }

    initNavigation() {
        const menuItems = document.querySelectorAll('.menu-item');
        const sections = document.querySelectorAll('.dashboard-section');

        menuItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                
                // Remove active class from all items
                menuItems.forEach(mi => mi.classList.remove('active'));
                sections.forEach(section => section.classList.remove('active'));
                
                // Add active class to clicked item
                item.classList.add('active');
                
                // Show corresponding section
                const sectionId = item.getAttribute('data-section');
                const targetSection = document.getElementById(sectionId);
                if (targetSection) {
                    targetSection.classList.add('active');
                }
            });
        });
    }

    initCharts() {
        // Price Chart
        const priceCtx = document.getElementById('priceChart');
        if (priceCtx) {
            new Chart(priceCtx, {
                type: 'line',
                data: {
                    labels: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'],
                    datasets: [{
                        label: 'DKX Price',
                        data: [0.0120, 0.0123, 0.0125, 0.0128, 0.0125, 0.0127],
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
                        y: {
                            grid: {
                                color: 'rgba(255, 255, 255, 0.1)'
                            },
                            ticks: {
                                color: '#a1a1aa'
                            }
                        },
                        x: {
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

        // Portfolio Chart
        const portfolioCtx = document.getElementById('portfolioChart');
        if (portfolioCtx) {
            new Chart(portfolioCtx, {
                type: 'doughnut',
                data: {
                    labels: ['DKX', 'Cash'],
                    datasets: [{
                        data: [80, 20],
                        backgroundColor: ['#6366f1', '#e2e8f0'],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                color: '#a1a1aa',
                                usePointStyle: true,
                                padding: 20
                            }
                        }
                    }
                }
            });
        }
    }

    async loadUserData() {
        // In a real app, this would fetch from your API
        const userData = {
            name: 'John Doe',
            balance: '1,250.75 DKX',
            portfolioValue: '$15.63',
            change24h: '+24.67%'
        };

        // Update user name in navigation
        const userName = document.querySelector('.user-name');
        if (userName) {
            userName.textContent = userData.name;
        }
    }

    async loadTransactions() {
        try {
            const response = await fetch('/api/transactions');
            const transactions = await response.json();
            
            const tbody = document.getElementById('transactionsList');
            if (tbody) {
                tbody.innerHTML = transactions.map(tx => `
                    <tr>
                        <td>${new Date(tx.timestamp).toLocaleDateString()}</td>
                        <td>
                            <span class="transaction-type ${tx.type.toLowerCase()}">
                                ${tx.type}
                            </span>
                        </td>
                        <td>${tx.amount} DKX</td>
                        <td>$${tx.price}</td>
                        <td>
                            <span class="status ${tx.status.toLowerCase()}">
                                ${tx.status}
                            </span>
                        </td>
                    </tr>
                `).join('');
            }
        } catch (error) {
            console.error('Failed to load transactions:', error);
        }
    }
}

// Utility Functions
function logout() {
    localStorage.removeItem('dkxUser');
    window.location.href = '/login';
}

// Initialize Dashboard
document.addEventListener('DOMContentLoaded', () => {
    new DashboardManager();
});

// Add some CSS for transaction types and status
const style = document.createElement('style');
style.textContent = `
    .transaction-type {
        padding: 0.25rem 0.75rem;
        border-radius: 6px;
        font-size: 0.875rem;
        font-weight: 500;
    }
    
    .transaction-type.buy {
        background: rgba(16, 185, 129, 0.2);
        color: #10b981;
    }
    
    .transaction-type.sell {
        background: rgba(239, 68, 68, 0.2);
        color: #ef4444;
    }
    
    .transaction-type.reward {
        background: rgba(245, 158, 11, 0.2);
        color: #f59e0b;
    }
    
    .status {
        padding: 0.25rem 0.75rem;
        border-radius: 6px;
        font-size: 0.875rem;
        font-weight: 500;
    }
    
    .status.completed {
        background: rgba(16, 185, 129, 0.2);
        color: #10b981;
    }
    
    .status.pending {
        background: rgba(245, 158, 11, 0.2);
        color: #f59e0b;
    }
`;
document.head.appendChild(style); 