<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<script type="text/javascript">
	var firstGrid, mySearch;
	var Grid = {
		init : function() {
			gfnCalRangeSet("startDt", "endDt");
			//그리드 및 검색 상자 호출
			//fnAxGridView(); // Grid 초기화  설정
			//fnSearchBoxControl(); // Search Grid 초기화 설정
			//그리드 및 검색 상자 호출
			fnAxGrid5View();
			fnSearchBoxControl();
		},
		columnOption : {
			prs2000View : [
						{key : "rn",label : "번호",width : 50,align : "center"},
						{key : "prjNm",label : "프로젝트",width : 300,align : "center", treeControl: true },
						{key : "prjDesc",label : "설명",width : 400,align : "center"},
						{key : "prjUsrCnt",label : "인원",width : 200,align : "center",tooltip : "프로젝트 인원"},
						{key : "startDt",label : "시작",width : 200,align : "center",tooltip : "프로젝트 시작"},
						{key : "endDt",label : "종료",width : 200,align : "center",tooltip : "프로젝트 종료"}
					],
			prs2000Search : [ 
	                  	{optionValue : "rn",optionText : "전체 보기",optionAll : true}, 
	                  	{optionValue : "prjNm",optionText : "프로젝트"}, 
	                  	{optionValue : "prjDesc",optionText : "설명"}, 
	                  	{optionValue : "prjUsrCnt",optionText : "인원"}
                  	]
				}
			}
	
	/* 배포 정보  삭제 */
	var fnDeleteAdmInfo = function(chkList) {
		var strInSql = "";
		var isExistFlowId = false;
		if (gfnIsNull(chkList)) {
			jAlert("선택한 사용자 정보가 없습니다.", "알림창");
			return false;
		}
		//체크된 요구사항의 ID를 IN 쿼리로 만들수 있도록 'reqId', 'reqId' 형식으로 감싸기
		$(chkList).each(function(idx, data) {
			strInSql += ",'"+data.usrId+"'";
		});

		//마지막 , 자르기
		strInSql = strInSql.substring(1);

		jConfirm("삭제 하시겠습니까?","알림창",
				function(result) {
					if (result) {
						//AJAX 설정
						var ajaxObj = new gfnAjaxRequestAction(
								{"url" : "<c:url value='/adm/prs2000/prs2000/deletePrs2000UsrInfo.do'/>"}, 
								{"usrId" : strInSql});
						//AJAX 전송 성공 함수
						ajaxObj.setFnSuccess(function(data) {
							data = JSON.parse(data);
							if (data.successYn == "Y") {
								jAlert(data.message, '알림창', function(result) {
									if (result) {
										fnAxGridView();
									}
								});
							} else {
								toast.push(data.message);
							}
							myGrid.reloadList();
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
	

	//axisj5 그리드
	function fnAxGrid5View(){
		firstGrid = new ax5.ui.grid();
	 
	        firstGrid.setConfig({
	            target: $('[data-ax5grid="first-grid"]'),
	            sortable:false,
	            header: {align:"center"},
	            frozenColumnIndex: 2,
	            columns: Grid.columnOption.prs2000View,
	            body: {
	                align: "center",
	                columnHeight: 30,
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
	                   fnInGridListSet(this.page.selectPage);
	                }
	            },tree: {
	                use: true,
	                indentWidth: 10,
	                arrowWidth: 15,
	                iconWidth: 18,
	                icons: {
	                    openedArrow: '<i class="fa fa-caret-down" aria-hidden="true"></i>',
	                    collapsedArrow: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
	                    groupIcon: '<img src="/images/icon/icon_request_group.png" class="iconRequestGroup"/>',
	                    collapsedGroupIcon: '<img src="/images/icon/icon_request_group.png" class="iconRequestGroup"/>',
	                    itemIcon: '<img src="/images/icon/icon_request.png" class="iconRequest"/>'
	                },
	                columnKeys: {
	                    parentKey: "prjGrpId",
	                    selfKey: "prjId"
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
					{"url":"<c:url value='/prs/prs2000/prs2000/selectPrs2000ListAjax.do'/>","loadingShow":true}
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
		var defaultEndDt = new Date().format('yyyy-MM-dd');
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
						list : [{label : "검색",labelWidth : "100",type : "selectBox",width : "",key : "searchSelect",addClass : "",valueBoxStyle : "",value : "all",options : Grid.columnOption.prs2000Search,
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
									{label : "",labelWidth : "",type : "inputText",width : "150",key : "searchTxt",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:0px;",value : ""},
									{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
										options:[]
									},
									{label:"기간", labelWidth:"", type:"inputText", width:"70", key:"startDt", addClass:"secondItem readonly", valueBoxStyle:"", value:""},
									{label:"", labelWidth:"", type:"inputText", width:"90", key:"endDt", addClass:"secondItem readonly", valueBoxStyle:"padding-left:0px;", value:"",
										AXBind:{
											type:"twinDate", config:{
												align:"right", valign:"top", startTargetID:"startDt"
											}
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "button_search",valueBoxStyle : "padding-left:10px;padding-right:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
									onclick : function() {
										/* 검색 조건 설정 후 reload */
										var pars = mySearch.getParam();
										var ajaxParam = $('form#searchFrm').serialize();
										if (!gfnIsNull(pars)) {
											ajaxParam += "&" + pars;
										}
										fnInGridListSet(0,ajaxParam);
										//폼 값 변경
							            $('#searchSelect').val(axdom("#" + mySearch.getItemId("searchSelect")).val());
										$('#searchCd').val(axdom("#" + mySearch.getItemId("searchCd")).val());
										$('#searchTxt').val(axdom("#" + mySearch.getItemId("searchTxt")).val());
										}
										
									
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_excel_newReqDemand",style : "float:right;",valueBoxStyle : "",value : "<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
										onclick : function() {
											firstGrid.exportExcel("all_request_list.xls");
										}
									},
									{label : "",labelWidth : "",type : "button",width : "70",key : "btn_print_newReqDemand",style : "float:right;",valueBoxStyle : "",value : "<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
										onclick : function() {
											$(firstGrid.exportExcel()).printThis();
										}
									} ]
							} ]
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

					//좌측메뉴 작업흐름으로 조회인 경우 (전체요구사항, 담당 요구사항)
					if (!gfnIsNull("${flowId}")) {
						//option 초기화
						axdom("#" + mySearch.getItemId("searchCd")).html('');

						//목록 불러오기
						axdom("#" + mySearch.getItemId("searchCd")).append('<option value="ALL">전체</option>');
						$.each(JSON.parse(flowList), function() {
							axdom("#" + mySearch.getItemId("searchCd")).append('<option value="'+this.flowId+'">'+ this.flowNm + '</option>');
						});
						axdom("#" + mySearch.getItemId("searchCd")).append('<option value="FLW">미분류</option>');

						//파라미터 세팅
						axdom("#" + mySearch.getItemId("searchSelect")).val('flowId');
						axdom("#" + mySearch.getItemId("searchCd")).val("${flowId}");
						axdom("#" + mySearch.getItemId("searchTxt")).hide();
						axdom("#" + mySearch.getItemId("searchCd")).show();

						//그리드 검색 호출
						var pars = mySearch.getParam();
						fnAxGridView(pars);
					}
					//좌측메뉴 개발주기로 조회인 경우
					if (!gfnIsNull("${sprintId}")) {
						//option 초기화
						axdom("#" + mySearch.getItemId("searchCd")).html('');

						//목록 불러오기
						$.each(JSON.parse(sprintList), function() {
							axdom("#" + mySearch.getItemId("searchCd")).append('<option value="'+this.sprintId+'">'+ this.sprintNm + '</option>');
						});

						//파라미터 세팅
						axdom("#" + mySearch.getItemId("searchSelect")).val('sprintId');
						axdom("#" + mySearch.getItemId("searchCd")).val("${sprintId}");
						axdom("#" + mySearch.getItemId("searchTxt")).hide();
						axdom("#" + mySearch.getItemId("searchCd")).show();

						//그리드 검색 호출
						var pars = mySearch.getParam();
						fnAxGridView(pars);
					}					
				});
		
	}
	

	$(document).ready(function() {
		Grid.init(); // AXISJ Grid 초기화 실행 부분들
		
	});
	$(window).load(function(){
		//twinDate 초기화 버튼 이벤트 동작 + 화면
		$("a#inputBasic_AX_AXSearchTarget_AX_0_AX_4_AX_endDt_AX_dateHandle").click(function(e) {
			$(".AXbindTwinDateExpandBox .AXButton").closest('div').append($("<div/>", { "id":"init", class: "AXButton Classic W70", value:"초기화", text:"초기화"} ) );
		});
		$(document).on('click', "#init", function(){
			$("#"+ mySearch.getItemId("startDt") + ", #"+ mySearch.getItemId("endDt")).val("").text("");
			$(".AXbindTwinDateExpandBox .AXButton").click();
		});
		//twinDate 초기화 버튼 이벤트 동작 + 화면
	});
	
	
	
</script>

<div class="main_contents" style="height: auto; max-width: 1500px;">
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu" style="max-width: 1500px;">
		<form:form commandName="prs2000VO" id="searchFrm" name="searchFrm" onsubmit="return false"></form:form>
		<form:form commandName="req4100VO" id="excelForm" name="excelForm" method="post" onsubmit="return false;">
                <input path="searchTxt" type="hidden" id="searchTxt" name="searchTxt"/>
                <input path="searchCd" type="hidden" id="searchCd" name="searchCd"/>
                <input path="searchSelect" type="hidden" id="searchSelect" name="searchSelect"/>
        </form:form>
		<div id="AXSearchTarget" style="border-top: 1px solid #ccc;"></div>
		<br />
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />