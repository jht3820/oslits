<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="ko">

<title>OpenSoftLab</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/highlight/styles/monokai-sublime.css'/>">
<script type="text/javascript" src="<c:url value='/js/highlight/highlight.pack.js'/>"></script>
<style>
#lpx1 .pop_left, .layer_popup_box .pop_right { height: 54px; }
.lp10{padding-left: 10px}
input[type='radio']+label{font-weight: bold;}
.textarea_height{height:107px !important;}
.required_info{color:red}
.ui-menu-item {font-size: 0.5em !important; text-align:left;}
div#contentsFrame {
    width: 100%;
    height: 600px;
    text-align: left;
    line-height:20px;
}
div#contentsFrame > code{    height: 100%;width: 100%;font-size: 10pt;}
</style>



<!--  등록 및 수정 에 따른 class , 근무시간, 휴게시간 세팅 -->


<script>
	var consoleContent = "";
	
	$(document).ready(function() {
		$('.pop_title').html( '${param.name}');
		
		$('#loading').show();
		
		buildPopCheck('${param.name}','${param.number}');
		
		/* 취소버튼 클릭 시 팝업 창 사라지기*/
		$('#btn_close_popup').click(function() {
			gfnLayerPopupClose();
		});
		
	});

	function consoleloading(jobname,number){
		
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildConsoleAjax.do'/>","loadingShow":false}
				,{"jobname" : jobname ,"buildnumber" : number , "prjId": '${param.prjId}', "dplId": '${param.dplId}'  });
		//AJAX 전송 성공 함수
		ajaxObj.async = false;
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			$('#fileContent').val(data.consoleText);
			
			//fnInGridListSet();
			
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
	 	});
		//AJAX 전송


		ajaxObj.send();
		
	}
	
	function buildPopCheck(jobname,number){
			

		if(  $('#consoleDiv').parent().parent().css('display') =="block"){
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildDetailAjax.do'/>","loadingShow":false}
					,{"jobname" : jobname , "buildnumber" : number , "prjId": '${param.prjId}', "dplId": '${param.dplId}'  });
			//AJAX 전송 성공 함수
			ajaxObj.async = false;
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				$('.pop_title').html("${param.name}#${param.number}");
				
				if(data.errorYn == "Y"){
					$('#fileContent').html(gfnReplace( data.consoleText , "\n" , "</br>"));
				}else{
					var logHtml = "";
					//var hisInfo = JSON.stringify(data.actions); 
					if(data.messageCode == undefined ){
						
						logHtml += "result : "+data.actions.result;
						var actionList = data.actions.actions;
						
						$(actionList).each(function(index, item){
							if(!gfnIsNull(item)){
								var causesList = item.causes;
								$(causesList).each(function(index, itm){
									logHtml += "userId : "+itm.userId+"</br>";			
									logHtml += "userName : "+itm.userName+"</br>";
									logHtml += "shortDescription : "+itm.shortDescription+"</br>";
								})
							}
						
						})
						
						logHtml += "</br>";
						logHtml += "</br>";
						logHtml += "==============================================Console Output==============================================</br>";
						
						logHtml  += gfnReplace( data.consoleText , "\n" , "</br>");
						
						$('#fileContent').html(logHtml);
						//코드 뷰어 달기
						$('#fileContent').each(function(i, block) {hljs.highlightBlock(block);});
					}else{
						jAlert(data.message, "알림창");
					}
				}
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
				jAlert(data.message, "알림창");
		 	});
			//AJAX 전송
	
	
			ajaxObj.send();
		}
	}
	

</script>

	<div class="popup" id = "consoleDiv" >
		<div class="pop_title">
		</div>
		<div class="pop_sub">
			<div id="contentsFrame">
				<code id="fileContent">
				
				</code>
			</div>
		</div>
		<div class="pop_sub">	
			<div class="btn_div">
				<div class="button_normal exit_btn" id="btn_close_popup">닫기</div>
			</div>
		</div>
	</div>
</html>