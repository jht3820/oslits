<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="ko" xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/css/common/reset.css" type="text/css" />
<title>Error Page</title>
<style>
	<c:choose>
		<c:when test="${!empty popupChk && popupChk == 'Y'}">
			.layer_popup_box .ajax_box{
		</c:when>
		<c:otherwise>
			body{
		</c:otherwise>
	</c:choose>	
	background-image: url(/images/error_bg.png);
    background-position: center center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    background-size: cover;
  	}
  	.contents_wrap{text-align:center; width:100%; }
  	.contents_wrap .errorBg{
  		position:absolute;
  		left: calc(50% - 257.5px);
  		background-image: url(/images/error_contents.png);
  		width:515px;
  		height:668px;
  		background-repeat:no-repeat;
  		background-position:50% 50%;
  	}
  	.contents_text{color:#48532e; position:relative; left:calc(54% - 257.5px); top:426px; font-size:33px;}
  	.contents_btn{position:relative; left:calc(50% - 257.5px); top:176px; font-size:33px; width:135px; height:135px; cursor:pointer; border-radius:50%;}
</style>
<script>
	//팝업 or 에러페이지 이전페이지 기능
	function fnErrorhistoryBack(){
		if("${popupChk}" == 'Y'){
			gfnLayerPopupClose();
		}else{
			window.history.back();
		}
	}
		
</script>
</head>
<body>

 <div class="error_page">
 	<div class="contents_wrap">
 		<div class="errorBg">
 			<div class="contents_text">
 				<c:if test="${empty requestScope.exception.message && empty exceptionMessage}">
 					오류 <br/> 페이지
 				</c:if>
 				<c:if test="${!empty requestScope.exception.message}">
 					<br/>${requestScope.exception.message }
 				</c:if>
 				<c:if test="${!empty exceptionMessage}">
 					<br/>${exceptionMessage}
 				</c:if>
 			</div>
 			
 			<!-- 이전페이지로  -->
 			<div class="contents_btn" onclick="fnErrorhistoryBack();">
 				<!-- 경로 -->
 			</div>
 			<!-- 이전페이지로  -->
 			
 		</div>

	</div>
</div>
</body>
</html>



<!--         <font color="red"> -->
<!--         	문의 : xxx-xxxx-xxxx(누구누구)<br><br> -->
<%--         	${requestScope.exception.message }<br> --%>
<!--         </font> -->