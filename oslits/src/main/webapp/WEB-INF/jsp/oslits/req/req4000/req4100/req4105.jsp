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
.popup_title_box { width: 100%; height: 44px; padding: 15px;font-weight: bold; color: #fff; background-color: #4b73eb;}

/* 항목 레이아웃 */
.req4105_req_leftBox {display: inline-block;float: left;width: 80%;height: calc(100% - 44px);border: 1px solid #8f95b3;}
.req4105_req_rightBox {display: inline-block;float: left;width: 20%;height: calc(100% - 44px);min-height: 846px;border: 1px solid #8f95b3;overflow-y: auto;border-left: none;position: relative;}
.req4105_nextFlowList {width: 100%;height: 100%;display: inline-block;float:left;}
.req4105_req_topBox {width: 100%;height: 130px;border-bottom: 1px solid #8f95b3;overflow-x: auto;overflow-y: hidden;white-space:nowrap;text-align:right;padding-top: 10px;padding-right: 10px;position:relative;}
.req4105_req_bottomBox {width: 100%;height: calc(100% - 130px);min-height: 714px;}
.req4105_reqBottom_topBox {display: block;width: 100%;height: 614px;min-height: 614px;overflow-y: auto;overflow-x: hidden;border-bottom:1px solid #8f95b3;font-size: 10pt;padding: 10px 15px 150px 15px;}
.req4105_reqBottom_bottomBox {display: block;width: 100%;height: 100px;}
.req4105_optionDiv {
    width: 100%;
    display: block;
    float: left;
    border-right: 1px solid #ccc;
}
div#req4105_reqOptDataList .req4105_default_option {
    border: 1px solid #ccc;
    border-top: none;
    padding: 10px;
    position:relative;
}
.req4105_frameTitleDiv {
    width: 100%;
    height: 45px;
    background-color: #fff;
    color: #414352;
    border: 1px solid #ccc;
    border-radius: 5px 5px 0 0;
    padding-left: 10px;
    line-height: 40px;
    font-weight: bold;
    float: left;
    display: block;
    margin-top: 10px;
    font-size: 13pt;
}
.req4105_option_title {width: 25%;float: left;height: 45px;padding-left: 10px;line-height: 40px;background-color: #f9f9f9;border: 1px solid #ccc;font-weight: bold;border-top: none;}
.req4105_option_all {float: left;height: 45px;line-height: 30px;width: 75%;border-bottom: 1px solid #ccc;padding: 5px;}
.req4105_option_half {float: left;height: 45px;line-height: 30px;width: 25%;border-bottom: 1px solid #ccc;padding:5px;}
input.req4105_input_text {min-width: 190px;height: 100%;border: 1px solid #ccc;display: block;padding-left: 15px;border-radius: 1px;}
textarea.req4105_textarea {width: 100%;height: 100%;resize: none;padding: 5px;border: 1px solid #ccc;border-radius: 1px;}
input.req4105_input_date {width: 100%;float: left;display: block;border-radius: 1px;height: 100% !important;background-color: #fff !important;border: 1px solid #ccc;text-align: center;}
input.req4105_charger,input.req4105_optCharger,input.req4105_cls,input.req4105_deploy {width: 195px !important;min-width: 100px;display: inline-block;float: left;margin-right: 5px;}
span.req4105_charger,span.req4105_optCharger,span.req4105_cls,span.req4105_deploy{height: 34px;line-height: 30px;width:30px;min-width: 30px;}
input.req4105_input_check {width: 100%;height: 80%;}
textarea.req4105_processBox_bottom[disabled] {background-color: #fff;white-space:normal;}
select.req4105_select {width: 100%;height: 100%;border-radius: 1px;border: 1px solid #ccc;text-align: center;font-size:10pt !important;}
input.req4105_input_text.req4105_readonly, textarea.req4105_textarea.req4105_readonly, select.req4105_select.req4105_readonly
,input.req4105_input_date[disabled]{cursor: default;background-color:#eee !important;}
img.ui-datepicker-trigger{float: left;margin-top: 2px;}
.req4105_option_half input.req4105_input_text
, .req4105_option_half textarea.req4105_textarea
, .req4105_option_half select.req4105_select {width:100%;height:100%;}
.req4105_option_all input.req4105_input_text, .req4105_option_all textarea.req4105_textarea, .req4105_option_all select.req4105_select{width: 100% !important;height:100% !important;}
.req4105_desc {height: 100px;}
.req4105_file {height: 150px;}
.req4105_clear{clear:both;}
.req4105_top_line{border-top:1px solid #ccc;}
.req4105_right_line{border-right:1px solid #ccc;}
input.req4105_input_date[disabled] {width: 100%;}
input.req4105_charger[readonly] ,input.req4105_optCharger[readonly] {width: 100% !important;}
.req4105_default_mask {position: absolute;width: 100%;/* height: 695px; */height:100%;background-color:rgba(0, 0, 0, 0);display:none;}
.endPrevStr{display:none;}

/* 프로세스 또는 이전 작업흐름 이력 목록 */
.req4105_processBox {width: 150px;display: inline-block;margin: 10px;margin-bottom: 0;border: 1px solid #ccc;border-radius: 5px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);height: 100px;padding: 5px;cursor: pointer;}
.req4105_processBox_top {width: 100%;height: 20px;line-height: 20px;text-align: center;font-weight: bold;border-bottom: 1px solid #ccc;font-size: 10pt;}
.req4105_processBox_bottom {height: 70px;padding: 2px 5px;font-size: 9pt;width: 138px;resize: none;border: none;overflow: hidden;cursor: pointer;}
.req4105_processBox:hover, .req4105_processBox.active {border: 1px solid #ff5643;}

/* 우측 작업흐름 목록 */
.req4105_flowBox {position: relative;width: 185px;height: 65px;text-align: center;border-radius: 0 0 10px 10px;border: 3px solid transparent;margin: 30px 10px 0 10px;display:inline-block;background-color: #F0F0F0;color: #000000;}
.req4105_flowOptionDiv {position: absolute;top: -24px;height: 24px;left: -3px;background-color: inherit;padding: 2px;border: 3px solid transparent;border-radius: 5px 5px 0 0;font-size: 10pt;border-bottom: 1px dashed #fff;width: 185px;text-align: left;}
.req4105_flowBox_title {width: 100%;height: 30px;line-height: 20px;font-weight: bold; padding-top: 5px;border-bottom: none;float:left;}
.req4105_flowBox_contents {width: 100%;height: 30px;border-radius: 0 0 8px 8px;line-height: 33px;background-color:#fff;color:#000;float:left;}
.req4105_flowBox.topFlowBox:hover, .req4105_flowBox.topFlowBox.flowActive{cursor:pointer;border: 3px solid #4b73eb;}
.req4105_flowBox.topFlowBox:hover .req4105_flowOptionDiv, .req4105_flowBox.topFlowBox.flowActive .req4105_flowOptionDiv{cursor:pointer;border: 3px solid #4b73eb;border-bottom: 1px dashed #fff;}

/* .req4105_flowFrameBox {position: relative;text-align: center;height: 130px;margin-bottom: 1px;border: 5px solid #f0efff;padding-top: 15px;} */
.req4105_flowFrameBox {position: relative;text-align: center;height: 130px;margin-bottom: 1px;padding-top: 15px;border: 5px solid transparent;}
.req4105_addOptionFrame {
    padding: 10px;
    border: 1px solid #ccc;
    border-top: none;
    float: left;
    width: 100%;
    display: inline-block;
}
/* .req4105_flowFrameBox:hover{cursor:pointer;} */
.req4105_flowFrameBox.active{display:block;}
/* .req4105_flowTopArrowBox {position: absolute;top: 0px;left: 100px;background: url(/images/right_arrow2.png);background-size: 44px 44px;width: 44px;height: 35px;transform: rotate(90deg);} */
.req4105_flowTopArrowBox {position: relative;top: 0px;left: 100px;background: url(/images/right_arrow2.png) center;background-size: 42px 42px;width: 42px;height: 35px;transform: rotate(90deg);}
.req4105_flowBox_contents > span {font-size: 9pt;}
.req4105_flowOptionDiv > li {margin: 0 2px;font-size: 10pt;}
.req4105_flow_topArrowBox {background: url(/images/right_arrow2.png);background-size: 44px 44px;display: inline-block;width: 44px;height: 44px;margin: 40px 10px 0 10px;background-repeat: no-repeat;}

/* 파일 리스트 */
.uploadOverFlow.optFileDiv, #dragandrophandler{width:81%;height:100%;max-height: 100%;overflow-y: auto;border: 1px solid #fff;padding: 5px;float:left;}
.uploadOverFlow.optFileDiv:hover, #dragandrophandler:hover{border:1px solid #fff;cursor: pointer;}

.req4105_option_all.req4105_file {padding: 0;}
.req4105_fileBtn {display: inline-block;width: 17%;height: 93%;line-height: 25px;text-align: center;border-radius: 2px;border: 1px solid #b8b8b8;cursor: pointer;background-color: #fff;color: #353535;margin: 5px 8px 5px 0;float: right;padding: 50px 0;}
.req4105_fileBtn:hover {background-color: #353553;color: #fff;}
.uploadOverFlow.widthAll{width:100% !important;}

/* 버튼 */
.req4105_close{width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;background-color: #fff;color: #000;}
.req4105_signBtn, .req4105_complete {width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin-right:20px;background-color: #fff;color: #000;}
.req4105_chargerChgBtn {display:none;width: 140px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin-right:20px;background-color: #fff;color: #000;}
.req4105_nextFlowBtn {width: 100px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin:0 10px;background-color: #fff;color: #000;}
.req4105_signBtn{width: 200px;display:none;}
.req4105_btnDiv {width: 100%;height: 100px;line-height: 100px;text-align: center;padding-left: 100px;}

/* 결재 */
.req4105_signBox {float:left;width: 100%;border-right: 1px solid #ccc;display: none;}
.req4105_signBox input.req4105_charger {width: 244px !important;}
.req4105_sign_maskBox {display:none;position: absolute;right: 0;width: 100%;height: 100%;min-height: 745px;background-color: rgba(0, 0, 0, 0.6);z-index: 2;color: #fff;padding-top: 350px;padding-left: 50px;text-align: left;line-height: 30px;}
span#signUsrHtml > img {display: inline-block;padding-left: 75px;width: 135px;height: 60px;}
.req4105_signHistoryDiv {width: 100%;height: 200px;border: 1px solid #ccc;overflow-y: hidden;overflow-x: auto;padding:0 10px;white-space:nowrap;position: relative;}

/* 작업 */
#req4105_work{display:none;border-right: none;border-bottom: 1px solid #ccc;}
.req4105_work_btnBox {height: 45px;line-height: 45px;border: 1px solid #ccc;border-bottom: none;border-top: none;background-color: #f9f9f9;font-weight: bold;padding-left: 10px;}
.req4105_work_frame {width: 100%;height: 210px;padding: 5px;border: 1px solid #ccc;border-bottom: none;}
div.req4105_work_btn {float: right;margin: 5px 10px;border: 1px solid #b8b8b8;width: 100px;height: 35px;line-height: 35px;text-align: center;box-shadow: 1px 1px 1px #ccd4eb;background-color: #fff;border-radius: 5px;cursor:pointer;}

/* 리비전 */
#req4105_revision{display:none;border-right: none;}
.req4105_revision_btnBox {height: 45px;line-height: 45px;border: 1px solid #ccc;border-bottom: none;border-top: none;background-color: #f9f9f9;font-weight: bold;padding-left: 10px;}
.req4105_revision_frame {width: 100%;height: 210px;padding: 5px;border: 1px solid #ccc;}
div.req4105_revision_btn {float: right;margin: 5px 10px;border: 1px solid #b8b8b8;width: 100px;height: 35px;line-height: 35px;text-align: center;box-shadow: 1px 1px 1px #ccd4eb;background-color: #fff;border-radius: 5px;cursor:pointer;}

/* 배포계획 */
.req4105_dplBox {float:left;width: 100%;border-right: 1px solid #ccc;display: none;}
.req4105_dplBox input.req4105_deploy {width: 244px !important;}

/* 작업흐름 선택 */
.req4105_flowSelect_maskBox {display:none;width: 100%;height: 856px;position: absolute;left: 0;background-color: rgba(0, 0, 0, 0.6);z-index: 5;}
.req4105_flowSelectBox {display:none;width: 20%;height: 804px;position: absolute;z-index: 6;top: 60px;left: 40%;background-color: #fff;border: 1px solid #ccc;}
.req4105_flowSelectBox_title {width: 100%;height: 44px;padding: 15px;font-weight: bold;color: #fff;background-color: #4b73eb;}
.req4105_flowSelectBox_top {height: 710px;overflow-y: scroll;padding-top: 1px;}
.req4105_flowSelectBox_bottom {height: 50px;text-align: center;border-top: 1px solid #8f95b3;padding: 4px 0;}
.req4105_flowSelectBox_top > .req4105_flowFrameBox:hover{border: 5px solid #4b73eb;}
.req4105_flowSelectBox_top > .req4105_flowFrameBox.active{border: 5px solid #4b73eb;}

/* 폴딩 */
.req4105_titleFoldingBtn {
    float: right;
    height: 35px;
    line-height: 32px;
    width: 45px;
    border: 2px solid #fff;
    background-color: rgba(255, 255, 255, 0.1);
    padding: 0 10px;
    cursor: pointer;
    margin: 5px 10px 2px 2px;
    border-radius: 3px;
    text-align: center;
}
.req4105_titleFoldingBtn:hover{background-color: #414352;color: #fff;border-radius: 10px;}
.req4105_titleFoldingContent {font-family: "Font Awesome 5 Free";font-size: 15pt;}
.req4105_titleFoldingContent.down::before{content: "\f107";}
.req4105_titleFoldingContent.up::before{content: "\f106"; }
.req4105_titleFolded{border-radius:5px;border-bottom: 1px solid #ccc;}
.req4105_frameTitleDiv:not(.req4105_titleFolded){border-bottom: none;}
/*결재이력*/
.req_main_box{height: 170px;width: 150px;display: inline-block;float: left;}
.req_top_box{border-left: 1px dotted #a4a7bb;height: 20px;padding-left: 5px;font-size: 9pt;line-height: 20px;display:block;}
.req_bottom_box{height: 150px;border: 1px solid #a4a7bb;border-radius: 0 0 5px 5px;padding: 5px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);font-size: 9pt;display:inline-block;width:100%;}
.req_box_title{padding-bottom: 4px;margin-bottom: 1px;text-align: center;}
.req_box_title.signDiv{height: 20px;line-height: 18px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);}
.req_box_main{height: 20px;line-height: 15px;text-align: center;padding: 2px 0;position: relative;}
.req_box_main.signRejectDiv{height: 80px;text-align: left;white-space: normal;width: 138px;resize:none;overflow-y: auto;font-size: 10pt;}
.req_box_main.signRejectDiv:hover {cursor: pointer;border: 1px solid #4b73eb;}
.req_box_main.signRejectUsrDiv{height: 30px;line-height: 25px;border: 1px solid #ccc;border-radius: 5px;}
.req_box_main.prev{box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);color:#fff;margin-top: 20px;}
.req_box_main.regUsrImg {width: 100%;height: 54px;display: block;text-align: left;clear: both;}
.req_box_main.regUsrImg > img {width: 50px;height: 50px;float: left;}
.req_box_main.regUsrImg > div {float: left;height: 25px;width: calc(100% - 50px);text-align: center;line-height: 25px;}
.req_box_main.signAcceptDiv {height: calc(100% - 20px);padding-top: 10px;}
.req_box_main.signAcceptDiv > div {height: 30px;line-height: 25px;border: 1px solid #ccc;margin-top: 10px;border-radius: 5px;}
.req_box_main.signAcceptDiv > img {width: 60px;height: 60px;}
.req_box_main.bottom:nth-child(1){margin-top:5px;}
.req_box_main.bottom{border: 1px solid #ccc;margin-bottom: 1px;}
.historyUsrDiv:hover{background-color:#323a47;color:#fff;cursor:pointer;}
.req4105_signFrame{display: inline-block;}
.req4105_flow_topArrowBox.signArrow{height:100px;}
.req4105_sign_TitleBox {
    height: 45px;
    line-height: 45px;
    border: 1px solid #ccc;
    border-bottom: none;
    border-top: none;
    background-color: #f9f9f9;
    font-weight: bold;
    padding-left: 10px;
    float: left;
    width: 100%;
}
.req4105_revision_TitleBox {
    height: 45px;
    line-height: 45px;
    border: 1px solid #ccc;
    border-top: none;
    background-color: #f9f9f9;
    font-weight: bold;
    padding-left: 10px;
    float: left;
    width: 100%;
}
.req4105_addOption_TitleBox {
    height: 1px;
    border-bottom: 1px solid #ccc;
    float: left;
    width: 100%;
}
/* 체크박스 */
.req4105_chk { position: relative;text-align: center;}
.req4105_chk input[type="checkbox"] {margin:0; opacity: 0; position: absolute; z-index: 2; width: 100%;height: 40px;left: 0;}
.req4105_chk input[type="checkbox"]+label { display: inline-block; width: 18px; height: 18px; background: url(/images/contents/normal_check.png) no-repeat; font-size: 1em; line-height: 24px; vertical-align: middle; cursor: pointer; z-index: 1; border-radius: 3px; margin-top: 10px;}
.req4105_chk input[type="checkbox"]:checked+label { display: inline-block; width: 18px; height: 18px; background: url('/images/contents/normal_check_on.png') no-repeat; line-height: 24px; vertical-align: middle; cursor: pointer; }
/* 체크박스 */
.req4105_end_maskBox {display: none;position: absolute;right: 0;width: 100%;height: 100%;min-height: 745px;background-color: rgba(0, 0, 0, 0.6);z-index: 2;color: #fff;padding-top: 300px;padding-left: 20px;text-align: left;line-height: 30px;}
</style>
<script>
//요구사항 Id, 로그인 사용자 Id
var reqId = "${reqId}";
var usrId = "${usrId}";

//파일 전송 FormData
var fd = new FormData();
//중복 파일 업로드 방지 전역변수
var fileChk = new Array();
var fileAppendList = new Array();

//현재 요구사항 담당자 Id
var reqUsrId;

//선택 프로세스 Id
var processId;

//요구사항 기본 atchFileId
var reqAtchFileId;

//수정 불가능한 첨부파일 Id리스트
var readonlyFileIdList = [];

//필수 첨부파일 id리스트
var essentialCdFileIdList = [];

/* 필수입력값 체크 공통 호출 */
var strFormId = "req4105InfoForm";
var strCheckObjArr = ["reqStDuDtm","reqEdDuDtm"];
var sCheckObjNmArr = ["작업 시작 예정일자","작업 종료 예정일자"];

//프로젝트 기간
var prjStartDt = '${selPrjInfo.startDt}';
var prjEndDt = '${selPrjInfo.endDt}';

//현재 작업흐름 정보
var flowInfo;

//작업 그리드
var work_grid;

//리비전 그리드
var revision_grid;

//결재 대기중 상태 체크
var signWaitChk = false;

//마지막 결재 정보
var lastChkInfo;

//최종 선택 작업흐름 nextId
var selFLowId;
var selFlowNextId;

//역할그룹 제한(null인경우 전체)
var authGrpList = [];	//01- 담당자
var signAuthGrpList = []; //02- 결재자

//로그인 사용자 역할그룹
var loginUsrAuthGrpId = "${sessionScope.selAuthGrpId}";

//파일 업로드 제한 사이즈
var FILE_INFO_MAX_SIZE = "${fileInfoMaxSize}";
var FILE_SUM_MAX_SIZE = "${fileSumMaxSize}";

//유효성 체크 값 (기본값 - 진척률 reqCompleteRatio)
var arrChkObj = {"reqCompleteRatio":{"type":"number"}
				,"reqFp":{"type":"number"}
				,"reqExFp":{"type":"number"}};
					
$(document).ready(function() {
	//파일 목록 초기화
	dndCancel();
	
	//데이터 불러오기
	fnReq4105DataLoad();
	
});

//이전으로 돌아갈수있는 작업흐름 목록
var prevFlowList = [];

/**
 * [데이터 불러오기]
 * - 프로세스
 * - 작업흐름
 * - 신규요구사항 유무
 * - 요구사항 정보
 */
function fnReq4105DataLoad(){
	//AJAX 설정 
	var ajaxObj = new gfnAjaxRequestAction({"url":"<c:url value='/req/req4000/req4100/selectReq4105DataLoad.do'/>",loadingShow:false,async:true}
	,{reqId: reqId, dshType:"dsh1000"});
   	gfnShowLoadingBar(true);
    		
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	if(data.errorYN == 'Y'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		
    		var default_mstCdStrArr = 'REQ00012|CMM00001|REQ00011';
			var default_arrComboType = ["OS","OS","OS"];
			var default_arrObj = [$("#reqTypeCd"),$("#piaCd"),$("#sclCd")];
		
    		//기본항목 SELECT 우선 생성
			var strUseYn = 'Y';
			gfnGetMultiCommonCodeDataForm(default_mstCdStrArr, strUseYn, default_arrObj, default_arrComboType , false);
    		
    		//프로세스아이디
    		processId = data.processId;

    		//기본 항목 값 세팅
    		gfnSetData2ParentObj(data.reqInfoMap,"req4105InfoForm");

    		//기본 파일 Id
			reqAtchFileId = data.reqInfoMap.atchFileId;
    		
    		//타이틀 요구사항명 세팅
    		var titleReqNm = data.reqInfoMap.reqNm;
    		$("span#popup_titleReqNm").text(gfnCutStrLen(titleReqNm,100));
    		
    		//작업흐름 데이터로 우측 세팅 데이터 구하기
    		fnRightFlowDivSetData(data.reqInfoMap.flowId,data.flowList);
    		
    		var tmp_prevFlowId = null;
    		//최종완료 이전 작업흐름 Id 구하기
    		$.each(data.flowList,function(idx, map){
    			//최종완료 체크
    			if(gfnIsNull(map.flowNextId)){
    				//현재 작업흐름 Id가 이전 Id인경우
    				if(flowInfo.flowId == tmp_prevFlowId){
	    				//시작, 종료일자 필수
						strCheckObjArr.push("reqStDtm","reqEdDtm");
						sCheckObjNmArr.push("작업 시작 일시","작업 종료 일시");
						
						//필수 문자열 보이기
						$(".endPrevStr").show();
						return false;
    				}
    			}
    			tmp_prevFlowId = map.flowId;
    		});
    		//이전으로 돌아갈수있는 작업흐름 목록 구하기
    		var flowPushChk = false;
    		$.each((data.flowList).reverse() ,function(idx, map){
    			//최종완료 제외
    			if(gfnIsNull(map.flowNextId)){
    				return true;
    			}
    			//현재 작업흐름인경우 제외
    			if(flowInfo.flowId == map.flowId){
    				//다음 작업흐름부터 담기
    				flowPushChk = true;
    				return true;
    			}
    			//결재있는경우 stop
    			if(map.flowSignCd == "01"){
    				return false;
    			}
    			if(flowPushChk){
	    			//이전 작업흐름 목록에 추가
	    			prevFlowList.push(map);
    			}
    			
    			//필수인경우 stop
    			if(map.flowEssentialCd == "01"){
    				return false;
    			}
    		});
    		
    		//'01' 접수가 아닌경우 현재 요구사항의 이전 작업흐름 이력 세팅
    		var reqTopData = fnTopReqChgDivSetData(data.reqChgList);

    		//성능 개선활동 여부 체크
    		fnPiaCdChg($("#piaCd")[0]);
    		
    		//상단 내용 채우기
    		$("#req4105_reqTopDataList").html(reqTopData);
    		
    		//스크롤 최 우측
    		$("#req4105_reqTopDataList").scrollLeft(9999);
    		
    		//현재 요구사항 담당자 Id
    		reqUsrId = data.reqInfoMap.reqChargerId;
    		
    		//요구사항 담당자가 아닌경우
    		if(reqUsrId != usrId){
    			//작업 관리 제외(작업 종료 버튼 제외)
    			$(".req4105_work_btn:not(.req4105_charger_work)").hide();
    			
    			//리비전 관리 제외
    			$(".req4105_revision_btn").hide();
    			
    			//완료버튼
    			$(".req4105_complete").hide();
    			
    			//mask 열기
    			$(".req4105_default_mask").show();
    			
    			//기본 파일 업로드 width 100%
   				$("#dragandrophandler").addClass("widthAll");
    			
    			//drop&drop 문구 가리기
    			$(".file_dragDrop_info").hide();
    			
    			//파일 드래그 이벤트 제거
				$("#dragandrophandler").off("dragenter, dragover, drop");
				$("#btn_insert_fileSelect").hide();
				
				//배포 계획 버튼
   				$('.btn_select_dplId').hide();
   				//readonly 걸기
	 			$(".req4105_input_text.req4105_deploy").attr("readonly","readonly");
    		}
    		//배포 목록 세팅
    		var reqDplList = data.reqDplList;
    		fnReqDplSetting(reqDplList);
    		
    		//결재 목록
    		var reqChkList = data.reqChkList;
    		
    		//결재 목록 있는경우 이력 세팅
    		if(!gfnIsNull(reqChkList)){
    			fnReqChkSetting(reqChkList);
    		}else{
    			$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").hide();
    		}
    		
    		//결재 작업흐름인경우 결재 분기
    		if(flowInfo.flowSignCd == "01"){
    			//결재 영역 스위치
    			var signBoxShow = true;
    			
    			//결재 목록 존재하는지 체크
    			if(!gfnIsNull(reqChkList) && reqChkList.length > 0){
	    			//마지막 결재 정보 가져오기
	    			lastChkInfo = reqChkList[reqChkList.length-1];
	    			
	    			//결재 대기중인지 체크
	    			if(lastChkInfo.signCd == "01"){
	    				//결재 대기 마스크 처리
	    				signBoxShow = false;
	    				
	    				//결재 완료시 변경되는 작업흐름 sel
	    				selFlowNextId = $(".req4105_flowFrameBox[id="+lastChkInfo.signFlowId+"]").attr("nextid");
	    				
	    				//결재자 img
						var signUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+lastChkInfo.signUsrImg;
				
	    				//결재자명 출력
	    				$("#signUsrHtml").html(
	    						'<br><i class="fa fa-user-check"></i>&nbsp;결재자: '+lastChkInfo.signUsrNm
	    						+'<br><img src="'+signUsrImgSrc+'">');
	    			}
    			}

    			//결재 영역 스위치
    			if(signBoxShow){
	    			//결재 영역 보이기
	    			$("#signIdSelBox").show();
	    			
	    			//클릭 이벤트
	    			$("#btn_select_signUser").click(function() {
	    				var authData = $('#reqSignNm').val();
					
						//허용 권한 있는경우 권한으로 검색
						if(!gfnIsNull(signAuthGrpList)){
							var signAuthGrpIds = [];
							$.each(signAuthGrpList,function(idx, map){
								signAuthGrpIds.push(map.authGrpId);
							});
							
							authData= { "usrNm" :  $('#reqSignNm').val() , authGrpIds : signAuthGrpIds };
						}
						gfnCommonUserPopup(authData,false,function(objs){
							if(objs.length>0){
								$('#reqSignId').val(objs[0].usrId);
								$('#reqSignNm').val(objs[0].usrNm);
							}
						});
					});
					
					$('#reqSignNm').keyup(function(e) {
						if($('#reqSignNm').val()==""){
							$('#reqSignId').val("");
						}
						if(e.keyCode == '13' ){
							$('#btn_select_signUser').click();
						}
					});
					
    			}else{
    				//결재 마스크 걸기
    				$(".req4105_sign_maskBox").show();
    				
    				signWaitChk = true;
    				//완료버튼
    				$(".req4105_complete").hide();
    				//작업 관리 버튼
    				$(".req4105_work_btn").hide();
    				//리비전 버튼
    				$(".req4105_revision_btn").hide();
    				//mask 열기
    				$(".req4105_default_mask").show();
    				//배포계획
    				$(".req4105_input_text.req4105_deploy").attr("readonly","readonly");
    				
    				//기본 파일 업로드 width 100%
   					$("#dragandrophandler").addClass("widthAll");
    				
    				//drop&drop 문구 가리기
    				$(".file_dragDrop_info").hide();
    				
    				//로그인 사용자와 결재자 Id가 같은경우
    				if(usrId == lastChkInfo.signUsrId){
    					$(".req4105_signBtn").css({"display":"inline-block"});
    				}
    			}
    		}
    		
    		//추가 항목 데이터 세팅 전에 실행되어야 함.
    		//작업흐름에 역할그룹 제한이 있는경우
    		if(flowInfo.flowAuthCd == "01"){
    			var flowAuthGrpList = data.flowAuthGrpList;
    			//담당자, 결재자 구분
    			if(!gfnIsNull(flowAuthGrpList)){
    				$.each(flowAuthGrpList,function(idx, map){
    					if(map.authGrpTargetCd == "01"){	//담당자
    						authGrpList.push(map);	
    					}else{	//결재자
    						signAuthGrpList.push(map);
    					}
    				});
    			}
    		}
    		
    		//결재 로직 이후에 실행되어야 함.
    		//추가 항목 데이터 세팅
    		fnFlowOptDivSetData(data.optList);
    		
    		//첨부파일 세팅하기 (추가 항목 다음에 실행)
    		if(!gfnIsNull(data.fileList)){
    			if(data.fileList.length > 0){
		        	//info 정보 show
		        	$('#dndCancel').show();
		        }
    			
				//첨부파일 리스트 세팅
			 	$.each(data.fileList, function(idx, fileVo){
			 		fileVo['reqId'] = reqId;
			 		
			 		//파일 atchFileId
			 		var fileAtchFileId = fileVo.atchFileId;
			 		
			 		//삭제버튼 표시유무
			 		var delChk = true;
			 		
			 		//기본 파일
			 		if(reqAtchFileId == fileAtchFileId){
			 			//결제 대기상태인경우 삭제버튼 숨김
			 			if(signWaitChk){
			 				delChk = false;
			 			}
			 			
				 		gfnFileListReadDiv(fileVo,"#dragandrophandler", "req",delChk);
			 		}else{
			 			//수정 불가이거나 결제 대기상태인경우 삭제버튼 숨김
			 			if(readonlyFileIdList.indexOf(fileAtchFileId) > -1 || signWaitChk){
			 				delChk = false;
			 			}
			 			gfnFileListReadDiv(fileVo,"#fileDiv_"+fileAtchFileId, "req",delChk);
			 		}
			    });
			}
    		
    		//작업흐름에 작업이 있는 경우 작업 관리 열기
    		if(flowInfo.flowWorkCd == "01"){
    			//작업 관리 영역 보이기
    			$("#req4105_work").show();
    			
    			//작업 그리드 세팅
    			fnWorkGridSetting();
    			
				work_grid.setData(data.workList);
    		}
    		
    		//작업흐름에 리비전이 있는 경우 리비전 관리 열기
    		if(flowInfo.flowRevisionCd == "01"){
    			//리비전 관리 영역 보이기
    			$("#req4105_revision").show();
    			
    			//리비전 그리드 세팅
    			fnRevisionGridSetting();
    			
				revision_grid.setData(data.reqRevisionList);
    		}
    		
    		//작업흐름에 배포계획 배정 확인
    		if(flowInfo.flowDplCd == "01"){
    			//배포계획 레이아웃 열기
    			$("#dplIdSelBox").show();
    			
   				//결제 대기상태인경우 버튼 숨김
	 			if(signWaitChk){
	 				//배포 계획 버튼
	   				$('.btn_select_dplId').hide();
	   				//readonly 걸기
		 			$(".req4105_input_text.req4105_deploy").attr("readonly","readonly");
	 			}
    		}
    		
    		//최종 종료 상태 작업흐름인경우
    		if(gfnIsNull(flowInfo.flowNextId)){
    			//최종완료 마스크 열기
    			$(".req4105_end_maskBox").show();
    			//완료버튼
   				$(".req4105_complete").hide();
   				//작업 관리 버튼
   				$(".req4105_work_btn").hide();
   				//리비전 관리 버튼
   				$(".req4105_revision_btn").hide();
   				//배포 계획 버튼
   				$('.btn_select_dplId').hide();
   				//readonly 걸기
	 			$(".req4105_input_text.req4105_deploy").attr("readonly","readonly");
   				
   				//mask 열기
    			$(".req4105_default_mask").show();
   				
   				//기본 파일 업로드 width 100%
   				$("#dragandrophandler").addClass("widthAll");
   				
   				//drop&drop 문구 가리기
    			$(".file_dragDrop_info").hide();
   				
   				//파일 삭제 버튼 숨기기
   				$(".file_btn.file_delete").hide();
    		}
    		
    		//결재 대기,최종 완료 상태가 아닌경우 날짜, 담당자,파일 첨부 세팅
    		if(!signWaitChk && !gfnIsNull(flowInfo.flowNextId)){
				//작업 기간 설정
				gfnCalRangeSet("reqStDtm", "reqEdDtm", prjStartDt, prjEndDt,true);
				
				//작업 예정 일자
				gfnCalRangeSet("reqStDuDtm", "reqEdDuDtm", prjStartDt, prjEndDt);
				
				//담당자 검색 걸기
				$("#btn_user_select").click(function() {
					var authData = $('#reqChargerNm').val();
					
					//허용 권한 있는경우 권한으로 검색
					if(!gfnIsNull(authGrpList)){
						var authGrpIds = [];
						$.each(authGrpList,function(idx, map){
							authGrpIds.push(map.authGrpId);
						});
						
						authData= { "usrNm" :  $('#reqChargerNm').val() , authGrpIds : authGrpIds };
					}
					gfnCommonUserPopup(authData,false,function(objs){
						if(objs.length>0){
							$('#reqChargerId').val(objs[0].usrId);
							$('#reqChargerNm').val(objs[0].usrNm);
						}
					});
				});
				
				$('#reqChargerNm').keyup(function(e) {
					if($('#reqChargerNm').val()==""){
						$('#reqChargerId').val("");
					}
					if(e.keyCode == '13' ){
						$('#btn_user_select').click();
					}
				});
				
				//분류 검색 걸기
				$("#btn_cls_select").click(function() {
					gfnCommonClsPopup(function(reqClsId,reqClsNm){
						$("#reqClsId").val(reqClsId);
						$("#reqClsNm").val(reqClsNm);
					});
				});
				
				//담당자 일경우에만
				if(reqUsrId == usrId){
					//첨부파일 업로드 설정 (Select)
					var maxFileNum = 30;
					if(maxFileNum==null || maxFileNum==""){
						maxFileNum = 3;
					}
					
					var multi_selector = new MultiSelector( document.getElementById( 'egovFileStatus' ), maxFileNum );
					multi_selector.addElement( document.getElementById( 'egovFileUpload' ) );	
					
					//선택 업로드 걸기
					$("#dragandrophandler").click(function(){
						document.getElementById('egovFileUpload').click();		
					});
					
					//허용 권한 있는경우 권한으로 검색
					if(!gfnIsNull(authGrpList)){
						var authGrpIds = [];
						$.each(authGrpList,function(idx, map){
							authGrpIds.push(map.authGrpId);
						});
						
						//역할그룹 체크
						if(authGrpIds.indexOf(loginUsrAuthGrpId) == -1){							
							//허용되지 않은 역할인경우 담당자 이관 버튼 열기
							$(".req4105_chargerChgBtn").css({"display": "inline-block"});
							//완료버튼
			   				$(".req4105_complete").hide();
			   				//작업 관리 버튼
			   				$(".req4105_work_btn").hide();
			   				//리비전 관리 버튼
			   				$(".req4105_revision_btn").hide();
			   				//배포 계획 버튼
			   				$('.btn_select_dplId').hide();
			   				//readonly 걸기
				 			$(".req4105_input_text.req4105_deploy").attr("readonly","readonly");
			   				
			   				//결재자 제외
			   				$("#signIdSelBox").hide();
			   				
			   				//mask 열기
			    			$(".req4105_default_mask").show();
			   				
			   				//기본 파일 업로드 width 100%
			   				$("#dragandrophandler").addClass("widthAll");
			   				
			   				//drop&drop 문구 가리기
			    			$(".file_dragDrop_info").hide();
			   				
			   				//파일 삭제 버튼 숨기기
			   				$(".file_btn.file_delete").hide();
			   				
			   				//파일 드래그 이벤트 제거
							$("#dragandrophandler").off("dragenter, dragover, drop, click");
							$("#btn_insert_fileSelect").hide();
						}
					}
				}
			}else{
				//파일 드래그 이벤트 제거
				$("#dragandrophandler").off("dragenter, dragover, drop");
				$("#btn_insert_fileSelect").hide();
			}
    		
    		//폴딩 버튼 동작
			$("div.req4105_titleFoldingBtn").click(function(){
				//폴더 타겟
				var $foldBtn = $(this).children(".req4105_titleFoldingContent");
				
				//폴드 번호
				var foldNum = $foldBtn.attr("folding");
				
				//true - 닫혀있음, false - 열려있음
				var foldLayoutDown = $foldBtn.hasClass("down");
				
				//foldDiv target
				var $foldDiv = $(".req4105_foldDiv[folding="+foldNum+"]");
				
				//foldDiv toggle
				$foldDiv.slideToggle();
				
				//div on/off
				if(foldLayoutDown){
					$(this).parent(".req4105_frameTitleDiv").removeClass("req4105_titleFolded");
					$foldBtn.removeClass("down").addClass("up");
				}else{
					$(this).parent(".req4105_frameTitleDiv").addClass("req4105_titleFolded");
					$foldBtn.removeClass("up").addClass("down");
				}
			});
    		gfnShowLoadingBar(false);
    	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push("ERROR STATUS("+status+")<br>"+err);
 	});
	
	//AJAX 전송
	ajaxObj.send();
}

//변경이력 목록 데이터 받아와서 상단 세팅
function fnTopReqChgDivSetData(reqChgList){
	var reqTopData = "";
	
	//상단 레이아웃에 이전 작업흐름 목록 세팅
	$.each(reqChgList,function(idx, map){
		//각 옵션 체크
		var flowOptionStr = "";
 		//flowNextId 없는경우
		if(gfnIsNull(map.chgFlowNextId) || map.chgFlowNextId == "null"){
			flowOptionStr += "<li class='far fa-stop-circle' title='[종료]최종 완료'></li>";
		} 
		//필수 체크
		if(map.chgFlowEssentialCd == "01"){
			flowOptionStr += '<li class="fa fa-key" title="필수"></li>';
		}
		//결재 체크
		if(map.chgFlowSignCd == "01"){
			flowOptionStr += '<li class="fa fa-file-signature" title="결재"></li>';
		}
		//종료분기 체크
		if(map.chgFlowEndCd == "01"){
			flowOptionStr += '<li class="fa fa-sign-out-alt" title="종료 분기"></li>';
		}
		//작업 체크
		if(map.chgFlowWorkCd == "01"){
			flowOptionStr += '<li class="fa fa-code-branch" title="작업 분기"></li>';
		}
		//리비전 체크
		if(map.chgFlowRevisionCd == "01"){
			flowOptionStr += '<li class="fa fa-code" title="리비전 저장 유무"></li>';
		}
		//배포계획 체크
		if(map.chgFlowDplCd == "01"){
			flowOptionStr += '<li class="fa fa-puzzle-piece" title="배포계획 저장 유무"></li>';
			
			//배포계획 div 만들기
			$("#req4105_dplDivFrame").append(
					'<div class="req4105_dplBox" flowid="'+map.chgFlowId+'">'
						+'<div class="req4105_option_title">배포 계획</div>'
						+'<div class="req4105_option_all">'
						+'	<input type="hidden" name="dplId_'+map.chgFlowId+'" id="dplId_'+map.chgFlowId+'" title="배포 계획" opttype="05" opttarget="03" optflowid="'+map.chgFlowId+'"/>'
						+'	<input type="text" title="배포 계획" class="req4105_input_text req4105_deploy" name="dplNm_'+map.chgFlowId+'" id="dplNm_'+map.chgFlowId+'" modifyset="02" flowid="'+map.chgFlowId+'"/>'
						+'	<span class="button_normal2 fl req4105_deploy btn_select_dplId" id="btn_select_dplId" flowid="'+map.chgFlowId+'"><li class="fa fa-search"></li></span>'
						+'</div>'
					+'</div>');
  			//클릭 이벤트
   			$(".btn_select_dplId[flowid="+map.chgFlowId+"]").click(function() {
   				var data = { "dplNm" :  $('#dplNm_'+map.chgFlowId).val() , "dplSts" : "01" }; 
				gfnCommonDplPopup(data ,false,
					function(objs){
						if(objs.length>0){
							$('#dplId_'+map.chgFlowId).val(objs[0].dplId);
							$('#dplNm_'+map.chgFlowId).val(objs[0].dplNm);
						}
				});
			});
			
			$('#dplNm_'+map.chgFlowId).keyup(function(e) {
				if($('#dplNm_'+map.chgFlowId).val()==""){
					$('#dplId_'+map.chgFlowId).val("");
				}
				if(e.keyCode == '13' ){
					$(".btn_select_dplId[flowid="+map.chgFlowId+"]").click();
				}
			});
		}
		//허용 역할
		if(map.chgFlowAuthCd == "01"){
			flowOptionStr += '<li class="fa fa-user-shield" title="허용 역할그룹 제한유무"></li>';
		}
		//추가항목 체크
		if(map.chgFlowOptCnt > 0){
			flowOptionStr += '<li class="fa fa-list" title="추가 항목"></li>+'+map.chgFlowOptCnt;
		}
	
		//옵션이 있는경우 div세팅
		//if(!gfnIsNull(flowOptionStr)){
			flowOptionStr = '<div class="req4105_flowOptionDiv">'+flowOptionStr+'</div>';
		//}
		
		//우측 화살표
		var topArrowBox = '';
		
		//최종완료가 아닌경우 
		if(!gfnIsNull(flowInfo.flowNextId) || flowInfo.flowId != map.chgFlowId){
			//화살표 표시
			topArrowBox = '<div class="req4105_flow_topArrowBox"></div>';
		}
		
		//active
		var flowActive = "";
		
		//마지막 idx인경우 active처리
		if(reqChgList.length-1 == idx){
			flowActive = " flowActive";
			
			//배포계획 있는경우 활성화
			if($(".req4105_dplBox[flowid="+map.chgFlowId+"]").length > 0){
				$(".req4105_dplBox[flowid="+map.chgFlowId+"]").show();
			}
		}
		
		reqTopData += 
				'<div class="req4105_flowBox topFlowBox'+flowActive+'" fileid="'+map.atchFileId+'" flowid="'+map.chgFlowId+'" workcd="'+map.chgFlowWorkCd+'" revisioncd="'+map.chgFlowRevisionCd+'" authcd="'+map.chgFlowAuthCd+'" dplcd="'+map.chgFlowDplCd+'" style="background-color: '+map.chgFlowTitleBgColor+';color: '+map.chgFlowTitleColor+';" onclick="fnSelectTopFlow(this)">'
				+flowOptionStr
				+'	<div class="req4105_flowBox_title">'+map.chgFlowNm+'</div>'
				+'	<div class="req4105_flowBox_contents" flowid="'+map.chgFlowId+'" style="background-color: '+map.chgFlowContentBgColor+';color: '+map.chgFlowContentColor+';">'
				+new Date(map.chgDtm).format('yyyy-MM-dd HH:mm:ss')
				+'</div>'
				+'</div>'
				+topArrowBox;
	});
	
	return reqTopData;
}

//작업흐름 Id로 이력 보이기&감추기
function fnSelFlowObjHidden(flowId){
	//현재 작업흐름 결재 이력 보이기
	$(".req_main_box.req_bottom[prevflowid="+flowId+"]").show();
	$(".req4105_flow_topArrowBox.signArrow[prevflowid="+flowId+"]").show();
	
	//현재 작업흐름과 작업흐름 Id가 다른 결재 이력 감추기
	$(".req_main_box.req_bottom[prevflowid!="+flowId+"]").hide();
	$(".req4105_flow_topArrowBox.signArrow[prevflowid!="+flowId+"]").hide();
	
	//결재 이력이 0개인경우 레이아웃 닫기
	if($(".req_main_box.req_bottom[prevflowid="+flowId+"]").length == 0){
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").hide();
	}else{
		//레이아웃 열기
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").show();
	}
	
	//현재 작업흐름 추가 항목 보이기
	$("#req4105_add_option .req4105_option_title[optflowid="+flowId+"]").show();
	$("#req4105_add_option .req4105_option_half[optflowid="+flowId+"]").show();
	$("#req4105_add_option .req4105_option_all[optflowid="+flowId+"]").show();
	
	//현재 작업흐름과 작업흐름 Id가 다른 항목 감추기
	$("#req4105_add_option .req4105_option_title[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_half[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_all[optflowid!="+flowId+"]").hide();
	
	//마지막 항목이 hlaf이고 홀수인경우 항목 채우기_마지막 작업흐름 Id
	if($("#req4105_add_option > div").eq($("#req4105_add_option > div").length-1).hasClass("req4105_option_half")){
		if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").length%2 == 1){
			var descClass = '';
			//desc인경우 class 추가
			if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").hasClass("req4105_desc")){
				descClass = " req4105_desc";
			}
			
			$("#req4105_add_option").append('<div class="req4105_option_title'+descClass+'" optflowid="'+flowId+'"></div><div class="req4105_option_half'+descClass+'" optflowid="'+flowId+'"></div>');
		}
	}
}
//상단 작업흐름 클릭시
function fnSelectTopFlow(thisObj){
	//결재 대기중인경우 확인불가
/* 	if(signWaitChk){
		return true;
	} */
	
	//active
	$(".req4105_flowBox.topFlowBox.flowActive").removeClass("flowActive");
	$(thisObj).addClass("flowActive");
	
	var flowId = $(thisObj).attr("flowid");
	var workCd = $(thisObj).attr("workcd");
	var revisionCd = $(thisObj).attr("revisioncd");
	var dplcd = $(thisObj).attr("dplcd");
	
	//작업흐름 Id로 이력 보이기&감추기
	fnSelFlowObjHidden(flowId);
	
	//결재 지정 작업흐름인지 체크
	if(flowInfo.flowSignCd == "01"){
		//선택 작업흐름과 현재 작업흐름이 다를때 결재자 존재하는경우 감추기
		if(flowId != flowInfo.flowId){
			$("#signIdSelBox").hide();
		}else{
			//결재 대기상태가 아닌경우에만 버튼 보이기
			if(!signWaitChk){
				$("#signIdSelBox").show();
			}
		}
	}
	
	//클릭 작업흐름에 작업이 있는 경우 show
	if(workCd == "01"){
		$("#req4105_work").show('fast',function(){
			//선택 작업흐름과 현재 작업흐름이 다른 경우 버튼 감추기
			if(flowId != flowInfo.flowId){
				$(".req4105_work_btn").hide();
			}else{
				//결제 대기인경우 버튼 감추기
				if(signWaitChk){
					$(".req4105_work_btn").hide();
				}
				//담당자 아닌경우 담당 종료만 보이기
				else if(reqUsrId != usrId){
	    			$(".req4105_charger_work").show();
	    		}else{	//담당자인경우 전체
	    			$(".req4105_work_btn").show();	
	    		}
			}
			
			//작업 그리드 세팅
			fnWorkGridSetting();
			
			//작업 갱신
			fnWorkRefresh(flowId);
		});
	}else{
		$("#req4105_work").hide();
	}
	
	//클릭 작업흐름에 리비전이 있는 경우 show
	if(revisionCd == "01"){
		$("#req4105_revision").show('fast',function(){
			//선택 작업흐름과 현재 작업흐름이 다른 경우 버튼 감추기
			if(flowId != flowInfo.flowId){
				$(".req4105_revision_btn").hide();
			}else{
				//결제 대기인경우 버튼 감추기
				if(signWaitChk){
					$(".req4105_revision_btn").hide();
				}else{
		    		$(".req4105_revision_btn").show();	
				}
			}
			
			//리비전 그리드 세팅
			fnRevisionGridSetting();
			
			//리비전 갱신
			fnRevisionRefresh(flowId);
		});
	}else{
		$("#req4105_revision").hide();
	}
	
	
	//클릭 작업흐름에 배포계획이 있는 경우 show
	if(dplcd == "01"){
		$(".req4105_dplBox").hide();
		$(".req4105_dplBox[flowid="+flowId+"]").show('fast');
	}else{
		$(".req4105_dplBox").hide();
	}
}

//작업 그리드 세팅
function fnWorkGridSetting(){
	work_grid = new ax5.ui.grid();
				
	//그리드 프레임 호출
	work_grid.setConfig({
		target: $('[data-ax5grid="work-grid"]'),
		showLineNumber: true,
		sortable:true,
		header: {align:"center"},
		columns: [
			{key: "flowNm", label: "작업흐름", width: 80, align: "center"},
			{key: "workStatusNm", label: "작업상태", width: 80, align: "center"},
			{key: "workChargerNm", label: "담당자", width: 80, align: "center"},
			{key: "workAdmContent", label: "작업 지시내용", width: 370, align: "left"},
			{key: "workAdmStDtm", label: "작업 시작예정일자", width: 140, align: "center"},
			{key: "workAdmEdDtm", label: "작업 종료예정일자", width: 140, align: "center"},
			{key: "workContent", label: "작업 내용", width: 370, align: "left"},
			{key: "workStDtm", label: "작업 시작일자", width: 140, align: "center"},
			{key: "workEdDtm", label: "작업 종료일자", width: 140, align: "center"},
			{key: "regDtm", label: "생성일자", width: 140, align: "center"},
			{key: "regUsrNm", label: "생성자", width: 80, align: "center"},
			{key: "modifyDtm", label: "수정일자", width: 140, align: "center"},
			{key: "modifyUsrNm", label: "수정자", width: 80, align: "center"},
		],
		body: {
			align: "center",
			columnHeight: 30
		}
	});
}

//작업흐름 목록 데이터 받아와서 우측 세팅
function fnRightFlowDivSetData(nowFlowId, flowList){
	//우측 내용 세팅 변수
 	var reqRightData = "";
 	
	//종료 분기 무조건 표시인지 확인
	var flowEndShowChk = false;
	
	//필수인지 체크
	var flowEssentialShowChk = false;
	
	//현재 작업흐름 nextId부터 세팅
	var flowCheck = false; 
	
	//화면에 그린 작업흐름 갯수
	var drawFlowCnt = 0;
	
 	//우측 레이아웃에 작업흐름 목록 세팅
 	if(!gfnIsNull(flowList)){
 		$.each(flowList,function(idx, map){
 			//flow Id 체크
 			if(!flowCheck){
 				if(map.flowId == nowFlowId){
 					flowCheck = true;
 					
 					//작업흐름 정보 담기
 					flowInfo = map;
 				}
 				return true;
 			}else{
 				//화면에 그린 작업흐름 갯수 추가
 				drawFlowCnt++;
 			}
 			
 			
 			//필수인경우 loop stop
 			if(flowEssentialShowChk){
 				//종료분기 존재하는지 체크
				if(flowInfo.flowEndCd == "01"){
					//종료 분기 나타날때까지 loop
					if(!gfnIsNull(map.flowNextId) && map.flowNextId != "null"){
						return true;	
					}
				}else{
	 				return false;
				}
 			}
 			
 			//작업흐름 화살표
 			var flowTopArrowBox = '';
 			
 			//화살표
 			if(drawFlowCnt > 1){
 				flowTopArrowBox = '<div class="req4105_flowTopArrowBox"></div>';
 			}
 			
	 		//각 옵션 체크
			var flowOptionStr = "";
 			//flowNextId 없는경우
			if(gfnIsNull(map.flowNextId) || map.flowNextId == "null"){
				flowOptionStr += "<li class='far fa-stop-circle' title='[종료]최종 완료'></li>&nbsp;";
			} 
			//필수 체크
			if(map.flowEssentialCd == "01"){
				flowOptionStr += '<li class="fa fa-key" title="필수"></li>&nbsp;';
			}
			//결재 체크
			if(map.flowSignCd == "01"){
				flowOptionStr += '<li class="fa fa-file-signature" title="결재"></li>&nbsp;';
			}
			//종료분기 체크
			if(map.flowEndCd == "01"){
				flowOptionStr += '<li class="fa fa-sign-out-alt" title="종료 분기"></li>&nbsp;';
			}
			//작업 체크
			if(map.flowWorkCd == "01"){
				flowOptionStr += '<li class="fa fa-code-branch" title="작업 분기"></li>&nbsp;';
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
				flowOptionStr = '<div class="req4105_flowOptionDiv">'+flowOptionStr+'</div>';
			//}
			
			
	 		reqRightData += flowTopArrowBox
 								+'<div class="req4105_flowFrameBox" id="'+map.flowId+'" nextid="'+map.flowNextId+'" flownm="'+map.flowNm+'">'
 								+'<div class="req4105_flowBox" style="background-color: '+map.flowTitleBgColor+';color: '+map.flowTitleColor+';">'
								+flowOptionStr
								+'	<div class="req4105_flowBox_title">'+map.flowNm+'</div>'
								+'	<div class="req4105_flowBox_contents" style="background-color: '+map.flowContentBgColor+';color: '+map.flowContentColor+';">'
								+'<span>요구사항: '+map.reqTotalCnt+'</span>'
								+'</div>'
								+'</div>'
							+'</div>';
							
			//작업흐름 종료분기 있는경우 종료 무조건 표시 스위치 온
 			if(map.flowEndCd == "01"){
 				flowEndShowChk = true;
 			}
			
			//작업흐름 필수인경우 다음 작업흐름 skip (종료 분기까지 loop)
			if(map.flowEssentialCd == "01"){
 				flowEssentialShowChk = true;
 			}
 		});
	}
 	
 	//우측 내용 채우기
    $("#req4105_reqRightDataList").html(reqRightData);
 	return true;
}

//선택 작업흐름 추가 항목 세팅
function fnFlowOptDivSetData(optList){
	var flowOptData = '';
	
	if(!gfnIsNull(optList)){
		//date 세팅 array
		var optDateDataArr = [];
		
		//공통코드 세팅을 위한 값
		var mstCdStrArr = '';
		var selectObjList = [];
		var arrComboType = [];
		
		//공통 팝업 세팅 값
		var commonPopup_charger = [];	//담당자
		var commonPopup_cls = [];	//분류
		
		//half 갯수 체크
		var halfCnt = 0;
		//hlaf 갯수 및 desc 체크
		var halfDivDesc = false;
		//hlaf 마지막 flowId
		var hlafFlowId = "";
		
		//마지막 작업흐름 Id
		var lastFlowId = null;
		
		var authGrpMissChk = false;
		
		//허용 권한 있는경우 권한으로 검색
		if(!gfnIsNull(authGrpList)){
			var authGrpIds = [];
			$.each(authGrpList,function(idx, map){
				authGrpIds.push(map.authGrpId);
			});
			
			//역할그룹 체크
			if(authGrpIds.indexOf(loginUsrAuthGrpId) == -1){	
				authGrpMissChk = true;
			}
		}
		
		//항목 세팅
		$.each(optList,function(idx, map){
			//열 넓이
			var optionWidthSize = '';
			//전체인경우 title에 clear:both
			var optionTitleClass = '';
			
			//열 넓이에 따른 class 세팅
			if(map.itemRowNum == "01"){ //부분
				optionWidthSize = "req4105_option_half";
				//현재 항목이 textarea이고 이전 항목이 부분인경우 빈항목 추가
				if(map.itemType == "02" && halfCnt%2 == 1){
					flowOptData += '<div class="req4105_option_title" optflowid="'+hlafFlowId+'"></div>'
							+'<div class="req4105_option_half" optflowid="'+hlafFlowId+'"></div>';
					halfCnt++;
				}
				
				halfCnt++;
				hlafFlowId = map.flowId;
			}else if(map.itemRowNum == "02"){ //전체
				//전체 추가 전에 hlaf가 홀수라면 강제 영역 추가
				if(halfCnt%2 == 1){
					//이전 항목이 desc인경우 desc추가
					if(halfDivDesc){
						optionWidthSize += " req4105_desc";
						optionTitleClass += " req4105_desc";
					}
					
					flowOptData += '<div class="req4105_option_title'+optionTitleClass+'" optflowid="'+hlafFlowId+'"></div>'
								+'<div class="req4105_option_half'+optionWidthSize+'" optflowid="'+hlafFlowId+'"></div>';
					
					halfCnt++;
				}
				optionWidthSize = "req4105_option_all";
				optionTitleClass = " req4105_clear";
			}
			
			//데이터 내용
			var flowOptContentData = '';
			
			var itemValue = '';
			var optTarget = "02";
			var optReadOnlyChk = false;
			var optReadOnly = '';
			var optAddClass = '';
			halfDivDesc = false;
			
			//현재 추가 항목 대상이 입력해야하는 항목인지 확인
			if(flowInfo.flowId != map.flowId){
				//수정 불가능한경우 readonly로 막기
				if(map.itemModifyCd == "02"){
					optReadOnlyChk = true;
				}
			}
			
			//결재 대기중인경우 모든 항목에 readonly처리
			if(signWaitChk){
				optReadOnlyChk = true;
			}
			
			//요구사항 담당자가 아닌경우 readonly처리
    		if(reqUsrId != usrId){
    			optReadOnlyChk = true;
    		}
			
			//우측 작업흐름 목록 없는경우 모든 항목에 readonly  처리
			if($(".req4105_flowFrameBox").length == 0){
				optReadOnlyChk = true;
			}
			//허용되지 않은 권한인경우 readonly  처리
			if(authGrpMissChk){
				optReadOnlyChk = true;
			}
			
			//값 null처리
			if(!gfnIsNull(map.itemValue)){
				itemValue = map.itemValue;
			}
			
			//추가 항목 분류에 따른 데이터 내용 처리
			if(map.itemCode == "01"){ //기타
				if(map.itemType == "01"){ //text
					if(optReadOnlyChk){
						optReadOnly = 'readonly="readonly"';
						optAddClass += ' req4105_readonly';
					}else{
						//유효값 설정
						var checkData = {};
						checkData[map.itemId] =  {"type":"length","msg":"추가 항목 "+map.itemNm+"(은)는 "+map.itemLength+"byte까지 입력이 가능합니다.",max:map.itemLength};
						arrChkObj = $.extend(arrChkObj,checkData);
					}
				
					flowOptContentData = '<input type="text" class="req4105_input_text'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" maxlength="'+map.itemLength+'" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" value="'+itemValue+'" '+optReadOnly+'/>';
					
				}else if(map.itemType == "02"){ //textarea
					if(optReadOnlyChk){
						optReadOnly = 'readonly="readonly"';
						optAddClass += ' req4105_readonly';
					}else{
						//유효값 설정
						var checkData = {};
						checkData[map.itemId] =  {"type":"length","msg":"추가 항목 "+map.itemNm+"(은)는 "+map.itemLength+"byte까지 입력이 가능합니다.",max:map.itemLength};
						arrChkObj = $.extend(arrChkObj,checkData);
					}
				
					//<br>, </br>바꾸기
					itemValue = itemValue.replace(/<br>/gi,"\n").replace(/<\/br>/gi,"\n");
					flowOptContentData = '<textarea class="req4105_textarea'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" '+optReadOnly+'>'+itemValue+'</textarea>';
					
					//textarea height높게
					optionWidthSize += " req4105_desc";
					optionTitleClass += " req4105_desc";
					
					halfDivDesc = true;
				}else if(map.itemType == "03"){ //checkbox
					if(optReadOnlyChk){
						optReadOnly = 'disabled="disabled"';
						optAddClass += ' req4105_readonly';
					}
					var optChkVal = ""
					//체크박스 값 체크
					if(map.itemValue == "01"){
						optChkVal = " checked";
					}
					
					flowOptContentData = '<div class="req4105_chk"><input type="checkbox" class="req4105_input_check'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" opttarget="'+optTarget+'"'+optChkVal+' '+optReadOnly+'/><label></label></div>';
				}else if(map.itemType == "04"){ //date
					if(optReadOnlyChk){
						optReadOnly = 'disabled="disabled"';
						optAddClass += ' req4105_readonly';
					}else{
						//date 세팅 배열에 추가
						optDateDataArr.push({id:map.itemId,format:'YYYY-MM-DD',options:{drops:"up"}});
					}
					flowOptContentData = '<input type="text" class="req4105_input_date'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" readonly="readonly" opttarget="'+optTarget+'" value="'+itemValue+'" '+optReadOnly+'/>';
					
					
				}else if(map.itemType == "05"){ //datetime
					if(optReadOnlyChk){
						optReadOnly = 'disabled="disabled"';
						optAddClass += ' req4105_readonly';
					}else{
						//date 세팅 배열에 추가
						optDateDataArr.push({id:map.itemId,format:'YYYY-MM-DD HH:mm',options:{timePicker:true,drops:"up"}});
					}
					flowOptContentData = '<input type="text" class="req4105_input_date'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" readonly="readonly" opttarget="'+optTarget+'" value="'+itemValue+'" '+optReadOnly+'/>';
					
					
				}
			}else if(map.itemCode == "02"){ //공통코드 (selectBox)
				if(optReadOnlyChk){
						optReadOnly = 'disabled="disabled"';
						optAddClass += ' req4105_readonly';
				}
				flowOptContentData = '<select type="text" class="req4105_select'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" opttype="02" cmmcode="'+map.itemCommonCode+'" opttarget="'+optTarget+'" OS="'+itemValue+'" '+optReadOnly+'></select>';
				
				//이미 추가된 공통코드가 있는 경우 '|' 붙임
				if(mstCdStrArr.length > 0){
					mstCdStrArr += "|"
				}
				
				//공통코드 세팅
				mstCdStrArr += map.itemCommonCode;
				selectObjList.push(map.itemId);
				arrComboType.push("OS");
			}else if(map.itemCode == "03"){ //첨부파일
				//첨부파일 height높게
				optionWidthSize += " req4105_file";
				optionTitleClass += " req4105_file";
				
				var fileUploadBtnStr = '';
				var fileUploadWidth = '';
				var fileUploadDesc = '';
				
				//수정이 가능한경우 업로드 버튼 추가
				if(!optReadOnlyChk){
					fileUploadBtnStr = '<div class="req4105_fileBtn" onclick="fnOptFileUpload(this)" id="btn_insert_fileSelect" itemid="'+map.itemId+'" fileid="'+map.itemValue+'">'
										+'<input type="file" style="display: none" id="fileUpload_'+map.itemValue+'" name="fileUpload_'+map.itemValue+'" multiple="multiple" />'
										+'<i class="fa fa-file-upload fa-2x"></i>&nbsp;파일 선택'
									+'</div>';
					//파일 설명 추가
					fileUploadDesc = '<div class="file_dragDrop_info">Drop files here or click to upload.</div>';
				}else{
					//수정 불가능한경우 width100%
					fileUploadWidth = ' widthAll';
					
					//배열에 수정불가 추가
					readonlyFileIdList.push(map.itemValue);
				}
				
				//필수 값인경우 필수체크 배열에 추가
				if(map.itemEssentialCd == "01" && !optReadOnlyChk){
					essentialCdFileIdList.push(map.itemValue);
				}
					
				flowOptContentData = '<div class="uploadOverFlow optFileDiv'+fileUploadWidth+'" id="fileDiv_'+map.itemValue+'" fileid="'+map.itemValue+'" onclick="fnOptFileUpload(this)">'
										+fileUploadDesc+'</div>'
										+fileUploadBtnStr;
				
			}else if(map.itemCode == "04"){ //담당자
				var popupBtnStr = '';
				if(optReadOnlyChk){
					optReadOnly = 'readonly="readonly"';
					optAddClass += ' req4105_readonly';
				}else{
					//담당자 공통팝업 추가
					commonPopup_charger.push(map.itemId);
					popupBtnStr = '<span class="button_normal2 fl req4105_optCharger" id="btn_user_select_'+map.itemId+'"><li class="fa fa-search"></li></span>';
				}
				flowOptContentData = '<input type="text" name="'+map.itemId+'" id="'+map.itemId+'" title="'+map.itemNm+'" opttype="03" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" value="'+itemValue+'" style="display:none;"/>'
									+'<input type="text" class="req4105_input_text req4105_optCharger'+optAddClass+'" title="'+map.itemNm+'" name="'+map.itemId+'Nm" id="'+map.itemId+'Nm" modifyset="02" value="'+$.trim(map.itemValueNm)+'"/>'
									+popupBtnStr;
									
				
			}else if(map.itemCode == "05"){ //분류
				var popupBtnStr = '';
				if(optReadOnlyChk){
					optReadOnly = 'readonly="readonly"';
					optAddClass += ' req4105_readonly';
				}else{
					//분류 공통팝업 추가
					commonPopup_cls.push(map.itemId);
					popupBtnStr = '<span class="button_normal2 fl req4105_cls" id="btn_cls_select_'+map.itemId+'"><li class="fa fa-search"></li></span>';
				}
				flowOptContentData = '<input type="text" name="'+map.itemId+'" id="'+map.itemId+'" title="'+map.itemNm+'" opttype="04" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" value="'+itemValue+'" style="display:none;"/>'
									+'<input type="text" class="req4105_input_text req4105_cls'+optAddClass+'" title="'+map.itemNm+'" name="'+map.itemId+'Nm" id="'+map.itemId+'Nm" modifyset="02" value="'+$.trim(map.itemValueNm)+'" readonly="readonly"/>'
									+popupBtnStr;
									
				
			}
			
			//담당자, 분류 팝업은 Nm이 필수
			if(map.itemEssentialCd == "01" && (map.itemCode == "04" || map.itemCode == "05")){
				strCheckObjArr.push(map.itemId+'Nm');
				sCheckObjNmArr.push(map.itemNm);
			}
			
			//필수 값인경우 필수체크 배열에 추가 - 첨부파일 제외
			if(map.itemCode != "03" && map.itemEssentialCd == "01"){
				strCheckObjArr.push(map.itemId);
				sCheckObjNmArr.push(map.itemNm);
			}
			
			//항목명
			var optNm = map.itemNm;
			
			//필수 값인경우 '*' 추가
			if(map.itemEssentialCd == '01'){
				optNm += '&nbsp;(*)';
			}
			
			flowOptData += '<div class="req4105_option_title'+optionTitleClass+'" optflowid="'+map.flowId+'">'
								+optNm
								+'</div>'
								+'<div class="'+optionWidthSize+'" optflowid="'+map.flowId+'">'
								+flowOptContentData
								+'</div>';
								
			//마지막 작업흐름 Id 세팅
			lastFlowId = map.flowId
		});
	}

	//추가 항목 내용 채우기
	$("#req4105_add_option").html(flowOptData);
	
	//현재 작업흐름과 작업흐름 Id가 다른 항목 가리기
	var flowId = flowInfo.flowId; 
	$("#req4105_add_option .req4105_option_title[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_half[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_all[optflowid!="+flowId+"]").hide();
	
	
	//마지막 항목이 hlaf이고 홀수인경우 항목 채우기_마지막 작업흐름 Id
	if($("#req4105_add_option > div").eq($("#req4105_add_option > div").length-1).hasClass("req4105_option_half")){
		if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").length%2 == 1){
			var descClass = '';
			//desc인경우 class 추가
			if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").hasClass("req4105_desc")){
				descClass = " req4105_desc";
			}
			$("#req4105_add_option").append('<div class="req4105_option_title'+descClass+'" optflowid="'+lastFlowId+'"></div><div class="req4105_option_half'+descClass+'" optflowid="'+lastFlowId+'"></div>');
		}
	}
	
	//date 캘린더 세팅
	if(!gfnIsNull(optDateDataArr)){
		$.each(optDateDataArr,function(idx, map){
			gfnCalendarSet(map.format,[map.id],map.options);
		});
	}
	
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
	*/
	if(!gfnIsNull(selectObjList)){
		var arrObj = [];
		
		//공통코드 object 세팅
		$.each(selectObjList,function(idx, map){
			arrObj.push($("#"+map+""));
		});
		
		
		var strUseYn = 'Y';
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	}
	
	if(!gfnIsNull(arrChkObj) && Object.keys(arrChkObj).length > 0){
		//유효성 체크
		gfnInputValChk(arrChkObj);
	}
	
	//공통 팝업 세팅
	if(!gfnIsNull(commonPopup_charger)){
		//담당자
		$.each(commonPopup_charger,function(idx, map){
			$("#btn_user_select_"+map).click(function() {
				gfnCommonUserPopup( $('#'+map+'Nm').val() ,false,function(objs){
					if(objs.length>0){
						$('#'+map).val(objs[0].usrId);
						$('#'+map+'Nm').val(objs[0].usrNm);
					}
				});
			});
			
			$('#'+map+'Nm').keyup(function(e) {
				if($('#'+map+'Nm').val()==""){
					$('#'+map).val("");
				}
				if(e.keyCode == '13' ){
					$("#btn_user_select_"+map).click();
				}
			});
		});
	}
	if(!gfnIsNull(commonPopup_cls)){
		//분류
		$.each(commonPopup_cls,function(idx, map){
			$("#btn_cls_select_"+map).click(function() {
				gfnCommonClsPopup(function(reqClsId,reqClsNm){
					$('#'+map).val(reqClsId);
					$('#'+map+'Nm').val(reqClsNm);
				});
			});
		});
	}
}

//작업 추가&수정 화면 오픈
function fnWorkPopupOpen(type){
	var workId = "";
	
	//수정인경우 작업 선택 확인
	if(type == "update"){
		
		//항목 선택 확인
		if(Object.keys(work_grid.focusedColumn).length == 0){
			jAlert("수정할 작업을 선택해주세요.","알림");
			return false;
		}
		
		var item = work_grid.list[work_grid.focusedColumn[Object.keys(work_grid.focusedColumn)].doindex];
		//종료처리된 작업은 수정 불가능
		if(item.workStatusCd == "02"){
			jAlert("종료된 작업은 수정이 불가능 합니다.","알림");
			return false;
		}
		else{
			workId = item.workId;
		}
	}
	
	//팝업 화면 오픈
	var data = {"type":type,reqId: reqId,processId: processId, flowId: flowInfo.flowId, workId: workId};
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4107View.do", data, '700', '430','scroll');
}

//작업 제거
function fnWorkDelete(){
	
	if(Object.keys(work_grid.focusedColumn).length == 0){
		jAlert("삭제할 작업을 선택해주세요.","알림");
		return false;
	}
	var item = work_grid.list[work_grid.focusedColumn[Object.keys(work_grid.focusedColumn)].doindex];
	var workId = item.workId;
	
	//종료처리된 작업은 삭제 불가능
	if(item.workStatusCd == "02"){
		jAlert("종료된 작업은 삭제가 불가능합니다.","알림");
		return false;
	}
	
	jConfirm("작업 내용을 삭제 하시겠습니까?", "알림", function( result ) {
		if( result ){
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req4000/req4400/deleteReq4400ReqWorkInfoAjax.do'/>"},
					{reqId: reqId,processId: processId, flowId: flowInfo.flowId, workId: workId});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				
				toast.push(data.message);
				
				//에러 없는경우
				if(data.errorYN != "Y"){
					work_grid.setData(data.workList);
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
}

//작업 그리드 조회
function fnWorkRefresh(flowId){
	//flowId 없는경우 현재 flowId넣기
	if(gfnIsNull(flowId)){
		flowId = flowInfo.flowId;
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4400/selectReq4400ReqWorkListAjax.do'/>","loadingShow":false},
			{reqId: reqId,processId: processId, flowId: flowId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			work_grid.setData(data.workList);
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
//요구사항 임시 저장
function fnReqHalfSave(){
	//담당자 같은지 체크
	if(usrId != reqUsrId){
		jAlert("담당자가 아닌경우 처리가 불가능합니다.","경고");
		return false;
	}
	
	//에러 지우기
	$(".inputError").removeClass("inputError");
	
	//항목 필수 체크
	var chkRtn = gfnRequireCheck("req4105InfoForm", strCheckObjArr, sCheckObjNmArr);
	
	//첨부파일 필수 체크
	var fileRtn = fnFileRequireCheck();
	
	//유효성 오류있는 경우
	if(chkRtn || !fileRtn){
		return false;
	}
	
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus();
		return false;
	}
	var signStr = '';
	//결재 알림 문구 추가
	if(flowInfo.flowSignCd == "01"){
		signStr += "<br>결재는 진행되지 않습니다.";
	}
	
	var fileListStr = fnFileUploadStrData();
	
	//유효성 체크
	if(!gfnSaveInputValChk(arrChkObj)){
		return false;	
	}
	
	jConfirm("임시 저장하시겠습니까?"+signStr+fileListStr, "알림", function( result ) {
		if( result ){
			//실제 파일 FormData에 적재
			fnFileUploadAppendData();
			
			//PK ID 가져오기
			fd.append("reqId",reqId);
			fd.append("processId",processId);
			fd.append("flowId",flowInfo.flowId);
			fd.append("preReqUsrId",reqUsrId);
			
			//추가 항목 가져오기
			gfnFormDataAutoJsonValue("req4105InfoForm",fd);
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req4000/req4100/saveReq4100ReqFlowChgAjax.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false},
					fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				
				
				//에러 없는경우
				if(data.errorYn != "Y"){
					toast.push(data.message);
					
					//레이어 팝업 닫기
					gfnLayerPopupClose();
				}else{
					// 에러일경우 alert 띄우며 확인 시 팝업닫음
					jAlert(data.message, '알림창', function( result ) {
						if( result ){
							gfnLayerPopupClose();
						}
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
	});	
}

//요구사항 다음 작업흐름 선택
function fnReqFlowChgBeforeSucc(){
	//담당자 같은지 체크
	if(usrId != reqUsrId){
		jAlert("담당자가 아닌경우 처리가 불가능합니다.","경고");
		return false;
	}
	
	//최종 변경처리 유효값 변수 저장
	var tmp_strCheckObjArr = strCheckObjArr.slice();
	var tmp_sCheckObjNmArr = sCheckObjNmArr.slice();
	
	//결재창 오픈상태인경우
	if(flowInfo.flowSignCd == "01"){
		//결재 필수체크 걸기
		tmp_strCheckObjArr.push("reqSignNm","reqSignId");
		tmp_sCheckObjNmArr.push("결재자","결재자");
	}
	//항목 필수 체크
	var chkRtn = gfnRequireCheck("req4105InfoForm", tmp_strCheckObjArr, tmp_sCheckObjNmArr);
	
	//첨부파일 필수 체크
	var fileRtn = fnFileRequireCheck();
	
	//유효성 오류있는 경우
	if(chkRtn || !fileRtn){
		return false;
	}
	
	//유효성 체크
	if(!gfnSaveInputValChk(arrChkObj)){
		return false;	
	}
	
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus();
		return false;
	}
	
	//작업체크
	var flowWorkChk = true;
	
	//작업 활성화인지 체크
	if(flowInfo.flowWorkCd == "01"){
		//작업 목록 체크
		flowWorkChk = false;
		
		//작업 목록이 하나도 없는경우 알림만
		if(work_grid.list.length == 0){
			jConfirm("작업 생성이 완료되지 않았습니다.<br>그대로 진행하시겠습니까?", "알림", function( result ) {
				if( result ){
					innerFnNextFlowPopupOpen();
				}
			});	
		}else{
			//모든 작업 목록의 작업상태가 종료 '02'인 경우에만 완료 가능
			var workStatusChk = true;
			$.each(work_grid.list,function(idx, map){
				if(map.workStatusCd == "01"){
					workStatusChk = false;
					jAlert("완료되지 않은 작업이 존재합니다.","경고");
					return false;
				}
			});
			
			if(workStatusChk){
				//skip 진행
				flowWorkChk = true;
			}
		}
	}
	
	//작업 체크 비 정상인경우 중지
	if(flowWorkChk){
		innerFnNextFlowPopupOpen();
	}
	
	
	//다음 작업흐름 선택 창 오픈
	function innerFnNextFlowPopupOpen(){
		//다음 작업흐름이 하나라면 바로 세팅 후 넘기기
		if($("#req4105_reqRightDataList .req4105_flowFrameBox").length == 1){
			selFLowId = $("#req4105_reqRightDataList .req4105_flowFrameBox").attr("id");
			selFlowNextId = $("#req4105_reqRightDataList .req4105_flowFrameBox").attr("nextid");
			
			if(fnEndFlowCheck(selFlowNextId)){
				fnReqFlowChgSucc();
			}
			return false;
		}
		
		//팝업창 mask 처리하고 작업흐름 변경 창 불러오기
		$(".req4105_flowSelect_maskBox").show();
		$(".req4105_flowSelectBox").show();
		$(".req4105_flowSelectBox_top").html($("#req4105_reqRightDataList").html());
		
		$(".req4105_flowSelectBox_top > .req4105_flowFrameBox").click(function(){
			$(".req4105_flowSelectBox_top > .req4105_flowFrameBox.active").removeClass("active");
			$(this).addClass("active");
			
			//작업흐름 Id넣기
			selFLowId = $(".req4105_flowSelectBox_top > .req4105_flowFrameBox.active").attr("id");
			selFlowNextId = $(".req4105_flowSelectBox_top > .req4105_flowFrameBox.active").attr("nextid");
		});
	}
}
//다음 작업흐름이 최종완료인지 체크하고 작업 시작, 종료일자 필수 넣기
function fnEndFlowCheck(selFlowNextId){
	//작업흐름이 필수 일때
	if(gfnIsNull(selFlowNextId) || selFlowNextId == "null"){
		//필수 항목
		var reqStDtmChk = $("#reqStDtm").val();
		var reqEdDtmChk = $("#reqEdDtm").val();
		
		//항목 필수 체크
		if(gfnIsNull(reqStDtmChk) || gfnIsNull(reqEdDtmChk)){
			jAlert("요구사항 최종완료 단계에는<br>작업 시작일자, 작업 종료일자 항목이 필수 입니다.","알림창");
			fnReqFlowChgCancle();
			return false;
		}else{
			return true;
		}
	}
	return true;
}

//다음 작업흐름 선택창 닫기
function fnReqFlowChgCancle(){
	$(".req4105_flowSelectBox_top > .req4105_flowFrameBox").off("click");
	$(".req4105_flowSelect_maskBox").hide();
	$(".req4105_flowSelectBox").hide();
	selFLowId = null;
	selFlowNextId = null;
}

//요구사항 작업흐름 변경 완료
function fnReqFlowChgSucc(){
	//작업흐름 선택 체크
	if(gfnIsNull(selFLowId)){
		jAlert("다음 작업흐름 목록에서<br>작업흐름을 선택해주세요.", "알림");
		return false;
	}
	if(fnEndFlowCheck(selFlowNextId)){
		//실제 저장 처리
		fnReqFlowChgSuccAction();
	}
	
}

//항목 조회 및 실제 저장 처리
function fnReqFlowChgSuccAction(){
	//다음 작업흐름 Id
	var flowNextId = selFLowId;
	
	//다음 작업흐름Id의 다음 작업흐름 Id(최종 종료 분기)
	var flowNextNextId = selFlowNextId;
	
	var signStr = "";
	
	//결재 알림 문구 추가
	if(flowInfo.flowSignCd == "01"){
		signStr += "<br>결재 대기 상태에서는 항목 수정이 불가능합니다.";
	}
	
	//작업흐름 선택 오류
	if(gfnIsNull(flowNextId)){
		jAlert("작업흐름 선택이 완료되지 않았습니다.","경고");
		return false;
	}
	
	var fileListStr = fnFileUploadStrData();
	
	jConfirm("항목 입력을 완료하고 작업흐름을 변경하시겠습니까?"+signStr+fileListStr, "알림", function( result ) {
		if( result ){
			//실제 파일 FormData에 적재
			fnFileUploadAppendData();
			
			//PK ID 가져오기
			fd.append("reqId",reqId);
			fd.append("processId",processId);
			fd.append("flowId",flowInfo.flowId);
			fd.append("flowNextId",flowNextId);
			fd.append("flowNextNextId",flowNextNextId);
			fd.append("flowSignCd",flowInfo.flowSignCd);
			fd.append("preReqUsrId",reqUsrId);
			
			//추가 항목 가져오기
			gfnFormDataAutoJsonValue("req4105InfoForm",fd);
			
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req4000/req4100/saveReq4100ReqFlowChgAjax.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false},
					fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				
				
				//에러 없는경우
				if(data.errorYn != "Y"){
					//해당 프로세스 새로고침
					//fnSubDataLoad("etc","process",processId);
					
					//레이어 팝업 닫기
					gfnLayerPopupClose();
					
					//대시보드 새로고침 함수 존재하는 경우 새로고침
		    		if(typeof fnDashBoardSetting == "function"){
		    			fnDashBoardSetting();
		    		}
				}else{
					// 에러일경우 alert 띄우며 확인 시 팝업닫음
					jAlert(data.message, '알림창', function( result ) {
						if( result ){
							// 팝업 닫기
							gfnLayerPopupClose();
						}
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
	});	
}

//작업 종료 처리 (담당자만 가능)
function fnWorkComplete(){
	//항목 선택 확인
	if(Object.keys(work_grid.focusedColumn).length == 0){
		jAlert("종료할 작업을 선택해주세요.","알림");
		return false;
	}
	
	var item = work_grid.list[work_grid.focusedColumn[Object.keys(work_grid.focusedColumn)].doindex];
	
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
	
	
	//팝업 화면 오픈
	var data = {reqId: reqId,processId: processId, flowId: flowInfo.flowId, workId: item.workId, type:"req4105"};
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4109View.do", data, '600', '430','scroll');
}

//결재 승인&반려
function fnReqSignAction(type){
	//요구사항명
	var reqNm = $("#reqNm").val();
	
	//승인
	if(type == "accept"){
		jConfirm("결재 승인하시겠습니까?", "알림", function( result ) {
			if( result ){
				//다음 작업흐름Id의 다음 작업흐름 Id(최종 종료 분기)
				var flowNextNextId = selFlowNextId;
				
				var rtnData = {reqId: reqId, reqNm: reqNm, processId: processId, signFlowId: lastChkInfo.signFlowId, signUsrId: usrId, signCd: "02", preFlowId: flowInfo.flowId, flowNextNextId: flowNextNextId, signRegUsrId: lastChkInfo.regUsrId};
				fnReqSignComplete(rtnData);
			}
		});
	}
	//반려
	else if(type == "reject"){
		//팝업 화면 오픈
		var data = {"type":"reject"};
		gfnLayerPopupOpen("/req/req4000/req4100/selectReq4108View.do", data, '500', '290','scroll');
	}
}

//결재 승인&반려 데이터 세팅후 Ajax 전송
function fnReqSignComplete(rtnData){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4900/insertReq4900SignActionAjax.do'/>"},
			rtnData);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			jAlert(data.message,"알림");
			//대시보드 새로고침 함수 존재하는 경우 새로고침
    		if(typeof fnDashBoardSetting == "function"){
    			fnDashBoardSetting();
    		}
			
			//레이어 팝업 닫기
			gfnLayerPopupClose();
		}
		else{
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

//결재 이력 세팅
function fnReqChkSetting(reqChkList){
	
	$.each(reqChkList,function(idx, map){
		var signRejectCmnt = map.signRejectCmnt;
		if(!gfnIsNull(signRejectCmnt)){
			signRejectCmnt = signRejectCmnt.replace(/&nbsp;&nbsp;&nbsp;&nbsp;/gi,"</br>");
		}
		
		reqViewStr = "";
		//날짜 계산
		var today = new Date(map.signDtm).format('yyyy-MM-dd HH:mm:ss');
		
		//요구사항명
		var reqNm = $("#reqNm").val();
		
		//결재 요청
		if(map.signCd == '01'){
			//요청자 img
			var regUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.regUsrImg;
			
			//결재자 img
			var signUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.signUsrImg;
			
			reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 요청]</div>' 
						+'<div class="req_box_main regUsrImg">'
							+'<img src="'+regUsrImgSrc+'">'
							+'<div>[요청자]</div>'
							+'<div class="historyUsrDiv" title="'+map.regUsrNm+'" onclick="gfnAlarmOpen(\''+map.regUsrId+'\',\''+map.reqId+'\',\''+reqNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-edit"></i>&nbsp;'+map.regUsrNm+'</div>'
						+'</div>'
						+'<div class="req_box_main arrow" style="height: 10px;"><li class="fa fa-angle-double-down fa-lg"></li></div>'
						+'<div class="req_box_main regUsrImg">'
							+'<img src="'+signUsrImgSrc+'">'
							+'<div>[결재자]</div>'
							+'<div class="historyUsrDiv" title="'+map.signUsrNm+'" onclick="gfnAlarmOpen(\''+map.signUsrId+'\',\''+map.reqId+'\',\''+reqNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-check"></i>&nbsp;'+map.signUsrNm+'</div>'
						+'</div>';
		}
		//결재 승인
		else if(map.signCd == '02'){
			//결재자 img
			var signUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.signUsrImg;
			
			reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 승인]</div>' 
						+'<div class="req_box_main signAcceptDiv">'
							+'<img src="'+signUsrImgSrc+'">'
							+'<div class="historyUsrDiv" title="'+map.regUsrNm+'" onclick="gfnAlarmOpen(\''+map.regUsrId+'\',\''+map.reqId+'\',\''+reqNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-file-signature"></i>&nbsp;'+map.regUsrNm+'</div>'
						+'</div>'
		}
		//결재 반려
		else if(map.signCd == '03'){
			reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 반려]</div>'
						+'<div class="req_box_main bottom signRejectDiv" readonly="readonly" title="'+map.signRejectCmnt+'" onclick="signRejectPopupOpen(this)">사유: '+signRejectCmnt+'</div>'
						+'<div class="req_box_main bottom historyUsrDiv signRejectUsrDiv" onclick="gfnAlarmOpen(\''+map.signUsrId+'\',\''+map.reqId+'\',\''+reqNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-times"></i>'+map.signUsrNm+'</div>';
		}
		
		//하단 요구사항 그리기
		viewStr =	'<div class="req4105_signFrame"><div class="req_main_box req_bottom reqSeq_'+map.signId+'" id="'+map.reqId+'" signflowid="'+map.signFlowId+'" prevflowid="'+map.prevFlowId+'">'
						+'<div class="req_top_box">'
						+today
						+'</div>'
						+'<div class="req_bottom_box">'
						+reqViewStr
						+'</div>'
					+'</div></div>';
					
		//마지막 이력 제외하고 화살표 추가
		if(idx < reqChkList.length-1){
			viewStr += '<div class="req4105_flow_topArrowBox signArrow" signflowid="'+map.signFlowId+'" prevflowid="'+map.prevFlowId+'"></div>';
		}
		$('#req4105_signHistoryDiv').append(viewStr);
	});
	
	//현재 작업흐름과 작업흐름 Id가 다른 결재 이력 감추기
	$(".req_main_box.req_bottom[prevflowid!="+flowInfo.flowId+"]").hide();
	$(".req4105_flow_topArrowBox.signArrow[prevflowid!="+flowInfo.flowId+"]").hide();
	
	//결재 이력이 0개인경우 레이아웃 닫기
	if($(".req_main_box.req_bottom[prevflowid="+flowInfo.flowId+"]").length == 0){
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").hide();
	}else{
		//레이아웃 열기
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").show();
	}
}

//결재 반려 사유 팝업 띄우기
function signRejectPopupOpen(thisObj){
	var data = {"type":"view","comment":$(thisObj).html()};
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4108View.do", data, '500', '290','scroll');
}
 /**********
  *  파일
 ***********/
function isValidFileExt(files){
	for(var idx = 0; idx < files.length; idx++) {
		// 파일 확장자 추출( 소문자 )
		var ext = files[idx].name.split(".").pop().toLowerCase();
		// 화이트 리스트가 아니라면 중지 업로드 중지.
		if(!gfnFileCheck(ext)){
			toast.push("확장자가 [ " +ext + " ] 인 파일은 첨부가 불가능 합니다.");
			return false
		};
	};
	return true;
}
 
//추가 항목 파일 업로드 버튼 클릭
function fnOptFileUpload(thisObj){
	//atchFileId 가져오기
	var atchFileId = $(thisObj).attr("fileid");
	var oslUpload_btn = document.getElementById("fileUpload_"+atchFileId);
	
	//파일 선택 버튼 있는경우(업로드 가능)
	if(!gfnIsNull(oslUpload_btn)){
		oslUpload_btn.value = '';
		oslUpload_btn.click();
		fnOslDocUpload(oslUpload_btn,atchFileId);
	}
}

//파일 업로드 버튼 선택 동작
function fnOslDocUpload(ele,atchFileId){
	ele.onchange = function() {
		var files = ele.files;
		
		//파일크기 총 합
		var sumFileSize = 0;
		
		//파일 목록
		var fileUploadList = "";
		
		//업로드 가능한 파일
		var fileUploadChk = false;
		
		//실제 업로드되는 파일 목록 구하기
		$.each(files,function(idx, map){
			// 파일 확장자 추출( 소문자 )
			var ext = map.name.split(".").pop().toLowerCase();
			var fileName = gfnCutStrLen(map.name,45);
			
			if(map.size > FILE_INFO_MAX_SIZE){
				var fileInfoMaxSizeStr = gfnByteCalculation(FILE_INFO_MAX_SIZE);
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> ('+fileInfoMaxSizeStr+' 용량 초과)</br>';
			}else if(sumFileSize > FILE_SUM_MAX_SIZE){
				var fileSumMaxSizeStr = gfnByteCalculation(FILE_SUM_MAX_SIZE);
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> ('+fileSumMaxSizeStr+' 전체 용량 초과)</br>';
			}else if(!gfnFileCheck(ext)){
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> ([ ' +ext + ' ] 확장자 불가)</br>';
			}else if(!gfnIsNull(fileChk.getObj(map.name+":"+map.size).index) && fileChk.getObj(map.name+":"+map.size).index != idx){
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> (중복 파일)</br>';
			}else if(map.size <= 0){
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> (0 Byte인 파일)</br>';
			}else{
				fileUploadList += '<i class="far fa-file"></i>&nbsp;'+gfnCutStrLen(map.name,90)+"</br>";
				sumFileSize += map.size;
				fileUploadChk = true;
			}
		});
		if(!fileUploadChk){
			jAlert("업로드 할 수 있는 파일이 없습니다.</br></br>[업로드 파일 목록] <div id='popup_fileList'>"+fileUploadList+"</div>","경고");
			return false;
		}else{
			jConfirm("추가 항목의 파일 첨부는 서버에 즉시 업로드됩니다.</br>계속 진행하시겠습니까?</br></br>[업로드 파일 목록] <div id='popup_fileList'>"+fileUploadList+"</div>", "경고", function( result ) {
			if( result ){
				//다중건 즉시 업로드 처리
				fnReq4105FileAjaxUpload(files,atchFileId);
			}
		});
		}
	};
}

//파일 업로드 AJAX 처리
function fnReq4105FileAjaxUpload(files,atchFileId) {
	//에러 제거
	$('#fileDiv_'+atchFileId).removeClass("inputError");
	
	//전송 전 시간 기록 변수
	var beforeSendTime;
	//전송 시간 기록 변수
	var sendTime;
	
	//FormData 세팅
	var optFileFormData = new FormData();
	
	optFileFormData.append('reqId',reqId);
	optFileFormData.append('atchFileId',atchFileId);
	
	//출력 개체
	upFileObj = [];
	
	//파일크기 총 합
	var sumFileSize = 0;
	
	//실제 파일 idx
	var trueIdx = 0;
	//file값 세팅
	$.each(files,function(idx, map){
		// 파일 확장자 추출( 소문자 )
		var ext = files[idx].name.split(".").pop().toLowerCase();
		
		if(map.size > FILE_INFO_MAX_SIZE){
			return true;
		}else if(sumFileSize > FILE_SUM_MAX_SIZE){
			return true;
		}else if(!gfnFileCheck(ext)){
			return true;
		}else if(!gfnIsNull(fileChk.getObj(map.name+":"+map.size).index) && fileChk.getObj(map.name+":"+map.size).index != idx){
			return true;
		}else if(map.size <= 0){
			return true;
		}else{
			sumFileSize += map.size;
		
			optFileFormData.append('file', map);
			//파일 정보 세팅
			var fileVo = {};
			var fileName = map.name;
			var fileExtsn = fileName.substring(fileName.lastIndexOf('.')+1);
		
			fileVo.orignlFileNm = fileName;
			fileVo.fileExtsn = fileExtsn;
			fileVo.fileMg = map.size;
			fileVo["atchFileId"] = atchFileId;
			fileVo['reqId'] = reqId;
			
			//파일 목록 먼저 출력
			upFileObj[trueIdx] = gfnFileListReadDiv(fileVo,'#fileDiv_'+atchFileId,'req');
			//업로드 게이지 바 0부터 시작
			$(upFileObj[trueIdx]).children('.file_progressBar').children('div').css({width:0});
			
			trueIdx++;
		}
	});
	
	//AJAX 설정 
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4100/insertReq4105FileUploadAjax.do'/>"
				,"contentType":false
				,"processData":false
				,"cache":false
				,"async":true
				,loadingShow:true}
			,optFileFormData);
	//AJAX 통신 전 실행 함수
	ajaxObj.setFnbeforeSend(function(){
		beforeSendTime = new Date().getTime();
 	});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		innerFail();
    		return false;
    	} 
    	else{
    		//첫 등록인 경우 Id세팅
    		if(data.firstInsert == 'Y' && gfnIsNull(atchFileId)){
    			atchFileId = data.addFileId;
    		}
    		sendTime = (new Date().getTime())-beforeSendTime;
    		toast.push("업로드 완료 "+(sendTime/1000)+"초");
    		
    		//추가된 파일 fileSn넣기
    		$.each(upFileObj, function(idx, map){
    			$(map).children().children('.file_contents').attr('fileSn',(data.addFileSn+idx));
    		});
    	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push("ERROR STATUS("+status+")<br>"+err);
    	innerFail();
 	});
	
	
	//AJAX 전송 완료후 게이지 바 채움
	ajaxObj.setFnComplete(function(xhr, status, err){
		//추가된 파일 bar지우기
   		$.each(upFileObj, function(idx, map){
   			//$(map).children('.file_progressBar').remove();
   			$(map).children('.file_progressBar').children('div').css('width','100%');
   		});
 	});
	
	//AJAX 전송file_progressBar
	ajaxObj.send();
	
	function innerFail(){
		var pgBarObj = $(upFileObj).children('.file_progressBar');
		pgBarObj.stop().animate({width:'100%'},500,function(){
    		pgBarObj.css('background-color','#FE5454');
    	});
	}
}

//성능 개선활동 콤보박스 변경
function fnPiaCdChg(thisObj){
	//예인경우 투입인력 활성화
	if(thisObj.value == "01"){
		$("#labInp").removeClass("req4105_readonly");
		$("#labInp").removeAttr("readonly");
	}else{
		//비활성화
		$("#labInp").addClass("req4105_readonly");
		$("#labInp").attr("readonly","readonly");
		$("#labInp").val(0);
	}
}

//추가항목 파일첨부 필수 체크
function fnFileRequireCheck(){
	var checkVal = true;
	//필수 첨부파일 존재하는 경우
	if(!gfnIsNull(essentialCdFileIdList)){
		$.each(essentialCdFileIdList,function(idx, map){
			//첨부된 파일이 없는경우
			if(gfnIsNull($(".file_contents[atchid="+map+"]")) || $(".file_contents[atchid="+map+"]").length == 0){
				$(".uploadOverFlow.optFileDiv[fileid="+map+"]").addClass("inputError");
				checkVal = false;
				return false;
			}
		});
	}
	
	if(!checkVal){
		jAlert("필수 항목에 파일이 첨부되지 않았습니다.","알림");
	}
	return checkVal;
}

/**
 * 리비전
 */
//리비전 등록
function fnRevisionInsert(){
	gfnSvnRevisionPopup('${sessionScope.selPrjId}',true,function(data){
		var selRepNumFd = new FormData();
		//기본 정보 대입
		selRepNumFd.append("reqId",reqId);
		selRepNumFd.append("processId",processId);
		selRepNumFd.append("flowId",flowInfo.flowId);
		
		//callback 선택 rep 대입
		for(var i=0; i<data.length; i++){
			selRepNumFd.append("selRepNum",JSON.stringify({svnRepId: data[i].svnRepId, revisionNum: data[i].revision, revisionComment: data[i].comment}));
		}
		
		//선택 rep갯수 넘기기
		selRepNumFd.append("selRepNumCnt",data.length);
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/prj/prj1000/prj1100/insertPrj1100RevisionNumList.do'/>"
					,"contentType":false
					,"processData":false
					,"cache":false},
				selRepNumFd)
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			//에러 없는경우
			if(data.errorYN != "Y"){
				//리비전 정보 갱신
				fnRevisionRefresh();
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
	});
}

//리비전 그리드 세팅
function fnRevisionGridSetting(){
	revision_grid = new ax5.ui.grid();
				
	//그리드 프레임 호출
	revision_grid.setConfig({
		target: $('[data-ax5grid="revision-grid"]'),
		showLineNumber: true,
		showRowSelector: true,
		sortable:true,
		header: {align:"center"},
		columns: [
			/* {key: "svnRepId", label: "Repository Id", width: 120, align: "center"}, */
			{key: "svnRepNm", label: "Repository", width: 120, align: "center"},
			{key: "revisionNum", label: "리비전 번호", width: 110, align: "center"},
			{key: "revisionComment", label: "리비전 내용", width: 420, align: "center"},
			{key: "svnRepUrl", label: "Repository Url", width: 270, align: "center"},
		],
		body: {
			align: "center",
			columnHeight: 30
		}
	});
}

//리비전 정보 조회
function fnRevisionRefresh(flowId){
	//flowId 없는경우 현재 flowId넣기
	if(gfnIsNull(flowId)){
		flowId = flowInfo.flowId;
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100ReqRepRevisionListAjax.do'/>","loadingShow":false},
			{reqId: reqId,processId: processId, flowId: flowId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			revision_grid.setData(data.reqRevisionList);
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

//리비전 삭제
function fnRevisionDelete(){
	var chkList = revision_grid.getList('selected');
		
	if(gfnIsNull(chkList)){
		jAlert("선택한 리비전이 없습니다.","알림창");
		return false;
	}
	
	var selRepNumFd = new FormData();
	//기본 정보 대입
	selRepNumFd.append("reqId",reqId);
	selRepNumFd.append("processId",processId);
	selRepNumFd.append("flowId",flowInfo.flowId);
	
	//callback 선택 rep 대입
	$.each(chkList,function(idx, map){
		selRepNumFd.append("selRepNum",JSON.stringify({svnRepId: map.svnRepId, revisionNum: map.revisionNum}));
	});
	
	//선택 rep갯수 넘기기
	selRepNumFd.append("selRepNumCnt",chkList.length);
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/deletePrj1100RevisionNumList.do'/>"
				,"contentType":false
				,"processData":false
				,"cache":false},
			selRepNumFd)
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//리비전 정보 갱신
			fnRevisionRefresh();
		}
		jAlert(data.message,"알림");
	});
	
	//AJAX 전송
	ajaxObj.send();
}

/**
 * 배포
 */
 //배포 데이터 세팅
function fnReqDplSetting(reqDplList){
	$.each(reqDplList,function(idx, map){
		$('#dplId_'+map.flowId).val(map.dplId);
		$('#dplNm_'+map.flowId).val(map.dplNm);
	});
}

//담당자 이관
function fnReqChargerChg(){
	//허용 권한 있는경우 권한으로 검색
	if(!gfnIsNull(authGrpList)){
		var authGrpIds = [];
		$.each(authGrpList,function(idx, map){
			authGrpIds.push(map.authGrpId);
		});
		
		authData= { "usrNm" :  "" , authGrpIds : authGrpIds };
	}
	gfnCommonUserPopup(authData,false,function(objs){
		if(objs.length>0){
			jConfirm("담당자를 이관하시겠습니까? </br>이관 대상 담당자: "+objs[0].usrNm, "알림", function( result ) {
				if( result ){
					//실제 파일 FormData에 적재
					fnFileUploadAppendData();
					
					//PK ID 가져오기
					fd.append("reqId",reqId);
					fd.append("reqNm",$('#reqNm').val());
					fd.append("reqChargerId",objs[0].usrId);
					
					$('#reqChargerId').val(objs[0].usrId);
					$('#reqChargerNm').val(objs[0].usrNm);
					//AJAX 설정
					var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/req/req4000/req4000/updateReq4105ReqChargerChgAjax.do'/>"
								,"contentType":false
								,"processData":false
								,"cache":false},
							fd);
					//AJAX 전송 성공 함수
					ajaxObj.setFnSuccess(function(data){
						data = JSON.parse(data);
						
						//에러 없는경우
						if(data.errorYn != "Y"){
							toast.push(data.message);
							//대시보드 새로고침 함수 존재하는 경우 새로고침
				    		if(typeof fnDashBoardSetting == "function"){
				    			fnDashBoardSetting();
				    		}
							
							//레이어 팝업 닫기
							gfnLayerPopupClose();
						}else{
							// 에러일경우 alert 띄우며 확인 시 팝업닫음
							jAlert(data.message, '알림창', function( result ) {
								if( result ){
									gfnLayerPopupClose();
								}
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
			});	
		}
	});
}
</script>
</head>
<body>
<div class="popup">
<form id="req4105InfoForm" onsubmit="return false;" style="width:100%;height:100%;">
	<div class="popup_title_box"><span>[${targetprjGrpNm} > ${targetPrjNm}]</span> : <span id="popup_titleReqNm"></span></div>
	<div class="req4105_flowSelect_maskBox"></div>
	<div class="req4105_flowSelectBox">
		<div class="req4105_flowSelectBox_title">다음 작업흐름 선택</div>
		<div class="req4105_flowSelectBox_top"></div>
		<div class="req4105_flowSelectBox_bottom">
			<div class="button_complete req4105_nextFlowBtn" onclick="fnReqFlowChgSucc()"><i class="fa fa-check"></i>&nbsp;완료</div>
			<div class="button_complete req4105_nextFlowBtn" onclick="fnReqFlowChgCancle()"><i class="fa fa-times"></i>&nbsp;취소</div>
		</div>
	</div>
	<div class="req4105_req_leftBox">
		<div class="req4105_req_topBox" id="req4105_reqTopDataList" title="작업흐름 변경이력">

		</div>
		<div class="req4105_req_bottomBox">
			<div class="req4105_reqBottom_topBox" id="req4105_reqOptDataList">
				<div class="req4105_frameTitleDiv">
					기본 항목 정보
					<div class="req4105_titleFoldingBtn"><span class="req4105_titleFoldingContent up" folding="0"></span></div>
				</div>
				<div class="req4105_optionDiv req4105_default_option req4105_foldDiv" folding="0">
					<div class="req4105_default_mask"></div>
					<div class="req4105_option_title req4105_top_line">
						프로세스
					</div>
					<div class="req4105_option_half req4105_top_line">
						<input type="text" class="req4105_input_text req4105_readonly" title="프로세스" id="processNm" name="processNm" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title req4105_top_line">
						
					</div>
					<div class="req4105_option_half req4105_top_line req4105_right_line">
					</div>
					<div class="req4105_option_title">
						현재 작업흐름
					</div>
					<div class="req4105_option_half">
						<input type="text" class="req4105_input_text req4105_readonly" title="현재 작업흐름" id="flowNm" name="flowNm" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						접수 유형
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="text" class="req4105_input_text req4105_readonly" title="접수 유형" id="reqNewTypeNm" name="reqNewTypeNm" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						요구사항 명
					</div>
					<div class="req4105_option_all req4105_right_line">
						<input type="text" class="req4105_input_text req4105_readonly" title="요구사항 명" id="reqNm" name="reqNm" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						요청자
					</div>
					<div class="req4105_option_half">
						<input type="text" class="req4105_input_text req4105_readonly" title="요청자" id="reqUsrNm" name="reqUsrNm" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						연락처
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="text" class="req4105_input_text req4105_readonly" title="연락처" id="reqUsrNum" name="reqUsrNum" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						요청일자
					</div>
					<div class="req4105_option_half">
						<input type="text" class="req4105_input_text req4105_readonly" title="요청일자" id="reqDtm" name="reqDtm" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						이메일
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="text" class="req4105_input_text req4105_readonly" title="이메일" id="reqUsrEmail" name="reqUsrEmail" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						요청자 소속
					</div>
					<div class="req4105_option_all req4105_right_line">
						<input type="text" class="req4105_input_text req4105_readonly" title="요청자 소속" id="reqUsrDeptNm" name="reqUsrDeptNm" readonly="readonly" modifyset="02"/>
					</div>
					<div class="req4105_option_title">
						작업 시작일시<span class="endPrevStr">(*)</span>
					</div>
					<div class="req4105_option_half">
						<input type="text" class="req4105_input_date req4105_readonly" title="시작일자" id="reqStDtm" name="reqStDtm" readonly="readonly" value=""/>
					</div>
					<div class="req4105_option_title">
						작업 시작 예정 일자(*)
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="text" class="req4105_input_date req4105_readonly" title="작업 시작 예정 일자" id="reqStDuDtm" name="reqStDuDtm" readonly="readonly" value=""/>
					</div>
					<div class="req4105_option_title">
						작업 종료일시<span class="endPrevStr">(*)</span>
					</div>
					<div class="req4105_option_half">
						<input type="text" class="req4105_input_date req4105_readonly" title="종료일자" id="reqEdDtm" name="reqEdDtm" readonly="readonly" value=""/>
					</div>
					<div class="req4105_option_title">
						작업 종료 예정 일자(*)
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="text" class="req4105_input_date req4105_readonly" title="작업 종료 예정 일자" id="reqEdDuDtm" name="reqEdDuDtm" readonly="readonly" value=""/>
					</div>
					<div class="req4105_option_title">
						진척률(%)
					</div>
					<div class="req4105_option_half">
						<input type="number" class="req4105_input_text" title="진척률" id="reqCompleteRatio" name="reqCompleteRatio" value="0" min="0" max="100"/>
					</div>
					<div class="req4105_option_title">
						담당자
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="hidden" name="reqChargerId" id="reqChargerId" title="담당자" opttype="03"/>
						<input type="text" title="담당자" class="req4105_input_text req4105_charger" name="reqChargerNm" id="reqChargerNm" modifyset="02"/>
						<span class="button_normal2 fl req4105_charger" id="btn_user_select"><li class="fa fa-search"></li></span>
					</div>
					<div class="req4105_option_title">
						예상 FP
					</div>
					<div class="req4105_option_half">
						<input type="number" class="req4105_input_text" title="예상 FP" id="reqExFp" name="reqExFp" value="0" min="0"/>
					</div>
					<div class="req4105_option_title">
						최종 FP
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="number" class="req4105_input_text" title="최종 FP" id="reqFp" name="reqFp" value="0" min="0"/>
					</div>
					<div class="req4105_option_title">
						요구사항 분류
					</div>
					<div class="req4105_option_half">
						<input type="hidden" name="reqClsId" id="reqClsId"/>
						<input type="text" title="요구사항 분류" class="req4105_input_text req4105_cls" name="reqClsNm" id="reqClsNm" style="width: 190px !important;" readonly="readonly" />
						<span class="button_normal2 fl req4105_cls" id="btn_cls_select"><li class="fa fa-search"></li></span>
					</div>
					<div class="req4105_option_title">
						요구사항 유형
					</div>
					<div class="req4105_option_half req4105_right_line">
						<select type="text" class="req4105_select" title="요구사항 유형" id="reqTypeCd" name="reqTypeCd" opttype="02" cmmcode="REQ00012"></select>
					</div>
					<div class="req4105_option_title">
						성능 개선활동 여부
					</div>
					<div class="req4105_option_half">
						<select type="text" class="req4105_select" title="성능 개선활동 여부" id="piaCd" name="piaCd" OS="02" opttype="02" cmmcode="CMM00001" onchange="fnPiaCdChg(this)"></select>
					</div>
					<div class="req4105_option_title">
						투입인력
					</div>
					<div class="req4105_option_half req4105_right_line">
						<input type="number" class="req4105_input_text req4105_readonly" title="투입인력" id="labInp" name="labInp" value="0" min="0" readonly="readonly"/>
					</div>
					<div class="req4105_option_title">
						시스템 구분
					</div>
					<div class="req4105_option_half">
						<select type="text" class="req4105_select" title="시스템 구분" id="sclCd" name="sclCd" opttype="02" cmmcode="REQ00011"></select>
					</div>
					<div class="req4105_option_title">
						
					</div>
					<div class="req4105_option_half req4105_right_line">
						
					</div>
					<div class="req4105_desc_file">
						<div class="req4105_option_title req4105_desc">
							접수 의견
						</div>
						<div class="req4105_option_all req4105_desc req4105_right_line">
							<textarea class="req4105_textarea req4105_readonly" title="접수 의견" id="reqAcceptTxt" name="reqAcceptTxt" readonly="readonly" modifyset="02"></textarea>
						</div>
						<div class="req4105_option_title req4105_desc">
							설명
						</div>
						<div class="req4105_option_all req4105_desc req4105_right_line">
							<textarea class="req4105_textarea req4105_readonly" title="설명" id="reqDesc" name="reqDesc" readonly="readonly" modifyset="02"></textarea>
						</div>
						<div class="req4105_option_title req4105_file">
							<input type="hidden" id="atchFileId" name="atchFileId"/>
							<input type="hidden" id="fileCnt" name="fileCnt"/>
							<input type="hidden" id="insertFileCnt" name="insertFileCnt"/>
							첨부파일
						</div>
						<div class="req4105_option_all req4105_file req4105_right_line">
							<div class="uploadOverFlow pop_file" id="dragandrophandler">
								<div class="file_dragDrop_info">Drop files here or click to upload.</div>
							</div>
							<div class="req4105_fileBtn" onclick="document.getElementById('egovFileUpload').click();" id="btn_insert_fileSelect">
								<input type="file" style="display: none" id="egovFileUpload" name="uploadFileList" multiple="multiple" />
								<i class="fa fa-file-upload fa-2x"></i>&nbsp;파일 선택
							</div>
						</div>
					</div>
				</div>
				
				<div class="req4105_frameTitleDiv">
					추가 항목 정보
					<div class="req4105_titleFoldingBtn"><span class="req4105_titleFoldingContent up" folding="1"></span></div>
				</div>
				<div class="req4105_addOptionFrame req4105_foldDiv" folding="1">
					<div class="req4105_addOption_TitleBox"></div>
					<div id="req4105_dplDivFrame"></div>
					<div class="req4105_signBox" id="signIdSelBox">
						<div class="req4105_option_title">
							결재자 (*)
						</div>
						<div class="req4105_option_all">
							<input type="hidden" name="reqSignId" id="reqSignId" title="결재자"/>
							<input type="text" title="결재자" class="req4105_input_text req4105_charger" name="reqSignNm" id="reqSignNm" modifyset="02"/>
							<span class="button_normal2 fl req4105_charger" id="btn_select_signUser"><li class="fa fa-search"></li></span>
						</div>
					</div>
					<div class="req4105_sign_TitleBox">
						결재 이력
					</div>
					<div class="req4105_signHistoryDiv" id="req4105_signHistoryDiv">
					</div>
					<div class="req4105_optionDiv" id="req4105_work">
						<div class="req4105_work_btnBox">
							작업 관리
							<div class="req4105_work_btn req4105_charger_work" id="btn_update" onclick="fnWorkComplete();"><li class="fa fa-user-check"></li>&nbsp;작업 종료</div>
							<div class="req4105_work_btn" id="btn_delete" onclick="fnWorkDelete();"><li class="fa fa-times"></li>&nbsp;작업 삭제</div>
							<div class="req4105_work_btn" id="btn_update" onclick="fnWorkPopupOpen('update');"><li class="fa fa-edit"></li>&nbsp;작업 수정</div>
							<div class="req4105_work_btn" id="btn_insert" onclick="fnWorkPopupOpen('insert');"><li class="fa fa-plus"></li>&nbsp;작업 추가</div>
							<div class="req4105_work_btn" id="btn_select" onclick="fnWorkRefresh();"><li class="fa fa-redo"></li>&nbsp;작업 조회</div>
						</div>
						<div class="req4105_work_frame">
							<div data-ax5grid="work-grid" data-ax5grid-config="{}" style="height: 200px;"></div>	
						</div>
					</div>
					<div class="req4105_optionDiv" id="req4105_revision">
						<div class="req4105_revision_btnBox">
							리비전 관리
							<div class="req4105_revision_btn" id="btn_delete" onclick="fnRevisionDelete();"><li class="fa fa-times"></li>&nbsp;리비전 삭제</div>
							<div class="req4105_revision_btn" id="btn_insert" onclick="fnRevisionInsert();"><li class="fa fa-plus"></li>&nbsp;리비전 추가</div>
							<div class="req4105_revision_btn" id="btn_select" onclick="fnRevisionRefresh();"><li class="fa fa-redo"></li>&nbsp;리비전 조회</div>
						</div>
						<div class="req4105_revision_frame">
							<div data-ax5grid="revision-grid" data-ax5grid-config="{}" style="height: 200px;"></div>	
						</div>
					</div>
					<div class="req4105_optionDiv" id="req4105_add_option">
						<!-- 작업흐름 입력해야하는 추가 항목 -->
					</div>
				</div>
			</div>
			<div class="req4105_reqBottom_bottomBox" id="req4105_reqBtnSign">
				<div class="req4105_btnDiv">
					<div class="button_complete req4105_signBtn" onclick="fnReqSignAction('accept')"><i class="fa fa-check-double"></i>&nbsp;결재 승인</div>
					<div class="button_complete req4105_signBtn" onclick="fnReqSignAction('reject')"><i class="fa fa-undo-alt"></i>&nbsp;결재 반려</div>
					<div class="button_complete req4105_chargerChgBtn" onclick="fnReqChargerChg()"><i class="fa fa-user-tag"></i>&nbsp;담당자 이관</div>
					<div class="button_complete req4105_complete" onclick="fnReqHalfSave()"><i class="fa fa-clock"></i>&nbsp;임시 저장</div>
					<div class="button_complete req4105_complete" onclick="fnReqFlowChgBeforeSucc()"><i class="fa fa-check"></i>&nbsp;다음</div>
					<div class="button_complete req4105_close" onclick="gfnLayerPopupClose()"><i class="fa fa-times"></i>&nbsp;닫기</div>
				</div>
			</div>
		</div>
	</div>
	<div class="req4105_req_rightBox">
		<div class="req4105_sign_maskBox"><li class="fa fa-file-signature" title="결재"></li>&nbsp;결재 대기중입니다.<span id="signUsrHtml"></span></div>
		<div class="req4105_end_maskBox"><li class="far fa-stop-circle" title="최종 완료"></li>&nbsp;최종 완료된 요구사항입니다.</div>
		<div class="req4105_nextFlowList" id="req4105_reqRightDataList">
		
		</div>
	</div>
</form>
</div>
</body>
</html>