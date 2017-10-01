#!/bin/bash
#================================================================================
# build
#================================================================================
#--------------------------------------------------
# 変数定義
#--------------------------------------------------
DIR_SCRIPT=$(cd $(dirname $0); pwd)
DIR_LOG=${DIR_SCRIPT}/log
FILE_LOG=`basename $0 .sh`.log
PATH_LOG=${DIR_LOG}/${FILE_LOG}

readonly SONAR_URL="https://sonarcloud.io"
readonly SONAR_ORGANIZATION="suwa-sh-github"
readonly SONAR_EXCLUDES="src/test/**,**/gen/**,**/*Exception.java"


#--------------------------------------------------
# 事前処理
#--------------------------------------------------
cd ${DIR_SCRIPT}

if [ ! -d ${DIR_LOG} ]; then
    mkdir ${DIR_LOG}
fi

#--------------------------------------------------
# 主処理
#--------------------------------------------------
cd ..
if [[ "${SONAR_TOKEN}x" = "x" ]]; then
  echo "SONAR_TOKEN が定義されていません。sonar解析をスキップします。"
  EXEC_CMD="mvn clean install site -DPID=$$"
  echo ${EXEC_CMD} | tee -a ${PATH_LOG}
  ${EXEC_CMD} 2>&1 | tee -a ${PATH_LOG} 2>&1

else
  EXEC_CMD="mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install site sonar:sonar -DPID=$$"
  echo ${EXEC_CMD} | tee -a ${PATH_LOG}
  ${EXEC_CMD}                                                                                      \
    -Dsonar.host.url=${SONAR_URL}                                                                  \
    -Dsonar.organization=${SONAR_ORGANIZATION}                                                     \
    -Dsonar.login=${SONAR_TOKEN}                                                                   \
    -Dsonar.exclusions="${SONAR_EXCLUDES}" 2>&1 | tee -a ${PATH_LOG} 2>&1
fi
retcode=$?
if [ ${retcode} -ne 0 ]; then
    echo build failure ${PATH_LOG} | tee -a ${PATH_LOG}
    exit 1
fi


#--------------------------------------------------
# 事後処理
#--------------------------------------------------
exit 0
