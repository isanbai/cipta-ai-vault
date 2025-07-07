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

for file in Roadmap/Week*.md; do
  WEEK=$(basename "$file" .md)
  TOTAL=$(grep -cE '^\s*[-*] \[.\]' "$file")
  DONE=$(grep -cE '^\s*[-*] \[x\]' "$file")
  echo "| [$WEEK]($file) | $TOTAL | $DONE |" >> "$DASHBOARD_FILE"
done


echo "" >> "$DASHBOARD_FILE"

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
echo "| Week | Course | Status |" >> "$DASHBOARD_FILE"
echo "|------|--------|--------|" >> "$DASHBOARD_FILE"

for f in Roadmap/Week*.md; do
  WEEK=$(basename "$f" .md)
  IN_COURSE_SECTION=false
  while IFS= read -r line; do
    if [[ $line == "## 📚 Course Progress:"* ]]; then
      IN_COURSE_SECTION=true
      continue
    fi

    if [[ $IN_COURSE_SECTION == true ]]; then
      if [[ $line == "## "* ]]; then
        break
      fi

      if echo "$line" | grep -qE "^\[[xX ]\]\ \[.*\]\(.*\)"; then
        STATUS=$(echo "$line" | grep -o "\[[xX ]\]" | sed 's/\[x\]/✅/;s/\[X\]/✅/;s/\[ \]/❌/')
        COURSE=$(echo "$line" | sed -E 's/^\[[xX ]\] //')
        echo "| $WEEK | $COURSE | $STATUS |" >> "$DASHBOARD_FILE"
      fi
    fi
  done < "$f"
done
