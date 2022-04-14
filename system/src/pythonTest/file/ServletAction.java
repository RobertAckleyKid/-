package pythonTest.file;

import org.json.JSONException;
import org.json.JSONObject;
import pythonTest.dao.Data;
import pythonTest.dao.ObjectDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

@WebServlet(name = "ServletAction", value = "/python_test1")
public class ServletAction extends HttpServlet {
    //    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
    String module="pythonTest";
    String sub="file";
    public void showDebug(String msg){
        System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"]["+module+"/"+sub+"/ServletAction]"+msg);
    }
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processAction(request,response);
    }
    /*========================================函数分流 开始========================================*/
    public void processAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        boolean actionOk = false;
        int resultCode=0;
        String resultMsg="ok";
        JSONObject json=new JSONObject();
        showDebug("processAction收到的action是："+action);
        if (action == null){
            resultMsg="传递过来的action是NULL";
        }else{
            if (action.equals("get_python_res")) {/////////////////////////////////////////////////////////////
                actionOk=true;
                try {
                    getPythonRes(request, response, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (action.equals("get_Reptile")) {/////////////////////////////////////////////////////////////
                actionOk=true;
                try {
                    getReptile(request, response, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (action.equals("get_process1")) {/////////////////////////////////////////////////////////////
                actionOk=true;
                try {
                    getProcess1(request, response, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (action.equals("get_process2")) {/////////////////////////////////////////////////////////////
                actionOk=true;
                try {
                    getProcess2(request, response, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            else if (action.equals("get_process3")) {/////////////////////////////////////////////////////////////
                actionOk=true;
                try {
                    getProcess3(request, response, json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                responseBack(request,response,json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*========================================函数分流 结束========================================*/
    /*========================================公共函数 开始========================================*/
    private Data getPageParameters(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws JSONException{
        Data data=new Data();
        HttpSession session = request.getSession();
        /*----------------------------------------获取所有表单信息 开始----------------------------------------*/
        showDebug("[getPageParameters]----------------------------------------获取所有表单信息 开始----------------------------------------");
        JSONObject param=data.getParam();
        Enumeration requestNames=request.getParameterNames();
        for(Enumeration e=requestNames;e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            String thisValue=request.getParameter(thisName);
            showDebug("[getPageParameters]"+thisName+"="+thisValue);
            showDebug(data.getParam().toString());
            param.put(thisName, thisValue);
        }
        String[] ids=request.getParameterValues("ids[]");if(ids!=null){param.put("ids[]",ids);}			//后头用这样来取出：String[] ids=(String[])(data.getParam().get("ids[]"));
        showDebug("[getPageParameters]----------------------------------------获取所有表单信息 完毕----------------------------------------");
        /*----------------------------------------获取所有表单信息 完毕----------------------------------------*/
        return data;
    }
    private void responseBack(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException {
        boolean isAjax=true;if (request.getHeader("x-requested-with") == null || request.getHeader("x-requested-with").equals("com.tencent.mm")){isAjax=false;}	//判断是异步请求还是同步请求，腾讯的特殊
        if(isAjax){
            response.setContentType("application/json; charset=UTF-8");
            try {
                response.getWriter().print(json);
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            String action=json.getString("action");
            String errorNo="0";
            String errorMsg="ok";
            String url = module+"/"+sub+"/result.jsp?action="+action+"&result_code="+errorNo+ "&result_msg=" + errorMsg;
            if(json.has("redirect_url"))url=json.getString("redirect_url");
            try {
                response.sendRedirect(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void getPythonRes(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws Exception {
        ObjectDao dao=new ObjectDao();
        Data data=getPageParameters(request,response,json);
        dao.getPythonRes(data,json);
    }

    private void getReptile(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws Exception {
        ObjectDao dao=new ObjectDao();
        Data data=getPageParameters(request,response,json);
        dao.getReptile(data,json);
    }

    private void getProcess1(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws Exception {
        ObjectDao dao=new ObjectDao();
        Data data=getPageParameters(request,response,json);
        dao.getProcess1(data,json);
    }

    private void getProcess2(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws Exception {
        ObjectDao dao=new ObjectDao();
        Data data=getPageParameters(request,response,json);
        dao.getProcess2(data,json);
    }

    private void getProcess3(HttpServletRequest request, HttpServletResponse response, JSONObject json) throws Exception {
        ObjectDao dao=new ObjectDao();
        Data data=getPageParameters(request,response,json);
        dao.getProcess3(data,json);
    }

}
