#!/usr/bin/env bash

export GPG_KEY_LOCATION=$(pwd)/encrypted.key
echo "Starting upload to maven"
echo "${GPG_SECRET_KEYS_ENC}" | base64 --decode > $GPG_KEY_LOCATION
./gradlew properties -q | grep "version:" | awk '{print $2}'
export PROJECT_VERSION=$(./gradlew properties -q | grep "version:" | awk '{print $2}')
# Upload only snapshots to sonatype oss so it can make its way to maven central
./gradlew publishMavenJavaPublicationToMavenRepository

# Only non-snapshot can be pushed as maven releases
if [[ ! $(echo "${PROJECT_VERSION}" | grep "SNAPSHOT") ]]; then
  ./gradlew closeAndReleaseRepository
fi