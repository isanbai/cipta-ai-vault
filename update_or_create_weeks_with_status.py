
from notion_client import Client
import os
import time
from datetime import datetime, timedelta

notion = Client(auth=os.environ["NOTION_TOKEN"])
database_id = os.environ["NOTION_DATABASE_ID"]
roadmap_dir = "Roadmap Mingguan"
weeks = [f"Week {i:02}.md" for i in range(1, 15)]
start_date = datetime(2025, 7, 1)  # Start tanggal target belajar

def query_page_by_title(title):
    results = notion.databases.query(
        database_id=database_id,
        filter={
            "property": "title",
            "title": {
                "equals": title
            }
        }
    ).get("results")
    return results[0] if results else None

def delete_existing_blocks(page_id):
    blocks = notion.blocks.children.list(page_id).get("results", [])
    for block in blocks:
        try:
            notion.blocks.delete(block["id"])
            time.sleep(0.3)  # avoid rate limit
        except:
            pass

for i, week_file in enumerate(weeks):
    file_path = os.path.join(roadmap_dir, week_file)
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        continue

    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    title = week_file.replace(".md", "")
    target_date = start_date + timedelta(weeks=i)
    date_str = target_date.strftime("%Y-%m-%d")
    page = query_page_by_title(title)

    properties = {
        "title": [
            {
                "type": "text",
                "text": {
                    "content": title
                }
            }
        ],
        "Status": {
            "status": {
                "name": "Not started"  # atau "Selesai", "In Progress"
            }
        },
        "Target Tanggal": {
            "date": {
                "start": date_str  # format: "YYYY-MM-DD"
            }
        }
    }

    children = [{
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
    }]

    if page:
        page_id = page["id"]
        print(f"🔁 Updating existing page: {title}")
        notion.pages.update(page_id=page_id, properties=properties)
        delete_existing_blocks(page_id)
        notion.blocks.children.append(block_id=page_id, children=children)
    else:
        print(f"🆕 Creating new page: {title}")
        notion.pages.create(
            parent={"database_id": database_id},
            properties=properties,
            children=children
        )
