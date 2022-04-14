var allCheck=0;
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
        initGetInformation();
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
    var initGetInformation = function () {
        getInfoControlEvent();
    }
    function getCheckBox() //得到批量处理的多选框
    {
        var objs = window.document.getElementsByName('url');
        var i;
        var idArray = new Array();
        var j = 0;
        for(i=0;i<objs.length;i++)
        {
            if(objs[i].type=='checkbox')
            {
                if(objs[i].checked == true)
                {
                    idArray[j] = objs[i].value;
                    j = j + 1;
                }
            }
        }

        return idArray;
    }
    function ifAllCheck() //得到批量处理的多选框
    {
        var objs = window.document.getElementsByName('url');
        var i;
        var checkFlag=0;
        for(i=0;i<objs.length;i++)
        {
            if(objs[i].type=='checkbox')
            {
                if(objs[i].checked == false)
                {
                    checkFlag=1;
                }
            }
        }
        if(checkFlag==0){
            return true;
        }
        else{
            return  false;
        }
    }
    //pythonTest ------------------------------------------之后要删掉
    var initPythonTest =function(){
        initPythonTestControlEvent();
    }
    var initPythonTestControlEvent=function(){
        // $('#request').click(function() {onPythonRequest1();});
    }
    //-------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    // var onPythonRequest=function(){
    //     var data={};
    //     data.file="helloworld.py";
    //     console.log("开始请求");
    //
    //     $.post("../../python_test_servlet_action?action=get_python_res",data,function(json){
    //         console.log("请求完了");
    //         console.log(JSON.stringify(json));
    //         if(json.result_code==0){
    //             var list=json.aaData;
    //             console.log("res:"+list);
    //             var html="";
    //             if(list!=undefined && list.length>0){
    //                 for(var i=0;i<list.length;i++){
    //                     var line=list[i];
    //                     html+='<p>'+line+'</p>';
    //                 }
    //             }
    //             $("#showRes").html(html);
    //         }
    //     })
    // }
    var onPythonRequest1=function(){
        var data={};
        data.file="myrun.py";
        console.log("开始请求");
        jQuery('#loading').showLoading();//
        getCheckBox();
        showOkMessage("开始采集，请耐心等候");
        $.post("../../python_test_servlet_action?action=get_Reptile",data,function(json){
            console.log("请求完了");
            showOkMessage("已完成采集");
            jQuery('#loading').hideLoading();
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
    //pythonTest --------------------------------------之后要删掉
    //-----------------------------------------
    var getInfoControlEvent = function () {
        $('#request').click(function() {onRequest();});
        $('#allCheck').click(function() {onAllCheck();});
        $('#submit_button').click(function() {onNameRequest();});
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

    var onAllCheck = function () {
        var check=document.getElementById("allCheck");
        // var checkbox=document.getElementById('checkboxs');//获取div
        var checked=window.document.getElementsByName('url');//获取div下的input
        if(check.className=="my-all-check"){
            check.className="my-no-check";
            allCheck=0;
            for(i=0;i<checked.length;i++){
                checked[i].checked=false;
            }
        }else {
            check.className="my-all-check";
            allCheck=1;
            for(i=0;i<checked.length;i++){
                checked[i].checked=true;
            }
        }

    }
    var onRequest=function(){
        // console.log($("#time_begin").val());
        if($("#problem").val()==""){
            showWrongMessage("您尚未填写信息");
        }else{
            showOkMessage("已提交，感谢您的建议");
        }
    }

    var nameSelect=function (){
        $("#name_select").modal("show");
    }

    var onNameRequest=function(){
        // console.log($("#time_begin").val());
        if($("#inputName").val()==""){
            showWrongMessage("请输入名称");
        } else {
            //截止时间
            $("#name_select").modal("hide");
            var data={};
            data.beginTime=$("#time_begin").val();
            data.endTime=$("#time_end").val();
            data.selectName=$("#inputName").val();
            //机构选择
            if(ifAllCheck()){
                data.selectCity="yes";

            }else {
                data.selectCity="no";
                data.city=JSON.stringify(getCheckBox());
                // showWrongMessage(data.city)
            }

            console.log("开始请求");
            jQuery('#loading').showLoading();//

            // showOkMessage("开始采集，请耐心等候");
            $.post("../../select_info_servlet_action?action=select_info",data,function(json){
                console.log("请求完了");
                jQuery('#loading').hideLoading();

                console.log(JSON.stringify(json));
                if(json.result_code==0){
                    showOkMessage("已添加选择，请至‘我的选择‘中查看");
                }
            })
        }
    }

    return {
        init: function () {
            initPageControl();
        }
    }
}();