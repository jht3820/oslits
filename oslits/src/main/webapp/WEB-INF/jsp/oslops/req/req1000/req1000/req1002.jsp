<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<script src="<c:url value='/js/common/oslFile.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<style>

/* 팝업 상단 위치*/ 
.ui-draggable{top: -5px;}

.layer_popup_box .close_btn{top:12px; width:18px; height:18px; background:url(/images/login/x_white.png) no-repeat}
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
.required_info { color: red; }
.pop_menu_row .pop_menu_col2 .pop_radio{ line-height: 17px; padding-top: 5px; }  

/*파일 삭제버튼 안보이도록 */
.file_main_box .file_delete{ display: none; }   

#menu_col2_input{ padding-right: 9px; padding-left: 0; }
#reqState{ line-height: 26px; }   
#reqNm, #reqState { width: 100%; }
#reqDesc{ height: 130px; }
/* .reqUsrNum_input { width:100%; } */
/* 파일업로드 Div */
#dragandrophandler { min-height: 130px; height: 130px; border: 1px solid #ccc; }

</style>
<script>
//프로젝트 Id
var prjId = "${prjId}";

//제거할 이벤트 목록 배열
var removeEventArr = ["dragenter", "dragover", "drop"];

$(document).ready(function() {
	
	var reqId = '${param.reqId}';
	fnSelectReq1001Info(reqId);
	
	/*
	 * 요구사항 상세보기(req1002.jsp)에서 항목명 구분을 위해 사용
	 * usrReqPage - 요구사항 요청(사용자) 
	 * admReqPage - 전체 요구사항 목록, 요구사항 생성관리(관리자)
	 */
	var reqPageType = '${param.reqPageType}';
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancle_popup').click(function() {
		gfnLayerPopupClose();
	});
	
	// 전체요구사항 목록, 요구사항 생성관리에서 상세보기(req1002.jsp)
	// 호출할 경우 항목 명 변경
	if(reqPageType == "admReqPage"){
		$(".pop_title").text("요구사항 상세보기");		
		$("label[for=reqNm]").text("요구사항 명");
		$("#reqDesc_div_txt").text("요구사항 설명");
	}
	
	// 상세정보 화면이므로 파일 drag&drop 이벤트 제거
	$.each(removeEventArr,function(idx, map){
		$("#dragandrophandler").off(map);
	});
});


	/**
	 * 	요구사항 하나 선택했을때 요구사항 디테일 정보 조회
	 *	@param selReqId 수정하려는 요구사항
	 */
	function fnSelectReq1001Info(selReqId){
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req1000/req1000/selectReq1000ReqInfoAjax.do'/>"}
			,{"prjId": prjId, "reqId" : selReqId });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
		var reqInfoMap = data.reqInfoMap;
		
    	//디테일폼 세팅
    	gfnSetData2ParentObj(reqInfoMap, "req1002PopupFrm");

    	// 처리유형이 반려일 경우 반려내용 보여줌 
    	fnRestorationChk(reqInfoMap.reqProType);
    	
    	// 접수유형
    	var reqNewType = reqInfoMap.reqNewType;
    	
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
    		gfnFileListReadDiv(fileVo,'.pop_file_detail','req');
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

	/* 
	 *	처리유형이 반려일 경우 반려내용을 보여준다.
	 *	@param reqProType 처리유형 코드
	 */
	function fnRestorationChk(reqProType){

		if(reqProType == "03"){
			$("#reqDesc").css("height", "100px"); 
			$("#reqDesc_div").css("margin-top", "20px"); 
			$("#reqDesc_div").css("margin-bottom", "10px"); 
			$("#reqAccept_div").css("display", "block"); 
			$("#reqAccept_div").css("margin-top", "5px"); 
			$("#reqAcceptTxt").css("height", "100px"); 
			$("#dragandrophandler").css("min-height", "100px"); 
			$("#dragandrophandler").css("height", "100px"); 
		}
	}



</script>

<div class="popup">
<form id="req1002PopupFrm" name="req1002PopupFrm" method="post">

	<div class="pop_title">요청사항 상세보기</div>

	<div class="pop_sub">

		<div class="pop_menu_row pop_menu_oneRow" style="border-top: 1px solid #ddd;"> 
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="reqNewType">접수유형</label></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<div class="pop_radio">
				<input id="reqNewType_stm" type="radio" name="reqNewType_stm" value="01"/><label>시스템</label>
				<input id="reqNewType_tel" type="radio" name="reqNewType_tel" value="02"/><label>유선</label>
				<input id="reqNewType_pub" type="radio" name="reqNewType_pub" value="03"/><label>공문</label>
				<input id="reqNewType_self" type="radio" name="reqNewType_self" value="04"/><label>자체식별</label>
				<input id="reqNewType_brd" type="radio" name="reqNewType_brd" value="05"/><label>게시판</label>
			</div>
			</div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="reqProTypeNm">진행상태</label></div>
			<div class="pop_menu_col2 pop_oneRow_col2"><input id="reqProTypeNm" type="text" name="reqProTypeNm" class="readonly" readonly="readonly" /></div>
		</div>
		
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="prjNm">프로젝트명</label></div>
			<div class="pop_menu_col2"><input id="prjNm" type="text" name="prjNm" class="readonly" readonly="readonly" value="${requestScope.prjName}" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqNo">공문번호</label></div>
			<div class="pop_menu_col2"><input id="reqNo" type="text" name="reqNo" class="readonly" readonly="readonly" /></div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label id="" for="reqNm">요청제목</label></div>
			<div class="pop_menu_col2 pop_oneRow_col2"><input id="reqNm" type="text" name="reqNm" class="readonly" readonly="readonly" /></div>
		</div>

		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrNm">요청자</label></div>
			<div class="pop_menu_col2"><input id="reqUsrNm" type="text" name="reqUsrNm" class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqDtm">요청일</label></div>
			<div class="pop_menu_col2"><input id="reqDtm" type="text" name="reqDtm" class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrDeptNm">소속</label></div>
			<div class="pop_menu_col2"><input id="reqUsrDeptNm" type="text" name="reqUsrDeptNm" class="readonly" readonly="readonly"  /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqUsrEmail">E-mail</label></div>
			<div class="pop_menu_col2"><input id="reqUsrEmail" type="text" name="reqUsrEmail" class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="reqUsrNum">연락처</label></div>
			<div class="pop_menu_col2"><input id="reqUsrNum" type="text" name="reqUsrNum" maxlength="11" class="readonly" readonly="readonly" style="width:100%;" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqUsrPositionCd">직급</label></div>
			<div class="pop_menu_col2">
				<input type="hidden" name="reqUsrPositionCd" id="reqUsrPositionCd" value="" />
				<input id="reqUsrPositionNm" type="text" name="reqUsrPositionNm" title="직급" class="readonly" readonly="readonly" />
			</div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for=regDtm>등록일</label></div>
			<div class="pop_menu_col2"><input id="regDtmDay" type="text" name="regDtmDay"  class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqUsrEmail">직책</label></div>
			<div class="pop_menu_col2">
				<input type="hidden" name="reqUsrDutyCd" id="reqUsrDutyCd" value="" />
				<input id="reqUsrDutyNm" type="text" name="reqUsrDutyNm" title="직책" class="readonly" readonly="readonly" />
			</div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for=reqStDtm>작업 시작일</label></div>
			<div class="pop_menu_col2"><input id="reqStDtm" type="text" name="reqStDtm"  class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for=reqStDuDtm>시작 예정일</label></div>
			<div class="pop_menu_col2"><input id="reqStDuDtm" type="text" name="reqStDuDtm"  class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for=reqEdDtm>작업 종료일</label></div>
			<div class="pop_menu_col2"><input id="reqEdDtm" type="text" name="reqEdDtm"  class="readonly" readonly="readonly" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for=reqEdDuDtm>종료 예정일</label></div>
			<div class="pop_menu_col2"><input id="reqEdDuDtm" type="text" name="reqEdDuDtm"  class="readonly" readonly="readonly" /></div>
		</div>
		
		<div class="pop_note" id="reqDesc_div" style="margin-top: 27px; margin-bottom:20px;">
			<div class="note_title" id="reqDesc_div_txt">요청 내용</div>
			<textarea class="input_note" title="요청 내용" name="reqDesc" id="reqDesc" rows="7" disabled></textarea>
		</div>
		
		<div class="pop_note" id="reqAccept_div" style="margin-bottom:10px; display:none">
			<div class="note_title">반려 내용</div>
			<textarea class="input_note" title="반려 내용" name="reqAcceptTxt" id="reqAcceptTxt" rows="7" disabled></textarea>
		</div>
			
		<!-- 파일 목록 표시 -->
		<div class="up_box">
			<!-- <span id="dndCancel" class="abort" style="display: none;" onclick='dndCancel(); '>파일목록 초기화</span> -->
			<span class="button_normal2 del_btn" onclick="document.getElementById('egovFileUpload').click(); " id="btn_insert_fileSelect" style="display: none">
				<input type="file" style="display: none" id="egovFileUpload" name="uploadFileList" multiple="multiple" />파일선택
			</span>
		</div>
		<div id="dndDiv">
			<div class="note_title" id="statudDnd2">첨부 파일</div>
			<div class="pop_file_detail" id="dragandrophandler"  ondragstart="return false;" ondrop="return false;"> <!--   onclick="document.getElementById('egovFileUpload').click(); " -->
				<!-- <div id="file_info">Drop files here or click to upload.</div> -->
				<!-- 파일목록이 표시되는 공간 -->
			</div>
		</div>
		<!-- 파일 목록 표시 -->
		<!-- <div id="egovFileStatus" class="uploadOverFlow"></div> -->
		
		<div class="btn_div">
			<div class="button_normal exit_btn" id="btn_cancle_popup">닫기</div>
		</div>
	</div>
</form>
</div>
<script type="text/javascript">
		//첨부파일 업로드 설정 (Select)
	/*  var maxFileNum = 30;
	   if(maxFileNum==null || maxFileNum==""){
	     maxFileNum = 3;
	    }     
	   var multi_selector = new MultiSelector( document.getElementById( 'egovFileStatus' ), maxFileNum );
	   multi_selector.addElement( document.getElementById( 'egovFileUpload' ) ); */
	</script>
</html>
