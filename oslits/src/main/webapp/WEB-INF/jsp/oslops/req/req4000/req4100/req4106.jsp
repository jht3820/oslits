<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value='/js/common/oslFile.js'/>" ></script>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<html lang="ko">
<head>
<title>OpenSoftLab</title>

<style>
/* 팝업 레이아웃 */
.layer_popup_box .popup {font-size: 0.95em;height: 100%;}
.popup_title_box { width: 100%; height: 44px; padding: 15px;font-weight: bold; color: #fff;background-color: #4b73eb;}

/* 항목 레이아웃 */
.req4106_req_mainBox {display: inline-block;float: left;width: 100%;height: calc(100% - 44px);border: 1px solid #8f95b3;}
.req4106_req_topBox {width: 100%;height: 130px;border-bottom: 1px solid #8f95b3;overflow-x: auto;overflow-y: hidden;white-space:nowrap;}
.req4106_req_bottomBox {width: 100%;height: calc(100% - 130px);min-height: 714px;}
.req4106_reqBottom_topBox {display: block;width: 100%;height: calc(100% - 100px);min-height: 614px;overflow-y: auto;border-bottom:1px solid #8f95b3;font-size: 10pt;padding: 10px;}
.req4106_reqBottom_bottomBox {display: block;width: 100%;height: 100px;}
.req4106_optionDiv {width: 100%;display: block;float: left;}
.req4106_option_title {width: 25%;float: left;height: 45px;padding-left: 10px;line-height: 40px;background-color: #f9f9f9;border: 1px solid #ccc;border-top: none;font-weight: bold;}
.req4106_option_all {float: left;height: 45px;line-height: 30px;width: 75%;border-bottom: 1px solid #ccc;padding: 5px;}
.req4106_option_half {float: left;height: 45px;line-height: 30px;width: 25%;border-bottom: 1px solid #ccc;padding: 5px;}
.req4106_default_option{border-top: 1px solid #ccc;border-right: 1px solid #ccc;}
.req4106_desc_file{border-right: 1px solid #ccc;}
input.req4106_input_text {min-width: 190px;height: 100%;border: 1px solid #ccc;display: block;padding-left: 15px;border-radius: 1px;}
textarea.req4106_textarea {width: 619px;height: 100%;resize: none;padding: 5px;border: 1px solid #ccc;border-radius: 1px;}
input.req4106_input_date {width: 157px;float: left;display: block;border-radius: 1px;height: 100%;border: 1px solid #ccc;text-align: center;margin-right: 5px;}
input.req4106_input_check {width: 100%;height: 80%;}
textarea.req4106_processBox_bottom[disabled] {background-color: transparent;white-space: normal;}
select.rerq4105_select {width: 100%;height: 100%;border-radius: 1px;border: 1px solid #ccc;text-align: center;}
input.req4106_charger {width: 170px !important;min-width: 100px;display: inline-block;float: left;margin-right: 5px;}
span.req4106_charger{height: 34px;line-height: 30px;width: 28px;min-width: 28px;}
.req4106_option_half input.req4106_input_text, .req4106_option_half textarea.req4106_textarea
, .req4106_option_half select.rerq4105_select {width:100%;}
.req4106_option_all input.req4106_input_text, .req4106_option_all textarea.req4106_textarea
, .req4106_option_all select.rerq4105_select {width:100%;}
input.req4106_input_text.req4106_readonly, textarea.req4106_textarea.req4106_readonly {cursor: default;background-color: #eee;}
img.ui-datepicker-trigger {float: left;margin-top: 2px;margin-left: 0;margin-right: 10px;}
.req4106_option_check_box{box-shadow: inset 0px 0px 1px 1px #0581f2;border-radius: 3px;border: 1px solid #fff;}
.req4106_desc {height: 90px;}
.req4106_file {height: 120px;}
.req4106_clear{clear:both;}
.req4106_date_span{float:left;margin: 0 12px;}
/* 프로세스 목록 */
.req4106_processBox {width: 150px;display: inline-block;margin: 10px;margin-bottom: 0;border: 3px solid #e8e8e8;border-radius: 5px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);height: 110px;padding: 5px;}
.req4106_processBox_top {width: 100%;height: 20px;line-height: 20px;text-align: center;font-weight: bold;border-bottom: 1px solid #ccc;font-size: 10pt;}
.req4106_processBox_bottom {height: 70px;padding: 2px 5px;font-size: 9pt;width: 138px;resize: none;border: none;overflow: hidden;}

/* 파일 리스트 */
#fileListDiv{height:100%;overflow-y: auto;}
.uploadOverFlow{width: 100%;min-height: 100%;}

/* 버튼 */
.req4106_close{width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;background-color: #fff;color: #000;}
.req4106_complete {width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin-right:20px;background-color: #fff;color: #000;}
.req4106_btnDiv {width: 100%;height: 100px;line-height: 100px;text-align: center;}
.req4106_nextProcessBtn {width: 100px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin:0 10px;background-color: #fff;color: #000;}

/* 작업흐름 선택 */
.req4106_processSelect_maskBox {display:none;width: 100%;height: 846px;position: absolute;left: 0;background-color: rgba(0, 0, 0, 0.6);z-index: 9;}
.req4106_processSelectBox {display:none;position: absolute;z-index: 10;top: 200px;left: 2.5%;width: 95%;height: 244px;border-bottom: 1px solid #8f95b3;overflow-x: auto;overflow-y: hidden;white-space: nowrap;background-color: #fff;}
.req4106_processSelectBox_title {width: 100%;height: 44px;padding: 15px;font-weight: bold;color: #fff;background-color: #4b73eb;}
.req4106_processSelectBox_top {height: 150px;overflow-y: hidden;padding-top: 1px;overflow-x: scroll;}
.req4106_processSelectBox_bottom {height: 50px;text-align: center;border-top: 1px solid #8f95b3;padding: 4px 0;}
.req4106_processSelectBox_top > .req4106_processBox{cursor:pointer;}
.req4106_processSelectBox_top > .req4106_processBox:hover{border: 3px solid #4b73eb;background-color: rgba(75, 115, 235, 0.1);}
.req4106_processSelectBox_top > .req4106_processBox.active{border: 3px solid #4b73eb;background-color: rgba(75, 115, 235, 0.1);}
.req4106_processSelectBox_top .req4106_processBox_bottom{cursor:pointer;}

</style>
<script>
//요구사항 Id
var reqId = "${reqId}";

//요청자 Id
var reqUsrId;

//선택 프로세스 Id
var processId;
/* 
//프로젝트 기간
var prjStartDt = '${selPrjInfo.startDt}';
var prjEndDt = '${selPrjInfo.endDt}'; */

$(document).ready(function() {
	//데이터 불러오기
	fnreq4106DataLoad();
	/* 
	//작업 기간 설정
	gfnCalRangeSet("reqStDtm", "reqEdDtm", prjStartDt, prjEndDt);
	
	//작업 종료 예정 일자
	gfnCalRangeSet("reqStDuDtm", "reqEdDuDtm", prjStartDt, prjEndDt);
	 */
	//유효성 체크
	var arrChkObj = {"reqAcceptTxt":{"type":"length","msg":"접수 의견은 4000byte까지 입력이 가능합니다.",max:4000}};
	gfnInputValChk(arrChkObj);
	
	//담당자 검색 걸기
	$("#btn_user_select").click(function() {
		gfnCommonUserPopup( $('#reqChargerNm').val() ,false,function(objs){
			if(objs.length>0){
				$('#reqChargerId').val(objs[0].usrId);
				$('#reqChargerNm').val(objs[0].usrNm);
			}
		});
	});
	
	$('#reqChargerNm').keyup(function(e) {
		if($('#reqChargerNm').val() == ""){
			$('#reqChargerId').val("");
		}
		if(e.keyCode == "13" ){
			$('#btn_user_select').click();
		}
	});
	
	$("#btn_cls_select").click(function() {
		gfnCommonClsPopup(function(reqClsId,reqClsNm){
			$("#reqClsId").val(reqClsId);
			$("#reqClsNm").val(reqClsNm);
		});
	});
	
});


/**
 * [데이터 불러오기]
 * - 프로세스
 * - 요구사항 정보
 */
function fnreq4106DataLoad(){
	//AJAX 설정 
	var ajaxObj = new gfnAjaxRequestAction({"url":"<c:url value='/req/req4000/req4100/selectReq4106DataLoad.do'/>"}
	,{reqId: reqId, dshType:"dsh1000"});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	if(data.errorYN == 'Y'){
    		jAlert(data.message,"알림");
    		
    		//대시보드 부분 새로고침 함수 존재하는 경우 새로고침
    		if(typeof fnSubDataLoad == "function"){
    			fnSubDataLoad('obj','request');
    		}
    		
    		//팝업 창 닫기
			gfnLayerPopupClose();
    		return false;
    	} 
    	else{
    		//기본 항목 값 세팅
    		gfnSetData2ParentObj(data.reqInfoMap,"req4106InfoForm");
    		$('#popTitle').html(data.reqInfoMap.reqNm);
    		//요청자Id 대입
    		reqUsrId = data.reqInfoMap.reqUsrId;
    		
    		if(!gfnIsNull(data.fileList)){
				//첨부파일 리스트 세팅
			 	$.each(data.fileList, function(idx, fileVo){
			 		fileVo['reqId'] = reqId;
			 		gfnFileListReadDiv(fileVo,"#fileListDiv", "newReq");
			    });
			}
    		//상단 내용 세팅 변수
    		var reqTopData = "";
    		
    		//처리유형
    		var reqProType = data.reqProType;
    		
   			//프로세스 목록 세팅
   			reqTopData = fnTopProcessDivSetData(data.processList);
    		
    		//상단 내용 채우기
    		$("#req4106_reqTopDataList").html(reqTopData);
    		
    		/*    
			 *	공통으로 콤보박스용 사용자 목록을 가져올때 사용하는 함수
			 *	현재 선택한 프로젝트에 권한롤에 배정되어 있는 사용자 정보만 가지고 온다. 
			 *   	1. 사용구분 저장(01: 사용중인 코드만, 02: 비사용중인 코드만, 공백: 전체)
			 *   	2. 사용자 목록 적용할 select 객체 직접 배열로 저장
			 *    3. 사용자 목록 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
			 *   	4. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
			 */
			var strUseCd = '01';
			var arrObj = [$("#reqChargerId")];
			var arrComboType = [ ""];
			gfnGetUsrDataForm(strUseCd, arrObj, arrComboType , true);
    	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push("ERROR STATUS("+status+")<br>"+err);
 	});
	
	//AJAX 전송
	ajaxObj.send();
}

//프로세스 목록 데이터 받아와서 상단 세팅
function fnTopProcessDivSetData(processList){
	//기본 값 - 접수 반려
	var reqTopData = '';
	
	//프로세스 없는경우 버튼 숨기고 alert
	if(gfnIsNull(processList)){
		$(".button_complete.req4106_complete").hide();
		jAlert("프로세스가 존재하지 않습니다.<br/>'프로젝트 프로세스 설정'메뉴에서<br/> 프로세스를 생성해주세요.","알림");
		return false;
	}else{
		
		//상단 레이아웃에 프로세스 목록 세팅
		$.each(processList,function(idx, map){
			var processNm = gfnCutStrLen(map.processNm,18);
			var processDesc = gfnCutStrLen(decodeURI(map.processDesc),90);
			reqTopData += '<div class="req4106_processBox" id="'+map.processId+'" proNm="'+map.processNm+'" onclick="fnProcessSelected(this)">'
					+'<div class="req4106_processBox_top">'
							+processNm
						+'</div>'
						+'<textarea class="req4106_processBox_bottom" disabled>'
							+processDesc
						+'</textarea>'
					+'</div>';
		});
	}
	return reqTopData;
}

//프로세스 선택 동작
function fnProcessSelected(thisObj){
	//active
	$(".req4106_processBox.active").removeClass("active");
	$(thisObj).addClass("active");
	
	//processId
	processId = thisObj.id;
}

//다음 프로세스 선택창 닫기
function fnReqProcessChgCancle(){
	$(".req4106_processSelectBox_top > .req4106_processFrameBox").off("click");
	$(".req4106_processSelect_maskBox").hide();
	$(".req4106_processSelectBox").hide();
	processId = null;
}

//프로세스 선택창 열기
function fnRequestBeforeAccept(){
	var checkObjArr = ["reqChargerId"];
	var checkObjNmArr = ["담당자"];
	
	//유효성 체크
	var chkRtn = gfnRequireCheck("req4106InfoForm", checkObjArr, checkObjNmArr);
	
	//유효성 오류있는 경우
	if(chkRtn){
		return false;
	}
	
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus()
		return false;
	}
	
	//다음 프로세스가 하나라면 바로 세팅 후 넘기기
	if($("#req4106_reqTopDataList .req4106_processBox").length == 1){
		processId = $("#req4106_reqTopDataList .req4106_processBox").attr("id");
		
		fnRequestAccept();
		return false;
	}
	
	//팝업창 mask 처리하고 작업흐름 변경 창 불러오기
	$(".req4106_processSelect_maskBox").show();
	$(".req4106_processSelectBox").show();
	$(".req4106_processSelectBox_top").html($("#req4106_reqTopDataList").html());
	
	$(".req4106_processSelectBox_top > .req4106_processBox").click(function(){
		$(".req4106_processSelectBox_top > .req4106_processBox.active").removeClass("active");
		$(this).addClass("active");
		
		//작업흐름 Id넣기
		processId = $(".req4106_processSelectBox_top > .req4106_processBox.active").attr("id");
	});
}

//요청사항 접수 반려
function fnRequestReject(){
	processId = "reject";
	
	fnRequestAccept();
}

//요청사항 접수 완료
function fnRequestAccept(){
	//입력 항목 값
	var formArray = $("#req4106InfoForm").serializeArray();
	
	//접수 반려인경우 경고창 띄우기
	if(processId == "reject"){
		jConfirm("요청 접수를 반려하시겠습니까?", "알림", function( result ) {
			if( result ){
				//Ajax 전송
				innerFnReqAccept();
			}
		});
	}else{
		//프로세스 선택했는지 체크
		if(gfnIsNull(processId)){
			jAlert("프로세스를 선택해주세요.","알림");
			return false;
		}else{
			//Ajax 전송
			innerFnReqAccept();
		}
	}
	
	
	//ajax 내부 함수
	function innerFnReqAccept(){
		//요구사항ID, 프로세스 Id
		formArray.push({name:"reqId",value: reqId});
		formArray.push({name:"processId",value: processId});
		formArray.push({name:"reqUsrId",value: reqUsrId});
		
		var paramInfo = {};
		$.each(formArray,function(idx, map){
			paramInfo[map.name] = map.value;
		});
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4100/insertReq4106NewReqAcceptInfoAjax.do'/>"},
				paramInfo);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);

			//에러 없는경우
			if(data.errorYN == "N"){
				toast.push(data.message);
				
				//대시보드 새로고침 함수 존재하는 경우 새로고침
	    		if(typeof fnDashBoardSetting == "function"){
	    			fnDashBoardSetting();
	    		}
				
				//요구사항 목록페이지인경우 그리드 새로고침
	    		if(typeof fnInGridListSet == "function"){
	    			fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
	    		}
				
				//팝업 창 닫기
				gfnLayerPopupClose();
			}else{
				// 에러일경우 alert 띄우며 확인 시 팝업닫음
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
						gfnLayerPopupClose();
					}
				});
	    		return;
			}
		});
		
		//AJAX 전송
		ajaxObj.send();
	}
}
</script>
</head>
<body>
<div class="popup">
	<div class="popup_title_box"><span>[${targetprjGrpNm} > ${targetPrjNm}]</span> : <span id="popTitle"></span></div>
	<div class="req4106_processSelect_maskBox"></div>
	<div class="req4106_processSelectBox">
		<div class="req4106_processSelectBox_title">접수 완료 프로세스 선택</div>
		<div class="req4106_processSelectBox_top"></div>
		<div class="req4106_processSelectBox_bottom">
			<div class="button_complete req4106_nextProcessBtn" onclick="fnRequestAccept()"><i class="fa fa-check"></i>&nbsp;완료</div>
			<div class="button_complete req4106_nextProcessBtn" onclick="fnReqProcessChgCancle()"><i class="fa fa-times"></i>&nbsp;취소</div>
		</div>
	</div>
	<div class="req4106_req_mainBox">
		<div class="req4106_req_topBox" id="req4106_reqTopDataList">

		</div>
		<div class="req4106_req_bottomBox">
			<div class="req4106_reqBottom_topBox" id="req4106_reqOptDataList">
				<form id="req4106InfoForm" name="req4106InfoForm" onsubmit="return false;">
				<div class="req4106_optionDiv req4106_default_option">
					<div class="req4106_option_title">
						접수 유형
					</div>
					<div class="req4106_option_half">
						<input type="text" class="req4106_input_text req4106_readonly" id="reqNewTypeNm" name="reqNewTypeNm" readonly="readonly"/>
					</div>
					<div class="req4106_option_title">
					</div>
					<div class="req4106_option_half">
					</div>
					<div class="req4106_option_title">
						요청 명
					</div>
					<div class="req4106_option_all">
						<input type="text" class="req4106_input_text req4106_readonly" id="reqNm" name="reqNm" readonly="readonly"/>
					</div>
					<div class="req4106_option_title">
						요청자 소속
					</div>
					<div class="req4106_option_all">
						<input type="text" class="req4106_input_text req4106_readonly" id="reqUsrDeptNm" name="reqUsrDeptNm" readonly="readonly"/>
					</div>
					<div class="req4106_option_title">
						요청자
					</div>
					<div class="req4106_option_half">
						<input type="text" class="req4106_input_text req4106_readonly" id="reqUsrNm" name="reqUsrNm" readonly="readonly"/>
					</div>
					<div class="req4106_option_title">
						연락처
					</div>
					<div class="req4106_option_half">
						<input type="text" class="req4106_input_text req4106_readonly" id="reqUsrNum" name="reqUsrNum" readonly="readonly"/>
					</div>
					<div class="req4106_option_title">
						요청일자
					</div>
					<div class="req4106_option_half">
						<input type="text" class="req4106_input_text req4106_readonly" id="reqDtm" name="reqDtm" readonly="readonly"/>
					</div>
					<div class="req4106_option_title">
						이메일
					</div>
					<div class="req4106_option_half">
						<input type="text" class="req4106_input_text req4106_readonly" id="reqUsrEmail" name="reqUsrEmail" readonly="readonly"/>
					</div>
					<div class="req4106_option_title">
						담당자<span class="required_info">&nbsp;*</span>
					</div>
					<div class="req4106_option_half">
						<input type="hidden" name="reqChargerId" id="reqChargerId"/>
						<input type="text" title="담당자" class="req4106_input_text req4106_charger" name="reqChargerNm" id="reqChargerNm"/>
						<span class="button_normal2 fl req4106_charger" id="btn_user_select"><li class="fa fa-search"></li></span>
					</div>
					<div class="req4106_option_title">
						요구사항 분류
					</div>
					<div class="req4106_option_half">
						<input type="hidden" name="reqClsId" id="reqClsId"/>
						<input type="text" title="요구사항 분류" class="req4106_input_text req4106_charger" name="reqClsNm" id="reqClsNm" style="width: 80%!important;" readonly="readonly" />
						<span class="button_normal2 fl req4106_charger" id="btn_cls_select"><li class="fa fa-search"></li></span>
					</div>
				</div>
				<div class="req4106_optionDiv req4106_desc_file">
					<div class="req4106_option_title req4106_desc">
						요청 내용
					</div>
					<div class="req4106_option_all req4106_desc">
						<textarea class="req4106_textarea req4106_readonly" id="reqDesc" name="reqDesc" readonly="readonly"></textarea>
					</div>
					<div class="req4106_option_title req4106_desc">
						접수 의견
					</div>
					<div class="req4106_option_all req4106_desc">
						<textarea class="req4106_textarea" id="reqAcceptTxt" name="reqAcceptTxt"></textarea>
					</div>
					<div class="req4106_option_title req4106_file">
						첨부파일
					</div>
					<div class="req4106_option_all req4106_file">
						<div id="fileListDiv" class="uploadOverFlow">
						</div>
					</div>
				</div>
				</form>
			</div>
			<div class="req4106_reqBottom_bottomBox" id="req4106_reqBtnSign">
				<div class="req4106_btnDiv">
					<div class="button_complete req4106_complete" onclick="fnRequestReject()"><i class="fa fa-check"></i>&nbsp;접수 반려</div>
					<div class="button_complete req4106_complete" onclick="fnRequestBeforeAccept()"><i class="fa fa-check"></i>&nbsp;다음</div>
					<div class="button_complete req4106_close" onclick="gfnLayerPopupClose()"><i class="fa fa-times"></i>&nbsp;닫기</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>