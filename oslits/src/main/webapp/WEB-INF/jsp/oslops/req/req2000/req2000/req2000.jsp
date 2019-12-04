<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>

<style>
.search_select select { font-size: 0.85em; }
.search_select {    width: 124px;    height: 28px;    margin: 0 5px 5px 0;}
.search_box_wrap {    width: calc(100% - 404px);} /* width:calc(100% - (.search_select너비 * 갯수 + 32px))  124*3+32=404 */
.columnShowNone{display:none;}
</style>

<script>

var viewGb = "${viewGb}";
var flowList = '${flowList}';
var sprintList = '${sprintList}';
var reqUsrNm  = '${param.reqUsrNm}';

//그리드
var firstGrid;
var mySearch;

//선택 요구사항 ID
var sel_reqId = null;
$(document).ready(function(){

	//그리드 및 검색 상자 호출
	fnAxGrid5View();
	// 검색상자 호출시 그리드 데이터 조회
	fnSearchBoxControl();
});

//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            sortable:false,
            header: {align:"center"},
            frozenColumnIndex: 3,
            columns: [
				{key: "reqOrd", label: "순번", width: '7%', align: "center"},
				{key: "reqProTypeNm", label: "처리유형", width: '9%', align: "center"},
				{key: "reqNm", label: "요구사항 명", width: '20%', align: "left"},
				{key: "reqDesc", label: "요구사항 설명", width: '30%', align: "left"},
				{key: "reqNo", label: "공문 번호", width: '8%', align: "center"},
				{key: "reqNewTypeNm", label: "접수유형", width: '9%', align: "center"},
				{key: "reqUsrNm", label: "요청자", width: '8%', align: "center"},
				{key: "reqChargerNm", label: "담당자", width: '8%', align: "center"},
				{key: "reqDtm", label: "요청일자", width: '8%', align: "center"},
				{key: "reqId", label: "요구사항 ID", width: '12%', align: "center"},
				{key: "processNm", label: "프로세스 명", width: '8%', align: "center"},
				{key: "flowNm", label: "작업흐름 명", width: '8%', align: "center"},
				{key: "reqStDtm", label: "실제 작업 시작일자", width: '10%', align: "center"},
				{key: "reqEdDtm", label: "실제 작업 종료일자", width: '10%', align: "center"},
				{key: "reqStDuDtm", label: "작업 시작 예정일자", width: '10%', align: "center"},
				{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: '10%', align: "center"},
				{key: "reqExFp", label: "예상 FP", width: '8%', align: "center"},
				{key: "reqFp", label: "FP", width: '8%', align: "center"},
				{key: "reqTypeNm", label: "요구사항 유형", width: '8%', align: "center"},
				{key: "sclNm", label: "시스템 구분", width: '12%', align: "center"},
				{key: "piaNm", label: "성능개선 활동여부", width: '12%', align: "center"},
				{key: "labInp", label: "투입인력", width: '8%', align: "center"},
				{key: "orgReqId", label: "프로젝트별 요구사항ID", width: '12%', align: "center"}
			],
            body: {
                align: "center",
                columnHeight: 30,
               onDBLClick:function(){
 
					var reqId = this.item.reqId;
					var reqProType = this.item.reqProType;
					
					/*
                	 * reqPageType 추가
                	 * 요구사항 상세보기(req1002.jsp)에서 항목명 구분을 위해 사용
                	 * usrReqPage - 요구사항 요청(사용자) 
                	 * admReqPage - 전체 요구사항 목록, 요구사항 생성관리(관리자)
                	 */
					var data = {
						"mode": "req", 
						"reqId": reqId,
						"reqPageType": "admReqPage"
					}; 
					
					var popHeight = "800";
						
					// 처리유형이 접수요청(01), 반려(03)일 경우 req1002.jsp 상세보기 화면으로
					if(reqProType == "01" || reqProType == "03"){
						
						reqProType == "03" ? popHeight = "930" : popHeight = "890";
						
						gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', popHeight,'scroll');	
					}else{
						// 그외에는 req4104.jsp 상세보기 화면으로
						gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
					}
               
               }
            } ,
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                   fnInGridListSet(this.page.selectPage,mySearch.getParam());
                }
            },
            contextMenu: {
                iconWidth: 20,
                acceleratorWidth: 100,
                itemClickAndClose: false,
                icons: {
                    'arrow': '<i class="fa fa-caret-right"></i>'
                },
                items: [
					{type: "reply", label: "쪽지 전송", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
                   	{divide: true},
                    {type: "rowFrozen", label: "열 고정", icon:"<i class='fa fa-lock' aria-hidden='true'></i>"}
                ],
                popupFilter: function (item, param) {
                	var selItem = firstGrid.list[param.doindex];
                	//선택 개체 없는 경우 중지
                	if(typeof selItem == "undefined"){
                		return false;
                	}
                	return true;
                },
                onClick: function (item, param) {
                	var selItem = firstGrid.list[param.doindex];
					//열 고정
                    if(item.type == "rowFrozen"){
                    	//이미 해당 열에 고정인경우 중지
                    	if(firstGrid.config.frozenColumnIndex != (param.colIndex+1)){
                    		firstGrid.setConfig({frozenColumnIndex:param.colIndex+1});
                    		fnInGridListSet(firstGrid.page.currentPage);
                    	}
                    //쪽지 전송
                    }else if(item.type == "reply"){
                    	
                    	var item = param.item;
                    	
                    	// 담당자가 없을경우
                    	/* if( gfnIsNull(item.reqChargerId) ){
                    		toast.push('담당자가 지정되어 있지 않아 쪽지를 전송할 수 없습니다.');
							return;
                    	} */
                    	
                    	gfnAlarmOpen(item.reqChargerId,item.reqId,item.reqNm);
                    }
                    firstGrid.contextMenu.close();
                }
            }
        });
        //그리드 데이터 불러오기
 		//fnInGridListSet();

}
//그리드 데이터 넣는 함수
function fnInGridListSet(_pageNo,ajaxParam){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	if(gfnIsNull(ajaxParam)){
   			ajaxParam = $('form#searchFrm').serialize();
   		}
     	
     	//페이지 세팅
     	if(!gfnIsNull(_pageNo)){
     		ajaxParam += "&pageNo="+_pageNo;
     	}else if(typeof firstGrid.page.currentPage != "undefined"){
     		ajaxParam += "&pageNo="+firstGrid.page.currentPage;
     	}
     	
     	//req3000 담당자 요구사항 목록인 경우
	    if(viewGb == "charge"){
	    	ajaxParam += "&viewGb="+viewGb;
	    }
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req2000/req2000/selectReq2000ListAjaxView.do'/>","loadingShow":true}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);

			// 조회 실패
	    	if(data.selectYN == 'N'){ 
	    		toast.push(data.message);
	    		return;
	    	}
			
			var list = data.list;
			var page = data.page;
			
		   	firstGrid.setData({
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
			
           	data = JSON.parse(data);
			jAlert(data.message, "알림");
		});
		
		//AJAX 전송
		ajaxObj.send();
}

//검색 상자
function fnSearchBoxControl(){
	var pageID = "AXSearch";
	mySearch = new AXSearch();
	// 현재일과 현재일 기준 한달전 날짜 기본세팅
	var defaultStDt = new Date(new Date().setMonth(new Date().getMonth()-1)).format('yyyy-MM-dd');
	var defaultEndDt = new Date().format('yyyy-MM-dd');

	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySearch.setConfig({
				targetID:"AXSearchTarget",
				theme : "AXSearch",
				rows:[
					{display:true, addClass:"", style:"", list:[
						{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"30", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
							options:[
		                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
		                                {optionValue:'reqNm', optionText:'요구사항 명'},
		                                {optionValue:'reqDesc', optionText:'요구사항 내용'},
		                                {optionValue:'reqUsrNm', optionText:'요청자'},
		                                {optionValue:'reqChargerNm', optionText:'담당자'},
		                                {optionValue:'processNm', optionText:'프로세스 명'},
		                                {optionValue:'reqNo', optionText:'공문번호'},
		                                {optionValue:'reqId', optionText:'요구사항 ID'},
		                                {optionValue:"reqProType", optionText:"처리유형", optionCommonCode:"REQ00008"},
		                                {optionValue:"reqNewType", optionText:"접수유형", optionCommonCode:"REQ00009"},
		                                {optionValue:"reqTypeCd", optionText:"요구사항 유형", optionCommonCode:"REQ00012"},
		                                {optionValue:'reqOrd', optionText:'순번'}
		                                
		                            ],onChange: function(selectedObject, value){
		                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
		    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
											axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");	
											axdom("#" + mySearch.getItemId("searchTxt")).val('');	
										}else{
											axdom("#" + mySearch.getItemId("searchTxt")).removeAttr("readonly");
										}
										
										//공통코드 처리 후 select box 세팅이 필요한 경우 사용
										if(!gfnIsNull(selectedObject.optionCommonCode)){
											gfnCommonSetting(mySearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
										}else{
											//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
											axdom("#" + mySearch.getItemId("searchTxt")).show();
											axdom("#" + mySearch.getItemId("searchCd")).hide();
										}
		    						}
						},
						{label:"", labelWidth:"", type:"inputText", width:"150", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
							onkeyup:function(e){
								if(e.keyCode == '13' ){
									axdom("#" + mySearch.getItemId("btn_search_newReqDemand")).click();
								}
							}
						},
						{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
							options:[]
						},
						{label : "시작일",labelWidth : "70",type : "inputText",width : "150",key : "srchFromDt",addClass : "secondItem sendBtn",valueBoxStyle : "",value : defaultStDt,
						},
						{label : "종료일",labelWidth : "70",type : "inputText",width : "150",key : "srchToDt",addClass : "secondItem sendBtn",valueBoxStyle : "",value : defaultEndDt,
						},
						{label:"<i class='fas fa-list-ol'></i>&nbsp;목록 수&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"pageSize", addClass:"", valueBoxStyle:"", value:"30",
							options:[
							         	{optionValue:15, optionText:"15"},
		                                {optionValue:30, optionText:"30"},
		                                {optionValue:50, optionText:"50"},
		                                {optionValue:100, optionText:"100"},
		                                {optionValue:300, optionText:"300"},
		                                {optionValue:600, optionText:"600"},
		                                {optionValue:1000, optionText:"1000"},
		                                {optionValue:5000, optionText:"5000"},
		                                {optionValue:10000, optionText:"10000"},
		                                
		                            ],onChange: function(selectedObject, value){
		                            	fnInGridListSet(0,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
		    						}
						},
						{label:"<i class='fas fa-arrows-v'></i>&nbsp;목록 높이&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"gridHeight", addClass:"", valueBoxStyle:"", value:"600",
							options:[
							         	{optionValue:300, optionText:"300px"},
		                                {optionValue:600, optionText:"600px"},
		                                {optionValue:1000, optionText:"1000px"},
		                                {optionValue:1200, optionText:"1200px"},
		                                {optionValue:2000, optionText:"2000px"},
		                                
		                            ],onChange: function(selectedObject, value){
		                            	firstGrid.setHeight(value);
		    						}
						},
						
           				{label:"", labelWidth:"", type:"button", width:"70", key:"btn_print_newReqDemand",style:"float:right;",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
           						onclick:function(){
           							$(firstGrid.exportExcel()).printThis({importCSS: false,importStyle: false,loadCSS: "/css/common/printThis.css"});
           				}},
						{label:"", labelWidth:"", type:"button", width:"55", key:"btn_excel_newReqDemand",style:"float:right;",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
       						onclick:function(){
       							// 엑셀 다운로드 함수 호출
       							fnExcelDownLoad();
       							// 기존방식 - ax5grid의 함수 호출하여 엑셀 다운로드 하는방법
       							//firstGrid.exportExcel("all_request_list.xls");
       						}},
           				{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_newReqDemand",style:"float:right;",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
						onclick:function(){
							/* 검색 조건 설정 후 reload */
 							var pars = mySearch.getParam();
						    var ajaxParam = $('form#searchFrm').serialize();

						    if(!gfnIsNull(pars)){
						    	ajaxParam += "&"+pars;
						    }
							
				            fnInGridListSet(0,ajaxParam);
				            
				            //폼 데이터 변경
							$('#searchSelect').val(axdom("#" + mySearch.getItemId("searchSelect")).val());
							$('#searchCd').val(axdom("#" + mySearch.getItemId("searchCd")).val());
							$('#searchTxt').val(axdom("#" + mySearch.getItemId("searchTxt")).val());
				            
						}},
					]}
				]
			});
		}
	};
	
	jQuery(document.body).ready(function(){
		fnObjSearch.pageStart();
		//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
		axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");
		
		//공통코드 selectBox hide 처리
		axdom("#" + mySearch.getItemId("searchCd")).hide();
		
		//버튼 권한 확인
		fnBtnAuthCheck(mySearch);

		//기간 검색 달기
		gfnCalRangeSet(mySearch.getItemId("srchFromDt"), mySearch.getItemId("srchToDt"));
	  	
	    if(!gfnIsNull(reqUsrNm)){
	    	//option 초기화
	    	axdom("#" + mySearch.getItemId("searchTxt")).html('');
	    	axdom("#" + mySearch.getItemId("searchTxt")).removeAttr("readonly");
			//파라미터 세팅
	    	axdom("#" + mySearch.getItemId("searchSelect")).val('reqChargerNm');
	    	axdom("#" + mySearch.getItemId("searchTxt")).val(reqUsrNm);
	    	
			
			axdom("#" + mySearch.getItemId("searchTxt")).show();
			axdom("#" + mySearch.getItemId("searchCd")).hide();
			
			//그리드 검색 호출
			var pars = mySearch.getParam();
			fnInGridListSet(0,pars);
	    }else{
		 	// 화면 로드 시 그리드 데이터 불러오기
			// 기본 세팅된 날짜를 이용해서 요구사항 조회
			axdom("#" + mySearch.getItemId("srchFromDt")).val();
			axdom("#" + mySearch.getItemId("srchToDt")).val();
			
			var pars = mySearch.getParam();
			fnInGridListSet(0,pars);
	    }
		
	});
}

/* 전체 요구사항 목록 엑셀 다운로드 */
function fnExcelDownLoad(){ 

	var searchSelect = axdom("#" + mySearch.getItemId("searchSelect")).val();	// 검색 콤보박스
	var searchCd = axdom("#" + mySearch.getItemId("searchCd")).val();			// 검색어 대신 콤보박스에서 선택하는 검색조건 (처리유형)
	var searchTxt = axdom("#" + mySearch.getItemId("searchTxt")).val();			// 검색어
	var srchFromDt = axdom("#" + mySearch.getItemId("srchFromDt")).val();		// 검색 시작일
	var srchToDt = axdom("#" + mySearch.getItemId("srchToDt")).val();			// 검색 종료일
	
	var excelForm = document.getElementById("req2000_excel_down_Form");
	
	excelForm.searchSelect.value = searchSelect;
	excelForm.searchTxt.value = searchTxt;
	excelForm.searchCd.value = searchCd;
	excelForm.srchFromDt.value = srchFromDt;
	excelForm.srchToDt.value = srchToDt;
	excelForm.action = "<c:url value='/req/req2000/req2000/selectReq2000ExcelList.do'/>";
	excelForm.submit();
	return false;
}

</script>

<div class="main_contents" style="height: auto;" >
	<div class="main_pc"> <!-- pc, 테블릿 화면 -->
		<form:form commandName="req4100VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
			<input path="sprintId"	type="hidden" id="sprintId" 	name="sprintId" 	value="${sprintId}" />
			<input path="flowId" 	type="hidden" id="flowId" 		name="flowId" 		value="${flowId}" />
		</form:form>
		<form id="req2000_excel_down_Form" method="post">
			<input type="hidden" name="searchSelect">
			<input type="hidden" name="searchTxt">
			<input type="hidden" name="srchFromDt">
			<input type="hidden" name="srchToDt">
			<input type="hidden" name="searchCd">
		</form>
		<div class="req_title">${sessionScope.selMenuNm}</div>	
		<div class="tab_contents menu">
			<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
			<br/>
			<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
		</div>
	</div>	
	<!-- pc, 테블릿 화면 -->
	
</div>
	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />