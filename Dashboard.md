# 🧭 Dashboard Cipta AI Vault

## ✅ Progress Checklist Mingguan


```dataview
table length(file.tasks) as "Total Tasks", length(filter(file.tasks, (t) => t.completed)) as "Selesai"
from "Roadmap"
where contains(file.name, "Week")
sort file.name asc
```

## 📌 Roadmap Mingguan
- [[Roadmap/Week 01]]
- [[Roadmap/Week 02]]
- [[Roadmap/Week 03]]
- [[Roadmap/Week 04]]
- [[Roadmap/Week 05]]
- [[Roadmap/Week 06]]
- [[Roadmap/Week 07]]
- [[Roadmap/Week 08]]
- [[Roadmap/Week 09]]
- [[Roadmap/Week 10]]
- [[Roadmap/Week 11]]
- [[Roadmap/Week 12]]
- [[Roadmap/Week 13]]
- [[Roadmap/Week 14]]
- [[Roadmap/Week 15]]

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
