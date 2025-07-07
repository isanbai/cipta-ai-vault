#!/usr/bin/env bash

DASHBOARD_FILE="Dashboard.md"
ROADMAP_DIR="Roadmap"

echo "# 🕰️ Dashboard Cipta AI Vault" > "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"

# Section: Progress Checklist Mingguan
echo "## ✅ Progress Checklist Mingguan" >> "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"
echo "| Week | Total Tasks | Selesai |" >> "$DASHBOARD_FILE"
echo "|------|-------------|---------|" >> "$DASHBOARD_FILE"

for file in "$ROADMAP_DIR"/Week*.md; do
    WEEK=$(basename "$file" .md)
    TOTAL=$(grep -cE "^- \[[ xX]\]" "$file")
    DONE=$(grep -cE "^- \[[xX]\]" "$file")
    echo "| [$WEEK]($ROADMAP_DIR/$WEEK.md) | $TOTAL | $DONE |" >> "$DASHBOARD_FILE"
done

echo "" >> "$DASHBOARD_FILE"

# Section: Course Progress
echo "## 📚 Course Progress" >> "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"
echo "| Week | Course | Status |" >> "$DASHBOARD_FILE"
echo "|------|--------|--------|" >> "$DASHBOARD_FILE"

for f in "$ROADMAP_DIR"/Week*.md; do
    WEEK=$(basename "$f" .md)
    IN_COURSE_SECTION=false

    while IFS= read -r line; do
        if [[ "$line" == "#### 📚 Course Progress:"* ]]; then
            IN_COURSE_SECTION=true
            continue
        fi

        # Stop if next heading
        if [[ $IN_COURSE_SECTION == true && "$line" == "#"* ]]; then
            break
        fi

        if [[ "$IN_COURSE_SECTION" == true ]]; then
            if echo "$line" | grep -qE "^- \[[ xX]\]"; then
                # Ambil status checklist
                STATUS=$(echo "$line" | grep -o "\[[ xX]\]" | sed 's/\[x\]/✅/;s/\[X\]/✅/;s/\[ \]/❌/')
                
                # Ambil course markdown link
                COURSE=$(echo "$line" | sed -E 's/^- \[[xX ]\] (.*)/\1/')
                
                echo "| [$WEEK]($ROADMAP_DIR/$WEEK.md) | $COURSE | $STATUS |" >> "$DASHBOARD_FILE"
            fi
        fi
    done < "$f"
done

echo "" >> "$DASHBOARD_FILE"

# Section: Proyek Aktif (opsional)
echo "## 🛠️ Proyek Aktif" >> "$DASHBOARD_FILE"
echo "" >> "$DASHBOARD_FILE"
echo "- [AmmarAI](Proyek/AmmarAI.md)" >> "$DASHBOARD_FILE"
echo "- [HireJob](Proyek/HireJob.md)" >> "$DASHBOARD_FILE"
echo "- [DakwahAI](Proyek/DakwahAI.md)" >> "$DASHBOARD_FILE"
