<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value='/js/common/oslFile.js'/>" ></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<html lang="ko">
<head>
<title>OpenSoftLab</title>

<style>
.popup_title_box { width: 100%; height: 44px; padding: 15px;font-weight: bold; color: #fff; background-color: #4b73eb;}
.req4109_work_popup {display: inline-block;width: 100%;height: 100%;}
.req4109_work_popupFrame {padding: 15px;display: inline-block;width: 100%;font-size: 10pt;}
.req4109_option_title {width: 25%;float: left;height: 45px;padding-left: 10px;line-height: 40px;background-color: #f9f9f9;border: 1px solid #ccc;font-weight: bold;border-top: none;}
.req4109_option_all {float: left;height: 45px;line-height: 30px;width: 75%;border-bottom: 1px solid #ccc;padding: 5px;}
textarea.req4109_textarea {width: 100%;height: 100%;resize: none;padding: 5px;border: 1px solid #ccc;border-radius: 1px;}
.req4109_btnDiv {width: 100%;height: 100px;line-height: 100px;text-align: center;padding-left: 100px;}
input.req4109_input_date {width: 200px;float: left;display: block;border-radius: 1px;height: 100% !important;background-color: #fff !important;border: 1px solid #ccc;text-align: center;}
.req4109_complete {width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin-right:20px;background-color: #fff;color: #000;}
.req4109_close{width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;background-color: #fff;color: #000;}
.req4109_desc {height: 100px;}
.req4109_top_line{border-top:1px solid #ccc;}
.req4109_right_line{border-right:1px solid #ccc;}
</style>
<script>
var workId = "${workId}";
var type = "${type}";

//프로젝트 기간
var prjStartDt = '${selPrjInfo.startDt}';
var prjEndDt = '${selPrjInfo.endDt}';

//유효성 체크
var arrChkObj = {"workContent":{"type":"length","msg":"작업 내용은 4000byte까지 입력이 가능합니다.","max":4000}};

$(document).ready(function() {
	//유효성 체크 걸기
	gfnInputValChk(arrChkObj);
	
	//작업 기간 설정
	gfnCalRangeSet("workStDtm", "workEdDtm", prjStartDt, prjEndDt);
});

//작업 저장
function fnReq4109SaveAction(){
	//inputError 초기화
	$(".inputError").removeClass("inputError");
	
	var checkObjArr = ["workContent","workStDtm","workEdDtm"];
	var checkObjNmArr = ["작업 내용","작업 시작일자","작업 종료일자"];
	
	//유효성 체크
	var chkRtn = gfnRequireCheck("req4109PopupFrm", checkObjArr, checkObjNmArr);
	
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
	var formArray = $("#req4109PopupFrm").serializeArray();
	
	jConfirm("작업을 종료처리 하시겠습니까?", "알림", function( result ) {
		if( result ){
    		
			//요구사항Id, 프로세스Id, 작업흐름 Id
			formArray.push({name:"type",value:"update" });
			formArray.push({name:"reqId",value: "${reqId}"});
			formArray.push({name:"processId",value: "${processId}"});
			formArray.push({name:"flowId",value: "${flowId}"});
			formArray.push({name:"workId",value: workId});
			formArray.push({name:"workStatusCd",value: "02"});
			
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
				
				
				//에러 없는경우
				if(data.errorYN != "Y"){
					jAlert("작업이 종료처리 되었습니다.","알림");
					
					//대시보드인경우 대시보드 작업 목록 갱신
					if(type != null && type == "dsh1000"){
						//대시보드 부분 새로고침
						fnSubDataLoad('etc',"work");
						
					}else{	//팝업 작업 목록 갱신
						//그리드 내용 새로고침
						work_grid.setData(data.workList);
					}
					
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
	});
	
}
</script>
</head>
<body>
<div class="popup">
<form id="req4109PopupFrm" name="req4109PopupFrm" method="post">
	<div class="req4109_work_popup">
		<div class="popup_title_box">작업 <span id="work_action_name">종료</span></div>
		<div class="req4109_work_popupFrame">
			<div class="req4109_option_title req4109_desc req4109_top_line">
				작업 내용 (*)
			</div>
			<div class="req4109_option_all req4109_desc req4109_top_line req4109_right_line">
				<textarea class="req4109_textarea" id="workContent" name="workContent">${workInfo.workContent}</textarea>
			</div>
			<div class="req4109_option_title">
				작업 시작일자 (*)
			</div>
			<div class="req4109_option_all req4109_right_line">
				<input type="text" class="req4109_input_date" id="workStDtm" name="workStDtm" value="${workInfo.workStDtm}" readonly="readonly"/>
			</div>
			<div class="req4109_option_title">
				작업 종료일자 (*)
			</div>
			<div class="req4109_option_all req4109_right_line">
				<input type="text" class="req4109_input_date" id="workEdDtm" name="workEdDtm" value="${workInfo.workEdDtm}" readonly="readonly"/>
			</div>
		</div>
		<div class="req4109_btnDiv" style="padding:0;">
			<div class="button_complete req4109_complete" onclick="fnReq4109SaveAction()">완료</div>
			<div class="button_complete req4109_close" onclick="gfnLayerPopupClose()">닫기</div>
		</div>
	</div>
</form>
</div>
</body>
</html>