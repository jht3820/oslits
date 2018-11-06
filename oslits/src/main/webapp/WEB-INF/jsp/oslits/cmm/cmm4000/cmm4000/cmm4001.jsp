<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
	.layer_popup_box { width: 540px !important; height: 444px !important; }
	.input_under_text_r { display: none; padding-left: 15px; font-size: 11px; color: #ff0000; margin-bottom: 15px; }
</style>
<script>
$(function(){
	//$(".pop_wrap").show();
	var pattern =/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	
	/* 아이디찾기, 인증번호 발송 버튼 클릭 */
	$('#searchIdAuth').click(function(){
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "searchId";
		var strCheckObjArr = ["inputNM", "inputEmail"];
		var sCheckObjNmArr = ["이름", "이메일"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;
		}
		//사용자 이름 입력
		var inputNM = $('#inputNM').val();
		//사용자 인증을 위한 이메일 입력
		var inputEmail = $('#inputEmail').val();
		if(!pattern.test(inputEmail)) {
			jAlert("이메일 형식이 아닙니다.");
			return false;
		}
		
		$("#usrEmail_message").html("이메일을 발송중 입니다.");
		$("#usrEmail_message").show(function(){
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/cmm/cmm4000/cmm4001/selectCmm4001MailSend.do'/>","async":false}
					,{InNm:inputNM, InEmail:inputEmail});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
            	//전달 받은 값 json으로 파싱
            	
	                if(data != null)    {
                	//이름 값이 잘못된 경우
                	if(data.nmChk != 'Y'){
                		$("#usrEmail_message").html("이름 값이 잘못되었습니다.");
           		    	$("#usrEmail_message").attr("class","input_under_text_r");
                    }
                	//이메일 값이 잘못된 경우
                	if(data.emailChk != 'Y'){
                		$("#usrEmail_message").html("이메일 값이 잘못되었습니다.");
           		    	$("#usrEmail_message").attr("class","input_under_text_r");
                    }
                	//사용자 정보 찾은 경우
                	//암호화된 인증 값 생성 완료
                	if(data.nmChk == 'Y' && data.emailChk == 'Y'){
                		$("#usrEmail_message").html("이메일을 발송 했습니다.");
						$("#usrEmail_message").attr("class","input_under_text_b").show();

                		if(data.emailSendTime != 'N'){
                			$("#usrEmail_message").html($("#usrEmail_message").html()+" 인증번호 재발송은 "+data.emailSendTime+"초 후 가능합니다.");
               		    	$("#usrEmail_message").attr("class","input_under_text_r");
                		}	
                	}
                } else {
                	// ajax 오류 인경우 경고메시지 보이기 
                	$("#usrEmail_message").html("이메일발송에 문제가 생겼습니다. 다시한번 확인해 주세요.");
					$("#usrEmail_message").show();
                } 
			});
			//AJAX 전송
			ajaxObj.send();
			
		});
		
	});
	
	/* 아이디 찾기 , 인증번호 입력 후 확인 버튼 클릭 */
	$('#idAuthSucc').click(function(){
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "searchId";
		var strCheckObjArr = ["inputNM", "inputEmail", "inputAuth"];
		var sCheckObjNmArr = ["이름", "이메일", "인증번호"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		//사용자 입력 이름 정보
		var inputNM = $('#inputNM').val();
		//사용자 입력 이메일 정보
		var inputEmail = $('#inputEmail').val();
		if(!pattern.test(inputEmail)) {
			jAlert("이메일 형식이 아닙니다.");
			return false;
		}
		
		//인증번호 입력
		var inputAuth = $('#inputAuth').val();
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm4000/cmm4001/selectCmm4001ChkAction.do'/>","async":false}
				,{InNm:inputNM, InEmail:inputEmail, idAuthSucc:inputAuth});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	// 전달 받은 값 json으로 파싱
             if(data != null)    {
             	// 이전에 입력한 정보가 없을 경우
             	if(data.nmChk == 'N' || data.emailChk == 'N' || data.authNum != 'Y'){
             		// 경고 문구 출력과 함께, input box 보여주기
             		toast.push({body:'입력 정보를 확인해주세요.', type:'Warning'});
             	}else if(data.succId != ''){
             		// 인증번호 성공 시 아이디 값 받기
	                	$('.find_id_info').html(data.succId)
	                	$('.find_info_id_div').css('display', 'none');
	               		$('.find_id_div').css('display', 'inline-block');
	               		
	               		$('.pop_bottom_pwd').css('display', 'none');
	               		$('.find_info_pwd_div').css('display', 'none');
	               		
             	}else{
             		$('.hiddeninfo_3').html('아이디를 찾을 수 없습니다.');
             	}
             }
		});
		//AJAX 전송
		ajaxObj.send();
	});
	/* 비밀번호 찾기 */
	//인증번호 발송 버튼 클릭
	$('#searchPwAuth').click(function(){
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "searchPw";
		var strCheckObjArr = ["pwInputId", "pwInputEmail"];
		var sCheckObjNmArr = ["아이디", "이메일"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		//사용자 입력 아이디 정보
		var pwInputId = $('#pwInputId').val();
		//사용자 입력 이메일 정보
		var pwInputEmail = $('#pwInputEmail').val();
		
		$("#usrEmail_message_pw").html("이메일을 발송중 입니다.");
		$("#usrEmail_message_pw").show(function(){
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/cmm/cmm4000/cmm4001/selectCmm4001PwMailSend.do'/>","async":false}
					,{InId:pwInputId, InEmail:pwInputEmail});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
            	//전달 받은 값 json으로 파싱
            	
	                if(data != null)    {
                	//아이디 값이 잘못된 경우
                	if(data.idChk != 'Y'){
                		$("#usrEmail_message_pw").html("아이디 값이 잘못되었습니다.");
           		    	$("#usrEmail_message_pw").attr("class","input_under_text_r");
                    }
                	//이메일 값이 잘못된 경우
                	if(data.emailChk != 'Y'){
                		$("#usrEmail_message_pw").html("이메일 값이 잘못되었습니다.");
           		    	$("#usrEmail_message_pw").attr("class","input_under_text_r");
                    }
                	if(data.dbChk == 'N'){
                	//사용자 정보 없음 또는 사용자 정지 상태
                	toast.push({body:'사용자 정보에 문제가 있습니다.<br>(조회 결과 없음 또는 정지 상태)', type:'Warning'});
                	$("#usrEmail_message_pw").html("사용자 정보에 문제가 있습니다.<br>(조회 결과 없음 또는 정지 상태)");
					$("#usrEmail_message_pw").attr("class","input_under_text_b").show();
                	}else if(data.idChk == 'Y' && data.emailChk == 'Y'){
                		$("#usrEmail_message_pw").html("이메일을 발송 했습니다.");
						$("#usrEmail_message_pw").attr("class","input_under_text_b").show();
						$('.find_div').remove();
                		//사용자 정보 찾은 경우
                		if(data.pwEmailSendTime != 'N'){
                			$("#usrEmail_message_pw").html($("#usrEmail_message_pw").html()+" 인증번호 재발송은 "+data.pwEmailSendTime+"초 후 가능합니다.");
               		    	$("#usrEmail_message_pw").attr("class","input_under_text_r");
                		}
                	}
                } else {
                	// ajax 오류 인경우 경고메시지 보이기
                	$("#usrEmail_message_pw").html("이메일발송에 문제가 생겼습니다. 다시한번 확인해 주세요.");
					$("#usrEmail_message_pw").show();
                } 
			});
			
			//AJAX 전송
			ajaxObj.send();
			
		});	 
	});
	//비밀번호 찾기, 확인 버튼 클릭
	$('#pwAuthSucc').click(function(){
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "searchPw";
		var strCheckObjArr = ["pwInputId", "pwInputEmail", "pwInputAuth"];
		var sCheckObjNmArr = ["아이디", "이메일", "인증번호"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		//사용자 입력 아이디 정보
		var pwInputId = $('#pwInputId').val();
		//사용자 입력 이메일 정보
		var pwInputEmail = $('#pwInputEmail').val();
		//인증번호 입력
		var pwInputAuth = $('#pwInputAuth').val();
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm4000/cmm4001/selectCmm4001PwChkAction.do'/>","async":false}
				,{InId:pwInputId, InEmail:pwInputEmail, idAuthSucc:pwInputAuth});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	// 전달 받은 값 json으로 파싱
             if(data != null)    {
             	// 이전에 입력한 정보가 없을 경우
             	if(data.idChk != 'Y' || data.emailChk != 'Y' || data.authNum != 'Y'){
             		// 경고 문구 출력과 함께, input box 보여주기
             		toast.push({body:'입력 정보를 확인해주세요.', type:'Warning'});
            		
             	}else if(data.dbChk == 'N'){
            	//사용자 정보 없음 또는 사용자 정지 상태
             		toast.push({body:'사용자 정보에 문제가 있습니다.<br>(조회 결과 없음 또는 정지 상태)', type:'Warning'});
            	}else if(data.succCode != ''){
             		// 인증번호 성공 시 hidden box에 인증 값 넣기
	                	$('#pwHiddenCode').val(data.succCode)
	                	$(".pop_bottom_id").css('display', 'none');
 	           		$('.find_info_id_div').css('display', 'none');
 	           		
 	           		$('.find_info_pwd_div').css('display', 'none');
 	           		$('.find_pwd_div').css('display', 'inline-block');
	               		
             	}else{
             		dialog.push({body:'아이디를 찾을 수 없습니다.', type:'Warning'});
             	}
             }
		});
		
		//AJAX 전송
		ajaxObj.send();
	});
	//비밀번호 재 설정
	$('#newPwSucc').click(function(){
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "searchPw";
		var strCheckObjArr = ["pwInputNew", "pwInputNew2"];
		var sCheckObjNmArr = ["새 비밀번호", "새 비밀번호 확인"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}
		//암호화 코드 정보 hidden box
		var pwAuthCode = $('#pwHiddenCode').val();
		//사용자 입력 새 비밀번호 
		var pwInputNew = $('#pwInputNew').val();
		//사용자 입력 새 비밀번호 확인
		var pwInputNew2 = $('#pwInputNew2').val();
		
			if(pwInputNew == pwInputNew2){
				
				//유효성 체크
				var saveObjectValid = {
							//아이디 중복검사 시에 체크 함
							/* "usrId":{"type":"regExp","pattern":/^[a-z0-9_-]{5,20}$/ ,"msg":"아이디는 반드시 5-20자내에서 사용 가능합니다.", "required":true} */  
							"pwInputNew":{"type":"regExp","pattern":/^.*(?=.{6,20})(?=.*[0-9])(?=.*[a-zA-Z]).*$/ ,"msg":"패스워드는 6자 이상 영문과 숫자를 조합해야 합니다.","required":true,"msgType":"toast"}
							//이메일 중복검사 시에 체크 함
							/* ,"usrEmail":{"type":"regExp","pattern":/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i ,"msg":"이메일 형식이 아닙니다.","required":true} */
				}
				if(!gfnInputValChk(saveObjectValid)){
					return false;	
				}
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/cmm/cmm4000/cmm4001/selectCmm4001PwNewAction.do'/>","async": false}
						,{InAuthCode:pwAuthCode, InNewPw:pwInputNew, InNewPw2:pwInputNew2});
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);
 	                if(data != null)    {
 	                	// 인증 값이 없는 경우
 	                	if(data.codeChk == 'N'){
 	                		toast.push({body:'인증 값이 잘못되었습니다.', type:'Warning'});
 	                	}else if(data.bothPwChk == 'N'){
 	                		// '새 비밀번호'입력 값과 '새 비밀번호 확인' 입력 값 이 다른경우 
 	                		toast.push({body:'두 개의 값이 다르게 입력되었습니다.', type:'Warning'});
 	                	}else if(data.bothCodeChk == 'N'){
 	                		// 인증 값이 손실된경우
 	                		dialog.push({body:'인증 값이 손실되었습니다.<br>다시 진행해주세요.', type:'Warning'});
 	                	}else if(data.allChk == 'Y'){
 	                		// 변경 성공
 	                		toast.push('비밀번호 재 설정 완료');
 	                		$('.find_pwd_div').html(
 	                				'비밀번호를 재 설정 했습니다.'
 	                				+'<div><input type=\"button\" class=\"btn_2\" value=\"완료\"  title=\"완료 버튼\" onclick=\"ajaxClose()\"></div>');
 	                	}else{
 	                		// 오류 발생
 	                		dialog.push({body:'재 설정 도중 오류가 발생했습니다.<br>다시 진행해주세요.', type:'Warning'});
 	                	}
 	                }
				});
				//AJAX 전송
				ajaxObj.send();
			}else{
				// '새 비밀번호'입력 값과 '새 비밀번호 확인' 입력 값 이 다른경우 
				toast.push({body:'두 개의 값이 다르게 입력되었습니다.', type:'Warning'});
			}
	});
	$('.find_id').click(function() {
		$(".pop_wrap").show();
		
		$(".pop_bottom_id").css('display', 'inline-block');
		$('.find_info_id_div').css('display', 'inline-block');
		$('.find_id_div').css('display', 'none');
		
		$('.pop_bottom_pwd').css('display', 'none');
		$('.find_pwd_div').css('display', 'none');
	});
	
	$('.find_pwd').click(function() {
		$(".pop_wrap").show();
		
		$(".pop_bottom_id").css('display', 'none');
		$('.find_id_div').css('display', 'none');
		
		$('.pop_bottom_pwd').css('display', 'inline-block');
		$('.find_info_pwd_div').css('display', 'inline-block');
		$('.find_pwd_div').css('display', 'none');
	});
	
});
function ajaxClose(){
	$(".layer_popup_box .ajaxBox").empty();
	$(".layer_popup_box").hide();
	$(".bg").hide();
}
</script>
<style>
	body { background-color: #323c4d !important; }
</style>

<!-- 팝업창 start -->
	<div class="pop_wrap">
		<div class="pop_common">
			<span style="color: #f6f6f6 !important;">아이디/비밀번호 찾기<p id="RsltTblAjax"></p></span>
		</div>
	
		<!-- 아이디 찾기 팝업창 -->	
		<div class="pop_bottom_id">
			<form id="searchId" name="searchId" method="post">
			<input type="hidden" name="cmd" value="default" />
			<div class="find_div">
				<div class="find_id fl active">아이디 찾기</div>
				<div class="find_pwd fr">비밀번호 찾기</div>		
			</div>
			<div class="find_info_id_div">
				<div class="find_info_title">본인확인 이메일 주소와 입력한 이메일 주소가 같아야, 인증번호를 받을 수 있습니다.</div>
				<div class="inputbox_1">
					<input type="text" class="input_1 input_common" placeholder="이름을 입력하세요." title="이름 입력창" name="inputNM" id="inputNM" value="${param.inputNM}"/>
				</div>
				<div class="input_2_btn_div" style="margin-bottom:0;">
					<span class="">
						<input type="text" class="input_2 fl input_common" placeholder="이메일을 입력하세요." name="inputEmail" id="inputEmail" value="${requestScope.inputEmail}"  title="이메일 입력창"/>
						<input type="button" name="searchIdAuth" id="searchIdAuth" class="input_btn fr" value="인증번호 발송"  title="인증번호 발송 버튼"/>
					</span>
				</div>
				<div class="input_under_text_r" id="usrEmail_message"></div>
				<div><input type="text" class="input_4 input_common" placeholder="인증번호를 입력하세요."  title="인증번호 입력창" id="inputAuth" name="inputAuth"/></div>
				<div>
					<input type="button" name="idAuthSucc" id="idAuthSucc" class="btn_1" value="확인"  title="확인 버튼"/>
					<input type="button" class="btn_3" value="취소"  title="취소 버튼" onclick="ajaxClose()"/>	
				</div>
			</div>

			<!-- 확인 클릭 후 화면 -->
			<div class="find_id_div">
				찾으신 정보의 아이디는 <span class="find_id_info"></span>입니다.
				<div><input type="button" class="btn_2" value="완료"  title="완료 버튼" onclick="ajaxClose()"/></div>
			</div>
			</form>		
		</div>
		<!-- 아이디 찾기 팝업창 -->
		
		
		<!-- 비밀번호 찾기 팝업창 -->	
		<div class="pop_bottom_pwd">
		<form id="searchPw" name="searchPw" method="post">
			<div class="find_div">
				<div class="find_id fl">아이디 찾기</div>
				<div class="find_pwd fr active">비밀번호 찾기</div>
			</div>
			<div class="find_info_pwd_div">
				<div class="find_info_title">본인확인 이메일 주소와 입력한 이메일 주소가 같아야, 인증번호를 받을 수 있습니다.</div>
				<div class="inputbox_1">
					<input type="text" id="pwInputId" name="pwInputId" class="input_1 input_common" placeholder="아이디를 입력하세요."  title="아이디 입력창"/>
				</div>
				<div class="input_2_btn_div" style="margin-bottom:0;">
					<span class="">
						<input type="text" id="pwInputEmail" name="pwInputEmail" class="input_2 fl input_common" placeholder="이메일을 입력하세요."  title="이메일 입력창"/>
						<input type="button" name="searchPwAuth" id="searchPwAuth" class="input_btn fr" value="인증번호 발송"  title="인증번호 발송 버튼"/>
					</span>
				</div>
				<div class="input_under_text_r" id="usrEmail_message_pw"></div>
				<div><input type="text" id="pwInputAuth" name="pwInputAuth" class="input_4 input_common" placeholder="인증번호를 입력하세요."  title="인증번호 입력창"/></div>
				<div>
					<input type="button" name="pwAuthSucc" id="pwAuthSucc" class="btn_3" value="확인"  title="확인 버튼"/>
					<input type="button" class="btn_3" value="취소"  title="취소 버튼" onclick="ajaxClose()"/>				
				</div>
				
			</div>

			<!-- 확인 클릭 후 화면 -->
			<div class="find_pwd_div">
				<div class="find_info_title">새 비밀번호를 설정해주세요</div>
				<input type="hidden" id="pwHiddenCode" name="pwHiddenCode">
				<div>
					<input type="password" id="pwInputNew" name="pwInputNew" class="input_4 input_common" placeholder="새 비밀번호를 입력하세요."  title="새 비밀번호 입력창" style="border-radius: 1px;"/>
				</div>
				<div>
					<input type="password" id="pwInputNew2" name="pwInputNew2" class="input_4 input_common" placeholder="새 비밀번호를 다시 입력하세요."  title="새 비밀번호 확인 입력창" style="border-radius: 1px;"/>
				</div>
				
				<div>
				<input type="button" name="newPwSucc" id="newPwSucc" class="btn_3" value="재 설정"  title="재 설정 버튼"/>
				<input type="button" class="btn_3" value="취소"  title="취소 버튼" onclick="ajaxClose()"/>
				</div>
			</div>
			</form>
		<!-- 비밀번호 찾기 팝업창 -->
		</div>
	<!-- 팝업창 end -->
	
</div>