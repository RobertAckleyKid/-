System
   --src
      --com文件夹--- 实现发送邮件验证功能
      --login文件夹--- 实现登录功能
      --process文件夹--- 实现数据处理功能
      --select文件夹--- 实现数据选择功能
      --pythonTest文件夹---实现调用python文件功能
   --web
      --maintain
         --login--- 登陆及注册界面
         --main--- 信息筛选界面
         --dateProcessing--- 处理信息界面
         --visualization--- 处理结果可视化界面
         --management--- 管理反馈界面

processFunction  数据处理python程序
processFile   处理结果保存


程序运行注意：

1、
DbRemote.java、Db.java 设置数据库链接，链接自己的数据库
//以下需修改，链接自己的数据库			
String connStr="jdbc:mysql://IP地址/"+dbName+"?user=账号&password=密码&useUnicode=true&characterEncoding=UTF-8";

2、
JavaMailUtil.java 设置自己的邮箱与授权码
// 发件人的邮箱-修改为自己的
public static String emailAccount = "*******";	
// 发件人邮箱授权码-修改为自己的
public static String emailPassword = "*******";

3、processFunction文件夹中的数据处理文件均需连接自己的数据库

 host = "***"  # 数据库的ip地址
 user = "***"  # 数据库的账号
 password = "***"  # 数据库的密码
 port = 3306  # mysql数据库通用端口号

 