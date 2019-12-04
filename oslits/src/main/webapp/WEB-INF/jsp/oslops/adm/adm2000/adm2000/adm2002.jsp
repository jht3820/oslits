<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style>
.popup { display: block; width: 859px; height: 812px; background: #fff; border: 1px solid #4b73eb; font-size: 1em; z-index: 2; }
	.pop_title { width: 100%; height: 40px; padding: 11px 15px; font-size: 1em; color: #fff; background: #4b73eb; }
	.pop_sub { width: 100%; height: 770px !important; padding: 30px 20px; }
	.del_box span { display: inline-block; float: left; }
	.up_box span { display: inline-block; float: right;  }
		input[type=text].input_txt { float: left; width: calc(100% - 180px); height: 28px; margin-right: 5px; border: 1px solid #ccc; }
		.del_btn,
		.up_btn,
		.idChk_btn { width: 71px; height: 28px; }
		.idChk_btn:hover,
		.del_btn:hover,
		.up_btn:hover { background: #4b73eb; color: #fff; border: 1px solid #4b73eb; }
	
	.pop_table { display: inline-block; width: 100%; height: 460px; text-align: center;margin: 10px 0 10px 0; overflow: hidden; }
		.search_table { width: 100%; table-layout: fixed; }
		.search_thead { height: 35px; color: #fff; background: #4b73eb; vertical-align: middle; border-bottom: 1px solid #ccc; }
		.search_td { padding: 0 15px; height: 35px; vertical-align: middle; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; border-bottom: 1px solid #ccc; }
			.std1 { width: 7% !important; }
			.std2 { width: 15% !important; }
			.std3 { width: 15% !important; }
			.std4 { width: 15% !important; }
			.std5 { width: 15% !important; }
			.std6 { width: 10% !important; }
			.std7 { width: 15% !important; }
			.std8 { width: 10% !important; }
			.pop_table .st-body-scroll { height: 358px; }
			.search_table tbody tr:nth-child(2n-1) { background: #fff; }
			.search_table tbody tr:nth-child(2n) { background: #f9f9f9; }
	.btn4103_box { width: 100%; text-align: center; }
		.btn4103_box .btn_save,
		.btn4103_box .btn_exit { display: inline-block; background: #fff; color: #000; border: 1px solid #ccc; font-weight: bold; font-size: 1em;}
		/* .btn4103_box .btn_save { background: #4b73eb; color: #fff; border: 1px solid #4b73eb; } */
.information{height: 210px;border: 1px solid #ccc;margin-top: 14px;padding: 5px;}
.information > p{text-align:left;}
.information > p > span{display: block;line-height: 25px;}
/* 체크박스 */
input[type="checkbox"] + label:before { margin: 0 !important; }
.std1 { position: relative; text-overflow: clip; }
.std1 input[type="checkbox"] { position: absolute; top: 7px; left: 16px; width: 18px; height: 18px; opacity: 0; z-index: -2; } /* 실제 체크박스는 숨김 */
.std1 input[type="checkbox"]+label { display: inline-block; width: 18px; height: 18px; background: url(/images/contents/normal_check.png) no-repeat; font-size: 1em; line-height: 24px; vertical-align: middle; cursor: pointer; z-index: 1; }
.std1 input[type="checkbox"]:checked+label { display: inline-block; width: 18px; height: 18px; background: url('/images/contents/normal_check_on.png') no-repeat; line-height: 24px; vertical-align: middle; cursor: pointer; }
tbody.invalidRow{ background: rgba(255,0,0,0.6);}
.data_sel_fail_msg {width: 100%;margin: 2px 0;border: 1px solid #ccc;border-left: 5px solid #f8d2cb;height: 95px;padding: 2px 2px 2px 5px;text-align: left;overflow-y: auto;line-height: 15px;}
.grid-cell-red{background: #f8d2cb;}
span#fileTextSpan {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}
</style>

<script>

	globals_guideChkFn = fnAdm2002GuideShow;
	
	// 중복체크 여부  flag
	var chkId = false;
	var useCdArr = [ 'Y', 'N' ]; 

	var isValid = function(actionType){
		var isReturn = false;
		if(actionType=="fileUpload"){
			if($('#file').val()==""){
				jAlert('엑셀 파일을 첨부 하지 않았습니다.', '알림창', function( result ) {
					if( result ){
						isReturn = true;
					}
				});
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
		
	};
	
	var emailValid = function(value,objType){
		if(!gfnIsNull(objType.pattern)) {
    		var pattern = objType.pattern;
    	}
    	if(pattern.test(value) == false){
        	//msg있는 경우 출력
            if(!gfnIsNull(objType.msg)){
            	jAlert(objType.msg);
            	return false;
            }
    	}else{
    		return true;
    	}
	}
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
			jAlert("유효하지 않은 첨부파일입니다.","알림창");
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
	 */
	
	var fnBind = function(dataList){
		//그리드 초기화
		insertGrid.setData([]);
		
		//이메일 중복체크
		var emailExistArr = [];
		
		$.each(dataList, function(idx, map) {
			
			//아이디 빈 값인경우 skip
			//if(gfnIsNull(map["usrId"])) return;
			
			//조건 검색 후 실패 스위치
			var failChk = false;
			//조건 검색 후 실패 메시지
			var failMsg = "";
			//유효하지 않은 데이터 Flag
			var invalidCheck = false;;
			
			/** 입력값 공백 제거 **/
			map.usrId = map.usrId.trim().replace(/</gi,'&lt;').replace(/>/gi,'&gt;'); 
			map.usrNm = map.usrNm.trim().replace(/</gi,'&lt;').replace(/>/gi,'&gt;'); 
			map.telno = map.telno.trim().replace(/</gi,'&lt;').replace(/>/gi,'&gt;'); 
			map.email = map.email.trim().replace(/</gi,'&lt;').replace(/>/gi,'&gt;'); 
			map.deptId = map.deptId.trim().replace(/</gi,'&lt;').replace(/>/gi,'&gt;'); 
			map.useCd = map.useCd.trim().replace(/</gi,'&lt;').replace(/>/gi,'&gt;'); 
			map.etc = map.etc.trim().replace(/</gi,'&lt;').replace(/>/gi,'&gt;'); 
			
			var usrId = map["usrId"];		// 사용자 ID
			var usrNm = map["usrNm"];		// 사용자명
			var telno = map["telno"];		// 전화번호
			var email = map["email"];		// 이메일
			var deptId = map["deptId"];		// 조직ID
			var useCd = map["useCd"];		// 사용여부
			var etc = map["etc"];			// 비고
			
			/** 입력 값 유효성 체크 시작 **/
			//사용자 아이디
			if(!gfnIsNull(usrId)) {
				var pattern = /^[a-z0-9_-]{5,20}$/;
				if(pattern.test(usrId) == false){
					failChk = true;
					failMsg += "아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.<br>";
				}
				if(gfnStrByteLen(usrId) > 20){
					failChk = true;
					failMsg += "사용자 아이디 글자 수가 최대치(20Byte)를 초과했습니다.<br>";
				}
			}else{
				failChk = true;
				failMsg += "사용자 아이디는 필수 입력값 입니다.<br>";
			}
			
			//이름
			if(!gfnIsNull(usrNm)){
				if(gfnStrByteLen(usrNm) > 200){
					failChk = true;
					failMsg += "성명 글자 수가 최대치(200Byte)를 초과했습니다.<br>";
				}
				
				var pattern = /^[0-9a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣_-]{1,200}$/;
				if(pattern.test(usrNm) == false){
                	failChk = true;
					failMsg += "이름은 한글, 영문, 숫자, 특수문자( _ -) 만 입력가능합니다.";
                }
			}else{
				failChk = true;
				failMsg += "성명은 필수 입력값 입니다.<br>";
			}
			
			//연락처
			if(!gfnIsNull(telno)){
				var pattern = /^[0-9]*$/;
				if(telno.length < 3 || telno.length > 13){
                	failChk = true;
					failMsg += "연락처를 다시 확인해 주세요.(3~13자리) 예) 01012341234 <br>";
                }
				if(pattern.test(telno) == false){
                	failChk = true;
					failMsg += "연락처 형식이 맞지 않습니다. 숫자만 입력해 주세요. 예) 01012341234 <br>";
                }
			}else{
				failChk = true;
				failMsg += "연락처는 필수 입력값 입니다.<br>";
			}
			
			//이메일
			if(!gfnIsNull(email)){
				var pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
				if(pattern.test(email) == false){
                	failChk = true;
					failMsg += "이메일 형식이 맞지 않습니다. 예) mymail@gmail.com <br>";
                }
				if(gfnStrByteLen(email) > 50){
					failChk = true;
					failMsg += "이메일 글자 수가 최대치(50Byte)를 초과했습니다.<br>";
				}
				
				//이메일 중복 체크
				if(emailExistArr.indexOf(email) != -1){
					failChk = true;
					failMsg += "중복된 이메일입니다.<br>";
				}else{
					//유일값 넣기
					emailExistArr.push(email);
				}
			}else{
				failChk = true;
				failMsg += "이메일은 필수 입력값 입니다.<br>";
			}
			
			// 소속
			if(!gfnIsNull(deptId)){

				var pattern = /^[0-9]*$/; 
				
				//글자수가 16자리인지 확인
             	if(deptId.length != 16){
             		failChk = true;
					failMsg += "소속의 조직코드 값은 16자리 입니다.<br>";
             	}
				// DPT로 시작하는지 체크 
				if(deptId.indexOf('DPT') == -1){
					failChk = true;
					failMsg += "소속의 조직코드 값은 DPT로 시작해야 합니다.<br>";
				}
				// DPT뒤에 13자리 값이 숫자인지 체크
				if(pattern.test(deptId.substring(3)) == false){
                	failChk = true;
					failMsg += "소속의 조직코드 형식이 맞지 않습니다. 예) DPT2018080900012 <br>";
                }		
			}else{
				failChk = true;
				failMsg += "소속은 필수 입력값 입니다.<br>";
			}
			
			//사용유무
			if(!gfnIsNull(useCd)){
				//글자수가 1자리인지 확인
             	if(useCd.length == 1){
             		useCd = useCd.replace(/\"/gi,'&quot');
             	}else{
             		failChk = true;
					failMsg += "사용유무 입력 값은 최대 1 글자입니다.<br>";
             	}
				
				var strUseCd = useCd;
				
             	if( useCdArr.indexOf(strUseCd.toUpperCase()) < 0){
					failChk = true;
					failMsg += "사용유무 값은 대소문자 구분없이 Y, N만 입력가능합니다. <br>";
				}
				
			}else{
				failChk = true;
				failMsg += "사용유무는 필수 입력값 입니다.<br>";
			}
			
			//비고
			if(!gfnIsNull(etc)){
				if(gfnStrByteLen(etc) > 4000){
					failChk = true;
					failMsg += "비고 글자 수가 최대치(4000Byte)를 초과했습니다.<br>";
				}
			}
			/** 입력 값 유효성 체크 종료 **/
			
			//유효하지 않은 데이터인경우 flag처리
			if(failChk){
				map.failRow = "Y";
				map.failMsg = failMsg;
			}else{
				map.failRow = "N";
				map.failMsg = "";
			}
			
			//그리드 데이터 추가
			insertGrid.addRow($.extend({}, map, {__index: undefined}));
        });
        
		// 엑셀에 입력한 조직이 있는지 체크한다.
		fnDeptCheck();
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
					{"url":"<c:url value='/adm/adm2000/adm2000/excelUpload.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false
						,"mimeType":"multipart/form-data"}
					,formData);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(successCallBackFn);
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
	        	jAlert("엑셀파일 두번째 시트에 업로드할 사용자 정보가 입력되어야 합니다. <br><br> 엑셀 파일을 확인해 주세요.","알림창");
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
			chkId = false;
			fnExcelFileUpload(callBack);
		});
		
		$('#btn_insert_usrExcel').click(function() {
			if(insertGrid.list.length == 0){
				toast.push({body:"저장할 수 있는 데이터가 없습니다." , type:"Warning"});
				return;
			} 

			// 중복체크 여부 확인
			if(!chkId){
				jAlert("전체 아이디 중복 체크를 실행해 주세요.", '알림창');
				return false;
			}
	
			jConfirm("저장하시겠습니까?", "알림창", function( result ) {
        		if(result){
        			fnInsertAllReqInfo();
        		}
        	});
		});
		//그리드 세팅
		fnAxGrid5View_insert();
	});
	
	function fnFileTextSpanVal(obj){
		var fileName = obj.value;
		document.getElementById('fileTextSpan').innerHTML= fileName.replace("C:\\fakepath\\","")+" ("+gfnByteCalculation(obj.files[0].size)+")";	
	}
	
	// 전체 사용자 전체 체크/전체 해제 이벤트 처리
	$("#req_chk_all").click(function() {
		if ($(this).is(':checked')) {
			$('input[id^=pop_ch]').prop("checked", true);
		} else {
			$('input[id^=pop_ch]').prop("checked", false);
		}
	});
	
	//axisj5 그리드
	function fnAxGrid5View_insert(){
		insertGrid = new ax5.ui.grid();
	        insertGrid.setConfig({
	            target: $('[data-ax5grid="adm2003-insert-grid"]'),
	            showRowSelector: true,
	            sortable:true,
	            header: {align:"center"},
	            columns: [
					{key: "usrId", label: "아이디", width: 130, align: "left",
						styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                    	}},
					{key: "usrNm", label: "성명", width: 130, align: "left",
						styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                    	}},
					{key: "telno", label: "연락처", width: 130, align: "left",
						styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                    	}},
					{key: "email", label: "이메일", width: 130, align: "left",
						styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                    	}},
					{key: "deptId", label: "소속", width: 130, align: "center",
						styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                    	}},
					{key: "useCd", label: "사용유무", width: 80, align: "center",
						styleClass: function () {
                        return (this.item.failRow == "Y") ? "grid-cell-red" : "";
                    	}},
					{key: "etc", label: "비고", width: 300, align: "left",
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
	
	/* 메뉴 전체 아이디 중복 체크 */
	$("#btn_select_idchk").click(function(){
		var dataList = insertGrid.list;
		if(gfnIsNull(dataList)){
			toast.push("아이디 중복체크를 위한 업로드 데이터가 없습니다.");
			return false;
		}
		
		//오류 메시지 초기화
		$('.data_sel_fail_msg').html('');
		
		//전체 그리드 데이터 루프
		$.each(dataList,function(idx,map){
			//중복 아이디 체크 AJAX
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/cmm/cmm3000/cmm3200/selectCmm3200IdCheck.do'/>"}
					,{InUsrId:map["usrId"]});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				// 중복체크 실행 후 true세팅
				chkId = true;

					if(data != null && data.chkId != "Y"){
						//오류 메시지 추가
						$('.data_sel_fail_msg').append("사용자 아이디 "+map.usrId+"(은)는 중복 아이디 입니다.<br>");
						map.failMsg = map.failMsg+"사용자 아이디 "+map.usrId+"(은)는 중복 아이디 입니다.<br>";

						//오류 내역 없는 경우 갱신
						if(map.failRow != "Y"){
							insertGrid.setValue(map["__index"], "failRow", "Y");
							insertGrid.updateRow(map,map["__index"]);
						}
					}
			});
			//AJAX 전송
			ajaxObj.send();
			//중복 아이디 체크 끝
		});
		toast.push("아이디 중복체크를 완료하였습니다.");
	});
	
	
	// 조직 ID체크 
	var fnDeptCheck = function(){
		
		var dataList = insertGrid.list;
		
		if(gfnIsNull(dataList)){
			toast.push("조직 ID 체크를 위해 필요한 업로드 데이터가 없습니다.");
			return false;
		}
		
		//오류 메시지 초기화
		$('.data_sel_fail_msg').html('');
		
		//전체 그리드 데이터 루프
		$.each(dataList,function(idx,map){
		
			//요청자 ID 유무 체크 AJAX
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/amd7000/adm7000/selectAdm7000ExistDeptChk.do'/>"}
					,{inDeptId:map["deptId"]});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
								
				// 엑셀 파일에 입력한 소속ID가 없을 경우
				if(data != null && data.chkDeptId != "Y"){
						//오류 메시지 추가
						$('.data_sel_fail_msg').append("소속 ID "+map.deptId+"(은)는 등록되지 않은 소속입니다.<br>");
						map.failMsg = map.failMsg+"소속 ID "+map.deptId+"(은)는 등록되지 않은 소속입니다.<br>";

						//오류 내역 없는 경우 갱신
						if(map.failRow != "Y"){
							insertGrid.setValue(map["__index"], "failRow", "Y");
							insertGrid.updateRow(map,map["__index"]);
						}
					}
				});
				//AJAX 전송
				ajaxObj.send();
		});
	}
	
	//사용자 일괄 저장
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
					{"url":"<c:url value='/adm/adm2000/adm2000/insertAdm2000AdmInfoListAjax.do'/>"}
					,{"jsonData":JSON.stringify(insertGrid.list)});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
		        
		    	//코멘트 등록 실패의 경우 리턴
		    	if(data.saveYN == 'N'){
		    		toast.push(data.message+"<br>");
		    		jAlert(data.message+"<br>- 유효하지 않은 데이터가 존재하는지 확인해주세요.<br>- 전체 중복 아이디 체크를 실행했는지 확인해주세요.", '알림창');
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
	
	function fnAdm2002GuideShow(){
		var mainObj = $(".popup");
		
		//mainObj가 없는경우 false return
		if(mainObj.length == 0){
			return false;
		}
		//guide box setting
		var guideBoxInfo = globals_guideContents["adm2002"];
		gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
	}
</script>

<div class="popup">
	<div class="pop_title">양식 업로드 및 적용</div>	
	<div class="pop_sub">
		<form id="findFrm" name="findFrm" method="post">
			<input type="hidden" id="parseClass" name="parseClass" >
			<input type="hidden" id="licGrpId" name="${sessionScope.loginVO.licGrpId}" >
			<div class="del_box" guide="selectDelete"  >
				<span class="button_normal2 del_btn" style="width:100px;" id="btn_delete_req" >선택 삭제</span>
				<span class="button_normal2 idChk_btn" style="width:150px;" id="btn_select_idchk" >전체 아이디 중복 체크</span>
			</div>
			<div class="up_box" guide="selectUpload"   >
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
			<div data-ax5grid="adm2003-insert-grid" data-ax5grid-config="{}" style="height: 360px;"></div>
			<div class="data_sel_fail_msg" guide="showExcelErrorLog"  >
				선택 오류 메시지
			</div>
		</div>
		<div class="btn4103_box">
			<div class="button_complete btn_save" id="btn_insert_usrExcel" >저장</div>
			<div class="button_complete btn_exit">취소</div>
		</div>
		<div class="information">
			<p>
				<span>*사용 방법</span>
				<span style="color:red;">- 엑셀파일은 두개의 시트가 필요합니다. 업로드할 사용자 정보는 두번째 시트에 입력해야 합니다. </span>
				<span style="color:red;">- 저장시 비밀번호는 아이디와 동일하게 적용 됩니다. </span>
				<span>- 행과 행사이에 빈 행이 들어갈 수 없습니다.(연속된 행 조건)</span>
				<span>- 아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다. </span>
				<span>- 아이디, 성명, 연락처, 이메일, 소속, 사용여부는 필수 입력 값입니다.</span>
				<span>- 조직 코드값의 조직관리 화면에서 등록된 조직 목록을 엑셀 파일로 다운로드 하여 확인할 수 있습니다.</span>
				<span>- 사용여부 ( 사용=Y, 미사용=N )</span>
			</p>
		</div>
	</div>
</div>
</html>