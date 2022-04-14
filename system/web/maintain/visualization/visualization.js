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
        initVisual();

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
    var initVisual = function () {
        ViewControlEvent();
        console.log("selectName:"+selectName);
        if (selectName==null){
            showMySelect();
        }else {
            DataViewShow();
        }

    }
    //-----------------------------------------
    var ViewControlEvent = function () {
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
        sessionStorage.setItem("status","new");
        console.log(selectName);
        $("#choose_select").modal("hide");
        DataViewShow();
        // window.location.href="../dateProcessing/textProcessing.html";
    }



    var javaSql="";
    var pythonSql="";
    var DataViewShow = function () {
        jQuery('#loading').showLoading();

        var ansfileName=sessionStorage.getItem("ansfileName");
        var askfileName=sessionStorage.getItem("askfileName");
        console.log("ansfileName:"+ansfileName)
        console.log("askfileName:"+askfileName)
        if(ansfileName==null){
            getViewCloud();
        }else{
            var status=sessionStorage.getItem("status");
            if(status=="new"){
                getViewCloud();
                sessionStorage.setItem("status","ok");
            }
            var transName=sessionStorage.getItem("transName");
            $('#select_name').html(transName);
            ShowCloud(askfileName,ansfileName);
        }

    }
    var getViewCloud=function (){
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
                    // pythonSQl=record.pythonSql;
                    $('#select_name').html(record.selectName);
                    sessionStorage.setItem("transName",record.selectName);
                    sessionStorage.setItem("pythonSql",record.pythonSql)
                    // showInitInfo();
                    onProcess2();
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
        var data={};
        data.pythonSql=sessionStorage.getItem("pythonSql");
        data.username=username;
        // alert(data.pythonSql);
        $.post("../../python_test_servlet_action?action=get_process2", data, function (json) {
            console.log(json);
            var askfileName=json.askFileName;
            var ansfileName=json.ansFileName;
            sessionStorage.setItem("askfileName",askfileName);
            sessionStorage.setItem("ansfileName",ansfileName);
            ShowCloud(askfileName,ansfileName);
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
    var cloudData=[];
    var askBarX=[];
    var askBarY=[];
    var  ShowCloud=function (askfileName,ansFileName){
        $("#loading").hideLoading();
        // 展示第一个
        var fileUrl="/processfile/"+askfileName;
        Papa.parse(fileUrl, {
            download: true,
            complete: function(results) {
                // console.log("results:");
                // console.log(results);
                var data = results.data;
                cloudData=[];
                // if(data.length-1>)
                // for(var i = 1, j = data.length-1; i < j; i++) {
                console.log(data.length);
                var j=210;
                if(data.length<j){
                    j=data.length;
                }
                askBarX=[];
                askBarY=[];
                for(var i = 1; i < j; i++) {
                    var item = data[i];
                    var cloud={};
                    cloud.name=item[1];
                    cloud.value=item[2];
                    cloudData.push(cloud);
                    if(i<101) {
                        askBarX.push(item[1]);
                        askBarY.push(item[2]);
                    }
                }
                console.log(cloudData);
                var data={
                    image:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAooAAAKKCAYAAAC6dHqMAAAACXBIWXMAAAsTAAALEwEAmpwYAAAJT2lUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyIgeG1sbnM6ZXhpZj0iaHR0cDovL25zLmFkb2JlLmNvbS9leGlmLzEuMC8iIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06RG9jdW1lbnRJRD0iYWRvYmU6ZG9jaWQ6cGhvdG9zaG9wOmI4NzFiZDljLTBhYWQtNGI0NC05NmQ3LTI3MWIwMDRmMmIxNiIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDowN2MwMTk0MS0wMDIyLTdhNGQtODQ3NS04Y2Q2MTRhOTEwZTciIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0iNTlDRjhDMTgzRjc5RjQ5MzYzMUFENjkxNzE3RDQ4MUMiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIiBwaG90b3Nob3A6Q29sb3JNb2RlPSIzIiBwaG90b3Nob3A6SUNDUHJvZmlsZT0iIiB0aWZmOkltYWdlV2lkdGg9IjY1MCIgdGlmZjpJbWFnZUxlbmd0aD0iNjUwIiB0aWZmOlBob3RvbWV0cmljSW50ZXJwcmV0YXRpb249IjIiIHRpZmY6U2FtcGxlc1BlclBpeGVsPSIzIiB0aWZmOlhSZXNvbHV0aW9uPSI3Mi8xIiB0aWZmOllSZXNvbHV0aW9uPSI3Mi8xIiB0aWZmOlJlc29sdXRpb25Vbml0PSIyIiBleGlmOkV4aWZWZXJzaW9uPSIwMjIxIiBleGlmOkNvbG9yU3BhY2U9IjY1NTM1IiBleGlmOlBpeGVsWERpbWVuc2lvbj0iNjUwIiBleGlmOlBpeGVsWURpbWVuc2lvbj0iNjUwIiB4bXA6Q3JlYXRlRGF0ZT0iMjAyMi0wMy0yOVQyMzoxNToxOCswODowMCIgeG1wOk1vZGlmeURhdGU9IjIwMjItMDMtMjlUMjM6Mjg6NDgrMDg6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMjItMDMtMjlUMjM6Mjg6NDgrMDg6MDAiPiA8eG1wTU06SGlzdG9yeT4gPHJkZjpTZXE+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJzYXZlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo2ZTJiMjMwNC1iYzZhLWQ3NGMtYjM5MC1hNmRlN2JhOTg5YjAiIHN0RXZ0OndoZW49IjIwMjItMDMtMjlUMjM6Mjg6NDgrMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIgc3RFdnQ6Y2hhbmdlZD0iLyIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY29udmVydGVkIiBzdEV2dDpwYXJhbWV0ZXJzPSJmcm9tIGltYWdlL2pwZWcgdG8gaW1hZ2UvcG5nIi8+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJkZXJpdmVkIiBzdEV2dDpwYXJhbWV0ZXJzPSJjb252ZXJ0ZWQgZnJv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"
                };
                data.value=cloudData;
                data.name="askcloud_show";
                data.tittle="提问统计";
                console.log(data);
                cloudSumShow(data);
                ShowAnsCloud(ansFileName);
            }
        });
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////
    var cloudData2=[];
    var ansBarX=[];
    var ansBarY=[];
    var  ShowAnsCloud=function (ansFileName){
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
                cloudData2=[];
                console.log(data.length);
                var j=210;
                if(data.length<j){
                    j=data.length;
                }
                ansBarX=[];
                ansBarY=[];
                // for(var i = 1, j = data.length-1; i < j; i++) {
                for(var i = 1; i < j; i++) {
                    var item = data[i];
                    var cloud={};
                    cloud.name=item[1];
                    cloud.value=item[2];
                    cloudData2.push(cloud);

                    if(i<101){
                        ansBarX.push(item[1]);
                        ansBarY.push(item[2]);
                    }
                }
                // var sumCloud=cloudData.concat(cloudData2);
                var data={
                    image:"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAooAAAKKCAYAAAC6dHqMAAAACXBIWXMAAAsTAAALEwEAmpwYAAAJT2lUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp0aWZmPSJodHRwOi8vbnMuYWRvYmUuY29tL3RpZmYvMS4wLyIgeG1sbnM6ZXhpZj0iaHR0cDovL25zLmFkb2JlLmNvbS9leGlmLzEuMC8iIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06RG9jdW1lbnRJRD0iYWRvYmU6ZG9jaWQ6cGhvdG9zaG9wOmI4NzFiZDljLTBhYWQtNGI0NC05NmQ3LTI3MWIwMDRmMmIxNiIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDowN2MwMTk0MS0wMDIyLTdhNGQtODQ3NS04Y2Q2MTRhOTEwZTciIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0iNTlDRjhDMTgzRjc5RjQ5MzYzMUFENjkxNzE3RDQ4MUMiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIiBwaG90b3Nob3A6Q29sb3JNb2RlPSIzIiBwaG90b3Nob3A6SUNDUHJvZmlsZT0iIiB0aWZmOkltYWdlV2lkdGg9IjY1MCIgdGlmZjpJbWFnZUxlbmd0aD0iNjUwIiB0aWZmOlBob3RvbWV0cmljSW50ZXJwcmV0YXRpb249IjIiIHRpZmY6U2FtcGxlc1BlclBpeGVsPSIzIiB0aWZmOlhSZXNvbHV0aW9uPSI3Mi8xIiB0aWZmOllSZXNvbHV0aW9uPSI3Mi8xIiB0aWZmOlJlc29sdXRpb25Vbml0PSIyIiBleGlmOkV4aWZWZXJzaW9uPSIwMjIxIiBleGlmOkNvbG9yU3BhY2U9IjY1NTM1IiBleGlmOlBpeGVsWERpbWVuc2lvbj0iNjUwIiBleGlmOlBpeGVsWURpbWVuc2lvbj0iNjUwIiB4bXA6Q3JlYXRlRGF0ZT0iMjAyMi0wMy0yOVQyMzoxNToxOCswODowMCIgeG1wOk1vZGlmeURhdGU9IjIwMjItMDMtMjlUMjM6Mjg6NDgrMDg6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMjItMDMtMjlUMjM6Mjg6NDgrMDg6MDAiPiA8eG1wTU06SGlzdG9yeT4gPHJkZjpTZXE+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJzYXZlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo2ZTJiMjMwNC1iYzZhLWQ3NGMtYjM5MC1hNmRlN2JhOTg5YjAiIHN0RXZ0OndoZW49IjIwMjItMDMtMjlUMjM6Mjg6NDgrMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIgc3RFdnQ6Y2hhbmdlZD0iLyIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY29udmVydGVkIiBzdEV2dDpwYXJhbWV0ZXJzPSJmcm9tIGltYWdlL2pwZWcgdG8gaW1hZ2UvcG5nIi8+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJkZXJpdmVkIiBzdEV2dDpwYXJhbWV0ZXJzPSJjb252ZXJ0ZWQgZnJv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"
                };
                data.value=cloudData2;
                data.name="anscloud_show";
                data.tittle="回答统计";
                console.log(data);
                cloudSumShow(data);
                drawBar();
            }
        });
    }

    var drawBar=function (){
        console.log("asdas");
        var chartDom = document.getElementById('bar_show');
        var myChart = echarts.init(chartDom);
        var option;
        var barX=[];
        var barAskY=[];
        var barAnsY=[];
        var ansXLength=ansBarX.length;
        var askXLength=askBarX.length;
        for(var i=0;ansXLength>0,askXLength>0;i++){
            barX.push(askBarX[i]);
            barAskY.push(askBarY[i]);
            barAnsY.push("");

            barX.push(ansBarX[i]);
            barAnsY.push(ansBarY[i]);
            barAskY.push("");

            ansXLength--;
            askXLength--;
        }
        if(askXLength!=0){
            barX.push(askBarX[i]);
            barX.push("");
            barAskY.push(askBarY[i]);
            barAskY.push("");
        }
        option = {
            title: {
                // text: 'Rainfall vs Evaporation',
                subtext: '词频'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['提问', '回答']
            },
            color:["#6aa84f","#6fa8dc"],
            toolbox: {
                show: true,
                feature: {
                    // dataView: { show: true, readOnly: false },
                    magicType: { show: true, type: ['line', 'bar'] },
                    restore: { show: true },
                    // saveAsImage: { show: true }
                }
            },
            calculable: true,
            xAxis: [
                {
                    type: 'category',
                    // prettier-ignore
                    // data: askBarX,
                    data: barX,
                    // data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
                },
                {
                    type: 'category',
                    // prettier-ignore
                    data: ansBarX,
                    // data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
                },
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            dataZoom: [
                {
                    show: true,
                    start: 0,
                    end: 50
                },
                {
                    type: 'inside',
                    start: 94,
                    end: 100
                },
                // {
                //   show: true,
                //   yAxisIndex: 0,
                //   filterMode: 'empty',
                //   width: 30,
                //   height: '80%',
                //   showDataShadow: false,
                //   left: '93%'
                // }
            ],
            series: [
                {
                    name: '提问',
                    type: 'bar',
                    data: barAskY,
                    markPoint: {
                        data: [
                            { type: 'max', name: 'Max' },
                            { type: 'min', name: 'Min' }
                        ]
                    },
                    markLine: {
                        data: [{ type: 'average', name: 'Avg' }]
                    }
                },
                {
                    name: '回答',
                    type: 'bar',
                    data: barAnsY,
                    markPoint: {
                        data: [
                            { type: 'max', name: 'Max' },
                            { type: 'min', name: 'Min' }
                        ]
                    },
                    markLine: {
                        data: [{ type: 'average', name: 'Avg' }]
                    }
                },
            ]
        };
        myChart.setOption(option);
       // drawPie();
    }




    var cloudSumShow=function (data){

        var myChart = echarts.init(document.getElementById(data.name));
        //温馨提示：image 选取有严格要求不可过大；，否则firefox不兼容 iconfont上面的图标可以
        var maskImage = new Image();
        maskImage.src = data.image;
        maskImage.onload = function(){
            myChart.setOption( {
                backgroundColor:'#fff',
                title:{
                      text: data.tittle,
                      left:'center',
                    bottom: 5,
                   },
                tooltip: {
                },
                series: [{
                    type: 'wordCloud',
                    shape:'diamond',
                    gridSize: 1,
                    sizeRange: [12, 55],
                    rotationRange: [-45, 0, 45, 90],
                    maskImage: maskImage,
                    textStyle: {
                        color: function() {
                            var colors = ['#81cacc','#fda67e', '#81cacc', '#cca8ba', "#88cc81", "#82a0c5", '#fddb7e', '#735ba1', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];
                            return colors[parseInt(Math.random() * 10)];
                        }
                    },
                    left: 'center',
                    top: 'center',
                    width: '96%',
                    height: '100%',
                    right: null,
                    bottom: null,
                    data: data.value
                }]
            })
        }
        // $("#loading").hideLoading();
    }

    function getRndInteger(min, max) {
        return Math.floor(Math.random() * (max - min) ) + min;
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