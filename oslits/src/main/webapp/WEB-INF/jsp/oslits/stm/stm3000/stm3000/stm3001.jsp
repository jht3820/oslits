<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<script src="<c:url value='/js/common/oslFile.js'/>"></script>
<script src="<c:url value='/js/common/comOslits.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<style>
.layer_popup_box .close_btn{top:12px; width:18px; height:18px; background:url(/images/login/x_white.png) no-repeat}
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
.required_info { color: red; }

/*익스플로러 적용 위해 !important 추가*/
/* 팝업에 따라 pop_menu_col1, pop_menu_col2 높이 변경 */
.pop_menu_row .pop_menu_col1 { width: 23% !important; height: 45px !important; padding-left: 6px !important; }
.pop_menu_row .pop_menu_col2 { width: 77% !important; height: 45px !important; }
.pop_menu_row .menu_col1_subStyle { width: 46% !important; }
.pop_menu_row .menu_col2_subStyle { width: 54% !important; }

#btn_job_select { width: 52px; height: 100%; padding-top: 3px;}

.pop_sub input[type="password"] {width:100% !important; height:100% !important;}
</style>
<script>
var fd = new FormData();
//중복 파일 업로드 방지 전역변수
var fileChk = new Array();

//현재 값
var nowJenUsrTok = null;
var nowJenTok = null;
// JENKINS 유효성
var arrChkObj = {	
			        "jenUrl":{"type":"length","msg":"URL은 500byte까지 입력이 가능합니다.","max":500},
			        "jenUsrId":{"type":"length","msg":"User Id는 20byte까지 입력이 가능합니다.","max":20},
			        "jenNm":{"type":"length","msg":"Job명은 500byte까지 입력이 가능합니다.","max":500},
			        "jenTxt":{"type":"length","msg":"JOB 설명은 1000byte까지 입력이 가능합니다.","max":1000} 
			};

$(document).ready(function() {

	$('#jenNm').focus();
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
	*/
	var mstCdStrArr = "CMM00001";
	var strUseYn = 'Y';
	var arrObj = [ $("#useCd")];
	var arrComboType = [ ""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	//탭인덱스 부여
	//gfnSetFormAllObjTabIndex(document.getElementById("jen1000PopupFrm"));
	
	// 유효성 체크
	gfnInputValChk(arrChkObj);
	
	/* 타이틀 변경 및 버튼명 변경, 수정일경우 값 세팅 */
	if('${param.popupGb}' == 'insert'){
		$(".pop_title").text("JENKINS 설정 등록");
		$("#btn_update_popup").text('등록');
	}
	else if('${param.popupGb}' == 'update'){
		
		$(".pop_title").text("JENKINS 설정 수정");
		$("#btn_update_popup").text('수정');
		
		var jenId = '${param.jenId}';
		fnSelectJen1001JobInfo(jenId);
	}
	
	/**
	 * 	젠킨스 job 기본정보 상세 조회
	 */
 	function fnSelectJen1001JobInfo(jenId){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm3000/stm3000/selectStm3000JobDetailAjax.do'/>"}
				,{ "jenId" : jenId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){

			data = JSON.parse(data);

        	//디테일폼 세팅
        	gfnSetData2ParentObj(data.jenInfo, "jen1000PopupFrm");

        	nowJenUsrTok = data.jenInfo.jenUsrTok;
			nowJenTok = data.jenInfo.jenTok;
        	fnEnableJobBtn();
        	
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
	} 
	
	/* 저장버튼 클릭 시 */
	$('#btn_update_popup').click(function() {
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "jen1000PopupFrm";
		var strCheckObjArr = ["jenNm", "jenUsrId","jenUsrTok"];
		var sCheckObjNmArr = ["Job 명", "USER" ,"USER TOKEN KEY" ];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		
		var formObj = document.getElementById("jen1000PopupFrm");
		
		// 등록/수정 전 유효성 체크
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}	

		fnInsertReqInfoAjax("jen1000PopupFrm");

	});
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancle_popup').click(function() {
		
		gfnLayerPopupClose();
	});
	
	
	$('#btn_job_select').click(function(){
		fnSelectStm3000URLConnect();
	});
	
	$('#jenUsrId').focusout(function(){
		fnEnableJobBtn();
	});
	$('#jenUsrTok').focusout(function(){
		fnEnableJobBtn();
	});
	$('#jenUrl').focusout(function(){
		fnEnableJobBtn();
	});
	
	$('#btn_job_select').hide();
	$('#jenNm').css("width","100%");
	
});

//요구사항 등록 함수
function fnInsertReqInfoAjax(formId){
	//FormData에 input값 넣기
	gfnFormDataAutoValue('jen1000PopupFrm',fd);
	
	//기본 값과 type 넘기기
	fd.append("type",'${param.popupGb}');
	fd.append("nowJenUsrTok",nowJenUsrTok);
	fd.append("nowJenTok",nowJenTok);
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/stm/stm3000/stm3000/saveStm3000JobInfoAjax.do'/>"
				,"contentType":false
				,"processData":false
				,"cache":false}
			,fd);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	//로딩바 숨김
    	gfnShowLoadingBar(false);
    	
    	//코멘트 등록 실패의 경우 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return;
    	}
    	
    	//그리드 새로고침
		fnInGridListSet(firstGrid.page.currentPage,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
    	
		jAlert(data.message, '알림창');
		gfnLayerPopupClose();
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push(xhr.status+"("+err+")"+" 에러가 발생했습니다.");
    	gfnLayerPopupClose();
	});
	//AJAX 전송
	ajaxObj.send();
}

function fnEnableJobBtn(){
	
	if( $('#jenUsrId').val() != "" &&
		$('#jenUsrTok').val() != "" &&
		$('#jenUrl').val() != "" ){
		
		$('#btn_job_select').show();
		$('#jenNm').css("width","87%");
	}else{
		$('#btn_job_select').hide();
		$('#jenNm').css("width","100%");
	}
}


function fnSelectStm3000URLConnect(){
	//AJAX 설정
	var jenUsrId = $('#jenUsrId').val();
	var jenUsrTok = $('#jenUsrTok').val();
	var jenUrl = $('#jenUrl').val();
	
	var type = '${param.popupGb}';
	
	//jenUsrTok이 기존 값과 다른경우 변경된 값으로 체크
	if(nowJenUsrTok != jenUsrTok){
		type = "insert";
	}
	
	var param = {
			"type":type,
			"jenUsrId" : jenUsrId ,
			 "jenUsrTok" : jenUsrTok ,
			 "jenUrl" : jenUrl 
		   };
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/stm/stm3000/stm3000/selectStm3000URLConnect.do'/>"}
			, param );
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		if(data.MSG_CD=="JENKINS_OK"){
			gfnLayerPopupOpen('/stm/stm3000/stm3000/selectStm3002JobView.do',param, "560", "453",'scroll');
			
		}else if(data.MSG_CD=="JENKINS_FAIL"){
			jAlert("설정 정보가 잘못 입력 되었거나, JENKINS 서버에 문제가 있습니다.<br/><br/>입력한 URL, USER, USER TOKEN KEY 를 확인 하시거나, JENKINS 서버를 확인 해주시기 바랍니다.", "알림창");
		}else if(data.MSG_CD=="JENKINS_WORNING_URL"){
			jAlert("허용되지 않는 URL입니다.<br/><br/>입력한 URL를 확인하십시오", "알림창");
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

<div class="popup" >
<form id="jen1000PopupFrm" name="jen1000PopupFrm" method="post">
	<input type="hidden" name="popupGb" id="popupGb" value="${param.popupGb}"/>
	<input type="hidden" name="jenId" id="jenId" value="${param.jenId}" />
	<input type="hidden" name="prjId" id="prjId" value="${sessionScope.selPrjId}"/>
	<input type="hidden" name="reqStatusCd" id="reqStatusCd" value="01"/>

	<div class="pop_title">JENKINS 설정 등록</div>
	<div class="pop_sub">
	
		<div class="pop_menu_row pop_menu_oneRow first_menu_row">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="jenUrl">URL</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<input type="text" title="URL" class="input_txt" name="jenUrl" id="jenUrl" value="" maxlength="500"  />
			</div>
		</div>
	
		<div class="pop_menu_row">
			<div class="pop_menu_col1 menu_col1_subStyle"><label for="jenUsrId">USER</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 menu_col2_subStyle"><input type="text" title="USER" class="input_txt" name="jenUsrId" id="jenUsrId" value="" maxlength="20" /></div>
		</div>
		
		<div class="pop_menu_row">
			<div class="pop_menu_col1 menu_col1_subStyle pop_menu_col1_right"><label for="useCd">사용여부</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 menu_col2_subStyle">
				<span class="search_select">
					<select class="select_useCd" name="useCd" id="useCd" value="" style="height:100%; width:100%;"></select>
				</span>
			</div>
		</div>


		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="jenUsrTok">USER TOKEN KEY</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<input type="password" title="USER TOKEN KEY" class="input_txt" name="jenUsrTok" id="jenUsrTok" value="" maxlength="50" />
			</div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="jenNm">Job 명</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<input type="text" title="Job 명 " class="input_txt" name="jenNm" id="jenNm" value="" style="width:87%;" maxlength="500" readonly />
				<span class="button_normal2 fl" id="btn_job_select"><i class="fa fa-search" styel="font-size: 15px;"></i></span>
			</div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="jenTok">TOKEN KEY</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<input type="password" title="TOKEN KEY" class="input_txt" name="jenTok" id="jenTok" value="" maxlength="50"  />
			</div>
		</div>
		
		<div class="pop_note" style="margin-bottom:0px;">
			<div class="note_title">JOB 설명</div>
			<textarea class="input_note" title="JOB 설명" name="jenTxt" id="jenTxt" rows="7" value="" maxlength="1000"  ></textarea>
		</div>
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_update_popup"></div>
			<div class="button_normal exit_btn" id="btn_cancle_popup">취소</div>
		</div>
	</div>
</form>
</div>

</html>
