

```
name = "Otto Matic"
dog_age = 21/7
print_llm_response(f"""
If {name} were a dog, he would be {dog_age} years old.`
Describe what life stage that would be for a dog and what that might entail in terms of energy level, interests, and behavior.
""")
```
```
name = "Tommy"
potatoes = 4.75
prompt = f"""Write a couplet about my friend {name} who has about {round(potatoes)} potatoes"""
response = get_llm_response(prompt)
print(response)
```
