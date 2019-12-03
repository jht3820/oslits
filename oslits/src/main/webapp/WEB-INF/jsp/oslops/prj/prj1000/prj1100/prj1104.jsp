<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<script>
//작업흐름 lib target
var $copyFlowChart = $('#processCopyFlowChartDiv');
var copyProcessJsonList = {};
var selCopyFlowId = null;
var copyCurrentZoom = 1;

$(document).ready(function() {
	//프로세스 복사 대상 불러오기
	fnProcessCopyListCall();
	
	//flowchart 선언
	fnCopyFlowChartDraw();
	 
	//zoom setting
	fnCopyFlowChartLayerZoom();
	
	$("#copyFlowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
	$("#copyFlowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#copyFlowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#copyFlowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
	
	//추가 항목 관리 목록
	fnCopyFlowOptGridSetting();
	
	//프로세스 조회 버튼
	$("#btn_insert_copyProSelect").click(function(){
		fnProcessCopyListCall();
	});
	
	//프로세스 복사
	$("#btn_copyProcess_save").click(function(){
		//선택 프로세스 id,프로젝트 id
		var prjId = $(".copyProcess_box.active").attr('prjid');
		var processId = $(".copyProcess_box.active").attr('id');
		
		if(gfnIsNull(prjId) && gfnIsNull(processId)){
			jAlert("선택된 프로세스가 없습니다.","알림");
			return false;
		}else{
			jConfirm("프로세스를 복사하시겠습니까?","알림",function(result){
				if(result){
					fnProcessCopyAction(prjId, processId);
				}	
			});
			
		}
		
	});
	
	//팝업 창 닫기
	$("#btn_close_flowClose").click(function(){
		//팝업 창 닫기
		gfnLayerPopupClose();
	});
});

//flowchart 선언
function fnCopyFlowChartDraw(){
	//작업흐름 flowchart 그리기
	$copyFlowChart.flowchart({
		multipleLinksOnInput:false,
		multipleLinksOnOutput:false,
		canUserEditLinks:false,
		canUserMoveOperators:false,
		distanceFromArrow:1,
		linkWidth:5,
		//작업흐름 선택
		onOperatorSelect:function(selFlwId){
			//선택 작업흐름 Id
			selCopyFlowId = selFlwId;
			
			//데이터 불러오기
			var selFlowData = $copyFlowChart.flowchart('getOperatorData',selCopyFlowId);
			var selFlowPro = selFlowData.properties;
			
			//선택 작업흐름 정보 세팅
			$("#copyFlowNm").val(selFlowPro.title);
			
			//제목,글씨 색상
			$("#copyFlowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowTitleBgColor});
			$("#copyFlowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowTitleColor});
			$("#copyFlowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowContentBgColor});
			$("#copyFlowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowContentColor});
			
			//체크 박스
			if(selFlowPro.flowEssential == "on"){	//필수
				$("#copyFlowEssential")[0].checked = true;
			}else{
				$("#copyFlowEssential")[0].checked = false;
			}
			if(selFlowPro.flowSign == "on"){		//결재
				$("#copyFlowSign")[0].checked = true;
			}else{
				$("#copyFlowSign")[0].checked = false;
			}
			if(selFlowPro.flowSignStop == "on"){		//결재 반려종료
				$("#copyFlowSignStop")[0].checked = true;
			}else{
				$("#copyFlowSignStop")[0].checked = false;
			}
			if(selFlowPro.flowEnd == "on"){			//종료분기
				$("#copyFlowEnd")[0].checked = true;
			}else{
				$("#copyFlowEnd")[0].checked = false;
			}
			if(selFlowPro.flowWork == "on"){			//작업
				$("#copyFlowWork")[0].checked = true;
			}else{
				$("#copyFlowWork")[0].checked = false;
			}
			if(selFlowPro.flowRevision == "on"){			//리비전
				$("#copyFlowRevision")[0].checked = true;
			}else{
				$("#copyFlowRevision")[0].checked = false;
			}
			if(selFlowPro.flowDpl == "on"){			//배포계획
				$("#copyFlowDpl")[0].checked = true;
			}else{
				$("#copyFlowDpl")[0].checked = false;
			}
			if(selFlowPro.flowAuth == "on"){			//배포계획
				$("#copyFlowAuth")[0].checked = true;
				$(".copyFlw_auth_frame").show();
			}else{
				$("#copyFlowAuth")[0].checked = false;
				$(".copyFlw_auth_frame").hide();
			}
			//추가 항목 목록 불러오기
			fnCopyFlowOptList(selFlwId);
			
			//허용 역할그룹 세팅
			fnCopyAuthListGrid();
			
			//허용 역할그룹 목록
			fnCopyAuthRefresh(selCopyFlowId,'01',copyAuthGrid);
			fnCopyAuthRefresh(selCopyFlowId,'02',copySignAuthGrid);
			
			//마스크 해제
			$("#copyFlowContentMask").hide();
			return true;
		},
		//작업흐름 포커스 잃었을때
		onOperatorUnselect:function(){
			//form 내용 초기화
			$("#prj1104PopupFrm")[0].reset();
			
			//추가 항목 목록 초기화
			firstGrid.setData([]);
			
			//color fix 초기화
			fnSpectrumReset();
			
			//마스크 설정
			$("#copyFlowContentMask").show();
			
			selCopyFlowId = null;
			return true;
		},
	});
}
//flowchart zoom
function fnCopyFlowChartLayerZoom(){
	if(!gfnIsNull($copyFlowChart)){
		var $copyContainer = $copyFlowChart.parent();
		
		var cx = $copyFlowChart.width() / 2;
	    var cy = $copyFlowChart.height() / 2;
	    
	    $copyFlowChart.panzoom();
    
	    // Centering panzoom
	    $copyFlowChart.panzoom('pan', 0, 0);
	}
}

//프로세스 복사 대상 목록 불러오기
function fnProcessCopyListCall(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectFlw1000ProcessCopyList.do'/>"});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		toast.push(data.message);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//기본 프로세스 목록
			var rootProcessList = data.rootProcessList;
			
			//프로세스 목록
			var processCopyList = data.processCopyList;
			
			//프로세스 목록 세팅
			$("#processCopyBoxList").html('');
			
			//form 내용 초기화
			$("#prj1104PopupFrm")[0].reset();
			
			//추가 항목 목록 초기화
			copyFlowOptGrid.setData([]);
			
			//flowchart 생성 된 경우 초기화
			if(!gfnIsNull($copyFlowChart)){
				//flowchart 그리기
				$copyFlowChart.flowchart('setData', {});
				//차트 선택 해제
				$copyFlowChart.flowchart('unselectOperator');
			}
			
			//jsonData 초기화
			copyProcessJsonList = {};
			
			//기본 프로세스 목록 출력
			$("#processCopyBoxList").append('<div class="copyProject_titleBox">기본 프로세스</div>');
			
			$.each(rootProcessList,function(idx, map){
				//배열값 없는 경우 생성
				if(gfnIsNull(copyProcessJsonList[map.prjId])){
					copyProcessJsonList[map.prjId] = {};
				}
				//JsonData 적재
				copyProcessJsonList[map.prjId][map.processId] = map.processJsonData;
				
				firstClassStr = "";
				//첫번째 프로세스 선택
				if(idx == 0){
					firstClassStr = " active";
					
					var jsonData = {};
				
					//jsonData가 null이 아닌경우 세팅
					if(!gfnIsNull(map.processJsonData)){
						jsonData = JSON.parse(map.processJsonData);
					}
					
					//데이터 그리기
					$copyFlowChart.flowchart('setData', jsonData);
				}
				
				$("#processCopyBoxList").append('<div class="copyProcess_box'+firstClassStr+'" id="'+map.processId+'" prjid="'+map.prjId+'" processnm="'+map.processNm+'" confirm="'+map.processConfirmCd+'" desc="'+map.processDesc+'" ord="'+map.processOrd+'" title="'+decodeURI(map.processDesc)+'" typecd="'+map.processTypeCd+'">'+map.processNm+'</div>');
			});
			
			var loopPrjId = null;
			//프로세스 목록 loop
			$.each(processCopyList,function(idx, map){
				//배열값 없는 경우 생성
				if(gfnIsNull(copyProcessJsonList[map.prjId])){
					copyProcessJsonList[map.prjId] = {};
				}
				//JsonData 적재
				copyProcessJsonList[map.prjId][map.processId] = map.processJsonData;
				
				if(loopPrjId != map.prjId){
					$("#processCopyBoxList").append('<div class="copyProject_titleBox">'+map.prjNm+'</div>');
				}
				
				$("#processCopyBoxList").append('<div class="copyProcess_box" id="'+map.processId+'" prjid="'+map.prjId+'" processnm="'+map.processNm+'" confirm="'+map.processConfirmCd+'" desc="'+map.processDesc+'" ord="'+map.processOrd+'" title="'+decodeURI(map.processDesc)+'">'+map.processNm+'</div>');
				loopPrjId = map.prjId;
			});
			
			//click event 제거
			$("#processCopyBoxList .copyProcess_box").off("click");
			
			//프로세스 active 제어
			$("#processCopyBoxList .copyProcess_box").click(function(){
				$(".copyProcess_box.active").removeClass("active");
				$(this).addClass("active");
				
				//선택 프로세스에 해당하는 작업흐름 데이터 가져오기
				var prjId = $(".copyProcess_box.active").attr('prjid');
				var processId = $(".copyProcess_box.active").attr('id');
				
				var jsonData = {};
				if(!gfnIsNull(copyProcessJsonList[prjId][processId])){
					jsonData = JSON.parse(copyProcessJsonList[prjId][processId]);
				}
				
				//flowchart 그리기
				$copyFlowChart.flowchart('setData', jsonData);
				
				//form 내용 초기화
				$("#prj1104PopupFrm")[0].reset();
				
				//추가 항목 목록 초기화
				firstGrid.setData([]);
				
				//차트 선택 해제
				$copyFlowChart.flowchart('unselectOperator');
				
				//color fix 초기화
				fnSpectrumReset();
			});
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

	
//spectrum 초기화
function fnCopySpectrumReset(){
	$("#copyFlowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
	$("#copyFlowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#copyFlowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#copyFlowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
}


//axisj5 그리드
function fnCopyFlowOptGridSetting(){
	copyFlowOptGrid = new ax5.ui.grid();
 
       copyFlowOptGrid.setConfig({
           target: $('[data-ax5grid="copyFlw-grid"]'),
           sortable:true,
           header: {align:"center"},
           columns: [
			{key: "itemEssentialCd", label: "필수", width: 100, align: "center"
			,formatter:function(){return (this.item.itemEssentialCd=="01")?"Y":"N";}},
			{key: "itemModifyCd", label: "수정", width: 100, align: "center"
			,formatter:function(){return (this.item.itemModifyCd=="01")?"Y":"N";}},
            {key: "itemNm", label: "항목 명", width: 200, align: "center"},
            {key: "itemCode", label: "추가 항목 분류", width: 150, align: "center"
			,formatter:function(){
			//02 '공통코드'인경우 해당 공통코드로 공통코드명 가져오기
			var rtnStr = "";
			//공통 코드 인경우
			if(this.item.itemCode == "01"){
				rtnStr = "기타";
			}
			//공통 코드 인경우
			else if(this.item.itemCode == "02"){
				rtnStr = "[공통]"+this.item.itemCommonCodeStr;
			}
			//기타
			else{
				rtnStr = this.item.itemCodeStr;
			}
			return rtnStr;
			}},
			{key: "itemTypeStr", label: "항목 타입", width: 120, align: "center"
            	,formatter:function(){
					//02 '공통코드'인경우 해당 공통코드로 공통코드명 가져오기
					var rtnStr = this.item.itemTypeStr;
					
					//공통 코드 인경우
					if(this.item.itemCode == "02"){
						rtnStr = "콤보 박스(select)";
					}
					//타입 없는경우
					else if(gfnIsNull(rtnStr)){
						rtnStr = this.item.itemCodeStr;
					}
					return rtnStr;
				}},
            {key: "itemRowNumStr", label: "열 넓이", width: 80, align: "center"},
            {key: "itemLength", label: "길이 제한", width: 120, align: "center"}
           ],
           body: {
               align: "center",
               columnHeight: 30
           },
           page:{display:false}
       });
       //그리드 데이터 불러오기
		//fnInGridListSet();
}

//역할 그룹 그리드
function fnCopyAuthListGrid(){
	copyAuthGrid = new ax5.ui.grid();
 
    copyAuthGrid.setConfig({
        target: $('[data-ax5grid="copyAuth-grid"]'),
        sortable:true,
        header: {align:"center"},
        columns: [
         {key: "authGrpNm", label: "역할그룹 명", width: 160, align: "center"},
         {key: "usrTypNm", label: "사용자유형", width: 120, align: "center"},
         {key: "authGrpDesc", label: "역할그룹 설명", width: 225, align: "center"},
         {key: "authGrpId", label: "역할그룹 Id", width: 225, align: "center",display:false}
        ],
        body: {
            align: "center",
            columnHeight: 30
        },
        page:{display:false}
    });
    
	copySignAuthGrid = new ax5.ui.grid();
 
    copySignAuthGrid.setConfig({
        target: $('[data-ax5grid="copySignAuth-grid"]'),
        sortable:true,
        header: {align:"center"},
        columns: [
         {key: "authGrpNm", label: "역할그룹 명", width: 160, align: "center"},
         {key: "usrTypNm", label: "사용자유형", width: 120, align: "center"},
         {key: "authGrpDesc", label: "역할그룹 설명", width: 225, align: "center"},
         {key: "authGrpId", label: "역할그룹 Id", width: 225, align: "center",display:false}
        ],
        body: {
            align: "center",
            columnHeight: 30
        },
        page:{display:false}
    });
}


//추가 항목 불러오기
function fnCopyFlowOptList(selFlwId){
	//선택 작업흐름 프로젝트 id
	var prjId = $(".copyProcess_box.active").attr('prjid');
	
	//선택 프로세스에 해당하는 작업흐름 데이터 가져오기
	var processId = $(".copyProcess_box.active").attr('id');
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100OptListAjax.do'/>",loadingShow:false},
			{prjId: prjId, processId: processId, flowId: selFlwId, type:"prj1100"});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		//에러 없는경우
		if(data.errorYN != "Y"){
			//추가 항목 목록 세팅
			if(!gfnIsNull(data.optList)){
				copyFlowOptGrid.setData(data.optList);
			}else{
				//결과 값 없는 경우 목록 초기화
				copyFlowOptGrid.setData([]);
			}
		}else{
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

//역할그룹 정보 조회
function fnCopyAuthRefresh(flowId,authGrpTargetCd,gridTarget){
	//선택 작업흐름 프로젝트 id
	var prjId = $(".copyProcess_box.active").attr('prjid');
	
	var processId = $(".copyProcess_box.active").attr('id');
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100FlowAuthGrpListAjax.do'/>","loadingShow":false},
			{prjId: prjId, processId: processId, flowId: flowId, authGrpTargetCd: authGrpTargetCd});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			gridTarget.setData(data.flowAuthGrpList);
		}else{
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

//프로세스 복사
function fnProcessCopyAction(prjId,processId){
	var selFlowData = $copyFlowChart.flowchart('getData');
	
	//프로세스 추가 정보
	var processNm = $(".copyProcess_box.active").attr('processnm');
	var processDesc = $(".copyProcess_box.active").attr('desc');
	var processOrd = $(".copyProcess_box.active").attr('ord');
	var processTypeCd = $(".copyProcess_box.active").attr('typecd');
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/insertPrj1100ProcessCopyInfoAjax.do'/>","loadingShow":true,"async":true},
			{processJsonData: JSON.stringify(selFlowData)
				,prjId :prjId
				,processId:processId
				,processNm: processNm
				,processDesc: processDesc
				,processOrd: processOrd
				,processTypeCd: processTypeCd
			});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러
		if(data.errorYN == "Y"){
			jAlert(toast.push(data.message),"경고");
			
			//팝업 창 닫기
			gfnLayerPopupClose();
		}else{
			toast.push(data.message);
			
			//프로세스 목록 조회
			fnProcessListAjax();
			
			//팝업 창 닫기
			gfnLayerPopupClose();
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


//flowchart zoom function
function fnFlowChartZoom(type){
	//줌 초기화
	if(type == "reset"){
		copyCurrentZoom=1;
		$copyFlowChart.flowchart('setPositionRatio', 1);
		$copyFlowChart.panzoom('reset');
		return false;
	}
	//줌 가능한 수치
	var possibleZooms = [2,1,0.75,0.5];
	
	//줌인
	if(type == "in"){
		copyCurrentZoom--;
		if(copyCurrentZoom < 0){
			copyCurrentZoom = 0;
		}
	}
	//줌아웃
	else if(type == "out"){
		copyCurrentZoom++;
		if(copyCurrentZoom > (possibleZooms.length-1)){
			copyCurrentZoom = (possibleZooms.length-1);
		}
	}
	
	$copyFlowChart.flowchart('setPositionRatio', possibleZooms[copyCurrentZoom]);
	        
    $copyFlowChart.panzoom('zoom',(possibleZooms[copyCurrentZoom]), {
        animate: true
    });
}
</script>

<div class="popup" style="height:100%;">
<form id="prj1104PopupFrm" name="prj1104PopupFrm" method="post">
	<div class="pop_title">작업흐름 항목 <span class="flowOpt_action_type">추가</span></div>
	<div class="pop_sub" style="padding:15px;display: inline-block;">
		<div class="prj1104_processCopy_mainBox">
			<div class="zoomBtn" guide="zoom">
				<span class="button_normal2" onclick="fnFlowChartZoom('reset')"><i class="fa fa-undo-alt"></i></span>
				<span class="button_normal2" onclick="fnFlowChartZoom('in')"><i class="fa fa-plus"></i></span>
				<span class="button_normal2" onclick="fnFlowChartZoom('out')"><i class="fa fa-minus"></i></span>
			</div>
			<div class="flw_box flw_main_leftBox">
				<div class="flw_box copyFlw_left_topBox">
					<span class="button_normal2" id="btn_insert_copyProSelect">조회</span>
				</div>
				<div class="flw_box copyFlw_left_bottomBox" id="processCopyBoxList">
					
				</div>
			</div>
			<div class="flw_box flw_main_rightBox" id="processCopyFlowParentDiv">
				<div id="processCopyFlowChartDiv">
				
				</div>
			</div>
			<div class="flw_box_clear"></div>
			<div class="flw_box flw_main_bottomBox">
				<div class="flw_content_mask" id="copyFlowContentMask">작업흐름을 선택해주세요.</div>
				<div class="flw_sub_box flw_bottom_leftBox" style="height: auto;">
					 <div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						작업흐름 명
					</div>
					<div class="flw_sub_box flw_sub3">
						<input type="text" name="copyFlowNm" id="copyFlowNm" readonly="readonly" style="border:1px solid #ccc;"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="선택 작업흐름을 필수 단계로 지정합니다.">
						필수
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowEssential" id="copyFlowEssential" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						제목 배경 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="copyFlowTitleBgColor" id="copyFlowTitleBgColor" value="#515769" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="다음 작업흐름 변경시 결재를 받도록 지정합니다.">
						결재 요청
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowSign" id="copyFlowSign" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						제목 글씨 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="copyFlowTitleColor" id="copyFlowTitleColor" value="#FFFFFF" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="결재 반려시 최종완료 작업흐름으로 변경되도록 합니다.">
						결재 반려시 종료
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowSignStop" id="copyFlowSignStop" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						내용 배경 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="copyFlowContentBgColor" id="copyFlowContentBgColor" value="#FFFFFF" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="다음 작업흐름 변경시 바로 최종완료 작업흐름으로 변경이 가능하도록 합니다.">
						종료 분기
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowEnd" id="copyFlowEnd" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						내용 글씨 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="copyFlowContentColor" id="copyFlowContentColor" value="#515769" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 리비전 번호를 입력 받도록 지정합니다.">
						리비전 저장
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowRevision" id="copyFlowRevision" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						배포 계획 저장
					</div>
					<div class="flw_sub_box flw_sub1">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowDpl" id="copyFlowDpl" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 리비전 번호를 입력 받도록 지정합니다.">
						허용 역할
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowAuth" id="copyFlowAuth" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 작업을 추가 할 수 있도록 지정합니다.">
						작업
					</div>
					<div class="flw_sub_box flw_sub1">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="copyFlowWork" id="copyFlowWork" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_sub_desc copyFlw_auth_frame">
						담당자</br>허용 역할 목록
					</div>
					<div class="flw_sub_box flw_sub3 flw_sub_desc flw_line_bottom_none copyFlw_auth_frame">
						<div data-ax5grid="copyAuth-grid" data-ax5grid-config="{}" style="height: 100px;"></div>	
					</div>
				</div>
				<div class="flw_sub_box flw_bottom_rightBox" style="height: auto;">
					<div class="flw_sub_box flw_sub_title flw_sub4 flw_line_left">
						추가 항목 내용
					</div>
					<div class="flw_sub_box flw_sub4" style="height: 240px;">
						<div data-ax5grid="copyFlw-grid" data-ax5grid-config="{}" style="height: 240px;"></div>	
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_sub_desc flw_line_left copyFlw_auth_frame">
						결재자</br>허용 역할 목록
					</div>
					<div class="flw_sub_box flw_sub3 flw_sub_desc flw_line_bottom_none copyFlw_auth_frame">
						<div data-ax5grid="copySignAuth-grid" data-ax5grid-config="{}" style="height: 100px;"></div>	
					</div>
				</div>
			</div>
		</div>
		<div class="flw_btn_box">
			<div class="button_complete" id="btn_copyProcess_save"><i class="fa fa-copy"></i>&nbsp;<span class="flowOpt_action_type">복사</span></div>
			<div class="button_complete" id="btn_close_flowClose"><i class="fa fa-times"></i>&nbsp;닫기</div>
		</div>
	</div>
</div>
</form>
</div>
</html>
