<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8">
	<!-- 모바일 : viewport 제거-->
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<!-- <meta http-equiv="X-UA-Compatible" content="IE=11"> -->
	<!-- <meta http-equiv="x-ua-compatible" content="IE=EmulateIE10"> -->
	<title>Open Soft Lab</title>
	
	<!-- 파비콘 -->
	<link rel="apple-touch-icon" sizes="180x180" href="<c:url value='/images/favicon/apple-touch-icon.png'/>">
	<link rel="icon" type="image/png" sizes="32x32" href="<c:url value='/images/favicon/favicon-32x32.png'/>">
	<link rel="icon" type="image/png" sizes="16x16" href="<c:url value='/images/favicon/favicon-16x16.png'/>">
	<link rel="manifest" href="<c:url value='/images/favicon/site.webmanifest'/>">
	<link rel="mask-icon" href="<c:url value='/images/favicon/safari-pinned-tab.svg'/>" color="#5bbad5">
	<meta name="msapplication-TileColor" content="#da532c">
	<meta name="theme-color" content="#ffffff">
	
	
	<link rel="stylesheet" href="<c:url value='/css/common/common.css'/>">
	
	<%-- <link rel="stylesheet" href="<c:url value='/css/font-awesome/font-awesome.css'/>"> --%>
	<link rel="stylesheet" href="<c:url value='/css/font-awesome/all.css'/>">
	<link rel="stylesheet" href="<c:url value='/css/font-awesome/v4-shims.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/axisj/ui/osl/ax5grid.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/axisj/ui/osl/ax5menu.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/axisj/ui/osl/ax5dialog.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/axisj/ui/osl/ax5select.css'/>">
	
	
	<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>" ></script>
	
	<script type="text/javascript" src="<c:url value='/js/axisj/dist/ax5/ax5core.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/axisj/dist/ax5/ax5menu.min.js'/>"></script>
	
	<!--   보고서 출력을 위한 script    
	html2canvas.js html 영역을 이미지로 축출
	jspdf.min.js pdf 라이브러리
	bluebird.min.js html2canvas IE 사용불가 이슈 해결 라이브러리
	   -->
	<script type="text/javascript" src="<c:url value='/js/html2canvas/html2canvas.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/jspdf/jspdf.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/bluebird/bluebird.min.js'/>"></script>
	
	<!-- Sortable - 배포 쪽에서 사용 추후 대시보드에서 사용 예정 -->
	<script type="text/javascript" src="<c:url value='/js/sortable/Sortable.js'/>"></script>
	
	<%-- <script type="text/javascript" src="<c:url value='/js/axisj/dist/ax5/ax5dialog.min.js'/>"></script> --%>
	<script src="/js/axisj/lib/mask/ax5mask.min.js"></script>
	<link href="/css/axisj/ui/osl/ax5mask.css" rel="stylesheet">
	<script type="text/javascript" src="<c:url value='/js/axisj/dist/ax5/ax5grid.js'/>"></script>
	<script src="<c:url value='/js/common/common.js'/>" ></script>
	
	<!-- select2 -->
	<link href="/vendors/select2/css/select2.css" rel="stylesheet">
	
	<style type="text/css">
	.menu_b_two {display: none;}

	.bm10p {margin-bottom :10px;}
	/* 더보기 버튼 아이콘 */
	.more_box_btn_icon{padding: 15px 14px 14px 14px;}
	</style>
	<script>
	/* 공통 버튼 권한 전역 변수 */
	var btnAuthSearchYn;
	var btnAuthInsertYn;
	var btnAuthUpdateYn;
	var btnAuthDeleteYn;
	var btnAuthExcelYn;
	var btnAuthPrintYn;
	
	//팝업 크기
	var popupWidth = 1800;
	var popupHeight = 980;
	
	function headerPrjListSetting(){
		//프로젝트 목록 select2
		$("#header_select").select2({
			templateResult: function(state) {
				var returnFlag = true;
				
				//disabled아닌 경우에만 출력
				if(!gfnIsNull(state.element) && !gfnIsNull(state.element.disabled) && state.element.disabled == true){
					returnFlag = false;
				}
				
				if(returnFlag){
					return state.text;
				}
			}
		});
	}
		$(document).ready(function(){
			//프로젝트 목록 select2
			headerPrjListSetting();
			
			// 사용자 비밀번호 만료일
			var pwExpireDay = '<c:out value="${sessionScope.limitDay}"/>';

			// 사용자 비밀번호 만료일 체크 - 7일보다 작으면 경고창 띄움
			if(pwExpireDay <= 7){
				fnPwChangeDay(pwExpireDay);
			}
			// 사용자 접속 IP정보
			// 이전 접속 IP
			var beforeIp = '<c:out value="${sessionScope.userAccessIpInfoList[1].loginIp}"/>';
			// 현재 접속 IP
			var currentIp = '<c:out value="${sessionScope.userAccessIpInfoList[0].loginIp}"/>';
			
			// 접속 IP주소가 다를경우 Alert 띄움
			if( !gfnIsNull(beforeIp) && !gfnIsNull(currentIp) && beforeIp != currentIp){
				fnAccessIpChk(beforeIp, currentIp);	
			}
			
			//윈도우 객체
			if (window.screen) {
				popupWidth = window.screen.availWidth;
				popupHeight = window.screen.availHeight;
			}
			$('.popup_osl').click(function(){
				window.open('<c:url value="/dsh/dsh3000/dsh3000/selectDsh3000View.do"/>','통합 대시보드','width='+popupWidth+', height='+popupHeight+', menubar=no, status=no, toolbar=no, location=no, scrollbars=yes, resizable=1');
			});
			
			
			$('.menu_b').click(function(){
				$(".menu_b").css('background' , '#fff');
				$(".menu_b_two").hide();
				$(this).css('background' , '#f9f9f9');
				
				var selObj = $(this);
				
				var $currId = $(this).attr('id');
				$("."+ $currId).find(".menu_b_triangle").css({"left":selObj.offset().left+20});
				$("."+ $currId).find(".menu_b_rect").css({"left":selObj.offset().left+8});
				if( !( $("#requestMng").length || $("#sprintReqMapping").length ) ){
					$("."+ $currId).css({"position":"absolute","top":'405px',"left":"0"}).show();								
				} else {
					$("."+ $currId).css({"position":"absolute","top":'500px',"left":"0"}).show();			
				}
				$('.one_wrap.parent').click(function(){
					$("."+ $currId).hide();
				});
			});
			
			/* 공통 버튼 권한 부여 */
			btnAuthSearchYn = '${sessionScope.selBtnAuthSearchYn}';
			btnAuthInsertYn = '${sessionScope.selBtnAuthInsertYn}';
			btnAuthUpdateYn = '${sessionScope.selBtnAuthUpdateYn}';
			btnAuthDeleteYn = '${sessionScope.selBtnAuthDeleteYn}';
			btnAuthExcelYn  = '${sessionScope.selBtnAuthExcelYn}';
			btnAuthPrintYn  = '${sessionScope.selBtnAuthPrintYn}';
			
			btnAuthSearchYn == 'Y' ? $("[id^=btn_search]").show() : $("[id^=btn_search]").hide();
			btnAuthInsertYn == 'Y' ? $("[id^=btn_insert]").show() : $("[id^=btn_insert]").hide();
			btnAuthUpdateYn == 'Y' ? $("[id^=btn_update]").show() : $("[id^=btn_update]").hide();
			btnAuthDeleteYn == 'Y' ? $("[id^=btn_delete]").show() : $("[id^=btn_delete]").hide();
			btnAuthExcelYn 	== 'Y' ? $("[id*=btn_excel]").show() : $("[id*=btn_excel]").hide();
			btnAuthPrintYn 	== 'Y' ? $("[id^=btn_print]").show() : $("[id^=btn_print]").hide();
			
		});
		/** 
		*	공통 버튼 권한 확인
		*	- 검색 상자가 있는 경우 그 안에 버튼들을 함께 검색 (AX로 시작하며 버튼 기능이 id값에 포함된 경우)
		*	조건
		*	- 검색 상자 타겟 DIV의 이름은 'AX'로 시작된다. (추후 변경 가능)
		**/
		function fnBtnAuthCheck(mySearch){
			if(gfnIsNull(mySearch)){
				return false;
			}
			if(btnAuthSearchYn != 'Y'){
				$("[id^=AX][id*='btn_search']").hide();
				// 조회버튼의 부모중 class=searchItem 를 찾아 hide 시킴
				// 부모를 찾으면 class=searchItem을 가진 div 한개만 나옴
				$("[id^=AX][id*='btn_search']").parents(".searchItem").hide();
			}
			if(btnAuthInsertYn != 'Y'){
				$("[id^=AX][id*='btn_insert']").hide();
				// 등록버튼의 부모중 class=searchItem 를 찾아 hide 시킴
				// 부모를 찾으면 class=searchItem을 가진 div 한개만 나옴
				$("[id^=AX][id*='btn_insert']").parents(".searchItem").hide();
			}
			if(btnAuthUpdateYn != 'Y'){
				$("[id^=AX][id*='btn_update']").hide();
				// 수정버튼의 부모중 class=searchItem 를 찾아 hide 시킴
				// 부모를 찾으면 class=searchItem을 가진 div 한개만 나옴
				$("[id^=AX][id*='btn_update']").parents(".searchItem").hide();
			}
			if(btnAuthDeleteYn != 'Y'){
				$("[id^=AX][id*='btn_delete']").hide();
				// 삭제버튼의 부모중 class=searchItem 를 찾아 hide 시킴
				// 부모를 찾으면 class=searchItem을 가진 div 한개만 나옴
				$("[id^=AX][id*='btn_delete']").parents(".searchItem").hide();
			}
			if(btnAuthExcelYn != 'Y'){
				$("[id^=AX][id*='btn_excel']").hide();
				// 엑셀버튼의 부모중 class=searchItem 를 찾아 hide 시킴
				// 부모를 찾으면 class=searchItem을 가진 div 한개만 나옴
				$("[id^=AX][id*='btn_excel']").parents(".searchItem").hide();
			}
			//if(btnAuthPrintYn != 'Y'){
				$("[id^=AX][id*='btn_print']").hide();
				// 프린트버튼의 부모중 class=searchItem 를 찾아 hide 시킴
				// 부모를 찾으면 class=searchItem을 가진 div 한개만 나옴
				$("[id^=AX][id*='btn_print']").parents(".searchItem").hide();
			//}
		}
	</script>
	
	<script>
		//상단 및 더보기 메뉴 클릭시 메뉴페이지 이동
		function fnGoMenu(menuUrl, menuId, menuNm, menuTypeCd){
			if(menuTypeCd != null && menuTypeCd != "" && menuTypeCd == "03"){
				
				window.open('<c:url value="'+menuUrl+'"/>',menuNm ,'width='+popupWidth+', height='+(popupHeight-100)+', menubar=no, status=no, toolbar=no, location=no, scrollbars =yes');
			}else{
				document.hidPrjForm.menuUrl.value = menuUrl;
				document.hidPrjForm.menuId.value = menuId;
				document.hidPrjForm.menuNm.value = menuNm;
				document.hidPrjForm.action="<c:url value='/cmm/cmm9000/cmm9000/selectCmm9000MenuChgView.do'/>";
				document.hidPrjForm.submit();
			}
		}
		
		//프로젝트 선택시 프로젝트에 해당하는 권한 체크하여 메인페이지 이동
		function fnGoPrjChg(thisObj){
			var prjId = thisObj.value;
			document.hidPrjForm.prjId.value = prjId;
			document.hidPrjForm.action="<c:url value='/cmm/cmm9000/cmm9000/selectCmm9000PrjChgView.do'/>";
			document.hidPrjForm.submit();
		}
		
		/**
		 * 사용자 비밀번호 만료일 체크
		 * @param pwlimitDay 비밀번호 만료일
		 */
		function fnPwChangeDay(pwExpireDay){

			// 비밀번호 만료일 쿠키가 없을경우 
			if( isCookie("pwExpire") == false ){
			
				if( !gfnIsNull(pwExpireDay) ){
					jAlert("비밀번호 변경주기는 1개월 입니다. \n\n 현재 비밀번호 만료일이 "+ pwExpireDay +"일 남았습니다. \n\n 비밀번호를 변경해 주세요.","알림창", function(result) {
						if (result) {
							// 비밀번호 만료일 alert이 로그인 시 한번만 나타나게 하기위한 쿠키를 생성
							setCookie("pwExpire", "expire"); 
						}
					});
				}	
			}
		}
		
		/**
		 * 사용자 접속 IP체크
		 * @param beforeIp 이전 접속 IP
		 * @param currentIp 현재 접속 IP
		 */
		function fnAccessIpChk(beforeIp, currentIp){

			// 접속 IP체크 쿠키가 없을경우 
			if( isCookie("accessIp") == false ){
			
				jAlert("이전 접속한 IP주소와 현재 접속한 IP주소가 다릅니다. \n접속한 IP주소를 확인해 주세요.\n\n> 이전 접속 IP : "+beforeIp+"\n> 현재 접속 IP : "+currentIp,"알림창", function(result) {
					if (result) {
						// 접속 IP체크 alert이 로그인 시 한번만 나타나게 하기위한 쿠키를 생성
						setCookie("accessIp", "notEqual"); 
					}
				});
			}
		}
		
		/*
		 * 쿠키의 유무 체크
		 * @param cookieName 쿠키이름
		 */
		function isCookie(cookieName) {
			cookieName = cookieName + '=';
			var cookieData = document.cookie;
			var cIdx = cookieData.indexOf(cookieName);
			var exist = false;	
			
			if(cIdx != -1 ){
				exist = true;	
			}
			
			return exist;
		}
		
		/*
		 * 비밀번호 만료일 alert이 로그인 시 한번만 나타나게 하기위한 쿠키를 생성한다.
		 * @param cookieName 쿠키이름
		 * @param cookieValue 쿠키값
		 */
		function setCookie(cookieName, cookieValue) {   
			var cDate = new Date();
			cDate.setTime(cDate.getTime() + 1000*60*60*1); // 만료시간 1시간
			document.cookie = cookieName + "=" + cookieValue + "; path=/; expires=" + cDate.toGMTString() + ";";
		} 
	</script>	
		
</head>
<body>
	<form name="hidPrjForm" method="post">
		<input type="hidden" name="menuUrl">
		<input type="hidden" name="menuId">
		<input type="hidden" name="menuNm">
		<input type="hidden" name="prjGrpId">
		<input type="hidden" name="prjId">
	</form>

	<header>
		<link rel="stylesheet" href="<c:url value='/css/menu/header.css'/>">
		<script src="<c:url value='/js/menu/header.js'/>" ></script>
		
		<div class="gnb">			
			<!-- 상단부분과 GNB -->
			<div class="gnb_top">
				<a href="javascript:fnGoMenu('/cmm/cmm9000/cmm9000/selectCmm9000MainMove.do', '000', '${sessionScope.firstMenuNm }','01')" title="메인으로 가기"><div class="logo_box"><img class="logo" src="/images/header/logo/logo_S_osl.png" alt="로고"></div></a>
				
				<div style="cursor:pointer;" class="osl_btn popup_osl"><img src="/images/header/topMenu/osl_btn_b.png" alt="osl"></div>
				<c:if test="${!empty sessionScope.menuList }">
					<c:forEach items="${sessionScope.menuList }" var="map">
						<c:if test="${map.lvl == '1' && map.accessYn == 'Y' && map.menuId < '000700000000' }">
							<div class="one_menu"><img src="${map.menuImgUrl}" alt="${map.menuNm }" />${map.menuNm }</div>
						</c:if>
					</c:forEach>
				</c:if>
				
				<!-- 더보기 버튼 -->
				<div class="more_box_btn" style="text-align:center;" ><i class="fas fa-cog fa-2x more_box_btn_icon"></i></div>
				<div class="more_box_wrap">
					<div class="triangle"></div>
					<div class="rect"></div>
					
					<!-- 더보기 메뉴들 -->
					<c:if test="${!empty sessionScope.menuList }">
						<!-- 대메뉴 반복 -->
						<c:set var="sum" value="1"/>
						<c:forEach items="${sessionScope.menuList }" var="map1">
							<c:if test="${map1.lvl == '1' && map1.accessYn == 'Y' && map1.menuId >= '000700000000' }">
								<div class="more_wrap">	
									<div class="more_one"><img src="${map1.menuImgUrl }" alt="${map1.menuNm }">${map1.menuNm }</div>
									<div class="more_two_wrap" style="">
										<!-- 중메뉴 반복 -->
										<c:forEach items="${sessionScope.menuList }" var="map2">
											<c:if test="${map2.lvl == '2' && map2.accessYn == 'Y' && map2.menuId >= '000700000000' }">
												<c:if test="${map2.lvl == '2' && map2.upperMenuId == map1.menuId}">
													<div class="more_two">${map2.menuNm }</div>
													<!-- 소메뉴 반복 -->
													<c:forEach items="${sessionScope.menuList }" var="map3">
														<c:if test="${map3.lvl == '3' && map3.accessYn == 'Y' && map3.upperMenuId == map2.menuId}">
															<a href="javascript:fnGoMenu('${map3.menuUrl}', '${map3.menuId}', '${map3.menuNm}', '${map3.menuTypeCd}' )"><div class="more_three">${map3.menuNm }</div></a>
														</c:if>
													</c:forEach>
													<!-- 소메뉴 반복종료 -->
												</c:if>
											</c:if>
										</c:forEach>
										<!-- 중메뉴 반복종료 -->
										<c:if test="${sum eq 1}">
											<!-- 글자크기 조절 -->
											<div style="margin-bottom: 30px;"></div>
											<div class="font_resize_wrap">글자크기<span></span><span></span></div>
										</c:if>
										<c:set var="sum"  value="${sum + 1}"/>
									</div>
								</div>
							</c:if>
						</c:forEach>
						<!-- 대메뉴 반복종료 -->
					</c:if>
					
				</div>
				
				<!-- 프로젝트 선택 -->
				<div class="prj_select_box" style="width: 330px;">
					<select class="header_select hdsel" id="header_select" onchange="fnGoPrjChg(this)">
						<c:if test="${!empty sessionScope.prjList }">
							<c:forEach items="${sessionScope.prjList }" var="map" varStatus="status">
								<c:if test="${map.prjGrpCd == '01'}" >
									<c:if test="${map.leaf == 0}">
										<optgroup label="[그룹]<c:out value='${map.prjNm}' />" data-ord="${map.ord}" value="${map.prjId}">
									</c:if>
									<c:if test="${map.leaf > 0}">
										<optgroup label="[그룹]<c:out value='${map.prjNm}' />" data-ord="${map.ord}" value="${map.prjId}" disabled="disabled">
									</c:if>
									<c:forEach items="${sessionScope.prjList }" var="map2" varStatus="status">
										<c:if test="${map.prjId == map2.prjGrpId}">
											<c:if test="${sessionScope.selPrjId == map2.prjId}" >
												<option selected value="${map2.prjId}"><c:out value='${map2.prjNm}' /></option>
											</c:if>
											<c:if test="${sessionScope.selPrjId != map2.prjId}" >
												<c:if test="${map2.prjGrpCd == '02'}">
														<option value="${map2.prjId}" data-ord="${map2.ord}"><c:out value='${map2.prjNm}' /></option>
												</c:if>
											</c:if>
										</c:if>
									</c:forEach>
									</optgroup>
								</c:if>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</div>

			<!-- 2depth 시작 -->
			<div class="gnb_bottom">
				<div class="menu_img"><img src="/images/header/topMenu/osl_Defense.png" alt="Oslits_Defense"></div>
				<nav>
					<c:if test="${!empty sessionScope.menuList }">
						<!-- 대메뉴 반복 -->
						<c:forEach items="${sessionScope.menuList }" var="map1">
							<c:if test="${map1.lvl == '1' && map1.accessYn == 'Y' && map1.menuId < '000700000000' }">
								<div class="two_menu_wrap">
									<!-- 중메뉴 반복 -->	
									<c:forEach items="${sessionScope.menuList }" var="map2">
										<c:if test="${map2.lvl == '2' && map2.accessYn == 'Y' && map2.menuId < '000700000000' }">
											<c:if test="${map2.lvl == '2' && map2.upperMenuId == map1.menuId}">
												<div class="two_menu">${map2.menuNm }</div>
												<!-- 소메뉴 반복 -->
												<c:forEach items="${sessionScope.menuList }" var="map3">
													<c:if test="${map3.lvl == '3' && map3.accessYn == 'Y' && map3.upperMenuId == map2.menuId}">
														<!-- 메뉴타입이 연계인경우, 연계 프로젝트 있는 경우에만 오픈 -->	
														<c:choose>
															<c:when test="${map3.menuTypeCd == '04'}">
																<c:if test="${sessionScope.pairCnt > 0}">
																	<a href="javascript:fnGoMenu('${map3.menuUrl}', '${map3.menuId}', '${map3.menuNm}','${map3.menuTypeCd}')"><div class="three_menu">${map3.menuNm }</div></a>
																</c:if>
															</c:when>
															<c:otherwise>
																<a href="javascript:fnGoMenu('${map3.menuUrl}', '${map3.menuId}', '${map3.menuNm}','${map3.menuTypeCd}')"><div class="three_menu">${map3.menuNm }</div></a>	
															</c:otherwise>
														</c:choose>
														
													</c:if>
												</c:forEach>
												<!-- 소메뉴 반복종료 -->
												
											</c:if>
										</c:if>
									</c:forEach>
									<!-- 중메뉴 반복종료 -->
								</div>
							</c:if>
						</c:forEach>
						<!-- 대메뉴 반복종료 -->
					</c:if>
				</nav>
			</div>
		</div>
		
		<!-- 모바일 메뉴 이미지 관리 객체 
		<jsp:useBean id="mobileImgUrl" class="java.util.HashMap" >
			<c:set target="${mobileImgUrl}" property="imgList" 
			value="${fn:split('
			 /images/header/GNB/GNB-icon_01.png
			,/images/header/GNB/m_GNB_con_03.png
			,/images/header/GNB/GNB-icon_04.png
			,/images/header/GNB/GNB-icon_08.png
			,/images/header/GNB/m_GNB_con_02.png
			,/images/header/GNB/m_GNB_con_01.png		', ',')}"/>
			
			<c:set target="${mobileImgUrl}" property="imgList2" 
			value="${fn:split('
			 /images/header/GNB/GNB-icon_01.png
			,/images/header/GNB/m_GNB_con_03.png
			,/images/header/GNB/GNB-icon_04.png
			,/images/header/GNB/GNB-icon_08.png
			,/images/header/GNB/m_GNB_con_02.png
			,/images/header/GNB/m_GNB_con_01.png		', ',')}"/>
		</jsp:useBean>	
		-->
	</header>
