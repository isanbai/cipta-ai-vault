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

## 📚 Course
```dataviewjs
const files = dv.pages('"Roadmap"')
  .where(p => p.file.name.startsWith("Week"));

let allCourses = [];

for (let page of files) {
  const contents = await dv.io.load(page.file.path);
  const sectionRegex = /## 📚 Course Progress([\s\S]*?)(\n## |$)/g;
  const match = sectionRegex.exec(contents);
  
  if (match && match[1]) {
    const checklistRegex = /^- \[[ xX]\] .+/gm;
    const tasks = match[1].match(checklistRegex);
    if (tasks) {
      allCourses.push(...tasks);
    }
  }
}

dv.paragraph(allCourses.join('\n'));
```

```dataviewjs
let pages = dv.pages('"Roadmap"')
    .where(p => p.file.name != "Weekly_Template" && p.Insight);

let allInsights = [];

for (let page of pages) {
    if (Array.isArray(page.Insight)) {
        allInsights.push(...page.Insight);
    } else if (typeof page.Insight === 'string') {
        allInsights.push(page.Insight);
    }
}

dv.header(2, "💡 Insight Mingguan");
dv.list(allInsights);
```

## 📘 Course Progress Mingguan

```dataviewjs
let weeks = dv.pages('"Roadmap"')
    .where(p => p.file.name.startsWith("Week") && p["Course Progress"]);

let allCourses = [];

for (let page of weeks) {
    let courses = page["Course Progress"];
    if (Array.isArray(courses)) {
        allCourses.push(...courses);
    } else if (courses) {
        allCourses.push(courses);
    }
}

dv.list(allCourses);
```
