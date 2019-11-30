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
#dplDt { width: 89%; margin-right: 0px; }
#btn_user_select { height: 100%; padding-top: 2px; min-width: 34px; }
#dplUsrNm { width: 88.8%; margin-right: 5px; }
/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
</style>
<script type="text/javascript">
	var popSearch;
	$(document).ready(function() {
		
		// 팝업 화면 유형 구분값 - 등록/수정/상세
		var pageType = '${param.popupGb}';
		
		
		//달력 세팅 (배포일)
		var serverTime=gfnGetServerTime('yyyy');
		fnSelectAdm8000MasterYearList();
		
		
		
		/* 배포 버전 텍스트 숫자만 입력가능 */
		
		$('#btn_insert_report').click(function() {
			var regExp = /^[0-9]([.]?[0-9])*?$/i;
			
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "admInsertForm";
			var strCheckObjArr = [ "stdReportYear"];
			var sCheckObjNmArr = [ "기준연도"];
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;	
			}
			fnInsertAdm800DefalutData();
		});
		
		/* 취소 */
		$('#btn_cancle').click(function() {
			gfnLayerPopupClose();
		});
				
	
		
		var fd = new FormData();
		function fnInsertAdm800DefalutData(){
			//FormData에 input값 넣기
			gfnFormDataAutoValue('admInsertForm',fd);
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm8000/adm8000/insertAdm8100ReportInfo.do'/>"
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
            	fnAxGrid5View();
        		
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
	
	function fnSelectAdm8000MasterYearList(){
		//AJAX 설정
 		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm8000/adm8000/selectAdm8000MasterYearList.do'/>","loadingShow":true}
				);
		var html = "";
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			
		   	for(var i=0; i<list.length; i++){
		   		if(list[i].confCnt>0){
		   			html+='<option value="'+list[i].reportYear+'" >'+list[i].reportYear+'</option>';
		   		}
		   	}
		  
		   	$('#stdReportYear').html(html);
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			//세션이 만료된 경우 로그인 페이지로 이동
           	if(status == "999"){
           		
        		return;
           	}
		});
		
		//AJAX 전송
		ajaxObj.send();
	} 

</script>

<div class="popup">
	<form id="admInsertForm"  onsubmit="return false;">
	
	<div class="pop_title">보고서 생성</div>
	<div class="pop_sub">
		
		<div class="pop_left bottom_line">생성연도<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right bottom_line">
			<span class="search_select">
				<select class="select_useCd" name="stdReportYear" id="stdReportYear" value="" style="height:100%; width:34%;">
				</select>
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