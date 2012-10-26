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
    <link type="text/css" rel="stylesheet" href="${ctx}/static/css/sinosoft.message.css" />
    <script type="text/javascript" src="${ctx}/static/js/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.tree.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.mouseoutclick.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/sinosoft.message.js"></script>
    <script type="text/javascript">
        $(function(){
            var funId = "";
            var jiGouId = "";
            $("#treeOne").jstree({
                "themes" : {
                    "theme" : "default",
                    "dots" : false
                },
                "json_data" : {
                    "ajax" : {
                        "url" : "${ctx}/static/html/tree.json",
                        "data" : function (n) {
                            return { id : n.attr ? n.attr("id") : 0 };
                        }
                    }
                },
                "plugins" : [ "themes", "json_data", "ui" ]
            }).bind("select_node.jstree", function(){
                        $("#treeTow").jstree({
                            "themes" : {
                                "theme" : "default",
                                "dots" : false
                            },
                            "json_data" : {
                                "ajax" : {
                                    "url" : "${ctx}/static/html/tree.json"
                                }
                            },
                            "plugins" : [ "themes", "json_data", "checkbox", "ui" ]
                        });
                    });
            fitHeight();
        });
        function saveFunction(){
            $("#treeTow").find(".jstree-checked").each(function(){
                funId = funId + $(this).attr("id") + ";";
            });
        };
        function fitHeight(){
            var pageHeight = $(document).height() - 62;
            $("#treeOne").height(pageHeight);
            $("#treeTow").height(pageHeight);
        };
        function saveFunction(){
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
        <td width="269" valign="top">
            <div class="title2"><b>功能列表</b></div>
            <div id="treeTow" class="tree_view"></div>
        </td>
        <td width="30" valign="top">&nbsp;</td>
        <td valign="top">
            <input type="button" class="def_btn" value="保  存"  onclick="saveFunction();" />
        </td>
    </tr>
</table>
</body>
</html>
