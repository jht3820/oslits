<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style type="text/css">
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 50px; }
.button_normal { width: 39px; height: 22px; line-height: 22px; text-align: center; font-weight: bold; font-size: 1em; border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; margin: 0 auto; border: 1px solid #b8b8b8; cursor: pointer; }
div.pop_sub .pop_left {width:28%;} /* common.css pop_left width값 오버라이딩 */
div.pop_sub .pop_right {width:72%;} /* common.css pop_left width값 오버라이딩 */
.input_txt {padding-left: 5px;}
.ui-datepicker-trigger {margin-left: 2px; margin-top: 2px; width: 32px; height: 32px;}
#btn_user_select { height: 100%; padding-top: 2px; min-width: 34px; }

/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
</style>
<script type="text/javascript">
	var popSearch;
	
	// 보고서 유효성	
	var arrChkObj = {"reportNm":{"type":"length","msg":"보고서 명은 50byte까지 입력이 가능합니다.",max:50}
					 ,"reportYear":{"type":"length","msg":"생성년도는 4byte까지 입력이 가능합니다.",max:4}
					};
	
	$(document).ready(function() {
		
		// 팝업 화면 유형 구분값 - 등록/수정/상세
		var pageType = '${param.popupGb}';
		var reportYear = '${param.reportYear}';
		var reportCd = '${param.reportCd}';
		
		
		/* 	
		*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
		* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
		*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
		*	3. 공통코드 적용할 select 객체 직접 배열로 저장
		* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
		*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
		*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
		*/
		var mstCdStrArr = "RPT00001";
		var strUseYn = 'Y';
		var arrObj = [ $("#confYn")];
		var arrComboType = [ ""];
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
		
		var serverTime=gfnGetServerTime('yyyy');

		
		
		if(pageType=="update"){
			$("#reportYear").attr("readonly", true);
			fnSelectAdm8000MasterInfo(reportYear,reportCd);
		}else if(pageType=="selectInsert"){
			$("#reportYear").attr("readonly", true);
			$("#popupGb").val("insert");
			
			$("#reportYear").val(reportYear);
			$("#confYn").val("02");
		}else{
			$("#reportYear").val(reportYear);
			$("#confYn").val("02");
		}
		
		
		//유효성 체크		
		gfnInputValChk(arrChkObj);


		/* 배포 버전 텍스트 숫자만 입력가능 */
		
		$('#btn_insert_report').click(function() {
			var regExp = /^[0-9]([.]?[0-9])*?$/i;
			
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "admInsertForm";
			var strCheckObjArr = [ "reportYear", "reportNm"];
			var sCheckObjNmArr = [ "기준연도", "보고서명"];
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;	
			}
			// 보고서 저장전 유효성 체크
 			if(!gfnSaveInputValChk(arrChkObj)){
 				return false;	
 			}
			// 보고서 저장
			fnInsertAdm8000Info();
		});
		
		/* 취소 */
		$('#btn_cancle').click(function() {
			gfnLayerPopupClose();
		});

		
		$('#confYn').change(function() {
			fnSelectAdm8000DetailListSize(reportYear,reportCd);
		});
				
		
		var fd = new FormData();
		function fnInsertAdm8000Info(){
			//FormData에 input값 넣기
			gfnFormDataAutoValue('admInsertForm',fd);
			//AJAX 설정
		
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm8000/adm8000/saveAdm8000MasterInfoAjax.do'/>"
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
            	
        		if(pageType=="update"){
        			fnInGridListSet("update");
        			fnInMasterGridListSet(reportYear);	
        		}else if(pageType=="selectInsert"){
        			fnInGridListSet("update");
        			fnInMasterGridListSet(reportYear);	
        		}
        		else{
        			fnInGridListSet();	
        		}
            	
        		
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
			        	gfnLayerPopupClose();
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
	});
	
	/**
	 * 	젠킨스 job 기본정보 상세 조회
	 */
 	function fnSelectAdm8000MasterInfo(reportYear,reportCd){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm8000/adm8000/selectAdm8000MasterInfo.do'/>"}
				,{ "reportYear" : reportYear ,"reportCd" : reportCd });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){

			data = JSON.parse(data);

        	//디테일폼 세팅
        	gfnSetData2ParentObj(data.reportInfo, "admInsertForm");
        	
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
	} 
	
 	function fnSelectAdm8000DetailListSize(reportYear,reportCd){
 		//AJAX 설정
 		var ajaxObj = new gfnAjaxRequestAction(
 				{"url":"<c:url value='/adm/adm8000/adm8000/selectAdm8000DetailList.do'/>","loadingShow":true}
 				,{ "reportYear" : reportYear , "reportCd" : reportCd });
 		//AJAX 전송 성공 함수
 		ajaxObj.setFnSuccess(function(data){
 			data = JSON.parse(data);
 			var list = data.list;
 			
 			if(list.length==0){
 				jAlert("측정항목이 등록되지 않았습니다 확정할수 없습니다.", '알림창');
 				$('#confYn').val('02');
 			}
 			
 		});
 		
 		//AJAX 전송 오류 함수
 		ajaxObj.setFnError(function(xhr, status, err){
 			//세션이 만료된 경우 로그인 페이지로 이동
 	       	if(status == "999"){
 	       		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
 	    		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
 	    		return;
 	       	}
 		});
 		
 		//AJAX 전송
 		ajaxObj.send();
 	}
 	
</script>

<div class="popup">
	<form id="admInsertForm"  onsubmit="return false;">
		<input type="hidden"  name="popupGb" id="popupGb" value="${param.popupGb}" />
		<input type="hidden"  name="reportCd" id="reportCd" value="${param.reportCd}" />

	<div class="pop_title">보고서 생성</div>
	<div class="pop_sub">
		
		<div class="pop_left">생성연도<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<input id="reportYear" type="number" min="1" name="reportYear" title="기준연도">
		</div>
		<div class="pop_left">보고서명<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<input type="text" title="보고서명" class="input_txt" name="reportNm" id="reportNm" value="" maxlength="50" />
		</div>
		<div class="pop_left bottom_line">확정여부</span></div>
		<div class="pop_right bottom_line">
			<span class="search_select">
					<select class="select_useCd" name="confYn" id="confYn" value="" style="height:100%; width:100%;"></select>
			</span>
		</div>
		
		
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_insert_report" >저장</div>
			<div class="button_normal exit_btn" id="btn_cancle">취소</div>
		</div>
	</div>
	</form>
</div>
</html>