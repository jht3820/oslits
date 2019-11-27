<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel="stylesheet" href="<c:url value='/css/oslits/index.css'/>">
<script src='/js/common/layerPopup.js'></script>

<script>
$(function(){	
	/* 테스트용 */
	$("button.ajax").click(function(){
		layer_popup("dsh/dsh1000/dsh1001/dsh1001.jsp.do");
	});
	
	setTitle("테스트");
});
</script>

<div class="main_contents">
<%@ page import="java.util.Date" %>
<%
	Date now = new Date();
%>
	<div class="date_box">
		<h1>Hello world!</h1>
		현재 시각은 <%=now%> 입니다.
	</div>
	<button class="ajax">ajax</button>

</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />