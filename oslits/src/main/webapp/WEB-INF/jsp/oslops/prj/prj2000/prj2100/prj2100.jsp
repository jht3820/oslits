<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/prj.css'/>' type='text/css'>

<style>
/* 클래스를 추가해서 왼쪽에서는 안나오게 , 오른쪽에서는 나오게 설정한다 */
.middle_td_div .hidden_td { display: none; }
.right_td_div .block_td { display: table-cell; }
.search_div { height: 37px; vertical-align: middle; text-align: right; margin-top: 10px;}
#btn_search_usr { float: right; margin-right: 7px;}
#searchSelect_usr { width:15%; float: right; margin-right: 7px; height: 29px;}
#searchTxt { float: right; margin-right: 7px; width: 150px; height: 28px; border: 1px solid #ccc; font-size: 13px; box-shadow: inset 0px 1px 2px #dddddd; padding-left: 6px;}
/* 화면로드 시 검색창 안나오도록 */
#search_th{ display: none;}
</style>

<script>
// 현재 선택된 권한그룹 ID
var selAuthGrpId = "";

// 배정 삭제된사용자 정보를 담을 배열
var prjDelUsrList = [];

$(document).ready(function() {
	/********************************************************************
	*	프로젝트 사용자 배정 부분 정의 시작											*		
	*********************************************************************/	
	$(".tab_contents.authority").show();
	
	//프로젝트 사용자 배정 - 권한 목록 클릭시 배정된 사용자 및 미배정된 사용자 정보 조회
	$('.left_tb tbody tr').on("click",function(){
	    $('.left_tb tbody tr').removeClass("table_active");
	    $('.left_tb tbody tr').addClass("table_inactive");
	    $(this).addClass("table_active");
	    $(this).removeClass("table_inactive");
	    
	    // 현재 선택된 권한그룹 ID - 사용자 검색에 사용
	    selAuthGrpId = $(this).attr("id");
	    
	    fnPrjUsrAddList($(this).attr("id"), '${sessionScope.selPrjId}', null, null);
	    
	    // 사용자 검색은 권한 그룹명 클릭 시 보이도록 
	    $("#search_th").show();

	    // select 전체 보기로 선택
	    $("#searchSelect_usr option:eq(0)").prop("selected", true);
	    fnChangeElement();
	    
	    // 권한그룹 클릭 시 배열 초기화
	    prjDelUsrList.length = 0;
	});

	// 전체 사용자 전체 체크/전체 해제 이벤트 처리
	$("#right_table_all").click(function() {
		if ( $(this).is(':checked') ) {
			$("input[name^=prj_chk_r]").prop("checked", true);
		} else {
			$("input[name^=prj_chk_r]").prop("checked", false);
		}
	});
	
	// 전체 사용자 전체 체크/전체 해제 이벤트 처리
	$("#middle_table_all").click(function() {
		if ($(this).is(':checked')) {
			$("input[name^=prj_chk_m]").prop("checked", true);
		} else {
			$("input[name^=prj_chk_m]").prop("checked", false);
		}
	});
	
	
	/* 프로젝트 사용자 배정 - 추가 */
	$('#btn_insert_usr').click( function(){
		var chkArr = [];
		
		$("input[name^=prj_chk_r]:checked").each(function(idx, obj){
			chkArr.push($(this).val());
		});
		if(gfnIsNull(chkArr)){
			jAlert("사용자를 선택후 추가해주세요.", "알림창");
			return false;
		}
		var strUsrIdInSql = "";
		
		$(chkArr).each(function(idx, data){
			strUsrIdInSql += data + ",";
		});

		// 사용자 추가 시 검색창 초기화 필요할 경우 사용
		//$("#searchSelect_usr option:eq(0)").prop("selected", true);
	    //fnChangeElement();
		
		strUsrIdInSql = strUsrIdInSql.substring(0,strUsrIdInSql.length-1);
		
		// 검색어도 같이 전달
		var selOption  = $("#searchSelect_usr option:selected").val();
		var searchTxt = $("#searchTxt").val();
		
		fnSavePrjUsrAuthList("I", strUsrIdInSql, $('.left_tb tbody tr.table_active').attr("id"), selOption, searchTxt);
	});
	
	/* 프로젝트 사용자 배정 - 삭제 */
	$('#btn_delete_usr').click( function(){
		var chkArr = [];
		
		$("input[name^=prj_chk_m]:checked").each(function(idx, obj){
			chkArr.push($(this).val());
		});
		if(gfnIsNull(chkArr)){
			jAlert("사용자를 선택후 삭제해주세요.", "알림창");
			return false;
		}
		var strUsrIdInSql = "";
		var continueChk = true;
		$(chkArr).each(function(idx, data){
			//라이센스 최초 발급자와 같은 아이디인지 확인, PO역할인지 확인
			if($('.table_active:not(.left_con)').attr('id') == "PO"){
				/* if(data+"_GRP" == "${sessionScope.licVO.licGrpId}"){
					jAlert("라이선스 최초 등록자는 PO역할에서 제외시킬 수 없습니다.","경고");
					continueChk = false;
					return false;
				} */
				if(($('#addUsrTblBody > tr').length-chkArr.length) == 0){
					jAlert("PO역할은 1명이상의 사용자가 필수로 배정되어있어야 합니다.","경고");
					continueChk = false;
					return false;
				}
			}
			strUsrIdInSql += data + ",";
		});
		
		if(!continueChk){
			return false;
		}
		
		// 사용자 삭제 시 검색창 초기화 필요할 경우 사용
		//$("#searchSelect_usr option:eq(0)").prop("selected", true);
	    //fnChangeElement();
		
		strUsrIdInSql = strUsrIdInSql.substring(0,strUsrIdInSql.length-1);
		// 검색어도 같이 전달
		var selOption  = $("#searchSelect_usr option:selected").val();
		var searchTxt = $("#searchTxt").val();
		
		
		fnSavePrjUsrAuthList("D", strUsrIdInSql, $('.left_tb tbody tr.table_active').attr("id"), selOption, searchTxt);
	});	

	/********************************************************************
	*	프로젝트 사용자 배정 부분 정의 종료											*		
	*********************************************************************/
	
	// 검색옵션 선택 (select)
	$("#searchSelect_usr").change(function() {
		fnChangeElement();
	});
	
	// 사용자 검색 엔터키 이벤트
	$('#searchTxt').keyup(function(e) {
		if(e.keyCode == '13' ){
			$('#btn_search_usr').click();
		}
	});
	
	// 사용자 검색버튼 클릭
	$("#btn_search_usr").click(function(){

		var selOption  = $("#searchSelect_usr option:selected").val();
		var searchTxt = $("#searchTxt").val();
	    
		// 전체검색이거나 검색어 입력안하고 검색할 경우 배열 초기화
		if(selOption == "rn" || gfnIsNull(searchTxt)){
			prjDelUsrList.length = 0;
		}
		
	    fnPrjUsrAddList(selAuthGrpId, '${sessionScope.selPrjId}', selOption, searchTxt); 
	});
	
});



/********************************************************************
* 프로젝트 사용자 배정 부분 정의 시작											*		
*********************************************************************/

/*
*	권한정보 클릭시 배정된 사용자 목록과 미배정 목록을 불러온다. 
*/
function fnPrjUsrAddList(authGrpId, selPrjId, searchSelect, searchTxt){
	
	var sendData = {"authGrpId" : authGrpId, "prjId" : selPrjId, 
					"searchSelect" : searchSelect, "searchTxt" : searchTxt}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj2000/prj2000/selectPrj2000UsrAddListAjax.do'/>","loadingShow":false}
			,sendData);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	//배정 및 미배정 사용자 목록 그리기
    	fnReDrawUsrAddList(data);
    	
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
*	권한 추가, 삭제를 수행한다. 
*/
function fnSavePrjUsrAuthList(status, strUsrIdInSql, authGrpId, searchSelect, searchTxt){
	var prjId = '${sessionScope.selPrjId}';
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj2000/prj2000/savePrj2000PrjUsrAuthListAjax.do'/>","loadingShow":false}
			,{"status" : status, "authGrpId" : authGrpId, "prjId" : prjId, "strUsrIdInSql" : strUsrIdInSql, 
				"searchSelect" : searchSelect, "searchTxt" : searchTxt});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	if(data.saveYN == 'N'){
    		jAlert(data.message, "알림창");
    		return;
    	}
    	else{
    		
    		if( !gfnIsNull(data.prjUsrAllListMap)){
    			$.merge(prjDelUsrList, data.prjUsrAllListMap);
    		}
    		
    		if( !gfnIsNull(data.prjDelUsrListMap)){
    			$.merge(prjDelUsrList, data.prjDelUsrListMap);
    		}
    		
    		prjDelUsrList = uniqurArray(prjDelUsrList);

    		//배정 및 미배정 사용자 목록 그리기
        	fnReDrawUsrAddList(data, status);
    		
    		if(status == "I" && data.prjUsrAllListMap != null ){
    			prjDelUsrList.length = 0;
    		}
    		
        	toast.push(data.message);	
    	}
	});
	
	//AJAX 전송
	ajaxObj.send();
}

/* 
 * 	배정된 사용자 정보 및 미배정 사용자 정보를 그린다.
 */
function fnReDrawUsrAddList(data, status){
	//기존 조회 정보 모두 제거
	$("#addUsrTblBody").children().remove();
	
	//기존 조회 정보 모두 제거
	$("#allUsrTblBody").children().remove();
	
	$.each(data.prjUsrAddListMap, function(idx, map){
		$("#addUsrTblBody").append(	"<tr id='tr" + map.usrId + "'>" 
								+		"<td class='middle_cn prj_chk m_td1'><input type='checkbox' title='체크박스' id='prj_chk_m_"+map.usrId+"' name='prj_chk_m_"+map.usrId+"' value='" + map.usrId + "'/><label for='prj_chk_m_"+map.usrId+"'></label></td>"
								+		"<td class='middle_cn m_td2'>" + map.usrId + "</td>"
								+		"<td class='middle_cn m_td3' title="+map.usrNm+">" + gfnCutStrLen(map.usrNm, 20) + "</td>"
								+		"<td class='middle_cn hidden_td'></td>"
								+		"<td class='middle_cn hidden_td'></td>"
								+		"<td class='middle_cn hidden_td'></td>"
								+	"</tr>"
								);
		
	});
	
	$.each(data.prjUsrAllListMap, function(idx, map){
		var email = gfnReplace(map.email, null, "");
		var telno = gfnReplace(map.telno, null, "");
		var etc = gfnCutStrLen(map.etc, "14");
		
		$("#allUsrTblBody").append(	"<tr id='tr" + map.usrId + "'>" 
								+		"<td class='right_cn prj_chk block_td'><input type='checkbox' title='체크박스' id='prj_chk_r_"+map.usrId+"' name='prj_chk_r_"+map.usrId+"' value='" + map.usrId + "'/><label for='prj_chk_r_"+map.usrId+"'></label></td>"
								+		"<td class='right_cn m_td2'>" + map.usrId + "</td>"
								+		"<td class='right_cn m_td3' title="+map.usrNm+">" + gfnCutStrLen(map.usrNm, 20) + "</td>"
								+		"<td class='right_cn hidden_td'>" + email + "</td>"
								+		"<td class='right_cn hidden_td'>" + telno + "</td>"
								+		"<td class='right_cn hidden_td'>" + etc.replace(/</gi,'&lt;').replace(/>/gi,'&gt;') + "</td>"
								+	"</tr>"
								);
		
	});
	
	// 배정 삭제이고, 배정 삭제된 사용자 정보가 있을경우
	// prjDelUsrList 배열의 값을 이용하여 우측 화면을 그린다.
	if(status == "D" && data.prjDelUsrListMap != null){
		
		$("#allUsrTblBody").children().remove();
		
		$.each(prjDelUsrList, function(idx, map){
			var email = gfnReplace(map.email, null, "");
			var telno = gfnReplace(map.telno, null, "");
			var etc = gfnCutStrLen(map.etc, "14");
				
			$("#allUsrTblBody").append(	"<tr id='tr" + map.usrId + "'>" 
										+		"<td class='right_cn prj_chk block_td'><input type='checkbox' title='체크박스' id='prj_chk_r_"+map.usrId+"' name='prj_chk_r_"+map.usrId+"' value='" + map.usrId + "'/><label for='prj_chk_r_"+map.usrId+"'></label></td>"
										+		"<td class='right_cn m_td2'>" + map.usrId + "</td>"
										+		"<td class='right_cn m_td3' title="+map.usrNm+">" + gfnCutStrLen(map.usrNm, 20) + "</td>"
										+		"<td class='right_cn hidden_td'>" + email + "</td>"
										+		"<td class='right_cn hidden_td'>" + telno + "</td>"
										+		"<td class='right_cn hidden_td'>" + etc + "</td>"
										+	"</tr>"
										); 
			
		});
	
	}
	
	
}

/* 배열 내부의 중복된 값을 제거한다. */
function uniqurArray(array){
    var conArr = array.concat();
    for(var i=0; i<conArr.length; i++) {
        for(var j=i+1; j<conArr.length; j++) {
            if(conArr[i].usrId === conArr[j].usrId){
         	   conArr.splice(j--, 1);
            }
        }
    }

    return conArr;
}


/********************************************************************
*	프로젝트 사용자 배정 부분 정의 종료											*		
*********************************************************************/	

	// 검색옵션(select) 변경시 이벤트 처리함수
	function fnChangeElement(){

		var txtElmt = $("#searchTxt");
		var searchOpt = $("#searchSelect_usr").val();

		if(searchOpt == "rn"){
			txtElmt.attr("readonly","readonly");
			txtElmt.attr("class","readonly");
			txtElmt.val("");
		}else{
			txtElmt.removeAttr("readonly");
			txtElmt.removeClass("readonly");
			txtElmt.val("");
		}
	}


</Script>

<div class="main_contents">
	<div class="tab_title">${sessionScope.selMenuNm }</div>
	<div class="tab_menu">
		<ul class="tab_box">
			<li class="on"><a>업무역할 사용자 배정</a></li>
		</ul>	
	</div>	
	
	<div class="tab_contents authority">
		<div class="top_control_wrap">
			<!-- <span class="button_normal2 btn_save" id="btn_update_prjUsrAdd">저장</span> -->
		</div>
		
		<div class="all_table_div">
			<div class="left_td_div">
				<div class="left_td_title">역할 그룹</div>
				<table class="left_tb">
					<caption>역할 그룹</caption>
					<thead>
						<tr>
							<th class="left_table_tt">그룹 명</th>
						</tr>
					</thead>
					<tbody>
						<!-- 프로젝트 사용자 배정 권한그룹 뿌리기 -->					
						<c:forEach items="${requestScope.prjAuthGrpList }" var="map">
							<tr id="${map.authGrpId}">
								<td class="left_cn l_td1" title="${map.authGrpDesc}">${map.authGrpNm}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
			<div class="middle_td_div fl">
				<div class="middle_td_title">배정 사용자</div>
				<table class="middle_table mtb">
					<caption>배정 사용자</caption>
					<thead>
						<tr>
							<th class="middle_table_tt m_wd1">
								<div class="prj_chk">
									<input type="checkbox" title="체크박스" id="middle_table_all" /><label for="middle_table_all"></label>
								</div>
							</th>
							<th class="middle_table_tt m_wd2 ">아이디</th>
							<th class="middle_table_tt m_wd3">성명</th>
						</tr>
					</thead>
					
					<!-- 배정된 사용자 목록 -->
					<tbody id="addUsrTblBody">
						
					</tbody>
				</table>
			</div>
			
			<div class="btn_td_div fl">
				<div class="button_check btn_left" id="btn_insert_usr"><img src="/images/contents/left_blue.png" alt="왼쪽 화살표" style="margin-right: 5px;">추가</div>
				<div class="button_check btn_right" id="btn_delete_usr">삭제<img src="/images/contents/right_blue.png" alt="오른쪽 화살표" style="margin-left: 5px;"></div>
			</div>
			
			<div class="right_td_div fl">
				<div class="right_td_title">등록된 사용자 목록</div>
				<table class="right_table rtb">
					<caption>등록된 사용자 목록</caption>
					<thead>
						<tr>
							<th class="right_table_tt" id="search_th" colspan="6" >
								<div class="search_div">
									<span class="button_normal2 fl" id="btn_search_usr"><i class="fa fa-search"></i></span>
									<input type="text" title="검색" id="searchTxt" class="readonly" readonly="readonly" />
									<span class="span_searchSelect">
										<select id="searchSelect_usr" name="searchSelect_usr">
											<option value="rn">전체 보기</option>
											<option value="usrId">아이디</option>
											<option value="usrNm">성명</option>
											<option value="deptNm">소속</option>	
										</select>
									</span>
								</div>
							</th>
						</tr>
						<tr>
							<th class="right_table_tt prj_wd1 block_td">
								<div class="prj_chk">	
									<input type="checkbox" title="체크박스" id="right_table_all" /><label for="right_table_all"></label>
								</div>
							</th>
							<th class="right_table_tt prj_wd2 block_td">아이디</th>
							<th class="right_table_tt prj_wd3 block_td" >성명</th>
							<th class="right_table_tt prj_wd4">이메일</th>
							<th class="right_table_tt prj_wd5">연락처</th>
							<th class="right_table_tt prj_wd6">비고</th>
						</tr>
					</thead>
					
					<!-- 미배정된 사용자 목록 -->
					<tbody id="allUsrTblBody">
						
					</tbody>
				</table>
			</div>
		</div>		
	</div>
	
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />