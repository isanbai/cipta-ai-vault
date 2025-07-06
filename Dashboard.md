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


```dataviewjs
const pages = dv.pages('"Roadmap"')
  .where(p => p.file.name.startsWith("Week"));

let checklistByHeading = {};

for (let page of pages) {
  const content = await dv.io.load(page.file.path);
  const lines = content.split("\n");

  let currentHeading = null;

  for (let line of lines) {
    const headingMatch = line.match(/^##\s+(.*)/);
    const checklistMatch = line.match(/^-\s\[( |x|X)\]\s+(.*)/);

    if (headingMatch) {
      currentHeading = headingMatch[1].trim();
    } else if (checklistMatch && currentHeading) {
      if (!checklistByHeading[currentHeading]) checklistByHeading[currentHeading] = [];
      checklistByHeading[currentHeading].push(line);
    }
  }
}

// Tampilkan hasilnya
for (let heading in checklistByHeading) {
  dv.header(2, heading); // buat heading
  dv.paragraph(checklistByHeading[heading].join("\n"));
}

```
