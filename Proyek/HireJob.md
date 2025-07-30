# 💼 Proyek HireJob

### **AutoGen (Microsoft)**

- **Kelebihan:**
    - Mendukung multi-agent conversation.
    - Modular dan bisa diatur untuk collaborative reasoning.
    - Terbuka untuk integrasi dengan API eksternal dan tool manusia.

- **Cocok untuk:** Platform AI yang melibatkan banyak agen bekerja sama (misal: Research Agent, Coding Agent, Planner Agent).
- **Repo:** [github.com/microsoft/autogen](https://github.com/microsoft/autogen)

### **LangGraph (oleh LangChain)**

- **Kelebihan:**
    - Alur kerja agent **berbasis state machine**.
    - Bisa membuat multi-agent workflow dengan kontrol penuh atas loop, branch, dan memory.
    - Terintegrasi penuh dengan LangChain Ecosystem.

- **Cocok untuk:** Platform produk AI yang melibatkan decision tree dan memory tracking kompleks.
- **Website:** https://www.langgraph.dev

### **CrewAI**

- **Kelebihan:**
    - Fokus pada **tim agent yang bisa bekerja sama** dengan role seperti researcher, writer, coder, reviewer, dsb.
    - Mengatur **workflow otomatis multi-agent**.
    - Support `agent memory`, tool chaining, dan LLM lokal.

- **Lebih fleksibel** dari emergent.sh dan cocok untuk web app dengan fungsionalitas AI produktif.
- **Repo:** [https://github.com/joaomdmoura/crewAI](https://github.com/joaomdmoura/crewAI)

### **OpenDevin (Developer-focused Agent OS)**

- **Fokus:** Asisten coding berbasis terminal dengan agent autonomous.
- **Kelebihan:**
    - Bisa menjalankan perintah real-time di terminal.
    - Bisa menjalankan shell, testing, ngoding real
    - Bisa ditambahin agent lain via AutoGen atau CrewAI
    - Cocok jika kamu mau buat platform AI engineer assistant (seperti DevOps AI).
- **Website:** https://www.opendevin.org

### **Superagent / AgentHub**

- **Superagent:** Framework LLM agent siap deploy dengan UI, logging, API key management.
- **AgentHub.ai:** UI-first platform untuk build & deploy agents dari UI (drag-and-drop).
- **Cocok untuk:** SaaS-style AI product builder.

### **GPT-Engineer**

- ✅ Kamu cukup beri prompt deskripsi proyek: “Buat platform pencari kerja untuk fresh graduate dengan fitur rekomendasi AI”
- 🔨 AI akan **bangun struktur folder, file, kode backend/frontend, dsb**
- ✅ Cocok untuk membangun awal platform HireJob
- 📦 [https://github.com/AntonOsika/gpt-engineer](https://github.com/AntonOsika/gpt-engineer)
- ❗Butuh sedikit setup awal (bisa saya bantu kalau kamu mau full auto pakai)

### **Cursor.so + GPT-4o (atau GPT-Engineer)** (Langsung coding dengan AI assist penuh)

- Jika kamu mau **partial ngoding** (copy-paste hasil dari AI), ini powerful banget.
- Bisa jalanin AI agent seperti emergent.sh, tapi lokal dan lebih kuat.

## Perbandingan Ringkas

|Platform|Multi-agent|Web Integration|Tool Use|Memory|Best For|
|---|---|---|---|---|---|
|emergent.sh|❌ Basic|✅ Mudah|❌ Terbatas|❌ No|Personal use|
|AutoGen (MS)|✅ Ya|✅ (FastAPI, UI)|✅ Ya|✅ Ya|Research, Team Agents|
|LangGraph|✅ Ya|✅ Modular|✅|✅|Workflow complex|
|CrewAI|✅ Ya|✅ Siap pakai|✅ Ya|✅ Ya|Role-based agent team|
|OpenDevin|✅ Ya|✅ (Dev UI)|✅|✅|AI Developer Agent|
|Superagent|✅ Ya|✅ Built-in UI|✅|✅|SaaS-ready LLM Agent|

Jika kamu ingin membangun **platform AI publik seperti emergent.sh namun lebih fleksibel dan powerful**, **kombinasi terbaik saat ini:**

- **CrewAI + LangGraph** (untuk team-based agent dan workflow branching)
- Integrasikan dengan **FastAPI** untuk backend dan **React/Tailwind** untuk frontend UI-nya.

## Rekomendasi Praktis Buat Kamu (No Code Langsung)

|Tujuan|Rekomendasi|
|---|---|
|Beneran tanpa coding, pakai UI dan AI langsung|**Builder.ai** atau **Bubble + GPT Plugin**|
|Semi-auto, AI generate semua kode, tinggal deploy|**GPT-Engineer** atau **OpenDevin**|
|Ingin bikin agen AI yang bantu bangun web-mu|**AutoGen + CrewAI** atau **LangGraph**|
|Butuh bantuan setup lokal/online|Saya bisa bantu setup pipeline-nya|
## Contoh Use Case (di OpenDevin atau GPT-Engineer)

Kamu cukup ketik:

> "Buat web platform pencari kerja bernama HireJob. Ada form login, halaman loker, dan fitur AI untuk rekomendasi kerja."

Dan AI akan:
- Membuat struktur project
- Kode HTML/React + Python backend
- Tambahkan fitur search, filter, API

### Stack AI Agent Lokal:

1. **[OpenDevin](https://github.com/OpenDevin/OpenDevin)** (main agent engine)
2. **[GPT-Engineer](https://github.com/AntonOsika/gpt-engineer)** (untuk generate full code project dari deskripsi)
3. **Model LLM Lokal** (opsional):
    - DeepSeek Coder 33B (via Ollama)
    - Code Llama 70B (via LM Studio atau vLLM)
    - Atau gunakan GPT-4o via API (jika ingin hybrid)

## Integrasi OpenDevin + GPT-Engineer

Kamu bisa minta OpenDevin untuk:
1. Generate full project struktur via GPT-Engineer.
2. Edit kodenya langsung via terminal yang bisa dia akses.
3. Jalankan hasilnya di localhost (dengan dukungan Docker/Node/Python).

### **Gunakan GPT-Engineer dulu → lanjut ke OpenDevin untuk refine**

- GPT-Engineer buat struktur full project
- OpenDevin bisa bantu debugging, perintah kecil, dan testing
    > “Tes koneksi frontend ke API backend”  
    > “Tambahkan halaman detail lowongan”
    
Semua otomatis nyambung karena **GPT-Engineer paham struktur proyek penuh**.

## Rencana Final: Full Lokal Agent untuk Platform HireJob

### ✅ Komponen yang Akan Kita Pakai

|Komponen|Fungsi|
|---|---|
|**Ollama**|Jalankan LLM lokal (DeepSeek, LLaMA 3)|
|**OpenDevin**|Agent utama: menerima perintah dan eksekusi|
|**GPT-Engineer**|Buat full project dari prompt|
|**Docker + Python**|Jalankan dan isolasi tool lain|
|**VS Code / Terminal**|(opsional) untuk lihat hasil|

## Penjelasan Teknis: Perilaku Default di Setup Lokal

|Komponen|Bisa pahami hubungan antar file?|Auto wiring?|Catatan|
|---|---|---|---|
|**GPT-Engineer**|✅ Ya (secara penuh)|✅ Ya|Jika prompt benar dan model cukup|
|**OpenDevin**|⚠️ Belum otomatis penuh|⚠️ Manual step|Bisa kamu minta: “Perbaiki wiring frontend-backend-nya”|
|**CrewAI / AutoGen**|✅ Dengan memory agent|✅ Bisa|Butuh konfigurasi awal multi-agent|
|**Ollama LLM (DeepSeek)**|✅ Bisa (33B ke atas)|✅|Tapi butuh waktu generate karena lokal|

## Cara Setup Hybrid (Rekomendasi)

|Langkah|Tools|
|---|---|
|1. Build project|Aku → prompt → GPT-Engineer|
|2. Jalankan lokal|GPT-Engineer lokal|
|3. Revisi cerdas|Aku → kasih langkah spesifik|
|4. Jalankan ulang|OpenDevin + lokal model (opsional)|