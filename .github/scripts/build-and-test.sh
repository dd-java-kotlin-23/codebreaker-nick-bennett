#!/bin/bash
#  Copyright 2026 CNM Ingenuity, Inc. All rights reserved.

set -e  # Exit on error

# --- Step 1: Build ---
echo "📋 Compiling main classes ..."
chmod +x ./gradlew && ./gradlew clean
if ! ./gradlew classes; then
  echo "❌ Implementation code failed to compile."
  echo "::error::Implementation code failed to compile."
  exit 1 # Fail the job here; nothing else can happen
fi

echo "::notice::Implementation code compiled successfully."
echo "✅ Implementation code compiled successfully."

echo "🔍 Checking tests ..."
if ! ./gradlew testClasses; then
  echo "❌ Test code failed to compile."
  echo "test_status=compile_fail" >> "$GITHUB_OUTPUT"
  exit 0
fi

if ! ./gradlew test --test-dry-run; then
  echo "⚠️ No tests found."
  echo "::warning::No tests found."
  exit 0
fi

if ! find build/test-results/test -name '*.xml' -print -quit | grep -q .; then
  echo "⚠️ No tests found."
  echo "::warning::No tests found."
  exit 0
fi

echo "🧪 Running tests..."
if ./gradlew test --continue; then
  echo "✅ All tests passed."
  echo "test_status=pass" >> "$GITHUB_OUTPUT"
else
  echo "❌ At least one test failed."
  echo "test_status=fail" >> "$GITHUB_OUTPUT"
fi
exit 0 # Always exit 0 to keep the job green if build passed