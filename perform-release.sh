#!/bin/bash
CONF="-P!selenium-session,!selenium-functional-test $*"
mvn clean release:prepare ${CONF}
mvn release:perform
