# mobileprogramming2026
Pemrograman Mobile 2026


Fitur:

-Login (untuk user yang main)

-Leaderboard (menampilkan seluruh pemain) dan diberi #1,#2,#3...

-Username untuk leaderboard (biar tidak menunjukkan email player)



# Log Kerja Kelompok - Tugas UAS Pemrograman Mobile

| Tanggal | Yang dikerjakan | Kendala | Rencana berikutnya | Penggunaan AI |
| :--- | :--- | :--- | :--- | :--- |
| 13-11-2026 | Mengintegrasikan Firebase Crashlytics ke dalam project Android Studio dan menambahkan dependency pada `build.gradle`. | Masih bingung menentukan cara untuk memicu tes crash secara tepat agar laporannya terdeteksi di Firebase Console. | Melanjutkan integrasi fitur autentikasi pengguna menggunakan Firebase Google Sign-In. | Berkonsultasi dengan AI mengenai alur kerja Crashlytics dan cara mentrigger `RuntimeException` untuk pengujian test crash. |
| 18-11-2026 | Melakukan konfigurasi Firebase Authentication (Google Sign-In) pada `MainActivity.java` & `activity_main.xml`, serta mendaftarkan SHA-1 fingerprint ke Firebase Console. | Mengalami kesulitan saat mengatur alur login, callback result auth, dan visibilitas tombol UI saat status pengguna berubah. | Merapikan kode autentikasi dan menambahkan penanganan sesi akun serta pembuatan fitur logout. | Bertanya ke AI untuk memperbaiki error handling pada callback auth serta rekomendasi pengaturan UI visibilitas setelah login. |
| 19-11-2026 | Merapikan (refactoring) struktur kode logika login Google Sign-In dan memperbarui layout tampilan interface agar lebih rapi. | Tombol dan logika fitur untuk Logout belum selesai dibuat dan masih belum terhubung secara penuh. | Membuat dan menyempurnakan fitur logout, membersihkan session `GoogleSignInClient`, serta melakukan testing alur secara keseluruhan. | Meminta bantuan AI untuk meninjau struktur kode `FirebaseAuth` dan cara mengimplementasikan alur `signOut()` dengan aman. |
