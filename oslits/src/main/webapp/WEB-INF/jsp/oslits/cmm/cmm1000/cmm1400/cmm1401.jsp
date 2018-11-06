<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="ko">

<title>OpenSoftLab</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/highlight/styles/monokai-sublime.css'/>">
<script type="text/javascript" src="<c:url value='/js/highlight/highlight.pack.js'/>"></script>
<style>
#lx1 .pop_left, .layer_popup_box .pop_right { height: 54px; }
#lx1{ width:590px !important; height:845px !important; }
.lp10{padding-left: 10px}
input[type='radio']+label{font-weight: bold;}
.textarea_height{height:107px !important;}
.required_info{color:red}
.ui-menu-item {font-size: 0.5em !important; text-align:left;}
textarea#fileContent{resize: none;}
div#contentsFrame {
    width: 100%;
    height: 596px;
    text-align: left;
    line-height:20px;
}
div#contentsFrame > pre{width:100%;height:100%;}
div#contentsFrame > pre > code{    height: 100%;width: 100%;font-size: 10pt;}

</style>



<!--  등록 및 수정 에 따른 class , 근무시간, 휴게시간 세팅 -->


<script>
	
	$(document).ready(function() {
		//var content = "${content}";
		fnGetFileContent();
		//
		$('#sourceTitle').html( '${param.name}');
		
		
		/* 닫기버튼 클릭 시 팝업 창 사라지기*/
		$('.exit_btn').click(function() {
			gfnLayerPopupClose();
		});
	});


	function fnGetFileContent(){
 	/* 그리드 데이터 가져오기 */
 	//파라미터 세팅
 	gfnShowLoadingBar(true);
  	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm1000/cmm1400/selectCmm1400FileContentAjax.do'/>","loadingShow":false}
				, {"revision" : '${param.revision}' ,  "path" : '${param.path}', "svnRepId" : '${param.svnRepId}'});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			
			data = JSON.parse(data);
					
			$('#fileContent').text(data.content);
			
			//코드 뷰어 달기
			$('#fileContent').each(function(i, block) {hljs.highlightBlock(block);});
			/*console.time() .promise().done( function(){ gfnShowLoadingBar(false);console.timeEnd() } ); */
			
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			//세션이 만료된 경우 로그인 페이지로 이동
	       	if(status == "999"){
	       		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
	    		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
	    		return;
	       	}
		});
		
		//AJAX 전송
		ajaxObj.send();
	}

</script>

	<div class="popup">
		<div class="pop_title" id="sourceTitle">
		</div>
		<div class="pop_sub">
			<div id="contentsFrame">
				<pre>
					<code id="fileContent">
					
					</code>
				</pre>
			</div>
		</div>
		<div class="pop_sub">
		<div class="btn_div">
			<div class="button_normal exit_btn" id="btn_cancle_popup">닫기</div>
		</div>
	</div>
	</div>
</html>