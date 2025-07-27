import requests
from openai import OpenAI
import os
import re
handle = open("regex_sum_2264599.txt", "r")
data = []
for line in handle:
    data += re.findall('[0-9]+', line)
# Convert all items to int and sum them
# total = sum(int(num) for num in data)
totals = 0
for num in data:
    total = int(num)
    totals += total
# print(totals)


API_URL = "https://router.huggingface.co/v1/chat/completions"
headers = {
    "Authorization": f"Bearer {os.environ['']}",
}


def query(payload):
    response = requests.post(API_URL, headers=headers, json=payload)
    return response.json()


response = query({
    "messages": [
        {
            "role": "user",
            "content": "What is the capital of France?"
        }
    ],
    "model": "Qwen/Qwen3-8B:featherless-ai"
})

print(response["choices"][0]["message"])
