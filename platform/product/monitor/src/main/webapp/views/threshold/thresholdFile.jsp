<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="<%=request.getContextPath()%>"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>查看预警配置文件</title>
<link href="${ctx}/static/css/base.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/css/base.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/css/sinosoft.message.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/css/sinosoft.grid.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/static/css/sinosoft.window.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${ctx}/static/js/jquery-1.7.1.js"></script>
<script language="javascript" src="${ctx}/static/js/sinosoft.layout.js"></script>
<script language="javascript" src="${ctx}/static/js/sinosoft.grid.js"></script>
<script language="javascript" src="${ctx}/static/js/sinosoft.window.js"></script>
<script language="javascript" src="${ctx}/static/js/sinosoft.message.js"></script>

<script type="text/javascript">
$(function(){
	$("body").layout({
		top:{topHeight:100},
		bottom:{bottomHeight:30}
	});
	$("#thresholdList").Grid({
		type : "post",
		url : "${ctx}/threshold/threshold/data",  
		dataType: "json",
		colDisplay: false,  
		clickSelect: true,
		draggable:false,
		height: "auto",
		colums:[  
			{id:'1',text:'名称',name:"name",index:'1',align:''},
			{id:'3',text:'严重告警条件',name:"criticalThresholdCondition",index:'1',align:''},
			{id:'4',text:'严重告警阈值',name:"criticalThresholdValue",index:'1',align:''},
			{id:'5',text:'严重告警信息',name:"criticalThresholdMessage",index:'1',align:''},
			{id:'6',text:'警告告警条件',name:"warningThresholdCondition",index:'1',align:''},
			{id:'7',text:'严重告警阈值',name:"warningThresholdValue",index:'1',align:''},
			{id:'8',text:'严重告警信息',name:"warningThresholdMessage",index:'1',align:''},
			{id:'9',text:'正常告警条件',name:"infoThresholdCondition",index:'1',align:''},
			{id:'10',text:'严重告警阈值',name:"infoThresholdValue",index:'1',align:''},
			{id:'11',text:'严重告警信息',name:"infoThresholdMessage",index:'1',align:''},
			{id:'12',text:'操作',name:"operation",index:'1',align:''}
		],  
		rowNum:9999,
		pager : false,
		number:false,  
		multiselect: true  
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
function delRow(e){
	var rows = $(e).parent().parent();
	var id = rows.attr('id');
	msgConfirm('系统消息','确定要删除该条配置文件吗？',function(){
		rows.remove();
		delThreshold("${ctx}/threshold/threshold/delete/"+id.replace("row_",""));
	});
}

function updRow(e){
	alert(111);
	var rows = $(e).parent().parent();
	var id = rows.attr('id');
	rows.remove();
	location.href="${ctx}/threshold/threshold/update/"+id.replace("row_","");
}
function batchDel(){
	var $g = $("#thresholdList div.grid_view > table");
	var selecteds = $("td.multiple :checked",$g);
	if(selecteds.length > 0){
		msgConfirm('系统消息','确定要删除该条配置文件吗？',function(){
			var checks = [];
			selecteds.each(function(){
				var rows = $(this).parent().parent();
				checks.push(rows.attr('id').replace("row_",""));
				rows.remove();
			});
			var delUrl = "${ctx}/threshold/threshold/batchDelete/"+checks;
			delThreshold(delUrl);
		});
	}else{
		msgAlert('系统消息','没有选中的文件！<br />请选择要删除的文件后，继续操作。')
	};
}
function delThreshold(url){
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		success : function(data) {
			msgSuccess("系统消息", "操作成功，配置已删除！");
		},
		error:function(){
			msgSuccess("系统消息", "操作失败，配置未被删除！");
			location.href="${ctx}/threshold/threshold/list";
		}
	});
}
function viewRelevance(e){
	var rows = $(e).parent().parent();
	var id = rows.attr('id');
	var name = rows.children("td").eq(1).text();
	var title = "监视器使用阈值: " + name;
	var temWin = $("body").window({
		"id":"window",   
        "title":title,  
		"url":"thresholdEdit.html",   
        "hasIFrame":true,
		"width": 740,
		"height":440,
		"diyButton":[{
			"id": "btOne",
			"btClass": "buttons",
			"value": "关闭",
			"onclickEvent" : "selectLear",
			"btFun": function() {
					temWin.closeWin();
				}
			}
		]
	});
}
</script>
</head>

<body>
<%@include file="/WEB-INF/layouts/menu.jsp"%>
<div id="layout_center">
	<div class="main">
    	<div class="threshold_file">
       	  <h2 class="title2"><b>查看阈值配置文件　</b></h2>
          <div class="tool_bar_top"><a href="javascript:void(0);" class="batch_del" onclick="batchDel()">批量删除</a></div>
          <div id="thresholdList"></div>
          <div class="tool_bar"></div>
        </div>
    </div>
</div>
<div id="layout_bottom">
	<p class="footer">Copyright &copy; 2013 Sinosoft Co.,Lt</p>
</div>
</body>
</html>