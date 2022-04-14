import os
import time
# import scrapy
# from scrapy import cmdline
# from data_crawler.items import DataCrawlerItem
# import copy
# import re
import sys
if __name__ == '__main__':
    sql0 = sys.argv[1]
    askfilename0 = sys.argv[2]
    ansfilename0 = sys.argv[3]
    sql=" \""+sql0+"\""
    askfilename = " \"" + askfilename0 + "\""
    ansfilename = " \"" + ansfilename0 + "\""


    myrun="cd e:\\chuang\\processFunction\\ & \
    	e: & \
    	python  E:\chuang\processFunction\dp_2_wordcount.py"+sql+askfilename+ansfilename

    print(myrun)

    # run = "cd e:\\chuang\\processFunction\\ & \
    #     	e: & \
    #     	python  E:\chuang\processFunction\dp_2_wordcount.py \"SELECT DISTINCT * FROM test.item WHERE askTime>'2022-03-06' AND askTime<'2022-03-08 23:59' AND (city='成都' OR city='德阳' OR city='南充' OR city='省级政府部门')ORDER BY askTime; \" \"123_0310233438_askDf.csv\" \"123_0310233438_ansDf.csv\""
    # print(run)

    # cmd = "cd e:\\chuang\\data_crawler\\data_crawler\\spiders & \
    # 	e: & \
    # 	scrapy crawl sichuan -o sichuan.csv"

    os.system(myrun)
    print("myrun_ok")
    # time.sleep(100)
    # cmdline.execute('scrapy crawl sichuan'.split())