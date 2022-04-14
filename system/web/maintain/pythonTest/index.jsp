<%--
  Created by IntelliJ IDEA.
  User: 86150
  Date: 2022/1/28
  Time: 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>python调用</title>
  </head>
  <body>
  <input type="hidden" id="page_id" value="python_test">
    你好 pythonTest
  <button type="button" id="request" >开始测试</button>
  <div id="showRes"></div>
  </body>
</html>
<script type="text/javascript" src="../../js/jquery-1.11.2.min.js"></script>
<script>
  jQuery(document).ready(function() {
    Page.init();
  });
  var Page = function() {
    /*----------------------------------------入口函数  开始----------------------------------------*/
    var initPageControl=function(){
      pageId=$("#page_id").val();
      if(pageId=="python_test"){
        initPythonTest();
      }
    };
    /*----------------------------------------入口函数  结束----------------------------------------*/


    /*----------------------------------------业务函数  开始----------------------------------------*/
    /*------------------------------针对各个页面的入口  开始------------------------------*/
    var initPythonTest =function(){
      initPythonTestControlEvent();
    }
    // var initAccountList =function(){
    //   initAccountListControlEvent();
    //   initPersonalControlEvent();
    //   initPersonalShow();
    //   initAccountListRecordList();
    // }
    /*------------------------------针对各个页面的入口 结束------------------------------*/
    //工具函数
    var getUrlParam=function(name){
      //获取url中的参数
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
      var r = window.location.search.substr(1).match(reg);  //匹配目标参数
      if (r != null) return decodeURI(r[2]); return null; //返回参数值，如果是中文传递，就用decodeURI解决乱码，否则用unescape
    }

    //
    var initPythonTestControlEvent=function(){
      $('#request').click(function() {onPythonRequest();});
    }

    var onPythonRequest=function(){
      var data={};
      data.file="helloworld.py";
      console.log("开始请求");
      $.post("../../python_test_servlet_action?action=get_python_res",data,function(json){
        console.log("请求完了");
        console.log(JSON.stringify(json));
        if(json.result_code==0){
          var list=json.aaData;
          console.log("res:"+list);
          var html="";
          if(list!=undefined && list.length>0){
            for(var i=0;i<list.length;i++){
              var line=list[i];
              html+='<p>'+line+'</p>';
            }
          }
          $("#showRes").html(html);
        }
      })
    }

    //Page return 开始         外面调用page可以访问的代码
    return {
      init: function() {
        initPageControl();
      }
    }
  }();//Page
</script>

