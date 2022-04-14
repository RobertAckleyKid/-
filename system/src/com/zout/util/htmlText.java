package com.zout.util;

/**
 * 
 *@class_name：htmlText
 *@comments:定义邮件内容
 *@param:
 *@return:
 */
public class htmlText {
		//  返回页面Html携带的6位随机码
		public static String html(String code) {
			
			String html = "Email地址验证<br/>"+ 
			"这封邮件是由【网络理政系统】发送的。<br/>"+
			"你收到这封邮件是【网络理政系统】进行新用户注册或者登录。<br/>"+
			"账号注册激活声明<br/>"+
			"请将下面的验证码输入到提示框即可：<h3 style='color:red;'>" + code + "</h3><br/>";
			return html;
		}
	}
