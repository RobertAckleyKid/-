package process.dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectDao {
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"]"+msg);
	}
	public static String deleteString(String str, char delChar) {
		StringBuffer stringBuffer = new StringBuffer("");
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != delChar) {
				stringBuffer.append(str.charAt(i));
			}
		}
		return stringBuffer.toString();
	}
	/*添加记录选择*/
	public void addSelectInfo(HttpServletRequest req, Data data, JSONObject json) throws JSONException, SQLException {
		//构造sql语句，根据传递过来的条件参数

		String selectCity=data.getParam().has("selectCity")? data.getParam().getString("selectCity") :null;
		String beginTime=data.getParam().has("beginTime")? data.getParam().getString("beginTime") :null;
		String endTime=data.getParam().has("endTime")? data.getParam().getString("endTime") :null;
		String javaSql="";
		String pythonSql="";
		if(selectCity.equals("no")){
			String citys=data.getParam().has("city")? data.getParam().getString("city") :null;
			citys=deleteString(citys,'"');
			String demosub =citys.substring(1,citys.length()-1);
			System.out.println(demosub);
			String cityArray[] = demosub.split(",");
			System.out.println("获取的数组："+Arrays.toString(cityArray));
			System.out.println("获取的："+cityArray[0]);
			//构建存储的查询语句
			String selectCitySql="AND (";
			for(int i=0;i<cityArray.length;i++){
				selectCitySql+="city='"+cityArray[i]+"'";
				if(i+1< cityArray.length){
					selectCitySql+=" OR ";
				}
			}
			selectCitySql+=")";
			javaSql="SELECT DISTINCT * FROM item WHERE askTime>'"+beginTime+"' AND askTime<'"+endTime+" 23:59' "+selectCitySql+"ORDER BY askTime; ";
			pythonSql="SELECT DISTINCT * FROM test.item WHERE askTime>'"+beginTime+"' AND askTime<'"+endTime+" 23:59' "+selectCitySql+"ORDER BY askTime; ";
		}else {
			//构建存储的查询语句
			javaSql="SELECT DISTINCT * FROM item WHERE askTime>'"+beginTime+"' AND askTime<'"+endTime+" 23:59' ORDER BY askTime; ";
			pythonSql="SELECT DISTINCT * FROM test.item WHERE askTime>'"+beginTime+"' AND askTime<'"+endTime+" 23:59' ORDER BY askTime; ";
		}
		System.out.println("javaSql"+javaSql);
		System.out.println("pythonSql"+pythonSql);

		Date date = new Date();//获取当前的日期
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");//设置日期格式
		String str = df.format(date);//获取String类型的时间
		System.out.println(str);

		String sql="insert into item_select(uesrname,javaSql,pythonSql,process,selectName)";
		sql = sql + " values('" + "123" + "',\"" + javaSql + "\",\"" + pythonSql + "\",'before','"+str+"')";
		System.out.println(sql);
		data.getParam().put("sql", sql);
		updateRecord(data, json);
	}

	public void getMyselect(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String username=data.getParam().has("username")?data.getParam().getString("username"):null;
		String sql="select * from  item_select where username='"+username+"'";
		sql+=" order by selectName desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}


	public void getInfo(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("selectName")?data.getParam().getString("selectName"):null;
		String username=data.getParam().has("username")?data.getParam().getString("username"):null;
		String sql="select * from  item_select where username='"+username+"' and id='"+id+"'";
//		sql+=" order by selectName desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}

	public void getInfoInit(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String sql=data.getParam().has("javaSql")?data.getParam().getString("javaSql"):null;
//		sql+=" order by selectName desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	public void addTodoRecord(Data data, JSONObject json) throws JSONException, SQLException {
		//构造sql语句，根据传递过来的条件参数
		String username=data.getParam().has("username")?data.getParam().getString("username"):null;
		String degree=data.getParam().has("degree")?data.getParam().getString("degree"):null;
		String todo=data.getParam().has("todo")?data.getParam().getString("todo"):null;
		String time_end=data.getParam().has("time_end")?data.getParam().getString("time_end"):null;
		String picture_url=data.getParam().has("picture_url")?data.getParam().getString("picture_url"):null;
		String create_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String status="undone";
		String sql = "insert into xm13_todo(username,degree,todo,time_end,picture_url,status,create_time)";
		sql = sql + " values('" + username + "','" + degree + "','" + todo + "','" + time_end + "','"  +picture_url+  "','" +status+  "','"+create_time + "')";
		data.getParam().put("sql", sql);
		updateRecord(data, json);

	}

	public void getObjectRecord(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String type=data.getParam().has("type")?data.getParam().getString("type"):null;
			String sql="select * from  software_object where status='待领取'";
			if(type!=null) {
				sql += " and  type='" + type+"'";
			}
			sql+=" order by create_time desc ";
			data.getParam().put("sql",sql);
			queryRecord(data,json);
	}

	public void getTodoRecord(Data data, JSONObject json) throws JSONException, SQLException{
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
		sql+=" order by create_time desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}

	public void TodoQuery(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String username=data.getParam().has("username")?data.getParam().getString("username"):null;
		String degree=data.getParam().has("degree")?data.getParam().getString("degree"):null;
		String status=data.getParam().has("status")?data.getParam().getString("status"):null;
		String todo=data.getParam().has("search_todo")?data.getParam().getString("search_todo"):"";
		String sql="select * from  xm13_todo where username='"+username+"'";
		if(degree!=null) {
			sql += " and  degree='" + degree+"'";
		}
		if(status!=null) {
			sql += " and  status='" + status+"'";
		}else{
			sql += " and  status='undone'";
		}
		sql+=" and todo like '%"+todo+"%'";
		sql+=" order by create_time desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	public void getTodoSortRecord(Data data, JSONObject json) throws JSONException, SQLException{
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
	public void getObjectAllRecord(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String sql="select * from  software_object";
		sql+=" order by create_time desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}

	public void getTodoAllRecord(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String username=data.getParam().has("username")?data.getParam().getString("username"):null;
		String sql="select * from  xm13_todo  where username='"+username+"'";
		sql+=" order by create_time desc ";
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	/*删除记录*/
	public void deleteTodoRecord(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的条件参数
		String id=data.getParam().has("id")?data.getParam().getString("id"):null;
		if(id!=null){
			String sql="delete from xm13_todo where id="+id;
			data.getParam().put("sql",sql);
			updateRecord(data,json);
		}
	}
	/*申请物品*/
	public void applyObject(Data data, JSONObject json) throws JSONException, SQLException{
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

	public void doneTodo(Data data, JSONObject json) throws JSONException, SQLException{
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
	public void changeObject(Data data, JSONObject json) throws JSONException, SQLException{
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

	public void changeTodo(Data data, JSONObject json) throws JSONException, SQLException{
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
	public void getDeviceRecord(Data data, JSONObject json) throws JSONException, SQLException{
		//构造sql语句，根据传递过来的查询条件参数
		String sql=createGetRecordSql(data);			//构造sql语句，根据传递过来的查询条件参数
		data.getParam().put("sql",sql);
		queryRecord(data,json);
	}
	/*
	 * 这是一个样板的函数，可以拷贝做修改用
	 */
	private void updateRecord(Data data, JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		JSONObject param=data.getParam();
		int resultCode=0;
		String resultMsg="ok";
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		DbRemote updateDb = new DbRemote("test");
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
	private void queryRecord(Data data, JSONObject json) throws JSONException, SQLException{
		/*--------------------获取变量 开始--------------------*/
		String resultMsg = "ok";
		int resultCode = 0;
		List jsonList = new ArrayList();
		List jsonName = new ArrayList();
		/*--------------------获取变量 完毕--------------------*/
		/*--------------------数据操作 开始--------------------*/
		DbRemote queryDb = new DbRemote("test");
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
	public void login(Data data, JSONObject json)throws JSONException,SQLException{
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
		DbRemote queryDb = new DbRemote("test");
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
