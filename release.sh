#!/bin/bash
read -p 'Enter release version: ' RELEASE
read -p 'Enter new development version: ' DEVELOPMENT
CONF="--batch-mode -Dtag=${RELEASE} -DreleaseVersion=${RELEASE} -DdevelopmentVersion=${DEVELOPMENT}"
echo "Configuration: ${CONF}"
read -p 'Press ENTER to dry run...'
mvn release:prepare -DdryRun=true ${CONF} || exit 1
read -p 'Press ENTER to clean...'
mvn release:clean
read -p 'Press ENTER to prepare...'
mvn clean release:prepare ${CONF} || exit 1
read -p 'Press ENTER to perform...'
mvn release:perform || exit 1
