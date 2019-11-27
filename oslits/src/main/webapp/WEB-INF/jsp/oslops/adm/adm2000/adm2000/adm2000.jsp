<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/adm.css'/>' type='text/css'>

<script src="<c:url value='/js/common/spectrum.js'/>" ></script>
<link rel='stylesheet' href='<c:url value='/css/common/spectrum.css'/>' type='text/css'>

<style>
.button_normal2 { font-size: 1em; }
.search_select { width: 124px; height: 28px; margin: 0 5px 5px 0; }
.search_box_wrap { width: calc(100% - 404px);} /* width:calc(100% - (.search_select너비 * 갯수 + 32px))  */
.paginate { border: 1px solid #ccc; border-top: none; }

</style>

<script type="text/javascript">
	
	// 수정 버튼권한
	var btnAuthUpdateYn = '${sessionScope.selBtnAuthUpdateYn}';

	//수정 권한에 따른 그리드 컬럼
	var btnAuthOption = "";
	
	//수정 권한에 따른 그리드 컬럼 변경
	if(btnAuthUpdateYn == "N"){
		// 수정 버튼권한이 없을경우 차단여부 컬럼에 editor 없도록
		btnAuthOption = {key: "block", label: "차단여부", width : 150, align : "center",
							formatter: function () { return this.item.block == "01"?"정상":"차단"; }  
						};
	}else if(btnAuthUpdateYn == "Y"){
		// 수정 버튼권한이 있을경우 차단여부 컬럼에 editor 생성
		btnAuthOption = {key: "block", label: "차단여부", width : 150, align : "center",
				            editor: {
								type: "select", config: {
								columnKeys: { optionValue: "code", optionText: "nm" },
						                      options: [ {code: "01", nm: "정상"},{code: "02", nm: "차단"} ]
								}
						    },  
							formatter: function () { return this.item.block == "01"?"정상":"차단"; }  
					 	};
	}

	// 차단여부 값의 변경확인용 flag
	var blockChageFlag = false;	
	var selectRowIdx = 0;
	var mySearch;
	var Grid = {
		init : function() {
			//그리드 및 검색 상자 호출
			fnAxGrid5View(); // Grid 초기화  설정
			fnSearchBoxControl(); // Search Grid 초기화 설정
		},
		columnOption : {
			adm2000View : [ 
					 	btnAuthOption,
						{key : "pwFailCnt",label : "비밀번호 실패 횟수",width : 150,align : "center"},
						{key : "useCd",label : "사용유무",width : 150,align : "center" ,
							formatter : function(){return this.item.useCd == "01"?"사용":"미사용";}
						},
						{key : "usrId",label : "아이디",width : 200,align : "center"},
						{key : "usrNm",label : "이름",width : 200,align : "center"},
						{key : "telno",label : "연락처",width : 200,align : "center"},
						{key : "email",label : "이메일",width : 300,align : "center"},
						{key : "deptName",label : "소속",width : 300,align : "center"},
						{key : "etc",label : "비고",width : 300,align : "center"} 
					],
			adm2000Search : [ 
	                  	{optionValue : "rn",optionText : "전체 보기",optionAll : true}, 
	                  	{optionValue : "usrId",optionText : "아이디"}, 
	                  	{optionValue : "usrNm",optionText : "성명"}, 
	                  	{optionValue : "telno",optionText : "연락처"},
	                  	{optionValue : "email",optionText : "이메일"}, 
	                  	{optionValue : "useCd",optionText : "사용유무", optionCommonCode:"CMM00001"}, 
	                  	{optionValue : "etc",optionText : "비고"} 
                  	]
				}
			}

	
	/* 사용자  삭제 */
	var fnDeleteAdmInfo = function(chkList) {
		var strInSql = "";
		var isExistFlowId = false;
		var continueChk = true;
		if (gfnIsNull(chkList)) {
			jAlert("선택한 사용자 정보가 없습니다.", "알림창");
			return false;
		}
		
		//체크된 요구사항의 ID를 IN 쿼리로 만들수 있도록 'reqId', 'reqId' 형식으로 감싸기
		$(chkList).each(function(idx, data) {
			//라이센스 최초 발급자와 같은 아이디인지 확인, PO역할인지 확인
			if(data.usrId+"_GRP" == "${sessionScope.licVO.licGrpId}"){
				jAlert("라이선스 최초 등록자는 삭제가 불가능합니다.","경고");
				continueChk = false;
				return false;
			}
			strInSql += ",'"+data.usrId+"'";
		});
		if(!continueChk){
			return false;
		}

		//마지막 , 자르기
		strInSql = strInSql.substring(1);

		jConfirm("삭제 시 사용자의 사용유무가 미사용으로 변경됩니다. \n 선택된 사용자를 삭제 하시겠습니까?","알림창",
				function(result) {
					if (result) {
						//AJAX 설정
						var ajaxObj = new gfnAjaxRequestAction(
								{"url" : "<c:url value='/adm/adm2000/adm2000/deleteAdm2000UsrInfo.do'/>"}, 
								{"usrId" : strInSql});
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
								//toast.push(data.message);
								jAlert(data.message, '알림창');
							}
							fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
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
	
	/**
	 * 	사용자 차단유무 수정
	 *	@param updateData 수정할 사용자 데이터
	 */
	function fnUpdateAdmBlockInfo(updateData){
		
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm2000/adm2000/updateAdm2000Block.do'/>"}
				,updateData);

		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
        	//수정 실패의 경우 리턴
        	if(data.saveYN == 'N'){
        		toast.push(data.message);
        		return;
        	}
    		//그리드 새로고침
			fnAxGrid5View();
			toast.push(data.message);
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
        	data = JSON.parse(data);
        	jAlert(data.message,"알림창");
	 	});
		
		//AJAX 전송
		ajaxObj.send();	
	}
	
	
	/*   
		business function 
	*/
	//axisj5 그리드
	function fnAxGrid5View(){
		firstGrid = new ax5.ui.grid();
	 
	        firstGrid.setConfig({
	            target: $('[data-ax5grid="first-grid"]'),
	            showRowSelector: true,
	            sortable:false,
	            header: {align:"center"},
	            columns: Grid.columnOption.adm2000View,
	            body: {
	                align: "center",
	                columnHeight: 30,
	                onDBLClick:function(){
	                	// 더블클릭 시 사용자 상세보기
	                	var data = {"proStatus" : "S" ,"usrId": this.item.usrId}; 
						gfnLayerPopupOpen("/adm/adm2000/adm2000/selectAdm2001View.do", data, "800", "770",'auto');
	                },
	                onDataChanged: function(){
	                	
	                	// 차단여부일 경우에만 동작하도록
	                	if(this.key != "block"){
	                		return;
	                	}
	                	
	            		var selectRowData = this.item;
	            		selectRowIdx = this.dindex;
	            		// 3개월 미접속 여부
	            		var loginExpr = selectRowData.loginExprYn;
	            		
	            		var blockMsg = selectRowData.usrNm + " 님의 차단여부를 수정 하시겠습니까?";
	            		if(loginExpr == "Y"){
	            			blockMsg = selectRowData.usrNm + " 님은 3개월 이상 미접속으로 차단되었습니다. \n\n 차단여부 수정시 로그인 할 수 있습니다. 차단여부를 수정하시겠습니까?";
	            		}

	            		/*
	            		 * 현재 화면에서 선택된 차단여부와 DB에 저장된 차단여부가 다를경우
	            		 * → 차단여부 수정된 상태 (blockChageFlag = true)
	            		 */
	            		selectRowData.block == selectRowData.beforeBlock ?  blockChageFlag = false : blockChageFlag = true;
	            		
	            		// 차단여부가 수정되지 않았을 경우
	            		if(!blockChageFlag){
	            			return;
	            		}
	            		
	            		// 차단여부 수정 알림창
	            		jConfirm(blockMsg, "알림창", function( result ) {
                    		if(result){
                    			var updateData = {"usrId" : selectRowData.usrId, "block" : selectRowData.block, "pwFailCnt": "", "loginExprChange":"N"};
                    			// 3개월 이상 미접속일 경우
                    			if(loginExpr == "Y"){
                    				// 차단유무 수정시 최근 로그인 일시를 수정하기 위한 값
                    				updateData.loginExprChange = "Y";
                    			}
                    			
                    			// 정상, 차단에 따라 비밀번호 실패횟수 변경
                    			selectRowData.block == "01" ? updateData.pwFailCnt = "0" : updateData.pwFailCnt = selectRowData.pwFailCnt;
								// 차단여부 수정
								if(selectRowData.block=='02'){
									gfnLayerPopupOpen("/adm/adm2000/adm2000/selectAdm2003View.do", updateData, '500', '350','scroll');
								}else{
									fnUpdateAdmBlockInfo(updateData);								
								}
                    		}else{
                    			// Confirm에서 취소를 누를경우, 기존 차단여부 데이터로 변경
                    			firstGrid.setValue(selectRowIdx, "block", selectRowData.beforeBlock);
                    		}
                    	});
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
	            },
	            contextMenu: {
	                iconWidth: 20,
	                acceleratorWidth: 100,
	                itemClickAndClose: false,
	                icons: {
	                    'arrow': '<i class="fa fa-caret-right"></i>'
	                },
	                items: [ // 마우스 우클릭 메뉴
	                   /*  {type: "modify", label: "수정", icon:"<i class='fa fa-envelope' aria-hidden='true'></i>"},
	                    {type: "delete", label: "삭제", icon:"<i class='fa fa-trash' aria-hidden='true'></i>"},
	                    {divide: true}, */
	                    {type: "reply", label: "쪽지 전송", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
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
	                	
	                    //수정
	                    if(item.type == "modify"){
							var data = {"proStatus" : "U" ,"usrId" : param.item.usrId};
							gfnLayerPopupOpen('/adm/adm2000/adm2000/selectAdm2001View.do',data, "780", "550",'auto');
	                    
						//삭제
	                    }else if(item.type == "delete"){
	                    	jConfirm("삭제 하시겠습니까?", "알림창", function( result ) {
	                    		if(result){
			                    	var chkList = [];
			                    	chkList.push(param.item);
									fnDeleteAdmInfo(chkList);
	                    		}
	                    	});
	                    //쪽지 전송
	                    }else if(item.type == "reply"){
	                    	gfnAlarmOpen(param.item.usrId);
	                    }
	                    
	                    firstGrid.contextMenu.close();
	                    
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
					{"url":"<c:url value='/adm/adm2000/adm2000/selectAdm2000ListAjax.do'/>","loadingShow":true}
					,ajaxParam);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				
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
					rows : [ 
						  {display : true,addClass : "top_searchGroup",style : "",list : [
						        {label:"", labelWidth:"", type:"button", width:"70",style:"float:right;", key:"btn_insert_excelPopup",valueBoxStyle:"float:right; padding:5px;", value:"<i class='fa fa-upload' aria-hidden='true'></i>&nbsp;<span>업로드</span>",
						            onclick:function(){
						            gfnLayerPopupOpen('/adm/adm2000/adm2000/selectAdm2002View.do',{},"1000","870",'auto');
						        }},                                           
								{label:"", labelWidth:"", type:"button", width:"100",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"float:right; padding:5px;", value:"<i class='fa fa-download' aria-hidden='true'></i>&nbsp;<span>양식 다운로드</span>",
									onclick:function(){
										$.download('/etc/uploadUserForm.xlsx','tmp','post');
								}},
					       ]},
					     
					      {display : true,addClass : "bottom_searchGroup",style : "",list : [
						  		{label : "<i class='fa fa-search'></i>&nbsp;",labelWidth : "30",type : "selectBox",width : "",key : "searchSelect",addClass : "",valueBoxStyle : "",value : "all",options : Grid.columnOption.adm2000Search,
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
									{label : "",labelWidth : "",type : "inputText",width : "120",key : "searchTxt",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:0px;",value : "",
										onkeyup:function(e){
											if(e.keyCode == '13' ){
												axdom("#" + mySearch.getItemId("btn_search_usr")).click();
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
									},
									{label : "",labelWidth : "",type : "button",width : "70",key : "btn_print_usr",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
										onclick : function() {
											$(firstGrid.exportExcel()).printThis();
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_excel_usr",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
										onclick : function() {
											firstGrid.exportExcel("${sessionScope.selMenuNm}.xls");
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_delete_usr", style : "float:right;",valueBoxStyle : "padding:5px;", value : "<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
										onclick : function() {
												var chkList = firstGrid.getList('selected');
												fnDeleteAdmInfo(chkList);
											}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_update_usr",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
										onclick : function() 
										{
												var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
												
												if(gfnIsNull(item)){
													toast.push('수정할 사용자를 선택해주세요');
													return;
												}
	 											
												var data = {"proStatus" : "U" ,"usrId" : item.usrId};
												gfnLayerPopupOpen('/adm/adm2000/adm2000/selectAdm2001View.do',data, "820", "770",'auto');
											}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_insert_usr",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
										onclick : function() 
										{
											gfnLayerPopupOpen('/adm/adm2000/adm2000/selectAdm2001View.do',{"proStatus" : "I"}, "820", "600",'auto');
										}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_search_usr",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
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
									}
								]
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
					
					// 상단 엑셀업로드, 양식다운로드 버튼이 권한이 없어서 hide일 경우
					// 해당 버튼이 있는 div도 hide 처리해야함 
					// $(".top_searchGroup") div의 하위에 있는 버튼 목록을 가져온다.
					var childList = $(".top_searchGroup").children('.searchItem');

					var childCnt = 0;
					
					// 가져온 버튼의 hide 여부 체크
					$.each(childList,function(idx, child){
						if($(this).is(':visible') == false ){
							childCnt ++;
						}
					});
					
					// 버튼의 개수와 hide된 버튼의 수가 같다면 
					// $(".top_searchGroup") div를 hide 처리한다.
					if(childList.length == childCnt){
						$(".top_searchGroup").hide();
					}

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
</script>

<div class="main_contents" style="height: auto;">
	<div class="tab_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu" style="max-width: 1500px;">
		<form:form commandName="adm2000VO" id="searchFrm" name="searchFrm" onsubmit="return false"></form:form>
		<form:form commandName="adm2000VO" id="excelForm" name="excelForm" method="post" onsubmit="return false;">
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