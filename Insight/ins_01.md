

```
# Follow up with an additional print command
print("How is your day going?")
print(f"Isabel is {28/7:.0f} dog years old.")

name = "Otto Matic"
dog_age = 21/7
print_llm_response(f"""
If {name} were a dog, he would be {dog_age} years old.
Describe what life stage that would be for a dog.
""")

fav_num = 10
print(f"Your favorite number plus 10 is {fav_num+10}")
```
```
name = "Tommy"
potatoes = 4.75
prompt = f"""Write a couplet about my friend {name} who has about {round(potatoes)} potatoes"""
response = get_llm_response(prompt)
print(response)
```
```
ice_cream_flavors = [
    "Vanilla",
    "Chocolate",
    "Strawberry",
    "Mint Chocolate Chip"
]
for flavor in ice_cream_flavors:
    prompt = f"""For the ice cream flavor listed below, 
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
def fahrenheit_to_celsius(fahrenheit):
    celsius = (fahrenheit - 32) * 5 / 9
    print(f"{fahrenheit}°F is equivalent to {celsius:.2f}°C")
    
from helper_functions import celsius_to_fahrenheit
import helper_functions
helper_functions.print_llm_response("What is the capital of France?")
from helper_functions import *
```