<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp" %>
<script type="text/javascript" src="<c:url value='/js/chart/chartJs/Chart.bundle.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/css/oslits/dsh.css'/>" type='text/css'>
<style>
header{display:none;}
</style>
<script>
//요구사항 선택 flag
var clickFlag = 0;

//그리드 객체 가지고있는 변수
var gridObj = {};

//자동 갱신 selected 처리
var timerVarSel;
var secondTime;
var timer;

//사용자 Id
var usrId = "${sessionScope.loginVO.usrId}";

//프로젝트 목록
var prjList = ${prjList};

var dsh3000_selPrjId = null;

//차트
var chart1,chart2,chart3,dsh3000_ctx1,dsh3000_ctx2,dsh3000_ctx3;

//자동 갱신 index
var autoSelPrjIdx = 0;

//접수 대기 그리드 설정 값
var newReqGrid_config = {
	showLineNumber: true,
	sortable:true,
	header: {align:"center"},
	columns: [
		{key: "reqDtm", label: "요청일자", width: 140, align: "center"},
		{key: "reqUsrNm", label: "요청자", width: 80, align: "center"},
		{key: "reqUsrEmail", label: "이메일", width: 140, align: "center"},
		{key: "reqUsrNum", label: "연락처", width: 100, align: "center"},
		{key: "reqNm", label: "요청 명", width: 400, align: "left"},
		{key: "reqDesc", label: "요청 내용", width: 250, align: "left"},
		{key: "reqDtm", label: "생성일자", width: 140, align: "center"},
		{key: "reqStDtm", label: "작업 시작일자", width: 140, align: "center"},
		{key: "reqEdDtm", label: "작업 종료일자", width: 140, align: "center"},
		{key: "reqStDuDtm", label: "시작 예정일자", width: 140, align: "center"},
		{key: "reqEdDuDtm", label: "종료 예정일자", width: 140, align: "center"}
		],
	body: {
		align: "center",
		columnHeight: 30,
		onDBLClick:function(){
			//요청사항 상세보기 (선택 prjId없는경우 동작 안함)
			if(!gfnIsNull(dsh3000_selPrjId)){
	          	var data = {
	          			"mode": "req",
	          			"prjId":dsh3000_selPrjId,
	          			"reqId": this.item.reqId,
	          			"reqProType": this.item.reqProType,
	          			"reqPageType" : "usrReqPage"
	          	}; 
	          	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '920','scroll');
			}
		}
	}
};

//그리드 공통 설정 값 -- (항목 값 정해지면 수정필요)
var flowGrid_config = {
	showLineNumber: true,
	sortable:true,
	header: {align:"center"},
	columns: [
	    {key: "reqDtm", label: "요청일자", width: 140, align: "center"},
		{key: "reqUsrNm", label: "요청자", width: 80, align: "center"},
		{key: "reqUsrEmail", label: "이메일", width: 140, align: "center"},
		{key: "reqUsrNum", label: "연락처", width: 100, align: "center"},
		{key: "reqChargerNm", label: "담당자", width: 80, align: "center"},
		{key: "reqNm", label: "요청 제목", width: 400, align: "left"},
		{key: "reqDesc", label: "요청 내용", width: 250, align: "left"},
		{key: "regDtm", label: "생성일자", width: 140, align: "center"},
		{key: "reqStDtm", label: "작업 시작일자", width: 140, align: "center"},
		{key: "reqEdDtm", label: "작업 종료 예정일자", width: 140, align: "center"},
		{key: "reqEdDtm", label: "작업 종료일자", width: 140, align: "center"},
	],
	body: {
		align: "center",
		columnHeight: 30,
		onDBLClick:function(){
			var data = {
					"mode":"newReq",
					"prjId":dsh3000_selPrjId,
					"reqId": this.item.reqId,
					 "reqProType":this.item.reqProType
					}; 
			gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
		}
	}
};
     
//페이지 로드시 실행
$(document).ready(function(){
	//프로젝트명 세팅
	var dashboardPrjNm = "${requestScope.prjNm}";
	$("#dashboardPrjNm").text(gfnCutStrLen(dashboardPrjNm,120));
	
	//프로젝트 세팅
	fnDsh3000PrjListSetting(prjList);
});

//차트 세팅
function fnChartSetting(data){
	//[차트1] 프로세스별 총개수 + 최종 완료 개수
	var processReqCnt = data.processReqCnt;
	
	//차트2
	var monthProcessReqCnt = data.monthProcessReqCnt;
	//차트생성이 되어있다면 차트 비우기
	if(!gfnIsNull(chart1)){
		chart1.destroy();
	}
	
	//차트1 있을때만 데이터 세팅
	if(!gfnIsNull(processReqCnt)){
		//차트1 - 데이터 세팅
		var chart_processNm = [];
		var chart_reqAllCnt = [];
		var chart_reqEndCnt = [];
		
		//차트1 데이터 분기
		$.each(processReqCnt,function(idx, map){
			//프로세스 명
			chart_processNm.push(map.processNm);
			
			//요구사항 총 개수
			chart_reqAllCnt.push(map.allCnt);
			
			//접수 대기의 경우 총개수와 최종 종료개수 맞춤
			if(map.processId == "request"){
				//요구사항 최종 종료 개수
				chart_reqEndCnt.push(map.allCnt);
			}else{
				//요구사항 최종 종료 개수
				chart_reqEndCnt.push(map.endCnt);	
			}
		});
		
		dsh3000_ctx1 = document.getElementById("reqTotalCntChart").getContext('2d');
		
		chart1 = new Chart(dsh3000_ctx1, {
		    type: 'bar',
		    data: {
		    	labels: chart_processNm,
		        datasets: [{
		        	type:'line',
		            label: '최종완료 요구사항 수',
		            data: chart_reqEndCnt,
		            backgroundColor:'rgb(255, 125, 110)',
		            borderColor: 'rgb(255, 86, 67, 1)',
		            borderWidth: 2,
		            pointStyle: 'rectRot',
		            fill: false,
		            pointRadius: 4
		        },{
		        	type:'bar',
		            label: '총 요구사항 수',
		            data: chart_reqAllCnt,
		            backgroundColor:'#4b73eb',
		            borderColor: 'rgb(75, 115, 235, 1)',
		            borderWidth: 2,
		            pointStyle: 'rect',
		        }]
		    },
		    options: {
					responsive: true,
					title: {display: true,text:'프로세스별 요구사항 수'},
					tooltips: {mode: 'index',intersect: false},
					legend: false//{labels: {usePointStyle: true}}
				}
		});
	}
	
	//데이터 없을때 차트생성이 되어있다면 차트 비우기
	if(!gfnIsNull(chart2)){
		chart2.destroy();
	}
	if(!gfnIsNull(chart3)){
		chart3.destroy();
	}
		
	//차트2 데이터 있는경우에만
	if(!gfnIsNull(monthProcessReqCnt)){
		//차트2 준비 데이터
		var chart2_x = ["01월","02월","03월","04월","05월","06월","07월","08월","09월","10월","11월","12월"];	//월별 라벨
		var chart2_y_label = [];	//프로세스 목록
		var chart2_y_proNm = {};	//프로세스 명
		var chart2_y_mmCnt = {};	//프로세스별 요구사항 개수
		var chart2_datasets = [];	//차트 데이터 세팅
		var chart2_bgColor = ["#4b73eb","#936de9","#ff5643","#58c3e5","#fba450","#eb4ba4","#89eb4b","#c4eb4b","#9f4beb","#fba450","#ff5643","#58c3e5","#fba450"];	//차트 배경색
		
		//차트3 준비 데이터
		
		var chart3_idxIf = {"01월":1,"02월":1,"03월":1,"04월":2,"05월":2,"06월":2,"07월":3,"08월":3,"09월":3,"10월":4,"11월":4,"12월":4};
		var chart3_quarter = [0,0,0,0,0];
		
		dsh3000_ctx2 = document.getElementById("reqMonthCntChart").getContext('2d');
		
		//차트2 데이터 분기
		$.each(monthProcessReqCnt,function(idx, map){
			var reqEdDtmMm = map.reqEdDuMm;
			//'월' 붙이기
			reqEdDtmMm += "월";
			
			//프로세스명 없는경우 push
			if(chart2_y_label.indexOf(map.processId) == -1){
				//프로세스명
				chart2_y_label.push(map.processId);
				
				//json 조합
				var tempJson = {};
				tempJson[map.processId] = map.processNm;
				$.extend(chart2_y_proNm,tempJson);
			}
			
			//데이터 배열 만들기
			if(Object.keys(chart2_y_mmCnt).indexOf(reqEdDtmMm)){
				chart2_y_mmCnt[reqEdDtmMm] = {};
			}
			//월별 프로세스별 요구사항 완료율 세팅
			if(Object.keys(chart2_y_mmCnt[reqEdDtmMm]).indexOf(map.processId) == -1){
				//완료율 계산
				//월별 요구사항 총 갯수
				var reqTotalCnt = map.reqTotalCnt;
				//월별 완료 갯수
				var reqEdMmCnt = map.reqEdMmCnt;
				//완료율
				var reqEdMmRatio = 0;
				
				//갯수가 0이상일 경우
				if(reqEdMmCnt > 0 && reqTotalCnt > 0){
					reqEdMmRatio = (reqEdMmCnt/reqTotalCnt)*100;
					reqEdMmRatio = reqEdMmRatio.toFixed(0);
				}
				
				chart2_y_mmCnt[reqEdDtmMm][map.processId] = reqEdMmRatio;
			}
		});
		
		//data 세팅 - 프로세스 루프
		$.each(chart2_y_label,function(idx, map){
			//데이터 배열
			var mmCntDataArr = [];
			
			//월별 루프
			$.each(chart2_x,function(inIdx, inMap){
				//월 데이터 없는경우
				if(gfnIsNull(chart2_y_mmCnt[inMap])){
					mmCntDataArr.push(0);
				}else{
					//월별 프로세스 데이터 체크
					if(gfnIsNull(chart2_y_mmCnt[inMap][map])){	//없는경우 0(배열 크기 맞추기 위함)
						mmCntDataArr.push(0);
					}else{	//있는경우 데이터 가져와서 push
						mmCntDataArr.push(chart2_y_mmCnt[inMap][map]);
					
						//차트3 - 분기별 검사하고 데이터 추가
						chart3_quarter[chart3_idxIf[inMap]] += chart2_y_mmCnt[inMap][map];
						
					}
				}
				
			});
			
			//배경색 인덱스
			var bgIdx = idx;
			
			//프로세스 수가 가진 색상을 넘어서는경우
			if(idx > chart2_bgColor.length-1){
				bgIdx = idx%chart2_bgColor.length;
			}
			
			//차트에 입력되는 데이터 세팅
			chart2_datasets.push({
				type:'bar',
	            label: chart2_y_proNm[map],
	            data: mmCntDataArr,
	            backgroundColor: chart2_bgColor[bgIdx],
	            borderWidth: 0,
	            pointStyle: 'rect',
	            fill: false,
	            pointRadius: 4
			});
		});
		
		chart2 = new Chart(dsh3000_ctx2, {
		    type: 'bar',
		    data: {
		        labels: chart2_x,
		        datasets: chart2_datasets
		    },
		    options: {
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true
						}]
					},
					title: {display: true,text:'각 프로세스별 처리율 (월)'},
					tooltips: {
						mode: 'index'
						,intersect: false
						,callbacks: {
							label:function(tooltipItems, data){
								var thisLabel = data.datasets[tooltipItems.datasetIndex].label;
								return thisLabel+": "+tooltipItems.yLabel+"%";
							}
							,
							footer: function(tooltipItems, data) {
								var sum = 0;
	
								tooltipItems.forEach(function(tooltipItem) {
										sum += data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
								});
								
								return '총 처리율: ' + sum+"%";
							},
						}
					},
					legend: {labels: {usePointStyle: true}}
				}
		});
		
		//차트2 데이터 있는 경우 월별로 분기 데이터 생성
		dsh3000_ctx3 = document.getElementById("reqQuarterCntChart").getContext('2d');
		
		chart3 = new Chart(dsh3000_ctx3, {
		    type: 'bar',
		    data: {
		        labels: ["미지정","1분기","2분기","3분기","4분기"],
		        datasets: [{
						type:'bar',
			            label: "처리율",
			            data: chart3_quarter,
			            backgroundColor: chart2_bgColor,
			            borderWidth: 0,
			            pointStyle: 'rect',
			            fill: false,
			            pointRadius: 4
					}]
		    },
		    options: {
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true
						}]
					},
					title: {display: true,text:'분기별 요구사항 수'},
					tooltips: {
						mode: 'index'
						,intersect: false
					},
					legend: false
				}
		});
		
	}
}

//그리드 세팅
function fnFlowGridSetting(processId){
	//그리드 생성자 생성
 	gridObj[processId] = new ax5.ui.grid();
    	 
	//설정값 가져오기
	var tmp_config = flowGrid_config;
    	 
	//접수대기 그리드 세팅인경우 다른 세팅 값 호출
	if(processId == "request"){
		tmp_config = newReqGrid_config;
	}
    	 
	//타겟 변경
	tmp_config.target = $('[data-ax5grid="flowGrid-'+processId+'"]');
    	 
	//그리드 프레임 호출
	gridObj[processId].setConfig(tmp_config);
}
     
//대시보드 데이터 세팅
/******* 1 - 프로세스 목록 조회
******** 2 - 차트 데이터 조회
******** 3 - 프로세스 별 작업흐름 조회
******** 4 - 프로세스별 그리드 세팅 
******** 5 - 작업흐름 별 요구사항 조회
********/
 
function fnDash3000BoardSetting(prjId){
	
	//매개변수 null인경우 dsh3000_selPrjId 사용
	if(gfnIsNull(prjId)){
		//dsh3000_selPrjId도 null인경우 오류
		if(gfnIsNull(dsh3000_selPrjId)){
			jAlert("프로젝트가 선택되지 않았습니다.","알림");
			return false;
		}
		prjId = dsh3000_selPrjId;
	}
	
	//자동 새로고침 타이머 세팅
	timerVarSel = $('#timerVarSel').val();
  	secondTime = (timerVarSel*60);
  	clearInterval(timer);
  	timer = setInterval('printTime()',1000);
  	
	//차트 및 접수 요구사항 목록 초기화
	$(".foldDiv[folding=0]").show('fast',function(){
		$(".titleFoldingContent[folding=0]").removeClass("down").addClass("up");	
	});
	$(".foldDiv[folding=1]").show('fast',function(){
	$(".titleFoldingContent[folding=1]").removeClass("down").addClass("up");
	
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000DashBoardDataAjax.do'/>","loadingShow":true}
			,{dshType:"dsh1000", prjId: prjId});
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
    		
			//프로세스 목록
			var processList = data.processList;
				
			//작업흐름 목록
			var flowList = data.flowList;
				
			//접수 대기 그리드 세팅
			fnFlowGridSetting("request");
			
			//접수 대기 요구사항 목록
			var newReqList = data.newReqList;
			
			if(!gfnIsNull(data.newReqList)){
				//접수대기 요구사항 건수 표시
				$("#requestTotalCnt").html(data.newReqList.length);
			}else{
				$("#requestTotalCnt").html(0);
			}
			
			//차트 세팅
			fnChartSetting(data);
			
			//요구사항 목록 세팅
			if(!gfnIsNull(newReqList)){
				gridObj["request"].setData(newReqList);
			}
			
			//프로세스 내용 초기화
			$("#process_list_box").html('');
				
			//프로세스 loop
			$.each(processList,function(idx, map){
				var foldingNum = (idx+2);
				//프로세스 html 추가 (append 이후 바로 그리드 세팅)
				$("#process_list_box").append(
					'<div class="process_info_box" processid="'+map.processId+'">'
						+'<div class="dsh_title_box">'
							+map.processNm
							+'<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="'+foldingNum+'"></span></div>'
							+'<div class="titleRedoBtn"  redoid="process" processid="'+map.processId+'" onclick="fnSubDataLoad(\'obj\',this);"><li class="fa fa-redo"></li></div>'
							+'<div class="titleAllViewBtn"><span class="titleAllViewContent on" processid="'+map.processId+'"></span></div>'
							+' [ 총 : <span class="totalReqCnt">0</span>건 ]</div>'
						+'<div class="foldDiv" folding="'+foldingNum+'">'
							+'<div class="process_flow_box">'
							+'</div>'
							+'<div class="process_req_box" folding="'+foldingNum+'" style="display:none;">'
							+'<div>'
								+'	<div data-ax5grid="flowGrid-'+map.processId+'" data-ax5grid-config="{}" style="height: 250px;"></div>'
							+'</div>'
							+'</div>'
						+'</div>'
					+'</div>');
			});
			
			//작업흐름 세팅
			fnFlowListSetting(flowList);
		
			//이벤트 초기화
			$("div.titleFoldingBtn, div.titleAllViewBtn").off("click");
		
			//요구사항 갯수별 작업흐름 보이기 버튼 동작
			$("div.titleAllViewBtn").click(function(){
				//프로세스 타겟
				var $allViewBtn = $(this).children(".titleAllViewContent");
				
				//프로세스 id
				var processId = $allViewBtn.attr("processid");
				
				//true - 숨김, false - 보임 
				var allViewDown = $allViewBtn.hasClass("off");
				
				//div on/off
				if(allViewDown){
					$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box[totalcnt=0]").show().next(".dsh_flow_arrow_box").show();
					
					//화살표 전체 보이기
					$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box").next(".dsh_flow_arrow_box").show();
					$allViewBtn.removeClass("off").addClass("on");
					
				}else{
					$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box[totalcnt=0]").hide().next(".dsh_flow_arrow_box").hide();
					
					//감추기할때 남은 작업흐름이 1개이면 화살표 전체 감추기
					if($(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box[totalcnt!=0]").length == 1){
						$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box").next(".dsh_flow_arrow_box").hide();
					}
					$allViewBtn.removeClass("on").addClass("off");
				}
			});
			
			//폴딩 버튼 동작
			$("div.titleFoldingBtn").click(function(){
				//폴더 타겟
				var $foldBtn = $(this).children(".titleFoldingContent");
				
				//폴드 번호
				var foldNum = $foldBtn.attr("folding");
				
				//true - 닫혀있음, false - 열려있음
				var foldLayoutDown = $foldBtn.hasClass("down");
				
				//foldDiv target
				var $foldDiv = $(".foldDiv[folding="+foldNum+"]");
				
				//foldDiv toggle
				$foldDiv.slideToggle();
				
				//div on/off
				if(foldLayoutDown){
					$(this).parent(".dsh_title_box").removeClass("titleFolded");
					$foldBtn.removeClass("down").addClass("up");
					
					//신호등 표시 보이기
					//$(".dtmOverAlertCnt_desc[folding="+foldNum+"]").show();
				}else{
					$(this).parent(".dsh_title_box").addClass("titleFolded");
					$foldBtn.removeClass("up").addClass("down");
					
					//신호등 표시 감추기
					//$(".dtmOverAlertCnt_desc[folding="+foldNum+"]").hide();
				}
			});
			
			//전체 접기/펼치기 버튼
			$("div.autoRefreshFoldingBtn").click(function(){
				//폴더 타겟
				var $foldBtn = $(this).children(".titleFoldingContent");
				
				//true - 닫혀있음, false - 열려있음
				var foldLayoutDown = $foldBtn.hasClass("down");
				
				//foldDiv target
				var $foldDiv = $(".foldDiv");
				
				//div on/off
				if(foldLayoutDown){
					$("div.titleFoldingBtn").parent(".dsh_title_box").removeClass("titleFolded");
					$("div.titleFoldingBtn").children(".titleFoldingContent").removeClass("down").addClass("up");
					$foldBtn.removeClass("down").addClass("up");
					
					//신호등 표시 보이기
					//$(".dtmOverAlertCnt_desc").show();
					
					//전체 열기
					$foldDiv.show();
				}else{
					$("div.titleFoldingBtn").parent(".dsh_title_box").addClass("titleFolded");
					$("div.titleFoldingBtn").children(".titleFoldingContent").removeClass("up").addClass("down");
					$foldBtn.removeClass("up").addClass("down");
					
					//신호등 표시 감추기
					//$(".dtmOverAlertCnt_desc").hide();
					
					//전체 닫기
					$foldDiv.hide();
				}
			});
		});
	
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
	});
}

//작업흐름 목록 세팅
function fnFlowListSetting(flowList){
	var processFlwCnt = 0;
	var loopProcessId = "";
		
	var proReqChargeCnt = 0;
	var proReqTotalCnt = 0;
	//작업흐름 LOOP
	$.each(flowList,function(idx, map){
		
		//값 올리기
		var $titleTarget = $("div.process_info_box[processid="+map.processId+"] .dsh_title_box");
		
		//프로세스 다른 경우 cnt 초기화
		if(loopProcessId != map.processId){
			//프로세스 Loop끝난 경우 total값 타이틀에 올리기
			if(idx != 0){
				proReqChargeCnt = 0;
				proReqTotalCnt = 0;
			}
			processFlwCnt = 0;
			loopProcessId = map.processId;
		}
		
		//프로세스 total값 계산
		proReqTotalCnt += map.reqTotalCnt;
		
		//총 건수
		$titleTarget.children(".totalReqCnt").text(proReqTotalCnt);
		
		//첫 작업흐름 아닌경우 화살표 추가
		if(processFlwCnt != 0){
			$("div.process_info_box[processid="+map.processId+"] .process_flow_box").append('<div class="dsh_flow_arrow_box"></div>');
		}
	
		//각 옵션 체크
		var flowOptionStr = "";
 		//flowNextId 없는경우
		if(gfnIsNull(map.flowNextId) || map.flowNextId == "null"){
			flowOptionStr += "<li class='far fa-stop-circle' title='[종료]최종 완료'></li>";
		} 
		//필수 체크
		if(map.flowEssentialCd == "01"){
			flowOptionStr += '<li class="fa fa-key" title="필수"></li>';
		}
		//결재 체크
		if(map.flowSignCd == "01"){
			flowOptionStr += '<li class="fa fa-file-signature" title="결재"></li>';
		}
		//종료분기 체크
		if(map.flowEndCd == "01"){
			flowOptionStr += '<li class="fa fa-sign-out-alt" title="종료 분기"></li>';
		}
		//작업 체크
		if(map.flowWorkCd == "01"){
			flowOptionStr += '<li class="fa fa-code-branch" title="작업 분기"></li>';
		}
		//리비전 체크
		if(map.flowRevisionCd == "01"){
			flowOptionStr += '<li class="fa fa-code" title="리비전 저장 유무"></li>';
		}
		//배포계획 체크
		if(map.flowDplCd == "01"){
			flowOptionStr += '<li class="fa fa-puzzle-piece" title="배포계획 저장 유무"></li>';
		}
		//허용 역할
		if(map.flowAuthCd == "01"){
			flowOptionStr += '<li class="fa fa-user-shield" title="허용 역할그룹 제한유무"></li>';
		}
		//추가항목 체크
		if(map.optCnt > 0){
			flowOptionStr += '<li class="fa fa-list" title="추가 항목"></li>+'+map.optCnt;
		}
	
		//옵션이 있는경우 div세팅
		//if(!gfnIsNull(flowOptionStr)){
			flowOptionStr = '<div class="flowOptionDiv">'+flowOptionStr+'</div>';
		//}
		$("div.process_info_box[processid="+map.processId+"] .process_flow_box").append(
				'<div class="dsh_flow_box" flowid="'+map.flowId+'" totalcnt="'+map.reqTotalCnt+'"  style="background-color: '+map.flowTitleBgColor+';color: '+map.flowTitleColor+';">'
				+flowOptionStr
				+'	<div class="flow_box_title">'+map.flowNm+'</div>'
				+'	<div class="flow_box_contents" flowid="'+map.flowId+'" style="background-color: '+map.flowContentBgColor+';color: '+map.flowContentColor+';">'
				+'<span onclick="fnFlowReqList(\''+map.processId+'\',\''+map.flowId+'\',\'all\')">'+map.reqTotalCnt+'</span>'
				+'</div>'
				+'</div>');
		/* 
		$(".process_info_box[processid="+map.processId+"] .process_flow_box .dsh_flow_box[totalcnt=0]").hide().next(".dsh_flow_arrow_box").hide();
					
		//감추기할때 남은 작업흐름이 1개이면 화살표 전체 감추기
		if($(".process_info_box[processid="+map.processId+"] .process_flow_box .dsh_flow_box[totalcnt!=0]").length == 1){
			$(".process_info_box[processid="+map.processId+"] .process_flow_box .dsh_flow_box").next(".dsh_flow_arrow_box").hide();
		} */
		processFlwCnt++;
	});
	
	//총 요구사항 갯수가 0인경우 감추기
	$(".dsh_flow_box[totalcnt=0]").hide().next(".dsh_flow_arrow_box").hide();
}

//부분 새로고침
//type: obj- div attr에서 값 가져오기, etc -> thisObj: redoId, thisProcessId: processId
function fnSubDataLoad(type,thisObj,thisProcessId){
	if(type == "obj"){
		var $targetObj = $(thisObj);
		var redoId = $targetObj.attr("redoid");
		var processId = $targetObj.attr("processid");
	}else{
		//obj type이 아닌경우 redoId입력 값 대입
		var redoId = thisObj;
		var processId = thisProcessId;
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000DashBoardSubDataAjax.do'/>","loadingShow":false}
			,{prjId: dsh3000_selPrjId, redoId: redoId,processId: processId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		//차트
		if(redoId == "chart"){
			fnChartSetting(data);
		}
		//접수 요청
		else if(redoId == "request"){
			//접수 대기 요구사항 목록
			var newReqList = data.newReqList;
				
			//요구사항 목록 세팅
			if(!gfnIsNull(newReqList)){
				gridObj["request"].setData(newReqList);
			}
		}
		//프로세스
		else if(redoId == "process"){
			var $reqGridDiv = $("div.process_info_box[processid="+processId+"] .titleFoldingContent");
			
			//요구사항 그리드 창 열려있다면 닫기
			$("div.process_info_box[processid="+processId+"] .process_req_box").hide("fast");
			
			//내용 초기화
			$("div.process_info_box[processid="+processId+"] .process_flow_box").html('');
			
			//작업흐름 목록
			var flowList = data.flowList;
			
			//작업흐름 데이터 세팅
			fnFlowListSetting(flowList);
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message,"알림창");
	});
	
	//AJAX 전송
	ajaxObj.send();
	
}

//작업흐름 선택시 요구사항 목록 세팅
function fnFlowReqList(processId, flowId, type){
	var $reqDivTarget = $("div.process_info_box[processid="+processId+"] .process_req_box");
	
	//flowBox active처리
	$(".process_info_box[processid="+processId+"] .dsh_flow_box.active").removeClass("active");
	$(".process_info_box[processid="+processId+"] .dsh_flow_box[flowid="+flowId+"]").addClass("active");
	
	//그리드 open
	$reqDivTarget.show("fast",function(){
		//그리드 설정값 초기화
		fnFlowGridSetting(processId);
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000ProFlowRequestAjax.do'/>","loadingShow":false}
				,{prjId: dsh3000_selPrjId, processId: processId, flowId: flowId, type: type});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			//요구사항 목록
			var proFlowReqList = data.proFlowReqList;

			//그리드 목록 세팅
			if(!gfnIsNull(proFlowReqList)){
				gridObj[processId].setData(proFlowReqList);
				
				//해당 작업흐름+ 타이틀 개수 변경
				//해당 프로세스 요구사항 수 obj
				var $processAllCnt = $(".process_info_box[processId="+processId+"] .totalReqCnt");
				
				//조회 결과 개수
				var selectCnt = proFlowReqList.length;
				
				//해당 작업흐름 요구사항 수
				var $spanTarget = $(".process_info_box[processId="+processId+"] .flow_box_contents[flowid="+flowId+"] > span");
				
				//전체인경우 전체 개수 재세팅
				if(type == "all"){
					//프로세스 기존 개수
					var before_proReqCnt = parseInt($processAllCnt.text());
					
					//작업흐름 div 기존 개수
					var $before_flowReqCnt = $(".process_info_box[processId="+processId+"] .flow_box_contents[flowid="+flowId+"] > span");
					
					//프로세스 타이틀 조회 개수가 다른 경우
					if(parseInt($before_flowReqCnt.text()) != selectCnt){
						var calc_value = before_proReqCnt;
						
						//담당 수 계산 1.기존 개수 감산
						calc_value -= parseInt($before_flowReqCnt.text());
						
						//2. 새로운 개수 가산
						calc_value += selectCnt;
						
						//3. 적용
						$processAllCnt.text(calc_value);
					}
					
					//작업흐름 div 개수 강제 세팅
					$before_flowReqCnt.text(selectCnt);
				}
			}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
		});
}

//실시간 시간 구하기
function printTime() {
	//프로젝트에 목록이 없으면 실행안함
	if($(".dsh3000_prjTitle").length > 0){
		timerStr = Math.floor(secondTime/60) + "분 " + (secondTime%60) + "초";
		$('#autoRefreshSpan').html(timerStr);
		secondTime--;
		
		if (secondTime < 0) {
			clearInterval(timer);
			//index 추가
			autoSelPrjIdx++;
			
			//다음 프로젝트 개체
			var $nextPrj = $(".dsh3000_prjTitle[prjidx="+autoSelPrjIdx+"]");
			
			//다음 개체 있는지 체크
			if($nextPrj.length != 0){
				//다음 개체 선택
				$nextPrj.click();
			}else{
				//개체 없는경우 첫번째 프로젝트 선택
				$(".dsh3000_prjTitle").eq(0).click();
			}
		}
	}
}

//자동 새로고침 끄기
function fnAutoRefreshEnd(){
	clearInterval(timer);
	$('#autoRefreshSpan').html("<i class='fa fa-infinity'></i>");
}

//프로젝트 목록 세팅
function fnDsh3000PrjListSetting(prjList){
	var prjListStr = '';
	
	//프로젝트 수 카운트
	var prjCnt = 0;
	
	$.each(prjList,function(idx, map){
		//프로젝트 그룹인경우
		if(gfnIsNull(map.prjGrpId)){
			var prjNm = gfnCutStrLen(map.prjNm,18);
			//프로젝트가 없는경우 skip
			if(map.leaf == 1){
				return true;
			}else{
				prjListStr += '<div class="dsh3000_prjGrpTitle dsh_title_box" title="'+map.prjNm+'">'
								+prjNm
							+'</div>';
			}
		}else{
			var prjNm = gfnCutStrLen(map.prjNm,28);
			//프로젝트인경우
			var active = '';
			var activeClass = "fa-angle-right";
			
			//첫번째 프로젝트 active
			if(prjCnt == 0){
				active = " active";
				activeClass = "fa-angle-double-right";
				
				dsh3000_selPrjId = map.prjId;
				
				$("#selPrjNm").html(gfnCutStrLen(map.prjNm,120));
				
				//대시보드 정보 불러오기
				fnDash3000BoardSetting(dsh3000_selPrjId);
			}
			
			var prjEndClass = '';
			//다음 데이터가 그룹인경우 마지막 프로젝트 class 추가
			if(idx != prjList.length){
				if(!gfnIsNull(prjList[idx+1]) && gfnIsNull(prjList[idx+1].prjGrpId)){
					prjEndClass = " dsh3000_prjTitleEnd";
				}
			}
			
			prjListStr += '<div class="dsh3000_prjTitle'+active+prjEndClass+'" prjid="'+map.prjId+'" prjidx="'+prjCnt+'" title="'+map.prjNm+'" onclick="fnDsh3000PrjSel(this)">'
								+'<i class="fa '+activeClass+'"></i>&nbsp;'
								+prjNm
							+'</div>';
			prjCnt++
		}
		
	});
	
	//화면에 프로젝트 목록 Div 뿌리기
	$("#dsh3000_prjListMainDiv").html(prjListStr);
}

//프로젝트 선택 동작
function fnDsh3000PrjSel(thisObj){
	//이전 active 소멸 동작
	$(".dsh3000_prjTitle.active .fa").removeClass("fa-angle-double-right").addClass("fa-angle-right");
	$(".dsh3000_prjTitle.active").removeClass("active");
	
	//active 대입
	$(thisObj).children(".fa").removeClass("fa-angle-right").addClass("fa-angle-double-right");
	$(thisObj).addClass("active");
	
	//대시보드 데이터 불러오기
	dsh3000_selPrjId = $(thisObj).attr("prjid");
	
	//자동갱신 인덱스
	autoSelPrjIdx = $(thisObj).attr("prjidx");
	
	//프로젝트명
	var prjNm = $(thisObj).attr("title");
	
	$("#selPrjNm").html(gfnCutStrLen(prjNm,120));
	
	//대시보드 정보 불러오기
	fnDash3000BoardSetting(dsh3000_selPrjId);
	
	
}
</script>
<div class="main_contents" style="width: 100%;padding: 30px;">
	<div class="contents_wrap">
		<div class="contents_title" style="width: 1750px;">
			통합 대시 보드
			<div class="dataAutoRefreshDiv" style="margin-right: 90px;">
				<i class="fa fa-clock"></i>&nbsp;
				<span id="autoRefreshSpan">30분 00초</span> 후 다음 정보&nbsp;-
				<i class="fa fa-cogs autoRefresh_timeIcon"></i>&nbsp;
				<select class="autoRefresh_select" id="timerVarSel" onchange="fnDash3000BoardSetting()">
					<option value="0.5">30초</option>
					<option value="1">1분</option>
					<option value="5" selected>5분</option>
					<option value="10">10분</option>
					<option value="30">30분</option>
					<option value="60">1시간</option>
				</select>
				<div class="autoRefreshFoldingBtn"><span class="titleFoldingContent up" folding="all"></span></div>
				<div class="autoRefreshEndBtn" onclick="fnAutoRefreshEnd();"><li class="fa fa-times"></li></div>
				<div class="autoRefreshRedoBtn" onclick="fnDash3000BoardSetting();"><li class="fa fa-redo"></li></div>
			</div>
		</div>
		<div class="dsh3000_leftDiv" id="dsh3000_prjListMainDiv">
		</div>
		<div class="dsh3000_rightDiv">
			<div class="dsh_top_box">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="0"></span></div>
					<div class="titleRedoBtn" redoid="chart" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					<span id="selPrjNm"></span>
				</div>
				<div class="dshChartDiv foldDiv" folding="0">
					<div class="dsh_top_sub_box dsh_top_left_box">
						<canvas id="reqTotalCntChart" width="350" height="278"></canvas>
					</div>
					<div class="dsh_top_sub_box dsh_top_middle_box">
						<canvas id="reqMonthCntChart" width="625" height="278"></canvas>
					</div>
					<div class="dsh_top_sub_box dsh_top_right_box">
						<canvas id="reqQuarterCntChart" width="400" height="278"></canvas>
					</div>
				</div>
			</div>
			<div class="dsh_middle_box">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="1"></span></div>
					<div class="titleRedoBtn" redoid="request" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					접수 대기 [총 <span id="requestTotalCnt">0</span>건]
				</div>
				<div class="foldDiv" folding="1">
					<div data-ax5grid="flowGrid-request" data-ax5grid-config="{}" style="height: 250px;"></div>
				</div>
			</div>
			<div class="process_list_box" id="process_list_box">
				<!-- 프로세스 목록 세팅 영역 -->
			</div>
		</div>
	</div>
<jsp:include page="/WEB-INF/jsp/oslits/bottom/footer.jsp" />