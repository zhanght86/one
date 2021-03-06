<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>权限管理-功能菜单管理</title>
    <link type="text/css" rel="stylesheet" href="${ctx}/static/css/sinosoft.base.css" />
    <link type="text/css" rel="stylesheet" href="${ctx}/static/css/sinosoft.tree2.css" />
    <link type="text/css" rel="stylesheet" href="${ctx}/static/css/sinosoft.grid.css" />
    <link type="text/css" rel="stylesheet" href="${ctx}/static/css/sinosoft.message.css" />
    <script type="text/javascript" src="${ctx}/static/js/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.tree.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.mouseoutclick.js"></script>
    <script language="javascript" src="${ctx}/static/js/sinosoft.grid.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.message.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#treeOne").jstree({
                "themes" : {
                    "theme" : "default",
                    "dots" : false
                },
                "json_data" : {
                    "ajax" : {
                        "type":"post",
                        "url" : "${ctx}/account/user/viewCompanyInfo",
                        "data" : function (n) {
                            return { id : n.attr ? n.attr("id") : 0 };
                        }
                    }
                },
                "plugins" : [ "themes", "json_data", "ui" ]
            }).bind("select_node.jstree", function (event, data) {
                        $("#grid").children().remove();
                        $("#grid").Grid({
                            url : "${ctx}/static/html/grid3.json",
                            dataType: "json",
                            height: 'auto',
                            colums:[
                                {id:'1',text:'角色名称',name:"appellation",width:'120',index:'1',align:'',color:''},
                                {id:'2',text:'指派日期',name:"Status",width:'90',index:'1',align:'',color:''},
                                {id:'3',text:'指派人',name:"Version",index:'1',align:'',color:''}
                            ],
                            rowNum:10,
                            sorts:false,
                            pager : false,
                            number:false,
                            multiselect: true
                        });
                    });
            fitHeight();
            $("#grid").Grid({
                url : "${ctx}/static/html/grid3.json",
                dataType: "json",
                height: 'auto',
                colums:[
                    {id:'1',text:'角色名称',name:"appellation",width:'120',index:'1',align:'',color:''},
                    {id:'2',text:'指派日期',name:"Status",width:'90',index:'1',align:'',color:''},
                    {id:'3',text:'指派人',name:"Version",index:'1',align:'',color:''}
                ],
                rowNum:0,
                sorts:false,
                pager : false,
                number:false,
                multiselect: true
            });
        });
        function fitHeight(){
            var pageHeight = $(document).height() - 95;
            $("#treeOne").height(pageHeight);
            $("#treeTow").height(pageHeight);
        };
        function save(){
            msgSuccess("", "保存成功！");
        }
    </script>
</head>

<body>
<table border="0" cellspacing="0" cellpadding="0" class="authorize">
    <tr>
        <td width="269" valign="top">
            <div class="title2"><b>机构列表</b></div>
            <div id="treeOne" class="tree_view"></div>
        </td>
        <td width="30" valign="top">&nbsp;</td>
        <td width="350" valign="top">
            <div class="title2"><b>角色列表</b></div>
            <div id="treeTow" class="tree_view">
                <div id="grid"></div>
            </div>
        </td>
    </tr>
    <tr>
        <td colspan="3" valign="top">
            <div  class="form_tool add_peo">
                <input type="button" value="保 存" class="def_btn" onclick="save()" />
            </div>
        </td>
    </tr>
</table>
</body>
</html>
