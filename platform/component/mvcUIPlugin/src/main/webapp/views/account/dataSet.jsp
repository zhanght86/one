<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>权限管理-功能菜单管理</title>
    <link type="text/css" rel="stylesheet" href="${ctx}/static/css/sinosoft.base.css" />
    <link type="text/css" rel="stylesheet" href="${ctx}/static/css/sinosoft.tree.css" />
    <script type="text/javascript" src="${ctx}/static/js/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.tree.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.mouseoutclick.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#sel_2").children().remove();
            $("#treeOne").jstree({
                "themes" : {
                    "dots" : false,
                    "icons" : false
                },
                "json_data":{
                    "ajax":{
                        "url":"${ctx}/static/html/tree.json"
                    }
                },
                "plugins":["themes","json_data","ui"]
            }).bind("open_node.jstree",function(e,data){
                        var theId = $(this).find(".jstree-open");
                        var thisId = data.rslt.obj.attr("id");
                        theId.each(function(){
                            var okId = $(this).attr("id");
                            if(okId != thisId){
                                $("#treeOne").jstree("close_node","#" + okId);
                            };
                        });
                    }).bind("select_node.jstree",function(e,data){
                        var $temValOne = $("<option id='op_1'>财产保险公司北京分公司</option>");
                        var $temValTwo = $("<option id='op_2'>财产保险公司山东分公司</option>");
                        var $temValThree = $("<option id='op_3'>财产保险公司上海分公司</option>");
                        var $temValFour = $("<option id='op_4'>财产保险公司深圳分公司</option>");
                        $("#sel_2").append($temValOne).append($temValTwo).append($temValThree).append($temValFour);

                    });
            $("#sel_2").find("option").live("dblclick", toLeftMove);
            $("#sel_1").find("option").live("dblclick", toRightMove);
        });
        function toLeftMove() {
            var ops = $("#sel_2").children();
            ops.each(function(){
                if($(this).attr("selected")) {
                    var thisId = $(this).attr("id");
                    var rootText = $(this).text();
                    $("#sel_1").append($(this));
                    var temValP = $("<p id='p_"+thisId+"'>"+rootText+"cc/read.php?tid=53766773424&_fp=4,文字数据.com/categroy/ux3724&_fp=4,文字数据.com/categroy/ux</p>");
                    var temValText = $("<input id='te_"+thisId+"' type='text' class='code_text' value='请输入参数' />");
                    $(".code_box").append(temValP).append(temValText);
                }
            });

        }

        function toRightMove() {
            var ops = $("#sel_1").children();
            ops.each(function(){
                if($(this).attr("selected")) {
                    var thisId = $(this).attr("id");
                    $("#sel_2").append($(this));
                    $("#p_"+thisId).remove();
                    $("#te_"+thisId).remove();
                }
            });
        };
    </script>
</head>

<body>
<div class="data_list">
    <div class="title2"><b><span>姓名：张山  编号：10009999000</span>数据设置</b></div>
    <div id="treeOne" class="tree_view f_left" style="height:439px"></div>
    <div class="data_right f_left">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>
                    <select id="sel_1" class="datas" multiple="multiple">
                    </select>
                </td>
                <td class="data_cen">
                    <a href="javascript:;" class="a_left" onclick="toLeftMove();"></a>
                    <a href="javascript:;" class="right a_right" onclick="toRightMove();"></a>
                </td>
                <td>
                    <select id="sel_2" multiple="multiple" class="datas">
                        <option id="op_1">测试一</option>
                        <option id="op_2">测试二</option>
                        <option id="op_3">测试三</option>
                        <option id="op_4">测试四</option>
                    </select>
                </td>
            </tr>
        </table>
        <div class="code_box" style="margin-bottom:10px;">

        </div>
    </div>
</div>
</body>
</html>
