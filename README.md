# 9.5 Lanjutan dari 9. Latihan Scheduler dengan WorkManager. Dimana Parsing REST API menggunakan moshi

Ini adalah proyek latihan dari kursus **Android Fundamental: Background Task & Scheduler** di platform **Dicoding**.



Misalnya Anda ingin mengambil data description maka Anda perlu melewati JsonArray weather dulu, kemudian ambil urutan ke-0 dan Anda baru bisa mengambil data description. 
Contoh untuk kodenya seperti ini: .getJSONArray("weather").getJSONObject(0).getString("description") dan jika Anda ingin mengambil data suhu maka kodenya 
seperti ini: .getJSONObject("main").getDouble("temp"). Sehingga, untuk mendapatkan data secara keseluruhan akan seperti ini:
