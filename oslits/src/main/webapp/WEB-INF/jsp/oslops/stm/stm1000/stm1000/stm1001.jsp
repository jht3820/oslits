<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<script src="<c:url value='/js/common/oslFile.js'/>"></script>
<script src="<c:url value='/js/common/comOslops.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<style>
.layer_popup_box .close_btn{top:12px; width:18px; height:18px; background:url(/images/login/x_white.png) no-repeat}
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
.required_info { color: red; }

/*익스플로러 적용 위해 !important 추가*/
/* 팝업에 따라 pop_menu_col1, pop_menu_col2 높이 변경 */
.pop_menu_row .pop_menu_col1 { width: 23% !important; height: 45px !important;}
.pop_menu_row .pop_menu_col2 { width: 77% !important; height: 45px !important; }
.pop_menu_row .menu_col1_subStyle { width: 46% !important; }
.pop_menu_row .menu_col2_subStyle { width: 54% !important; }

#btn_api_select { width: 52px; height: 100%; padding-top: 3px;}

#dragandrophandler{ border: 1px solid #ccc; }
.btn_div {margin-top: 20px !important; }
</style>
<script>
var fd = new FormData();
//중복 파일 업로드 방지 전역변수
var fileChk = new Array();
var fileAppendList = new Array();

// api 유효성
var arrChkObj = {	"apiNm":{"type":"length","msg":"서비스명은 500byte까지 입력이 가능합니다.",max:500},
				     "apiTxt":{"type":"length","msg":"API 설명은 1000byte까지 입력이 가능합니다.",max:1000}
				};

//파일 업로드 제한 사이즈
var FILE_INFO_MAX_SIZE = "${fileInfoMaxSize}";
var FILE_SUM_MAX_SIZE = "${fileSumMaxSize}";

globals_guideChkFn = fnStm1001GuideShow;

$(document).ready(function() {
	var languages = gfnGetUrlList();

	$( "#apiUrl" ).autocomplete({
	   		source: languages
	});
	
	$("#apiNm").focus();
	
	

	// 유효성 체크	
	gfnInputValChk(arrChkObj);
	
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
	//gfnSetFormAllObjTabIndex(document.getElementById("api1000PopupFrm"));
	
	var serverTime=gfnGetServerTime('yyyy-mm-dd');

	$('#regDtm').val(serverTime);
	
	/* 타이틀 변경 및 버튼명 변경, 수정일경우 값 세팅 */
	if('${param.popupGb}' == 'insert'){
		$(".pop_title").text("API 등록");
		$("#btn_save_popup").text('등록');
	}
	else if('${param.popupGb}' == 'update'){
		$(".pop_title").text("API 수정");
		$("#btn_save_popup").text('수정');
		
		var apiId = '${param.apiId}';
		fnSelectApi1001Info(apiId);
	}
	
	/**
	 * 	요구사항 하나 선택했을때 요구사항 디테일 정보 조회
	 */
 	function fnSelectApi1001Info(apiId){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm1000/stm1000/selectStm1001InfoAjax.do'/>"}
				,{ "apiId" : apiId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
        	//디테일폼 세팅
        	gfnSetData2ParentObj(data.apiInfoMap, "api1000PopupFrm");
        	
        	
        	$('#regDtm').val(data.apiInfoMap.regDtm);
        	
        	if(data.fileList.length > 0){
        		//info 정보 show
        		$('#dndCancel').show();
        		$("#file_info").hide();
        	}
        	$.each(data.fileList, function(idx, fileVo){
        		fileVo["apiId"] = '${param.apiId}';
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
	$('#btn_save_popup').click(function() {
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "api1000PopupFrm";
		var strCheckObjArr = ["apiNm", "apiUrl"];
		var sCheckObjNmArr = ["서비스 명", "서비스 주소(URL)" ];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		
		var formObj = document.getElementById("api1000PopupFrm");
		
		// 등록/수정 전 유효성 체크
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}	
		
		fnInsertReqInfoAjax("api1000PopupFrm");
	});
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancle_popup').click(function() {
		
		gfnLayerPopupClose();
	});
	
	
	$('#btn_api_select').click(function() {
		var data = {};
		gfnLayerPopupOpen('/stm/stm1000/stm1000/selectStm1002View.do',data, "560", "453",'scroll');
	});
	
	
	// TODO
	$('#apiUrl').change(function() {
		
		console.log(11);
	});

});

//요구사항 등록 함수
function fnInsertReqInfoAjax(formId){
	var fileListStr = fnFileUploadStrData();
	
	jConfirm("저장하시겠습니까?"+fileListStr, "알림", function( result ) {
		if( result ){
			//실제 파일 FormData에 적재
			fnFileUploadAppendData();
			
			//FormData에 input값 넣기
			gfnFormDataAutoValue('api1000PopupFrm',fd);
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/stm/stm1000/stm1000/saveStm1001InfoAjax.do'/>"
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
	});
}

function fnStm1001GuideShow(){
	var mainObj = $(".popup");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["stm1001"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}

</script>

<div class="popup" id="aaa">
<form id="api1000PopupFrm" name="api1000PopupFrm" method="post">
	<input type="hidden" name="popupGb" id="popupGb" value="${param.popupGb}"/>
	<input type="hidden" name="apiId" id="apiId" value="${param.apiId}" />
	<input type="hidden" name="prjId" id="prjId" value="${sessionScope.selPrjId}"/>
	<input type="hidden" name="reqStatusCd" id="reqStatusCd" value="01"/>

	<div class="pop_title">API 등록</div>
	<div class="pop_sub">

		<div class="pop_menu_row pop_menu_oneRow first_menu_row">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="apiNm">서비스명</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<input type="text" title="서비스 명" class="input_txt" name="apiNm" id="apiNm" value="" maxlength="500" />
			</div>
		</div>
		
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="apiUrl" style="height: 100%; margin-bottom: 0px;">서비스 주소(URL)</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2" guide="apiUrl"  >
				<input type="text" title="링크정보" class="input_txt" name="apiUrl" style="width:88%;" id="apiUrl" value="" maxlength="500" readonly  />
				<span class="button_normal2 fl" id="btn_api_select"><i class="fa fa-search" styel="font-size: 15px;"></i></span>	
			</div>
		</div>
		
		<div class="pop_menu_row">
			<div class="pop_menu_col1 menu_col1_subStyle"><label for="regUsrId">등록자</label></div>
			<div class="pop_menu_col2 menu_col2_subStyle"><input type="text" title="등록자" class="input_txt" name="regUsrId" id="regUsrId" readonly value="${regUsrId}"  /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 menu_col1_subStyle pop_menu_col1_right"><label for="regDtm">등록일</label></div>
			<div class="pop_menu_col2 menu_col2_subStyle"><input type="text" title="등록일" class="input_txt" name="regDtm" id="regDtm" readonly value=""  /></div>
		</div>

		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="useCd">사용여부</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2 pop_oneRow_col2">
				<span class="search_select">
					<select class="select_useCd" name="useCd" id="useCd" value="" style="height:100%; width:34%;"></select>
				</span>
			</div>
		</div>
		
		<div class="pop_note" style="margin-bottom:10px;">
			<div class="note_title">API 설명</div>
			<textarea class="input_note" title="API 설명" name="apiTxt" id="apiTxt" rows="7" value="" maxlength="1000"  ></textarea>
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
			<div class="button_normal save_btn" id="btn_save_popup"></div>
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
