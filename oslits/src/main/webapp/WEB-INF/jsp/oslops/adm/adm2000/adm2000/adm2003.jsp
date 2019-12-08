<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value='/js/common/oslFile.js'/>" ></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<html lang="ko">
<head>
<title>OpenSoftLab</title>

<style>
	   .pop_sub{ height: auto !important;}
</style>
<script>
// 유효성
var arrChkObj = {"blkLog":{"type":"length","msg":"차단사유는 1000byte까지 입력가능합니다.", "max":1000}
};

	$(document).ready(function() {
		
		gfnInputValChk(arrChkObj);
	});

	/**
	 * 	사용자 차단유무 수정
	 *	@param updateData 수정할 사용자 데이터
	 */
	function fnUpdateAdm2000BlockInfo(){
	
		var strFormId = "usrInfo";
		
		var strCheckObjArr = ["blkLog"];
		 
		var sCheckObjNmArr = ["차단사유"];
		
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		
		// 차단사유 유효성 체크
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}
	

			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm2000/adm2000/updateAdm2000Block.do'/>"}
					,{  "usrId" : '${param.usrId}', "block" : '${param.block}'
						, "pwFailCnt": '${param.pwFailCnt}' ,"blkLog" : $('#blkLog').val()
						
					});
		
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				
		    	//수정 실패의 경우 리턴
		    	if(data.saveYN == 'N'){
		    		toast.push(data.message);
		    		return;
		    	}
				// 차단사유 등록 후 그리드 데이터 새로고침
				fnInGridListSet(firstGrid.page.currentPage, $('form#searchFrm').serialize()+"&"+mySearch.getParam());
				
				toast.push(data.message);
				gfnLayerPopupClose();
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
		    	data = JSON.parse(data);
		    	jAlert(data.message,"알림창");
		 	});
			
			//AJAX 전송
			ajaxObj.send();	
	}
	
	function fnClosePopup(){
		firstGrid.setValue(selectRowIdx, "block", '01');
		gfnLayerPopupClose();
	}


</script>
</head>
<body>
	<div class="popup">
		<div class="pop_title">사용자 차단 사유 입력</div>
		<div class="pop_sub">
		<form id="usrInfo" name="usrInfo" method="post" enctype="multipart/form-data" >
			<div class="pop_note" style="margin-bottom:10px;">
				<div class="note_title">차단 사유</div>
				
				<textarea class="input_note lp10" title="비고" style="height:130px;" id="blkLog" name="blkLog" style="height:100%;" maxlength="2000" ><c:out value='${adm2000UsrInfo.blkLog}' /></textarea>	
				
			</div>
	
		
			<div class="btn_div">
				<div class="button_normal save_btn" onclick="fnUpdateAdm2000BlockInfo()">차단</div>
				<div class="button_normal exit_btn" onclick="fnClosePopup()">취소</div>
		    </div>
	    </form>	
	</div>

</body>
</html>