#!/bin/bash
mvn -f library/functional-test/test/pom.xml clean verify -DcontainerId=tomcat6x -Dbrowser="*firefox" $*
