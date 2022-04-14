var selectName=window.sessionStorage.getItem("selectName");
var username=window.sessionStorage.getItem("username");
console.log(selectName);
console.log(username);
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
        initDataProcess();

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
    var showWaitMessage = function(message){
        iziToast.show({
            color: 'rgba(117,192,118,0.75)',
            icon: 'icon-person',
            timeout: 3400,
            title: 'OK',
            message: message,
            position: 'topCenter',
            progressBarColor: 'rgb(114,113,113)',
        });
    }
    var sleep=function (ms){
        for(var t = Date.now();Date.now() - t <= ms;);
    }
    //---------------------------------------
    var initDataProcess = function () {
        DataControlEvent();
        console.log(selectName);
        if (selectName==null){
            showMySelect();
        }else {
            DataShow();
        }

    }
    //-----------------------------------------
    var DataControlEvent = function () {
        $('#login_from #login-btn').click(function () {onLoginBtn();})
        $('#process1').click(function () {onProcess1();})
        $('#process2').click(function () {onProcess2();})
        $('#process3').click(function () {onProcess3();})
        $('#mySelect').click(function () {showMySelect();})
        // $('#process4').click(function () {onProcess4();})

    }
    //------------------------------------------

    //-------------------------------------
    var showMySelect=function() {
        // jQuery('#loading').showLoading();
        var data={};
        data.username=username;
        $.post("../../select_info_servlet_action?action=get_mySelect",data,function(json){
            console.log(JSON.stringify(json));
            console.log(json);
            if(json.result_code==0){
                var list=json.aaData;
                var html="";
                if(list!=undefined && list.length>0){
                    for(var i=0;i<list.length;i++){
                        var record=list[i];
                        console.log(record.selectName);
                        var ss=record.id;

                        html+=' <div class="templatemo-content-widget no-padding  white-bg col-2" style="width:28%;float: left;background-color: rgba(187,209,131,0.75);padding: 2px;text-align: center">\n' +
                            '                      <a href="JavaScript:Page.processSelect('+ss+');">\n' +
                            '                        <text style="color: #0d1217">'+record.selectName+'</text>\n' +
                            '                      </a>\n' +
                            '                    </div>';
                    }
                }
                $("#choose_content").html(html);
                // jQuery('#loading').hideLoading();
                $("#choose_select").modal("show");
            }
            else {
                showWrongMessage("您还未添加选择数据，请先行添加数据");
            }
        })
    }
    var processSelect=function(id){
        console.log(id);
        sessionStorage.setItem("selectName",id);
        selectName=sessionStorage.getItem("selectName");
        console.log(selectName);
        $("#choose_select").modal("hide");
        DataShow();
        // window.location.href="../dateProcessing/textProcessing.html";
    }



    var javaSql="";
    var pythonSql="";
    var DataShow = function () {
        jQuery('#loading').showLoading();
        var data={};
        data.selectName=selectName;
        data.username=window.sessionStorage.getItem("username");/////////////////////////////
        $.post("../../process_info_servlet_action?action=get_InfoSql", data, function (json) {
            console.log(JSON.stringify(json));
            if(json.result_code==0) {
                var list = json.aaData;
                if(list!=undefined && list.length>0) {
                    record=list[0];
                    javaSql=record.javaSql;
                    $('#select_name').html(record.selectName);
                    sessionStorage.setItem("transName",record.selectName);
                    // pythonSQl=record.pythonSql;
                    sessionStorage.setItem("pythonSql",record.pythonSql)
                    showInitInfo();
                }
            }
        })

    }


    var html="";
    var html1="";
    var showInitInfo=function (){
        var data={};
        data.javaSql=javaSql;
        $.post("../../process_info_servlet_action?action=get_InfoInit", data, function (json) {
            console.log(JSON.stringify(json));
            if(json.result_code==0) {
                html="";
                html1="";
                var list = json.aaData;
                var j=0;
                if(list!=undefined && list.length>0) {
                    // html+='<table class="table templatemo-user-table table-striped table-bordered table-hover" id="database">\n' +
                    html+='<div class="form-group">\n' +
                        // '        <button type="button" class="templatemo-blue-button">全部统计</button>\n' +
                        '        <button type="button" class="templatemo-white-button" id="process1" style="margin-right: 3vmin ">关键词统计</button>\n' +
                        '        <button type="button" class="templatemo-white-button" id="process2" style="margin-right: 3vmin " >词频统计</button>\n' +
                        '        <button type="button" class="templatemo-white-button" id="process3" style="margin-right: 3vmin ">主题词-分词结果</button>\n' +
                        // '        <button type="button" class="templatemo-white-button" id="process4" style="margin-right: 3vmin ">主题词</button>\n' +
                        '      </div>';
                    html+='<table class="table templatemo-user-table table-striped table-bordered table-hover" id="database" style="white-space:nowrap;">\n' +
                        '        <thead>\n' +
                        '        <tr>\n' +
                        '          <td >序号</td>\n' +
                        '          <td >标题</td>\n' +
                        '          <td >类别1</td>\n' +
                        '          <td >类别2</td>\n' +
                        '          <td >市</td>\n' +
                        '          <td >区</td>\n' +
                        '          <td >提问内容</td>\n' +
                        '          <td >提问长度</td>\n' +
                        '          <td >提问时间</td>\n' +
                        '          <td id="need_click">回答个数</td>\n' +
                        '          <td >回答部门</td>\n' +
                        '          <td >回答内容</td>\n' +
                        '          <td >回答长度</td>\n' +
                        '          <td >回答时间</td>\n' +
                        '          <td >网址链接</td>\n' +
                        '        </tr>\n' +
                        '        </thead>\n' +
                        '        <tbody id="database_content" name="database_content">\n' +
                        '        </tbody>\n' +
                        '      </table>';
                    $('#table_show').hide();
                    $('#process_show').hide();
                    $('#table_show').html(html);

                    $('#total_number').html("共有"+list.length+"条记录");
                    sessionStorage.setItem("totalNum",list.length);


                    for(var i=0;i<list.length;i++) {
                        record = list[i];
                        j=i+1;
                        html1+= '<tr>';
                        html1 = html1 + '<td>' + j + '</td>';
                        html1 = html1 + '<td>' + record.name + '</td>';
                        html1 = html1 + '<td>' + record.class01 + '</td>';
                        html1 = html1 + '<td>' + record.class02 + '</td>';
                        html1 = html1 + '<td>' + record.city + '</td>';
                        html1 = html1 + '<td>' + record.county + '</td>';
                        html1 = html1 + '<td>' + record.askContent + '</td>';
                        html1 = html1 + '<td>' + record.askLen + '</td>';
                        html1 = html1 + '<td>' + record.askTime + '</td>';
                        html1 = html1 + '<td>' + record.ansOrNot + '</td>';
                        if(record.ansOrNot=="0"){
                            html1 = html1 + '<td>' + '-' + '</td>';
                            html1 = html1 + '<td>' + '-' + '</td>';
                            html1 = html1 + '<td>' + '-' + '</td>';
                            html1 = html1 + '<td>' + '-' + '</td>';
                        }else{
                            html1 = html1 + '<td>' + record.ansDpt + '</td>';
                            html1 = html1 + '<td>' + record.ansContent + '</td>';
                            html1 = html1 + '<td>' + record.ansLen + '</td>';
                            html1 = html1 + '<td>' + record.ansTime + '</td>';
                        }
                        html1 = html1 + '<td><a href="'+record.scUrl+'">' + record.scUrl + '</a></td>';
                        html1 = html1 + '</tr>';
                    }
                    $('#database_content').html(html1);
                    // table.columns.adjust();
                    $('#database').dataTable(
                        { "scrollX":true,
                        "bAutoWidth":true,
                        }
                    );
                    jQuery('#loading').hideLoading();
                    // table.columns.adjust();
                    $('#table_show').show();

                    // table.columns.adjust();
                    tableclick();
                    DataControlEvent();
                }
            }
        })
    }
    var tableclick=function (){
        setTimeout(function() {
            // IE
            if(document.all) {
                document.getElementById("need_click").click();
            }
            // 其它浏览器
            else {
                var e = document.createEvent("MouseEvents");
                e.initEvent("click", true, true);
                document.getElementById("need_click").dispatchEvent(e);
            }
        }, 1);
    }
    var  onProcess1=function (){
        jQuery('#loading').showLoading();
        var data={};
        // data.pythonSql=pythonSql;
        data.pythonSql=sessionStorage.getItem("pythonSql");
        data.username=username;
        // alert(data.pythonSql);
        $.post("../../python_test_servlet_action?action=get_process1", data, function (json) {
            console.log(json);
            var fileName=json.fileName;
            ShowProcess1Res(fileName);
        })
    }
    var  onProcess2=function (){
        // jQuery('#loading').showLoading();
        showWaitMessage("正在处理中，请稍等")
        var data={};
        // data.pythonSql=pythonSql;
        data.pythonSql=sessionStorage.getItem("pythonSql");
        data.username=username;
        // alert(data.pythonSql);
        $.post("../../python_test_servlet_action?action=get_process2", data, function (json) {
            console.log(json);
            var askfileName=json.askFileName;
            var ansfileName=json.ansFileName;
            sessionStorage.setItem("askfileName",askfileName);
            sessionStorage.setItem("ansfileName",ansfileName);
            ShowProcess2Res(askfileName,ansfileName);
        })
    }
    var  onProcess3=function (){
        jQuery('#loading').showLoading();

        var tableshow=document.getElementById("table_show");
        tableshow.style.width="82vmax";

        var data={};
        // data.pythonSql=pythonSql;
        data.pythonSql=sessionStorage.getItem("pythonSql");
        data.username=username;
        // alert(data.pythonSql);
        $.post("../../python_test_servlet_action?action=get_process3", data, function (json) {
            console.log(json);
            var fileName=json.fileName;
            ShowProcess3Res(fileName);
        })
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    var process1html=""
    var  ShowProcess1Res=function (fileName){
        process1html="";
        $('#process_show').html(process1html);
        jQuery('#loading').hideLoading();
        // showOkMessage(fileName);
        var fileUrl="/processfile/"+fileName;
        // showOkMessage(fileUrl);
        Papa.parse(fileUrl, {
            download: true,
            complete: function(results) {
                console.log("results:");
                console.log(results);
                var data = results.data;
                process1html+='<div class="media margin-bottom-30">\n' +
                    '          <div class="media-left padding-right-25">\n' +
                    '          </div>\n' +
                    '          <div class="media-body">\n' +
                    '            <br>\n' +
                    '            <h2 class="media-heading text-uppercase blue-text">关键词统计<a href="'+fileUrl+'"><img src="../../images/export.png" style="width: 20px;height: 20px;position: fixed;right: 20px"></a></h2>\n' +
                    '          </div>\n' +
                    '        </div>\n' +
                    '        <div class="table-responsive my-roll" style="height:77vmin;overflow-y: auto;overflow-x: hidden" >\n' +
                    '          <table class="table">\n' +
                    '            <tbody>'
                for(var i = 1, j = data.length-1; i < j; i++) {
                    var item = data[i];
                    console.log(item[1]);
                    console.log(item[2]);
                    process1html+='<tr>\n' +
                        '              <td>\n'
                    if(i%2==1){
                        process1html+=   '                <div class="circle green-bg"></div>\n'
                    }else {
                        process1html+=   '                <div class="circle" style="background-color: rgba(103,181,185,0.44)"></div>\n'
                    }
                    process1html+=   '              </td>\n' +
                        '              <td>'+item[1]+'</td>\n' +
                        '              <td>'+item[2]+'</td>\n' +
                        '            </tr>'
                    if(i+1==j){
                        process1html+=' </tbody>\n' +
                            '          </table>\n' +
                            '        </div>\n' +
                            '      </div>'
                    }
                }
                $('#process_show').hide();
                $('#process_show').html(process1html);
                var tableshow=document.getElementById("table_show");
                tableshow.style.width="63vmax";
                $('#process_show').show();
                // $('#table tbody').append(html);
            }
        });
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    var process2html="";
    var  ShowProcess2Res=function (askfileName,ansFileName){
        process1html="";
        $('#process_show').html("");
        // jQuery('#loading').hideLoading();
        // 展示第一个
        var fileUrl="/processfile/"+askfileName;
        // showOkMessage(fileUrl);
        Papa.parse(fileUrl, {
            download: true,
            complete: function(results) {
                console.log("results:");
                console.log(results);
                var data = results.data;
                process1html+='<div class="media margin-bottom-10">\n' +
                    '          <div class="media-left padding-right-25">\n' +
                    '          </div>\n' +
                    '          <div class="media-body">\n' +
                    '            <br>\n' +
                    '            <h2 class="media-heading text-uppercase blue-text">分词-词频统计-提问<a href="'+fileUrl+'"><img src="../../images/export.png" style="width: 20px;height: 20px;position: fixed;right: 20px"></a></h2>\n' +
                    '          </div>\n' +
                    '        </div>\n' +
                    '        <div class="table-responsive my-roll" style="height:35vmin;overflow-y: auto;overflow-x: hidden" >\n' +
                    '          <table class="table">\n' +
                    '            <tbody>'
                for(var i = 1, j = data.length-1; i < j; i++) {
                    var item = data[i];
                    process1html+='<tr>\n' +
                        '              <td>\n'
                    if(i%2==1){
                        process1html+=   '                <div class="circle green-bg"></div>\n'
                    }else {
                        process1html+=   '                <div class="circle" style="background-color: rgba(103,181,185,0.44)"></div>\n'
                    }
                    process1html+=   '              </td>\n'
                        if(item[1].length>=5){}
                        else {
                            process1html+=        '              <td>'+item[1]+'</td>\n'
                        }
                    process1html+=    '              <td>'+item[2]+'</td>\n' +
                        '            </tr>'
                    if(i+1==j){
                        process1html+=' </tbody>\n' +
                            '          </table>\n' +
                            '        </div>\n' +
                            '      </div>'
                    }
                }
                // $('#process_show').hide();
                $('#process_show').html(process1html);
                ShowProcess2Res2(ansFileName);
                // $('#process_show').show();
                // $('#table tbody').append(html);
            }
        });
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////
    var process2html2=""
    var  ShowProcess2Res2=function (ansFileName){
        process2html2=""
        // 展示第二个
        var fileUrl="/processfile/"+ansFileName;
        // showOkMessage(fileUrl);
        Papa.parse(fileUrl, {
            download: true,
            complete: function(results) {
                console.log("results:");
                console.log(results);
                var data = results.data;
                process2html2+='<div class="media margin-bottom-10">\n' +
                    '          <div class="media-left padding-right-25">\n' +
                    '          </div>\n' +
                    '          <div class="media-body">\n' +
                    '            <br>\n' +
                    '            <h2 class="media-heading text-uppercase blue-text">分词-词频统计-回答<a href="'+fileUrl+'"><img src="../../images/export.png" style="width: 20px;height: 20px;position: fixed;right: 20px"></a></h2>\n' +
                    '          </div>\n' +
                    '        </div>\n' +
                    '        <div class="table-responsive my-roll" style="height:35vmin;overflow-y: auto;overflow-x: hidden" >\n' +
                    '          <table class="table">\n' +
                    '            <tbody>'
                for(var i = 1, j = data.length-1; i < j; i++) {
                    var item = data[i];
                    process2html2+='<tr>\n' +
                        '              <td>\n'
                    if(i%2==1){
                        process2html2+=   '                <div class="circle green-bg"></div>\n'
                    }else {
                        process2html2+=   '                <div class="circle" style="background-color: rgba(103,181,185,0.44)"></div>\n'
                    }
                    process2html2+=   '              </td>\n'
                    if(item[1].length>=5){}
                    else {
                        process2html2+=     '              <td>'+item[1]+'</td>\n'
                    }
                    process2html2+=   '              <td>'+item[2]+'</td>\n' +
                        '            </tr>'
                    if(i+1==j){
                        process2html2+=' </tbody>\n' +
                            '          </table>\n' +
                            '        </div>\n' +
                            '      </div>'
                    }
                }
                // $('#process_show').hide();
                $('#process_show').append(process2html2);
                var tableshow=document.getElementById("table_show");
                tableshow.style.width="63vmax";
                $('#process_show').show();
                // $('#table tbody').append(html);
            }
        });
    }

    var process3html=""
    var processData=""
    var  ShowProcess3Res=function (fileName){
        process3html="";
        jQuery('#loading').hideLoading();
        $('#table_show').html(process3html);
        // showOkMessage(fileName);
        var fileUrl="/processfile/"+fileName;
        // showOkMessage(fileUrl);
        Papa.parse(fileUrl, {
            download: true,
            complete: function(results) {
                console.log("results:");
                console.log(results);
                var data = results.data;
                process3html+='<div class="form-group">\n' +
                    // '        <button type="button" class="templatemo-blue-button">全部统计</button>\n' +
                    '        <button type="button" class="templatemo-white-button" id="process1" style="margin-right: 3vmin ">关键词统计</button>\n' +
                    '        <button type="button" class="templatemo-white-button" id="process2" style="margin-right: 3vmin " >词频统计</button>\n' +
                    '        <button type="button" class="templatemo-white-button" id="process3" style="margin-right: 3vmin ">主题词-分词结果</button>\n' +
                    // '        <button type="button" class="templatemo-white-button" id="process4" style="margin-right: 3vmin ">主题词</button>\n' +
                    '      </div>';
                process3html+='<table class="table templatemo-user-table table-striped table-bordered table-hover" id="database" style="white-space:nowrap;">\n' +
                    '        <thead>\n' +
                    '        <tr>\n' +
                    '          <td >序号</td>\n' +
                    '          <td >标题</td>\n' +
                    '          <td style="background-color: rgb(243,168,95)">提问主题词</td>\n' +
                    '          <td style="background-color: rgb(243,168,95)">回答主题词</td>\n' +
                    '          <td >类别1</td>\n' +
                    '          <td >类别2</td>\n' +
                    '          <td >市</td>\n' +
                    '          <td >区</td>\n' +
                    '          <td >提问内容</td>\n' +
                    '          <td style="background-color:rgb(243,168,95)">提问分词</td>\n' +
                    '          <td >提问长度</td>\n' +
                    '          <td >提问时间</td>\n' +
                    '          <td id="need_click">回答个数</td>\n' +
                    '          <td >回答部门</td>\n' +
                    '          <td >回答内容</td>\n' +
                    '          <td style="background-color:rgb(243,168,95)">回答分词</td>\n' +
                    '          <td >回答长度</td>\n' +
                    '          <td >回答时间</td>\n' +
                    '          <td >网址链接</td>\n' +
                    '        </tr>\n' +
                    '        </thead>\n' +
                    '        <tbody id="database_content" name="database_content">\n' +
                    '        </tbody>\n' +
                    '      </table>';
                $('#table_show').hide();
                $('#process_show').hide();
                $('#table_show').html(process3html);
                processData=""
                var k=1;
                for(var i = 1, j = data.length-1; i < j; i++) {
                    var record = data[i];
                    console.log(record);
                // for(var i=0;i<list.length;i++) {
                //     record = list[i];
                    k=i;
                    processData+= '<tr>';
                    processData = processData + '<td>' +k + '</td>';
                    processData = processData + '<td>' + record[1] + '</td>';
                    processData = processData + "<td>" + record[21] + "</td>";
                    console.log(record[21]+" 主题词 "+record[22])
                    if(record[22]==""){
                        processData = processData + "<td>--</td>";
                    }else {
                        processData = processData + "<td>" + record[22] + "</td>";
                    }
                    processData = processData + '<td>' + record[2] + '</td>';
                    processData = processData + '<td>' + record[3] + '</td>';
                    processData = processData + '<td>' + record[4] + '</td>';
                    processData = processData + '<td>' + record[5] + '</td>';
                    processData = processData + '<td>' + record[6] + '</td>';
                    processData = processData + '<td>' + record[19] + '</td>';
                    processData = processData + '<td>' + record[7] + '</td>';
                    processData = processData + '<td>' + record[8] + '</td>';
                    processData = processData + '<td>' + record[9] + '</td>';
                    if(record[9]=="0"){
                        processData = processData + '<td>' + '-' + '</td>';
                        processData = processData + '<td>' + '-' + '</td>';
                        processData = processData + '<td>' + '-' + '</td>';
                        processData = processData + '<td>' + '-' + '</td>';
                        processData = processData + '<td>' + '-' + '</td>';
                    }else{
                        processData = processData + '<td>' + record[10] + '</td>';
                        processData = processData + '<td>' + record[11] + '</td>';
                        processData = processData + '<td>' + record[12] + '</td>';
                        processData = processData + '<td>' + record[20] + '</td>';
                        processData = processData + '<td>' + record[13] + '</td>';
                    }
                    processData = processData + '<td><a href="'+record[18]+'">' + record[18] + '</a></td>';
                    processData = processData + '</tr>';
                }
                $('#database_content').html(processData);
                // table.columns.adjust();
                $('#database').dataTable(
                    { "scrollX":true,
                        "bAutoWidth":true,
                    }
                );

                jQuery('#loading').hideLoading();
                var tableshow=document.getElementById("table_show");
                tableshow.style.width="82vmax";
                // table.columns.adjust();
                $('#table_show').show();

                // table.columns.adjust();
                tableclick();
                DataControlEvent();
                // $('#process_show').show();
                // $('#table tbody').append(html);
            }
        });
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