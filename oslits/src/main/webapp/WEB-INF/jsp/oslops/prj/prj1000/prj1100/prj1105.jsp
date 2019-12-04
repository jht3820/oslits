<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<style>
.pop_menu_topLine{border-top: 1px solid #ddd;}
.pop_menu_col2 select {
    width: 100%;
    height: 100%;
    border: 1px solid #ccc;
    text-align-last: center;
    font-size: 10pt !important;
}
.button_complete{margin: 0 10px 0 10px;}
.pop_menu_row .pop_menu_col1, .pop_menu_row .pop_menu_col2{height:48px;line-height:33px;}
</style>
<script>

//사업 구분
var prjTaskTypeCd = "${sessionScope.prjTaskTypeCd}";

var popupGb = "${param.popupGb}";

$(document).ready(function() {
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
	*/
	var mstCdStrArr = "FLW00006";
	var strUseYn = 'Y';
	var arrObj = [$("#processTypeCd")];
	var arrComboType = ["OS"];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	//유효성 체크
	var arrChkObj = {"processNm":{"type":"length","msg":"프로세스 명은 100byte까지 입력이 가능합니다.",max:100}
					,"processDesc":{"type":"length","msg":"프로세스 설명은 4000byte까지 입력이 가능합니다.",max:4000}
					,"processOrd":{"type":"number","msg":"프로세스 순서는 숫자만 입력 가능합니다.",min:0}
					};
	gfnInputValChk(arrChkObj);
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_close_optClose').click(function() {
		gfnLayerPopupClose();
	});
	
	//생성인경우
	if(popupGb == "insert"){
		//순서 자동 입력
		$("#processOrd").val(($(".process_box").length+1));
	}
	//수정인경우
	if(popupGb == "update"){
		//설명 값이 있는 경우
		if(!gfnIsNull("${processInfo.processDesc}")){
			//프로세스 설명 입력
			$("#processDesc").val(decodeURI("${processInfo.processDesc}"));
		}
	}
	
	//입력 포커스
	$("#processNm").focus();

	//프로세스 저장 버튼
	$("#btn_insert_flowPopup").click(function(){
		// 저장 전 유효성 체크
		if(gfnSaveInputValChk(arrChkObj)){
			fnProcessSaveAjax();
		}
	});
});


//프로세스 삽입 또는 수정
function fnProcessSaveAjax(){
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus()
		return false;
	}
	
	var processId = "";
	var processConfirmCd = "";
	var url = "";
	var queryJson = {};
	
	
	if(popupGb == "insert"){ //삽입
		url = "<c:url value='/prj/prj1000/prj1100/insertPrj1100ProcessInfoAjax.do'/>"
		processConfirmCd = "01";
	}else if(popupGb == "update"){ //수정
		url = "<c:url value='/prj/prj1000/prj1100/updatePrj1100ProcessInfoAjax.do'/>"
		processId = $(".process_box.active").attr('id');
	}else{
		jAlert("잘못된 요청입니다.","경고");
	}
	
	//값 체크
	var processNm = $("#processNm").val();
	if(!gfnIsNull(processNm)){
		//프로세스 설명 - 4000byte로 짜르기
		var processDesc = encodeURI(gfnCutStrLen($("#processDesc").val(),4000));

		//프로세스 정보
		var processId = "${param.processId}";
		var processNm = $("#processNm").val();
		var processOrd = $("#processOrd").val();
		var processTypeCd = $("#processTypeCd").val();
		var processConfirmCd = $("#processConfirmCd").val();
		
		//확정 구분 없으면 01
		if(gfnIsNull(processConfirmCd)){
			processConfirmCd = "01";
		}
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":url},
				{processNm:processNm,processId: processId,processDesc: processDesc, processOrd: processOrd, processTypeCd: processTypeCd, processConfirmCd: processConfirmCd});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			toast.push(data.message);
			
			//에러 없는경우
			if(data.errorYN != "Y"){
				//업데이트인경우
				if(popupType == "update"){
					$(".process_box.active").html(processNm);
					$(".process_box.active").attr("desc",processDesc);
					$(".process_box.active").attr("ord",processOrd);
				}else{
	        		//프로세스 목록 재 조회
	        		fnProcessListAjax();
				}
				
				$("#btn_insert_plcCancle").click();
			}
			
			gfnLayerPopupClose();
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림");
	 	});
		//AJAX 전송
		ajaxObj.send();
	}else{
		jAlert("프로세스명을 입력해주세요.","알림",function(){
			//입력 포커스
			$("#processNm").focus();
		});
		return false;
	}
}
</script>

<div class="popup" style="height:100%;">
<form id="prj1105PopupFrm" name="prj1105PopupFrm" method="post">
	<input type="hidden" name="processConfirmCd" id="processConfirmCd" value="${processInfo.processConfirmCd}" />
	<div class="pop_title">프로세스 <c:choose><c:when test="${param.popupGb == 'update'}">수정</c:when><c:otherwise>생성</c:otherwise></c:choose></div>
	<div class="pop_sub" style="padding:15px;display: inline-block;">
		<div class="pop_menu_row pop_menu_oneRow pop_menu_topLine">
			<div class="pop_menu_col1"><label for="prjNm">프로세스 명 </label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2">
				<input id="processNm" type="text" name="processNm" title="프로세스 명" value="${processInfo.processNm}" />
			</div>
		</div>
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1"><label for="prjNm">순서 </label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2">
				<input id="processOrd" type="number" name="processOrd" title="순서" value="${processInfo.processOrd}" min="0"/>
			</div>
		</div>
		<c:if test="${sessionScope.selPrjTaskTypeCd == '01'}">
			<div class="pop_menu_row pop_menu_oneRow">
				<div class="pop_menu_col1"><label for="prjNm">프로세스 구분</label><span class="required_info">&nbsp;*</span></div>
				<div class="pop_menu_col2">
					<c:choose>
						<c:when test="${param.popupGb == 'update' and processInfo.processConfirmCd == '01'}">
							<select id="processTypeCd" name="processTypeCd" modifyset="false" OS="${processInfo.processTypeCd}"></select>
						</c:when>
						<c:when test="${param.popupGb != 'update' and not empty sessionScope.selPrjTaskTypeCd and sessionScope.selPrjTaskTypeCd == '01'}">
							<select id="processTypeCd" name="processTypeCd" modifyset="false" OS="${processInfo.processTypeCd}"></select>
						</c:when>
						<c:otherwise>
							${processInfo.processTypeNm}
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:if>
		<div class="pop_note" style="margin-bottom:10px;">
			<div class="note_title">프로세스 설명</div>
			<textarea class="input_note" title="프로세스 설명" name="processDesc" id="processDesc" rows="7" value=""  ></textarea>
		</div>
		
		<div class="flw_btn_box">
			<div class="button_complete" id="btn_insert_flowPopup"><i class="fa fa-save"></i>&nbsp;저장</div>
			<div class="button_complete" id="btn_close_optClose"><i class="fa fa-times"></i>&nbsp;닫기</div>
		</div>
	</div>
</div>
</form>
</div>
</html>
