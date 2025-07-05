
import os
from notion_client import Client
from datetime import datetime, timedelta

notion = Client(auth=os.environ["NOTION_TOKEN"])
database_id = os.environ["NOTION_DATABASE_ID"]
vault_path = "Cipta_AI_Vault"

def markdown_to_blocks(md_text):
    # Markdown sederhana → blok paragraph Notion
    return [{
        "object": "block",
        "type": "paragraph",
        "paragraph": {
            "rich_text": [
                {
                    "type": "text",
                    "text": {"content": md_text}
                }
            ]
        }
    }]

def create_or_update_weeks():
    start_date = datetime(2025, 7, 1)
    status_default = "Not started"

    for i in range(1, 13):
        title = f"Week {i:02d}"
        date_str = (start_date + timedelta(weeks=i - 1)).strftime("%Y-%m-%d")
        md_path = os.path.join(vault_path, f"week-{i:02d}.md")

        if not os.path.exists(md_path):
            print(f"❌ Markdown file not found: {md_path}")
            continue

        with open(md_path, "r", encoding="utf-8") as f:
            md_content = f.read()

        # Cek apakah halaman sudah ada
        query = notion.databases.query(
            **{
                "database_id": database_id,
                "filter": {
                    "property": "Week",
                    "title": {"equals": title}
                }
            }
        )

        properties = {
            "Week": {
                "title": [
                    {
                        "type": "text",
                        "text": {"content": title}
                    }
                ]
            },
            "Status": {
                "status": {
                    "name": status_default
                }
            },
            "Target Tanggal": {
                "date": {
                    "start": date_str
                }
            }
        }

        new_blocks = markdown_to_blocks(md_content)

        if query["results"]:
            page_id = query["results"][0]["id"]
            print(f"🔄 Updating page: {title}")

            # 1. Hapus semua blok lama
            children = notion.blocks.children.list(page_id).get("results", [])
            for child in children:
                try:
                    notion.blocks.delete(block_id=child["id"])
                except:
                    print(f"⚠️ Gagal hapus blok: {child['id']}")

            # 2. Update properti + isi ulang konten
            notion.pages.update(page_id=page_id, properties=properties)
            notion.blocks.children.append(block_id=page_id, children=new_blocks)
        else:
            print(f"🆕 Creating page: {title}")
            notion.pages.create(
                parent={"database_id": database_id},
                properties=properties,
                children=new_blocks
            )

if __name__ == "__main__":
    create_or_update_weeks()
