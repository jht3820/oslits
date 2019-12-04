<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/js/axisj/dist/ax5/ax5select.min.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>
<script>
	/*   
		business function 
	*/
	//사용자 타입
	var usrTyp = "${sessionScope.usrTyp}";
	
	var clickFlag = 0;
	var mySearch;
	var prjInfoStDt = '${currPrjInfo.startDt }';
	var prjInfoEdDt = '${currPrjInfo.endDt }';
	// 좌측 담당 요구사항 조회 시 담당자명
	var reqChargerNmParam  = '${param.reqChargerNm}';
	
	/* 요구사항 상세 삭제 */
	var fnDeleteReqInfo = function(chkList){
		var params = "";
		var rowChk = false;
		
		var delCount = 0;
		if(gfnIsNull(chkList)){
			jAlert("선택한 요구사항이 없습니다.","알림창");
			return false;
		}
		$(chkList).each(function(idx, val){
			
			// 접수 요청을 제외하고 요구사항 삭제 불가
			if( val.reqProType != "01" ){
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
			jAlert('처리중, 반려, 최종완료된 요구사항은 삭제할 수 없습니다.','알림창');
			return false;
		}
		
		if(delCount===0){
			jAlert("선택한 요구사항이 없습니다.","알림창");
			return false;
		}

		jConfirm("요구사항을 삭제하시겠습니까?", "알림창", function( result ) {
			if( result ){
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/req/req4000/req4100/deleteReq4100ReqInfoAjax.do'/>", "loadingShow":true}
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
						{key: "reqOrd", label: "순번", width: '7%', align: "center"},
						{key: "reqProTypeNm", label: "처리유형", width: '9%', align: "center"},
						{key: "reqNm", label: "요구사항 명", width: '20%', align: "left"},
						{key: "reqDesc", label: "요구사항 설명", width: '30%', align: "left"},
						{key: "reqNo", label: "공문번호", width: '9%', align: "center"},
						{key: "reqNewTypeNm", label: "접수유형", width: '9%', align: "center"},
						{key: "reqUsrNm", label: "요청자", width: '9%', align: "center"},
						{key: "reqChargerNm", label: "담당자", width: '9%', align: "center"},
						{key: "reqDtm", label: "요청일자", width: '9%', align: "center"},
						{key: "reqId", label: "요구사항 ID", width: '12%', align: "center"},
						{key: "processNm", label: "프로세스 명", width: '8%', align: "center"},
						{key: "flowNm", label: "작업흐름 명", width: '8%', align: "center"},
						{key: "regDtmDay", label: "등록일자", width: '9%', align: "center"},
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
	                	var data = {"mode": "req", "reqId": reqId, "reqPageType": "admReqPage"}; 
	                	var popHeight = "800";
	                	
	                	//처리유형이 접수요청(01)
						if(reqProType == "01" ){
							//권한 체크해서 사용자인경우 상세보기로 전환
							if(usrTyp == "01"){
				               	var data = {
				               			"mode": "req", 
				               			"reqId": this.item.reqId,
				               			"reqProType": this.item.reqProType,
				               			"reqPageType" : "usrReqPage"
				               	}; 
				               	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '890','scroll');
							}else{
								var data = {"reqId": this.item.reqId}; 
								gfnLayerPopupOpen("/req/req4000/req4100/selectReq4106View.do", data, '900', '950','auto');
							}
						}
	                	// 반려(03)일 경우 req1002.jsp 상세보기 화면으로
	                	else if(reqProType == "03"){
							
							reqProType == "03" ? popHeight = "930" : popHeight = "890";
							
							gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', popHeight,'scroll');	
						}else{
							//권한 체크해서 사용자인경우 상세보기로 전환
							if(usrTyp == "01"){
								var data = {"mode":"newReq","reqId": this.item.reqId, "reqProType":"02"}; 
								gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
							}else{
								var data = {"reqId": this.item.reqId}; 
								gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1330', '900','scroll',false);
							}
						}
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
	                    {type: "detailPopup", label: "상세 정보", icon:"<i class='fa fa-info-circle' aria-hidden='true'></i>"},
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

	                    if(item.type == "update"){
	                    	
	                    	// 접수요청인 요구사항만 수정가능
	                    	if(selItem.reqProType != "01"){
	                    		jAlert('접수 요청인 요구사항만 수정 가능합니다.','알림창');
								return false;
	                    	}
	                    	
							var data = {
								"reqId": selItem.reqId,
								"type" : "update"	
						};
						gfnLayerPopupOpen('/req/req4000/req4100/selectReq4101View.do',data,"640","933",'scroll');
						
						//열 고정
	                    }else if(item.type == "rowFrozen"){
	                    	//이미 해당 열에 고정인경우 중지
	                    	if(firstGrid.config.frozenColumnIndex != (param.colIndex+1)){
	                    		firstGrid.setConfig({frozenColumnIndex:param.colIndex+1});
	                    		fnInGridListSet(firstGrid.page.currentPage);
	                    	}
	                    //쪽지 전송
	                    }else if(item.type == "reply"){
	                    	gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
	                    }
	                    //상세 정보
						else if(item.type == "detailPopup"){
							// 처리유형이 접수대기(01), 반려(03)인 경우 접수 상세 화면
	                    	if(param.item.reqProType == "01" || param.item.reqProType == "03"){
	                    		/*
				               	 * reqPageType 추가
				               	 * 요구사항 상세보기(req1002.jsp)에서 항목명 구분을 위해 사용
				               	 * usrReqPage - 요구사항 요청(사용자) 
				               	 * admReqPage - 전체 요구사항 목록, 요구사항 생성관리(관리자)
				               	 */
				               	var data = {
				               			"mode": "req", 
				               			"reqId": param.item.reqId,
				               			"reqProType": param.item.reqProType,
				               			"reqPageType" : "usrReqPage"
				               	};
				               	
				  				// 반려일경우 높이 890, 접수요청일경우 높이 850
				               	var popHeight = param.item.reqProType == "03" ? "930" : "890";
				               	
				               	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', popHeight ,'scroll');
				               	
	                    	}else{
	                    		var data = {"mode":"newReq","reqId": param.item.reqId, "reqProType":param.item.reqProType}; 
								gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
	                    	}
							
						}
	                    firstGrid.contextMenu.close();
	                    
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

	        // 좌측 메뉴 담당 요구사항 조회를 클릭하지 않았을 경우(= 담당자 이름 null) 그리드 조회
	        // 이렇게 하지 않을 경우 비동기로 데이터를 조회하므로 담당 요구사항 클릭 시 전체 요구사항 목록이 출력되는 경우 발생
	        //if(gfnIsNull(reqChargerNmParam)){
	        	//그리드 데이터 불러오기
			 	//fnInGridListSet();
	       	//}
		    

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
				{"url":"<c:url value='/req/req4000/req4100/selectReq4100ListAjaxView.do'/>","loadingShow":true}
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
	
	//공통코드가 아닌 목록이 필요할 때 가져오는 함수
	function fnSelectBoxSet(url, ajaxParam){
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":url,"loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);

	    	if(data.ERROR_CODE == '-1'){
	    		jAlert(data.ERROR_MSG,'알림창');
				return;
			}
	    	axdom("#" + mySearch.getItemId("searchCd")).html('');
	    	axdom("#" + mySearch.getItemId("searchCd")).append()
	    			.append($("<option>",{value:'', text: "전 체"}));
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
						{display:true, addClass:"top_searchGroup", style:"", list:[
							{label:"", labelWidth:"", type:"button", width:"70",style:"float:right;", key:"btn_insert_excelPopup",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-upload' aria-hidden='true'></i>&nbsp;<span>업로드</span>",
							onclick:function(){
								gfnLayerPopupOpen('/req/req4000/req4100/selectReq4103View.do',{},"1000","891",'scroll');
							}},
							{label:"", labelWidth:"", type:"button", width:"100",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-download' aria-hidden='true'></i>&nbsp;<span>양식 다운로드</span>",
							onclick:function(){
								$.download('/etc/uploadRequestForm.xlsx','tmp','post');
							}},
							{label:"", labelWidth:"", type:"button", width:"70",style:"float:right;", key:"btn_print_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
								onclick:function(){
									$(firstGrid.exportExcel()).printThis({importCSS: false,importStyle: false,loadCSS: "/css/common/printThis.css"});
							}},
							{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
								onclick:function(){
									// 엑셀 다운로드 함수 호출
	       							fnExcelDownLoad();
	       							// 기존방식 - ax5grid의 함수 호출하여 엑셀 다운로드 하는방법
									//firstGrid.exportExcel("all_request_list.xls");
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
									toast.push('먼저 요구사항을 선택해주세요');
									return;
								}
								var selReqId = item.reqId;
								var reqProType = item.reqProType;

								// 접수요청인 요구사항만 수정 가능
		                    	if(reqProType != "01"){
		                    		jAlert('접수 요청인 요구사항만 수정 가능합니다.','알림창');
									return false;
		                    	}

								var data = {
									"reqId": selReqId,
									"type" : "update"	
								};
								gfnLayerPopupOpen('/req/req4000/req4100/selectReq4101View.do',data,"640","880",'scroll');
							}},
							{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_insert_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
							onclick:function(){
								var data = {
									"type" : "insert"
								};
								
								gfnLayerPopupOpen('/req/req4000/req4100/selectReq4101View.do',data,"640","880","scroll");
							}},
							{label:"", labelWidth:"", type:"button", width:"55", style:"float:right;", key:"btn_search_req",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
	 							var pars = mySearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();
	
							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }

					            fnInGridListSet(0,ajaxParam);
					            
					            //폼 데이터 변경
								$('#searchSelect').val(axdom("#" + mySearch.getItemId("searchSelect") ).val());
								$('#searchCd').val(axdom("#" + mySearch.getItemId("searchCd")).val() );
								$('#searchTxt').val(axdom("#" + mySearch.getItemId("searchTxt")).val() );
							}}
						]},{display:true, addClass:"bottom_searchGroup", style:"", list:[
						    {label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"30", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
								options:[
		                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
		                                {optionValue:'reqNm', optionText:'요구사항 명'},
		                                {optionValue:'reqDesc', optionText:'요구사항 설명'},
		                                {optionValue:'reqUsrNm', optionText:'요청자'},
		                                {optionValue:'reqChargerNm', optionText:'담당자'},
		                                {optionValue:'processNm', optionText:'프로세스 명'},
		                                {optionValue:'reqNo', optionText:'공문번호'},
		                                {optionValue:'reqId', optionText:'요구사항 ID'},
		                                {optionValue:"reqProType", optionText:"처리유형", optionCommonCode:"REQ00008"},
		                                {optionValue:"reqNewType", optionText:"접수유형", optionCommonCode:"REQ00009"},
		                                {optionValue:"reqTypeCd", optionText:"요구사항 유형", optionCommonCode:"REQ00012"},
		                                {optionValue:'reqOrd', optionText:'순번'}
		                                
		                            ],onChange: function(selectedObject, value) {
		                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
		    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
											axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");	
											axdom("#" + mySearch.getItemId("searchTxt")).val('');	
										}else{
											axdom("#" + mySearch.getItemId("searchTxt")).removeAttr("readonly");
										}
		                            	
		    							//공통코드 처리 후 select box 세팅이 필요한 경우 사용
										if( !gfnIsNull(selectedObject.optionCommonCode) ){
											gfnCommonSetting(mySearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
										} else {
											//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
											axdom("#" + mySearch.getItemId("searchTxt")).show();
											axdom("#" + mySearch.getItemId("searchCd")).hide();
										}
		    						}
							},
							{label:"", labelWidth:"", type:"inputText", width:"150", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
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
								         	 {optionValue:300, optionText:"300px"}
			                                ,{optionValue:600, optionText:"600px"}
			                                ,{optionValue:1000, optionText:"1000px"}
			                                ,{optionValue:1200, optionText:"1200px"}
			                                ,{optionValue:2000, optionText:"2000px"}
			                            ],onChange: function(selectedObject, value){
			                            	firstGrid.setHeight(value);
			    						}
							}
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
			
			//좌측메뉴 작업흐름으로 조회인 경우
		    if(!gfnIsNull("${flowId}")){
		    	//option 초기화
		    	axdom("#" + mySearch.getItemId("searchCd")).html('');
		    	
		    	//목록 불러오기
				$.each(flowList,function(){
					axdom("#"+mySearch.getItemId("searchCd")).append('<option value="'+this.flowId+'">'+this.flowNm+'</option>');	
				});
				axdom("#"+mySearch.getItemId("searchCd")).append('<option value="FLW">미분류</option>');
		    	//파라미터 세팅
				axdom("#" + mySearch.getItemId("searchSelect")).val('flowId');
		    	axdom("#" + mySearch.getItemId("searchCd")).val("${flowId}");
		    	axdom("#" + mySearch.getItemId("searchTxt")).hide();
				axdom("#" + mySearch.getItemId("searchCd")).show();
				
				//그리드 검색 호출
				var pars = mySearch.getParam();
				fnInGridListSet(0,pars);
		    }
			
			// 좌측(aside.jsp)에서 담당 요구사항 메뉴를 클릭했을 경우
		    if(!gfnIsNull(reqChargerNmParam)){ 
		    	
		    	//option 초기화
		    	axdom("#" + mySearch.getItemId("searchTxt")).html('');
		    	axdom("#" + mySearch.getItemId("searchTxt")).removeAttr("readonly");
				//파라미터 세팅
		    	axdom("#" + mySearch.getItemId("searchSelect")).val('reqChargerNm');
		    	axdom("#" + mySearch.getItemId("searchTxt")).val(reqChargerNmParam);
		    	
				axdom("#" + mySearch.getItemId("searchTxt")).show();
				axdom("#" + mySearch.getItemId("searchCd")).hide();
				
				//그리드 검색 호출
				var pars = mySearch.getParam();
				
				// 담당자 명으로 요구사항 검색
				fnInGridListSet(0,pars);
		    }

		 	// 화면 로드 시 그리드 데이터 불러오기
			// 기본 세팅된 날짜를 이용해서 요구사항 조회
			axdom("#" + mySearch.getItemId("srchFromDt")).val();
			axdom("#" + mySearch.getItemId("srchToDt")).val();
			
			var pars = mySearch.getParam();
			fnInGridListSet(0,pars);
			
		});
	}
	/*
		Initialize	
	*/
	
	$(document).ready(function() {
		//그리드 및 검색 상자 호출
		fnAxGrid5View();
		// 검색상자 호출시 그리드 데이터 조회
		fnSearchBoxControl();
	});

	/* 전체 요구사항 관리 엑셀 다운로드 */
	function fnExcelDownLoad(){ 

		var searchSelect = axdom("#" + mySearch.getItemId("searchSelect")).val();	// 검색 콤보박스
		var searchCd = axdom("#" + mySearch.getItemId("searchCd")).val();			// 검색어 대신 콤보박스에서 선택하는 검색조건 (처리유형)
		var searchTxt = axdom("#" + mySearch.getItemId("searchTxt")).val();			// 검색어
		var srchFromDt = axdom("#" + mySearch.getItemId("srchFromDt")).val();		// 검색 시작일
		var srchToDt = axdom("#" + mySearch.getItemId("srchToDt")).val();			// 검색 종료일
		
		var excelForm = document.getElementById("req4100_excel_down_Form");
		
		excelForm.searchSelect.value = searchSelect;
		excelForm.searchTxt.value = searchTxt;
		excelForm.searchCd.value = searchCd;
		excelForm.srchFromDt.value = srchFromDt;
		excelForm.srchToDt.value = srchToDt;
		excelForm.action = "<c:url value='/req/req4000/req4100/selectReq4100ExcelList.do'/>";
		excelForm.submit();
		return false;
	}
	
</script>

<style>
input[type="text"].search_txt { float: none; width: 250px; height: 28px; line-height:1; padding: 5px; font-size: 1em; vertical-align: top; border: 1px solid #ccc; }
</style>
		<div class="main_contents">
			<form id="req4100_excel_down_Form" method="post">
				<input type="hidden" name="searchSelect">
				<input type="hidden" name="searchTxt">
				<input type="hidden" name="srchFromDt">
				<input type="hidden" name="srchToDt">
				<input type="hidden" name="searchCd">
			</form>
			<div class="req_title">${sessionScope.selMenuNm }</div>
			<div class="tab_contents menu">
				<input type="hidden" id="pageIndex" name="pageIndex"  value="1"/>
				<form:form commandName="req4100VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
					 <form:hidden path="pageIndex" id="pageIndex" name="pageIndex"  /> 
				</form:form>
				<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
				<br />
				<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
			</div>
		</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />