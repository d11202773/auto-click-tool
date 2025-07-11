#!/bin/bash

echo "🚀 AUTO CLICK TOOL - QUICK APK GENERATOR"
echo "========================================"
echo ""

# Prompt for GitHub username
read -p "📝 Nhập GitHub username của bạn: " username

if [ -z "$username" ]; then
    echo "❌ Vui lòng nhập username!"
    exit 1
fi

echo ""
echo "🔗 Repository sẽ tạo: https://github.com/$username/auto-click-tool"
echo ""
echo "📋 BƯỚC 1: Tạo repository"
echo "   → Mở: https://github.com/new"
echo "   → Tên: auto-click-tool"
echo "   → Public repository"
echo "   → Create repository"
echo ""

read -p "✅ Đã tạo repository chưa? (y/n): " created

if [[ $created == "y" || $created == "Y" ]]; then
    echo ""
    echo "🚀 BƯỚC 2: Upload code..."
    
    # Add remote
    git remote add origin "https://github.com/$username/auto-click-tool.git" 2>/dev/null || git remote set-url origin "https://github.com/$username/auto-click-tool.git"
    
    # Push to GitHub
    echo "📤 Pushing to GitHub..."
    git branch -M main
    git push -u origin main
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "🎉 UPLOAD THÀNH CÔNG!"
        echo ""
        echo "📱 BƯỚC 3: Tải APK"
        echo "   → Vào: https://github.com/$username/auto-click-tool"
        echo "   → Tab 'Actions'"
        echo "   → Chờ build xong (5 phút)"
        echo "   → Download 'auto-click-tool-debug'"
        echo ""
        echo "⏱️  APK sẽ sẵn sàng trong 5-7 phút!"
    else
        echo "❌ Upload thất bại. Kiểm tra lại repository URL."
    fi
else
    echo "📋 Hãy tạo repository trước rồi chạy lại script này!"
fi
