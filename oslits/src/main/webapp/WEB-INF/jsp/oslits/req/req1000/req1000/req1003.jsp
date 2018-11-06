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
</style>
<script>
//중복 파일 업로드 방지 전역변수
var fileChk = new Array();

$(document).ready(function() {
	dndCancel('Y');
	/* 달력 세팅(시작일, 종료일) */
	gfnCalRangeSet("reqStDtm", "reqEdDtm", new Date('${reqInfoMap.reqStDtm}').format('yyyy-MM-dd'), new Date('${reqInfoMap.reqEdDtm}').format('yyyy-MM-dd'));
	$("#reqStDtm").val($.trim(new Date('${reqInfoMap.reqStDtm }').format("yyyy-MM-dd")));
	$("#reqEdDtm").val($.trim(new Date('${reqInfoMap.reqEdDtm }').format("yyyy-MM-dd")));
	
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
	*/
	var mstCdStrArr = "REQ00001|REQ00002";
	var strUseYn = 'Y';
	var arrObj = [$("#reqTypeCd"), $("#reqImprtCd")];
	var arrComboType = ["", "", ""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	//타입, 중요도 세팅
	$('#reqTypeCd').val("${reqInfoMap.reqTypeCd}");
	$('#reqImprtCd').val("${reqInfoMap.reqImprtCd}");
	
	//탭인덱스 부여
	//gfnSetFormAllObjTabIndex(document.getElementById("req1000PopupFrm"));
	
	/* 타이틀 변경 및 버튼명 변경, 수정일경우 값 세팅 */
	var reqId = '${param.reqId}';
		
	$('.pop_title').text('하위 요구사항 추가');
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancle_popup').click(function() {
		gfnLayerPopupClose();
	});
	
});
//요구사항 등록
function fnRequestInsert(continue_type){
	var reqStatusCd = "<c:out value='${reqInfoMap.reqStatusCd}'/>";
		
	// 승인된 데이터는 수정이 안됨.
	if( reqStatusCd == '02' ){
		jAlert("승인된 요구사항은 수정 할 수 없습니다.","알림창");
		return false;
	}
		
	/* 필수입력값 체크 공통 호출 */
	var strFormId = "req1000PopupFrm";
	var strCheckObjArr = ["reqNm", "reqTypeCd", "reqImprtCd"];
	var sCheckObjNmArr = ["요구사항명", "요구사항타입", "중요도"];
	if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
		return;	
	}
	if(!gfnIsNull($('#reqDevWkTm').val()) && $('#reqDevWkTm').val() > 99999999){
		toast.push("개발공수에 너무 많은 수를 입력하셨습니다.");
		return false;
	}
	//선택하기로 파일 추가한 리스트 가져오기
	var inFileList = $("input[name=uploadFileList]");
	
	//기본 1개 input, 1개이상 파일 추가된 경우 2부터 카운트
	if(inFileList.length > 1){
		//추가된 파일 루프 돌려서, formData에 값 넣기
		for(var i=1;i<inFileList.length;i++){
			if(inFileList[i].files[0] != null){
				fd.append('file', inFileList[i].files[0]);
			}
		}
	}
	//FormData에 input값 넣기
	gfnFormDataAutoValue(strFormId,fd);
	
	if(gfnIsNumeric("reqDevWkTm")){
		sendFileToServer(continue_type);
	}
}

	//input file의 id값 (필수)
	function sendFileToServer(continue_type)
	{	
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req1000/req1000/saveReq1001ReqInfoAjax.do'/>"
					,"contentType":false
					,"processData":false
					,"cache":false}
				,fd);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	//등록 실패의 경우 리턴
        	if(data.saveYN == 'N'){
        		toast.push(data.message);
        		return;
        	}
			//그리드 새로고침
			fnInGridListSet(firstGrid.page.currentPage,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
			
			jAlert(data.message, '알림창', function( result ) {
				if( result ){
					//일반 등록일경우에만 창 닫기
					if(continue_type == 'N'){
		        		gfnLayerPopupClose();
					}else{
					//연속등록인 경우
						//파일 목록 초기화
						dndCancel('Y');
					}
				}
			});
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			toast.push("ERROR STATUS("+data.status+")<br>"+data.statusText);
        	gfnLayerPopupClose();
		});
		//AJAX 전송
		ajaxObj.send();
	}
</script>

<div class="popup">
<form id="req1000PopupFrm" name="req1000PopupFrm" method="post">
	<input type="hidden" name="popupGb" id="popupGb" value="insert"/>
	<input type="hidden" name="reqId" id="reqId" value="${param.reqId}" />
	<input type="hidden" name="upperReqId" id="upperReqId" value="${reqInfoMap.reqId}"/>
	<input type="hidden" name="prjId" id="prjId" value="${sessionScope.selPrjId}"/>
	<input type="hidden" name="reqStatusCd" id="reqStatusCd" value="01"/>
	<input type="hidden" name="useCd" id="useCd" value="01"/>

	<div class="pop_title">신규 요구사항 요청</div>
	<div class="pop_sub">
	
		<div class="pop_left">요구사항 명 <span class="required_info">*</span></div>
		<div class="pop_right">
			<input type="text" title="요구사항 명" class="input_txt" name="reqNm" id="reqNm" placeholder="${reqInfoMap.reqNm}" />
		</div>

		<div class="pop_left">요구사항 번호</div>
		<div class="pop_right">
			<input type="text" title="요구사항 번호" class="input_txt" name="reqNo" id="reqNo" placeholder="${reqInfoMap.reqNo}" />
		</div>
		
		<div class="pop_left">작업 기간</div>
		<div class="pop_right">
			<span class="fl"><input type="text" id="reqStDtm" name="reqStDtm" class="calendar_input" readonly="readonly" title="개발 시작일" value="${reqInfoMap.reqStDtm}"/></span>
			<span class="calendar_bar fl">~</span>
			<span class="fl"><input type="text" id="reqEdDtm" name="reqEdDtm" class="calendar_input" readonly="readonly" title="개발 종료일" value="${reqInfoMap.reqEdDtm}"/></span>
		</div>
		
		<div class="pop_left">타입 <span class="required_info">*</span></div>
		<div class="pop_right">
			<span class="search_select">
				<span class="search_select">
					<select class="w200" name="reqTypeCd" id="reqTypeCd" placeholder="${reqInfoMap.reqTypeCd}"></select>
				</span>
			</span>
		</div>
		
		<div class="pop_left">중요도 <span class="required_info">*</span></div>
		<div class="pop_right">
			<span class="search_select">
				<span class="search_select">
					<select class="w200" name="reqImprtCd" id="reqImprtCd" placeholder="${reqInfoMap.reqImprtCd}"></select>
				</span>
			</span>
		</div>
		
		<div class="pop_left">링크정보</div>
		<div class="pop_right">
			<input type="text" title="링크정보" class="input_txt" name="reqLink" id="reqLink" placeholder="${reqInfoMap.reqLink}"/>
		</div>
		
		<div class="pop_left">개발공수</div>
		<div class="pop_right">
			<input type="number" min="1" title="개발공수" class="input_txt" name="reqDevWkTm" id="reqDevWkTm" placeholder="${reqInfoMap.reqDevWkTm}"/>
		</div>

		<div class="pop_note" style="margin-bottom:10px;">
			<div class="note_title">요구사항 설명</div>
			<textarea class="input_note" title="요구사항 설명" name="reqDesc" id="reqDesc" rows="7" placeholder="${reqInfoMap.reqDesc}"></textarea>
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
			<div class="button_normal save_btn" id="btn_insert_continue" onclick="fnRequestInsert('Y')">연속 등록</div>
			<div class="button_normal save_btn" id="btn_insert_reqDetail" onclick="fnRequestInsert('N')">등록</div>
			<div class="button_normal save_btn" onclick="document.getElementById('req1000PopupFrm').reset()">입력 초기화</div>
			<div class="button_normal exit_btn">취소</div>
			<div style="margin-top:5px;"><span>* 연속 등록시 첨부된 파일은 초기화 됩니다.</span></div>
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
