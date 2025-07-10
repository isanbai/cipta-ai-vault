

```
# Follow up with an additional print command
name = "Otto Matic"
dog_age = 21/7
print_llm_response(f"""
If {name} were a dog, he would be {dog_age} years old.
Describe what life stage that would be for a dog.
""")

fav_num = 10
print(f"Your favorite number plus 10 is {fav_num+10}")
print("How is your day going?")
print(f"Isabel is {28/7:.0f} dog years old.")

```
```
#List and Dictionary
ice_cream_flavors = [
    "Vanilla",
    "Chocolate",
    "Strawberry",
    "Mint Chocolate Chip"
]
for flavor in ice_cream_flavors:
    prompt = f"""
    For the ice cream flavor listed below, 
    provide a captivating description that could be used for promotional purposes.
    Flavor: {flavor}
    """
    print_llm_response(prompt)
    
food_preferences_tommy = {
        "dietary_restrictions": "vegetarian",
        "favorite_ingredients": ["tofu", "olives"],
        "experience_level": "intermediate",
        "maximum_spice_level": 6
}
print(food_preferences_tommy.keys())
print(food_preferences_tommy.values())

```
```
#defind function
def fahrenheit_to_celsius(fahrenheit):
    celsius = (fahrenheit - 32) * 5 / 9
    print(f"{fahrenheit}°F is equivalent to {celsius:.2f}°C")

#import method    
from helper_functions import celsius_to_fahrenheit
from helper_functions import *
import helper_functions
helper_functions.print_llm_response("What is the capital of France?")
```
```
# Pandas and Matplotlib
# Dataset adapted from here https://www.kaggle.com/datasets/nehalbirla/vehicle-dataset-from-cardekho

import pandas as pd
data = pd.read_csv('car_data.csv')
print(data)
print(data[data["Price"]>=10000])
filtered_data = data[data["Year"]==2015]
print(filtered_data["Price"].median())

import matplotlib.pyplot as plt
plt.title('Car Price vs. Kilometers Driven', fontsize=16)
plt.xlabel('Kilometers Driven')
plt.ylabel('Price (in USD)')
# Add the grid
plt.grid(True)
# Display the plot
plt.show()
```
```
!pip install bs4
from bs4 import BeautifulSoup
import requests # let's you download webpages into python
from helper_functions import * 
from IPython.display import HTML, display

# The url from one of the Batch's newsletter
url = 'https://www.deeplearning.ai/the-batch/the-world-needs-more-intelligence/'

# Getting the content from the webpage's contents
response = requests.get(url)
print(response)
HTML(f'<iframe src={url} width="60%" height="400"></iframe>')

# Using beautifulsoup to extract the text
soup = BeautifulSoup(response.text, 'html.parser')
all_text = soup.find_all('p')
combined_text = ""
for text in all_text:
    combined_text = combined_text + "\n" + text.get_text()

print(combined_text)
prompt = f"""
Extract the key bullet points from the following text.
Text:{combined_text}
"""
print_llm_response(prompt)
```
```
import os
import requests
from aisetup import print_llm_response
from dotenv import load_dotenv

# Get the Weather API key from the .env file
load_dotenv('.env', override=True)
api_key = os.getenv('WEATHER_API_KEY')

url = f"https://api.openweathermap.org/data/2.5/forecast?units=metric&cnt=1&lat={lat}&lon={lon}&appid={api_key}"
response = requests.get(url, verify=False)
data = response.json()
print(data)
```