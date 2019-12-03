<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />
<script src="<c:url value='/js/common/spectrum.js'/>" ></script>
<script src="<c:url value='/js/flowchart/jquery.flowchart.js'/>"></script>
<script src="<c:url value='/js/panzoom/jquery.panzoom.min.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/common/spectrum.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/flowchart/jquery.flowchart.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslops/flw.css'/>' type='text/css'>
<script>
//ie 함수 재정의
if (!('remove' in Element.prototype)) {
    Element.prototype.remove = function() {
        if (this.parentNode) {
            this.parentNode.removeChild(this);
        }
    };
}
var ax5Mask = new ax5.ui.mask();

//팝업 타입
var popupType = "";

//프로세스 JSON 데이터 보관
var processJsonList = {};

//작업흐름 lib target
var $flowchart;

//선택 작업흐름 Id
var selFlowId = null;

//zoom
var currentZoom = 1;
$(function(){
	//가이드 상자 호출
	gfnGuideStack("add",fnGuideShow);
	
	$("#previewFlowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
	$("#previewFlowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#previewFlowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#previewFlowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
	
	//추가 항목 관리 목록
	fnAxGrid5View();
	
	/************* 프로세스 제어 시작 **/
	//프로세스 목록 불러오기
	fnProcessListAjax();
	
	//프로세스 조회 버튼
	$("#btn_insert_proSelect").click(function(){
		fnProcessListAjax();
	});
	
	//프로세스 추가 버튼
	$("#btn_insert_proInsert").click(function(){
		//팝업창 오픈
		var data = {popupGb: "insert"};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1105View.do", data, '550', '470','scroll');
	});
	
	//프로세스 수정 버튼
	$("#btn_update_proUpdate").click(function(){
		if($(".process_box.active").length == 0){
			jAlert("프로세스를 선택해주세요.","알림");
			return true;	
		}
		
		var selProcessId = $(".process_box.active")[0].id;
		
		//팝업창 오픈
		var data = {processId: selProcessId , popupGb: "update"};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1105View.do", data, '550', '470','scroll');
	});
	
	//프로세스 삭제 버튼
	$("#btn_delete_proDelete").click(function(){
		//확정 프로세스 삭제 불가
		var processConfirmCd = $(".process_box.active").attr("confirm");
		if(processConfirmCd == "02"){
			jAlert("확정처리된 프로세스는 삭제가 불가능합니다.","알림");
			return false;
		}
		
		jConfirm("프로세스를 삭제하시겠습니까?", "알림", function( result ) {
 			if( result ){
 				var processId = $(".process_box.active").attr('id');
 				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/prj/prj1000/prj1100/deletePrj1100ProcessInfoAjax.do'/>"},
						{processId:processId});
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);
					
					toast.push(data.message);
					
					//에러 없는경우
					if(data.errorYN != "Y"){
		        		//프로세스 목록 재 조회
		        		fnProcessListAjax();
		        		
						$("#btn_insert_plcCancle").click();
						
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
		});
	});
	
	//프로세스 확정 버튼 - (확정시 작업흐름명, 색상, 위치만 수정 가능)
	$("#btn_update_proConfirm").click(function(){
		var processConfirmCd = $(".process_box.active").attr("confirm");

		//이미 확정처리 되있는 경우
		if(processConfirmCd == "02"){
			jAlert("이미 확정처리된 프로세스입니다.","알림");
		}else{
			jConfirm("프로세스를 확정하시겠습니까?<br>확정된 프로세스의 작업흐름은 기본 정보만 수정이 가능합니다.<br>(작업흐름명, 색상, 위치)", "알림", function( result ) {
	 			if( result ){
	 				fnProcessConfirm();
	 			}
			});	
		}
	});
	
	//프로세스 확정 취소 버튼
	$("#btn_update_proConfirmCancel").click(function(){
		var processConfirmCd = $(".process_box.active").attr("confirm");

		//확정처리 되있는 경우만
		if(processConfirmCd == "01"){
			jAlert("미확정처리된 프로세스입니다.","알림");
		}else{
			jConfirm("프로세스를 확정 취소하시겠습니까?<br>1개 이상의 요구사항이 배정되어있으면 취소가 불가능합니다.", "알림", function( result ) {
	 			if( result ){
	 				fnProcessConfirmCancel();
	 			}
			});	
		}
	});
	
	//프로세스 복사 버튼
	$("#btn_delete_proCopy").click(function(){
		//팝업창 오픈
		var data = {};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1104View.do", data, '1330', '970','auto');
	});
	
	/************* 작업흐름 제어 시작 **/
	//작업흐름 추가
	$("#btn_insert_flwInsert").click(function(){
		//프로세스 Id
		var processId = $(".process_box.active").attr('id');
		
		//확정 프로세스 추가 불가
		var processConfirmCd = $(".process_box.active").attr("confirm");
		if(processConfirmCd == "02"){
			jAlert("확정처리된 프로세스는 추가가 불가능합니다.","알림");
			return false;
		}
		
		//팝업창 오픈
		var data = {processId: processId, type: "insert"};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1102View.do", data, '1100', '610','scroll');
	});
	
	//작업흐름 수정
	$("#btn_update_flwUpdate").click(function(){
		if(gfnIsNull(selFlowId)){
			jAlert("작업흐름을 선택해주세요.","알림");
			return false;
		}
	
		//프로세스 Id
		var processId = $(".process_box.active").attr('id');
		
		//팝업창 오픈
		var data = {processId: processId, flowId: selFlowId, type: "update"};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1102View.do", data, '1100', '610','scroll');
	});
	
	//선택 작업흐름&링크 제거
	$("#btn_delete_flwDelete").click(function(){
		//확정 처리된 프로세스인지 확인
		var processConfirmCd = $(".process_box.active").attr("confirm");
		
		if(processConfirmCd == "02"){
			jAlert("확정처리된 프로세스의 작업흐름은 삭제가 불가능합니다.","알림");
			return false;
		}
		
		if(gfnIsNull(selFlowId)){
			jAlert("작업흐름을 선택해주세요.","알림");
			return false;
		}
		jConfirm("작업흐름을 삭제하시겠습니까?<br>연결된 링크가 함께 삭제됩니다.", "알림", function( result ) {
 			if( result ){
 				fnFlowDeleteAjax();
 			}
		});
	});
	
	//선택 작업흐름 링크 제거
	$("#btn_delete_flwLinkDelete").click(function(){
		//확정 처리된 프로세스인지 확인
		var processConfirmCd = $(".process_box.active").attr("confirm");
		
		if(processConfirmCd == "02"){
			jAlert("확정처리된 프로세스의 작업흐름 링크는 삭제가 불가능합니다.","알림");
			return false;
		}
		
		if(gfnIsNull(selFlowId)){
			jAlert("작업흐름을 선택해주세요.","알림");
			return false;
		}
		
		jConfirm("선택 작업흐름의 링크를 삭제하시겠습니까?", "알림", function( result ) {
 			if( result ){
 				//flowchart data
				var flowData = $flowchart.flowchart('getData');
				
				//links data
				var flowLinkData = flowData.links;
				
				var delChk = -1;
				//링크 Id 구하기
				$.each(flowLinkData,function(idx, map){
					if(map.fromOperator == selFlowId){
						delChk = idx;
						return false;
					}
				});
				
				//링크 있는지 체크
				if(delChk >= 0){
					$flowchart.flowchart('deleteLink',delChk);
				}else{
					jAlert("연결된 링크가 없습니다.","경고");
					return false;
				}
				
				
 			}
		});
		
	});
	
	//선택 작업흐름 추가항목 관리
	$("#btn_insert_flwItemInsert").click(function(){
		//확정 처리된 프로세스인지 확인
		var processConfirmCd = $(".process_box.active").attr("confirm");
		/* 
		if(processConfirmCd == "02"){
			jAlert("확정처리된 프로세스의 작업흐름은 추가항목 관리가 불가능합니다.","알림");
			return false;
		}
		 */
		//선택 작업흐름 Id
		if(gfnIsNull(selFlowId)){
			jAlert("작업흐름을 선택해주세요.","알림");
			return false;
		}
		
		//프로세스 Id
		var processId = $(".process_box.active").attr('id');
		
		//팝업창 오픈
		var data = {processId: processId, flowId: selFlowId};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1101View.do", data, '1100', '600','scroll');
	});
	
});

//프로세스 삽입 또는 수정
function fnProcessSaveAjax(){
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus()
		return false;
	}
	
	var processId = "";
	var processConfirmCd = "";
	var url = "";
	var queryJson = {};
	
	
	if(popupType == "insert"){ //삽입
		url = "<c:url value='/prj/prj1000/prj1100/insertPrj1100ProcessInfoAjax.do'/>"
		processConfirmCd = "01";
	}else if(popupType == "update"){ //수정
		url = "<c:url value='/prj/prj1000/prj1100/updatePrj1100ProcessInfoAjax.do'/>"
		processId = $(".process_box.active").attr('id');
	}else{
		jAlert("잘못된 요청입니다.","경고");
	}
	
	//값 체크
	var processNm = $("#processNm").val();
	if(!gfnIsNull(processNm)){
		//프로세스 설명 - 4000byte로 짜르기
		var processDesc = encodeURI(gfnCutStrLen($("#processDesc").val(),4000));

		//프로세스 순서
		var processOrd = $("#processOrd").val();
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":url},
				{processNm:processNm,processId:processId,processDesc: processDesc, processOrd: processOrd, processConfirmCd: processConfirmCd});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			toast.push(data.message);
			
			//에러 없는경우
			if(data.errorYN != "Y"){
				//업데이트인경우
				if(popupType == "update"){
					$(".process_box.active").html(processNm);
					$(".process_box.active").attr("desc",processDesc);
					$(".process_box.active").attr("ord",processOrd);
				}else{
	        		//프로세스 목록 재 조회
	        		fnProcessListAjax();
				}
				
				$("#btn_insert_plcCancle").click();
			}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림");
	 	});
		//AJAX 전송
		ajaxObj.send();
	}else{
		jAlert("프로세스명을 입력해주세요.","알림",function(){
			//입력 포커스
			$("#processNm").focus();
		});
		return false;
	}
}

//프로세스 확정 처리
function fnProcessConfirm(){
	//프로세스 id
	var processId = $(".process_box.active").attr('id');
	
	//현재 프로세스 데이터
	var flowGetData = $flowchart.flowchart('getData');
	
	var linkChk = 0;
	
	//연결 마지막 작업흐름 Id 구하기
    var flowKeys = Object.keys(flowGetData.operators);
	
	//생성된 작업흐름이 없는 경우 중단
	if(flowKeys == 0){
		jAlert("1개 이상의 작업흐름이 필요합니다.","경고");
		return false;
	}
    var lastFlowIdx = null;
    var lastFlowId = null;
    
    //작업흐름 Loop
    $.each(flowKeys,function(idx,map){
    	var flowData = $flowchart.flowchart('getOperatorData',map);
    	
    	//nextId가 없는 경우 loop stop
    	if(gfnIsNull(flowData.properties.flowNextId) || flowData.properties.flowNextId == "null"){
	    	lastFlowIdx = idx;
    		linkChk++;
    		return true;
    	}
    	
    });

	//연결 안된 작업흐름이 2개 이상 있는 경우 중지
	if(linkChk > 1){
		jAlert("모든 작업흐름이 연결되어 있어야 확정이 가능합니다.","경고");
		//해당 작업흐름 선택
	//	$flowchart.flowchart("selectOperator",flowKeys[lastFlowIdx]);
		
		//해당 작업흐름 하이라이트
		gfnLayerHighLight(".flowchart-default-operator:eq("+lastFlowIdx+")");
		return false;
	}else{
		lastFlowId = flowKeys[lastFlowIdx];
	}

	//작업흐름 종료 추가 (화면 최우측 하단)
	var operatorData = {
			top: 380,
			left: 840,
			properties: {
				title: "[종료]최종 완료",
				inputs: {input_1: {label: '이전'}},
				outputs: {output_1: {label: '다음'}},
				flowTitleBgColor:"#ff5643",
				flowTitleColor:"#ffffff",
				flowContentBgColor:"#ffffff",
				flowContentColor:"#ff5643",
				flowEssentialCd:"02",
				flowSignCd:"02",
				flowSignStopCd:"02",
				flowEndCd:"02",
				flowWorkCd:"02",
				flowRevisionCd:"02",
				flowDplCd:"02",
				flowAuthCd:"02",
				flowOptCnt:0
			}
		};
     	
	//작업흐름 Id 세팅
	var flowId = 'F'+new Date().format('yyMMddHHmmssms');
    $flowchart.flowchart('createOperator', flowId, operatorData);
    
    //마지막 작업흐름 ID로 data가져오기
    var lastFlowData = $flowchart.flowchart('getOperatorData',lastFlowId);
    
    //마지막 작업흐름 flowNextId 값 넣기
    lastFlowData.properties.flowNextId = flowId;
    $flowchart.flowchart('setOperatorData', lastFlowId, lastFlowData);
    
	//마지막 링크 해당 작업흐름 nextId에 종료 작업흐름 연결
	var linkData = {
		fromConnector:"output_1"
		,fromOperator:lastFlowId
		,fromSubConnector:0
		,linkCreateEnd:"Y"
		,toConnector:"input_1"
		,toOperator:flowId
		,toSubConnector:0
	}
	$flowchart.flowchart("addLink",linkData);
	
	//현재 프로세스 데이터
	flowGetData = $flowchart.flowchart('getData');
	
	//프로세스 json 수정
	processJsonList[processId] = JSON.stringify(flowGetData);
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/updatePrj1100ProcessConfirmInfo.do'/>"},
			{
				processJsonData:JSON.stringify(flowGetData)
				,processId:processId
				,processConfirmCd:"02"
				,flowId: flowId
				,flowNm:operatorData.properties.title
				,flowTitleBgColor: operatorData.properties.flowTitleBgColor
				,flowTitleColor: operatorData.properties.flowTitleColor
				,flowContentBgColor: operatorData.properties.flowContentBgColor
				,flowContentColor: operatorData.properties.flowContentColor
				,flowEssentialCd: operatorData.properties.flowEssentialCd
				,flowSignCd: operatorData.properties.flowSignCd
				,flowSignStopCd: operatorData.properties.flowSignStopCd
				,flowEndCd: operatorData.properties.flowEndCd
				,flowWorkCd: operatorData.properties.flowWorkCd
				,flowRevisionCd: operatorData.properties.flowRevisionCd
				,flowDplCd: operatorData.properties.flowDplCd
				,flowAuthCd: operatorData.properties.flowAuthCd
				,lastFlowId: lastFlowId
			});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러
		if(data.errorYN == "Y"){
			toast.push(data.message);
		}else{
			jAlert("확정되었습니다.","알림");
			//데이터 갱신
			$(".process_box.active").attr("confirm","02");
			
			//링크 연결 막기
			$flowchart.flowchart({canUserEditLinks:false});
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

//프로세스 확정 취소
function fnProcessConfirmCancel(){
	//프로세스 id
	var processId = $(".process_box.active").attr('id');
	
	//요구사항 수 체크
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100ProcessReqCntAjax.do'/>", async:true},
			{processId:processId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러
		if(data.errorYN == "Y"){
			toast.push(data.message);
		}else{
			var proReqCnt = data.proReqCnt;
			if(proReqCnt > 0){
				jAlert("프로세스에 배정된 요구사항이 존재합니다.<br>확정 취소 할 수 없습니다.","알림");
			}else{
				//현재 프로세스 데이터
				var flowGetData = $flowchart.flowchart('getData');
		
				//최종완료 Id
				var endFlowId = "";
				//최종완료 이전 id
				var prevFlowId = "";
				
				//작업흐름 목록 루프
				if(!gfnIsNull(flowGetData) && !gfnIsNull(flowGetData.operators)){
					//최종완료 찾기
					$.each(flowGetData.operators, function(idx, map){
						//properties
						var flowProp = map.properties;
						
						//nextId 체크
						if(gfnIsNull(flowProp.flowNextId)){
							//최종완료 요구사항
							endFlowId = idx;
							return false;
						}
					});
					
					//최종완료 이전 id 찾기
					$.each(flowGetData.operators, function(idx, map){
						//properties
						var flowProp = map.properties;
						
						//nextId 체크
						if(!gfnIsNull(flowProp.flowNextId) && flowProp.flowNextId == endFlowId){
							//최종완료 이전 id 요구사항
							prevFlowId = idx;
							return false;
						}
					});
				}
				
				//현재 data
				var tmp_flowData = $flowchart.flowchart('getData');
				
				//작업흐름 json 제거
				delete tmp_flowData.operators[endFlowId];
				
				//연결 링크 찾기
				$.each(tmp_flowData.links,function(idx, map){
					if(map.toOperator == endFlowId){
						delete tmp_flowData.links[idx];
						return false;
					}
				});
				
				//이전 작업흐름 Id , flowNextId null
				tmp_flowData.operators[prevFlowId].properties.flowNextId = "null";
				
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/prj/prj1000/prj1100/updatePrj1100ProcessConfirmCancelAjax.do'/>",async:true},
						{processJsonData:JSON.stringify(tmp_flowData)
							,processId:processId
							,endFlowId: endFlowId
							,prevFlowId: prevFlowId
						});
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);
					
					//에러
					if(data.errorYN == "Y"){
						toast.push(data.message);
					}else{
						//프로세스 확정 취소
						$(".process_box.active").attr("confirm","01");
						
						//링크 연결 허용
						$flowchart.flowchart({canUserEditLinks:true});
						
						//데이터 교체
						$flowchart.flowchart('setData',tmp_flowData);
						
						//데이터 갱신
						processJsonList[processId] = JSON.stringify(tmp_flowData);
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
		}
	});
	//AJAX 전송
	ajaxObj.send();
}

//작업흐름 삭제
function fnFlowDeleteAjax(){
	var selFlowId = $flowchart.flowchart('getSelectedOperatorId');
	
	//선택 작업흐름+연결된 링크 제거
	$flowchart.flowchart("deleteSelected");
	
	//프로세스 id
	var processId = $(".process_box.active").attr('id');
	
	//작업흐름 json Data
	var flowGetData = $flowchart.flowchart('getData');
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/deletePrj1100FlowInfoAjax.do'/>"},
			{processJsonData:JSON.stringify(flowGetData)
				,processId:processId
				,flowId :selFlowId
			});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러
		if(data.errorYN == "Y"){
			toast.push(data.message);
		}else{
			//데이터 갱신
			processJsonList[processId] = JSON.stringify(flowGetData);
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

//작업흐름 연결 저장 (링크 생성시, 다음 작업흐름 ID 입력)
function fnFlowLinkConnAjax(linkId,linkData,type){

	//from,to작업흐름 ID
	var fromFlowId = linkData.fromOperator;
	var toFlowId = linkData.toOperator;
	
	//해당 작업흐름 정보 불러오기
	var preFlowInfo = $flowchart.flowchart("getOperatorData",linkData.fromOperator);
	
	//수정인경우
	if(type == "update"){
		//해당 작업흐름 flowNextId 입력	
		preFlowInfo.properties["flowNextId"] = linkData.toOperator;
	}
	//삭제인경우
	else if(type == "delete"){
		//해당 작업흐름 flowNextId null
		preFlowInfo.properties["flowNextId"] = "null";
		toFlowId = "null";
		
	}
	
	//작업흐름 데이터 교체
	$flowchart.flowchart("setOperatorData",linkData.fromOperator,preFlowInfo);
	
	var flowGetData = $flowchart.flowchart('getData');
	
	//수정인경우 해당 getData에 linkCreateEnd 추가
	if(type == "update"){
		flowGetData.links[linkId].linkCreateEnd = "Y";
	}
	//삭제인경우 링크 제거
	else if(type == "delete"){
		delete flowGetData.links[linkId];
	}
	var processId = $(".process_box.active").attr('id');
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/updatePrj1100FlowInfoAjax.do'/>","loadingShow":true,"async":true},
			{processJsonData:JSON.stringify(flowGetData)
				,processId:processId
				,flowId :fromFlowId
				,flowNextId:toFlowId
			});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러
		if(data.errorYN == "Y"){
			toast.push(data.message);
		}else{
			//데이터 갱신
			processJsonList[processId] = JSON.stringify(flowGetData);
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

//process List Call
function fnProcessListAjax(){
	ax5Mask.close();
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100ProcessListAjax.do'/>","loadingShow":false});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 발생
		if(data.errorYN == "Y"){
       		toast.push(data.message);
		}
		else{
			//프로세스 목록 세팅
			$("#processBoxList .process_box").remove();
			//form 내용 초기화
			$("#flowInfo")[0].reset();
			
			//작업흐름 위치 정보값 변경
			/* $("#flow_position").html(''); */
			
			//추가 항목 목록 초기화
			firstGrid.setData([]);

			//flowchart 생성 된 경우 초기화
			if(!gfnIsNull($flowchart)){
				//flowchart 그리기
				$flowchart.flowchart('setData', {});
				//차트 선택 해제
				$flowchart.flowchart('unselectOperator');
			}
			//프로세스 목록이 없는 경우
			if(data.processList.length == 0){
				/* 
				ax5Mask.open({
					zIndex:90,
					target: $("#flw_mainBox"),
					content: "프로세스를 추가해주세요."
				});
				 */
				//프로세스 등록 창 오픈(취소키 제거)
				$("#btn_insert_proInsert").click();
				$("#btn_insert_plcCancle").hide();
				
				//color fix 초기화
				fnSpectrumReset();
			}else{
				
				$("#btn_insert_plcCancle").show();
			
				//jsonData 초기화
				processJsonList = {};
				
				//프로세스 목록 loop
				$.each(data.processList,function(idx, map){
					//JsonData 적재
					processJsonList[map.processId] = map.processJsonData;
					
					var firstClassStr = '';
					var canUserEditLinks = true;
					
					//첫번째 프로세스 선택
					if(idx == 0){
						firstClassStr = "active";
						
						//flowChart
						$flowchart = $('#flowChartDiv');
						
						var jsonData = {};
					
						//jsonData가 null이 아닌경우 세팅
						if(!gfnIsNull(map.processJsonData)){
							jsonData = JSON.parse(map.processJsonData);
						}
						
						//확정 처리의 경우
						if(map.processConfirmCd == "02"){
							//링크 연결 막기
							canUserEditLinks = false;
						}
					}
					
					$("#processBoxList").append('<div class="process_box '+firstClassStr+'" id="'+map.processId+'" confirm="'+map.processConfirmCd+'" desc="'+map.processDesc+'" ord="'+map.processOrd+'" title="'+decodeURI(map.processDesc)+'">'+map.processNm+'</div>');
					
					//html process box 생성 후 세팅
					if(idx == 0){
						//작업흐름 flowchart 그리기
						$flowchart.flowchart({
							multipleLinksOnInput:false,
							multipleLinksOnOutput:false,
							canUserEditLinks:canUserEditLinks,
							distanceFromArrow:1,
							linkWidth:5,
							//작업흐름 이동
							onOperatorMoved :function(operatorId, position){
								//작업흐름 위치 변경 시 위치 정보값 변경
								/* $("#flow_position").html("Top: "+position.top+"</br>Left: "+position.left); */
								
								//드래그 작업흐름에 포커스 주기
								$flowchart.flowchart('unselectOperator');
								$flowchart.flowchart('selectOperator',operatorId);
								
								//위치 변경정보 즉시 저장
								fnFlowMoveSave();
							},
							//작업흐름 선택
							onOperatorSelect:function(selFlwId){
								//선택 작업흐름 Id가 있는 경우 색상 초기화 
								if(!gfnIsNull(selFlowId)){
									fnColorFixReset();	
								}
								//선택 작업흐름 Id
								selFlowId = selFlwId;
								
								//입력 오류 초기화
								$(".inputError").removeClass("inputError");
								
								//데이터 불러오기
								var selFlowData = $flowchart.flowchart('getOperatorData',selFlwId);
								var selFlowPro = selFlowData.properties;
								
								//작업흐름 위치 정보값 변경
								/* $("#flow_position").html("Top: "+selFlowData.top+"</br>Left: "+selFlowData.left); */
								
								//선택 작업흐름 정보 세팅
								$("#previewFlowNm").val(selFlowPro.title);
								
								//제목,글씨 색상
								$("#previewFlowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowTitleBgColor});
								$("#previewFlowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowTitleColor});
								$("#previewFlowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowContentBgColor});
								$("#previewFlowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: selFlowPro.flowContentColor});
								
								//체크 박스
								if(selFlowPro.flowEssential == "on"){	//필수
									$("#previewFlowEssential")[0].checked = true;
								}else{
									$("#previewFlowEssential")[0].checked = false;
								}
								if(selFlowPro.flowSign == "on"){		//결재
									$("#previewFlowSign")[0].checked = true;
								}else{
									$("#previewFlowSign")[0].checked = false;
								}
								if(selFlowPro.flowEnd == "on"){			//종료분기
									$("#previewFlowEnd")[0].checked = true;
								}else{
									$("#previewFlowEnd")[0].checked = false;
								}
								if(selFlowPro.flowWork == "on"){			//작업
									$("#previewFlowWork")[0].checked = true;
								}else{
									$("#previewFlowWork")[0].checked = false;
								}
								if(selFlowPro.flowRevision == "on"){			//리비전
									$("#previewFlowRevision")[0].checked = true;
								}else{
									$("#previewFlowRevision")[0].checked = false;
								}
								if(selFlowPro.flowDpl == "on"){			//배포계획
									$("#previewFlowDpl")[0].checked = true;
								}else{
									$("#previewFlowDpl")[0].checked = false;
								}
								if(selFlowPro.flowAuth == "on"){			//배포계획
									$("#previewFlowAuth")[0].checked = true;
									$(".flw_auth_frame").show();
								}else{
									$("#previewFlowAuth")[0].checked = false;
									$(".flw_auth_frame").hide();
								}
								
								//추가 항목 목록 불러오기
								fnFlowOptList(selFlwId);

								//허용 역할그룹 세팅
								fnPreviewAuthListGrid();
								
								//허용 역할그룹 목록
								fnPreviewAuthRefresh(selFlowId,'01',previewAuthGrid);
								fnPreviewAuthRefresh(selFlowId,'02',previewSignAuthGrid);
								
								//마스크 해제
								$("#flowContentMask").hide();
								return true;
							},
							//작업흐름 생성
							onOperatorCreate:function(operatorId, operatorData, fullElement){
								return true;
							},
							//작업흐름 포커스 잃었을때
							onOperatorUnselect:function(){
								//form 내용 초기화
								$("#flowInfo")[0].reset();
								
								//작업흐름 위치 정보값 변경
								/* $("#flow_position").html(''); */
								
								//추가 항목 목록 초기화
								firstGrid.setData([]);
								
								//color fix 초기화
								fnSpectrumReset();
								
								//마스크 설정
								$("#flowContentMask").show();
								
								selFlowId = null;
								return true;
							},
							//사용자 링크 생성시, 최초 프로세스 JSON 데이터 링크 로드시 생성
							//return 관계없이 데이터 적용 후 callback Function이기 때문에 사용시 주의
							//2018-08-02 flow getData적용 문제 때문에 위와 같이 적용
							onLinkCreate: function (linkId, linkData) {
								//이미 생성된 링크인경우 DB수정 안함
								if(linkData.linkCreateEnd != "Y"){
									//기존 데이터 불러오기
									var preFlowInfo = $flowchart.flowchart("getOperatorData",linkData.fromOperator);
									//기존 연결된 작업흐름 Id와 같은경우 처리 안함
									if(preFlowInfo.properties.flowNextId != linkData.toOperator){
										//링크 반복 체크( A -> B -> A 체크)
										if(!fnLinksLimitLoopChk(linkData.fromOperator,linkData.toOperator)){
											jAlert("작업흐름은 반복되지 않습니다.","경고");
											//무한 반복 링크 삭제
											$flowchart.flowchart("deleteLink",linkId);
											
										}else{ //반복 이상 없는경우 수정
											fnFlowLinkConnAjax(linkId,linkData,"update");
										}
									}
								}else {
									var flowGetData = $flowchart.flowchart('getData');
									
									//실제로 링크 데이터가 남아 있는 경우 체크
									if(flowGetData.links.hasOwnProperty(linkId)){
										// 이미 생성된 링크 변경인지 체크 
										//기존 데이터 불러오기
										var preFlowInfo = $flowchart.flowchart("getOperatorData",linkData.fromOperator);
										//기존 연결된 작업흐름 Id와 다른 경우 DB수정
										if(preFlowInfo.properties.flowNextId != linkData.toOperator){
											fnFlowLinkConnAjax(linkId,linkData,"update");
										}
									}
								}
				            },
				            //링크 삭제
				            onLinkDelete :  function (linkId, forced) {
				            	//링크 제거된 경우 이전에 연결되어있던 작업흐름 nextId null
				            	var linkData = {fromOperator: $flowchart.flowchart('getData').links[linkId].fromOperator};
				            	fnFlowLinkConnAjax(linkId,linkData,"delete");
				            	return true;
				            },
				            //링크 선택
				            onLinkSelect: function(linkId){
				            	//링크 선택 불가
				            	//링크 삭제는 작업흐름 클릭 후 가능
				            	return false;
				            }
						});
						
						//데이터 그리기
						$flowchart.flowchart('setData', jsonData);
						fnFlowChartLayerZoom();
					}
				});
				
				
				//프로세스 active 제어
				$(".process_box").click(function(){
					$(".process_box.active").removeClass("active");
					$(this).addClass("active");
					
					$(".inputError").removeClass("inputError");
					
					var processConfirmCd = $(".process_box.active").attr("confirm");
					//확정 처리인지 확인
					//확정 처리의 경우 기본 정보 외에 disabled
					if(processConfirmCd == "02"){
						//링크 연결 막기
						$flowchart.flowchart({canUserEditLinks:false});
					}else{
						//링크 연결 허용
						$flowchart.flowchart({canUserEditLinks:true});
					}
					
					//선택 프로세스에 해당하는 작업흐름 데이터 가져오기
					var processId = $(".process_box.active").attr('id');
					
					var jsonData = {};
					if(!gfnIsNull(processJsonList[processId])){
						jsonData = JSON.parse(processJsonList[processId]);
					}
					
					var $flowchartDiv = $('#flowChartDiv');
					//zoom 초기화 
					//$flowchartDiv.flowchart('setPositionRatio', 1);
					//$flowchartDiv.panzoom('reset');
					 
					
					//flowchart 그리기
					$flowchart.flowchart('setData', jsonData);
					
					//form 내용 초기화
					$("#flowInfo")[0].reset();
					
					//작업흐름 위치 정보값 변경
					/* $("#flow_position").html(''); */
					
					//추가 항목 목록 초기화
					firstGrid.setData([]);
					
					//차트 선택 해제
					$flowchart.flowchart('unselectOperator');
					
					//color fix 초기화
					fnSpectrumReset();
				});
			}
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

//링크 연결이 무한 루프인지 체크
function fnLinksLimitLoopChk(fromOperator,flowNextId){
	//기준 Id와 다음 작업흐름 ID가 같은 경우 연결 중지
	if(fromOperator == flowNextId){
		return false;
	}
	
	//"null"인 경우 null과 같이 계산
	if(flowNextId == "null"){
		return true;
	}
	
	//다음 작업흐름 정보 조회
	var nextFlowInfo = $flowchart.flowchart("getOperatorData",flowNextId);
	
	//flowNextId 존재하는지 체크
	if(nextFlowInfo.properties.hasOwnProperty("flowNextId")){
		var nextFlowInfoNextId = nextFlowInfo.properties.flowNextId;
		
		//"null"인 경우 null과 같이 계산
		if(nextFlowInfoNextId == "null"){
			return true;
		}
		//함수 재귀 호출
		return fnLinksLimitLoopChk(fromOperator,nextFlowInfoNextId);
	}else{
		//flowNextId 없는경우 이상 없이 추가
		return true;
	}
} 

//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
       firstGrid.setConfig({
           target: $('[data-ax5grid="flw-grid"]'),
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

//추가 항목 불러오기
function fnFlowOptList(selFlwId){
	//선택 프로세스에 해당하는 작업흐름 데이터 가져오기
	var processId = $(".process_box.active").attr('id');
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100OptListAjax.do'/>",loadingShow:false},
			{processId: processId, flowId: selFlwId, type:"prj1100"});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		//에러 없는경우
		if(data.errorYN != "Y"){
			//추가 항목 목록 세팅
			if(!gfnIsNull(data.optList)){
				firstGrid.setData(data.optList);
			}else{
				//결과 값 없는 경우 목록 초기화
				firstGrid.setData([]);
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

//작업흐름 색상 수정시 바로 적용
function fnColorFixSet(type,css){
	if(!gfnIsNull(selFlowId)){
		//해당 css 수정 적용
		$flowchart.flowchart("editStyleOperator",selFlowId,type,css);
	}
}

//작업흐름 색상 초기화
function fnColorFixReset(){
	//작업흐름 Id가 있는 경우에만
	if(!gfnIsNull(selFlowId)){
		//작업흐름 원본 데이터
		var selOperatorData = $flowchart.flowchart("getOperatorData",selFlowId).properties;
		
		//원본 데이터 있는 경우에만
		if(!gfnIsNull(selOperatorData)){
			//작업흐름 색상 초기화
			fnColorFixSet("operator",{"background-color":selOperatorData.flowContentBgColor, "color":selOperatorData.flowContentColor});
			fnColorFixSet("title",{"background-color":selOperatorData.flowTitleBgColor, "color":selOperatorData.flowTitleColor});
		}
	}
}
	
//spectrum 초기화
function fnSpectrumReset(){
	$("#previewFlowTitleBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
	$("#previewFlowTitleColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#previewFlowContentBgColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#FFFFFF"});
	$("#previewFlowContentColor").spectrum({showInput: true,chooseText: "선택",cancelText: "닫기",preferredFormat: "name",color: "#515769"});
}

//역할 그룹 그리드
function fnPreviewAuthListGrid(){
	previewAuthGrid = new ax5.ui.grid();

  previewAuthGrid.setConfig({
      target: $('[data-ax5grid="previewAuth-grid"]'),
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
  
	previewSignAuthGrid = new ax5.ui.grid();

  previewSignAuthGrid.setConfig({
      target: $('[data-ax5grid="previewSignAuth-grid"]'),
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

//역할그룹 정보 조회
function fnPreviewAuthRefresh(flowId,authGrpTargetCd,gridTarget){
	var processId = $(".process_box.active").attr('id');
	
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
//flowchart zoom
function fnFlowChartLayerZoom(){
	if(!gfnIsNull($flowchart)){
		var $flowchartDiv = $('#flowChartDiv');
		var $container = $flowchart.parent();
		
		var cx = $flowchart.width() / 2;
	    var cy = $flowchart.height() / 2;
	    
	    $flowchartDiv.panzoom();
    
	    // Centering panzoom
	    $flowchartDiv.panzoom('pan', 0, 0);
	    /* 
	    // Panzoom zoom handling...
	    var possibleZooms = [2,1,0.75,0.5];
	    var currentZoom = 2;
	    $container.on('mousewheel.focal', function( e ) {
	        e.preventDefault();
	        var delta = (e.delta || e.originalEvent.wheelDelta) || e.originalEvent.detail;
	        var zoomOut = delta ? delta < 0 : e.originalEvent.deltaY > 0;
	        currentZoom = Math.max(0, Math.min(possibleZooms.length - 1, (currentZoom + (zoomOut * 2 - 1))));
	        
	        $flowchartDiv.flowchart('setPositionRatio', possibleZooms[currentZoom]);
	        
	        $flowchartDiv.panzoom('zoom',(possibleZooms[currentZoom]), {
	            animate: false,
	            focal: e
	        });
	    }); */
	    
	}
}

//flowchart zoom function
function fnFlowChartZoom(type){
	var $flowchartDiv = $('#flowChartDiv');
	
	//줌 초기화
	if(type == "reset"){
		currentZoom=1;
		$flowchartDiv.flowchart('setPositionRatio', 1);
		$flowchartDiv.panzoom('reset');
		return false;
	}
	//줌 가능한 수치
	var possibleZooms = [2,1,0.75,0.5];
	
	//줌인
	if(type == "in"){
		currentZoom--;
		if(currentZoom < 0){
			currentZoom = 0;
		}
	}
	//줌아웃
	else if(type == "out"){
		currentZoom++;
		if(currentZoom > (possibleZooms.length-1)){
			currentZoom = (possibleZooms.length-1);
		}
	}
	
	$flowchartDiv.flowchart('setPositionRatio', possibleZooms[currentZoom]);
	        
    $flowchartDiv.panzoom('zoom',(possibleZooms[currentZoom]), {
        animate: true
    });
}

//작업흐름 움직였을때 자동 저장
function fnFlowMoveSave(){
	var processId = $(".process_box.active").attr('id');
	
	flowGetData = $flowchart.flowchart('getData');
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/prj/prj1000/prj1100/updatePrj1100ProcessInfoAjax.do","async":true},
			{processJsonData:JSON.stringify(flowGetData)
				,processId:processId
			});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		if(data.errorYN == "N"){
			//데이터 갱신
			processJsonList[processId] = JSON.stringify(flowGetData);
		}
	});
	//AJAX 전송
	ajaxObj.send();
}
//가이드 상자
function fnGuideShow(){
	//guide box setting - 완성후 매개변수로 전환
	var guideBoxInfo = globals_guideContents["prj1100"];
	gfnGuideBoxDraw(true,$("#flw_mainFrame"),guideBoxInfo);
}
</script>

<div class="main_contents">
	<div class="flw_title">${sessionScope.selMenuNm }</div>
	<div class="flw_main_middleBox_save" id="flw_main_middleBox_save">
		<div class="flw_title_box">프로세스 <span id="processActionTypeNm">등록</span></div>
		<div class="process_regFrame">
			<div class="processNm">
				<div class="process_regTitle"><label for="processNm">프로세스 명</label></div>
				<input type="text" name="processNm" id="processNm" placeholder="프로세스명을 입력해주세요."/>
			</div>
			<div class="processDesc">
				<div class="process_regTitle"><label for="processDesc">프로세스 설명</label></div>
				<textarea name="processDesc" id="processDesc"></textarea>
			</div>
			<div class="processOrd">
				<div class="process_regTitle"><label for="processOrd">프로세스 순서</label></div>
				<input type="number" name="processOrd" id="processOrd" placeholder="프로세스 순서를 입력해주세요." value="0" min="0"/>
			</div>
			<div class="processBtn">
				<span class="button_normal2" id="btn_insert_plcInsert">프로세스 저장</span>
				<span class="button_normal2" id="btn_insert_plcCancle">저장 취소</span>
			</div>
		</div>
	</div>
	<div class="flw_mainFrame" id="flw_mainFrame">
		<div class="zoomBtn" guide="zoom">
			<span class="button_normal2" onclick="fnFlowChartZoom('reset')"><i class="fa fa-undo-alt"></i></span>
			<span class="button_normal2" onclick="fnFlowChartZoom('in')"><i class="fa fa-plus"></i></span>
			<span class="button_normal2" onclick="fnFlowChartZoom('out')"><i class="fa fa-minus"></i></span>
		</div>
		<div class="flw_mainBox" id="flw_mainBox">
			<div class="flw_box flw_main_leftBox">
				<div class="flw_box flw_left_topBox" guide="leftMenu">
					<span class="button_normal2" id="btn_insert_proSelect">조회</span>
					<span class="button_normal2" id="btn_insert_proInsert">추가</span>
					<span class="button_normal2" id="btn_update_proUpdate">수정</span>
					<span class="button_normal2" id="btn_delete_proDelete">삭제</span>
					<span class="button_normal2" id="btn_delete_proCopy">복사</span>
					<span class="button_normal2" id="btn_update_proConfirm">확정</span>
					<span class="button_normal2" id="btn_update_proConfirmCancel">확정취소</span>
				</div>
				<div class="flw_box_clear"></div>
				<div class="flw_box flw_left_bottomBox" id="processBoxList">
				</div>
			</div>
			<div class="flw_box flw_main_rightBox">
				<div id="flowChartDiv">
				
				</div>
			</div>
			<div class="flw_box_clear"></div>
			<div class="flw_box flw_main_middleBox" guide="functionList">
				<!-- <div id="flow_position">
						
					</div> -->
				<!-- <span class="button_normal2" id="btn_update_flwSave"><i class="fa fa-save"></i>&nbsp;작업흐름 저장</span> -->
				<span class="button_normal2" id="btn_insert_flwInsert"><i class="fa fa-plus"></i>&nbsp;작업흐름 추가</span>
				<span class="button_normal2" id="btn_update_flwUpdate"><i class="fa fa-edit"></i>&nbsp;선택 작업흐름 수정</span>
				<span class="button_normal2" id="btn_delete_flwDelete"><i class="fa fa-times"></i>&nbsp;선택 작업흐름 삭제</span>
				<span class="button_normal2" id="btn_delete_flwLinkDelete"><i class="fa fa-times"></i>&nbsp;선택 작업흐름 링크 삭제</span>
				<!-- <span class="button_normal2" id="btn_update_flwReset"><i class="fa fa-history"></i>&nbsp;내용 초기화</span> -->
				<span class="button_normal2" id="btn_insert_flwItemInsert"><i class="fa fa-list"></i>&nbsp;추가 항목 관리</span>
			</div>
			<div class="flw_box_clear"></div>
			<div class="flw_box flw_main_bottomBox" guide="flowInfo">
			<form name="flowInfo" id="flowInfo">
				<div class="flw_content_mask" id="flowContentMask">작업흐름을 선택해주세요.</div>
				<div class="flw_sub_box flw_bottom_leftBox" style="height: auto;">
					 <div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						작업흐름 명
					</div>
					<div class="flw_sub_box flw_sub3">
						<input type="text" name="previewFlowNm" id="previewFlowNm" readonly="readonly" style="border:1px solid #ccc;"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="선택 작업흐름을 필수 단계로 지정합니다.">
						필수
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="previewFlowEssential" id="previewFlowEssential" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						제목 배경 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="previewFlowTitleBgColor" id="previewFlowTitleBgColor" value="#515769" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="다음 작업흐름 변경시 결재를 받도록 지정합니다.">
						결재 요청
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="previewFlowSign" id="previewFlowSign" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						제목 글씨 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="previewFlowTitleColor" id="previewFlowTitleColor" value="#FFFFFF" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1" title="">
						
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						내용 배경 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="previewFlowContentBgColor" id="previewFlowContentBgColor" value="#FFFFFF" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="다음 작업흐름 변경시 바로 최종완료 작업흐름으로 변경이 가능하도록 합니다.">
						종료 분기
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="previewFlowEnd" id="previewFlowEnd" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
						내용 글씨 색상
					</div>
					<div class="flw_sub_box flw_sub1">
						<input type="color" name="previewFlowContentColor" id="previewFlowContentColor" value="#515769" disabled="disabled"/>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 리비전 번호를 입력 받도록 지정합니다.">
						허용 역할
					</div>
					<div class="flw_sub_box flw_sub1 flw_line_right">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="previewFlowAuth" id="previewFlowAuth" disabled="disabled"/><label></label>
						</div>
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 작업을 추가 할 수 있도록 지정합니다.">
						작업
					</div>
					<div class="flw_sub_box flw_sub1">
						<div class="flw_chk"> 
							<input type="checkbox" title="체크박스" name="previewFlowWork" id="previewFlowWork" disabled="disabled"/><label></label>
						</div>
					</div>
					<c:choose>
						<c:when test="${empty svnkitModuleUseChk or svnkitModuleUseChk == true }">
							<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 리비전 번호를 입력 받도록 지정합니다.">
								리비전 저장
							</div>
							<div class="flw_sub_box flw_sub1 flw_line_right">
								<div class="flw_chk"> 
									<input type="checkbox" title="체크박스" name="previewFlowRevision" id="previewFlowRevision" disabled="disabled"/><label></label>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right" title="현재 작업흐름에서 리비전 번호를 입력 받도록 지정합니다.">
							</div>
							<div class="flw_sub_box flw_sub1 flw_line_right">
								<input type="hidden" title="체크박스" name="previewFlowRevision" id="previewFlowRevision" disabled="disabled"/>
							</div>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${empty jenkinsModuleUseChk or jenkinsModuleUseChk == true }">
							<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
								배포 계획 저장
							</div>
							<div class="flw_sub_box flw_sub1">
								<div class="flw_chk"> 
									<input type="checkbox" title="체크박스" name="previewFlowDpl" id="previewFlowDpl" disabled="disabled"/><label></label>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
							</div>
							<div class="flw_sub_box flw_sub1">
								<div class="flw_chk"> 
									<input type="hidden" title="체크박스" name="previewFlowDpl" id="previewFlowDpl" class="optPreviewCdChg" opt="optDpl"/>
								</div>
							</div>
						</c:otherwise>
					</c:choose>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_sub_desc flw_auth_frame">
						담당자</br>허용 역할 목록
					</div>
					<div class="flw_sub_box flw_sub3 flw_sub_desc flw_line_bottom_none flw_auth_frame">
						<div data-ax5grid="previewAuth-grid" data-ax5grid-config="{}" style="height: 100px;"></div>	
					</div>
				</div>
				<div class="flw_sub_box flw_bottom_rightBox" style="height: auto;">
					<div class="flw_sub_box flw_sub_title flw_sub4 flw_line_left">
						추가 항목 내용
					</div>
					<div class="flw_sub_box flw_sub4" style="height: 240px;">
						<div data-ax5grid="flw-grid" data-ax5grid-config="{}" style="height: 240px;"></div>	
					</div>
					<div class="flw_sub_box flw_sub_title flw_sub1 flw_sub_desc flw_line_left flw_auth_frame">
						결재자</br>허용 역할 목록
					</div>
					<div class="flw_sub_box flw_sub3 flw_sub_desc flw_line_bottom_none flw_auth_frame">
						<div data-ax5grid="previewSignAuth-grid" data-ax5grid-config="{}" style="height: 100px;"></div>	
					</div>
				</div>
			</form></div>
		</div>
	</div>
</div>
	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />