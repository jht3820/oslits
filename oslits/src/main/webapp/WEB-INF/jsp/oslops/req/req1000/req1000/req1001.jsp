
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
#reqDesc{ height: 200px;}
/* #reqNm { width: 100%; } */
#btn_insert_reqContinuePopup{display:none;}
/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
/* 파일업로드 Div */
#dragandrophandler { border: 1px solid #ccc !important; }
.btn_div{margin-top: 22px !important;}

/* 화면 레이아웃 */
.pop_sub select {
    width: 100%;
    min-width: 150px;
    height: 100%;
    font-size: 10pt !important;
}
#reqInsertDiv{position:relative;}
.pop_service_div {
	display:none;
    position: absolute;
    width: 400px;
    left: 640px;
    height: 100%;
    background-color: #fff;
}
.pop_service_title{    width: 100%;
    height: 40px;
    padding: 0px 15px;
    background: #4b73eb;
    color: #fff;
    font-size: 12pt;
    line-height: 40px;
    font-weight: bold;}
.pop_service_content {
    border-left: 1px solid #ccc;
    width: 100%;
    display: inline-block;
    height: 740px;
    float: left;
    padding: 20px 10px;
    overflow-y: auto;
}
.serviceDiv {
    width: 100%;
    float: left;
    display: inline-block;
    border: 1px solid #ccc;
    border-radius: 7px;
    font-size: 10pt;
    box-shadow: 0 2px 3px 2px rgba(0,0,0,.03);
    margin-bottom: 8px;
    padding: 10px;
}
.serviceDiv_date {
    text-align: right;
    font-size: 10pt;
    color: #666;
    height: 20px;
    line-height: 20px;
    float:right;
}
.serviceDiv_topInfo {
    display: inline-block;
    width: 100%;
    float:left;
    margin-bottom: 2px;
    padding: 0 5px;
}
.serviceDivInfo {
    float: left;
    display: inline-block;
    height: 20px;
    line-height: 20px;
    text-align: center;
}
.pop_service_cntTitle {
    height: 20px;
    text-align: right;
    font-size: 10pt;
    line-height: 20px;
    background-color: #f9f9f9;
    border: 1px solid #ccc;
    border-top: none;
    border-right: none;
    padding-right: 5px;
}
.serviceDivInfo.serviceDivInfo_proType {
    font-weight: bold;
    margin-right: 5px;
}
.serviceDiv_date > small {
    font-size: 8pt;
}
.serviceDiv:hover{border:1px solid #4b73eb;cursor:pointer;}
.serviceDiv_reqNm {
    display: inline-block;
    float: left;
    width: 100%;
    height: 40px;
    border: 1px solid #ccc;
    line-height: 20px;
    padding: 0 10px;
    background-color: #f9f9f9;
}
.serviceDiv_reqDesc {
    height: 80px;
    width: 100%;
    display: inline-block;
    float: left;
    border: 1px solid #ccc;
    border-top: none;
    padding: 5px 10px;
    overflow-y: auto;
}
.serviceDiv_reqNm span ,.serviceDiv_reqDesc span {
    color: #4b73eb;
    font-weight: bold;
}
</style>
<script>
var url = "";
var fd = new FormData();
//중복 파일 업로드 방지 전역변수
var fileChk = new Array();
var fileAppendList = new Array();

//팝업 화면 유형 구분값 - 등록/수정
var pageType = "${pageType}";

var autoData = [];

//연속 등록 체크
var continueInsert = false;


//파일 업로드 제한 사이즈
var FILE_INFO_MAX_SIZE = "${fileInfoMaxSize}";
var FILE_SUM_MAX_SIZE = "${fileSumMaxSize}";

// 기본 유효성 체크
var arrChkObj = {"reqNm":{"type":"length","msg":"요청제목 500byte까지 입력이 가능합니다.","max":500}
				,"reqUsrEmail":{"type":"length","msg":"이메일은 100byte까지 입력이 가능합니다.", "max":100}
				,"reqUsrNum":{"type":"number"}
				,"reqDesc":{"type":"length","msg":"요청내용은 4000byte까지 입력가능합니다.", "max":4000}
				};

// 연락처, 이메일  유효성 체크
var saveObjectValid = {
			"reqUsrNum":{"type":"regExp","pattern":/^([0-9]{3,13}).*$/ ,"msg":"연락처 형식이 아닙니다. (3~13자리) (예) 01012341234", "required":true}
			 ,"reqUsrEmail":{"type":"regExp","pattern":/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i ,"msg":"이메일 형식이 아닙니다.<br>(예) mymail@naver.com","required":true}
		};

function fnReqClick(thisObj){
	var prjId = $(thisObj).attr("prjid");
	var reqId = $(thisObj).attr("reqid");
	var reqProType = $(thisObj).attr("reqprotype");
	
	if(reqProType == "01" || reqProType == "03"){
		// 접수요청(reqProType = 01), 반려(reqProType = 03)에 따라 팝업 높이 변경
		var popHeight = "925";
		if(reqProType == "03"){
			popHeight = "900";
		}
		
		var data = {
             			"mode": "req",
             			"popupPrjId":prjId,
             			"reqId":reqId,
             			"reqProType": reqProType,
             			"reqPageType" : "usrReqPage"
             	}; 
       gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', popHeight,'scroll');
	}
	if(reqProType == "02" || reqProType == "04" || reqProType == "05"){
		var data = {"mode":"newReq","popupPrjId":prjId,"reqId": reqId, "reqProType":"02"}; 
		gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
	}
}
$(document).ready(function() {
	
	//프로젝트 세팅
	$("select#viewPrjId").html($("#header_select").html());
	
	//유효성 체크
	gfnInputValChk(arrChkObj);
	
	//탭인덱스 부여
	//gfnSetFormAllObjTabIndex(document.getElementById("req1000PopupFrm"));
	
	// 연속등록 시 요청내용의 placeholder 부분에 <br>태그 들어가는 부분 제거
	var ph_reqDesc = '${ph_reqInfoMap.reqDesc}';
	if(!gfnIsNull(ph_reqDesc)){
		$( '#reqDesc' ).prop( 'placeholder', ph_reqDesc.replace(/(<\/br>|<br>|<br\/>|<br \/>)/gi, '\r\n') );
	}
	
	/* 타이틀 변경 및 버튼명 변경, 수정일경우 값 세팅 */
	if(pageType == 'insert'){
		
		url = "<c:url value='/req/req1000/req1000/insertReq1001ReqInfoAjax.do'/>";
    	// 등록이면 접수유형 시스템으로 체크
    	$("#reqNewType_stm").prop("checked", true);
		$(".pop_title").text("요청사항 생성");
		$("#btn_insert_reqPopup").text('등록');
		
		//연속등록 버튼 보이기
		$("#btn_insert_reqContinuePopup").css({"display":"inline-block"});
	}
	else if(pageType == 'update'){
		
		url = "<c:url value='/req/req1000/req1000/updateReq1001ReqInfoAjax.do'/>";
		$(".pop_title").text("요청사항 수정");
		$("#btn_insert_reqPopup").text('수정');
		
		var prjId = '${param.popupPrjId}';
		var reqId = '${param.reqId}';
		fnSelectReq1001Info(reqId,prjId);
		
		//프로젝트 고정
		$("select#viewPrjId").attr("disabled","disabled");
	}
	
	// 요청일 현재 날짜로 기본 세팅
	$("#reqDtm").val( gfnGetServerTime('YYYY-MM-DD') );
	
	
	/**
	 * 	요구사항 하나 선택했을때 요구사항 디테일 정보 조회
	 *	@param selReqId 수정하려는 요구사항
	 */
 	function fnSelectReq1001Info(selReqId,selPrjId){
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req1000/req1000/selectReq1000ReqInfoAjax.do'/>"}
				,{"prjId":selPrjId, "reqId" : selReqId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
        	//디테일폼 세팅
        	gfnSetData2ParentObj(data.reqInfoMap, "req1000PopupFrm");
 	
        	//프로젝트
        	$("select#viewPrjId").val($("#prjId").val());
        	
        	// 처리유형
        	var reqProType = data.reqInfoMap.reqProType;
        	// hidden에 처리 유형 세팅
        	$("#reqProType").val(reqProType);
        	
        	// 접수유형
        	var reqNewType = data.reqInfoMap.reqNewType;
        	
        	// hidden에 접수 유형 세팅
        	$("#reqNewType").val(reqNewType);

        	
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
			data = ㅣㅐㅏJSON.parse(data);
			jAlert(data.message, "알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
	} 

	
	/* 저장버튼 클릭 시 */
	$('#btn_insert_reqPopup').click(function() {

		/* 필수입력값 체크 공통 호출 */
		var strFormId = "req1000PopupFrm";
		var strCheckObjArr = ["reqNm", "reqUsrEmail", "reqUsrNum", "reqDesc"];
		var sCheckObjNmArr = ["요청제목", "이메일", "연락처", "요청 내용"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
				
		// 이메일 공백제거
		var reqUsrEmali = $("#reqUsrEmail").val();
		$("#reqUsrEmail").val(reqUsrEmali.trim());
		
		// 연락처, 이메일 유효성 검사
		if(!gfnInputValChk(saveObjectValid)){
			return false;	
		}
		
		//FormData에 input값 Json형태로 넣기
		gfnFormDataAutoJsonValue(strFormId,fd);
		
		
		// 기본 유효성 검사
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}
		
		// 연락처, 이메일 유효성 검사
		if(!gfnSaveInputValChk(saveObjectValid)){
			return false;	
		}
		
		fnInsertReqInfoAjax(strFormId);
	});
	
	/* 연속 등록버튼 클릭 시 */
	$('#btn_insert_reqContinuePopup').click(function() {
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "req1000PopupFrm";
		var strCheckObjArr = ["reqNm", "reqUsrEmail", "reqUsrNum", "reqDesc"];
		var sCheckObjNmArr = ["요청제목", "이메일", "연락처", "요청 내용"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		
		// 연락처, 이메일  유효성 체크
/* 		var saveObjectValid = {
					"reqUsrNum":{"type":"regExp","pattern":/^([0-9]{3,13}).*$/ ,"msg":"연락처 형식이 아닙니다. (3~13자리) (예) 01012341234", "required":true}
					 ,"reqUsrEmail":{"type":"regExp","pattern":/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i ,"msg":"이메일 형식이 아닙니다. <br>(예) mymail@naver.com","required":true}
		} */
		
		// 연락처, 이메일 유효성 검사
		if(!gfnInputValChk(saveObjectValid)){
			return false;	
		}
		
		//FormData에 input값 Json형태로 넣기
		gfnFormDataAutoJsonValue(strFormId,fd);
		
		continueInsert = true;

		// 기본 유효성 검사
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}
		
		// 연락처, 이메일 유효성 검사
		if(!gfnSaveInputValChk(saveObjectValid)){
			return false;	
		}
		
		// 연속등록 전 유효성 체크
		fnInsertReqInfoAjax(strFormId);


	});
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancle_popup').click(function() {
		gfnLayerPopupClose();
	});
	
	$('#btn_cls_select').click(function() {
		
		gfnCommonClsPopup(function(reqClsId,reqClsNm){
			$("#reqClsId").val(reqClsId);
			$("#reqClsNm").val(reqClsNm);
		});
		
	});
	
	
	$('#btn_delete_file').click(function(e) {
		e.stopPropagation();
	});
	
	
});

//요구사항 등록 함수
function fnInsertReqInfoAjax(formId){
	//FormData에 input값 넣기
	//gfnFormDataAutoValue('req1000PopupFrm',fd);
	
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
		    	jAlert(data.message, '알림창');
		    	gfnLayerPopupClose();
		    	
		    	//그리드 새로고침
		    	if(pageType == "insert"){
		    		//fnInGridListSet(firstGrid.page.currentPage,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
    				fnInGridListSet(0,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
		    		//연속 등록 아닌경우
		    		if(continueInsert){ //연속 등록인경우
		    			var reqInfo = data.reqInfo;
		    			var data = {
							"type": "insert",
							"continueInsert":"Y",
							"ph_prjId":reqInfo.prjId,
							"ph_reqId":reqInfo.reqId
						};
						
						gfnLayerPopupOpen('/req/req1000/req1000/selectReq1001View.do',data,"640","800",'scroll');
		    		}
		    	}else if(pageType == "update"){
		    		// 수정일 경우 그리드 현재 페이지 번호, 검색어 정보를 전달하여 새로고침
		    		fnInGridListSet(firstGrid.page.currentPage,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
		    	}
		    	
				
				
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


//프로젝트 변경
function fnPrjChg(thisObj){
	$("#prjId").val(thisObj.value);
}

</script>
<div class="popup" id="reqInsertDiv">
<form id="req1000PopupFrm" name="req1000PopupFrm" method="post">
	<input type="hidden" name="popupType" id="popupType" value="${pageType}"/>
	<input type="hidden" name="reqId" id="reqId" value="${param.reqId}" />
	<input type="hidden" name="prjId" id="prjId" value="${sessionScope.selPrjId}"/>
	<input type="hidden" name="reqClsId" id="reqClsId" value=""/>
	<input type="hidden" name="reqProType" id="reqProType" />
	<input type="hidden" name="reqKey" id="reqKey" />
	<!-- 접수유형은 01 (시스템) : 등록시 기본 접수유형 -->
	<input type="hidden" name="reqNewType" id="reqNewType" value="01"/>

	<div class="pop_title">	
	</div>

	<div class="pop_sub">
		<!-- <input id="hidden" type="radio" modifyset="false" value="01"/> -->
		<div class="pop_menu_row pop_menu_oneRow"style="border-top: 1px solid #ddd;">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="reqNewType">접수유형</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<div class="pop_radio">
					<input id="reqNewType_stm" type="radio" modifyset="false" value="01"/><label>시스템</label>
					<input id="reqNewType_tel" type="radio" modifyset="false" value="02"/><label>유선</label>
					<input id="reqNewType_pub" type="radio" modifyset="false" value="03"/><label>공문</label>
					<input id="reqNewType_self" type="radio" modifyset="false" value="04"/><label>자체식별</label>
					<input id="reqNewType_brd" type="radio" modifyset="false" value="05"/><label>게시판</label>
				</div>
			</div>
		</div>
		<div class="pop_menu_row" style="border-top: 1px solid #ddd;">
			<div class="pop_menu_col1"><label for="prjNm">대상체계</label></div>
			<div class="pop_menu_col2">
				<select id="viewPrjId" name="viewPrjId" modifyset="false" onchange="fnPrjChg(this)"></select>
			</div>
		</div>
		<div class="pop_menu_row" style="border-top: 1px solid #ddd;">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqNo">공문번호</label></div>
			<div class="pop_menu_col2"><input id="reqNo" type="text" name="reqNo" title="공문번호"  class="readonly" readonly="readonly" placeholder="${ph_reqInfoMap.reqNo}"/></div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="reqNm">요청제목</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2"><input id="reqNm" type="text" name="reqNm" title="요청제목"  maxlength="300" placeholder="${ph_reqInfoMap.reqNm}"/></div>
		</div>

		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrNm">요청자 성명</label></div>
			<div class="pop_menu_col2"><input id="reqUsrNm" type="text" name="reqUsrNm" title="요청자" class="readonly" readonly="readonly" value="${reqUsrInfoMap.usrNm}" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqDtm">요청일</label></div>
			<div class="pop_menu_col2"><input id="reqDtm" type="text" name="reqDtm" title="요청일" class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrDeptNm">요청부서</label></div>
			<div class="pop_menu_col2"><input id="reqUsrDeptNm" type="text" name="reqUsrDeptNm" title="소속" class="readonly" readonly="readonly" value="${reqUsrInfoMap.deptNm}" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqUsrEmail">E-mail</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2"><input id="reqUsrEmail" type="text" name="reqUsrEmail" title="E-mail" value="${reqUsrInfoMap.email}"/></div>
		</div>
		<div class="pop_menu_row ">
			<div class="pop_menu_col1"><label for="reqUsrNum">연락처</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2"><input id="reqUsrNum" type="text" name="reqUsrNum" title="연락처" maxlength="13" max="11199999999" value="${reqUsrInfoMap.telno}" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqUsrPositionNm">직급</label></div>
			<div class="pop_menu_col2">
				<input id="reqUsrPositionNm" type="text" name="reqUsrPositionNm" title="직급" class="readonly" readonly="readonly" value="${reqUsrInfoMap.usrPositionNm}" />
			</div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"></div>
			<div class="pop_menu_col2"></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqUsrDutyNm">직책</label></div>
			<div class="pop_menu_col2">
				<input id="reqUsrDutyNm" type="text" name="reqUsrDutyNm" title="직급" class="readonly" readonly="readonly" value="${reqUsrInfoMap.usrDutyNm}" />
			</div>
		</div>
		<div class="pop_note" style="margin-bottom:10px;">
			<div class="note_title">요청 내용<span class="required_info">&nbsp;*</span></div>
			<textarea class="input_note" title="요청 내용" name="reqDesc" id="reqDesc" rows="7" value="${req1000ReqInfo.reqDesc}"  ></textarea> <!-- placeholder="${ph_reqInfoMap.reqDesc}" -->
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
			<div class="button_normal save_btn" id="btn_insert_reqContinuePopup">연속 등록</div>
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
