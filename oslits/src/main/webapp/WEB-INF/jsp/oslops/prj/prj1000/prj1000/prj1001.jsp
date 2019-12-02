<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<title>OpenSoftLab</title>
<style>
	.check_prj{padding:31px 25px !important;}
	.checking{color:#4b73eb;padding:5px 0px; display:inline-block;}
	.dup_btn { font-size: 0.95em; }
	.search_select {width : 30%; float:left; line-height:24px; font-weight: bold;}
	#prjGrpPeriod { float: left; /* padding-left:5px; padding-top: 5px;  */line-height: 2em; padding: 0px; width: 100%; height: 100%;}
	.textarea_height{height:334px !important;}
	.fl{border-bottom:none !important;}
	.pop_sub input[type="number"] { width:100%; height:100%; }
	.pop_sub input[type="text"].input_txt { width:100%; height:100%; }
	.pop_right {height:50px;}
	.pop_left {height:50px;}
	/* 팝업 위치 */
	.ui-datepicker-trigger { padding-top: 5px; }
	.required_info{color:red; font-weight: bold; }
</style>

<script type="text/javascript">

//유효성
var arrChkObj = {"prjNm":{"type":"length","msg":"프로젝트 ${prjGrpNm}명은 200byte까지 입력이 가능합니다.",max:200}
				,"prjAcrm":{"type":"length","msg":"프로젝트 약어는 10byte까지 입력이 가능합니다.",max:10}
				,"prjDesc":{"type":"length","msg":"프로젝트 설명은 4000byte까지 입력이 가능합니다.",max:4000}
				,"ord":{"type":"number"}};

// 프로젝트 약어 영문 숫자여부 체크
var arrChkObj2 = {"prjAcrm":{"type":"english","engOption":"includeNumber"}};

$(document).ready(function() {
	//그룹 or 단위
	var type = "${type}";

	// 유효성 체크
	gfnInputValChk(arrChkObj);
	
	// 프로젝트 약어 영문 숫자여부 체크
	gfnInputValChk(arrChkObj2);
	
	var prjGrpStartDt = "${startDt}";
	var prjGrpEndDt = "${endDt}";

	//그룹인경우
	if(type == "group"){
		//그룹 정보 숨김
		$("#popupPrjForm .prjGrpNameSpan").hide();
		
		// 프로젝트 약어 숨김
		$("#popupPrjForm .popPrjAcrmDiv").hide();
		
		//그룹 코드
		$("#prjGrpCd").val("01");
		
		/* 달력 세팅(시작일, 종료일) */
		gfnCalRangeSet("startDt", "endDt");
	}
	//단위 프로젝트인경우
	else{
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
	}

	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
	*/
	var mstCdStrArr = "PRJ00001";
	var strUseYn = 'Y';
	var arrObj = [$("#prjTaskTypeCd")];
	var arrComboType = ["",""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	/* 저장버튼 클릭 시 */
	$('#btn_insertReg').click(function() {
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "popupPrjForm";
		var strCheckObjArr = ["prjNm", "startDt", "endDt", "ord"];
		var sCheckObjNmArr = ["프로젝트 명", "시작일자", "종료일자", "정렬 순서"];
		
		//필수 값 확인
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		
		// 단위 프로젝트인 경우
		if(type != "group"){
			
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

		// 프로젝트 등록
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
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancelReg').click(function() {
		gfnLayerPopupClose();
	});
	
});



/**
 * 프로젝트 생성관리 등록(insert) AJAX
 */
function fnInsertReg(){
	//프로젝트 설명이 빈값인경우 빈문자열 추가
	if(gfnIsNull($("#popupPrjForm #prjDesc").val())){
		$("#popupPrjForm #prjDesc").val(" ");
	}
	
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
			if(data.duplicateYN=="Y"){
				jAlert("이미 등록된 약어 입니다.", '알림창');
			}else{
				jAlert(data.message, '알림창');	
			}
		}else{
	    	toast.push(data.message);
	    	fnSelectPrjList();
	    	zTree.expandAll(true);
	    	
			prjInfoArray.push({"name":"prjId","value":data.prjId});
	    	
	    	//jsonArray to json Map
			var prjInfo = {};
			$.each(prjInfoArray,function(idx, map){
				prjInfo[map.name] = map.value;
			});

			var type = $("#type").val();
			
			//그룹 폴더형 아이콘
			if(type == "group"){
				prjInfo.isParent = true;
			}
			//상단 콤보박스 정보 변경
			fnHeaderHandle(prjInfo,"insert");
	    	
	    	/*
	    	//prjId 추가
	    	
	    	
			
			//zTree에 생성 프로젝트 추가
			var parentNode = zTree.getSelectedNodes()[0];
			zTree.addNodes(parentNode,prjInfo);
			*/
	    	//layerpopup close
			gfnLayerPopupClose();
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


</script>

<div class="popup">
	<div class="pop_title">프로젝트 ${groupStr} 등록</div>
	<div class="pop_sub">
		<form id="popupPrjForm" name="popupPrjForm" method="post">
			<input type="hidden" name="type" id="type" value="${type}"/>
			<input type="hidden" name="prjGrpId" id="prjGrpId" value="${prjGrpId}"/>
			<input type="hidden" id="useCd" name="useCd" value="01" />
			<input type="hidden" id="prjGrpCd" name="prjGrpCd" value="01" />
			<input type="hidden" id="prjType" name="prjType" value="01" />
			<div class="pop_left prjGrpNameSpan">프로젝트 그룹</div>
			<div class="pop_right prjGrpNameSpan">
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
				<span class="fl"><input type="text" id="startDt" name="startDt" class="calendar_input" readonly="readonly" title="개발 시작일" style="height: 32px;"/></span>
				<span class="calendar_bar fl">~</span>
				<span class="fl"><input type="text" id="endDt" name="endDt" class="calendar_input" readonly="readonly" title="개발 종료일"/ style="height: 32px;"></span>
			</div>
			<div class="pop_left popPrjAcrmDiv">프로젝트 약어<span class="required_info">&nbsp;*</span> </div>
			<div class="pop_right popPrjAcrmDiv">
				<input id="prjAcrm" type="text"  name="prjAcrm" maxlength="10" />
			</div>
			<div class="pop_left popPrjAcrmDiv">프로젝트 사업 구분<span class="required_info">&nbsp;*</span> </div>
			<div class="pop_right popPrjAcrmDiv">
				<select class="sel_menu" name="prjTaskTypeCd" id="prjTaskTypeCd"></select> (* 생성시 수정 불가)
			</div>
			<div class="menu_col_textarea">
				<div class="pop_left">프로젝트 ${groupStr}설명</div>
				<div class="pop_right">
					<textarea class="input_note" style="height: 100%;" title="설명" id="prjDesc" name="prjDesc"></textarea>
				</div>
			</div>
			<div class="pop_left bottom_line">정렬 순서<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right bottom_line">
				<input type="number" min="1" id="ord" name="ord" value="1" title="정렬 순서"/>	<!-- 기본적인 한줄           input에선 class 없음 -->
			</div>
			<div class="btn_div">
				<div class="button_normal save_btn complete_btn" id="btn_insertReg">저장</div>
				<div class="button_normal exit_btn" id="btn_cancelReg">취소</div>	
			</div>
		</form>	
	</div>
</div>

</html>