//增加一个新的险种，并且检查该险种是否已存在
function addRiskCode(){
	if(null==$("#riskCode").val()||$("#riskCode").val()==""){
		alert("请输入待申请的产品险种代码！");
		return false;
	}
	//url,params,callback
	var url = ctx + "/product/checkRiskExist";
	var params ={
		"pdLMRisk.riskCode":$("#riskCode").val(),
		"pdLMRisk.makeDate":$("#makeDate").val()
	};
	jQuery.post(url,params,callbackRiskExist);
}

//检查是否已存在该险种 存在--Y 不存在--N
function callbackRiskExist(obj){
	if(obj == "Y"){
		alert("申请的险种编码【"+$("#riskCode").val()+"】已存在!");
		return false;
	}
}


//查询定义中的产品
function queryApplingRisk(){
	var url=ctx + "/product/queryApplingRisk";
	$("#frmInput").attr("action",url);
	$("#frmInput").attr("target","fraInterface");
	$("#frmInput").submit();	
}


//查询已有核保规则
function queryApplingUW(){
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/queryApplingUW",
		data : "",
		dataType :"json",
		success : function(data){
			   var showContentString = "";
			   $("#HASUWdetail").html("");
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'>"
				   +"<td><input type='radio' name='selectApplingRadio' value='"+data[i].uwCode+"'/></td>"
				   +"<td>"+(i+1)+"</td>"
				   +"<td>"+data[i].uwCode+"</td>"
				   +"<td>"+data[i]["PDLMRisk.riskCode"]+"</td>"
				   +"<td>"+data[i].uwOrder+"</td>"
				   +"<td>"+data[i].remark+"</td>"
				   +"</tr>";
			   }
		   		$("#HASUWdetail").html(showContentString);
			
		}
	});
}
 

//新增核保规则
function insertUW(){
	$("#UWDetail").show();

	$.ajax({
		   type: "POST",
		   url: ctx + "/product/pdriskUnderwrite",
		   data : {"uwCode":""},
		   dataType : "json",
		   success: function(data){
			   var showContentString = "";
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'>"
				   +"<td>"+data[i].displayOrder+"</td>"
				   +"<td>"+data[i].fieldName+"</td>"
				   +"<td>"+data[i].fieldType+"</td>"
				   +"<td><input name='"+data[i].fieldValueName+"' class='common' type='text' value='"+data[i].fieldValue+"' /></td>";
				   if(null==data[i].officialDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].officialDesc+"</td>";
				   }
				   if(null==data[i].busiDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].busiDesc+"</td>";
				   }
				   showContentString  +="</tr>";
			   }
		   		$("#UWContent").html(showContentString);
		   }
		});
	
	
}

//修改核保规则查询
function  updateUW(){
    //判断产品是否存并是否已选择定义中的产品
	if(null==$('input:radio[name="selectApplingRadio"]:checked').val()
	  ||undefined==$('input:radio[name="selectApplingRadio"]:checked').val()){
		alert("请先选择一个需要定义的产品！");
		return false;
	}
	
	$("#UWDetail").show();

	$.ajax({
		   type: "POST",
		   url: ctx + "/product/pdriskUnderwrite",
		   data : {"uwCode":""+$('input:radio[name="selectApplingRadio"]:checked').val()},
		   dataType : "json",
		   success: function(data){
			   var showContentString = "";
			   $("#UWContent").html("");
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'>"
				   +"<td>"+data[i].displayOrder+"</td>"
				   +"<td>"+data[i].fieldName+"</td>"
				   +"<td>"+data[i].fieldType+"</td>"
				   +"<td><input name='"+data[i].fieldValueName+"' class='common' type='text' value='"+data[i].fieldValue+"' /></td>";
				   if(null==data[i].officialDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].officialDesc+"</td>";
				   }
				   if(null==data[i].busiDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].busiDesc+"</td>";
				   }
				   showContentString  +="</tr>";
			   }
		   		$("#UWContent").html(showContentString);
		   }
		});
	
}

//保存核保规则
function saveUW(){
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/saveLMUW",
		data : $("#frmInput").serialize(),
		dataType :"json",
		success : function(msg){
			if(null!=msg){
			  alert("保存成功！");
			  $("#save").attr('disabled',true);
			  queryApplingUW();
		    }
		}
	});

}

//删除核保规则
function deleteUW(){
	
    //判断产品是否存并是否已选择定义中的产品
	if(null==$('input:radio[name="selectApplingRadio"]:checked').val()
	  ||undefined==$('input:radio[name="selectApplingRadio"]:checked').val()){
		alert("请先选择一个需要定义的产品！");
		return false;
	}
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/deleteLMUW",
		data : {"uwCode":""+$('input:radio[name="selectApplingRadio"]:checked').val()},
		dataType :"json",
		success : function(msg){
			if(null!=msg){
			  alert(msg);
			  queryApplingUW();
		    }
		}
	});

}





//查询已有投保规则
function queryApplingCF(){
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/queryApplingCF",
		data : {"riskCode":"GCMR"},
		dataType :"json",
		success : function(data){
			   var showContentString = "";
			   $("#HASCFdetail").html("");
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'>"
				   +"<td><input type='radio' name='selectApplingRadio' value='"+data[i]["PDLMRisk.riskCode"]+","+data[i]["id.fieldName"]+","+data[i]["id.serialNO"]+"'/></td>"
				   +"<td>"+(i+1)+"</td>"
				   +"<td>"+data[i]["PDLMRisk.riskCode"]+"</td>"
				   +"<td>"+data[i]["id.fieldName"]+"</td>"
				   +"<td>"+data[i]["id.serialNO"]+"</td>"
				   +"<td>"+data[i].msg+"</td>"
				   +"</tr>";
			   }
		   		$("#HASCFdetail").html(showContentString);
			
		}
	});
}


//新增投保规则
function insertCF(){
	$("#CFDetail").show();

	$.ajax({
		   type: "POST",
		   url: ctx + "/product/insertCF",
		   data : {},
		   dataType : "json",
		   success: function(data){
			   var showContentString = "";
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'>"
				   +"<td>"+data[i].displayOrder+"</td>"
				   +"<td>"+data[i].fieldName+"</td>"
				   +"<td>"+data[i].fieldType+"</td>"
				   +"<td><input name='"+data[i].fieldValueName+"' class='common' type='text' value='"+data[i].fieldValue+"' /></td>";
				   if(null==data[i].officialDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].officialDesc+"</td>";
				   }
				   if(null==data[i].busiDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].busiDesc+"</td>";
				   }
				   showContentString  +="</tr>";
			   }
		   		$("#CFContent").html(showContentString);
		   }
		});
}



//修改投保规则查询
function  updateCF(){
  //判断产品是否存并是否已选择定义中的产品
	if(null==$('input:radio[name="selectApplingRadio"]:checked').val()
	  ||undefined==$('input:radio[name="selectApplingRadio"]:checked').val()){
		alert("请先选择一个需要定义的产品！");
		return false;
	}
	
	$("#CFDetail").show();

	$.ajax({
		   type: "POST",
		   url: ctx + "/product/insertCF",
		   data : {"id":$('input:radio[name="selectApplingRadio"]:checked').val()},
		   dataType : "json",
		   success: function(data){
			   var showContentString = "";
			   $("#CFContent").html("");
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'>"
				   +"<td>"+data[i].displayOrder+"</td>"
				   +"<td>"+data[i].fieldName+"</td>"
				   +"<td>"+data[i].fieldType+"</td>"
				   +"<td><input name='"+data[i].fieldValueName+"' class='common' type='text' value='"+data[i].fieldValue+"' /></td>";
				   if(null==data[i].officialDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].officialDesc+"</td>";
				   }
				   if(null==data[i].busiDesc){
					   showContentString  +="<td>"+""+"</td>";
				   }else{
					   showContentString  +="<td>"+data[i].busiDesc+"</td>";
				   }
				   showContentString  +="</tr>";
			   }
		   		$("#CFContent").html(showContentString);
		   }
		});
	
}



//删除投保规则
function deleteCF(){
	
    //判断产品是否存并是否已选择定义中的产品
	if(null==$('input:radio[name="selectApplingRadio"]:checked').val()
	  ||undefined==$('input:radio[name="selectApplingRadio"]:checked').val()){
		alert("请先选择一个需要定义的产品！");
		return false;
	}
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/deleteCF",
		 data : {"id":$('input:radio[name="selectApplingRadio"]:checked').val()},
		dataType :"json",
		success : function(msg){
			if(null!=msg){
			  alert(msg);
			  queryApplingCF();
		    }
		}
	});

}







//保存投保规则
function saveCF(){
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/saveCF",
		data : $("#frmInput").serialize(),
		dataType :"json",
		success : function(msg){
			if(null!=msg){
			  alert("保存成功！");
			  $("#save").attr('disabled',true);
			  queryApplingCF();
		    }
		}
	});

}




//查询已发送的问题件规则
function queryApplingIssue(){
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/queryApplingIssue",
		data : {"riskCode":"","issueState":"1"},
		dataType :"json",
		success : function(data){
			   var showContentString = "";
			   $("#HASIssuedetail").html("");
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'>"
				   +"<td><input type='radio' name='selectApplingRadio' value='"+data[i]["id.riskCode"]+","+data[i]["id.serialNo"]+"'/></td>"
				   +"<td>"+(i+1)+"</td>"
				   +"<td>"+data[i].backpost+"</td>"
				   +"<td>"+data[i].issuecont+"</td>"
				   +"<td>"+data[i].issuestate+"</td>"
				   +"</tr>";
			   }
		   		$("#HASIssuedetail").html(showContentString);
			
		}
	});
}





//新增问题件
function insertIssue(){
	$("#IssueDetail").show();
	$("#serialNo").val(""); 
}




//保存问题件
function saveIssue(){
	$.ajax({
		type : "POST",
		url : ctx + "/product/saveIssue",
		data : $("#frmIssueInput").serialize(),
		dataType :"json",
		success : function(msg){
			if(null!=msg){
			  alert("保存成功！");
			  queryApplingIssue();
		    }
		}
	});

}


//删除问题件
function deleteIssue(){
	
    //判断产品是否存并是否已选择定义中的产品
	if(null==$('input:radio[name="selectApplingRadio"]:checked').val()
	  ||undefined==$('input:radio[name="selectApplingRadio"]:checked').val()){
		alert("请先选择一个需要定义的产品！");
		return false;
	}
	
	$.ajax({
		type : "POST",
		url : ctx + "/product/deleteIssue",
		 data : {"id":$('input:radio[name="selectApplingRadio"]:checked').val()},
		dataType :"json",
		success : function(msg){
			if(null!=msg){
			  alert(msg);
			  queryApplingIssue();
		    }
		}
	});

}





//修改问题件
function  updateIssue(){
//判断产品是否存并是否已选择定义中的产品
	if(null==$('input:radio[name="selectApplingRadio"]:checked').val()
	  ||undefined==$('input:radio[name="selectApplingRadio"]:checked').val()){
		alert("请先选择一个需要定义的产品！");
		return false;
	}
	
	$("#IssueDetail").show();

	$.ajax({
		   type: "POST",
		   url: ctx + "/product/updateIssue",
		   data : {"id":$('input:radio[name="selectApplingRadio"]:checked').val()},
		   dataType : "json",
		   success: function(data){
			   for(var i = 0 ; i < data.length ; i++){
					$("#issuecont").val(data[i].issuecont);
					$("#riskCode").val(data[i].riskCode);
					$("#serialNo").val(data[i].serialNo);
					$("#codeType").val(data[i].backpost);
					$("#codeName").val(data[i].backpostname);
					
			   }
		   }
		});
	
}
//查询已存在的对应险种的险种角色信息
function findRiskRoleByRisk(){
	$.ajax({
	   type: "POST",
	   url: ctx + "/product/findRiskRoleByRisk",
	   data : {"riskCode":"a"},
	   dataType : "json",
	   success: function(data){
		   var showContentString = "";
		   for(var i = 0 ; i < data.length ; i++){
			   showContentString += "<tr class='content'><td><input name='riskRoleUnion' value='"+data[i]["id.riskCode"]+","+data[i]["id.riskRole"]+","+data[i]["id.riskRoleSex"]+","+data[i]["id.riskRoleNo"]+"' type='radio' /></td><td>"+i+1+"</td><td>"
			   	+data[i]["id.riskCode"]+"</td><td>"
			   	+data[i].riskVer+"</td><td>"
			   	+data[i]["id.riskRole"]+"</td><td>"
			   	+data[i]["id.riskRoleSex"]+"</td><td>"
			   	+data[i]["id.riskRoleNo"]+"</td><td>"
			   	+data[i].minAppAgeFlag+"</td><td>"
			   	+data[i].minAppAge+"</td><td>"
			   	+data[i].maxAppAgeFlag+"</td><td>"
			   	+data[i].maxAppAge+"</td>";
		   }
	   		$("#roleList").html(showContentString);
	   }
	});
	$("#saveriskRole").attr('disabled',true);
}
//展示险种信息的时候，先查询一次险种角色信息
$(function(){
	findRiskRoleByRisk();
});
//查询需要修改的险种角色信息的
function updateRiskRole() { 
	//判断是否已选择要修改的产品
	if(null==$('input:radio[name="riskRoleUnion"]:checked').val()
	  ||undefined==$('input:radio[name="riskRoleUnion"]:checked').val()){
		alert("请先选择一个需要修改的险种角色！");
		return false;
	}
	$("#riskRole").show();
	var val=$('input:radio[name="riskRoleUnion"]:checked').val();
	$.ajax({
		   type: "POST",
		   url: ctx + "/product/findRiskRole",
		   data : {"riskRoleUnion":val},
		   dataType : "json",
		   success: function(data){
			   var showContentString = "";
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'><td>"
				    +data[i].displayOrder+"</td><td>"
				   	+data[i].fieldName+"</td><td>"
				   	+data[i].fieldCode+"</td><td>"
				   	+data[i].fieldType+"</td><td><input type='text' value='"
				   	+data[i].fieldValue+"' name='pdlmRiskRole."+data[i].fieldCode+"' class='common'/></td><td>";
				   if(null==data[i].officialDesc){
					   showContentString  +="</td><td>";
				   }else{
					   showContentString  +=data[i].officialDesc+"</td><td>";
				   }
				   if(null==data[i].busiDesc){
					   showContentString  +="</td>";
				   }else{
					   showContentString  +=data[i].busiDesc+"</td>";
				   }
				   showContentString  +="</tr>";
			   }
		   		$("#oneRole").html(showContentString);
		   }
		});
	 $("#operateRole").val("u");
	$("#saveriskRole").attr('disabled',false);
};
//新增险种角色信息时查询需要定义的险种角色的字段的名称属性等信息
function addRiskRole() { 
	$("#riskRole").show();
	$.ajax({
		   type: "POST",
		   url: ctx + "/product/findRiskRole",
		   data : {"riskRoleUnion":""},
		   dataType : "json",
		   success: function(data){
			   var showContentString = "";
			   for(var i = 0 ; i < data.length ; i++){
				   showContentString += "<tr class='content'><td>"
				    +data[i].displayOrder+"</td><td>"
				   	+data[i].fieldName+"</td><td>"
				   	+data[i].fieldCode+"</td><td>"
				   	+data[i].fieldType+"</td><td><input type='text' value='"
				   	+data[i].fieldValue+"' name='pdlmRiskRole."+data[i].fieldCode+"' class='common'/></td><td>";
				   if(null==data[i].officialDesc){
					   showContentString  +="</td><td>";
				   }else{
					   showContentString  +=data[i].officialDesc+"</td><td>";
				   }
				   if(null==data[i].busiDesc){
					   showContentString  +="</td>";
				   }else{
					   showContentString  +=data[i].busiDesc+"</td>";
				   }
				   showContentString  +="</tr>";
			   }
		   		$("#oneRole").html(showContentString);
		   }
		});
	 $("#operateRole").val("s");
	 $("#saveriskRole").attr('disabled',false);
};
//保存险种角色信息
function saveRiskRole() {
	//判断是新增还是更新数据
	if($("#operateRole").val()=="s"){
		operateRiskRole='saveRiskRole';
	}else{
		operateRiskRole='updateRiskRole';
	}
		
	$.ajax({
		type : "POST",
		url : ctx + "/product/"+operateRiskRole,
		data :$("#riskRoleForm").serialize(),
		dataType :"json",
		success : function(){
			  $("#saveriskRole").attr('disabled',true);
			  findRiskRoleByRisk();
		    }
		}
	);
};
function deleteRiskRole(){
	
    //判断是否存已选择要删除的险种角色
	if(null==$('input:radio[name="riskRoleUnion"]:checked').val()
	  ||undefined==$('input:radio[name="riskRoleUnion"]:checked').val()){
		alert("请先选择一个需要删除的险种角色！");
		return false;
	}
	var val=$('input:radio[name="riskRoleUnion"]:checked').val();
	$.ajax({
		type : "POST",
		url : ctx + "/product/deleteRiskRole",
		data : {"riskRoleUnion":val},
		dataType :"json",
		success : function(msg){
			$("#saveriskRole").attr('disabled',false);
			if(null!=msg){
			  $("#oneRole").html("");
			  alert(msg);
			  //查询剩余险种角色记录
			  findRiskRoleByRisk();
			  //删除后清空下面的数据
			  
		    }
			
		}
	});

}

