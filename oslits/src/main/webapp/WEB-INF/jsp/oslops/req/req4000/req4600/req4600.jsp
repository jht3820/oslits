<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<link rel="stylesheet" href="/css/chart/gantt/jsgantt.css" type="text/css">
<script src="/js/chart/gantt/jsgantt.js" type="text/javascript" charset="utf-8"></script>

<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<style>
.menu_row {float: left;width: 50%;}
.menu_row .menu_col1 {float: left;clear: both;padding-left: 25px;width: 27%;height: 40px;line-height: 30px;background: #f9f9f9;border-width: 0 1px 1px 0;border-style: solid;border-color: #ddd;}
.menu_row .menu_col2{float: left;padding: 5px;width: 73%;height: 40px;line-height: 0px;border-bottom: 1px solid #ddd;}

</style>
<script type="text/javascript">
	
	var ganttChart;
	var firstGrid;
	var mySearch;
	var selectObject =  [];
	
	/*var styleList = [ 'gtaskred' 
					,'gtaskyellow','gtaskgreen','gtaskblue' ,'gtaskpurple', 'gtaskpink' ]; */
	var styleList = [ 'gtaskgreen' 
						,'gtaskgreen','gtaskgreen','gtaskgreen' ,'gtaskgreen', 'gtaskgreen' ];
	
	$(document).ready(function(){

		//fnAxGrid5View();	
		//fnInWbsListSet();
		fnSearchBoxControl();
		

		
		var endDate = gfnGetServerTime('yyyy-mm-dd');
		var nowdate = new Date(endDate);
		var startDate =gfnGetMonthAgo(nowdate,1,'yyyy-mm-dd');
		
		fnInWbsListSet(startDate,endDate);
		 
	
	});
	
	
	function fnInWbsListSet(startDt,endDt){
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4600/selectReq4600ListAjaxView.do'/>", "loadingShow":true, "async":true}
				,{ "prjId" : '${prjId}',"startDt": startDt , "endDt" : endDt});
		//AJAX 전송 성공 함수
		
		$('#GanttChartDIV').html("");
		
		ganttChart = new JSGantt.GanttChart(document.getElementById('GanttChartDIV'), 'day');
		 
		ganttChart.setCaptionType('Complete');  // Set to Show Caption (None,Caption,Resource,Duration,Complete)
		
		ganttChart.setWeekMinorDateDisplayFormat('mon/dd'); // Set format to display dates in the "Minor" header of the "Week" view
		ganttChart.setShowTaskInfoLink(1); // Show link in tool tip (0/1)
		ganttChart.setShowEndWeekDate(0); // Show/Hide the date for the last day of the week in header for daily view (1/0)
		ganttChart.setUseSingleCell(10000); // Set the threshold at which we will only use one cell per table row (0 disables).  Helps with rendering performance for large charts.
		ganttChart.setFormatArr('Day', 'Week', 'Month', ''); // Even with setUseSingleCell using Hour format on such a large chart can cause issues in some browsers

		selectObject=[];
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			var list = data.list;
			selectObject = list;	
			
			
			for(var i= 0; i<list.length; i++){
				var reqNm = gfnCheckStrLength(gfnReplace(list[i].name, null, ""), 17);
				var startDt = list[i].start;
				
				ganttChart.AddTaskItem( new JSGantt.TaskItem(  list[i].id // pID
				 											  , reqNm // pName
				                                              , list[i].start // pStart
				                                              , list[i].end // pEnd
				                                              , styleList[ ( i % styleList.length ) ]  // pStyle
				                                              ,  ''  // pLink (unused)  pMile
				                                              ,  ''  //  pRes
				                                              ,   list[i].usrNm   //  pComp
				                                              ,   list[i].progress  //  pGroup
				                                              ,   list[i].dependencies  //  pParent
				                                              ,   1  //  pOpen
				                                              ,   0  //  pDepend
				                                              ,   0  //   pCaption
				                                              ,  ''  //   pNotes
				                                              ,  list[i].reqDesc  //  pNotes
				                                             
				                                         	  ,  ganttChart )); // pGantt
			}
			
			ganttChart.Draw(); 
			
			$('[name="progress"]').blur(function(){
				var val = fnValidPercentage($(this).val() );
				$(this).val(val);
			});
			
			$("[id^=GanttChartDIVbardiv_]").click(function(e){
				var divId = $(this).attr("id");
				var key = "GanttChartDIVbardiv_";
		
				var sInx = key.length;		
				var reqId = divId.substring(sInx); 
				
				/*
            	 * reqPageType 추가
            	 * 요구사항 상세보기(req1002.jsp)에서 항목명 구분을 위해 사용
            	 * usrReqPage - 요구사항 요청(사용자) 
            	 * admReqPage - 전체 요구사항 목록, 요구사항 생성관리(관리자)
            	 */
				var data = {
					"mode": "req", 
					"reqId": reqId,
					"reqPageType": "admReqPage"
				}; 
				
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');

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

	

	
	
	function fnSaveProgress(){
		
		var progInputList = $('[name="progress"]');
				
		var param = "";
		for(var i = 0; i< selectObject.length; i++){
			var compId = selectObject[i].id;
			if($('#comp_'+compId).val() != selectObject[i].progress){
				param +="&reqId="+selectObject[i].reqId;
				param +="&progress="+$('#comp_'+compId).val();
				param +="&prjId="+selectObject[i].prjId;
			}
		}
		
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4600/updateReq4600ProgresInfo.do'/>", "loadingShow":true, "async":true}
				, param );
		
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	    	//로딩바 숨김
	    	
	    	//코멘트 등록 실패의 경우 리턴
	    	if(data.saveYN == 'N'){
	    		toast.push(data.message);
	    		return;
	    	}
	    	var startDate = axdom("#" + mySearch.getItemId("srchFromDt")).val();
			var endDate = axdom("#" + mySearch.getItemId("srchToDt")).val();
			
			fnInWbsListSet(startDate,endDate);  
			jAlert(data.message, '알림창');
			
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			toast.push(xhr.status+"("+err+")"+" 에러가 발생했습니다.");
	    	
		});
		//AJAX 전송
		ajaxObj.send();
	}
	
	function fnValidPercentage(val ){
		var ret ;
		ret = val;
		if(isNaN(val)){
			alert("숫자를 입력하세요");
        	ret = 0;
        }else{
        	if(val<0 || 100 < val){
        		alert("0~100사이 숫자를 입력하세요");
        		ret = 0;
        	}
        
        }                    	
        return ret;
	}

	function fnSearchBoxControl(){
		var pageID = "AXSearch";
		mySearch = new AXSearch();
		var endDate = gfnGetServerTime('yyyy-mm-dd');
		var nowdate = new Date(endDate);
		var startDate =gfnGetMonthAgo(nowdate,1,'yyyy-mm-dd');
		
		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				mySearch.setConfig({
					targetID:"AXSearchTarget",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
							
							{label:"<i class='fa fa-search'></i>&nbsp;기간", labelWidth:"70", type:"inputText", width:"90", key:"srchFromDt", addClass:"secondItem readonly", valueBoxStyle:"", value:startDate,
								onChange: function(){}
								},
							{label:"", labelWidth:"", type:"inputText", width:"90", key:"srchToDt", addClass:"secondItem readonly", valueBoxStyle:"padding-left:0px;", value:endDate,
									AXBind:{
										type:"twinDate", config:{
											align:"right", valign:"top", startTargetID:"srchFromDt"
										}
									}
								},
							
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_update_rate",style:"float:right;",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>저장</span>",
	       						onclick:function(){
	       							fnSaveProgress();
	       						}},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_login",style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){					
								/* 검색 조건 설정 후 reload */
	 							var startDate = axdom("#" + mySearch.getItemId("srchFromDt")).val();
	 							var endDate = axdom("#" + mySearch.getItemId("srchToDt")).val();
							    
	 							if( gfnTermValid(axdom("#"+ mySearch.getItemId("srchFromDt")),axdom("#"+ mySearch.getItemId("srchToDt")),365) ){
	 								fnInWbsListSet(startDate,endDate);   
								}else{
								   alert('조회기간은 365일 이내로 조회 가능합니다.');
								}
	 							
	 							
	 							
							    

							}}
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			fnObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
		
			//버튼 권한 확인
			fnBtnAuthCheck(mySearch);

		});
		
	}
</script>
<div class="main_contents" style="height: auto;" >
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
		<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>

		<div class = "menu_wrap" style="overflow-y:scroll; max-width: 1500px;height: 600px;" >
			<div style="position:relative" class="gantt" id="GanttChartDIV" >
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />