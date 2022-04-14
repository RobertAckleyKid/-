import pymysql
import pandas as pd
import numpy as np
from collections import Counter
from pprint import pprint
import jieba
import jieba.analyse as ana
import csv
import sys
from aip import AipNlp


def dataprocess():
    sql = sys.argv[1]

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
    
    APP_ID = '25874980'
    API_KEY = 'P9IzEZ9t7fTCDVBjN1atKr11'
    SECRET_KEY = 'P82HtoiOwcFsKc7xew70zVQoLZOyGMcW'
    client = AipNlp(APP_ID, API_KEY, SECRET_KEY)
    ans = df1['askContent'].apply(client.sentimentClassify)
    allNums = 0
    posNums = 0
    midNums = 0
    negNums = 0
    for i in ans:
        if 'items' in i.keys():
            allNums += 1
            if i['items'][0]['positive_prob'] >= 0.6:
                posNums += 1
            elif i['items'][0]['positive_prob'] >= 0.3:
                midNums += 1
            else:
                negNums += 1
    res = [allNums, posNums, midNums, negNums]
    #所有、正向、中性、消极
    resDF = pd.DataFrame(res)
    resDF.to_csv("sentimentAnalysis.csv")
    return  resDF.to_json()
if __name__ == '__main__':
    
    all = dataprocess()
    print(all)

