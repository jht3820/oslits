<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/highlight/styles/ir-black.css'/>">
<script type="text/javascript" src="<c:url value='/js/highlight/highlight.pack.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/diff/diff_match_patch_uncompressed.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/oslops/dpl.css'/>' type='text/css'>
<style type="text/css">
.layer_popup_box .pop_sub {width: 100%;padding: 20px 20px;text-align: center;display: inline-block;position:relative;font-size: 12px;}
	
/*익스플로러 적용 위해 !important 추가*/
/* 팝업에 따라 pop_menu_col1, pop_menu_col2 높이 변경 */
.pop_menu_row .pop_menu_col1 { width: 26% !important; height: 45px !important; padding-left: 6px !important; }
.pop_menu_row .pop_menu_col2 { width: 74% !important; height: 45px !important; }

.layer_popup_box input[readonly]:not(.searchInputTextItem), .layer_popup_box input:read-only:not(.searchInputTextItem){background-color: #fff;}

.pop_dpl_div_sub.divDetail_sub_left{width: 380px;float: left;margin-right: 10px;height: 725px;}
.pop_dpl_div_sub.divDetail_sub_rightTop {width: 750px;float: left;height: 340px;margin-bottom: 10px;}
.pop_dpl_div_sub.divDetail_sub_rightBottom {width: 750px;float: left;height: 375px;}
.pop_dpl_div_sub.divDetail_sub_rightBottomTab {width: 750px;float: left;height: 35px;border: none;box-shadow: none;padding: 0;}
	
/* dpl1003 -배포상세정보*/
.sub_title{height: 35px !important;}
div#dplHistoryDiv {width: 100%;display: inline-block;height: 290px;overflow-y: scroll;overflow-x: hidden;border: 1px solid #ccc;position: relative;padding-top: 20px;float: left;}
.dplHis_centerLine {position: absolute;top: 0;left: 50%;height: 100%;width: 2px;border-left: 1px dashed #4b73eb;} 
.divDetail_tabDiv:last-child {border-right: 1px solid #ccc;}
.divDetail_tabDiv {height: 35px;border: 1px solid #ccc;border-top: none;border-right: none;display: inline-block;float: left;width: 100px;line-height: 35px;font-weight: 600;cursor:pointer;}
.divDetail_tabDiv.tabActive, .divDetail_tabDiv:hover {background-color: #4b73eb;color: #fff;}
.hisDplNm{color:#4b73eb;font-weight:600;}
.dplHisDivFrame {display: inline-block;width: 100%;margin: 10px 0;position: relative;text-align: center;font-size:12px;}
.regDplHisFrame {height: 50px;line-height: 20px;margin-top: 10px;display: inline-block;width: 450px;border: 1px solid #ccc;background-color: #fff;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;padding:5px;border-radius: 2px;}
.dplHisDivFrame.hisContentLeft{text-align:left;}
.dplHisDivFrame.hisContentLeft .dplHisContentsFrame {margin-left: 15px;}
.dplHisDivFrame.hisContentRight{text-align:right;}
.dplHisDivFrame.hisContentRight .dplHisContentsFrame {margin-right: 15px;}
.dplHis_centerIcon {display: inline-block;font-size: 16pt;background-color: #fff;position: absolute;left: 350px;top: 20px;}
.dplHisUsrImgFrame {display: inline-block;padding: 4px;width: 70px;height: 85px;text-align: center;}
.dplHisDivFrame.hisContentLeft .dplHisUsrImgFrame{float: left;}
.dplHisDivFrame.hisContentRight .dplHisUsrImgFrame{float: right;}
.dplHisContentsFrame {display: inline-block;width: 305px;height: 112px;border: 1px solid #ccc;background-color: #fff;text-align: left;position:relative;border-radius: 2px;}
.dplHisDivFrame.hisContentLeft .dplHisContentsFrame:before {content: '';position: absolute;border-top: 30px solid transparent;border-right: 30px solid #fff;border-left: 10px solid transparent;top: 19px;right: -16px;transform: rotate(-45deg);z-index: 1;}
.dplHisDivFrame.hisContentLeft .dplHisContentsFrame:after {content: '';position: absolute;border-top: 30px solid transparent;border-right: 30px solid #ccc;border-left: 10px solid transparent;top: 19px;right: -17px;transform: rotate(-45deg);}
.dplHisDivFrame.hisContentRight .dplHisContentsFrame:before {content: '';position: absolute;border-top: 30px solid transparent;border-right: 10px solid transparent;border-left: 30px solid #fff;top: 19px;left: -16px;transform: rotate(45deg);z-index: 1;}
.dplHisDivFrame.hisContentRight .dplHisContentsFrame:after {content: '';position: absolute;border-top: 30px solid transparent;border-right: 10px solid transparent;border-left: 30px solid #ccc;top: 19px;left: -17px;transform: rotate(45deg);}
.dplHisSubContentsFrame {display: inline-block;float: left;padding: 7px;}
.dplHisResultDiv {display: inline-block;height: 25px;line-height: 25px;width: 50px;text-align: center;color: #fff;border: 1px solid #1f2128;border-bottom: 1px solid #1f2128;background-color: #414352;}
.dplHisResultDiv.resultBg-blue{background-color: #4b73eb;}
.dplHisResultDiv.resultBg-red{background-color: #ff5643;}
.dplHisResultDiv.resultBg-yellow{background-color: #fba450;}
.dplHisDivFrame.hisContentLeft .dplHisResultDiv {float: right;}
.dplHisDivFrame.hisContentRight .dplHisResultDiv {float: left;}
.dplHisNameDiv {display: inline-block;height: 25px;line-height: 25px;width: 169px;padding: 0 10px;border-bottom: 1px solid #1f2128;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
.dplHisDivFrame.hisContentLeft .dplHisNameDiv {float: right;text-align: right;}
.dplHisDivFrame.hisContentRight .dplHisNameDiv {float: left;text-align: left;}
.dplHisDescDiv {display: inline-block;float: left;width: 230px;height: 45px;padding: 0 10px;overflow-y: auto;overflow-x:hidden;text-overflow: ellipsis;}
.dplHisDtmDiv {display: inline-block;float: left;height: 25px;line-height: 25px;width: 273px;text-align: center;border-top: 1px solid #ccc;}
.dplHisDivFrame.hisContentLeft .dplHisDtmDiv {float: right;}
.dplHisConsoleDiv {display: inline-block;height: 25px;line-height: 25px;font-size: 10pt;width: 30px;border-top: 1px solid #ccc;text-align: center;cursor: pointer;}
.dplHisConsoleDiv:hover .fas {color: #fff;}
.dplHisDivFrame.hisContentLeft .dplHisConsoleDiv {float: right;border-right: 1px solid #ccc;}
.dplHisDivFrame.hisContentRight .dplHisConsoleDiv {float: left;border-left: 1px solid #ccc;}
.dplHisConsoleDiv:hover{background-color:#1f2128;color:#fff;}
.dplHisDivFrame.hisContentLeft .fas {color: #4b73eb;}
.dplHisDivFrame.hisContentRight .fas {color: #ff5643;}
.dplHisDivFrame[type=SIG] .dplHisConsoleDiv, .dplHisDivFrame[type=MOD] .dplHisConsoleDiv {display: none;}
.dplHisDivFrame[type=SIG] .dplHisDtmDiv, .dplHisDivFrame[type=MOD] .dplHisDtmDiv {width: 100%;}
.hisDplNm.hisRestore{color: #ff5643}
.pop_dpl_div_sub.full_screen {position: absolute;top: 20px;left: 410px;width: 750px;height: 725px;z-index: 2;background-color: #fff;}
.full_screen div#dplHistoryDiv {height: 640px;}
div.pop_dpl_consoleLogDiv {display:none;position: absolute;top: 20px;left: 410px;width: 750px;height: 725px;z-index: 2;background-color: #fff;border: 1px solid #ccc;padding: 5px;}
div.pop_dpl_modifyHisDiv {display: none;position: absolute;top: 20px;left: 410px;width: 750px;height: 683px;z-index: 2;background-color: #fff;border: 1px solid #ccc;padding: 5px;border-bottom: none;}
div#dplPopContentsFrame{background: #23241f;color: #f8f8f2;height: 675px;display: inline-block;width: 100%;text-align: left;line-height: 20px;position: relative;}
.dplSubButton {display: inline-block;width: 40px;margin: 0 5px;height: 29px;text-align: center;font-size: 14pt;float: right;line-height: 30px;border: 1px solid #ccc;background-color: #fff;cursor: pointer;}
.dplSubButton:hover{background: #e8e8e8;text-decoration: none;color: #6e7a85;}
div#dplPopContentsFrame > pre{width:100%;height:100%}
div#dplPopContentsFrame > pre > code {height: 100%;width: 100%;font-size: 10pt;position: absolute;top: 0;left: 0;padding: 0.5em;}
div#btnPopDpl1003SignAccept, div#btnPopDpl1003SignReject {width: 100px;}
/* diff style */
.dplPopModifyDiffOptInfo {display: inline-block;width: 100%;height: 190px;}
.chgPopDiffDiv {width: 100%;top: 361px;height: 188px;border: 1px solid #ccc;}
.chgPopDiffOptInfo {display: block;height: 30px;line-height: 30px;text-align: center;border-bottom: 1px solid #ccc;font-weight: bold;background: #f9f9f9;border-radius: 6px;}
.chgPopDiffOldDiv, .chgPopDiffNewDiv {width: 50%;float: left;display: inline-block;height: 156px;}
.chgPopDiffTitleDiv {text-align: center;height: 30px;line-height: 30px;border-bottom: 1px solid #ccc;background: #f9f9f9;}
div.chgPopDiffContentDiv {height: 125px;padding: 5px;line-height: 22px;overflow-y: scroll;overflow-x: hidden;}
span.text-add {background-color: #a8d975;}
span.text-del {background-color: #ff9a8f;}
#chgPopDiffOldStr .text-del{text-decoration: line-through;}
.chgPopDiffOldDiv{border-right: 1px solid #ccc;}
div#dplPopModifyHisFrame {display: inline-block;width: 100%;height: 441px;overflow-y: scroll;overflow-x: hidden;border: 1px solid #ccc;margin-bottom: 10px;}
.chgPopMainDiv {width: 709px;min-height: 80px;height: auto;border: 1px solid #ccc;margin: 5px;border-radius: 6px;display: block;float: right;background: #f9f9f9;}
.chgPopLeftDiv {float: left;display: inline-block;width: 100px;text-align: center;padding: 10px 0;}
.chgPopRightDiv {float: left;display: inline-block;width: 568px;min-height: 80px;padding: 5px;}
.chgPopRightTopDiv {width: 100%;height: 25px;line-height: 25px;}
.chgPopRightBottomDiv {display: block;min-height: 50px;width: 100%;padding: 5px 0;line-height: 20px;text-align: left;}
.chgPopRightBottomDiv > span {display: block;font-size: 10pt;border-bottom: 1px solid #f9f9f9;}
.chgPopRightBottomDiv > span > b {font-weight: bold;}
.chgPopRightBottomDiv > span.chgPopDiff:hover {text-decoration-line: underline;cursor: pointer;}
.chgPopRightBottomDiv > span.chgPopDiff:active {color: #bbbdbf;}
.chgPopRightBottomDiv > span.chgPopDiff::before{font-family: "Font Awesome 5 Free";content:"\f044\00a0"}
.chgPopUsrDiv {float: left;width: 50%;display: inline-block;font-weight: bold;text-align: left;}
.chgPopAgoDiv {float: right;width: 50%;text-align: right;display: inline-block;padding-right: 20px;color: #424242;font-size: 8pt;}
.chgPopRightBottomDiv > span {display: block;font-size: 10pt;border-bottom: 1px solid #f9f9f9;}
.chgPopLeftDiv > img {width: 60px;height: 60px;}
.dplModifyReqNm {width: 200px;overflow: hidden;padding: 0 2px;text-overflow: ellipsis;white-space: nowrap; font-weight: bold;display: inline-block;line-height: 1;text-align: center;cursor: pointer;}
.dplModifyReqNm::before{font-family: "Font Awesome 5 Free";content:"\f35d";}
.dplModifyReqNm:hover{text-decoration: underline;}
.dplContentsSimple {display: inline-block;width: 40px;margin: 0 5px;height: 29px;text-align: center;font-size: 14pt;float: right;line-height: 30px;border: 1px solid #ccc;background-color: #fff;cursor: pointer;}
.dplContentsSimple:hover{background: #e8e8e8;text-decoration: none;color: #6e7a85;}

/* 이력 내용 축소 */
.dplHisDivFrame.contentSimple .dplHisContentsFrame{height:66px;}
.dplHisDivFrame.contentSimple .dplHisUsrImgFrame{display:none;}
.dplHisDivFrame.contentSimple .dplHisSubContentsFrame{width:100%;}
.dplHisDivFrame.contentSimple .dplHisNameDiv{width:200px;}
.dplHisDivFrame.contentSimple .dplHisDescDiv{display:none;}
</style>
<script type="text/javascript">
	//배포계획 ID
	var selDplId = "${dpl1000DplInfo.dplId}";
	// 세션에 있는 현재 선택된 프로젝트
	var selPrjId = "${sessionScope.selPrjId}"
	// 배포 상세정보 팝업 호출시 넘어온 프로젝트 ID
	var popupPrjId = "${prjId}";
	// 어디에서 배포 상세정보 팝업을 호출했는지 구분하기 위한 값
	var callView = "${callView}";
	
	//수정이력
	var dplChgDetailList = [];
	
	// 배포계획 상세보기 팝업 가이드 상자
	globals_guideChkFn = fnDpl1003GuideShow;
	
	var popSearch;
	var pop_reqListGrid  = {
			init : function() {
				//그리드 및 검색 상자 호출
				
				fnAxGridSet();  // Grid 초기화  설정\
				
				fnSearchBoxControl(); // Search Grid 초기화 설정
				
			}
			
	}


	$(document).ready(function() {
		pop_reqListGrid.init(); // AXISJ Grid 초기화 실행 부분들
		
		//전체 이력 불러오기
		fnDplHistoryLoad();
		
		/* 취소 */
		$('#btnPopDpl1003Cancle').click(function() {
			gfnLayerPopupClose();
		});
		
		//이력 탭 클릭
		$(".divDetail_tabDiv").click(function(){
			//탭 active
			$(".divDetail_tabDiv.tabActive").removeClass("tabActive");
			$(this).addClass("tabActive");
			
			//탭 타겟
			var tabFnType = $(this).attr("tab");
			
			//전체 이력
			if(tabFnType == "ALL"){
				$(".dplHisDivFrame").show();
				$("#pop_dpl_modifyHisDiv").hide();
			}
			//빌드 이력
			else if(tabFnType == "BLD"){
				$(".dplHisDivFrame[type!=BLD][type!=ALL]").hide();
				$(".dplHisDivFrame[type=BLD]").show();
				$("#pop_dpl_modifyHisDiv").hide();
			}
			//결재 이력
			else if(tabFnType == "SIG"){
				$(".dplHisDivFrame[type!=SIG][type!=ALL]").hide();
				$(".dplHisDivFrame[type=SIG]").show();
				$("#pop_dpl_modifyHisDiv").hide();
			}
			//수정 이력
			else if(tabFnType == "MOD"){
				//별도 수정이력 탭 열기
				$("#pop_dpl_modifyHisDiv").show();
				
			}
			
			//중앙 선 크기 조절
			$(".dplHis_centerLine").height($("#dplHistoryContents").height());
		});
		
		//풀스크린 버튼
		$(".dplFullScreanBtn").click(function(){
			var $thisObj = $(this);
			//풀스크린 대상 번호
			var fullMode = $thisObj.attr("fullmode");
			
			var $targetObj = $("div.pop_dpl_div_sub[fullmode="+fullMode+"]");
			
			//풀스크린인지 체크
			var fullCheck = $targetObj.hasClass("full_screen");
			
			//풀모드
			if(fullCheck){
				$targetObj.removeClass("full_screen");
				$thisObj.children("i").addClass("fa-expand");
				$thisObj.children("i").removeClass("fa-compress");
			}else{
				$targetObj.addClass("full_screen");
				$thisObj.children("i").removeClass("fa-expand");
				$thisObj.children("i").addClass("fa-compress");
			}
		});
		
		//이력 내용 축소 버튼
		$(".dplContentsSimple").click(function(){
			var $thisObj = $(this);
			
			//축소 체크
			var simpleCheck = $thisObj.hasClass("simpleCheck");
			
			//내용 확대 하기
			if(simpleCheck){
				$thisObj.removeClass("simpleCheck");
				$(".dplHisDivFrame").removeClass("contentSimple");
				$thisObj.children("i").addClass("fa-minus-square");
				$thisObj.children("i").removeClass("fa-plus-square");
			}else{
				$thisObj.addClass("simpleCheck");
				$(".dplHisDivFrame").addClass("contentSimple");
				$thisObj.children("i").addClass("fa-plus-square");
				$thisObj.children("i").removeClass("fa-minus-square");
			}
			
		});
		
		//콘솔로그 닫기 버튼
		$("#dplConsoleLogDivCancel").click(function(){
			
			//콘솔로그 창 닫기
			$("#pop_dpl_consoleLogDiv").hide();
			
			//내용 초기화
			$("#dplPopBuildConsoleLog").html('');
		});
		
		//콘솔 내용 복사
		$("#dplConsoleTextCopy").click(function(){
			//복사 후 remove될 때 스크롤 움직이지 않도록 포지션 지정 
		    var $temp = $("<textarea style='position:fixed;right:0;'>");
		    $("body").append($temp);
		
		    $temp.val( $("#dplPopBuildConsoleLog").text()).select();
		    
		    document.execCommand("copy");
		    $temp.remove();
		    toast.push('클립보드에 복사되었습니다.');
		});
		
		//결재 승인
		$("#btnPopDpl1003SignAccept").click(function(){
			fnPopRequestEvent("accept");
		});
		
		//결재 반려
		$("#btnPopDpl1003SignReject").click(function(){
			fnPopRequestEvent("reject");
		});
	});
	
	//요구사항 승인&반려 처리
	function fnPopRequestEvent(type){
		var item = {dplId: "${dpl1000DplInfo.dplId}", signId: "${dpl1000DplInfo.signId}"};
		//결재 승인
		if(type == "accept"){
			var rtnData = {	
							view: 'dpl2100',
							type: 'accept',
							dplId: item.dplId,
							signId: item.signId
						};
			//팝업 화면 오픈
			gfnLayerPopupOpen("/dpl/dpl2000/dpl2100/selectDpl2101View.do", rtnData, '550', '290','scroll');
		}
		//반려
		else if(type == "reject"){
			var rtnData = {	
							view: 'dpl2100',
							type: 'reject',
							dplId: item.dplId,
							signId: item.signId
						};
			//팝업 화면 오픈
			gfnLayerPopupOpen("/dpl/dpl2000/dpl2100/selectDpl2102View.do", rtnData, '500', '290','scroll');
		}
	}
	
	//결재 승인&반려 데이터 세팅후 Ajax 전송
	function fnDplSignComplete(rtnData){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2100/insertDpl2100SignActionAjax.do'/>"},
				rtnData);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			//에러 없는경우
			if(data.errorYn != "Y"){
				jAlert(data.message,"알림");
				try{
					//그리드 새로고침
					fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
				}catch(e){
					//새로고침 함수 없는경우 중지
				}
				//팝업창 닫기
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
	//전체 이력 불러오기
	function fnDplHistoryLoad(){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl1000/dpl1000/selectDpl1000DplHistoryListAjax.do'/>","loadingShow":false}
				,{prjId: "${param.prjId}", dplId: "${param.dplId}"});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			if(data.errorYn == "Y"){
				toast.push(data.message);
			}else{
				//수정이력
				var dplDplHistoryList = data.dplDplHistoryList;
				
				//JOB 목록
				var jobList = data.jobList;
				
				//기본 정보
				var hisStr = '';
				
				//빌드,결재 이력
				if(!gfnIsNull(dplDplHistoryList)){
					//loop
					$.each(dplDplHistoryList,function(idx, map){
						//이력 종류
						var type = map.type;
						
						//결과 값
						var result = map.result;
						
						//정보 변수
						var jenId = map.jenId;
						var jobId = map.jobId;
						var startDtm = map.startDtm;
						var regUsrNm = map.regUsrNm;
						var regUsrImg = map.regUsrImg;
						var typeNm = map.typeNm;
						var bldNum = map.bldNum;
						var bldSeq = map.bldSeq;
						
						//생성자 없는경우 시스템
						if(gfnIsNull(regUsrNm)){
							regUsrNm = "시스템";
						}
						
						//ago Time구하기
						var agoTime = gfnDtmAgoStr(new Date(startDtm).getTime());
							
						var hisContentDis = "hisContentRight";
						var hisCenterIcon = "";
						var hisContentIcon = "";
						var hisResultStr = "";
						var hisResultMsg = map.resultMsg;
						var hisResultcolor = "";
						
						//빌드 이력
						if(type == "BLD"){
							typeNm += "-"+bldNum;
							//빌드 아이콘
							hisContentIcon = "fa-play-circle";
							
							//빌드 중(시작)
							if(result == "PROGRESS"){
								//출력
								hisStr +=
									'<div class="dplHisDivFrame" type="'+type+'">'
									+'	<div class="regDplHisFrame">'
									+agoTime+'</br><span class="hisDplNm">['+typeNm+']'+jobId+'</span> 실행'
									+'	</div>'
									+'</div>';
								return true;
								
							//원복 시작
							}else if(result == "RESTORE"){
								 //출력
								hisStr +=
									'<div class="dplHisDivFrame" type="'+type+'">'
									+'	<div class="regDplHisFrame">'
									+agoTime+'</br><span class="hisDplNm hisRestore">['+typeNm+'] '+jobId+'</span> 빌드 실패, 원복 JOB 실행'
									+'	</div>'
									+'</div>';
								hisResultStr = "실패";
								
								//배경색
								hisResultcolor = "resultBg-red";
							//빌드 성공
							}else if(result == "SUCCESS"){
								hisContentDis = "hisContentLeft";
								hisCenterIcon = "fa-flip-horizontal";
								hisResultStr = "성공";
								
								//배경색
								hisResultcolor = "resultBg-blue";
							//빌드 실패
							}else if(result == "FAILURE"){
								hisResultStr = "실패";
								hisContentIcon = "fa-times-circle";
								
								//배경색
								hisResultcolor = "resultBg-red";
							//빌드 중지
							}else if(result == "ABORTED"){
								hisResultStr = "중단";
								hisContentIcon = "fa-stop-circle";
								
								//배경색
								hisResultcolor = "resultBg-yellow";
							
							//빌드 준비중
							}else if(result == "START"){
								return true;
							//기타
							}else{
								hisResultStr = "기타";
								hisContentIcon = "fa-question-circle";
							}
						}
						//결재 이력
						else if(type == "SIG"){
							hisResultMsg = map.jobId;
							//대기
							if(result == "01"){
								hisContentDis = "hisContentLeft";
								hisCenterIcon = "fa-flip-horizontal";
								hisResultStr = "요청";
								jobId = "결재 요청";
								
								//결재 아이콘
								hisContentIcon = "fa-user-circle";
							}
							//승인
							else if(result == "02"){
								hisContentDis = "hisContentLeft";
								hisCenterIcon = "fa-flip-horizontal";
								hisResultStr = "승인";
								jobId = "결재 승인";
								
								//결재 아이콘
								hisContentIcon = "fa-check-circle";
								
								//배경색
								hisResultcolor = "resultBg-blue";
							}
							//반려
							else if(result == "03"){
								hisResultStr = "반려";
								//결재 아이콘
								hisContentIcon = "fa-exclamation-circle";
								
								//배경색
								hisResultcolor = "resultBg-red";
								hisResultMsg = map.resultMsg;
							}
							//기안
							else if(result == "04"){
								hisContentDis = "hisContentLeft";
								hisResultStr = "기안";
								jobId = "기안서 작성";
								hisResultMsg = "${dpl1000DplInfo.dplNm} 기안서 작성 중";
								//결재 아이콘
								hisContentIcon = "fa-edit";
							}
							//변경
							else if(result == "05"){
								hisContentDis = "hisContentLeft";
								hisResultStr = "변경";
								jobId = "결재자 변경";
								hisResultMsg = "${dpl1000DplInfo.dplNm} 결재자 변경";
								//결재 아이콘
								hisContentIcon = "fa-edit";
							}
						}
						
						hisStr +=
								'<div class="dplHisDivFrame '+hisContentDis+'" type="'+type+'">'
								+'	<div class="dplHis_centerIcon"><i class="fas '+hisContentIcon+' '+hisCenterIcon+'"></i></div>'
								+'	<div class="dplHisContentsFrame">'
								+'		<div class="dplHisUsrImgFrame"><img class="usrImgClass" src="/cmm/fms/getImage.do?fileSn=0&atchFileId='+regUsrImg+'"></br>'+regUsrNm+'</div>'
								+'		<div class="dplHisSubContentsFrame">'
								+'			<div class="dplHisResultDiv '+hisResultcolor+'">'+hisResultStr+'</div>'
								+'			<div class="dplHisNameDiv" title="'+jobId+'">['+typeNm+'] '+jobId+'</div>'
								+'		</div>'
								+'		<div class="dplHisDescDiv">'+hisResultMsg+'</div>'
								+'		<div class="dplHisDtmDiv">'+agoTime+'</div>'
								+'		<div class="dplHisConsoleDiv" onclick="fnPopConsoleLogLoad(this)" jenid="'+jenId+'" jobid="'+jobId+'" bldseq="'+bldSeq+'"><i class="fas fa-desktop"></i></div>'
								+'	</div>'
								+'</div>';
					});
					
					//JOB 등록
					if(!gfnIsNull(jobList)){
						$.each(jobList,function(idx, map){
							hisStr += 
								'<div class="dplHisDivFrame" type="ALL">'
								+'	<div class="regDplHisFrame">'
								+new Date(map.regDtm).format('yyyy-MM-dd HH:mm:ss')+' - JOB <span class="hisDplNm">['+map.jobTypeNm+'] '+map.jobId+'</span> 등록'
								+'	</div>'
								+'</div>'
						});
					}
					
					//기본 정보
					hisStr += 
						'<div class="dplHisDivFrame" type="ALL">'
						+'	<div class="regDplHisFrame">'
						+'		<fmt:formatDate value="${dpl1000DplInfo.regDtm}" pattern="yyyy-MM-dd HH:mm:ss"/> - 배포 계획 <span class="hisDplNm">${dpl1000DplInfo.dplNm}</span> 생성'
						+'	</div>'
						+'</div>'
				}
				$("#dplHistoryContents").html(hisStr);
					
				//중앙 선 크기 조절
				$(".dplHis_centerLine").height($("#dplHistoryContents").height());
				
				//수정이력
				dplChgDetailList = data.dplModifyHistoryList;
				fnDplChgDetailView();
			}
		});
		
		//AJAX 전송
		ajaxObj.send();
	}

	function fnSearchBoxControl(){
		var pageID = "AXSearchPop";
		popSearch = new AXSearch();

		var fnPopObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				popSearch.setConfig({
					targetID:"AXSearchPopTarget",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
														
							{label:"검색", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
								options:[
									{optionValue:"0", 				optionText:"전체 보기",	optionAll:true},
	                                {optionValue:'reqOrd', 			optionText:'요구사항 순번'},
	                                {optionValue:'reqNm', 			optionText:'요구사항 명'},
	                                {optionValue:'reqDesc', 		optionText:'요구사항 설명'},
	                                {optionValue:'reqUsrNm', 		optionText:'요청자'},
	                                {optionValue:'reqChargerNm', 	optionText:'담당자'},
	                                {optionValue:'reqNo', 			optionText:'공문번호'},
	                                {optionValue:'processNm', 		optionText:'프로세스 명'},
	                                {optionValue:'flowNm', 			optionText:'작업흐름 명'},
	                                {optionValue:"reqProType", 		optionText:"처리상태", 	optionCommonCode:"REQ00008"},
	                                {optionValue:"reqNewType", 		optionText:"접수유형", 	optionCommonCode:"REQ00009"},
	                                {optionValue:"reqTypeCd",	 	optionText:"요구사항 유형", 	optionCommonCode:"REQ00012"}         
							    ],onChange: function(selectedObject, value){
							    	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
									if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
										axdom("#" + popSearch.getItemId("searchTxt")).attr("readonly", "readonly");	
										axdom("#" + popSearch.getItemId("searchTxt")).val('');	
									}else{
										axdom("#" + popSearch.getItemId("searchTxt")).removeAttr("readonly");
									}
									
									//공통코드 처리 후 select box 세팅이 필요한 경우 사용
									if(!gfnIsNull(selectedObject.optionCommonCode)){
										gfnCommonSetting(popSearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
									}else{
										//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
										axdom("#" + popSearch.getItemId("searchTxt")).show();
										axdom("#" + popSearch.getItemId("searchCd")).hide();
									}
								},
							
							},
							{label:"", labelWidth:"", type:"inputText", width:"120", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
								onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + popSearch.getItemId("btn_search_request")).click();
									}
								} 
							},
							{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
								options:[]
							},
							
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_request",valueBoxStyle:"padding-left:0px;padding-right:2px;", value:"<i class='fa fa-list' id='btn_search_api' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
					            fnInGridPopListSet(0,popSearch.getParam(),{"clsMode":"clsAdd","dplId": selDplId});
							    
							}}
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			fnPopObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + popSearch.getItemId("searchTxt")).attr("readonly", "readonly");	
			axdom("#" + popSearch.getItemId("searchCd")).hide();
			
			//버튼 권한 확인
			fnBtnAuthCheck(popSearch);
		});
		
	}

	//axisj5 그리드
	function fnAxGridSet(){
				
		pop_reqListGrid = new ax5.ui.grid();
 
        pop_reqListGrid.setConfig({
            target: $('[data-ax5grid="pop-reqGrid"]'),
            sortable:false,
            showRowSelector: false,
            multipleSelect: false ,
            header: {align:"center" },
            //frozenColumnIndex: 2,
            columns: [
               {key: "reqOrd", label: "요구사항 순번", width: '13%', align: "center"} 
                ,{key: "reqNewTypeNm", label: "접수유형", width: '10%', align: "center"}
                ,{key: "reqProTypeNm", label: "처리상태", width: '10%', align: "center"}
				,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left"}
				,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
				,{key: "processNm", label: "프로세스 명", width: '15%', align: "center"}
	            ,{key: "flowNm", label: "작업흐름 명", width: '15%', align: "center"}
				,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
				,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
				,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
				,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"}
				,{key: "reqStDtm", label: "작업 시작 일자", width: '14%', align: "center"}
				,{key: "reqEdDtm", label: "작업 종료 일자", width: '14%', align: "center"}
				,{key: "reqStDuDtm", label: "작업 시작 예정일자", width: '15%', align: "center"}
				,{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: '15%', align: "center"}
				,{key: "reqTypeNm", label: "요구사항 유형", width: '15%', align: "center"}
				,{key: "reqCompleteRatio", label: "진척률", width: '15%', align: "center"}
				,{key: "reqExFp", label: "예상 FP", width: '15%', align: "center"}
				,{key: "reqFp", label: "FP", width: '15%', align: "center"}
				,{key: "sclNm", label: "시스템 구분", width: '15%', align: "center"}
				,{key: "piaNm", label: "성능 개선활동 여부", width: '15%', align: "center"}
				,{key: "labInp", label: "투입인력", width: '15%', align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30
                ,onDBLClick:function(){
                	var data = {"mode": "req", "reqId": this.item.reqId}; 
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
                   	
                 	//배정된 요구사항 목록 갱신
			 		fnInGridPopListSet(this.page.selectPage, popSearch.getParam(), {"clsMode":"clsAdd","dplId":selDplId});
                }
            } 
        });
        
        fnInGridPopListSet(0, "", {"clsMode":"clsAdd","dplId": selDplId});
	}
	
	//그리드 데이터 넣는 함수
	function fnInGridPopListSet(_pageNo,ajaxParam, gridData){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
         	
		if(gfnIsNull(ajaxParam)){		
			ajaxParam = $('form#searchFrm').serialize();
		}
     	
     	//데이터 세팅
     	if(!gfnIsNull(gridData)){
     		ajaxParam += "&"+$.param(gridData);
     	}
    	
     	//페이지 세팅
     	if(!gfnIsNull(_pageNo)){
     		ajaxParam += "&pageNo="+_pageNo;
     	}else if(typeof pop_reqListGrid.page.currentPage != "undefined"){
     		ajaxParam += "&pageNo="+pop_reqListGrid.page.currentPage;
     	}
     	
     	//사용유무 '01'인경우만
     	ajaxParam += '&useCd=01';
     	
     	// 어디서 배포계획 상세보기 팝업을 호출했는지 판단하기 위한 callView 값이 있는지 체크한다.
    	if(!gfnIsNull(callView)){
    		// 통합 대시보드, 또는 쪽지에서 현재프로젝트가 아닌 다른 프로젝트의 요구사항 상세보기 팝업을 열어 배포계획 상세보기 팝업을 열었을 경우
    		if(callView == "dsh3000" || callView == "arm"){
    			// 해당 프로젝트의 ID를 같이 넘겨준다.
    			ajaxParam += "&prjId="+popupPrjId;
    		}
    	}
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl1000/dpl1100/selectDpl1100DplListAjax.do'/>","loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			// 배포 계획 배정/미배정 요구사항 목록 조회실패
			if(data.errorYn == "Y"){
				toast.push(data.message);
			}

		 	// 배포 계획 요구사항 배정/미배정 목록 정보
			var list = data.list;
			// 페이지 정보
			var page = data.page;
					
			// 배포 계획 배정/미배정 요구사항 목록 그리드에 데이터 및 페이지 정보 세팅
			pop_reqListGrid.setData({
		             	list:list,
		             	page: {
		                  currentPage: _pageNo || 0,
		                  pageSize: page.pageSize,
		                  totalElements: page.totalElements,
		                  totalPages: page.totalPages
		              }
		    });
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			//세션이 만료된 경우 로그인 페이지로 이동
           	if(status == "999"){
           		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
        		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
        		return;
           	}
		});
		
		//AJAX 전송
		ajaxObj.send();
	}
	
	
//콘솔 내용 조회
function fnPopConsoleLogLoad(thisObj){
	//창 보이기
	$("#pop_dpl_consoleLogDiv").show();
	
	//로딩 아이콘 삽입
	$("#dplPopBuildConsoleLog").html('<i class="fa fa-spinner fa-spin"></i>');
	
	//타겟
	var $targetObj = $(thisObj);
	
	//정보
	var jenId = $targetObj.attr("jenid");
	var jobId = $targetObj.attr("jobId");
	var bldSeq = $targetObj.attr("bldseq");
		
	var paramData = {dplId: selDplId, jenId: jenId, jobId: jobId, bldSeq: bldSeq};
	
 	// 어디서 배포계획 상세보기 팝업을 호출했는지 판단하기 위한 callView 값이 있는지 체크한다.
	if(!gfnIsNull(callView)){
		// 통합 대시보드, 또는 쪽지에서 현재프로젝트가 아닌 다른 프로젝트의 요구사항 상세보기 팝업을 열어 배포계획 상세보기 팝업을 열었을 경우
		if(callView == "dsh3000" || callView == "arm"){
			// 해당 프로젝트의 ID를 같이 넘겨준다.
			paramData = {prjId : popupPrjId, dplId: selDplId, jenId: jenId, jobId: jobId, bldSeq: bldSeq};
		}
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dpl/dpl1000/dpl1000/selectDpl1400DplSelBuildConsoleLogAjax.do'/>","loadingShow":false}
			,paramData);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		if(data.errorYn == "Y"){
			$("#dplPopBuildConsoleLog").html(data.message);
			
		}else{
			var buildMap = data.dpl1400InfoMap;
			
			//로그 빈값
			if(gfnIsNull(buildMap.bldConsoleLog)){
				$("#dplPopBuildConsoleLog").html("콘솔 로그가 없습니다.");
				return false;
			}
			//콘솔로그 출력
			$("#dplPopBuildConsoleLog").html(buildMap.bldConsoleLog);
			$('#dplPopBuildConsoleLog').each(function(i, block) {hljs.highlightBlock(block);});
						
			//스크롤 최 하단
			$("#dplPopBuildConsoleLog").scrollTop(9999);
		}
		
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		$("#dplPopBuildConsoleLog").html("콘솔 내용 조회 오류");
		//세션이 만료된 경우 로그인 페이지로 이동
       	if(status == "999"){
       		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
    		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
    		return;
       	}
	});
	
	//AJAX 전송
	ajaxObj.send();
	
}

//배포 상세보기 팝업 가이드상자
function fnDpl1003GuideShow(){
	var mainObj = $(".popup");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["dpl1003"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}

/**********
  *  배포계획 수정 이력
 ***********/
function fnDplChgDetailView(){
	//$(".reqHistory .middle_box")
	
	var dplChgDetailStr = "";
	var chgDetailContent = "";
	var prevChgDetailId = "";
	
	//수정 이력 loop
	$.each(dplChgDetailList,function(idx, map){
		//수정이력 내용 구하기
		var chgTypeCd = map.chgTypeCd;
		
		//배포계획 수정
		if(chgTypeCd == "01"){
			chgDetailContent += "<span class='chgPopDiff' num='"+idx+"' onclick='fnDplPopOptDiff(this)'><b>"+map.chgNm+"</b>(이)가 수정되었습니다.</span>";
		}
		//요구사항 배정
		else if(chgTypeCd == "02"){
			chgDetailContent += "<span><i class='fas fa-plus-square'></i>&nbsp;요구사항 [<span class='dplModifyReqNm' onclick='fnReq4104Open(\""+map.preVal+"\")'>"+map.chgNm+"</span>](을)를 배정했습니다.</span>";
		}
		//요구사항 배정 제외
		else if(chgTypeCd == "03"){
			chgDetailContent += "<span><i class='fas fa-minus-square'></i>&nbsp;요구사항 [<span class='dplModifyReqNm' onclick='fnReq4104Open(\""+map.preVal+"\")'>"+map.chgNm+"</span>](을)를 배정 제외 했습니다.</span>";
		}
		else {
			return true;
		}
		
		//CHG_DETAIL_ID값 다른 경우 내용 보여주기
		if((dplChgDetailList.length-1) == idx || map.chgId != dplChgDetailList[idx+1].chgId){
			//ago Time구하기
			var agoTime = gfnDtmAgoStr(new Date(map.chgDtm).getTime());
			
			//사용자 img
			var regUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.regUsrImg;
			
			//문자열 넣기
			dplChgDetailStr += '<div class="chgPopMainDiv">'
									+'<div class="chgPopLeftDiv">'
										+'<img src="'+regUsrImgSrc+'"/>'
									+'</div>'
									+'<div class="chgPopRightDiv">'
										+'<div class="chgPopRightTopDiv">'
											+'<div class="chgPopUsrDiv"><i class="fa fa-user"></i>&nbsp;'+map.regUsrNm+' <small>[ '+map.regUsrEmail+' ]</small></div>'
											+'<div class="chgPopAgoDiv"><li class="far fa-clock"></li>&nbsp;'+agoTime+'</div>'
										+'</div>'
										+'<div class="chgPopRightBottomDiv">'
											+chgDetailContent
										+'</div>'
									+'</div>'
								+'</div>';
			chgDetailContent = "";
		}
		
		prevChgDetailId = map.chgId;
	});

	$("#dplPopModifyHisFrame").html(dplChgDetailStr);
}


//diff - 변경 정보 보여주기
function fnDplPopOptDiff(thisObj){
	var chgIdx = thisObj.getAttribute("num");
	
	var chgInfo = dplChgDetailList[chgIdx];
	
	//수정이력 정보
	var chgNm = chgInfo.chgNm;
	var chgId = chgInfo.chgId;
	var preVal = chgInfo.preVal;
	var chgVal = chgInfo.chgVal;
	
	//빈값인경우 공백 처리
	if(gfnIsNull(preVal)){preVal = "";}
	if(gfnIsNull(chgVal)){chgVal = "";}
	
	//항목 명
	$("div.chgPopDiffOptInfo > span#chgPopDiffOptNm").text(chgNm);
	
	//비교 후 결과 값
	var oldVal = "";
	var newVal = "";
	
	//문자열 비교
	var dmp = new diff_match_patch();
	var diff = dmp.diff_main(preVal, chgVal);
	dmp.diff_cleanupEfficiency(diff);
	
	//문자열 비교 값 loop돌면서 class 입히기
	for (var i = 0, j = diff.length; i < j; i++) {
        var arr = diff[i];
        if (arr[0] == 0) {	//변화 없음
            oldVal += arr[1];
            newVal += arr[1];
        } else if (arr[0] == -1) { //이전 값에서 제거
            oldVal += "<span class='text-del'>" + arr[1] + "</span>";
        } else { //변경 값에서 추가
            newVal += "<span class='text-add'>" + arr[1] + "</span>";
        }
    }
	
	//값 출력
	$("div.chgPopDiffOldDiv > div#chgPopDiffOldStr").html(oldVal);
	$("div.chgPopDiffNewDiv > div#chgPopDiffNewStr").html(newVal);
}

//요구사항 상세화면 열기
function fnReq4104Open(reqId){
	var data = {"mode": "req", "reqId": reqId}; 
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
}

</script>

<div class="popup">
	<form:form commandName="dpl1100VO" id="searchFrm" name="searchFrm" method="post"></form:form>
	<div class="pop_title">[ ${dpl1000DplInfo.dplNm} ] 상세 팝업</div>
	<div class="pop_sub">
		<div class="pop_dpl_div_sub divDetail_sub_left" guide="dpl1003dplInfo">
			<div class="sub_title">배포 정보</div>
			<div class="pop_menu_row pop_menu_oneRow first_menu_row">
				<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplStsCd">배포 상태</label></div>
				<div class="pop_menu_col2 pop_oneRow_col2">
					<span class="search_select">
						<input type="text" title="배포 버전" class="input_txt" name="dplStsNm" id="dplStsNm" value="${dpl1000DplInfo.dplStsNm}" readonly="readonly"/>
					</span>
				</div>
			</div>
			<div class="pop_menu_row pop_menu_oneRow">
				<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplVer">배포 버전</label></div>
				<div class="pop_menu_col2 pop_oneRow_col2">
					<input type="text" title="배포 버전" class="input_txt" name="dplVer" id="dplVer" value="${dpl1000DplInfo.dplVer}"  readonly="readonly"/>
				</div>
			</div>
			<div class="pop_menu_row pop_menu_oneRow">
				<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplNm">배포 명</label></div>
				<div class="pop_menu_col2 pop_oneRow_col2">
					<input type="text" title="배포 명" class="input_txt" name="dplNm" id="dplNm" value="${dpl1000DplInfo.dplNm}"  readonly="readonly"/>
				</div>
			</div>
			<div class="pop_menu_row pop_menu_oneRow">
				<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplDt">배포 일자</label></div>
				<div class="pop_menu_col2 pop_oneRow_col2">
					<input type="text" title="배포 일자" class="input_txt" name="dplDt" id="dplDt" value="<fmt:formatDate value="${dpl1000DplInfo.dplDt}" pattern="yyyy-MM-dd"/>"  readonly="readonly"/>
				</div>
			</div>
			<div class="pop_menu_row pop_menu_oneRow">
				<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplUsrNm">배포자</label></div>
				<div class="pop_menu_col2 pop_oneRow_col2">
					<input type="text" title="배포자" class="input_txt" name="dplUsrNm" id="dplUsrNm" value="${dpl1000DplInfo.dplUsrNm}"  readonly="readonly"/>
				</div>
			</div>
			<div class="pop_menu_row pop_menu_oneRow">
				<div class="pop_menu_col1 pop_oneRow_col1"><label for="signUsrNm">결재자</label></div>
				<div class="pop_menu_col2 pop_oneRow_col2">
					<input type="text" title="결재자" class="input_txt" name="signUsrNm" id="signUsrNm" value="${dpl1000DplInfo.signUsrNm}"  readonly="readonly"/>
				</div>
			</div>
			<div class="pop_menu_row pop_menu_oneRow">
				<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplTypeCd">배포 방법</label></div>
				<div class="pop_menu_col2 pop_oneRow_col2">
					<input type="text" title="배포 방법" class="input_txt" name="dplTypeNm" id="dplTypeNm" value="${dpl1000DplInfo.dplTypeNm}" readonly="readonly"/>
				</div>
			</div>
			<c:if test="${dpl1000DplInfo.dplTypeCd == '01'}">
				<div class="pop_menu_row pop_menu_oneRow">
					<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplAutoAfterCd">실패 후 처리</label></div>
					<div class="pop_menu_col2 pop_oneRow_col2">
						<input type="text" title="실패 후 처리" class="input_txt" name="dplAutoAfterNm" id="dplAutoAfterNm" value="${dpl1000DplInfo.dplAutoAfterNm}" readonly="readonly"/>
					</div>
				</div>
				<div class="pop_menu_row pop_menu_oneRow">
					<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplAutoDtm">자동 실행 일시</label></div>
					<div class="pop_menu_col2 pop_oneRow_col2">
						<input type="text" title="자동 실행 일시" class="input_txt" name="dplAutoDtm" id="dplAutoDtm" value="<fmt:formatDate value="${dpl1000DplInfo.dplAutoDtm}" pattern="yyyy-MM-dd HH:mm:ss"/>"  readonly="readonly"/>
					</div>
				</div>
				<div class="pop_menu_row pop_menu_oneRow">
					<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplRestoreCd">원복 타입</label></div>
					<div class="pop_menu_col2 pop_oneRow_col2">
						<input type="text" title="원복 타입" class="input_txt" name="dplRestoreNm" id="dplRestoreNm" value="${dpl1000DplInfo.dplRestoreNm}" readonly="readonly"/>
					</div>
				</div>
			</c:if>
			<div class="pop_note" style="margin-bottom:0px;">
				<div class="note_title">결재 요청 의견</div>
				<textarea class="input_note dpl_note" title="결재 의견" name="dplSignTxt" id="dplSignTxt" rows="7" value="" readonly="readonly" > ${dpl1000DplInfo.dplSignTxt}</textarea>
			</div>
			<div class="pop_note" style="margin-bottom:0px;">
				<div class="note_title">배포 설명</div>
				<textarea class="input_note dpl_note" title="배포 설명" name="dplDesc" id="dplDesc" rows="7" value="" readonly="readonly" >${dpl1000DplInfo.dplDesc}</textarea>
			</div>
		</div>
		<div class="pop_dpl_div_sub divDetail_sub_rightTop" guide="dpl1003DplReq">
			<div class="sub_title">배정된 요구사항 목록</div>
			<!-- 배정 요구사항 목록 검색상자 -->
			<div class="req_search_area" id="AXSearchPopTarget"></div>
			<!-- 미배정 요구사항 목록 그리드 -->
			<div data-ax5grid="pop-reqGrid" data-ax5grid-config="{}" style="height: 240px;"></div>
		</div>
		<div class="pop_dpl_div_sub divDetail_sub_rightBottom" fullmode="2" guide="dpl1003DplHistory">
			<div class="sub_title">
			배포 이력
				<div class="sub_title_btn right">
					<div class="dplFullScreanBtn" fullmode="2"><i class="fas fa-expand"></i></div>
					<div class="dplContentsSimple"><i class="fas fa-minus-square"></i></div>
				</div>
			</div>
			<div class="dplHistoryDiv" id="dplHistoryDiv">
				<div class="dplHis_centerLine"></div>
				<div id="dplHistoryContents">
				</div>
			</div>
			<div class="pop_dpl_div_sub divDetail_sub_rightBottomTab">
				<div class="divDetail_tabDiv tabActive" tab="ALL">전체 이력</div>
				<div class="divDetail_tabDiv" tab="BLD">빌드 이력</div>
				<div class="divDetail_tabDiv" tab="SIG">결재 이력</div>
				<div class="divDetail_tabDiv" tab="MOD">수정 이력</div>
			</div>
		</div>
		<div class="pop_dpl_consoleLogDiv" id="pop_dpl_consoleLogDiv">
			<div class="sub_title">
				콘솔 로그
				<div class="sub_title_btn right">
					<div class="dplSubButton" id="dplConsoleLogDivCancel"><i class="fas fa-times"></i></div>
					<div class="dplSubButton" id="dplConsoleTextCopy"><i class="fas fa-copy"></i></div>
				</div>
			</div>
			<div id="dplPopContentsFrame">
				<pre>
					<code id="dplPopBuildConsoleLog"></code>
				</pre>
			</div>
		</div>
		<div class="pop_dpl_modifyHisDiv" id="pop_dpl_modifyHisDiv">
			<div class="sub_title">
				수정 이력
			</div>
			<div id="dplPopModifyHisFrame">
			</div>
			<div class="dplPopModifyDiffOptInfo">
				<div class="chgPopDiffDiv">
					<div class="chgPopDiffOptInfo">
						<li class="fa fa-exchange-alt"></li>&nbsp;<span id="chgPopDiffOptNm"></span>
					</div>
					<div class="chgPopDiffOldDiv">
						<div class="chgPopDiffTitleDiv diffAdd">변경 <span class="text-del">전</span> 내용</div>
						<div id="chgPopDiffOldStr" class="chgPopDiffContentDiv"></div>
					</div>
					<div class="chgPopDiffNewDiv">
						<div class="chgPopDiffTitleDiv diffDel">변경 <span class="text-add">후</span> 내용</div>
						<div id="chgPopDiffNewStr" class="chgPopDiffContentDiv"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="btn_div">
			<!-- 결재 요청 상태가 대기인경우 -->
			<c:if test="${dpl1000DplInfo.signStsCd == '01'}">
				<!-- 로그인 사용자가 결재자 -->
				<c:if test="${dpl1000DplInfo.signUsrId == sessionScope.loginVO.usrId}">
					<div class="button_normal save_btn" id="btnPopDpl1003SignAccept" >결재 승인</div>
					<div class="button_normal save_btn" id="btnPopDpl1003SignReject" >결재 반려</div>
				</c:if>
			</c:if>
			<div class="button_normal exit_btn" id="btnPopDpl1003Cancle" >닫기</div>
		</div>
	</div>
	</form>
</div>
</html>