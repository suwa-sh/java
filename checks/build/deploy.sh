#!/bin/sh
#================================================================================
#================================================================================
#--------------------------------------------------
# 変数定義
#--------------------------------------------------
DIR_SCRIPT=`cd $(dirname $0); pwd`
DIR_LOG=${DIR_SCRIPT}/log
FILE_LOG=`basename $0 .sh`.log
PATH_LOG=${DIR_LOG}/${FILE_LOG}

EXEC_CMD="mvn clean site deploy"


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
echo ${EXEC_CMD} | tee -a ${PATH_LOG}
${EXEC_CMD} 2>&1 | tee -a ${PATH_LOG}
if [ $? -ne 0 ]; then
    echo build failure ${PATH_LOG} | tee -a ${PATH_LOG}
    exit 1
fi


#--------------------------------------------------
# 事後処理
#--------------------------------------------------
exit 0
