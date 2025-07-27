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
print(totals)
