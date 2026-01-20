#!/usr/bin/env bash

export GPG_KEY_LOCATION=$(pwd)/encrypted.key
echo "Starting upload to maven"
echo "${GPG_SECRET_KEYS_ENC}" | base64 --decode > $GPG_KEY_LOCATION

./gradlew properties -q | grep "version:" | awk '{print $2}'
export PROJECT_VERSION=$(./gradlew properties -q | grep "version:" | awk '{print $2}')

# Publish to Sonatype (Central Portal via OSSRH Staging API)
./gradlew publishToSonatype

# Only non-snapshot versions should be closed and released.
if [[ ! $(echo "${PROJECT_VERSION}" | grep "SNAPSHOT") ]]; then
  ./gradlew closeAndReleaseSonatypeStagingRepository
fi