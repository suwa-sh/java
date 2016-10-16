#!/bin/bash
####################################################################################################
# production build
####################################################################################################
cd $(cd `dirname $0`; cd ..; pwd)
PROFILE=production


CMD="mvn clean package site:site -P ${PROFILE} -DPID=$$"
echo "---- ${CMD}"
${CMD}
retCode=$?
if [ ${retCode} -ne 0 ]; then
    exit ${retCode}
fi

echo
echo "NORMAL END"
