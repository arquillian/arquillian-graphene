#!/bin/bash
mvn clean install -P'!selenium-session,!functional-test'
