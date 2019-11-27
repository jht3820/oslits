<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<!-- css는 adm5000(로그인 이력 log)과 공유 -->
<link rel="stylesheet" href="<c:url value='/css/oslits/adm.css'/>">

<style>
img.ui-datepicker-trigger {margin-bottom: 7px; }
.spr_pop_text { width: 130px; height: 28px; border: 1px solid #ccc; }
</style>
<script>
	var fmt = {
		menuCd : function (){
			switch(this.item.menuCd){
			case "01" :  return "조회" 
			case "02" :  return "삽입" 
			case "03" :  return "수정" 
			case "04" :  return "삭제" 
			case "05" :  return "저장" 
			default : return "기타"  
			}
		}
	} 

	/*
		business function
	*/


	//axisj5 그리드
	function fnAxGrid5View(){
		firstGrid = new ax5.ui.grid();
	            firstGrid.setConfig({
		            target: $('[data-ax5grid="first-grid"]'),
		            sortable:false,
		            header: {align:"center"},
		            columns:[
			                    {key: "menuCd", 	label: "유형", 		width: 150, 	align: "center",	formatter: fmt.menuCd },
				                {key: "licGrpId", 	label: "라이센스 ID", 	width: 170, 	align: "center"},
					            {key: "logTime", 	label: "로그 일시", 	width: 200, 	align: "center"},
			                	{key: "logUrl", 	label: "로그 URL", 	width: 250, 	align: "center"},
			                	{key: "logUsrId", 	label: "사용자 ID", 	width: 160, 	align: "center"},
			                	{key: "usrNm", 		label: "사용자 명", 	width: 160, 	align: "center"},
					            {key: "logIp", 		label: "로그 IP", 	width: 180, 	align: "center"},
					            {key: "menuId", 	label: "메뉴 ID", 	width: 170, 	align: "center"},
					            {key: "menuNm", 	label: "메뉴 이름", 	width: 200, 	align: "center"},
					            {key: "menuUrl", 	label: "메뉴 URL", 	width: 200, 	align: "center"}
			                ],
		            body: {
		                align: "center",
		                columnHeight: 30
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
					{"url":"<c:url value='/adm/adm5000/adm5100/selectAdm5100ViewAjax.do'/>","loadingShow":true}
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
	function fnSearchBoxControl(){
		var pageID = "AXSearch";
		mySearch = new AXSearch();
	
		//검색 기준 -  ~ 현재
		var defaultEndDt = new Date().format('yyyy-MM-dd');
		var defaultStDt = new Date().format('yyyy-MM-dd');
		
		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				mySearch.setConfig({
					targetID:"AXSearchTarget",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
							{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
								options:[
	                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
	                                //라이선스 그룹 ID 검색조건 제거{optionValue:"licGrpId", optionText:"라이센스 그룹 ID"},
	                                {optionValue:"logUsrId", optionText:"사용자 ID"},
	                                {optionValue:"usrNm", optionText:"사용자 명"},
	                                {optionValue:"logIp", optionText:"사용자 IP"},
	                                {optionValue:"menuCd", optionText:"로그 유형", optionCommonCode:"ADM00003"}
	                            ],onChange: function(selectedObject, value){
	                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
	    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
										axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");	
										axdom("#" + mySearch.getItemId("searchTxt")).val('');	
									}else{
										axdom("#" + mySearch.getItemId("searchTxt")).removeAttr("readonly");
									}
	                            	if(value==="menuCd") {
	                            		gfnCommonSetting(mySearch,selectedObject.optionCommonCode,"searchCd","searchTxt");	
	                            		axdom("#"+ mySearch.getItemId("searchCd")).prepend('<option value="" selected>전체</option>');
	                            	} else {
										//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
										axdom("#"+ mySearch.getItemId("searchTxt")).show();
										axdom("#"+ mySearch.getItemId("searchCd")).hide();
									}
								}
							},
							{label:"", labelWidth:"", type:"inputText", width:"150", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
								onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + mySearch.getItemId("btn_search")).click();
									}
								}
							},
							{label : "",labelWidth : "",type : "selectBox",width : "100",key : "searchCd",addClass : "selectBox",valueBoxStyle : "padding-left:0px;",value : "01",options : []},
							{label:"기간", labelWidth:"70", type:"inputText", width:"70", key:"srchFromDt", addClass:"secondItem readonly", valueBoxStyle:"", value:defaultStDt,
								onChange: function(){}
							},
							{label:"", labelWidth:"", type:"inputText", width:"90", key:"srchToDt", addClass:"secondItem readonly", valueBoxStyle:"padding-left:0px;", value:defaultEndDt,
								AXBind:{
									type:"twinDate", config:{
										align:"right", valign:"top", startTargetID:"srchFromDt"
									}
								}
							},
							{label:"&nbsp;<i class='fas fa-list-ol'></i>&nbsp;목록 수&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"pageSize", addClass:"", valueBoxStyle:"padding-left:7px;", value:"30",
								options:[
											{optionValue:15, optionText:"15"},
											{optionValue:30, optionText:"30"},
											{optionValue:50, optionText:"50"},
											{optionValue:100, optionText:"100"},
											{optionValue:300, optionText:"300"},
											{optionValue:600, optionText:"600"},
											{optionValue:1000, optionText:"1000"},
											{optionValue:5000, optionText:"5000"},
											{optionValue:10000, optionText:"10000"}
			                                
			                            ],onChange: function(selectedObject, value){
			                            	fnInGridListSet(0,$('form#searchFrm').serialize()+"&pageSize="+value);
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
							},
						    {label:"", labelWidth:"", type:"button", width:"70",style:"float:right;", key:"btn_print_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
								onclick:function(){
									$(firstGrid.exportExcel()).printThis();
							}},
							{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
								onclick:function(){
									firstGrid.exportExcel("${sessionScope.selMenuNm}.xls");
							}},
							{label:"", labelWidth:"", type:"button", width:"55", style:"float:right;", key:"btn_search", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
	 							var pars = mySearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();
							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }
							    fnInGridListSet(0,ajaxParam);
							     //폼 값 변경
					            $('#searchSelect').val(axdom("#" + mySearch.getItemId("searchSelect")).val());
								$('#searchCd').val(axdom("#" + mySearch.getItemId("searchCd")).val());
								$('#searchTxt').val(axdom("#" + mySearch.getItemId("searchTxt")).val());
							}}
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

		});
	}
	/*
		Initialize	
	*/
	$(document).ready(function(){
		/*
			Event Handler	
		*/
		fnAxGrid5View();
		fnSearchBoxControl();
		
	});
	
	$(window).load(function(){
		//twinDate 초기화 버튼 이벤트 동작 + 화면
		$("a.AXanchorDateHandle").click(function(e) {
			$(".AXbindTwinDateExpandBox .AXButton").closest('div').append($("<div/>", { "id":"init", class: "AXButton Classic W70", value:"초기화", text:"초기화"} ) );
		});
		$("#"+ mySearch.getItemId("srchFromDt") + ", #"+ mySearch.getItemId("srchToDt")).val("").text("");
		$(document).on('click', "#init", function(){
			$("#"+ mySearch.getItemId("srchFromDt") + ", #"+ mySearch.getItemId("srchToDt")).val("").text("");
			$(".AXbindTwinDateExpandBox .AXButton").click();
		});
		//twinDate 초기화 버튼 이벤트 동작 + 화면
	});
</script>
<div class="main_contents" style="height: auto;">
	<form:form commandName="adm5100VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;"></form:form>
	<form:form commandName="adm5100VO" id="excelForm" name="excelForm" method="post" onsubmit="return false;">
                        <input path="searchTxt" type="hidden" id="searchTxt" name="searchTxt"/>
                        <input path="searchCd" type="hidden" id="searchCd" name="searchCd"/>
                        <input path="searchSelect" type="hidden" id="searchSelect" name="searchSelect"/>
                </form:form>
    <div class="tab_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu" style="max-width:1500px;">
		<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
		<br />
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />