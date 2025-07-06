# 🧭 Dashboard Cipta AI Vault

## ✅ Progress Checklist Mingguan


```dataview
table length(file.tasks) as "Total Tasks", length(filter(file.tasks, (t) => t.completed)) as "Selesai"
from "Roadmap"
where contains(file.name, "Week")
sort file.name asc
```


## 🔧 Proyek Aktif
- [[Proyek/AmmarAI]]
- [[Proyek/HireJob]]
- [[Proyek/DakwahAI]]

## 📚 Kursus
- [[Resources/Coursera_Tracker]]

## 💡 Insight Mingguan

```dataview
list from "Roadmap"
where contains(text, "Insight") and file.name != "Weekly_Template"
```
