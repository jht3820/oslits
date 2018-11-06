<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="ko">

<title>OpenSoftLab</title>

<style>
#lpx1 .pop_left, .layer_popup_box .pop_right { height: 54px; }
#lpx1{ width:590px !important; height:645px !important; }
.lp10{padding-left: 10px}
input[type='radio']+label{font-weight: bold;}
.textarea_height{height:107px !important;}
.required_info{color:red}
.ui-menu-item {font-size: 0.5em !important; text-align:left;}
</style>



<!--  등록 및 수정 에 따른 class , 근무시간, 휴게시간 세팅 -->


<script>
	var consoleContent = "";
	
	$(document).ready(function() {
		$('#fileContent').height(500);
		$('#fileContent').width(550);

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
					{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildStatusAjax.do'/>","loadingShow":false}
					,{"jobname" : jobname , "buildnumber" : number , "prjId": '${param.prjId}', "dplId": '${param.dplId}'  });
			//AJAX 전송 성공 함수
			ajaxObj.async = false;
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				$('.pop_title').html( '${param.name}'+" #"+data.number	);
				
				if(data.actions.result == null){
					consoleloading(jobname,number);
						//fnInGridListSet();
					setTimeout(function(){
							buildPopCheck(jobname,number);
						}, 10000
					);
				}else{
					consoleloading(jobname,number);
					$('#loading').hide();
					alert("빌드완료");
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
			<div  id="loading" style="text-align: center;vertical-align:middle;margin:20px;border:5px;padding:5px;">
				<img src='/images/login/loding/ajax-loader.gif' alt='로딩중 표시 이미지' />
			</div>
			<textarea title="file" id="fileContent" name="menuDesc" value="" />		 
		</div>
		<div class="pop_sub">	
			<div class="btn_div">
				<div class="button_normal exit_btn" id="btn_close_popup">닫기</div>
			</div>
		</div>
	</div>
</html>