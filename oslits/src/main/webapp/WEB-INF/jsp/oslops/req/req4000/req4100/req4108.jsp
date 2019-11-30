<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value='/js/common/oslFile.js'/>" ></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<html lang="ko">
<head>
<title>OpenSoftLab</title>

<style>
/* 팝업 타이틀 */
.popup_title_box { width: 100%; height: 44px; padding: 15px;font-weight: bold; color: #fff; background-color: #4b73eb; font-size: 15px;}

.req4108_sign_popupFrame { padding: 15px; display: inline-block;width: 100%; font-size: 10pt;}
.req4108_sign_popup { display: inline-block; width: 100%; height: 100%; }	

.req4105_option_title { width: 25%; float: left; height: 45px; padding-left: 10px; line-height: 40px; background-color: #f9f9f9; border: 1px solid #ccc; font-weight: bold; border-top: none; }

.req4105_top_line {border-top: 1px solid #ccc;}
.req4105_right_line { border-right: 1px solid #ccc; }

.req4105_option_all { float: left; height: 45px; line-height: 30px; width: 75%; border-bottom: 1px solid #ccc; padding: 5px; }
.req4105_desc {height: 100px;}

/* textarea */
textarea.req4105_textarea { width: 100%; height: 100%; resize: none; padding: 5px; border: 1px solid #ccc; border-radius: 1px; }
textarea.req4105_textarea{ width: 100% !important; height: 100% !important; }

/* 버튼 */
.req4105_btnDiv { width: 100%; height: 100px; line-height: 100px; text-align: center; padding-left: 100px;}
.req4105_signBtn, .req4105_complete {width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin-right:20px;background-color: #fff;color: #000;}
.req4105_close{width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;background-color: #fff;color: #000;}

#rejectContentView{height: 100%;text-align: left;white-space: normal;width: 100%;overflow-y: auto;font-size: 10pt;border: 1px solid #ccc;padding: 2px;}
</style>
<script>

//유효성 체크
var arrChkObj = {"rejectContent":{"type":"length","msg":"반려 내용은 400byte까지 입력이 가능합니다.","max":4000}
				};

$(document).ready(function() {
	//유효성 체크
	gfnInputValChk(arrChkObj);
});

//결재 반려
function fnReq4108RejectAction(){
	
	var viewName = '${param.view}';
	
	var checkObjArr = ["rejectContent"];
	var checkObjNmArr = ["반려 내용"];

	//유효성 체크
	var chkRtn = gfnRequireCheck("req4108PopupFrm", checkObjArr, checkObjNmArr);
	
	//유효성 오류있는 경우
	if(chkRtn){
		return false;
	}
	
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus()
		return false;
	}
	
	jConfirm("결재를 반려하시겠습니까?", "알림", function( result ) { 
		if( result ){
			//요구사항명
			var reqNm = $("#reqNm").val();
			
			//반려 사유
			var signRejectCmnt = $("#rejectContent").val().replace(/\n/gi, "<br>");

			// 요구사항 결재 승인 화면(chk1100)에서 반려 팝업 호출할 경우
			if( !gfnIsNull(viewName) && viewName == 'chk1100'){
				
				// 반려 글 작성시 regUsrId가 controller에서  파라미터를 map 변환시 로그인한 사람의 ID로 지정되므로
				// 별도로 담당자 ID값을 regUsrId에 지정해야함
				var rtnData = {	
								reqId: '${param.reqId}', 
								reqNm: '${param.reqNm}', 
								processId: '${param.processId}', 
								signFlowId: '${param.signFlowId}', 
								signUsrId: '${param.signUsrId}', 
								preFlowId: '${param.preFlowId}', 
								flowNextNextId: '${param.flowNextNextId}', 
								signRegUsrId: '${param.signRegUsrId}',
								signCd: "03",
								signRejectCmnt: signRejectCmnt,
								regUsrId: '${param.signRegUsrId}' // 요구사항 담당자(=결재 요청자)
							};
				// 반려 저장전 유효성 체크
				if(!gfnSaveInputValChk(arrChkObj)){
					return false;	
				}
				
				fnReqSignComplete(rtnData);
				//창 닫기
				gfnLayerPopupClose();

			}else{
				// 담당 결재목록에서 호출할 경우
				// 반려 글 작성시 regUsrId가 controller에서  파라미터를 map 변환시 로그인한 사람의 ID로 지정되므로
				// 별도로 담당자 ID값을 regUsrId에 지정해야함
				var rtnData = {
								reqId: reqId, reqNm: reqNm, processId: processId, 
								signFlowId: lastChkInfo.signFlowId, signUsrId: usrId, 
								signCd: "03", signRejectCmnt: signRejectCmnt, 
								signRegUsrId: lastChkInfo.regUsrId,
								regUsrId: lastChkInfo.regUsrId 
							};
				// 반려 저장전 유효성 체크
				if(!gfnSaveInputValChk(arrChkObj)){
					return false;	
				}
				
				fnReqSignComplete(rtnData);
				//창 닫기
				gfnLayerPopupClose();
			}
		}
	});
}

</script>
</head>
<body>
<div class="popup">
<form id="req4108PopupFrm" name="req4108PopupFrm" method="post">
	<div class="req4108_sign_popup">
		<div class="popup_title_box">결재 반려 사유 <c:if test="${param.type == 'reject'}">입력</c:if></div>
		<div class="req4108_sign_popupFrame">
			<div class="req4105_option_title req4105_desc req4105_top_line">
				반려 사유 (*)
			</div>
			<div class="req4105_option_all req4105_desc req4105_top_line req4105_right_line">
				<c:choose>
					<c:when test="${param.type == 'reject'}">
						<textarea class="req4105_textarea" id="rejectContent" name="rejectContent"></textarea>
					</c:when>
					<c:when test="${param.type == 'view'}">
						<div class="req4105_textarea" id="rejectContentView" name="rejectContentView">
								${param.comment}
						</div>
					</c:when>
				</c:choose>
				
			</div>
		</div>
		<div class="req4105_btnDiv" style="padding:0;">
			<c:if test="${param.type == 'reject'}">
				<div class="button_complete req4105_complete" onclick="fnReq4108RejectAction()"><i class="fa fa-undo-alt"></i>&nbsp;반려</div>
			</c:if>
			<div class="button_complete req4105_close" onclick="gfnLayerPopupClose()"><i class="fa fa-times"></i>&nbsp;닫기</div>
		</div>
	</div>
</form>
</div>
</body>
</html>