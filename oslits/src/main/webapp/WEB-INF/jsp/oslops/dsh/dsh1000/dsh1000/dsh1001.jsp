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
				fnAxGridSet();  // Grid 초기화  설정\
			}
	};

	// 호출  정보
	// 통합대시보드에서 요구사항 상세정보 호출 시 해당 정보를 요구사항 상세팝업(req4104)에 전달
	// 요구사항 상세정보 팝업에서는 리비전, 배포권한 정보 조회시 호출 팝업 정보를 가지고 selPrjId를 사용할지 아니면 해당 팝업에 있는 
	// 프로젝트 ID를 사용할지 판단한다.
	var callView = "${param.callView}"
	// 프로세스 명
	var processNm = "${param.processNm}";
	// 요구사항 처리유형
	var reqProType = "${param.reqProType}";
	// 프로세스 월
	var processMonth = "${param.processMonth}";
	// 프로세스 시작월
	var processStartMonth = "${param.processStartMonth}";
	// 프로세스 종료월
	var processEndMonth = "${param.processEndMonth}";
	// 신호등 구분 타입 - 초과, 임박, 여유, 실패, 적기
	var overType = "${param.overType}";
	// 프로세스 ID
	var processId = "${param.processId}";
	// 프로젝트 ID
	var projectId = "${param.projectId}";
	// 차트 종류 - 통합대시보드에서(dsh3000) 프로젝트별 요구사항 수 차트를 구분하기 위한값
	var chartType = "${param.chartType}";

	
	$(document).ready(function() {
		// 그리드 초기화
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
				{key: "reqOrd", label: "요구사항 순번", width: '13%', align: "center"},
				{key: "reqProTypeNm", label: "처리 상태", width: '13%', align: "center"},
				{key: "reqDtm", label: "요청일자", width: '12%', align: "center"},
				{key: "reqUsrNm", label: "요청자", width: '10%', align: "center"},
				{key: "reqUsrEmail", label: "이메일", width: '18%', align: "center"},
				{key: "reqUsrNum", label: "연락처", width: '13%', align: "center"},
				{key: "processNm", label: "프로세스 명", width: '20%', align: "center"},
				{key: "reqNm", label: "요청 명", width: '30%', align: "left"},
				{key: "reqDesc", label: "요청 내용", width: '35%', align: "left"},
				{key: "reqStDtm", label: "작업 시작일자", width: '15%', align: "center"},
				{key: "reqEdDtm", label: "작업 종료일자", width: '15%', align: "center"},
				{key: "reqStDuDtm", label: "작업 시작 예정일자", width: '17%', align: "center"},
				{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: '17%', align: "center"}
            ],
            body: {
	            align: "center",
	            columnHeight: 30,
	            onDBLClick: function () {
	  				// 요구사항 처리유형
	  				var reqProType = this.item.reqProType;

	  				// 요구사항의 처리유형이 접수요청 또는 반려일 경우
	  				if(reqProType == "01" || reqProType == "03"){
	  					var data = {"mode": "req", "popupPrjId":this.item.prjId, "reqId": this.item.reqId, "reqProType": this.item.reqProType,"reqPageType" : "usrReqPage"}; 
	  				    gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '890','scroll');
	  				}else{
	  				  	var data = {"callView" : callView, "mode":"newReq", "popupPrjId":this.item.prjId, "reqId": this.item.reqId, "reqProType":"02"}; 
	  	       			gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
	  				}
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
        var ajaxParam = "";//"&searchPopTxt=${param.usrNm}";
		
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

     	// 프로세스 명이 있을경우 파라미터 세팅
     	if(!gfnIsNull(processNm)){
    		ajaxParam += "&processNm="+processNm;
    	}     	

     	// 요구사항 처리유형이있을 경우 파라미터 세팅
     	if(!gfnIsNull(reqProType)){
    		ajaxParam += "&reqProType="+reqProType;
    	}     	

     	// 프로세스 월이 있을 경우 파라미터 세팅
     	if(!gfnIsNull(processMonth)){
    		ajaxParam += "&processMonth="+processMonth;
    	}     	

     	// 프로세스 시작,종료월이 있을경우 파라미터 세팅
     	if(!gfnIsNull(processStartMonth)){
    		ajaxParam += "&processStartMonth="+processStartMonth+"&processEndMonth="+processEndMonth;
    	}     	

     	// 신호등 구분 타입이 있을경우 파라미터 세팅
     	if(!gfnIsNull(overType)){
    		ajaxParam += "&overType="+overType;
    	}     	

     	// 프로세스 id가 있을경우 파라미터 세팅
     	if(!gfnIsNull(processId)){
    		ajaxParam += "&processId="+processId;
    	}     	
     	
     	// 프로젝트 id가 있을경우 파라미터 세팅
     	if(!gfnIsNull(projectId)){
    		ajaxParam += "&projectId="+projectId;
    	}
     	
     	// 차트 종류가 있을경우 세팅
     	if(!gfnIsNull(chartType)){
    		ajaxParam += "&chartType="+chartType;
    	}  
     	
    	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dsh/dsh1000/dsh1000/selectDsh1000ReqList.do'/>","loadingShow":false}
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
			<form:form commandName="dsh1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			
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