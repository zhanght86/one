<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@include file="/common/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>赔案信息查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="${ctx}/common/css/Standard.css" rel="stylesheet" type="text/css" />
	<script src="${ctx}/common/calender/WdatePicker.js"></script>

  </head>
  
  <body>
    <form name="fm" method="post" onkeypress="KeyDown()">
	<div style="width:100%">
		<table id="RegisterInfo" class="common" cellpadding="3" cellspacing="1">
			<tr>
				<td class="formtitle" colspan="6"><img src="${ctx}/images/bgformtitle.gif" style="cursor:hand;">立案信息</td>
			</tr>
			<tr>
				<td  class="left">事件号：</td>
				<td  class="right"><input name="caseNo" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">赔案号：</td>
				<td  class="right"><input name="RegisterNo" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">申请人姓名：</td>
				<td  class="right"><input name="ApplyName" class="common" type="text" onchange="clickable()" /></td>
			</tr>
			<tr>
				<td  class="left">申请人电话：</td>
				<td  class="right"><input name="RgtantTel" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">申请人地址：</td>
				<td  class="right"><input name="RgtantAddress" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">申请人与出险人关系：</td>
				<td  class="right">
					<input class="codecode" id="relatshipCode" name="lcReport.relatshipCode" class="common" type="text" value="" style="width:20%" ><input name="relatship" class="common" type="text" onchange="clickable()" style="width:68%" value="">
				</td>
				</tr>
			<tr>
				<td  class="left">出险地点：</td>
				<td  class="right"><input name="AppntAddress" class="common" type="text" onchange="clickable()"><img src="${ctx}/images/bgMarkMustInput.jpg" ></td>
				<td  class="left">立案日期：</td>
				<td class="right"><input name="RgtDate" id="RgtDate" class="common" type="text" onchange="clickable()" /></td>
				<td  class="left">管辖机构：</td>
				<td  class="right"><input name="MngCom" class="common" type="text" onchange="clickable()"></td>
			</tr>
			<tr>
				<td  class="left">立案受理人：</td>
				<td  class="right"><input name="Rgtperson" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">受托人类型：</td>
				<td  class="right"><input name="AgentType" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">受托人代码：</td>
				<td  class="right"><input name="AgentCode" class="common" type="text" onchange="clickable()"></td>
			</tr>
			<tr>
				<td  class="left">受托人姓名：</td>
				<td  class="right"><input name="AgentName" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">受托人性别：</td>
				<td  class="right"><input name="AgentSex" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">受托人电话：</td>
				<td  class="right"><input name="AgentTel" class="common" type="text" onchange="clickable()"></td>
			</tr>
			<tr>
				<td  class="left">受托人地址：</td>
				<td  class="right"><input name="AgentAddress" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">受托人邮编：</td>
				<td  class="right"><input name="AgentZip" class="common" type="text" onchange="clickable()"></td>
				<td  class="left">申请类型：</td>
				<td class="right"><input name="ApplyType" class="common" type="text" onchange="clickable()" /></td>
			</tr>
			<tr>
				<td  class="left">理赔金领取方式：</td>
				<td  class="right"><input name="ReceiveType" class="common" type="text" onchange="clickable()"></td>
				<td  class="common"> </td>
				<td  class="common"> </td>
				<td  class="common"> </td>
				<td  class="common"> </td>
			</tr>
		</table>
		<hr />
		<table id="RegisterList" class="common" cellpadding="3" cellspacing="0">
			<thead>
				<tr class="tableHead">
					<td width="2%">&nbsp;</td>
					<td width="4%">序号</td>
					<td width="20%">出险人姓名</td>
					<td width="15%">性别</td>
					<td width="20%">出生日期</td>
					<td width="15%">证件类型</td>
					<td width="20%">证件号码</td>
				</tr>
			</thead>
		</table>
		<table id="AppntInfoTitle" class="common" cellpadding="3" cellspacing="1">
			<tr>
				<td class="formtitle"><img src="${ctx}/images/bgformtitle.gif" style="cursor:hand;">出险人信息</td>
			</tr>
		</table>
		<table id="PageInfo" class="common" cellpadding="3" cellspacing="0">
			<tr>
				<td>
					<input type="button" class="cssbutton" name="PolQuery" value="保单查询" onclick="self.location.href='${ctx}/claim/claimoperate/register/PolicyQuery.jsp'" />
					<input type="button" class="cssbutton" name="PastClaimQuery" value="既往赔案查询" onclick="self.location.href='${ctx}/claim/claimoperate/register/PastClaimQuery.jsp'" />
					<input type="button" class="cssbutton" name="ImageQuery" value="影像查询" onclick="" />
				</td>
			</tr>
		</table>
		<table id="AppntInfo" class="common" cellpadding="3" cellspacing="1">
			<tr>
				<td class="left">出险人姓名：</td>
				<td class="right"><input name="AppntName" class="common" type="text" onchange="clickable()"></td>
				<td class="left">出险人年龄：</td>
				<td class="right"><input name="AppntAge" class="common" type="text" onchange="clickable()"></td>
				<td class="left">出险人性别：</td>
				<td class="right"><input name="AppntSex" class="common" type="text" onchange="clickable()"></td>
			</tr>
			<tr>
				<td class="left">联系电话：</td>
				<td class="right"><input name="Telephone" class="common" type="text" onchange="clickable()"><img src="${ctx}/images/bgMarkMustInput.jpg" ></td>
				<td  class="left">事故日期：</td>
				<td class="right">
					<input name="AccidDate" id="AccidDate" class="common" type="text" onchange="clickable()" style="width: 73%" value='' />
					<img style='cursor: hand' align="absmiddle" src="${ctx}/images/bgcalendar.gif" onclick="WdatePicker({el:'AccidDate',startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',minDate:'#{%y-10}-%M-%d',maxDate:'#{%y+10}-%M-%d',alwaysUseStartDate:true})"><img src="${ctx}/images/bgMarkMustInput.jpg" >
				</td>
				<td class="left">出险原因：</td>
				<td class="right">
					<input class="codecode" id="appntReasonCode" name="lcReport.appntReasonCode" class="common" type="text" value="" style="width:20%" ><input name="appntReason" class="common" type="text" onchange="clickable()" style="width:68%" value=""><img src="${ctx}/images/bgMarkMustInput.jpg" >
				</td>
			</tr>
			<tr>
				<td class="left">治疗医院：</td>
				<td class="right">
					<input class="codecode" id="treatHospitalCode" name="lcReport.treatHospitalCode" class="common" type="text" value="" style="width:20%" ><input name="treatHospital" class="common" type="text" onchange="clickable()" style="width:68%" value="">
				</td>
				<td  class="left">出险日期：</td>
				<td class="right">
					<input name="AppntDate" id="AppntDate" class="common" type="text" onchange="clickable()" style="width: 73%" value='' />
					<img style='cursor: hand' align="absmiddle" src="${ctx}/images/bgcalendar.gif" onclick="WdatePicker({el:'AppntDate',startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',minDate:'#{%y-10}-%M-%d',maxDate:'#{%y+10}-%M-%d',alwaysUseStartDate:true})">
				</td>
				<td class="left">意外细节：</td>
				<td class="right">
					<input class="codecode" id="unexpectDetailCode" name="lcReport.unexpectDetailCode" class="common" type="text" value="" style="width:20%" ><input name="unexpectDetail" class="common" type="text" onchange="clickable()" style="width:68%" value="">
				</td>
			</tr>
			<tr>
				<td class="left">治疗情况：</td>
				<td class="right">
					<input class="codecode" id="treatStateCode" name="lcReport.treatStateCode" class="common" type="text" value="" style="width:20%" ><input name="treatState" class="common" type="text" onchange="clickable()" style="width:68%" value="">
				</td>
				<td class="left">出险结果1：</td>
				<td class="right">	
					<input class="codecode" id="CaseResult1Code" name="lcReport.CaseResult1Code" class="common" type="text" value="" style="width:20%" ><input name="CaseResult1" class="common" type="text" onchange="clickable()" style="width:68%" value=""><img src="${ctx}/images/bgMarkMustInput.jpg" >
				</td>
				<td class="left">出险结果2：</td>
				<td class="right">	
					<input class="codecode" id="CaseResult2Code" name="lcReport.CaseResult2Code" class="common" type="text" value="" style="width:20%" ><input name="CaseResult2" class="common" type="text" onchange="clickable()" style="width:68%" value=""><img src="${ctx}/images/bgMarkMustInput.jpg" >
				</td>
			</tr>
			<tr>
				<td class="left">住院科室：</td>
				<td class="right"><input name="HosDepart" class="common" type="text" onchange="clickable()"></td>
				<td class="left">单证齐全标识：</td>
				<td class="right">
					<input class="codecode" id="affixComplCode" name="lcReport.affixComplCodee" class="common" type="text" value="" style="width:20%" ><input name="affixComplFlag" class="common" type="text" onchange="clickable()" style="width:68%" value=""><img src="${ctx}/images/bgMarkMustInput.jpg" >
				</td>
				<td class="common"> </td>
				<td class="common"> </td>
			</tr>
			<tr>
				<td class="left">案件标识：</td>
				<td class="right"><input name="CaseFlag" class="common" type="text" onchange="clickable()"></td>
				<td class="left">赔付次数：</td>
				<td class="right"><input name="Casetimes" class="common" type="text" onchange="clickable()"></td>
				<td class="common"> </td>
				<td class="common"> </td>
			</tr>
		</table>
		<table id="ClaimType" class="common" cellpadding="3" cellspacing="1">
			<tr>
				<td >理赔类型：<img src="${ctx}/images/bgMarkMustInput.jpg" >&nbsp;&nbsp;
					<input type="checkbox" name="Death" value="" />身故
					<input type="checkbox" name="HeavyResidue" value="" />重疾
					<input type="checkbox" name="Injury" value="" />伤残
					<input type="checkbox" name="Subsidy" value="" />津贴
					<input type="checkbox" name="MedicalCare" value="" />医疗
				</td>
			</tr>
		</table>
		<table id="AccidentDesc" class="common" cellpadding="3" cellspacing="1">
			<tr>
				<td >事故描述</td>
			</tr>
			<tr>
				<td><textarea name="accidentDesc" cols="" rows="4" style="width:100%"></textarea></td>
			</tr>
			<tr>
				<td >备注</td>
			</tr>
			<tr>
				<td><textarea name="Remark" cols="" rows="4" style="width:100%"></textarea></td>
			</tr>
		</table>
		<table id="RegisterResult" class="common" cellpadding="3" cellspacing="1">
			<tr>
				<td class="formtitle" colspan="6"><img src="${ctx}/images/bgformtitle.gif" style="cursor:hand;">立案结论信息</td>
			</tr>
			<tr>
				<td class="left">立案结论：</td>
					<td class="right">
						<input class="codecode" id="registerCode" name="lcReport.registerCode" class="common" type="text" value="" style="width:20%" ><input name="registerResult" class="common" type="text" onchange="clickable()" style="width:68%" value="">
					</td>
				<td class="common"> </td>
				<td class="common"> </td>
				<td class="common"> </td>
				<td class="common"> </td>
			</tr>
		</table>
		<table id="PageInfo" class="common" cellpadding="3" cellspacing="0">
			<tr>
				<td>
					<input type="button" class="cssbutton" name="AccessReport" value="查看呈报" onclick="self.location.href='${ctx}/claim/claimoperate/register/ReportInfoQuery.jsp'" />
					<input type="button" class="cssbutton" name="AccessSurvey" value="查看调查" onclick="self.location.href='${ctx}/claim/claimoperate/report/SurveyInfoQuery.jsp'" />
					<input type="button" class="cssbutton" name="AffixAdjust" value="备注信息" onClick="" />
				</td>
			</tr>
		</table>
		<hr />
		<table id="PageInfo" class="common" cellpadding="3" cellspacing="0">
			<tr>
				<td>
					<input type="button" class="cssbutton" name="returnButton" value="返  回" onclick="javascript:history.back();" />
				</td>
			</tr>
		</table>
	</div>
	</form>
  </body>
</html>
