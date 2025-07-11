#!/bin/bash

echo "🚀 Checking GitHub Actions build status..."
echo "Repository: https://github.com/d11202773/auto-click-tool"
echo ""

# Check if git remote exists
if git remote -v | grep -q "github.com/d11202773/auto-click-tool"; then
    echo "✅ Git remote configured correctly"
else
    echo "❌ Git remote not configured"
    exit 1
fi

echo ""
echo "📱 Once build completes successfully, you can download APK from:"
echo "https://github.com/d11202773/auto-click-tool/actions"
echo ""
echo "Or wait for a release at:"
echo "https://github.com/d11202773/auto-click-tool/releases"
echo ""
echo "🔄 Current status can be checked at:"
echo "https://github.com/d11202773/auto-click-tool/actions"
echo ""
echo "⏱️  Build usually takes 5-10 minutes..."
echo ""
echo "📥 To get APK directly when ready:"
echo "1. Go to Actions tab"
echo "2. Click on the latest successful workflow"
echo "3. Download 'app-release' artifact"
echo "4. Extract ZIP to get APK file"
