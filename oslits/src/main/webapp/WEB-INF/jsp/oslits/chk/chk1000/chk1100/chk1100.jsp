<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslits/top/aside.jsp" />
<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<style type="text/css">
	.accptFont{color:#4b73eb !important;text-shadow: none !important;}
	.rejectFont{color:#eb4b6a !important;text-shadow: none !important;}
	.defaultFont{color:#000 !important;}
</style>

<script>
var mySearch;
var loginUsrId = '${sessionScope.loginVO.usrId}';

$(document).ready(function(){
	// 팝업 나타내기
	$("#btn_reject").click(function(){
		fnRequestEvent("reject");
	});
	// 팝업 숨기기
	$("#btn_cancel_chk1100").click(function(){
		$("#chkCmntPop").val("");
		$('.approval_box').hide();
	});
	
	//타겟이 상세정보 창이 아닌 경우 상세 정보 창 닫기
	$(document).click(function(event){

	});	
	
	//그리드 및 검색 상자 호출
	fnAxGrid5View();
	fnSearchBoxControl();
});

//요구사항 승인&반려 처리
function fnRequestEvent(type){
	var item = firstGrid.list[firstGrid.selectedDataIndexs[0]];
	
	var reqStatusCd = item.signCd;
	if(reqStatusCd != "01"){
		jAlert("승인상태가 대기 상태인 요구사항만 승인/반려가 가능합니다.","알림창");
		return false;
	}
	
	if(item.signUsrId != loginUsrId){
		jAlert("결재자가 본인일 경우에만 결재 승인/반려가 가능합니다.","알림창");
		return false;
	}
	// 작업흐름 정보 필요
	var nextFlowInfo = fnGetFlowNextNextId(item);

	if(type == "accept"){
		jConfirm("결재 승인하시겠습니까?", "알림", function( result ) {
			if( result ){
				//다음 작업흐름Id의 다음 작업흐름 Id(최종 종료 분기)
				
				var rtnData = item;
				rtnData.signCd ="02";
				rtnData.preFlowId  = item.flowId;
				rtnData.flowNextNextId = nextFlowInfo.flowNextId; // 현재 요구사항읜 다음, 다음 작업흐름 ID
				rtnData.signRegUsrId = item.regUsrId;
				fnReqSignComplete(rtnData);
			}
		});
	}
	//반려
	else if(type == "reject"){
		
		var rtnData = {	
						view: 'chk1100',
						reqId: item.reqId, 
						reqNm: item.reqNm, 
						processId: item.processId, 
						signFlowId: item.signFlowId, 
						signUsrId: item.signUsrId, 
						preFlowId: item.flowId, 
						flowNextNextId: nextFlowInfo.flowNextId, 
						signRegUsrId: item.regUsrId,
						type: 'reject'
					};

		//팝업 화면 오픈
		gfnLayerPopupOpen("/req/req4000/req4100/selectReq4108View.do", rtnData, '500', '290','scroll');
	}

}


//결재 승인&반려 데이터 세팅후 Ajax 전송
function fnReqSignComplete(rtnData){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4900/insertReq4900SignActionAjax.do'/>"},
			rtnData);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYn != "Y"){
			jAlert(data.message,"알림");
			axdom("#" + mySearch.getItemId("btn_search_sign")).click();
		}
		else{
			toast.push(data.message);
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림");
 	});
	//AJAX 전송
	ajaxObj.send();
	
}


// 결재 승인 시 필요한 작업흐름ID 조회
function fnGetFlowNextNextId(selReqInfo){
	
	var nextFlowInfo;
	
	var data = {"prjId" : selReqInfo.prjId, "processId" : selReqInfo.processId, "signFlowId" : selReqInfo.signFlowId};
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/chk/chk1000/chk1100/selectChk1100FlowNextNextIdInfoAjax.do'/>"},
			data);
	
	ajaxObj.setProperty({"async":false});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			nextFlowInfo = data.nextFlowInfo;
		}
		else{
			toast.push(data.message);
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림");
 	});
	//AJAX 전송
	ajaxObj.send();
	
	return nextFlowInfo;
}


//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            sortable:false,
            header: {align:"center"},
          
            columns: [
                		{key: "signCdNm", label: "결재 상태", width: '9%', align: "center"},
                		{key: "signFlowNm", label: "작업흐름", width: '12%', align: "center"},
                		{key: "signUsrNm", label: "결재자", width: '9%', align: "center"},
                		{key: "signDtm", label: "결재 요청 일자", width: '9%', align: "center",formatter:function(){
                			return new Date(this.item.signDtm).format("yyyy-MM-dd");
                		}},
                		{key: "reqNm", label: "요구사항 명", width: '27%', align: "left"},
                		{key: "regUsrNm", label: "요청자", width: '9%', align: "center"},
                		{key: "signRejectCmnt", label: "반려내용", width: '27%', align: "left"},
                		],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	//이전 선택 row 해제
                    this.self.select(firstGrid.selectedDataIndexs[0]);
                	
                	//현재 선택 row 전체 선택
                    this.self.select(this.doindex);
                	
                	var reqId = this.item.reqId;
                	
                },onDBLClick:function(){
                	var data = {"mode": "req", "reqId": firstGrid.list[this.doindex].reqId}; 
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
                    if(item.type == "rowFrozen"){
                    	//이미 해당 열에 고정인경우 중지
                    	if(firstGrid.config.frozenColumnIndex != (param.colIndex+1)){
                    		firstGrid.setConfig({frozenColumnIndex:param.colIndex+1});
                    		fnInGridListSet(firstGrid.page.currentPage);
                    	}
                    //쪽지 전송
                    }else if(item.type == "reply"){
                    	gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
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
				{"url":"<c:url value='/chk/chk1000/chk1100/selectChk1100AjaxView.do'/>","loadingShow":true}
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

	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySearch.setConfig({
				targetID:"AXSearchTarget",
				theme : "AXSearch",
				rows:[
					{display:true, addClass:"top_searchGroup", style:"", list:[
       					{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_update",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>반려</span>",
							onclick:function(){
								fnRequestEvent("reject");
						}},
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_update",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-check' aria-hidden='true'></i>&nbsp;<span>승인</span>",
							onclick:function(){
								fnRequestEvent("accept");
						}}
					]                                            
					},
					{display:true, addClass:"bottom_searchGroup", style:"", list:[
						{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"30", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
							options:[
								{optionValue:"0", optionText:"전체 보기",optionAll:true},
								{optionValue:'reqNm', optionText:'요구사항 명'},
								{optionValue:'reqNo', optionText:'요구사항 번호'},
								{optionValue:"regUsrNm", optionText:"담당자 명"},
								{optionValue:'reqId', optionText:'요구사항 ID'},
								
								{optionValue:"signCd", optionText:"결재 상태", optionCommonCode:"REQ00004"}
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
									axdom("#" + mySearch.getItemId("btn_search_sign")).click();
								}
							}
						},
						{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
							options:[]
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
						
						{label:"", labelWidth:"", type:"button", width:"70",style:"float:right;", key:"btn_print_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
							onclick:function(){
								$(firstGrid.exportExcel()).printThis();
						}},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
							onclick:function(){
								firstGrid.exportExcel("요구사항 검수 승인_거부 목록.xls");
						}},
						
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_search_sign",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
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
					]}
				]
			});
		},
		search1: function(){
			var pars = mySearch.getParam();
			fnAxGridView(pars);
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
		
		// 상단 승인, 반려 버튼이 권한이 없어서 hide일 경우
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
		
	});
}
</script>

<div class="main_contents" style="height: auto;">
	<form:form commandName="chk1100VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;"></form:form>
	<div class="req_title">${sessionScope.selMenuNm}</div>
	<div class="tab_contents menu">
		<input type="hidden" name="strInSql" id="strInSql" />
		<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
		<br/>
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>	
	</div>
</div>

<!-- 
<div class="approval_box">
	<div class="reject_bg">
		<div class='popup'>
			<textarea id='chkCmntPop'></textarea>
			<span class="button_normal2" id="btn_insert_reject">거부 메시지 전달</span>
			<span class="button_normal2" id="btn_cancel_chk1100">취소</span>
		</div>
	</div>
</div>
 -->

<jsp:include page="/WEB-INF/jsp/oslits/bottom/footer.jsp" />