<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>
<style type="text/css">
	.accptFont{color:#4b73eb !important;text-shadow: none !important;}
	.rejectFont{color:#eb4b6a !important;text-shadow: none !important;}
	.defaultFont{color:#000 !important;}
</style>
<script>
var mySearch;
var prjStartDt = '${currPrjInfo.startDt}';
var prjEndDt = '${currPrjInfo.endDt}';

$(function(){
	//타겟이 상세정보 창이 아닌 경우 상세 정보 창 닫기
	$(document).click(function(event){

	});	

	//그리드 검색 호출
	fnAxGrid5View();
	// 검색상자 호출시 그리드 데이터 조회
	fnSearchBoxControl();
});

/* 요구사항 상세 삭제 */
var fnDeleteReqInfo = function(chkList){
	var params = "";
	var rowChk = false;
	
	var delCount = 0;
	if(gfnIsNull(chkList)){
		jAlert("선택한 요청사항이 없습니다.","알림창");
		return false;
	}

	$(chkList).each(function(idx, val){
		
		// 처리유형이 접수 요청 상태가 아니라면 
		if( val.reqProType != "01"){
			rowChk = true;
			return false;
		}
		if(delCount==0){
			params ="reqId="+val.reqId+"&atchFileId="+val.atchFileId+"&reqProType="+val.reqProType;
		}else{
			params +="&reqId="+val.reqId+"&atchFileId="+val.atchFileId+"&reqProType="+val.reqProType;
		}
		
		delCount++;
	});
	
	if(rowChk){
		jAlert('접수요청인 요청사항만 삭제할 수 있습니다.','알림창');
		return false;
	}
	if(delCount===0){
		jAlert("선택한 요청사항이 없습니다.","알림창");
		return false;
	}

	jConfirm("요청사항을 삭제 하시겠습니까?", "알림창", function( result ) {
		if( result ){
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req1000/req1000/deleteReq1001ReqInfoAjax.do'/>"}
					,params);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
	        	if( data.successYn == "Y" ){
					jAlert(data.message, '알림창', function( result ) {
						if( result ){
							fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
						}
					});
	        	}else{
	        		
	        		toast.push(data.message);
	        	}
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
	        	toast.push(data.message);
			});
			//AJAX 전송
			ajaxObj.send();
		}
	});
	
}; 

//검색조건 변경되었을때 이벤트
function fnSelectChg(){
	var selVal = $("#searchSelect option:selected").val();
	if(selVal == '0'){
		$("#searchTxt").val("");
		$("#searchTxt").attr("readonly", true);
	}else{
		$("#searchTxt").attr("readonly", false);
	}
}

//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            showRowSelector: true,
            sortable:true,
            header: {align:"center"},
            frozenColumnIndex: 3,
            columns: [
				{key: "prjNm", label: "프로젝트명", width: '10%', align: "center"},
				{key: "reqOrd", label: "순번", width: '7%', align: "center"},
				{key: "reqProTypeNm", label: "처리유형", width: '9%', align: "center"},
				{key: "reqNm", label: "요청 제목", width: '25%', align: "left"},
				{key: "reqDesc", label: "요청 내용", width: '38%', align: "left"},
				{key: "reqDtm", label: "요청일", width: '10%', align: "center"},
				{key: "reqUsrNm", label: "요청자", width: '10%', align: "center"},
				{key: "regDtmDay", label: "등록일", width: '10%', align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	// 클릭 이벤트 처리
                },onDBLClick:function(){

                	var prjId = this.item.prjId;
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
                			"popupPrjId":prjId,
             				"reqId":reqId,
                			"reqPageType" : "usrReqPage"
                	}; 
  
                	var popHeight = "890";
                	
                	// 처리유형이 반려일 경우
                	if(reqProType == "03"){
                		popHeight = "930";
                	}
                	
                	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', popHeight,'scroll');
                	
                	
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
					{type: "reqDetail", label: "상세보기", icon:"<i class='fa fa-info-circle' aria-hidden='true'></i>"},
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
                	
                	// 상세보기시 req4104 화면 호출
                	if(item.type == "reqDetail"){
                		 
                		 var data = {
         						"mode": "req", 
         						"reqId": selItem.reqId,
         						"prjId": selItem.prjId
         					}; 
                		 
                		gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');

                    //열 고정
                	}else if(item.type == "rowFrozen"){
                    	//이미 해당 열에 고정인경우 중지
                    	if(firstGrid.config.frozenColumnIndex != (param.colIndex+1)){
                    		firstGrid.setConfig({frozenColumnIndex:param.colIndex+1});
                    		fnInGridListSet(firstGrid.page.currentPage);
                    	}
                    }
                    firstGrid.contextMenu.close();
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
     	
     	// 요구사항 요청에서는  신규 요청한 요구사항만 조회해야 하므로
     	// mode값 추가해서 전송
     	ajaxParam += "&mode=newReq";
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req1000/req1000/selectReq1000ListAjaxView.do'/>","loadingShow":true}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			// 조회 실패
	    	if(data.errorYn == 'Y'){ 
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
							{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_print_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
								onclick:function(){
									$(firstGrid.exportExcel()).printThis({importCSS: false,importStyle: false,loadCSS: "/css/common/printThis.css"});
							}},
							{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
								onclick:function(){
									// 엑셀 다운로드
									fnExcelDownLoad();
							}},
							{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_delete_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
							onclick:function(){
								var chkList = firstGrid.getList('selected');

								fnDeleteReqInfo(chkList);
							}},
							{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_update_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
							onclick:function(){
								var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
								
								if(gfnIsNull(item)){
									toast.push('먼저 요청사항을 선택해주세요');
									return;
								}

								var selPrjId = item.prjId;
								var selReqId = item.reqId;
								var reqProType = item.reqProType;

								if(reqProType != "01"){
									jAlert('접수 요청중인 요청사항만 수정 가능합니다.','알림창');
									return false;
								}
								
								var data = {
										"popupPrjId": selPrjId,
										"reqId": selReqId,
										"type" : "update",
								};
								gfnLayerPopupOpen('/req/req1000/req1000/selectReq1001View.do',data,"640","845",null);
							}},
							{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_insert_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
							onclick:function(){
								var data = {
									"type": "insert"
								};
								
								gfnLayerPopupOpen('/req/req1000/req1000/selectReq1001View.do',data,"640","845",null);
							}},
							{label:"", labelWidth:"", type:"button",style:"float:right;", width:"55", key:"btn_search_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
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
					            
							}}							
						]},
						{display:true, addClass:"", style:"", list:[
						{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"30", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
							options:[
                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
                                {optionValue:'reqNm', optionText:'요청 제목'},
                                {optionValue:'reqDesc', optionText:'요청 내용'},
                                {optionValue:"reqProType", optionText:"처리유형", optionCommonCode:"REQ00008"},
                                {optionValue:'prjNm', optionText:'프로젝트명'},
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
    						},

						},
						{label:"", labelWidth:"", type:"inputText", width:"120", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
							onkeyup:function(e){
								if(e.keyCode == '13' ){
									axdom("#" + mySearch.getItemId("btn_search_req")).click();
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
						{label:"<i class='fas fa-arrows-alt-v'></i>&nbsp;목록 높이&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"gridHeight", addClass:"", valueBoxStyle:"", value:"600",
							options:[
							         	{optionValue:300, optionText:"300px"},
		                                {optionValue:600, optionText:"600px"},
		                                {optionValue:1000, optionText:"1000px"},
		                                {optionValue:1200, optionText:"1200px"},
		                                {optionValue:2000, optionText:"2000px"},
		                                
		                            ],onChange: function(selectedObject, value){
		                            	firstGrid.setHeight(value);
		    						}
						}
					]}
				]
			});
		}
		/*,
		search1: function(){
			var pars = mySearch.getParam();
			fnAxGridView(pars);
		}
		*/
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
		
		var pars = mySearch.getParam();
		fnInGridListSet(0,pars);
	});
}


/* 사용자 요구사항 등록 엑셀 다운로드 */
function fnExcelDownLoad(){ 

	var searchSelect = axdom("#" + mySearch.getItemId("searchSelect")).val();	// 검색 콤보박스
	var searchCd = axdom("#" + mySearch.getItemId("searchCd")).val();			// 검색어 대신 콤보박스에서 선택하는 검색조건 (처리유형)
	var searchTxt = axdom("#" + mySearch.getItemId("searchTxt")).val();			// 검색어
	var srchFromDt = axdom("#" + mySearch.getItemId("srchFromDt")).val();		// 검색 시작일
	var srchToDt = axdom("#" + mySearch.getItemId("srchToDt")).val();			// 검색 종료일
	
	var excelForm = document.getElementById("req1000_excel_down_Form");
	
	excelForm.searchSelect.value = searchSelect;
	excelForm.searchTxt.value = searchTxt;
	excelForm.searchCd.value = searchCd;
	excelForm.srchFromDt.value = srchFromDt;
	excelForm.srchToDt.value = srchToDt;
	excelForm.action = "<c:url value='/req/req1000/req1000/selectReq1000ExcelList.do'/>";
	excelForm.submit();
	return false;
}


</script>


		<div class="main_contents">
			<form:form commandName="req1000VO" id="req1000_excel_down_Form" name="req1000_excel_down_Form" method="post" onsubmit="return false;">
				<input type="hidden" name="searchSelect">
				<input type="hidden" name="searchTxt">
				<input type="hidden" name="srchFromDt">
				<input type="hidden" name="srchToDt">
				<input type="hidden" name="searchCd">
			</form:form>
			<form:form commandName="req1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
			</form:form>
			<div class="req_title">${sessionScope.selMenuNm }</div>
			<div class="tab_contents menu">
				<input type="hidden" name="strInSql" id="strInSql" />
				<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
				<br />
				<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>	
			</div>
		</div>

		<script>
			$(document).find(".bottom_line label").css({display:"none"});
		</script>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />