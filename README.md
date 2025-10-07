# 📱 Mobil Cihazlarda Uygulama Geliştirme

Bu repoda Android platformu için Kotlin dili kullanılarak geliştirilmiş mobil uygulama projelerim yer almaktadır.

## 🎯 Projeler

### 1. [Öğrenci Kayıt Sistemi](./ogrenci-kayit-sistemi)
Öğrenci bilgilerini kaydetme, güncelleme ve yönetme uygulaması.

**Özellikler:**
- 📝 Öğrenci ekleme, güncelleme ve silme
- 🔍 Öğrenci arama ve filtreleme
- 💾 SQLite veritabanı ile veri kalıcılığı
- 📊 Öğrenci listesi görüntüleme

**Teknolojiler:** Android Studio, Kotlin, SQLite

---

### 2. [Hesap Makinesi](./hesap-makinesi)
Temel matematiksel işlemler yapan kullanıcı dostu hesap makinesi uygulaması.

**Özellikler:**
- ➕➖✖️➗ Dört işlem (Toplama, Çıkarma, Çarpma, Bölme)
- 🔢 Ondalıklı sayı desteği
- 🧹 Temizleme (Clear) fonksiyonu
- 📱 Modern ve sezgisel arayüz

**Teknolojiler:** Android Studio, Kotlin

---

## 🛠️ Kullanılan Teknolojiler

- **IDE:** Android Studio (Latest Version)
- **Dil:** Kotlin
- **Min SDK:** API 21 (Android 5.0 Lollipop)
- **Target SDK:** API 34 (Android 14)
- **Veritabanı:** SQLite (Öğrenci Kayıt Sistemi)
- **UI:** XML Layouts, Material Design Components

## 📋 Gereksinimler

- Android Studio Hedgehog | 2023.1.1 veya üzeri
- JDK 17 veya üzeri
- Android SDK
- Kotlin 1.9.0 veya üzeri

## 🚀 Projeleri Çalıştırma

1. **Projeyi Klonlayın:**
```bash
git clone https://github.com/Sercansen21/mobil-uygulama-gelistirme.git
```

2. **Android Studio'da Açın:**
   - Android Studio'yu açın
   - `File > Open` seçin
   - İlgili proje klasörünü seçin (ogrenci-kayit-sistemi veya hesap-makinesi)

3. **Gradle Sync:**
   - Android Studio otomatik olarak Gradle sync başlatacaktır
   - Bekleyin ve bağımlılıkların indirilmesini sağlayın

4. **Uygulamayı Çalıştırın:**
   - Emülatör veya fiziksel cihaz bağlayın
   - `Run > Run 'app'` veya `Shift + F10` tuşlarına basın

## 📂 Proje Yapısı

```
mobil-uygulama-gelistirme/
│
├── ogrenci-kayit-sistemi/
│   ├── app/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/
│   │   │   │   ├── res/
│   │   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   ├── gradle/
│   ├── build.gradle
│   └── README.md
│
├── hesap-makinesi/
│   ├── app/
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/
│   │   │   │   ├── res/
│   │   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   ├── gradle/
│   ├── build.gradle
│   └── README.md
│
├── README.md
└── .gitignore
```

## 📚 Öğrenilen Konular

- ✅ Kotlin programlama dili temelleri
- ✅ Android Activity ve Fragment yaşam döngüsü
- ✅ XML ile UI tasarımı
- ✅ SQLite veritabanı işlemleri
- ✅ CRUD (Create, Read, Update, Delete) operasyonları
- ✅ RecyclerView kullanımı
- ✅ Material Design prensipleri
- ✅ Event handling ve button click işlemleri

## 🤝 Katkıda Bulunma

Pull request'ler kabul edilir. Büyük değişiklikler için lütfen önce bir issue açarak neyi değiştirmek istediğinizi tartışın.

## 📄 Lisans

[MIT](https://choosealicense.com/licenses/mit/)

## 👤 Geliştirici

**Sercan Şen**
- GitHub: [@Sercansen21](https://github.com/Sercansen21)
- Email: sen.sercan.21@email.com

---

⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!