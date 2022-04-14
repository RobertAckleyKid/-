var username;
jQuery(document).ready(function () {
    Page.init();
});
var Page = function () {
    var initPageControl = function () {
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
        //
        let pageId = $("#page_id").val();
        if (pageId == 'login') {
            window.sessionStorage.setItem('login-state', '0');
            initLogin();
        }
        if (pageId == 'email') {
            window.sessionStorage.setItem('login-state', '0');
            initEmailLogin();
        }
        if (pageId == 'register') {
            window.sessionStorage.setItem('login-state', '0');
            initRegister();
        }
    };
    //---------------------------------------
    var initLogin = function () {
        loginControlEvent();
    }

    var initEmailLogin = function () {
        EmailLoginControlEvent();
    }
    var initRegister = function () {
        registerControlEvent();
    }
    //-----------------------------------------
    var loginControlEvent = function () {
        $('#login_from #login-btn').click(function () {
            onLoginBtn();
        })
    }
    var EmailLoginControlEvent = function () {
        $('#email-from #login-btn').click(function () {onEailLoginBtn();});
        $('#email-from #get_code').click(function () {onEmailGetCodeBtn();});

    }
    var registerControlEvent = function () {
        $('#register-btn').click(function () {onRegisterBtn();});
        $('#getCode').click(function () {onGetCodeBtn();});
    }
    //------------------------------------------
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
            // buttons: [
            //     ['<button>Ok</button>', function (instance, toast) {
            //         alert("Hello world!");
            //     }],
            //     ['<button>Close</button>', function (instance, toast) {
            //         instance.hide({ transitionOut: 'fadeOutUp' }, toast);
            //     }]
            // ]
        });
    }
    var sleep=function (ms){
        for(var t = Date.now();Date.now() - t <= ms;);
    }
    //-------------------------------------
    var onLoginBtn = function () {
        let username = $('#login_from #username').val();
        let password = $('#login_from #password').val();
        if (username == null || username == '') {
            showWrongMessage("用户名不能为空!");
        } else if (password == null || password == '') {
           showWrongMessage('密码不能为空!');
        } else {
            let url = '../../login_file_servlet_action';
            let data = {};
            data.action = 'login';
            data.username = username;
            data.password = password;
            $.post(url, data, function (json) {
                console.log(json);
                if (json.result_code == 0) {
                    showOkMessage(json.result_msg);
                    window.sessionStorage.setItem('login-state', 'ok');
                    window.sessionStorage.setItem('username', json.aaData[0].username);
                    // window.sessionStorage.setItem('role', json.aaData[0].role);
                    window.location.href = '../main/index.html';
                    // setLoginTable(json.aaData[0].id);
                } else {
                    showWrongMessage(json.result_msg);
                }
            })
        }
    }

    var onEailLoginBtn = function () {
        let email = $('#email').val();
        let code = $('#code').val();
        console.log(email+"::"+code);
        if (email == null || email == '') {
            showWrongMessage("请填写邮箱!");
        } else if (code == null || code == '') {
            showWrongMessage('请填写验证码!');
        } else {
            let url = '../../login_file_servlet_action';
            let data = {};
            data.action = 'loginEmail';
            data.email = email;
            data.code = code;
            $.post(url, data, function (json) {
                console.log(json);
                if (json.result_code == 0) {
                    window.sessionStorage.setItem('login-state', 'ok');
                    window.sessionStorage.setItem('username', username);
                    // window.sessionStorage.setItem('username', json.aaData[0].username);
                    // window.sessionStorage.setItem('username', json.aaData[0].username);
                    // window.sessionStorage.setItem('role', json.aaData[0].role);
                    window.location.href = '../main/index.html';
                } else {
                    showWrongMessage(json.result_msg);
                }
            })
        }
    }
    var onEmailGetCodeBtn = function () {
        console.log($("#email").val())
        if($("#email").val()) {
            let url = "../../login_file_servlet_action";
            let data = {};
            data.action = "find_email";
            data.email = $('#email').val();
            $.post(url, data, function (json) {
                console.log(JSON.stringify(json));
                if (json.result_code == 0) {
                    username=json.aaData[0].username
                    GetLoginCode();
                } else {
                    showWrongMessage(json.result_msg);
                }
            });
        }else{
            showWrongMessage("请填写邮箱");
        }
    }
    var setLoginTable = function (id) {
        let url = '../../general_file_servlet_action';
        let data = {};
        data.id = id;
        data.action = 'set_login_table';
        $.post(url, data, function (json) {
            console.log(json);
        })
    }
    var onGetCodeBtn = function () {
        if($("#register_from #email").val()) {
            let url = "../../login_file_servlet_action";
            let data = {};
            data.action = "check_email";
            data.email = $('#register_from #email').val();
            $.post(url, data, function (json) {
                console.log(JSON.stringify(json));
                if (json.result_code == 0) {
                    //alert(json.result_msg);
                    GetCode();
                } else {
                    //alert(json.result_msg);
                    showWrongMessage(json.result_msg);
                }
            });

        }else{
            showWrongMessage("请填写邮箱");
        }
    }
    var GetCode = function (){
        if($("#register_from #email").val()){
            console.log($("#register_from #email").val());
            // showOkMessage("正在发送验证码，请等候。")
            $.ajax({
                type:"POST",
                // url :"SendEmailServlet?random"+Math.random(),
                url :"../../register_email_servlet_action?random"+Math.random(),
                data:{
                    email:$("#register_from #email").val(),
                },
                success:function(data){
                    showOkMessage("验证码发送成功，请注意查收。")
                },
            })
        }else{
            showWrongMessage("请填写邮箱")
        }
    }

    var GetLoginCode = function (){
        if($("#email").val()){
            console.log($("#email").val());
            //showOkMessage("正在发送验证码，请等候。")
            $.ajax({
                type:"POST",
                // url :"SendEmailServlet?random"+Math.random(),
                url :"../../register_email_servlet_action?random"+Math.random(),
                data:{
                    email:$("#email").val(),
                },
                success:function(data){
                    showOkMessage("验证码发送成功，请注意查收。")
                },
            })
        }else{
            showWrongMessage("请填写邮箱")
        }
    }
    var onRegisterBtn = function () {
        let url = "../../login_file_servlet_action";
        let data = {};
        data.action = "register";
        data.username = $('#register_from #username').val();
        data.password = $('#register_from #password').val();
        let confirmPassword = $('#register_from #password2').val();
        data.email = $('#register_from #email').val();
        data.code = $('#register_from #code').val();
        if (data.username == "" || data.password == "" || data.confirmPassword == "" || data.email == "" ) {
            showWrongMessage("内容不能为空！");
        } else if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$/.test(data.password)) {
            showWrongMessage("密码必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-16之间");
        } else if (data.password != confirmPassword) {
            showWrongMessage("两次输入密码不一致！");
        } else if( data.code==""){
            showWrongMessage("请输入验证码！");
        }else {
            $.post(url, data, function (json) {
                console.log(JSON.stringify(json));
                if (json.result_code == 0) {
                    $.ajaxSettings.async = false;//同步的方式
                    showOkMessage(json.result_msg);
                    var url = 'login.html';
                    setTimeout("window.location.href = '" + url + "'",2000);//500毫秒后跳转
                    $.ajaxSettings.async = true;//异步的方式
                } else {
                    showWrongMessage(json.result_msg);
                }
            });
        }
    }
    return {
        init: function () {
            initPageControl();
        }
    }
}();