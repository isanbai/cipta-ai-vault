## 📊 Progress Mingguan

```dataview
table file.link as "Week", length(filter(list, (t) => t.text != "")) as "Total Tasks", length(filter(list, (t) => t.completed)) as "Selesai"
from "Roadmap"
where contains(file.name, "Week")
flatten file.tasks as list
group by file.name
sort file.name asc
```