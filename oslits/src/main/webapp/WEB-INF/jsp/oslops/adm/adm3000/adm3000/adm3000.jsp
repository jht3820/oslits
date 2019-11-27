<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/adm.css'/>' type='text/css'>

<script>

</script>

<div class="main_contents">
	<div class="adm3000_title">${sessionScope.selMenuNm }</div>
	
	<div class="adm3000_title_td">
		<div class="title_td1">라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시</div>
		<div class="title_td2">
			라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시<br>
			라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시
		</div>
		<div class="title_td3">라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시 및 라이선스 등록 방법</div>
	</div>
		
	<div class="adm3000_sub_td">
		<div class="sub_left_td">
			<div class="left_td_title">라이선스</div>
			<div class="left_td_sub">License Type - Demo</div>
		</div>
		
		<div class="sub_righ_td">
			<div class="subbox">
				<div class="righ_td_title">그룹 ID</div><div class="righ_td_sub">gruopldkey1000</div>
				<div class="righ_td_title">그룹 이름</div><div class="righ_td_sub">OpenSoftLab</div>
			</div>
			<div class="subbox">
				<div class="righ_td_title">구매 날짜</div><div class="righ_td_sub">2015-12-21</div>
				<div class="righ_td_title">만료 날짜</div><div class="righ_td_sub">2015-12-27<span class="red_font">(+7일)</span></div>
			</div>
			<div class="subbox">
				<div class="righ_td_title2">사용자 제한</div><div class="righ_td_sub2 fl">1/10</div>
			</div>
			<div class="button_complete btn_com">구매</div>	
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />