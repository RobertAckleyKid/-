import pymysql
import pandas as pd
import numpy as np
from collections import Counter
from pprint import pprint
import jieba
import jieba.analyse as ana
import csv
import sys

def dataprocess():
    sql = sys.argv[1]
    filename = sys.argv[2]
    fileurl = "****\\" + filename #处理结果保存的地址

    host = "***"  # 数据库的ip地址
    user = "***"  # 数据库的账号
    password = "***"  # 数据库的密码
    port = 3306  # mysql数据库通用端口号

    mysql = pymysql.connect(host=host, user=user, password=password, port=port)

    # 2.新建个查询页面
    cursor = mysql.cursor()

    # 3编写sql
    # sql = 'select * from test.item'

    # 4.执行sql
    cursor.execute(sql)

    # 5.查看结果
    results = cursor.fetchall()  # 用于返回多条数据

    # 6.关闭查询
    cursor.close()

    # 关闭数据库
    mysql.close()

    ####################################################################
    ## 2. 转换为dataframe 并进行数据处理

    df1 = pd.DataFrame(list(results),
                       columns=['name', 'class01', 'class02', 'city', 'county',
                                'askContent', 'askLen', 'askTime', 'ansOrNot', 'ansDpt',
                                'ansContent', 'ansLen', 'ansTime', 'ansDpt2', 'ansContent2',
                                'ansLen2', 'ansTime2', 'scUrl'])

    # df1.head
    df1.replace({'None': None}, inplace=True)


    ####################################################################
    ## 4. 分词 结果
    def chinese_word_cut(mytext):
        if mytext:
            return " ".join(jieba.cut(mytext))

    df1["askCon_cutted"] = df1.askContent.apply(chinese_word_cut)
    df1["ansCon_cutted"] = df1.ansContent.apply(chinese_word_cut)
    ####################################################################
    ## 6. 主题词 词频统计
    def tfidf(mytext):
        if mytext:
            return ana.tfidf(mytext, topK=3)

    df1["tfidf_askCon_key_word"] = df1.askContent.apply(tfidf)
    df1["tfidf_ansCon_key_word"] = df1.ansContent.apply(tfidf)

    
    df1.to_csv(fileurl,encoding="utf_8_sig")
    # return  df1.to_json()
    return  df1
if __name__ == '__main__':
    all = dataprocess()
    # print(all)
    print("ok")

