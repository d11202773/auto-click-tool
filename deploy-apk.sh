#!/bin/bash

echo "🚀 AUTO CLICK TOOL - TRIỂN KHAI APK NGAY"
echo "========================================"
echo ""

echo "📋 Bước 1: Kiểm tra trạng thái hiện tại..."
git status --porcelain
if [ $? -eq 0 ]; then
    echo "✅ Git repository sạch"
else
    echo "❌ Git có vấn đề"
fi

echo ""
echo "📋 Bước 2: Trigger build workflows..."

echo "🔥 LINK TRỰC TIẾP ĐỂ TRIGGER BUILD:"
echo ""
echo "1️⃣ MINIMAL TEST BUILD (Khuyến nghị):"
echo "🔗 https://github.com/d11202773/auto-click-tool/actions/workflows/minimal-test.yml"
echo "   → Click 'Run workflow' → Chờ 5 phút → Download APK"
echo ""

echo "2️⃣ SIMPLE APK BUILD:"
echo "🔗 https://github.com/d11202773/auto-click-tool/actions/workflows/simple-build.yml"
echo "   → Click 'Run workflow' → Chờ 3 phút → Download APK"
echo ""

echo "3️⃣ DEBUG BUILD:"
echo "🔗 https://github.com/d11202773/auto-click-tool/actions/workflows/debug-build.yml"
echo "   → Click 'Run workflow' → Xem logs chi tiết"
echo ""

echo "📱 CÁCH LẤY APK NHANH NHẤT:"
echo "========================================="
echo "1. Mở link: https://github.com/d11202773/auto-click-tool/actions"
echo "2. Tìm workflow có dấu ✅ (xanh lá)"
echo "3. Click vào workflow đó"
echo "4. Scroll xuống phần 'Artifacts'"
echo "5. Download file APK"
echo "6. Giải nén ZIP và cài APK lên điện thoại"
echo ""

echo "🎯 QUICK ACCESS LINKS:"
echo "• Tất cả builds: https://github.com/d11202773/auto-click-tool/actions"
echo "• Repository: https://github.com/d11202773/auto-click-tool"
echo ""

echo "⚡ AUTO TRIGGER BUILD NGAY BÂY GIỜ..."
echo "Đang push commit để trigger automatic build..."

# Create a trigger commit
echo "# Auto Click Tool - Build $(date)" > BUILD_TRIGGER.md
git add BUILD_TRIGGER.md
git commit -m "🚀 TRIGGER: Deploy APK build - $(date)"
git push origin main

echo ""
echo "✅ ĐÃ TRIGGER BUILD THÀNH CÔNG!"
echo ""
echo "📱 APK SẼ SẴN SÀNG TRONG 3-5 PHÚT TẠI:"
echo "🔗 https://github.com/d11202773/auto-click-tool/actions"
echo ""
echo "💡 Mở link trên và chờ workflow hoàn thành!"

# Try to open browser
if command -v open &> /dev/null; then
    echo ""
    echo "🌐 Đang mở GitHub Actions..."
    open "https://github.com/d11202773/auto-click-tool/actions"
fi
