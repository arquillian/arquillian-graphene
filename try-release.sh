#!/bin/bash
CONF="-P!selenium-session,!selenium-functional-test $*"
mvn clean release:prepare -DdryRun=true ${CONF}
mvn release:clean
