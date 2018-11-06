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
#pop_sub_wrap {width: 521px; height: auto; margin-left: 8px; padding-left: 10px; padding-right: 10px;}
#ord {border: 1px solid #ccc; font-size: 1em; width: 100%; min-width: 150px; height: 100%;}
#deptEtc { height: 123px; }
#deptName, #upperDeptId { /* height: 35px; */ height: 100% }
</style>
<script type="text/javascript">

//유효성 체크
var arrChkObj = {"deptName":{"type":"length","msg":"조직명은 200byte까지 입력이 가능합니다.",max:200}
				,"ord":{"type":"number"}
				,"ord":{"type":"length","msg":"순번는 20byte까지 입력이 가능합니다.", "max":20}
				,"deptEtc":{"type":"length","msg":"비고는 2000byte까지 입력이 가능합니다.", "max":2000}
				};

// 순번 길이 체크
var saveObjectValid = {
		"ord":{"type":"length","msg":"순번는 20byte까지 입력이 가능합니다.", "max":20}
}

	$(document).ready(function() {
		

		gfnInputValChk(arrChkObj);
	
		// 등록버튼 클릭
		$('#btn_insert_dept').click(function() {
			
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "newDeptInfoFrm";
			var strCheckObjArr = ["deptName","ord"];
			var sCheckObjNmArr = ["조직명","순번"];
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;	
			}
			
			// 루트(최상위 조직)는 순번 0이므로 순번은 1부터 입력가능
			var chkOrd = $('#ord').val();
			if(chkOrd < 1){
				jAlert("순번은 1 이상 입력가능합니다.");
				$('#ord').val("");
				return false;
			}
			
			// 저장 전 유효성 검사
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}
			
			// 순번 유효성
			if(!gfnSaveInputValChk(saveObjectValid)){
				return false;	
			}
			
			sendToServer();
		});
		
		/* 취소 */
		$('.exit_btn').click(function() {
			gfnLayerPopupClose();
		});
				

		var fd = new FormData();
		function sendToServer(){
			
			//FormData에 input값 넣기
			gfnFormDataAutoValue('newDeptInfoFrm',fd);
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(					
						{"url":"<c:url value='/adm/adm7000/adm7000/insertAdm7000DeptInfoAjax.do'/>"
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
	        	// adm7000.jsp 기존 트리에 새로 추가된 데이터(조직)를 추가한다.
		        fnInsertDeptInfoAjax(data)
	        		
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
	
</script>

<div class="popup">
	<form id="newDeptInfoFrm"  onsubmit="return false;">
		<input type="hidden" id="deptId" name="deptId" value="${param.deptId}"/>
		<input type="hidden" id="lvl" name="lvl" value="${param.lvl}"//>
	<div class="pop_title">신규 조직 등록</div>
	<div class="pop_sub" id="pop_sub_wrap">
		<div class="pop_left">상위 조직 ID</div>
		<div class="pop_right">
			<input type="text" id="upperDeptId" name="upperDeptId" title="상위 조직 ID" class="input_txt readonly" readonly="readonly" value="${param.deptId}" >
		</div>
		<div class="pop_left">조직명<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<input type="text" id="deptName" name="deptName" title="조직명" class="input_txt" maxlength="180" >
		</div>
		<!-- 
			순번을 보여줄 때 상위 조직의 순번을 가져와서 +1 한 값을 사용자에게 보여준다. 
			등록 시 +1 한 순번값을 그대로 등록하므로 쿼리에서 ord(순번)+1 처리를 할 필요 없다.
		-->
		<div class="pop_left">순번<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<input type="number" id="ord"  name="ord" min="1"  value="1" maxlength="20" style= "width: 100%;">	
		</div>

		<div class="pop_left textarea_height bottom_line" style="height: 140px; margin-bottom:0px;">비고</div>
		<div class="pop_right bottom_line" style="height: 140px; margin-bottom:0px;">
			<textarea class="input_note" title="비고" id="deptEtc" name="deptEtc" maxlength="2000"></textarea>
		</div>

		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_insert_dept" >등록</div>
			<div class="button_normal exit_btn">취소</div>
		</div>
	</div>
	</form>
</div>
</html>