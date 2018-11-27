<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<!-- 모바일 viewprot 제거 -->
	<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
	<title>Open Soft Lab</title>
	
	<link rel="stylesheet" href="<c:url value='/css/common/common.css' />" type="text/css" />
	
	<link rel="stylesheet" href="<c:url value='/css/oslits/login.css' />" type="text/css" />
	<link rel="stylesheet" href="<c:url value='/css/oslits/cmm.css'/>" type="text/css" />
	
	<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>"></script>

	<script src="<c:url value='/js/common/common.js'/>"></script>
	<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
	<script src="<c:url value='/js/common/comOslits.js'/>"></script>
	
	<script>
		$(function(){
			/* 레이어 팝업 띄우기 */
			$('.find').click(function() {
				//레이어팝업 공통함수 이용 호출할 것.
				layer_popup('/cmm/cmm4000/cmm4001/selectCmm4001View.do', 'idPwFind');
			});
		});

		
		// enter cross browsing ( 엔터키 처리 )
		// Firefox에서 event객체를 처리하기 위해서는 argument로 event객체를 넘겨야함.
		document.onkeypress = function(e){

			var result = "";
			var usrId = $("#usrId").val();
			var usrPw = $("#usrPw").val();
			 
		      if(typeof(e) != "undefined"){
		         result = e.which;
		     } else {
		         result = event.keyCode;
		     }
		     
		      if(document.getElementsByClassName('pop_wrap').length){ // 팝업화면이 있으면 enter key 안먹이게 함
		    	return;  
		      }
		      if( result == "13"){
		    	  if( usrId != "" || usrPw ==""){
		    		  $("#usrPw").focus();
		    	  }
		    	  if( usrId != "" && usrPw !=""){
		    		  fnLoginAction();
		    	  }
		      }
		}
		
		//최초 초기화
		function fnInit(){
			var loginYn = '${requestScope.loginYn}';
			var isPrjYn = '${requestScope.isPrjYn}';
			var logoutYn = '${requestScope.logoutYn}';
			var message = '${requestScope.message}';
			var sessionYn = '${requestScope.sessionYn}';
			var iniYn  = '${requestScope.iniYn}';  
			var loginSessionYn = '${requestScope.loginSessionYn}';

			if(iniYn == 'Y'){
				
				var data = { "usrId" : '${requestScope.usrId}'  };
				gfnLayerPopupOpen('/cmm/cmm4000/cmm4002/selectCmm4002View.do', data , '550', '180','scroll');
			}			
			
			//세션이 만료된 경우 세션 만료 메시지 띄움.
			if(sessionYn == 'N'){
				jAlert('${requestScope.message}','알림창');
			}
			
			/* 로그인 여부, 라이선스 활성화 여부, 프로젝트 존재여부가 N일 경우 실패 이유 얼럿 */
			if(loginYn == "N" || isPrjYn == "N"){
				jAlert('${requestScope.message}','알림창');				
			}
			//로그아웃인 경우 alert창 안 띄우고 toast 메시지 처리
			if(logoutYn == 'Y'){
				toast.push('${requestScope.message}');
			}
			
		
			if(loginSessionYn == 'Y'){
				if(confirm("${requestScope.message}")){
					$('#loginSessionYn').val('${requestScope.loginSessionYn}');
					$('#usrId').val('${requestScope.usrId}');
					$('#usrPw').val('${requestScope.usrPw}');
					fnLoginAction();
				}
			}
			
			$("#usrId").focus();	// 아이디 입력란 key focus
		}
		
		//로그인 처리
		function fnLoginAction(){
			document.loginFrm.action = "<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000LoginAction.do'/>";
			document.loginFrm.submit();
		}
		
		
	</script>
</head>

<body onload="fnInit();">
	<div id="center">
		<form id="loginFrm" name="loginFrm" method="post">
			<input type="hidden" name="loginSessionYn" id="loginSessionYn" >
			<input type="hidden" name="initPassYn" id="initPassYn" value="">
			<input type="hidden" name="licGrpId" id="licGrpId" value="${requestScope.licGrpId}">
			
			<img class="logo" src="/images/login/login_logo_osl.png"/> <!--  alt="국방전산정보원 로고" -->
			<!-- <div class="div_title_txt">SW형상관리시범체계</div> -->
			<input type="text" name="usrId" id="usrId" placeholder="Username" title="로그인" 
			onkeyup="this.value=this.value.replace(/[^a-zA-Z-_0-9]/g,'');" maxlength="20"
			>
			<input type="password" name="usrPw" id="usrPw" placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;" style="margin-bottom:20px;" title="패스워드" 
			maxlength="200"
			>
			<a href="javascript:fnLoginAction();"><span class="login_button">Log In</span></a>	
			<div class="join_find">
					<c:if test="${not empty joinCheck and joinCheck eq 'Y'?true:false}">
						<a href="<c:url value='/cmm/cmm3000/cmm3000/selectCmm3000View.do'/>" class="join">
						<span>회원가입</span></a>
						<span class="sbar"></span>
						<span class="find">아이디/비밀번호 찾기</span>
					</c:if>
			</div>
		</form>
	</div>
</body>
</html>