<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<style type="text/css">
	.accptFont{color:#4b73eb !important;text-shadow: none !important;}
	.rejectFont{color:#eb4b6a !important;text-shadow: none !important;}
	.defaultFont{color:#000 !important;}
</style>
<script>
var mySearch;
var mySecondSearch;
var myThirdSearch;

var secondGrid;
var thirdGrid;


$(function(){
	//타겟이 상세정보 창이 아닌 경우 상세 정보 창 닫기
	$(document).click(function(event){
		//타겟이 상세정보 창, 그리드 노드가 아닌 경우 확인
		if(!$(event.target).is('.slideBox') && !$(event.target.offsetParent).is('.slideBox, .req_right_table_wrap')){
			//디테일 창이 표시되어 있는지 확인, 그리드 선택인지 확인
		    if((!$(event.target).is($('[data-ax5grid] *')) || !$(event.target.offsetParent).is($('[data-ax5grid] *'))) && !$('.req_right_table_wrap').hasClass('on')){
				$('.req_right_table_wrap, .slideBox, .slideBox > img').toggleClass('on');
	    	} 
		}
	});	
	
	//우측 디테일 창 'ESC'버튼으로도 닫히기
	$(document).on('keydown', function(e){
		if(e.keyCode==27 && !$('.req_right_table_wrap').hasClass('on')){
			$('.req_right_table_wrap, .slideBox, .slideBox > img').toggleClass('on');	
		};
	});
	
	//우측 디테일 창
	$('.slideBox').click(function(){
		$('.req_right_table_wrap, .slideBox, .slideBox > img').toggleClass('on');
	});
	
	//그리드 검색 호출
	fnAxGrid5View();
	fnSearchBoxControl();
	
	fnAxSecondGrid5View();
	fnSearchSecondBoxControl();

	fnAxThirdGrid5View();
	fnSearchThirdBoxControl();
});



//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
         
            sortable:false,
     
            header: {align:"center"  },
                      
            columns: [
				
				{key: "reportYear", label: "기준연도", width: 190, align: "center"} 
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick:function(){
                	var reportYear=firstGrid.list[this.doindex].reportYear;
                	fnInMasterGridListSet(reportYear);   
                      

                }
            } 
        });
        //그리드 데이터 불러오기
 		fnInGridListSet();

}

function fnAxSecondGrid5View(){
	secondGrid = new ax5.ui.grid();
 
	secondGrid.setConfig({
            target: $('[data-ax5grid="second-grid"]'),
         
            sortable:false,
            showRowSelector: true,
            multipleSelect: false  ,
            header: {align:"center", selector : false  },
          
            columns: [
				{key: "reportYear", label: "기준연도", width: 70, align: "center"},
				{key: "reportNm", label: "보고서명", width: 180, align: "left"}, 
				{key: "confYnNm", label: "확정여부", width: 70, align: "center"} 
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick:function(){
                	 secondGrid.select(this.doindex, {selected: true});
                	 fnSelectAdm8000DetailList(secondGrid.list[this.doindex].reportYear,secondGrid.list[this.doindex].reportCd);		
                }
            } 
        });

}


function fnAxThirdGrid5View(){
	thirdGrid = new ax5.ui.grid();
 
	thirdGrid.setConfig({
            target: $('[data-ax5grid="third-grid"]'),
         
            sortable:false,
            showRowSelector: true,
            multipleSelect: false  ,
            header: {align:"center", selector : false  },
          
            columns: [
				
				{key: "indexNm", label: "지표구분", width: '10%', align: "center"}, 
				{key: "itemNm", label: "측정항목", width: '24%', align: "left"} ,
				{key: "itemTypeNm", label: "측정항목구분", width: '20%', align: "center"}, 
				{key: "weightVal", label: "가중치", width: '10%', align: "right"},
				{key: "periodNm", label: "보고/평가주기", width: '13%', align: "center"},
				{key: "processNm", label: "프로세스명", width: '15%', align: "left"},
				{key: "periodOrd", label: "순번", width: '10%', align: "right"}
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick:function(){
                	thirdGrid.select(this.doindex, {selected: true});
                }
            } 
        });

}
//그리드 데이터 넣는 함수
function fnInGridListSet(mode){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm8000/adm8000/selectAdm8000MasterYearList.do'/>","loadingShow":true}
				);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			
		   	firstGrid.setData(list);
		   	if(gfnIsNull(mode)){
			   	if(list.length>0){
			   		
			   		fnInMasterGridListSet(list[0].reportYear);    
			   	}
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






function fnDeleteAdm8000YearInfo(reportYear){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm8000/adm8000/deleteAdm8000MasterInfoAjax.do'/>"}
			,{ "reportYear" : reportYear  });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
		fnInMasterGridListSet(reportYear);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
	});
	
	//AJAX 전송
	ajaxObj.send();
} 



/**
 * 보고서 마스터  삭제
 */
function fnDeleteAdm8000Info(reportYear,reportCd){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm8000/adm8000/deleteAdm8000MasterInfoAjax.do'/>"}
			,{ "reportYear" : reportYear , "reportCd" : reportCd  });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
		fnInGridListSet();
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
	});
	
	//AJAX 전송
	ajaxObj.send();
} 



/**
 * 보고서 디테일  삭제
 */
function fnDeleteAdm8000DetailInfo(reportYear,reportCd,itemCd){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm8000/adm8000/deleteAdm8000DetailInfoAjax.do'/>"}
			,{ "reportYear" : reportYear , "reportCd" : reportCd ,"itemCd" : itemCd });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
		fnSelectAdm8000DetailList(reportYear,reportCd);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
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
						
						{label : "",labelWidth : "",type : "button",width : "55",key : "btn_delete_report",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
							onclick : function() {
								var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
								if(gfnIsNull(item)){
									toast.push('삭제 할 목록을 선택하세요.');
									return;
								}
								
								console.log(item);
								
								if(item.confCnt == 0){
									if(confirm("하위 항목까지 삭제 됩니다. 삭제 하시겠습니까?")){
										fnDeleteAdm8000YearInfo(item.reportYear);	
									}
								}else{
									jAlert("확정된 항목이 있습니다. 삭제할수 없습니다.", "알림창");
								}
								
							}
						},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_insert_copyReport",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>복사</span>",
							onclick:function(){
								var data ={};
								var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
								if(gfnIsNull(item)){
									data = {
											"popupGb": "insert"
										};
								}else{
									data = {
											"popupGb": "selectInsert" , "reportYear" : item.reportYear
									};
								}
								 
								gfnLayerPopupOpen('/adm/adm8000/adm8000/selectAdm8003View.do',data,"450","330",'scroll');
						}}
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
		//버튼 권한 확인
		fnBtnAuthCheck(mySearch);
	});
}

function fnSearchSecondBoxControl(){
	var pageID = "AXSearch";
	mySecondSearch = new AXSearch();

	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySecondSearch.setConfig({
				targetID:"AXSecondSearchTarget",
				theme : "AXSearch",
				rows:[
						
					{display:true, addClass:"", style:"", list:[
						
						{label : "",labelWidth : "",type : "button",width : "55",key : "btn_delete_reportMaster",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
							onclick : function() {
								var item = (!gfnIsNull(Object.keys(secondGrid.focusedColumn)))? secondGrid.list[secondGrid.focusedColumn[Object.keys(secondGrid.focusedColumn)].doindex]:null;
								
								if(gfnIsNull(item)){
									var chkList = secondGrid.getList('selected');
		
									$(chkList).each(function(idx, data) {
										if(data.__selected__){
											item=data;
										}
									});	
									
								}
								
								if(gfnIsNull(item)){
									toast.push('삭제 할 목록을 선택하세요.');
									return;
								}
								
								if(item.confYn=="01"){
									jAlert("확정된 항목은 삭제할수 없습니다.", "알림창");
								}else{
									if(confirm("하위 항목까지 삭제 됩니다. 삭제 하시겠습니까?")){
										fnDeleteAdm8000Info(item.reportYear,item.reportCd);
									}
										
								}
								
							}
						},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_update_reportMaster",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
							onclick:function(){
								var item = (!gfnIsNull(Object.keys(secondGrid.focusedColumn)))? secondGrid.list[secondGrid.focusedColumn[Object.keys(secondGrid.focusedColumn)].doindex]:null;
								
								if(gfnIsNull(item)){
									var chkList = secondGrid.getList('selected');
									
									$(chkList).each(function(idx, data) {
										if(data.__selected__){
											item=data;
										}
									});	
								}
								
								if(gfnIsNull(item)){
									toast.push('수정 할 목록을 선택하세요.');
									return;
								}
								
								if(item.confYn=="01"){
									jAlert("확정된 항목은  수정할수 없습니다.", "알림창");
									return;
								}
																
								var data = {"popupGb": "update", "reportYear": item.reportYear, "reportCd": item.reportCd};
        	                	
								gfnLayerPopupOpen('/adm/adm8000/adm8000/selectAdm8001View.do',data,"450","330",'scroll');
						}},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_insert_reportMaster",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>추가</span>",
							onclick:function(){
								var data ={};
								var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
			
								if(gfnIsNull(item)){
									data = {
											"popupGb": "insert"
										};
								}else{
									data = {
											"popupGb": "selectInsert" , "reportYear" : item.reportYear
									};
								}
								 
								gfnLayerPopupOpen('/adm/adm8000/adm8000/selectAdm8001View.do',data,"450","330",'scroll');
						}}
					]}
				]
			});
		}
	};
	
	jQuery(document.body).ready(function(){
		
		fnObjSearch.pageStart();

	});
}


function fnSearchThirdBoxControl(){
	var pageID = "AXSearch";
	myThirdSearch = new AXSearch();

	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			myThirdSearch.setConfig({
				targetID:"AXThirdSearchTarget",
				theme : "AXSearch",
				rows:[
						
					{display:true, addClass:"", style:"", list:[
						
						{label : "",labelWidth : "",type : "button",width : "55",key : "btn_delete_reportDetail",style : "float:right;",valueBoxStyle : "padding:5px;",value : "<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
							onclick : function() {
								var item = (!gfnIsNull(Object.keys(thirdGrid.focusedColumn)))? thirdGrid.list[thirdGrid.focusedColumn[Object.keys(thirdGrid.focusedColumn)].doindex]:null;
								if(gfnIsNull(item)){
									var chkList = thirdGrid.getList('selected');
								
									$(chkList).each(function(idx, data) {
										if(data.__selected__){
											item=data;
										}
									});									
								}
								
								if(gfnIsNull(item)){
									toast.push('삭제 할 목록을 선택하세요.');
									return;
								}
								
								var chkList = secondGrid.getList('selected');
								var pItem={};
								$(chkList).each(function(idx, data) {
									if(data.__selected__){
										pItem=data;
									}
								});								
								
								if(pItem.confYn=='01'){
									jAlert("확정되었습니다 항목을 삭제할수 없습니다.", '알림창');
									return;
								}
								if(confirm("삭제 하시겠습니까?")){
									fnDeleteAdm8000DetailInfo( item.reportYear, item.reportCd, item.itemCd );	
								}
								
							}
						},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_update_reportDetail",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
							onclick:function(){
								var item = (!gfnIsNull(Object.keys(thirdGrid.focusedColumn)))? thirdGrid.list[thirdGrid.focusedColumn[Object.keys(thirdGrid.focusedColumn)].doindex]:null;
								if(gfnIsNull(item)){
									var chkList = thirdGrid.getList('selected');
								
									$(chkList).each(function(idx, data) {
										if(data.__selected__){
											item=data;
										}
									});									
								}
								
								if(gfnIsNull(item)){
									toast.push('수정 할 목록을 선택하세요.');
									return;
								}
								
								var chkList = secondGrid.getList('selected');
								var pItem={};
								$(chkList).each(function(idx, data) {
									if(data.__selected__){
										pItem=data;
									}
								});								
								
								if(pItem.confYn=='01'){
									jAlert("확정되었습니다 항목을 수정할수 없습니다.", '알림창');
									return;
								}
								
								
								var data = {"popupGb": "update", "reportYear": item.reportYear, "reportCd": item.reportCd, "itemCd": item.itemCd};
        	                	
								gfnLayerPopupOpen('/adm/adm8000/adm8000/selectAdm8002View.do',data,"650","450",'scroll');
						}},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_insert_reportDetail",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>추가</span>",
							onclick:function(){
								var item = (!gfnIsNull(Object.keys(secondGrid.focusedColumn)))? secondGrid.list[secondGrid.focusedColumn[Object.keys(secondGrid.focusedColumn)].doindex]:null;
								if(gfnIsNull(item)){
									var chkList = secondGrid.getList('selected');
									$(chkList).each(function(idx, data) {
										if(data.__selected__){
											item=data;
										}
									});		
								}
								
								if(gfnIsNull(item)){
									toast.push('등록 할 보고서 마스터 목록을 선택하세요.');
									return;
								}
								
								if(item.confYn=='01'){
									jAlert("확정되었습니다 항목을 추가할수 없습니다.", '알림창');
									return;
								}
								
								var data = {
									"popupGb": "insert" , "reportYear": item.reportYear, "reportCd": item.reportCd
								};
								gfnLayerPopupOpen('/adm/adm8000/adm8000/selectAdm8002View.do',data,"650","450",'scroll');
						}}
					]}
				]
			});
		}
	};
	
	jQuery(document.body).ready(function(){
		
		fnObjSearch.pageStart();

	});
}


//그리드 데이터 넣는 함수
function fnInMasterGridListSet(year){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm8000/adm8000/selectAdm8000MasterList.do'/>","loadingShow":true}
				,{"reportYear": year});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			
		   	secondGrid.setData(list);
		   	if(list.length>0){
		   		secondGrid.select(0, {selected: true});
		   		fnSelectAdm8000DetailList(list[0].reportYear,list[0].reportCd);	
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



function fnSelectAdm8000DetailList(reportYear,reportCd){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm8000/adm8000/selectAdm8000DetailList.do'/>","loadingShow":true}
			,{ "reportYear" : reportYear , "reportCd" : reportCd });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		var list = data.list;
		
	   	thirdGrid.setData(list);
	   	if(list.length>0){
	   		thirdGrid.select(0, {selected: true});	
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


</script>


<div class="main_contents" style="height: auto;">
	<form:form commandName="adm8000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
	</form:form>
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
		<input type="hidden" name="strInSql" id="strInSql" />
		<table style="width:100%;">
			<tr>
				<td style="width:13%;">
					<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
					<br />
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
				</td>
				<td style="width:1%;"></td>
				<td style="width:23%;" >
					<div id="AXSecondSearchTarget" style="border-top:1px solid #ccc;"></div>
					<br />
					<div data-ax5grid="second-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
				</td>
				<td style="width:1%;"></td>
				<td style="width:62%;" >
					<div id="AXThirdSearchTarget" style="border-top:1px solid #ccc;"></div>
					<br />
					<div data-ax5grid="third-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
				</td>
			</tr>
		</table>
			
	</div>
</div>
		
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />