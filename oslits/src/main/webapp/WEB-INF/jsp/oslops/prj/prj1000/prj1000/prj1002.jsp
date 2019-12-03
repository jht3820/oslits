<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<title>OpenSoftLab</title>

<style>
	.check_prj{padding:31px 25px !important;}
	.checking{color:#4b73eb;padding:5px 0px; display:inline-block;}
	.dup_btn { font-size: 0.95em; }
	.ui-datepicker .ui-datepicker-title{font-size:0.85em;}
</style>

<link rel="stylesheet" href="<c:url value='/css/common/common.css'/>">
<link rel='stylesheet' href='<c:url value='/css/oslops/prj.css'/>' type='text/css'>

<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>" ></script>

<script src="<c:url value='/js/common/common.js'/>" ></script>

<script>
var type = "${type}";
$(document).ready(function() {
	//프로젝트 그룹 생성 높이
	var popupHeight = "580";
								
	//프로젝트 생성 높이
	if(type == "group"){
		popupHeight = "380";
	}
	var data = {type: type};
	gfnLayerPopupOpen('/prj/prj1000/prj1000/selectPrj1003View.do', data, '761', popupHeight,'auto');
});
</script>

</html>