import re
handle = open("regex_sum_2264599.txt", "r")
data = []
for line in handle:
    data += re.findall(r'[0-9]+', line)
print(data)
