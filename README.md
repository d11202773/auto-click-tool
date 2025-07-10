# Auto Click Tool - Android

Ứng dụng Android tự động nhấp chuột tương tự Super Auto Click với khả năng mở rộng cho tương lai.

## 🚀 Tải APK ngay

### Cách 1: GitHub Releases (Khuyến nghị)
- Vào tab **[Releases](../../releases)**
- Tải file APK mới nhất
- Cài đặt trên Android

### Cách 2: GitHub Actions Artifacts
- Vào tab **[Actions](../../actions)**
- Chọn build mới nhất có ✅
- Download "auto-click-tool-debug" artifact

## 📱 Hướng dẫn cài đặt nhanh

1. **Tải APK** từ Releases hoặc Actions
2. **Cài đặt** trên Android device (cho phép "Unknown sources")
3. **Cấp quyền**:
   - Settings → Accessibility → Auto Click Tool → ON
   - Settings → Apps → Auto Click Tool → Display over other apps → Allow
4. **Sử dụng ngay!**

## Tính năng chính

### 🎯 Tự động nhấp chuột
- Tạo và quản lý nhiều điểm nhấp
- Thiết lập tọa độ X, Y chính xác
- Cấu hình độ trễ và số lần lặp cho mỗi điểm
- Bật/tắt từng điểm nhấp độc lập

### 🎮 Điều khiển overlay
- Giao diện điều khiển nổi trên các ứng dụng khác
- Bắt đầu/dừng/tạm dừng auto click
- Thu nhỏ overlay khi không sử dụng
- Kéo thả để di chuyển vị trí

### ⚙️ Cài đặt linh hoạt
- Độ trễ mặc định có thể điều chỉnh
- Rung động khi nhấp (tùy chọn)
- Hiển thị/ẩn overlay
- Tự động khởi chạy

### 📊 Theo dõi trạng thái
- Hiển thị số lần đã nhấp
- Trạng thái dịch vụ realtime
- Thông báo foreground service

## Cấu trúc dự án

```
app/
├── src/main/kotlin/com/autoclick/tool/
│   ├── data/
│   │   ├── database/          # Room Database
│   │   ├── model/             # Data models
│   │   └── repository/        # Repository pattern
│   ├── di/                    # Dependency Injection (Hilt)
│   ├── presentation/          # UI (Jetpack Compose)
│   │   ├── theme/
│   │   └── viewmodel/
│   ├── service/               # Background services
│   │   ├── AutoClickAccessibilityService.kt
│   │   └── OverlayService.kt
│   └── utils/                 # Utility classes
└── src/main/res/              # Resources
```

## Công nghệ sử dụng

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: MVVM + Clean Architecture
- **DI**: Dagger Hilt
- **Database**: Room
- **Async**: Coroutines + Flow
- **Core**: Accessibility Services

## Yêu cầu hệ thống

- Android API 24+ (Android 7.0)
- Quyền Accessibility Service
- Quyền System Alert Window

## Cài đặt và chạy

### 1. Clone và build
```bash
git clone <repository-url>
cd Tool\ click
./gradlew build
```

### 2. Tạo APK
```bash
./gradlew assembleDebug
```

### 3. Cài đặt APK
```bash
./gradlew installDebug
```

### 4. Cấp quyền
1. Mở ứng dụng
2. Cấp quyền "Hiển thị trên ứng dụng khác"
3. Bật dịch vụ Accessibility trong Cài đặt > Trợ năng

## Hướng dẫn sử dụng

### Bước 1: Thêm điểm nhấp
1. Nhấn nút "Thêm điểm"
2. Nhập tên điểm và tọa độ X, Y
3. Thiết lập độ trễ (ms) và số lần lặp
4. Lưu điểm nhấp

### Bước 2: Khởi chạy auto click
1. Đảm bảo các điểm nhấp đã được bật
2. Nhấn nút "Bắt đầu"
3. Overlay điều khiển sẽ xuất hiện
4. Sử dụng overlay để điều khiển trong lúc chạy

### Bước 3: Điều khiển
- **Play/Pause**: Bắt đầu hoặc tạm dừng
- **Stop**: Dừng hoàn toàn
- **Minimize**: Thu nhỏ overlay

## Tính năng nâng cao

### Accessibility Service
- Sử dụng `dispatchGesture()` để thực hiện nhấp chính xác
- Hỗ trợ multi-touch và gesture phức tạp
- Tương thích với tất cả ứng dụng Android

### Database với Room
- Lưu trữ điểm nhấp persistent
- Backup và restore cấu hình
- Migration schema tự động

### Background Service
- Foreground service đảm bảo không bị kill
- Notification liên tục hiển thị trạng thái
- Battery optimization friendly

## Mở rộng tương lai

### 1. AI Recognition
```kotlin
// Tích hợp TensorFlow Lite để nhận diện UI elements
class AIClickDetector {
    fun detectButtons(screenshot: Bitmap): List<ClickTarget>
    fun recognizeText(region: Rect): String
}
```

### 2. Script Automation
```kotlin
// Hỗ trợ script tự động phức tạp
class AutomationScript {
    fun executeSequence(actions: List<AutoAction>)
    fun waitForElement(selector: String, timeout: Long)
}
```

### 3. Cloud Sync
```kotlin
// Đồng bộ cấu hình qua cloud
class CloudSyncManager {
    suspend fun uploadConfig(config: AutoClickConfig)
    suspend fun downloadConfig(): AutoClickConfig
}
```

## Monetization Model

### Free Version
- Tối đa 3 điểm nhấp
- Overlay cơ bản
- Tính năng core

### Premium Version
- Unlimited điểm nhấp
- AI recognition
- Cloud sync
- Advanced scripting
- Priority support

## Troubleshooting

### Lỗi thường gặp

1. **Auto click không hoạt động**
   - Kiểm tra dịch vụ Accessibility đã bật chưa
   - Đảm bảo có quyền overlay

2. **Overlay không hiển thị**
   - Cấp quyền "System Alert Window"
   - Restart ứng dụng

3. **Click không chính xác**
   - Kiểm tra lại tọa độ X, Y
   - Đảm bảo màn hình không thay đổi độ phân giải

### Debug
```bash
# Xem log accessibility service
adb logcat -s AutoClickService

# Monitor service status
adb shell dumpsys accessibility
```

## Contributing

1. Fork repository
2. Tạo feature branch
3. Commit changes
4. Push và tạo Pull Request

## License

MIT License - xem file LICENSE để biết thêm chi tiết.

## Support

- Email: support@autoclicktool.com
- Issues: GitHub Issues
- Documentation: Wiki

---

**Auto Click Tool** - Tự động hóa mọi thao tác trên Android một cách thông minh và hiệu quả! 🚀
