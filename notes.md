```dataview
table file.link, length(filter(list, (l) => contains(l.text, "- [x]"))) as "Done"
from "Roadmap"
```
