<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />
<script type="text/javascript" src="<c:url value='/js/chart/chartJs/Chart.bundle.js'/>"></script>
<link rel='stylesheet' href="<c:url value='/css/oslops/dsh.css'/>" type='text/css'>
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

//프로세스의 작업흐름 DATA
var processFlowList = {};

//사용자 대시보드 표시 구분
var dshDisplayCd = "${usrInfo.dshDisplayCd}";

//프로세스 표시 형식 저장 변수(kanban, etc..)
var dshProcessDisplayCd = {};

//대시보드 차트
var dashboardChart = [];

//프로젝트 기간
var prjStartDt;
var prjEndDt;


//배포계획 결재 그리드 설정 값
var dplSignGrid_config = {
    sortable:true,
    header: {align:"center"},
    columns: [
              {key: "rn", label: " ", width: '4%', align: "center"},	
        		{key: "signStsNm", label: "결재 상태", width: 150, align: "center"},
       		{key: "signRegUsrNm", label: "요청자", width: 150, align: "center"},
       		{key: "signUsrNm", label: "결재자", width: 150, align: "center"},
       		{key: "signDtm", label: "결재 요청 일자", width: 150, align: "center",formatter:function(){
       			return new Date(this.item.signDtm).format("yyyy-MM-dd");
       		}},
       		{key: "dplNm", label: "배포계획명", width: 220, align: "center"},
       		{key: "signTxt", label: "결재 의견", width: 320, align: "left"},
       		{key: "signRejectTxt", label: "반려내용", width: 320, align: "left"},
        		],
    body: {
        align: "center",
        columnHeight: 30,
        onClick: function () {
        	//이전 선택 row 해제
            this.self.select(dplSignGrid.selectedDataIndexs[0]);
        	
        	//현재 선택 row 전체 선택
            this.self.select(this.doindex);
        	
        },onDBLClick:function(){
        	// 더블클릭 시 상세보기
        	var item = this.item;
        	var data = {"dplId" : item.dplId, "prjId" : item.prjId};
		gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1003View.do',data, "415", "690",'scroll');
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
        ],
        popupFilter: function (item, param) {
        	//선택 개체 없는 경우 중지
        	if(typeof dplSignGrid.list[param.doindex] == "undefined"){
        		return false;
        	}

        	return true;
        },
        onClick: function (item, param) {
            if(item.type == "reply"){
            	gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
            }
            dplSignGrid.contextMenu.close();
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
           fnInGridListSetting(this.page.selectPage,"&signUsrId=${sessionScope.loginVO.usrId}",gridObj["dplSign"],"<c:url value='/dpl/dpl2000/dpl2100/selectDpl2100AjaxView.do'/>",false);
        }
    } 
};

//요구사항 결재 그리드 설정 값
var signGrid_config = {
	showLineNumber: false,
	sortable:true,
	header: {align:"center"},
	columns: [
	    {key: "rn", label: " ", width: '4%', align: "center"},	
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
			//선택 개체 없는 경우 중지
			if(typeof param.item == "undefined"){
				return false;
			}
			return true;
		},
		onClick: function (item, param) {
			//상세 정보
			if(item.type == "detailPopup"){
				var data = {"mode":"newReq","reqId": param.item.reqId, "reqProType":"02"}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}
			param.gridSelf.contextMenu.close();
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
             fnInGridListSetting(this.page.selectPage,"&signUsrId=${sessionScope.loginVO.usrId}",gridObj["sign"],"<c:url value='/chk/chk1000/chk1100/selectChk1100AjaxView.do'/>",false);
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
			//선택 개체 없는 경우 중지
			if(typeof param.item == "undefined"){
				return false;
			}
			return true;
		},
		onClick: function (item, param) {
			//상세 정보
			if(item.type == "detailPopup"){
				var data = {"mode":"newReq","reqId": param.item.reqId, "reqProType":param.item.reqProType}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			}
			param.gridSelf.contextMenu.close();
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
        	fnInGridListSetting(this.page.selectPage,"&workChargerId="+usrId,gridObj["work"],"<c:url value='/req/req4000/req4400/selectReq4400ReqWorkListAjax.do'/>",false);
        }
    }
};

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
             	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '890','scroll');
			}else{
				var data = {"reqId": this.item.reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4106View.do", data, '900', '950','auto');
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
			//선택 개체 없는 경우 중지
			if(typeof param.item == "undefined"){
				return false;
			}
			return true;
		},
		onClick: function (item, param) {
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
             	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '890','scroll');
			}
			param.gridSelf.contextMenu.close();
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
      	fnInGridListSetting(this.page.selectPage,"&mode=dashboard&reqProType=01",gridObj["request"],"<c:url value='/req/req1000/req1000/selectReq1000ListAjaxView.do'/>",false);
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
		{key: "calcReqDay", label: "초과 일수", width: 120, align: "center",formatter:function(){
			return this.item.calcReqDay+"일";
		}},
		{key: "reqChargerNm", label: "담당자", width: 80, align: "center"},
		{key: "regDtm", label: "생성일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.regDtm).format("yyyy-MM-dd");
		}},
		{key: "reqStDtm", label: "작업 시작일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.reqStDtm).format("yyyy-MM-dd HH:mm");
		}},
		{key: "reqEdDtm", label: "작업 종료일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.reqEdDtm).format("yyyy-MM-dd HH:mm");
		}},
		{key: "reqEdDuDtm", label: "작업 시작 예정일자", width: 140, align: "center",formatter:function(){
			return new Date(this.item.reqStDuDtm).format("yyyy-MM-dd");
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
        	fnFlowReqGridSetting(this.page.selectPage,this.self.processId, this.self.flowId, this.self.type);
        }
    }
};

//페이지 로드시 실행
$(document).ready(function(){

	// 개발 대시보드 가이드 상자 호출
	gfnGuideStack("add",fnDsh2000GuideShow);
	
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
								chartY = position.y-((position.y-element._model.base)/2.3);
							}
							//가로축 표시
							else if(dataset.valueShow == "barX"){
								chartX = position.x-((position.x-element._model.base)/2.3);
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
	//차트 및 접수 요구사항 목록 초기화
	$(".foldDiv[folding=0]").show('fast',function(){
		$(".titleFoldingContent[folding=0]").removeClass("down").addClass("up");	
	});
	
	$(".foldDiv[folding=work]").show('fast',function(){
		$(".titleFoldingContent[folding=work]").removeClass("down").addClass("up");	
	});
	
	$(".foldDiv[folding=sign]").show('fast',function(){
		$(".titleFoldingContent[folding=sign]").removeClass("down").addClass("up");	
	});
	
	$(".foldDiv[folding=1]").show('fast',function(){
		$(".titleFoldingContent[folding=1]").removeClass("down").addClass("up");
	});
	
	//전체 down/up 초기화
	$(".titleFoldingContent[folding=all]").removeClass("down").addClass("up");
	
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

		//대시보드 칸반 표시인경우 요구사항 불러오기
		if(!gfnIsNull(dshDisplayCd) && dshDisplayCd == "02"){
			if(!gfnIsNull(processFlowList) && Object.keys(processFlowList).length > 0){
				//대시보드 표시 구분에 따라 그리드, 칸반 출력
				$.each(processFlowList,function(idx, map){
					fnFlowReqKanbanSetting(idx);
				});
			}
		}else{
			//부분 칸반 프로세스 구하기
			if(!gfnIsNull(processFlowList) && Object.keys(processFlowList).length > 0){
				//대시보드 표시 구분에 따라 그리드, 칸반 출력
				$.each(processFlowList,function(idx, map){
					//해당 프로세스ID 있는지 체크
					if(dshProcessDisplayCd.hasOwnProperty(idx)){
						var typeCd = dshProcessDisplayCd[idx];
						
						//kanban인경우 요구사항 조회
						if(typeCd == "kanban"){
							fnFlowReqKanbanSetting(idx);
						}else{
							return true;
						}
					}else{
						return true;
					}
				});
			}
		}
		
		//접수 대기 그리드 세팅
		fnDashboardGridSetting("request");
		fnInGridListSetting(0,"&mode=dashboard&reqProType=01",gridObj["request"],"<c:url value='/req/req1000/req1000/selectReq1000ListAjaxView.do'/>",false);

		//담당 작업 그리드 세팅
		fnDashboardGridSetting("work");
		fnInGridListSetting(0,"&workChargerId="+usrId,gridObj["work"],"<c:url value='/req/req4000/req4400/selectReq4400ReqWorkListAjax.do'/>",false);
		
		//요구사항 결재 그리드 세팅
		fnDashboardGridSetting("sign");
		fnInGridListSetting(0,"&signUsrId=${sessionScope.loginVO.usrId}",gridObj["sign"],"<c:url value='/chk/chk1000/chk1100/selectChk1100AjaxView.do'/>",false);
		
		//배포계획 결재 그리드 세팅
		fnDashboardGridSetting("dplSign");
		fnInGridListSetting(0,"&signUsrId=${sessionScope.loginVO.usrId}",gridObj["dplSign"],"<c:url value='/dpl/dpl2000/dpl2100/selectDpl2100AjaxView.do'/>",false);
		
		//차트 세팅
		fnChartSetting(data);
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

	var prjNm = prjInfo.prjNm;
	regPrjNm = (prjNm.replace(/</g,"&lt;")).replace(/>/g,"&gt;");
	
	//프로젝트명
	$("span#prjNm").html(gfnCutStrLen(regPrjNm,85));
		
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
	
	var reqDtmOverLabels = [];
	var reqDtmOverDatasets = [];
	
	// 계획대비 미처리건수 데이터 가공
	var reqOverArr = {};
	$.each(reqDtmOverList, function(idx, map){
		
		var processId = map.processId;
		// 프로세스 ID 배열이 없을경우 배열생성
		if(gfnIsNull(reqOverArr[processId])){
			reqOverArr[processId] = [];
		}
		// 배열에 데이터 세팅
		reqOverArr[processId].push({"processId":processId, "processNm":map.processNm});
	});
	
	// 계획대비 미처리건수 차트 데이터를 만든다.
	$.each(reqOverArr, function(processId, overList){
		// 프로세스의 미처리 건수 요구사항이 있을경우
		if(overList.length > 0){
			// 미처리 요구사항이 있는 프로세스명, 미처리 건수를 배열에 담는다.
			reqDtmOverLabels.push(overList[0].processNm);
			reqDtmOverDatasets.push(overList.length);
		}
	});
	
	//차트 표시
	var ctx = document.getElementById("reqDtmOverChart").getContext('2d');
	//차트 데이터 있는지 체크하고 이미 있는경우 차트 소멸하고 다시 세팅
	if(!gfnIsNull(dashboardChart[3])){
		dashboardChart[3].destroy();
	}
	
	//차트 생성
	dashboardChart[3] = new Chart(
			ctx, {
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
					,'onClick' : function (evt, item) { 
				          if(!gfnIsNull(item) && item.length >0){
				       			var label  = item[0]._model.label;

				            	var data = { "processNm" : label, "popTitleMsg" : label};	

				            	gfnLayerPopupOpen('/dsh/dsh2000/dsh2000/selectDsh2001View.do',data, "850", "550",'scroll');
				            }
				     }
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
		
	}
}

//프로세스 목록 세팅
function fnProcessListSetting(processList){
	//프로세스 내용 초기화
	$("#process_list_box").html('');

	var dshDisplayClass = '';
	//대시보드 표시 구분(그리드, 칸반)
	if(!gfnIsNull(dshDisplayCd) && dshDisplayCd == "02"){
		dshDisplayClass = "processKanban";
	}
	
	//프로세스 loop
	$.each(processList,function(idx, map){
		var foldingNum = (idx+2);

		var setDisplayClass = dshDisplayClass;
		
		//이전에 열어둔 대시보드 타입 적용(grid, kanban)
		if(dshProcessDisplayCd.hasOwnProperty(map.processId)){
			var typeCd = dshProcessDisplayCd[map.processId];
			
			//kanban인경우 kanban적용
			if(typeCd == "kanban"){
				setDisplayClass = "processKanban";
			}else{
				setDisplayClass = '';
			}
		}else{
			//대시보드 타입이 칸반인경우 전체 프로세스 칸반 적용
			if(!gfnIsNull(dshDisplayCd) && dshDisplayCd == "02"){
				dshProcessDisplayCd[map.processId] = "kanban";
			}
		}
		
		//진척률
		var proDoneReqRatio = fnDoneRatio(map.proDoReqCnt, map.proDoneReqCnt);

		//프로세스 html 추가 (append 이후 바로 그리드 세팅)
		$("#process_list_box").append(
			'<div class="process_info_box '+setDisplayClass+'" processid="'+map.processId+'" totalReq='+(map.proTotalReqCnt)+'>'
				+'<div class="dsh_title_box">'
					+map.processNm
					+'<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="'+foldingNum+'"></span></div>'
					+'<div class="titleFullScreanBtn" onclick="fnDivFullScrean(\''+map.processId+'\');"><i class="fas fa-expand"></i></div>'
					+'<div class="titleRedoBtn"  redoid="process" processid="'+map.processId+'" onclick="fnSubDataLoad(\'obj\',this);"><li class="fa fa-redo"></li></div>'
					+'<div class="titleAllViewBtn"><span class="titleAllViewContent on" processid="'+map.processId+'"></span></div>'
					+'<div class="titleKanbanBtn" onclick="fnDivKanbanScrean(\''+map.processId+'\');"><i class="fas fa-columns"></i></div>'
					+' [ 진행 : <span class="doReqCnt">'+map.proDoReqCnt+'</span>건 / 완료 : <span class="doneReqCnt">'+map.proDoneReqCnt+'</span>건 / 진척률 : <span class="doneReqRatio">'+proDoneReqRatio+'</span>%]'
						+'<div class="dsh2000_dtmOverAlert" processid="'+map.processId+'">'
						+'<div class="dtmOverAlertCnt_desc" folding="'+foldingNum+'">'
						+'	<div class="alertDesc">초과</div>'
						+'	<div class="alertDesc">임박</div>'
						+'	<div class="alertDesc">여유</div>'
						+'	<div class="alertDesc">실패</div>'
						+'	<div class="alertDesc">적기</div>'
						+'</div>'
						+'	<div class="dtmOverAlertCnt alert-red" onclick="fnOpenReq(\''+map.processId+'\', \'03\', \''+map.processNm+' [초과]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-yellow" onclick="fnOpenReq(\''+map.processId+'\', \'02\', \''+map.processNm+' [임박]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-green" onclick="fnOpenReq(\''+map.processId+'\', \'01\', \''+map.processNm+' [여유]\');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-overRed" onclick="fnOpenReq(\''+map.processId+'\', \'04\', \''+map.processNm+' [실패]\');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>'
						+'	<div class="dtmOverAlertCnt alert-blue" onclick="fnOpenReq(\''+map.processId+'\', \'05\', \''+map.processNm+' [적기]\');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>'
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
							+'	<div data-ax5grid="dshGrid-'+map.processId+'" data-ax5grid-config="{}" style="height: 250px;"></div>'
						+'</div>'
					+'</div>'
					+'<div class="process_kanban_box" processid="'+map.processId+'">'
					+'</div>'
				+'</div>'
			+'</div>');
	});
	
	//이벤트 초기화
	$("div.titleFoldingBtn, div.titleAllViewBtn, div.autoRefreshFoldingBtn").off("click");

	//요구사항 갯수별 작업흐름 보이기 버튼 동작
	$("div.titleAllViewBtn").click(function(){
		//프로세스 타겟
		var $allViewBtn = $(this).children(".titleAllViewContent");
		
		//프로세스 id
		var processId = $allViewBtn.attr("processid");
		
		//true - 숨김, false - 보임 
		var allViewDown = $allViewBtn.hasClass("off");
		
		//div on/off
		fnFlowDivOnOff($allViewBtn,processId,allViewDown);
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
//칸반 작업흐름 접기
function kanbanFlowFoldOn(targetObj,processId){
	event.stopPropagation();
	$(targetObj).parent(".kanbanFrameDiv").addClass("reqZeroCntFolding");
	
	//갯수가 0일경우 모두 접기
	var foldingCnt = $(".process_kanban_box[processid="+processId+"] > .kanbanFrameDiv[totalcnt='0']:not(.reqZeroCntFolding)").length;
	
	//접힌 작업흐름이 없는경우 전체 펼치기
	if(foldingCnt == 0){
		var $allViewBtn = $(".process_info_box.processKanban[processid="+processId+"]").children(".dsh_title_box").children(".titleAllViewBtn").children(".titleAllViewContent");
		//div on/off
		fnFlowDivOnOff($allViewBtn,processId,false);
	} 
}
//칸반 작업흐름 펼치기
function kanbanFlowFoldOff(targetObj,processId){
	event.stopPropagation();
	//접힌 상태인경우에만
	if($(targetObj).hasClass("reqZeroCntFolding")){
		$(targetObj).removeClass("reqZeroCntFolding");
	}
}

//div on/off
function fnFlowDivOnOff($allViewBtn, processId, allViewDown){
	//펼치기
	if(allViewDown){
		$(".process_kanban_box[processid="+processId+"] .kanbanFrameDiv").removeClass("reqZeroCntFolding");
		$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box[totalcnt=0]").show().next(".dsh_flow_arrow_box").show();
		//$(".process_kanban_box[processid="+processId+"] .kanbanFrameDiv[totalcnt=0]").show();
		
		//화살표 전체 보이기
		$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box").next(".dsh_flow_arrow_box").show();
		 
		$allViewBtn.removeClass("off").addClass("on");
		
	}else{	//접기
		$(".process_kanban_box[processid="+processId+"] .kanbanFrameDiv[totalcnt=0]").addClass("reqZeroCntFolding");
		$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box[totalcnt=0]").hide().next(".dsh_flow_arrow_box").hide();
		//$(".process_kanban_box[processid="+processId+"] .kanbanFrameDiv[totalcnt=0]").hide();
		
		//감추기할때 남은 작업흐름이 1개이면 화살표 전체 감추기
		if($(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box[totalcnt!=0]").length == 1){
			$(".process_info_box[processid="+processId+"] .process_flow_box .dsh_flow_box").next(".dsh_flow_arrow_box").hide();
		}
		 
		$allViewBtn.removeClass("on").addClass("off");
		
		//칸반 펼치기 이벤트 걸기
		$(".kanbanFrameDiv.reqZeroCntFolding").click(function(){
			$(this).removeClass("reqZeroCntFolding");
			
			//모두 펼칠경우 해당 view 아이콘 변경
			var foldingCnt = $(this).parent(".process_kanban_box").children(".kanbanFrameDiv.reqZeroCntFolding").length;
			
			//접힌 작업흐름이 없는경우 전체 펼치기
			if(foldingCnt == 0){
				//div on/off
				fnFlowDivOnOff($allViewBtn,$(this).parent(".process_kanban_box").attr("processid"),true);
			}
		});
	}
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

		//작업흐름 옵션
		var flowOptionStrDiv = '<div class="flowOptionDiv">'+flowOptionStr+'</div>';
		
		//일반 그리드 목록
		$("div.process_info_box[processid="+map.processId+"] .process_flow_box").append(
				'<div class="dsh_flow_box" flowid="'+map.flowId+'" totalcnt="'+map.reqTotalCnt+'" style="background-color: '+map.flowTitleBgColor+';color: '+map.flowTitleColor+';">'
				+flowOptionStrDiv
				+'	<div class="flow_box_title">'+map.flowNm+'</div>'
				+'	<div class="flow_box_contents" flowid="'+map.flowId+'" style="background-color: '+map.flowContentBgColor+';color: '+map.flowContentColor+';">'
				+'<span onclick="fnFlowReqList(\''+map.processId+'\',\''+map.flowId+'\',\'charge\')">'+map.reqChargerCnt+'</span>'
				+' / '
				+'<span onclick="fnFlowReqList(\''+map.processId+'\',\''+map.flowId+'\',\'all\')">'+map.reqTotalCnt+'</span>'
				+'</div>'
				+'</div>');
		
		//flowOptionStr 내용 없는경우 빈칸 넣기
		if(gfnIsNull(flowOptionStr)){
			flowOptionStr = "&nbsp;";
		}
		
		//칸반 목록
		$("div.process_kanban_box[processid="+map.processId+"]").append(
				'<div class="kanbanFrameDiv" totalcnt="'+map.reqTotalCnt+'" style="background-color: '+map.flowTitleBgColor+';color: '+map.flowTitleColor+';">'
					+'<div class="kanbanFoldBtn" onclick="kanbanFlowFoldOn(this,\''+map.processId+'\')"><i class="fas fa-eye-slash fa-2x"></i></div>'
					+'<div class="kanbanFlowOpt">'+flowOptionStr+'</div>'
					+'<div class="kanbanFlowReqCntDiv" flowid="'+map.flowId+'">'
						+'<span>'+map.reqChargerCnt+'</span>'
						+' / '
						+'<span>'+map.reqTotalCnt+'</span>'
					+'</div>'
					+'<div class="kanbanFlowNameDiv">'+map.flowNm+'</div>'
					+'<div class="kanbanContentReqList" processid="'+map.processId+'" flowid="'+map.flowId+'" style="background-color: '+map.flowContentBgColor+';color: '+map.flowContentColor+';"></div>'
				+'</div>');

		//칸반 작업을 위한 프로세스 - 작업흐름 세팅
		if(gfnIsNull(processFlowList[map.processId])){
			processFlowList[map.processId] = {};
		}
		if(gfnIsNull(processFlowList[map.processId][map.flowId])){
			processFlowList[map.processId][map.flowId] = map;
		}

		Sortable.create($(".kanbanContentReqList[flowid="+map.flowId+"]")[0],{
			group: 'requestList',
			draggable: '.kanbanSubMainFrame',
			direction: "vertical",
			filter: '.filtered',
			ghostClass : "dsh_req_row_ghost",
			chosenClass : "dpl_job_row_chosen",
			scroll:true,
			scrollSensitivity: 50,
			bubbleScroll:true,
			onMove: function (evt, originalEvent) {
				var fromProcessId = $(evt.from).attr("processid");
				var toProcessId = $(evt.to).attr("processid");
				
				//다른 프로세스인경우 중지 (프로세스 이관 기능 없음)
				if(fromProcessId != toProcessId){
					return false;
				}
			},
			onEnd: function (evt) {
				//processId, flowId
				var startProcessId = $(evt.from).attr("processid");
				var startFlowId = $(evt.from).attr("flowid");
				var endProcessId = $(evt.to).attr("processid");
				var endFlowId = $(evt.to).attr("flowid");

				var req4105Check = true;
				
				//시작 작업흐름 정보
				var startFlowInfo = processFlowList[startProcessId][startFlowId];
				
				//같은 flowId인경우 중지
				if(startProcessId == endProcessId && startFlowId == endFlowId){
					return false;
				}
				
				//다음 이동 작업흐름 없는 경우 오류
				if(gfnIsNull(startFlowInfo.flowNextId)){
					toast.push("이미 최종완료 처리된 요구사항 입니다.");
				}
				
				//현재 작업흐름부터 이동 작업흐름까지 이동 가능한지 체크
				var reqMoveCheck = fnFlowLoopCheck(startProcessId,startFlowId,endProcessId,endFlowId);
				
				//return value값이 false인경우 중지
				if(reqMoveCheck != true && reqMoveCheck != false && !reqMoveCheck.value){
					toast.push(reqMoveCheck.message);
					
					req4105Check = false;
				}
				//이동 불가
				else if(reqMoveCheck == false){
					req4105Check = false;
				}
				else if(reqMoveCheck == true){
					//업무화면 이동
					req4105Check = true;
				}
				
				
				//업무화면 팝업
				if(req4105Check){
					//요구사항 Id
					var paramReqId = $(evt.item).attr("reqid");
					
					var data = {"reqId": paramReqId, moveType: "kanban", endProcessId: endProcessId, endFlowId: endFlowId}; 
					gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1330', '900','scroll',false);
					
				}
				//이동 취소
				$(evt.item).appendTo($(evt.from));
			}
		});
		
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

//대시보드 작업 종료 처리 (담당자만 가능)
function fnDshboardWorkComplete(){
	//항목 선택 확인
	if(Object.keys(gridObj["work"].focusedColumn).length == 0){
		jAlert("종료할 작업을 선택해주세요.","알림");
		return false;
	}
	
	var item = gridObj["work"].list[gridObj["work"].focusedColumn[Object.keys(gridObj["work"].focusedColumn)].doindex];
	
	//종료처리된 작업은 종료 불가능
	if(item.workStatusCd == "02"){
		jAlert("이미 종료된 작업입니다.","알림");
		return false;
	}
	//담당자 외에 작업 종료 불가능 
	else if(item.workChargerId != usrId){
		jAlert("작업 종료는 담당자만 가능합니다.","알림");
		return false;
	}
	
	//매개변수 세팅
	var reqId = item.reqId;
	var processId = item.processId;
	var flowId = item.flowId;
	var workId = item.workId;
	
	
	//팝업 화면 오픈
	var data = {reqId: reqId,processId: processId, flowId: flowId, workId: workId, type:"dsh1000"};
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4109View.do", data, '600', '430','scroll');
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
			//if(map.processId == "request"){
				//요구사항 최종 종료 개수
			//	chart_reqEndCnt.push(map.allCnt);
			//}else{
				//요구사항 최종 종료 개수
				chart_reqEndCnt.push(map.endCnt);	
			//}
		});
		
		var ctx1 = document.getElementById("reqTotalCntChart").getContext('2d');
		
		//차트 데이터 있는지 체크하고 이미 있는경우 차트 소멸하고 다시 세팅
		if(!gfnIsNull(dashboardChart[0])){
			dashboardChart[0].destroy();
		}
		
		//차트 생성
		dashboardChart[0] = new Chart(ctx1, {
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
					,'onClick' : function (evt, item) {
			           if(!gfnIsNull(item) && item.length >2){
			       			var label  = item[2]._model.label;
			       			var data = {};
			            	if(label =="접수 대기"){
			            		data = { "reqProType" : "01" , "popTitleMsg" : label};	
			            	}else{
			            		data = { "processNm" : label, "popTitleMsg" : label};	
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
			if(Object.keys(chart2_y_mmCnt).indexOf(reqEdDtmMm) == -1){
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
		//차트 데이터 있는지 체크하고 이미 있는경우 차트 소멸하고 다시 세팅
		if(!gfnIsNull(dashboardChart[1])){
			dashboardChart[1].destroy();
		}
		
		//차트 생성
		dashboardChart[1] = new Chart(ctx2, {
		    type: 'bar',
		    data: {
		        labels: chart2_x,
		        datasets: chart2_datasets
		    },
		    options: {
			    	'onClick' : function (evt, item) {
		    	 		if(item.length>0){
		    	 			var label =  item[0]._model.label;
		    	 			var month = label.substring(0,2);
		    	 			var data = {};
			            	
			            	data = { "processMonth" : month, "popTitleMsg" : label};	
			            	
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
		var ctx3 = document.getElementById("reqQuarterCntChart").getContext('2d');
		
		//차트 데이터 있는지 체크하고 이미 있는경우 차트 소멸하고 다시 세팅
		if(!gfnIsNull(dashboardChart[2])){
			dashboardChart[2].destroy();
		}
		
		//차트 생성
		dashboardChart[2] = new Chart(ctx3, {
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
			    	'onClick' : function (evt, item) {
			    		if(item.length>0){
		    	 			var label =  item[0]._model.label;
		    	 			var quarter = label.substring(0,1);
		    	 			var data = {};
			            	if(quarter=="1"){
			            		data = { "processStartMonth" : "01" ,"processEndMonth" : "03", "popTitleMsg" : "1분기"};	
			            	}else if(quarter=="2"){
			            		data = { "processStartMonth" : "04" ,"processEndMonth" : "06", "popTitleMsg" : "2분기"};
			            	}else if(quarter=="3"){
			            		data = { "processStartMonth" : "07" ,"processEndMonth" : "09", "popTitleMsg" : "3분기"};
			            	}else if(quarter=="4"){
			            		data = { "processStartMonth" : "10" ,"processEndMonth" : "12", "popTitleMsg" : "4분기"};
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


//현재 작업흐름부터 이동 작업흐름까지 이동 가능한지 체크
function fnFlowLoopCheck(startProcessId,startFlowId,endProcessId,endFlowId){
	//시작 작업흐름 정보
	var startFlowInfo = processFlowList[startProcessId][startFlowId];
	
	//이동지점 작업흐름 정보
	var endFlowInfo = processFlowList[endProcessId][endFlowId];

	//다음 이동 작업흐름이 이동지점 작업흐름과 같은 경우 return
	if(startFlowInfo.flowNextId == endFlowInfo.flowId){
		return {value:true};
	}else{
		//다음 이동 작업흐름 없는 경우 
		if(gfnIsNull(startFlowInfo.flowNextId)){
			return false;
		}
		
		//다음 작업흐름 정보 불러오기
		var nextFlowInfo = processFlowList[startProcessId][startFlowInfo.flowNextId];
		
		//필수인경우 중지
		if(nextFlowInfo.flowEssentialCd == "01"){
			//이동지점이 최종완료인 경우
			if(gfnIsNull(endFlowInfo.flowNextId)){
				//현재 작업흐름에 종료분기 있는 경우만
				if(startFlowInfo.flowEndCd == "01"){
					return {value:true};
				}else{
					return {message:"최종완료 처리 될 수 없는 요구사항입니다.",value:false};
				}
			}else{
				return {message:"다음 작업흐름 "+nextFlowInfo.flowNm+"(이)가 필수 작업흐름입니다.",value:false};
			}
		}
		
		//다음 작업흐름 Id체크
		return fnFlowLoopCheck(startProcessId,startFlowInfo.flowNextId,endProcessId,endFlowId);
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

	//접수 요청
	if(redoId == "request"){
		fnInGridListSetting(gridObj["request"].page.currentPage,"&mode=dashboard&reqProType=01",gridObj["request"],"<c:url value='/req/req1000/req1000/selectReq1000ListAjaxView.do'/>",true);
		return false;
	}
	//요구사항 결재 목록
	else if(redoId == "sign"){
		fnInGridListSetting(gridObj["sign"].page.currentPage,"&signUsrId=${sessionScope.loginVO.usrId}",gridObj["sign"],"<c:url value='/chk/chk1000/chk1100/selectChk1100AjaxView.do'/>",false);
		return false;
	}
	//배포계획 결재 목록
	else if(redoId == "dplSign"){
		fnInGridListSetting(gridObj["dplSign"].page.currentPage,"&signUsrId=${sessionScope.loginVO.usrId}",gridObj["dplSign"],"<c:url value='/dpl/dpl2000/dpl2100/selectDpl2100AjaxView.do'/>",false);
		return false;
	}
	//작업 목록
	else if(redoId == "work"){
		fnInGridListSetting(gridObj["work"].page.currentPage,"&workChargerId="+usrId,gridObj["work"],"<c:url value='/req/req4000/req4400/selectReq4400ReqWorkListAjax.do'/>",false);
		return false;
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh2000/dsh2000/selectDsh2000DashBoardSubDataAjax.do'/>","loadingShow":true}
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
		//프로세스
		else if(redoId == "process"){
			//요구사항 그리드 창 열려있다면 닫기
			$("div.process_info_box[processid="+processId+"] .process_req_box").hide("fast");
			
			//내용 초기화
			$("div.process_info_box[processid="+processId+"] .process_flow_box").html('');
			$("div.process_kanban_box[processid="+processId+"]").html('');
			
			//작업흐름 목록
			var flowList = data.flowList;
			
			//작업흐름 데이터 세팅
			fnFlowListSetting(flowList);
			
			//칸반 열려있는 경우 요구사항 세팅
			if($("div.process_info_box.processKanban[processid="+processId+"]").length > 0){
				//요구사항 목록 불러오기
				fnFlowReqKanbanSetting(processId);
			}
			
			//allView off인경우 on로 변경
			var $allViewBtnChk = $("div.process_info_box[processid="+processId+"] .titleAllViewContent");
			if($allViewBtnChk.hasClass("off")){
				$allViewBtnChk.click();
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
		fnDashboardGridSetting(processId);
		
		//그리드 내용 세팅
		fnFlowReqGridSetting(0,processId, flowId, type);
	});
}

//그리드 내용 세팅
function fnFlowReqGridSetting(_pageNo,processId, flowId, type){
	var pageNo = 0;
	
	//페이지 세팅
 	if(!gfnIsNull(_pageNo)){
 		pageNo = _pageNo;
 	}else if(typeof gridTarget.page.currentPage != "undefined"){
 		pageNo = gridObj[processId].page.currentPage;
 	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh1000/dsh1000/selectDsh1000ProFlowRequestAjax.do'/>","loadingShow":false}
			,{pageNo: pageNo, processId: processId, flowId: flowId, type: type});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//요구사항 목록
		var proFlowReqList = data.proFlowReqList;
	
		//그리드 목록 세팅
		if(!gfnIsNull(proFlowReqList)){
			var page = data.page;
		
			gridObj[processId].setData({
				list:proFlowReqList,
				page: {
					currentPage: _pageNo || 0,
					pageSize: page.pageSize,
					totalElements: page.totalElements,
					totalPages: page.totalPages
				}
			});
	   	
			//그리드 내용 세팅
			gridObj[processId]["processId"] = processId;
			gridObj[processId]["flowId"] = flowId;
			gridObj[processId]["type"] = type;
			
			
			//해당 작업흐름+ 타이틀 개수 변경
			//해당 프로세스 요구사항 수 obj
			var $processChargeCnt = $(".process_info_box[processId="+processId+"] .chargeReqCnt");
			var $processAllCnt = $(".process_info_box[processId="+processId+"] .totalReqCnt");
			
			//조회 결과 개수
			var selectCnt = page.totalElements;
			
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
}

//칸반 보드 요구사항 세팅
function fnFlowReqKanbanSetting(processId){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dsh/dsh1000/dsh1000/selectReq4100ProcessReqList.do'/>","loadingShow":false}
			,{processId: processId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//요구사항 목록
		var proFlowReqList = data.proFlowReqList;
	
		//그리드 목록 세팅
		if(!gfnIsNull(proFlowReqList)){
			//요구사항 목록 초기화
			$(".process_kanban_box[processid="+processId+"] .kanbanContentReqList").html('');
			
			//칸반 요구사항 목록 세팅
			$.each(proFlowReqList,function(idx, map){
				
				//정보 변수
				var reqChargerNm = map.reqChargerNm;
				var reqChargerImgId = map.reqChargerImgId;
				var reqNm = map.reqNm;
				var reqDesc = map.reqDesc;
				var flowId = map.flowId;
				var reqProType = map.reqProType;
				var dDay = 0;
				
				//일자 getTime
				var currDay = 24 * 60 * 60 * 1000;
				
          	//신호등 색상 구하기
          	var overTimeColor = '';
          	
          	//ago Time구하기
          	var reqEdDuDtm = new Date(new Date(map.reqEdDuDtm).format('yyyy-MM-dd'));
				var reqEdDtm = new Date(new Date(map.reqEdDtm).format('yyyy-MM-dd'));
				var agoTime = '종료 일자 미 등록';
				var dDayStr = '';
				
				//종료 일자
				if(reqEdDtm > 0){
					agoTime = new Date(map.reqEdDtm).format('yyyy-MM-dd HH:mm:ss');
					if(reqEdDuDtm > 0){
						dDay = parseInt((reqEdDtm-reqEdDuDtm)/currDay);
					}else{
						dDay = parseInt((reqEdDtm-new Date())/currDay);
					}
				}
				//종료 예정 일자
				else if(reqEdDuDtm > 0){
					agoTime = reqEdDtm.format('yyyy-MM-dd');
					dDay = parseInt((new Date()-reqEdDuDtm)/currDay);
				}
				
				//day 구하기
				dDayStr = " "+dDay+"일";
				if(gfnIsNull(map.reqEdDtm)){
					reqEdDtm = new Date(new Date().format('yyyy-MM-dd'));
				}
				
				//신호등 색상 구하기
				if(reqProType == "04"){
					//실패
					if(new Date(reqEdDuDtm) < new Date(reqEdDtm)){
						overTimeColor = "alert-overRed";
					}else{	//적기
						overTimeColor = "alert-blue";
					}
				}else{
					//초과
					if(new Date(reqEdDuDtm) < new Date(reqEdDtm)){
						overTimeColor = "alert-red";
					}
					//임박
					else if(new Date(new Date(reqEdDuDtm).setDate(new Date(reqEdDuDtm).getDate()-3)) < new Date(reqEdDtm)){
						overTimeColor = "alert-yellow";
					}else{	//여유
						overTimeColor = "alert-green";
					}
				}

				//완료 된경우 filter 걸기
				var filterClass = '';
				
				var signMaskStr = '';
				//결재 대기중인경우
				if(!gfnIsNull(map.signCd) && map.signCd == "01"){
					signMaskStr = '<div class="completeMask"><div class="completeContent">결재 대기중 (결재자: '+map.signUsrNm+')</div></div>';
					filterClass = " filtered";
				}
				
				//팝업 type
				var reqPopType = true;
				
				//최종완료 인지 체크
				if(reqProType == "04"){
					reqPopType = false;
					filterClass = " filtered";
				}
				
				//담당자가 아닌 경우 이동 불가
				if(map.reqChargerId != "${sessionScope.loginVO.usrId}"){
					filterClass = " filtered";
				}
				
				//요구사항 frame
				kanbanReqListStr = 
					'<div class="kanbanSubMainFrame'+filterClass+'" onclick="fnReqDetailPopupOpen(\''+map.reqId+'\','+reqPopType+')" reqid="'+map.reqId+'" reqprotype="'+reqProType+'">'
						+signMaskStr
						+'<div class="kbSubHeaderFrame">'
						+'<i class="fa fa-id-card"></i>&nbsp;'+map.reqOrd
						+'</div>'
						+'<div class="kbSubTopFrame">'
							+'<div class="kbSubLeftFrame">'
								+'<img class="usrImgClass" src="/cmm/fms/getImage.do?fileSn=0&atchFileId='+reqChargerImgId+'"></br>'
								+'<i class="fa fa-user-tie"></i>&nbsp;'
								+reqChargerNm
							+'</div>'
							+'<div class="kbSubRightFrame">'
								+'<div class="kbSubRightTopFrame">'
									+'<span class="kbSubReqNmDiv" title="'+reqNm+'">&nbsp;'+reqNm+'</span>'
								+'</div>'
								+'<div class="kbSubRightBottomFrame" title="'+reqDesc+'">'
									+reqDesc
								+'</div>'
							+'</div>'
						+'</div>'
						+'<div class="kbSubBottomFrame">'
						+'<div class="kbSubAgoTimeIcon '+overTimeColor+'"><i class="fas fa-clock"></i>'+dDayStr+'</div>'
							+agoTime
						+'</div>'
					+'</div>';
				
				
				//칸반 목록 넣기
				$(".process_kanban_box[processid="+processId+"] .kanbanContentReqList[flowid="+flowId+"]").append(kanbanReqListStr);
			});
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

//div 풀스크린
function fnDivFullScrean(processId){
	//대상 object
	var $targetObj = $(".process_info_box[processid="+processId+"]");
	
	//현재 풀스크린 object
	var $fullDiv = $(".process_info_box.processFullscrean");
	var fullDivId = $fullDiv.attr("processId");
	
	
	//이미 풀스크린인 Div 풀스크린 해제
	$(".process_info_box.processFullscrean").stop().animate({top:'20%'},200,function(){
		//칸반 보드 인경우
		if($targetObj.hasClass("processKanban")){
			$(".process_info_box.processKanban.processFullscrean .process_kanban_box").css({"height":""});
			$(".process_info_box.processKanban.processFullscrean .kanbanContentReqList").css({"height":""});
		
			$("body").css({"overflow":"auto"});	
		}
		
		$(this).removeClass("processFullscrean");
	});
	
	//대상과 현재 풀스크린이 같은 div인경우 풀스크린 중지
	if(processId != fullDivId){
		//풀스크린
		$targetObj.addClass("processFullscrean");
		
		//animate
		$targetObj.animate({top:'0px'});
		
		//칸반 보드 인경우
		if($targetObj.hasClass("processKanban")){
			var screenHeight = $(window).height();
			
			//kanban 높이 구하기
			$("body").css({"overflow":"hidden"});
			
			$(".process_info_box.processKanban.processFullscrean .process_kanban_box").css({"height":screenHeight-55});
			$(".process_info_box.processKanban.processFullscrean .kanbanContentReqList").css({"height":screenHeight-170});
		}
	}
}

//div 칸반보드
function fnDivKanbanScrean(processId){
	//대상 object
	var $targetObj = $(".process_info_box[processid="+processId+"]");
	
	//현재 칸반형태인지 체크
	if($targetObj.hasClass("processKanban")){
		$targetObj.children(".foldDiv").children(".process_kanban_box").slideUp();
		
		//칸반 보드 인경우
		if($targetObj.hasClass("processKanban")){
			$(".process_info_box[processid="+processId+"].processKanban .process_kanban_box").css({"height":""});
			$(".process_info_box[processid="+processId+"].processKanban .kanbanContentReqList").css({"height":""});
		
			$("body").css({"overflow":"auto"});	
			
			dshProcessDisplayCd[processId] = "grid";
		}
		$targetObj.removeClass("processKanban");
		
	}else{
		dshProcessDisplayCd[processId] = "kanban";
		//요구사항 목록 불러오기
		fnFlowReqKanbanSetting(processId);
		
		$targetObj.addClass("processKanban");
		$targetObj.children(".foldDiv").children(".process_kanban_box").show();
		
		//풀스크린인경우
		if($targetObj.hasClass("processFullscrean")){
			//화면 높이 구하기
			var screenHeight = $(window).height();
			
			//kanban 높이 구하기
			$(".process_info_box.processKanban.processFullscrean .process_kanban_box").css({"height":screenHeight-55});
			$(".process_info_box.processKanban.processFullscrean .kanbanContentReqList").css({"height":screenHeight-170});
			
			$("body").css({"overflow":"hidden"});
		}else{
			$(".process_info_box[processid="+processId+"].processKanban .process_kanban_box").css({"height":""});
			$(".process_info_box[processid="+processId+"].processKanban .kanbanContentReqList").css({"height":""});
		
			$("body").css({"overflow":"auto"});	
		}
	}
}

//그리드 데이터 조회
function fnInGridListSetting(_pageNo,paramData,gridTarget,dataUrl,loadingShow){
   	//페이지 세팅
   	if(!gfnIsNull(_pageNo)){
   		paramData += "&pageNo="+_pageNo;
   	}else if(typeof gridTarget.page.currentPage != "undefined"){
   		paramData += "&pageNo="+gridTarget.page.currentPage;
   	}
   	
   	//로딩 표시 없는경우 default = true
   	if(gfnIsNull(loadingShow)){
   		loadingShow = true;
   	}
   	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":dataUrl,"loadingShow":loadingShow}
				,paramData);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			// 조회 실패
	    	if(data.errorYn == 'Y'){ 
	    		toast.push(data.message);
	    		return;
	    	}
			
			var list = data.list;
			var page = data.page;

			if(!gfnIsNull(gridObj["request"]) && gridTarget == gridObj["request"]){
				//접수대기 요구사항 건수 표시
				$("#requestTotalCnt").html(list.length);
			}
			
		   	gridTarget.setData({
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

//요구사항 상세화면 팝업(칸반보드)
function fnReqDetailPopupOpen(reqId,type){
	//기본값 type
	if(gfnIsNull(type)){
		type = true;
	}
	//권한 체크해서 사용자인경우 상세보기로 전환
	if(usrTyp == "01"){
		var data = {"mode":"newReq","reqId": reqId, "reqProType":"02"}; 
		gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
	}else{
		//type이 flase인경우 상세정보 보기
		if(!type){
			var data = {"mode":"newReq","reqId": reqId, "reqProType":"02"}; 
			gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
		}else{
			var data = {"reqId": reqId}; 
			gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1330', '900','scroll',false);
		}
	}
}

//그리드 세팅
function fnDashboardGridSetting(gridId){
	//그리드 생성자 생성
	gridObj[gridId] = new ax5.ui.grid();
  	 
	//설정값 가져오기
	var tmp_config = flowGrid_config;
  	 
	//접수대기 그리드 세팅인경우 다른 세팅 값 호출
	if(gridId == "request"){
		tmp_config = newReqGrid_config;
	}
	//작업 목록 세팅
	else if(gridId == "work"){
		tmp_config = workGrid_config;
	}
	//요구사항 결재 목록 세팅
	else if(gridId == "sign"){
		tmp_config = signGrid_config;
	}
	//배포계획 결재 목록 세팅
	else if(gridId == "dplSign"){
		tmp_config = dplSignGrid_config;
	}
  	 
	//타겟 변경
	tmp_config.target = $('[data-ax5grid="dshGrid-'+gridId+'"]');
  	 
	//그리드 프레임 호출
	gridObj[gridId].setConfig(tmp_config);
}

// 개발 대시보드 가이드 상자
function fnDsh2000GuideShow(){
	var mainObj = $(".main_contents");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["dsh2000"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}

// 신호등, 차트 클릭 시 요구사항 팝업 호출
function fnOpenReq(processId, overType, popTitleMsg){
	var data = {};
	
	data = { "overType" : overType ,"processId" : processId, "popTitleMsg" : popTitleMsg};
	
	gfnLayerPopupOpen('/dsh/dsh1000/dsh1000/selectDsh1001View.do',data, "850", "550",'scroll');
}

</script>
<div class="main_contents">
	<div class="contents_wrap">
		<div class="contents_title">
			${sessionScope.selMenuNm }
			<div class="dataAutoRefreshDiv" guide="dsh2000AutoRefresh">
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
			<div class="dsh_top_box" guide="dsh2000DshTopPrjBox">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="0"></span></div>
					<div class="titleRedoBtn" redoid="chart" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					<span id="prjNm"></span>
					<div class="dsh2000_prjInfo" guide="dsh2000DshPrjProgressBox">[ 진행: <span id="prjReqDo">0</span>건 / 완료: <span id="prjReqDone">0</span>건 / 진척률: <span id="prjReqPer">0</span>% ]</div>
					<div class="dsh2000_dtmOverAlert" processid="project" guide="dsh2000DtmOver">
						<div class="dtmOverAlertCnt_desc" folding="0">
							<div class="alertDesc">초과</div>
							<div class="alertDesc">임박</div>
							<div class="alertDesc">여유</div>
							<div class="alertDesc">실패</div>
							<div class="alertDesc">적기</div>
						</div>
						<div class="dtmOverAlertCnt alert-red" onclick="fnOpenReq('', '03', '초과');" title="완료되지 않은 요구사항 중 현재일을 기준으로 작업시한을 넘긴 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-yellow" onclick="fnOpenReq('', '02','임박');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 3일 이하로 남은 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-green" onclick="fnOpenReq('', '01', '여유');" title="완료되지 않은 요구사항 중 현재일을 기준으로 완료시한이 4일 이상 여유있는 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-overRed" onclick="fnOpenReq('', '04', '실패');" title="요구사항 중 완료시한을 초과하여 완료된 요구사항 수 입니다.">0</div>
						<div class="dtmOverAlertCnt alert-blue" onclick="fnOpenReq('', '05', '적기');" title="요구사항 중 완료시한 이내 정상완료된 요구사항 수 입니다.">0</div>
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
				<div class="foldDiv" folding="reqDtmOver" guide="dsh2000DshNotCmplReq">
					<div class="reqDtmOver_leftDiv">
						<canvas id="reqDtmOverChart" width="500" height="250"></canvas>
					</div>
					<div class="reqDtmOver_RightDiv">
						<div data-ax5grid="reqDtmOverGrid" data-ax5grid-config="{}" style="height: 250px;"></div>
					</div>
				</div>
			</div>
			<div class="dsh_middle_box" guide="dsh2000docTimeOver">
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
			<div class="dsh_middle_box" guide="dsh2000Request">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="1"></span></div>
					<div class="titleRedoBtn" redoid="request" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					접수 대기 [총 <span id="requestTotalCnt">0</span>건]
				</div>
				<div class="foldDiv" folding="1">
					<div data-ax5grid="dshGrid-request" data-ax5grid-config="{}" style="height: 250px;"></div>
				</div>
			</div>
			<div class="process_list_box" id="process_list_box" guide="dsh2000ProcessList">
				<!-- 프로세스 목록 세팅 영역 -->
			</div>
			<div class="dsh_middle_box dsh_middle_half fl">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="sign"></span></div>
					<div class="titleRedoBtn" redoid="sign" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					담당 결재 목록
				</div>
				<div class="foldDiv" folding="sign" guide="dsh2000SignList">
					<div data-ax5grid="dshGrid-sign" data-ax5grid-config="{}" style="height: 250px;"></div>
				</div>
			</div>
			<div class="dsh_middle_box dsh_middle_half fr">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="sign"></span></div>
					<div class="titleRedoBtn" redoid="dplSign" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					담당 배포계획 결재 목록
				</div>
				<div class="foldDiv" folding="sign">
					<div data-ax5grid="dshGrid-dplSign" data-ax5grid-config="{}" style="height: 250px;"></div>
				</div>
			</div>
			<div class="dsh_middle_box">
				<div class="dsh_title_box">
					<div class="titleFoldingBtn"><span class="titleFoldingContent up" folding="work"></span></div>
					<div class="titleRedoBtn" redoid="work" onclick="fnSubDataLoad('obj',this);"><li class="fa fa-redo"></li></div>
					<div class="titleWorkBtn" onclick="fnDshboardWorkComplete()"><li class="fa fa-user-check"></li>&nbsp;작업 종료</div>
					담당 작업 목록
				</div>
				<div class="foldDiv" folding="work">
					<div data-ax5grid="dshGrid-work" data-ax5grid-config="{}" style="height: 250px;"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />