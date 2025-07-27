from urllib.request import urlopen
from bs4 import BeautifulSoup
import ssl

# Ignore SSL certificate errors
ctx = ssl.create_default_context()
ctx.check_hostname = False
ctx.verify_mode = ssl.CERT_NONE

url = "http://py4e-data.dr-chuck.net/comments_2264601.html"
html = urlopen(url, context=ctx).read()
soup = BeautifulSoup(html, "html.parser")

# Retrieve all of the anchor tags
total = 0
tags = soup('span')
for tag in tags:
    data = tag.contents[0]
    data = int(data)
    total += data
print(total)
