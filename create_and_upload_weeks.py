
from notion_client import Client
import os

notion = Client(auth=os.environ["NOTION_TOKEN"])
database_id = os.environ["NOTION_DATABASE_ID"]

roadmap_dir = "Roadmap Mingguan"
weeks = [f"Week {i:02}.md" for i in range(1, 15)]

for week_file in weeks:
    file_path = os.path.join(roadmap_dir, week_file)
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        continue

    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    title = week_file.replace(".md", "")

    page = notion.pages.create(
        parent={"database_id": database_id},
        properties={
            "title": [
                {
                    "type": "text",
                    "text": {"content": title}
                }
            ]
        },
        children=[
            {
                "object": "block",
                "type": "paragraph",
                "paragraph": {
                    "rich_text": [
                        {
                            "type": "text",
                            "text": {"content": content[:2000]}
                        }
                    ]
                }
            }
        ]
    )

    print(f"✅ Created: {title} → Page ID: {page['id']}")
