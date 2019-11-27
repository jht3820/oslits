<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/prs.css'/>' type='text/css'>
<style>
</style>
<script>
	$(document).ready(function(){
	    /*$('div.toggler').click(function(){
	        $(this).toggleClass('off');
	        $(this).prev().toggleClass('off');
	    });*/
	    
	    $('input[type="checkbox"].onoffswitch-checkbox').click(function(){
	    	var chk = $(this).is(":checked");
	        $(this).parent().parent().parent().find('.title_name').toggleClass('off');
	    	if(chk){
	    		//$('.onoffswitch-switch').css('background', '#4b73eb');
	    		$(this).next().find('.onoffswitch-switch').css('background', '#4b73eb');
	    	}else{
	    		//$('.onoffswitch-switch').css('background', '#f1f1f1');
	    		$(this).next().find('.onoffswitch-switch').css('background', '#f1f1f1');
	    	}
	    });
	});
</script>
<div class="main_contents">
	<div class="prs_title">${sessionScope.selMenuNm }</div>
	<form>
		<div class="set_prs1100">
			<div class="title_prs1100">
				<div class="set_title">
					<span class="title_name">기능 툴팁 표시</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch1" checked>
						 		<label class="onoffswitch-label" for="myonoffswitch1">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
				<div class="set_title font_color">
					<span class="title_name off">튜토리얼 안내 메시지</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch2" value="true">
						 		<label class="onoffswitch-label" for="myonoffswitch2">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
				<div class="set_title ">
					<span class="title_name">전체 튜토리얼 기능</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch3" checked>
						 		<label class="onoffswitch-label" for="myonoffswitch3">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
				<div class="set_title font_color">
					<span class="title_name off">전체 튜토리얼 기능</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch4" >
						 		<label class="onoffswitch-label" for="myonoffswitch4">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
				<div class="set_title">
					<span class="title_name">전체 튜토리얼 기능</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch5" checked>
						 		<label class="onoffswitch-label" for="myonoffswitch5">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
				<div class="set_title">
					<span class="title_name">전체 튜토리얼 기능</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch6" checked>
						 		<label class="onoffswitch-label" for="myonoffswitch6">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
				<div class="set_title">
					<span class="title_name">전체 튜토리얼 기능</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch7" checked>
						 		<label class="onoffswitch-label" for="myonoffswitch7">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
				<div class="set_title font_color">
					<span class="title_name off">전체 튜토리얼 기능</span>
					<div>
						 <div class="onoffswitch">
						 	<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="myonoffswitch8" >
						 		<label class="onoffswitch-label" for="myonoffswitch8">
						 			<span class="onoffswitch-inner"></span>
						   			<span class="onoffswitch-switch"></span>
						  		</label>
						 </div>
					</div>
				</div>
			</div>
			<div class="btn_prs1100">
				<input type="submit" class="set_btn_prs1100" value="저장">
			</div>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />