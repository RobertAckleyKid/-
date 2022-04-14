var username=sessionStorage.getItem("username");
jQuery(document).ready(function () {
    var loginState=window.sessionStorage.getItem("login-state");
    if(loginState=="ok"){
        Page.init();
    }else {
        window.location.href= '../login/login.html';
    }

});
var Page = function () {
    var initPageControl = function () {
        //针对不同page_id的不同初始化
        initMySelect();
        //initPythonTest();

        //初始化弹出框
        iziToast.settings({
            timeout: 1000,
            resetOnHover: true,
            icon: 'material-icons',
            transitionIn: 'flipInX',
            transitionOut: 'flipOutX',
            position: 'topCenter',
            onOpen: function(){
                console.log('callback abriu!');
            },
            onClose: function(){
                console.log("callback fechou!");
            }
        });

    };
    //---------------------------------------
    var initMySelect = function () {
        MySelectControlEvent();
        showMySelect();
    }
    //-----------------------------------------
    var MySelectControlEvent = function () {
        $('#request').click(function() {onRequest();});
        $('#allCheck').click(function() {onAllCheck();});
    }
    //------------------------------------------------------------------
    var showWrongMessage = function(message){
        iziToast.show({
            color: 'rgba(243,130,105,0.75)',
            icon: 'icon-person',
            title: '错误',
            message: message,
            position: 'topCenter',
            progressBarColor: 'rgb(114,113,113)',

        });
    }
    var showOkMessage = function(message){
        iziToast.show({
            color: 'rgba(117,192,118,0.75)',
            icon: 'icon-person',
            timeout: 1500,
            title: 'OK',
            message: message,
            position: 'topCenter',
            progressBarColor: 'rgb(114,113,113)',
        });
    }
    var sleep=function (ms){
        for(var t = Date.now();Date.now() - t <= ms;);
    }
    //-------------------------------------------------------------------
    var showMySelect=function() {
        jQuery('#loading').showLoading();
        var data={};
        data.username=username;
        $.post("../../select_info_servlet_action?action=get_mySelect",data,function(json){
            // console.log(JSON.stringify(json));
            console.log(json);
            if(json.result_code==0){
                var list=json.aaData;
                var html="";
                if(list!=undefined && list.length>0){
                    for(var i=0;i<list.length;i++){
                        var record=list[i];
                        // html=html+'<div class="templatemo-flex-row flex-content-row">';
                        if(record.process=="before"){
                            html=html+'<div class="templatemo-content-widget white-bg col-2" style="width:19%;height:180px;float: left;margin-left: 3%">';
                            html=html+'	<img src="../../images/待处理.png" style="width:70px;height: 70px;float: right;right: 20px;margin-top: -20px">';
                        }else{
                            // html=html+'<div class="templatemo-content-widget white-bg col-2" style="width: 19%;height:180px;float: left;margin-left: 3%;background-color: rgba(110,183,127,0.37)">';
                            html=html+'<div class="templatemo-content-widget white-bg col-2" style="width: 19%;height:180px;float: left;margin-left: 3%;">';

                            html=html+'	<img src="../../images/已处理.png" style="width:70px;height: 70px;float: right;right: 20px;margin-top: -20px">';
                        }
                        html=html+"<a href=\"JavaScript:Page.processSelect('"+record.id+"');\">";
                        // html=html+"<a href=\"window.location.href='../dateProgressing/';\">";
                        html=html+'	<h2 className="templatemo-inline-block" style="color: #0d1217">'+record.selectName+'</h2>';
                        html=html+'	<hr>';
                        if(record.process=="before"){
                            html=html+'<h4 style="color: #757575">'+record.addTime+'</h4><br>';
                        }else{
                            html=html+'<h4 style="color: #757575">'+record.addTime+'</h4><br>';
                        }
                        html=html+'<p style="color: #0d638f">点击查看</p>';
                        html=html+"</a>";
                        html=html+"</div>";

                    }
                }
                jQuery('#loading').hideLoading();
                $("#trade_stat_div").html(html);
            }
            else {
                showWrongMessage("您还为添加选择数据！");
            }
        })
    }
    var processSelect=function(id){
        sessionStorage.setItem("selectName",id);
        var data={};
        data.id=sessionStorage.getItem("selectName");
        $.post("../../select_info_servlet_action?action=change_status",data,function(json){
            // console.log(JSON.stringify(json));
            console.log(json);
            if(json.result_code==0){
                window.location.href="../dateProcessing/textProcessing.html";
            }
            else {
                showWrongMessage("系统错误！请稍后再试");
            }
        })

    }

    return {
        init: function () {
            initPageControl();
        },
        processSelect:function(id){
            processSelect(id);
        },
    }
}();