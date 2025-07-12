#!/bin/bash

echo "🔧 Auto Click Tool - Manual Build Trigger"
echo "========================================"
echo ""

echo "📊 Current Status:"
echo "Latest commit: $(git log --oneline -1)"
echo "Remote status: $(git status | head -2 | tail -1)"
echo ""

echo "🚀 Available Manual Workflows:"
echo ""
echo "1. ⚡ Minimal Test Build (RECOMMENDED)"
echo "   URL: https://github.com/d11202773/auto-click-tool/actions/workflows/minimal-test.yml"
echo "   → Most reliable, step-by-step debugging"
echo ""
echo "2. 🔄 Simple APK Build"
echo "   URL: https://github.com/d11202773/auto-click-tool/actions/workflows/simple-build.yml"
echo "   → Quick APK build"
echo ""
echo "3. 🐛 Debug Build"
echo "   URL: https://github.com/d11202773/auto-click-tool/actions/workflows/debug-build.yml"
echo "   → Full debugging info"
echo ""

echo "📋 Manual Trigger Steps:"
echo "1. Go to one of the URLs above"
echo "2. Click 'Run workflow' button"
echo "3. Keep 'Branch: main' selected"
echo "4. Click 'Run workflow' green button"
echo "5. Wait 3-5 minutes for completion"
echo "6. If ✅ success → Download APK from Artifacts"
echo "7. If ❌ error → Click to view logs"
echo ""

echo "🎯 Quick Actions:"
echo "• All workflows: https://github.com/d11202773/auto-click-tool/actions"
echo "• Latest runs: https://github.com/d11202773/auto-click-tool/actions"
echo ""

echo "💡 TIP: Start with 'Minimal Test Build' - it has the most debugging info!"
echo ""

# Open browser if possible
if command -v open &> /dev/null; then
    echo "🌐 Opening Minimal Test Build workflow..."
    open "https://github.com/d11202773/auto-click-tool/actions/workflows/minimal-test.yml"
else
    echo "📋 Copy this URL: https://github.com/d11202773/auto-click-tool/actions/workflows/minimal-test.yml"
fi
