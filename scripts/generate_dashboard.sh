#!/bin/bash

# Output file
DASHBOARD_FILE="Dashboard.md"

echo "# 🕰️ Dashboard Cipta AI Vault" > "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"

# Progress Checklist Mingguan (Total Task + Selesai)
echo "## ✅ Progress Checklist Mingguan" >> "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"
echo "| Week | Total Tasks | Selesai |" >> "$DASHBOARD_FILE"
echo "|------|-------------|---------|" >> "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"
for file in Roadmap/Week*.md; do
  WEEK=$(basename "$file" .md)
  TOTAL=$(grep -cE '^\s*[-*] \[.\]' "$file")
  DONE=$(grep -cE '^\s*[-*] \[x\]' "$file")
  echo " [$WEEK](/$file)  $TOTAL  $DONE " >> "$DASHBOARD_FILE"
done



# Function untuk ambil isi per heading
extract_section() {
  local heading="$1"
  local file="$2"
  awk "/^## $heading/{flag=1;next}/^## /{flag=0}flag" "$file" |
    sed -e '/^$/d' -e 's/^ *//'
}

# 📚 Course Progress
echo "## 📚 Course Progress" >> "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"

for file in Roadmap/Week*.md; do
  extract_section "📚 Course Progress:" "$file" | grep -E '^\- \[[ xX]\]' >> "$DASHBOARD_FILE"
done

echo "" >> "$DASHBOARD_FILE"
