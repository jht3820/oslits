<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<script type="text/javascript" src="<c:url value='/js/chart/chartJs/Chart.bundle.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/css/oslops/dsh.css'/>" type='text/css'>
<style>
.contents_wrap {
    width: 100%;
    height: 100%;
    display: block;
    float: left;
}
header{display:none;}
body{display:block;}
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

//그룹별 프로젝트 분배 데이터
var prjGrpListData = [];

//그룹 분배 데이터
var prjGrpAllListData = [];

var dsh3000_selPrjId = null;

//프로젝트 - 차트
var chart1 = [];
var chart2 = [];
var chart3 = [];
var dsh3000_ctx1;
var dsh3000_ctx2;
var dsh3000_ctx3;

//그룹 차트
var grpChart1,grpChart2,grpChart3;

//자동 갱신 index
var autoSelPrjIdx = 0;

//접수 대기 그리드 설정 값
var newReqGrid_config = {
	showLineNumber: false,
	sortable:true,
	header: {align:"center"},
	columns: [
			{key: "rn", label: " ", width: '4%', align: "center"},	
			{key: "reqOrd", label: "순번", width: '8%', align: "center"},	          
			{key: "reqDtm", label: "요청일자", width: '8%', align: "center"},
			{key: "reqUsrNm", label: "요청자", width: '8%', align: "center"},
			{key: "reqUsrEmail", label: "이메일", width: '11%', align: "center"},
			{key: "reqUsrNum", label: "연락처", width: '10%', align: "center"},
			{key: "reqNm", label: "요청 명", width: '30%', align: "left"},
			{key: "reqDesc", label: "요청 내용", width: '35%', align: "left"},
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
	          	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '890','scroll');
			}
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
        	fnAcceptReqGridListSetting(this.page.selectPage, false);
        }
    }
};

//그리드 공통 설정 값 -- (항목 값 정해지면 수정필요)
var flowGrid_config = {
	showLineNumber: false,
	sortable:true,
	header: {align:"center"},
	columns: [
		{key: "rn", label: " ", width: '4%', align: "center"},	
		{key: "reqOrd", label: "순번", width: '8%', align: "center"},	
		{key: "reqProTypeNm", label: "처리 상태", width: 100, align: "center"},
		{key: "signCdNm", label: "결재 상태", width: 100, align: "center"},
		{key: "signUsrNm", label: "결재자", width: 140, align: "center"},
		{key: "reqDtm", label: "요청일자", width: 140, align: "center"},
		{key: "reqUsrNm", label: "요청자", width: 80, align: "center"},
		{key: "reqUsrEmail", label: "이메일", width: 140, align: "center"},
		{key: "reqUsrNum", label: "연락처", width: 100, align: "center"},
		{key: "reqChargerNm", label: "담당자", width: 80, align: "center"},
		{key: "reqNm", label: "요청 제목", width: 400, align: "left"},
		{key: "reqDesc", label: "요청 내용", width: 250, align: "left"},
		{key: "reqStDtm", label: "작업 시작일자", width: 140, align: "center"},
		{key: "reqEdDtm", label: "작업 종료일자", width: 140, align: "center"},
		{key: "reqStDuDtm", label: "작업 시작 예정일자", width: 140, align: "center"},
		{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: 140, align: "center"}
	],
	body: {
		align: "center",
		columnHeight: 30,
		onDBLClick:function(){
			var data = {
					"callView" : "dsh3000",
					"mode":"newReq",
					"prjId":dsh3000_selPrjId,
					"reqId": this.item.reqId,
					 "reqProType":this.item.reqProType
					}; 
			gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
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
        	fnFlowReqList(this.page.selectPage, this.self.processId, this.self.flowId, this.self.type);
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
	
	// 차트 위에 데이터 라벨링 하기
	Chart.plugins.register({
		afterDatasetsDraw: function(chart) {
			var ctx = chart.ctx;

			chart.data.datasets.forEach(function(dataset, i) {
				//미처리 건수 제외
				if(!gfnIsNull(dataset.valueShow) && !dataset.valueShow){
					return true;
				}
				var meta = chart.getDatasetMeta(i);
				if (!meta.hidden) {
					meta.data.forEach(function(element, index) {
						if(dataset.data[index] > 0){
							//글씨 스타일
							ctx.fillStyle = 'rgb(255, 255, 255)';

							var fontSize = 12;
							var fontStyle = 'normal';
							var fontFamily = 'Helvetica Neue';
							ctx.font = Chart.helpers.fontString(fontSize, fontStyle, fontFamily);

							//출력 값 구하기
							var eleValue = dataset.data[index];
							
							//최종 값
							var dataString = parseInt(eleValue);
							
							//진척률 구하는경우
							if(!gfnIsNull(dataset.valueType) && dataset.valueType == "reqEndRatio"){
								//출력 값 구하기
								var maxValue = 0;
								
								//datasets loop해서 최종 합 구하기
								if(!gfnIsNull(element._chart.data.datasets)){
									$.each(element._chart.data.datasets,function(idx, map){
										maxValue += map.data[0];
									});
								}else{
									maxValue = element._xScale.max;
								}
								
								//진척률 구하기
								dataString = fnDoneRatio((maxValue-eleValue),eleValue,1);
							}
							
							//추가 문자열 체크
							if(!gfnIsNull(dataset.valueShowStr)){
								dataString += dataset.valueShowStr;
							}
							//위치 조정
							ctx.textAlign = 'center';
							ctx.textBaseline = 'middle';

							var position = element.tooltipPosition();
							
							//Left값
							var chartX = position.x
							//Top값
							var chartY = position.y
							
							//세로축 표시
							if(dataset.valueShow == "barY"){
								chartY = position.y-((position.y-element._model.base)/2.5);
							}
							//가로축 표시
							else if(dataset.valueShow == "barX"){
								chartX = position.x-((position.x-element._model.base)/2.5)
							}
							
							//중앙 자리에 string이 위치하도록
							ctx.fillText(dataString, chartX, chartY);
						}
					});
				}
			});
		}
	});
});

//차트 세팅
function fnPrjChartSetting(data, chart1Target, chart2Target, chart3Target, prjIdTarget){
	//[차트1] 프로세스별 총개수 + 최종 완료 개수
	var processReqCnt = data.processReqCnt;
	
	//차트2
	var monthProcessReqCnt = data.monthProcessReqCnt;
	
	var targetPrjId = dsh3000_selPrjId;
	
	//월 데이터 있는경우 data에서 세팅
	if(!gfnIsNull(monthProcessReqCnt)){
		targetPrjId = monthProcessReqCnt[0].prjId;
	}
	//월 데이터가 없고 매개변수에 prjId가 있는경우
	else if(!gfnIsNull(prjIdTarget)){
		targetPrjId = prjIdTarget;
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
			//if(map.processId == "request"){
				//요구사항 최종 종료 개수
			//	chart_reqEndCnt.push(map.allCnt);
			//}else{
				//요구사항 최종 종료 개수
				chart_reqEndCnt.push(map.endCnt);	
			//}
		});
		
		dsh3000_ctx1 = document.getElementById(chart1Target).getContext('2d');
		
		chart1[targetPrjId] = new Chart(dsh3000_ctx1, {
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
		            pointRadius: 4,
		            valueShow: false
		        },{
		        	type:'bar',
		            label: '총 요구사항 수',
		            data: chart_reqAllCnt,
		            backgroundColor:'#4b73eb',
		            borderColor: 'rgb(75, 115, 235, 1)',
		            borderWidth: 2,
		            pointStyle: 'rect',
			        valueShow: 'barY'
		        }]
		    },
		    options: {
					responsive: true,
					title: {display: true,text:'프로세스별 요구사항 수'},
					tooltips: {mode: 'index',intersect: false},
					legend: false,//{labels: {usePointStyle: true}}
					scales: {
			            xAxes: [{
			                display:false
			            }]
			        }
					// 프로젝트의 프로세스 차트
					,'onClick' : function (evt, item) {
						if(!gfnIsNull(item) && item.length >1){

							// 프로세스 명을 가져온다.
			       			var label  = item[1]._model.label;
			       			var data = {};
			            	if(label =="접수 대기"){
			            		data = {"projectId" : targetPrjId, "reqProType" : "01", "popTitleMsg" : label};	
			            	}else{
			            		data = {"callView" : "dsh3000", "projectId" : targetPrjId, "processNm" : label, "popTitleMsg" : label};	
			            	}
			       			
			            	gfnLayerPopupOpen('/dsh/dsh1000/dsh1000/selectDsh1001View.do',data, "850", "550",'scroll');
			            }
					}
				}
		});
		
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
		
		var chart3_idxIf = {"01월":0,"02월":0,"03월":0,"04월":1,"05월":1,"06월":1,"07월":2,"08월":2,"09월":2,"10월":3,"11월":3,"12월":3};
		var chart3_quarter = [0,0,0,0];
		
		dsh3000_ctx2 = document.getElementById(chart2Target).getContext('2d');
		
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
			if(Object.keys(chart2_y_mmCnt).indexOf(reqEdDtmMm) == -1){
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
						//분기별 % /3
						var mmCntVal = chart2_y_mmCnt[inMap][map];
						if(mmCntVal > 0){
							mmCntVal = (mmCntVal/3);
						}
						
						//차트3 - 분기별 검사하고 데이터 추가
						chart3_quarter[chart3_idxIf[inMap]] += mmCntVal;
						
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
	            pointRadius: 4,
		        valueShow: 'barY',
		        valueShowStr: "%"
			});
		});
		
		chart2[targetPrjId] = new Chart(dsh3000_ctx2, {
		    type: 'bar',
		    data: {
		        labels: chart2_x,
		        datasets: chart2_datasets
		    },
		    options: {
		    		// 각 프로세스별 처리율(월) 차트 클릭 이벤트 - 월에 해당하는 요구사항 조회
			    	'onClick' : function (evt, item) {
		    	 		if(item.length>0){
		    	 			var label =  item[0]._model.label;
		    	 			var month = label.substring(0,2);
		    	 			var data = {};
			            	
			            	data = {"callView" : "dsh3000", "projectId" : targetPrjId, "processMonth" : month, "popTitleMsg" : label };	
			            	
			            	gfnLayerPopupOpen('/dsh/dsh1000/dsh1000/selectDsh1001View.do',data, "850", "550",'scroll');
		    	 		}
		    	 		
		    	 	},
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true,
							ticks: {
								beginAtZero: true,
								suggestedMin: 0,
			                    callback: function(value, index, values) {
			                        return value+'%';
			                    }
			                }
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
										sum += parseInt(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
								});
								
								return '총 처리율: ' + sum+"%";
							},
						}
					},
					legend: {labels: {usePointStyle: true}}
				}
		});
		
		//차트2 데이터 있는 경우 월별로 분기 데이터 생성
		dsh3000_ctx3 = document.getElementById(chart3Target).getContext('2d');
		
		chart3[targetPrjId] = new Chart(dsh3000_ctx3, {
		    type: 'bar',
		    data: {
		        labels: ["1분기","2분기","3분기","4분기"],
		        datasets: [{
						type:'bar',
			            label: "처리율",
			            data: chart3_quarter,
			            backgroundColor: chart2_bgColor,
			            borderWidth: 0,
			            pointStyle: 'rect',
			            fill: false,
			            pointRadius: 4,
				        valueShow: 'barY',
				        valueShowStr: "%"
					}]
		    },
		    options: {
		    		// 분기별 처리율 차트 클릭 - 해당하는 요구사항 목록 조회
			    	'onClick' : function (evt, item) {
			    		if(item.length > 0){
		    	 			var label =  item[0]._model.label;
		    	 			var quarter = label.substring(0,1);
		    	 			var data = {};
			            	if(quarter=="1"){
			            		data = {"callView" : "dsh3000", "projectId" : targetPrjId, "processStartMonth" : "01" ,"processEndMonth" : "03", "popTitleMsg" : "1분기"};	
			            	}else if(quarter=="2"){
			            		data = {"callView" : "dsh3000", "projectId" : targetPrjId, "processStartMonth" : "04" ,"processEndMonth" : "06", "popTitleMsg" : "2분기"};
			            	}else if(quarter=="3"){
			            		data = {"callView" : "dsh3000", "projectId" : targetPrjId, "processStartMonth" : "07" ,"processEndMonth" : "09", "popTitleMsg" : "3분기"};
			            	}else if(quarter=="4"){
			            		data = {"callView" : "dsh3000", "projectId" : targetPrjId, "processStartMonth" : "10" ,"processEndMonth" : "12", "popTitleMsg" : "4분기"};
			            	}
			            	
			            	gfnLayerPopupOpen('/dsh/dsh1000/dsh1000/selectDsh1001View.do',data, "850", "550",'scroll');
		    	 		}
		    	 	},
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true,
							ticks: {
								beginAtZero: true,
								suggestedMin: 0,
			                    callback: function(value, index, values) {
			                        return value+'%';
			                    }
			                }
						}]
					},
					title: {display: true,text:'분기별 처리율'},
					tooltips: {
						mode: 'index'
						,intersect: false
						,callbacks: {
							label:function(tooltipItems, data){
								return "";
							},
							footer: function(tooltipItems, data) {
								return '처리율: ' +parseInt(tooltipItems[0].yLabel)+"%";
							},
						}
					},
					legend: false
				}
		});
	}
}

//프로젝트 그룹 차트 세팅
function fnPrjGrpChartSetting(data, chart1Target, chart2Target, chart3Target){
	//[차트1] 프로젝트별 총개수 + 최종 완료 개수
	var projectReqCnt = data.projectReqCnt;
	
	//차트2
	var monthPrjReqCnt = data.monthPrjReqCnt;
	
	var targetPrjId = dsh3000_selPrjId;
	if(!gfnIsNull(monthPrjReqCnt) && dsh3000_selPrjId == "prjAll"){
		targetPrjId = monthPrjReqCnt[0].prjId;
	}
	
	//차트1 있을때만 데이터 세팅
	if(!gfnIsNull(projectReqCnt)){
		//차트1 - 데이터 세팅
		var chart_prjNm = [];
		var chart_reqAllCnt = [];
		var chart_reqEndCnt = [];
		
		//차트1 데이터 분기
		$.each(projectReqCnt,function(idx, map){
			//프로세스 명
			chart_prjNm.push(map.prjNm);
			
			//요구사항 총 개수
			chart_reqAllCnt.push(map.allCnt);
			
			//요구사항 최종 종료 개수
			chart_reqEndCnt.push(map.endCnt);	
		});
		
		var dsh3000_ctxGrp1 = document.getElementById(chart1Target).getContext('2d');

		//차트 데이터 있는지 체크하고 이미 있는경우 차트 소멸하고 다시 세팅
		if(!gfnIsNull(chart1[targetPrjId])){
			chart1[targetPrjId].destroy();
		}
		
		chart1[targetPrjId] = new Chart(dsh3000_ctxGrp1, {
		    type: 'bar',
		    data: {
		    	labels: chart_prjNm,
		        datasets: [{
		        	type:'line',
		            label: '최종완료 요구사항 수',
		            data: chart_reqEndCnt,
		            backgroundColor:'rgb(255, 125, 110)',
		            borderColor: 'rgb(255, 86, 67, 1)',
		            borderWidth: 2,
		            pointStyle: 'rectRot',
		            fill: false,
		            pointRadius: 4,
		            valueShow: false
		        },{
		        	type:'bar',
		            label: '총 요구사항 수',
		            data: chart_reqAllCnt,
		            backgroundColor:'#4b73eb',
		            borderColor: 'rgb(75, 115, 235, 1)',
		            borderWidth: 2,
		            pointStyle: 'rect',
			        valueShow: 'barY'
		        }]
		    },
		    options: {
					responsive: true,
					title: {display: true,text:'프로젝트별 요구사항 수'},
					tooltips: {mode: 'index',intersect: false},
					legend: false,
					scales: {
			            xAxes: [{
			                display:false
			            }]
			        } 
					// 프로젝트 그룹 차트 데이터 세팅
					,'onClick' : function (evt, item) {
				   		if(!gfnIsNull(item) && item.length >1){  
				   			// 프로젝트 그룹의 명칭을 가져온다.
				       		var label  = item[1]._model.label;  
				   			
				   			var canvasId = item[0]._chart.canvas.id;
		    	 			// 프로젝트 그룹 id를 가져온다.
		    	 			var prjGrpId = canvasId.split("reqPrjTotalCntChart_")[1];
		    	 			// 프로젝트 그룹 ID가 없을경우 - 왼쪽 목록에서 프로젝트 그룹을 선택하였을 경우 차트 canvas에 프로젝트 그룹 id 없음
		    	 			if(gfnIsNull(prjGrpId)){
		    	 				// sel-grpid에 세팅된 프로젝트 그룹 id를 가져온다.
		    	 				prjGrpId = $("#selPrjGrpNm").attr("sel-grpid");
		    	 			}; 
		    	 			
				           	var data = {"callView" : "dsh3000", "projectGrpId" : prjGrpId, "projectNm" : label, "popTitleMsg" : label, "chartType" : "prjReqChart"}
	
				           	gfnLayerPopupOpen('/dsh/dsh3000/dsh3000/selectDsh3001View.do',data, "850", "550",'scroll');
				   		}
				    }
				}
		});
	}
	
	//차트2 데이터 있는경우에만
	if(!gfnIsNull(monthPrjReqCnt)){
		//차트2 준비 데이터
		var chart2_x = ["01월","02월","03월","04월","05월","06월","07월","08월","09월","10월","11월","12월"];	//월별 라벨
		var chart2_y_label = [];	//프로젝트 목록
		var chart2_y_proNm = {};	//프로젝트 명
		var chart2_y_mmCnt = {};	//프로젝트별 요구사항 개수
		var chart2_datasets = [];	//차트 데이터 세팅
		var chart2_bgColor = ["#4b73eb","#936de9","#ff5643","#58c3e5","#fba450","#eb4ba4","#89eb4b","#c4eb4b","#9f4beb","#fba450","#ff5643","#58c3e5","#fba450"];	//차트 배경색
		
		//차트3 준비 데이터
		var chart3_idxIf = {"01월":0,"02월":0,"03월":0,"04월":1,"05월":1,"06월":1,"07월":2,"08월":2,"09월":2,"10월":3,"11월":3,"12월":3};
		var chart3_quarter = [0,0,0,0];
		
		dsh3000_ctxGrp2 = document.getElementById(chart2Target).getContext('2d');
		
		//차트2 데이터 분기
		$.each(monthPrjReqCnt,function(idx, map){
			
			var reqEdDtmMm = map.reqEdDuMm;
			//'월' 붙이기
			reqEdDtmMm += "월";
			
			//프로젝트명 없는경우 push
			if(chart2_y_label.indexOf(map.prjId) == -1){
				//프로젝트명
				chart2_y_label.push(map.prjId);
				
				//json 조합
				var tempJson = {};
				tempJson[map.prjId] = map.prjNm;
				$.extend(chart2_y_proNm,tempJson);
			}
			
			//데이터 배열 만들기
			if(Object.keys(chart2_y_mmCnt).indexOf(reqEdDtmMm) == -1){
				chart2_y_mmCnt[reqEdDtmMm] = {};
			}
			//월별 프로젝트별 요구사항 완료율 세팅
			if(Object.keys(chart2_y_mmCnt[reqEdDtmMm]).indexOf(map.prjId) == -1){
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
				chart2_y_mmCnt[reqEdDtmMm][map.prjId] = reqEdMmRatio;
			}
		});
		
		//data 세팅 - 프로젝트 루프
		$.each(chart2_y_label,function(idx, map){
			//데이터 배열
			var mmCntDataArr = [];
			
			//월별 루프
			$.each(chart2_x,function(inIdx, inMap){
				//월 데이터 없는경우
				if(gfnIsNull(chart2_y_mmCnt[inMap])){
					mmCntDataArr.push(0);
				}else{
					//월별 프로젝트 데이터 체크
					if(gfnIsNull(chart2_y_mmCnt[inMap][map])){	//없는경우 0(배열 크기 맞추기 위함)
						mmCntDataArr.push(0);
					}else{	//있는경우 데이터 가져와서 push
						mmCntDataArr.push(chart2_y_mmCnt[inMap][map]);
						//분기별 % /3
						var mmCntVal = chart2_y_mmCnt[inMap][map];
						if(mmCntVal > 0){
							mmCntVal = (mmCntVal/3);
						}
						
						//차트3 - 분기별 검사하고 데이터 추가
						chart3_quarter[chart3_idxIf[inMap]] += mmCntVal;
						
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
	            pointRadius: 4,
		        valueShow: 'barY',
		        valueShowStr: "%"
			});
		});
		
		chart2[targetPrjId] = new Chart(dsh3000_ctxGrp2, {
		    type: 'bar',
		    data: {
		        labels: chart2_x,
		        datasets: chart2_datasets
		    },
		    options: {
			    	// 각 프로젝트별 처리율(월) 차트 클릭 시 요구사항 목록 조회
			    	'onClick' : function (evt, item) {
		    	 		if(item.length>0){
		    	 			var label =  item[0]._model.label;
		    	 			var canvasId = item[0]._chart.canvas.id;
		    	 			// 프로젝트 그룹 id를 가져온다.
		    	 			var prjGrpId = canvasId.split("reqPrjMonthCntChart_")[1];
		    	 			var month = label.substring(0,2);
		    	 			
		    	 			// 프로젝트 그룹 ID가 없을경우 - 왼쪽 목록에서 프로젝트 그룹을 선택하였을 경우 차트 canvas에 프로젝트 그룹 id 없음
		    	 			if(gfnIsNull(prjGrpId)){
		    	 				// sel-grpid에 세팅된 프로젝트 그룹 id를 가져온다.
		    	 				prjGrpId = $("#selPrjGrpNm").attr("sel-grpid");
		    	 			}
		    	 			
			            	var data = {"callView" : "dsh3000", "projectId" : prjGrpId, "projectMonth" : month, "popTitleMsg" : label };	
			            	
			            	gfnLayerPopupOpen('/dsh/dsh3000/dsh3000/selectDsh3001View.do',data, "850", "550",'scroll');
		    	 		}
		    	 	},
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true,
							ticks: {
								beginAtZero: true,
								suggestedMin: 0,
			                    callback: function(value, index, values) {
			                        return value+'%';
			                    }
			                }
						}]
					},
					title: {display: true,text:'각 프로젝트별 처리율 (월)'},
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
										sum += parseInt(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
								});
								
								return '총 처리율: ' + sum+"%";
							},
						}
					},
					legend: {labels: {usePointStyle: true}}
				}
		});
		
		//차트2 데이터 있는 경우 월별로 분기 데이터 생성
		dsh3000_ctxGrp3 = document.getElementById(chart3Target).getContext('2d');
		
		chart3[targetPrjId] = new Chart(dsh3000_ctxGrp3, {
		    type: 'bar',
		    data: {
		        labels: ["1분기","2분기","3분기","4분기"],
		        datasets: [{
						type:'bar',
			            label: "처리율",
			            data: chart3_quarter,
			            backgroundColor: chart2_bgColor,
			            borderWidth: 0,
			            pointStyle: 'rect',
			            fill: false,
			            pointRadius: 4,
				        valueShow: 'barY',
				        valueShowStr: "%"
					}]
		    },
		    options: {
		    		// 분기별 처리율 차트 클릭 - 해당하는 요구사항 목록 조회
			    	'onClick' : function (evt, item) {
			    		if(item.length > 0){
		    	 			var label =  item[0]._model.label;
		    	 			var quarter = label.substring(0,1);
		    	 			var data = {};
		    	 			
		    	 			var canvasId = item[0]._chart.canvas.id;
		    	 			// 프로젝트 그룹 id를 가져온다.
		    	 			var prjGrpId = canvasId.split("reqPrjQuarterCntChart_")[1];
		    	 			
		    	 			// 프로젝트 그룹 ID가 없을경우 - 왼쪽 목록에서 프로젝트 그룹을 선택하였을 경우 차트 canvas에 프로젝트 그룹 id 없음
		    	 			if(gfnIsNull(prjGrpId)){
		    	 				// sel-grpid에 세팅된 프로젝트 그룹 id를 가져온다.
		    	 				prjGrpId = $("#selPrjGrpNm").attr("sel-grpid");
		    	 			}
		    	 			
			            	if(quarter=="1"){
			            		data = {"callView" : "dsh3000", "projectId" : prjGrpId, "projectStartMonth" : "01" ,"projectEndMonth" : "03", "popTitleMsg" : "1분기"};	
			            	}else if(quarter=="2"){
			            		data = {"callView" : "dsh3000", "projectId" : prjGrpId, "projectStartMonth" : "04" ,"projectEndMonth" : "06", "popTitleMsg" : "2분기"};
			            	}else if(quarter=="3"){
			            		data = {"callView" : "dsh3000", "projectId" : prjGrpId, "projectStartMonth" : "07" ,"projectEndMonth" : "09", "popTitleMsg" : "3분기"};
			            	}else if(quarter=="4"){
			            		data = {"callView" : "dsh3000", "projectId" : prjGrpId, "projectStartMonth" : "10" ,"projectEndMonth" : "12", "popTitleMsg" : "4분기"};
			            	}
			            	
			            	gfnLayerPopupOpen('/dsh/dsh3000/dsh3000/selectDsh3001View.do',data, "850", "550",'scroll');
			    		}
		    		},
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true,
							ticks: {
								beginAtZero: true,
								suggestedMin: 0,
			                    callback: function(value, index, values) {
			                        return value+'%';
			                    }
			                }
						}]
					},
					title: {display: true,text:'분기별 처리율'},
					tooltips: {
						mode: 'index'
						,intersect: false
						,callbacks: {
							label:function(tooltipItems, data){
								return "";
							},
							footer: function(tooltipItems, data) {
								return '처리율: ' +parseInt(tooltipItems[0].yLabel)+"%";
							},
						}
					},
					legend: false
				}
		});
	}
}

//프로젝트 그룹 전체 차트 세팅
function fnPrjGrpAllChartSetting(data){
	//[그룹 차트] 프로젝트 그룹별 총갯수 + 최종 완료 갯수
	var projectGrpReqCnt = data.projectGrpReqCnt;
	
	//[그룹 차트] 월별 프로젝트 그룹별 요구사항 갯수
	var monthPrjGrpReqCnt = data.monthPrjGrpReqCnt;
	
	//차트생성이 되어있다면 차트 비우기
	if(!gfnIsNull(grpChart1)){
		grpChart1.destroy();
	}
	
	//차트1 있을때만 데이터 세팅
	if(!gfnIsNull(projectGrpReqCnt)){
		//차트1 - 데이터 세팅
		var chart_prjNm = [];
		var chart_reqAllCnt = [];
		var chart_reqEndCnt = [];
		var chart_prjId = []
		
		//차트1 데이터 분기
		$.each(projectGrpReqCnt,function(idx, map){
			//프로세스 명
			chart_prjNm.push(map.prjNm);
			
			// 프로젝트 그룹 ID
			chart_prjId.push(map.prjId);
			
			//요구사항 총 개수
			chart_reqAllCnt.push(map.allCnt);
			
			//요구사항 최종 종료 개수
			chart_reqEndCnt.push(map.endCnt);	
		});
		
		var dsh3000_ctxGrp1 = document.getElementById("reqPrjGrpTotalCntChart").getContext('2d');
		
		grpChart1 = new Chart(dsh3000_ctxGrp1, {
		    type: 'bar',
		    data: {
		    	labels: chart_prjNm,
		        datasets: [{
		        	type:'line',
		            label: '최종완료 요구사항 수',
		            data: chart_reqEndCnt,
		            backgroundColor:'rgb(255, 125, 110)',
		            borderColor: 'rgb(255, 86, 67, 1)',
		            borderWidth: 2,
		            pointStyle: 'rectRot',
		            fill: false,
		            pointRadius: 4,
		            valueShow: false
		        },{
		        	type:'bar',
		            label: '총 요구사항 수',
		            data: chart_reqAllCnt,
		            backgroundColor:'#4b73eb',
		            borderColor: 'rgb(75, 115, 235, 1)',
		            borderWidth: 2,
		            pointStyle: 'rect',
			        valueShow: 'barY'
		        }]
		    },
		    options: {
					responsive: true,
					title: {display: true,text:'프로젝트 그룹별 요구사항 수'},
					tooltips: {mode: 'index',intersect: false},
					legend: false,
					scales: {
			            xAxes: [{
			                display:false
			            }]
			        }  
					// 통합데이터 프로젝트 그룹별 요구사항 수 차트 클릭
					,'onClick' : function (evt, item) {
				   		if(!gfnIsNull(item) && item.length >1){ 
				   			// 프로젝트 그룹의 명칭을 가져온다.
				       		var label  = item[1]._model.label;  
				   			// 차트 클릭시 차트데이터의 index를 가져온다.
				   			var chartPoint = grpChart1.getElementAtEvent(evt)[0];
				   			var chartIndex = chartPoint._index;
				           	// 프로젝트 그룹 id를 담은 차드 배열에서 차트 index로 프로젝트 그룹 id를 가져온다.
				           	var prjGrpId = chart_prjId[chartIndex];
				           	// 데이터 세팅 - 데이터 조회시 조회조건을 구분하기 위한 chartType 값을 넘겨준다.
				           	var data = {"callView" : "dsh3000", "projectId" : prjGrpId, "popTitleMsg" : label, "chartType" : "integratedPrjGrpChart"}
				           	
				           	gfnLayerPopupOpen('/dsh/dsh3000/dsh3000/selectDsh3001View.do',data, "850", "550",'scroll');
						}
				    }
				}
		});
	}
	
	//데이터 없을때 차트생성이 되어있다면 차트 비우기
	if(!gfnIsNull(grpChart2)){
		grpChart2.destroy();
	}
	
	if(!gfnIsNull(grpChart3)){
		grpChart3.destroy();
	}
		
	//차트2 데이터 있는경우에만
	if(!gfnIsNull(monthPrjGrpReqCnt)){
		//차트2 준비 데이터
		var chart2_x = ["01월","02월","03월","04월","05월","06월","07월","08월","09월","10월","11월","12월"];	//월별 라벨
		var chart2_y_label = [];	//프로젝트 목록
		var chart2_y_proNm = {};	//프로젝트 명
		var chart2_y_mmCnt = {};	//프로젝트별 요구사항 개수
		var chart2_datasets = [];	//차트 데이터 세팅
		var chart2_bgColor = ["#4b73eb","#936de9","#ff5643","#58c3e5","#fba450","#eb4ba4","#89eb4b","#c4eb4b","#9f4beb","#fba450","#ff5643","#58c3e5","#fba450"];	//차트 배경색
		
		//차트3 준비 데이터
		var chart3_idxIf = {"01월":0,"02월":0,"03월":0,"04월":1,"05월":1,"06월":1,"07월":2,"08월":2,"09월":2,"10월":3,"11월":3,"12월":3};
		var chart3_quarter = [0,0,0,0];
		
		dsh3000_ctxGrp2 = document.getElementById("reqPrjGrpMonthCntChart").getContext('2d');
		
		//차트2 데이터 분기
		$.each(monthPrjGrpReqCnt,function(idx, map){
			var reqEdDtmMm = map.reqEdDuMm;
			//'월' 붙이기
			reqEdDtmMm += "월";
			
			//프로세스명 없는경우 push
			if(chart2_y_label.indexOf(map.prjId) == -1){
				//프로세스명
				chart2_y_label.push(map.prjId);
				
				//json 조합
				var tempJson = {};
				tempJson[map.prjId] = map.prjNm;
				$.extend(chart2_y_proNm,tempJson);
			}
			
			//데이터 배열 만들기
			if(Object.keys(chart2_y_mmCnt).indexOf(reqEdDtmMm) == -1){
				chart2_y_mmCnt[reqEdDtmMm] = {};
			}
			//월별 프로세스별 요구사항 완료율 세팅
			if(Object.keys(chart2_y_mmCnt[reqEdDtmMm]).indexOf(map.prjId) == -1){
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
				
				chart2_y_mmCnt[reqEdDtmMm][map.prjId] = reqEdMmRatio;
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
						//분기별 % /3
						var mmCntVal = chart2_y_mmCnt[inMap][map];
						if(mmCntVal > 0){
							mmCntVal = (mmCntVal/3);
						}
						
						//차트3 - 분기별 검사하고 데이터 추가
						chart3_quarter[chart3_idxIf[inMap]] += mmCntVal;
						
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
	            pointRadius: 4,
		        valueShow: 'barY',
		        valueShowStr: "%"
			});
		});
		
		grpChart2 = new Chart(dsh3000_ctxGrp2, {
		    type: 'bar',
		    data: {
		        labels: chart2_x,
		        datasets: chart2_datasets
		    },
		    options: {
			    	// 통합데이터 프로젝트 그룹별 처리율(월) 차트 클릭 시 요구사항 목록 조회
			    	'onClick' : function (evt, item) {
		    	 		if(item.length>0){
		    	 			var label =  item[0]._model.label;
		    	 			var month = label.substring(0,2);
			            	var data = {"callView" : "dsh3000", "projectId" : "all", "projectMonth" : month, "popTitleMsg" : label };	
							
			            	gfnLayerPopupOpen('/dsh/dsh3000/dsh3000/selectDsh3001View.do',data, "850", "550",'scroll');
		    	 		}
		    	 	},
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true,
							ticks: {
								beginAtZero: true,
								suggestedMin: 0,
			                    callback: function(value, index, values) {
			                        return value+'%';
			                    }
			                }
						}]
					},
					title: {display: true,text:'각 프로젝트 그룹별 처리율 (월)'},
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
										sum += parseInt(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
								});
								
								return '총 처리율: ' + sum+"%";
							},
						}
					},
					legend: {labels: {usePointStyle: true}}
				}
		});
		
		//차트2 데이터 있는 경우 월별로 분기 데이터 생성
		dsh3000_ctxGrp3 = document.getElementById("reqPrjGrpQuarterCntChart").getContext('2d');
		
		grpChart3 = new Chart(dsh3000_ctxGrp3, {
		    type: 'bar',
		    data: {
		        labels: ["1분기","2분기","3분기","4분기"],
		        datasets: [{
						type:'bar',
			            label: "처리율",
			            data: chart3_quarter,
			            backgroundColor: chart2_bgColor,
			            borderWidth: 0,
			            pointStyle: 'rect',
			            fill: false,
			            pointRadius: 4,
				        valueShow: 'barY',
				        valueShowStr: "%"
					}]
		    },
		    options: {
			    	// 통합데이터 분기별 처리율 차트 클릭 - 해당하는 요구사항 목록 조회
			    	'onClick' : function (evt, item) {
			    		if(item.length > 0){
		    	 			var label =  item[0]._model.label;
		    	 			var quarter = label.substring(0,1);
		    	 			var data = {};

			            	if(quarter=="1"){
			            		data = {"callView" : "dsh3000", "projectId" : "all", "projectStartMonth" : "01" ,"projectEndMonth" : "03", "popTitleMsg" : "1분기"};	
			            	}else if(quarter=="2"){
			            		data = {"callView" : "dsh3000", "projectId" : "all", "projectStartMonth" : "04" ,"projectEndMonth" : "06", "popTitleMsg" : "2분기"};
			            	}else if(quarter=="3"){
			            		data = {"callView" : "dsh3000", "projectId" : "all", "projectStartMonth" : "07" ,"projectEndMonth" : "09", "popTitleMsg" : "3분기"};
			            	}else if(quarter=="4"){
			            		data = {"callView" : "dsh3000","projectId" : "all", "projectStartMonth" : "10" ,"projectEndMonth" : "12", "popTitleMsg" : "4분기"};
			            	}
			            	
			            	gfnLayerPopupOpen('/dsh/dsh3000/dsh3000/selectDsh3001View.do',data, "850", "550",'scroll');
			    		}
		    		},
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true,
							ticks: {
								beginAtZero: true,
								suggestedMin: 0,
			                    callback: function(value, index, values) {
			                        return value+'%';
			                    }
			                }
						}]
					},
					title: {display: true,text:'분기별 처리율'},
					tooltips: {
						mode: 'index'
						,intersect: false
						,callbacks: {
							label:function(tooltipItems, data){
								return "";
							},
							footer: function(tooltipItems, data) {
								return '처리율: ' +parseInt(tooltipItems[0].yLabel)+"%";
							},
						}
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
 
function fnDash3000PrjBoardSetting(prjId){
	//매개변수 null인경우 dsh3000_selPrjId 사용
	if(gfnIsNull(prjId)){
		//dsh3000_selPrjId도 null인경우 오류
		if(gfnIsNull(dsh3000_selPrjId)){
			jAlert("프로젝트가 선택되지 않았습니다.","알림");
			return false;
		}
		prjId = dsh3000_selPrjId;
	}
	
	//차트 전체 비우기
	$.each(Object.keys(chart1), function(idx, map){
		chart1[map].destroy();
	});
	//차트 전체 비우기
	$.each(Object.keys(chart2), function(idx, map){
		chart2[map].destroy();
	});
	//차트 전체 비우기
	$.each(Object.keys(chart3), function(idx, map){
		chart3[map].destroy();
	});
	chart1 = [];
	chart2 = [];
	chart3 = [];
	
	//자동 새로고침 타이머 세팅
	timerVarSel = $('#timerVarSel').val();
  	secondTime = (timerVarSel*60);
  	clearInterval(timer);
  	timer = setInterval('printTime()',1000);
  	
	//차트 및 접수 요구사항 목록 초기화
	$(".foldDiv[folding=prj]").show('fast',function(){
		$(".titleFoldingContent[folding=prj]").removeClass("down").addClass("up");	
	});
	$(".foldDiv[folding=request]").show('fast',function(){
	$(".titleFoldingContent[folding=request]").removeClass("down").addClass("up");
	
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000DashBoardPrjDataAjax.do'/>","loadingShow":true}
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
			
			// 접수대기 그리드 요구사항 세팅
			fnAcceptReqGridListSetting(gridObj["request"].page.currentPage, false);

			/*
			// 접수대기 요구사항 - fnAcceptReqGridListSetting() 에서 세팅
			//접수 대기 요구사항 목록
			var newReqList = data.newReqList;
			if(!gfnIsNull(data.newReqList)){
				//접수대기 요구사항 건수 표시
				$("#requestTotalCnt").html(data.newReqList.length);
			}else{
				$("#requestTotalCnt").html(0);
			}*/
			
			//차트 세팅
			fnPrjChartSetting(data,"reqTotalCntChart","reqMonthCntChart","reqQuarterCntChart");
			
			//요구사항 목록 세팅
			/* if(!gfnIsNull(newReqList)){
				gridObj["request"].setData(newReqList);
			} */
			
			//프로세스 내용 초기화
			$("#process_list_box").html('');
				
			//프로세스 loop
			$.each(processList,function(idx, map){
				var foldingNum = (idx+2);
				
				//진척률
				var proDoneReqRatio = fnDoneRatio(map.proDoReqCnt, map.proDoneReqCnt);
				
				//프로세스 html 추가 (append 이후 바로 그리드 세팅)
				$("#process_list_box").append(
					'<div class="process_info_box" processid="'+map.processId+'">'
						+'<div class="dsh_title_box">'
							+map.processNm
							+'<div class="titleFoldingBtn" onclick="fnTitleFoldingAction(this)"><span class="titleFoldingContent up" folding="'+foldingNum+'"></span></div>'
							+'<div class="titleRedoBtn"  redoid="process" prjid="'+map.prjId+'" processid="'+map.processId+'" onclick="fnSubDataLoad(this);"><li class="fa fa-redo"></li></div>'
							+'<div class="titleAllViewBtn" onclick="fnTitleAllViewBtn(this)"><span class="titleAllViewContent on" processid="'+map.processId+'"></span></div>'
							+' [ 진행 : <span class="doReqCnt">'+map.proDoReqCnt+'</span>건 / 완료 : <span class="doneReqCnt">'+map.proDoneReqCnt+'</span>건 / 진척률 : <span class="doneReqRatio">'+proDoneReqRatio+'</span>%]'
								+'<div class="dsh2000_dtmOverAlert" processid="'+map.processId+'">'
								+'<div class="dtmOverAlertCnt_desc" folding="'+foldingNum+'">'
								+'	<div class="alertDesc">초과</div>'
								+'	<div class="alertDesc">임박</div>'
								+'	<div class="alertDesc">여유</div>'
								+'	<div class="alertDesc">실패</div>'
								+'	<div class="alertDesc">적기</div>'
								+'</div>'
								+'	<div class="dtmOverAlertCnt alert-red" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \''+map.processId+'\', \'03\', \''+map.processNm+' [초과]\');"  title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>'
								+'	<div class="dtmOverAlertCnt alert-yellow" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \''+map.processId+'\', \'02\', \''+map.processNm+' [임박]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>'
								+'	<div class="dtmOverAlertCnt alert-green" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \''+map.processId+'\', \'01\', \''+map.processNm+' [여유]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>'
								+'	<div class="dtmOverAlertCnt alert-overRed" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \''+map.processId+'\', \'04\', \''+map.processNm+' [실패]\');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>'
								+'	<div class="dtmOverAlertCnt alert-blue" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \''+map.processId+'\', \'05\', \''+map.processNm+' [적기]\');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>'
								+'</div>'
							+'</div>'
						+'<div class="foldDiv" folding="'+foldingNum+'">'
							+'<div class="process_flow_box">'
							+'</div>'
							/* +'<div class="process_flow_chart_box">'
								+'<canvas id="PRO_'+map.processId+'" width="1370" height="60"></canvas>'
							+'</div>' */
							+'<div class="process_req_box" folding="'+foldingNum+'" style="display:none;">'
							+'<div>'
								+'	<div data-ax5grid="flowGrid-'+map.processId+'" data-ax5grid-config="{}" style="height: 250px;"></div>'
							+'</div>'
							+'</div>'
						+'</div>'
					+'</div>');
			});
			
			//프로세스별 기간 경고 조회
			fnReqDtmOverAlertListSetting(data.reqDtmOverAlertList);
			
			//작업흐름 세팅
			fnFlowListSetting(flowList);
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

//프로젝트 그룹 대시보드 데이터 불러오기
function fnDash3000PrjGrpBoardSetting(prjId){
	//매개변수 null인경우 dsh3000_selPrjId 사용
	if(gfnIsNull(prjId)){
		//dsh3000_selPrjId도 null인경우 오류
		if(gfnIsNull(dsh3000_selPrjId)){
			jAlert("프로젝트가 선택되지 않았습니다.","알림");
			return false;
		}
		prjId = dsh3000_selPrjId;
	}
	
	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000DashBoardPrjGrpDataAjax.do'/>","loadingShow":true}
			,{prjId: prjId});
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			//차트 전체 비우기
			$.each(Object.keys(chart1), function(idx, map){
				chart1[map].destroy();
			});
			//차트 전체 비우기
			$.each(Object.keys(chart2), function(idx, map){
				chart2[map].destroy();
			});
			//차트 전체 비우기
			$.each(Object.keys(chart3), function(idx, map){
				chart3[map].destroy();
			});
			chart1 = [];
			chart2 = [];
			chart3 = [];
			
			//자동 새로고침 타이머 세팅
			timerVarSel = $('#timerVarSel').val();
		  	secondTime = (timerVarSel*60);
		  	clearInterval(timer);
		  	timer = setInterval('printTime()',1000);
		  	
			//차트 및 접수 요구사항 목록 초기화
			$(".foldDiv[folding=prjGrp]").show('fast',function(){
				$(".titleFoldingContent[folding=prjGrp]").removeClass("down").addClass("up");	
			});
			
			//차트 세팅
			fnPrjGrpChartSetting(data,"reqPrjTotalCntChart","reqPrjMonthCntChart","reqPrjQuarterCntChart");
			
			//프로젝트별 차트 세팅
			fnPrjLayoutSetting(data);
			
			//프로젝트별 기간 알림
			fnPrjReqDtmOverAlertListSetting(data.prjReqDtmOverAlertList,"projectGrp");
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
}

//전체 프로젝트 그룹 대시보드 데이터 불러오기
function fnDash3000PrjGrpAllBoardSetting(){
				
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
		{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000DashBoardPrjGrpAllDataAjax.do'/>","loadingShow":true});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//차트 전체 비우기
		$.each(Object.keys(chart1), function(idx, map){
			chart1[map].destroy();
		});
		//차트 전체 비우기
		$.each(Object.keys(chart2), function(idx, map){
			chart2[map].destroy();
		});
		//차트 전체 비우기
		$.each(Object.keys(chart3), function(idx, map){
			chart3[map].destroy();
		});
		chart1 = [];
		chart2 = [];
		chart3 = [];
		
		//자동 새로고침 타이머 세팅
		timerVarSel = $('#timerVarSel').val();
	  	secondTime = (timerVarSel*60);
	  	clearInterval(timer);
	  	timer = setInterval('printTime()',1000);
	  	
		//차트 및 접수 요구사항 목록 초기화
		$(".foldDiv[folding=prjGrpAll]").show('fast',function(){
			$(".titleFoldingContent[folding=prjGrpAll]").removeClass("down").addClass("up");	
		});
		
		//차트 세팅
		fnPrjGrpAllChartSetting(data);
		
		//프로젝트별 차트 세팅
		fnPrjGrpLayoutSetting(data);
		
		//프로젝트별 기간 알림
		fnPrjReqDtmOverAlertListSetting(data.prjGrpReqDtmOverAlertList,"projectGrpAll");
		 
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message,"알림창");
	});
	
	//AJAX 전송
	ajaxObj.send();
}

//프로세스별 기간 경고 조회
function fnReqDtmOverAlertListSetting(reqDtmOverAlertList){
	//프로젝트 경고 총 합
	var prjGreen = 0;
	var prjYellow = 0;
	var prjRed = 0;
	var prjOverRed = 0;
	var prjBlue = 0;
	
	$.each(reqDtmOverAlertList,function(idx, map){
		var $target = $(".dsh2000_dtmOverAlert[processid="+map.processId+"]");
		//종료일 없이 조건
		//green
		if(map.overType == "01"){
			prjGreen += map.overCnt;
			$target.children(".alert-green").html(map.overCnt);
		}
		//yellow
		else if(map.overType == "02"){
			prjYellow += map.overCnt;
			$target.children(".alert-yellow").html(map.overCnt);
		}
		//red
		else if(map.overType == "03"){
			prjRed += map.overCnt;
			$target.children(".alert-red").html(map.overCnt);
		}
		//종료일  있는 조건
		//overRed
		else if(map.overType == "04"){
			prjOverRed += map.overCnt;
			$target.children(".alert-overRed").html(map.overCnt);
		}
		//blue
		else if(map.overType == "05"){
			prjBlue += map.overCnt;
			$target.children(".alert-blue").html(map.overCnt);
		}
	});
	
	//프로젝트 경고 출력
	var $prjTarget = $(".dsh2000_dtmOverAlert[processid=project]");
	$prjTarget.children(".alert-green").html(prjGreen);
	$prjTarget.children(".alert-yellow").html(prjYellow);
	$prjTarget.children(".alert-red").html(prjRed);
	$prjTarget.children(".alert-overRed").html(prjOverRed);
	$prjTarget.children(".alert-blue").html(prjBlue);
}


//프로젝트별 기간 경고 조회
function fnPrjReqDtmOverAlertListSetting(prjReqDtmOverAlertList,target){
	//프로젝트 경고 총 합
	var prjGreen = 0;
	var prjYellow = 0;
	var prjRed = 0;
	var prjOverRed = 0;
	var prjBlue = 0;
	
	$.each(prjReqDtmOverAlertList,function(idx, map){
		var $target = $(".dsh2000_dtmOverAlert[prjid="+map.prjId+"]");
		 
		//해당 프로젝트 없는 경우 skip
		if(gfnIsNull($target)){
			return true;	
		}
		
		//종료일 없이 조건
		//green
		if(map.overType == "01"){
			prjGreen += map.overCnt;
			$target.children(".alert-green").html(map.overCnt);
		}
		//yellow
		else if(map.overType == "02"){
			prjYellow += map.overCnt;
			$target.children(".alert-yellow").html(map.overCnt);
		}
		//red
		else if(map.overType == "03"){
			prjRed += map.overCnt;
			$target.children(".alert-red").html(map.overCnt);
		}
		//종료일  있는 조건
		//overRed
		else if(map.overType == "04"){
			prjOverRed += map.overCnt;
			$target.children(".alert-overRed").html(map.overCnt);
		}
		//blue
		else if(map.overType == "05"){
			prjBlue += map.overCnt;
			$target.children(".alert-blue").html(map.overCnt);
		}
	});
	
	//프로젝트 경고 출력
	var $prjTarget = $(".dsh2000_dtmOverAlert[prjid="+target+"]");
	$prjTarget.children(".alert-green").html(prjGreen);
	$prjTarget.children(".alert-yellow").html(prjYellow);
	$prjTarget.children(".alert-red").html(prjRed);
	$prjTarget.children(".alert-overRed").html(prjOverRed);
	$prjTarget.children(".alert-blue").html(prjBlue);
}

//작업흐름 목록 세팅
function fnFlowListSetting(flowList){
	var processFlwCnt = 1;
	
	//chart dataset
	var chartDataset = [];
	
	//- 작업흐름 색상 세팅 후 제거 (map.flowTitleBgColor 대체)
	var chart2_bgColor = ["#4b73eb","#936de9","#ff5643","#58c3e5","#fba450","#eb4ba4","#89eb4b","#c4eb4b","#9f4beb","#fba450","#ff5643","#58c3e5","#fba450"];	//차트 배경색
	
	//작업흐름 LOOP
	$.each(flowList,function(idx, map){
		//프로세스 요구사항 총 갯수 가져오기
		//var processTotalReqCnt = $(".process_info_box[processid="+map.processId+"]").attr("totalreq");
		/* 
		//작업흐름별 진척률 구하기
		var flowReqRatio = map.reqTotalCnt;//fnDoneRatio((processTotalReqCnt-map.reqTotalCnt), map.reqTotalCnt,1);
		
		//차트 데이터 입력
		chartDataset.push({
            label: map.flowNm,
            data: [flowReqRatio],
            fontColor: map.flowTitleColor,
            backgroundColor: map.flowTitleBgColor,
	        valueShow: 'barX',
	        valueShowStr: "%",
	        valueType:'reqEndRatio'
		});
		 */
		//첫 작업흐름 아닌경우 화살표 추가
		if(processFlwCnt > 1){
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
		//결재 반려종료 체크
		if(map.flowSignStopCd == "01"){
			flowOptionStr += '<li class="far fa-stop-circle" title="결재 반려시 종료"></li>';
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
				'<div class="dsh_flow_box" flowid="'+map.flowId+'" totalcnt="'+map.reqTotalCnt+'" style="background-color: '+map.flowTitleBgColor+';color: '+map.flowTitleColor+';">'
				+flowOptionStr
				+'	<div class="flow_box_title">'+map.flowNm+'</div>'
				+'	<div class="flow_box_contents" flowid="'+map.flowId+'" style="background-color: '+map.flowContentBgColor+';color: '+map.flowContentColor+';">'
				+'<span onclick="fnFlowReqList(0, \''+map.processId+'\',\''+map.flowId+'\',\'all\')">'+map.reqTotalCnt+'</span>'
				+'</div>'
				+'</div>');
		/* 
		$(".process_info_box[processid="+map.processId+"] .process_flow_box .dsh_flow_box[totalcnt=0]").hide().next(".dsh_flow_arrow_box").hide();
					
		//감추기할때 남은 작업흐름이 1개이면 화살표 전체 감추기
		if($(".process_info_box[processid="+map.processId+"] .process_flow_box .dsh_flow_box[totalcnt!=0]").length == 1){
			$(".process_info_box[processid="+map.processId+"] .process_flow_box .dsh_flow_box").next(".dsh_flow_arrow_box").hide();
		} */
		 
		//프로세스 다른 경우 cnt 초기화
		if(!gfnIsNull(flowList[idx+1]) && flowList[idx+1].processId != map.processId || flowList.length-1 == idx){
			//프로세스 Loop끝난 경우 total값 타이틀에 올리기
			processFlwCnt = 0;
			/* 
			//차트 그리기
			var ctx = document.getElementById("PRO_"+map.processId).getContext('2d');
			var processChart = new Chart(ctx, {
			    type: 'horizontalBar',
			    data: {
			    	labels:["진척률"],
			        datasets: chartDataset
			    },
			    options: {
						responsive: true,
						scales: {
							xAxes: [{
								display: false,
								stacked: true,
							}],
							yAxes: [{
								display: false,
								stacked: true
							}]
						},
						title: {display: false},
						tooltips: false,
						legend: {labels: {usePointStyle: false},position: 'bottom'},
						layout: {
				            padding: {
				                left: 1,
				                right: 1,
				                top: 0,
				                bottom: 0
				            }
				        }
					}
			});
			//차트 배열 초기화
			chartDataset = []; */
		}
		 
		processFlwCnt++;
	});
}

/**
 * 부분 새로고침
 * @element-data	redoid:
 *	 		prjGrpAllChart - 프로젝트 그룹 전체
 *	 		prjGrpChart - 프로젝트 그룹
 *	 		project - 프로젝트 차트
 *	 		process - 프로세스
 *	 		request - 접수대기
 **/
function fnSubDataLoad(thisObj){
	 var $targetObj = $(thisObj);
	 var prjId = $targetObj.attr("prjid");
	 var redoId = $targetObj.attr("redoid");
	 
	 //canvas 추가 문자열
	 var canvas_prjId = "_"+prjId;
	 
	 //prjId없는경우 현재 선택 prjId
	 if(gfnIsNull(prjId)){
		prjId = dsh3000_selPrjId;
		canvas_prjId = "";
	 }
	 
	 //controller에 넘길 json Data
	 var queryJsonData = {prjId: prjId, redoId: redoId};
	 // 데이터 조회 URL
	 var dataSelectUrl = "<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000DashBoardSubDataAjax.do'/>";
	 
	 // 프로세스일경우
	 if(redoId == "process"){
		 var processId = $targetObj.attr("processid");
		 $.extend(queryJsonData,{processId: processId});
	// 접수대기일 경우	 
	}else if(redoId == "request"){
		 fnAcceptReqGridListSetting(gridObj["request"].page.currentPage, false);
		 return false;
	 }

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000DashBoardSubDataAjax.do'/>","loadingShow":false}
			,queryJsonData);
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		//전체 프로젝트 그룹 차트
		if(redoId == "prjGrpAllChart"){
			fnPrjGrpAllChartSetting(data);
		}
		//프로젝트 그룹 차트
		if(redoId == "prjGrpChart"){
			fnPrjGrpChartSetting(data,"reqPrjTotalCntChart"+canvas_prjId,"reqPrjMonthCntChart"+canvas_prjId,"reqPrjQuarterCntChart"+canvas_prjId);
		}
		//프로젝트 차트
		else if(redoId == "project"){
			fnPrjChartSetting(data,"reqTotalCntChart"+canvas_prjId,"reqMonthCntChart"+canvas_prjId,"reqQuarterCntChart"+canvas_prjId);
		}
		/* 접수 대기 그리드는 따로 조회 
		else if(redoId == "request"){
			//접수 대기 요구사항 목록
			var newReqList = data.newReqList;
				
			//요구사항 목록 세팅
			if(!gfnIsNull(newReqList)){
				gridObj["request"].setData(newReqList);
			}
		} */
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
function fnFlowReqList(_pageNo, processId, flowId, type){
	
	var pageNo = 0;
	
	//페이지 세팅
   	if(!gfnIsNull(_pageNo)){
   		pageNo = _pageNo;
   	}else if(typeof gridObj[processId].page.currentPage != "undefined"){
   		pageNo = gridObj[processId].page.currentPage;
   	}
	
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
				,{prjId: dsh3000_selPrjId, pageNo : pageNo, processId: processId, flowId: flowId, type: type});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			//요구사항 목록
			var proFlowReqList = data.proFlowReqList;
			// 페이지
			var proFlowReqPage = data.page;
			
			//그리드 목록 세팅
			if(!gfnIsNull(proFlowReqList)){
				gridObj[processId].setData({
					list:proFlowReqList,
					page: {
						currentPage: _pageNo || 0,
						pageSize: proFlowReqPage.pageSize,
						totalElements: proFlowReqPage.totalElements,
						totalPages: proFlowReqPage.totalPages
					}
				});
				
				//그리드 내용 세팅
				gridObj[processId]["processId"] = processId;
				gridObj[processId]["flowId"] = flowId;
				gridObj[processId]["type"] = type;
				
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
					$before_flowReqCnt.text($before_flowReqCnt.text());
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

// 접수대기 요구사항 데이터 세팅
function fnAcceptReqGridListSetting(_pageNo, loadingShow){

		var ajaxParam = "";
		
     	//페이지 세팅
     	if(!gfnIsNull(_pageNo)){
     		ajaxParam += "&pageNo="+_pageNo;
     	}else if(typeof gridObj["request"].page.currentPage != "undefined"){
     		ajaxParam += "&pageNo="+gridObj["request"].page.currentPage;
     	}
     	
     	//로딩 표시 없는경우 default = true
     	if(gfnIsNull(loadingShow)){
     		loadingShow = true;
     	}
     	
     	// 프로젝트 Id 세팅
     	ajaxParam += "&prjId="+dsh3000_selPrjId;
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dsh/dsh3000/dsh3000/selectDsh3000AcceptRequestAjax.do'/>","loadingShow":loadingShow}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			// 조회 실패
	    	if(data.errorYn == 'Y'){ 
	    		toast.push(data.message);
	    		return;
	    	}
			
			var list = data.acceptReqList;
			var page = data.page;
			var totalCnt = data.acceptTotalCnt;
			
			if(!gfnIsNull(gridObj["request"])){
				//접수대기 요구사항 건수 표시
				$("#requestTotalCnt").html(totalCnt);
			}
			
			gridObj["request"].setData({
		             	list:list,
		             	page: {
		                  currentPage: _pageNo || 0,
		                  pageSize: page.pageSize,
		                  totalElements: page.totalElements,
		                  totalPages: page.totalPages
		              }
		     });
		});
		
		//AJAX 전송
		ajaxObj.send();
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
			
			//다음 개체 type 구하기
			var nextSelType = $("#autoRefreshType").val();
			
			//현재 선택 값
			var $selPrj = $(".dsh3000_prj[prjidx="+autoSelPrjIdx+"]");
			
			//다음 개체
			var $nextPrj = null;
			
			//다음 type
			if(nextSelType == "all" || nextSelType == "grpPro" || gfnIsNull(nextSelType)){
				//index 추가
				autoSelPrjIdx++;
				
				//다음 프로젝트 개체
				$nextPrj = $(".dsh3000_prj[prjidx="+autoSelPrjIdx+"]");
			}
			else if(nextSelType == "grp" || nextSelType == "allGrp"){
				$nextPrj = $(".dsh3000_prj[prjidx="+autoSelPrjIdx+"] ~ .dsh3000_prj[prjtype=prjGrp]").eq(0);
			}
			else if(nextSelType == "pro"){
				$nextPrj = $(".dsh3000_prj[prjidx="+autoSelPrjIdx+"] ~ .dsh3000_prj[prjtype=prj]").eq(0);
			}
			
			//다음 개체 있는지 체크
			if(!gfnIsNull($nextPrj) && $nextPrj.length != 0){
				//다음 개체 선택
				$nextPrj.click();
			}else{
				var $nextNlPrj = $(".dsh3000_prj").eq(0);
				
				//개체 없는경우
				if(nextSelType == "grp" || nextSelType == "grpPro"){
					$nextNlPrj = $(".dsh3000_prj[prjtype=prjGrp]").eq(0);
				}
				else if(nextSelType == "pro"){
					$nextNlPrj = $(".dsh3000_prj[prjtype=prj]").eq(0);
				}

				if($nextNlPrj.length == 0){
					$(".dsh3000_prj").eq(0).click();
				}else{
					$nextNlPrj.click();	
				}
				
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
	var prjCnt = 1;
	dsh3000_selPrjId = "prjAll";
	//전체 그룹
	prjListStr += '<div class="dsh3000_prjGrpAllTitle dsh_title_box dsh3000_prj active" title="통합 데이터" prjidx="0" prjid="prjAll" prjtype="prjGrpAll" onclick="fnDsh3000PrjGrpAllSel(this)"><i class="fa fa-tv"></i>&nbsp;통합 데이터</div>';
	fnDash3000PrjGrpAllBoardSetting();
	
	$.each(prjList,function(idx, map){
		//프로젝트 그룹인경우
		if(gfnIsNull(map.prjGrpId)){
			var prjNm = gfnCutStrLen(map.prjNm,18);
			//프로젝트가 없는경우 skip
			if(map.leaf > 0){
				return true;
			}else{
				/* 
				var active = '';
				//첫번째 프로젝트 그룹 active
				if(idx == 0){
					dsh3000_selPrjId = map.prjId;
					active = " active";
					
					$("#selPrjGrpNm").html(gfnCutStrLen(map.prjNm,120));
					
					//프로젝트 그룹 데이터 불러오기
					fnDash3000PrjGrpBoardSetting(dsh3000_selPrjId);
				}
				 */
				prjListStr += '<div class="dsh3000_prjGrpTitle dsh_title_box dsh3000_prj" title="'+map.prjNm+'" prjidx="'+prjCnt+'" prjid="'+map.prjId+'" prjtype="prjGrp" onclick="fnDsh3000PrjGrpSel(this)">'
								+'<i class="fa fa-layer-group"></i>&nbsp;'
								+map.prjNm
							+'</div>';
							
				prjCnt++;
				
				//그룹 배열 데이터 만들기
				prjGrpListData[map.prjId] = [];
				
				//그룹 데이터 push
				prjGrpAllListData.push(map);
			}
		}else{
			var prjNm = gfnCutStrLen(map.prjNm,28);
			//프로젝트인경우
			
			var prjEndClass = '';
			//다음 데이터가 그룹인경우 마지막 프로젝트 class 추가
			if(idx != prjList.length){
				if(!gfnIsNull(prjList[idx+1]) && gfnIsNull(prjList[idx+1].prjGrpId)){
					prjEndClass = " dsh3000_prjTitleEnd";
				}
			}
			//마지막 idx인경우 class추가
			if(prjList.length-1 == idx){
				prjEndClass = " dsh3000_prjTitleEnd";
			}
			
			prjListStr += '<div class="dsh3000_prjTitle dsh3000_prj'+prjEndClass+'" prjid="'+map.prjId+'" prjidx="'+prjCnt+'" title="'+map.prjNm+'" prjtype="prj" onclick="fnDsh3000PrjSel(this)">'
								+'<i class="fa fa-angle-right"></i>&nbsp;'
								+prjNm
							+'</div>';
							
			//프로젝트 push
			prjGrpListData[map.prjGrpId].push(map);
			prjCnt++;
		}
		
	});
	
	//화면에 프로젝트 목록 Div 뿌리기
	$("#dsh3000_prjListMainDiv").html(prjListStr);
}

//프로젝트 선택 동작
function fnDsh3000PrjSel(thisObj){
	$("#dsh3000_prj_frameDiv").css({"display":"inline-block"});
	$("#dsh3000_prjGrp_frameDiv").hide();
	$("#dsh3000_prjGrpAll_frameDiv").hide();
	
	//이전 active 소멸 동작
	$(".dsh3000_prjGrpAllTitle.active").removeClass("active");
	$(".dsh3000_prjTitle.active .fa").removeClass("fa-angle-double-right").addClass("fa-angle-right");
	$(".dsh3000_prjTitle.active").removeClass("active");
	$(".dsh3000_prjGrpTitle.active").removeClass("active");
	
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
	$("#selPrjNm").attr("sel-prjid", dsh3000_selPrjId);
	
	//대시보드 정보 불러오기
	fnDash3000PrjBoardSetting(dsh3000_selPrjId);
}

//프로젝트 그룹 선택 동작
function fnDsh3000PrjGrpSel(thisObj){
	$("#dsh3000_prj_frameDiv").hide();
	$("#dsh3000_prjGrp_frameDiv").css({"display":"inline-block"});
	$("#dsh3000_prjGrpAll_frameDiv").hide();
	
	//class 제거
	$(".dsh3000_prjTitle.active .fa").removeClass("fa-angle-double-right").addClass("fa-angle-right");
	
	//이전 active 소멸 동작
	$(".dsh3000_prjGrpAllTitle.active").removeClass("active");
	$(".dsh3000_prjGrpTitle.active").removeClass("active");
	$(".dsh3000_prjTitle.active").removeClass("active");
	
	//active 대입
	$(thisObj).addClass("active");
	
	//대시보드 데이터 불러오기
	dsh3000_selPrjId = $(thisObj).attr("prjid");
	
	//자동갱신 인덱스
	autoSelPrjIdx = $(thisObj).attr("prjidx");
	
	//프로젝트명
	var prjGrpNm = $(thisObj).attr("title");
	
	$("#selPrjGrpNm").html(gfnCutStrLen(prjGrpNm,120));
	$("#selPrjGrpNm").attr("sel-grpid", dsh3000_selPrjId);
	
	//대시보드 정보 불러오기
	fnDash3000PrjGrpBoardSetting(dsh3000_selPrjId);
}

//전체 프로젝트 그룹 선택 동작
function fnDsh3000PrjGrpAllSel(thisObj){
	$("#dsh3000_prj_frameDiv").hide();
	$("#dsh3000_prjGrp_frameDiv").hide();
	$("#dsh3000_prjGrpAll_frameDiv").css({"display":"inline-block"});
	
	//class 제거
	$(".dsh3000_prjTitle.active .fa").removeClass("fa-angle-double-right").addClass("fa-angle-right");
	
	//이전 active 소멸 동작
	$(".dsh3000_prjGrpAllTitle.active").removeClass("active");
	$(".dsh3000_prjGrpTitle.active").removeClass("active");
	$(".dsh3000_prjTitle.active").removeClass("active");
	
	//active 대입
	$(thisObj).addClass("active");
	
	//대시보드 데이터 불러오기
	dsh3000_selPrjId = $(thisObj).attr("prjid");
	
	//자동갱신 인덱스
	autoSelPrjIdx = $(thisObj).attr("prjidx");
	
	//프로젝트명
	var prjGrpNm = $(thisObj).attr("title");
	
	$("#selPrjGrpNm").html(prjGrpNm);
	
	//대시보드 정보 불러오기
	fnDash3000PrjGrpAllBoardSetting();
}

//폴딩 버튼 동작
function fnTitleFoldingAction(thisObj){
	//폴더 타겟
	var $foldBtn = $(thisObj).children(".titleFoldingContent");
	
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
		$(thisObj).parent(".dsh_title_box").removeClass("titleFolded");
		$foldBtn.removeClass("down").addClass("up");
		
		//신호등 표시 보이기
		$(".dtmOverAlertCnt_desc[folding="+foldNum+"]").show();
	}else{
		$(thisObj).parent(".dsh_title_box").addClass("titleFolded");
		$foldBtn.removeClass("up").addClass("down");
		
		//신호등 표시 감추기
		$(".dtmOverAlertCnt_desc[folding="+foldNum+"]").hide();
	}
}

//전체 접기/펼치기 버튼
function fnAutoRefreshAction(thisObj){
	//폴더 타겟
	var $foldBtn = $(thisObj).children(".titleFoldingContent");
	
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
		$(".dtmOverAlertCnt_desc").show();
		
		//전체 열기
		$foldDiv.show();
	}else{
		$("div.titleFoldingBtn").parent(".dsh_title_box").addClass("titleFolded");
		$("div.titleFoldingBtn").children(".titleFoldingContent").removeClass("up").addClass("down");
		$foldBtn.removeClass("up").addClass("down");
		
		//신호등 표시 감추기
		$(".dtmOverAlertCnt_desc").hide();
		
		//전체 닫기
		$foldDiv.hide();
	}
}

//요구사항 갯수별 작업흐름 보이기 버튼 동작
function fnTitleAllViewBtn(thisObj){
	//프로세스 타겟
	var $allViewBtn = $(thisObj).children(".titleAllViewContent");
	
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
}

//대시보드 프로젝트 타입에 따라 데이터 로드
function fnDsh3000PrjTypeReload(){
	//전체 프로젝트 그룹
	if(dsh3000_selPrjId == "prjAll"){
		fnDash3000PrjGrpAllBoardSetting();
	}
	//프로젝트
	else if(dsh3000_selPrjId.indexOf("PRJ") != -1){
		fnDash3000PrjBoardSetting(dsh3000_selPrjId);
	}
	//그룹
	else{
		fnDash3000PrjGrpBoardSetting(dsh3000_selPrjId);
	}
}


//진척률 구하기 (do, done)
function fnDoneRatio(doCnt, doneCnt, fixed){
	
	var doneRatio = 0;
	var default_fixed = 2;
	
	//소수점 자리
	if(!gfnIsNull(fixed)){
		default_fixed = fixed;
	}
	
	//완료수가 0이상인경우만 진척률 계산 그외 0%
	if(!gfnIsNull(doneCnt) && doneCnt > 0){
		//1건 이상인경우
		doneRatio = (doneCnt/(doCnt+doneCnt))*100;
		doneRatio = doneRatio.toFixed(default_fixed);
	}
	
	return doneRatio;
}

//프로젝트별 차트 데이터 세팅
function fnPrjLayoutSetting(data){
	//차트 데이터
	var prjProcessReqCntList = data.prjProcessReqCntList;
	var monthPrjProcessReqCntList = data.monthPrjProcessReqCntList;
	
	//차트 데이터 프로젝트별 분리
	var prj_processReqCnt = [];
	var prj_monthProcessReqCnt = [];
	
	//프로젝트별 프로세스 데이터 분리
	$.each(prjProcessReqCntList, function(idx, map){
		//프로젝트 배열 만들기
		if(gfnIsNull(prj_processReqCnt[map.prjId])){
			prj_processReqCnt[map.prjId] = [];
		}
		
		//데이터 넣기
		prj_processReqCnt[map.prjId].push(map);
	});
	
	//프로젝트별 월별 데이터 분리
	$.each(monthPrjProcessReqCntList, function(idx, map){
		//프로젝트 배열 만들기
		if(gfnIsNull(prj_monthProcessReqCnt[map.prjId])){
			prj_monthProcessReqCnt[map.prjId] = [];
		}
		
		//데이터 넣기
		prj_monthProcessReqCnt[map.prjId].push(map);
	});
	
	var prjGrpSubList = prjGrpListData[dsh3000_selPrjId];
	
	$("#project_list_box").html('');
	//프로젝트 목록이 존재하는경우
	if(!gfnIsNull(prjGrpSubList)){
		$.each(prjGrpSubList,function(idx, map){
			
			//차트 데이터 둘다 0인경우 skip
			if(gfnIsNull(prj_processReqCnt[map.prjId]) && gfnIsNull(prj_monthProcessReqCnt[map.prjId])){
				return true;	
			}
			
			//프로젝트 html 추가 (append 이후 바로 차트 세팅)
			$("#project_list_box").append(
				'<div class="project_info_box" prjid="'+map.prjId+'">'
					+'<div class="dsh_title_box">'
						+'<div class="titleFoldingBtn" onclick="fnTitleFoldingAction(this)"><span class="titleFoldingContent up" folding="grp'+idx+'"></span></div>'
						+'<div class="titleRedoBtn"  redoid="project" prjid="'+map.prjId+'" onclick="fnSubDataLoad(this);"><li class="fa fa-redo"></li></div>'
						+map.prjNm
						+'<div class="dsh2000_dtmOverAlert" prjid="'+map.prjId+'">'
							+'<div class="dtmOverAlertCnt_desc" folding="grp'+idx+'">'
							+'	<div class="alertDesc">초과</div>'
							+'	<div class="alertDesc">임박</div>'
							+'	<div class="alertDesc">여유</div>'
							+'	<div class="alertDesc">실패</div>'
							+'	<div class="alertDesc">적기</div>'
							+'</div>'
							+'	<div class="dtmOverAlertCnt alert-red"  onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \'\', \'03\', \''+map.prjNm+' [초과]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-yellow" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \'\', \'02\', \''+map.prjNm+' [임박]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-green" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \'\', \'01\', \''+map.prjNm+' [여유]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-overRed" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \'\', \'04\', \''+map.prjNm+' [실패]\');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-blue" onclick="fnDsh3000PrjReqPopupOpen(\''+map.prjId+'\', \'\', \'05\', \''+map.prjNm+' [적기]\');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>'
							+'</div>'
					+'</div>'
					+'<div class="dshChartDiv foldDiv" folding="grp'+idx+'">'
					+'	<div class="dsh_top_sub_box dsh_top_left_box">'
					+'		<canvas id="reqTotalCntChart_'+map.prjId+'" width="350" height="278"></canvas>'
					+'	</div>'
					+'	<div class="dsh_top_sub_box dsh_top_middle_box">'
					+'		<canvas id="reqMonthCntChart_'+map.prjId+'" width="625" height="278"></canvas>'
					+'	</div>'
					+'	<div class="dsh_top_sub_box dsh_top_right_box">'
					+'		<canvas id="reqQuarterCntChart_'+map.prjId+'" width="400" height="278"></canvas>'
					+'	</div>'
					+'</div>'
				+'</div>');
			
			//차트 데이터 세팅
			var chartData = {"processReqCnt": prj_processReqCnt[map.prjId], "monthProcessReqCnt": prj_monthProcessReqCnt[map.prjId]};
			
			//차트 그리기
			fnPrjChartSetting(chartData, "reqTotalCntChart_"+map.prjId, "reqMonthCntChart_"+map.prjId, "reqQuarterCntChart_"+map.prjId, map.prjId);
		});
	}
}


//프로젝트 그룹별 차트 데이터 세팅
function fnPrjGrpLayoutSetting(data){
	//차트 데이터
	var prjGrpReqCntList = data.prjGrpReqCntList;
	var monthPrjGrpReqCntList = data.monthPrjGrpReqCntList;
	
	//차트 데이터 프로젝트별 분리
	var prjGrp_prjReqCnt = [];
	var prjGrp_monthPrjReqCnt = [];
	
	//프로젝트 그룹 데이터 분리
	$.each(prjGrpReqCntList, function(idx, map){
		//프로젝트 배열 만들기
		if(gfnIsNull(prjGrp_prjReqCnt[map.prjId])){
			prjGrp_prjReqCnt[map.prjId] = [];
		}
		//데이터 넣기
		prjGrp_prjReqCnt[map.prjId].push(map);
	});
	
	//프로젝트 그룹 월별 데이터 분리
	$.each(monthPrjGrpReqCntList, function(idx, map){
		//프로젝트 배열 만들기
		if(gfnIsNull(prjGrp_monthPrjReqCnt[map.prjGrpId])){
			prjGrp_monthPrjReqCnt[map.prjGrpId] = [];
		}
		//데이터 넣기
		prjGrp_monthPrjReqCnt[map.prjGrpId].push(map);
	});
	
	
	$("#projectGrp_list_box").html('');
	//프로젝트 목록이 존재하는경우
	if(!gfnIsNull(prjGrpAllListData)){
		$.each(prjGrpAllListData,function(idx, map){
			
			//차트 데이터 둘다 0인경우 skip
			if(gfnIsNull(prjGrp_prjReqCnt[map.prjId]) && gfnIsNull(prjGrp_monthPrjReqCnt[map.prjId])){
				return true;	
			}
			
			//프로젝트 html 추가 (append 이후 바로 차트 세팅)
			$("#projectGrp_list_box").append(
				'<div class="project_info_box" prjid="'+map.prjId+'">'
					+'<div class="dsh_title_box">'
						+'<div class="titleFoldingBtn" onclick="fnTitleFoldingAction(this)"><span class="titleFoldingContent up" folding="grpAll'+idx+'"></span></div>'
						+'<div class="titleRedoBtn"  redoid="prjGrpChart" prjid="'+map.prjId+'" onclick="fnSubDataLoad(this);"><li class="fa fa-redo"></li></div>'
						+'<i class="fa fa-layer-group"></i>&nbsp;'
						+map.prjNm
						+'<div class="dsh2000_dtmOverAlert" prjid="'+map.prjId+'">'
							+'<div class="dtmOverAlertCnt_desc" folding="grpAll'+idx+'">'
							+'	<div class="alertDesc">초과</div>'
							+'	<div class="alertDesc">임박</div>'
							+'	<div class="alertDesc">여유</div>'
							+'	<div class="alertDesc">실패</div>'
							+'	<div class="alertDesc">적기</div>'
							+'</div>'
							+'	<div class="dtmOverAlertCnt alert-red" onclick="fnDsh3000PrjGrpReqPopupOpen(\''+map.prjId+'\',\'03\', \''+map.prjNm+' [초과]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-yellow" onclick="fnDsh3000PrjGrpReqPopupOpen(\''+map.prjId+'\',\'02\', \''+map.prjNm+' [임박]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-green" onclick="fnDsh3000PrjGrpReqPopupOpen(\''+map.prjId+'\',\'01\', \''+map.prjNm+' [여유]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-overRed" onclick="fnDsh3000PrjGrpReqPopupOpen(\''+map.prjId+'\',\'04\', \''+map.prjNm+' [실패]\');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>'
							+'	<div class="dtmOverAlertCnt alert-blue" onclick="fnDsh3000PrjGrpReqPopupOpen(\''+map.prjId+'\',\'05\', \''+map.prjNm+' [적기]\');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>'
							+'</div>'
					+'</div>'
					+'<div class="dshChartDiv foldDiv" folding="grpAll'+idx+'">'
					+'	<div class="dsh_top_sub_box dsh_top_left_box">'
					+'		<canvas id="reqPrjTotalCntChart_'+map.prjId+'" width="350" height="278"></canvas>'
					+'	</div>'
					+'	<div class="dsh_top_sub_box dsh_top_middle_box">'
					+'		<canvas id="reqPrjMonthCntChart_'+map.prjId+'" width="625" height="278"></canvas>'
					+'	</div>'
					+'	<div class="dsh_top_sub_box dsh_top_right_box">'
					+'		<canvas id="reqPrjQuarterCntChart_'+map.prjId+'" width="400" height="278"></canvas>'
					+'	</div>'
					+'</div>'
				+'</div>');
			
			//차트 데이터 세팅
			var chartData = {"projectReqCnt": prjGrp_prjReqCnt[map.prjId], "monthPrjReqCnt": prjGrp_monthPrjReqCnt[map.prjId]};
			
			//차트 그리기
			fnPrjGrpChartSetting(chartData, "reqPrjTotalCntChart_"+map.prjId, "reqPrjMonthCntChart_"+map.prjId, "reqPrjQuarterCntChart_"+map.prjId, map.prjId);
		});
	}
}

/*
 * 통합대시보드 요구사항 목록 조회
 * @param projectId 요구사항 목록을 조회 할 프로젝트 ID
 * @param overType 초과, 임박, 여우, 실패, 적기
 * @param popTitleMsg 팝업 타이틀
 */
function fnDsh3000PrjGrpReqPopupOpen(projectId, overType, popTitleMsg){
	
	var data = {}
	// 왼쪽에서 프로젝트 그룹을 선택했을 때, 프로젝트 그룹의 신호등 클릭
	if(projectId == "projectGrp"){
		// 프로젝트 그룹 ID, 메시지 세팅
		var selGrpId = $("#selPrjGrpNm").attr("sel-grpid");
		var popMsg = $("#selPrjGrpNm").text() + " " + popTitleMsg
		data = {"callView" : "dsh3000", "projectId" : selGrpId, "overType" : overType ,"popTitleMsg" : popMsg};
	// 통합데이터의 신호등  클릭
	}else{
		data = {"callView" : "dsh3000", "projectId" : projectId, "overType" : overType ,"popTitleMsg" : popTitleMsg};
	}

	gfnLayerPopupOpen('/dsh/dsh3000/dsh3000/selectDsh3001View.do',data, "850", "550",'scroll');
}

/*
 * 통합대시보드 프로젝트,프로세스의 요구사항 목록 조회
 * @param projectId 요구사항 목록을 조회 할 프로젝트 ID
 * @param processId 프로세스 Id
 * @param overType 초과, 임박, 여우, 실패, 적기
 * @param popTitleMsg 팝업 타이틀
 */
function fnDsh3000PrjReqPopupOpen(projectId, processId, overType, popTitleMsg){

	var data = {}
	// 왼쪽에서 프로젝트를 선택했을 때, 오른쪽 화면 상단의 프로젝트 그룹의 신호등 클릭
	if(projectId == "project"){
		// 프로젝트 그룹 ID, 메시지 세팅
		var selprjId = $("#selPrjNm").attr("sel-prjid");
		var popMsg = $("#selPrjNm").text() + " " + popTitleMsg
		data = {"callView" : "dsh3000", "projectId" : selprjId, "processId" : processId, "overType" : overType ,"popTitleMsg" : popMsg};
	// 프로젝트 또는 프로세스 클릭
	}else{
		data = {"callView" : "dsh3000", "projectId" : projectId, "processId" : processId, "overType" : overType ,"popTitleMsg" : popTitleMsg};
	}
	
	gfnLayerPopupOpen('/dsh/dsh1000/dsh1000/selectDsh1001View.do',data, "850", "550",'scroll');
}


</script>
<div class="main_contents" style="width: 100%;padding: 30px;">
	<div class="contents_wrap">
		<div class="contents_title" style="width: 1750px;">
			통합 대시 보드
			<div class="dataAutoRefreshDiv dsh3000_autoRefreshDiv" style="margin-right: 90px;">
				<i class="fa fa-clock"></i>&nbsp;
				<span id="autoRefreshSpan">30분 00초</span> 후 다음 정보&nbsp;-
				<i class="fa fa-cogs autoRefresh_timeIcon"></i>&nbsp;
				<select class="autoRefresh_type" id="autoRefreshType" onchange="fnDsh3000LoopTypeReload()">
					<option value="all">전체</option>
					<option value="grp">그룹</option>
					<option value="pro">프로젝트</option>
					<option value="allGrp">전체+그룹</option>
					<option value="grpPro">그룹+프로젝트</option>
				</select>
				<select class="autoRefresh_select" id="timerVarSel" onchange="fnDsh3000PrjTypeReload()">
					<option value="0.5">30초</option>
					<option value="1">1분</option>
					<option value="5" selected>5분</option>
					<option value="10">10분</option>
					<option value="30">30분</option>
					<option value="60">1시간</option>
				</select>
				<div class="autoRefreshFoldingBtn" onclick="fnAutoRefreshAction(this)"><span class="titleFoldingContent up" folding="all"></span></div>
				<div class="autoRefreshEndBtn" onclick="fnAutoRefreshEnd();"><li class="fa fa-times"></li></div>
				<div class="autoRefreshRedoBtn" onclick="fnDsh3000PrjTypeReload();"><li class="fa fa-redo"></li></div>
			</div>
		</div>
		<div class="dsh3000_leftDiv" id="dsh3000_prjListMainDiv">
		</div>
		<div class="dsh3000_rightDiv">
			<div id="dsh3000_prj_frameDiv" class="dsh3000_frameDiv">
				<div class="dsh_top_box">
					<div class="dsh_title_box">
						<div class="titleFoldingBtn" onclick="fnTitleFoldingAction(this)"><span class="titleFoldingContent up" folding="prj"></span></div>
						<div class="titleRedoBtn" redoid="project" onclick="fnSubDataLoad(this);"><li class="fa fa-redo"></li></div>
						<span id="selPrjNm"></span>
						<div class="dsh2000_dtmOverAlert" processid="project">
							<div class="dtmOverAlertCnt_desc" folding="prj">
								<div class="alertDesc">초과</div>
								<div class="alertDesc">임박</div>
								<div class="alertDesc">여유</div>
								<div class="alertDesc">실패</div>
								<div class="alertDesc">적기</div>
							</div>
							<div class="dtmOverAlertCnt alert-red" onclick="fnDsh3000PrjReqPopupOpen('project', '', '03','[초과]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-yellow" onclick="fnDsh3000PrjReqPopupOpen('project', '', '02','[임박]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-green" onclick="fnDsh3000PrjReqPopupOpen('project', '', '01','[여유]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-overRed" onclick="fnDsh3000PrjReqPopupOpen('project', '', '04','[실패]');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-blue" onclick="fnDsh3000PrjReqPopupOpen('project',  '', '05','[적기]');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>
						</div>
					</div>
					<div class="dshChartDiv foldDiv" folding="prj">
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
						<div class="titleFoldingBtn" onclick="fnTitleFoldingAction(this)"><span class="titleFoldingContent up" folding="request"></span></div>
						<div class="titleRedoBtn" redoid="request" onclick="fnSubDataLoad(this);"><li class="fa fa-redo"></li></div>
						접수 대기 [총 <span id="requestTotalCnt">0</span>건]
					</div>
					<div class="foldDiv" folding="request">
						<div data-ax5grid="flowGrid-request" data-ax5grid-config="{}" style="height: 250px;"></div>
					</div>
				</div>
				<div class="process_list_box" id="process_list_box">
					<!-- 프로세스 목록 세팅 영역 -->
				</div>
			</div>
			<div id="dsh3000_prjGrp_frameDiv" class="dsh3000_frameDiv">
				<div class="dsh_top_box">
					<div class="dsh_title_box">
						<div class="titleFoldingBtn" onclick="fnTitleFoldingAction(this)"><span class="titleFoldingContent up" folding="prjGrp"></span></div>
						<div class="titleRedoBtn" redoid="prjGrpChart" onclick="fnSubDataLoad(this);"><li class="fa fa-redo"></li></div>
						<i class="fa fa-layer-group"></i>&nbsp;<span id="selPrjGrpNm"></span>
						<div class="dsh2000_dtmOverAlert" prjid="projectGrp">
							<div class="dtmOverAlertCnt_desc" folding="prjGrp">
								<div class="alertDesc">초과</div>
								<div class="alertDesc">임박</div>
								<div class="alertDesc">여유</div>
								<div class="alertDesc">실패</div>
								<div class="alertDesc">적기</div>
							</div>
							<div class="dtmOverAlertCnt alert-red" onclick="fnDsh3000PrjGrpReqPopupOpen('projectGrp','03','[초과]');"  title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-yellow"  onclick="fnDsh3000PrjGrpReqPopupOpen('projectGrp','02','[임박]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-green"  onclick="fnDsh3000PrjGrpReqPopupOpen('projectGrp','01','[여유]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-overRed"  onclick="fnDsh3000PrjGrpReqPopupOpen('projectGrp','04','[실패]');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-blue"  onclick="fnDsh3000PrjGrpReqPopupOpen('projectGrp','05','[적기]');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>
						</div>
					</div>
					<div class="dshChartDiv foldDiv" folding="prjGrp">
						<div class="dsh_top_sub_box dsh_top_left_box">
							<canvas id="reqPrjTotalCntChart" width="350" height="278"></canvas>
						</div>
						<div class="dsh_prjGrp_top_sub_box dsh_top_middle_box">
							<canvas id="reqPrjMonthCntChart" width="625" height="278"></canvas>
						</div>
						<div class="dsh_prjGrp_top_sub_box dsh_top_right_box">
							<canvas id="reqPrjQuarterCntChart" width="400" height="278"></canvas>
						</div>
					</div>
				</div>
				<div class="project_list_box" id="project_list_box">
					<!-- 프로젝트 차트 목록 세팅 영역 -->
				</div>
			</div>
			<div id="dsh3000_prjGrpAll_frameDiv" class="dsh3000_frameDiv">
				<div class="dsh_top_box">
					<div class="dsh_title_box">
						<div class="titleFoldingBtn" onclick="fnTitleFoldingAction(this)"><span class="titleFoldingContent up" folding="prjGrpAll"></span></div>
						<div class="titleRedoBtn" redoid="prjGrpAllChart" onclick="fnSubDataLoad(this);"><li class="fa fa-redo"></li></div>
						<i class="fa fa-tv"></i>&nbsp;통합 데이터
						<div class="dsh2000_dtmOverAlert" prjid="projectGrpAll">
							<div class="dtmOverAlertCnt_desc" folding="prjGrpAll">
								<div class="alertDesc">초과</div>
								<div class="alertDesc">임박</div>
								<div class="alertDesc">여유</div>
								<div class="alertDesc">실패</div>
								<div class="alertDesc">적기</div>
							</div>
							<div class="dtmOverAlertCnt alert-red" onclick="fnDsh3000PrjGrpReqPopupOpen('all','03','통합 데이터 [초과]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-yellow" onclick="fnDsh3000PrjGrpReqPopupOpen('all','02','통합 데이터 [임박]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-green" onclick="fnDsh3000PrjGrpReqPopupOpen('all','01','통합 데이터 [여유]');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-overRed" onclick="fnDsh3000PrjGrpReqPopupOpen('all','04','통합 데이터 [실패]');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>
							<div class="dtmOverAlertCnt alert-blue" onclick="fnDsh3000PrjGrpReqPopupOpen('all','05','통합 데이터 [적기]');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>
						</div>
					</div>
					<div class="dshChartDiv foldDiv" folding="prjGrpAll">
						<div class="dsh_top_sub_box dsh_top_left_box">
							<canvas id="reqPrjGrpTotalCntChart" width="350" height="278"></canvas>
						</div>
						<div class="dsh_prjGrp_top_sub_box dsh_top_middle_box">
							<canvas id="reqPrjGrpMonthCntChart" width="625" height="278"></canvas>
						</div>
						<div class="dsh_prjGrp_top_sub_box dsh_top_right_box">
							<canvas id="reqPrjGrpQuarterCntChart" width="400" height="278"></canvas>
						</div>
					</div>
				</div>
				<div class="projectGrp_list_box" id="projectGrp_list_box">
					<!-- 프로젝트 그룹 차트 목록 세팅 영역 -->
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />