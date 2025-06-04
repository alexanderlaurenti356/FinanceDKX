import openai
import tweepy
import facebook
import requests
import json
import time
import random
from datetime import datetime, timedelta
from typing import List, Dict, Optional
import logging
from dataclasses import dataclass
from configparser import ConfigParser
import os

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('affiliate_content.log'),
        logging.StreamHandler()
    ]
)

@dataclass
class AffiliateProduct:
    """Data class for affiliate products"""
    name: str
    description: str
    affiliate_link: str
    category: str
    price: Optional[str] = None
    hashtags: List[str] = None

@dataclass
class SocialMediaPost:
    """Data class for social media posts"""
    content: str
    platform: str
    hashtags: List[str]
    affiliate_links: List[str]
    scheduled_time: Optional[datetime] = None

class ContentGenerator:
    """AI-powered content generator for affiliate marketing"""
    
    def __init__(self, openai_api_key: str):
        openai.api_key = openai_api_key
        self.logger = logging.getLogger(__name__)
    
    def generate_product_content(self, product: AffiliateProduct, content_type: str = "social_post") -> str:
        """Generate AI content for a specific product"""
        
        prompts = {
            "social_post": f"""
            Create an engaging social media post for this product:
            Product: {product.name}
            Description: {product.description}
            Category: {product.category}
            
            Requirements:
            - Make it compelling and authentic
            - Include benefits and features
            - Use engaging language
            - Keep it under 280 characters for Twitter compatibility
            - Don't include hashtags or links (they'll be added separately)
            """,
            
            "instagram_story": f"""
            Create a short, catchy Instagram story text for:
            Product: {product.name}
            Description: {product.description}
            
            Make it trendy, use emojis, and create FOMO (fear of missing out).
            """,
            
            "blog_snippet": f"""
            Write a compelling 2-3 sentence product recommendation for:
            Product: {product.name}
            Description: {product.description}
            
            Write as if you're personally recommending it to a friend.
            """
        }
        
        try:
            response = openai.ChatCompletion.create(
                model="gpt-3.5-turbo",
                messages=[
                    {"role": "system", "content": "You are a skilled affiliate marketer who creates authentic, engaging content that converts without being pushy."},
                    {"role": "user", "content": prompts.get(content_type, prompts["social_post"])}
                ],
                max_tokens=200,
                temperature=0.7
            )
            
            content = response.choices[0].message.content.strip()
            self.logger.info(f"Generated {content_type} content for {product.name}")
            return content
            
        except Exception as e:
            self.logger.error(f"Error generating content: {str(e)}")
            return f"Check out this amazing {product.name}! {product.description}"

    def generate_multiple_variations(self, product: AffiliateProduct, count: int = 3) -> List[str]:
        """Generate multiple content variations for A/B testing"""
        variations = []
        
        for i in range(count):
            content = self.generate_product_content(product)
            variations.append(content)
            time.sleep(1)  # Rate limiting
        
        return variations

class SocialMediaManager:
    """Manages posting to multiple social media platforms"""
    
    def __init__(self, config_file: str = "social_config.ini"):
        self.config = ConfigParser()
        self.config.read(config_file)
        self.logger = logging.getLogger(__name__)
        self.platforms = {}
        self._initialize_platforms()
    
    def _initialize_platforms(self):
        """Initialize social media platform APIs"""
        try:
            # Twitter API v2
            if self.config.has_section('twitter'):
                self.platforms['twitter'] = tweepy.Client(
                    consumer_key=self.config.get('twitter', 'consumer_key'),
                    consumer_secret=self.config.get('twitter', 'consumer_secret'),
                    access_token=self.config.get('twitter', 'access_token'),
                    access_token_secret=self.config.get('twitter', 'access_token_secret'),
                    wait_on_rate_limit=True
                )
            
            # Facebook/Instagram (requires Facebook Graph API)
            if self.config.has_section('facebook'):
                self.platforms['facebook'] = {
                    'access_token': self.config.get('facebook', 'access_token'),
                    'page_id': self.config.get('facebook', 'page_id')
                }
            
            # LinkedIn API
            if self.config.has_section('linkedin'):
                self.platforms['linkedin'] = {
                    'access_token': self.config.get('linkedin', 'access_token'),
                    'person_id': self.config.get('linkedin', 'person_id')
                }
                
        except Exception as e:
            self.logger.error(f"Error initializing platforms: {str(e)}")
    
    def post_to_twitter(self, content: str, hashtags: List[str], links: List[str]) -> bool:
        """Post content to Twitter"""
        try:
            if 'twitter' not in self.platforms:
                self.logger.error("Twitter not configured")
                return False
            
            # Combine content with hashtags and links
            full_content = f"{content}\n\n"
            full_content += " ".join([f"#{tag}" for tag in hashtags[:5]])  # Twitter hashtag limit
            
            if links:
                full_content += f"\n{links[0]}"  # Add first affiliate link
            
            # Ensure under 280 characters
            if len(full_content) > 280:
                # Truncate content and add ellipsis
                available_chars = 280 - len(" ".join([f"#{tag}" for tag in hashtags[:5]])) - len(f"\n{links[0]}") - 4
                content = content[:available_chars] + "..."
                full_content = f"{content}\n\n"
                full_content += " ".join([f"#{tag}" for tag in hashtags[:5]])
                if links:
                    full_content += f"\n{links[0]}"
            
            response = self.platforms['twitter'].create_tweet(text=full_content)
            self.logger.info(f"Posted to Twitter: {response.data['id']}")
            return True
            
        except Exception as e:
            self.logger.error(f"Error posting to Twitter: {str(e)}")
            return False
    
    def post_to_facebook(self, content: str, hashtags: List[str], links: List[str]) -> bool:
        """Post content to Facebook page"""
        try:
            if 'facebook' not in self.platforms:
                self.logger.error("Facebook not configured")
                return False
            
            fb_config = self.platforms['facebook']
            
            full_content = f"{content}\n\n"
            full_content += " ".join([f"#{tag}" for tag in hashtags])
            
            if links:
                full_content += f"\n\nLink: {links[0]}"
            
            url = f"https://graph.facebook.com/v18.0/{fb_config['page_id']}/feed"
            payload = {
                'message': full_content,
                'access_token': fb_config['access_token']
            }
            
            response = requests.post(url, data=payload)
            
            if response.status_code == 200:
                self.logger.info(f"Posted to Facebook: {response.json()}")
                return True
            else:
                self.logger.error(f"Facebook post failed: {response.text}")
                return False
                
        except Exception as e:
            self.logger.error(f"Error posting to Facebook: {str(e)}")
            return False
    
    def post_to_linkedin(self, content: str, hashtags: List[str], links: List[str]) -> bool:
        """Post content to LinkedIn"""
        try:
            if 'linkedin' not in self.platforms:
                self.logger.error("LinkedIn not configured")
                return False
            
            linkedin_config = self.platforms['linkedin']
            
            full_content = f"{content}\n\n"
            full_content += " ".join([f"#{tag}" for tag in hashtags])
            
            if links:
                full_content += f"\n\n{links[0]}"
            
            headers = {
                'Authorization': f"Bearer {linkedin_config['access_token']}",
                'Content-Type': 'application/json',
                'X-Restli-Protocol-Version': '2.0.0'
            }
            
            payload = {
                "author": f"urn:li:person:{linkedin_config['person_id']}",
                "lifecycleState": "PUBLISHED",
                "specificContent": {
                    "com.linkedin.ugc.ShareContent": {
                        "shareCommentary": {
                            "text": full_content
                        },
                        "shareMediaCategory": "NONE"
                    }
                },
                "visibility": {
                    "com.linkedin.ugc.MemberNetworkVisibility": "PUBLIC"
                }
            }
            
            response = requests.post(
                'https://api.linkedin.com/v2/ugcPosts',
                headers=headers,
                data=json.dumps(payload)
            )
            
            if response.status_code == 201:
                self.logger.info(f"Posted to LinkedIn: {response.json()}")
                return True
            else:
                self.logger.error(f"LinkedIn post failed: {response.text}")
                return False
                
        except Exception as e:
            self.logger.error(f"Error posting to LinkedIn: {str(e)}")
            return False

class AffiliateContentBot:
    """Main bot class that orchestrates content generation and posting"""
    
    def __init__(self, openai_api_key: str, config_file: str = "social_config.ini"):
        self.content_generator = ContentGenerator(openai_api_key)
        self.social_manager = SocialMediaManager(config_file)
        self.logger = logging.getLogger(__name__)
        self.products = []
        
    def add_product(self, product: AffiliateProduct):
        """Add a product to the affiliate catalog"""
        self.products.append(product)
        self.logger.info(f"Added product: {product.name}")
    
    def create_and_post_content(self, product: AffiliateProduct, platforms: List[str] = None) -> Dict[str, bool]:
        """Generate content and post to specified platforms"""
        if platforms is None:
            platforms = ['twitter', 'facebook', 'linkedin']
        
        # Generate content
        content = self.content_generator.generate_product_content(product)
        
        # Prepare hashtags
        hashtags = product.hashtags or [product.category.lower(), 'affiliate', 'recommendation']
        
        # Post to each platform
        results = {}
        for platform in platforms:
            if platform == 'twitter':
                results[platform] = self.social_manager.post_to_twitter(
                    content, hashtags, [product.affiliate_link]
                )
            elif platform == 'facebook':
                results[platform] = self.social_manager.post_to_facebook(
                    content, hashtags, [product.affiliate_link]
                )
            elif platform == 'linkedin':
                results[platform] = self.social_manager.post_to_linkedin(
                    content, hashtags, [product.affiliate_link]
                )
            
            # Add delay between posts to avoid rate limiting
            time.sleep(random.uniform(5, 15))
        
        return results
    
    def schedule_content_campaign(self, duration_days: int = 7, posts_per_day: int = 3):
        """Schedule a content campaign over specified duration"""
        if not self.products:
            self.logger.error("No products available for campaign")
            return
        
        total_posts = duration_days * posts_per_day
        campaign_schedule = []
        
        for day in range(duration_days):
            for post_num in range(posts_per_day):
                # Select random product
                product = random.choice(self.products)
                
                # Calculate posting time
                base_time = datetime.now() + timedelta(days=day)
                post_time = base_time.replace(
                    hour=random.randint(9, 21),  # Post between 9 AM and 9 PM
                    minute=random.randint(0, 59),
                    second=0,
                    microsecond=0
                )
                
                campaign_schedule.append({
                    'product': product,
                    'scheduled_time': post_time,
                    'platforms': ['twitter', 'facebook', 'linkedin']
                })
        
        self.logger.info(f"Created campaign schedule with {len(campaign_schedule)} posts")
        return campaign_schedule
    
    def run_automated_posting(self, schedule: List[Dict]):
        """Run automated posting based on schedule"""
        for post_config in sorted(schedule, key=lambda x: x['scheduled_time']):
            # Wait until scheduled time
            wait_time = (post_config['scheduled_time'] - datetime.now()).total_seconds()
            
            if wait_time > 0:
                self.logger.info(f"Waiting {wait_time/60:.1f} minutes until next post...")
                time.sleep(wait_time)
            
            # Create and post content
            results = self.create_and_post_content(
                post_config['product'],
                post_config['platforms']
            )
            
            self.logger.info(f"Posted content for {post_config['product'].name}: {results}")

def create_sample_config():
    """Create a sample configuration file"""
    config = ConfigParser()
    
    config.add_section('twitter')
    config.set('twitter', 'consumer_key', 'your_twitter_consumer_key')
    config.set('twitter', 'consumer_secret', 'your_twitter_consumer_secret')
    config.set('twitter', 'access_token', 'your_twitter_access_token')
    config.set('twitter', 'access_token_secret', 'your_twitter_access_token_secret')
    
    config.add_section('facebook')
    config.set('facebook', 'access_token', 'your_facebook_access_token')
    config.set('facebook', 'page_id', 'your_facebook_page_id')
    
    config.add_section('linkedin')
    config.set('linkedin', 'access_token', 'your_linkedin_access_token')
    config.set('linkedin', 'person_id', 'your_linkedin_person_id')
    
    with open('social_config.ini', 'w') as configfile:
        config.write(configfile)
    
    print("Sample configuration file 'social_config.ini' created!")
    print("Please fill in your actual API keys and tokens.")

if __name__ == "__main__":
    # Example usage
    
    # Create configuration file if it doesn't exist
    if not os.path.exists('social_config.ini'):
        create_sample_config()
    
    # Set up your OpenAI API key
    OPENAI_API_KEY = "your_openai_api_key_here"  # Replace with your actual key
    
    # Initialize the bot
    bot = AffiliateContentBot(OPENAI_API_KEY)
    
    # Add sample products
    sample_products = [
        AffiliateProduct(
            name="Premium Wireless Headphones",
            description="High-quality noise-canceling headphones with 30-hour battery life",
            affiliate_link="https://example.com/affiliate-link-1",
            category="Electronics",
            price="$199",
            hashtags=["headphones", "audio", "tech", "music", "wireless"]
        ),
        AffiliateProduct(
            name="Fitness Tracker Pro",
            description="Advanced fitness tracker with heart rate monitoring and GPS",
            affiliate_link="https://example.com/affiliate-link-2",
            category="Fitness",
            price="$149",
            hashtags=["fitness", "health", "tracker", "workout", "smartwatch"]
        )
    ]
    
    for product in sample_products:
        bot.add_product(product)
    
    # Generate and post content for one product
    if bot.products:
        print("Generating content for sample product...")
        results = bot.create_and_post_content(bot.products[0], platforms=['twitter'])
        print(f"Posting results: {results}")
    
    # Create a campaign schedule
    print("\nCreating campaign schedule...")
    schedule = bot.schedule_content_campaign(duration_days=3, posts_per_day=2)
    
    print(f"Campaign created with {len(schedule)} posts")
    for post in schedule[:3]:  # Show first 3 posts
        print(f"- {post['product'].name} at {post['scheduled_time']}")
