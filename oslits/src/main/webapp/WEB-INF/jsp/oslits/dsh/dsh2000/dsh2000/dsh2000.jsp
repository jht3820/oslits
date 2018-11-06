<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslits/top/aside.jsp" />
<script type="text/javascript" src="<c:url value='/js/chart/chartJs/Chart.bundle.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/css/oslits/dsh.css'/>" type='text/css'>
<script>
//그리드 객체 가지고있는 변수
var gridObj = {};

//자동 갱신 selected 처리
var timerVarSel;
var secondTime;
var timer;

//사용자 Id, 권한 타입
var usrId = "${sessionScope.loginVO.usrId}";
var usrTyp = "${sessionScope.usrTyp}";

//프로젝트 기간
var prjStartDt;
var prjEndDt;


//결재 그리드 설정 값
var signGrid_config = {
	showLineNumber: true,
	sortable:true,
	header: {align:"center"},
	columns: [
		{key: "signCdNm", label: "결재 상태", width: 80, align: "center"},
		{key: "signFlowNm", label: "작업흐름", width: 80, align: "center"},
		{key: "signUsrNm", label: "결재자", width: 80, align: "center"},
		{key: "signDtm", label: "결재 요청 일자", width: 130, align: "center",formatter:function(){
			return new Date(this.item.signDtm).format("yyyy-MM-dd");
		}},
		{key: "regUsrNm", label: "요청자", width: 80, align: "center"},
		{key: "signRejectCmnt", label: "반려내용", width: 200, align: "center"},
		],
	body: {
		align: "center",
		columnHeight: 30,
		onDBLClick:function(){
			//권한 체크해서 사용자인경우 상세보기로 전환
			if(usrTyp == "01"){
				var data = {"mode":"newReq","reqId": this.item.reqId, "reqProType":"02"}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}else{
				var data = {"reqId": this.item.reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1330', '900','scroll',false);
			}
		}
	},
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {'arrow': '<i class="fa fa-caret-right"></i>'},
		items: [
			{type: "detailPopup", label: "상세 정보", icon:"<i class='fa fa-info-circle' aria-hidden='true'></i>"},
		],
		popupFilter: function (item, param) {
			var selItem = param.item;
			//선택 개체 없는 경우 중지
			if(typeof selItem == "undefined"){
				return false;
			}
			return true;
		},
		onClick: function (item, param) {
			var selItem = param.item;

			//상세 정보
			if(item.type == "detailPopup"){
				var data = {"mode":"newReq","reqId": param.item.reqId, "reqProType":"02"}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}
			param.gridSelf.contextMenu.close();
		}
	}
};

//작업 그리드 설정 값
var workGrid_config = {
	showLineNumber: true,
	sortable:true,
	frozenColumnIndex: 1,
	header: {align:"center"},
	columns: [
		{key: "workStatusNm", label: "작업상태", width: 80, align: "center"},
		{key: "flowNm", label: "작업흐름", width: 80, align: "center"},
		{key: "workChargerNm", label: "담당자", width: 80, align: "center"},
		{key: "workAdmContent", label: "작업 지시내용", width: 370, align: "left"},
		{key: "workAdmStDtm", label: "작업 시작예정일자", width: 140, align: "center"},
		{key: "workAdmEdDtm", label: "작업 종료예정일자", width: 140, align: "center"},
		{key: "workContent", label: "작업 내용", width: 390, align: "left"},
		{key: "workStDtm", label: "작업 시작일자", width: 140, align: "center"},
		{key: "workEdDtm", label: "작업 종료일자", width: 140, align: "center"},
		],
	body: {
		align: "center",
		columnHeight: 30,
		onDBLClick:function(){
			//권한 체크해서 사용자인경우 상세보기로 전환
			if(usrTyp == "01"){
				var data = {"mode":"newReq","reqId": this.item.reqId, "reqProType":"02"}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}else{
				var data = {"reqId": this.item.reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1330', '900','scroll',false);
			}
		}
	},
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {'arrow': '<i class="fa fa-caret-right"></i>'},
		items: [
			{type: "detailPopup", label: "상세 정보", icon:"<i class='fa fa-info-circle' aria-hidden='true'></i>"},
		],
		popupFilter: function (item, param) {
			var selItem = param.item;
			//선택 개체 없는 경우 중지
			if(typeof selItem == "undefined"){
				return false;
			}
			return true;
		},
		onClick: function (item, param) {
			var selItem = param.item;

			//상세 정보
			if(item.type == "detailPopup"){
				var data = {"mode":"newReq","reqId": param.item.reqId, "reqProType":param.item.reqProType}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}
			param.gridSelf.contextMenu.close();
		}
	}
};

//접수 대기 그리드 설정 값
var newReqGrid_config = {
	showLineNumber: true,
	sortable:true,
	header: {align:"center"},
	columns: [
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
			//권한 체크해서 사용자인경우 상세보기로 전환
			if(usrTyp == "01"){
				/*
               	 * reqPageType 추가
               	 * 요구사항 상세보기(req1002.jsp)에서 항목명 구분을 위해 사용
               	 * usrReqPage - 요구사항 요청(사용자) 
               	 * admReqPage - 전체 요구사항 목록, 요구사항 생성관리(관리자)
               	 */
               	var data = {
               			"mode": "req", 
               			"reqId": this.item.reqId,
               			"reqProType": this.item.reqProType,
               			"reqPageType" : "usrReqPage"
               	}; 
               	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '845','scroll');
			}else{
				var data = {"reqId": this.item.reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4106View.do", data, '880', '890','scroll');
			}
		}
	},
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {'arrow': '<i class="fa fa-caret-right"></i>'},
		items: [
			{type: "detailPopup", label: "상세 정보", icon:"<i class='fa fa-info-circle' aria-hidden='true'></i>"},
			{type: "reply", label: "쪽지 전송", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
		],
		popupFilter: function (item, param) {
			var selItem = param.item;
			//선택 개체 없는 경우 중지
			if(typeof selItem == "undefined"){
				return false;
			}
			return true;
		},
		onClick: function (item, param) {
			var selItem = param.item;
			
			//쪽지 전송
			if(item.type == "reply"){
				gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
			}
			//상세 정보
			else if(item.type == "detailPopup"){
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
               	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '845','scroll');
			}
			param.gridSelf.contextMenu.close();
		}
	}
};

//계획대비 미처리건수 그리드 세팅
var reqDtmOverGrid_config = {
	showLineNumber: true,
	sortable:true,
	header: {align:"center"},
	columns: [
	    {key: "processNm", label: "구분", width: 120, align: "center"},
		{key: "reqNm", label: "요청 제목", width: 250, align: "left"},
		{key: "reqDesc", label: "요청 내용", width: 350, align: "left"},
		{key: "calcReqDay", label: "초과 일수", width: 120, align: "left",formatter:function(){
			return this.item.calcReqDay+"일";
		}},
		{key: "reqChargerNm", label: "담당자", width: 80, align: "center"},
		{key: "regDtm", label: "생성일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.regDtm).format("yyyy-MM-dd");
		}},
		{key: "reqStDtm", label: "작업 시작일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.reqStDtm).format("yyyy-MM-dd");
		}},
		{key: "reqEdDtm", label: "작업 종료일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.reqEdDtm).format("yyyy-MM-dd");
		}},
		{key: "reqEdDuDtm", label: "작업 시작 예정일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.reqEdDuDtm).format("yyyy-MM-dd");
		}},
		{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.reqEdDuDtm).format("yyyy-MM-dd");
		}}
	],
	body: {
		align: "center",
		columnHeight: 30,
		onDBLClick:function(){
			//권한 체크해서 사용자인경우 상세보기로 전환
			if(usrTyp == "01"){
				var data = {"mode":"newReq","reqId": this.item.reqId, "reqProType":"02"}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}else{
				var data = {"reqId": this.item.reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1300', '900','scroll',false);
			}
		}
	},
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {'arrow': '<i class="fa fa-caret-right"></i>'},
		items: [
			{type: "detailPopup", label: "상세 정보", icon:"<i class='fa fa-info-circle' aria-hidden='true'></i>"},
			{type: "reply", label: "쪽지 전송", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
		],
		popupFilter: function (item, param) {
			var selItem = param.item;
			//선택 개체 없는 경우 중지
			if(typeof selItem == "undefined"){
  				return false;
 			}
				return true;
			},
		onClick: function (item, param) {
			var selItem = param.item;

			//쪽지 전송
			if(item.type == "reply"){
				gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
			}
			//상세 정보
			else if(item.type == "detailPopup"){
				var data = {"mode":"newReq","reqId": param.item.reqId, "reqProType":param.item.reqProType}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}
			//메뉴 닫기
			param.gridSelf.contextMenu.close();
		}
	}
};

//산출물 미제출 건
var docDtmOverGrid_config = {
	showLineNumber: true,
	sortable:true,
	header: {align:"center"},
	columns: [
		//{key: "upupDocNm", label: "상위 산출물 명", width: 400, align: "left"},
		{key: "upDocNm", label: "상위 산출물 명", width: 360, align: "left"},
		{key: "docNm", label: "산출물 명", width: 380, align: "left"},
		{key: "docDesc", label: "산출물 설명", width: 350, align: "left"},
		{key: "docCalcEdDay", label: "초과 일수", width: 120, align: "left",formatter:function(){
			return this.item.docCalcEdDay+"일";
		}},
		{key: "docEdDtm", label: "마감 일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.docEdDtm).format("yyyy-MM-dd");
		}}
	],
};


//그리드 공통 설정 값
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
		{key: "reqEdDtm", label: "작업 종료일자", width: 140, align: "center"},
		{key: "reqStDuDtm", label: "작업 시작 예정일자", width: 140, align: "center"},
		{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: 140, align: "center"},
	],
	body: {
		align: "center",
		columnHeight: 30,
		onDBLClick:function(){
			//권한 체크해서 사용자인경우 상세보기로 전환
			if(usrTyp == "01"){
				var data = {"mode":"newReq","reqId": this.item.reqId, "reqProType":"02"}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}else{
				var data = {"reqId": this.item.reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1300', '900','scroll',false);
			}
		}
	},
	contextMenu: {
		iconWidth: 20,
		acceleratorWidth: 100,
		itemClickAndClose: false,
		icons: {'arrow': '<i class="fa fa-caret-right"></i>'},
		items: [
			{type: "detailPopup", label: "상세 정보", icon:"<i class='fa fa-info-circle' aria-hidden='true'></i>"},
			{type: "reply", label: "쪽지 전송", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
		],
		popupFilter: function (item, param) {
			var selItem = param.item;
			//선택 개체 없는 경우 중지
			if(typeof selItem == "undefined"){
  				return false;
 			}
				return true;
			},
		onClick: function (item, param) {
			var selItem = param.item;

			//쪽지 전송
			if(item.type == "reply"){
				gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
			}
			//상세 정보
			else if(item.type == "detailPopup"){
				var data = {"mode":"newReq","reqId": param.item.reqId, "reqProType":param.item.reqProType}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}
			//메뉴 닫기
			param.gridSelf.contextMenu.close();
		}
	}
};

//페이지 로드시 실행
$(document).ready(function(){
	//대시보드 데이터 세팅
	fnDashBoardSetting();
	
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
							//글씨 색상
							var fontColor = dataset.fontColor;
							
							if(gfnIsNull(fontColor)){
								fontColor = 'rgb(255, 255, 255)';
							}
							//글씨 스타일
							ctx.fillStyle = fontColor;

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
							var chartX = position.x;
							//Top값
							var chartY = position.y;
							
							//세로축 표시
							if(dataset.valueShow == "barY"){
								chartY = position.y-((position.y-element._model.base)/2);
							}
							//가로축 표시
							else if(dataset.valueShow == "barX"){
								chartX = position.x-((position.x-element._model.base)/2);
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

//대시보드 데이터 세팅
function fnDashBoardSetting(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
		{"url":"<c:url value='/dsh/dsh2000/dsh2000/selectDsh2000DashBoardDataAjax.do'/>","loadingShow":true});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		//자동 새로고침 타이머 세팅
		timerVarSel = $('#timerVarSel').val();
   		secondTime = (timerVarSel*60);
   		clearInterval(timer);
   		timer = setInterval('printTime()',1000);
   		
		//프로젝트 정보 세팅
		fnProjectInfoSetting(data.prjInfo);
		
		//계획대비 미처리건수
		fnReqDtmOverListSetting(data.reqDtmOverList);
		
		//산출물 미제출건
		fnDocDtmOverListSetting(data.docDtmOverList);
		
		//프로세스 목록 세팅
		fnProcessListSetting(data.processList);
		
		//프로세스별 기간 경고 조회
		fnReqDtmOverAlertListSetting(data.reqDtmOverAlertList);
		
		//작업흐름 목록 세팅
		fnFlowListSetting(data.flowList);
		
		//접수 대기 그리드 세팅
		fnFlowGridSetting("request");
		
		//작업 그리드 세팅
		fnFlowGridSetting("work");
		
		//결재 그리드 세팅
		fnFlowGridSetting("sign");
			
		//차트 세팅
		fnChartSetting(data);
		
		//접수 대기 요구사항 목록
		var newReqList = data.newReqList;
		
		if(!gfnIsNull(data.newReqList)){
			//접수대기 요구사항 건수 표시
			$("#requestTotalCnt").html(data.newReqList.length);
		}else{
			$("#requestTotalCnt").html(0);
		}
		
		//요구사항 목록 세팅
		if(!gfnIsNull(newReqList)){
			gridObj["request"].setData(newReqList);
		}
		
		//작업 목록
		var workList = data.workList;
		
		//담당 작업 목록 세팅
		if(!gfnIsNull(workList)){
			gridObj["work"].setData(workList);
		}
		
		//결재 목록
		var signList = data.signList;
		
		//담당 작업 목록 세팅
		if(!gfnIsNull(signList)){
			gridObj["sign"].setData(signList.reverse());
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

//프로젝트 정보 세팅
function fnProjectInfoSetting(prjInfo){
	//프로젝트명
	$("span#prjNm").html(gfnCutStrLen(prjInfo.prjNm,85));
	
	//진행중 요구사항 수
	$("span#prjReqDo").html(prjInfo.prjDoReqCnt);
	
	//완료건 요구사항 수
	$("span#prjReqDone").html(prjInfo.prjDoneReqCnt);
	
	//진척률 구하기
	var prjReqDoneRatio = fnDoneRatio(prjInfo.prjDoReqCnt, prjInfo.prjDoneReqCnt);
	
	$("span#prjReqPer").html(prjReqDoneRatio);
	
	//프로젝트 기간
	prjStartDt = prjInfo.startDt;
	prjEndDt = prjInfo.endDt;
	
	$("div.dsh2000_prjDateRangeInfo").html("("+prjStartDt+" ~ "+prjEndDt+")");
}

//계획대비 미처리건수
function fnReqDtmOverListSetting(reqDtmOverList){
	//차트 배경색
	var chart2_bgColor = ["#4b73eb","#936de9","#ff5643","#58c3e5","#fba450","#eb4ba4","#89eb4b","#c4eb4b","#9f4beb","#fba450","#ff5643","#58c3e5","#fba450"];
	
	var reqDtmOverCnt = 0;
	var reqDtmOverLabels = [];
	var reqDtmOverDatasets = [];
	
	$.each(reqDtmOverList, function(idx, map){
		//다음 정보 있는경우 프로세스 비교
		if((!gfnIsNull(reqDtmOverList[idx+1]) && reqDtmOverList[idx+1].processId != map.processId) || reqDtmOverList.length == (idx+1)){
			//차트 데이터 대입
			reqDtmOverLabels.push(map.processNm);
			reqDtmOverDatasets.push(reqDtmOverCnt);
			
			//요구사항 수 초기화
			reqDtmOverCnt = 0;
		}
		
		//프로세스별 요구사항 수 카운트
		reqDtmOverCnt++;
	});
	
	//차트 표시
	var ctx = document.getElementById("reqDtmOverChart").getContext('2d');
	new Chart(ctx, {
		    type: 'bar',
		    data: {
		        labels: reqDtmOverLabels,
		        datasets: [{
		        	label:"미처리 건수",
		            data: reqDtmOverDatasets,
		            backgroundColor: chart2_bgColor,
		            borderWidth: 0,
		            valueShow:false,
			        valueShow: 'barY'
				}]
		    },
		    options: {
					responsive: true,
					title: {display: false},
					legend: {labels: {usePointStyle: false}}
				}
		});
	//그리드 표시
	reqDtmOverGrid = new ax5.ui.grid();
	reqDtmOverGrid_config.target = $('[data-ax5grid="reqDtmOverGrid"]');
	
	//그리드 프레임 호출
	reqDtmOverGrid.setConfig(reqDtmOverGrid_config);
	
	//그리드 데이터 넣기
	reqDtmOverGrid.setData(reqDtmOverList);
	
	//상단 타이틀바 건수 표시
	$("span#reqDtmOverDo").html(reqDtmOverList.length);
	
	//요구사항 총 건수
	var reqTotalCnt = parseInt($("span#prjReqDone").text())+parseInt($("span#prjReqDo").text());
	$("span#reqDtmOverDone").html(reqTotalCnt);
	
	//완료율 구하기
	var reqDtmOverDoneRatio = fnDoneRatio(reqDtmOverList.length,(reqTotalCnt-reqDtmOverList.length));
	$("span#reqDtmOverPer").html(reqDtmOverDoneRatio);
	
}

//산출물 미제출 건
function fnDocDtmOverListSetting(docDtmOverList){
	//그리드 표시
	docDtmOverGrid = new ax5.ui.grid();
	docDtmOverGrid_config.target = $('[data-ax5grid="docDtmOverGrid"]');
	
	//그리드 프레임 호출
	docDtmOverGrid.setConfig(docDtmOverGrid_config);

	//미제출 산출물건이 있는경우
	if(!gfnIsNull(docDtmOverList)){
		//그리드 데이터 넣기
		docDtmOverGrid.setData(docDtmOverList);
		
		//상단 타이틀바 건수 표시
		$("span#docDtmOverDo").html(docDtmOverList.length);
	
		//요구사항 총 건수
		var docTotalCnt = docDtmOverList[0].docTotalCnt;
		$("span#docDtmOverDone").html(docTotalCnt);
		
		//완료율 구하기
		var docDtmOverDoneRatio = fnDoneRatio(docDtmOverList.length,(docTotalCnt-docDtmOverList.length));
		$("span#docDtmOverPer").html(docDtmOverDoneRatio);
		
		//미등록
		var docNullCnt = docDtmOverList[0].docNullCnt;
		$("span#docDtmOverNull").html(docNullCnt);
		
	}else{
		//없는경우 산출물 폴딩
		$("span.titleFoldingContent[folding=docDtmOver]").click();
	}
}

//프로세스 목록 세팅
function fnProcessListSetting(processList){
	//프로세스 내용 초기화
	$("#process_list_box").html('');
		
	//프로세스 loop
	$.each(processList,function(idx, map){
		var foldingNum = (idx+2);
		
		//진척률
		var proDoneReqRatio = fnDoneRatio(map.proDoReqCnt, map.proDoneReqCnt);
		
		//프로세스 html 추가 (append 이후 바로 그리드 세팅)
		$("#process_list_box").append(
			'<div class="process_info_box" processid="'+map.processId+'" totalReq='+(map.proTotalReqCnt)+'>'
				+'<div class="dsh_title_box">'
					+map.processNm
					+'<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="'+foldingNum+'"></span></div>'
					+'<div class="titleRedoBtn"  redoid="process" processid="'+map.processId+'" onclick="fnSubDataLoad(\'obj\',this);"><li class="fa fa-redo"></li></div>'
					+'<div class="titleAllViewBtn"><span class="titleAllViewContent on" processid="'+map.processId+'"></span></div>'
					+' [ 진행 : <span class="doReqCnt">'+map.proDoReqCnt+'</span>건 / 완료 : <span class="doneReqCnt">'+map.proDoneReqCnt+'</span>건 / 진척률 : <span class="doneReqRatio">'+proDoneReqRatio+'</span>%]'
						+'<div class="dsh2000_dtmOverAlert" processid="'+map.processId+'">'
						+'<div class="dtmOverAlertCnt_desc" folding="'+foldingNum+'">'
						+'	<div class="alertDesc">초과</div>'
						+'	<div class="alertDesc">임박</div>'
						+'	<div class="alertDesc">여유</div>'
						+'	<div class="alertDesc">실패</div>'
						+'	<div class="alertDesc">적기</div>'
						+'</div>'
						+'	<div class="dtmOverAlertCnt alert-red" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-yellow" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-green" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-overRed" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-blue" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>'
						+'</div>'
					+'</div>'
				+'<div class="foldDiv" folding="'+foldingNum+'">'
					+'<div class="process_flow_box">'
					+'</div>'
					+'<div class="process_flow_chart_box">'
						+'<canvas id="PRO_'+map.processId+'" width="1370" height="60"></canvas>'
					+'</div>'
					+'<div class="process_req_box" folding="'+foldingNum+'" style="display:none;">'
						+'<div>'
							+'	<div data-ax5grid="flowGrid-'+map.processId+'" data-ax5grid-config="{}" style="height: 250px;"></div>'
						+'</div>'
					+'</div>'
				+'</div>'
			+'</div>');
	});
	
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
			$(".dtmOverAlertCnt_desc[folding="+foldNum+"]").show();
		}else{
			$(this).parent(".dsh_title_box").addClass("titleFolded");
			$foldBtn.removeClass("up").addClass("down");
			
			//신호등 표시 감추기
			$(".dtmOverAlertCnt_desc[folding="+foldNum+"]").hide();
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
	});
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
		var processTotalReqCnt = $(".process_info_box[processid="+map.processId+"]").attr("totalreq");
		
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
				+'<span onclick="fnFlowReqList(\''+map.processId+'\',\''+map.flowId+'\',\'charge\')">'+map.reqChargerCnt+'</span>'
				+' / '
				+'<span onclick="fnFlowReqList(\''+map.processId+'\',\''+map.flowId+'\',\'all\')">'+map.reqTotalCnt+'</span>'
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
			chartDataset = [];
		}
		
		processFlwCnt++;
	});
	
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

//실시간 시간 구하기
function printTime() {
	timerStr = Math.floor(secondTime/60) + "분 " + (secondTime%60) + "초";
	$('#autoRefreshSpan').html(timerStr);
	secondTime--;
	
	if (secondTime < 0) {
		clearInterval(timer);
		fnDashBoardSetting();
	}
}

//자동 새로고침 끄기
function fnAutoRefreshEnd(){
	clearInterval(timer);
	$('#autoRefreshSpan').html("<i class='fa fa-infinity'></i>");
}

//차트 세팅
function fnChartSetting(data){
	//[차트1] 프로세스별 총개수 + 최종 완료 개수
	var processReqCnt = data.processReqCnt;
	
	//차트2
	var monthProcessReqCnt = data.monthProcessReqCnt;
	
	//차트1 있을때만 데이터 세팅
	if(!gfnIsNull(processReqCnt)){
		//차트1 - 데이터 세팅
		var chart_processNm = [];
		var chart_reqAllCnt = [];
		var chart_reqEndCnt = [];
		var chart_reqChargerCnt = [];
		
		//차트1 데이터 분기
		$.each(processReqCnt,function(idx, map){
			//프로세스 명
			chart_processNm.push(map.processNm);
			
			//요구사항 총 개수
			chart_reqAllCnt.push(map.allCnt);
			
			//담당 개수
			chart_reqChargerCnt.push(map.chargerCnt);
			
			
			//접수 대기의 경우 총개수와 최종 종료개수 맞춤
			if(map.processId == "request"){
				//요구사항 최종 종료 개수
				chart_reqEndCnt.push(map.allCnt);
			}else{
				//요구사항 최종 종료 개수
				chart_reqEndCnt.push(map.endCnt);	
			}
		});
		
		var ctx1 = document.getElementById("reqTotalCntChart").getContext('2d');
		
		new Chart(ctx1, {
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
	           	 valueShow:false
		        },{
		        	type:'line',
		            label: '담당 요구사항 수',
		            data: chart_reqChargerCnt,
		            backgroundColor: 'rgb(255, 217, 123)',
		            borderColor: 'rgba(255, 206, 86, 1)',
		            borderWidth: 2,
		            pointStyle: 'circle',
		            fill: false,
		            pointRadius: 4,
	            	valueShow:false
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
					legend: false//{labels: {usePointStyle: true}}
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
		var chart2_y_chargerCnt = {};	//월별 담당 요구사항 개수
		var chart2_datasets = [];	//차트 데이터 세팅
		var chart2_bgColor = ["#4b73eb","#936de9","#ff5643","#58c3e5","#fba450","#eb4ba4","#89eb4b","#c4eb4b","#9f4beb","#fba450","#ff5643","#58c3e5","#fba450"];	//차트 배경색
		
		//차트3 준비 데이터
		
		var chart3_idxIf = {"01월":1,"02월":1,"03월":1,"04월":2,"05월":2,"06월":2,"07월":3,"08월":3,"09월":3,"10월":4,"11월":4,"12월":4};
		var chart3_quarter = [0,0,0,0,0];
		var chart3_chargerQuarter = [0,0,0,0,0];
		
		var ctx2 = document.getElementById("reqMonthCntChart").getContext('2d');
		
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
				chart2_y_chargerCnt[reqEdDtmMm] = {};
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
				chart2_y_chargerCnt[reqEdDtmMm][map.processId] = map.reqChargerCnt;
			}
		});

		//담당자 데이터 배열
		var chargerDataArr = [];
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
				//월 데이터 없는경우
				if(gfnIsNull(chart2_y_chargerCnt[inMap])){
					chargerDataArr[inIdx] = 0;
				}else{
					//월별 프로세스 담당 데이터 체크
					if(!gfnIsNull(chart2_y_chargerCnt[inMap][map])){
						//데이터 없는경우 초기값 0
						if(gfnIsNull(chargerDataArr[inIdx])){
							chargerDataArr[inIdx] = 0;
						}
						//데이터 있는경우 모두 더하기
						chargerDataArr[inIdx] = chargerDataArr[inIdx]+chart2_y_chargerCnt[inMap][map];
						
						//차트3 - 담당자
						chart3_chargerQuarter[chart3_idxIf[inMap]] += chart2_y_chargerCnt[inMap][map];
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
		/* 
		//담당 차트 추가
		chart2_datasets.unshift({
				type:'line',
	            label: "담당 요구사항 수",
	            data: chargerDataArr,
	            backgroundColor: 'rgb(255, 217, 123)',
	            borderColor: 'rgba(255, 206, 86, 1)',
	            borderWidth: 1,
	            pointStyle: 'circle',
	            fill: false,
	            pointRadius: 2
			});
		 */
		new Chart(ctx2, {
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
		var ctx3 = document.getElementById("reqQuarterCntChart").getContext('2d');
		
		new Chart(ctx3, {
		    type: 'bar',
		    data: {
		        labels: ["미지정","1분기","2분기","3분기","4분기"],
		        datasets: [/* {
							type:'line',
				            label: "담당 요구사항 수",
				            data: chart3_chargerQuarter,
				            backgroundColor: 'rgb(255, 217, 123)',
				            borderColor: 'rgba(255, 206, 86, 1)',
				            borderWidth: 1,
				            pointStyle: 'circle',
				            fill: false,
				            pointRadius: 2
						}, */{
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
					responsive: true,
					scales: {
						xAxes: [{
							stacked: true,
						}],
						yAxes: [{
							stacked: true
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
			{"url":"<c:url value='/dsh/dsh2000/dsh2000/selectDsh2000DashBoardSubDataAjax.do'/>","loadingShow":false}
			,{redoId: redoId,processId: processId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		//차트
		if(redoId == "chart"){
			fnProjectInfoSetting(data.prjInfo);
			fnChartSetting(data);
		}
		//계획대비 미처리 건수
		else if(redoId == "reqDtmOver"){
			//계획대비 미처리건수
			fnReqDtmOverListSetting(data.reqDtmOverList);
		}
		//산출물 미제출 건수
		else if(redoId == "docDtmOver"){
			//산출물 미제출건
			fnDocDtmOverListSetting(data.docDtmOverList);
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
		//작업 목록
		else if(redoId == "work"){
			//작업 목록
			var workList = data.workList;
			
			//담당 작업 목록 세팅
			if(!gfnIsNull(workList)){
				gridObj["work"].setData(workList);
			}
		}
		//결재 목록
		else if(redoId == "sign"){
			//작업 목록
			var signList = data.signList;
			
			//담당 작업 목록 세팅
			if(!gfnIsNull(signList)){
				gridObj["sign"].setData(signList);
			}
		}
		//프로세스
		else if(redoId == "process"){
			var $reqGridDiv = $("div.process_info_box[processid="+processId+"] .titleFoldingContent");
			
			//요구사항 그리드 창 열려있다면 닫기
			$("div.process_info_box[processid="+processId+"] .process_req_box").hide("fast");
			
			//내용 초기화
			$("div.process_info_box[processid="+processId+"] .process_flow_box").html('');
			
			//작업흐름 세팅
			fnFlowListSetting(data.flowList);
			
			//프로세스 Cnt 세팅
			var processInfo;
			if(!gfnIsNull(data.processList[0])){
				processInfo = data.processList[0];
				//진척률
				var proDoneReqRatio = fnDoneRatio(processInfo.proDoReqCnt, processInfo.proDoneReqCnt);
				
				$(".process_info_box[processid="+processInfo.processId+"] span.doReqCnt").html(processInfo.proDoReqCnt);
				$(".process_info_box[processid="+processInfo.processId+"] span.doneReqCnt").html(processInfo.proDoneReqCnt);
				$(".process_info_box[processid="+processInfo.processId+"] span.doneReqRatio").html(proDoneReqRatio);
				
			}else{
				jAlert("프로세스 정보 조회에 실패했습니다.","알림");
				return false;
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
				{"url":"<c:url value='/dsh/dsh1000/dsh1000/selectDsh1000ProFlowRequestAjax.do'/>","loadingShow":false}
				,{processId: processId, flowId: flowId, type: type});
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
				var $processChargeCnt = $(".process_info_box[processId="+processId+"] .chargeReqCnt");
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
					var $before_flowReqCnt = $(".process_info_box[processId="+processId+"] .flow_box_contents[flowid="+flowId+"] > span").eq(1);
					
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
				//담당인경우 담당 개수 재세팅
				else if(type == "charge"){
					//프로세스 기존 개수
					var before_proReqCnt = parseInt($processChargeCnt.text());
					
					//작업흐름 div 기존 개수
					var $before_flowReqCnt = $(".process_info_box[processId="+processId+"] .flow_box_contents[flowid="+flowId+"] > span").eq(0);
					
					//조회 개수가 다른 경우
					if(parseInt($before_flowReqCnt.text()) != selectCnt){
						var calc_value = before_proReqCnt;
						
						//담당 수 계산 1.기존 개수 감산
						calc_value -= parseInt($before_flowReqCnt.text());
						
						//2. 새로운 개수 가산
						calc_value += selectCnt;
						
						//3. 적용
						$processChargeCnt.text(calc_value);
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
	//작업 목록 세팅
	else if(processId == "work"){
		tmp_config = workGrid_config;
	}
	//결재 목록 세팅
	else if(processId == "sign"){
		tmp_config = signGrid_config;
	}
	
	//타겟 변경
	tmp_config.target = $('[data-ax5grid="flowGrid-'+processId+'"]');
    	 
	//그리드 프레임 호출
	gridObj[processId].setConfig(tmp_config);
}
</script>
<div class="main_contents">
	<div class="contents_wrap">
		<div class="contents_title">
			${sessionScope.selMenuNm }
			<div class="dataAutoRefreshDiv">
				<i class="fa fa-clock"></i>&nbsp;
				<span id="autoRefreshSpan">00분 00초</span> 후 자동 갱신&nbsp;-
				<i class="fa fa-cogs autoRefresh_timeIcon"></i>&nbsp;
				<select class="autoRefresh_select" id="timerVarSel" onchange="fnDashBoardSetting()">
					<option value="0.5">30초</option>
					<option value="1">1분</option>
					<option value="5" selected>5분</option>
					<option value="10">10분</option>
					<option value="30">30분</option>
					<option value="60">1시간</option>
				</select>
				<div class="autoRefreshFoldingBtn"><span class="titleFoldingContent up" folding="all"></span></div>
				<div class="autoRefreshEndBtn" onclick="fnAutoRefreshEnd();"><li class="fa fa-times"></li></div>
				<div class="autoRefreshRedoBtn" onclick="fnDashBoardSetting();"><li class="fa fa-redo"></li></div>
			</div>
		</div>
		<div class="osl_main_wrap">
			<div class="dsh_top_box">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="0"></span></div>
					<div class="titleRedoBtn" redoid="chart" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					<span id="prjNm"></span>
					<div class="dsh2000_prjInfo">[ 진행: <span id="prjReqDo">0</span>건 / 완료: <span id="prjReqDone">0</span>건 / 진척률: <span id="prjReqPer">0</span>% ]</div>
					<div class="dsh2000_dtmOverAlert" processid="project">
						<div class="dtmOverAlertCnt_desc" folding="0">
							<div class="alertDesc">초과</div>
							<div class="alertDesc">임박</div>
							<div class="alertDesc">여유</div>
							<div class="alertDesc">실패</div>
							<div class="alertDesc">적기</div>
						</div>
						<div class="dtmOverAlertCnt alert-red" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-yellow" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-green" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-overRed" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-blue" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>
					</div>
					<div class="dsh2000_prjDateRangeInfo"></div>
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
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="reqDtmOver"></span></div>
					<div class="titleRedoBtn" redoid="reqDtmOver" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					계획대비 미처리건수
					<div class="dsh2000_prjInfo">[ 미처리: <span id="reqDtmOverDo">0</span>건 / 총: <span id="reqDtmOverDone">0</span>건 / 완료율: <span id="reqDtmOverPer">0</span>% ]</div>
				</div>
				<div class="foldDiv" folding="reqDtmOver">
					<div class="reqDtmOver_leftDiv">
						<canvas id="reqDtmOverChart" width="500" height="250"></canvas>
					</div>
					<div class="reqDtmOver_RightDiv">
						<div data-ax5grid="reqDtmOverGrid" data-ax5grid-config="{}" style="height: 250px;"></div>
					</div>
				</div>
			</div>
			<div class="dsh_middle_box">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="docDtmOver"></span></div>
					<div class="titleRedoBtn" redoid="docDtmOver" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					산출물 제출 예정일 대비 미제출 건수
					<div class="dsh2000_prjInfo">[ 미등록: <span id="docDtmOverNull">0</span>건/  미처리: <span id="docDtmOverDo">0</span>건 / 총: <span id="docDtmOverDone">0</span>건 ]</div>
				</div>
				<div class="foldDiv" folding="docDtmOver">
					<div data-ax5grid="docDtmOverGrid" data-ax5grid-config="{}" style="height: 250px;"></div>
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
			<div class="dsh_middle_box dsh_middle_half fl">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="sign"></span></div>
					<div class="titleRedoBtn" redoid="sign" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					담당 결재 목록
				</div>
				<div class="foldDiv" folding="sign">
					<div data-ax5grid="flowGrid-sign" data-ax5grid-config="{}" style="height: 250px;"></div>
				</div>
			</div>
			<div class="dsh_middle_box dsh_middle_half fr">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="work"></span></div>
					<div class="titleRedoBtn" redoid="work" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					<div class="titleWorkBtn" onclick="fnDshboardWorkComplete()"><li class="fa fa-user-check"></li>&nbsp;작업 종료</div>
					담당 작업 목록
				</div>
				<div class="foldDiv" folding="work">
					<div data-ax5grid="flowGrid-work" data-ax5grid-config="{}" style="height: 250px;"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslits/bottom/footer.jsp" />