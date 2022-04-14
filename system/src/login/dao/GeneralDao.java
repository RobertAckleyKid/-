package login.dao;

import login.data.Data;
import login.db.Db;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneralDao {
    public void showDebug(String msg) {
        System.out.println("[" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "][device/dao/Db]" + msg);
    }

    private void updateRecord(Data data, JSONObject json) throws JSONException, SQLException {
        /*--------------------获取变量 开始--------------------*/
        JSONObject param = data.getParam();
        int resultCode = 0;
        String resultMsg = "ok";
        /*--------------------获取变量 完毕--------------------*/
        /*--------------------数据操作 开始--------------------*/
        Db updateDb = new Db("test");
        String sql = data.getParam().getString("sql");
        showDebug("[updateRecord]" + sql);
        updateDb.executeUpdate(sql);
        updateDb.close();
        /*--------------------数据操作 结束--------------------*/
        /*--------------------返回数据 开始--------------------*/
        json.put("result_msg", resultMsg);                                                            //如果发生错误就设置成"error"等
        json.put("result_code", resultCode);                                                        //返回0表示正常，不等于0就表示有错误产生，错误代码
        /*--------------------返回数据 结束--------------------*/
    }

    private void queryRecord(Data data, JSONObject json) throws JSONException, SQLException {
        /*--------------------获取变量 开始--------------------*/
        String resultMsg = "ok";
        int resultCode = 0;
        List jsonList = new ArrayList();
        List jsonName = new ArrayList();
        /*--------------------获取变量 完毕--------------------*/
        /*--------------------数据操作 开始--------------------*/
        Db queryDb = new Db("test");
        String sql = data.getParam().getString("sql");
        showDebug("[queryRecord]构造的SQL语句是：" + sql);
        try {
            ResultSet rs = queryDb.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int fieldCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map map = new LinkedHashMap();
                for (int i = 0; i < fieldCount; i++) {
                    map.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
                }
                jsonList.add(map);
            }
            rs.close();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                String columnLabel = rsmd.getColumnLabel(i + 1);
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
        json.put("aaData", jsonList);
        json.put("aaFieldName", jsonName);
        json.put("result_msg", resultMsg);                                                            //如果发生错误就设置成"error"等
        json.put("result_code", resultCode);                                                        //返回0表示正常，不等于0就表示有错误产生，错误代码
        /*--------------------返回数据 结束--------------------*/
    }

    private String createGetRecordSql(Data data) throws JSONException {
        String sql = "select * from user_info";
        String order_by = data.getParam().has("order_by") ? data.getParam().getString("order_by") : null;
        String key = data.getParam().has("key") ? data.getParam().getString("key") : null;
        if (key != null && !key.isEmpty()) {
            sql = sql + " where username like '%" + key + "%' ";
            sql = sql + "or create_time like '%" + key + "%' ";
        }
        if (order_by != null && !order_by.isEmpty()) {
            sql = sql + order_by;
        }
        return sql;
    }

    public void login(Data data, JSONObject json) throws IOException, JSONException {
        String resultMsg = "ok";
        int resultCode = 0;
        List jsonList = new ArrayList();
        String userName = data.getParam().getString("username");
        String password = data.getParam().getString("password");
        Db queryDb = new Db("test");
        String sql = "select id, username, role from user_info where username='" + userName + "' and password='" + password + "'";
        try {
            ResultSet rs = queryDb.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                Map map = new LinkedHashMap();
                map.put("id", rs.getInt("id"));
                map.put("username", rs.getString("username"));
                map.put("role", rs.getString("role"));
                jsonList.add(map);
                resultCode = 0;
                resultMsg = "登录成功";
                i++;
            }
            if (i == 0) {
                resultCode = 1;
                resultMsg = "用户名或密码错误";
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = 10;
            resultMsg = "查询数据库错误!" + e.getMessage();
        }
        queryDb.close();
        json.put("aaData", jsonList);
        json.put("result_msg", resultMsg);
        json.put("result_code", resultCode);
    }

    public void loginEmail(HttpServletRequest request,Data data, JSONObject json) throws JSONException, SQLException {
        String sessionCode = (String) request.getSession().getAttribute("code");
        System.out.println("生成的验证码:"+sessionCode);
        String resultMsg = "ok";
        int resultCode = 0;
        String email = data.getParam().getString("email");
        String code = data.getParam().getString("code");
        System.out.println("输入的验证码:"+code);
        if (sessionCode.toLowerCase().equals(code.toLowerCase())) {
            json.put("result_msg", resultMsg);
            json.put("result_code", resultCode);
        }else {
            resultCode = 3;
            resultMsg = "验证码输入错误！";
            json.put("result_msg", resultMsg);
            json.put("result_code", resultCode);
        }
    }
    public void register(HttpServletRequest request,Data data, JSONObject json) throws JSONException, SQLException {
        String sessionCode = (String) request.getSession().getAttribute("code");
        System.out.println("生成的验证码:"+sessionCode);
        String resultMsg = "ok";
        int resultCode = 0;
        String userName = data.getParam().getString("username");
        String password = data.getParam().getString("password");
        String email = data.getParam().getString("email");
        String code = data.getParam().getString("code");
        System.out.println("输入的验证码:"+code);
        String role = "common";
        if (sessionCode.toLowerCase().equals(code.toLowerCase())) {
            Db queryDb = new Db("test");
            String sql = "select * from user_info where username='" + userName + "'";
            try {
                ResultSet rs = queryDb.executeQuery(sql);
                if (rs.next()) {
                    resultCode = 1;
                    resultMsg = "用户名已存在";
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                resultCode = 10;
                resultMsg = "查询数据库失败!" + e.getMessage();
            }
            if (userName != null && resultCode == 0) {
                sql = "insert into user_info(username,password,email,role)";
                sql = sql + " values('" + userName + "'";
                sql = sql + ", '" + password + "'";
                sql = sql + ", '" + email + "'";
                sql = sql + ", '" + role + "')";
//            data.getParam().put("sql", sql+";"+sql2);
                data.getParam().put("sql", sql);
                updateRecord(data, json);
                resultMsg = "注册成功,将跳转至登陆界面";
            }
            queryDb.close();
            json.put("result_msg", resultMsg);
            json.put("result_code", resultCode);
        }else {
            resultCode = 3;
            resultMsg = "验证码输入错误！";
            json.put("result_msg", resultMsg);
            json.put("result_code", resultCode);
        }
    }

    public void checkEmail(Data data, JSONObject json) throws JSONException, SQLException {
        String resultMsg = "ok";
        int resultCode = 0;
        String email = data.getParam().getString("email");
            Db queryDb = new Db("test");
            String sql = "select * from user_info where email='" + email + "'";
            try {
                ResultSet rs = queryDb.executeQuery(sql);
                if (rs.next()) {
                    resultCode = 1;
                    resultMsg = "该邮箱已绑定，不能重复绑定";
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                resultCode = 10;
                resultMsg = "查询数据库失败!" + e.getMessage();
            }
            queryDb.close();
            json.put("result_msg", resultMsg);
            json.put("result_code", resultCode);
    }

    public void findEmail(Data data, JSONObject json) throws JSONException, SQLException {
        String resultMsg = "ok";
        int resultCode = 0;
        List jsonList = new ArrayList();
        String email = data.getParam().getString("email");
        Db queryDb = new Db("test");
        String sql = "select * from user_info where email='" + email + "'";
        try {
            ResultSet rs = queryDb.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int fieldCount = rsmd.getColumnCount();
            if (rs.next()) {
                resultCode = 0;
                Map map = new HashMap();
                for (int i = 0; i < fieldCount; i++) {
                    map.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
                }
                jsonList.add(map);
            } else {
                resultCode = 1;
                resultMsg = "该邮箱尚未绑定！";
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = 10;
            resultMsg = "查询数据库失败!" + e.getMessage();
        }
        queryDb.close();
        json.put("aaData",jsonList);
        json.put("result_msg", resultMsg);
        json.put("result_code", resultCode);
    }
    public void setLoginTable(Data data, JSONObject json) throws JSONException, SQLException {
        String id = data.getParam().has("id") ? data.getParam().getString("id") : null;
        Date time = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String loginTime = df.format(time);
        String sql = "insert into xm13_login values(" + id + ", '" + loginTime + "')";
        data.getParam().put("sql", sql);
        updateRecord(data, json);
    }
}
