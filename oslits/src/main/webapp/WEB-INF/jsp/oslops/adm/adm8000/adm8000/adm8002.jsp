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
#btn_process_select { height: 100%; padding-top: 2px; min-width: 34px; }
#processNm { width: 88.8%; margin-right: 5px; }

/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
</style>
<script type="text/javascript">
	var popSearch;
	
	//유효성 체크		
	var arrChkObj = {"itemNm":{"type":"length","msg":"측정항목 명은 50byte까지 입력이 가능합니다.",max:50}
					};
	
	$(document).ready(function() {
		
		// 팝업 화면 유형 구분값 - 등록/수정/상세
		var pageType = '${param.popupGb}';
		var reportYear = '${param.reportYear}';
		var reportCd = '${param.reportCd}';
		var itemCd = '${param.itemCd}';
		
		
		/* 	
		*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
		* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
		*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
		*	3. 공통코드 적용할 select 객체 직접 배열로 저장
		* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
		*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
		*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
		*/
		var mstCdStrArr = "RPT00002|RPT00003|RPT00005";
		var strUseYn = 'Y';
		var arrObj = [ $("#indexCd") ,$("#periodCd"),$("#itemTypeCd") ];
		var arrComboType = [ ""];
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
		
		if(pageType=="update"){
		
			fnSelectAdm8000DetailInfo(reportYear,reportCd,itemCd);
		}else if(pageType=="selectInsert"){
			$("#reportYear").attr("readonly", true);
			$("#popupGb").val("insert");
			$("#reportYear").val(reportYear);
		}
		
		
	
		
		//유효성 체크		
		gfnInputValChk(arrChkObj);


		/* 배포 버전 텍스트 숫자만 입력가능 */
		
		$('#btn_insert_report').click(function() {
			var regExp = /^[0-9]([.]?[0-9])*?$/i;
			
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "admInsertForm";
			var strCheckObjArr = [];
			var sCheckObjNmArr = [];
			
			if($('#itemTypeCd').val()=="01" ){
				strCheckObjArr = [ "indexCd", "itemNm","itemTypeCd", "weightVal" ,"periodCd" ,"periodOrd","processId"];
				sCheckObjNmArr = [ "지표구분", "측정항목", "측정항목유형", "가중치","보고/평가주기" ,"순번","프로세스"];
			}else{
				strCheckObjArr = [ "indexCd", "itemNm" ,"itemTypeCd" ,"weightVal" ,"periodCd" ,"periodOrd"];
				sCheckObjNmArr = [ "지표구분", "측정항목", "측정항목유형" , "가중치","보고/평가주기" ,"순번" ];
			}
			
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;	
			}
			// 보고서 상세 작성전 유효성 체크
	 		if(!gfnSaveInputValChk(arrChkObj)){
	 			return false;	
	 		}
			
			// 보고서 상세 작성
			fnInsertAdm8000Info();
			
		});
		
		/* 취소 */
		$('#btn_cancle').click(function() {
			gfnLayerPopupClose();
		});

		$('#itemTypeCd').change(function() {
			if($(this).val()=="01" ){ // 프로세스 처리율
				$('#processLabel').show();
				$('#processInput').show();
			}else{
				$('#processLabel').hide();
				$('#processInput').hide();
			}			
		});
		
		$('#btn_process_select').click(function() {
			var data = { "processNm" :  $('#processNm').val()  }; 
			 
			gfnCommonProcessPopup(data ,false,function(objs){
							if(objs.length>0){
								$('#processId').val(objs[0].processId);
								$('#processNm').val(objs[0].processNm);
							}
						});
			
		});
		
		$('#processNm').keyup(function(e) {
			if($('#processNm').val()==""){
				$('#processId').val("");
			}
			if(e.keyCode == '13' ){
				$('#btn_process_select').click();
			}
		});
				
		
		var fd = new FormData();
		function fnInsertAdm8000Info(){
			//FormData에 input값 넣기
			gfnFormDataAutoValue('admInsertForm',fd);
			//AJAX 설정
		
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm8000/adm8000/saveAdm8000DetailInfoAjax.do'/>"
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
            	fnSelectAdm8000DetailList(reportYear,reportCd);	
        		
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
	 * 	보고서 기본정보 상세 조회
	 */
 	function fnSelectAdm8000DetailInfo(reportYear,reportCd,itemCd){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm8000/adm8000/selectAdm8000DetailInfo.do'/>"}
				,{ "reportYear" : reportYear ,"reportCd" : reportCd ,"itemCd" : itemCd });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){

			data = JSON.parse(data);

        	//디테일폼 세팅
        	gfnSetData2ParentObj(data.detailInfo, "admInsertForm");
        	
        	if(data.detailInfo.itemTypeCd !="01"  ){
        		$('#processLabel').hide();
        		$('#processInput').hide();	
        	}else{
        		$('#processLabel').show();
        		$('#processInput').show();
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
	<form id="admInsertForm"  onsubmit="return false;">
		<input type="hidden"  name="popupGb" id="popupGb" value="${param.popupGb}" />
		<input type="hidden"  name="reportCd" id="reportCd" value="${param.reportCd}" />
		<input type="hidden"  name="reportYear" id="reportYear" value="${param.reportYear}" />
		<input type="hidden"  name="itemCd" id="itemCd" value="${param.itemCd}" />

	<div class="pop_title">보고서 상세 생성</div>
	<div class="pop_sub">
		<div class="pop_left">지표구분<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<span class="search_select">
					<select class="select_useCd" name="indexCd" id="indexCd" value="" style="height:100%; width:100%;"></select>
			</span>
		</div>
		<div class="pop_left">측정항목<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<input type="text" title="측정항목" class="input_txt" name="itemNm" id="itemNm" value="" maxlength="50" />
		</div>
		<div class="pop_left">측정항목 유형<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<span class="search_select">
					<select class="select_useCd" name="itemTypeCd" id="itemTypeCd" value="" style="height:100%; width:100%;"></select>
			</span>
		</div>
		<div class="pop_left">가중치<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<input id="weightVal" type="number" min="1" name="weightVal" title="가중치">
		</div>
		<div class="pop_left">보고/평가주기<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<span class="search_select">
					<select class="select_useCd" name="periodCd" id="periodCd" value="" style="height:100%; width:100%;"></select>
			</span>
		</div>
		
		<div class="pop_left" id="processLabel" >프로세스<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right" id="processInput">
			<input type="hidden" name="processId" id="processId" value=""/>
			<input type="text" title="프로세스" class="input_txt lp10" name="processNm" id="processNm" value="" maxlength="20"/>
			<span class="button_normal2 fl" id="btn_process_select"><i class="fa fa-search"></i></span>	
				
		</div>
		
		<div class="pop_left bottom_line">순번<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right bottom_line">
			<input id="periodOrd" type="number" min="1" name="periodOrd" title="순번">
		</div>
		
		
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_insert_report" >저장</div>
			<div class="button_normal exit_btn" id="btn_cancle">취소</div>
		</div>
	</div>
	</form>
</div>
</html>