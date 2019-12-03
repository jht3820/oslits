<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html lang="ko">
<title>OpenSoftLab</title>

<style>
	.check_prj { padding: 31px 25px !important; }
	.checking { color: #4b73eb; padding: 5px 0px; display: inline-block; }
	.prj_chk input[type="checkbox"] { top: 17px !important; }
	.pop_right {height:50px;}
	.pop_left {height:50px;}
	/* 익스플로러 대응 !important; 추가 */
	.textarea_height{height:309px !important;}
	
	/* 팝업창 위치 */
	.ui-draggable{ top: -56px;}
	.required_info{color:red; font-weight: bold; }
	.btn_div{margin-top: 20px !important;}
</style>
	
<script>
var type = '${param.type}';
var gb = '${param.gb}';

//유효성 체크
var arrChkObj = {"authGrpNm":{"type":"length","msg":"권한 그룹 명은 50byte까지 입력이 가능합니다.",max:50}
				,"authGrpOrd":{"type":"number"}
				,"authGrpDesc":{"type":"length","msg":"권한 그룹 설명 길이 값은 500까지 입력이 가능합니다.",max:500}};
				
globals_guideChkFn = fnPrj2001GuideShowr;
$(document).ready(function() {

	// 유효성 체크
	gfnInputValChk(arrChkObj);
	
	/********************************************************************
	*	공통기능 부분 정의 시작													*
	*********************************************************************/
	
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = CMM00001:사용여부 
	*/
	var mstCdStrArr = "CMM00001|ADM00004";
	var strUseYn = 'Y';
	var arrObj = [$("#authGrpUseCd"), $("#usrTyp")];
	var arrComboType = [""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	$('#acceptUseCd').val("02");

	if(type == "admin"){
		$(".ui-draggable").css("top", "26px");
	}
	// 팝업 구분이 수정일 경우
	if(gb == "update"){
		fnSelectPrj2000AuthGrpInfoAjax();
	// 팝업 구분이 등록일 경우
	}else{
		// 등록된 역할그룹 순번 중 최고값+1
		var authGrpNextOrd = "${authGrpNextOrd}";
		
		// 조회된 다음 순번값을 순번에 세팅한다.
		if(!gfnIsNull(authGrpNextOrd)){
			$("#authGrpOrd").val(authGrpNextOrd);
		}
	}
	
	// 프로젝트 권한그룹추가 레이어 팝업 - 저장버튼 클릭 시 롤 저장 처리 
	$('#saveBtn').click(function() {
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "authGrpFrm";
		var strCheckObjArr = ["authGrpNm", "authGrpOrd", "authGrpUseCd"];
		var sCheckObjNmArr = ["역할그룹 명", "우선순위", "사용 유무"];
		if(!gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			
			// 저장 전 유효성 검사
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}
			
			//신규 롤 등록
			if(gb == "update"){
				// 권한그룹 수정 전 유효성 체크
				fnUpdateAuthGrp();
			}else{
				// 권한그룹 등록 전 유효성 체크
				fnInsertAuthGrp();
			}	
		}
		
	});
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#exitBtn').click(function() {
		gfnLayerPopupClose();
	});

	$('#acceptUseCd').change(function() {
		if($(this).val()=="01"){
			var message = "";
			
			message += "접수권한을 활성화 시킬경우 업무 화면(대시보드>운영 대시 보드>대시보드(운영)<br/>";
			message += "또는 대시보드>개발 대시 보드>대시보드(개발))을 접근 활성화 시켜<br/>";
			message += "야합니다.";
			
			jAlert(message, "알림창");
			
		}
	});
});

/**
 * 	권한그룹의 신규 롤을 추가한다.
 */
function fnInsertAuthGrp(){
	var type = '${param.type}';
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/prj/prj2000/prj2000/insertPrj2000AuthGrpInfoAjax.do"}
			,$("#authGrpFrm").serialize());
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	if(data.saveYN == 'N'){
    		jAlert(data.message,"알림창");
    		return;
    	}else{
    		var newAuthGrpId = data.newAuthGrpId;
    		
    		var usrType = $("#usrTyp").val();
    		usrType = (usrType == "01" ? "사용자" : "관리자");
    		
    		//권한롤 선택 창에 새권한 추가
    		if(type=="admin"){
    			fnSelectAdm1000PrjAuthGrpList();
    		}else{
    			fnSelectPrj2000PrjAuthGrpList();
    		}
    		    		
    		// 권한 목록 클릭시 소메뉴 권한 목록 조회
    		$('.left_table tbody tr').unbind();
    		$('.left_table tbody tr').on("click",function(){
    		    $('.left_table tbody tr').removeClass("table_active");
    		    $('.left_table tbody tr').addClass("table_inactive");
    		    $(this).addClass("table_active");
    		    $(this).removeClass("table_inactive");
    		    
    		    //소메뉴 권한 목록 조회
    		    fnAuthGrpSmallMenuList($(this).attr("id"), '${sessionScope.selPrjId}');
    		});
    		
    		//레이어 팝업창 닫기
    		gfnLayerPopupClose();
    		
    		toast.push(data.message);
    	}
	});
	//AJAX 전송
	ajaxObj.send();		
}


function fnUpdateAuthGrp(){
	var type = '${param.type}';
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/prj/prj2000/prj2000/updatePrj2000AuthGrpInfoAjax.do"}
			,$("#authGrpFrm").serialize());
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	if(data.saveYN == 'N'){
    		jAlert(data.message,"알림창");
    		return;
    	}else{
    		if(type=="admin"){
    			fnSelectAdm1000PrjAuthGrpList();
    		}else{
    			fnSelectPrj2000PrjAuthGrpList();
    		}
    		
    		var tdList = $(".left_table tbody #" + $('#authGrpId').val()).children();
    		
    		//레이어 팝업창 닫기
    		gfnLayerPopupClose();
    		
    		toast.push(data.message);
    	}
	});
	//AJAX 전송
	ajaxObj.send();	
}


function fnSelectPrj2000AuthGrpInfoAjax(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/prj/prj2000/prj2100/selectPrj2000AuthGrpInfoAjax.do"}
			,{ "authGrpId" : '${param.authGrpId}' , "type" :  type   });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){

		data = JSON.parse(data);
		
    	//디테일폼 세팅
    	gfnSetData2ParentObj(data.prjAuthGrpInfo, "authGrpFrm");

	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
	});
	
	//AJAX 전송
	ajaxObj.send();
} 

function fnPrj2001GuideShowr(){
	var mainObj = $(".popup");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["prj2001"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}


</script>

<div class="popup">

	<form id="authGrpFrm" name="authGrpFrm">
		<input type="hidden" id="newAuthGrpId" name="newAuthGrpId">
		<input type="hidden" id="authGrpId" name="authGrpId" name="${param.authGrpId}">
		<input type="hidden" id="type" name="type" value="${param.type}" >
		<input type="hidden" id="acceptUseCd" name="acceptUseCd" value="01" >
	
		<div class="pop_title">역할 그룹 생성</div>
		<div class="pop_sub">
		
			<div class="pop_left">그룹 명<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right">
				<input type="text" title="역할그룹 명" class="input_txt" id="authGrpNm" name="authGrpNm" />
			</div>
			
			<div class="pop_left textarea_height">설명</div>
			<div class="pop_right textarea_height">
				<textarea class="input_note" title="역할그룹 설명" id="authGrpDesc" name="authGrpDesc" style="height:100%;"></textarea>
			</div>
			
			<div class="pop_left">우선순위<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right">
				<input type="number" title="우선순위" value="0"  id="authGrpOrd" name="authGrpOrd"/>
			</div>
			
			<div class="pop_left">사용자유형<span class="required_info">&nbsp;*</span></div> 
			<div class="pop_right">
				<select class="search_select" name="usrTyp" id="usrTyp" style="height: 100%;"></select>
			</div>
			<div class="pop_left bottom_line">사용유무<span class="required_info">&nbsp;*</span></div> 
			<div class="pop_right bottom_line">
				<select class="search_select" name="authGrpUseCd" id="authGrpUseCd" style="height: 100%;"></select>
			</div>
			
			<div class="btn_div">
				<div class="button_normal save_btn complete_btn" id="saveBtn">저장</div>
				<div class="button_normal exit_btn" id="exitBtn">취소</div>
			</div>
		</div>
	
	</form>
</div>

</html>