<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/prj.css'/>' type='text/css'>

<script src="<c:url value='/js/common/layerPopup.js'/>"></script>

<style>
/* 클래스를 추가해서 왼쪽에서는 안나오게 , 오른쪽에서는 나오게 설정한다 */
.middle_td_div .hidden_td { display: none; }
.right_td_div .block_td { display: table-cell; }
.auth_table_title { font-weight: bold; text-align: center;}
</style>

<script>

var selMainMenuId = "";

$(document).ready(function() {
	/********************************************************************
	*	공통기능 부분 정의 시작													*
	*********************************************************************/
	
	/* 메뉴, 권한 그룹 탭 전환 */
	$(".tab_box li").click(function(){
		var idx = $(this).index();
		
		switch(idx){
		case 0:
			$(".tab_contents.menu").show();
			$(".tab_contents.authority").hide();
			break;
		case 1:
			$(".tab_contents.menu").hide();
			$(".tab_contents.authority").show();
			break;
		}
		
		$(this).addClass("on").siblings().removeClass("on");
	});
	
	/********************************************************************
	*	공통기능 부분 정의 종료													*
	*********************************************************************/
	
	
	/********************************************************************
	*	프로젝트 권한 관리 부분 정의 시작											*		
	*********************************************************************/
	
	
	
	// 그룹메뉴명 전체 체크/전체 해제 이벤트 처리
	// 전체체크박스는 form에 담지 않기 위해 밸류값을 변경하지 않는다.
	$("input[type='checkbox']").click(function() {
		var allChkId = $(this).attr("id").substring(0,10);
		if(allChkId == "prjAuthAll"){
			if ($(this).is(':checked')) {
				//start with selector는 ^= , end with selector는 $=
				var allName = $(this).attr("name");
				$("input[name$=" + allName + "]").prop("checked", true);
				$("input[name$=" + allName + "]").val("Y");
				$("input[name^=status]").val("U");
			} else {
				//start with selector는 ^= , end with selector는 $=
				var allName = $(this).attr("name");
				$("input[name$=" + allName + "]").prop("checked", false);
				$("input[name$=" + allName + "]").val("N");
				$("input[name^=status]").val("U");
				
				//접근권한 체크해제인경우 메인화면 해제
				if(allName == "accessYn"){
					$('input[id^=rd]:checked').prop("checked",false);
				}
			}
			
			//전체선택 체크박스 값을 초기화 하지 않으면 오류 발생함. substring으로 자를수 없기 때문
			$("[id^=prjAuthAll_ch]").val("");
		}
	});
	
	// 프로젝트 권한 관리 - 추가
	$('#btn_insert_prjAuth').click(function() {
		//layer_popup('/prj/prj2000/prj2000/selectPrj2001View.do', null);
		gfnLayerPopupOpen('/prj/prj2000/prj2000/selectPrj2001View.do', null, '730', '700','scroll');
		
	});
	
	// 프로젝트 권한 관리 - 삭제 
	$('#btn_delete_prjAuth').click(function() {
		fnDeleteAuthGrp();
	});
	
	// 프로젝트 권한 관리 - 수정 
	$('#btn_update_prjAuth').click(function() {
		var authGrpId = $('.left_con.table_active').attr("id");
		
		//삭제할 롤을 선택했는지 확인하고 진행
		if(!($('.left_con.table_active').hasClass("table_active"))){
			toast.push('수정할 롤을 선택하고 수정버튼을 눌러주세요.');                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
			return;
		}
		
		
		var data = { "gb" : "update" , "authGrpId" : authGrpId};
		gfnLayerPopupOpen('/prj/prj2000/prj2000/selectPrj2001View.do', data , '730', '700','scroll');
	});

	// 프로젝트 버튼권한 저장
	$('#btn_insert_prjAuth_button').click(function() {
		fnSaveAuthGrpMenuAuthListAjax();
	});

	/********************************************************************
	*	프로젝트 권한 관리 부분 정의 종료											*		
	*********************************************************************/	
	fnSelectPrj2000PrjAuthGrpList();
	
});




/********************************************************************
* 프로젝트 권한 관리 기능 부분 정의 시작											*
*********************************************************************/

/**
*	권한정보 클릭시 메뉴권한 정보 조회 
**/
function fnAuthGrpSmallMenuList(authGrpId, selPrjId){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj2000/prj2000/selectPrj2000AuthGrpMenuListAjax.do'/>","loadingShow":true}
			,{"authGrpId" : authGrpId, "prjId" : selPrjId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	//로딩바 제거
    	//gfnShowLoadingBar(false);
    	
    	//기존 조회 정보 모두 제거
    	$("#authTblBody").children().remove();
    	
    	//rowspan 대상 ID
    	var prevUpupMenuId = '';
    	
    	//메뉴 rowspan 담기
    	var menuIdRowSpan = {};
    	
    	//반복하며 그리기
    	$.each(data.authGrpSmallMenuList,function(idx, data){
    		//시스템 관리자 막기
    		/*
    		if(data.upupMenuId == "000900000000"){
    			return true;
    		}*/
    	
    		//tr 태그 id 부여하여 생성
    		$("#authTblBody").append("<tr id='tr" + data.menuId + "'>");
			
    		//생성한 tr태그 객체 얻기
    		var trObj = $("#tr" + data.menuId );
    		
    		//새로운 대 메뉴인경우
    		if(gfnIsNull(prevUpupMenuId) || prevUpupMenuId != data.upupMenuId){
    				trObj.append("<td class='right_con wd_2_1' id='menuRow_"+data.upupMenuId+"' rowspan='1'>" + data.upupMenuNm + "</td>");	
    		}else{
    			//menuId별 rowspan 생성
    			if(gfnIsNull(menuIdRowSpan[data.upupMenuId])){
    				menuIdRowSpan[data.upupMenuId] = 1;
    			}
    			
    			//오류 처리 안함
   				try{
   					//대메뉴 rowspan +1
    				var upObjRowspan = parseInt(menuIdRowSpan[data.upupMenuId]);
    				menuIdRowSpan[data.upupMenuId] = (upObjRowspan+1);
   				}catch(err){console.log(err);}
    		}
    		prevUpupMenuId = data.upupMenuId;
    		
    		trObj.append("<td class='right_con wd_2_2'>" + data.upMenuNm + " &#62; " + data.menuNm + "</td>");
    		
    		//배열로 체크박스 컬럼명 저장하여 반복 루프 처리함.
    		var strArrYn = ["accessYn", "selectYn", "regYn", "modifyYn", "delYn", "excelYn", "printYn"];
    		
			//해당 로우 상태 담기
			trObj.append("<input type='hidden' name='" + "status" + data.menuId + "' id='" + "status" + data.menuId + "' value='" + data.status + "' />");
			var mainYn = data.mainYn; 
			if(data.menuTypeCd == '02'){
				if(mainYn=='Y'){
					selMainMenuId = data.menuId;
					trObj.append("<td class='right_con wd_2_3 prj_chk'><input id='rd_"+data.menuId+ "' type='radio' name='rdMainMenu' value='"+data.menuId+"' checked='checked' onclick='fnRdChk(this,\""+data.menuId+"\");'  ><label for='rd_"+data.menuId+ "' ></label></td>");
				}else{
					trObj.append("<td class='right_con wd_2_3 prj_chk'><input id='rd_"+data.menuId+ "' type='radio' name='rdMainMenu' value='"+data.menuId+"' onclick='fnRdChk(this,\""+data.menuId+"\");' ><label for='rd_"+data.menuId+ "' ></label></td>");
				}	
			}else{
				trObj.append("<td class='right_con wd_2_3 prj_chk'></td>");
			}
			
						
			
    		//메뉴별 권한 체크박스 생성
    		$.each(strArrYn, function(idx, val){
    			var cnt = idx + 4;
    			var strMenuId = data.menuId + val;
    			var hidMenuId = "hidden" + data.menuId + val ;
    			var hidStatus = "status" + data.menuId;	//수정상태인지 그냥 수정전 상태인지 상태값 가진 인풋
    			trObj.append("<td class='right_con wd_2_" + cnt + " prj_chk' style='text-align: center;' ><input type='hidden' name='" + hidMenuId + "' id='" + hidMenuId + "' value='" + eval("data." + val) + "' /> <input type='checkbox' title='체크박스' onclick='fnValToChk(this,"+idx+");' name='" + strMenuId + "' id='" + strMenuId + "' value='" + eval("data." + val) + "' /><label for='" + strMenuId + "'></label></td>");
    		});
    		
    		// 가로로 전체 체크가능한 체크박스 
    		trObj.append("<td class='right_con wd_2_11 prj_chk' style='text-align: center;' ><input type='checkbox' title='체크박스'  style='left:calc(50% - 7px);' name='"+data.menuId+"_prjAuthHorizon' id='"+data.menuId+"_prjAuthHorizon' onclick='fnHorizonChk(this);' value /><label for='"+data.menuId+"_authHorizon'></label></td>");
    		
    		//밸류값 확인하여 체크 상태 변경
    		$.each(strArrYn, function(idx, val){
    			var objYn = $("#" + data.menuId + val);
        		if($(objYn).val() == 'Y'){
        			$(objYn).prop("checked", true);
        		}
        		else{
        			$(objYn).prop("checked", false);
        		}	
    		});
    		
    		//기존 선택 메인화면
			$prevChkVal = $("input[name=rdMainMenu]:checked");
    		
    		//전체 선택 체크 풀기
    		$("input[id^=prjAuthAll_]").prop("checked",false);
    	});
    	
    	//rowspan 걸기
    	$.each(menuIdRowSpan, function(idx, map){
    		$("#menuRow_"+idx).attr("rowspan",map);
    	});
    	
    	toast.push(data.message);
    	
    	//출력 감추기
    	$("input[name$=printYn]").parent().hide();
    	
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
 	});
	
	//AJAX 전송
	ajaxObj.send();
}
/**
 * 메인 화면 지정 라디오 이벤트
 */
function fnRdChk(obj,menuId){
	if( $(obj).prop("checked")){
		if(! $('#'+menuId+'accessYn').prop("checked") ){
			jConfirm("접근 권한이 없는 화면은 메인화면으로 지정할수 없습니다 계속 진행하시겠습니까?", "알림", function( result ) {
 				if( result ){
	 				$('#'+menuId+'accessYn').prop("checked",true);
					$('#'+menuId+'accessYn').val("Y");
					$("#hidden" + menuId+'accessYn').val("Y");
					$("#status" + menuId).val("U");
					
					$prevChkVal = $("input[name=rdMainMenu]:checked");
	 			}else{
	 				$(obj).prop("checked",false);
	 				$prevChkVal.prop("checked",true);
	 			}
			});
		}else{
			$prevChkVal = $("input[name=rdMainMenu]:checked");
		}
	}
}

/**
 * 	권한그룹의 선택한 권한롤을 삭제한다.
 */
function fnDeleteAuthGrp(){
	var authGrpId = $('.left_con.table_active').attr("id");
	var authGrpCnt = $('tr.left_con').length;
	
	//역할 그룹이 1개인경우 삭제 불가
	if(authGrpCnt == 1){
		jAlert("역할그룹은 1개 이상 존재해야합니다.<br>삭제가 불가능합니다.","경고");
		return false;
	}
	
	//삭제할 롤을 선택했는지 확인하고 진행
	if(!($('.left_con.table_active').hasClass("table_active"))){
		toast.push('삭제할 롤을 선택하고 삭제버튼을 눌러주세요.');
		return;
	}
	
	jConfirm("삭제하면 되돌릴 수 없습니다. \r\n삭제하시겠습니까?", "알림", function( result ) {
		if( result ){
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/prj/prj2000/prj2000/deletePrj2000AuthGrpInfoAjax.do'/>","loadingShow":false}
					,{"authGrpId" : authGrpId});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
			      
		    	if(data.saveYN == 'N'){
		    		jAlert(data.message,"알림창");
		    		return;
		    	}else{
		    		//삭제 성공시 권한그룹 목록, 메뉴별접근권한 목록, 프로젝트사용자배정 권한그룹 목록, 배정 및 등록 사용자 목록 초기화
		    		fnSelectPrj2000PrjAuthGrpList();
		    		
		    		//현재 선택한 권한그룹인경우 페이지 새로고침
		    		if($("#LNB_project_select").val() == authGrpId){
		    			jAlert("현재 지정된 역할그룹이 삭제되었습니다.</Br>페이지를 재 호출합니다.","알림",function(){
		    				location.href = "/cmm/cmm4000/cmm4000/selectCmm4000LoginAfter.do";
		    			});
		    		}
		    		
		    		toast.push(data.message);
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
	});
}

/**
 * 	권한그룹의 메뉴권한 정보들을 저장한다. 
 */
function fnSaveAuthGrpMenuAuthListAjax(){
	
	var $authGrpObj = $('.left_con.table_active');

	//menuAuthFrm에 현재 선택한 롤 id를 저장한다.
	$("#menuAuthGrpId").val($authGrpObj.attr("id"));
	
	var rdList =  $('[name="rdMainMenu"]');
	
	for(var i = 0 ; i < rdList.length; i++ ){
		if($(rdList[i]).prop("checked")){
			$("#mainMenuId").val($(rdList[i]).val());
			if(selMainMenuId!=$(rdList[i]).val()){
				$("#status"+$(rdList[i]).val()).val("U");
				$("#status"+selMainMenuId).val("U");
			}
		}
	}
	
	

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj2000/prj2000/savePrj2000AuthGrpMenuAuthListAjax.do'/>","loadingShow":true}
			,$("#menuAuthFrm").serialize());
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	toast.push(data.message);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
 	});
	
	//AJAX 전송
	ajaxObj.send();
}


/*
 *	해당 메뉴의 가로 전체 체크/전체 해제 처리
 * @param chkObj 가로 전체체크 체크박스
 */
function fnHorizonChk(chkObj){
	
	// 체크박스의 ID를 가져온다
	var horizonChkId = $(chkObj).attr("id"); 
	// 체크박스 ID에서 메뉴 ID를 추출
	var horizonMenuId = horizonChkId.split("_")[0];
	
	// 가로 전체체크 할 경우
	if($(chkObj).is(':checked')){
		$("input[name^=" + horizonMenuId + "]").prop("checked", true);
		$("input[name^=" + horizonMenuId + "]").val("Y");
		$("input[name^=hidden" + horizonMenuId + "]").val("Y");
		$("input[name^=status" + horizonMenuId + "]").val("U");
	// 가로 전체 체크해제 할 경우	
	}else{
		$("input[name^=" + horizonMenuId + "]").prop("checked", false);
		$("input[name^=" + horizonMenuId + "]").val("N");
		$("input[name^=hidden" + horizonMenuId + "]").val("N");
		$("input[name^=status" + horizonMenuId + "]").val("U");
		
		// 접근 체크박스인지 체크할 ID값
		var accessChkId = $("input[name^=" + horizonMenuId + "]").attr("id");
		
		//접근권한 체크해제인경우 메인화면 해제
		if(accessChkId.indexOf("accessYn") != -1){
			$('input[id^=rd_'+horizonMenuId+']:checked').prop("checked",false);
		}
	}
}

/**
*	권한관리 체크박스 클릭시 체크상태를 확인하여 밸류값을 변환 
*/
function fnValToChk(chkObj,index){
	var strMenuId = $(chkObj).attr("id").substring(0,12);
	
	//만약 체크 상태가 체크이면 value에 Y 세팅, 상태를 수정모드로 변경
	if(	$(chkObj).prop('checked')){
		$(chkObj).val("Y");
		$("#hidden" + $(chkObj).attr("id")).val("Y");
		$("#status" + strMenuId).val("U");
	}
	//체크 상태가 아니라면 value에 N 세팅, 상태를 수정모드로 변경
	else{
		if($('#rd_'+strMenuId).prop("checked") && index== 0 ){
			jConfirm("메인화면으로 지정되있는 화면의 접근권한을 해제시 메인화면으로 지정상태가 해제됩니다. 계속 진행하시겠습니까?", "알림", function( result ) {
				if( result ){
					$('#rd_'+strMenuId).prop("checked",false);
					$(chkObj).val("N");
					$("#hidden" + $(chkObj).attr("id")).val("N");
					$("#status" + strMenuId).val("U");
				}else{
					$(chkObj).prop('checked',true);
				}
			});
		}else{
			$(chkObj).val("N");
			$("#hidden" + $(chkObj).attr("id")).val("N");
			$("#status" + strMenuId).val("U");	
		}
	}
}
/********************************************************************
* 프로젝트 권한 관리 기능 부분 정의 종료											*
*********************************************************************/


/**
*	권한정보 클릭시 메뉴권한 정보 조회 
**/
function fnSelectPrj2000PrjAuthGrpList(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj2000/prj2000/selectPrj2000PrjAuthGrpList.do'/>","loadingShow":true}
			,{ });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	//기존 조회 정보 모두 제거
    	$("#prjAuthGrpList").children().remove();
    	
    	//반복하며 그리기
    	$.each(data.prjAuthGrpList,function(idx, data){
    		
    		var authGrpDesc = data.authGrpDesc;
    		authGrpDesc = (authGrpDesc.replace(/</g,"&lt;")).replace(/>/g,"&gt;");
    		authGrpDesc = gfnReplace(authGrpDesc, 'null', ' ');
    		
    		//tr 태그 id 부여하여 생성
    		var html = "";
    		html += '<tr class="left_con" id="'+data.authGrpId+'">';
    		html += '	<td class="left_con right_line">'+data.authGrpNm+'</td>';
    		html += '	<td class="left_con  right_line" title="'+authGrpDesc+'">'+gfnCutStrLen(authGrpDesc, 60)+'</td>';
    		html += '	<td class="left_con right_line">'+data.usrTypNm+'</td>';
    		html += '</tr>';
    		
    		$("#prjAuthGrpList").append(html);
    	});
    	
    	// 프로젝트 권한관리 - 권한 목록 클릭시 소메뉴 권한 목록 조회
    	$('.left_table tbody tr').on("click",function(){
    	    $('.left_table tbody tr').removeClass("table_active");
    	    $('.left_table tbody tr').addClass("table_inactive");
    	    $(this).addClass("table_active");
    	    $(this).removeClass("table_inactive");
    	  	
    	    fnAuthGrpSmallMenuList($(this).attr("id"), '${sessionScope.selPrjId}');
    	});
    	
    	toast.push(data.message);
    	
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
 	});
	
	//AJAX 전송
	ajaxObj.send();
}


function fnNvl(str){
	if(str==null){
		return "";
	}else{
		return str;
	}
}

</Script>

<div class="main_contents" style="height: auto;"  >
	<div class="tab_title">${sessionScope.selMenuNm }</div>
	<div class="tab_menu">
		<ul class="tab_box">
			<li class="ok_bottom_line on"><a>프로젝트 역할그룹 관리</a></li>
		</ul>

		<div class="tab_contents menu">
		
			<div class="left_div">
			<div class="left_title">역할 그룹
				<span class="button_normal btn_one" id="btn_insert_prjAuth">추가</span>
				<span class="button_normal btn_two" id="btn_update_prjAuth">수정</span>
				<span class="button_normal btn_three" id="btn_delete_prjAuth">삭제</span>
			</div>
				<table class="left_table">
					<caption>역할 그룹</caption>
					<thead>
						<tr>
							<th class="left_sub_title right_line" style="width: 30%;">그룹 명</th>
							<th class="left_sub_title right_line" style="width: 30%;">설명</th>
							<th class="left_sub_title" style="width: 20%;">사용자<br/>유형</th>
						</tr>
					</thead>
					<tbody id="prjAuthGrpList" >
					
					
						
					</tbody>
				</table>
			</div> 
			<div class="right_div">
				<div class="right_title">시스템 권한
					<span class="button_normal btn_three" id="btn_insert_prjAuth_button">저장</span>
				</div>
				<form id="menuAuthFrm" name="menuAuthFrm">		
					<input type="hidden" id="menuAuthGrpId" name="menuAuthGrpId" >
					<input type="hidden" id="mainMenuId" name="mainMenuId" >
					
					<table class="right_table">
						<caption>시스템 권한</caption>
						<thead>
							<tr class="auth_table_title">
								<th class="right_sub_title wd_2_1" style="text-align: center;">대 메뉴명</th>
								<th class="right_sub_title wd_2_2 sub_two" style="text-align: center;">중 메뉴명 > 소 메뉴명</th>
								<th class="right_sub_title wd_2_3">메인</th>
								<th class="right_sub_title wd_2_4" >
									<div class="prj_chk"> 
										<input type="checkbox" title="체크박스" style="left:calc(50% - 23px);" name="accessYn" id="prjAuthAll_ch1"/><label for="prjAuthAll_ch1"></label><span class="title_align">접근</span>
									</div>
								</th>
								<th class="right_sub_title wd_2_5">
									<div class="prj_chk">
										<input type="checkbox" title="체크박스" style="left:calc(50% - 23px);" name="selectYn" id="prjAuthAll_ch2"/><label for="prjAuthAll_ch2"></label><span class="title_align">조회</span>
									</div>
								</th>
								<th class="right_sub_title wd_2_6">
									<div class="prj_chk">
										<input type="checkbox" title="체크박스" style="left:calc(50% - 23px);" name="regYn" id="prjAuthAll_ch3"/><label for="prjAuthAll_ch3"></label><span class="title_align">등록</span>
									</div>
								</th>
								<th class="right_sub_title wd_2_7">
									<div class="prj_chk">
										<input type="checkbox" title="체크박스" style="left:calc(50% - 23px);" name="modifyYn" id="prjAuthAll_ch4"/><label for="prjAuthAll_ch4"></label><span class="title_align">수정</span>
									</div>
								</th>
								<th class="right_sub_title wd_2_8">
									<div class="prj_chk">
										<input type="checkbox" title="체크박스" style="left:calc(50% - 23px);" name="delYn" id="prjAuthAll_ch5"/><label for="prjAuthAll_ch5"></label><span class="title_align">삭제</span>
									</div>
								</th>
								<th class="right_sub_title wd_2_9">
									<div class="prj_chk">
										<input type="checkbox" title="체크박스" style="left:calc(50% - 23px);" name="excelYn" id="prjAuthAll_ch6"/><label for="prjAuthAll_ch6"></label><span class="title_align">엑셀</span>
									</div>
								</th>
								<!-- <th class="right_sub_title wd_2_10">
									<div class="prj_chk">
										<input type="checkbox" title="체크박스" style="left:calc(50% - 23px);" name="printYn" id="prjAuthAll_ch7"/><label for="prjAuthAll_ch7"></label><span class="title_align">출력</span>
									</div>
								</th> -->
								<th class="right_sub_title wd_2_11">전체</br>체크</th>
							</tr>
						</thead>
						
						<tbody id="authTblBody">
							<!-- 테이블 바디 내용 들어감 AJAX로 조회시 삽입됨.  -->
							
						</tbody>
					</table>
				</form>
			</div>
		</div>		
	</div>	
	
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />