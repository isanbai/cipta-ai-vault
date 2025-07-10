Mantap, kita mulai dari awal ya. Kamu akan belajar AI dari **dasar** sampai bisa membangun proyek nyata seperti **Ammar-AI**, **Dakwah-AI**, dan **HireJob**. Aku akan bantu siapkan:

1. **Langkah setup tools**
    
2. **Struktur belajar Week 1**
    
3. **File latihan + Google Colab**
    
4. **GitHub repo dan struktur folder**
    

---

## 🧰 LANGKAH 1: Setup Tools & Lingkungan Kerja

### ✅ 1. Gunakan **Google Colab** (gratis)

- Kelebihan: tidak perlu install apapun
    
- Cukup akun Google → buka [https://colab.research.google.com](https://colab.research.google.com/)
    

### ✅ 2. (Opsional) Setup Lokal di PC

> Kalau kamu mau juga pakai lokal (misal di VSCode):

1. Install **Python 3.10**
    
2. Install **VSCode**
    
3. Install **Jupyter Notebook**
    
    ```bash
    pip install notebook
    ```
    
4. Install library dasar:
    
    ```bash
    pip install numpy pandas matplotlib scikit-learn
    ```
    

### ✅ 3. Install Git (untuk GitHub)

- [Download Git](https://git-scm.com/downloads)
    
- Setup Git:
    
    ```bash
    git config --global user.name "Namamu"
    git config --global user.email "emailkamu@example.com"
    ```
    

---

## 🗂️ LANGKAH 2: Struktur Folder & GitHub

### Struktur repo `AI-Roadmap-Personal`

```plaintext
AI-Roadmap-Personal/
├── README.md
├── Week01_Fundamentals/
│   ├── modul.md
│   ├── prak1_linear_regression.ipynb
├── Week02_NeuralNet/
│   ├── ...
```

> Nanti akan saya buatkan file ZIP untuk Week 1–3 agar kamu tinggal upload ke GitHub dan buka di Colab.

---

## 🧪 LANGKAH 3: Week 1 – Belajar Dasar AI & ML

### 🎯 Tujuan:

Pahami perbedaan AI, ML, dan Deep Learning + latihan Linear Regression

### 📘 Materi:

- Apa itu AI, ML, DL?
    
- Supervised vs Unsupervised
    
- Linear Regression
    
- Evaluasi model (MSE, MAE)
    

### 🛠️ Tools:

- Google Colab
    
- `numpy`, `pandas`, `matplotlib`, `scikit-learn`
    

### 🧑‍💻 Praktikum:

Prediksi tinggi badan berdasarkan umur dengan **Linear Regression**

```python
# Contoh kode awal (nanti lengkap di file IPYNB)
from sklearn.linear_model import LinearRegression
import numpy as np
import matplotlib.pyplot as plt

X = np.array([[5], [6], [7], [8], [9]])
y = np.array([110, 115, 120, 130, 135])

model = LinearRegression()
model.fit(X, y)

plt.scatter(X, y)
plt.plot(X, model.predict(X), color='red')
plt.title("Prediksi Tinggi Badan berdasarkan Umur")
plt.show()
```

---

## 📦 LANGKAH 4: File ZIP Week 1–3

Saya akan **generate ulang file ZIP** yang berisi:

- `README.md`
    
- Folder Week 1–3
    
- File `.ipynb` awal + modul.md per minggu
    

Tunggu sebentar ya...

Berikut file ZIP terbaru untuk memulai belajar AI dari Week 1–3:

📦 [Download AI-Roadmap-Personal.zip](sandbox:/mnt/data/AI-Roadmap-Personal.zip)

---

## ✅ Langkah Lanjutan

### 1. **Upload ke GitHub**

- Buat repo: `AI-Roadmap-Personal`
    
- Upload isi ZIP ke dalamnya
    

### 2. **Buka di Google Colab**

Setelah diupload, kamu bisa buka notebook seperti ini:

```
https://colab.research.google.com/github/USERNAME/AI-Roadmap-Personal/blob/main/Week01_Fundamentals/prak1_linear_regression.ipynb
```

Ganti `USERNAME` dengan akun GitHub kamu.

### 3. **Mulai Praktikum Week 1**

- Buka `prak1_linear_regression.ipynb` di Colab
    
- Saya bisa bantu lengkapi kodenya di situ
    
- Atau kamu bisa bilang **“lanjut coding Week 1”** sekarang juga
    

Siap lanjut coding?