#!/usr/bin/env bash

echo "Starting upload to maven"
echo "ENCRYPTED_GPG_KEY_LOCATION=${ENCRYPTED_GPG_KEY_LOCATION}"
mkdir .travis
echo "${GPG_SECRET_KEYS_ENC}" | base64 --decode > ${ENCRYPTED_GPG_KEY_LOCATION}
openssl aes-256-cbc -K $encrypted_a2869fb015d7_key -iv $encrypted_a2869fb015d7_iv -in $ENCRYPTED_GPG_KEY_LOCATION -out $GPG_KEY_LOCATION -d
./gradlew properties -q | grep "version:" | awk '{print $2}'
export PROJECT_VERSION=$(./gradlew properties -q | grep "version:" | awk '{print $2}')
# Upload only snapshots to sonatype oss so it can make its way to maven central
./gradlew publishMavenJavaPublicationToMavenRepository

# Only non-snapshot can be pushed as maven releases
if [[ ! $(echo "${PROJECT_VERSION}" | grep "SNAPSHOT") ]]; then
  ./gradlew closeAndReleaseRepository
fi
echo "Update to maven completed"
