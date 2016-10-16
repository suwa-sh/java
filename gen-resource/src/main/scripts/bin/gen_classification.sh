#!/bin/bash
#===================================================================================================
# 区分リソース生成スクリプト
#
# 概要
#   指定javaプロジェクトの区分定義ファイルから、javaソース、propertiesファイルを生成します。
#
# 前提
#   ${dir_proj}/src/main/scripts/classification/def/ 配下に、区分定義jsonファイル群が配置されていること
#
# 引数
#   1: javaプロジェクトディレクトリ
#   2: DDプロパティファイル名
#
# 出力
#   javaファイル      : ${dir_proj}/src/main/java
#   propertiesファイル: ${dir_proj}/src/main/resource
#===================================================================================================
cd $(cd ..; pwd)

# 定数
readonly FILENAME_PROP_DEFAULT="dd_source.properties"

# Javaプロジェクトディレクトリ
dir_proj="$1"
dir_input="${dir_proj}/src/main/scripts/classification/def"
dir_output_java="${dir_proj}/src/main/java"
dir_output_resource="${dir_proj}/src/main/resources"

# DDプロパティファイル名
filename_prop="$2"
if [ "${filename_prop}" = "" ]; then
    filename_prop="${FILENAME_PROP_DEFAULT}"
fi

# 実行
CLASSPATH=.:./config:./lib/*:${CLASSPATH}
echo "java -cp ${CLASSPATH} -Dpid=$$ me.suwash.gen.classification.ClassificationGenerator ${dir_input} ${dir_output_java} ${dir_output_resource} ${filename_prop}"
java                                                                                                \
    -cp "${CLASSPATH}"                                                                              \
    -DPID=$$                                                                                        \
    me.suwash.gen.classification.ClassificationGenerator                                            \
        "${dir_input}"                                                                              \
        "${dir_output_java}"                                                                        \
        "${dir_output_resource}"                                                                    \
        "${filename_prop}"
