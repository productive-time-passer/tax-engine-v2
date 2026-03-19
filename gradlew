#!/usr/bin/env bash
set -euo pipefail

# Lightweight wrapper for environments where the Gradle wrapper JAR is unavailable.
# It also avoids known Gradle script-compilation failures with Java 25+ by preferring JDK 21.

if ! command -v gradle >/dev/null 2>&1; then
  echo "Error: 'gradle' is not installed or not on PATH." >&2
  echo "Install Gradle or add a standard Gradle wrapper (gradle/wrapper/*)." >&2
  exit 127
fi

current_java_version="$(java -version 2>&1 | head -n1 | sed -E 's/.*version \"([0-9]+).*/\1/')"

if [[ "${current_java_version:-0}" -ge 25 ]]; then
  jdk_candidates=(
    "${JAVA21_HOME:-}"
    "/root/.local/share/mise/installs/java/21"
    "/root/.local/share/mise/installs/java/21.0.2"
    "/usr/lib/jvm/java-21-openjdk-amd64"
    "/usr/lib/jvm/temurin-21-jdk-amd64"
  )

  for jdk_home in "${jdk_candidates[@]}"; do
    if [[ -n "$jdk_home" && -x "$jdk_home/bin/java" ]]; then
      export JAVA_HOME="$jdk_home"
      export PATH="$JAVA_HOME/bin:$PATH"
      break
    fi
  done
fi

exec gradle "$@"
