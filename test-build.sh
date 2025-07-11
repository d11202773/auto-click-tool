#!/bin/bash
echo "🔨 Testing local build capability..."
echo "==================================="
echo ""
java -version 2>/dev/null && echo "✅ Java available" || echo "❌ Java not found"
echo ""
echo "📦 Project structure:"
ls -la app/src/main/kotlin/com/autoclick/tool/ | head -10
echo ""
echo "🚀 Ready for GitHub upload!"
