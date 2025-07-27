import urllib.request
import urllib.parse
import urllib.error
from bs4 import BeautifulSoup
# defauts to certicate verification and most secure protocol (now TLS)
import ssl

# Ignore SSL/TLS certificate errors
ctx = ssl.create_default_context()
ctx.check_hostname = False
ctx.verify_mode = ssl.CERT_NONE

count = 1
counts = int(input('Count:'))
pos = int(input('Position:'))

# Retrieve all of the anchor tags
url = "http://py4e-data.dr-chuck.net/known_by_Shanelle.html"
while count <= counts:
    html = urllib.request.urlopen(url, context=ctx).read()
    soup = BeautifulSoup(html, 'html.parser')
    tags = soup('a')
    data = tags[pos-1]
    url = data.get('href', None)
    print(url)
    count += 1
