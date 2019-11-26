<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp"%>
<jsp:include page="/WEB-INF/jsp/oslits/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/dpl.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>

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
	                
	                 {optionValue : "dplNm",optionText : '배포 명'}, 
	                 {optionValue : "dplUsrId",optionText : "배포자"} 
	                 ]
			}
		}

	/* 배포 정보  삭제 */
	var fnDeleteDplInfo = function(chkList) {
		var strInSql = "";
		var isExistFlowId = false;
		if (gfnIsNull(chkList)) {
			jAlert("선택한 배포버전 요구사항이 없습니다.", "알림창");
			return false;
		}
		//체크된 요구사항의 ID를 IN 쿼리로 만들수 있도록 'reqId', 'reqId' 형식으로 감싸기
		$(chkList).each(function(idx, data) {
			strInSql += "'" + data.dplId + "',";
		});

		//마지막 , 자르기
		strInSql = strInSql.substring(0, strInSql.length - 1);

		jConfirm("삭제 하시겠습니까?","알림창",
				function(result) {
					if (result) {
						//AJAX 설정
						var ajaxObj = new gfnAjaxRequestAction(
								{"url" : "<c:url value='/dpl/dpl1000/dpl1000/deleteDpl1000DeployVerInfoListAjax.do'/>"}, 
								{"dplIds" : strInSql});
						//AJAX 전송 성공 함수
						ajaxObj.setFnSuccess(function(data) {
							data = JSON.parse(data);
							if (data.successYn == "Y") {
								jAlert(data.message, '알림창', function(result) {
									if (result) {
										fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
									}
								});
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
                {key : "dplStsNm",label : "배포 상태",width : 100,align : "center"},
				{key : "dplNm",label : "배포 명",width : '40%',align : "left"},
				{key : "dplDt",label : "배포 날짜",width : '16%',align : "center",
					formatter : function() {
						var fmtDt = this.item.dplDt;
						return new Date(fmtDt.substr(0, 4), fmtDt.substr(4,2) - 1, fmtDt.substr(6, 2)).format("yyyy-MM-dd", true);
					}
				},
				{key : "dplId",label : "배포 ID",width : '21%',align : "center"},
				{key : "dplUsrId",label : "배포자",width :'17%',align : "center"}				 
				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
           			// 클릭 이벤트
                },onDBLClick:function(){
                	// 더블클릭 시 상세보기
                	var item = this.item;
                	var data = {"dplId" : item.dplId, "prjId" : item.prjId, "popupGb" : "select"};
					gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1001View.do',data, "560", "560",'scroll');
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
		var pageID = "AXSearch";
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
									{label : "",labelWidth : "",type : "inputText",width : "150",key : "searchTxt",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:0px;",value : "",
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
					                            	fnInGridListSet(0,$('form#searchFrm').serialize()+"&pageSize="+value);
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
											$(firstGrid.exportExcel()).printThis();
										}
									},									
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_excel_newReqDemand",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
										onclick : function() {
											firstGrid.exportExcel("all_request_list.xls");
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
												toast.push('먼저 배포 버전 항목을 선택하시고 수정하세요.');
												return;
											}

											var data = {"dplId" : item.dplId,"prjId" : item.prjId,"popupGb" : "update"};
											gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1001View.do',data, "560", "560",'scroll');
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_insert_dpl",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
										onclick : function() {
 											gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1001View.do',{"popupGb" : "insert"}, "560", "560",'scroll');
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_search_dlp",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
										onclick : function() {
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
</script>
<div class="main_contents" style="height: auto;">
	<div class="dpl_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu" style="max-width: 1500px;">
		<form:form commandName="dpl1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
		<div id="AXSearchTarget" style="border-top: 1px solid #ccc;"></div>
		<br />
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslits/bottom/footer.jsp" />