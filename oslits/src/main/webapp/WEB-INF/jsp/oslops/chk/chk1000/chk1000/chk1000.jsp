<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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

//선택 요구사항 ID
var sel_reqId = null;

$(document).ready(function(){
	//타겟이 상세정보 창이 아닌 경우 상세 정보 창 닫기
	$(document).click(function(event){

	});	
	
	//그리드 및 검색 상자 호출
	fnAxGrid5View();
	fnSearchBoxControl();
});
//요구사항 코멘트 저장 이벤트
function fnInsertReqCmntInfo(){
	
	if(gfnIsNull(sel_reqId)){
		toast.push('먼저 요구사항을 선택하시고 코멘트를 등록하세요.');
		return;
	}
	var prjId = "${sessionScope.selPrjId}";
	var reqId = sel_reqId;
	var reqCmnt = $("#reqCmnt").val();
	
	
	
	//입력내용이 없으면
	if( reqCmnt == "" || reqCmnt == null ){
		toast.push('코멘트를 입력하시고 저장하세요.');
		return;
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req2000/req2000/insertReq2000ReqCommentInfoAjax.do'/>"}
			,{"prjId" : prjId, "reqId" : reqId, "reqCmnt" : reqCmnt});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//로딩바 숨김
    	gfnShowLoadingBar(false)
    	
    	//코멘트 등록 실패의 경우 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return;
    	}
    	
    	//코멘트 리스트 세팅
    	gfnSetData2CommentsArea(data.reqCommentList, "reqCmntListDiv");
    	
    	//코멘트 입력창 클리어
    	$("#reqCmnt").val("")
    	
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
           			// 클릭 이벤트
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
     	//프로젝트 옵션(PRJ00009)에 해당하는 옵션값 전달
     	ajaxParam += "&reqMoveFlowChk=${reqMoveFlowChk}";
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/chk/chk1000/chk1000/selectChk1000AjaxView.do'/>","loadingShow":true}
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
					{display:true, addClass:"", style:"", list:[
						{label:"", labelWidth:"", type:"button", width:"70",style:"float:right;", key:"btn_print_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
							onclick:function(){
								$(firstGrid.exportExcel()).printThis({importCSS: false,importStyle: false,loadCSS: "/css/common/printThis.css"});
						}},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_excel_newReqDemand",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
							onclick:function(){
								firstGrid.exportExcel("${sessionScope.selMenuNm}.xls");
						}},
           				
           				{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_sign",style:"float:right;",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
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
           				
						{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
							options:[
                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
                                {optionValue:'reqNm', optionText:'요구사항 명'},
                                {optionValue:"regUsrNm", optionText:"요청자명"},
                                {optionValue:'reqId', optionText:'요구사항 ID'},
                           
                                {optionValue:"signCd", optionText:"결재 상태", optionCommonCode:"REQ00004"},
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
						{label:"<i class='fa fa-arrows-v'></i>&nbsp;목록 높이", labelWidth:"100", type:"selectBox", width:"", key:"gridHeight", addClass:"", valueBoxStyle:"", value:"600",
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

	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />