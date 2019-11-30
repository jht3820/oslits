<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style type="text/css">
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 50px; }
.button_normal { width: 39px; height: 22px; line-height: 22px; text-align: center; font-weight: bold; font-size: 1em; border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; margin: 0 auto; border: 1px solid #b8b8b8; cursor: pointer; }
div.pop_sub .pop_left {width:28%;} /* common.css pop_left width값 오버라이딩 */
div.pop_sub .pop_right {width:72%;} /* common.css pop_left width값 오버라이딩 */
.input_txt {padding-left: 5px;}
.ui-datepicker-trigger {margin-left: 2px; margin-top: 2px; width: 32px; height: 32px;}
#dplDt { width: 89%; margin-right: 0px; }
#btn_user_select { height: 100%; padding-top: 2px; min-width: 34px; }
#dplUsrNm { width: 88.8%; margin-right: 5px; }
.dplSts_select { width: 100% !important; height: 100% !important; max-width: 100% !important; padding-left: 2px !important; padding-right: 0px !important; }
/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
</style>
<script type="text/javascript">
	
	// 배포명, 작업 설명 유효성
	var arrChkObj = {"dplNm":{"type":"length","msg":"배포 명은 200byte까지 입력이 가능합니다.",max:200}
					 ,"dplTxt":{"type":"length","msg":"배포 작업 설명은 1000byte까지 입력이 가능합니다.", "max":1000}
					};
	
	$(document).ready(function() {
		
		// 팝업 화면 유형 구분값 - 등록/수정/상세
		var pageType = '${param.popupGb}';
		
		//달력 세팅 (배포일)
		var serverTime=gfnGetServerTime('yyyy-mm-dd');
		
		$("#dplNm").focus();
		
		//유효성 체크		
		gfnInputValChk(arrChkObj);

		//공통코드 세팅
		var mstCdStrArr = 'DPL00001';
		var arrComboType = ["OS"];
		var arrObj = [$("#dplStsCd")];
	
   		//기본항목 SELECT 우선 생성
		var strUseYn = 'Y';
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
		
		// 팝업 화면 타이틀
		var popTitle = "배포 계획 추가"
		
		var dataOptions = {};
		
		if( pageType == 'update' ){
			popTitle = "배포 계획 수정";
			gfnCalendarSet('YYYY-MM-DD',['dplDt'],dataOptions);
		}else if( pageType == 'select' ){
			popTitle = "배포 계획 상세보기";
		}else if( pageType == 'insert' ){
			gfnCalendarSet('YYYY-MM-DD',['dplDt'],dataOptions);
		}
		
		
		
		// 팝업유형에 따른 화면 항목 변경
		fnPopupElementChange(pageType);
		
		// 타이틀명 변경
		$(".pop_title").text(popTitle);

		/* 배포 버전 텍스트 숫자만 입력가능 */
		
		$('#btn_insert_dplDetail').click(function() {
			var regExp = /^[0-9]([.]?[0-9])*?$/i;
			
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "dplInsertForm";
			var strCheckObjArr = [ "dplNm", "dplDt", "dplUsrId"];
			var sCheckObjNmArr = [ "배포 명", "배포 날짜", "배포자"];
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;	
			}
			
			// 저장 전 유효성 검사
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}
			
			// 저장
			sendToServer();

		});
		
		/* 취소 */
		$('#btn_cancle').click(function() {
			gfnLayerPopupClose();
		});
				
		$("#btn_user_select").click(function() {
			var data  = {"usrNm" : $('#dplUsrNm').val() ,"authGrpIds" : [ ] };	//'${sessionScope.selAuthGrpId }'		
			gfnCommonUserPopup( data ,false,function(objs){
				if(objs.length>0){
					$('#dplUsrId').val(objs[0].usrId);
					$('#dplUsrNm').val(objs[0].usrNm);
				}
			});
		});
		
		$('#dplUsrNm').keyup(function(e) {
			if($('#dplUsrNm').val()==""){
				$('#dplUsrId').val("");
			}
			if(e.keyCode == '13' ){
				$('#btn_user_select').click();
			}
		});
		
		
		var fd = new FormData();
		function sendToServer(){
				//FormData에 input값 넣기
				gfnFormDataAutoValue('dplInsertForm',fd);
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/dpl/dpl1000/dpl1000/insertDpl1000DeployVerInfoAjax.do'/>"
								,"contentType":false
								,"processData":false
								,"cache":false}
							,fd);
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);
			    //등록 실패의 경우 리턴
			    if(data.saveYN == 'N'){
			       	toast.push(data.message);
			       	return;
			    }
		        //그리드 새로고침
		        if( pageType == 'update' ){
		           	fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
		        }else{
		        	fnAxGrid5View();
		        }
		        		
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
					 	gfnLayerPopupClose();
					}
				});
			});
					
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				toast.push("ERROR STATUS("+data.status+")<br>"+data.statusText);
			    gfnLayerPopupClose();
			});
			//AJAX 전송
			ajaxObj.send();	
		}
	});
	
	/*
	var conditions = { 'LIC_GRP_ID' :'${sessionScope.loginVO.licGrpId}' };
	gfnInitDynamicComboBox("jenId", "JEN1000", " JEN_ID","JEN_NM",conditions);
	
	$("#jenId").val("${dpl1000DplInfo.jenId}");
	*/
	fnGetExistBuildInfo();
	
	function fnGetExistBuildInfo(){
		var checkBuild;
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl1000/dpl1100/selectDpl1100ExistBuildInfoAjax.do'/>"}
				,{ "dplId" : "${param.dplId}" });
		//AJAX 전송 성공 함수
		ajaxObj.async = false;

		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			checkBuild=data.buildInfo;
			if(checkBuild=="1"){
				$("#jenId").attr("disabled",true);
			}
					
		});
		
		//AJAX 전송 오류 함수 서버시간 조회 실패시 클라이언트 시간 조회
		ajaxObj.setFnError(function(xhr, status, err){
					
	 	});
		//AJAX 전송
		ajaxObj.send();

	}
	
	// 등록,수정,상세보기에 따른 팝업 항목 변경
	function fnPopupElementChange(pageType){
		
		$("#btn_insert_dplDetail").show();
		
		if( pageType == "select"){
			
			// input 모두 readonly 처리
			$(".input_txt").attr("readonly", true);
			$("#dplStsCd").attr("disabled", true);
			
			// 배포날짜, 배포자 input width 변경
			$("#dplDt").css("width", "100%"); 
			$("#dplUsrNm").css("width", "100%"); 
			
			// 달력 hide 
			$(".ui-datepicker-trigger").hide();
			
			// 배포자 검색버튼 hide
			$("#btn_user_select").hide();
			
			// 배포작업 설명 disabled
			$("#dplTxt").attr("disabled", true);
			
			// 저장버튼 hide
			$("#btn_insert_dplDetail").hide();
			$("#btn_cancle").text("닫기");
			
			
		}

	}
	
</script>

<div class="popup">
	<form id="dplInsertForm"  onsubmit="return false;">
		<input type="hidden" name="popupGb" id="popupGb" value="${param.popupGb}"/>
		<c:if test="${param.popupGb eq 'update'}">
			<input type="hidden" name="dplId" id="dplId" value="${param.dplId}"/>
		</c:if>
	<!-- <div class="pop_title">배포 계획 ${empty dpl1000DplInfo.dplId ? '추가':'수정'}</div> -->
	<div class="pop_title">배포 계획 추가</div>
	<div class="pop_sub">
		<div class="pop_left">배포 상태</div>
		<div class="pop_right">
			<span class="pop_select_box">
				<select type="text" class="dplSts_select" title="배포 상태" id="dplStsCd" name="dplStsCd"  OS="${dpl1000DplInfo.dplStsCd}"></select>
			</span>
		</div>
		<div class="pop_left">배포 명<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<input type="text" id="dplNm" name="dplNm" title="배포 명" class="input_txt" value="${dpl1000DplInfo.dplNm}" maxlength="200" />
		</div>
		<div class="pop_left">배포 날짜<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right">
			<fmt:parseDate value='${dpl1000DplInfo.dplDt}' pattern='yyyyMMdd' var='parseDt'/>
			<fmt:formatDate value='${parseDt}' pattern='yyyy-MM-dd' var='dplDt'/>
			<input type="text" id="dplDt" name="dplDt" title="배포 날짜" class="input_txt" value="${dplDt}" readonly="readonly" />
		</div>
		<!-- 
		<div class="pop_left">배포 JOB 명</div>
		<div class="pop_right">
			<span class="pop_select_box">
				<select class="w200" id="jenId" name="jenId">
				</select>
			</span>
		</div>
		 -->
		<div class="pop_left bottom_line">배포자<span class="required_info">&nbsp;*</span></div>
		<div class="pop_right bottom_line">
			<input type="hidden" name="dplUsrId" id="dplUsrId" value="${dpl1000DplInfo.dplUsrId}"/>
			<input type="text" title="배포자" class="input_txt lp10" name="dplUsrNm" id="dplUsrNm" value="${dpl1000DplInfo.dplUsrNm}" maxlength="20"/>
			<span class="button_normal2 fl" id="btn_user_select"><i class="fa fa-search"></i></span>	
				
		</div>
		
		<div class="pop_note" style="margin-bottom:0px;">
			<div class="note_title">배포 작업 설명</div>
			<textarea class="input_note" title="배포 작업 설명" name="dplTxt" id="dplTxt" rows="7" value="${dpl1000DplInfo.dplTxt}" maxlength="1000"  >${dpl1000DplInfo.dplTxt}</textarea>
		</div>
		
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_insert_dplDetail" >${empty dpl1000DplInfo.dplId ? '저장':'수정'}</div>
			<div class="button_normal exit_btn" id="btn_cancle">취소</div>
		</div>
	</div>
	</form>
</div>
</html>