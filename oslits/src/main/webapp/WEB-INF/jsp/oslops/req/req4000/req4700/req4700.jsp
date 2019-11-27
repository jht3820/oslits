<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<link rel="stylesheet" href="/css/chart/gantt/dhtmlxgantt.css" type="text/css">
<script src="/js/chart/gantt/dhtmlxgantt.js" type="text/javascript" charset="utf-8"></script>
<script src="/js/chart/gantt/dhtmlxgantt_fullscreen.js" type="text/javascript" charset="utf-8"></script> 
<script src="/js/chart/gantt/dhtmlxgantt_marker.js" type="text/javascript" charset="utf-8"></script>
<script src="/js/chart/gantt/locale_kr.js" type="text/javascript" charset="utf-8"></script>
<script src="//export.dhtmlx.com/gantt/api.js" type="text/javascript" charset="utf-8"></script>

<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<style>
	@keyframes blink { 0% {color: red;} 50% {color: white;} }
	#gantt_controls { padding:7px; font-size:12px; border-top: 1px solid #bbbbbb;height: 30px; border-left: 1px solid #bbbbbb; border-right: 1px solid #BBBBBB; background: linear-gradient(to bottom, #fff, #d4d3d3); }
	#srchGantt , #ganttPrint {height:18px; margin-top:-1px; margin-right:10px; float:left;}
	#srchGantt , #srchGantt option { font-size:12px !important; }
	
	#fullScreen {cursor: pointer; }
		
	.Proj{ border:2px solid #34c461; color:#34c461; background: #34c461; }
    .Proj .gantt_task_progress{ background: #23964d; }
    
    .high{ border:2px solid #d96c49; color: #d96c49; background: #d96c49; }
    .high .gantt_task_progress{ background: #db2536; }
    
    .low{ border:2px solid #6ba8e3; color:#6ba8e3; background: #6ba8e3; }
    .low .gantt_task_progress{ background: #547dab; }
    
    .danger{ border:2px solid #f1ca72; color:#6ba8e3; background: #f1ca72; }
    .danger .gantt_task_progress{ background: #f1ca72; }
    
    .overPeriod { border:2px solid red; color:black; background: red; }
    .overPeriod .gantt_task_progress{ background: red; }
    .overPeriod .gantt_task_content { color:white; font-weight : bold;}
    
   	.gantt_task_cell.week_end{ background-color: #EFF5FD; }
	.gantt_task_row.gantt_selected .gantt_task_cell.week_end{ background-color: #F8EC9C; }
    
   	.gantt_task_cell.week_end{ background-color: #EFF5FD; }
	.gantt_task_row.gantt_selected .gantt_task_cell.week_end{ background-color: #F8EC9C; }
    
	.req_right_box{ border-radius: 5px; }
	.req_left_table_wrap{ width:73%; }
	img.ui-datepicker-trigger {width: 20px; }
	
	.menu_tree_help { 
		box-shadow: 1px 1px #c3c3c3; border: 1px solid #aaa; padding: 0 4px 0 3px; font-size: 11pt;
	}
	.menu_tree_helpBox { 
		width:600px; height: 240px; z-index: 1; top:21px; left:-19px; 
	}
	.sub_title { font-weight: bold; }
	
	.menu_expand_all , .menu_collapse_all { box-shadow : 1px 1px #aaa; }
	#expand:active { box-shadow: none; }
	#collapse:active { box-shadow:none; }
	#gantt_here { z-index: 9004;}
	.downloadGantt{cursor: pointer;}
	
	.task_warning {
	 	animation: blink 1s linear  infinite;
	 	-webkit-animation: blink 1s step-end infinite;
	}
</style>
<script type="text/javascript">
	var tasks = {
		 data:[]  //data 
		,links:[]  //간선링크
		,etc: {
			reqOrders : ""
		}  //간선링크
	};
	var verifyParent = "";
	var evt;
	
	$(function(){		
		// 일/주/월/년 별 조회
		$("input[name='scale']").on('click',function(e){ 
			setScaleConfig(e.currentTarget.value);
			gantt.render();
		});
		gantt.attachEvent("onTaskOpened", function(id) {
		   gantt.render();
		});
		// 요구사항 작업상태 조회
		$("input[name='state']").on('click',function(e){
			
		
			
			evt = e;
			verifyParent = "";
			//wbs Filtering Logic
		 	gantt.attachEvent("onBeforeTaskDisplay", function(id, task){
		 		var d = new Date(task.eDt);
				d = new Date(d.getFullYear(), d.getMonth(), d.getDate(), 23,59,0);
				var digitDt = fnDiffDays(new Date(), d);
		 		
		 		if(task.id=="1") return false;
		 		if(task.id != "1" && task.parent === verifyParent) {
		 			return true;
		 		}
	        	switch (evt.currentTarget.value){
		        	case "0": return true; break;
		        	case "1":  
		        		if (!( task.id.indexOf("_1") != '-1' || task.id.indexOf("_2") != '-1' ) && task.taskProg.indexOf("최종완료") != -1  ){
		        			verifyParent = task.id;
		        			return true;
		        		}
		        	break;
		        	case "2": 
		        		if ( !(task.id.indexOf("_1") != '-1' || task.id.indexOf("_2") != '-1'  ) && !(task.taskProg.indexOf("최종완료") != -1) || task.parent === verifyParent ){
		        			verifyParent = task.id;
		        			return true;
		        		}
	        		break;
		        	case "3":  //미완료
	        			if (  !( task.id.indexOf("_1") != '-1' || task.id.indexOf("_2") != '-1'  ) && !(task.taskProg.indexOf("최종완료") != -1) && digitDt < 0 || task.parent === verifyParent ){
	        				verifyParent = task.id;
	        				return true;
	        			}
		        	break;
		        	case "4": //긴급
		        		if ( !( task.id.indexOf("_1") != '-1' || task.id.indexOf("_2") != '-1'  ) && !(task.taskProg.indexOf("최종완료") != -1) && ( digitDt <= 3 && digitDt >= 0 ) || task.parent === verifyParent ){
		        			verifyParent = task.id;
		        			return true;
		        		}
		        		break;
	        	}
		    });
			gantt.refreshData(); //gantt chart reload
		});
		// 전체 열기 / 닫기
		$("#expand , #collapse").on('click',function(e){ 
			if( e.currentTarget.id == 'expand' ){
				gantt.eachTask(function(task){
					task.$open = true;
				});
			} else { 
				gantt.eachTask(function(task){
					task.$open = false;
				});
			}
			gantt.render();
			gantt.render();
		});
		// wbs Filtering View
		$("#srchContents").append($('<input>', {type:"text", id:"reqNm",  style:"width:100px; margin-right:10px;" } ));
		$("#srchGantt").on('change',function(e){
			$("#srchContents  > *").remove();
			if ( e.currentTarget.value == "name" ) {
				$("#srchContents").append($('<input>', {type:"text", id:"reqNm",  style:"width:100px; margin-right:10px;"} ));
			} else if ( e.currentTarget.value == "period" ) {
				$("#srchContents").append($('<input>', {type:"text", id:"reqStDtm", disabled:true, style:"width:100px;"} ));
				$("#srchContents").append($('<span>', {text: ' ~ '}));
				$("#srchContents").append($('<input>', {type:"text", id:"reqEdDtm" , disabled:true, style:"width:100px; " } ));
				gfnCalRangeSet("reqStDtm", "reqEdDtm", '', '' );
			}	
		});
		
		//조회버튼
		$(".srchBtn").on('click', function(e) { 
			//wbs Filtering Logic
		 	gantt.attachEvent("onBeforeTaskDisplay", function(id, task){
	        	//task id가 1이면 프로젝트 기간
		        if($("#srchGantt option:selected").val() == "name"){
		        	if(task.id=="1") return false;
		        	if( task.parent == "" && task.id != "1" && task.text.indexOf( $.trim($("#reqNm").val()) ) ){
		        		verifyParent = task.id;
		        	}
					if( (task.text.indexOf($.trim($("#reqNm").val())) != '-1') || task.parent == verifyParent || task.id == "1" ){
						return true;
					} else { 
						return false;
					}
		        } else if( $("#srchGantt option:selected").val() == "period" ){
		        	var st = $("#reqStDtm").val() == ""?"":new Date($("#reqStDtm").val()); 
		        	var ed = $("#reqEdDtm").val() == ""?"":new Date($("#reqEdDtm").val());
		        	if(task.id=="1") return false;
		        	if( $("#reqStDtm").val() != "" && $("#reqEdDtm").val() == "" ) {
		        		if( (st.format('yyyy-MM-dd') <= task.start_date.format('yyyy-MM-dd') ) || task.parent == verifyParent ){
		        			if ( !( task.id.indexOf("_1") != '-1' || task.id.indexOf("_2") != '-1'  ) ) verifyParent = task.id;
		        			return true;
		        		} else
		        			return false;
		        	} else if( $("#reqStDtm").val() == "" || $("#reqEdDtm").val() != "" ) {
		        		if( (task.end_date.format('yyyy-MM-dd') <= ed.format('yyyy-MM-dd') ) || task.parent == verifyParent ){
		        			if ( !( task.id.indexOf("_1") != '-1' || task.id.indexOf("_2") != '-1'  ) ) verifyParent = task.id;
		        			if ( task.$level == "2" && !(gantt.getTask(task.parent).end_date.format('yyyy-MM-dd') <= ed.format('yyyy-MM-dd') ))   return false;
		        			return true;
		        		} else
		        			return false;
		        	} else if( $("#reqStDtm").val() != "" || $("#reqEdDtm").val() != "" ) {
		        		if(  (new Date(st).format('yyyy-MM-dd') <= new Date(task.start_date)).format('yyyy-MM-dd') || (new Date(task.end_date).format('yyyy-MM-dd') <= new Date(ed).format('yyyy-MM-dd') ) || task.parent == verifyParent){
		        			if ( !(task.id.indexOf("_1") != '-1' || task.id.indexOf("_2") != '-1'  ) ) verifyParent = task.id;
		        			return true;
		        		} else
		        			return false;
		        	}
		        	
		        }
		    });
			gantt.refreshData(); //gantt chart reload
		})

		//전체 화면
		$("#fullScreen").on('click',function(e){ 
		    if (!gantt.getState().fullscreen) {
	            gantt.expand();
	        }
	        else {
	            gantt.collapse();
	        }
		});
		
		$("#reqNm").on('keydown',function(e){ 
			if ( e.which == 13 ) {
				$(".srchBtn").trigger('click');
			}
		})
		
		//요구사항 상세조회
		gantt.attachEvent("onTaskDblClick", function(id,e){
			if(id == '1' ) return false;
			var reqId = id.replace(/_2/gi, '').replace(/_1/gi, ''); 
			gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", {"mode":"req","reqId":reqId }, '1300', '887','scroll');
		    return false;
		});
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4700/selectReq4700ListAjaxView.do'/>", "loadingShow":false, "async":true}
				,{ "prjId" : '${prjId}'});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			var data = JSON.parse(data);
			$(data.listAdm).each(function(i,v){
				gantt.setWorkTime({
					date:new Date(v.holiDate),
					hours:false
				});
			})
			var pObj ="";
			$(data.list).each(function(i,v){
				if(v.reqStDtm != null && v.reqEdDtm != null) {
					if(v.type == '개발계획공수') {
					var d = new Date(v.reqStDtm);
					d = new Date(d.getFullYear(), d.getMonth(), d.getDate() + parseInt(v.reqDevWkTm/8), 23,59,0).format('yyyy-MM-dd')
						tasks.data.push( {
							 "id": v.reqId
							,"text": v.type
							,"parent" : v.upperReqId
							,"start_date": new Date(pObjStDt).format("dd-MM-yyyy")
							,"eDt":v.reqDevWkTm == 0 ? new Date(pObjStDt).format("yyyy-MM-dd"):d 
							,"open":true
							,"duration":v.reqDevWkTm == 0 ? -1 : v.reqDevWkTm/8
							,'priority':'2'
							,'taskProg' : ''
							,'taskRate' : '' 
							,'taskState' : ''
							});
					} else if(v.type == '실제소요공수') {
						var devTime;
						var timeDisplay;
						var d = new Date(v.reqStDtm);
						d = new Date(d.getFullYear(), d.getMonth(), d.getDate() + parseInt(v.realDevTime/8), 23,59,0).format('yyyy-MM-dd')
						if ( pObj.upperCnt > 0 ){ devTime = v.realDevTime == 0 ? -1 : parseInt(v.realDevTime /8) + (v.realDevTime%8)/10
						} else { devTime = v.realDevTime == 0 ? -1 : (v.realDevTime/8) }
						tasks.data.push({
							 "id": v.reqId
							,"text": v.type
							,"start_date": new Date(pObjStDt).format("dd-MM-yyyy")
							,"eDt":(devTime == '-1')?new Date(pObjStDt).format("yyyy-MM-dd"):d.format("yyyy-MM-dd")
							,"duration": devTime
							,"parent" : v.upperReqId
							,"open":true
							,'priority':'3'
							,'taskProg' : ''
							,'taskRate' : ''  
							,'taskState' : ''
						});
						pObj = "";
					} else {
						pObjStDt = new Date(v.reqStDtm); //실제소요공수에서 사용하기 위해 부모 데이터를 임시저장
						var d = new Date(v.reqEdDtm);
						d = new Date(d.getFullYear(), d.getMonth(), d.getDate(), 23,59,0)
						if(v.upperCnt > 0) pObj = v; 
						var taskRate = Math.round(v.flowNm==="최종완료"?100:(parseFloat(v.realDevTime) / v.reqDevWkTm) * 100);
						tasks.data.push({
								 "id": v.reqId
								,"text":  (v.upperCnt > 0 ? "<span style='color:blue'> (* 그룹 ) </span>": "") + v.reqNm
								,"parent" : ''
								,"eDt":d
								,"end_date":d
								,"start_date": new Date(v.reqStDtm).format("dd-MM-yyyy")
								,"priority":'1'
								,"open":false
								,'taskProg' : (v.upperCnt > 0 && v.flowNm != "최종완료" ?"진행중":v.flowNm) 
								,'taskRate' : isNaN(taskRate)?"0":(taskRate > 100 ?'<span style="color:red">'+taskRate+'%</span>':'<span>'+taskRate+'%</span>')  
								,'taskState' : fnGetTaskProgStateTag(v, d)
						});
						tasks.links.push({
							 "id": v.reqId
							,"source":v.reqId
							,"target":v.upperLinkId != null ?v.upperLinkId:""
							,"type":"1"
						});
					}
				} else {
					if(v.type == '개발계획공수') {
						} else if(v.type == '실제소요공수') {
						} else {
							if(v.upperCnt > 0) pObj = v; 
							var taskRate = ( v.realDevTime + v.reqDevWkTm ) == 0?"":Math.round(v.flowNm==="최종완료"?100:(parseFloat(v.realDevTime) / v.reqDevWkTm) * 100);
							tasks.data.push({
									 "id": v.reqId
									,"text":  (v.upperCnt > 0 ? "<span style='color:blue'> (* 그룹 ) </span>": "") + v.reqNm,"text": "<span style='color:red; font-weiht:bold;'> ※ </span>" + v.reqNm + (v.upperCnt > 0 ? "<span style='color:blue'> (* 그룹 )</span>": "")
									,"parent" : ''
									,"eDt": ''
									,"end_date":''
									,"start_date": new Date(v.regDtm)
									,'duration':-1
									,'taskProg' :  (v.upperCnt > 0 && v.flowNm != "최종완료" ? "진행중" :(v.flowNm == null)?" - ":v.flowNm ) 
									,'taskRate' : (v.upperCnt > 0 )?(v.flowNm != "최종완료"?"":"100%"):taskRate > 100 ?'<span style="color:red">'+taskRate+'%</span>':'<span>'+(isNaN(taskRate)?"0":taskRate)+'%</span>'  
									,'taskState' : "<span title='"+ v.flowNm +"' style='color:"+(v.flowClsCd==04?"#34c461":v.flowClsCd==01?"#3db9d3":"rgb(247, 227, 167)")+";padding: 0 5px;'>●</span>"
							});
							tasks.links.push({
								 "id": v.reqId
								,"source":v.reqId
								,"target":v.upperLinkId != null ?v.upperLinkId:""
								,"type":"1"
							});
						}
				}
			});
		}); 
		ajaxObj.setFnComplete(function(data){
			//gantt chart configuration
			ganttChartOptions({
				showMarkerToday : true //오늘 날짜 마커를 보여줄지 설정 
				,showMarkerPeriod : true // 프로젝트 시작일 마커를 보여줄지 설정
				,setReadonly : true //간트챠트를 읽기 전용으로 할지 설정
			});
			ganttChartConfig();
		
			gantt.parse(tasks);
			tasks.etc.reqOrders = gantt.$data.tasksStore.visibleOrder; // 
		});
		//AJAX 전송
		ajaxObj.send();
		
		gantt.init( "gantt_here" );
		setScaleConfig('1');
	})
	
	//간트 차트 Grid 속성 설정
	gantt.config.grid_width = 520;
	gantt.config.columns = [
           {name:"text",       	label:"요구사항 명"	, width:'210' , tree: true},
           {name:"start_date", 	label:"시작일자" 	, width:'80'},
           {name:"eDt", 		label:"종료일자" 	, width:'80'},
           {name:"taskProg", 	label:"작업" 		, width:'60'},
           {name:"taskRate", 	label:"진행율" 	, width:'50'},
           {name:"taskState", 	label:"상태"		},
  	];
	gantt.attachEvent("onTaskLoading", function(task){
		task.eDate = task.end_date==undefined?task.start_date:new Date(new Date(task.end_date).setDate(new Date(task.end_date).getDate()-1))
	    return true;
	});
	
	/**
	 * gantt 차트 등급 설정
	 */
	function setScaleConfig(value){
		switch (value) {
		case "1":
			gantt.config.scale_unit = "day";
			gantt.config.step = 1;
			gantt.config.date_scale = "%M %d일" ;
			gantt.config.subscales = [
   				{unit:"day", step:1, date:"%D" }
   			];
			gantt.config.scale_height = 27;
			gantt.templates.date_scale = null;
			break;
		case "2":
			var weekScaleTemplate = function(date){
				var dateToStr = gantt.date.date_to_str("%M %d일");
				var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
				return dateToStr(date) + " - " + dateToStr(endDate);
			};

			gantt.config.scale_unit = "week";
			gantt.config.step = 1;
			gantt.templates.date_scale = weekScaleTemplate;
			gantt.config.scale_height = 50;
			break;
		case "3":
			gantt.config.scale_unit = "month"; //달 기준
			gantt.config.date_scale = "%Y %F"; // 중제목
			gantt.config.subscales = [
				{unit:"week", step:1, date:"%j" }
			];
			gantt.config.scale_height = 50;
			gantt.templates.date_scale = null;
			break;
		case "4":
			gantt.config.scale_unit = "year";
			gantt.config.step = 1;
			gantt.config.date_scale = "%Y년";
			gantt.config.min_column_width = 50;

			gantt.config.scale_height = 70;
			gantt.templates.date_scale = null;

			gantt.config.subscales = [
				{unit:"month", step:1, date:"%M월" }
			];
			break;
		}
	}
	
	/**
	 * gantt 차트 기본 설정
	 */
	function ganttChartConfig(){
		gantt.config.row_height = 25; //프로그레스바 크기
		gantt.config.min_column_width = 60; //최소 속성 크기
		gantt.config.work_time = true; //작업시간 표시
		gantt.templates.task_cell_class = function(task, date){
			if(!gantt.isWorkTime({date:date, task:task}))
				return "week_end";
			return "";
		};
		gantt.setWorkTime({hours : [8, 12, 13, 18]});
		
		gantt.config.scale_unit = "day";
		gantt.config.step = 1;
		gantt.config.date_scale = "%M %d일" ;
		gantt.config.subscales = [
				{unit:"day", step:1, date:"%D" }
			];
		gantt.config.scale_height = 27;
		gantt.templates.date_scale = null;

	}
	/**
	 * gantt 차트  옵션 설정
	 */
	function ganttChartOptions(options) {
		//오늘 날짜 마커를 보여줄지 설정 
		if(options.showMarkerToday){ 
			var date_to_str = gantt.date.date_to_str(gantt.config.task_date);
			gantt.addMarker({
				id:"today",
				start_date: new Date(),
				css: "today",
				text: "오늘",
				title:"오늘: "+ date_to_str( new Date() )
			});
		} 
		prjDt = '${currPrjInfo.startDt}'.split('-');	
		//오늘 날짜 마커를 보여줄지 설정 
		if(options.showMarkerPeriod){ 
			var date_to_str = gantt.date.date_to_str(gantt.config.task_date);
		} 
		
		//트리메뉴 도움말 클릭
		$(".menu_tree_help").click(function(){
			if($(".menu_tree_helpBox").hasClass("boxOn")){
				$(".menu_tree_helpBox").hide();
				$(".menu_tree_helpBox").removeClass("boxOn");
			}else{
				$(".menu_tree_helpBox").show();
				$(".menu_tree_helpBox").addClass("boxOn");
			}
		});
		
		
	 	if(options.setReadonly){
			gantt.config.readonly = true;
		}
	   	
	    gantt.templates.task_class  = function(start, end, task){
 	    	if( task.$level == 1 ) {
	    		var d = new Date(end);
				d = new Date(d.getFullYear(), d.getMonth(), d.getDate()-1, 23,59,0).format('yyyy-MM-dd')
	    		task.eDt = task.duration==0 ?task.start_date.format('yyyy-MM-dd'):d;
	    		
	    	}
 	    	
 	    	if(task.$level >= 1){
 	    		switch (task.priority){
					case "1": return "Proj"; break;
					case "2": return "high"; break;
					case "3": return "low"; break;
				}
 	    	} 
		};
		gantt.templates.grid_folder = function(item) {
		    return "<div class='gantt_tree_icon gantt_folder_" +
		    (item.$open ? "open" : "closed") + "'></div>";
		};
	}
	
	/* 일수 계산  */
	function fnDiffDays(d1, d2)
	{
	  var ndays;
	  var tv1 = d1.valueOf();  // msec since 1970
	  var tv2 = d2.valueOf();
	
	  ndays = (tv2 - tv1) / 1000 / 86400;
	  ndays = Math.round(ndays - 0.5);
	  return ndays;
	}
	
	function fnGetTaskProgStateTag(t, d){
		var digitDt = fnDiffDays(new Date(), d)
		if ( t.flowNm == "최종완료" ) {
    		return "<span title='"+ t.flowNm +"' style='color:#34c461;padding: 0 5px;'>●</span>";
    	} else if( 0 <= digitDt && digitDt <= 3){
    		return "<span title='"+ t.flowNm +"' style='color:#ffbc24; padding: 0 7px; font-size: 2em; font-weight: bold;'>!</span>";	
    	} else if ( digitDt < 0 ){
    		return "<span title='"+ t.flowNm +"' class='task_warning' style='color:red; padding: 0 7px; font-size: 2em; font-weight: bold;'>!</span>";
    	} else { 
    		return "<span title='"+ t.flowNm +"' style='color:"+(t.flowClsCd==01?"#3db9d3":"rgb(247, 227, 167)")+";padding: 0 5px;'>●</span>";	
    	}
	}
	
</script>
<div class="main_contents" style="height: auto; ">
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu" style="max-width:1600px">
		<div style="font-family: monospace; margin-bottom:3px;"><span style='color:red;'>※  </span>작업 기간(시작/종료)이 존재하지 않는 요구사항</div>
		<!-- WBS 내용 -->
		<div id="gantt_controls">
			<span class="menu_tree_help"><i class="fa fa-question"></i>
				<div class="menu_tree_helpBox">
					<span>
						[WBS 메뉴 기능 안내]<br/>
						<br/>
							
							&nbsp;-요구사항 클릭: 요구사항 진행도 보기<br/>
							&nbsp;-요구사항 더블 클릭: 요구사항 상세정보 보기<br/>
						
							<br/>
						
							&nbsp;- 기간 별 조회: <br/>
							&nbsp;<span style="margin-left: 51px;"></span>- 일(Day), 주(Week), 월(Month), 년(Year) 별 조회 <br/>
							
							<br/>
							
							&nbsp;- 요구사항 상태 조회: <br/>
							&nbsp;<span style="margin-left: 51px;"></span>- 최종완료 (<span style='color:#34c461;padding: 0 5px;'>●</span>) : 요구사항 작업 기간 내 최종완료된 목록<br/>
							&nbsp;<span style="margin-left: 51px;"></span>- 할일  (<span style='color:#3db9d3;padding: 0 5px;'>●</span>)  : 개발이 진행중인상태가 아닌 요구사항 [할일 단계]<br/>
							&nbsp;<span style="margin-left: 51px;"></span>- 진행중  (<span style='color:rgb(247, 227, 167);padding: 0 5px;'>●</span>)  : 현재 진행중인 요구사항 [진행중, 진행완료, 검수중 단계]<br/>
							&nbsp;<span style="margin-left: 51px;"></span>- 미완료  (<span style='color:red; padding: 0 7px; font-size: 1.4em; font-weight: bold;'>!</span>): 요구사항 작업 기간 내 미완료된 목록 (점멸)<br/>
							&nbsp;<span style="margin-left: 51px;"></span>- 긴급  (<span style='color:#ffbc24; padding: 0 7px; font-size: 1.4em; font-weight: bold;'>!</span>) : 작업종료기간(3일)이 임박한 요구사항 <br/>
					</span>
				</div>
			</span>
			<div class="menu_ctrl_btn_wrap" style="display: inline; float: left; line-height: 0px; border: none; height: 1px;">
				<div class="menu_all_wrap">
					<span class="menu_expand_all" id="expand" title="전체 열기"></span>
					<span class="menu_collapse_all" id="collapse" title="전체 닫기"></span>
				</div>
			</div>
			
			<span style="margin: 0 10px; border : 1px solid #969292a1; float:left; height:15px; "></span>
			<div style="line-height:15px; float:left;">
				<span class="sub_title"> 기 간 : </span>
				<input type="radio" id="scale1" name="scale" value="1" checked/><label for="scale1">일 별</label>
				<input type="radio" id="scale2" name="scale" value="2"  /><label for="scale2">주 별</label>
				<input type="radio" id="scale3" name="scale" value="3" /><label for="scale3">월 별</label>
				<input type="radio" id="scale4" name="scale" value="4" /><label for="scale4">연 별</label>
			</div>
			<span style="margin: 0 10px; border : 1px solid #969292a1; float:left; height:15px; "></span>
			<div style="line-height:15px; float:left;">
				<span  class="sub_title"> 요구 사항 : </span>
				<input type="radio" id="state0" name="state" value="0" checked/><label for="state0">전체 </label>
				<input type="radio" id="state1" name="state" value="1" /><label for="state1">완료 </label>
				<input type="radio" id="state2" name="state" value="2"  /><label for="state2">진행중</label>
				<input type="radio" id="state3" name="state" value="3" /><label for="state3">미 완료</label>
				<input type="radio" id="state4" name="state" value="4" /><label for="state4">긴 급</label>
			</div>
			<span style="margin: 0 10px; border : 1px solid #969292a1; float:left; height:15px; "></span>
			<div style="float:left;">
				<select id="srchGantt">
					<option class="opt" value="name" >요구사항</option>
					<option class="opt" value="period" > 기간 </option>
				</select>	
				<span id="srchContents" style="float:left;"></span>
				<div style="float:left;  margin : -2px 10px 0 10px; ">
					<div class="button_normal srchBtn"  style="background-color:#e5e9ec; font-size:1.1em;">조회</div>
				</div>
			</div>

			<div style="float:right; margin:0px 5px; ">
				<div id="fullScreen"><img title="전체보기" src="/images/contents/if_fullscreen_118670.png" width='19px' alt="전체화면"/></div>
			</div>
			<div style="float:right; ">
				<img class="downloadGantt" title="pdf 다운로드" src="/images/ext/pdf.png" width='19px' alt="png" onclick="gantt.exportToPDF()"/>
				<img class="downloadGantt" title="png 다운로드" src="/images/ext/png.png" width='19px' alt="png" onclick="gantt.exportToPNG()"/>
			</div>
		</div>
		<div id="gantt_here" style='width:100%; height:650px;'></div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />