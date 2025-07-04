
import os
from notion_client import Client
from datetime import datetime, timedelta

notion = Client(auth=os.environ["NOTION_TOKEN"])
database_id = os.environ["NOTION_DATABASE_ID"]

def create_or_update_weeks():
    start_date = datetime(2025, 7, 1)
    status_default = "Not started"

    for i in range(1, 13):
        title = f"Week {i:02d}"
        date_str = (start_date + timedelta(weeks=i - 1)).strftime("%Y-%m-%d")

        # Cek apakah page dengan judul itu sudah ada
        query = notion.databases.query(
            **{
                "database_id": database_id,
                "filter": {
                    "property": "Week",
                    "title": {
                        "equals": title
                    }
                }
            }
        )

        properties = {
            "Week": {
                "title": [
                    {
                        "type": "text",
                        "text": {
                            "content": title
                        }
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

        if query["results"]:
            # Update jika sudah ada
            page_id = query["results"][0]["id"]
            print(f"🔄 Updating: {title}")
            notion.pages.update(page_id=page_id, properties=properties)
        else:
            # Buat baru
            print(f"🆕 Creating: {title}")
            notion.pages.create(parent={"database_id": database_id}, properties=properties)

if __name__ == "__main__":
    create_or_update_weeks()
