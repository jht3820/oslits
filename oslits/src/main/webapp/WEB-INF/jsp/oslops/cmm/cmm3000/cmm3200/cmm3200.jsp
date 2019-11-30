<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="UTF-8">
		<!-- 모바일 : viewport 제거 -->
		<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
		<title>Open Soft Lab</title>
		<link rel="stylesheet" href="<c:url value='/css/common/reset.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/font/font.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/common/common.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/oslits/cmm.css'/>">
			
		<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>"></script>

		<script src="<c:url value='/js/common/common.js'/>"></script>
	</head>
	<style>
	
	</style>
	<script>
	$(document).ready(function(){
		//이메일 유효성 체크
		//유효성 체크
		var arrChkObj = {"usrEmail":{"type":"email","msg":"이메일 형식이 아닙니다."}
						,"usrNm":{"type":"length","max":20}};
		gfnInputValChk(arrChkObj);
		
		/* 이름 등록 유효성 체크 시작 */
		$("#usrNm").blur(function() {
			var usrNm = $("#usrNm").val();
			
			if( usrNm == "" ){
				$("#usrNm_message").html("필수 입력사항 입니다.");
				$("#usrNm_message").attr("class","input_under_text_r").show();
				$("#usrNmChk").val("N");
			}else{
				$("#usrNm_message").html("정상 등록 하셨습니다.");
				$("#usrNm_message").attr("class","input_under_text_b").show();
				$("#usrNmChk").val("Y");
			} 
		});
		/* 이름 등록 유효성 체크 끝 */
		
		/* 아이디 유효성 체크 시작 */
		$('#usrId').blur(function() {
			var chk_id = /^[a-zA-Z0-9_-]{5,20}$/; // 소문자, 언더바(_),파이픈(-) 최소 5자리 ~ 20자리 
			var usrId = $("#usrId").val(); 
			if( usrId == "" ){
				$("#usrId_message").html("필수 입력사항 입니다.");
				$("#usrId_message").attr("class","input_under_text_r").show();
				$("#usrIdChk").val("N");
			}else{
				if(chk_id.test($("#usrId").val())){ 
					
					/* 아이디 중복 확인 시작 */
					//AJAX 설정
					var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/cmm/cmm3000/cmm3200/selectCmm3200IdCheck.do'/>"}
							,{InUsrId:usrId});
					//AJAX 전송 성공 함수
					ajaxObj.setFnSuccess(function(data){
						data = JSON.parse(data);

	 	                if(data != null)    {
							if(data.chkId == "Y"){
								// 사용가능한 아이디
								$("#usrId_message").html("사용가능한 아이디 입니다.");
								$("#usrId_message").attr("class","input_under_text_b").show();
								$("#usrIdChk").val("Y");
		                		
							}else{
								// 사용 불가능한 아이디
								$("#usrId_message").html(usrId+"는 사용중인 아이디 입니다.");
								$("#usrId_message").attr("class","input_under_text_r").show();
								$("#usrId").val("");
								$("#usrId").focus();
								$("#usrIdChk").val("N");
							}
							
	 	                }
	 	                
					});
					//AJAX 전송
					ajaxObj.send();
					/* 아이디 중복 확인 끝 */
					
				}else{
					$("#usrId_message").html("5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
					$("#usrId_message").attr("class","input_under_text_r").show();
					$('#usrId').val("");
					$("#usrIdChk").val("N");
				} 
			}
		});
		/* 아이디 유효성 체크 종료 */
		
		
		/* 비밀번호 유효성 체크 시작 */
		$('#usrPw').blur(function() {

			var usrId = $("#usrId").val();
			var userPw = $("#usrPw").val();
			
			if( userPw == "" ){
				$("#usrPw_message").html("필수 입력 사항입니다.");
				$("#usrPw_message").attr("class","input_under_text_r").show();
				$('#usrPwChk').val("N");
			}else{
				if( fnUsrPwValidationChk(usrId, userPw) ){
					$("#usrPw_message").html("정상적인 비밀번호 입니다.");
					$("#usrPw_message").attr("class","input_under_text_b").show();
					$('#usrPwChk').val("Y");
				}else{
					$('#usrPw').val("");
					$('#usrPwChk').val("N");
				} 
			}
		});
		/* 비밀번호 유효성 체크 종료 */
		
		
		/* 비밀번호 확인 유효성 체크 시작 */
		$('#reUsrPw').blur(function() {
			var usrPw = $("#usrPw").val();
			var reUsrPw = $("#reUsrPw").val();
 
			if( reUsrPw == "" ){
				$("#reUsrPw_message").html("필수 입력사항 입니다.");
				$("#reUsrPw_message").attr("class","input_under_text_r").show();
				$('#reUsrPwChk').val("N");
			}else{
				if( usrPw !=  reUsrPw ){ // 비밀번호 불일치
					$("#reUsrPw_message").html("비밀번호가 일치하지 않습니다.");
					$("#reUsrPw_message").attr("class","input_under_text_r").show();
					$("#reUsrPw").val("");
					$('#reUsrPwChk').val("N");
				}

				if( usrPw ==  reUsrPw ){ // 비밀번호 일치
					$("#reUsrPw_message").html("비밀번호가 일치합니다.");
					$("#reUsrPw_message").attr("class","input_under_text_b").show();
					$('#reUsrPwChk').val("Y");
				}
			}
		});
		/* 비밀번호 확인 유효성 체크 종료 */
		
		
		/* 인증번호 발송 시작 */
		$('#sendValbtn').click(function(){

			var usrEmail = $('#usrEmail').val();
			var pattern =/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
			
			if( usrEmail == "" ){
				$("#usrEmail_message").html("이메일을 입력하세요.");
				$("#usrEmail_message").show();
				$("#usrEmail").focus();
				$("#usrEmailChk").val("N");
			}else if(usrEmail.length<6 || !pattern.test(usrEmail)) {
				$("#usrEmail_message").html("이메일 형식이 아닙니다.");
				$("#usrEmail_message").show();
			}else{
				$("#usrEmail_message").html("이메일을 발송중 입니다.");
				$("#usrEmail_message").show(function(){
					//AJAX 설정
					var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/cmm/cmm3000/cmm3200/selectCmm3200MailSend.do'/>","async":false}
							,{InEmail:usrEmail});
					//AJAX 전송 성공 함수
					ajaxObj.setFnSuccess(function(data){
						data = JSON.parse(data);
		            	
	 	                if(data != null)    {
							if(data.emailChk == "Y"){
								// 인증메일 발송중 메시지 출력
								
								$("#usrEmail_message").html("이메일을 발송 했습니다.");
								$("#usrEmail_message").attr("class","input_under_text_b").show();
								$("#usrEmailChk").val("Y");
								
		                		if(data.emailSendTime != 'N'){
		               		    	$("#usrEmail_message").html($("#usrEmail_message").html()+" 인증번호 재발송은 "+data.emailSendTime+"초 후 가능합니다.");
		               		    	$("#usrEmail_message").attr("class","input_under_text_r");
		                		}
		                		
							}else{
								// 이메일 값이 잘못됐다는 메시지 출력
								$("#usrEmail_message").html("이메일을 다시 확인해주세요");
								$("#usrEmail_message").attr("class","input_under_text_r").show();
								$("#usrEmailChk").val("N");
							}
							
	 	                } else {
							$("#usrEmail_message").html("이메일발송에 문제가 생겼습니다. 다시한번 확인해 주세요.");
							$("#usrEmail_message").show();
							$("#usrEmailChk").val("N");

	 	                }
					});
					//AJAX 전송
					ajaxObj.send();
					
				});
			}
		});
		/* 인증번호 발송 종료 */
		
		
		/* 인증번호 확인 시작 */
		$('#reSendValbtn').click(function(){

			var sendVal = $('#sendVal').val();
			
			if(  sendVal == "" ){
				$("#sendVal_message").html("인증번호를 입력하세요.");
				$("#sendVal_message").show();
				$("#sendVal").focus();
				$("#sendValChk").val("N");
			}else{
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/cmm/cmm3000/cmm3200/selectCmm3200SendValCheck.do'/>"}
						,{InSendVal:sendVal});
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);

 	                if(data != null)    {
 	                	if(data.authNum == "Y"){
 	                		$("#sendVal_message").html("정상인증 되었습니다.");
							$("#sendVal_message").attr("class","input_under_text_b").show();
							$("#sendValChk").val("Y");
							$("#successToggle").hide();
							$('.join_form_border').stop().animate({height:'390px'},50);
 	                	}else{
 	                		$("#sendVal_message").html("인증번호를 다시 확인해주세요.");
 	                		$('#sendVal').val("");
 	                		$("#sendVal_message").attr("class","input_under_text_r").show();
 	                		$("#sendValChk").val("N");
 	                	}
 	                }
				});
				
				//AJAX 전송
				ajaxObj.send();
			}
		});
		/* 인증번호 확인 종료 */
		
		// 가입완료 버튼 클릭
		$(".button_agree").click(function(){
			var usrNm 	= $("#usrNm").val();
			var usrId 		= $("#usrId").val();
			var usrPw 		= $("#usrPw").val();
			var usrEmail 	= $("#usrEmail").val();
			var sendValChk = $('#sendValChk').val();
			
			if( $("#usrNmChk").val() != "Y"){
				jAlert("이름을 확인해 주세요.","알림창");
				return false;	
			}
			
			if( $("#usrIdChk").val() != "Y"){
				jAlert("아이디를 확인해 주세요.","알림창");
				return false;				
			}
			
			if( $("#usrPwChk").val() != "Y"){
				jAlert("비밀번호를 확인해 주세요","알림창");
				return false;				
			}
			
			if( $("#reUsrPwChk").val() != "Y"){
				jAlert("비밀번호를 확인해 주세요","알림창");
				return false;				
			}
			
			if(sendValChk != 'Y' &&  $("#usrEmailChk").val() != "Y"){
				jAlert("이메일을 확인해 주세요","알림창");
				return false;				
			}
			
			// 인증 확인.
			if(sendValChk != 'Y' &&  $("#sendValChk").val() != "Y"){
				jAlert("인증 확인을 해주세요.","알림창");
				return false;				
			}

			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/cmm/cmm3000/cmm3200/insertCmm3200JoinIng.do'/>"}
					,{ "usrNm":usrNm, "usrId":usrId, "usrPw":usrPw, "usrEmail":usrEmail });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
	        	
	        	//등록이 실패하면 실패메시지 후 리턴
	        	if(data.successYn == 'Y'){

	        		jAlert("회원가입에 성공 하셨습니다. 로그인 페이지로 이동 하겠습니다.","알림",function(result){
	        			if( result ){
	        				$(location).attr('href',"/cmm/cmm4000/cmm4000/selectCmm4000View.do");
	        			}
	        		});
	        		
	        		return false;

	        	} else{
	        		toast.push(data.message);
	        		return false;
	        	}
	        	
	        	toast.push(data.message);
			});
			
			//AJAX 전송
			ajaxObj.send();
			
		});
	
		// 가입취소 버튼 클릭
		$(".button_agree_cancel").click(function(){
			$(location).attr('href',"/cmm/cmm4000/cmm4000/selectCmm4000View.do");
		})
		
	});
	
	
	/**
	 * 	사용자 비밀번호 유효성 체크
	 *	@param usrId 사용자 아이디
	 *	@param usrPw 사용자 비밀번호
	 *  @returns 결과(boolean)
	 */
	function fnUsrPwValidationChk(usrId, usrPw){
		
		// 비밀번호 유효성체크 정규식
		var pwRegx = /^(?=.{9,})(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]).*$/;
		
		if( pwRegx.test(usrPw) == false ){
			$("#usrPw_message").html("비밀번호는 9자 이상 영문 대소문자,숫자,특수문자를 조합해야 합니다.");
			$("#usrPw_message").attr("class","input_under_text_r").show();	
			return false;
		}
		
		// 비밀번호 사용자 아이디 포함여부 체크
		if(usrPw.indexOf(usrId) > -1){
			$("#usrPw_message").html("비밀번호에는 사용자 아이디를 포함할 수 없습니다.");
			$("#usrPw_message").attr("class","input_under_text_r").show();	
			return false;
		}
		
		// 비밀번호 공백포함 체크
		var emptyRegx = /\s/g;
		if(emptyRegx.test(usrPw)){
			$("#usrPw_message").html("비밀번호는 공백을 포함할 수 없습니다.");
			$("#usrPw_message").attr("class","input_under_text_r").show();
			return false;
		}

		// 같은 문자열 반복 체크
		var repetRegx = /(\w)\1\1/;
		if(repetRegx.test(usrPw)){
			$("#usrPw_message").html("비밀번호는 같은 문자를 3번 이상 연속해서 사용하실 수 없습니다.");
			$("#usrPw_message").attr("class","input_under_text_r").show();
            return false;
        } 
		
		// 문자열 연속성 체크 - 123, 321, abc, cba
		if( fnContinueStrChk(usrPw, 3) == false ){
			$("#usrPw_message").html("비밀번호는 연속된 문자열(123,abc 등)을 3자 이상 사용 할 수 없습니다.");
			$("#usrPw_message").attr("class","input_under_text_r").show();
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
	
	</script>
	<body style="background-color:#e5e9ec; width:100%; height:100%;">
		<div class="join_wrap">
			<div class="header_img">
				<!-- <img src="/images/login/login_logo2.png" alt="OpenSoftLab 로고" /> -->
			</div>
			
			<!-- 회원 가입 폼 -->
			<div class="join_form_wrap">
				<div class="join_form_border">
					<form id="joinFrm" name="joinFrm" method="post">
						<input type="hidden" name="usrNmChk" 		id="usrNmChk" />
						<input type="hidden" name="usrIdChk" 		id="usrIdChk" />
						<input type="hidden" name="usrPwChk" 		id="usrPwChk" />
						<input type="hidden" name="reUsrPwChk" 		id="reUsrPwChk" />
						<input type="hidden" name="usrEmailChk" 	id="usrEmailChk" />
						<c:choose>
							<c:when test="${not empty emailAuthChk and emailAuthChk eq 'Y'}">
								<input type="hidden" name="sendValChk" 		id="sendValChk"/>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="sendValChk" 		id="sendValChk" value="Y" />
							</c:otherwise>
						</c:choose>
						

						<!-- 이름 -->
						<input type="text" class="input_type" name="usrNm" id="usrNm" placeholder="이름" title="이름" style="margin-bottom:8px;"/>	
						<div class="input_under_text" id="usrNm_message">이름을 입력해 주세요.</div>
						<!--// 이름 -->
						
						<!-- 아이디 -->
						<input type="text" class="input_type" name="usrId" id="usrId" placeholder="아이디" title="아이디" style="margin-bottom:8px;" />
						<div class="input_under_text" id="usrId_message" >5~20자의 영문 소문자,대문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.</div>
						<!--// 아이디 -->
						
						<!-- 비밀번호 -->
						<input type="password" class="input_type" name="usrPw" id="usrPw"  placeholder="비밀번호" title="비밀번호" style="margin-bottom:8px;"/>	
						<div class="input_under_text" id="usrPw_message" >비밀번호는 9자 이상 영문 대소문자,숫자,특수문자를 조합해야 합니다.</div>
						<!--// 비밀번호 -->
						
						<!-- 비밀번호 확인 -->
						<input type="password" class="input_type" name="reUsrPw" id="reUsrPw" placeholder="비밀번호 확인" title="비밀번호 확인" style="margin-bottom:8px;"/>	
						<div class="input_under_text" id="reUsrPw_message">비밀번호를 한번 더 입력해 주세요.</div>
						<!--// 비밀번호 확인 -->
						<c:choose>
							<c:when test="${not empty emailAuthChk and emailAuthChk eq 'Y'}">
								<div id="successToggle">
									<!-- 이메일 -->
									<input type="text" class="input_type2" name="usrEmail" id="usrEmail" placeholder="이메일로 인증번호를 확인하세요." title="인증번호를 확인하세요" style="margin-bottom:8px;"/>
									<span class="button_email" style="margin-bottom:8px;cursor:pointer;" id="sendValbtn">인증번호 발송</span>
									<div class="input_under_text_r" id="usrEmail_message"></div>
									<!--// 이메일 -->
								
									<!-- 인증번호 -->
									<input type="text" class="input_type2" name="sendVal" id="sendVal" placeholder="인증번호." title="인증번호 확인" style="margin-bottom:8px;"/>
									<span class="button_email" style="margin-bottom:8px;cursor:pointer;" id="reSendValbtn">인증번호 확인</span>
								</div>
								<div class="input_under_text_r" id="sendVal_message"></div>
								<!--// 인증번호 -->
							</c:when>
							<c:otherwise>
								<input type="text" class="input_type2" name="usrEmail" id="usrEmail" placeholder="이메일" title="이메일" style="margin-bottom:8px;width: 420px;"/>
							</c:otherwise>
						</c:choose>
						
					</form>
				</div>
				
				<div class="agree_btn_box">
					<span class="button_agree">가입완료</span><span class="button_agree_cancel">가입 취소</span>	
				</div>
				
			</div>
			<!-- 회원 가입 폼 -->


			<div class="join_footer">Copyright ⓒ<span class="bold">Open Soft Lab</span> Corp. All Rights Reserved.</div>
		</div>				
	</body>
</html>