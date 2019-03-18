#!/bin/sh

mvn clean package -DskipTests
curl -u admin:11a2c2a54f98e8eae81786d304f6c08c9c http://jenkins.k8s.surenpi.com/pluginManager/uploadPlugin -F "name=@target/localization-zh-cn.hpi"
curl -u admin:11a2c2a54f98e8eae81786d304f6c08c9c http://jenkins.k8s.surenpi.com/restart -X POST
