<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<title>OpenSoftLab</title>

<style>
	.ui-draggable {top: -68px;}
	.check_prj{padding:31px 25px !important;}
	.checking{color:#4b73eb;padding:5px 0px; display:inline-block;}
	.dup_btn { font-size: 0.95em; }
	.pop_left { padding-top:20px; padding-bottom:20px; }
	.pop_right {height:50px;}
	.pop_left {height:50px;}
	.ui-datepicker-trigger { padding-top: 5px; }
</style>

<script>

//유효성 체크
var arrChkObj = {"prjNm":{"type":"length","msg":"프로젝트 ${prjGrpNm}명은 200byte까지 입력이 가능합니다.",max:200}
				,"prjAcrm":{"type":"length","msg":"프로젝트 약어는 10byte까지 입력이 가능합니다.",max:10}
				,"prjDesc":{"type":"length","msg":"프로젝트 설명은 4000byte까지 입력이 가능합니다.",max:4000}
				,"ord":{"type":"number"}};

// 프로젝트 약어 영문 숫자여부 체크
var arrChkObj2 = {"prjAcrm":{"type":"english","engOption":"includeNumber"}};

//생성 타입 (group - 그룹, project - 단위)
$(document).ready(function() {
	//그룹 or 단위
	var type = "${type}";
	
	// 유효성 체크
	gfnInputValChk(arrChkObj);
	// 프로젝트 약어 유효성 체크
	gfnInputValChk(arrChkObj2);
	
	var prjGrpStartDt = "${startDt}";
	var prjGrpEndDt = "${endDt}";
	
	//프로젝트 그룹 생성 메시지
	if(type == "group"){
		//그룹 정보 숨김
		$("#popupPrjForm .prjGrpNameSpan").hide();
		
		// 프로젝트 약어 숨김
		$("#popupPrjForm .popPrjAcrmDiv").hide();
		
		//그룹 코드
		$("#prjGrpCd").val("01");
		
		/* 달력 세팅(시작일, 종료일) */
		gfnCalRangeSet("startDt", "endDt");
		
		jAlert("프로젝트 그룹 생성이 필요합니다.", "알림", function( result ) {
			if( result ){
				jAlert("현재 진행하고자 하는 프로젝트 그룹의 이름, 기간, 설명 등을 \n입력하고 저장하여 진행 해주세요.", "알림");
			}
		});
	}
	//단위 프로젝트 생성 메시지
	else if(type == "project"){
		prjGrpStartDt = prjGrpStartDt.replace( /(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
		prjGrpEndDt = prjGrpEndDt.replace( /(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
		
		//그룹 기간 표시
		//$("#prjGrpPeriod").text(prjGrpStartDt + " ~ " + prjGrpEndDt);
		$("#prjGrpDt_txt").val(prjGrpStartDt + " ~ " + prjGrpEndDt);
		
		//그룹 정보 표시
		$("#popupPrjForm .prjGrpNameSpan").show();
		
		//프로젝트 약어 표시
		$("#popupPrjForm .popPrjAcrmDiv").show();
		
		//그룹 코드
		$("#prjGrpCd").val("02");
		
		/* 달력 세팅(시작일, 종료일) */
		gfnCalRangeSet("startDt", "endDt", prjGrpStartDt, prjGrpEndDt);
		
		jAlert("프로젝트 그룹 생성을 완료하셨습니다.", "알림", function( result ) {
			if( result ){
				jAlert("다음으로  프로젝트를 생성하여야 합니다.", "알림", function( result ) {
					if( result ){
						jAlert("현재 진행하고자 하는 프로젝트의 이름, 기간, 설명 등을 \n입력하고 저장하여 해주세요.", "알림");
					}
				});
			}
		});
	}else{
		//호출 오류
	}
	
	
	//레이어 팝업 닫기 막기
	$('.close_btn.white').click(function(event){
		event.stopImmediatePropagation();
		return;
	});
	//CTRL + 클릭 막기
	ctrlCloseVar = "N";
	$(document).click('onmousedown', function(event){
	  	if(event.ctrlKey && (event.button == 0) ){
	    	return false;
	  	}
	});
	
	//esc키 막기
	$(document).off("keyup");
	
	/* 저장버튼 클릭 시 */
	$('#btn_insertReg').click(function() {
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "popupPrjForm";
		var strCheckObjArr = ["prjNm", "startDt", "endDt", "ord"];
		var sCheckObjNmArr = ["프로젝트 명", "시작일자", "종료일자", "정렬순서"];
		
		//필수 값 확인
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		
		// 단위 프로젝트인 경우
		if(type == "project"){
			
			// 프로젝트 약어는 필수값
			var strCheckObjArr2 = ["prjAcrm"];
			var sCheckObjNmArr2 = ["프로젝트 약어"];

			if(gfnRequireCheck(strFormId, strCheckObjArr2, sCheckObjNmArr2)){
				return false;	
			}

			// 프로젝트 약어 유효성 검증
			var saveObjectValid = {
					// 영문 대문자 또는 영문대문자+숫자 조합 입력
					"prjAcrm":{"type":"regExp","pattern":/^(?=.*?[A-Z])(?=.*?[0-9])|[A-Z]{3,10}$/ ,"msg":"프로젝트 약어는 영문 대문자 또는 영문 대문자, 숫자 조합으로 3~10자만 사용 가능합니다.", "required":true} 
			}
			
			// 약어 유효성 검사
			if(!gfnInputValChk(saveObjectValid)){
				return false;	
			}
			
			// 등록 전 약어 유효성 검사
			if(!gfnSaveInputValChk(saveObjectValid)){
				return false;	
			}
		}

		// 등록 전 입력값 유효성 검사
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}
		
		//error있는경우 오류
		if($("#popupPrjForm .inputError").length > 0){
			jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
			$(".inputError")[0].focus()
			return false;
		}
		
		// 저장
		fnInsertReg();
		
	});
	
	/* 프로젝트 약어 입력될 경우 이벤트 */
	$('#prjAcrm').change(function() {
		  if($("#prjAcrm").hasClass("inputError")){
				$("#prjAcrm").removeClass("inputError");
          }
	});
	
	// 프로젝트 약어 입력 keyup 이벤트
	$("#prjAcrm").keyup(function(e){
		 var inputVal = $("#prjAcrm").val();
		 // 입력된 값을 대문자로 변환
	 	$("#prjAcrm").val(inputVal.toUpperCase());
	});
		
	// 프로젝트 약어 focusout 이벤트
	$("#prjAcrm").focusout(function(e){
		var inputVal = $("#prjAcrm").val();  
		// 입력된 값을 대문자로 변환
	 	$("#prjAcrm").val(inputVal.toUpperCase());
		// 숫자만 입력했는지 체크
		if($.isNumeric($("#prjAcrm").val())){
			jAlert("프로젝트 약어는 영문 대문자 또는 영문 대문자, 숫자 조합으로 입력해야 합니다.", "알림창");
			$("#prjAcrm").val(''); 
			$("#prjAcrm").focus();
		}
	});
	/**
	 * 프로젝트 생성관리 등록(insert) AJAX
	 */
	function fnInsertReg(){
		
		//입력 정보
		var prjInfoArray = $("#popupPrjForm").serializeArray();
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/prj/prj1000/prj1000/insertPrj1001Ajax.do'/>"}
				,prjInfoArray);
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			if(data.errorYN == "Y"){
				jAlert(data.message, '알림창');
			}else{
				//그룹 생성인경우 프로젝트 생성 팝업 재 호출
		    	if(type == "group"){
		    		gfnLayerPopupClose();
		    		
		    		//프로젝트 생성 팝업 호출
					var data = {type: "project"};
					gfnLayerPopupOpen('/prj/prj1000/prj1000/selectPrj1003View.do', data, '761', '580','auto');
		    	}else{
		    		//프로젝트 생성 완료된경우
		    		jAlert('프로젝트가 정상적으로 생성되었습니다.', '알림창', function( result ) {
						if( result ){
					    	//로그인 페이지로 강제 이동
					    	document.location.href="/cmm/cmm4000/cmm4000/selectCmm4000View.do"
						}
					});
		    	}
			}
			
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
	 	});
		
		//AJAX 전송
		ajaxObj.send();
	}
});

</script>

<div class="popup">
	<div class="pop_title">최초 라이선스 등록자 프로젝트 ${groupStr}등록</div>
	<div class="pop_sub">
		<form id="popupPrjForm" name="prjForm" method="post">
			<input type="hidden" name="type" id="type" value="${type}"/>
			<input type="hidden" name="prjGrpId" id="prjGrpId" value="${prjGrpId}"/>
			<input type="hidden" id="useCd" name="useCd" value="01" />
			<input type="hidden" id="prjGrpCd" name="prjGrpCd" value="01" />
			<input type="hidden" name="ord" id="ord" value="1">
			<input type="hidden" name="prjType" id="prjType" value="01">
			<div class="pop_left prjGrpNameSpan">프로젝트 그룹</div>
			<div class="pop_right prjGrpNameSpan">
				<!--
				<span id="search_select" style="line-height: 28px;margin-left: 5px;">${prjGrpNm }</span>
				-->
				<input type="text" id="prjGrpNm_txt" name="prjGrpNm_txt" class="grpNm_txt" readonly="readonly" title="프로젝트 그룹 명" value="${prjGrpNm}"/>
			</div>
			<div class="pop_left prjGrpNameSpan">프로젝트 그룹 기간</div>
			<div class="pop_right prjGrpNameSpan">
				<div id="prjGrpPeriod">
					<input type="text" id="prjGrpDt_txt" name="prjGrpDt_txt" class="dt_txt" readonly="readonly" title="프로젝트 그룹 기간"/>
				</div>
			</div>
			<div class="pop_left">프로젝트 ${groupStr}명<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right">
				<input type="text" title="프로젝트 ${groupStr}명" class="input_txt" id="prjNm" name="prjNm">
			</div>
			<div class="pop_left">프로젝트 ${groupStr}기간<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right">
				<span class="fl"><input type="text" id="startDt" name="startDt" class="calendar_input" readonly="readonly" title="개발 시작일" style="height: 32px;" /></span>
				<span class="calendar_bar fl">~</span>
				<span class="fl"><input type="text" id="endDt" name="endDt" class="calendar_input" readonly="readonly" title="개발 종료일" style="height: 32px;"/></span>
			</div>
			<div class="pop_left popPrjAcrmDiv">프로젝트 약어<span class="required_info">&nbsp;*</span> </div>
			<div class="pop_right popPrjAcrmDiv">
				<input id="prjAcrm" type="text"  name="prjAcrm" maxlength="10" />
			</div>
			<div class="menu_col_textarea">
				<div class="pop_left bottom_line">프로젝트 ${groupStr}설명</div>
				<div class="pop_right bottom_line">
					<textarea class="input_note" style="height: 100%;" title="설명" id="prjDesc" name="prjDesc"></textarea>
				</div>
			</div>
			<div class="btn_div">
				<div class="button_normal save_btn complete_btn" id="btn_insertReg">저장</div>
			</div>
		</form>	
	</div>
</div>

</html>