<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style>
/* .popup { display: block; width: 859px; height: 812px; background: #fff; border: 1px solid #4b73eb; font-size: 1em; z-index: 2; } */
 .popup { display: block; width: 859px; height: 891px; background: #fff; border: 1px solid #4b73eb; font-size: 1em; z-index: 2; }
	.pop_title { width: 100%; height: 40px; padding: 11px 15px; font-size: 1em; color: #fff; background: #4b73eb; }
	.pop_sub { width: 100%; height: 800px; padding: 30px 20px;} /* 높이 770*/
	.del_box span { display: inline-block; float: left; }
	.up_box span { display: inline-block; float: right;  }
		input[type=text].input_txt { float: left; width: calc(100% - 180px); height: 28px; margin-right: 5px; border: 1px solid #ccc; }
		.del_btn,
		.up_btn { width: 71px; height: 28px;  }
		.del_btn:hover,
		.up_btn:hover { background: #4b73eb; color: #fff; border: 1px solid #4b73eb; }
	.pop_table { display: inline-block; width: 100%; height: 460px; text-align: center; margin: 10px 0 13px 0; overflow: hidden; }
		.search_table { width: 100%; table-layout: fixed; }
		.search_thead { height: 35px; color: #fff; background: #4b73eb; vertical-align: middle; border-bottom: 1px solid #ccc; }
		.search_td { padding: 0 15px; height: 35px; vertical-align: middle; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; border-bottom: 1px solid #ccc; }
			.std1 { width: 7% !important; }
			.std2 { width: 13% !important; }
			.std3 { width: 15% !important; }
			.std4 { width: 8% !important; }
			.std5 { width: 8% !important; }
			.std6 { width: 10% !important; }
			.std7 { width: 15% !important; }
			.std8 { width: 10% !important; }
			.std9 { width: 14% !important; }
			.st-head { padding: 0 !important; }
			.pop_table .st-body-scroll { height: 358px; }
			.search_table tbody tr:nth-child(2n-1) { background: #fff; }
			.search_table tbody tr:nth-child(2n) { background: #f9f9f9; }
	.btn4103_box { width: 100%; text-align: center; }
		.btn4103_box .btn_save,
		.btn4103_box .btn_exit { display: inline-block; background: #fff; color: #000; border: 1px solid #ccc; font-weight: bold; font-size: 1em;}
		/* .btn4103_box .btn_save { background: #4b73eb; color: #fff; border: 1px solid #4b73eb; } */
.information{height: 240px;border: 1px solid #ccc;margin-top: 13px;padding: 5px;}
.information > p{text-align:left;}
.information > p > span{display: block;line-height: 25px;}
/* 체크박스 */
input[type="checkbox"] + label:before { margin: 0 !important; }
.std1 { position: relative; text-overflow: clip; }
.std1 input[type="checkbox"] { position: absolute; top: 7px; left: 16px; width: 18px; height: 18px; opacity: 0; z-index: -2; } /* 실제 체크박스는 숨김 */
.std1 input[type="checkbox"]+label { display: inline-block; width: 18px; height: 18px; background: url(/images/contents/normal_check.png) no-repeat; font-size: 1em; line-height: 24px; vertical-align: middle; cursor: pointer; z-index: 1; }
.std1 input[type="checkbox"]:checked+label { display: inline-block; width: 18px; height: 18px; background: url('/images/contents/normal_check_on.png') no-repeat; line-height: 24px; vertical-align: middle; cursor: pointer; }
.dataListInfo{ padding-left:1%; font-size: 10px; line-height:25px; font-weight: bold; color:blue; }
.data_sel_fail_msg {width: 100%;margin: 2px 0;border: 1px solid #ccc;border-left: 5px solid #f8d2cb;height: 95px;padding: 2px 2px 2px 5px;text-align: left;overflow-y: auto;line-height: 15px;}
.grid-cell-red{background: #f8d2cb;}
/* 팝업창 위치 */
.ui-draggable{ top: -3px;}
span#fileTextSpan {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}
</style>

<script>

	// 접수유형  
	var acceptReqTypeArr = [ '01', '02', '03', '04', '05' ];
	// 요청자 정보 직접입력여부 값
	var reqInputTypeArr = [ 'Y', 'N' ]; 
	
	var isValid = function(actionType){
		var isReturn = false;
		if(actionType=="fileUpload"){
			if($('#file').val()==""){
				jAlert('엑셀 파일을 첨부 하지 않았습니다.', '알림창');
				isReturn = true;
			} 
		}
		return isReturn;
	};
	
	var callBack = function(data){
		data = JSON.parse(data); 
		if(gfnIsNull(data.parseList)){
			alert("업로드 할 수 없는 내용이 포함 되어있습니다.(빈 값 혹은 잘못된 값)");
			return;
		}

		fnBind(data.parseList);
		
		/* 메뉴 삭제 버튼 */
		$("#btn_delete_req").click(function(){
			$('tbody input[type=checkbox]:checked').parent().parent().remove();
		});
	};
	
	
	/**
	 * function명 	: isExtend [첨부파일의 확장명 체크 ]
	 * function설명	: 첨부파일의 확장명 체크  
	 * @param fileId		:	첨부 엘레먼트 ID
	 * @param extendRule    :	유효한 확장명 ex txt|jpg|xlsx

	 */
	var isExtend = function(fileId,extendRule){
		var fileName=$('#'+fileId).val();
		var isValid = false;
		
		var ext = fileName.substr(fileName.lastIndexOf(".")+1);
		
		var exts=extendRule.split('|');
		
		for(var i=0; i<exts.length; i++){
			if(exts[i].toLowerCase()==ext.toLowerCase()){
				isValid=true;		
			}
		}
		if(!isValid){
			alert("유효하지 않은 첨부파일입니다.");
			return true;
			
		}
		return !isValid;
	}
	 
	function fnStringLen(str,max){
		//문자열 길이 확인
	     if(!gfnIsNull(str)){
	     	//Byte값 저장 변수
	     	var charByte = 0;
	     	//실제 길이 변수
	         var len = 0;
	         for (i = 0; i < str.length; i++) {
	             var oneChar = str.charAt(i);
	             if (escape(oneChar).length > 4) {
	             	charByte += 2;
	             } else {
	             	charByte++;
	             }
	             if (charByte <= max) {
	             	len = i+1;
	             }
	         }
	         if(charByte > max){
	           //문자열 길이 오버된 경우 최대 문자 갯수 까지 자르기
	           return str.substring(0,len);
	         }else{
	        	 return str;
	         }
	     }
		
	}

	
	/**
	 * function명 	: fnBind [JsonList를 table Tbody에 bind한다 ]
	 * function설명	: JsonArray의 Json Property 의 값을 template Area의 {property}를 치환하여 Table에 bind한다.   
	 * @param dataList		:	Json ArrayList ex [  { a : xx , b: yy....   },{ ....  }        ]
	 * @param templateId    :	Tbody 에 Set될 tr의 디자인 row {propery} 치환
	 * @param gridId		:	Tbody의 Id
	 */
	var fnBind = function(dataList) {
		 
		//그리드 초기화
		insertGrid.setData([]);
		 
		var realDataList = 0;

		$.each(dataList, function(idx,map) {
			//조건 검색 후 실패 스위치
			var failChk = false;
			//조건 검색 후 실패 메시지
			var failMsg = "";
			//유효하지 않은 데이터 Flag
			var invalidCheck = false;
			
			/** 입력 값 유효성 체크 시작 **/
			
			/** 입력값 공백 제거 **/
			map.reqNewType = map.reqNewType.trim(); 
			map.reqNo = map.reqNo.trim();
			map.reqNm = map.reqNm.trim();
			map.reqDesc = map.reqDesc.trim();
			map.reqDtm = map.reqDtm.trim();
			map.reqInputType = map.reqInputType.trim();
			map.reqUsrId = map.reqUsrId.trim();
			map.reqUsrNm = map.reqUsrNm.trim();
			map.reqUsrDeptNm = map.reqUsrDeptNm.trim();
			map.reqUsrNum = map.reqUsrNum.trim();
			map.reqUsrEmail = map.reqUsrEmail.trim();
			map.reqUsrPositionNm = map.reqUsrPositionNm.trim();
			map.reqUsrDutyNm = map.reqUsrDutyNm.trim();
			
			var reqNewType 	 = map["reqNewType"];		// 접수유형
			var reqNo 		 = map["reqNo"];			// 공문번호
			var reqNm 		 = map["reqNm"];			// 요구사항 명
			var reqDesc 	 = map["reqDesc"];			// 요구사항 설명
			var reqDtm 		 = map["reqDtm"];			// 요청일자
			var reqInputType = map["reqInputType"];		// 요청자 정보 직접입력 여부
			var reqUsrId 	 = map["reqUsrId"];			// 요청자 ID
			var reqUsrNm 	 = map["reqUsrNm"];			// 요청자명
			var reqUsrDeptNm = map["reqUsrDeptNm"];		// 요청자 소속명
			var reqUsrNum 	 = map["reqUsrNum"];		// 요청자 연락처
			var reqUsrEmail  = map["reqUsrEmail"];		// 요청자명
			var reqUsrPositionNm  = map["reqUsrPositionNm"];	// 직급
			var reqUsrDutyNm  = map["reqUsrDutyNm"];			// 직책
			
			/** 접수유형 **/
			if( !gfnIsNull(reqNewType) ){
				if(gfnStrByteLen(reqNewType) > 2){	// 코드값 숫자2자리
					failChk = true;
					failMsg += "접수유형 글자 수가 최대치(2 Byte)를 초과했습니다.<br>";
				}
				if( acceptReqTypeArr.indexOf(reqNewType) < 0){
					failChk = true;
					failMsg += "접수유형은 01, 02, 03, 04, 05 값만 입력가능합니다.<br>";
				}
			}else{
				failChk = true;
				failMsg += "접수유형은 필수 입력값 입니다.<br>";
			}
			
			/** 접수유형이 공문(03)일 경우 **/
			if( !gfnIsNull(reqNewType) && reqNewType == "03"){
				// 공문번호 유효성체크
				if( !gfnIsNull(reqNo) ){
					if( gfnStrByteLen(reqNo) > 100 ){
						failChk = true;
						failMsg += "공문번호 글자 수가 최대치(100Byte)를 초과했습니다.<br>";
					}
				}else{
					failChk = true;
					failMsg += "접수유형이 공문일 경우 공문번호는 필수 입력값 입니다.<br>";
				}
			}
			else{
				// 접수유형이 공문이 아닌데, 공문번호를 입력했을 경우
				if(!gfnIsNull(map["reqNo"])){
					failChk = true;
					failMsg += "접수유형이 공문일 경우에만 공문번호를 입력해야 합니다.<br>";
				}
			}
			
			
			/** 요구사항명 **/
			if( !gfnIsNull(reqNm) ){
				if( gfnStrByteLen(reqNm) > 500 ){
					failChk = true;
					failMsg += "요구사항 명 글자 수가 최대치(500Byte)를 초과했습니다.<br>";
				}
			}else{
				failChk = true;
				failMsg += "요구사항 명은 필수 입력값 입니다.<br>";
			}
			
			/** 요구사항 설명 **/
			if( !gfnIsNull(reqDesc) ){
				if( gfnStrByteLen(reqDesc) > 4000 ){
					failChk = true;
					failMsg += "요구사항 설명 글자 수가 최대치(4000Byte)를 초과했습니다.<br>";
				}
			}else{
				failChk = true;
				failMsg += "요구사항 설명은 필수 입력값 입니다.<br>";
			}
			
			/** 요청 일자 **/
			if( !gfnIsNull(reqDtm) ){
				
				var year = reqDtm.substr(0,4);
				var month = reqDtm.substr(4,2);
				var day = reqDtm.substr(6,2);
				
				var ReqDtm = new Date(year, month-1, day);
				// 말일 체크
				var lastDay = new Date(year, month, 0);

				// 숫자입력 체크 정규식
				var pattern = /^[0-9]{8}$/;

				if( pattern.test(reqDtm) == false ){
					failChk = true; 
					failMsg += "요청일자는 8자리 숫자만 입력가능 하며, YYYYMMDD 형식으로 입력해야 합니다. (예)20180101 <br>";
				}
				else if(ReqDtm == "Invalid Date"){
					failChk = true; 
					failMsg += "요청일자는 YYYYMMDD 형식으로 입력해야 합니다. (예)20180101 <br>";
				}
				
				//월 유효성
				if(parseInt(month) < 1 || parseInt(month) > 12){
					failChk = true; 
					failMsg += "월은 1~12 사이의 값을 입력해야 합니다. <br>";
				}
				// 말일체크 
				if( parseInt(lastDay.getDate()) < parseInt(day) ){
					failChk = true; 
					failMsg += month+"월의 일자는 1~"+lastDay.getDate()+" 사이의 값을 입력해야 합니다. <br>";
				}

			}else{
				failChk = true;
				failMsg += "요청일자는 필수 입력값 입니다.<br>";
			}
			
			/** 요청자 정보 직접입력 여부 **/
			if( !gfnIsNull(reqInputType) ){
				if( gfnStrByteLen(reqInputType) > 1 ){
					failChk = true;
					failMsg += "요청자 정보 직접입력 여부 값은 최대 1 글자입니다.<br>";
				}

				reqInputType = reqInputType.toUpperCase();
				
				if( reqInputTypeArr.indexOf(reqInputType) < 0){
					failChk = true;
					failMsg += "요청자 정보 직접입력 여부값은 대소문자 구분없이 Y, N만 입력가능합니다. <br>";
				}
				
				// 접수유형이 게시판이고, 직접입력 여부가 N일경우
				if( reqNewType == "05" && reqInputType == "N" ){
					failChk = true;
					failMsg += "접수유형이 게시판일 경우 요청자 정보 직접입력 여부값은 Y만 입력가능합니다. <br>";
				}
				
			}else{
				failChk = true;
				failMsg += "요청자 정보 직접입력 여부값은 필수 입력값 입니다.<br>";
			}

			
			/* 
			 * 접수유형이 게시판이거나, 직접입력여부가 Y일 경우 
			 * 요청자 ID입력 불필요, 요청자명, 소속, 이메일, 연락처 필수 입력
			 */
			if(reqNewType == "05" || reqInputType == "Y"){
				
				// 요청자 ID 입력 불필요
				if( !gfnIsNull(reqUsrId) ){
					failChk = true;
					failMsg += "접수유형이 게시판 이거나 요청자 정보 직접입력 시, 요청자 ID는 입력할 필요가 없습니다.<br>";
				}

				// 요청자명
				if( !gfnIsNull(reqUsrNm) ){
					if( gfnStrByteLen(reqUsrNm) > 200){
						failChk = true;
						failMsg += "요청자 명 글자 수가 최대치(200Byte)를 초과했습니다.<br>";
					}
				}else{
					failChk = true;
					failMsg += "접수유형이 게시판 이거나 요청자 정보 직접입력 시, 요청자 명은 필수 입력값 입니다.<br>";
				}
				
				// 요청자 소속명
				if( !gfnIsNull(reqUsrDeptNm) ){
					if( gfnStrByteLen(reqUsrDeptNm) > 500){
						failChk = true;
						failMsg += "요청자 소속명 글자 수가 최대치(500Byte)를 초과했습니다.<br>";
					}
				}else{
					failChk = true;
					failMsg += "접수유형이 게시판 이거나 요청자 정보 직접입력 시, 요청자 소속명은 필수 입력값 입니다.<br>";
				}
				
				// 요청자 이메일
				if( !gfnIsNull(reqUsrEmail) ){
					
					// 이메일 정규식
					var pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
					
					if( gfnStrByteLen(reqUsrEmail) > 100){
						failChk = true;
						failMsg += "요청자 이메일 글자 수가 최대치(100Byte)를 초과했습니다.<br>";
					}
					if( pattern.test(reqUsrEmail) == false ){
	                	failChk = true;
						failMsg += "이메일 형식이 맞지 않습니다. 예) mymail@gmail.com <br>";
	                }
				}else{
					failChk = true;
					failMsg += "접수유형이 게시판 이거나 요청자 정보 직접입력 시, 이메일은 필수 입력값 입니다.<br>";
				}
				
				// 요청자 연락처
				if( !gfnIsNull(reqUsrNum) ){
					// 숫자 정규식
					var pattern = /^[0-9]*$/;
					
					if( reqUsrNum.length < 9 || reqUsrNum.length > 11){
	                	failChk = true;
						failMsg += "연락처는 9~11자로 입력해야 합니다. 예) 01012341234 <br>";
	                }
					
					if( pattern.test(reqUsrNum) == false ){
	                	failChk = true;
						failMsg += "연락처 형식이 맞지 않습니다. 숫자만 입력해야 합니다. 예) 01012341234 <br>";
	                }
				}else{
					failChk = true;
					failMsg += "접수유형이 게시판 이거나 요청자 정보 직접입력 시, 요청자 연락처는 필수 입력값 입니다.<br>";
				}
				
				// 요청자 직급 - 필수입력값이 아니므로 필수입력 체크하지 않음
				if( !gfnIsNull(reqUsrPositionNm) ){
					if( gfnStrByteLen(reqUsrPositionNm) > 500){
						failChk = true;
						failMsg += "요청자 직급의 글자 수가 최대치(500Byte)를 초과했습니다.<br>";
					}
				}
				
				// 요청자 칙책 - 필수입력값이 아니므로 필수입력 체크하지 않음
				if( !gfnIsNull(reqUsrDutyNm) ){
					if( gfnStrByteLen(reqUsrDutyNm) > 500){
						failChk = true;
						failMsg += "요청자 직책의 글자 수가 최대치(500Byte)를 초과했습니다.<br>";
					}
				}
			}
			// end 직접입력여부가 Y일 경우
			
			
			/* 
			 * 접수유형이 게시판이 아니고, 직접입력여부가 N일 경우 
			 * 요청자 ID 필수 입력, 요청자명, 소속 입력 불필요 
			 * 이메일, 연락처 선택입력
			 */
			if(reqNewType != "05" && reqInputType == "N"){
				
				// 요청자 ID
				if( !gfnIsNull(reqUsrId) ){
					if( gfnStrByteLen(reqUsrId) > 20){
						failChk = true;
						failMsg += "요청자 ID 글자 수가 최대치(20Byte)를 초과했습니다.<br>";
					}
				}else{
					failChk = true;
					failMsg += "요청자 ID는 필수 입력값 입니다.<br>";
				}
				
				// 요청자명
				if( !gfnIsNull(reqUsrNm) ){
					failChk = true;
					failMsg += "요청자 정보 직접 입력하지 않을 시, 요청자 명은 입력할 필요가 없습니다.<br>";
				}
				
				// 소속
				if( !gfnIsNull(reqUsrDeptNm) ){
					failChk = true;
					failMsg += "요청자 정보 직접 입력하지 않을 시, 요청자 소속명은 입력할 필요가 없습니다.<br>";
				}
				
				// 이메일 - 선택입력
				if( !gfnIsNull(reqUsrEmail) ){
					
					// 이메일 정규식
					var pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
					
					if( gfnStrByteLen(reqUsrEmail) > 100){
						failChk = true;
						failMsg += "요청자 이메일 글자 수가 최대치(100Byte)를 초과했습니다.<br>";
					}
					if( pattern.test(reqUsrEmail) == false ){
	                	failChk = true;
						failMsg += "이메일 형식이 맞지 않습니다. 예) mymail@gmail.com <br>";
	                }
				}
				
				// 요청자 연락처
				if( !gfnIsNull(reqUsrNum) ){
					// 숫자 정규식
					var pattern = /^[0-9]*$/;
					
					if( reqUsrNum.length < 9 || reqUsrNum.length > 11){
	                	failChk = true;
						failMsg += "연락처는 9~11자로 입력해야 합니다. 예) 01012341234 <br>";
	                }
					
					if( pattern.test(reqUsrNum) == false ){
	                	failChk = true;
						failMsg += "연락처 형식이 맞지 않습니다. 숫자만 입력해야 합니다. 예) 01012341234 <br>";
	                }
				}
				
				// 직급 입력 체크
				if( !gfnIsNull(reqUsrPositionNm) ){
					failChk = true;
					failMsg += "요청자 정보 직접 입력하지 않을 시, 직급은 입력할 필요가 없습니다.<br>";
				}
				
				// 직책 입력체크 
				if( !gfnIsNull(reqUsrDutyNm) ){
					failChk = true;
					failMsg += "요청자 정보 직접 입력하지 않을 시, 직책은 입력할 필요가 없습니다.<br>";
				}
			}
			// end 직접입력여부가 N일 경우
			 	
			/** 입력 값 유효성 체크 종료 **/
			
			//유효하지 않은 데이터인경우 flag처리
			if(failChk){
				map.failRow = "Y";
				map.failMsg = failMsg;
			}else{
				map.failRow = "N";
				map.failMsg = "";
				
				//에러 없는 경우 유효한 데이터 +1
				realDataList++;
			}
			//그리드 데이터 추가
			insertGrid.addRow($.extend({}, map, {__index: undefined}));
        });
		
		// 요청자 ID 체크 - 입력한 요청자 ID가 시스템에 등록되어 있는지 체크한다.
		fnReqUsrIdCheck();
		
		$(".dataListInfo").text('유효한 /전체 데이터 수   : ' + realDataList + " / " + dataList.length);
	};

	
	/**
	 * function명 	: fnExcelFileUpload [엑셀 parse ]
	 * function설명	: 엑셀을 첨부하여 JsonArray 형태로 반환한다   
	 * @param parseClass			:	parse 대상 vo 풀패키지명
	 * @param successCallBackFn     :	성공시 동작할 callback function
	 */
	var fnExcelFileUpload = function(successCallBackFn) {
		 
		if(isValid('fileUpload')) return false;
		
		if(isExtend('file',"xlsx|xls")) return false;
		
		if (window.FormData !== undefined) // for HTML5 browsers
		{
			var formData = new FormData();			
			formData.append('file', $('#file')[0].files[0]);
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req4000/req4100/excelUpload.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false
						,"mimeType":"multipart/form-data"}
					,formData);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(successCallBackFn);
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
	        	jAlert("엑셀파일 두번째 시트에 업로드할 요구사항 정보가 입력되어야 합니다. <br><br> 엑셀 파일을 확인해 주세요.","알림창");
		 	});
			
			//AJAX 전송
			ajaxObj.send();
	
		} 
	
	};
	
	

	$(document).ready(function() {
	
		/* 취소 */
		$('.btn_exit').click(function() {
			gfnLayerPopupClose();
		});
		
		$('.common_table').scrolltable({
			stripe: true,
			oddClass: 'odd'
		});
		
		$('#btn_insert_excelUpload').click(function() {
			fnExcelFileUpload(callBack);
		});
		
		$('#btn_insert_reqExcel').click(function() {
			
			if(insertGrid.list.length == 0){
				toast.push({body:"저장할 수 있는 데이터가 없습니다." , type:"Warning"});
				return;
			} 
			jConfirm("저장하시겠습니까?", "알림창", function( result ) {
        		if(result){
        			fnInsertAllReqInfo();
        		}
        	});
		});
		
		//그리드 호출
		fnAxGrid5View_insert();
		
	});
	
	function fnFileTextSpanVal(obj){
		var fileName = obj.value;
		// 크롬의 경우 파일 선택 창에서 취소 하면 file 정보가 사라짐
		if(!gfnIsNull(fileName)){
			document.getElementById('fileTextSpan').innerHTML= fileName.replace("C:\\fakepath\\","")+" ("+gfnByteCalculation(obj.files[0].size)+")";	
		}else{
			// 파일 정보가 사라졌을 경우 파일명 표시하는 부분 초기화
			$("#fileTextSpan").text("");
		}
	}
	
	//axisj5 그리드
	function fnAxGrid5View_insert(){
		insertGrid = new ax5.ui.grid();
	        insertGrid.setConfig({
	            target: $('[data-ax5grid="req4103-insert-grid"]'),
	            showRowSelector: true,
	            sortable:true,
	            header: {align:"center"},
	            columns: [
					{key: "reqNewType", label: "접수유형", width: 100, align: "center",
						styleClass: function () {
					    return (this.item.failRow == "Y") ? "grid-cell-red" : "";
						}}, 
					{key: "reqNo", label: "공문번호", width: 150, align: "left",
						styleClass: function () {
					    return (this.item.failRow == "Y") ? "grid-cell-red" : "";
					    }},
					{key: "reqNm", label: "요구사항 명", width: 200, align: "left",
						styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                    	}},
    				{key: "reqDesc", label: "요구사항 설명", width: 350, align: "left",
    						styleClass: function () {
                            return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                       	}},
                    {key: "reqDtm", label: "요청일자", width: 100, align: "center",
    						styleClass: function () {
                            return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                       	}},
                    {key: "reqInputType", label: "요청자 정보 직접입력 여부", width: 200, align: "center",
    						styleClass: function () {
                            return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                       	}},   	
                    {key: "reqUsrId", label: "요청자 ID", width: 100, align: "left",
    					styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                     	}},
                    {key: "reqUsrNm", label: "요청자 명", width: 100, align: "left",
        				styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                        }},
                    {key: "reqUsrDeptNm", label: "요청자 소속명", width: 200, align: "left",
            			styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                        }},
                    {key: "reqUsrEmail", label: "이메일", width: 140, align: "left",
                		styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                        }},
                    {key: "reqUsrNum", label: "연락처", width: 140, align: "left",
                    	styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                        }},
                    {key: "reqUsrPositionNm", label: "직급", width: 140, align: "left",
                    	styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                        }},
                    {key: "reqUsrDutyNm", label: "직책", width: 140, align: "left",
                    	styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                        }}
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30,
	                onClick: function () {
	                	//오류 메시지 세팅
	                	$('.data_sel_fail_msg').html('');
	                	if(this.item.failRow == "Y"){
	                		$('.data_sel_fail_msg').html(this.item.failMsg);	
	                	}
	                }
	            }
	        });
	}
	/* 메뉴 삭제 버튼 */
	$("#btn_delete_req").click(function(){
		var chkList = insertGrid.getList('selected');
		if(gfnIsNull(chkList)){
			toast.push("선택된 데이터가 없습니다.");
			return false;
		}
		
		//선택된 데이터 제거
		insertGrid.removeRow("selected");
	});
	

	// 요청자 ID체크 
	var fnReqUsrIdCheck = function(){
		
		var dataList = insertGrid.list;
		
		if(gfnIsNull(dataList)){
			toast.push("요청자 ID 체크를 위해 필요한 업로드 데이터가 없습니다.");
			return false;
		}
		
		//오류 메시지 초기화
		$('.data_sel_fail_msg').html('');
		
		//전체 그리드 데이터 루프
		$.each(dataList,function(idx,map){
		
			// 접수유형이 게시판이 아니고, 요청자 정보 직접입력여부가 N일 경우 입력된 요청자 ID 체크
			if(map.reqNewType != "05" && map.reqInputType == "N"){
				
				//요청자 ID 유무 체크 AJAX
				//AJAX 설정
				var ajaxObj = new gfnAjaxRequestAction(
						{"url":"<c:url value='/req/req4000/req4100/selectReq4100ReqUsrChk.do'/>"}
						,{InReqUsrId:map["reqUsrId"]});
				//AJAX 전송 성공 함수
				ajaxObj.setFnSuccess(function(data){
					data = JSON.parse(data);
								
					// 엑셀 파일에 입력한 요청자 아이디가 없을 경우
					if(data != null && data.chkId != "Y"){
						//오류 메시지 추가
						$('.data_sel_fail_msg').append("요청자 ID "+map.reqUsrId+"(은)는 등록되지 않은 ID 입니다.<br>");
						map.failMsg = map.failMsg+"요청자 ID "+map.reqUsrId+"(은)는 등록되지 않은 ID 입니다.<br>";

						//오류 내역 없는 경우 갱신
						if(map.failRow != "Y"){
							insertGrid.setValue(map["__index"], "failRow", "Y");
							insertGrid.updateRow(map,map["__index"]);
						}
					}
				});
				//AJAX 전송
				ajaxObj.send();
			}
		});
	}
	
	//요구사항 일괄 저장
	var fnInsertAllReqInfo = function(){
		var dataChk = true;

		//유효하지 않은 데이터 체크
		$.each(insertGrid.list,function(){

			if(this.failRow == "Y"){
				jAlert("유효하지 않은 데이터가 존재합니다.<br>수정 또는 삭제 후 다시 시도 해주세요.", '알림창');
				dataChk = false;
				return false;
			}

		});
		
		if(dataChk){
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req4000/req4100/insertReq4100ReqInfoListAjax.do'/>", "loadingShow":true}
					,{"jsonData":JSON.stringify(insertGrid.list)});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
		        
	        	//코멘트 등록 실패의 경우 리턴
		    	if(data.saveYN == 'N'){
		    		toast.push(data.message+"<br>");
		    		jAlert(data.message+"<br>- 유효하지 않은 데이터가 존재하는지 확인해주세요.", '알림창');
		    		return;
		    	}
	        	
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
						gfnLayerPopupClose();
						//그리드 새로고침
						fnInGridListSet(firstGrid.page.currentPage,mySearch.getParam());
					}
				});
	
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
				jAlert(data.message,"알림창");
			});
			
			//AJAX 전송
			ajaxObj.send();
		}
	};
</script>

<div class="popup" style="height:891px;">
	<div class="pop_title">
			양식 업로드 및 적용
	</div>	
	<div class="pop_sub">
		<form id="findFrm" name="findFrm" method="post">
			<input type="hidden" id="parseClass" name="parseClass" >
			<div class="del_box">
				<span class="button_normal2 del_btn" style="width:100px;" id="btn_delete_req" >선택 삭제</span>
				<span class="dataListInfo"> </span>
			</div>
			<div class="up_box">
				<span class="button_normal2 up_btn" id="btn_insert_excelUpload">
					<img src="/images/contents/upload_img.png" alt="업로드" style="margin-right: 5px"/>업로드
				</span>
				<span class="button_normal2 del_btn" onclick="document.getElementById('file').click();" id="btn_insert_fileSelect">
					<input type="file" name="file" id="file" style="display: none" onchange="fnFileTextSpanVal(this)" />파일선택
				</span>
				<span class="button_normal2" style="width: 500px;" id="fileTextSpan">
					
				</span>
			</div>
		</form>
		<div class="pop_table">
			<div data-ax5grid="req4103-insert-grid" data-ax5grid-config="{}" style="height: 360px;"></div>
			<div class="data_sel_fail_msg">
				선택 오류 메시지
			</div>
		</div>
		<div class="btn4103_box">
			<div class="button_complete btn_save" id="btn_insert_reqExcel" >저장</div>
			<div class="button_complete btn_exit">취소</div>
		</div>
		<div class="information">
			<p>
				<span>* 사용 방법</span>
				<span style="color:red;">- 엑셀파일은 두개의 시트가 필요합니다. 업로드할 요구사항 정보는 두번째 시트에 입력해야 합니다. </span>
				<span style="color:red;">- 접수유형, 요구사항명, 요구사항 설명, 요청일자, 요청자 정보 직접입력 여부는 필수 입력 값입니다. </span>
				<span>- 접수유형은 [ 01, 02, 03, 04, 05 ] 코드값만 입력가능합니다.</span>				
				<span>- 접수유형의 항목은 시스템(01), 유선(02), 공문(03), 자체식별(04), 게시판(05)이며 접수유형이 공문(03)일 경우 공문번호를 입력해야 합니다.</span>	
				<span>- 요청자 정보 직접입력 여부가 Y일경우 요청자명, 요청자 소속명, 이메일 연락처를 입력해야 합니다.</span>
				<span>- 요청자 정보 직접입력 여부가 N일경우 시스템에 등록된 요청자 ID를 입력해야 하면 이메일, 연락처는 선택입력 입니다.</span>						
				<span>- 요청일자 양식은 'YYYYMMDD' 형식입니다. 다른 특수문자기호를 사용하지 마십시오.</span>
				<span style="color:red;">- 행과 행사이에 빈 행이 들어갈 수 없습니다.(연속된 행 조건)</span>
			</p>
		</div>
	</div>
</div>
</html>