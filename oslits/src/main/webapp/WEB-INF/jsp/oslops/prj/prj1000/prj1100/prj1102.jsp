<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<style>
.dsh_flow_box_preview {position: relative;width: 200px;height: 65px;text-align: center;border-radius: 0 0 10px 10px;border: 3px solid transparent;margin: 30px 10px 0 10px;display:inline-block;background-color: #515769;color: #FFFFFF;}
.flowOptionDiv_preview {position: absolute;top: -24px;height: 24px;left: -3px;background-color: inherit;padding: 2px;border: 3px solid transparent;border-radius: 5px 5px 0 0;font-size: 10pt;border-bottom: 1px dashed #fff;width: 185px;text-align: left;}
.flow_box_title {width: 100%;height: 30px;line-height: 20px;font-weight: bold; padding-top: 5px;border-bottom: none;float:left;}
.flow_box_contents {width: 100%;height: 30px;border-radius: 0 0 8px 8px;line-height: 33px;background-color:#fff;color:#515769;float:left;}
</style>
<script>

//프로세스, 작업흐름 Id
var processId = "${processId}";
var flowId = "${flowId}";
var type = "${type}";


var flwOptGrid;
//공통코드 목록
$(document).ready(function() {
	
	//유효성 체크
	var arrChkObj = {"flowNm":{"type":"length","msg":"작업흐름 명은 100byte까지 입력이 가능합니다.",max:100}};
	gfnInputValChk(arrChkObj);
	
	//수정인경우 처리
	if(type == "update"){
		$(".flow_action_type").html("수정");
		
		//flowChart
		$flowchart = $('#flowChartDiv');
		
		//기존 데이터 불러오기
		var selFlowData = $flowchart.flowchart('getOperatorData',selFlowId);
		var selFlowPro = selFlowData.properties;
		
		//선택 작업흐름 정보 세팅
		$("#flowNm").val(selFlowPro.title);
		
		//제목,글씨 색상
		$("#flowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowTitleBgColor});
		$("#flowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowTitleColor});
		$("#flowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowContentBgColor});
		$("#flowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowContentColor});
		
		fnColorFixSet("title",{"background-color":selFlowPro.flowTitleBgColor});
		fnColorFixSet("title",{"color":selFlowPro.flowTitleColor});
		fnColorFixSet("content",{"background-color":selFlowPro.flowContentBgColor});
		fnColorFixSet("content",{"color":selFlowPro.flowContentColor});
		
		$("#flowTitleBgColor").val(selFlowPro.flowTitleBgColor);
		$("#flowTitleColor").val(selFlowPro.flowTitleColor);
		$("#flowContentBgColor").val(selFlowPro.flowContentBgColor);
		$("#flowContentColor").val(selFlowPro.flowContentColor);
		
		//체크 박스
		if(selFlowPro.flowEssential == "on"){	//필수
			$("#flowEssential")[0].checked = true;
			fnOptPreviewOnOff("optKey", true);	//아이콘 onOff
		}else{
			$("#flowEssential")[0].checked = false;
			fnOptPreviewOnOff("optKey", false);	//아이콘 onOff
		}
		if(selFlowPro.flowSign == "on"){		//결재
			$("#flowSign")[0].checked = true;
			fnOptPreviewOnOff("optSign", true);	//아이콘 onOff
		}else{
			$("#flowSign")[0].checked = false;
			fnOptPreviewOnOff("optSign", false);	//아이콘 onOff
		}
		if(selFlowPro.flowEnd == "on"){			//종료분기
			$("#flowEnd")[0].checked = true;
			fnOptPreviewOnOff("optEnd", true);	//아이콘 onOff
		}else{
			$("#flowEnd")[0].checked = false;
			fnOptPreviewOnOff("optEnd", false);	//아이콘 onOff
		}
		if(selFlowPro.flowWork == "on"){			//작업
			$("#flowWork")[0].checked = true;
			fnOptPreviewOnOff("optWork", true);	//아이콘 onOff
		}else{
			$("#flowWork")[0].checked = false;
			fnOptPreviewOnOff("optWork", false);	//아이콘 onOff
		}
		if(selFlowPro.flowRevision == "on"){			//리비전
			$("#flowRevision")[0].checked = true;
			fnOptPreviewOnOff("optRevision", true);	//아이콘 onOff
		}else{
			$("#flowRevision")[0].checked = false;
			fnOptPreviewOnOff("optRevision", false);	//아이콘 onOff
		}
		if(selFlowPro.flowDpl == "on"){			//배포 계획
			$("#flowDpl")[0].checked = true;
			fnOptPreviewOnOff("optDpl", true);	//아이콘 onOff
		}else{
			$("#flowDpl")[0].checked = false;
			fnOptPreviewOnOff("optDpl", false);	//아이콘 onOff
		}
		if(selFlowPro.flowAuth == "on"){			//허용역할
			$("#flowAuth")[0].checked = true;
			fnOptPreviewOnOff("optAuth", true);	//아이콘 onOff
			fnAuthGridOnOff();
			
			// 팝업 높이 변경
	        fnChangeFlowPopUp(true);
		}else{
			$("#flowAuth")[0].checked = false;
			fnOptPreviewOnOff("optAuth", false);	//아이콘 onOff
		}
		
		var processConfirmCd = $(".process_box.active").attr("confirm");
		//확정 처리인지 확인
		//확정 처리의 경우 기본 정보 외에 disabled
		if(processConfirmCd == "02"){
			$("#flowEssential").attr("disabled","disabled");
			$("#flowSign").attr("disabled","disabled");
			$("#flowEnd").attr("disabled","disabled");
			$("#flowWork").attr("disabled","disabled");
			$("#flowRevision").attr("disabled","disabled");
			$("#flowDpl").attr("disabled","disabled");
			$("#flowAuth").attr("disabled","disabled");
		}
	}else{
		//제목,글씨 색상
		$("#flowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
		$("#flowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
		$("#flowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
		$("#flowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
		
		//아이콘 숨기기
		$("#previewOpt > i").hide('fast');
	}
	
	//작업흐름 추가
	$("#btn_flow_save").click(function(){
		// 저장 전 유효성 체크
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;
		}
		
		//error있는경우 오류
		if($(".inputError").length > 0){
			jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
			$(".inputError")[0].focus()
			return false;
		}
		
		//유효성 체크
		var checkObjArr = ["flowNm"];
		var checkObjNmArr = ["작업흐름 명"]
	
		//유효성 체크
		var chkRtn = gfnRequireCheck("prj1102PopupFrm", checkObjArr, checkObjNmArr);
	
		//유효성 오류있는 경우
		if(chkRtn){
			return false;
		}
		
		//입력 폼값
		var flowInfoArray = $("#prj1102PopupFrm").serializeArray();
		
		//jsonArray to json Map
		var flowInfo = {};
		$.each(flowInfoArray,function(idx, map){
			flowInfo[map.name] = map.value;
		});
		
		//flowChart
		$flowchart = $('#flowChartDiv');
		
		//작업흐름 추가인경우
		if(type == "insert"){
	     	//작업흐름 저장
     		fnFlowSaveAjax("insert",flowInfo);
		}
		//작업흐름 수정
		else if(type == "update"){
			//작업흐름 저장
     		fnFlowSaveAjax("update",flowInfo);
		}else{
			jAlert("잘못된 접근입니다.","경고");
		}
     	
     	
	});
	
	//팝업 창 닫기
	$("#btn_close_flowClose").click(function(){
		//팝업 창 닫기
		gfnLayerPopupClose();
	});
	

	
	//칼라값 입력
	$("#flowTitleBgColor").change(function(){fnColorFixSet("title",{"background-color":$(this).val()});});
	$("#flowTitleColor").change(function(){fnColorFixSet("title",{"color":$(this).val()});});
	$("#flowContentBgColor").change(function(){fnColorFixSet("content",{"background-color":$(this).val()});});
	$("#flowContentColor").change(function(){fnColorFixSet("content",{"color":$(this).val()});});
	
	//체크박스 변경되는경우 미리보기 값 변경
	$(".optPreviewCdChg").click(function(){
		var opt = $(this).attr("opt");
		var chkVal = this.checked;
		
		//아이콘 onOff
		fnOptPreviewOnOff(opt,chkVal);
	});
	

	// 허용역할 체크에 따른 팝업 높이 변경
	$("#flowAuth").click(function(){
		// 허용역할 체크값을 가져온다
		var isAuthChk = $("#flowAuth").is(":checked");
		// 팝업 높이 변경
        fnChangeFlowPopUp(isAuthChk);
    });
});

//미리보기 작업흐름 아이콘 onOff
function fnOptPreviewOnOff(id, onOff){
	//show
	if(onOff){
		$("#previewOpt.flowOptionDiv_preview > i#"+id).show({"display":"inline-block"});
	}else{ //hide
		$("#previewOpt.flowOptionDiv_preview > i#"+id).hide({"display":"none"});
	}
}
		
//작업흐름 색상 수정시 미리보기
function fnColorFixSet(type,css){
	//제목
	if(type == "title"){
		$("#preview_flowBox .dsh_flow_box_preview").css(css);
	}
	//내용
	else if(type == "content"){
		$("#preview_flowBox .flow_box_contents").css(css);
	}
}
//작업흐름 추가 or 수정
function fnFlowSaveAjax(type,flowInfo){
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus()
		return false;
	}
	
	//프로세스 ID
	var processId = $(".process_box.active").attr('id');
	
	//확정 처리 확인
	var processConfirmCd = $(".process_box.active").attr('confirm');
	
	//insert flow 추가
	if(type == "insert"){
		//작업흐름 Id 세팅
		var newflowId = 'F'+new Date().format('yyMMddHHmmssms');
		flowInfo.flowId = newflowId;
		
		var operatorData = {
			top: 200,
			left: 500,
			properties: {
				title: flowInfo.flowNm,
				inputs: {input_1: {label: '이전'}},
				outputs: {output_1: {label: '다음'}},
				flowTitleBgColor:flowInfo.flowTitleBgColor,
				flowTitleColor:flowInfo.flowTitleColor,
				flowContentBgColor:flowInfo.flowContentBgColor,
				flowContentColor:flowInfo.flowContentColor,
				flowEssential:flowInfo.flowEssential,
				flowSign:flowInfo.flowSign,
				flowSignStop:flowInfo.flowSignStop,
				flowEnd:flowInfo.flowEnd,
				flowWork:flowInfo.flowWork,
				flowRevision:flowInfo.flowRevision,
				flowDpl:flowInfo.flowDpl,
				flowAuth:flowInfo.flowAuth,
				flowOptCnt:0
			}
		};
     	$flowchart.flowchart('createOperator', newflowId, operatorData);
	}
	
	var flowGetData = $flowchart.flowchart('getData');
	
	//기존 데이터 불러오기
	var selFlowData = $flowchart.flowchart('getOperatorData',selFlowId);
	var selFlowPorp = selFlowData.properties;
	
	//필수, 결재, 종료분기 체크박스 세팅
	var flowEssentialCd = null;
	var flowSignCd = null;
	var flowSignStopCd = null;
	var flowEndCd = null;
	var flowWorkCd = null;
	var flowRevisionCd = null;
	var flowDplCd = null;
	var flowAuthCd = null;
   		
	if(processConfirmCd != "02"){
   		//필수
		if(typeof flowInfo.flowEssential != "undefined" && flowInfo.flowEssential == "on"){
			flowEssentialCd = "01";
		}else{
			flowEssentialCd = "02";
		}
   		
   		//결재
   		if(typeof flowInfo.flowSign != "undefined" && flowInfo.flowSign == "on"){
			flowSignCd = "01";
		}else{
			flowSignCd = "02";
		}
   		
   		//종료
   		if(typeof flowInfo.flowEnd != "undefined" && flowInfo.flowEnd == "on"){
			flowEndCd = "01";
		}else{
			flowEndCd = "02";
		}
   		
   		//작업
   		if(typeof flowInfo.flowWork != "undefined" && flowInfo.flowWork == "on"){
			flowWorkCd = "01";
		}else{
			flowWorkCd = "02";
		}
   		
   		//리비전
   		if(typeof flowInfo.flowRevision != "undefined" && flowInfo.flowRevision == "on"){
			flowRevisionCd = "01";
		}else{
			flowRevisionCd = "02";
		}
   		
   		//배포 계획
   		if(typeof flowInfo.flowDpl != "undefined" && flowInfo.flowDpl == "on"){
			flowDplCd = "01";
		}else{
			flowDplCd = "02";
		}
   		
   		//허용 역할
   		if(typeof flowInfo.flowAuth != "undefined" && flowInfo.flowAuth == "on"){
   			flowAuthCd = "01";
		}else{
			flowAuthCd = "02";
		}
	}else{
		//필수
		if(typeof selFlowPorp.flowEssential != "undefined" && selFlowPorp.flowEssential == "on"){
			flowEssentialCd = "01";
		}else{
			flowEssentialCd = "02";
		}
   		
   		//결재
   		if(typeof selFlowPorp.flowSign != "undefined" && selFlowPorp.flowSign == "on"){
			flowSignCd = "01";
		}else{
			flowSignCd = "02";
		}
   		
   		//종료
   		if(typeof selFlowPorp.flowEnd != "undefined" && selFlowPorp.flowEnd == "on"){
			flowEndCd = "01";
		}else{
			flowEndCd = "02";
		}
   		
   		//작업
   		if(typeof selFlowPorp.flowWork != "undefined" && selFlowPorp.flowWork == "on"){
			flowWorkCd = "01";
		}else{
			flowWorkCd = "02";
		}
   		
   		//리비전
   		if(typeof selFlowPorp.flowRevision != "undefined" && selFlowPorp.flowRevision == "on"){
			flowRevisionCd = "01";
		}else{
			flowRevisionCd = "02";
		}
   		
   		//배포 계획
   		if(typeof selFlowPorp.flowDpl != "undefined" && selFlowPorp.flowDpl == "on"){
			flowDplCd = "01";
		}else{
			flowDplCd = "02";
		}
   		

   		//허용 역할
   		if(typeof selFlowPorp.flowAuth != "undefined" && selFlowPorp.flowAuth == "on"){
   			flowAuthCd = "01";
		}else{
			flowAuthCd = "02";
		}
	}
  		var url = null;
  		
  		if(type == "insert"){
  			url = "<c:url value='/prj/prj1000/prj1100/insertPrj1100FlowInfoAjax.do'/>";
  		}else if(type == "update"){
  			url = "<c:url value='/prj/prj1000/prj1100/updatePrj1100FlowInfoAjax.do'/>"
  			flowInfo.flowId = flowId;
  		}
  		
  		if(gfnIsNull(url)){
  			jAlert("url 오류가 발생했습니다.","알림");
  		}
  		
  		//return false;
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":url,"async":true},
			{processJsonData:JSON.stringify(flowGetData)
				,processId: processId
				,flowId :flowInfo.flowId
				,flowNm :flowInfo.flowNm
				,flowTitleBgColor :flowInfo.flowTitleBgColor
				,flowTitleColor :flowInfo.flowTitleColor
				,flowContentBgColor :flowInfo.flowContentBgColor
				,flowContentColor :flowInfo.flowContentColor
				,flowEssentialCd :flowEssentialCd
				,flowSignCd :flowSignCd
				,flowSignStopCd :flowSignStopCd
				,flowEndCd :flowEndCd
				,flowWorkCd :flowWorkCd
				,flowRevisionCd :flowRevisionCd
				,flowDplCd :flowDplCd
				,flowAuthCd :flowAuthCd
			});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러
		if(data.errorYN == "Y"){
			//data 복구
			$flowchart.flowchart('setData',JSON.parse(processJsonList[processId]));
			toast.push(data.message);
		}else{
			//작업흐름 수정인경우
			if(type == "update"){
				
				var processConfirmCd = $(".process_box.active").attr("confirm");
				
				//프로세스 확정 처리인경우 체크박스 값은 그대로 유지
				if(processConfirmCd == "02") {
					flowInfo.flowEssential = selFlowPorp.flowEssential;
					flowInfo.flowSign = selFlowPorp.flowSign;
					flowInfo.flowSignStop = selFlowPorp.flowSignStop;
					flowInfo.flowEnd = selFlowPorp.flowEnd;
					flowInfo.flowWork = selFlowPorp.flowWork;
					flowInfo.flowRevision = selFlowPorp.flowRevision;
					flowInfo.flowDpl = selFlowPorp.flowDpl;
					flowInfo.flowAuth = selFlowPorp.flowAuth;
				}
				
				//추가 항목 갯수
				var flowOptCnt = selFlowPorp.flowOptCnt;
				if(gfnIsNull(flowOptCnt) || flowOptCnt < 0){flowOptCnt = 0;}
				
				//수정 데이터
				var operatorData = {
					top: selFlowData.top,
					left: selFlowData.left,
					properties: {
						title: flowInfo.flowNm,
						inputs: {input_1: {label: '이전'}},
						outputs: {output_1: {label: '다음'}},
						flowTitleBgColor:flowInfo.flowTitleBgColor,
						flowTitleColor:flowInfo.flowTitleColor,
						flowContentBgColor:flowInfo.flowContentBgColor,
						flowContentColor:flowInfo.flowContentColor,
						flowNextId:selFlowData.properties.flowNextId,
						flowEssential:flowInfo.flowEssential,
						flowSign:flowInfo.flowSign,
						flowSignStop:flowInfo.flowSignStop,
						flowEnd:flowInfo.flowEnd,
						flowWork:flowInfo.flowWork,
						flowRevision:flowInfo.flowRevision,
						flowDpl:flowInfo.flowDpl,
						flowAuth:flowInfo.flowAuth,
						flowOptCnt:flowOptCnt
					}
				};
				
				//작업흐름 Id 세팅
				$flowchart.flowchart('setOperatorData', selFlowId, operatorData);
				flowGetData = $flowchart.flowchart('getData');
				
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"/prj/prj1000/prj1100/updatePrj1100ProcessInfoAjax.do","async":true},
						{processJsonData:JSON.stringify(flowGetData)
							,processId:processId
						});
				//AJAX 전송
				ajaxObj.send();
				
				$flowchart.flowchart('unselectOperator')
			}
			toast.push(data.message);
			
			//데이터 갱신
			processJsonList[processId] = JSON.stringify(flowGetData);
			
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

//색상 자동 변경
function selColorChgGo(titleBg, contentBg){
	fnColorFixSet("title",{"background-color":titleBg});
	fnColorFixSet("title",{"color":"#fff"});
	fnColorFixSet("content",{"background-color":contentBg});
	fnColorFixSet("content",{"color":titleBg});
	
	//제목,글씨 색상
	$("#flowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: titleBg});
	$("#flowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#fff"});
	$("#flowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: contentBg});
	$("#flowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: titleBg});
	
	//실제 값 변경
	$("#flowTitleBgColor").val(titleBg);
	$("#flowTitleColor").val("#fff");
	$("#flowContentBgColor").val(contentBg);
	$("#flowContentColor").val(titleBg);
}


//역할 그룹 그리드
function fnAuthListGrid(){
	authGrid = new ax5.ui.grid();

  authGrid.setConfig({
      target: $('[data-ax5grid="auth-grid"]'),
      sortable:true,
      showRowSelector: true,
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
  
  signAuthGrid = new ax5.ui.grid();

  signAuthGrid.setConfig({
      target: $('[data-ax5grid="sign-auth-grid"]'),
      sortable:true,
      showRowSelector: true,
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

//역할그룹 정보 조회
function fnAuthRefresh(authGrpTargetCd,gridTarget){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100FlowAuthGrpListAjax.do'/>","loadingShow":false},
			{processId: processId, flowId: flowId, authGrpTargetCd: authGrpTargetCd});
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

//역할 그룹 선택
function fnAuthGridOnOff(){
	//체크된경우 그리드 표시
	if($("#flowAuth")[0].checked){
		$(".flow_auth_gridFrame").show();
		
		//그리드 세팅 및 정보 불러오기
		fnAuthListGrid();
		// 수정일 경우 그리드 데이터 조회
		if(type == "update"){
			fnAuthRefresh('01',authGrid);
			fnAuthRefresh('02',signAuthGrid);
		}
	}else{ //그리드 감추기
		$(".flow_auth_gridFrame").hide();
	}
}

//역할 추가
function fnAuthInsert(authGrpTargetCd){
	//작업흐름 추가인경우 역할 추가 불가
	if(gfnIsNull(flowId)){
		jAlert("작업흐름 추가 이후 역할 추가&삭제가 가능합니다.","알림");
		return false;
	}
	
	var processConfirmCd = $(".process_box.active").attr("confirm");
	//확정 처리인지 확인
	//확정 처리의 경우 기본 정보 외에 disabled
	if(processConfirmCd == "02"){
		jAlert("프로세스가 확정상태인경우 역할그룹 추가가 불가능합니다.","알림");
		return false;
	}else{
		//cmm1700 공통팝업 호출
		gfnCommonAuthPopup("", "" ,true,function(objs){
			if(objs.length>0){
				var selAuthFd = new FormData();
				
				//기본 정보 대입
				selAuthFd.append("processId",processId);
				selAuthFd.append("flowId",flowId);
				selAuthFd.append("authGrpTargetCd",authGrpTargetCd);
				
				//callback 선택 역할그룹 대입
				for(var i=0; i<objs.length; i++){
					selAuthFd.append("selAuth",JSON.stringify({authGrpId: objs[i].authGrpId, authGrpTargetCd: authGrpTargetCd}));
				}
				
				//선택 역할그룹갯수 넘기기
				selAuthFd.append("selAuthCnt",objs.length);
				
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/prj/prj1000/prj1100/insertPrj1100FlowAuthGrpList.do'/>"
							,"contentType":false
							,"processData":false
							,"cache":false},
						selAuthFd)
				
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);
					
					//에러 없는경우
					if(data.errorYN != "Y"){
						//리비전 정보 갱신
						fnAuthRefresh('01',authGrid);
						fnAuthRefresh('02',signAuthGrid);
					}
					jAlert(data.message,"알림");
				});
				
				//AJAX 전송 오류 함수
				ajaxObj.setFnError(function(xhr, status, err){
					data = JSON.parse(data);
					jAlert(data.message, "알림");
			 	});
				//AJAX 전송
				ajaxObj.send();
			}
		});
	}
}

//역할그룹 삭제
function fnAuthDelete(authGrpTargetCd){
	var processConfirmCd = $(".process_box.active").attr("confirm");
	//확정 처리인지 확인
	//확정 처리의 경우 기본 정보 외에 disabled
	if(processConfirmCd == "02"){
		jAlert("프로세스가 확정상태인경우 역할그룹 삭제가 불가능합니다.","알림");
		return false;
	}
	
	var chkList = authGrid.getList('selected');
	if(authGrpTargetCd == "02"){
		chkList = signAuthGrid.getList('selected');
	}
	
	if(gfnIsNull(chkList)){
		jAlert("선택한 역할그룹이 없습니다.","알림창");
		return false;
	}
	
	var selAuthFd = new FormData();
	//기본 정보 대입
	selAuthFd.append("processId",processId);
	selAuthFd.append("flowId",flowId);
	selAuthFd.append("authGrpTargetCd",authGrpTargetCd);
	
	//callback 선택 역할그룹 대입
	$.each(chkList,function(idx, map){
		selAuthFd.append("selAuth",JSON.stringify({authGrpId: map.authGrpId, authGrpTargetCd: authGrpTargetCd}));
	});
	
	//선택 역할그룹 갯수 넘기기
	selAuthFd.append("selAuthCnt",chkList.length);
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/deletePrj1100FlowAuthGrpList.do'/>"
				,"contentType":false
				,"processData":false
				,"cache":false},
			selAuthFd)
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//리비전 정보 갱신
			fnAuthRefresh('01',authGrid);
			fnAuthRefresh('02',signAuthGrid);
		}
		jAlert(data.message,"알림");
	});
	
	//AJAX 전송
	ajaxObj.send();
}

//허용역할 체크에 따라 팝업 높이 변경
function fnChangeFlowPopUp(isAuthChk){
	// 허용역할 체크이면
	if(isAuthChk){
		$(".layer_popup_box").height(820);
	// 허용역할 체크가 아닐 경우	
	}else{
		$(".layer_popup_box").height(620);
	}
}
</script>

<div class="popup" style="height:100%;">
<form id="prj1102PopupFrm" name="prj1102PopupFrm" method="post">
	<div class="pop_title">작업흐름 <span class="flow_action_type">추가</span></div>
	<div class="pop_sub" style="padding:15px;display: inline-block;">
		<div class="flw_box_preview" id="preview_flowBox">
			<div class="dsh_flow_box_preview" style="background-color: #515769;color: #FFFFFF;">
				<div class="flowOptionDiv_preview" id="previewOpt" style="width: 200px;">
					<i class="fa fa-key previewOpt" title="필수" id="optKey"></i>
					<i class="fa fa-file-signature previewOpt" title="결재" id="optSign"></i>
					<i class="far fa-stop-circle previewOpt" title="결재 반려종료" id="optSignStop"></i>
					<i class="fa fa-sign-out-alt previewOpt" title="종료 분기" id="optEnd"></i>
					<i class="fa fa-code-branch previewOpt" title="작업" id="optWork"></i>
					<i class="fa fa-code previewOpt" title="리비전 저장유무" id="optRevision"></i>
					<i class="fa fa-puzzle-piece previewOpt" title="배포계획 저장유무" id="optDpl"></i>
					<i class="fa fa-user-shield previewOpt" title="허용 역할 제한" id="optAuth"></i>
				</div>
				<div class="flow_box_title">작업흐름 미리보기</div>
				<div class="flow_box_contents" style="background-color: #FFFFFF;color: #515769;">작업흐름 내용</div>
			</div>
		</div>
		<div class="flw_box_colorSet">
			<div class="selColorBox" onclick="selColorChgGo('#515769','#FFFFFF')" style="background: linear-gradient(#515769 50%, #FFFFFF 50%);"><span>T</span></br><span style="color:#515769;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#4b73eb','#FAFAFA')" style="background: linear-gradient(#4b73eb 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#4b73eb;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#936de9','#FAFAFA')" style="background: linear-gradient(#936de9 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#936de9;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#ff5643','#FAFAFA')" style="background: linear-gradient(#ff5643 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#ff5643;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#58c3e5','#FAFAFA')" style="background: linear-gradient(#58c3e5 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#58c3e5;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#fba450','#FAFAFA')" style="background: linear-gradient(#fba450 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#fba450;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#eb4ba4','#FAFAFA')" style="background: linear-gradient(#eb4ba4 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#eb4ba4;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#89eb4b','#FAFAFA')" style="background: linear-gradient(#89eb4b 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#89eb4b;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#c4eb4b','#FAFAFA')" style="background: linear-gradient(#c4eb4b 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#c4eb4b;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#0d927b','#FAFAFA')" style="background: linear-gradient(#0d927b 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#0d927b;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#260d92','#FAFAFA')" style="background: linear-gradient(#260d92 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#260d92;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#5b0d92','#FAFAFA')" style="background: linear-gradient(#5b0d92 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#5b0d92;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#920d30','#FAFAFA')" style="background: linear-gradient(#920d30 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#920d30;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#ff7407','#FAFAFA')" style="background: linear-gradient(#ff7407 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#ff7407;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#4c07ff','#FAFAFA')" style="background: linear-gradient(#4c07ff 50%, #FAFAFA 50%);"><span>T</span></br><span style="color:#4c07ff;">T</span></div>
			<div class="selColorBox" onclick="selColorChgGo('#000000','#FFFFFF')" style="background: linear-gradient(#000000 50%, #FFFFFF 50%);"><span>T</span></br><span style="color:#000000;">T</span></div>
		</div>
		<div class="flw_pop_box">
			<div class="flw_sub_box flw_sub_title flw_sub4">
				작업흐름 내용
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				작업흐름 명 (*)
			</div>
			<div class="flw_sub_box flw_sub3">
				<input type="text" name="flowNm" id="flowNm" placeholder="작업흐름명을 입력해주세요."/>
			</div>
			<div class="flw_box_clear"></div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="선택 작업흐름을 필수 단계로 지정합니다.">
				필수
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<div class="flw_chk"> 
					<input type="checkbox" title="체크박스" name="flowEssential" id="flowEssential" class="optPreviewCdChg" opt="optKey"/><label></label>
				</div>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				제목 배경 색상
			</div>
			<div class="flw_sub_box flw_sub1">
				<input type="text" name="flowTitleBgColor" id="flowTitleBgColor" value="#515769"/>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="다음 작업흐름 변경시 결재를 받도록 지정합니다.">
				결재 요청
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<div class="flw_chk"> 
					<input type="checkbox" title="체크박스" name="flowSign" id="flowSign" class="optPreviewCdChg" opt="optSign"/><label></label>
				</div>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				제목 글씨 색상
			</div>
			<div class="flw_sub_box flw_sub1">
				<input type="text" name="flowTitleColor" id="flowTitleColor" value="#FFFFFF"/>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1">
			
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
			
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				내용 배경 색상
			</div>
			<div class="flw_sub_box flw_sub1">
				<input type="text" name="flowContentBgColor" id="flowContentBgColor" value="#FFFFFF"/>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="다음 작업흐름 변경시 바로 최종완료 작업흐름으로 변경이 가능하도록 합니다.">
				종료 분기
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<div class="flw_chk"> 
					<input type="checkbox" title="체크박스" name="flowEnd" id="flowEnd" class="optPreviewCdChg" opt="optEnd"/><label></label>
				</div>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				내용 글씨 색상
			</div>
			<div class="flw_sub_box flw_sub1">
				<input type="text" name="flowContentColor" id="flowContentColor" value="#515769"/>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 담당자 허용 역할그룹을 제한합니다.">
				허용 역할 제한
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<div class="flw_chk"> 
					<input type="checkbox" title="체크박스" name="flowAuth" id="flowAuth" class="optPreviewCdChg" opt="optAuth" onchange="fnAuthGridOnOff()"/><label></label>
				</div>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 작업을 추가 할 수 있도록 지정합니다.">
				작업
			</div>
			<div class="flw_sub_box flw_sub1">
				<div class="flw_chk"> 
					<input type="checkbox" title="체크박스" name="flowWork" id="flowWork" class="optPreviewCdChg" opt="optWork"/><label></label>
				</div>
			</div>
			<c:choose>
				<c:when test="${empty svnkitModuleUseChk or svnkitModuleUseChk == true }">
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 리비전 번호를 입력 받도록 지정합니다.">
						리비전 저장
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="flowRevision" id="flowRevision"/><label></label>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 리비전 번호를 입력 받도록 지정합니다.">
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<input type="hidden" title="체크박스" name="flowRevision" id="flowRevision"/>
					</div>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${empty jenkinsModuleUseChk or jenkinsModuleUseChk == true }">
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 베포계획을 배정합니다.">
						배포 계획 저장
					</div>
					<div class="flw_sub_box flw_sub1">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="flowDpl" id="flowDpl" class="optPreviewCdChg" opt="optDpl"/><label></label>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
					</div>
					<div class="flw_sub_box flw_sub1">
							<input type="hidden" title="체크박스" name="flowDpl" id="flowDpl" class="optPreviewCdChg" opt="optDpl"/>
					</div>
				</c:otherwise>
			</c:choose>
			<div class="flow_auth_gridFrame">
				<div class="flw_sub_box flw_sub_desc flw_sub_title flw_sub1">
					담당자 허용 역할 목록
				</div>
				<div class="flw_sub_box flw_sub_desc flw_sub2 flw_line_right">
					<div data-ax5grid="auth-grid" data-ax5grid-config="{}" style="height: 100px;"></div>	
				</div>
				<div class="flw_sub_box flw_sub1 flw_sub_desc" style="padding: 5px;">
					<div class="button_authBtn" id="btn_insert_auth" onclick="fnAuthInsert('01');"><i class="fa fa-save"></i>&nbsp;역할 추가</div>
					<div class="button_authBtn" id="btn_delete_auth" onclick="fnAuthDelete('01');"><i class="fa fa-times"></i>&nbsp;선택 삭제</div>
				</div>
				<div class="flw_sub_box flw_sub_desc flw_sub_title flw_sub1">
					결재자 허용 역할 목록
				</div>
				<div class="flw_sub_box flw_sub_desc flw_sub2 flw_line_right">
					<div data-ax5grid="sign-auth-grid" data-ax5grid-config="{}" style="height: 100px;"></div>	
				</div>
				<div class="flw_sub_box flw_sub1 flw_sub_desc" style="padding: 5px;">
					<div class="button_authBtn" id="btn_insert_auth" onclick="fnAuthInsert('02');"><i class="fa fa-save"></i>&nbsp;역할 추가</div>
					<div class="button_authBtn" id="btn_delete_auth" onclick="fnAuthDelete('02');"><i class="fa fa-times"></i>&nbsp;선택 삭제</div>
				</div>
			</div>
		</div>
		<div class="flw_btn_box">
			<div class="button_complete" id="btn_flow_save"><i class="fa fa-save"></i>&nbsp;<span class="flow_action_type">추가</span></div>
			<div class="button_complete" id="btn_close_flowClose"><i class="fa fa-times"></i>&nbsp;닫기</div>
		</div>
	</div>
</div>
</form>
</div>
</html>
