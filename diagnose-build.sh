#!/bin/bash

echo "🔍 Diagnosing Android Auto Click Tool Build..."
echo "================================================"
echo ""

echo "📁 Project Structure Check:"
echo "✓ Checking critical files..."

# Check if essential files exist
files=(
    "build.gradle.kts"
    "settings.gradle.kts"
    "gradle.properties"
    "gradlew"
    "gradle/wrapper/gradle-wrapper.jar"
    "gradle/wrapper/gradle-wrapper.properties"
    "app/build.gradle.kts"
    "app/src/main/AndroidManifest.xml"
)

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file exists"
    else
        echo "❌ $file missing"
    fi
done

echo ""
echo "🔧 Gradle Wrapper Check:"
ls -la gradle/wrapper/

echo ""
echo "📝 AndroidManifest.xml validation:"
if grep -q "android:name" app/src/main/AndroidManifest.xml; then
    echo "✅ AndroidManifest.xml has application name"
else
    echo "❌ AndroidManifest.xml missing application name"
fi

echo ""
echo "🏗️ Build.gradle dependencies check:"
if grep -q "org.jetbrains.kotlin.android" app/build.gradle.kts; then
    echo "✅ Kotlin Android plugin found"
else
    echo "❌ Kotlin Android plugin missing"
fi

if grep -q "compose" app/build.gradle.kts; then
    echo "✅ Compose dependencies found"
else
    echo "❌ Compose dependencies missing"
fi

echo ""
echo "🌐 GitHub Remote Check:"
git remote -v

echo ""
echo "📊 Latest commit info:"
git log --oneline -5

echo ""
echo "🚀 GitHub Actions Status:"
echo "Visit: https://github.com/d11202773/auto-click-tool/actions"
echo ""
echo "🔍 Common Build Issues:"
echo "1. Missing gradle-wrapper.jar ✓ (Fixed)"
echo "2. Wrong Java version in workflow"
echo "3. Missing Android SDK components"
echo "4. Dependency version conflicts"
echo "5. Invalid AndroidManifest.xml"
echo ""
echo "📱 If build succeeds, APK will be available at:"
echo "https://github.com/d11202773/auto-click-tool/actions"
