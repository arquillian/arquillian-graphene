#!/bin/bash
mvn clean install -P'!selenium-session,!selenium-functional-test' $*
