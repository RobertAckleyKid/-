package com.zout.controller;

import com.zout.util.GetMessageCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 *@class_name：SendSms
 *@comments:登录 -发送手机短信验证码
 *@param:phone
 *@return: code
 */
@WebServlet("/sendSMS")
public class SendSms extends HttpServlet {
	
	 /** serialVersionUID*/
	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String phone=req.getParameter("phone");
		//根据获取到的手机号发送验证码
		String code = GetMessageCode.getCode(phone); 
		System.out.println(code);
		resp.getWriter().print(code);
	}
}
