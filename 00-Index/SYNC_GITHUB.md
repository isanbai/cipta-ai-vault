# 🔄 Sync Vault ke GitHub

### 1. Inisialisasi Git
```bash
git init
git remote add origin https://github.com/username/Obsidian-AI-Vault.git
```

### 2. Commit dan Push
```bash
git add .
git commit -m "Update vault"
git push -u origin main
```

### 3. Sync Otomatis (optional)
Gunakan plugin [Obsidian Git](https://github.com/denolehov/obsidian-git) untuk sync otomatis setiap kali ada perubahan.

- Aktifkan plugin → masuk ke Settings → Plugin → Obsidian Git
- Set auto-pull dan auto-commit: ON
