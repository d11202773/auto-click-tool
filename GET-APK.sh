#!/bin/bash

echo "📱 Auto Click Tool - APK Status Checker"
echo "======================================="
echo ""

echo "� GitHub Repository: https://github.com/d11202773/auto-click-tool"
echo ""

echo "📋 Current Status:"
echo "Latest commit: $(git log --oneline -1)"
echo "Last push: $(git log -1 --format='%cd' --date=relative)"
echo ""

echo "🚀 GitHub Actions Workflows:"
echo "1. Main Build APK: https://github.com/d11202773/auto-click-tool/actions/workflows/build-apk.yml"
echo "2. Debug Build: https://github.com/d11202773/auto-click-tool/actions/workflows/debug-build.yml"
echo ""

echo "📥 To get APK (if build successful):"
echo "1. Go to: https://github.com/d11202773/auto-click-tool/actions"
echo "2. Click on latest successful ✅ workflow run"
echo "3. Scroll down to 'Artifacts' section"
echo "4. Download 'app-release' or 'auto-click-tool-latest'"
echo "5. Extract ZIP file to get APK"
echo ""

echo "🔄 Manual Trigger Options:"
echo "• Main Build: git push origin main (automatic)"
echo "• Debug Build: Manual trigger at GitHub Actions"
echo ""

echo "⚡ Quick Links:"
echo "🌟 All Actions: https://github.com/d11202773/auto-click-tool/actions"
echo "📦 Releases: https://github.com/d11202773/auto-click-tool/releases"
echo ""

echo "💡 Tip: If you see ✅ next to a workflow run, APK is ready!"
echo "💡 If you see ❌, click to view error details"
echo ""

# Check if we can open browser
if command -v open &> /dev/null; then
    echo "🌐 Opening GitHub Actions in browser..."
    open "https://github.com/d11202773/auto-click-tool/actions"
elif command -v xdg-open &> /dev/null; then
    echo "🌐 Opening GitHub Actions in browser..."
    xdg-open "https://github.com/d11202773/auto-click-tool/actions"
else
    echo "📋 Copy this link to browser: https://github.com/d11202773/auto-click-tool/actions"
fi
echo "   - Chờ 3-5 phút để build hoàn thành"
echo "   - APK sẽ sẵn sàng tải về"
echo ""
echo "4️⃣ TẢI APK:"
echo "   - Vào tab 'Actions' trong repository"
echo "   - Chọn build có dấu ✅ (xanh)"
echo "   - Download 'auto-click-tool-debug'"
echo "   - Giải nén và cài APK"
echo ""
echo "🚀 HOẶC TẠO RELEASE:"
echo "   - Vào tab 'Actions'"
echo "   - Chọn 'Release APK'"
echo "   - Click 'Run workflow'"
echo "   - Nhập version (vd: v1.0.0)"
echo "   - APK sẽ có trong Releases"
echo ""
echo "📱 APK SẴN SÀNG SỬ DỤNG!"
echo "========================="
echo "✅ Tự động click chính xác"
echo "✅ Overlay điều khiển"  
echo "✅ Quản lý nhiều điểm click"
echo "✅ Giao diện hiện đại"
echo "✅ Android 7.0+"
echo ""
echo "💡 Lưu ý: Nhớ cấp quyền Accessibility và Overlay sau khi cài!"
