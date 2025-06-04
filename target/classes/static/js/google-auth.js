// Google OAuth Configuration
const GOOGLE_CLIENT_ID = 'your-google-client-id.googleusercontent.com';

class GoogleAuth {
    constructor() {
        this.initGoogleSignIn();
    }

    initGoogleSignIn() {
        // Load Google Sign-In API
        const script = document.createElement('script');
        script.src = 'https://accounts.google.com/gsi/client';
        script.onload = () => {
            google.accounts.id.initialize({
                client_id: GOOGLE_CLIENT_ID,
                callback: this.handleGoogleSignIn.bind(this)
            });
        };
        document.head.appendChild(script);
    }

    async handleGoogleSignIn(response) {
        try {
            // Send Google token to your backend
            const result = await fetch('/api/auth/google-login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    token: response.credential
                })
            });

            const data = await result.json();

            if (data.success) {
                localStorage.setItem('dkxUser', JSON.stringify(data.user));
                this.showSuccess('Google sign-in successful!');
                setTimeout(() => {
                    window.location.href = '/dashboard';
                }, 1500);
            } else {
                this.showError(data.message);
            }
        } catch (error) {
            this.showError('Google sign-in failed. Please try again.');
        }
    }

    showError(message) {
        // Use the same notification system from auth.js
        if (window.authManager) {
            window.authManager.showError(message);
        } else {
            alert(message);
        }
    }

    showSuccess(message) {
        if (window.authManager) {
            window.authManager.showSuccess(message);
        } else {
            alert(message);
        }
    }
}

// Global functions for buttons
function signInWithGoogle() {
    google.accounts.id.prompt();
}

function signInWithGitHub() {
    // Redirect to GitHub OAuth
    window.location.href = '/api/auth/github';
}

function togglePassword() {
    const passwordInput = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.classList.remove('fa-eye');
        toggleIcon.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        toggleIcon.classList.remove('fa-eye-slash');
        toggleIcon.classList.add('fa-eye');
    }
}

// Initialize Google Auth
document.addEventListener('DOMContentLoaded', () => {
    new GoogleAuth();
}); 