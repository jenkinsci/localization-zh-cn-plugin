#!/bin/sh
# current script needs env.sh to setup some environment variables
# use python to parse json result

target_file=target/localization-zh-cn.hpi

if [ ! -f "$(dirname "${BASH_SOURCE[0]}")/env.sh" ]; then
    echo 'we need the env.sh to setup vars'
    exit -1
fi

source $(dirname "${BASH_SOURCE[0]}")/env.sh

issuer=$(curl -u admin:$JENKINS_TOKEN $JENKINS_URL/crumbIssuer/api/json -s -o /dev/null -w %{http_code})
if [ "$issuer" == "200" ]; then
    export issuer=$(curl -u admin:$JENKINS_TOKEN $JENKINS_URL/crumbIssuer/api/json -s)
    issuer=$(python -c "import json;import os;issuer=os.getenv('issuer');issuer=json.loads(issuer);print issuer['crumb']")
else
    issuer=""
fi

curl -u admin:$JENKINS_TOKEN $JENKINS_URL/pluginManager/uploadPlugin -F "name=@$(dirname "${BASH_SOURCE[0]}")/$target_file" --header "Jenkins-Crumb: $issuer"
curl -u admin:$JENKINS_TOKEN $JENKINS_URL/restart -X POST --header "Jenkins-Crumb: $issuer"