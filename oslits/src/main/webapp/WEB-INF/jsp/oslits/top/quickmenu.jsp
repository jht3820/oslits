<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head> 
	<meta charset="UTF-8">
	<!-- 모바일 : viewport 제거-->
	<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
	<title>Open Soft Lab</title>
</head> 
<script>
	/** 
	 * quick menu 세팅 
	 */
	$(document).ready(function(){
		/* 마우스를 클릭했을때 퀵메뉴 나타나기 */
		$(".menu_div, .menu_list_left, #quickBtn_hide").click(function(){
			$(".quick_menu").toggleClass("wh");
			$(".menu_div, .menu_list_left").next(".menu_info, .menu_list_right").toggleClass("dh");
			$('#quickBtn_show').toggle();
			$('#quickBtn_hide').toggle();
		});
		
		/* 마우스가 오버되었을 때 퀵메뉴 나타나기
		$(".menu_div, .menu_list_left, .quick_menu").hover(function(){
			$(".menu_div, .menu_list_left").next(".menu_info, .menu_list_right").toggleClass("dh");
			$(".quick_menu").toggleClass("wh");
		});
		*/
	});
	
	//로그아웃 처리
	function fnGoLogout(){
		jConfirm("로그아웃 하시겠습니까?", "알림창", function( result ) {
			if( result ){
				$(location).attr('href',"/cmm/cmm4000/cmm4000/selectCmm4000Logout.do");
			}
		});
	}
	//사용자 정보수정 이동
	function fnGoPrs3000(){
		$(location).attr('href',"/prs/prs3000/prs3000/selectPrs3000View.do");
	}
	
</script>
<body>	
	<div class="quick_menu wh">
		<div id="quickBtn_hide">
			<span class="quickBtnLogOutSpan">◀</span>
		</div>
		<div id="quickBtn_show" style="display:none;">
			<div id="quickBtn_left" onclick="javascript:fnGoLogout();">
				<img alt="로그아웃" src="/images/quickmenu/quick_logout.png" class="m_l_l_img">
			</div>
			<div id="quickBtn_right">
				<span onclick="javascript:fnGoPrs3000();">정보수정</span>
			</div>
		</div>
		<div class="menu_div">
			M<br/>Y<br/>I<br/>N<br/>F<br/>O<br/>
		</div>
		<div class="menu_info dh">
			<c:choose>
				<c:when test="${sessionScope.loginVO.usrImgId ne null}">
					<div><img src="<c:url value="/cmm/fms/getImage.do?fileSn=0&atchFileId=${sessionScope.loginVO.usrImgId}" />" class="user_img" /></div>
				</c:when>
				<c:otherwise>
					<img alt="사용자 사진" src="/images/contents/sample.png" class="user_img">
				</c:otherwise>
			</c:choose>
			<div class="info_right">
				<div>이름 / ID</div>
				<div class="font_bold">${sessionScope.loginVO.usrNm} / ${sessionScope.loginVO.usrId}</div>
			</div>
			
		</div>
		
		<div class="menu_list_left">
			<img alt="라이선스그룹" src="/images/quickmenu/quick_user.png" class="m_l_l_img">
		</div>
		<div class="menu_list_right dh">
			<div>라이선스그룹</div>
			<div class="font_bold">${sessionScope.loginVO.licGrpId}</div>
		</div>
		
		<div class="menu_list_left">
			<img alt="라이선스그룹" src="/images/quickmenu/quick_book.png" class="m_l_l_img">
		</div>
		<div class="menu_list_right dh">
			<div>연락처</div>
			<div class="font_bold">${sessionScope.loginVO.telno}</div>
		</div>
		
		<div class="menu_list_left">
			<img alt="라이선스그룹" src="/images/quickmenu/quick_email.png" class="m_l_l_img">
		</div>
		<div class="menu_list_right dh">
			<div>이메일</div>
			<div class="font_bold">${sessionScope.loginVO.email}</div>
		</div>
		
		<div class="menu_list_left">
			<img alt="라이선스그룹" src="/images/quickmenu/quick_clock.png" class="m_l_l_img">
		</div>
		<div class="menu_list_right dh">
			<div>기준근무시간</div>
			<div class="font_bold">${sessionScope.loginVO.wkStTm}시 ~ ${sessionScope.loginVO.wkEdTm}시</div>
		</div>
	</div>
	
</body>
</html>