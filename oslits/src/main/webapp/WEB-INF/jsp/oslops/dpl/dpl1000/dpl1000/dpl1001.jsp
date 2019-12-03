<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style type="text/css">
	.layer_popup_box .pop_sub {
	    width: 100%;
	    padding: 20px 20px;
	    text-align: center;
	    display: inline-block;
	}
	
	/*익스플로러 적용 위해 !important 추가*/
	/* 팝업에 따라 pop_menu_col1, pop_menu_col2 높이 변경 */
	.pop_menu_row .pop_menu_col1 { width: 26% !important; height: 45px !important; padding-left: 6px !important; }
	.pop_menu_row .pop_menu_col2 { width: 74% !important; height: 45px !important; }
</style>
<script type="text/javascript">

	// 배포계획 등록/수정 팝업 가이드 상자
	globals_guideChkFn = fnDpl1001GuideShow;

	//결재 상태
	var signStsCd;
	
	//저장 FormData
	var fd = new FormData();
	//추가된 job list
	var ADD_JOB_LIST = [];
	// 배포계획 유효성
	var arrChkObj = {	
				        "dplNm":{"type":"length","msg":"배포 명은 200byte까지 입력이 가능합니다.","max":200}, 
				        "dplVer":{"type":"length","msg":"배포 버전은 100byte까지 입력이 가능합니다.","max":100}, 
				        "dplDesc":{"type":"length","msg":"배포 설명은 1000byte까지 입력이 가능합니다.","max":1000}, 
				};
	/* 필수입력값 체크 공통 호출 */
	var strFormId = "dpl1001PopupFrm";
	
	$(document).ready(function() {
		
		/* 	
		*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
		* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
		*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
		*	3. 공통코드 적용할 select 객체 직접 배열로 저장
		* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
		*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
		*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
		*/
 		var mstCdStrArr = "DPL00001|DPL00003|DPL00004|DPL00005";
		var strUseYn = 'Y';
		var arrObj = [ $("#dplStsCd"), $("#dplTypeCd"), $("#dplAutoAfterCd"), $("#dplRestoreCd")];
		var arrComboType = ["OS","OS","OS","OS"];
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
		
		//배포 상태 변경 불가능
		$("#dplStsCd").attr("disabled","disabled");
		/* 타이틀 변경 및 버튼명 변경, 수정일경우 값 세팅 */
		if('${param.popupGb}' == 'insert'){
			$(".pop_title").text("배포 계획 생성");
			$("#btn_update_popup").text('등록');
			
			//배포 상태 대기 고정
			$("#dplStsCd").val("01");
			
		}
		else if('${param.popupGb}' == 'update'){
			
			$(".pop_title").text("배포 계획 수정");
			ADD_JOB_LIST = ${dpl1000DplJobListJson};
			
			//결재 상태
			signStsCd = "${dpl1000DplInfo.signStsCd}";
			
			//결재 상태가 대기인경우 mask씌우기
			if(signStsCd == "01"){
				$("#pop_dpl_mask").show();
				
				//ago Time구하기
				var agoTime = gfnDtmAgoStr(new Date("${dpl1000DplInfo.signDtm}").getTime());
				
				$("#signDtmAgo").html("[요청 일시] "+agoTime);
			}
			//결재 승인인경우 수정 불가능
			else if(signStsCd == "02"){
				jAlert("성공된 배포 계획은 수정이 불가능합니다.");
				return false;
			}
			else{
				$("#btn_update_popup").text('수정');
			}
			
			//배포 상태 승인인경우 수정 불가능
			if("${dpl1000DplInfo.dplStsCd}" == "02"){
				jAlert("성공된 배포 계획은 수정이 불가능합니다.");
				return false;
			}
			
			//SELECT
			$("#dplTypeCd").val("${dpl1000DplInfo.dplTypeCd}");
			fnDplTypeCdChg($("#dplTypeCd")[0]);
		}
		
		// 유효성 체크
		gfnInputValChk(arrChkObj);
		
		//일자 최소값
		var minMaxYear = new Date().getFullYear();
		var minDate = new Date(new Date().setDate(new Date().getDate()+ 1));
		var maxDate =  new Date(minMaxYear, 12, 0);
		
		//배포 일자, 자동 실행 일시 캘린더
		gfnCalendarSet('YYYY-MM-DD',['dplDt'],{minYear: minMaxYear, maxYear: minMaxYear, minDate: minDate, maxDate:maxDate});
		
		$('#dplAutoTm').daterangepicker({
            timePicker: true,
            timePicker24Hour: true,
            timePickerIncrement: 1,
            timePickerSeconds: true,
            singleDatePicker:true,
            minYear: minMaxYear,
			locale: {
				format: 'HH:mm:ss',
					"applyLabel": "적용",
					"cancelLabel": "취소"
			}
        }).on('show.daterangepicker', function (ev, picker) {
            picker.container.find(".calendar-table").hide();
        });
		
		//배포자 검색 걸기
		$("#btn_dplUser_select").click(function() {
			var authData = $('#dplUsrNm').val();

			//사용자 목록 팝업
			gfnCommonUserPopup(authData,false,function(objs){
				if(objs.length>0){
					$('#dplUsrId').val(objs[0].usrId);
					$('#dplUsrNm').val(objs[0].usrNm);
				}
			});
		});
		
		//엔터키로 팝업 호출
		$('#dplUsrNm').keyup(function(e) {
			if($('#dplUsrNm').val()==""){
				$('#dplUsrId').val("");
			}
			if(e.keyCode == '13' ){
				$('#btn_dplUser_select').click();
			}
		});
		
		//결재자 검색 걸기
		$("#btn_signUser_select").click(function() {
			var authData = $('#signUsrNm').val();

			//사용자 목록 팝업
			gfnCommonUserPopup(authData,false,function(objs){
				if(objs.length>0){
					$('#signUsrId').val(objs[0].usrId);
					$('#signUsrNm').val(objs[0].usrNm);
				}
			});
		});
		
		//엔터키로 팝업 호출
		$('#signUsrNm').keyup(function(e) {
			if($('#signUsrNm').val()==""){
				$('#signUsrId').val("");
			}
			if(e.keyCode == '13' ){
				$('#btn_signUser_select').click();
			}
		});
		
		//JOB 등록 버튼 클릭
		$("#btn_insert_job").click(function(){
			var data = {};
			gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1002View.do',data,"640","583",'scroll');
		});
		
		//선택 JOB 삭제 버튼 클릭
		$("#btn_delete_job").click(function(){
			//선택 JOB
			var $selJobChk = $("input[name=addJobDelChk]:checked");

			//선택 JOB이 존재하는 경우
			if($selJobChk.length > 0){
				//선택 JOB Loop
				$.each($selJobChk, function(idx, map){
					//선택 JOB 삭제
					$(map).parent(".dpl_chk").parent(".dpl_middle_cell").parent(".dpl_job_row").remove();
					
					//선택 JOB(jenId, jobId)
					var jenId = $(map).attr("jenid");
					var jobId = $(map).attr("jobid");
					
					//선택 JOB index
					//var findItem = ADD_JOB_LIST.findIndex(function(item, idx){return (item.jenId == jenId && item.jobId == jobId)});
					
					var findItem = -1;
					
					$.each(ADD_JOB_LIST, function(idx2, map2){
						if(map.jenId == map2.jenId && map.jobId == map2.jobId){
							findItem = idx2;
							return false;
						}
					});
					
					if(findItem != -1){
						//ADD_JOB_LIST 목록에서 제거
						ADD_JOB_LIST.splice(findItem,1);
					}
				});
				
				//JOB 전체 ORD 다시 세팅 (0부터)
				fnJobListAllOrdSet();
			}else{ //JOB이 없는 경우
				jAlert("삭제하려는 JOB을 선택해주세요.");
			}
			
		});
		
		//JOB 모두 선택 & 해제
		$("#addJobDelChk_all").click(function(){
			//전체 체크 상태
			var allChkSts = $("#addJobDelChk_all")[0].checked;
			
			//전체 선택
			if(allChkSts){
				$("input[name=addJobDelChk]").prop("checked", true);
			}
			//전체 선택 해제
			else{
				$("input[name=addJobDelChk]").prop("checked", false);
			}
			
		});
		
		//job ord up
		$("#btn_update_job_up").click(function(){
			//실행순서 변경
			fnJobInfoOrdMove("up");
		});
		
		//job ord down
		$("#btn_update_job_down").click(function(){
			//실행순서 변경
			fnJobInfoOrdMove("down");
		});
		
		/* 저장버튼 클릭 시 */
		$('#btn_update_popup').click(function() {
			var strCheckObjArr = ["dplNm","dplDt","dplUsrId","dplUsrNm","signUsrId","signUsrNm"];
			var sCheckObjNmArr = ["배포명","배포 일자","배포자","배포자","결재자","결재자"];
			
			//배포 방법이 자동인경우 추가 항목 필수
			if($("#dplTypeCd").val() == "01"){
				strCheckObjArr.push("dplAutoAfterCd","dplAutoTm","dplRestoreCd");
				sCheckObjNmArr.push("실패 후 처리","자동 실행 일시","원복 타입");
			}
			
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;	
			}
			
			// 등록/수정 전 유효성 체크
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}	
	
			//저장
			fnSaveReqInfoAjax("dpl1001PopupFrm");
	
		});
			
		//Job List - Sortable걸기
		Sortable.create(dpl_bottom_job_content,{
			"direction": "vertical",
			"ghostClass" : "dpl_job_row_ghost",
			"chosenClass" : "dpl_job_row_chosen",
			onEnd: function (evt) {
				//변경된 index
				var newIndex = evt.newIndex;
				var oldIndex = evt.oldIndex;
				
				//변경된 index가 제자리인경우 중지
				
				if(newIndex == oldIndex){
					return false;
				}
				
				//실행 순서 변경 적용
				fnJobDivOrdModify(evt.item, newIndex, oldIndex);
			}
		});
	});
	
	//실행 순서 변경 적용
	function fnJobDivOrdModify(item, newIndex, oldIndex){
			//ord 변경하기
			$.each($(".dplStartOrdCell"),function(idx, map){
				//현재 ord가져오기
				var targetOrd = parseInt($(map).attr("ord"));
				var newOrd = targetOrd; 
				
				//아래에서 위
				if(oldIndex > newIndex){
					//new ~ old
					if(idx < newIndex || idx > oldIndex){
						return true;
					}else{
						newOrd = newOrd+1;
					}
				}
				//위에서 아래
				else if(oldIndex < newIndex){
					//old ~ new
					if(idx > newIndex || idx < oldIndex){
						return true;
					}else{
						newOrd = newOrd-1;
					}
				}
				
				//ord 적용
				$(map).attr("ord",newOrd);
				$(map).text(newOrd);
				$(map).parent(".dpl_middle_row.dpl_job_row").attr("ord",newOrd);
			});
			
			//변경된 object ord변경
			var $chgObj = $(item).children(".dplStartOrdCell");
			$chgObj.attr("ord",newIndex+1);
			$chgObj.parent(".dpl_middle_row.dpl_job_row").attr("ord",newIndex+1);
			$chgObj.text(newIndex+1);
	}
	
	//배포 방법 선택
	function fnDplTypeCdChg(thisObj){
		//자동인경우 자동 항목 보이기
		if(thisObj.value == "01"){
			$(".dplTypeCdMask").slideDown();
		}else{
			$(".dplTypeCdMask").slideUp();
		}
	}
	
	//JOB 전체 ORD 다시 세팅 (0부터)
	function fnJobListAllOrdSet(){
		//job loop
		$.each($(".dpl_job_row"), function(idx, map){
			//ord 변경
			$(map).attr("ord",idx+1);
			$(map).children(".dplStartOrdCell").attr("ord",idx+1);
			$(map).children(".dplStartOrdCell").text(idx+1);
		});
	}
	
	//JOB 클릭시 체크박스 선택
	function fnJobInfoClick(thisObj, event){
		//label 체크시 중지
		if($(event.target).is("label[for^=addJobDelChk]") || $(event.target).is("input[name=addJobDelChk]")){
			return false;
		}
		
		var $jobInfo = $(thisObj);
		
		//하위 checkbox 체크
		$jobInfo.children(".dpl_middle_cell").children(".dpl_chk").children("input[type=checkbox]").click();
	}
	
	//JOB 실행순서 변경
	function fnJobInfoOrdMove(type){
		//전체 JOB 갯수
		var jobList = $(".dpl_middle_row.dpl_job_row").length;
		
		//선택 JOB INFO 가져오기
		var $chkInfo = $("input[type=checkbox][name=addJobDelChk]:checked");
		
		//1개만 선택
		if($chkInfo.length > 1){
			jAlert("1개의 JOB만 선택해주세요.");
			return false;
		}
		
		//jenId, jobId
		var jenId = $chkInfo.attr("jenid");
		var jobId = $chkInfo.attr("jobid");
		
		//선택 JOB 부모 div
		var $chkJobInfo = $(".dpl_middle_row.dpl_job_row[jenid="+jenId+"][jobid="+jobId+"]");
		
		//선택 JOB ord
		var jobOrd = parseInt($chkJobInfo.attr("ord"));
		if(type == "up"){
			//실행순서가 1일경우 위로 불가능
			if(jobOrd == 1){
				return false;
			}
			
			//변경 ord
			var chgJobOrd = jobOrd-1;
			
			//선택 JOB 하위 JOB
			var $targetObj = $(".dpl_middle_row.dpl_job_row[ord="+chgJobOrd+"]");
			$targetObj.before($chkJobInfo);
		
			//변경된 object ord변경
			$chkJobInfo.attr("ord",chgJobOrd);
			$chkJobInfo.children(".dplStartOrdCell").attr("ord",chgJobOrd);
			$chkJobInfo.children(".dplStartOrdCell").text(chgJobOrd);
		
			//변경된 object ord변경
			$targetObj.attr("ord",jobOrd);
			$targetObj.children(".dplStartOrdCell").attr("ord",jobOrd);
			$targetObj.children(".dplStartOrdCell").text(jobOrd);
		}
		else if(type == "down"){
			//실행순서가 마지막일경우 위로 불가능
			if(jobOrd == jobList){
				return false;
			}
			
			//변경 ord
			var chgJobOrd = jobOrd+1;
			
			//선택 JOB 하위 JOB
			var $targetObj = $(".dpl_middle_row.dpl_job_row[ord="+chgJobOrd+"]");
			$targetObj.after($chkJobInfo);
		
			//변경된 object ord변경
			$chkJobInfo.attr("ord",chgJobOrd);
			$chkJobInfo.children(".dplStartOrdCell").attr("ord",chgJobOrd);
			$chkJobInfo.children(".dplStartOrdCell").text(chgJobOrd);
		
			//변경된 object ord변경
			$targetObj.attr("ord",jobOrd);
			$targetObj.children(".dplStartOrdCell").attr("ord",jobOrd);
			$targetObj.children(".dplStartOrdCell").text(jobOrd);
		}else{
			jAlert("알 수 없는 명령 입니다.");
			return false;
		}
	}
	
	
//배포계획 저장
function fnSaveReqInfoAjax(formId){
	//등록된 JOB 목록
	var $addJobList = $(".dpl_middle_row.dpl_job_row");
	
	//JOB 추가 확인
	if($addJobList.length == 0){
		jAlert("JOB을 등록해주세요.");
		return false;
	}
	jConfirm("배포 계획을 저장하시겠습니까?", "알림창", function( result ) {
		if( result ){
			//FormData에 input값 Json형태로 넣기
			gfnFormDataAutoJsonValue(strFormId,fd);
			
			//자동 실행 일시 생성
			fd.append("dplAutoDtm",$("#dplDt").val()+" "+$("#dplAutoTm").val());
			
			
			//추가된 JOB TO JSON DATA
			var jobData = [];
			
			//JSON DATA 만들기
			$.each($addJobList,function(idx, map){
				var jenId = $(map).attr("jenid");
				var jobId = $(map).attr("jobid");
				var jobStartOrd = $(map).attr("ord");
				
				//push
				jobData.push({jenId: jenId, jobId: jobId, jobStartOrd: jobStartOrd});
			});
			
			//기본 값과 type 넘기기
			fd.append("type",'${param.popupGb}');
			fd.append("selJobList",JSON.stringify(jobData));
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/dpl/dpl1000/dpl1000/saveDpl1000DeployVerInfoAjax.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false}
					,fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
		    	
		    	//오류 발생
		    	if(data.errorYn == 'Y'){
		    		jAlert(data.message);
		    		fd = new FormData();
		    		return false;
		    	}		    
		    	//그리드 새로고침
				fnInGridListSet(0,mySearch.getParam());
		    	
				jAlert(data.message, '알림창');
				gfnLayerPopupClose();
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				toast.push(xhr.status+"("+err+")"+" 에러가 발생했습니다.");
		    	gfnLayerPopupClose();
			});
			//AJAX 전송
			ajaxObj.send();
		}
	});	
}

// 배포 계획 등록/수정 팝업
function fnDpl1001GuideShow(){
	var mainObj = $(".popup");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["dpl1001"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}

</script>

<div class="popup">
	<form id="dpl1001PopupFrm" name="dpl1001PopupFrm" method="post" onsubmit="return false;">
			<input type="hidden" name="popupGb" id="popupGb" value="${param.popupGb}"/>
			<c:if test="${param.popupGb eq 'update'}">
				<input type="hidden" name="dplId" id="dplId" value="${param.dplId}"/>
			</c:if>
			
			<div class="pop_title">배포 계획 생성</div>
			<div class="pop_sub" style="position: relative;">
				<div class="pop_dpl_mask" id="pop_dpl_mask">
					<div class="pop_dpl_sign_frame">
						<div class="signFrameSub signFrameHalf">
							<img class="usrImgClass" src="/cmm/fms/getImage.do?fileSn=0&atchFileId=${dpl1000DplInfo.signUsrImg}">
						</div>
						<div class="signFrameSub signFrameHalf">
							[${dpl1000DplInfo.signUsrNm}]</br>
							결재 대기중입니다.
						</div>
						<div class="signFrameSub" id="signDtmAgo">
							
						</div>
					</div>
				</div>
				<div class="pop_dpl_div_sub div_sub_left" guide="dpl1001dplInfo">
					<div class="pop_menu_row pop_menu_oneRow first_menu_row">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplStsCd">배포 상태</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<span class="search_select">
								<select class="select_useCd" name="dplStsCd" id="dplStsCd" value="${dpl1000DplInfo.dplStsCd}" style="height:100%; width:100%;" opttype="02" cmmcode="DPL00001"></select>
							</span>
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplVer">배포 버전</label></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<input type="text" title="배포 버전" class="input_txt" name="dplVer" id="dplVer" value="${dpl1000DplInfo.dplVer}" maxlength="100"  />
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplNm">배포 명</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<input type="text" title="배포 명" class="input_txt" name="dplNm" id="dplNm" value="${dpl1000DplInfo.dplNm}" maxlength="200"  />
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplDt">배포 일자</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<input type="text" title="배포 일자" class="input_txt" name="dplDt" id="dplDt" value="<fmt:formatDate value="${dpl1000DplInfo.dplDt}" pattern="yyyy-MM-dd"/>" modifyset="02" readonly="readonly"/>
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplUsrNm">배포자</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<input type="hidden" name="dplUsrId" id="dplUsrId" title="배포자" opttype="03" value="${dpl1000DplInfo.dplUsrId}"/>
							<input type="text" title="배포자" class="dpl1001_charger" name="dplUsrNm" id="dplUsrNm" value="${dpl1000DplInfo.dplUsrNm}" modifyset="02"/>
							<span class="button_normal2 dpl1001_charger" id="btn_dplUser_select"><li class="fa fa-search"></li></span>
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="signUsrNm">결재자</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<input type="hidden" name="signUsrId" id="signUsrId" title="결재자" opttype="03" value="${dpl1000DplInfo.signUsrId}"/>
							<input type="text" title="결재자" class="dpl1001_charger" name="signUsrNm" id="signUsrNm" value="${dpl1000DplInfo.signUsrNm}" modifyset="02"/>
							<span class="button_normal2 dpl1001_charger" id="btn_signUser_select"><li class="fa fa-search"></li></span>
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplTypeCd">배포 방법</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<span class="search_select">
								<select class="select_useCd" name="dplTypeCd" id="dplTypeCd" title="배포 방법" value="${dpl1000DplInfo.dplTypeCd}" OS="${dpl1000DplInfo.dplTypeCd}" style="height:100%; width:100%;" onchange="fnDplTypeCdChg(this)" opttype="02" cmmcode="DPL00003"></select>
							</span>
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow dplTypeCdMask">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplAutoAfterCd">실패 후 처리</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<span class="search_select">
								<select class="select_useCd" name="dplAutoAfterCd" id="dplAutoAfterCd" title="실패 후 처리" value="${dpl1000DplInfo.dplAutoAfterCd}" OS="${dpl1000DplInfo.dplAutoAfterCd}" style="height:100%; width:100%;" opttype="02" cmmcode="DPL00004">
								</select>
							</span>
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow dplTypeCdMask">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplAutotm">자동 실행 시간</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<input type="text" title="자동 실행 시간" class="input_txt" name="dplAutoTm" id="dplAutoTm" value="<fmt:formatDate value="${dpl1000DplInfo.dplAutoDtm}" pattern="HH:mm:ss"/>"   />
						</div>
					</div>
					<div class="pop_menu_row pop_menu_oneRow dplTypeCdMask">
						<div class="pop_menu_col1 pop_oneRow_col1"><label for="dplRestoreCd">원복 타입</label><span class="required_info">&nbsp;*</span></div>
						<div class="pop_menu_col2 pop_oneRow_col2">
							<span class="search_select">
								<select class="select_useCd" name="dplRestoreCd" id="dplRestoreCd" title="원복 타입" value="${dpl1000DplInfo.dplRestoreCd}" OS="${dpl1000DplInfo.dplRestoreCd}" style="height:100%; width:100%;" opttype="02" cmmcode="DPL00005">
								</select>
							</span>
						</div>
					</div>
					<div class="pop_note" style="margin-bottom:0px;">
						<div class="note_title">결재 요청 의견</div>
						<textarea class="input_note dpl_note" title="결재 요청 의견" name="dplSignTxt" id="dplSignTxt" rows="7" value="" maxlength="2000"> ${dpl1000DplInfo.dplSignTxt}</textarea>
					</div>
					<div class="pop_note" style="margin-bottom:0px;">
						<div class="note_title">배포 설명</div>
						<textarea class="input_note dpl_note" title="배포 설명" name="dplDesc" id="dplDesc" rows="7" value="" maxlength="1000"  >${dpl1000DplInfo.dplDesc}</textarea>
					</div>
				</div>
				<div class="pop_dpl_div_sub div_sub_right">
					<div class="dpl_top_menu_div" guide="dpl1001dplJobBtn">
						<div class="button_normal2" id="btn_update_job_up"><i class='fas fa-long-arrow-alt-up' aria-hidden='true'></i>&nbsp;위로</div>
						<div class="button_normal2" id="btn_update_job_down"><i class='fas fa-long-arrow-alt-down' aria-hidden='true'></i>&nbsp;아래로</div>
						<div class="button_normal2" id="btn_insert_job"><i class='fa fa-save' aria-hidden='true'></i>&nbsp;등록</div>
						<div class="button_normal2" id="btn_delete_job"><i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;삭제</div>
					</div>
					<div class="dpl_middle_job_header">
						<div class="dpl_middle_cell">
							<div class="dpl_chk">
								<input type="checkbox" title="체크박스" name="addJobDelChk_all" id="addJobDelChk_all"/><label for="addJobDelChk_all"></label>
							</div>
						</div>
						<div class="dpl_middle_cell">&nbsp;</div>
						<div class="dpl_middle_cell">JENKINS</br>NAME</div>
						<div class="dpl_middle_cell">JENKINS</br>URL</div>
						<div class="dpl_middle_cell">JOB</br>TYPE</div>
						<div class="dpl_middle_cell">JOB</br>ID</div>
						<div class="dpl_middle_cell">원복</br>JOB ID</div>
					</div>
					<div class="dpl_bottom_job_content" id="dpl_bottom_job_content" guide="dpl1001dplJobInfo">
						<c:if test="${not empty dpl1000DplJobList}">
							<c:forEach items="${dpl1000DplJobList}" var="map">
							<div class="dpl_middle_row dpl_job_row" jenid="${map.jenId}" jobid="${map.jobId}" ord="${map.jobStartOrd}" onclick="fnJobInfoClick(this,event)">
								<div class="dpl_middle_cell">
									<div class="dpl_chk">
										<input type="checkbox" title="체크박스" name="addJobDelChk" id="addJobDelChk_${map.jenId}_${map.jobId}" jenid="${map.jenId}" jobid="${map.jobId}"/><label for="addJobDelChk_${map.jenId}_${map.jobId}"></label>
									</div>
								</div>
								<div class="dpl_middle_cell dplStartOrdCell" ord="${map.jobStartOrd}">${map.jobStartOrd}</div>
								<div class="dpl_middle_cell" title="${map.jenNm}">${map.jenNm}</div>
								<div class="dpl_middle_cell" title="${map.jenUrl}">${map.jenUrl}</div>
								<div class="dpl_middle_cell">${map.jobTypeNm}</div>
								<div class="dpl_middle_cell">${map.jobId}</div>
								<div class="dpl_middle_cell">${map.jobRestoreId}</div>
							</div>
							</c:forEach>
						</c:if>
					</div>
				</div>
				<div class="btn_div">
					<c:if test="${empty dpl1000DplInfo or dpl1000DplInfo.signStsCd != '01'}">
						<div class="button_normal save_btn" id="btn_update_popup">저장</div>
					</c:if>
						<div class="button_normal exit_btn" id="btn_cancle_popup">취소</div>
					</div>
			</div>
	</form>
</div>
</html>