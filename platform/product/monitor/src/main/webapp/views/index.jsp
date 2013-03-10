<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>monitor监控系统</title>
<link href="${ctx}/global/css/base.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/global/css/style.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${ctx}/global/js/jquery-1.7.1.js"></script>
<script language="javascript" src="${ctx}/global/js/sinosoft.layout.js"></script>
<script type="text/javascript">
$(function(){
	$("body").layout({
		top:{topHeight:100},
		bottom:{bottomHeight:30}
	});
	$("#myDesk").height($("#layout_center").height());
	$("#nav").delegate('li', 'mouseover mouseout', navHover);
	$("#nav,#menu").delegate('li', 'click', navClick);
});
function navHover(){
	$(this).toggleClass("hover")
}
function navClick(){
	$(this).addClass("seleck").siblings().removeClass("seleck");
	if($(this).hasClass('has_sub')){
		var subMav = $(this).children("ul.add_sub_menu");
		var isAdd = false;
		if($(this).parent().attr("id") == "menu"){
			isAdd = true;
		};
		subMav.slideDown('fast',function(){
			$(document).bind('click',{dom:subMav,add:isAdd},hideNav);
			return false;
		});		
	};
}
function hideNav(e){
	var subMenu = e.data.dom;
	var isAdd = e.data.add;
	subMenu.slideUp('fast',function(){
		if(isAdd){
			subMenu.parent().removeClass('seleck');
		};
	});	
	$(document).unbind();
}
</script>
</head>

<body>
<div id="layout_top">
	<div class="header">
    	<p class="user">您好,系统管理员 <span>|</span> <a href="#">退出系统</a></p>
    	<div class="menu_box">
        	<ul class="nav" id="nav">
            	<li><a href="index.html">首页</a></li>
                <li class="has_sub">
                	<a href="javascript:viod(0)">监视器</a><span class="show_sub_anv"></span>
                	<ul class="add_sub_menu" id="subNav">
                    	<li class="action"><span class="sever">操作系统</span>
                        	<ul class="list">
                            	<li><a href="${ctx }/os/systemMonitor"> Linux(2)</a></li>
                            </ul>
                        </li>
                        <li class="action"><span class="system">应用系统</span>
                        	<ul class="list">
                                <li><a href="performance.html">在线查询</a></li>
                            </ul>
                        </li>
                        <li class="action" style="border:none"><span>数据库</span>
                        	<ul class="list">
                            	<li><a href="oracleMonitor.html">oracle</a></li>
                            </ul>
                        </li>
                        <li class="clear"></li>
                    </ul>
                    
                </li>
                <li><a href="performance.html">应用性能</a></li>
                <li><a href="BusinessSimulation.html">业务仿真</a></li>                
                <li><a href="alertList.html">告警</a></li>
                <li><a href="userManager.html">用户管理</a></li>                
            </ul>
        </div>
        <ul class="add_menu" id="menu">
        	<li><a href="addMonitorList.html">新建监视器</a></li>
            <li class="has_sub">
            	<a href="javascript:viod(0)"><span>阈值配置文件</span></a>
            	<ul class="add_sub_menu">
                    <li><a class="addThreshold" href="addThreshold.html">新建阈值文件</a></li>
                    <li><a class="thresholdFile" href="thresholdFile.html">查看阈值配置文件</a></li>
                </ul>
            </li>            
            <li><a href="deployMonitor.html">配置监视器</a></li>
            <li class="has_sub">
            	<a href="javascript:viod(0)"><span>动作</span></a>
            	<ul class="add_sub_menu">
                	<li class="title"><a href="showMotion.html">显示动作</a></li>
                    <li class="action">创建新动作</li>
                    <li><a class="sms" href="message.html">短信动作</a></li>
                    <li><a class="email" href="mail.html">邮件动作</a></li>
                </ul>
            </li>
            <li><a href="setEmergency.html">配置告警</a></li>
        </ul>
    </div>
</div>
<div id="layout_center">
	
</div>
<div id="layout_bottom">
	<p class="footer">Copyright &copy; 2013 Sinosoft Co.,Lt</p>
</div>
</body>
</html>