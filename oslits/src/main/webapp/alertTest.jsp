<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Open Soft Lab</title>
	
	<link rel="stylesheet" href="<c:url value='/css/common/common.css'/>">

	<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>" ></script>
	<script src="<c:url value='/js/common/common.js'/>" ></script>
	
<script type="text/javascript">
$(document).ready(function() {	
	
	/* 단순 ALERT 창. STOP 기능은 없음.*/
	$("#btnAlert1").click(function() {
		jAlert("alert 테스트1", "알림창");
	});
	
	/* ALERT 창. STOP 기능. */
	$("#btnAlert2").click(function() {
		jAlert("저장되었습니다.", "알림창", function( result ){
			if( result ){	// 확인 버튼 클릭 및 esc 누를 경우
				$('#testFrm').attr({action:'/alertTest.jsp', method:'post'}).submit();
			}
		});
	});
	
	/* confrim 창 */
	$("#btnConfirm").click(function() {
		jConfirm( '선택 하시겠습니까?', '알림창', function( result ) {
			if( result ){	// 확인 버튼 클릭
				jAlert("OK 선택", "알림창");
			}else{			// 취소버튼 클릭
				jAlert("Cancle 선택", "알림창");
			}
		});
	});
	
});
</script>

<div class="main_contents">
	
	<form id="testFrm" name="testFrm" method="post">
	
	<div class="txt_div" style="text-align: center;" >
		<div class="btn_box" > 
			<span class="button_normal2" id="btnAlert1" style="margin-top: 100px;">Alert</span>
			
			<span class="button_normal2" id="btnAlert2" style="margin-top: 100px;">저장후 페이지 이동</span>
			
			<span class="button_normal2" id="btnConfirm" style="margin-top: 100px;">Confirm</span>
		</div>
	</div>
	
	</form>
	
</div>

</head>
</html>

