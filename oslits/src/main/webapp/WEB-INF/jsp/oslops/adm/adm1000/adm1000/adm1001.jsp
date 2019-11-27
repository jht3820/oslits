<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<title>OpenSoftLab</title>

<style>
	.check_prj{padding:31px 25px !important;}
	.checking{color:#4b73eb;padding:5px 0px; display:inline-block;}
</style>
	
<script>
$(document).ready(function() {
	
	/********************************************************************
	*	공통기능 부분 정의 시작													*
	*********************************************************************/
	
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = CMM00001:사용여부 
	*/
	var mstCdStrArr = "CMM00001";
	var strUseYn = 'Y';
	var arrObj = [$("#authGrpUseCd")];
	var arrComboType = ["S"];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , true);
	
	
	// 권한그룹 추가버튼 레이어 팝업 - 취소버튼 클릭 시 팝업 창 사라지기
	$('#exitBtn').click(function() {
		gfnLayerPopupClose();
	});
	
	// 권한그룹추가 레이어 팝업 - 저장버튼 클릭 시 롤 저장 처리 
	$('#saveBtn').click(function() {
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "authGrpFrm";
		var strCheckObjArr = ["authGrpNm", "authGrpDesc"]; //, "authGrpUseCd"
		var sCheckObjNmArr = ["권한그룹 명", "권한그룹 설명"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		
		var cnt = 0;
		var newAuthGrpId = "";
		
		//기본롤 체크 갯수와 새 ID 생성
		for(var i = 1; i < 6; i++){
			if($("#pop_ch" + i).prop("checked") == true){
				cnt++;
				newAuthGrpId += ($("#pop_ch" + i).attr("name")).substring(0,2);
			}
		}
		
		//체크된롤이 2개 미만이면 롤은 2가지 이상 선택해야함.
		if(cnt < 2){
			jAlert('기본권한을 2개 이상 선택하여야 합니다.', '알림창');
			return;
		}else if(gfnIsNumeric('authGrpOrd')){
			//신규 롤 등록
			fnInsertAuthGrp(newAuthGrpId);	
		}
		
	});
});

</script>
	
<div class="popup">
	<form id="authGrpFrm" name="authGrpFrm">
		<input type="hidden" id="newAuthGrpId" name="newAuthGrpId">
		<div class="pop_title">권한 그룹 생성 ${selInfo }</div>
		<div class="pop_sub">
		
			<div class="pop_left">권한그룹 명</div>
			<div class="pop_right">
				<input type="text" title="권한그룹 명" class="input_txt" id="authGrpNm" name="authGrpNm"/>
			</div>
			
			<div class="pop_left">기본 권한</div>
			<div class="pop_right adm_chk">
				<div class="chk_div">
					<input type="checkbox" title="체크박스" class="PO_ck" name="PO_ck" id="pop_ch1"/><label for="pop_ch1"></label><span class="title_align">PO</span>
					<input type="checkbox" title="체크박스" class="SM_ck" name="SM_ck" id="pop_ch2"/><label for="pop_ch2"></label><span class="title_align">SM</span>
					<input type="checkbox" title="체크박스" class="ST_ck" name="ST_ck" id="pop_ch3"/><label for="pop_ch3"></label><span class="title_align">ST</span>
					<input type="checkbox" title="체크박스" class="TT_ck" name="TT_ck" id="pop_ch4"/><label for="pop_ch4"></label><span class="title_align">TT</span>
					<input type="checkbox" title="체크박스" class="CS_ck" name="CS_ck" id="pop_ch5"/><label for="pop_ch5"></label><span class="title_align">CS</span>
				</div>
			</div>
			
			<div class="pop_left textarea_height" style="height:332px !important;">권한 설명</div> 
			<div class="pop_right">
				<textarea class="input_note" id="authGrpDesc" name="authGrpDesc" title="권한그룹 설명"></textarea>
				<!-- <input type="text" title="권한그룹 설명" class="note_tx" id="authGrpDesc" name="authGrpDesc" />  -->
			</div>
			
			<div class="pop_left bottom_line">권한 순서</div>
			<div class="pop_right bottom_line">
				<input type="number" min="1" title="권한 순서" class="input_txt" id="authGrpOrd" name="authGrpOrd"/>
			</div>
			
			<div class="btn_div">
				<div class="button_normal save_btn complete_btn" id="saveBtn">저장</div>
				<div class="button_normal exit_btn" id="exitBtn">취소</div>
			</div>
		</div>
	</form>
</div>

</html>