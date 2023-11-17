# SUBMODUL REST SERVICE

Repositori ini adalah submodul rest-service dari Tugas Besar IF3110 Pengembangan Aplikasi Berbasis Web sekaligus merupakan back-end dari submodul client-spa

## How to run
0. Clone repository submodul config dengan
```sh
git clone https://github.com/AlphaThrone/client-spa
git clone https://github.com/AlphaThrone/rest-service
git clone https://github.com/AlphaThrone/php-app
git clone https://github.com/AlphaThrone/config
git clone https://github.com/AlphaThrone/soap-service
```
1. Buka reposity **config** dan jalankan pada terminal
```sh
docker compose up --build -d
```
2. Lakukan seeding dari backend database dengan cara sebagai berikut: <br>
    1. Buka phpmyadmin <br>
    klik [disini](localhost:8080) atau akses link berikut pada browser
    ```
    localhost:8080
    ```
    2. Pilih database saranghaengbok_rest
    3. Pilih import
    4. Pilih file pada direktori core/db.sql
3. Open folder in Intellij IDEA
4. Go to File->Project Structure->Project
5. Change the SDK to correto-1.8 by following these step
6. *Click* SDK -> + Add SDK -> Download JDK
7. Change version and vendor to:
```
version: 1.8
vendor: Amazon Corretto
```
8. Done

### SOAP
Fungsi Transaction : 13521012
