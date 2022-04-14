package pythonTest.dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class ObjectDao {
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"][device/dao/Db]"+msg);
	}

	//获取当前目录@gl
    public String getlocaldir() throws IOException {
		    File directory = new File("");//参数为空
			String dir = directory.getCanonicalPath() ;
			System.out.println(dir);
			return dir;
	}
	public void getPythonRes(Data data, JSONObject json) throws Exception{
		// TODO Auto-generated method stub
		Process proc;
		List jsonList = new ArrayList();
		try {

//			String str="cmd /c python  C:/chuang/"+data.getParam().getString("file");//本地正确版
			String str="python  E:/chuang/"+data.getParam().getString("file");//原始版--实测可行
//			String str="C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python310\\python.exe  C:/chuang/"+data.getParam().getString("file");//原始改进版--实测可行


			proc = Runtime.getRuntime().exec(str);// 执行py文件
			//用输入输出流来截取结果
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			String line = null;
			String message=new String();
			int flag=0;
			while ((line = in.readLine()) != null) {
				flag=1;
				System.out.println(line);
				message+=line;
				message+="\n";
				jsonList.add(line);
			}

			System.out.println(message);
			System.out.println(jsonList);
			//返回json
			if(flag==1){
				json.put("content","有");
			}else {
				json.put("content","无");
			}
			int resultCode = 0;
			json.put("str",str);
			json.put("result_code",resultCode);
			json.put("aaData",jsonList);
			//json设置完毕

			//收尾
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			json.put("wrong",e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			json.put("wrong",e);
		}
	}


	public void getReptile(Data data, JSONObject json) throws Exception{
		PythonInterpreter interpreter = new PythonInterpreter();
		//TODO 直接执行python代码
		   interpreter.exec("a='hello world22'; ");
		   interpreter.exec("print a;");
        //TODO  直接调用python脚本
		   //interpreter.execfile("D:\\javaPythonFile.py");
		//TODO  调用python中的函数
		System.out.println("调用python中的函数");
		   //PythonInterpreter interpreter = new PythonInterpreter();
		   interpreter.execfile("E:\\chuang\\add.py");

		   // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
		   PyFunction pyFunction = interpreter.get("add", PyFunction.class);
		   int a = 5, b = 10;
		   //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
		   PyObject pyobj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
		   System.out.println("the anwser is: " + pyobj);
		// TODO 使用Runtime.getRuntime()执行python脚本文件 --较上一个直接调用脚本，推荐此方法
//		Process proc;
//		List jsonList = new ArrayList();
		int resultCode = 1;
		try {
//			String str="cmd /c python  C:/chuang/"+data.getParam().getString("file");//本地正确版
			String str="python  E:\\chuang\\data_crawler\\data_crawler\\spiders\\"+data.getParam().getString("file");//原始版--实测可行
//			String str="cmd /c start E:\\chuang\\run.bat";//原始版--实测可行
//			String str="C:\\Users\\Administrator\\AppData\\Local\\Programs\\Python\\Python310\\python.exe  C:/chuang/"+data.getParam().getString("file");//原始改进版--实测可行

			System.out.println(str);
//			proc = Runtime.getRuntime().exec(str);// 执行py文件
			Runtime.getRuntime().exec(str);// 执行py文件
			resultCode=0;
			json.put("str",str);
			json.put("pythonRes",pyobj);

			json.put("result_code",resultCode);
			//json设置完毕

			//收尾
		} catch (IOException e) {
			e.printStackTrace();
			json.put("wrong",e);
		}
	}

	public void getProcess1(Data data, JSONObject json) throws Exception{
		// TODO 使用Runtime.getRuntime()执行python脚本文件 --较上一个直接调用脚本，推荐此方法
		Process proc;
		List jsonList = new ArrayList();

		//设置文件名称 用户名-日期-处理.csv
		String username=data.getParam().getString("username");
		Date date = new Date();//获取当前的日期
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");//设置日期格式
		String datestr = df.format(date);//获取String类型的时间
		String fileName="\""+username+"_"+datestr+"_keyDf.csv\"";
		String fileName1=username+"_"+datestr+"_keyDf.csv";
		System.out.println(fileName);

		//
		String pythonSql=data.getParam().getString("pythonSql");

		//设置cmd内容

//		String str1="python  E:\\chuang\\processFunction\\dp_1_key_words.py ";//原始版--实测可行
		String str="python  E:\\chuang\\processFunction\\dp_1_key_words.py "+"\""+pythonSql+"\" "+ fileName;
		System.out.println(str);

		proc = Runtime.getRuntime().exec(str);// 执行py文件
		//用输入输出流来截取结果
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GB2312"));
		String line = null;
		String message=new String();
		int flag=0;
		while ((line = in.readLine()) != null) {
			flag=1;
//			System.out.println(line);
//			message+=line;
//			message+="\n";
			jsonList.add(line);
		}
//		System.out.println(message);
		System.out.println(jsonList);
		//返回json
		if(flag==1){
			json.put("content","有");
		}else {
			json.put("content","无");
		}
		int resultCode = 0;
//		json.put("str",str);
		json.put("fileName",fileName1);
		json.put("result_code",resultCode);
		json.put("aaData",jsonList);
		//json设置完毕
		//收尾
		in.close();
		proc.waitFor();
		//json设置完毕
		//收尾
	}


	public void getProcess2(Data data, JSONObject json) throws Exception{
		Process proc;
		List jsonList = new ArrayList();

		//设置文件名称 用户名-日期-处理.csv
		String username=data.getParam().getString("username");
		Date date = new Date();//获取当前的日期
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");//设置日期格式
		String datestr = df.format(date);//获取String类型的时间

		//askDf
		String askfileName="\""+username+"_"+datestr+"_askDf.csv\"";
		String askfileName1=username+"_"+datestr+"_askDf.csv";
		System.out.println(askfileName);
		//ansDf
		String ansfileName="\""+username+"_"+datestr+"_ansDf.csv\"";
		String ansfileName1=username+"_"+datestr+"_ansDf.csv";
		System.out.println(ansfileName);

		//获取sql
		String pythonSql=data.getParam().getString("pythonSql");

		//设置cmd内容
//		String str="python  E:\\chuang\\processFunction\\dp_2_wordcount.py "+"\""+pythonSql+"\" "+ askfileName+" "+ansfileName;
		String str="python  E:\\chuang\\processFunction\\myrun.py "+"\""+pythonSql+"\" "+ askfileName+" "+ansfileName;
		System.out.println(str);
        //cmd /k start   运行时打开框，可以来调试
		String str1=" python  E:\\chuang\\processFunction\\myrun.py";
		proc = Runtime.getRuntime().exec(str);// 执行py文件
		//用输入输出流来截取结果

//		Thread.sleep(5000);
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GB2312"));
//		Thread.sleep(1000);//等待后台线程读写完毕
		String line = null;
		String message=new String();
		int flag=0;
		while ((line = in.readLine()) != null) {
			flag=1;
			jsonList.add(line);
		}
		System.out.println(jsonList);
		//返回json
		if(flag==1){
			json.put("content","有");
		}else {
			json.put("content","无");
		}
		int resultCode = 0;
		json.put("askFileName",askfileName1);
		json.put("ansFileName",ansfileName1);
		json.put("result_code",resultCode);
		json.put("aaData",jsonList);
		//json设置完毕
		//收尾
		in.close();
		proc.waitFor();

	}


	public void getProcess3(Data data, JSONObject json) throws Exception{
		// TODO 使用Runtime.getRuntime()执行python脚本文件 --较上一个直接调用脚本，推荐此方法
		Process proc;
		List jsonList = new ArrayList();

		//设置文件名称 用户名-日期-处理.csv
		String username=data.getParam().getString("username");
		Date date = new Date();//获取当前的日期
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");//设置日期格式
		String datestr = df.format(date);//获取String类型的时间
		String fileName="\""+username+"_"+datestr+"_Df.csv\"";
		String fileName1=username+"_"+datestr+"_Df.csv";
		System.out.println(fileName);

		//
		String pythonSql=data.getParam().getString("pythonSql");

		//设置cmd内容

//		String str1="python  E:\\chuang\\processFunction\\dp_1_key_words.py ";//原始版--实测可行
		String str="python  E:\\chuang\\processFunction\\dp_3_otherAll.py "+"\""+pythonSql+"\" "+ fileName;
		System.out.println(str);

		proc = Runtime.getRuntime().exec(str);// 执行py文件
		//用输入输出流来截取结果
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GB2312"));
		String line = null;
		String message=new String();
		int flag=0;
		while ((line = in.readLine()) != null) {
			flag=1;
//			System.out.println(line);
//			message+=line;
//			message+="\n";
			jsonList.add(line);
		}
//		System.out.println(message);
		System.out.println(jsonList);
		//返回json
		if(flag==1){
			json.put("content","有");
		}else {
			json.put("content","无");
		}
		int resultCode = 0;
//		json.put("str",str);
		json.put("fileName",fileName1);
		json.put("result_code",resultCode);
		json.put("aaData",jsonList);
		//json设置完毕
		//收尾
		in.close();
		proc.waitFor();
		//json设置完毕
		//收尾
	}
	public void getProcessTest(Data data, JSONObject json) throws Exception{
		PythonInterpreter interpreter = new PythonInterpreter();
		//TODO 直接执行python代码
//		interpreter.exec("a='hello world22';print a;import sys\n" +
//				"print(sys.path) ");
//		interpreter.exec("print a;");
//		interpreter.exec("sys.path.append('E:/python/Lib/site-packages')");
//		interpreter.exec("sys.path.append('E:/python/Lib')");
//		interpreter.exec("sys.path.append('C:\\jython2.7.0\\Lib')");
//		interpreter.exec("sys.path.append('C:\\jython2.7.0\\Lib\\site-packages')");
//		interpreter.exec("print(sys.path) ");
//		interpreter.exec("import pymysql");
		interpreter.exec("print(11122) ");
		//TODO  直接调用python脚本
		//interpreter.execfile("D:\\javaPythonFile.py");
		//TODO  调用python中的函数
//		System.out.println("调用python中的函数");
//		//PythonInterpreter interpreter = new PythonInterpreter();
//		interpreter.execfile("E:\\chuang\\processFunction\\dataprocess.py");
//
//		// 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
//		PyFunction pyFunction = interpreter.get("dataprocess1", PyFunction.class);
////		int a = 5, b = 10;
//		String pythonSql=data.getParam().getString("pythonSql");
//		System.out.println(pythonSql);
////		PyString varNam;
////		interpreter.set(varName, pythonSql);
////		key, ask, ans, all = dataprocess()
//		//调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
////		PyObject pyobj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
//		PyObject pyobj = pyFunction.__call__(new PyString(pythonSql));
//		System.out.println("the anwser is: " + pyobj);
		// TODO 使用Runtime.getRuntime()执行python脚本文件 --较上一个直接调用脚本，推荐此方法
		Process proc;
		List jsonList = new ArrayList();

		String str="python  E:\\chuang\\processFunction\\dataprocess.py";//原始版--实测可行

		proc = Runtime.getRuntime().exec(str);// 执行py文件
		//用输入输出流来截取结果
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"GB2312"));
		String line = null;
		String message=new String();
		int flag=0;
		while ((line = in.readLine()) != null) {
			flag=1;
			System.out.println(line);
			message+=line;
			message+="\n";
			jsonList.add(line);
		}
		System.out.println(message);
		System.out.println(jsonList);
		//返回json
		if(flag==1){
			json.put("content","有");
		}else {
			json.put("content","无");
		}
		int resultCode = 0;
		json.put("str",str);
		json.put("result_code",resultCode);
		json.put("aaData",jsonList);
		//json设置完毕

		//收尾
		in.close();
		proc.waitFor();
		//json设置完毕

		//收尾
	}
//	/**
//	 * 杀掉一个进程
//	 */
//	public static void killProcess(String name) {
//		try {
//			String[] cmd = {"tasklist"};
//			Process proc = Runtime.getRuntime().exec(cmd);
//			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//			String string_Temp = in.readLine();
//			while (string_Temp != null) {
//				// System.out.println(string_Temp);
//				if (string_Temp.indexOf(name) != -1) {
//					Runtime.getRuntime().exec("taskkill /F /IM " + name);
//					System.out.println("杀死进程  " + name);
//				}
//				string_Temp = in.readLine();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 判断进程是否存在
//	 */
//	public static boolean processIsRun(String ProjectName) {
//		boolean flag = false;
//		try {
//			Process p = Runtime.getRuntime().exec("cmd /c tasklist ");
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			InputStream os = p.getInputStream();
//			byte b[] = new byte[256];
//			while (os.read(b) > 0)
//				baos.write(b);
//			String s = baos.toString();
//			if (s.indexOf(ProjectName) >= 0) {
//				flag = true;
//			} else {
//				flag = false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}
	public void getTodoSortRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String username=data.getParam().has("username")?data.getParam().getString("username"):null;
		String degree=data.getParam().has("degree")?data.getParam().getString("degree"):null;
		String status=data.getParam().has("status")?data.getParam().getString("status"):null;
		String sql="select * from  xm13_todo where username='"+username+"'";
		if(degree!=null) {
			sql += " and  degree='" + degree+"'";
		}
		if(status!=null) {
			sql += " and  status='" + status+"'";
		}else{
			sql += " and  status='undone'";
		}
		sql+=" order by time_end  ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	public void getObjectAllRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String sql="select * from  software_object";
		sql+=" order by create_time desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}

	public void getTodoAllRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String username=data.getParam().has("username")?data.getParam().getString("username"):null;
		String sql="select * from  xm13_todo  where username='"+username+"'";
		sql+=" order by create_time desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	/*删除记录*/
	public void deleteTodoRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null){
			String sql="delete from xm13_todo where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*申请物品*/
	public void applyObject(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null){
			String sql="update software_object";
			sql=sql+" set status='已领取'";
			sql=sql+" where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}

	public void doneTodo(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null){
			String sql="update xm13_todo";
			sql=sql+" set status='done'";
			sql=sql+" where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*修改记录*/
	public void changeObject(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;


		String objectName=data.getParam().has("name")?data.getParam().getString("name"):null;
		String place=data.getParam().has("place")?data.getParam().getString("place"):null;
		String time=data.getParam().has("time")?data.getParam().getString("time"):null;
		String describe=data.getParam().has("describe")?data.getParam().getString("describe"):null;
		String phone=data.getParam().has("phone")?data.getParam().getString("phone"):null;

		if(id!=null){
			String sql="update software_object";
			sql=sql+" set name='"+objectName+"'";
			sql=sql+" ,place='"+place+"'";
			sql=sql+" ,time='"+time+"'";
			sql=sql+" ,obj_describe='"+describe+"'";
			sql=sql+" ,phone='"+phone+"'";
			sql=sql+" where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}

	public void changeTodo(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		String todo=data.getParam().has("todo")?data.getParam().getString("todo"):null;
		String time_end=data.getParam().has("time_end")?data.getParam().getString("time_end"):null;
		String degree=data.getParam().has("degree")?data.getParam().getString("degree"):null;

		if(id!=null){
			String sql="update xm13_todo";
			sql=sql+" set todo='"+todo+"'";
			sql=sql+" ,time_end='"+time_end+"'";
			sql=sql+" ,degree='"+degree+"'";
			sql=sql+" where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*查询记录*/
	public void getDeviceRecord(Data data,JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetRecordSql(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	/*
	 * 这是一个样板的函数，可以拷贝做修改用
	 */
	private void updateRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		JSONObject param=data.getParam();
		int resultCode=0;
		String resultMsg="ok";
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		DbRemote updateDb = new DbRemote("yjykfsj2021");
		String sql=data.getParam().getString("sql");
		showDebug("[updateRecord]"+sql);
		updateDb.executeUpdate(sql);
		updateDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}
	private void queryRecord(Data data,JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		List jsonName = new ArrayList();
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		DbRemote queryDb = new DbRemote("yjykfsj2021");
		String sql=data.getParam().getString("sql");
		showDebug("[queryRecord]构造的SQL语句是：" + sql);
		try {
			ResultSet rs = queryDb.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int fieldCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 0; i < fieldCount; i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
				}
				jsonList.add(map);
			}
			rs.close();
			//加表头信息
			for(int i=0;i<rsmd.getColumnCount();i++)
			{
				String columnLabel=rsmd.getColumnLabel(i+1);
				jsonName.add(columnLabel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showDebug("[queryRecord]查询数据库出现错误：" + sql);
			resultCode = 10;
			resultMsg = "查询数据库出现错误！" + e.getMessage();
		}
		queryDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("aaData",jsonList);
		json.put("aaFieldName",jsonName);
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}

	private String createGetRecordSql(Data data) throws JSONException {
		String sql="select * from device_file";
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null && !id.isEmpty())
			sql=sql+" where id="+id;
		String device_id=data.getParam().has("device_id")?data.getParam().getString("device_id"):null;
		if(device_id!=null && !device_id.isEmpty()){
			if (sql.indexOf("where")>-1){
				sql=sql+" and device_id like '%"+device_id+"%'";
			}else{
				sql=sql+" where device_id like '%"+device_id+"%'";
			}
		}
		String device_name=data.getParam().has("device_name")?data.getParam().getString("device_name"):null;
		if(device_name!=null && !device_name.isEmpty()){
			if (sql.indexOf("where")>-1){
				sql=sql+" and device_name like '%"+device_name+"%'";
			}else{
				sql=sql+" where device_name like '%"+device_name+"%'";
			}
		}
		return sql;
	}
	public void login(Data data,JSONObject json)throws JSONException,SQLException{
		/*------------------------获取变量 开始--------------------------*/
		String resultMsg="ok";
		int resultCode=0;
		List jsonList=new ArrayList();
		String account=data.getParam().getString("account");
		String password=data.getParam().getString("password");
		String action =data.getParam().getString("action");
		/*------------------------获取变量 开始--------------------------*/
		/*------------------------数据操作 开始--------------------------*/
		DbRemote queryDb=new DbRemote("yjykfsj2021");
		String sql="select * from software_user where account='"+account+"' and password='"+password+"'";
		showDebug("[login]构造的sq语句是："+sql);
		try {
			ResultSet rs=queryDb.executeQuery(sql);
			ResultSetMetaData rsmd =rs.getMetaData();
			int fieldCount=rsmd.getColumnCount();
			if(!rs.next()){
				resultCode=10;
				resultMsg="登陆失败，请核对用户名及密码!";
			}
			rs.close();
		}catch (Exception e){
			e.printStackTrace();
			showDebug("[queryRecord]查询数据库出现错误："+sql);
			resultCode=10;
			resultMsg="查询数据库出现错误！"+e.getMessage();
		}
		queryDb.close();
		/*------------------------数据操作 结束--------------------------*/
		/*------------------------返回数据 开始--------------------------*/
		json.put("aaData",jsonList);
		json.put("action",action);
		json.put("result_mag",resultMsg);
		json.put("result_code",resultCode);

		/*------------------------返回数据 结束--------------------------*/

	}

	public void register(Data data, JSONObject json) throws JSONException, SQLException {
		//构造sql语句，根据传递过来的条件参数
		String counter=data.getParam().has("counter")?data.getParam().getString("counter"):null;
		String password=data.getParam().has("password")?data.getParam().getString("password"):null;
		String action=data.getParam().has("action")?data.getParam().getString("action"):null;
		String create_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if(password!=null && password!=null){
			String sql="insert into software_user(account,password,create_time)";
			sql=sql+" values('"+counter+"'";
			sql=sql+" ,'"+password+"','"+create_time+"')";
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	public void saveUploadRecord(Data data, JSONObject json) throws JSONException, SQLException {
		if(json.getJSONArray("files").length()>0){
			JSONArray array=(JSONArray)json.getJSONArray("files");
			for(int i=0;i<array.length();i++) {
				HashMap map = (HashMap)array.get(0);
				String downloadUrl= (String) map.get("download_url");
				String newFileName= (String) map.get("file_name");
				String filePath= (String) map.get("file_path");
				filePath=filePath.replaceAll("\\\\","\\\\\\\\");
				String creatorId="00000000";					//这个可以换成当前操作者的系统唯一用户编号
				String creator="系统管理员";					//这个可以换成当前操作者的用户中文姓名
				String objectId="ATTACH_"+(new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());				//传到前端的本次上传的唯一流水号
				String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
				String sql="insert into public_attachment(object_id,attachment_name,attachment_filename,attachment_url,creator_id,creator,create_time)";
				sql=sql+" values('"+objectId+"','"+newFileName+"','"+filePath+"','"+downloadUrl+"','"+creatorId+"','"+creator+"','"+createTime+"')";
				data.getParam().put("sql",sql);
				updateRecord(data,json);
				json.put("attachment_id",objectId);
			}
		}
	}

	public void getTodoStatistic(Data data, JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String username=data.getParam().getString("username");
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		DbRemote queryDb = new DbRemote("yjykfsj2021");
		//String sql=data.getParam().getString("sql");
		//DbRemote queryDb = new DbRemote("ylxdb");
		String sql="select * from xm13_todo where username='"+username+"' ";
		showDebug("[getTodoStatistic]构造的SQL语句是：" + sql);
		try {
			ResultSet rs = queryDb.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int fieldCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 0; i < fieldCount; i++) {
					map.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
				}
				jsonList.add(map);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			showDebug("[getGpsStatistic]查询数据库出现错误：" + sql);
			resultCode = 10;
			resultMsg = "查询数据库出现错误！" + e.getMessage();
		}
		queryDb.close();
		/*--------------------数据操作 结束--------------------*/
		/*--------------------返回数据 开始--------------------*/
		json.put("aaData",jsonList);
		json.put("result_msg",resultMsg);															//如果发生错误就设置成"error"等
		json.put("result_code",resultCode);														//返回0表示正常，不等于0就表示有错误产生，错误代码
		/*--------------------返回数据 结束--------------------*/
	}
}