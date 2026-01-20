#!/usr/bin/env bash

export GPG_KEY_LOCATION=$(pwd)/encrypted.key
echo "Starting upload to maven"
echo "${GPG_SECRET_KEYS_ENC}" | base64 --decode > $GPG_KEY_LOCATION

# Fail fast with actionable errors if required secrets are missing.
# Prefer SONATYPE_CENTRAL_* (current convention), fall back to SONATYPE_* (legacy).
export SONATYPE_USERNAME="${SONATYPE_CENTRAL_USERNAME:-${SONATYPE_USERNAME}}"
export SONATYPE_PASSWORD="${SONATYPE_CENTRAL_PASSWORD:-${SONATYPE_PASSWORD}}"
if [[ -z "${SONATYPE_USERNAME}" || -z "${SONATYPE_PASSWORD}" ]]; then
  echo "ERROR: Missing Sonatype credentials. Set SONATYPE_CENTRAL_USERNAME/SONATYPE_CENTRAL_PASSWORD (preferred) or SONATYPE_USERNAME/SONATYPE_PASSWORD (legacy)." >&2
  exit 1
fi

./gradlew properties -q | grep "version:" | awk '{print $2}'
export PROJECT_VERSION=$(./gradlew properties -q | grep "version:" | awk '{print $2}')
# Upload only snapshots to sonatype oss so it can make its way to maven central
./gradlew publishMavenJavaPublicationToMavenRepository

# Only non-snapshot can be pushed as maven releases
if [[ ! $(echo "${PROJECT_VERSION}" | grep "SNAPSHOT") ]]; then
  # The nexus staging plugin task names have changed across versions/configs.
  # Prefer the combined task if present, otherwise fall back to close + release.
  if ./gradlew -q tasks --all | grep -q "^closeAndReleaseRepository"; then
    ./gradlew closeAndReleaseRepository
  else
    if ./gradlew -q tasks --all | grep -q "^closeRepository"; then
      ./gradlew closeRepository
    fi
    if ./gradlew -q tasks --all | grep -q "^releaseRepository"; then
      ./gradlew releaseRepository
    fi
  fi
fi