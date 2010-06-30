#!/bin/bash
mvn -f library/functional-test/pom.xml clean verify -DcontainerId=tomcat6x $*
