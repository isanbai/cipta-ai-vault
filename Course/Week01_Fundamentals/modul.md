# Week 1 – Dasar AI & ML

- Pengenalan AI, ML, Deep Learning
- Supervised vs Unsupervised
- Linear Regression
- Tugas: Prediksi tinggi badan berdasarkan umur

> AI adalah bidang komputer yang membuat mesin mampu **melakukan tugas-tugas cerdas** seperti manusia: berpikir, belajar, mengambil keputusan.

> ML adalah cabang dari AI. Di sini **mesin belajar dari data** (tanpa diprogram eksplisit).
- Data CV dan hasil wawancara → mesin belajar dan bisa menilai kandidat baru

> DL adalah bagian dari ML, tapi menggunakan **neural network** yang mirip otak manusia. Biasanya digunakan untuk:
- Gambar (vision)
- Suara (speech)
- Bahasa (text/NLP)
 Neural Network = Jaringan Otak Buatan
 Struktur dasarnya seperti ini:
[Input Layer]     
	↓ 
[Hidden Layer 1]     
	↓ 
[Hidden Layer 2]
	↓ 
[Output Layer]`
Setiap layer berisi **neuron** (mirip sel otak) yang mengolah angka dan memberikan hasil ke layer berikutnya.

| Konsep            | Penjelasan Singkat                    | Contoh                 |
| ----------------- | ------------------------------------- | ---------------------- |
| **AI**            | Mesin bisa berpikir & ambil keputusan | Game catur AI, ChatGPT |
| **ML**            | Mesin belajar dari data               | Prediksi cuaca         |
| **Deep Learning** | ML dengan neural network dalam        | Face recognition, TTS  |

|Kasus|ML Biasa|DL (Deep Learning)|
|---|---|---|
|Prediksi harga rumah|✔️ Linear Regression|❌ Tidak perlu DL|
|Deteksi wajah|❌ Sulit|✔️ Convolutional Neural Network (CNN)|
|ChatBot|❌ Terbatas|✔️ Transformer / LSTM|
|Deteksi suara|❌ Lemah|✔️ Recurrent Neural Network (RNN)|

Data ➜ Preprocessing ➜ Training Model ➜ Evaluation ➜ Prediction

1. Kumpulkan Data (Dataset)
2. Preprocessing (Persiapan Data)
	- Mengubah teks jadi angka (`Lokasi: Bandung → 1, Cimahi → 0`)
	- Normalisasi angka (misalnya agar semua kolom dalam rentang 0–1)
	- Hapus data kosong/error
3. Training Model (Melatih Otak AI-nya)
	Algoritma yang digunakan bisa:
	- Decision Tree (ML)
	- Linear Regression (ML)
	- Neural Network (jika masuk DL)
	🔧 Tujuannya: agar data bisa diproses komputer.
4. Evaluasi (Uji Akurasi)
5. Prediksi Data Baru

### 🟩 **Supervised Learning**

- Ada input & output
- Model belajar dari contoh yang lengkap
- Contoh: Prediksi harga rumah (dengan data harga sebelumnya)
    
### 🟦 **Unsupervised Learning**

- Hanya ada input (tidak ada label)
- Model cari pola sendiri
- Contoh: Clustering pelanggan berdasarkan kebiasaan belanja
    
### 🟥 **Reinforcement Learning**

- Model belajar dari **trial and error**
- Diberi **reward atau hukuman**
- Contoh: Robot belajar jalan, AI main game



ML Flow:

[Data mentah] 
  ↓
[Preprocessing] 
  ↓
[Training dengan algoritma ML] 
  ↓
[Evaluasi model] 
  ↓
[Prediksi data baru]

DL Flow:

[Data besar (gambar, suara, teks)]
  ↓
[Preprocessing minimal] 
  ↓
[Deep Neural Network Training] 
  ↓
[Evaluasi model] 
  ↓
[Prediksi]

### 🧰 Tools Coding & Praktik:

|Tool|Fungsi|Saran|
|---|---|---|
|**Python**|Bahasa utama AI|Pakai Python saja|
|**Jupyter/Colab**|Tempat eksperimen|Gunakan **Google Colab**|
|**Scikit-learn**|Untuk ML dasar|Sudah kamu pakai tadi|
|**Pandas**|Kelola data (CSV, Excel, dll)|Akan kita pakai minggu depan|
|**Matplotlib/Seaborn**|Grafik & visualisasi|Untuk analisis data|
|**TensorFlow / Keras**|Untuk Deep Learning|Kita pakai di Week 3|
|**PyTorch**|Alternatif DL (lebih teknikal)|Dipelajari saat lanjut ke level LLM|
|**HuggingFace**|Model pretrained (NLP)|Masuk Week 5+|
