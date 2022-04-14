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
    filename=sys.argv[2]
    fileurl="*******\\"+filename #处理结果保存的地址

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
    ## 3. 关键词统计
    key_word = []
    for i in range(len(df1)):
        if df1['class01'].iloc[i]:
            key_word.append(df1['class01'].iloc[i])
        if df1['class02'].iloc[i]:
            key_word.append(df1['class02'].iloc[i])
    key_counter = Counter(key_word)
    # pprint(key_counter.most_common(10))
    key_df = pd.DataFrame.from_dict(key_counter, orient='index').reset_index()
    key_df.columns = ['word', 'num']
    key_df.sort_values(by='num', ascending=False, inplace=True)

#     ####################################################################
#     ## 4. 分词 结果
#     def chinese_word_cut(mytext):
#         if mytext:
#             return " ".join(jieba.cut(mytext))

#     df1["askCon_cutted"] = df1.askContent.apply(chinese_word_cut)
#     df1["ansCon_cutted"] = df1.ansContent.apply(chinese_word_cut)
#     ####################################################################
#     ## 5. 分词 词频统计
#     stopwords = pd.read_csv('stop_word.txt', encoding='utf8', names=['stopword'], index_col=False,
#                             quoting=csv.QUOTE_NONE)
#     stop_words = set(stopwords['stopword'].tolist())

#     ask_text = ""
#     ans_text = ""
#     for i in range(len(df1)):
#         di = df1.iloc[i]
#         if not pd.isna(di["askContent"]):
#             ask_text += di["askContent"]
#         if not pd.isna(di["ansContent"]):
#             ans_text += di["ansContent"]
#         if not pd.isna(di["ansContent2"]):
#             ans_text += di["ansContent2"]
#     words_ask = list(jieba.cut(ask_text))

#     ask_words = []
#     for i in words_ask:
#         if i not in stop_words:
#             ask_words.append(i)

#     ask_counter = Counter(ask_words)

#     ask_df = pd.DataFrame.from_dict(ask_counter, orient='index').reset_index()

#     ask_df.columns = ['word', 'num']
#     ask_df.sort_values(by='num', ascending=False, inplace=True)

# ##----------------------------------------------
#     words_ans = list(jieba.cut(ans_text))

#     ans_words = []
#     for i in words_ans:
#         if i not in stop_words:
#             ans_words.append(i)

#     ans_counter = Counter(ans_words)

#     ans_df = pd.DataFrame.from_dict(ans_counter, orient='index').reset_index()

#     if not ans_df.empty:
#         ans_df.columns = ['word', 'num']
#         ans_df.sort_values(by="num", inplace=True, ascending=False)
#     ####################################################################
#     ## 6. 主题词 词频统计
#     def tfidf(mytext):
#         if mytext:
#             return ana.tfidf(mytext, topK=3)

#     df1["tfidf_askCon_key_word"] = df1.askContent.apply(tfidf)
#     df1["tfidf_ansCon_key_word"] = df1.ansContent.apply(tfidf)

    # key_df.to_csv('key_df.csv')
    key_df.to_csv(fileurl,encoding="utf_8_sig")
    # key_df.to_csv(filename,encoding="utf_8_sig")
    return key_df.to_json()
    # , ask_df.to_json(), ans_df.to_json(), df1.to_json()

if __name__ == '__main__':
    key = dataprocess()
    # print(key)
    print("ok")