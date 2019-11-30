<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/dpl.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>

<style type="text/css">
/* 모바일 @media 제거 */
/*
@media screen and (max-width: 1024px) {
	.menu_wrap { height: 100%; }
	.menu_ctrl_wrap { float: none; width: 100%; height: 30%; border-right: transparent; border-bottom: 1px solid #ccc; }
		.menu_ctrl_wrap { height: 240px !important; }
	.menu_info_wrap { width: 100%; height: 70%; padding: 20px; margin-left: 0; border-left: none;}
		.bottom_one_table { height: 169px; }
		.middle_table { height: 14%; padding: 12px; }
		.bottom_one_table { height: 43%; }
}
*/
.menu_lists_wrap{ padding:10px 0; overflow-y:auto; height:643px;}
.tab_contents.menu {height:100%;display: inline-block;}
.bottom_one_table{height:auto;}
#defaultBtn{text-align:center;width:100%;font-size: 2em;}
.button_check{display:none;}
table{ border-top-left-radius: 10px; border-top-right-radius: 10px; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;}
.menu_wrap {border:none; width: 1500px; max-width: 1500px;}
#left_contents { width : 500px;float: left;}
#right_contents { width : 990px;float: right;}
.sub_title{ font-weight: bold; background: #f9f9f9; border-bottom: 1px solid #ccc; text-align: left;padding: 5px 0;height: 25px;border-radius: 5px 5px 0 0;}
</style>

<script>
	var mySearch;
	
	var dlpSearch;
	var fnObjDplSearch={};
	var Grid = {
		init : function() {
			//그리드 및 검색 상자 호출
			fnAxDlpGrid5View(); // Grid 초기화  설정
			fnSearchDlpBoxControl(); // Search Grid 초기화 설정
		},
		columnOption : {
			dpl1000Search : [ 
	                 {optionValue : "rn",optionText : "전체 보기",optionAll : true}, 
	                 {optionValue : "dplVer",optionText : '배포 계획 계수'}, 
	                 {optionValue : "dplNm",optionText : '배포 명'}, 
	                 {optionValue : "dplUsrId",optionText : "배포자"} 
	                 ]
			}
		}
	
	var dplDt = "${dplDt}";
	var srchDplId = "";
	
	//선택 배포 상태
	var selDplStsCd = "";
	

	
	/* 배포에 추가&제거 */
	var fnUpdateDpl1100Dpl =function(chkList,dplId){
		var ajaxUrl = "<c:url value='/dpl/dpl1000/dpl1100/insertDpl1100Dpl.do'/>";
		//배정 제외
		if(gfnIsNull(dplId)){
			ajaxUrl = "<c:url value='/dpl/dpl1000/dpl1100/deleteDpl1100Dpl.do'/>";
		}
		var strText = (gfnIsNull(dplId))?"제외":"배정";
		
		if(chkList.length == 0) {
			toast.push(strText+'할 요구사항을 체크하세요.'); 
			return false;
		}
		
		var selReqFd = new FormData();
		
		$.each(chkList,function(idx, map){
			var rtnDplId = dplId;
			
			//배포 Id가 null인경우 현재 자신의 dplId
			if(gfnIsNull(rtnDplId)){
				rtnDplId = map.dplId;
			}
			selReqFd.append("selReq",JSON.stringify({processId: map.processId, flowId: map.flowId, reqId: map.reqId, dplId: rtnDplId}));
		});
		
		//선택 역할그룹갯수 넘기기
		selReqFd.append("selReqCnt",chkList.length);
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":ajaxUrl
					,"contentType":false
					,"processData":false
					,"cache":false},
				selReqFd)
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			if(data.saveYN =="N"){
        		jAlert(data.message, "알림창");
        	}else{
        		//배정된 요구사항 목록 갱신
		 		fnInGridListSet(null,{grid:firstGrid,data:{"clsMode":"clsAdd","dplId":srchDplId}});
		 		//미배정 요구사항 목록 갱신
		 		fnInGridListSet(null,{grid:secondGrid,data:{"clsMode":"clsDel","dplId":srchDplId}});
        		toast.push("요구사항이 "+strText+" 되었습니다.");
        	}
			
			jAlert(data.message,"알림");
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림");
	 	});
		//AJAX 전송
		ajaxObj.send();
	};



	/*
	Initialize	
	*/
	
	$(document).ready(function(){
		Grid.init();
		fnObjDplSearch.pageStart();
		
		
		fnSearchUpBoxControl();
		fnSearchDownBoxControl();
		
		fnAxGrid5View_first();
		fnAxGrid5View_second();
		
		axdom("#"+ dlpSearch.getItemId("searchTxt")).attr("readonly","readonly");
		axdom("#"+ dlpSearch.getItemId("searchCd")).hide();
	});

	//axisj5 그리드 개발주기 배정된 요구사항
	function fnAxGrid5View_first(dplId){
		firstGrid = new ax5.ui.grid();
	 
	        firstGrid.setConfig({
	            target: $('[data-ax5grid="first-grid"]'),
	            showRowSelector: true,
	            sortable:false,
	            header: {align:"center"},
	            columns: [
					{key: "reqOrd", label: "요구사항 순번", width: '14%', align: "center"} 
	                ,{key: "processNm", label: "프로세스 명", width: 120, align: "center"}
	                ,{key: "flowNm", label: "작업흐름 명", width: 120, align: "center"}
					,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left"}
					,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
					,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
					,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
					,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
					,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"}
					
					,{key: "reqStDtm", label: "시작 기간", width: '14%', align: "center"}
					,{key: "reqEdDtm", label: "종료 기간", width: '14%', align: "center"}
					,{key: "reqStDuDtm", label: "시작 예정일자", width: '15%', align: "center"}
					,{key: "reqEdDuDtm", label: "종료 예정일자", width: '15%', align: "center"}
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30
	                ,onDBLClick:function(){
	                	var data = {"mode": "req", "reqId": this.item.reqId}; 
						gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
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
	                    /* {type: "rowFrozen", label: "열 고정", icon:"<i class='fa fa-lock' aria-hidden='true'></i>"} */
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
						//열 고정
	                    if(item.type == "rowFrozen"){
	                    	//이미 해당 열에 고정인경우 중지
	                    	if(firstGrid.config.frozenColumnIndex != (param.colIndex+1)){
	                    		firstGrid.setConfig({frozenColumnIndex:param.colIndex+1});
	                    		fnInGridListSet();
	                    	}
	                    //쪽지 전송
	                    }else if(item.type == "reply"){
	                    	gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
	                    }
	                    firstGrid.contextMenu.close();
	                }
	            }
	        });
	        
	        //그리드 데이터 불러오기
	 		fnInGridListSet(null,{grid:firstGrid,data:{"clsMode":"clsAdd","dplId":dplId}});
	
	}

	//axisj5 그리드 개발주기 미 배정 요구사항
	function fnAxGrid5View_second(dplId){
		secondGrid = new ax5.ui.grid();
	 
	        secondGrid.setConfig({
	            target: $('[data-ax5grid="second-grid"]'),
	            showRowSelector: true,
	            sortable:false,
	            header: {align:"center"},
	            columns: [
					{key: "reqOrd", label: "요구사항 순번", width: '14%', align: "center"}    
					,{key: "processNm", label: "프로세스 명", width: 120, align: "center"}
	                ,{key: "flowNm", label: "작업흐름 명", width: 120, align: "center"}
					,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left"}
					,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
					,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
					,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
					,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
					,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"}
					
					,{key: "reqStDtm", label: "시작 기간", width: '14%', align: "center"}
					,{key: "reqEdDtm", label: "종료 기간", width: '14%', align: "center"}
					,{key: "reqStDuDtm", label: "시작 예정일자", width: '15%', align: "center"}
					,{key: "reqEdDuDtm", label: "종료 예정일자", width: '15%', align: "center"}
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30
	                ,onDBLClick:function(){
	                	var data = {"mode": "req", "reqId": this.item.reqId}; 
						gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
	                }
	            }
	        });
	        //그리드 데이터 불러오기
	 		fnInGridListSet(null,{grid:secondGrid,data:{"clsMode":"clsDel","dplId":null}});
	
	}
	//그리드 데이터 넣는 함수
	function fnInGridListSet(ajaxParam,gridTarget){
		
	     	/* 그리드 데이터 가져오기 */
	     	//파라미터 세팅
	     	if(gfnIsNull(ajaxParam)){
	   			ajaxParam = $('form#searchFrm').serialize();
	   		}
	     	
	     	//데이터 세팅
	     	if(!gfnIsNull(gridTarget.data)){
	     		ajaxParam += "&"+$.param(gridTarget.data);
	     	}
	     	
	     	//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/dpl/dpl1000/dpl1100/selectDpl1100DplListAjax.do'/>","loadingShow":true}
					,ajaxParam);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				var list = data.list;
				
			   	gridTarget.grid.setData(list);
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
	function fnSearchUpBoxControl(){
		var pageID = "AXSearch";
		mySearchUp = new AXSearch();
	
		
		
		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				mySearchUp.setConfig({
					targetID:"AXSearchTargetUp",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
							 {label:"배포 계획", labelWidth:"100", type:"", width:"", key:"", addClass:"", valueBoxStyle:"", value:"",
							 	
							 },
	           			/*	{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_search_req",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								//배정된 요구사항 목록 갱신
						 		fnInGridListSet(null,{grid:firstGrid,data:{"clsMode":"clsAdd","dplId":srchDplId}});
						 		//미배정 요구사항 목록 갱신
						 		fnInGridListSet(null,{grid:secondGrid,data:{"clsMode":"clsDel","dplId":srchDplId}});
							}}, */
								{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
									onclick:function(){
										firstGrid.exportExcel("배포버전 배정 요구사항.xls");
								}}
							
						]}
					]
				});
			}	
		};
		
		jQuery(document.body).ready(function(){
			fnObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + mySearchUp.getItemId("txtSearch")).attr("readonly", "readonly");
			//사용 유무 hide() 처리
			axdom("#" + mySearchUp.getItemId("search_useCd")).hide();
			//버튼 권한 확인
			fnBtnAuthCheck(mySearchUp);
		});
	}
	//검색 상자
	function fnSearchDownBoxControl(){
		var pageID = "AXSearch";
		mySearchDown = new AXSearch();
	
		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				mySearchDown.setConfig({
					targetID:"AXSearchTargetDown",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
							{label:"", labelWidth:"", type:"button", width:"50", key:"btn_insert_reqAssign", style:"margin-left:40%;", value:"<img src='/images/contents/top_blue.png' alt='위쪽 화살표' id='imgUpBtn' style='margin-right: 5px;'> 배정", 
							onclick : function(){
									var chkList = secondGrid.getList('selected');
									if(gfnIsNull(chkList)){
										toast.push('배정 할 요구사항을 체크하세요.'); 
										return false;
									}
									if(selDplStsCd != "01"){
										jAlert("대기중인 배포 계획에만 배정 할 수 있습니다.","알림");
										return false;
									}
									fnUpdateDpl1100Dpl(chkList,srchDplId);
								}
							}
							,{label:"", labelWidth:"", type:"button", width:"50", key:"btn_delete_reqAssign", style:"", value:"제외<img src='/images/contents/bottom_red.png' alt='위쪽 화살표' id='imgUpBtn' style='margin-left: 5px;'>",
							onclick : function(){
								var chkList = firstGrid.getList('selected');
								if(gfnIsNull(chkList)){
									toast.push('배정에서 제외 할 요구사항을 체크하세요.'); 
									
									return false;
								}
								if(selDplStsCd != "01"){
									jAlert("대기중인 배포 계획에만 배정 제외 할 수 있습니다.","알림");
									return false;
								}
								fnUpdateDpl1100Dpl(chkList,null);
							}},/* {label:"", labelWidth:"", type:"button", width:"100", key:"btn_select_detailInfo",style:"float:right;",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list-alt' aria-hidden='true'></i>&nbsp;<span>상세 정보</span>",
	           						onclick:function(){
	           							
										var item = secondGrid.list[secondGrid.focusedColumn[Object.keys(secondGrid.focusedColumn)].doindex];
										
										if(gfnIsNull(item)){
											toast.push('먼저 요구사항을 선택해주세요');
											return;
										}
										var data = {"mode": "req", "reqId": item.reqId}; 
										gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '887','scroll');
	           				}}, */
							{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
								onclick:function(){
									secondGrid.exportExcel("미배정_요구사항_목록.xls");
							}}
						]}
					]
				});
			}	
		};
		
		jQuery(document.body).ready(function() {
			fnObjSearch.pageStart();
			//버튼 권한 확인
			fnBtnAuthCheck(mySearchDown);
		});
	}

	//axisj5 그리드
	function fnAxDlpGrid5View(){
		dlpGrid = new ax5.ui.grid();
	 
		dlpGrid.setConfig({
	            target: $('[data-ax5grid="dpl-grid"]'),
	          
	            sortable:false,
	            showRowSelector: true,
	            multipleSelect: false  ,
	            header: {align:"center", selector : false  },
	            columns: [
					{key : "dplStsNm",label : "배포 상태",width : 100,align : "center"},
					{key : "dplNm",label : "배포 명",width : '55%',align : "left"},
					{key : "dplDt",label : "배포 날짜",width : '25%',align : "center",
						formatter : function() {
							var fmtDt = this.item.dplDt;
							return new Date(fmtDt.substr(0, 4), fmtDt.substr(4,2) - 1, fmtDt.substr(6, 2)).format("yyyy-MM-dd", true);
						}
					},
					{key : "dplId",label : "배포 ID",width : '35%',align : "center"},
					{key : "dplUsrId",label : "배포자",width :'25%',align : "center"} 
					
					
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30,
	                onClick: function () {
	                	srchDplId=dlpGrid.list[this.doindex].dplId;
	                	selDplStsCd=dlpGrid.list[this.doindex].dplStsCd;
	                	
	                	fnAxGrid5View_first(srchDplId);
	                	dlpGrid.select(this.doindex, {selected: true});
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
	                   fnInDlpGridListSet(this.page.selectPage,dlpSearch.getParam());
	                }
	            }
	        });
	        //그리드 데이터 불러오기
	 		fnInDlpGridListSet();
	
	}
	//그리드 데이터 넣는 함수
	function fnInDlpGridListSet(_pageNo,ajaxParam){
	     	/* 그리드 데이터 가져오기 */
	     	//파라미터 세팅
	     	if(gfnIsNull(ajaxParam)){
	   			ajaxParam = $('form#searchFrm').serialize();
	   		}
	     	
	     	//페이지 세팅
	     	if(!gfnIsNull(_pageNo)){
	     		ajaxParam += "&pageNo="+_pageNo;
	     	}else if(typeof dlpGrid.page.currentPage != "undefined"){
	     		ajaxParam += "&pageNo="+dlpGrid.page.currentPage;
	     	}
	     	
	     	//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/dpl/dpl1000/dpl1000/selectDpl1000DeployVerInfoListAjax.do'/>","loadingShow":false}
					,ajaxParam);
			ajaxObj.async = false;
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				var list = data.list;
				var page = data.page;
								
				dlpGrid.setData({
			             	list:list,
			             	page: {
			                  currentPage: _pageNo || 0,
			                  pageSize: page.pageSize,
			                  totalElements: page.totalElements,
			                  totalPages: page.totalPages
			              }
			             });
				if(list.length>0){
					srchDplId=list[0].dplId;
					selDplStsCd=list[0].dplStsCd;
					fnAxGrid5View_first(srchDplId);
					dlpGrid.select(0, {selected: true});
					
				}
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
	function fnSearchDlpBoxControl() {
		var pageID = "AXSearch";
		dlpSearch = new AXSearch();

		fnObjDplSearch = {
			pageStart : function() {
				//검색도구 설정 01 ---------------------------------------------------------
				dlpSearch.setConfig({
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
											axdom("#"+ dlpSearch.getItemId("searchTxt")).attr("readonly","readonly");
											axdom("#"+ dlpSearch.getItemId("searchTxt")).val('');
										} else {
											axdom("#"+ dlpSearch.getItemId("searchTxt")).removeAttr("readonly");
										}
		
										//공통코드 처리 후 select box 세팅이 필요한 경우 사용
										if (!gfnIsNull(selectedObject.optionCommonCode)) {
											gfnCommonSetting(dlpSearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
										} else if (value == "flowId") {
											//option 초기화
											axdom("#"+ dlpSearch.getItemId("searchCd")).html('');
											//목록 불러오기
											axdom("#"+ dlpSearch.getItemId("searchCd")).append('<option value="ALL">전체</option>');
											$.each(JSON.parse(flowList),function() {
												axdom("#"+ dlpSearch.getItemId("searchCd")).append('<option value="'+this.flowId+'">'+ this.flowNm+ '</option>');
											});
											axdom("#"+ dlpSearch.getItemId("searchCd")).append('<option value="FLW">미분류</option>');
											axdom("#"+ dlpSearch.getItemId("searchTxt")).hide();
											axdom("#"+ dlpSearch.getItemId("searchCd")).show();
										} else if (value == "sprintId") {//option 초기화
											axdom("#"+ dlpSearch.getItemId("searchCd")).html('');
										//개발주기가 있는 경우 목록 조회
										if (gfnIsNull(sprintList)) {
											axdom("#"+ dlpSearch.getItemId("searchCd")).append('<option value="">없음</option>');
										} else {
											//목록 불러오기
											$.each(JSON.parse(sprintList),function() {
												axdom("#"+ dlpSearch.getItemId("searchCd")).append('<option value="'+this.sprintId+'">'+ this.sprintNm+ '</option>');
											});
										}
											axdom("#"+ dlpSearch.getItemId("searchTxt")).hide();
											axdom("#"+ dlpSearch.getItemId("searchCd")).show()
										} else {
											//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
											axdom("#"+ dlpSearch.getItemId("searchTxt")).show();
											axdom("#"+ dlpSearch.getItemId("searchCd")).hide();
										}
									}
									},
									{label : "",labelWidth : "",type : "inputText",width : "150",key : "searchTxt",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:0px;",value : ""
										, onkeyup:function(e){
											if(e.keyCode == '13' ){
												axdom("#" + dlpSearch.getItemId("btn_search_dlp")).click();
											}
										}
									},
									{label : "",labelWidth : "",type : "selectBox",width : "100",key : "searchCd",addClass : "selectBox",valueBoxStyle : "padding-left:0px;",value : "01",options : []},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_search_dlp", style:"float:right;" , valueBoxStyle : "padding-left:10px;padding-right:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
										onclick : function() {
											/* 검색 조건 설정 후 reload */
				 							var pars = dlpSearch.getParam();
										    var ajaxParam = $('form#searchFrm').serialize();
				
										    if(!gfnIsNull(pars)){
										    	ajaxParam += "&"+pars;
										    }
											
								            fnInDlpGridListSet(0,ajaxParam);
								            
								            //폼 데이터 변경
											$('#searchSelect').val(axdom("#" + dlpSearch.getItemId("searchSelect")).val());
											$('#searchCd').val(axdom("#" + dlpSearch.getItemId("searchCd")).val());
											$('#searchTxt').val(axdom("#" + dlpSearch.getItemId("searchTxt")).val());
										}
									}
									
							]}]
						});
			}
		};
	}
	
</script>
<div class="main_contents" style="height: auto;" >
	<div class="dpl_title">${sessionScope.selMenuNm }</div>
	<div class="dpl_table_wrap_dpl1100">
	<form:form commandName="dpl1100VO" id="searchFrm" name="searchFrm" method="post"></form:form>
	<div class = "menu_wrap" >
		<div class="tab_contents menu" id="left_contents">
			<div id="AXSearchTarget" style="border-top: 1px solid #ccc;"></div><br>
			<div class="sub_title">
				배포 계획 목록
			</div>
			<div class="dpl_wrap white">
					<!-- <div>개발주기에 지정되어 있는 요구사항 목록</div> -->
				
				<div class="dpl_table_dpl1100 white">
					<div data-ax5grid="dpl-grid" data-ax5grid-config="{}" style="height: 679px;"></div>
				</div>
			</div>
		</div>
		<div class="tab_contents menu" id="right_contents">
			<div id="AXSearchTargetUp" style="border-top:1px solid #ccc;"></div><br>
			<div class="sub_title">
				배정 요구사항 목록
			</div>
			<div class="dpl_wrap white">
				<div class="dpl_table_dpl1100 white">
					<!-- <div>개발주기에 지정되어 있는 요구사항 목록</div> -->
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 290px;"></div>
				</div>
				<br>
				<div id="AXSearchTargetDown" style="border-top:1px solid #ccc;"></div><br>
				<div class="sub_title">
				미배정 요구사항 목록
				</div>
				<div class="dpl_table_dpl1100 white">
					<!-- <div>개발주기에 지정되어 않은 요구사항 목록</div> -->
					<div data-ax5grid="second-grid" data-ax5grid-config="{}" style="height: 290px;"></div>
				</div>
			</div>
		</div>
	</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />