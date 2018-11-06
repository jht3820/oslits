<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<script src="<c:url value='/js/common/oslFile.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<style>
.layer_popup_box .close_btn{top:12px; width:18px; height:18px; background:url(/images/login/x_white.png) no-repeat}
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
.required_info { color: red; }
.pop_menu_row .pop_menu_col2 .pop_radio{ line-height: 17px; padding-top: 5px;}  
.pop_menu_row .pop_col2_input{ text-align: left; padding-left: 5px; }   

/* 달력 */
.ui-datepicker-trigger{ padding-left: 0px; padding-top: 1px; width: 27px; margin-left:0px;}

/* 팝업창 위치 */
.ui-draggable{ top: -28px;}

#menu_col2_input{ padding-right: 9px; padding-left: 0; }  
#reqNm, #reqUsrNum, #reqUsrEmail {width: 100%;} 
#reqDtm {width: 84%; margin-right: 0px;}
#reqDesc{ height: 200px;}
#reqUsrNm {width: 81%}
#btn_user_select { min-width: 35px; height: 27px; line-height: 25px;}
#reqInputType_txt { padding-left: 10px; }
#regDtmDay {width: 41%;} 
/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
/* 파일업로드 Div */
#dragandrophandler { border: 1px solid #ccc !important; }
.btn_div{margin-top: 25px !important;}
</style>
<script>
var url = "";
var fd = new FormData();
//중복 파일 업로드 방지 전역변수
var fileChk = new Array();
var fileAppendList = new Array();
// 요청자 이름 체크를 위한 변수
var beforeUsrNm = "";
// 이전 공문번호
var beforeReqNo = "";
//팝업 화면 유형 구분값 - 등록/수정
var pageType = "${param.type}"

//파일 업로드 제한 사이즈
var FILE_INFO_MAX_SIZE = "${fileInfoMaxSize}";
var FILE_SUM_MAX_SIZE = "${fileSumMaxSize}";

//유효성 체크
var arrChkObj = {"reqNm":{"type":"length","msg":"요청제목 500byte까지 입력이 가능합니다.","max":500}
				,"reqNo":{"type":"length","msg":"공문번호는 100byte까지 입력이 가능합니다.", "max":100}
				,"reqUsrNm":{"type":"length","msg":"요청자 명은 100byte까지 입력이 가능합니다.", "max":100}
				,"reqUsrDeptNm":{"type":"length","msg":"소속은 500byte까지 입력이 가능합니다.", "max":500}
				,"reqUsrEmail":{"type":"length","msg":"이메일은 100byte까지 입력이 가능합니다.", "max":100}
				,"reqUsrNum":{"type":"number"}
				,"reqDesc":{"type":"length","msg":"요청내용은 4000byte까지 입력가능합니다.", "max":4000}
				};
				

// 연락처, 이메일  유효성 체크
var saveObjectValid = {
			"reqUsrNum":{"type":"regExp","pattern":/^([0-9]{9,11}).*$/ ,"msg":"연락처 형식이 아닙니다. (예) 01012341234", "required":true}
			 ,"reqUsrEmail":{"type":"regExp","pattern":/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i ,"msg":"이메일 형식이 아닙니다. <br>(예) mymail@naver.com","required":true}
}				

$(document).ready(function() {

	//파일 목록 초기화
	dndCancel();
	
	//유효성 체크
	gfnInputValChk(arrChkObj);
	
	//탭인덱스 부여
	//gfnSetFormAllObjTabIndex(document.getElementById("req4101PopupFrm"));
	
	/* 타이틀 변경 및 버튼명 변경, 수정일경우 값 세팅 */
	if(pageType == 'insert'){
		url = "<c:url value='/req/req4000/req4100/insertReq4100ReqInfoAjax.do'/>";
		
    	// 등록일때  접수유형 디폴트로 시스템에 체크
    	$("#reqNewType_stm").prop("checked", true);

		$(".pop_title").text("요구사항 생성");
		$("#btn_insert_reqPopup").text('등록');
	}
	else if(pageType == 'update'){
		url = "<c:url value='/req/req4000/req4100/updateReq4100ReqInfoAjax.do'/>";
		$(".pop_title").text("요구사항 수정");
		$("#btn_insert_reqPopup").text('수정');
		
		var reqId = '${param.reqId}';
		fnSelectReq4001Info(reqId);	
	}
	
	var serverTime = gfnGetServerTime('yyyy-mm-dd');
	
	// 요청일 현재 날짜로 기본 세팅
	//$("#reqDtm").val( serverTime );
	// 등록일 현재 날짜로 세팅
	$("#regDtmDay").val( serverTime );

	// 요청일 달력 세팅
	gfnCalendarSet('YYYY-MM-DD',['reqDtm']);
	
	$("#btn_user_select").click(function() {
		gfnCommonUserPopup( $('#reqUsrNm').val() ,false,function(objs){
			if(objs.length>0){
				$('#reqUsrId').val(objs[0].usrId);
				$('#reqUsrNm').val(objs[0].usrNm);
				
				// 저장시 input에 입력된 값과 비교하기 위한 사용자 이름세팅
				beforeUsrNm = objs[0].usrNm;
				
				var deptName = gfnGetUpperDeptNames(objs[0].deptId);
				$('#reqUsrDeptNm').val(deptName);				
				$('#reqUsrEmail').val(objs[0].email);
				$('#reqUsrNum').val(objs[0].telno);
			}
		});
	});
	
	/* 요청자명 입력 이벤트 */
	$('#reqUsrNm').keyup(function(e) {
		if($('#reqUsrNm').val()==""){
			$('#reqUsrId').val("");
		}
		
		// 접수유형
		var rdReqNewTypeVal = $('input[name="rdReqNewType"]:checked').val();
		// 입력유형
		var rdReqInputTypeVal = $('input[name="reqInputType"]:checked').val();
		
		// 접수유형이 게시판이 아니고, 입력유형이 직접입력이 아닐경우 사용자 검색
		if( rdReqNewTypeVal != "05" && rdReqInputTypeVal == "N" && e.keyCode == '13' ){
			$('#btn_user_select').click();
		}
	});
	
	
	/**
	 * 	요구사항 하나 선택했을때 요구사항 디테일 정보 조회
	 *	@param selReqId 수정하려는 요구사항
	 */
 	function fnSelectReq4001Info(selReqId){
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4100/selectReq4100ReqInfoAjax.do'/>"}
				,{ "reqId" : selReqId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);  

			// 요구사항 조회 결과
			var reqInfoMap = data.reqInfoMap;

        	//디테일폼 세팅
        	gfnSetData2ParentObj(reqInfoMap, "req4101PopupFrm");
        	
        	// 요청자명 변경시 체크를 위해 값 세팅
        	beforeUsrNm = reqInfoMap.reqUsrNm;
        	
        	// 공문번호값 전역변수에 세팅 
        	// 접수유형을 공문 → 다른유형 → 공문 이렇게 변경할 경우 공문번호값 세팅을 위해 사용
        	beforeReqNo = reqInfoMap.reqNo;
        	
        	// 처리유형
        	var reqProType = reqInfoMap.reqProType;
        	// hidden에 처리 유형 세팅
        	$("#reqProType").val(reqProType);
        	
        	// 접수유형
        	var reqNewType = reqInfoMap.reqNewType;
        	
        	// hidden에 접수 유형 세팅
        	$("#reqNewType").val(reqNewType);

        	// 접수유형에 따른 화면 input 변경
        	fnChangeReqPopupInput(reqNewType);
        	
        	if(reqInfoMap.reqNewType != "05"){
        		if( gfnIsNull(reqInfoMap.reqUsrId) ){
        		
        			$("#reqInputType_Y").prop("checked", true);
        			fnChangeReqInputType("Y");
            	}else{
            		$("#reqInputType_N").prop("checked", true);
            	}
        	}
        	
        	// 접수유형 선택
    		switch(reqNewType){
    		case '01':
				$("#reqNewType_stm").prop("checked", true);
    			break;
    		case '02':
    			$("#reqNewType_tel").prop("checked", true);
    			break;
    		case '03':
    			$("#reqNewType_pub").prop("checked", true);
    			break;
    		case '04':
    			$("#reqNewType_self").prop("checked", true);
    			break;
    		case '05':
    			$("#reqNewType_brd").prop("checked", true);
    			break;	
    		}
    		
	        if(data.fileList.length > 0){
	        	//info 정보 show
	        	$('#dndCancel').show();
	        	$("#file_info").hide();
	        }
	        $.each(data.fileList, function(idx, fileVo){
	        	fileVo["reqId"] = '${param.reqId}';
	        	gfnFileListReadDiv(fileVo,'.pop_file','req');
	        });
	        if(!gfnIsNull(data.fileList)){
	
	        	$('#atchFileId').val(data.fileList[0].atchFileId);
	        	$('#fileCnt').val(data.fileListCnt);
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
	
	/* 저장버튼 클릭 시 */
	$('#btn_insert_reqPopup').click(function() {

		//FormData에 접수유형값 넣기
		var rdList =  $('[name="rdReqNewType"]');
		
		for(var i = 0 ; i < rdList.length; i++ ){
			if($(rdList[i]).prop("checked")){
				$("#reqNewType").val($(rdList[i]).val());
			}
		}
		
		var reqUsrId = $("#reqUsrId").val();
		var reqUsrNm = $("#reqUsrNm").val();
		var reqNewType = $("#reqNewType").val();
		var reqInputType = $('input:radio[name="reqInputType"]:checked').val();
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "req4101PopupFrm";

		// 요청일을 별도로 체크하지 않을경우 reqDtm id를 못찾는 오류가 발생
	 	var strCheckObjArr = ["reqDtm"];
		var sCheckObjNmArr = ["요청일"];

	 	if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		
		var strCheckObjArr2 = ["reqNm", "reqUsrNm", "reqUsrDeptNm", "reqUsrEmail", "reqUsrNum", "reqDesc"];
		var sCheckObjNmArr2 = ["요구사항 명", "요청자", "소속", "이메일", "연락처", "요구사항 내용"];
		
	 	if(gfnRequireCheck(strFormId, strCheckObjArr2, sCheckObjNmArr2)){
			return;	
		}
	 	
		// 공문일 경우 공문번호 입력하도록 처리
		if(reqNewType == "03"){

			var chkReqNoObj = ["reqNo"];
			var chkReqNoObjNm = ["공문번호"];
			// 공문번호 입력 체크
		 	if(gfnRequireCheck(strFormId, chkReqNoObj, chkReqNoObjNm)){
				return;	
			}
		}


		// 연락처, 이메일 유효성 검사
		if(!gfnInputValChk(saveObjectValid)){
			return false;	
		}
		

		// 접수유형이 게시판이 아닐 경우에만 아래 유효성 체크
		if(reqNewType != "05" && reqInputType == "N"){

			// 등록 시 hidden에 요청자 id가 없을 경우
			if( gfnIsNull(reqUsrId) ){
				$("#reqUsrNm").addClass("inputError");
				jAlert("등록된 요청자 명을 검색해서 입력해야 합니다.", '알림창', function( result ) {
					if( result ){
						$("#reqUsrNm").focus();
					}
				});
				return false;	
			}
			
			// 검색을 통해 요청자 ID와 요청자 명이 세팅된 후
			// 화면에서 요청자 명을 임의로 변경했을 경우 체크
			if( reqUsrNm != beforeUsrNm ){	
				$("#reqUsrNm").addClass("inputError");
				jAlert("정확한 요청자 명을 입력해야 합니다.", '알림창', function( result ) {
					if( result ){
						$("#reqUsrNm").focus();
					}
				});
				return false;	
			}
		}
		
		//FormData에 input값 Json형태로 넣기
		gfnFormDataAutoJsonValue(strFormId,fd);
		
		// 기존방식 - FormData에 input값 넣기
		//gfnFormDataAutoValue(strFormId,fd);
		
		
		// 저장 전 연락처, 이메일 유효성 검사
		if(!gfnSaveInputValChk(saveObjectValid)){
			return false;	
		}
		
		// 저장 전 유효성 체크
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}
		
		// 요구사항 저장
		fnInsertReqInfoAjax(strFormId);

	});
	
	/* 접수유형 변경 이벤트 */
	 $(':input:radio[name="rdReqNewType"]').click(function() {
		 var reqNtype = $(":input:radio[name=rdReqNewType]:checked").val();
		 
		 // 입력유형 아니오에 체크
		 $("input:radio[name=reqInputType]:input[value='N']").prop("checked", true);
		 
		 // 접수유형에 따른 화면 input 변경
		 fnChangeReqPopupInput(reqNtype);
	});
	
	 /* 입력유형 변경 이벤트 */
	 $(':input:radio[name="reqInputType"]').click(function() {
		 var inputType = $(":input:radio[name=reqInputType]:checked").val();

		 // 입력유형에 따른 화면 input 변경
		 fnChangeReqInputType(inputType);
	});
	
	/* 요청자 입력될 경우 이벤트 */
	$('#reqUsrNm').keydown(function() {
		$("#reqUsrNm").removeClass("inputError");
	});
	
	/* 요청일 입력될 경우 이벤트 */
	$('#reqDtm').change(function() {
		$("#reqDtm").removeClass("inputError");
	});
	
	/* 소속 입력될 경우 이벤트 */
	$('#reqUsrDeptNm').change(function() {
		$("#reqUsrDeptNm").removeClass("inputError");
	});
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancle_popup').click(function() {
		gfnLayerPopupClose();
	});
	
});

	//요구사항 등록 함수
	function fnInsertReqInfoAjax(formId){
		
	var fileListStr = fnFileUploadStrData();
	
	jConfirm("저장하시겠습니까?"+fileListStr, "알림", function( result ) {
		if( result ){
			//실제 파일 FormData에 적재
			fnFileUploadAppendData();
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":url
						,"contentType":false
						,"processData":false
						,"cache":false}
					,fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
		    	//로딩바 숨김
		    	gfnShowLoadingBar(false)
		    	
		    	//요구사항 등록 실패의 경우 리턴
		    	if(data.saveYN == 'N'){
		    		jAlert(data.message, '알림창', function( result ) {
						if( result ){
							gfnLayerPopupClose();
						}
					});
		    		return;
		    	}
		    	
		    	//그리드 새로고침
		    	if(pageType == "insert"){
		    		// 등록일 경우 그리드 새로고침
		    		fnInGridListSet(0,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
		    	}else if(pageType == "update"){
		    		// 수정일 경우 그리드 현재 페이지 번호, 검색어 정보를 전달하여 새로고침
		    		fnInGridListSet(firstGrid.page.currentPage,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
		    	}
		    	
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
			
		}else{
			fd = new FormData();
		}
	});
	}

	// 접수유형에 따른 입력화면 변경
	function fnChangeReqPopupInput(reqNewType){
		
		$("#reqInputType_div").hide();
		
		// 유선, 공문, 자체식별일 경우 입력유형 활성화
		if(reqNewType == "02" || reqNewType == "03" || reqNewType == "04"){
			$("#reqInputType_div").show();
		}
		
		// 접수유형 공문 선택 시 공문번호 input 활성화
		if(reqNewType == "03"){
			 
			// 이전 공문번호값 있을 경우 세팅 
			if( !gfnIsNull(beforeReqNo) ){
				$("#reqNo").val(beforeReqNo);
			} 
			$("#reqNo").removeClass("readonly");
			$("#reqNo").attr("readonly", false); 
			
		 }else{
			// 공문이 아닐경우 공문번호값 삭제
			$("#reqNo").val("");
			$("#reqNo").attr("class","readonly");
			$("#reqNo").attr("readonly",true);
		 }
		
		// 접수유형이 게시판일 경우 
		if(reqNewType == "05"){
			
			// 타 시스템에서 넘어온 데이터에는 요청자 ID값이 없음
			// hidden에 세팅된 요청자 ID값 제거
			$("#reqUsrId").val("");
			
			// 현 시스템에 등록된 사용자가 아니므로 검색버튼 숨김, input width 조절
			$("#btn_user_select").css("display", "none"); 
			$("#reqUsrNm").css("width", "100%"); 
			
			// 소속 입력 가능하도록 변경
			$("#reqUsrDeptNm").removeClass("readonly");
			$("#reqUsrDeptNm").attr("readonly", false); 
			
		}else{
			// 게시판이 아닐경우 원래 화면으로 변경
			$("#btn_user_select").css("display", "block"); 
			$("#reqUsrNm").css("width", "81%"); 
			$("#reqUsrDeptNm").attr("class","readonly");
			$("#reqUsrDeptNm").attr("readonly",true);
		}
	}
	
	// 입력유형에 따른 입력화면 변경
	function fnChangeReqInputType(inputType){
		
		// 입력유형이 직접입력일 경우
		if(inputType == "Y"){
			$("#reqUsrId").val("");

			// 사용자 검색버튼 숨김, input width 조절
			$("#btn_user_select").css("display", "none"); 
			$("#reqUsrNm").css("width", "100%"); 
			
			// 소속 입력 가능하도록 변경
			$("#reqUsrDeptNm").removeClass("readonly");
			$("#reqUsrDeptNm").attr("readonly", false); 
			
		}else{
			// 직접입력이 아닐경우 원래 화면으로
			$("#btn_user_select").css("display", "block"); 
			$("#reqUsrNm").css("width", "81%"); 
			$("#reqUsrDeptNm").attr("class","readonly");
			$("#reqUsrDeptNm").attr("readonly",true);
		}
	}

</script>

<div class="popup">
<form id="req4101PopupFrm" name="req4101PopupFrm" method="post">
	<input type="hidden" name="popupType" id="popupType" value="${param.type}"/>
	<input type="hidden" name="reqId" id="reqId" value="${param.reqId}" />
	<input type="hidden" name="prjId" id="prjId" value="${sessionScope.selPrjId}"/>
	<!-- 현재 프로젝트의 약어 -->
	<input type="hidden" name="prjAcrm" id="prjAcrm" value="${currPrjInfo.prjAcrm}"/>
	<input type="hidden" name="processId" id="processId"/>
	<!-- 등록 시 처리유형은 기본적으로 접수 요청(01) -->
	<input type="hidden" name="reqProType" id="reqProType" value="01"/>
	<input type="hidden" name="reqUsrId" id="reqUsrId" />
	<input type="hidden" name="reqNewType" id="reqNewType"/>

	<div class="pop_title">	
	</div>

	<div class="pop_sub"> 

		<div class="pop_menu_row pop_menu_oneRow" style="border-top: 1px solid #ddd;">
			<div class="pop_menu_col1 pop_oneRow_col1" ><label for="reqNewType">접수유형</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2" style="text-align: left;" >
				<div class="pop_radio">
					<input id="reqNewType_stm" type="radio" name="rdReqNewType" modifyset="false" value="01" checked="checked" /><label for="reqNewType_stm" >시스템</label>
					<input id="reqNewType_tel" type="radio" name="rdReqNewType" modifyset="false" value="02"/><label for="reqNewType_tel" >유선</label>
					<input id="reqNewType_pub" type="radio" name="rdReqNewType" modifyset="false" value="03"/><label for="reqNewType_pub" >공문</label>
					<input id="reqNewType_self" type="radio" name="rdReqNewType" modifyset="false" value="04"/><label for="reqNewType_self" >자체식별</label>
					<input id="reqNewType_brd" type="radio" name="rdReqNewType" modifyset="false" value="05"/><label for="reqNewType_brd" >게시판</label>
				</div>
			</div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow" id="reqInputType_div" style="display: none;">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="InputType_txt">입력유형</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<div class="pop_radio">
					<label id="reqInputType_txt" for="reqInputType_txt">요청자 정보 직접입력 여부</label>
					<input id="reqInputType_Y" type="radio" name="reqInputType" modifyset="false" value="Y"/><label for="reqInputType_Y" >예</label>
					<input id="reqInputType_N" type="radio" name="reqInputType" modifyset="false" value="N" checked="checked"/><label for="reqInputType_N" >아니오</label>
				</div>
			</div>
		</div>
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="prjNm">체계명</label></div>
			<div class="pop_menu_col2 pop_oneRow_col2"><input id="prjNm" type="text" name="prjNm" title="체계명" modifyset="false" class="readonly" readonly="readonly" value="${currPrjInfo.prjNm}" /></div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="reqNm">요구사항명</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2"><input id="reqNm" type="text" name="reqNm" title="요구사항 명" value=""/></div>
		</div>

		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqNo">공문번호</label></div>
			<div class="pop_menu_col2 pop_col2_input"><input id="reqNo" type="text" name="reqNo" title="공문번호" class="readonly" readonly="readonly" value=""/></div>
		</div>

		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqDtm">요청일</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2"><input id="reqDtm" type="text" name="reqDtm" title="요청일" class="readonly" readonly="readonly" value="" /></div>
		</div>
		
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrNm">요청자</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2" style="text-align: left;  padding-left: 5px;">
			<input id="reqUsrNm" type="text" name="reqUsrNm"  title="요청자" value="" maxlength="25" />
			<span class="button_normal2 fr" id="btn_user_select"><i class="fa fa-search"></i></span>
			</div>
		</div>
		
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrDeptNm">소속</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2"><input id="reqUsrDeptNm" type="text" name="reqUsrDeptNm" title="소속" class="readonly" readonly="readonly" value="" maxlength="230"/></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrEmail">E-mail</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_col2_input"><input id="reqUsrEmail" type="text" title="E-mail" name="reqUsrEmail" value=""/></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1" ><label for="reqUsrNum">연락처</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_col2_input"><input id="reqUsrNum" type="text" name="reqUsrNum" title="연락처" maxlength="11" value="" /></div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="regDtmDay">등록일</label></div>
			<div class="pop_menu_col2 pop_oneRow_col2"><input id="regDtmDay" type="text" name="regDtmDay" class="readonly" title="등록일" readonly="readonly" value=""/></div>
		</div>

		<div class="pop_note" style="margin-bottom:10px;">
			<div class="note_title">요구사항 내용<span class="required_info">&nbsp;*</span></div>
			<textarea id="reqDesc" name="reqDesc" class="input_note" title="요구사항 내용" rows="7" value=""></textarea>
		</div>

		<input type="hidden" id="atchFileId" name="atchFileId"/>
		<input type="hidden" id="fileCnt" name="fileCnt"/>
		<input type="hidden" id="insertFileCnt" name="insertFileCnt"/>
			
		<!-- 파일 목록 표시 -->
		<div class="up_box">
			<!-- <span id="dndCancel" class="abort" style="display: none;" onclick='dndCancel(); '>파일목록 초기화</span> -->
			<span class="button_normal2 del_btn" onclick="document.getElementById('egovFileUpload').click(); " id="btn_insert_fileSelect">
				<input type="file" style="display: none" id="egovFileUpload" name="uploadFileList" multiple="multiple" />파일선택
			</span>
		</div>
		<div id="dndDiv">
			<div class="note_title" id="statudDnd2">파일 첨부</div>
			<div class="pop_file" id="dragandrophandler" onclick="document.getElementById('egovFileUpload').click(); ">
				<div id="file_info">Drop files here or click to upload.</div>
				<!-- 파일목록이 표시되는 공간 -->
			</div>
		</div>
		<!-- 파일 목록 표시 -->
		<!-- <div id="egovFileStatus" class="uploadOverFlow"></div> -->
		
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_insert_reqPopup">저장</div>
			<div class="button_normal exit_btn" id="btn_cancle_popup">취소</div>
		</div>
	</div>
</form>
</div>
<script type="text/javascript">
		//첨부파일 업로드 설정 (Select)
	   var maxFileNum = 30;
	   if(maxFileNum==null || maxFileNum==""){
	     maxFileNum = 3;
	    }     
	   var multi_selector = new MultiSelector( document.getElementById( 'egovFileStatus' ), maxFileNum );
	   multi_selector.addElement( document.getElementById( 'egovFileUpload' ) );	
	</script>
</html>
