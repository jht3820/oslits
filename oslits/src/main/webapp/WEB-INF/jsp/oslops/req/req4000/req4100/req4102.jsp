<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>

<html lang="ko">
<title>OpenSoftLab</title>

<style>
.button_normal { width: 39px; height: 22px; line-height: 22px; text-align: center; font-weight: bold; font-size: 1em; border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; margin: 0 auto; border: 1px solid #b8b8b8; cursor: pointer; }

.layer_popup_box .pop_list .span_margin { margin-right: 9px; }
</style>

<script>
	/*
	business function
	
	*/

	/**
	 * 	요구사항 하나 선택했을때 요구사항 디테일 정보 조회
	 */
	function fnSelectReq4102Info(selReqId){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4100/selectReq4102ReqInfoAjax.do'/>"}
				,{ "reqId" : selReqId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
        	//디테일폼 세팅
        	gfnSetData2ParentObj(data.reqInfoMap, "reqDetailForm");
        	
        	if(!gfnIsNull(data.fileList)){
	        	$.each(data.fileList, function(idx, fileVo){
	        		gfnFileListDiv(fileVo,'#fileListDiv');
	        	});
	        	if(data.fileListCnt >= 0){
	        		$('#atchFileId').val(data.fileList[0].atchFileId);
	        		$('#fileCnt').val(data.fileListCnt);
	        	}
        	}
        	
        	//개발주기가 존재하는 경우 개발주기 수정 불가능
/*         	if(!gfnIsNull(data.reqInfoMap.sprintId)){        		
        		$('select[name=sprintId]').parent().append(
        				'<input type="hidden" name="sprintId" id="sprintId" value="'+data.reqInfoMap.sprintId+'"/>'
        				+'<span class="w200" style="height: 28px;float: left;line-height: 28px;">'+data.reqInfoMap.sprintNm+'</span>');
        		$('select[name=sprintId]').remove();
        	} */
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
		Initialize
	
	*/
	
	$(document).ready(function() {
		//유효성 체크
		var arrChkObj = {"reqNm":{"type":"length","max":500}					
						,"reqDesc":{"type":"length","max":4000}};
		gfnInputValChk(arrChkObj);
		
		/* 	
		*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
		* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
		*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
		*	3. 공통코드 적용할 select 객체 직접 배열로 저장
		* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
		*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
		*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 
		*/
		var mstCdStrArr = "REQ00001|REQ00002";
		var strUseYn = 'Y';
		var arrObj = [$("#reqTypeCd"), $("#reqImprtCd")];
		var arrComboType = ["S", "S"];
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
		
		/*    
		 *	공통으로 콤보박스용 사용자 목록을 가져올때 사용하는 함수
		 *	현재 선택한 프로젝트에 권한롤에 배정되어 있는 사용자 정보만 가지고 온다. 
		 *  1. 사용구분(01: 사용중인 코드만, 02: 비사용중인 코드만, A: 전체)
		 *  2. 사용자 목록 적용할 select 객체 직접 배열로 저장
		 *  3. 사용자 목록 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
		 *	4. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
		 */
		var strUseCd = '01';
		var arrObj = [$("#reqChargerId")];
		var arrComboType = [ "S"];
		gfnGetUsrDataForm(strUseCd, arrObj, arrComboType , true);
		
		/*    
		 *	공통으로 콤보박스용 개발주기 목록을 가져올때 사용하는 함수
		 *	현재 선택한 프로젝트에 권한롤에 배정되어 있는 사용자 정보만 가지고 온다.
		 *	1. 개발주기상태(01: 대기, 02: 시작, 03: 종료, A: 전체)
		 *	2. 사용구분(01: 사용중인 코드만, 02: 비사용중인 코드만, A: 전체)
		 *  3. 사용자 목록 적용할 select 객체 직접 배열로 저장
		 *  4. 사용자 목록 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
		 *  5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
		 */
/* 		var statusCd = '01';
		var useCd = '01';
		var arrObj = [$("#sprintId")];
		var arrComboType = ["S"];
		gfnGetSprDataForm(statusCd, useCd, arrObj, arrComboType, true);		
		 */
		var reqId = '${param.reqId}';
		
		fnSelectReq4102Info(reqId);
		
		var fd = new FormData();
		
		
		/* 수정 */
		$('#btn_update_reqDetail').click(function() {
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "reqDetailForm";
			var strCheckObjArr = ["reqNm", "reqTypeCd", "reqImprtCd"];
			var sCheckObjNmArr = ["요구사항명", "요구사항타입", "중요도"];
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;	
			}
			
			
			//선택 파일, 추가 리스트 가져오기
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
			gfnFormDataAutoValue('reqDetailForm',fd);
			
			if(gfnIsNumeric("reqDevWkTm")){
				updateFileToServer();
			}
			//fnUpdateReqInfo();
		});
		function updateFileToServer()
		{
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req4000/req4100/updateReq4100ReqInfoAjax.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false}
					,fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
	        	//코멘트 등록 실패의 경우 리턴
	        	if(data.saveYN == 'N'){
	        		toast.push(data.message);
	        		return;
	        	}
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
						//그리드 새로고침
			        	myGrid.reloadList();
			        	gfnLayerPopupClose();
					}
				});

			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				toast.push("ERROR STATUS("+data.status+")<br>"+data.statusText);
			});
			//AJAX 전송
			ajaxObj.send();
		}
		/* 취소 */
		$('#btn_cancle_reqDetail').click(function() {
			gfnLayerPopupClose();
		});	
	});
</script>

<div class="popup">
	<form id="reqDetailForm" enctype="multipart/form-data">
	<input type="hidden" id="reqId" name="reqId" >
	<input type="hidden" id="sprintId" name="sprintId">
	<div class="pop_title">요구사항 상세 수정</div>
	<div class="pop_sub">	
		<div class="pop_left">요구사항 번호</div>
		<div class="pop_right">
			<input type="text" id="reqNo" name="reqNo" title="기능" class="input_txt" maxlength="250" readonly="readonly"/>
		</div>
		
		<div class="pop_left">요구기능</div>
		<div class="pop_right">
			<input type="text" id="reqNm" name="reqNm" title="기능" class="input_txt" maxlength="250" />
		</div>
		
		<div class="pop_left">타입</div>
		<div class="pop_right">
			<span class="pop_select_box">
				<select class="w200" id="reqTypeCd" name="reqTypeCd" >
				</select>
			</span>
		</div>
		
<!-- 		<div class="pop_left">개발주기</div>
		<div class="pop_right">
			<span class="pop_select_box">
				<select class="w200" id="sprintId" name="sprintId">
				</select>
			</span>
		</div>
		 -->
		<div class="pop_left">중요도</div>
		<div class="pop_right">
			<span class="pop_select_box">
				<select class="w200" id="reqImprtCd" name="reqImprtCd">
				</select>
			</span>
		</div>
		
		<div class="pop_left">담당자</div>
		<div class="pop_right">
			<span class="pop_select_box">
				<select class="w200" id="reqChargerId" name="reqChargerId">
				</select>
			</span>
		</div>
		
		<div class="pop_left">링크</div>
		<div class="pop_right">
			<input type="text" id="reqLink" name="reqLink" title="링크" class="input_txt" maxlength="500" />
		</div>
		
		<div class="pop_left bottom_line">개발공수</div>
		<div class="pop_right bottom_line">
			<input type="number"  min="0" id="reqDevWkTm" name="reqDevWkTm" title="개발공수" class="input_num" />
			<span>숫자입력</span>
		</div>
		
		<div class="pop_note">
			<div class="note_title">요구사항 설명</div>
			<textarea class="input_note" id="reqDesc" id="reqDesc" title="요구사항 설명" maxlength="2000" ></textarea>
		</div>
		
		<div class="pop_note">
			<div class="note_title">첨부파일 목록</div>
			<div id="fileListDiv" class="uploadOverFlow">
			</div>
		</div>
		<input type="hidden" id="atchFileId" name="atchFileId"/>
		<input type="hidden" id="fileCnt" name="fileCnt"/>
		<input type="hidden" id="insertFileCnt" name="insertFileCnt"/>
		<div class="selectFileDiv">
			<div class="note_title" style="float:left;width:100px;">파일 첨부 (Select)</div>
			<div class="up_box">
				<span class="button_normal2 del_btn" onclick="document.getElementById('egovFileUpload').click();" id="btn_insert_fileSelect">
					<input type="file" style="display: none" id="egovFileUpload" name="uploadFileList" />파일선택
				</span>
			</div>
		</div>
		<div id="egovFileStatus" class="uploadOverFlow"></div>
		
<!-- 		<div class="pop_list">
			<div class="list_line">
				<span class="span_margin"><img src="../../../../images/contents/sample.png" title="사용자 이미지" class="img_span">사용자</span>
				<span class="span_margin">Test_1 수정</span>
				<span class="span_margin">중요도 낮음에서 높음으로 변경</span>
				<span class="span_margin">2015.09.11</span>
				<span class="span_margin">13:40</span>
			</div>
			<div class="list_line">
				<span class="span_margin"><img src="../../../../images/contents/sample.png" title="사용자 이미지" class="img_span">사용자</span>
				<span class="span_margin">Test_1 수정</span>
				<span class="span_margin">중요도 낮음에서 높음으로 변경</span>
				<span class="span_margin">2015.09.11</span>
				<span class="span_margin">13:40</span>
			</div>
			<div class="list_line">
				<span class="span_margin"><img src="../../../../images/contents/sample.png" title="사용자 이미지" class="img_span">사용자</span>
				<span class="span_margin">Test_1 수정</span>
				<span class="span_margin">중요도 낮음에서 높음으로 변경</span>
				<span class="span_margin">2015.09.11</span>
				<span class="span_margin">13:40</span>
			</div>
		</div> -->
		
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_update_reqDetail" >수정</div>
			<div class="button_normal exit_btn" id="btn_cancle_reqDetail">취소</div>
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