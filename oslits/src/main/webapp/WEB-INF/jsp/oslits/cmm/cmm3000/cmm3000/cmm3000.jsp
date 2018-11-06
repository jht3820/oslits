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
		.join_wrap{position:absolute; top:48px; left:50%; width:580px; height:500px; overflow:hidden; margin-left:-290px;}	
			
			.join_footer{float:left; width:100%; border-top:1px solid #ddd; height:51px; line-height:51px; text-align:center; font-size:11px;}
	</style>
	<body style="background-color:#e5e9ec; width:100%; height:100%;">
		<div class="join_wrap">
			<div class="header_img">
				<!-- <img src="/images/login/login_logo2.png" alt="OpenSoftLab 로고" />  -->
			</div>
			<div class="join_box_wrap">
				<div class="join_box">
					<div class="join_header">
						<span class="join_header_l"></span><span class="join_header_r"></span>			
					</div>	
					<div class="center_img">
						<img src="/images/login/smile.png" alt="일반회원가입"/>	
					</div>
					<div class="join_bold_text">일반 회원</div>
					<div class="join_text">일반 회원가입 안내메세지 입니다.</div>
					<form>
						<div class="join_btn_box">
							<div class="join_button"><a href="<c:url value='/cmm/cmm3000/cmm3100/selectCmm3100View.do'/>">회원가입</a></div>		
						</div>
					</form>
				</div>	
				
				<!-- 라이센스 회원 -->
				<!-- <div class="join_box">
					<div class="join_header">
						<span class="l_join_header"></span><span class="r_join_header"></span>	
					</div>
					<div class="center_img" style="height:53px;">
						<img src="/images/login/license.png" alt="라이센스회원"/>	
					</div>
					<div class="join_bold_text">라이센스 회원</div>
					<div class="join_text">라이센스 회원가입 안내메세지 입니다.</div>
					<form>
						<div class="join_btn_box">
							<div class="join_button">회원가입</div>
						</div>
					</form>
				</div> -->
		
			</div>
			<div class="join_footer">Copyright ⓒ<span class="bold">Open Soft Lab</span> Corp. All Rights Reserved.</div>
		</div>				
	</body>
</html>