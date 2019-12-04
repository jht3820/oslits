<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="ko">
<head>
<title>OpenSoftLab</title>

<style>


.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
/* .layer_popup_box{ width:590px !important; height:845px !important; } */
layer_popup_box{ width:590px; height:845px; }
.lp10{padding-left: 10px}
input[type='radio']+label{font-weight: bold;}
/* .textarea_height{height:107px !important;} */

/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
.ui-menu-item {font-size: 0.5em !important; text-align:left;}

.layer_popup_box .pop_left { padding: 20px 17px; }
.pop_radio { padding: 10px 15px 12px 1px; }

#textarea_height{height:140px}

input::-webkit-input-placeholder, textarea::-webkit-input-placeholder { color: #FF6C6C; }
input::-moz-placeholder, textarea::-moz-placeholder { color: #FF6C6C; }
input::-moz-placeholder, textarea::-moz-placeholder { color: #FF6C6C; }
input:-ms-input-placeholder, textarea:-ms-input-placeholder { color: #FF6C6C; }
input::-webkit-input-placeholder, textarea::-webkit-input-placeholder { color: #FF6C6C; }

/*익스플로러 적용 위해 !important 추가*/
/* 팝업에 따라 pop_menu_col1, pop_menu_col2 높이 변경 */
.pop_menu_row .pop_menu_col1 { width: 34% !important;   height: 48px !important;  }
.pop_menu_row .pop_menu_col2 { width: 66% !important;  height: 48px !important;  }
.pop_menu_row .pop_oneRow_col1{width: 17% !important;  }
.pop_menu_row .pop_oneRow_col2{width: 83% !important; text-align: left;}
.pop_menu_row .menu_col1_subStyle { width: 40%; }
.pop_menu_row .menu_col2_subStyle { width: 60%; }
.pop_sub input[type="password"].input_txt { width:100%;  height:100% !important;  }
.pop_menu_row .pop_menu_col2 .pop_radio{ line-height: 17px; padding-top: 10px; text-align: left;} 
.pop_menu_row .pop_menu_col2 .div_message { padding-top: 10px; padding-left: 8px; font-size: 13px; text-align: left; line-height: 17px; display:none;}
.pop_input {width: 100%; height: 100%;}

#usrId, #usrEmail { width: 73%; margin-right: 3px; }
#usrIdChk, #usrEmailChk, #btn_search_dept, #btn_init{ padding: 4px 4px 0 4px; font-weight: bold; width: 60px; height: 100%; }
#selectDeptName { width: 89%; margin-right: 3px; }
#usrUpdateMsg { text-align: left; font-size: 13px; }
#usrDutyCd { min-width:100% !important;}
#usrPositionCd { min-width:100% !important;}


</style>


<script>

globals_guideChkFn = fnAdm2001GuideShow;

// 팝업 페이지 타입
var popupType = "${param.proStatus}";


//유효성 체크
var arrChkObj = {"usrId":{"type":"length","msg":"아이디는 20byte까지 입력이 가능합니다.","max":20}
				,"usrNm":{"type":"length","msg":"이름은 200byte까지 입력이 가능합니다.", "max":200}
				,"usrTelNo":{"type":"number"}
				,"usrEmail":{"type":"length","msg":"이메일은 50byte까지 입력이 가능합니다.", "max":50}
				,"usrPw":{"type":"length","msg":"비밀번호는 150byte까지 입력이 가능합니다.", "max":150}	
				,"reUsrPw":{"type":"length","msg":"비밀번호는 50byte까지 입력이 가능합니다.", "max":50}
					,"selectDeptName":{"type":"length","msg":"소속 명은 300byte까지 입력가능합니다.", "max":300}
				,"usrEtc":{"type":"length","msg":"비고는 4000byte까지 입력가능합니다.", "max":4000}
				};
		

//유효성 체크
var saveObjectValid = {
			"usrTelNo":{"type":"regExp","pattern":/^([0-9]{3,13}).*$/ ,"msg":"연락처 형식이 아닙니다. (3~13자리) (예) 01012341234", "required":true}
			,"usrNm":{"type":"regExp","pattern":/^[0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣_-]{1,200}$/ ,"msg":"이름은 한글, 영문, 숫자, 특수문자( _ -) 만 입력가능합니다.", "required":true}
			//아이디 중복검사 시에 체크 함
			/* "usrId":{"type":"regExp","pattern":/^[a-z0-9_-]{5,20}$/ ,"msg":"아이디는 반드시 5-20자내에서 사용 가능합니다.", "required":true} */  
			//이메일 중복검사 시에 체크 함
			/* ,"usrEmail":{"type":"regExp","pattern":/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i ,"msg":"이메일 형식이 아닙니다.","required":true} */
}	

// 수정일 경우 추가로 소속,비고 유효성 체크
var arrChkObj2 = {
			"selectDeptName":{"type":"length","msg":"소속 명은 300byte까지 입력가능합니다.", "max":300}
		,"usrEtc":{"type":"length","msg":"비고는 4000byte까지 입력가능합니다.", "max":4000}
		};

$(window).resize(function(){
	elmt_resize(); 
});	

/*
 * 화면 확대 축소에따라 팝업 input 및 button 사이즈 조정
 */
function elmt_resize(){
	
	var popupType = "${param.proStatus}";
	var windowWidth = $( window ).width();
	
	if(windowWidth >= 2560){
		if(popupType == "I"){
			
			$(".button_normal2").css("width", "70px");
			$("#usrId").css("width", "68%");
			$("#usrEmail").css("width", "68%");
			$("#selectDeptName").css("width", "88%");
			
		}else if(popupType == "U"){
			
			$("#usrEmailChk").css("width", "70px");
			$("#btn_search_dept").css("width", "70px");
			$("#usrEmail").css("width", "68%");
			$("#selectDeptName").css("width", "88%");
		}
		
	}else if(windowWidth < 2560){
		if(popupType == "I"){
			
			$("#usrIdChk").css("width", "60px");
			$("#usrEmailChk").css("width", "60px");
			$("#btn_search_dept").css("width", "64px");
			$("#usrId").css("width", "73%");
			$("#usrEmail").css("width", "73%");
			$("#selectDeptName").css("width", "89%");
			
		}else if(popupType == "U"){

			$("#usrEmailChk").css("width", "60px");
			$("#btn_search_dept").css("width", "64px");
			$("#usrEmail").css("width", "73%");
			$("#selectDeptName").css("width", "89%");
		}
	}
}
	
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
	var mstCdStrArr = "CMM00001|ADM00005|ADM00008|ADM00007";
	var strUseYn = 'Y';
	var arrObj = [ $("#useCd"),$("#block"),$("#usrDutyCd"), $("#usrPositionCd")];
	var arrComboType = ["","","S","S"];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	

	// 팝업창 타입 - 등록/수정/상세
	var popupType = "${param.proStatus}";
	
	gfnInputValChk(arrChkObj);
	
	// 수정일 경우 조직명/비고 유효성체크 추가로
	if(popupType == "U"){
		//유효성 체크
		gfnInputValChk(arrChkObj2);
	}

	
	// 등록, 수정, 삭제에 따라 input css 변경
	fnInputStyleChange(popupType);
	
	// 사용자의 이전 비밀번호
	var usrPwBefore = "${adm2000UsrInfo.usrPw}";
	var usrEmailBefore = "${adm2000UsrInfo.email}";
	
	var chkId = "N";
	var chkEmail = "N";
	
	// 필수값 체크
	var strCheckObjArr = ["usrId","usrNm","usrTelNo","usrEmail","usrPw", "selectDeptName"];
	var sCheckObjNmArr = ["아이디", "성명", "연락처", "이메일", "비밀번호", "소속"];
	
	var proStatus = $("#proStatus").val();
	// 사용자의 조직ID (DB데이터)
	var beforeDeptId = "${adm2000UsrInfo.deptId}";
	// 사용자 조직명 (DB데이터)
	var beforeDeptNm = "${adm2000UsrInfo.deptName}";
	
	var useCdVal = "${adm2000UsrInfo.useCd}";
	var blockVal = "${adm2000UsrInfo.block}";
	var usrDutyCdVal = "${adm2000UsrInfo.usrDutyCd}";
	var usrPositionCdVal = "${adm2000UsrInfo.usrPositionCd}";
	
	// 등록일 경우 사용여부 기본값 [예]
	if( proStatus == "I" ){
		$("#useCd").val("01").prop("selected", true);
		// 등록 시 포커스트 사용자 Id로
		$("#usrId").focus();
	}
	
	// 수정일 경우  select 선택
	if( proStatus == "U" ){
		// 사용유무, 차단여부, 직책, 직급
		$("#useCd").val(useCdVal).prop("selected", true);
		$("#block").val(blockVal).prop("selected", true);
		$("#usrDutyCd").val(usrDutyCdVal).prop("selected", true);
		$("#usrPositionCd").val(usrPositionCdVal).prop("selected", true);
		// 수정 시 포커스트 사용자 이름으로
		$("#usrNm").focus();
	}
	
	// 차단유무 변경 이벤트
	$("#block").change(function(){
		// 3개월 이상 로그인하지 않았는지 체크하기 위한 값
		var loginExprYn = "${adm2000UsrInfo.loginExprYn}";
		// 차단여부 값
		var usrBlock = $("#block").val();
		
		// 3개월이상 로그인하지 않았을 경우
		if(loginExprYn == "Y"){
			// 차단여부를 정상으로 변경 시
			if(usrBlock == "01"){
				var blockMsg = $("#usrNm").val() + " 님은 3개월 이상 미접속으로 차단되었습니다. \n\n 차단여부를 [정상]으로 변경시 로그인 할 수 있습니다";
				jAlert(blockMsg, "알림");
				 $("#loginExprChange").val("Y");
			// 차단여부 차단으로 변경 시	
			}else{
				$("#loginExprChange").val("N");
			}
		}
	});
	
	/* 저장버튼 클릭 시 */
	$('#btn_insert_usrPopup').click(function() {
		
			var usrNm 		= $("#usrNm").val();
			var usrId 		= $("#usrId").val();
			var usrPw 		= $("#usrPw").val();
			var reUsrPw 	= $("#reUsrPw").val();
			var usrEmail	= $("#usrEmail").val();
			var usrEtc		= $("#usrEtc").val();
			var usrTelNo	= $("#usrTelNo").val();
			var useCd		= $("#useCd option:selected").val();
			var deptId 		= $("#deptId").val();
			var pwFailCnt	= $("#pwFailCnt").val();
			var block		= $("#block option:selected").val();
			var deptNm		= $("#selectDeptName").val();
			var usrDutyCd 		= $("#usrDutyCd").val();
			var usrPositionCd 	= $("#usrPositionCd").val();
			var loginExprChange 	= $("#loginExprChange").val();

			var strFormId = "usrInfo";
			var alertStr	= "";
			
			if( proStatus == "I" ){
				alertStr = "등록"
			}
			if( proStatus == "U" ){
				alertStr = "수정"
			}
			 
			// 필수값 여부 체크
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return false;	
			}

			// 연락처 유효성 검사
			if(!gfnInputValChk(saveObjectValid)){
				return false;	
			}
			
			// 비밀번호 유효성 검사
			if( fnUsrPwValidationChk(usrId, usrPw) == false ){
				$("#usrPw").focus();
				$("#usrPw").addClass("inputError");
				return false;
			}
			
			// 수정일 경우 1년간 사용된 비밀번호 체크
			if( proStatus == "U" ){	
				// 비밀번호가 변경되었다면
				if( usrPwBefore != usrPw ){	
					// 1년간 사용된 비밀번호와 입력된 비밀번호 비교
					if( fnBeforeUsedPwChk(usrId, usrPw) == "Y" ){
						jAlert("1년이내 사용한 비밀번호는 사용할 수 없습니다.","알림창");
						return false;
					}
				}
			}
			
			// 신규 등록이고, 아이디 중복 검사를 하지 않았을 경우
			if( (proStatus == "I") && (chkId == "N") ){		
				jAlert("아이디 중복확인을 하세요.","알림창");
				$("#usrId").focus();
				return false;
			}
			
			// 등록, 수정시 이메일 중복검사를 하지 않았을 경우	
			if(chkEmail == "N"){
				if(usrEmail == usrEmailBefore){
           			chkEmail = "Y";
           			$("#usrEmail").removeClass("inputError");
	        		$("#usrEmail").attr("readonly",true);
	        		$("#usrEmail").attr("class","fl readonly");
				}else{
					jAlert("이메일 중복확인이 필요합니다.","알림창");
					$("#usrEmail").focus();
					return false;
				}
			}
			
			// 신규등록일 경우
			if( proStatus == "I" ){
				if(usrPw != reUsrPw ){	// 비밀번호 와 비밀번호 확인 값이 같지 않을 경우
					jAlert("비밀번호가 일치 하지 않습니다.","알림창");
					$("#reUsrPw").focus();
					$("#reUsrPw").val("");
					$("#reUsrPw").addClass("inputError");
					return false;
				}
			}
			
			// 사용자 등록 시 조직선택 팝업에서 조직을 선택하지 않았을 경우
			if( gfnIsNull(deptId) ){
				jAlert("소속을 검색하거나 소속 검색 창에서 소속을 선택하세요.","알림창");
				$("#selectDeptName").focus();
				$("#selectDeptName").addClass("inputError");
				return false;
			}

			// 수정일 경우
			if( proStatus == "U" ){	
				
				// 수정 전 사용자의 조직 ID와 현재 조직ID가 같을 경우 → 조직이 수정되지 않은 상태
				// 따라서 조직 선택 텍스트필드 체크
				if( beforeDeptId == deptId ){
					
					/*
					 * 현재 입력된 조직명과 가져온 조직명을 비교
					 * 수정 전 조직명 : '솔루션사업본부 > 기획사업부 > 기획1팀'
					 * 현재 조직명 : ㅇㅁㄴㅇㅁㄴㄹㄴㅇ (이렇게 입력되어 있을경우 체크)
					 */
					if( deptNm != beforeDeptNm ){
						jAlert("입력된 소속명이 잘못되었습니다. <br>소속 검색 창에서 정확한 소속을 선택해 주세요.","알림창");
						$("#selectDeptName").focus();
						$("#selectDeptName").addClass("inputError");
						return false;
					}
				}	
			}

			//error있는경우 오류
			if($("#usrInfo .inputError").length > 0){
				jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
				$("#usrInfo .inputError")[0].focus()
				return false;
			}
			
			// 사용자 등록/수정 정보
			var usrInfo = { "usrNm":usrNm, "usrId":usrId, "usrPw":usrPw, "usrEmail":usrEmail.trim()
							,"usrEtc":usrEtc, "useCd":useCd, "usrTelNo":usrTelNo , "deptId":deptId
							, "pwFailCnt":pwFailCnt, "block":block, "usrDutyCd":usrDutyCd, "usrPositionCd":usrPositionCd
							, "proStatus":proStatus, "loginExprChange":loginExprChange}
			
			// 차단유무에 따라 비밀번호 실패횟수 수정
			block == "01" ? usrInfo.pwFailCnt = "0" : usrInfo.pwFailCnt;

			// 저장 전 연락처 정규식 유효성 체크
			if(!gfnSaveInputValChk(saveObjectValid)){
				return false;	
			}
			
			// 저장 전 입력값 유효성 체크
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}	
			
			// 수정일 경우 조직명, 비고 추가로 체크
			if( proStatus == "U" ){	
				// 입력값 유효성 체크
				if(!gfnSaveInputValChk(arrChkObj2)){
					return false;	
				}	
			}
			
			// 사용자 정보 등록/수정
			fnSabeUserInfo(usrInfo);
	});
	
	/* 아이디 중복 체크 */
	$('#usrIdChk').click(function() {
		$(".inputError").removeClass("inputError");
		var usrId = $("#usrId").val();
		var strFormId = "usrInfo";
		
		var usrIdChk = ["usrId"];
		var usrIdChkNm = ["아이디"];
		
		if(gfnRequireCheck(strFormId, usrIdChk, usrIdChkNm)){
			return false;	
		}
	
		var chk_id = /^[a-z0-9_-]{5,20}$/; // 소문자, 언더바(_),파이픈(-) 최소 5자리 ~ 20자리 
		if(chk_id.test($("#usrId").val())){
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm2000/adm2000/selectCmm2000IdCheck.do'/>"}
					,{ "usrId":usrId });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
						if( data.chkId == "Y"){
							chkId = "Y";
							$("#usrId").removeClass("inputError");
			        		$("#usrId").attr("readonly",true);
			        		$("#usrId").attr("class","fl readonly");
						}else{
							chkId = "N";
							$("#usrId").addClass("inputError");
							$("#usrId").val("");
							$("#usrId").focus();
						}
					}
				});
			});
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				chkId = "N";
	        	data = JSON.parse(data);
	        	jAlert(data.message,"알림창");
		 	});
			//AJAX 전송
			ajaxObj.send();
		}else{
			jAlert("5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.", "알림창", function( result ) {
				if( result ){
					$("#usrId").addClass("inputError");
					$('#usrId').val("");
					$('#usrId').focus();
					chkId = "N";
				}
			});
					
		}
	});
	
	
	/* 이메일 중복 체크 */
	$('#usrEmailChk').click(function() {
		$(".inputError").removeClass("inputError");
		
		// 이메일값 공백제거
		var usrEmail = $("#usrEmail").val().trim();
		
		var strFormId = "usrInfo";
		var usrEmailChk = ["usrEmail"];
		var usrEmailChkNm = ["이메일"];
		
		
		if(gfnRequireCheck(strFormId, usrEmailChk, usrEmailChkNm)){
			return false;	
		}
		
		// 이메일 체크 정규식
		var pattern =/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		
		if(usrEmail.length<6 || !pattern.test(usrEmail)) {
			$("#usrEmail").addClass("inputError");
			jAlert("이메일 형식이 아닙니다.<br>(예) mymail@naver.com", "알림창");
		}else{
			//현재 사용자의 이메일과 동일한 경우 사용유무 물어봄
			if(usrEmail == usrEmailBefore){ 
				jConfirm("현재 사용자의 이메일입니다. <br>현재 이메일을 사용하시겠습니까?", "알림창", function( result ) {
            		if(result){
            			chkEmail = "Y";
            			$("#usrEmail").removeClass("inputError");
		        		$("#usrEmail").attr("readonly",true);
		        		$("#usrEmail").attr("class","fl readonly");
            		}
            	});	
			}else{
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/adm/adm2000/adm2000/selectCmm2000EmailCheck.do'/>"}
						,{ "usrEmail":usrEmail });
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);
		        	jAlert(data.message, '알림창', function( result ) {
						if( result ){
							if( data.chkEmail == "Y"){
								chkEmail = "Y";
								$("#usrEmail").removeClass("inputError");
				        		$("#usrEmail").attr("readonly",true);
				        		$("#usrEmail").attr("class","fl readonly");
							}else{
								chkEmail = "N";
								$("#usrEmail").addClass("inputError");
								$("#usrEmail").val("");
								$("#usrEmail").focus();
							}
						}
					});
				});
				//AJAX 전송 오류 함수
				ajaxObj.setFnError(function(xhr, status, err){
					data = JSON.parse(data);
					jAlert(data.message, "알림창");
			 	});
				//AJAX 전송
				ajaxObj.send();
			}
		}
	});
	
	/* 비밀번호 확인 포커스 아웃시 처리 */
	$("#reUsrPw").focusout(function() {
		if( proStatus == "I" ){
			var usrPw 	= $("#usrPw").val();
			var reUsrPw = $("#reUsrPw").val();
			// 비밀번호 와 비밀번호 확인 값이 같지 않을 경우
			if(usrPw != reUsrPw ){	
				$("#reUsrPw").attr( "placeholder", "비밀번호가 일치하지 않습니다." );
				$("#reUsrPw").val("");
				$("#reUsrPw").addClass("inputError");
			}
		}
	});
	
	/* 조직 검색창 엔터키 이벤트 */
	$('#selectDeptName').keyup(function(e) {
		
		// inputError가 있으면 제거
		if($(this).hasClass("inputError")){
    		$(this).removeClass("inputError");
    	}
		if(e.keyCode == '13' ){
			$('#btn_search_dept').click();
		}
	});
	
	/* 조직검색 버튼 클릭*/
	$('#btn_search_dept').click(function() {

		var selectDeptName = $("#selectDeptName").val();
		
		gfnCommonDeptPopup(selectDeptName, function(deptId,deptNm){
			$("#selectDeptName").val(deptNm);
			$('input[name=deptId]').attr('value',deptId);
			
			// 소속 창 readonly
			$("#selectDeptName").attr("readonly",true);
			$("#selectDeptName").attr("class","fl readonly");
		});
		
	});
	
	/* 취소버튼 클릭 시 팝업 창 사라지기*/
	$('#btn_cancle_usrPopup').click(function() {
		gfnLayerPopupClose();
	});
	
	$('#btn_init').click(function() {
		if(confirm("초기화 진행시 현재 입력중인 정보가 저장되지 않습니다 계속 진행하시겠습니까?")){
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm2000/adm2000/updateAdm2000AccountInit.do'/>"}
					,{ "usrId":$("#usrId").val() });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
	        	jAlert(data.message, '알림창', function( result ) {
	        		gfnLayerPopupClose();
				});
			});
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
				jAlert(data.message, "알림창");
		 	});
			//AJAX 전송
			ajaxObj.send();
		}
	});
	
});


	/* 사용자 등록/수정 */
	function fnSabeUserInfo(usrInfo){
		// 등록,수정 구분값
		var proStatus = $("#proStatus").val();
		
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm2000/adm2000/insertAdm2000UsrInfo.do'/>"}
				,usrInfo);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	    	//등록 실패의 경우 리턴
	    	if(data.saveYN == 'N'){
	    		toast.push(data.message);
	    		return;
	    	}

	    	// 그리드 새로고침
	    	if(proStatus == "I"){
	    		// 등록일 경우 그리드 새로고침
	    		//fnInGridListSet();
	    		fnInGridListSet(0,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
	    	}else if(proStatus == "U"){
	        	// 수정이나 삭제 후 1페이지가 되지 않도록 그리드의 현재 페이지 번호를 넘겨준다.
	        	fnInGridListSet(firstGrid.page.currentPage,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
	    	}
	
			jAlert(data.message, '알림창', function( result ) {
				if( result ){
		        	gfnLayerPopupClose();
				}
			});
		});
		
		//AJAX 전송
		ajaxObj.send();
	}

	/**
	 * 	사용자 비밀번호 유효성 체크
	 *	@param usrId 사용자 아이디
	 *	@param usrPw 사용자 비밀번호
	 *  @returns 결과(boolean)
	 */
	function fnUsrPwValidationChk(usrId, usrPw){
		
		// 비밀번호 유효성체크 정규식
		var pwRegx = /^(?=.{9,})(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]).*$/;
		
		//유효성 체크
		var saveObjectValid = {  
					"usrPw":{"type":"regExp","pattern":pwRegx ,"msg":"비밀번호는 9자 이상 영문,숫자,특수문자를 조합해야 합니다.", "required":true}
		}

		// 비밀번호 유효성 검사
		if(!gfnInputValChk(saveObjectValid)){
			return false;	
		}
		
		// 비밀번호 사용자 아이디 포함여부 체크
		if(usrPw.indexOf(usrId) > -1){
			jAlert("비밀번호에는 사용자 아이디를 포함할 수 없습니다.","알림창");
			return false;
		}
		
		// 비밀번호 공백포함 체크
		var emptyRegx = /\s/g;
		if(emptyRegx.test(usrPw)){
			jAlert("비밀번호는 공백을 포함할 수 없습니다.","알림창");
			return false;
		}

		// 같은 문자열 반복 체크
		var repetRegx = /(\w)\1\1/;
		if(repetRegx.test(usrPw)){
			jAlert("비밀번호는 같은 문자를 3번 이상 연속해서 사용하실 수 없습니다.","알림창"); 
            return false;
        } 
		
		// 문자열 연속성 체크 - 123, 321, abc, cba
		if( fnContinueStrChk(usrPw, 3) == false ){
			jAlert("연속된 문자열(123 ,321, abc, cba 등)을 3자 이상 사용 할 수 없습니다.","알림창"); 
            return false;
		}
		
		return true;
	}
	
	/**
	 * 	입력된 문자열에 연속된 문자(123, abc 등)가 있는지 체크한다
	 *	@param str 입력 문자열
	 *	@param limit 자리수, 3 → 123이렇게 3자리 입력시 체크됨
	 *  @returns 결과(boolean)
	 */
	function fnContinueStrChk(str, limit) {
		
		var char1, char2, char3, char4 = 0;

		for (var i = 0; i < str.length; i++) {
			var inputChar = str.charCodeAt(i);

			if (i > 0 && (char3 = char1 - inputChar) > -2 && char3 < 2 && (char4 = char3 == char2 ? char4 + 1 : 0) > limit - 3){
				return false;
			}	
			char2 = char3;
			char1 = inputChar;
		}
		return true;
	}

	
	/**
	 * 	입력한 비밀번호와 1년간 사용된 비밀번호 비교
	 *	@param usrId 사용자 ID
	 *	@param usrPw 현재 화면에 입력된 비밀번호
	 */
	function fnBeforeUsedPwChk(usrId, usrPw){
		
		var chkResult = "Y";
		var sendData = { "usrId" : usrId, "usrPw" : usrPw };
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/adm/adm2000/adm2000/selectAdm2000BeforeUsedPwChkAjax.do'/>"
							,"loadingShow":false}
							,sendData);
		
		ajaxObj.async = false;
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			// 비밀번호 사용했던 기록 있을경우
			if( data.isUsedPw == "Y"){
				$("#usrPw").addClass("inputError");
				$("#usrPw").focus();
			}else{
				chkResult = "N";
				$("#usrPw").removeClass("inputError");
			}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
		
		return chkResult;
	}
	
	// 값에 따른 CSS 변경
	function fnInputStyleChange(popupType){
		
		// 익스플로러 대응
		// 비밀번호 input width 조절
		$("#usrPw").css("width","100%");
		$("#reUsrPw").css("width","100%");
		
		// 수정일 경우 
		if( popupType == "U" ){
			$("#usrId").css("width","100%");
			$("#usrPw").css("width","73%");
			$("#usrPw").css("margin-right","3px");
			
		// 상세보기일 경우	
		}else if( popupType == "S" ){
			$(".required_info").hide();
			$(".input_txt").css("width","100%");
			$(".input_txt").attr("readonly", true);
			$(".search_select").hide();
			$(".pop_input").show();
			$("#usrEtc").attr("disabled", true);
		}
		
		// 라이센스 그룹을 가져와 enldp qnxsms _GRP 제거
		var usrIdGrp = "${fn:replace(sessionScope.licVO.licGrpId,'_GRP','')}";
		// 사용자 아이디
		var usrId = "${adm2000UsrInfo.usrId}";
		
		// 최초 라이선스 등록자(usrIdGrp = usrId)일 경우
		// 사용여부, 차단여부 수정불가
		if(popupType == "U" && usrIdGrp == usrId){
			$(".select_useCd").hide();
			$(".div_message").show();
			$(".div_message").html("<span class='required_info'>수정 불가</span>");
			$("#usrUpdateMsg").html("* 최초 라이선스 등록자는 사용 유무를 수정할 수 없습니다.");
			$(".pop_note").css("margin-top","15px");
			$(".bottom_menu_row").css("margin-bottom","13px");
		}
	}
	

	function fnAdm2001GuideShow(){
		var mainObj = $(".popup");
		
		//mainObj가 없는경우 false return
		if(mainObj.length == 0){
			return false;
		}
		//guide box setting
		var guideBoxInfo = globals_guideContents["adm2001"];
		gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
	}

</script>
</head>
<body>
	<div class="popup">
		<div class="pop_title">사용자 
			<c:choose>
				<c:when test="${param.proStatus eq 'I'}">등록</c:when>
				<c:when test="${param.proStatus eq 'U'}">수정</c:when>
				<c:when test="${param.proStatus eq 'S'}">상세보기</c:when>
			</c:choose>
		</div>
		<div class="pop_sub">
		
			<form id="usrInfo" name="usrInfo" method="post" enctype="multipart/form-data" >
				<input type ="hidden" id="proStatus" name="proStatus" value="<c:out value='${param.proStatus}' />" />
				<input type ="hidden" id="deptId" name="deptId" value="<c:out value='${adm2000UsrInfo.deptId}' />" />
				<input type ="hidden" id="loginExprChange" name="loginExprChange" value="N" />
				
				<div class="pop_menu_row first_menu_row">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="usrId">아이디</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2">
						<input type="text" title="아이디" class="input_txt lp10" id="usrId" name="usrId" maxlength="20" value="<c:out value='${adm2000UsrInfo.usrId}' />" <c:if test="${param.proStatus eq 'U'}">readonly="readonly"</c:if> />
						<c:if test="${param.proStatus eq 'I'}">
							<span class="button_normal2 fl" id="usrIdChk">중복확인</span>
						</c:if>
					</div>
				</div>
				<div class="pop_menu_row first_menu_row">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="reqNo">성명</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2 pop_menu_col1_left">
						<input type="text" title="성명" class="input_txt lp10" id="usrNm" name="usrNm" maxlength="100" value="<c:out value='${adm2000UsrInfo.usrNm}' />"/>
					</div>
				</div>
				
				<div class="pop_menu_row">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="usrTelNo">연락처</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2">
						<input type="text" title="연락처" class="input_txt lp10" id="usrTelNo" name="usrTelNo" maxlength="13" max="11199999999" value="<c:out value='${adm2000UsrInfo.telno}' />" />
					</div>
				</div>
				<div class="pop_menu_row">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="usrEmail">이메일</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2 pop_menu_col1_left">
						<input type="text" title="이메일" class="input_txt lp10" id="usrEmail" name="usrEmail" maxlength="48" value="<c:out value='${adm2000UsrInfo.email}' />" />
						<c:if test="${param.proStatus ne 'S'}">
							<span class="button_normal2 fl" id="usrEmailChk">중복확인</span>
						</c:if>
					</div>
				</div>
				
				<div class="pop_menu_row">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="usrPw">비밀번호</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2">
						<input type="password" title="비밀번호" class="input_txt lp10" id="usrPw" name="usrPw" maxlength="150" value="<c:out value='${adm2000UsrInfo.usrPw}' />" />
						<c:if test="${param.proStatus eq 'U'}">
							<span class="button_normal2 fl" id="btn_init">초기화</span>
						</c:if>
					</div>
				</div>
				
				<!--// 비밀번호 확인은 등록 시 에만 노출 -->	
				<c:if test="${param.proStatus eq 'I'}">
				<div class="pop_menu_row" id="div_reUsrPw">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="reUsrPw">비밀번호 확인</label> <span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2 pop_menu_col1_left">
						<input type="password" title="비밀번호 확인" id="reUsrPw" class="input_txt lp10" maxlength="150" />
					</div>
				</div>
				</c:if>
				<!--// 비밀번호 확인은 등록 시 에만 노출 -->	
				
				<!-- 비밀번호 실패 횟수는 상세보기 및 수정 시에만 노출 -->
				<c:if test="${param.proStatus ne 'I'}">
				<div class="pop_menu_row" id="div_pwFailCnt">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="pwFailCnt">비밀번호 실패 횟수</label></div>
					<div class="pop_menu_col2 pop_menu_col1_left">
						<input type="number" title="비밀번호 실패 횟수" class="input_txt lp10" id="pwFailCnt" name="pwFailCnt" min="0"  readonly="readonly" value="<c:out value='${adm2000UsrInfo.pwFailCnt}' />"/>
					</div>
				</div>
				</c:if>
				<!-- 비밀번호 실패 횟수는 상세보기 및 수정 시에만 노출 -->
				
				<!-- 직급/직책 -->
				<div class="pop_menu_row" id="div_position">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="usrPositionCd">직급</label></div>
					<div class="pop_menu_col2">
						<span class="search_select">
							<select class="select_positionCd" name="usrPositionCd" id="usrPositionCd" style="height:100%;"></select>
						</span>
						<!-- 상세보기 시에 출력 -->
						<div class="pop_input" style="display:none">
							<input type="text" title="직급" class="input_txt lp10" id="input_usrPositionCd" name="input_usrPositionCd" readonly="readonly" value="<c:out value='${adm2000UsrInfo.usrPositionCdNm}' />"/>
						</div>
					</div>
				</div>
				
				<div class="pop_menu_row" id="div_duty">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="usrDutyCd">직책</label></div>
					<div class="pop_menu_col2 pop_menu_col1_left">
						<span class="search_select">
							<select class="select_usrDutyCd" name="usrDutyCd" id="usrDutyCd" style="height:100%;"></select>
						</span>
						<!-- 상세보기 시에 출력 -->
						<div class="pop_input" style="display:none">
							<input type="text" title="직책" class="input_txt lp10" id="input_usrDutyCd" name="input_usrDutyCd" readonly="readonly" value="<c:out value='${adm2000UsrInfo.usrDutyCdNm}' />"/>
						</div>
					</div>
				</div>
				

				
				<div class="pop_menu_row pop_menu_oneRow">
					<div class="pop_menu_col1 pop_oneRow_col1 pop_menu_col1_right"><label for="selectDeptName">소속</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2 pop_oneRow_col2 pop_menu_col1_left">
						<input type="text" title="소속" class="input_txt lp10" id="selectDeptName" name="selectDeptName" maxlength="300" value="<c:out value='${adm2000UsrInfo.deptName}' />"/>
						<c:if test="${param.proStatus ne 'S'}">
							<span class="button_normal2 fl" id="btn_search_dept" style="width: 64px;">소속검색</span>
						</c:if>
					</div>
				</div>
		
				<!-- 등록일 경우 -->
				<c:if test="${param.proStatus eq 'I'}">
				<div class="pop_menu_row pop_menu_oneRow">
					<div class="pop_menu_col1 pop_oneRow_col1 pop_menu_col1_right"><label for="useCd">사용유무</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2 pop_oneRow_col2 pop_menu_col1_left">
						<span class="search_select">
							<select class="select_useCd" name="useCd" id="useCd" value="" style="height:100%; width:100%; max-width: 38.5%;"></select>
						</span>
					</div>
				</div>
				</c:if>
				
				<!-- 수정, 상세보기일 경우 -->
				<c:if test="${param.proStatus ne 'I'}">
				<div class="pop_menu_row bottom_menu_row">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="useCd">사용유무</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2">
						<div class="div_message" style="display:none"></div>
						<span class="search_select">
							<select class="select_useCd" name="useCd" id="useCd" style="height:100%; width:100%; max-width: 100%;" ></select>
						</span>
						<!-- 상세보기 시에 출력 -->
						<div class="pop_input" style="display:none">
							<input type="text" title="사용유무" class="input_txt lp10" id="useCdNm" name=""useCdNm"" readonly="readonly" value="<c:out value='${adm2000UsrInfo.useCdNm}' />"/>
						</div>
					</div>
				</div>
				
				<div class="pop_menu_row bottom_menu_row">
					<div class="pop_menu_col1 pop_menu_col1_right"><label for="block">차단여부</label><span class="required_info">&nbsp;*</span></div>
					<div class="pop_menu_col2 pop_menu_col1_left" guide="block" >
						<span class="search_select">
							<select class="select_block" name="block" id="block" value="" style="height:100%; width:100%; max-width: 100%;"></select>
						</span>
						<!-- 상세보기 시에 출력 -->
						<div class="pop_input" style="display:none">
							<input type="text" title="차단여부" class="input_txt lp10" id="blockNm" name="blockNm" readonly="readonly" value="<c:out value='${adm2000UsrInfo.blockNm}' />"/>
						</div>
					</div>
				</div>
				</c:if>
				
				<div class="div_message required_info" id="usrUpdateMsg" style="display:none"></div>
				
				<div class="pop_note" style="margin-bottom:0px;">
					<div class="note_title">비고</div>
					<textarea class="input_note" title="비고" id="usrEtc" name="usrEtc" style="height:130px;"><c:out value='${adm2000UsrInfo.etc}' /></textarea>
				</div>
				
				<c:if test="${param.proStatus ne 'I'}">
				<div class="pop_note" style="margin-bottom:0px;">
					<div class="note_title">차단 사유</div>
					<textarea class="input_note lp10" title="비고" id="blkLog" readonly disabled="true" name="blkLog" style="height:130px;"><c:out value='${adm2000UsrInfo.blkLog}' /></textarea>
				</div>
				</c:if>
        		
				<div class="btn_div">
					<!-- 상세보기가 아닐경우 버튼 출력, 상세보기일 경우 닫기버튼만 출력  -->
					<c:choose>
        				<c:when test="${param.proStatus ne 'S'}">
	        				<div class="button_normal save_btn" id="btn_insert_usrPopup">${empty adm2000UsrInfo.usrId ? '등록':'수정'}</div>
							<div class="button_normal exit_btn" id="btn_cancle_usrPopup"  >취소</div>
        				</c:when>
        				<c:otherwise>
        					<div class="button_normal exit_btn" id="btn_cancle_usrPopup" >닫기</div>
        				</c:otherwise>
        			</c:choose>
				</div>
			</form>
		</div>
	</div>
</body>	
</html>