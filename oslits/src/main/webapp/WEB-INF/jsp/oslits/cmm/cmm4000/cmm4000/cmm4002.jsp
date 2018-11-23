<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<title>OpenSoftLab</title>

<style type="text/css">
/*팝업창 시작 위치*/
.ui-draggable {top: -145px;}
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 50px; }
.button_normal { width: 39px; height: 22px; line-height: 22px; text-align: center; font-weight: bold; font-size: 1em; border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; margin: 0 auto; border: 1px solid #b8b8b8; cursor: pointer; }
div.pop_sub .pop_left {width:28%; padding: 18px 20px 25px 15px;} /* common.css pop_left width값 오버라이딩 */
div.pop_sub .pop_right {width:72%;} /* common.css pop_left width값 오버라이딩 */
.input_txt {padding-left: 5px;}
.ui-datepicker-trigger {margin-left: 2px; margin-top: 2px; width: 32px; height: 32px;}
#dplDt { width: 89%; margin-right: 0px; }
#btn_user_select { height: 100%; padding-top: 2px; min-width: 34px; }
#dplUsrNm { width: 88.8%; margin-right: 5px; }
.bottom_line{border-bottom: 1px solid #ccc;}
.layer_popup_box input[type="password"].input_txt { border-radius: 0px; width: 100%; height: 100%;}
.button_normal2{border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; font-weight: bold;}

</style>
<script>

$(document).ready(function() {
	// 팝업창 오픈 시 비밀번호 input에 포커스
	$("#popUsrPw").focus();
	
});

$(function(){
	//$(".pop_wrap").show();

	//비밀번호 재 설정
	$('#newPwSucc').click(function(){
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "searchPw";
		var strCheckObjArr = ["popUsrPw", "popUsrPw2"];
		var sCheckObjNmArr = ["새 비밀번호", "새 비밀번호 확인"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		//사용자 입력 새 비밀번호 
		var popUsrPw = $('#popUsrPw').val();
		//사용자 입력 새 비밀번호 확인
		var popUsrPw2 = $('#popUsrPw2').val();
		var popUsrId = $('#popUsrId').val();
		
		if(popUsrPw != popUsrPw2){
			jAlert("비밀번호가 일치하지 않습니다.","알림창");
			return false;
		}
		
		if( fnUsrPwValidationChk(popUsrId, popUsrPw) == false ){
			
			return false;
		}
		
		$('#usrPw').val($('#popUsrPw').val());
		$('#usrId').val($('#popUsrId').val());
		$('#initPassYn').val('Y');
		
		fnLoginAction();
	});
	
});


	/**
	 * 	사용자 비밀번호 유효성 체크
	 *	@param popUsrId 사용자 아이디
	 *	@param popUsrPw 사용자 비밀번호
	 *  @returns 결과(boolean)
	 */
	function fnUsrPwValidationChk(popUsrId, popUsrPw){
		
		// 비밀번호 유효성체크 정규식
		var pwRegx = /^(?=.{9,})(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]).*$/;
		
		//유효성 체크
		var saveObjectValid = {  
					"popUsrPw":{"type":"regExp","pattern":pwRegx ,"msg":"비밀번호는 9자 이상 영문 대소문자,숫자,특수문자를 조합해야 합니다.", "required":true}
		}
	
		// 비밀번호 유효성 검사
		if(!gfnInputValChk(saveObjectValid)){
			return false;	
		}
		
		// 비밀번호 사용자 아이디 포함여부 체크
		if(popUsrPw.indexOf(popUsrId) > -1){
			jAlert("비밀번호에는 사용자 아이디를 포함할 수 없습니다.","알림창");
			return false;
		}
		
		// 비밀번호 공백포함 체크
		var emptyRegx = /\s/g;
		if(emptyRegx.test(popUsrPw)){
			jAlert("비밀번호는 공백을 포함할 수 없습니다.","알림창");
			return false;
		}
	
		// 같은 문자열 반복 체크
		var repetRegx = /(\w)\1\1/;
		if(repetRegx.test(popUsrPw)){
			jAlert("비밀번호는 같은 문자를 3번 이상 연속해서 사용하실 수 없습니다.","알림창"); 
	        return false;
	    } 
		
		// 문자열 연속성 체크 - 123, 321, abc, cba
		if( fnContinueStrChk(popUsrPw, 3) == false ){
			jAlert("연속된 문자열(123 ,321, abc, cba 등)을 3자 이상 사용 할 수 없습니다.","알림창"); 
	        return false;
		}
		
		return true;
	}

	/**
	 * 	입력된 문자열에 연속된 문자(123, abc 등)가 있는지 체크한다
	 *	@param str 입력 문자열
	 *	@param limit 자리수, 3 → 123이렇게 3자리 입력시 체크됨
	 *  @returns 결과(boolean)
	 */
	function fnContinueStrChk(str, limit) {
		
		var char1, char2, char3, char4 = 0;

		for (var i = 0; i < str.length; i++) {
			var inputChar = str.charCodeAt(i);

			if (i > 0 && (char3 = char1 - inputChar) > -2 && char3 < 2 && (char4 = char3 == char2 ? char4 + 1 : 0) > limit - 3){
				return false;
			}	
			char2 = char3;
			char1 = inputChar;
		}
		return true;
	}

	function ajaxClose(){
		$(".layer_popup_box .ajaxBox").empty();
		$(".layer_popup_box").hide();
		$(".bg").hide();
	}
</script>
<div class="popup">
	<form id="searchPw" name="searchPw">
	<input type ="hidden" id=popUsrId name="popUsrId" value="${param.usrId}" />
		<div class="pop_title">비밀번호 변경</div>
		<div class="pop_sub">
			
			<div class="pop_left">비밀번호</div>
			<div class="pop_right">
				<input type="password" id="popUsrPw" name="popUsrPw" title="비밀번호" class="input_txt" value="" maxlength="20" />
			</div>
			<div class="pop_left bottom_line">비밀번호 확인</div>
			<div class="pop_right bottom_line">
				<input type="password" id="popUsrPw2" name="popUsrPw2" title="비밀번호 확인" class="input_txt" value="" maxlength="20" />
			</div>
			<div class="btn_div">
				<div class="button_normal2 save_btn" id="newPwSucc" >확인</div>
				<div class="button_normal2 exit_btn" onclick="ajaxClose()" >취소</div>
			</div>
		</div>
	</form>
</div>
