<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>performance性能</title>

    <link href="${ctx}/global/css/base.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/global/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/global/css/performance/performance.css" rel="stylesheet" type="text/css"/>
    <script language="javascript" src="${ctx}/global/js/jquery-1.7.1.js"></script>
    <script language="javascript" src="${ctx}/global/js/sinosoft.layout.js"></script>
    <script type="text/javascript">
        $(function () {
            $("body").layout({
                top:{topHeight:100}
            });
            $("#myDesk").height($("#layout_center").height());
            $("#nav").delegate('li', 'mouseover mouseout', navHover);
            $("#nav,#menu").delegate('li', 'click', navClick);

            var _data = getBarLength();
            $(".p_info_table_body_bar").find("div").each(function (index) {
                $(this).css("width", _data[index] + "%");
            });
            $(".percent").each(function (i) {
                $(this).text(_data[i] + "%");
            });
            //颜色条的异步数据调用

        });
        function getBarLength() {
            var _data;
            $.ajax({
                url:"performance_bar_length.json",
                type:"get",
                dataType:"json",
                async:false,
                success:function (data) {
                    _data = data.bar;
                },
                error:function (a, b, c) {
                    alert(c);
                }
            });
            return _data;
        }
        function navHover() {
            $(this).toggleClass("hover")
        }
        function navClick() {
            $(this).addClass("seleck").siblings().removeClass("seleck");
            if ($(this).hasClass('has_sub')) {
                var subMav = $(this).children("ul.add_sub_menu");
                var isAdd = false;
                if ($(this).parent().attr("id") == "menu") {
                    isAdd = true;
                }
                ;
                subMav.slideDown('fast', function () {
                    $(document).bind('click', {dom:subMav, add:isAdd}, hideNav);
                    return false;
                });
            }
            ;
        }
        function hideNav(e) {
            var subMenu = e.data.dom;
            var isAdd = e.data.add;
            subMenu.slideUp('fast', function () {
                if (isAdd) {
                    subMenu.parent().removeClass('seleck');
                }
                ;
            });
            $(document).unbind();
        }
        function eidRow(appId){
            /*管理业务场景页面*/
            window.location.href="${ctx}/application/manager/bsmanager/bizscenariolist/"+appId;
        }
        function delRow(appId){
            /*删除应用*/
            window.location.href="${ctx}/application/manager/appmanager/delete/"+appId;
        }
    </script>
</head>

<body>
<div id="layout_top">
    <div class="header">
        <p class="user">您好,系统管理员 <span>|</span> <a href="#">退出系统</a></p>

        <div class="menu_box">
            <ul class="nav" id="nav">
                <li><a href="javascript:viod(0)">首页</a></li>
                <li class="has_sub">
                    <a href="javascript:viod(0)">监视器</a><span class="show_sub_anv"></span>
                    <ul class="add_sub_menu" id="subNav">
                        <li class="action"><span class="sever">操作系统</span>
                            <ul class="list">
                                <li><a href="javascript:viod(0)">操作系统1</a></li>
                                <li><a href="javascript:viod(0)">操作系统2</a></li>
                            </ul>
                        </li>
                        <li class="action"><span class="system">应用系统</span>
                            <ul class="list">
                                <li><a href="javascript:viod(0)">在线投保</a></li>
                                <li><a href="javascript:viod(0)">在线查询</a></li>
                                <li><a href="javascript:viod(0)">应急处置</a></li>
                                <li><a href="javascript:viod(0)">人员角色管理</a></li>
                            </ul>
                        </li>
                        <li class="action" style="border:none"><span>数据库</span>
                            <ul class="list">
                                <li><a href="javascript:viod(0)">SQL DBA</a></li>
                                <li><a href="javascript:viod(0)">SQL SYS</a></li>
                            </ul>
                        </li>
                        <li class="clear"></li>
                    </ul>

                </li>
                <li class="seleck"><a href="${ctx}/application/manager/appmanager/applist">应用性能</a></li>
                <li><a href="javascript:viod(0)">业务仿真</a></li>
                <li><a href="javascript:viod(0)">告警</a></li>
            </ul>
        </div>
        <ul class="add_menu" id="menu">
            <li><a href="javascript:viod(0)">新建监视器</a></li>
            <li><a href="javascript:viod(0)">配置监视器</a></li>
            <li class="has_sub">
                <a href="javascript:viod(0)"><span>预警对象管理</span></a>
                <ul class="add_sub_menu">
                    <li class="title"><a href="javascript:viod(0)">显示动作</a></li>
                    <li class="action">创建新动作</li>
                    <li><a class="sms" href="javascript:viod(0)">短信动作</a></li>
                    <li><a class="email" href="javascript:viod(0)">邮件动作</a></li>
                </ul>
            </li>
        </ul>
    </div>
</div>
<div id="layout_center">
    <div class="p_all">
        <div class="p_sub_index">
            <table class="p_sub_table">
                <tr>
                    <td class="p_sub_table_left">
                        <span><a href="#">监视器</a></span>
                        <span>></span>
                        <span><strong>应用性能透视</strong></span>
                    </td>
                    <td class="p_sub_table_right">
                        <p>
                            <span><strong>rpm</strong>=每分钟请求</span>,<span><strong>cpm</strong>=每分钟调用数</span>
                        </p>
                    </td>
                </tr>
            </table>
            <div class="p_info_div">
                <table class="p_table" cellspacing="0" cellpadding="0" border="0" align="center">
                    <tr>
                        <td class="p_table_t_l">
                            应用系统名称
                        </td>
                        <td class="p_table_t_r">
                            <div>
                                <select>
                                    <option>最近一小时</option>
                                    <option>最近三小时</option>
                                    <option>最近六小时</option>
                                    <option>最近十二小时</option>
                                </select>
                            </div>
                            <div>查看:</div>
                        </td>
                    </tr>
                </table>
                <div class="p_info">
                    <table class="p_info_table">
                        <thead class="p_info_table_head">
                        <th>&nbsp;</th>
                        <th class="head_name">实例名称</th>
                        <th class="head_score">应用性能指数评分</th>
                        <th class="head">平均响应时间</th>
                        <th class="head">吞吐量</th>
                        <th class="head">管理</th>
                        </thead>
                    </table>
                    <div class="p_info_body_div">
                        <table>
                            <tbody>
                            <c:forEach items="${applications}" var="application">
                                <tr class="p_info_table_body">
                                    <td></td>
                                    <td class="body_name">
                                        <div><a class="p_info_table_td_text"
                                                href="${ctx}/application/manager/appmanager/appinfo/${application.id}">${application.applicationName} </a></div>
                                        <div>${application.cnName}</div>
                                    </td>
                                    <td class="body_score">
                                        <table>
                                            <tr>
                                                <td><strong>0.98</strong></td>
                                                <td style="width:100%">
                                                    <div class="p_info_table_body_bar">
                                                        <div class="green">&nbsp;</div>
                                                        <div class="yellow">&nbsp;</div>
                                                        <div class="red">&nbsp;</div>
                                                    </div>

                                                </td>
                                                <td class="percent" style="color:green;">90%</td>
                                                <td class="percent" style="color: #C60">7%</td>
                                                <td class="percent" style="color:red;">3%</td>

                                            </tr>
                                        </table>
                                    </td>
                                    <td class="body"><span>494.0 毫秒</span></td>
                                    <td class="body"><span>148.42 rpm</span></td>
                                    <td class="body"><a href="${ctx}/application/manager/bsmanager/list/${application.id}">管理业务场景</a>
                                        <a href="delete/${application.id}">删除</a>
                                        <%--<a href="javascript:void(0), onclick=delRow(${application.id})">删除</a>--%>
                                    </td>
                                            <%--href="${ctx}/application/manager/bsmanager/bizscenariolist/${application.id}">管理业务场景</a--%>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <table class="p_info_table_bottom">
                        <tr style="width:30%;">
                            <td class="user_info">
                                <div><img src="${ctx}/global/images/performance/legend-green.png"/><span>满意用户</span>
                                </div>
                                <div><img src="${ctx}/global/images/performance/legend-orange.png"/><span>容忍用户</span>
                                </div>
                                <div><img src="${ctx}/global/images/performance/legend-red.png"/><span>失望用户</span></div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="layout_bottom">
    <p class="footer">Copyright &copy; 2013 Sinosoft Co.,Lt</p>
</div>

</body>
</html>