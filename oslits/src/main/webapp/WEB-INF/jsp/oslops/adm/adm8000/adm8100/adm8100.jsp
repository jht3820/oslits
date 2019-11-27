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
});


//검색조건 변경되었을때 이벤트
function fnSelectChg(){
	var selVal = $("#searchSelect option:selected").val();
	if(selVal == '0'){
		$("#searchTxt").val("");
		$("#searchTxt").attr("readonly", true);
	}else{
		$("#searchTxt").attr("readonly", false);
	}
}

//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            
            sortable:false,
            header: {align:"center"},
            columns: [

				{key: "reportNm", label: "보고서명", width: 620, align: "left"},
				{key: "tmNm", label: "팀장명", width: 150, align: "left"},
				{key: "chargerNm", label: "담당자명", width: 150, align: "left"},
				{key: "pmNm", label: "PM명", width: 150, align: "left"},
				{key: "meaFmDtm", label: "측정월", width: 180, align: "center"},
				{key: "writeYnNm", label: "작성여부", width: 100, align: "center"},
				{key: "confYnNm", label: "확정여부", width: 100, align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30, 
                onDBLClick:function(){
                	var item = firstGrid.list[this.doindex];
                	                	
                	var data = {"meaDtm": item.meaDtm, "reportCd": item.reportCd, "reportNm": item.reportNm,"meaFmDtm" : item.meaFmDtm};
					                	
					gfnLayerPopupOpen('/adm/adm8000/adm8100/selectAdm8103View.do',data,"680","870",'scroll');
                }
            } ,
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
        
        var defaultStartDt = gfnGetServerTime('yyyy')+"-01";
	    var newYearDate = new Date(defaultStartDt);   	    	
	    var defaultEndtDt = gfnGetMonthAgo(newYearDate,-11,'yyyy-mm');
	    var ajaxParam = "&endDt="+defaultEndtDt+"&startDt="+defaultStartDt;
        
        //그리드 데이터 불러오기
 		fnInGridListSet(0,ajaxParam);

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
				{"url":"<c:url value='/adm/adm8000/adm8100/selectAdm8100ReportListAjax.do'/>","loadingShow":true}
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
	var defaultStartDt = gfnGetServerTime('yyyy')+"-01";
	var newYearDate = new Date(defaultStartDt);   	    	
	var defaultEndtDt = gfnGetMonthAgo(newYearDate,-11,'yyyy-mm');
	
	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySearch.setConfig({
				targetID:"AXSearchTarget",
				theme : "AXSearch",
				rows:[
					{display:true, addClass:"", style:"", list:[
							
							{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_update_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>확정</span>",
								onclick:function(){
									var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
									if(gfnIsNull(item)){
										toast.push('확정 할 목록을 선택하세요.');
										return;
									}
									
									if(item.confYn=="01"){
										jAlert("이미 확정되었습니다.", "알림창");
										return;
									}

									if(item.writeYn=="02"){
										jAlert("보고서가 미작성 되었습니다 작성후에 확정처리 하세요.", "알림창");
										return;
									}
									
									if(gfnIsNull(item.tmNm)){
										
										jAlert("보고서에 팀장이 입력되지 않았습니다. 팀장을 입력하세요", "알림창");
										return;
									}
									
									if(gfnIsNull(item.chargerNm)){
									
										jAlert("보고서에 담당자를 입력되지 않았습니다. 담당자를 입력하세요", "알림창");
										return;
									}
									
									if(gfnIsNull(item.pmNm)){
										jAlert("보고서에 PM을 입력되지 않았습니다. PM를 입력하세요", "알림창");
										return;
									}
								 
									fnUpdateAdm8100ReporConfirm( item.meaDtm, item.reportCd );
							}},
							
							{label:"", labelWidth:"", type:"button", width:"100",style:"float:right;", key:"btn_insert_rep",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>보고서생성</span>",
								onclick:function(){
									var data={};									
									gfnLayerPopupOpen('/adm/adm8000/adm8100/selectAdm8101View.do',data,"480","230",'scroll');
							}}
						]
					},
					
					{display:true, addClass:"", style:"", list:[

						{label:"<i class='fa fa-search'></i>&nbsp;<i>기간</i>&nbsp;&nbsp;", labelWidth:"70", type:"inputText", width:"70", key:"startDt", addClass:"secondItem readonly", valueBoxStyle:"", value:defaultStartDt,
							onChange: function(){}
						},
						{label:"", labelWidth:"", type:"inputText", width:"90", key:"endDt", addClass:"secondItem readonly", valueBoxStyle:"padding-left:0px;", value:defaultEndtDt,
							AXBind:{
								type:"twinDate" , config:{
									align:"right", valign:"top", startTargetID:"startDt" , selectType:"m"
								}
							}
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
						},
						
						
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_update_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
							onclick:function(){
								var item = (!gfnIsNull(Object.keys(firstGrid.focusedColumn)))? firstGrid.list[firstGrid.focusedColumn[Object.keys(firstGrid.focusedColumn)].doindex]:null;
								if(gfnIsNull(item)){
									toast.push('수정 할 목록을 선택하세요.');
									return;
								}
								
								if(item.confYn=="01"){
									jAlert("이미 확정되었습니다.", "알림창");
									return;
								}
								
								var data = {"popupGb": "update", "meaDtm": item.meaDtm, "reportCd": item.reportCd, "reportNm": item.reportNm,"meaFmDtm" : item.meaFmDtm};
								
        	                	
								gfnLayerPopupOpen('/adm/adm8000/adm8100/selectAdm8102View.do',data,"680","850",'scroll');
						}},
						{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_api",  style:"float:right;" , valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
	 							var pars = mySearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();

							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }
								
					            fnInGridListSet(0,ajaxParam);
					            
					            
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




/**
 *
 * 보고서 확정처리
 *
 */
function fnUpdateAdm8100ReporConfirm( meaDtm, reportCd ){
	//FormData에 input값 넣기
	
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm8000/adm8100/updateAdm8100ReporConfirm.do'/>","loadingShow":true}
			,{ "meaDtm": meaDtm, "reportCd" : reportCd  });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	//등록 실패의 경우 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return;
    	}
    	
    	var pars = mySearch.getParam();
	    var ajaxParam = $('form#searchFrm').serialize();

	    if(!gfnIsNull(pars)){
	    	ajaxParam += "&"+pars;
	    }
		//그리드 새로고침
    	fnInGridListSet( firstGrid.page.currentPage,ajaxParam);
		
		jAlert(data.message, '알림창', function( result ) {
			if( result ){
	        	gfnLayerPopupClose();
			}
		});
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push("ERROR STATUS("+data.status+")<br>"+data.statusText);
    	gfnLayerPopupClose();
	});
	//AJAX 전송
	ajaxObj.send();
}

</script>


<div class="main_contents" style="height: auto;">
	<form:form commandName="amd8000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
	</form:form>
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
		<input type="hidden" name="strInSql" id="strInSql" />
		<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
		<br />
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>	
	</div>
</div>
		
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />