<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslits/prs.css'/>' type='text/css'>
<script src="<c:url value='/js/common/oslFile.js'/>"></script>
<script src="<c:url value='/js/common/EgovMultiFile.js'/>"></script>
<script src="<c:url value='/js/common/spectrum.js'/>" ></script>
<link rel='stylesheet' href='<c:url value='/css/common/spectrum.css'/>' type='text/css'>
<style>
.info_table { max-width: 1200px; width: 100%; height: 855px;; font-size: 0.75em; padding: 40px 36px; border: 1px solid #ccc; border-radius: 3px; background: #fff; overflow: auto; }
.up_box {/* float: none; */ /* padding-top: 10px; */}

.statusbar { border: 1px solid #A9CCD1; width: 100%; padding: 3px 6px; vertical-align: center; text-align: right !important; height: 28px;}
.prsDiv {/* width: 280px;margin: 3px 0 0 0; */}
.uploadOverFlow {min-height: 10px; padding-left: 20px; padding-bottom: 25px;}

.prs_btn {/* margin: 33px 0 0 2px; */}
.prs_usrImgDiv {/* width: 60px;height: 60px;border: 1px solid silver;float: left; */ padding-left: 10px;}
.reqTicket_view{float: right;width: 120px;height: 50px;border: 1px solid;border-radius: 4px;margin-top: -10px;text-align: center;line-height: 40px;}

.input_box_1 {padding: 15px 20px 15px 10px;}
.input_box_1 input[type=text]{ height: 100%; padding-left: 7px; font-size:14px; max-width: 295px;}
.input_box_1 input[type=password]{ height: 100%; padding-left: 4px; max-width: 295px;}
.text_btn_div { height: 100%;}
.text_btn_div .input_btn { height: 35px;padding-top: 4px;}

/* 필수값 표시 */
.required_info{color:red; font-weight: bold; }

.top_content_wrap {  width:100%; height: 340px;}
.bottom_content_wrap { width:100%; height: 363px; padding-top:22px;}

.info_div { border: 1px solid #ccc; width:100%; heigth:400px;}
.content_top_left {width:49%; float:left; border-radius: 10px;}
.content_top_right{width:49%; float:right; border-radius: 10px;}
.content_bottom_left {width:49%; float:left; border-radius: 10px;}
.content_bottom_right{width:49%; float:right; border-radius: 10px;}

table {    width: 100%; height: 100%; }
table, tbody tr, td:hover { background: none !important; }

/* table, tbody tr, td { border: 1px solid #ccc; } */

/* input */
.box_line { background: none; border-top: none; border-right: none; font-weight: bold; float: left; clear: both; padding-left: 25px; width: 20%; border-width: 0 1px 1px 0; font-size: 13px; }
.input_box_1{ float: left; padding: 5px; width: 73%; margin-left: 0px;  border-top:none;}
.input_nm{}
.content_title {    font-size: 1.7em; font-weight: bold; padding-left: 18px;}
/* 이메일 중복확인 버튼*/
.check_btn{  position: none; top: none; right: none; float: right; background: #fff; font-size: 0.9em; height: 34px; line-height: 33px; } 
/* 검색버튼 */    
.search_btn{  position: none; top: none; right: none;  float: right; background: #fff; font-size: 0.9em; height: 34px; line-height: 33px; }      
/* 사용자 프로필 */
.profile_div{ width: 27%; padding: 19px 20px 15px 37px; }
/* 사용자 프로필 input box*/
.profile_input_div{padding: 8px 20px 6px 10px;}
/* 사용자 이미지 */
/* .user_img {float: none; width: auto; height: 180px; max-width: 200px !important;max-height: 200px !important;}  */
.user_img { float: none; width: 160px; height: 180px; max-width: 180px !important; max-height: 180px !important; padding-top: 20px; padding-bottom: 20px;}
/* 연락처 */
.address_div{ padding: 19px 10px 15px 15px;}
/* 연락처 input box */
.address_input_div{padding: 8px 20px 6px 10px;}
/* 비밀번호 */
.password_div{ padding: 19px 10px 15px 15px; }
/* 비밀번호 input box */
.password_input_div{padding: 8px 20px 6px 10px;}
/* 기타정보 */
.ect_info_div{ padding: 19px 10px 15px 15px; width: 22%;}
/* 기타정보 input_box */
.ect_info_input_div{padding: 8px 20px 6px 10px;}
/* 비고 */
.etc_div { height: auto; padding: 10px 20px 10px 10px;}
/* 수정버튼 */
.button_complete { background: #fff !important; color: #000 !important; border: 1px solid #ccc !important; font-weight: bold;}
/* 직급,직책 선택 */
.select_cd {height:100%; width:100%;}

.pw_txt_message{color:#f47e7e;}

</style>
<script>

	//이메일 중복확인 체크
	var emailCheckFlag  = false;
	// 개인정보 수정 전 사용자 이메일
	var beforeEmail = "";
	//input file의 id값 (필수)
	var obj = $("#dragandrophandler");
	
	var fd = new FormData();

	//유효성 체크
	var arrChkObj = {
		"usrNm":{"type":"length","msg":"이름은 30byte까지 입력이 가능합니다.", "max":30}
		,"usrPw":{"type":"length","msg":"비밀번호는 50byte까지 입력이 가능합니다.", "max":50}	
		,"reUsrPw":{"type":"length","msg":"비밀번호는 50byte까지 입력이 가능합니다.", "max":50}
		,"email":{"type":"length","msg":"이메일은 50byte까지 입력이 가능합니다.", "max":50}
		,"telno":{"type":"number"}
		,"prjNm":{"type":"length","msg":"프로젝트명은 200byte까지 입력가능합니다.", "max":200}
		,"deptName":{"type":"length","msg":"조직명은 300byte까지 입력가능합니다.", "max":300}
		,"etc":{"type":"length","msg":"비고는 2000byte까지 입력가능합니다.", "max":2000}
	};

	// 연락처 유효성
	var saveObjectValid = {
				"telno":{"type":"regExp","pattern":/^([0-9]{9,11}).*$/ ,"msg":"연락처 형식이 아닙니다. (예) 01012341234", "required":true}
	}
	
	$(document).ready(function() {
		
		// 수정 버튼권한
		var btnAuthUpdateYn = '${sessionScope.selBtnAuthUpdateYn}';
		// 수정권한이 없을경우 프로필 이미지 클릭 이벤트 제거
		if(btnAuthUpdateYn == "N"){
			$(".prs_usrImgDiv").attr('onclick', '').unbind('click');
			$(".prs_usrImgDiv").css("cursor", "default");
			// 화면 높이 조정
			$('.info_table').css("height", "815px");
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
		var mstCdStrArr = "ADM00007|ADM00008";
		var strUseYn = 'Y';
		var arrObj = [ $("#usrPositionCd"),$("#usrDutyCd")];
		var arrComboType = ["S","S"];
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
		

		// 사용자 개인정보 상세조회
		fnSelectPrs3000Info();
		
		/***** 유효성 체크 시작 *****/ 	
		
		//유효성 체크
		gfnInputValChk(arrChkObj);
		
		/* 이메일 중복확인 버튼 클릭 */
		$('#btn_search_checkRepetition').click(function() {
			
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "prs3000Form";
			var strCheckObjArr = ["email"];
			var sCheckObjNmArr = ["이메일"];
			
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return false;	
			}else{
				fnCheckRepetition();
			}
		});
		
		/* 프로젝트 검색버튼 클릭 */
		$('#btn_search_project').click(function() {
			var param="";
			gfnCommonProjectPopup(param,function(e){
				// 선택된 프로젝트 정보가있을경우
				if(!gfnIsNull(e[0])){
					$('#prjId').val(e[0].prjId);
					$('#prjNm').val(e[0].prjNm);
					
					// 프로젝트 입력부분 readonly
					$("#prjId").attr("readonly",true);
					$("#prjNm").attr("class","fl readonly");
				}
			});
		});
		
		/* 조직검색 버튼 클릭*/
		$('#btn_search_dept').click(function() {

			var inputDeptName = $("#deptName").val();
			gfnCommonDeptPopup(inputDeptName, function(deptId,deptNm){

				$("input[name=deptName]").val(deptNm);
				$('input[name=deptId]').attr('value',deptId);

				// 소속입력부분 readonly
				$("input[name=deptName]").attr("readonly",true);
				$("input[name=deptName]").attr("class","fl readonly");
			});
		});
		
		
		/* 조직 검색창 엔터키 이벤트 */
		$('#deptName').keyup(function(e) {
			
			// inputError가 있으면 제거
			if($(this).hasClass("inputError")){
	    		$(this).removeClass("inputError");
	    	}
			if(e.keyCode == '13' ){
				$('#btn_search_dept').click();
			}
		});
		
		// 수정완료 버튼 클릭시
		$('#btn_update').click(function() {

			
			/* 필수입력값 체크 공통 호출 */
			var strFormId 		= "prs3000Form";
			var strCheckObjArr  = ["usrId", "usrNm", "usrPw", "telno", "email", "prjNm", "deptName"];
			var sCheckObjNmArr  = ["아이디", "성명", "비밀번호", "연락처", "이메일","프로젝트", "소속"];
			
			/* 입력값 */
			var newMail	   		= $("#email").val();
			var usrId 			= $("#usrId").val();
			var usrPw 			= $("#usrPw").val();
			var reUsrPw 		= $("#reUsrPw").val();
			var currentDeptId	= $("#deptId").val();
			var deptNm			= $("#deptName").val();
			var currentPrjId	= $("#prjId").val();
			var prjNm			= $("#prjNm").val();
			
			/*
			 *	개인정보 수정 전 정보들
			 *	현재 입력된 값과 DB상의 사용자 정보의 비교에 사용
			 */
			var beforePrjId		= $("#beforePrjId").val();
			var beforPrjNm		= $("#beforePrjNm").val();
			var beforeDeptId 	= $("#beforeDeptId").val();
			var beforeDeptNm 	= $("#beforeDeptNm").val();
			
			//사진 업로드 Input
			var inFileList = $("input[name=uploadFileList]");
			
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

			// 이전 비밀번호 조회
			var beforePw = fnUserBeforePw(usrId);
			
			if(gfnIsNull(beforePw)){
				jAlert("사용자의 수정 전 비밀번호를 조회할 수 없습니다.","알림창");
				return false;
			}
			
			// 최초 비밀 번호와 현재 비밀번호가 다르다면 비밀번호 확인 입력
			if( beforePw != usrPw ){
				// 현재 비밀번호와 비밀번호 확인 값이 다르다면
				if(usrPw != reUsrPw ){
					
					if(gfnIsNull(reUsrPw)){
						jAlert("비밀번호 변경 시 비밀번호 확인란에 비밀번호를 입력해야합니다.","알림창");
						$("#reUsrPw").focus();
						$("#reUsrPw").addClass("inputError");
						return false;
					}
										
					if( fnBeforeUsedPwChk(usrId, usrPw) == "Y" ){
						jAlert("1년이내 사용한 비밀번호는 사용할 수 없습니다.","알림창");
						return false;
					}	
					else{
						jAlert("비밀번호가 일치 하지 않습니다.","알림창");
						$("#reUsrPw").focus();
						$("#reUsrPw").val("");
						$("#reUsrPw").addClass("inputError");
					}
					return false;
				}				
			}
			
			// 이메일이 수정되었다면 중복체크 하도록
			if( (beforeEmail != newMail) && (!emailCheckFlag) ){
				jAlert("이메일 중복체크를 하세요.","알림창");
				return false;
			}
			
			if( !gfnIsNull(inFileList[1]) ) {
				//선택하기로 파일 추가한 리스트 가져오기
				if(inFileList[1] != null){
					fd.append('file', inFileList[1].files[0]);
				}
				var fileExt = inFileList[1].files[0].name.substring(inFileList[1].files[0].name.lastIndexOf(".") + 1);
				var reg = /gif|jpg|jpeg|png/i; // 업로드 가능 확장자.
				if (reg.test(fileExt) == false) {
					jAlert("첨부파일은 gif, jpg, png로 된 이미지만 가능합니다.","알림창");
					return false;
				}
			}
			
			// 수정 전 사용자의 프로젝트 ID와 현재 프로젝트 ID가 같을 경우 → 프로젝트가 수정되지 않은 상태
			if( beforePrjId == currentPrjId ){
				
				/*
				 * 현재 입력된 프로젝트명과 가져온 프로젝트명을 비교
				 * 수정 전 프로젝트명 : OSL AGILE SOLUTION
				 * 현재 프로젝트명 : ㅇㅁㄴㅇㅁㄴㄹㄴㅇ (이렇게 입력되어 있을경우 체크)
				 */
				if( prjNm != beforPrjNm ){
					jAlert("입력된 프로젝트명이 잘못되었습니다. \n\n프로젝트 검색 창에서 정확한 프로젝트를 선택해 주세요.","알림창");
					$("#prjNm").focus();
					$("#prjNm").addClass("inputError");
					return false;
				}
			}	
			
			// 수정 전 사용자의 조직 ID와 현재 조직ID가 같을 경우 → 조직이 수정되지 않은 상태
			// 따라서 조직 선택 텍스트필드 체크
			if( beforeDeptId == currentDeptId ){
				
				/*
				 * 현재 입력된 조직명과 가져온 조직명을 비교
				 * 수정 전 조직명 : '솔루션사업본부 > 기획사업부 > 기획1팀'
				 * 현재 조직명 : ㅇㅁㄴㅇㅁㄴㄹㄴㅇ (이렇게 입력되어 있을경우 체크)
				 */
				if( deptNm != beforeDeptNm ){
					jAlert("입력된 소속명이 잘못되었습니다. \n\n소속 검색 창에서 정확한 소속을 선택해 주세요.","알림창");
					$("#deptName").focus();
					$("#deptName").addClass("inputError");
					return false;
				}
			}	
			
			//FormData에 input값 넣기
			gfnFormDataAutoValue('prs3000Form',fd);
			
			// 수정 전 유효성 체크
			if(!gfnSaveInputValChk(arrChkObj)){  console.log(771234);
				return false;	
			}
			
			// 수정 전 연락처 유효성 검사
			if(!gfnSaveInputValChk(saveObjectValid)){
				return false;	
			}
			
			// 개인정보 수정
			fnUpdatePrs3000();

		});
		

		/* 비밀번호 입력  이벤트 */
		$('#usrPw').keyup(function(e) {
			$(".pw_txt_message").html("비밀번호 변경 시 비밀번호 확인란에 비밀번호를 입력해야합니다." );
		});
		
	});


	/**
	 * 	사용자 개인정보 상세조회 조회
	 */
	function fnSelectPrs3000Info(){
	
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/prs/prs3000/prs3000/selectPrs3000UsrInfoAjxa.do'/>"}
				,{ "usrId" : "${param.loginUsrId}" });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);

	    	// 사용자의 수정 전 이메일
			beforeEmail = data.usrInfo.email;
			
			var prjId = data.usrInfo.prjId;
			// 수정 전의 프로젝트 ID를 hidden에 세팅
			$("#beforePrjId").val(prjId);
			
			var prjNm = data.usrInfo.prjNm;
			// 수정 전의 프로젝트 명을 hidden에 세팅
			$("#beforePrjNm").val(prjNm);

			var deptId = data.usrInfo.deptId;
			// 수정전의 조직 ID를 hidden에 세팅
			$("#beforeDeptId").val(deptId);
			
			var deptNm = data.usrInfo.deptName;
			// 수정전의 조직명을 hidden에 세팅
			$("#beforeDeptNm").val(deptNm);
			
			// 사용자 이미지 ID
			var usrImgId = data.usrInfo.usrImgId;
			
			if(!gfnIsNull(usrImgId)){
				// 사용자 이미지가 있다면 이미지를 보여줌
				$("#usrImg_not_exist").hide();
				var imgUrl = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+usrImgId;
				$("#usrImg_exist").attr("src", imgUrl);
				$("#usrImg_exist").show();
			}
			else{
				$("#usrImg_exist").hide();
				$("#usrImg_not_exist").show();
			}
			
	    	//디테일폼 세팅
	    	gfnSetData2ParentObj(data.usrInfo, "prs3000Form");
	
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
	} 


	/**
	 * 개인정보 수정 화면(이메일 체크) AJAX
	 */
	function fnCheckRepetition(){
		
		// 현재 입력된 사용자의 이메일
		var usrEmail = $('#email').val();
		
		/* 이메일 필수값 체크 */
		var strFormId = "prs3000Form";
		var usrEmailChk = ["email"];
		var usrEmailChkNm = ["이메일"];
		
		if(gfnRequireCheck(strFormId, usrEmailChk, usrEmailChkNm)){
			return false;	
		}
		
		// 이메일 정규식
		var pattern =/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		
		if(usrEmail.length<6 || !pattern.test(usrEmail) ) {
			$("#email").addClass("inputError");
			toast.push("이메일 형식이 아닙니다.");
		}else if(usrEmail == beforeEmail){
			//현재 사용자의 이메일과 동일한 경우 중복체크 안함
			toast.push("현재 사용자의 이메일입니다.");
		}else{
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/prs/prs3000/prs3000/selectPrs3000emailChRepAjax.do'/>"}
					,{ "email":usrEmail });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
	        	if(data.errorChk == 'Y'){
	        		toast.push(data.message);
	        		// 이메일 중복
	        		$("#email").addClass("inputError");
	        		$("#email").attr("readonly",false);
	        		emailCheckFlag = false;
	        	}else if(data.errorChk == 'N'){
	        		toast.push(data.message);
	        		// 중복된 이메일 없음
	        		$("#email").removeClass("inputError");
	        		$("#email").attr("readonly",true);
	        		emailCheckFlag = true;
	        	}

			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
				
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
			        	$("#email").attr("readonly",false);
			        	emailCheckFlag = false;
					}
				});
			});
			//AJAX 전송
			ajaxObj.send();
		}
	}
	
	/****** 비밀번호 유효성 체크 ********/
	
	/**
	 * 	사용자 비밀번호 유효성 체크
	 *	@param usrId 사용자 아이디
	 *	@param usrPw 사용자 비밀번호
	 *  @returns 결과(boolean)
	 */
	function fnUsrPwValidationChk(usrId, usrPw){
		
		// 비밀번호 유효성체크 정규식
		var pwRegx = /^(?=.{9,})(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]).*$/;
		
		//유효성 체크
		var saveObjectValid = {  
					"usrPw":{"type":"regExp","pattern":pwRegx ,"msg":"비밀번호는 9자 이상 영문 대소문자,숫자,특수문자를 조합해야 합니다.", "required":true}
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
	
	/****** 비밀번호 유효성 체크 끝 *******/
	
	
	/**
	 * 	수정 전 비밀번호 조회
	 */
	function fnUserBeforePw(usrId){
		
		var befroePw = "";
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/prs/prs3000/prs3000/selectPrs3000PwCheckAjxa.do'/>"
							,"loadingShow":false}
							,{"usrId" : usrId});
		
		ajaxObj.async = false;
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			// 에러가 없을경우
			if( data.errorYN == "N"){
				befroePw = data.beforePw;
			}else{
				jAlert(data.message,"알림창");
			}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
		
		return befroePw;
	}
	
	/**
	 * 개인정보 수정(UPDATE) AJAX
	 */
	function fnUpdatePrs3000(){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/prs/prs3000/prs3000/updatePrs3000.do'/>"
					,"contentType":false
					,"processData":false
					,"cache":false}
				,fd);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			if(data.fileError == 'Y'){
        		jAlert(data.message,"알림창");
        		return;
        	}else{
	        	data = JSON.parse(data);
	        	jAlert(data.message,"알림창");
	        	setTimeout("location.reload()", 500);	
        	}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		//AJAX 전송
		ajaxObj.send();
	}
	
	
</script>



<div class="main_contents prs3000_contents">
	<div class="prs_title">${sessionScope.selMenuNm }</div>
	<div class="info_table">
		<form id="prs3000Form" name="prs3000Form" method="post">
				<input type="hidden"  id="usrImgId" name="usrImgId"  value="" />
				<input type="hidden"  id="prjId" name="prjId"  value="" />
				<input type ="hidden" id="deptId" name="deptId" value="" />
				<!-- 기존 정보와 수정된 정보를 비교하기 위한 hidden -->
				<input type="hidden"  id="beforePrjId" name="beforePrjId"  value="" />
				<input type ="hidden" id="beforePrjNm" name="beforePrjNm" value="" />
				<input type ="hidden" id="beforeDeptId" name="beforeDeptId" value="" />
				<input type ="hidden" id="beforeDeptNm" name="beforeDeptNm" value="" />
			
				<div class="top_content_wrap">
				
					<!-- 사용자 프로필 -->
					<div class="info_div content_top_left" style="height: 340px;">
					
						<table>
							<tbody>
								<tr>
									<td colspan="2" style="height: 70px;">
										<span class="content_title"><i class="far fa-address-card"></i> 사용자 프로필</span>
									</td>
								</tr>
								<tr>
									<!-- 사진 -->
									<td rowspan="3">
										<div class="up_box">
										<div class="prs_usrImgDiv" onclick="document.getElementById('egovFileUpload').click();" style="cursor:pointer; text-align: center;">
											<img src="" id="usrImg_exist" class="user_img" style="display:none"/>
											<img alt="사용자 사진" src="/images/contents/sample.png" id="usrImg_not_exist" class="user_img" style="display:none">
											<!-- <span class="prs_btn">(최대 60×60 픽셀)</span>  -->
										</div>
										</div>
									</td>
									<!-- 아이디 -->
									<td style="height: 30px;">
										<div style="width: 100%;">
											<div class="box_1 box_line profile_div">아이디</div>
											<div class="input_box_1 read_only profile_input_div">
												<input type="text" title="사용자 입력창" class="input_id" readonly="readonly" id="usrId" name="usrId" value="">
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td style="width: 70%; height: 30px;">
										<div style="width: 100%;">
											<div class="box_1 box_line profile_div">성명<span class="required_info">&nbsp;*</span></div>
											<div class="input_box_1 profile_input_div">
												<input type="text" title="사용자 입력창" class="input_nm" id="usrNm" name="usrNm" value="" maxlength="14">
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td style="width: 70%; height: 30px;">
										<div style="width: 100%;">
											<div class="box_1 box_line profile_div">직책</div>
											<div class="input_box_1 profile_input_div">
												<span class="code_select">
													<select class="select_cd select_dutyCd" name="usrDutyCd" id="usrDutyCd" value=""></select>
												</span>
											</div>
										</div>
									</td>
								</tr>
								<tr style="height:60px;">
									<td style="text-align: center;">
											<span class="button_normal2 prs_btn" onclick="document.getElementById('egovFileUpload').click();" id="btn_update_fileSelect">
											<input type="file" style="display: none" id="egovFileUpload" name="uploadFileList" data-user/> 파일선택
											</span>
											<br>
										
									</td>
									<td style="width: 70%; height: 30px;">
										<div style="width: 100%;">
											<div class="box_1 box_line profile_div">직급</div>
											<div class="input_box_1 profile_input_div">
												<span class="code_select">
													<select class="select_cd select_positionCd" name="usrPositionCd" id="usrPositionCd" value=""></select>
												</span>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<!-- 업로드된 파일명, 파일 사이즈 -->
										<div id="egovFileStatus" class="uploadOverFlow prsDiv"></div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					
					<!-- 연락처, 이메일 -->
					<div class="info_div content_top_right" style="height: 340px;">
						<table>
							<tbody>
								<tr>
									<td style="height: 70px;">
										<span class="content_title"><i class="fas fa-tty"></i> 연락처</span>
									</td>
								</tr>
								<tr style="height: 60px;">
									<td >
										<div style="width: 100%;">
											<div class="box_1 box_line address_div">연락처<span class="required_info">&nbsp;*</span></div>
											<div class="input_box_1 address_input_div">
												<input type="text" title="사용자 연락처" class="input_nm" id="telno" name="telno" value="" maxlength="11">
											</div>
										</div>
									</td>
								</tr>
								<tr style="height: 60px;">
									<td>
										<div style="width: 100%;">
											<div class="box_1 box_line address_div">이메일<span class="required_info">&nbsp;*</span></div>
											<div class="input_box_1 address_input_div">
												<input type="text" title="사용자 이메일" class="input_nm" id="email" name="email" value="" maxlength="48">
												<div class="button_check check_btn" id="btn_search_checkRepetition">중복확인</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
									</td>
								</tr>
							</tbody>
						</table>
						
					</div>
				</div>
				
				
				<div class="bottom_content_wrap">
					<!-- 비밀번호, 비밀번호 확인-->
					<div class="info_div content_bottom_left" style="height: 340px;">
						<table>
							<tbody>
								<tr>
									<td style="height: 70px;">
										<span class="content_title"><i class="fab fa-expeditedssl"></i> 비밀번호</span>
									</td>
								</tr>
								<tr style="height: 60px;">
									<td >
										<div style="width: 100%;">
											<div class="box_1 box_line password_div">비밀번호<span class="required_info">&nbsp;*</span></div>
											<div class="input_box_1 password_input_div">
												<input type="password" title="비밀번호 입력창" class="input_nm" id="usrPw" name="usrPw" value="" maxlength="50">
											</div>
										</div>
									</td>
								</tr>
								<tr style="height: 60px;">
									<td>
										<div style="width: 100%;">
											<div class="box_1 box_line password_div">비밀번호 확인</div>
											<div class="input_box_1 password_input_div">
												<input type="password" title="비밀번호 수정 시 비밀번호 확인에 입력해야 합니다." class="input_nm" id="reUsrPw" name="reUsrPw" value="" maxlength="50">
												</br></br><span class="pw_txt_message"></span>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
									</td>
								</tr>
							</tbody>
						</table>
					
					</div>
					
					<!-- 기타정보 - 프로젝트, 소속, 비고 -->
					<div class="info_div content_bottom_right" style="height: 340px;">
						<table>
							<tbody>
								<tr >
									<td style="height: 70px;">
										<span class="content_title"><i class="fas fa-clipboard-list"></i> 기타정보</span>
									</td>
								</tr>
								<tr style="height: 60px;">
									<td >
										<div style="width: 100%;">
											<div class="box_1 box_line ect_info_div">메인 프로젝트<span class="required_info">&nbsp;*</span></div>
											<div class="input_box_1 ect_info_input_div">
												<input type="text" title="메인 프로젝트 선택" class="input_nm" id="prjNm" name="prjNm" value="" maxlength="200">
												<div class="button_check search_btn" id="btn_search_project">검색</div>
											</div>
										</div>
									</td>
								</tr>
								<tr style="height: 60px;">
									<td>
										<div style="width: 100%;">
											<div class="box_1 box_line ect_info_div">소속<span class="required_info">&nbsp;*</span></div>
											<div class="input_box_1 ect_info_input_div">
												<input type="text" title="소속 선택" class="input_nm" id="deptName" name="deptName" value="" maxlength="200">
												<div class="button_check search_btn" id="btn_search_dept">소속검색</div>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td style="height: 152px;">
										<div style="width: 100%; height:100%;">
											<div class="box_1 box_line ect_info_div">비고</div>
											<div class="input_box_1 etc_div">
												<!-- <textarea class="input_note" title="비고" id="etc" name="etc" style="height:120px;" maxlength="2000" ></textarea> -->
												<textarea class="input_note" title="비고" id="etc" name="etc" style="height:120px;" maxlength="2000" ></textarea>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
									</td>
								</tr>
							</tbody>
						</table>
					
					</div>
				</div>
			<div class="btn_ok_div">
				<div class="button_complete" style="font-size: 1.2em;" id="btn_update">수정완료</div>
			</div>
			<div class="pop_file" ></div>
		</form>
	</div>
		<script type="text/javascript">
			//첨부파일 업로드 설정 (Select)
		   var maxFileNum = 1;
		   var multi_selector = new MultiSelector( document.getElementById( 'egovFileStatus' ), maxFileNum );
		   multi_selector.addElement( document.getElementById( 'egovFileUpload' ) );
		</script>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />