<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value='/js/common/oslFile.js'/>" ></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<html lang="ko">
<head>
<title>OpenSoftLab</title>

<style>
.req4107_work_popup {
    display: inline-block;
    width: 100%;
    height: 100%;
}
.req4107_work_popupFrame {
    padding: 15px;
    display: inline-block;
    width: 100%;
    font-size: 10pt;
}
</style>
<script>

var type = "${type}";
var workId = "${workId}";

//유효성 체크
var arrChkObj = {"workAdmContent":{"type":"length","msg":"작업 지시내용은 4000byte까지 입력이 가능합니다.","max":4000}};
						
$(document).ready(function() {
	//유효성 체크 걸기
	gfnInputValChk(arrChkObj);
	
	//타입별 명칭
	if(type == "update"){
		$("#work_action_name").html("수정");
	}
	//작업 기간 설정
	gfnCalRangeSet("workAdmStDtm", "workAdmEdDtm", prjStartDt, prjEndDt);
	
	//담당자 검색 걸기
	$("#btn_workUser_select").click(function() {
		gfnCommonUserPopup( $('#workChargerNm').val() ,false,function(objs){
			if(objs.length>0){
				$('#workChargerId').val(objs[0].usrId);
				$('#workChargerNm').val(objs[0].usrNm);
			}
		});
	});
	
	$('#workChargerNm').keyup(function(e) {
		if($('#workChargerNm').val()==""){
			$('#workChargerId').val("");
		}
		if(e.keyCode == '13' ){
			$('#btn_workUser_select').click();
		}
	});
});

//작업 저장
function fnReq4107SaveAction(){
	//inputError 초기화
	$(".inputError").removeClass("inputError");
	
	var checkObjArr = ["workAdmContent","workChargerId","workChargerNm","workAdmStDtm","workAdmEdDtm"];
	var checkObjNmArr = ["작업 내용","담당자","담당자","작업 시작일자","작업 종료일자"];
	
	//유효성 체크
	var chkRtn = gfnRequireCheck("req4107PopupFrm", checkObjArr, checkObjNmArr);
	
	//유효성 오류있는 경우
	if(chkRtn){
		return false;
	}
	
	//유효성 값 체크
	if(!gfnSaveInputValChk(arrChkObj)){
		return false;	
	}
		
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus()
		return false;
	}
	
	//입력 항목 값
	var formArray = $("#req4107PopupFrm").serializeArray();
	
	//요구사항Id, 프로세스Id, 작업흐름 Id
	formArray.push({name:"type",value:type });
	formArray.push({name:"reqId",value: reqId});
	formArray.push({name:"processId",value: processId});
	formArray.push({name:"flowId",value: flowInfo.flowId});
	formArray.push({name:"workStatusCd",value: "01"});
	
	//수정인경우 작업 Id 추가
	if(type == "update"){
		formArray.push({name:"workId",value: workId});
	}
	
	var paramInfo = {};
	$.each(formArray,function(idx, map){
		paramInfo[map.name] = map.value;
	});
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4400/saveReq4400ReqWorkInfoAjax.do'/>"},
			paramInfo);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		toast.push(data.message);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//대시보드 새로고침 함수 존재하는 경우 새로고침
    		if(typeof fnDashBoardSetting == "function"){
    			//대시보드새로고침
				fnDashBoardSetting();
    		}
			
			//그리드 내용 새로고침
			work_grid.setData(data.workList);
			
			//레이어 팝업 닫기
			gfnLayerPopupClose();
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림");
 	});
	//AJAX 전송
	ajaxObj.send();
	
}
</script>
</head>
<body>
<div class="popup">
<form id="req4107PopupFrm" name="req4107PopupFrm" method="post">
	<div class="req4107_work_popup">
		<div class="popup_title_box">작업 <span id="work_action_name">추가</span></div>
		<div class="req4107_work_popupFrame">
			<div class="req4105_option_title req4105_desc req4105_top_line">
				작업 지시내용 (*)
			</div>
			<div class="req4105_option_all req4105_desc req4105_top_line req4105_right_line">
				<textarea class="req4105_textarea" id="workAdmContent" name="workAdmContent">${workInfo.workAdmContent}</textarea>
			</div>
			<div class="req4105_option_title">
				담당자 (*)
			</div>
			<div class="req4105_option_all req4105_right_line">
				<input type="hidden" name="workChargerId" id="workChargerId" value="${workInfo.workChargerId}"/>
				<input type="text" style="width: 92% !important;" title="담당자" class="req4105_input_text req4105_charger" name="workChargerNm" id="workChargerNm" value="${workInfo.workChargerNm}"/>
				<span class="button_normal2 fl req4105_charger" id="btn_workUser_select"><li class="fa fa-search"></li></span>
			</div>
			<div class="req4105_option_title">
				작업 시작예정일자 (*)
			</div>
			<div class="req4105_option_all req4105_right_line">
				<input type="text" class="req4105_input_date" id="workAdmStDtm" name="workAdmStDtm" value="${workInfo.workAdmStDtm}" readonly="readonly"/>
			</div>
			<div class="req4105_option_title">
				작업 종료예정일자 (*)
			</div>
			<div class="req4105_option_all req4105_right_line">
				<input type="text" class="req4105_input_date" id="workAdmEdDtm" name="workAdmEdDtm" value="${workInfo.workAdmEdDtm}" readonly="readonly"/>
			</div>
		</div>
		<div class="req4105_btnDiv" style="padding:0;">
			<div class="button_complete req4105_complete" onclick="fnReq4107SaveAction()">완료</div>
			<div class="button_complete req4105_close" onclick="gfnLayerPopupClose()">닫기</div>
		</div>
	</div>
</form>
</div>
</body>
</html>