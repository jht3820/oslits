<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style type="text/css">
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
.button_normal { width: 39px; height: 22px; line-height: 22px; text-align: center; font-weight: bold; font-size: 1em; border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; margin: 0 auto; border: 1px solid #b8b8b8; cursor: pointer; }
div.pop_sub .pop_left {width:28%;} /* common.css pop_left width값 오버라이딩 */
div.pop_sub .pop_right {width:72%;} /* common.css pop_left width값 오버라이딩 */
.input_txt {padding-left: 5px;}
</style>
<script type="text/javascript">

	var popSearch;
	var popGrid  = {
			init : function() {
				//그리드 및 검색 상자 호출
				fnAxGridSet();  // Grid 초기화  설정
			}
	};

	// 호출  정보
	// 통합대시보드에서 요구사항 상세정보 호출 시 해당 정보를 요구사항 상세팝업(req4104)에 전달
	// 요구사항 상세정보 팝업에서는 리비전, 배포권한 정보 조회시 호출 팝업 정보를 가지고 selPrjId를 사용할지 아니면 해당 팝업에 있는 
	// 프로젝트 ID를 사용할지 판단한다.
	var callView = "${param.callView}"
	// 프로젝트 ID
	var projectId = "${param.projectId}";
	// 프로젝트 명
	var projectNm = "${param.projectNm}";
	// 요구사항 처리유형
	var reqProType = "${param.reqProType}";
	// 월별 프로젝트 처리율 차트의 월
	var projectMonth = "${param.projectMonth}";
	// 분기별 프로젝트 처리율의 시작월
	var projectStartMonth = "${param.projectStartMonth}";
	// 분기별 프로젝트 처리율의 종료월
	var projectEndMonth = "${param.projectEndMonth}";
	// 신호등 구분 타입 - 초과, 임박, 여유, 실패, 적기
	var overType = "${param.overType}";
	// 프로세스 ID
	var processId = "${param.processId}";
	// 차트 종류 - 각 프로젝트 처리율 차트를 클릭 시 데이터 조회조건을 달리하기 위한 구분값
	var chartType = "${param.chartType}";
	// 프로젝트 그룹 ID
	var prjGrpId = "${param.projectGrpId}";

	$(document).ready(function() {

		popGrid.init(); // AXISJ Grid 초기화 실행 부분들
		
		/* 취소 */
		$('#btnPopCmm1000Cancle').click(function() {
			gfnLayerPopupClose();
		});
	});
	


	//axisj5 그리드
	function fnAxGridSet(){
		
		
		popGrid = new ax5.ui.grid();
 
        popGrid.setConfig({
            target: $('[data-ax5grid="pop-grid"]'),
            sortable:false,
            showRowSelector: false,
            
            header: {align:"center" },
            //frozenColumnIndex: 2,
            columns: [
            	{key: "prjGrpNm", label: "프로젝트 그룹 명", width: '22%', align: "left"},
            	{key: "prjNm", label: "프로젝트 명", width: '22%', align: "left"},
				{key: "reqOrd", label: "요구사항 순번", width: '13%', align: "center"},
				{key: "reqProTypeNm", label: "처리 상태", width: '13%', align: "center"},
				{key: "processNm", label: "프로세스 명", width: '20%', align: "center"},
				{key: "reqNm", label: "요청 명", width: '30%', align: "left"},
				{key: "reqDesc", label: "요청 내용", width: '35%', align: "left"},
				{key: "reqDtm", label: "요청일자", width: '12%', align: "center"},
				{key: "reqUsrNm", label: "요청자", width: '10%', align: "center"},
				{key: "reqUsrEmail", label: "이메일", width: '18%', align: "center"},
				{key: "reqUsrNum", label: "연락처", width: '13%', align: "center"},
				{key: "reqStDtm", label: "작업 시작일자", width: '15%', align: "center"},
				{key: "reqEdDtm", label: "작업 종료일자", width: '15%', align: "center"},
				{key: "reqStDuDtm", label: "작업 시작 예정일자", width: '17%', align: "center"},
				{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: '17%', align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onDBLClick: function () {
					// 프로젝트 ID를 같이 넘겨준다.
       				var data = {"callView" : callView,  "mode":"newReq", "popupPrjId":this.item.prjId , "reqId": this.item.reqId, "reqProType":"02"}; 
					
       				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
  
                }
            },
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                	fnInGridPopListSet(this.page.selectPage,"");
                }
            }
        });
        var ajaxParam = "";
		
        fnInGridPopListSet(0,ajaxParam);
	}
	
	//그리드 데이터 넣는 함수
	function fnInGridPopListSet(_pageNo,ajaxParam){
 
     	//페이지 세팅
    	if(!gfnIsNull(_pageNo)){
    		ajaxParam += "&pageNo="+_pageNo;
    	}else if(typeof popGrid.page.currentPage != "undefined"){
    		ajaxParam += "&pageNo="+popGrid.page.currentPage;
    	}

     	
     	// 프로젝트 id가 있을경우 파라미터 세팅
     	if(!gfnIsNull(projectId)){
    		ajaxParam += "&prjId="+projectId;
    	}
     	
     	// 프로젝트 명이 있을경우 파라미터 세팅
     	if(!gfnIsNull(projectNm)){
    		ajaxParam += "&prjNm="+projectNm;
    	}     	

     	// 요구사항 처리유형이있을 경우 파라미터 세팅
     	if(!gfnIsNull(reqProType)){
    		ajaxParam += "&reqProType="+reqProType;
    	}     	

     	// 월별 프로젝트 처리율 차트의 월이 있는 경우 파라미터 세팅
     	if(!gfnIsNull(projectMonth)){
    		ajaxParam += "&projectMonth="+projectMonth;
    	}     	

    	// 분기별 프로젝트 처리율의 시작월, 종료월이 있는경우 파라미터 세팅
     	if(!gfnIsNull(projectStartMonth)){
    		ajaxParam += "&projectStartMonth="+projectStartMonth+"&projectEndMonth="+projectEndMonth;
    	}     	

     	// 신호등 구분 타입이 있을경우 파라미터 세팅
     	if(!gfnIsNull(overType)){
    		ajaxParam += "&overType="+overType;
    	}     		

     	// 차트 종류가 있을경우 세팅
     	if(!gfnIsNull(chartType)){
    		ajaxParam += "&chartType="+chartType;
    	}  
     	
     	// 프로젝트 그룹 ID가 있을경우 파라미터 세팅
     	if(!gfnIsNull(prjGrpId)){
    		ajaxParam += "&prjGrpId="+prjGrpId;
    	}  

     	
    	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000ReqList.do'/>","loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			var page = data.page;
			
			popGrid.setData({
		             	list:list,
		             	page: {
		                  currentPage: _pageNo || 0,
		                  pageSize: page.pageSize,
		                  totalElements: page.totalElements,
		                  totalPages: page.totalPages
		              }
		             });

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
		
	<div class="pop_title"><c:out value="${param.popTitleMsg}" /> 요구사항 목록</div>
	<div class="pop_sub">
		<div class="tab_contents menu" > <!-- style="max-width:800px;" -->
			<form:form commandName="dsh3000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			
			<br />
			<div data-ax5grid="pop-grid" data-ax5grid-config="{}" style="height: 380px;"></div>		
		</div>
		<div class="btn_div">		
			<div class="button_normal exit_btn" id="btnPopCmm1000Cancle" >닫기</div>
		</div>
	</div>
	</form>
</div>
</html>