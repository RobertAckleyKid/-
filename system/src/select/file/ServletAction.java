package select.file;
/*
 * 待完成：用MVC模式分开DB和Action操作
 * 增删改查看导印统功能的实现
 */

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;
import org.json.JSONObject;
import select.dao.Data;
import select.dao.ObjectDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServletAction extends HttpServlet {
	String module="object";
	String sub="file";
	public void showDebug(String msg){
		System.out.println("["+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+"]["+module+"/"+sub+"/ServletAction]"+msg);
	}
	/*
	 * 处理顺序：先是service，后根据情况doGet或者doPost
	 */
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
			//这几个常规增删改查功能
//			if (action.equals("upload_record")) {
//				actionOk=true;
//				try {
//					uploadRecord(request, response, json);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}

			if (action.equals("select_info")) {
				actionOk=true;
				try {
					addSelectInfo(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (action.equals("get_mySelect")) {
				actionOk=true;
				try {
					getMyselect(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (action.equals("change_status")) {
				actionOk=true;
				try {
					changeStatus(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (action.equals("get_object_record")) {
				actionOk=true;
				try {
					getObjectRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (action.equals("get_todo_record")) {
				actionOk=true;
				try {
					getTodoRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (action.equals("get_todo_sort_record")) {
				actionOk=true;
				try {
					getTodoSortRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("get_object_all_record")) {
				actionOk=true;
				try {
					getObjectAllRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("get_todo_all_record")) {
				actionOk=true;
				try {
					getTodoAllRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("get_todo_query_record")) {/////////////////////////////////////////////////////////////
				actionOk=true;
				try {
					TodoQuery(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("apply_object")) {
				actionOk=true;
				try {
					applyObject(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (action.equals("done_todo")) {
				actionOk=true;
				try {
					doneTodo(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("change_object")) {
				actionOk=true;
				try {
					changeObject(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("change_todo")) {
				actionOk=true;
				try {
					changeTodo(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (action.equals("delete_todo_record")) {
				actionOk=true;
				try {
					deleteTodoRecord(request, response, json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			if (action.equals("export_todo_record")) {////////////////////////////////////////////////////////////导出AccountList//////////////
//				actionOk=true;
//				try {
//					exportAccountRecord(request, response, json);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			if (action.equals("get_todo_statistic")) {
				actionOk=true;
				try {
					getTodoStatistic(request, response, json);
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
	private Data getPageParameters(HttpServletRequest request,HttpServletResponse response,JSONObject json) throws JSONException{
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
			try {
				response.sendRedirect(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*========================================公共函数 结束========================================*/
	/*========================================CRUD业务函数 开始========================================*/



	////////////////////////////////////////////// add  object/////////////////////////////////


	private void addSelectInfo(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.addSelectInfo(request,data,json);
	}

	private void getMyselect(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.getMyselect(data,json);
	}

	private void changeStatus(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.changeStatus(data,json);
	}
	private void addTodoRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.addTodoRecord(data,json);
	}
	private void getObjectRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.getObjectRecord(data,json);
	}

	private void getTodoRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.getTodoRecord(data,json);
	}
	private void TodoQuery(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.TodoQuery(data,json);
	}
	private void getTodoSortRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.getTodoSortRecord(data,json);
	}
	private void getObjectAllRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.getObjectAllRecord(data,json);
	}

	private void getTodoAllRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.getTodoAllRecord(data,json);
	}
	private void applyObject(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
	}

	private void doneTodo(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.doneTodo(data,json);
	}
	private void changeObject(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.changeObject(data,json);
	}

	private void changeTodo(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.changeTodo(data,json);
	}
	private void deleteTodoRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.deleteTodoRecord(data,json);
	}
	/*========================================CRUD业务函数 结束========================================*/

//	private void exportAccountRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException, IOException {
//		ObjectDao dao=new ObjectDao();
//		Data data=getPageParameters(request,response,json);
//		dao.TodoQuery(data,json);
//		getExportTodoRecordToExcel(json, data);
//	}

	private void getTodoStatistic(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException {
		ObjectDao dao=new ObjectDao();
		Data data=getPageParameters(request,response,json);
		dao.getTodoStatistic(data,json);
	}
//	private void getExportTodoRecordToExcel(JSONObject json, Data data) throws JSONException, IOException {
//		String username=data.getParam().getString("username");
//		MyExcel me = new MyExcel("D:\\webapps\\teach\\yjykfsj2021\\xm13_file\\export_"+username+"_todo_table.xls");
//		json.put("download_url", "/yk2021_xm13/export_"+username+"_todo_table.xls");
//		json.put("file_path", "D:\\webapps\\teach\\yjykfsj2021\\xm13_file\\export_"+username+"_todo_table.xls");
//		me.exportData(data, json);
//	}
	private void uploadRecord(HttpServletRequest request, HttpServletResponse response,JSONObject json) throws JSONException, SQLException, UnsupportedEncodingException {
		Data data=getPageParameters(request,response,json);
		uploadRecord(request,data,json);
		ObjectDao dao=new ObjectDao();
		dao.saveUploadRecord(data,json);
	}

	private void uploadRecord(HttpServletRequest request,Data data, JSONObject json) throws UnsupportedEncodingException, JSONException {
		String resultMsg="ok";
		int resultCode=0;
		String title=null;
		String limitTime=null;
		String rootPath = this.getServletContext().getRealPath("/");//获取当前文件的绝对路径
		String savePath = "/";
		String downloadUrl="";                              //传到前端的下载链接
		List jsonFile=new ArrayList();
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("file", "doc,docx,pdf,txt,xml,xls,xlsx,xml,ppt,pptx,jpg");//设置上传文件到文件夹file下，文件类型只能为doc docx...这几类
		long maxSize = 1000000000;//设置上传的文件大小最大为1000000000

		if(ServletFileUpload.isMultipartContent(request)){
			File uploadDir = new File(rootPath+savePath);//new一个file 路径为rootPath-savePath
			if(!uploadDir.isDirectory()){
				uploadDir.mkdirs();
			}
			if(!uploadDir.canWrite()){//上传目录file是否有写入的权限
				resultMsg = "1";//上传目录没有写权限
			}else{
				String dirName = "file";//设置上传目录为file
				if(!extMap.containsKey(dirName)){//判断上传目录是否正确
					resultMsg = "2";//目录名不正确
				}else{
					savePath += dirName + "/";
					File saveDirFile = new File(rootPath+savePath);
					if (!saveDirFile.exists()) {
						saveDirFile.mkdirs();
					}
					File dirFile = new File(rootPath+savePath);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					upload.setHeaderEncoding("UTF-8");
					List items = null;
					try {
						items = upload.parseRequest(request);
					} catch (FileUploadException e) {
						e.printStackTrace();
					}
					Iterator itr = items.iterator();
					while (itr.hasNext()) {
						FileItem item = (FileItem) itr.next();
						String fileName = item.getName();
						long fileSize = item.getSize();
						if (!item.isFormField()) {
							//检查文件大小
							if(item.getSize() > maxSize){
								resultMsg = "3";//上传文件大小超过限制
							}else{
								showDebug("[file_upload]fileName="+fileName);
								String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
								if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
									resultMsg = "4";//上传文件扩展名是不允许的扩展名
								}else{
									SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
									String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
									try{
										File uploadedFile = new File(rootPath+savePath, newFileName);
										item.write(uploadedFile);
										downloadUrl = savePath+newFileName;
										resultMsg = "5";//上传文件成功
										//写数据库准备
										HashMap map=new HashMap();
										map.put("download_url",downloadUrl);
										map.put("file_name",newFileName);
										map.put("file_path",rootPath+savePath+newFileName);
										jsonFile.add(map);
									}catch(Exception e){
										resultMsg = "6";//上传文件失败
									}
								}
							}
						}else{
							//如果是FormField，就是前端的device_id,device_name这些
							String fieldName=item.getFieldName();
							String fieldValue=item.getString("UTF-8");
							showDebug("[upload_record][form_field]fieldName="+fieldName+"，fieldValue="+fieldValue);
							if(fieldName.equals("title")){title=fieldValue;};
							if(fieldName.equals("limit_time")){limitTime=fieldValue;};
						}
					}
				}
			}
		}
		showDebug("[upload_record]download_url="+downloadUrl+"&result_code="+resultCode+"&resultMsg="+resultMsg);
		//然后返回给前端
		json.put("result_code",0);
		json.put("result_msg","ok");
		json.put("files", jsonFile);
		json.put("download_url",downloadUrl);
	}
}


















