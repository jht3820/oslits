<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp"%>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/dpl.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>

<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<style type="text/css">
	.search_select select {font-size: 0.85em;}
	.search_select {width: 124px;height: 28px;margin: 0 5px 5px 0;}
	.search_box_wrap {width: calc(100% - 404px);} /* width:calc(100% - (.search_select너비 * 갯수 + 32px))  */
	.req_right_box {border-radius: 5px;}
	.req_left_table_wrap {width: 73%;}
	
	/* 모바일 @media 제거 */
	/*
	@media screen and (max-width: 1100px) {
		.search_box {height: 100%;}
		.search_select {width: 124px;}
		.search_select select {max-width: 124px;}
		.req_left_table_wrap {width: 100%;}
		.approval_btn {margin-right: 1%;}
	}
	*/
</style>

<script type="text/javascript">

	var mySearch;
	var Grid = {
		init : function() {
			//그리드 및 검색 상자 호출
			fnAxGrid5View(); // Grid 초기화  설정
			fnSearchBoxControl(); // Search Grid 초기화 설정
		},
		columnOption : {
			dpl1000Search : [ 
	                 {optionValue : "rn",optionText : "전체 보기",optionAll : true}, 
	                 {optionValue : "dplStsCd",optionText : '배포 상태' , optionCommonCode:"DPL00001" }, 
	                 {optionValue : "dplVer",optionText : '배포 버전'}, 
	                 {optionValue : "dplNm",optionText : '배포 명'}, 
	                 {optionValue : "dplTypeCd",optionText : '배포 방법' , optionCommonCode:"DPL00003"},
	                 {optionValue : "dplUsrNm",optionText : "배포자"},
	                 {optionValue : "dplDesc",optionText : "배포 설명"} 
	        		]
			}
		}

	/* 배포 정보  삭제 */
	var fnDeleteDplInfo = function(chkList) {
		if (gfnIsNull(chkList)) {
			jAlert("선택된 배포계획이 없습니다.", "알림창");
			return false;
		}
		
		jConfirm("배포 계획을 삭제하시겠습니까?</br>연관된 모든 정보가 삭제됩니다.","알림창",
				function(result) {
					if (result) {
						var params = "";
						var delCount = 0;
						var delSkipCount = 0;
						
						//삭제 dplId 세팅
						$(chkList).each(function(idx, val){
							//결재 대기중 삭제 불가능
							if( val.signStsCd == "01" ){
								delSkipCount++;
								return false;
							}
							
							if(delCount==0){
								params ="dplId="+val.dplId;
							}else{
								params +="&dplId="+val.dplId;
							}
							
							delCount++;
						});
						
						//삭제 배포 계획 없는 경우
						if(delCount===0){
							//삭제 제외 수
							if(delSkipCount > 0){
								jAlert('결재 대기중인 배포 계획은 삭제가 불가능합니다.</br>-> '+delSkipCount+'개의 배포계획 삭제 취소','알림창');
								return false;
							}
							
							jAlert("선택한 배포계획이 없습니다.","알림창");
							return false;
						}
										
						//AJAX 설정
						var ajaxObj = new gfnAjaxRequestAction(
								{"url" : "<c:url value='/dpl/dpl1000/dpl1000/deleteDpl1000DeployVerInfoListAjax.do'/>"}, 
								params);
						//AJAX 전송 성공 함수
						ajaxObj.setFnSuccess(function(data) {
							data = JSON.parse(data);
							if (data.successYn == "Y") {
								fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
								delSkipMsg ="";
								
								//결재 대기 취소건 있는 경우
								if(delSkipCount > 0){
									delSkipMsg = "</br>결재 대기 중인 "+delSkipCount+"개의 배포 계획 삭제를 취소했습니다.";
								}
								
								jAlert(data.message+delSkipMsg, "알림창");
							} else {
								toast.push(data.message);
							}
						});
						//AJAX 전송 오류 함수
						ajaxObj.setFnError(function(xhr, status, err) {
							data = JSON.parse(data);
							toast.push(data.message);
						});
						//AJAX 전송
						ajaxObj.send();
					}
				});

	};
	
	
	
	$(document).ready(function() {
		// AXISJ Grid 초기화 실행 부분들
		Grid.init(); 
		
		// 배포 계획 생성관리 가이드 상자 호출
		gfnGuideStack("add",fnDpl1000GuideShow);
		
	});

//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            showRowSelector: true,
            sortable:false,

            header: {align:"center"},
            columns: [
                {key : "signStsNm",label : "결재 상태",width : 100,align : "center"},
                {key : "dplStsNm",label : "배포 상태",width : 100,align : "center"},
                {key : "dplVer",label : "배포 버전",width : 100,align : "center"},
				{key : "dplNm",label : "배포 명",width : 260,align : "left"},
				{key : "dplTypeNm",label : "배포 방법",width : 120,align : "center"},
				{key : "dplDt",label : "배포 일자",width : 200,align : "center",
					formatter : function() {
						var fmtDt = this.item.dplDt;
						// IE에서는 날짜값 뒤에 시간값이 붙어있을경우 format("yyyy-MM-dd")이 정상동작 하지 않아 yyyy-MM-dd만 자른다.
						var fmtDtStr = fmtDt.substring(0, 10);
						return new Date(fmtDtStr).format("yyyy-MM-dd", true);
					}
				},
				{key : "dplUsrNm",label : "배포자",width :200,align : "center"},
				{key : "dplDesc",label : "배포 설명",width :350,align : "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
           			// 클릭 이벤트
                },onDBLClick:function(){
                	// 더블클릭 시 상세보기
                	var item = this.item;
                	var data = {"dplId" : item.dplId, "prjId" : item.prjId};
					gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1003View.do',data, "1200", "870",'scroll');
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
                   fnInGridListSet(this.page.selectPage,mySearch.getParam());
                }
            }
        });
        //그리드 데이터 불러오기
 		fnInGridListSet();

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
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl1000/dpl1000/selectDpl1000DeployVerInfoListAjax.do'/>","loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
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
	function fnSearchBoxControl() {
		mySearch = new AXSearch();

		var fnObjSearch = {
			pageStart : function() {
				//검색도구 설정 01 ---------------------------------------------------------
				mySearch.setConfig({
					targetID : "AXSearchTarget",
					theme : "AXSearch",
					rows : [ {
						display : true,
						addClass : "",
						style : "",
						list : [{label : "<i class='fa fa-search'></i>&nbsp;",labelWidth : "50",type : "selectBox",width : "",key : "searchSelect",addClass : "",valueBoxStyle : "",value : "all",
							options : Grid.columnOption.dpl1000Search,
								onChange : function(selectedObject,value) {
										//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
										if (!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true) {
											axdom("#"+ mySearch.getItemId("searchTxt")).attr("readonly","readonly");
											axdom("#"+ mySearch.getItemId("searchTxt")).val('');
										} else {
											axdom("#"+ mySearch.getItemId("searchTxt")).removeAttr("readonly");
										}
		
										//공통코드 처리 후 select box 세팅이 필요한 경우 사용
										if (!gfnIsNull(selectedObject.optionCommonCode)) {
											gfnCommonSetting(mySearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
										} else if (value == "flowId") {
											//option 초기화
											axdom("#"+ mySearch.getItemId("searchCd")).html('');
											//목록 불러오기
											axdom("#"+ mySearch.getItemId("searchCd")).append('<option value="ALL">전체</option>');
											$.each(JSON.parse(flowList),function() {
												axdom("#"+ mySearch.getItemId("searchCd")).append('<option value="'+this.flowId+'">'+ this.flowNm+ '</option>');
											});
											axdom("#"+ mySearch.getItemId("searchCd")).append('<option value="FLW">미분류</option>');
											axdom("#"+ mySearch.getItemId("searchTxt")).hide();
											axdom("#"+ mySearch.getItemId("searchCd")).show();
										} else if (value == "sprintId") {//option 초기화
											axdom("#"+ mySearch.getItemId("searchCd")).html('');
										//개발주기가 있는 경우 목록 조회
										if (gfnIsNull(sprintList)) {
											axdom("#"+ mySearch.getItemId("searchCd")).append('<option value="">없음</option>');
										} else {
											//목록 불러오기
											$.each(JSON.parse(sprintList),function() {
												axdom("#"+ mySearch.getItemId("searchCd")).append('<option value="'+this.sprintId+'">'+ this.sprintNm+ '</option>');
											});
										}
											axdom("#"+ mySearch.getItemId("searchTxt")).hide();
											axdom("#"+ mySearch.getItemId("searchCd")).show()
										} else {
											//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
											axdom("#"+ mySearch.getItemId("searchTxt")).show();
											axdom("#"+ mySearch.getItemId("searchCd")).hide();
										}
									}
									},
									{label : "",labelWidth : "",type : "inputText",width : "225",key : "searchTxt",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:0px;",value : "",
										onkeyup:function(e){
											if(e.keyCode == '13' ){
												axdom("#" + mySearch.getItemId("btn_search_dlp")).click();
											}
										}
									},
									{label : "",labelWidth : "",type : "selectBox",width : "100",key : "searchCd",addClass : "selectBox",valueBoxStyle : "padding-left:0px;",value : "01",options : []},
									{label:"<i class='fas fa-list-ol'></i>&nbsp;목록 수&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"pageSize", addClass:"", valueBoxStyle:"", value:"30",
										options:[
													{optionValue:15, optionText:"15"},
					                                {optionValue:30, optionText:"30"},
					                                {optionValue:50, optionText:"50"},
					                                {optionValue:100, optionText:"100"},
					                                {optionValue:200, optionText:"200"},
					                                {optionValue:300, optionText:"300"},
													{optionValue:600, optionText:"600"},
													{optionValue:1000, optionText:"1000"},
													{optionValue:5000, optionText:"5000"},
													{optionValue:10000, optionText:"10000"}
					                                
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
									{label : "",labelWidth : "",type : "button",width : "70",key : "btn_print_newReqDemand",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
										onclick : function() {
											$(firstGrid.exportExcel()).printThis({importCSS: false,importStyle: false,loadCSS: "/css/common/printThis.css"});
										}
									},									
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_excel_newReqDemand",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
										onclick : function() {
											firstGrid.exportExcel("${sessionScope.selMenuNm}.xls");
										}
									},
									
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_delete_req",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
										onclick : function() {
											var chkList = firstGrid.getList('selected');
											fnDeleteDplInfo(chkList);
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_update_req",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
										onclick : function() {
											var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
											if(gfnIsNull(item)){
												toast.push('수정하려는 배포 계획을 선택해주세요.');
												return;
											}
											
											//결재 승인된 배포 계획은 수정 불가능
											if(item.signStsCd == "02"){
												jAlert("결재 승인된 배포 계획은 수정이 불가능합니다.", "알림창");
												return false;
											}
											//배포상태 성공된 배포 계획은 수정 불가능
											if(item.dplStsCd == "02"){
												jAlert("성공된 배포 계획은 수정이 불가능합니다.", "알림창");
												return false;
											}

											var data = {"dplId" : item.dplId,"popupGb" : "update"};
											gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1001View.do',data, "1200", "830",'scroll');
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_insert_dpl",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
										onclick : function() {
 											gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1001View.do',{"popupGb" : "insert"}, "1200", "830",'scroll');
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_search_dlp",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
										onclick : function() {
											/* 검색 조건 설정 후 reload */
								            fnInGridListSet(0,mySearch.getParam());
										}
									},
									{label : "",labelWidth : "",type : "button",width : "80",key : "btn_insert_sign",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-file-signature' aria-hidden='true'></i>&nbsp;<span>결재 요청</span>",
										onclick : function() {
											var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
											if(gfnIsNull(item)){
												toast.push('결재를 요청하려는 배포 계획을 선택해주세요');
												return;
											}
											
											if(item.signStsCd == "05" || item.signStsCd == "04" || item.signStsCd == "03"){
												jConfirm("선택한 배포 계획을 결재 요청하시겠습니까?","알림창",
													function(result) {
														if (result) {
															fnDplSignRequest(item);
														}
													}
												);
											}else{
												jAlert("결재 상태가 '대기','기안','거절'인 배포 계획만 요청이 가능합니다.", "알림창");
											}
 											
										}
									}
									
							]}]
						});
			}
		};

		jQuery(document.body).ready(
				function() {
					fnObjSearch.pageStart();
					//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
					axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");

					//공통코드 selectBox hide 처리
					axdom("#" + mySearch.getItemId("searchCd")).hide();

					//버튼 권한 확인
					fnBtnAuthCheck(mySearch);

				});
	}
	
	//결재 대기 요청
	function fnDplSignRequest(item){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url" : "<c:url value='/dpl/dpl1000/dpl1000/insertDpl1000DplsignRequestAjax.do'/>"}, 
				{dplId: item.dplId, dplSignTxt: item.dplSignTxt, signUsrId: item.signUsrId, dplNm: item.dplNm});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data) {
			data = JSON.parse(data);
			if (data.errorYn == "N") {
				fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
				
			} else {
				toast.push(data.message);
			}
		});
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err) {
			data = JSON.parse(data);
			toast.push(data.message);
		});
		//AJAX 전송
		ajaxObj.send();
	}
	
	// 배포 계획 생성관리 가이드 상자
	function fnDpl1000GuideShow(){
		var mainObj = $(".main_contents");
		
		//mainObj가 없는경우 false return
		if(mainObj.length == 0){
			return false;
		}
		//guide box setting
		var guideBoxInfo = globals_guideContents["dpl1000"];
		gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
	}
	
</script>
<div class="main_contents" style="height: auto;">
	<div class="dpl_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu" style="max-width: 1500px;">
		<form:form commandName="dpl1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
		<div id="AXSearchTarget" style="border-top: 1px solid #ccc;" guide="dpl1000button" ></div>
		<br />
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;" guide="dpl1000Grid"></div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />