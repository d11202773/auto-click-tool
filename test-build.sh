#!/bin/bash

echo "� Test Build Tool - Local Build Test"
echo "====================================="
echo ""

echo "📋 This script helps test the Android build locally"
echo "Note: Requires Java JDK 17+ to be installed"
echo ""

# Check if Java is available
if command -v java &> /dev/null; then
    echo "✅ Java found: $(java -version 2>&1 | head -n1)"
else
    echo "❌ Java not found. Install Java JDK 17+ to test build locally."
    echo ""
    echo "� GitHub Actions Status (recommended):"
    echo "https://github.com/d11202773/auto-click-tool/actions"
    echo ""
    echo "📱 Available workflows:"
    echo "1. Build Android APK - Main build workflow"
    echo "2. Debug Build - Troubleshooting workflow"
    echo ""
    echo "💡 Manual trigger debug workflow at:"
    echo "https://github.com/d11202773/auto-click-tool/actions/workflows/debug-build.yml"
    exit 1
fi

echo ""
echo "🧪 Testing Gradle wrapper..."
if ./gradlew --version; then
    echo "✅ Gradle wrapper working"
else
    echo "❌ Gradle wrapper failed"
    exit 1
fi

echo ""
echo "🧹 Cleaning project..."
if ./gradlew clean; then
    echo "✅ Clean successful"
else
    echo "❌ Clean failed"
    exit 1
fi

echo ""
echo "🏗️ Building APK..."
echo "This may take 5-10 minutes..."
if ./gradlew assembleRelease --stacktrace; then
    echo ""
    echo "🎉 BUILD SUCCESSFUL!"
    echo ""
    echo "📱 APK Location:"
    find app/build/outputs/apk -name "*.apk" -type f
    echo ""
    echo "📋 APK Info:"
    ls -lh app/build/outputs/apk/release/*.apk 2>/dev/null || echo "No release APK found"
else
    echo ""
    echo "❌ BUILD FAILED"
    echo ""
    echo "� Check the error above and try:"
    echo "1. Manual debug workflow: https://github.com/d11202773/auto-click-tool/actions/workflows/debug-build.yml"
    echo "2. Check GitHub Actions logs: https://github.com/d11202773/auto-click-tool/actions"
    exit 1
fi
