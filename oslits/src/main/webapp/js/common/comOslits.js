/**
 * js명 			: comOslits.js
 * 설명			: oslits 솔루션에서 공통으로 사용할 Java Script를 정의한다
 * 작성자			: 정형택
 * 작성일			: 2016.01.12
 * 수정일			: 2016.01.12
 * 수정내용		: 최초생성
 * 
 */

/**
 * function명 	: gfnCommonSetting [검색 상자 세팅 용]
 * function설명	: 검색 상자에서 사용유무, 승인상태, 중요도 등 선택상자에 공통코드를 적용 할 경우 사용한다.
 * 				  공통코드 테이블을 참조하여 콤보데이터를 가지고 온다.
 * 				  사용 예제 > gfnCommonSetting(mySearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
 * @param searchObj			:	검색 정보를 가지고 있는 객체
 * @param cmmCode			:	공통코드
 * @param showSearchKey		:	SelectBox Key value
 * @param hideSearchKey		:	TextBox Key value
 * showSearchKey와 hideSearchKey가 서로 toggle된다.
 */
function gfnCommonSetting(searchObj,cmmCode,showSearchKey,hideSearchKey){
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가,OS:선택 값 selected, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 
	*/

	var mstCdStrArr = cmmCode;
	var strUseYn = 'Y';
	var arrObj = [axdom("#" + searchObj.getItemId(showSearchKey))];
	var arrComboType = ["OS"];
	
	//해당 선택상자 초기화
	axdom("#" + searchObj.getItemId(showSearchKey)).html('');
	
	//공통코드 불러오기
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	axdom("#" + searchObj.getItemId(showSearchKey)).show();
	axdom("#" + searchObj.getItemId(hideSearchKey)).hide();
}
/**
 * function명 	: gfnGetMultiCommonCodeDataForm [조회 조건 select Box 용]
 * function설명	: 트랜잭션을 여러번 날리는게 아닌 단일 트랜잭션으로 콤보 코드를 가지고 오는 용도로 사용, 콤보용 공통 코드 및 공통코드명 가져올때 사용
 * 				  공통코드 테이블을 참조하여 콤보데이터를 가지고 온다.
 * 				  사용 예제 > gfnGetMultiCommonCodeDataForm(mstCdStr, useYn, arrObj, arrComboType);
 * 				  Ex> 1. 개발 화면에서 대분류 코드를 "0210|0220|0310" 형식으로 "|" 으로 합쳐서 순서대로 보낸다.
 * 					  2. 대분류 코드를 세팅할 selectBox 객체를 배열로 대분류 코드 순서와 일치하게 세팅하여 보낸다.
 * 					  3. 사용여부가 사용인지, 미사용인지 아니면 전체를 다 가지고 올지를 판단. (N: 사용하지 않는 것만, Y: 사용하는 것만, 그외: 전체)
 *            		  4. 콤보타입을 전체, 선택, 일반 바로 선택 가능한 상태에 대한 조건을 순서대로 배열로 보낸다. ["S", "A", "E", "OS","JSON",""] S: 선택, A: 전체, E:공백추가 OS:선택 값 selected , JSON:반환 데이터를 json으로 리턴 , 그 외: 없음  
 *            			OS: 해당 select attr에 OS="01" 등과 같이 입력 -> option elements 생성 후 해당 value의 option을 selected한다.
 *            			JSON: 반환 데이터를 기타 사용 할 수 있도록 JSON OBJECT로 제공 
 * @param mstCdStr		:	대분류코드를 "|"으로 합친 문자열
 * @param useYn			:	사용여부 ( Y: 사용, N: 미사용, A: 전체 )
 * @param arrObj		:	selectBox 객체 배열
 * @param arrComboType	:	콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
 * @param isAsyncMode	:	동기, 비동기 모드( true: 비동기식 모드, false: 동기식 모드 )
 */
function gfnGetMultiCommonCodeDataForm(mstCdStr, useYn, arrObj, arrComboType , isAsyncMode){
	
	//대분류코드 배열로 '' 로 감싸서 배정
	var mstCdArr = mstCdStr.split("|"); 
	var mstCds = "";
	for(var i = 0 ; i < mstCdArr.length ; i++){
		mstCds += "'" + mstCdArr[i] + "'," ;	
	}
	mstCds = mstCds.substring(0,mstCds.length-1);
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/cmm9000/cmm9100/selectCmm9100MultiCommonCodeList.do"
				,"async":isAsyncMode}
			,{ "mstCds":mstCds, "useYn":useYn, "mstCdStr":mstCdStr });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	if(data.ERROR_CODE == '-1'){
    		jAlert(data.ERROR_MSG,'알림창');
			return;
		}

    	/* 넘겨받은 대분류코드 갯수 만큼 루프 돌며 해당 select 객체에 대입되는 소분류 항목을 세팅해준다. */
		var len = mstCdArr.length;
		
		for(var i = 0 ; i < len ; i++){
			
			var codeArr;
			var textArr;
			var strCodeData = eval("data.mstCd" + mstCdArr[i] + "code");
			var strTextData = eval("data.mstCd" + mstCdArr[i] + "text");
			
			
			if(arrComboType[i] == 'A'){
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');

				//옵션키 A 로 세팅한 전 체(검색조건용)
				arrObj[i].append("<option value='A'>전 체</option>");
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			else if(arrComboType[i] == 'N'){
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');
				
				//전체 한줄 추가
				arrObj[i].append("<option value=''>전 체</option>");
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			else if(arrComboType[i] == 'S'){
				
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');

				//선택 한줄 추가
				arrObj[i].append("<option value=''>선 택</option>");
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			else if(arrComboType[i] == 'E'){
				
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');
				
				//공백 한줄 추가
				arrObj[i].append("<option value=''></option>");
				
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
				
			}
			else if(arrComboType[i] == 'JSON'){
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');

				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].innerHTML = textArr[j];
				}
			}
			else{
				
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			
			//comboType이 OS인 경우 selected 지정
			if(arrComboType[i] == 'OS'){
					$.each(arrObj[i],function(idx,map){
						if(!gfnIsNull($(map).attr('OS'))){
							var seledObj = $(map).children('option[value='+$(map).attr('OS')+']');
							if(!gfnIsNull(seledObj)){
								$(seledObj).attr('selected','selected');
							}
						}
					});
			}
		}
		
    	//toast.push(data.menuNm + " : " + data.menuId);
		
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
			//data = JSON.parse(data);
		jAlert(xhr.statusText);;
	});
	//AJAX 전송
	ajaxObj.send();
}

/**
 * AJAX 통신 공통 처리
 * - ajax통신 옵션은 property에서 배열로 처리
 * - 로딩 바 기본(통신 완료 퍼센트)
 * - AJAX통신 중 Background처리가 있는 경우 무조건 async = true(동기) 처리  예) 메일 전송 AJAX
 * property 옵션
 * - url
 * - data
 * - dataType
 * - contentType
 * - async
 * - cache
 * - processData
 * data는 setData로 따로 설정 가능
 * 예제)
 * 1. 객체 선언과 동시에 옵션 세팅
 * var ajaxObj = new gfnAjaxRequestAction({
		"url":"<c:url value='/req/req2000/req2000/insertReq2000ReqCommentInfoAjax.do'/>"
		,"contentType":"application/x-www-form-urlencoded; charset=UTF-8"
		,"datatype":"json"
		,"async":false
		,"cache":true
		,"processData":true
		});
 * 
 * 2. 객체 선언과 이후 옵션 세팅
 * //setProperty를 여러번 나누어서 설정해도 상관 없음
 * var ajaxObj = new gfnAjaxRequestAction({
		"url":"<c:url value='/req/req2000/req2000/insertReq2000ReqCommentInfoAjax.do'/>"
		});
	ajaxObj.setProperty({
		"contentType":"application/x-www-form-urlencoded; charset=UTF-8"
		,"datatype":"json"
		,"async":false
		,"cache":true
		,"processData":true
	});
 * 
 * 3. data 설정
 * ajaxObj.setData({"prjId" : prjId, "reqId" : reqId, "reqCmnt" : reqCmnt});
 * var ajaxObj = new gfnAjaxRequestAction({
		"url":"<c:url value='/req/req2000/req2000/insertReq2000ReqCommentInfoAjax.do'/>"
		,"contentType":"application/x-www-form-urlencoded; charset=UTF-8"
		,"datatype":"json"
		,"async":false
		,"cache":true
		,"processData":true}
		,{"prjId" : prjId, "reqId" : reqId, "reqCmnt" : reqCmnt});
 * 3-1. 객체 선언과 동시에 data 설정
 * 
 * 4. AJAX 성공처리 함수 설정
 * //AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	//코멘트 등록 실패의 경우 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return;
    	}
    	//코멘트 리스트 세팅
    	gfnSetData2CommentsArea(data.reqCommentList, "reqCmntListDiv", "BRD");
    	//코멘트 입력창 클리어
    	$("#reqCmnt").val("");
    	toast.push(data.message);
	});
 * 
 * 5. AJAX 에러처리 함수 설정
 * ajaxObj.fnError(function(xhr, status, err){
 	
 	});
 * 
 * 6. AJAX 통신 준비, 통신 완료처리 4번과 동일
 * 
 * 7. AJAX 통신 시작
 * ajaxObj.send();
 * 
 * 		- 그 외 커스텀 추가 시 내용 삽입 - 
 * 2016-09-13			최초 작성			진주영
 * 2016-09-19			수정				진주영
 */
function gfnAjaxRequestAction(property,data){
	//url, data
	this.url = "";
	this.data = "";
	
	// xml, json, script, html
	this.dataType ="json";
	
	//application/x-www-form-urlencoded, multipart/form-data, text/plain
	this.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
	
	//false = 비동기, true = 동기
	this.async = "false";
	
	//GET 방식 전달인 경우 IE 캐싱 문제 때문에 false로 설정해야 한다.
	this.cache = "true";
	
	//기본값인 true일 때 객체로 전달된 데이터를 쿼리 문자열로 변환한다. FormData 등 쿼리 문자열 변환이 불가능한 -비 처리된- 데이터를 전달할 때는 false로 설정한다.
	this.processData = "true";
	
	this.mimeType = "";
	
	//업로드 진행률 바 객체
	this.pgBarObj = null;
	
	//로딩 이미지 표시 유무(기본값 = 표시)
	this.loadingShow = true;
	
	//Success, beforeSend, complete, error에 null값인 경우 빈 Function 삽입
	//$.noop = jQuery에서 제공하는 빈함수 no-op
	this.fnSuccess = $.noop;
	this.fnbeforeSend = $.noop;
	this.fnComplete = $.noop;
	this.fnError = $.noop;

	//함수 setter
	this.setData = function setData(data){
		this.data = data;
	}
	this.setFnSuccess = function setFnSuccess(fnContent){
		this.fnSuccess = fnContent;
	}
	this.setFnbeforeSend = function setFnbeforeSend(fnContent){
		this.fnbeforeSend = fnContent;
	}
	this.setFnComplete = function setFnComplete(fnContent){
		this.fnComplete = fnContent;
	}
	this.setFnError = function setFnError(fnContent){
		this.fnError = fnContent;
	}

	//AJAX 옵션 설정
	this.setProperty = function setProperty(prop){
		if(!gfnIsNull(prop)){
			this.url = gfnIsNull(prop['url'])?this.url:prop['url'];
			this.data = gfnIsNull(prop['data'])?this.data:prop['data'];
			this.dataType = gfnIsNull(prop['dataType'])?this.dataType:prop['dataType'];
			this.contentType = gfnIsNull(prop['contentType'])?this.contentType:prop['contentType'];
			this.async = gfnIsNull(prop['async'])?this.async:prop['async'];
			this.cache = gfnIsNull(prop['cache'])?this.cache:prop['cache'];
			this.processData = gfnIsNull(prop['processData'])?this.processData:prop['processData'];
			this.mimeType = gfnIsNull(prop['mimeType'])?this.mimeType:prop['mimeType'];
			this.pgBarObj = gfnIsNull(prop['pgBarObj'])?this.pgBarObj:prop['pgBarObj'];
			this.loadingShow = gfnIsNull(prop['loadingShow'])?this.loadingShow:prop['loadingShow'];
		}
	}

	//생성자
	if(!gfnIsNull(property)){
		eval(this.setProperty(property));
	}
	if(!gfnIsNull(data)){
		eval(this.setData(data));
	}

	//AJAX 전송
	this.send = function send(){
		//AJAX 객체 변수
		var obj = this;
		
		//로딩 표시 변수
		var loadingShow = this.loadingShow;
		//AJAX 호출
		$.ajax({
	        type: "POST",
	        url: this.url,
	        data: this.data,
	        contentType: this.contentType,

	        async: this.async,
	        cache: this.cache,
	        processData: this.processData,
	        mimeType: this.mimeType,
	        xhr: function(){
	        	//게이지바 객체가 존재하지 않는 경우 (로딩 이미지 %)
	        	if(gfnIsNull(obj.pgBarObj)){
	        		return gfnLoadProgressStr();
	        	}else{
	        		return gfnLoadProgressBar(obj.pgBarObj);
	        	}
	        },
	        beforeSend: function(){
	        	//게이지바 객체가 존재하지 않는 경우 (로딩 이미지 %)
	        	if(gfnIsNull(obj.pgBarObj) && loadingShow){
		        	//로딩 게이지 바 출력
		    		gfnShowLoadingBar(true);
	        	}
	    		obj.fnbeforeSend();
	        },
	        success: function(data, status, xhr) {
	        	try{
	        		obj.fnSuccess(data, status, xhr);
	        	}catch(e){
	        		alert("success error: "+e);
	        		return;
	        	}
	        },
	        error: function(xhr, status, err){
	        	//세션이 만료된 경우
	        	if(xhr.status == '999'){
	        		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
	        		document.location.href="/cmm/cmm4000/cmm4000/selectCmm4000View.do"
	        		return;
	        	}else{
	        		//그 외에 커스텀 에러 처리
	        		obj.fnError(xhr, status, err);
	        	}
	        	
	        	return;
	        },
	        complete: function(){
	        	//게이지바 객체가 존재하지 않는 경우 (로딩 이미지 %)
	        	if(gfnIsNull(obj.pgBarObj) && loadingShow){
	        		gfnShowLoadingBar(false);
	        	}
	        	obj.fnComplete();
	        }
	    });
	}
}

/**
 * function명 	: gfnGetUsrDataForm [조회 조건 select Box 용]
 * function설명	: 현재 선택되어 있는 프로젝트에 포함되어 있는 사용자 리스트를 가져와서 select Box에 세팅한다.
 * 				  사용자 정보 테이블을 참조하여 가져온다.
 * 				  사용 예제 > gfnGetUsrDataForm(useCls, arrObj, arrComboType, isAsyncMode);
 * 				  Ex> 1. 대분류 코드를 세팅할 selectBox 객체를 배열로 세팅하여 보낸다.
 * 					  2. 사용여부가 사용인지, 미사용인지 아니면 전체를 다 가지고 올지를 판단. (N: 사용하지 않는 것만, Y: 사용하는 것만, 그외: 전체)
 *            		  3. 콤보타입을 전체, 선택, 일반 바로 선택 가능한 상태에 대한 조건을 순서대로 배열로 보낸다. ["S", "A", "E", ""] S: 선택, A: 전체, E:공백추가 , 그 외: 없음  
 * @param useCd			:	사용여부 ( 01: 사용, 02: 미사용, A: 전체 )
 * @param arrObj		:	selectBox 객체 배열
 * @param arrComboType	:	콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
 * @param isAsyncMode	:	동기, 비동기 모드( true: 비동기식 모드, false: 동기식 모드 )
 */
function gfnGetUsrDataForm(useCd, arrObj, arrComboType , isAsyncMode){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/cmm9000/cmm9200/selectCmm9200PrjUsrList.do"
				,"async":isAsyncMode}
			,{ "useCd":useCd });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	if(data.ERROR_CODE == '-1'){
    		jAlert(data.ERROR_MSG, '알림창');
			return;
		}
    	
    	/* 넘겨받은 대분류코드 갯수 만큼 루프 돌며 해당 select 객체에 대입되는 소분류 항목을 세팅해준다. */
		var len = arrObj.length;
		
		for(var i = 0 ; i < len ; i++){
			var codeArr;
			var textArr;
			var strCodeData = data.usrIdcode;
			var strTextData = data.usrNmtext;
			
			if(arrComboType[i] == 'A'){
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');

				//옵션키 A 로 세팅한 전 체(검색조건용)
				arrObj[i].append("<option value='A'>전 체</option>");
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			else if(arrComboType[i] == 'N'){
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');
				
				//전체 한줄 추가
				arrObj[i].append("<option value=''>전 체</option>");
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			else if(arrComboType[i] == 'S'){
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');

				//선택 한줄 추가
				arrObj[i].append("<option value=''>선 택</option>");
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			else if(arrComboType[i] == 'E'){
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');

				//공백 한줄 추가
				arrObj[i].append("<option value=''></option>");
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
			else{
				
				codeArr = strCodeData.split('|');
				textArr = strTextData.split('|');
				
				for(var j = 0 ; j < codeArr.length; j++){
					arrObj[i].append("<option value='" + codeArr[j] + "'>" + textArr[j] + "</option>");
				}
			}
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
			//data = JSON.parse(data);
		jAlert(xhr.statusText);
	});
	//AJAX 전송
	ajaxObj.send();
}

/**
 * function명 	: gfnRequireCheck
 * function설명	: 폼id, 해당 폼에 속한 필수입력 객체 id, 사용자에게 보여줄 이름을 입력받아
 * 				  해당 폼의 공백여부 및 선택여부를 체크하여 true, false로 반환
 * 				  사용 예제 > gfnRequireCheck(formId, checkInputIdArr, checkInputNmArr);
 *            		  1. form ID 를 찾아 해당 form에 속한 객체의 필수 입력 사항을 체크.
 *            		  2. 입력한 객체에 입력값이 없을 경우 true 리턴
 * @param   	: formId			- Form ID
 * @param   	: checkInputIdArr	- 해당 Form에 속한 객체의 ID
 * @param   	: checkInputNmArr	- 해당 객체들의 디스플레이용 이름
 * 
 * 
 *- 수정 - 
 * 2018-08-06			error Class 추가					진주영
 */
function gfnRequireCheck(formId, checkObjArr, checkObjNmArr){
	var inputCnt = checkObjArr.length;
	if(inputCnt < 1){
		return false;
	}
	
	/* 
	 * 	필수 입력조건을 받아서 true, false 리턴함.
	 * 	input box가 select인 항목은 S 일경우만 트루 리턴.
	 */ 
	var value = '';
	for(var i = 0 ; i < inputCnt ; i++){
		value = eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".value");
		text = eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".text");
		inputType = (eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".type")).substring(0,3);
		nm = checkObjNmArr[i];
		
		if(value.length < 1 ){
			jAlert(nm + ' 은(는) 필수 입력 사항입니다.\n\n\r ' + nm + ' 항목을 입력하세요.','알림창',function(){
				eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".focus()");
				
				//error 클래스 추가
				eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".classList.add('inputError')");
			});
			
			return true;
		}
		else if(inputType == 'sel'){
			if(text == '선 택'){
				jAlert(nm + ' 은(는) 필수 선택 사항입니다.\n\n\r ' + nm + ' 항목을 입력하세요.','알림창',function(){
					eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".focus()");
					
					//error 클래스 추가
					eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".classList.add('inputError')");
				});
				return true;
			}
		}
		
		//에러 없는경우 error class 확인후 제거
		var classChk = eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".classList.contains('inputError')");
		if(classChk){
			//error class 제거
			eval("document.getElementById('" + formId + "')." + checkObjArr[i] + ".classList.remove('inputError')");
		}
	}
	
	//필수 체크 전에 유효하지 않은 값이 있는 경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
		$(".inputError")[0].focus();
		return true;
	}
	
	return false ;
}

/**
 * function명 	: gfnFormAllObjReset
 * function설명	: form안의 전체 자식 객체 value값 "" 처리
 * @param formObj 초기화할 Form ID 문자열
 * @param 
 */
function gfnFormAllObjReset(form){
	var list = document.getElementById(form).elements;
	var listCnt = document.getElementById(form).elements.length;
	var inputType;
	
	for(var i = 0 ; i < listCnt ; i++ ){
		inputType = list[i].type.substring(0,3);
		
		switch (inputType){
			case "sel" :
				//셀렉트 박스의 경우 첫번째 옵션을 강제로 선택시킴.
				list[i].selectedIndex = 0;
				break;
			case "che" :
				//체크박스일 경우 강제로 해제 시킴.
				$(list[i]).prop("checked", false);
				break;
			case "rad" :
				//라디오 버튼 컨트롤 안함.
				break;
			default :
				$(list[i]).val("");
				break;
		}
		
	}
}

/**
 * function명 	: gfnSetFormAllObjTabIndex
 * function설명	: form안의 전체 자식 객체에 tabindex 순서대로 처리
 * @param formObj 초기화할 Form ID 문자열
 * @param 
 */
function gfnSetFormAllObjTabIndex(form){
	var list = document.getElementById(form).elements;
	var listCnt = document.getElementById(form).elements.length;
	var inputType;
	
	for(var i = 0 ; i < listCnt ; i++ ){
		inputType = list[i].type.substring(0,3);
		
		switch (inputType){
			case "sel" :
				$(list[i]).attr("tabindex", i + 1);
				break;
			case "che" :
				$(list[i]).attr("tabindex", i + 1);
				break;
			case "rad" :
				$(list[i]).attr("tabindex", i + 1);
				break;
			default :
				$(list[i]).attr("tabindex", i + 1);
				break;
		}
		
	}
}

/**
 * function 명 	: gfnSetDetailObj
 * function 설명	: json데이터로 온 객체의 key값을 배열로 저장하여 해당 키와 동일한 부모 OBJ ID 안의 ID값을 찾아
 * 				  자동으로 데이터를 세팅하는 메서드.
 * 				  작업중.(먼저 사용해도 무방함) - 2016.1.24 정형택
 * @param jsonObj : jsonObj
 * @param objId   : 부모 obj ID 
 * @param pk	  : pk 컬럼의 값
 * @param pkKey	  : pk id
 */
function gfnSetDetailObj(jsonObj, objId, pk, pkKey){
	if(jsonObj == undefined){
		
		var keyArr = new Array();
		
		//맵에 담긴 키값 배열로 저장
		for(var key in jsonObj){
			toast.push(key);
			keyArr.push(key);
		}
		
		for(var i = 0; i < keyArr.length; i++){
			var key = keyArr[i];
			var val = gfnReplace(eval("jsonObj." + key), null, '');
			
			$("#" + objId + " #" + key).text(val);
			$("#" + objId + " #" + key).val(val);
		}
	}
	else{
		//해당하는 키 찾아서 폼안에 존재하는  세팅
		$.each(jsonObj, function(idx, map){
			if(pk == eval("map." + pkKey)){
				
				var keyArr = new Array();
	
				//맵에 담긴 키값 배열로 저장
				for(var key in map){
					keyArr.push(key);
				}
				
				for(var i = 0; i < keyArr.length; i++){
					var key = keyArr[i];
					var val = gfnReplace(eval("map." + key), null, '');
					
					$("#" + objId + " #" + key).text(val);
					$("#" + objId + " #" + key).val(val);
				}
			}
		});
	}
}

/**
 * function 명 	: gfnSetData2Form
 * function 설명	: json데이터로 온 값을 해당 form의 자식 엘레먼트들을 반복하며 id로 스캔하여
 * 				  json key = element id 인곳에 value를 자동으로 세팅한다.
 * @param json 	: json
 * @param frmObj : form Obj 
 */
function gfnSetData2Form(jsonObj, frmId){

	var frmChilds = document.getElementById(frmId).elements;
	var child = null;
	var strType = null;
	var strValue = "";
	var frmChild = null;
	
	$.each(jsonObj, function(key, val){
		
		try{
			//toast.push(key + " : " + val);
			child = $("#" + frmId + " #" + key);
			strType = $("#" + frmId + " #" + key).attr("type");
	    
	        //radio의 경우 child가 배열형태가 되므로, child의 타입을 알수 없다.
	        if (typeof strType == "undefined" && child.length > 0) {
	            strType = child[0].type;
	        }
	    
	        //타입별로 값을 설정한다.
	        switch(strType) {
	            case undefined:
	            case "button":
	            case "reset":
	            case "submit":
	                break;
	            case "select-one":
	            	//값이 null인경우 첫번째 option 선택
	            	if(gfnIsNull(val)){
	            		$(child).children("option:eq(0)").attr("selected","selected");
	            	}else{
	            		$(child).val(val);
	            	}
	                break;
	            case "radio":
	                for (idx = 0, max = child.length; idx < max; idx++) {
	                    if (child[idx].value == val) {
	                        child[idx].checked=true;
	                        break;
	                    }
	                }
	                break;
	            case "checkbox":
	                child.checked = (val == 1);
	                break;
	            case "textarea":
		            	/*$(child).val(val.replace(/<br>/gi,'\n'));*/
	            		$(child).val(val.replace(/(<\/br>|<br>|<br\/>|<br \/>)/gi, '\r\n'));
		            	break;
	            default :
	                $(child).val(val);
	                break;
	        }
	        
	        //toast.push(key + " : " + child.val());
		}catch(e){
			//toast.push('해당 id 엘레먼트 존재하지 않음');
			return;
		}
	});
}

/**
 * function 명 	: gfnSetData2ParentObj (요구사항 목록 관련 화면에서 사용)
 * function 설명	: json데이터로 온 객체(Json 형식 단건 list 아님)를 키와 동일한 부모 OBJ ID 안의 ID값을 찾아
 * 				  자동으로 데이터를 세팅하는 메서드.
 * 				  부모 obj 안에 포함되어 있는 폼엘레먼트들도 type을 체크하여 라디오 버튼을 제외하고는 밸류를 세팅한다.
 * @param json 	: json info(단건)
 * @param parentObj : parent Obj ID
 */
function gfnSetData2ParentObj(jsonObj, parentObjId){

	var child = null;
	var strType = null;
	
	//해당하는 키 찾아서 폼안에 존재하는  세팅
	$.each(jsonObj, function(key, val){
		try{
			
			val = gfnReplace(val, null, '');
			
			child = $("#" + parentObjId + " #" + key);
			strType = $("#" + parentObjId + " #" + key).attr("type");	
        	
			//radio의 경우 child가 배열형태가 되므로, child의 타입을 알수 없다.
	        if (typeof strType == "undefined" && child.length > 0) {
	            strType = child[0].type;
	        }
			
	        if(typeof strType == "undefined" && child.length > 0){
	        	
	        	$("#" + parentObjId + " #" + key).text(val);
	        	$("#" + parentObjId + " #" + key).val(val);
	        }
	        else{
	        	//타입별로 값을 설정한다.
		        switch(strType) {
		            case undefined:
		            case "button":
		            case "reset":
		            case "submit":
		                break;
		            case "select-one":
		            	if(!gfnIsNull(val)){
		            		$(child).val(val);
		            	}
		                break;
		            case "radio":
		                /*for (idx = 0, max = child.length; idx < max; idx++) {
		                    if (child[idx].value == val) {
		                        child[idx].checked=true;
		                        break;
		                    }
		                }
		                break;*/
		            case "checkbox":
		                child.checked = (val == 1);
		                break;
		            case "textarea":
		            	/*$(child).val(val.replace(/<br>/gi,'\n'));*/
		            	$(child).val(val.replace(/(<\/br>|<br>|<br\/>|<br \/>)/gi, '\r\n'));
		            	break;
		            default :
		                if(!gfnIsNull(val)){
		            		$(child).val(val);
		            	}
		                break;
		        }
	        }
			
			
			
			
		}
		catch(e){
			//해당사항 없어도 넘김.
		}
	});
}

/**
 * function 명 	: gfnSetData2CommentsArea (요구사항 목록 관련 화면에서 사용)
 * function 설명	: json데이터로 온 객체(Json 형식 list 형)을 코멘트 영역 부모 OBJ ID 안의 ID값을 찾아
 * 				  자동으로 코멘트를 세팅하는 메서드.
 * @param mapList
 * @param parentId
 * @param mode  : BRD = OSL 보드 등의 요구사항 상세에서 사용할때
 */
function gfnSetData2CommentsArea(mapList, parentId, mode){
	//map data내용이 없는 경우 중지
	if(gfnIsNull(mapList)){
		return false;
	}
	
	//각종 보드에서 사용하는 요구사항 상세 정보 화면에서 사용
	if(mode == 'BRD'){
		$("#" + parentId).children().remove();
		
		var cmntAllCnt = 0;
		$.each(mapList, function(idx, map){
			cmntAllCnt = map.cmntAllCnt;
		});
		if(!gfnIsNull($('#cmntAllCnt'))){
			$('#cmntAllCnt').html('('+cmntAllCnt+'개)');
		}
		//$("#" + parentId).append("<div class='m_t_title' id='cmntAllCnt'>요구사항 댓글 (" + cmntAllCnt + "개)</div>");
		
		$.each(mapList, function(idx, map){
			/*$("#" + parentId).append(
										"<div class='m_t_sub'>"
									+		" <span class='m_t_user'>" + map.regUsrNm + "</span>"
									+		" <span class='m_t_date'>" + map.regDtm + "</span>"
									+		" <div class='m_t_txt'>" + map.reqCmnt + "</div>"
									+	"</div>"
			);*/
			//ago Time구하기
			var agoTime = gfnDtmAgoStr(new Date(map.regDtm).getTime());
			
			$("#" + parentId).append(
				"<div onclick='fnRecentCmnt(this);' class='reqChangeDiv recentCmnt' reqId='"+map.reqId+"' title='"+map.reqCmnt+"'>"
				+" <div class='subRCD'>"
				+gfnCheckStrLength(map.reqCmnt,180)
				+"</div>"
				+		" <div class='subRCD'>"
				+map.regUsrNm+" - "
				+ agoTime + "</div>"
				+	"</div>"
			);
		});
	}
	//그외 일반적인 요구사항 상세 정보 화면에서 사용
	else{
		$("#" + parentId).children().remove();
		
		$.each(mapList, function(idx, map){
			$("#" + parentId).append(
										"<div class='comment_list'>"
									+		" <span class='comment_user'>" + map.regUsrNm + "</span>"
									+		" <span class='comment_date'>" + map.regDtm + "</span>"
									+		" <pre class='comment_contents'>" + map.reqCmnt + "</pre>"
									+	"</div>"
			);
		});
	}
}

/**
 * function 명 	: gfnSetData2ChgHistsArea (요구사항 목록 관련 화면에서 사용)
 * function 설명	: json데이터로 온 객체(Json 형식 list 형)을 코멘트 영역 부모 OBJ ID 안의 ID값을 찾아
 * 				  자동으로 변경정보를 세팅하는 메서드.
 * @param mapList
 * @param parentId
 * @param mode  : BRD = OSL 보드 등의 요구사항 상세에서 사용할때
 */
function gfnSetData2ChgHistsArea(mapList, parentId, mode){
/*
 		<div class='b_title'>요구사항 이력</div>
		<div class='b_sub'>
			<span class='b_user'>사용자1</span>
			<img src='/images/contents/bar.png' alt='' class='bar_img' />
			<span class='b_one'>Test_1 수정</span>
			<img src='/images/contents/bar.png' alt='' class='bar_img' />
			<span class='b_two'>중요도 낮음에서 높음으로 변경</span>
			<img src='/images/contents/bar.png' alt=''class='bar_img' />
			<span class='b_date'>15.12.11</span>
		</div>
 */
	if(mode == 'BRD'){
		$("#" + parentId).children().remove();
		
		var chgHistAllCnt = 0;
		$.each(mapList, function(idx, map){
			chgHistAllCnt = map.chgHistAllCnt;
		});
		
		$("#" + parentId).append("<div class='b_title' id='chgHistAllCnt'>요구사항 이력(" + chgHistAllCnt + "개)</div>");
		
		$.each(mapList, function(idx, map){
			var msg = "";
			if(map.chgGbCd == '01'){
				msg = map.preSprintNm + " => " + map.chgSprintNm;
			}
			else if(map.chgGbCd == '02'){
				msg = map.preFlowNm + " => " + map.chgFlowNm;
			}
			else{
				msg = map.preEtcNm + " => " + map.chgEtcNm;
			}
			
			$("#" + parentId).append(
										"<div class='b_sub'>"
									+		"<span class='b_user'>" + map.reqChgUsrNm + "</span>"
									+		"<img src='/images/contents/bar.png' alt='' class='bar_img' />"
									+		"<span class='b_one'>" + map.chgGbNm + "</span>"
									+		"<img src='/images/contents/bar.png' alt='' class='bar_img' />"
									+		"<span class='b_two'>" + msg + "</span>"
									+		"<img src='/images/contents/bar.png' alt='' class='bar_img' />"
									+		"<span class='b_date'>" + map.reqChgDtm + "</span>"
									+	"</div>"
			);
		});
	}
	else{
		$("#" + parentId).children().remove();
		
		$.each(mapList, function(idx, map){
			$("#" + parentId).append(
										"<div class='comment_list'>"
									+		" <span class='comment_user'>" + map.regUsrNm + "</span>"
									+		" <span class='comment_date'>" + map.regDtm + "</span>"
									+		" <pre class='comment_contents'>" + map.reqCmnt + "</pre>"
									+	"</div>"
			);
		});
	}
}

/**
 * function 명 	: gfnShowLoadingBar
 * function 설명	: Ajax로 트랜잭션시 사용할 loading 바를 show/hide 한다.
 * @param isShow: 로딩바호출 : true, 로딩바숨김 : false
 */
function gfnShowLoadingBar(isShow){
	if(isShow){
		$(".top_fixed").show();
	}
	else{
		$(".top_fixed").hide();
		$('.top_str').html('');
	}
}

/**
 * 	function 명 	: gfnLayerPopup
 *  function 설명	: 레이어 팝업을 호출한다.
 *  url			: 호출 URL
 *  data		: 1. json 형식 ex> {"key1" : "value1", "key2" : "value2"}
 *  			  2. form serialize 형식 ex> $("#formObj").serialize(); => id=jht&pw=jht
 *  width		: 레이어팝업의 가로사이즈 px	- default 540px
 *  height		: 레이어팝업의 세로사이즈 px	- default 444px
 *  overflowY	: overflow-y속성 기본값 hidden
 *  loadingShow : 로딩바 표현 = true, 미 표현 = false (기본값 true)
 */
function gfnLayerPopupOpen(url, data, width, height, overflowY, loadingShow){
	//레이어 팝업이 2개 오픈된 경우, 3개 이상부터 경고창 알림
//	if(!gfnIsNull($('.layer_popup_box')) && $('.layer_popup_box').length >= 1){
//		jAlert("팝업 오픈은 1개까지만 가능합니다.");
//		return false;
//	}
	width = Number(width);
	height = Number(height);
	//입력받은 넓이와 높이값이 숫자가 아니면 강제 리턴
	if(isNaN(width) || isNaN(height)){
		jAlert('입력한 높이와 넓이의 값이 숫자가 아닙니다.','알림창');
		return;
	}
	
	var boxLength=$(".layer_popup_box").length;
	var maxZIndex=0;
	var valid = false;
	$(".layer_popup_box").each(function(idx, item){
		var zIndex= $(item).css("z-index");

		if($(item).attr("layerurl")==url ){
			valid = true;
		}
		
		if(maxZIndex < zIndex ){
			maxZIndex = zIndex;
		}
	});
	
	if(valid){
		return;
	}
			
	var layerBoxDivId= layer_popup(url, data,loadingShow);
	var cssObj = {};
	
	if(maxZIndex==0){
		cssObj = {	/* 로우 추가시 height 수정 */
				"width" : width + "px",
				"height" : height + "px"				
			};
	}else{
		cssObj = {	/* 로우 추가시 height 수정 */
			"width" : width + "px",
			"height" : height + "px",
			"z-index" : maxZIndex+1
		};
	}	
	$('#'+layerBoxDivId).css(cssObj);
	

	
	if(overflowY != null){
		//브라우저 height보다 같거나 클 경우 overflowY 적용함
		if($(window).height() <= $('#'+layerBoxDivId).height()){
			$('#'+layerBoxDivId+' .ajax_box').css({"overflow-y" : overflowY});
		}
		
		//브라우저 크기 조절할 경우 위와 같은 분기
		$(window).resize(function(){
			//윈도우 크기 변경 될 경우 스크롤 고정 오류를 해결하기 위해 스크롤 위치 최상
			$('#'+layerBoxDivId+' .ajax_box').scrollTop(0);
			
			if($(window).height() <= $('.layer_popup_box').height()){
				$('#'+layerBoxDivId+' .ajax_box').css({"overflow-y" : overflowY});
			}else{
				$('#'+layerBoxDivId+' .ajax_box').css({"overflow-y" : "hidden"});
				//스크롤이 toggle될 경우 스크롤 width 공백 현상 수정
				$('.layerpopup.user_pop_wrap').width($('.layer_popup_box .ajax_box').width());
			}
		});
	}else{
		$('#'+layerBoxDivId+' .ajax_box').css({"overflow-y" : "hidden"});
	}
 
		if($('#'+layerBoxDivId+' .pop_left').length > 0){
			var bodyFont = $('body').css("font-size").replace('px','') % 16;
			var paddingSize = $('#'+layerBoxDivId+' .pop_left').css('padding-top').replace('px','');
		if(bodyFont<10){
			$('#'+layerBoxDivId+' .pop_left').css({'padding-top': paddingSize - bodyFont/2, 'padding-bottom': paddingSize - bodyFont/2});  
		}
	}
}




/**
 * 	function 명 	: gfnLayerPopupClose
 *  function 설명	: 레이어 팝업을 닫는다.
 */
function gfnLayerPopupClose(){
	var maxDiv="";
	var maxZIndex = 0;
	$(".layer_popup_box").each(function(idx, item){
		var zIndex= $(item).css("z-index");
		if(maxZIndex < zIndex ){
			maxZIndex = zIndex;
			maxDiv= $(item).attr('id');
		}
	});
		
	$("#"+maxDiv).remove();
	var layerlength=$(".layer_popup_box").length;
	if(layerlength==0){
		$(".bg").remove();
		$("body").removeClass("bhpf");
	}else{
		$("#lpxBgF_"+layerlength).remove();
		$("#lpxBgS_"+layerlength).remove();
	}
	
	
}

/**
 * 	function 명 		: gfnCalRangeSet
 * 	function 설명		: 달력의 시작일과 종료일의 유효성 및 달력 아이콘 등, 달력 컴포넌트를 세팅하는 함수
 * 	@param fromId 	: 시작일 input의 ID 
 * 	@param toId	  	: 종료일 input의 ID
 * 	@param timeUseCd : timePicker 사용-true/미사용-false (default-false)
 */
function gfnCalRangeSet(fromId, toId, grpFromDt, grpEndDt,timeUseCd){
	
	//년 범위 구하기 (-10 ~ +10)
	var minYear = new Date().getFullYear()-10;
	var maxYear = new Date().getFullYear()+10;
	
	//Date type
	var format = 'YYYY-MM-DD';
	var subFormat = 'yyyy-MM-dd'
	
	//timePicker 사용유무
	var timePicker = false;
	if(!gfnIsNull(timeUseCd) && timeUseCd){
		timePicker = true;
		format += " HH:mm";
		subFormat += " HH:mm";
	}
	
	var fromData = $("#" + fromId).val();
	var toData = $("#" + toId).val();
	var fromStartDate = new Date(fromData).format(subFormat);
	var toStartDate = new Date(toData).format(subFormat);
	
	$("#" + fromId).daterangepicker({
		showDropdowns:true,
		singleDatePicker:true,
		timePicker: timePicker,
		timePicker24Hour:true,
		autoUpdateInput:false,
		applyLabel: "적용",
		minYear: minYear,
		maxYear: maxYear,
		buttonClasses:"button_normal2",
		locale: {
		format: format,
			"daysOfWeek": ["일","월","화","수","목","금","토"],
			"monthNames": ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
			"separator": " ~ ",
			"applyLabel": "적용",
			"cancelLabel": "취소",
			"customRangeLabel": "사용자 지정",
		}
	},function( selectedDate,picker ) {
		$("#" + fromId).val(this.startDate.format(this.locale.format));
    	$("#" + toId ).data('daterangepicker').setMinDate(new Date(selectedDate._d).format(subFormat));
    });
	
	$("#" + toId).daterangepicker({
		showDropdowns:true,
		singleDatePicker:true,
		timePicker: timePicker,
		timePicker24Hour:true,
		autoUpdateInput:false,
		applyLabel: "적용",
		minYear: minYear,
		maxYear: maxYear,
		buttonClasses:"button_normal2",
		locale: {
		format: format,
			"daysOfWeek": ["일","월","화","수","목","금","토"],
			"monthNames": ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
			"separator": " ~ ",
			"applyLabel": "적용",
			"cancelLabel": "취소",
			"customRangeLabel": "사용자 지정",
		}
	},function( selectedDate ) {
		$("#" + toId).val(this.startDate.format(this.locale.format));
    	$("#" + fromId ).data('daterangepicker').setMaxDate(new Date(selectedDate._d).format(subFormat));
    });
	
	//값이 있는경우 startDate 삽입
	if(!gfnIsNull(fromData)){
		$("#" + fromId ).data('daterangepicker').setStartDate(fromStartDate);
	}
	if(!gfnIsNull(toData)){
		$("#" + toId ).data('daterangepicker').setStartDate(toStartDate);
	}
	
	inFnGrpDateSet();
	
	//내부 함수
	function inFnGrpDateSet(){
		//그룹 기간이 존재하는 경우 옵션 추가
		if(!gfnIsNull(grpFromDt)){
			$("#" + toId ).data('daterangepicker').setMinDate(grpFromDt);
			$("#" + fromId ).data('daterangepicker').setMinDate(grpFromDt);
		}	
		
		if(!gfnIsNull(grpEndDt)){
			$("#" + toId ).data('daterangepicker').setMaxDate(grpEndDt);
			$("#" + fromId ).data('daterangepicker').setMaxDate(grpEndDt);
		}
	}
	//$.datepicker.setDefaults($.datepicker.regional['ko']);
}

/**
 * 	function 명 		: gfnCalRangeDel
 * 	function 설명		: 해당 오브젝트에 선언된 datepicker 제거
 * 	@param fromId 	: 시작일 input의 ID 
 * 	@param toId	  	: 종료일 input의 ID
 */
function gfnCalRangeDel(fromId, toId){
	$( "#" + fromId ).data('daterangepicker').remove();
	$( "#" + fromId ).next().remove();
	$( "#" + toId).data('daterangepicker').remove();
	$( "#" + toId ).next().remove();
	
}

/**
 * 	function 명 			: gfnCalSet
 * 	function 설명			: Input box에 달력속성 부여
 * 	@param formatType 	: 출력 일자 타입 (ex. yy-mm-dd) 
 * 	@param ~(동적)  		: 
 */
function gfnCalSet(formatType){
	
	//년 범위 구하기 (-10 ~ +10)
	var minYear = new Date().getFullYear()-10;
	var maxYear = new Date().getFullYear()+10;
	
	//동적 매개변수 루프
	for(var i=1;i<arguments.length;i++){
		$("#" + arguments[i]).daterangepicker({
			showDropdowns:true,
			singleDatePicker:true,
			timePicker: false,
			timePicker24Hour:true,
			autoUpdateInput:false,
			applyLabel: "적용",
			minYear: minYear,
			maxYear: maxYear,
			buttonClasses:"button_normal2",
			locale: {
			format: formatType,
				"daysOfWeek": ["일","월","화","수","목","금","토"],
				"monthNames": ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
				"separator": " ~ ",
				"applyLabel": "적용",
				"cancelLabel": "취소",
				"customRangeLabel": "사용자 지정",
			}
		},function( selectedDate ) {
			this.element.val(this.startDate.format(this.locale.format));
	    });
	}
	//$.datepicker.setDefaults($.datepicker.regional['ko']);
}

/**
 * 
 * @param formatType 	: 출력 일자 타입 (ex. yy-mm-dd) 
 * @param elementIds    : datepicker 가 적용될 elementId
 * @param options		: 추가 적용 옵션 예 minDate , maxDate 등.. datepicker의 모든 옵션 Object Type으로 적용가능
 */
function gfnCalendarSet(formatType,elementIds,options){
	
	//년 범위 구하기 (-10 ~ +10)
	var minYear = new Date().getFullYear()-10;
	var maxYear = new Date().getFullYear()+10;
	
	//동적 매개변수 루프
	var dateObjects = {};	
	for(var i=0;i<elementIds.length;i++){
		//date type 매개변수 제외하고 datepicker 삽입
		dateObjects = {
			showDropdowns:true,
			singleDatePicker:true,
			timePicker: false,
			timePicker24Hour:true,
			autoUpdateInput:false,
			minYear: minYear,
			maxYear: maxYear,
			applyLabel: "적용",
			buttonClasses:"button_normal2",
			locale: {
			format: formatType,
				"daysOfWeek": ["일","월","화","수","목","금","토"],
				"monthNames": ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"],
				"separator": " ~ ",
				"applyLabel": "적용",
				"cancelLabel": "취소",
				"customRangeLabel": "사용자 지정",
			}
		};
		
		if(!gfnIsNull(options)){
			$.each(options,function(key, value) {
				dateObjects[key]=value;
			});
		}
		
		$("#" + elementIds[i]).daterangepicker(dateObjects,function( selectedDate ) {
			this.element.val(this.startDate.format(this.locale.format));
	    });
		//$("#" + elementIds[i]).datepicker( dateObjects );
	}	
	//$.datepicker.setDefaults($.datepicker.regional['ko']);
	
}


/**
 * 널 체크
 * @param sValue
 * @returns {Boolean}
 */
function gfnIsNull(sValue)
{
     if( String(sValue).valueOf() == "undefined") {
        return true;
    }
    if( sValue == null ){
        return true;
    }
    if( ("x"+sValue == "xNaN") && ( new String(sValue.length).valueOf() == "undefined" ) ){
        return true;
    }
    if( sValue.length == 0 ){
        return true;
    }
    if( sValue == "NaN"){
        return true;
    }
    return false;
}

/**
 * 문자열의 일부분을 다른 문자열로 치환
 * @param sOrg		가운데 부문을 얻어올 원본 문자열
 * @param sRepFrom	치환대상 문자열
 * @param sRepTo	치환될 문자열
 * @returns
 */
function gfnReplace( sOrg, sRepFrom, sRepTo )
{
	var pos, nStart=0, sRet="";

	sOrg = gfnStr(sOrg);

	if( gfnIsNull(sOrg) )			return "";
	if( gfnIsNull(sRepFrom) )		return sOrg;
	
	while(1){
		pos = gfnPos( sOrg, sRepFrom, nStart );
		if( pos < 0 ){
			sRet += sOrg.substr( nStart );
			break;
		}else{
			sRet += sOrg.substr( nStart, pos - nStart);
			sRet += sRepTo;
			nStart = pos+sRepFrom.length;
		}
	}
	return sRet;
}

/**
 * 문자열에서 해당문자열의 위치 검색
 * @param sOrg 		가운데 부문을 얻어올 원본 문자열
 * @param sFind		검색할 문자열
 * @param nStart	검색 시작 위치
 * @returns
 */
function gfnPos(sOrg, sFind, nStart)
{
	if( gfnIsNull(sOrg) || gfnIsNull(sFind) )	return -1;
	if( gfnIsNull(nStart) )	nStart = 0;

	return sOrg.indexOf(sFind, nStart);
}

/**
 * 문자형식으로 변경
 * @param sText
 * @returns
 */
function gfnStr(sText){
	if(sText == undefined) return "";
	if(sText == null) return "";
	if(sText instanceof String) return sText;

	return ""+sText;
}

/**
 * function명 	: String 객체에 특정 문자 배열로 대체. 
 * function설명	: 문자열 객체에 {0},{1} 과 같은 형태로 {n}개를 선언한 다음
 * 				  n개수 만큼의 문자열을 배열에 입력된 값으로 대체 시킨다.
 * @param args	: {0} 과 같은 값을 대체 할 배열
 * ex) "지금 시간은 {0}시 {1}분 {2}초 입니다".format({'11','22','33'}); 
 * 		=> 지금 시간은 11시 22분 33초 입니다. 
 * 
 */
String.prototype.format = function (args) {
	var str = this;
	return str.replace(String.prototype.format.regex, function(item) {
		var intVal = parseInt(item.substring(1, item.length - 1));
		var replace;
		if (intVal >= 0) {
			replace = args[intVal];
		} else if (intVal === -1) {
			replace = "{";
		} else if (intVal === -2) {
			replace = "}";
		} else {
			replace = "";
		}
		return replace;
	});
};
String.prototype.format.regex = new RegExp("{-?[0-9]+}", "g");

/**
 * 로딩화면 완료 퍼센트 구하기
 * AJAX속성에서 async:true (기본값)
 * AJAX속성에서 xhr: function(){return gfnLoadProgressStr();} 처리
 * AJAX처리 시작 0%에서 완료 시 100%
 * 로딩화면 종료에서 $('.top_str').html('') 처리
 */
function gfnLoadProgressStr(){
	var xhr = new window.XMLHttpRequest();
    //Upload progress
    xhr.upload.addEventListener("progress", function(evt){
      if (evt.lengthComputable) {
        var percentComplete = evt.loaded / evt.total;
        //Do something with upload progress
        $('.top_str').html(parseInt(percentComplete*100)+'%');
      }
    }, false);
    return xhr;
}

/**
 * 업로드 게이지 바
 * AJAX속성에서 async:true (기본값)
 * AJAX속성에서 xhr: function(){return gfnLoadProgressBar(pgBarObj);} 처리
 * AJAX처리 시작 width0 ~ 100%
 * @param pgBarObj	: 게이지바 오브젝트 
 */
function gfnLoadProgressBar(pgBarObj){
	var xhr = new window.XMLHttpRequest();
	
	//업로드 객체가 존재하는 경우에만 게이지 바 동작
	if(!gfnIsNull(pgBarObj)){
	    //Upload progress
	    xhr.upload.addEventListener("progress", function(evt){
	      if (evt.lengthComputable) {
	        var percentComplete = evt.loaded / evt.total;
	        //단건인경우
	        if(Object.isArray(pgBarObj)){
	        	var pgBarObj_bottomBar = $(pgBarObj[0]).children('.file_progressBar').children('div');
	        	//게이지바 width 조절
	        	pgBarObj_bottomBar.stop().animate({width:(percentComplete*100)+'%'},1);
	        }
	        else{ //배열인경우
	        	$.each(pgBarObj,function(idx, map){
	        		var pgBarObj_bottomBar = $(map).children('.file_progressBar').children('div');
		        	//게이지바 width 조절
		        	pgBarObj_bottomBar.stop().animate({width:(percentComplete*100)+'%'},1);
	        	});
	        }
	      }
	    }, false);
	}
    return xhr;
}

/**
 * fileVo정보 div 구현
 * <다운로드, 삭제 기능>
 * @param fileVo	파일정보
 * @param divId		생성된 div를 넣을 divId값
 */
function gfnFileListDiv(fileVo,divId){
	//사이즈 구하기
	var size = gfnByteCalculation(fileVo.fileMg);
    var fnStr = "gfnFileDownload(this,false,'"+fileVo.fileExtsn+"', event)";
    
    //DIV
	var divTemp = $(
			"<div class='fileDivBoth'>"
			+"<div onclick="+fnStr+" class='fileInfo' atchId='"
			+fileVo.atchFileId
			+"' fileSn='"
			+fileVo.fileSn
			+"'>"
			+fileVo.orignlFileNm
			+" ("+size+")"
			+"</div><div onclick='gfnFileDelete(this, event);' class='fileDel'  atchId='"
			+fileVo.atchFileId
			+"' fileSn='"
			+fileVo.fileSn
			+"'>X</div>"
			+"</div>");
	$(divTemp).appendTo(divId);
}

/**
 * fileVo정보 div 구현
 * <다운로드 기능>
 * @param fileVo	파일정보
 * @param divId		생성된 div를 넣을 divId값
 * @param option	파일 다운로드 옵션
 */
function gfnFileListReadDiv(fileVo,divId,type,delChk){
	//확장자 이미지 구하기
	var fileExtsnImg = gfnFileExtImages(fileVo.fileExtsn);
	//사이즈 구하기
	var size = gfnByteCalculation(fileVo.fileMg);

	//삭제 기본 disabled
	var delChkClass = " delNone";
	var delEventStr = "";
	var delStr = "";
	
	//삭제 체크
	if(gfnIsNull(delChk)){
		delChk = true;
	}

	//삭제가 가능한 권한인지 확인
	if(typeof btnAuthDeleteYn != "undefined" && btnAuthDeleteYn == "Y" && type != "req4104" && delChk){
		delChkClass = "";
		var scriptStr = "gfnFileDelete($(this).siblings(\".file_contents\"), event)";
		delEventStr = " onclick='"+scriptStr+"'";
		delStr = '<div id="btn_delete_file" class="file_btn file_delete'+delChkClass+'"'+delStr+delEventStr+' atchId="'+fileVo.atchFileId+'">삭제</div>';
	}
	
	//다운로드 str생성
	var scriptStr = "gfnFileDownload($(this),true)";
	downStr = " onclick='"+scriptStr+"'";

	//글자수 제한 type별로 세팅 (default = 13, doc = 50(산출물))
	var defaultStrLength = 30;
	
	//모바일 화면인 경우 더 작게
	if($(document).width() <= 1500){
		defaultStrLength = 20;
	}
	//파일 등록일
	var creatDt_str = "";
	var creatDt = "";
	
	//ie에 맞춰 형식 변경
	var fileCreatDt = fileVo.creatDt;
	if(!gfnIsNull(fileCreatDt)){
		fileCreatDt = fileCreatDt.replace(/-/g,"/");
		fileCreatDt = fileCreatDt.substring(0,fileCreatDt.indexOf("."));
	}
	
	//vo정보가 없는 경우 현재 시간 기록
	if(typeof fileCreatDt == "undefined"){
		creatDt = new Date().format('yyyy-MM-dd HH:mm:ss');
	}else{
		creatDt = new Date(fileCreatDt).format('yyyy-MM-dd HH:mm:ss');
	}
	
	if(!gfnIsNull(type)){
		if(type == "doc"){
			defaultStrLength = 50;
			creatDt_str = "<span style='font-size:0.9em;'> - ("+creatDt+")</span>";
		}else if(type == "req" || type == "req4104"){
			defaultStrLength = 30;
		}else if(type == "more"){
			defaultStrLength = 16;
		}else if(type == "dpl"){
			defaultStrLength = 35;
		}else if(type == "req_popup"){
			defaultStrLength = 50;
		}else if(type == "newReq"){ //요청 접수
			defaultStrLength = 50;
			delStr = "";
		}
	}
	
	
	//삭제 타입
	var deleteType = "normal";
	
	if(type == "req" || type == "req_popup"){
		deleteType = "request";
	}
	
	//Div title
	var fileTitle = fileVo.orignlFileNm+" - ("+creatDt+")";

	//DIV 생성
	var divTemp = $('<div class="file_frame_box">'
						+'<div class="file_main_box">'
							+'<div class="file_contents" reqId="'+fileVo.reqId+'" deleteType="'+deleteType+'"'
							+' atchId="'+fileVo.atchFileId+'" fileSn="'+fileVo.fileSn+'"'+downStr+' title="'+fileTitle+'">'+
							fileExtsnImg
							+gfnCutStrLen(fileVo.orignlFileNm, defaultStrLength)
							+'('+size+')'
							+creatDt_str
							+'</div>'
							+delStr
						+'</div>'
						+'<div class="file_progressBar"><div></div></div></div>');
	$(divTemp).appendTo(divId);
	
	//생성 객체를 반환한다.
	return $(divTemp);
}

/**
 * 1. File 데이터를 목록화 해주고 제어함
 * 2. Drag&Drop 과 [ input type='file' ] 태그 및 전체 삭제에 대한 기능을 담당하는 곳에서 사용하게끔 변경
 * 3. 기존 함수에서는 파일제어의 흐름이 맞지않아 함수 기능을 새로 정의
 * @param fileVo	파일정보
 * @param divId		생성된 div를 넣을 divId값
 * @param option	파일 다운로드 옵션
 */
/*function gfnFileListReadDiv2(fileVo, divId, option){
	
}*/


/**
 * 파일 확장자 이미지 구하기
 * @param fileExtsn 파일 확장자명
 * @returns {String} <img>
 */
function gfnFileExtImages(fileExtsn){
	var extArrayGif = ["aif","aifc","aiff","app","arj","asf","asx","au","avi","bat","bmp","cdf","cgi","com","compressed","css","css2","csv","default","device","dif","dll","dv","eml","etc","exe","exe2","fla","gif","gz","htm","htm2","html","ico_plus","iff","image","img","ini","jfif","jpeg","jpg","js","lhz","lzh","mac","midi","mov","movie","mp2","mp3","mpe","mpeg","mpg","nws","pcx","ps","psd","qif","qt","qti","qtif","ra","ram","rar","rle","rm","rtf","rtf2","rv","sound","spl","swf","sys.gif","tar","text","tga","tgz","tif","tiff","txt","unknow","unknown","wav","wav2","wma","wmf","wmv","z"];
	var extArrayPng = ["psd","7z","xls","xlsx","gz","msi","ttf","gif","tgz","mid","fla","bin","bat","mpeg","swf","flv","bmp","html","pdf","jar","ai","png","doc","docx","tmp","htm","zip","jpg","eml","dat","iso","wav","tif","php","rar","jpeg","ppt","pptx"];
	var fileExtsnImg = "";
	if(extArrayPng.indexOf(fileExtsn) != -1){
		fileExtsnImg = '<img src="/images/ext/'+fileExtsn+'.png" style="height:20px;max-width:25px;margin-right:5px;"/>';
	}else if(extArrayGif.indexOf(fileExtsn) != -1){
		fileExtsnImg = '<img src="/images/ext/'+fileExtsn+'.gif" style="height:20px;max-width:25px;margin-right:5px;"/>';
	}else{
		fileExtsnImg = '<img src="/images/ext/etc.png" style="height:20px;max-width:25px;margin-right:5px;"/>';
	}
	return fileExtsnImg;
}

/**
 * byte 용량을 받아서 형 변환 후 리턴해주는 함수
 * 2016-06-20
 * @param bytes
 * @returns {String} 변환 값
 */
function gfnByteCalculation(bytes) {
    var bytes = parseInt(bytes);
    var s = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
    var e = Math.floor(Math.log(bytes)/Math.log(1024));
   
    if(e == "-Infinity") return "0 "+s[0]; 
    else 
    return (bytes/Math.pow(1024, Math.floor(e))).toFixed(2)+" "+s[e];
}

/**
 * Date Format Function
 * @param f
 * @returns
 */
//getTime to date
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";
 
    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;
    if(gfnIsNull(f)){
    	f = "yyyy-mm-dd";
    }
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|ms|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "ms": return d.getMilliseconds().zf(3);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};
 
String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};
 
/**
 * AJAX 페이지 이동 없이 파일 다운로드
 */
jQuery.download = function(url, data, method){
    if( url && data ){
    	toast.push("다운로드 준비중입니다.<br>페이지를 이동하지 말아주십시오.");
        // data 는  string 또는 array/object 를 파라미터로 받는다.
        data = typeof data == 'string' ? data : jQuery.param(data);
        //Form input box 삭제
        var tagNameInputBox = document.tmpFrame.downForm.getElementsByTagName('input');
        $.each(tagNameInputBox,function(){
        	document.tmpFrame.downForm.removeChild(document.tmpFrame.downForm.childNodes[0]);
        });
        
        // 파라미터를 form의  input으로 만든다.
        var inputs = '';
        jQuery.each(data.split('&'), function(){
            var pair = this.split('=');
            inputs = document.createElement("INPUT");
            inputs.type = "hidden";
            inputs.name = pair[0];
            inputs.value = pair[1];
            document.tmpFrame.downForm.appendChild(inputs);
        });
        // form값 설정
        document.tmpFrame.downForm.action = url;
        document.tmpFrame.downForm.method = (method||'post');
        document.tmpFrame.downForm.submit();
        
        //jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
        //.appendTo($('#tmpFrame')).submit().remove();
       // $('#tmpFrame').remove();
    }else{
    	console.log("non data");
    }
};


/**
 * div attr로 존재하는 atchId, fileSn으로 파일 다운로드
 * @param divElement	선택한 파일정보 DIV
 * @param pdfDown	pdf 다운로드 여부 (true/false)
 * @param fileExtsn	선택한 파일의 순번
 * @param event	선택한 파일정보 DIV의 이벤트
 */
function gfnFileDownload(divElement,pdfDown,fileExtsn, event){
	try{
		event = event || window.event;
		event.stopPropagation();
	}catch(e){
		//ie는 stop메소드 없음
		window.event.cancelBubble = true;
	}
	
	var downAtchFileId = $(divElement).attr('atchId');
	var downFileSn = $(divElement).attr('fileSn');
	if(gfnIsNull(downAtchFileId) || gfnIsNull(downFileSn)){
		alert("다운로드 실패");
	}else{
		/**
		 * PDF 뷰어 동작
		 * - PDFObjec 동작 확인 후 PDF 뷰어 창 오픈(새 창)
		 * - PDF 뷰어 창 내에서 다운로드 발생 시, gfnFileDownload 매개변수 pdfDown = true로 세팅한다.
		 **/
		if((!gfnIsNull(fileExtsn) && fileExtsn.trim() == "pdf") || (gfnIsNull(pdfDown))){
			var pdfFrame = window.open("/com/fms/pdfViewerPage.do?downAtchFileId="+downAtchFileId+"&downFileSn="+downFileSn, "_blank","location=no, menubar=no, width=1000,height=1000");
		}else{
			$.download('/com/fms/FileDown.do','downAtchFileId='+downAtchFileId+'&downFileSn='+downFileSn,'post');
		}
	}
}

/**
 * div attr로 존재하는 fileSn으로 파일 삭제
 * @param divElement	선택한 파일 Sn
 * @param event	선택한 파일 DIV의 이벤트
 */
function gfnFileDelete(divElement, event){
	try{
		event = event || window.event;
		event.stopPropagation();
	}catch(e){
		//ie는 stop메소드 없음
		window.event.cancelBubble = true;
	}
	
	console.log($(divElement));
	//삭제 권한 체크
	if(btnAuthDeleteYn != 'Y'){
		jAlert('삭제 권한이 없습니다.', '알림창');
		return false;
	}

	jConfirm("파일을 삭제하시겠습니까??", "알림창", function( result ) {
		
		if( result ){
			
			var atchFileId = $(divElement).attr('atchId');
			var fileSn = $(divElement).attr('fileSn');
			var reqId = $(divElement).attr('reqId');
			var deleteType = $(divElement).attr('deleteType');
			
			//null값인 경우 기본값 normal
			if(typeof deleteType == "undefined" || deleteType == null){
				deleteType = "normal";
			}
			//파일 삭제 AJAX
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"/com/fms/FileDelete.do"}
					,{ "atchFileId": atchFileId, "fileSn" : fileSn , "reqId": reqId, "deleteType":deleteType});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
		    	if(data.Success == 'Y'){
		    		$(divElement).parent().parent().remove();
		    		toast.push(data.message);
		    	}else{
		    		//이미 삭제된 파일
		    		if(!gfnIsNull(data.nonFile) && data.nonFile == "Y"){
		    			$(divElement).parent().parent().remove();
		    		}
		    		toast.push(data.message);
		    	}
		  	  if(typeof fnSelHisInfoList == "function"){
		  		fnSelHisInfoList();
		  	  }
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
					//data = JSON.parse(data);
				jAlert(xhr.statusText);
			});
			//AJAX 전송
			ajaxObj.send();
			
			//드래그앤 드롭, 산출물 삭제일 경우 sortable 취소 처리
			function thisItemCancel(thisItem){
				if($(thisItem) != null){
					$(thisItem).sortable('cancel');
				}
			}

		}
	}); /* end jConfirm */   
}

/**
 * 	gfnCheckStrLength
 * 	설정한 글자수 이상은 '···' 처리 하여 리턴하는 스크립트
 * 	길이를 넣지 않거나 0으로 넣은경우는 문자열 그대로 리턴한다.
 */
function gfnCheckStrLength(str,len) {
	var temp = "";
	
	//str이 빈 값일 경우 str그대로 반환
	if(gfnIsNull(str)){
		return str;
	}
	var strLen = str.length;

	if(len > 0 || len == null){
		if(strLen > len){
			temp = str.substring(0, len) + "...";
		}
		else{
			temp = str;
		}
	}else{
		temp = str;
	}
	
	return temp;
}

/**
 * 	엔터키 처리시 강제 함수 호출 공통
 * 	param : fnFullNm
 */
function gfnEnterAction(fnFullNm){
	//엔터일때 이벤트 처리함
	if(event.keyCode == '13'){
		eval(fnFullNm);
	}
}

/**
 * 지정된 Element(Input box)에서 엔터키 입력 시 해당 함수 실행
 * @param	fnc 실행 함수
 * 			매개변수가 있는 경우 전달된 매개변수 타입은 문자열(String)이여야 한다.
 * @param	objName Input box 아이디 값 (동적으로 매개변수 할당)
 */
function gfnEnterAction2(fnc){
	//동적 매개변수 루프
	for(var i=1;i<arguments.length;i++){
		//실행 함수 제외하고 keypress 이벤트 삽입
		$('#' + arguments[i]).keypress(function(e) {
			var key = e.which;
			if (key == 13) {
				eval(fnc);
			}
		});
	}
	
}

/**
 * 해당 폼에서 자동으로 폼값을 가져와 FormData()에 세팅
 * @param formName	값을 가져올 폼 이름
 * @param fd		값을 넣을 FormData()
 */
function gfnFormDataAutoValue(formName,fd){
	var fdInput = $('#'+formName+' input');
	var fdSelect = $('#'+formName+' select');
	var fdText = $('#'+formName+' textarea'); 

	$.each(fdInput, function(index, element){
		fd.append(element.id,element.value);
	});
	$.each(fdSelect, function(index, element){
		fd.append(element.id,element.value);
	});
	$.each(fdText, function(index, element){
		// textarea에 <br>들어가는 부분 주석처리
		//fd.append(element.id,element.value.replace(/\n/gi,'<br>'));
		fd.append(element.id,element.value);
	});
}

/**
 * 해당 폼에서 자동으로 폼값을 가져와 FormData()에 세팅
 * @attr
 * - input box -	title -> 항목 명
 *					value -> 항목 값
 *					id	  -> 항목 필드명
 *					type  -> 항목 타입
 *					modifyset	-> 01- 이력 저장 항목[기본값], 02- 이력 저장 안함
 *					opttarget	-> 01 - 기본 컬럼, 02 - 추가 항목, 03 - 배포계획
 *					opttype		-> 01 - 기본값 , 02- 공통코드(cmmcode 속성 값 필요), 03- 사용자, 04- 배포계획
 *					cmmcode		-> 공통코드
 *					optFlowId		-> 작업흐름 Id
 *					(opttype="02" cmmcode="REQ00001")
 * @param formName	값을 가져올 폼 이름
 * @param fd		값을 넣을 FormData()

 */
function gfnFormDataAutoJsonValue(formName,fd){
	//input, select, textarea 객체 구하기
	var fdInput = $('#'+formName+' input');
	var fdSelect = $('#'+formName+' select');
	var fdText = $('#'+formName+' textarea');
	
	//array merge
	var fdEle;
	
	fdEle = $.merge(fdInput,fdSelect);
	fdEle = $.merge(fdEle,fdText);
	
	$.each(fdEle, function(index, element){
		//id값이 없다면 수집하지 않음
		if(gfnIsNull(element.id)){
			return true;
		}
		//항목 작업흐름
		var optFlowId = element.getAttribute("optflowid");
		
		//항목 타겟
		var chgDetailOptTarget = element.getAttribute("opttarget");
		
		//항목 타겟 없는경우 normal
		if(gfnIsNull(chgDetailOptTarget)){
			chgDetailOptTarget = "01";
		}
		
		//항목 타입
		var chgDetailOptType = element.getAttribute("opttype");
		
		//항목 타입 없는경우 normal
		if(gfnIsNull(chgDetailOptType)){
			chgDetailOptType = "01";
		}
		
		//항목 공통코드
		var chgDetailCommonCd = element.getAttribute("cmmcode");
		
		//항목 공통코드 없는경우 공백
		if(gfnIsNull(chgDetailCommonCd)){
			chgDetailCommonCd = "";
		}
		
		//결과값에 포함시키지 않는 경우 제외
		var modifySetCd = element.getAttribute("modifyset");
		
		//수정 이력 저장 구분 값 없는경우 01
		if(gfnIsNull(modifySetCd)){
			modifySetCd = "01";
		}
		
		/* jsonData 세팅 */
		//개체 항목 명 (title)
		var eleTitle = element.title;
		
		//개체 항목 명 없는경우 id값이 항목 명
		if(gfnIsNull(eleTitle)){
			eleTitle = element.id;
		}
		
		//개체 값(value)
		var eleValue = element.value.replace(/\n/gi,'</br>');
		
		//체크 박스인경우 checked로 값 판별
		if(element.type == "checkbox"){
			eleValue = (element.checked)?"01":"02";
		}
		
		//jsonData
		var rtnVal = JSON.stringify({optNm:eleTitle,optVal:eleValue,chgDetailOptTarget:chgDetailOptTarget, chgDetailOptType:chgDetailOptType, chgDetailCommonCd:chgDetailCommonCd, modifySetCd:modifySetCd, optFlowId: optFlowId});
		
		//hidden인경우 String, 배포계획 제외
		if(chgDetailOptType != "04" && element.type == "hidden"){
			rtnVal = eleValue;
		}
		
		fd.append(element.id,rtnVal);
	});
}

/**
 * 객체 값이 숫자인지 확인하고 숫자가 아니라면 알파벳, 한글 지움
 * @param	숫자 유무 판별 객체 ID값
 * @returns 숫자인경우 true
 * 			숫자가 아닌경우 false
 * @desc	해당 함수 사용 후 같은 분기에 ajax로직이 있는 경우 에러가 발생한다. (숫자 체크 후 return되기 전 ajax이미 비동기 실행중)
 * 			if(gfnIsNumeric("objName")) 으로 ajax를 감싼다.
 */
function gfnIsNumeric(obj){
	var pattern = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z]/g;
	if($.isNumeric($("#"+obj).val()) == false || pattern.test($("#"+obj).val()) == true){
		if(!gfnIsNull($("#"+obj).val())){
			alert("숫자만 입력 가능합니다.");
			$("#"+obj).val($("#"+obj).val().replace( /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|a-z|A-Z]/g, ''));
			$("#"+obj).focus();
			return false;
		}else{
			return true;
		}
		
	}else{
		return true;
	}
}


/**
 * Input Box (ID or Class)의 값 길이를 체크한다.
 * @param objName	Input Box 아이디값 혹은 클래스이름
 * @param objDesc	해당 Input box의 이름
 * @param size		최대 글자 수
 */
function gfnIsLength(objName,objDesc,size){
	var fnObj = null;
	
	//객체명 ID값으로 검색
	if(!gfnIsNull($('#'+objName))){
		fnObj = $('#'+objName);
	}else if(!gfnIsNull($('.'+objName))){
		//객체명 Class값으로 검색
		fnObj = $('.'+objName);
	}
	//검색 결과 없는 경우 
	if(fnObj == null){
		alert(objDesc+" 입력 폼에 값이 입력되지 않았습니다.");
		return true;
	}else if(fnObj.val().length > size){
		alert(objDesc+" 입력 폼에 값이 지정된 길이를 초과했습니다.("+size+")");
		return false;
	}
	
}


/* printThis v1.5
* @desc Printing plug-in for jQuery
* @author Jason Day
*
* Resources (based on) :
* jPrintArea: http://plugins.jquery.com/project/jPrintArea
* jqPrint: https://github.com/permanenttourist/jquery.jqprint
* Ben Nadal: http://www.bennadel.com/blog/1591-Ask-Ben-Print-Part-Of-A-Web-Page-With-jQuery.htm
*
* Licensed under the MIT licence:
* http://www.opensource.org/licenses/mit-license.php
*
* (c) Jason Day 2015
*
* Usage:
*
* $("#mySelector").printThis({
* debug: false, * show the iframe for debugging
* importCSS: true, * import page CSS
* importStyle: false, * import style tags
* printContainer: true, * grab outer container as well as the contents of the selector
* loadCSS: "path/to/my.css", * path to additional css file - us an array [] for multiple
* pageTitle: "", * add title to print page
* removeInline: false, * remove all inline styles from print elements
* printDelay: 333, * variable print delay
* header: null, * prefix to html
* formValues: true * preserve input/form values
* });
*
* Notes:
* - the loadCSS will load additional css (with or without @media print) into the iframe, adjusting layout
*/
;
(function($) {
	var opt;
	$.fn.printThis = function(options) {
		
	window.print();
	return false;	
		
	opt = $.extend({}, $.fn.printThis.defaults, options);
	var $element = this instanceof jQuery ? this : $(this);
	
	var strFrameName = "printThis-" + (new Date()).getTime();
	
	if (window.location.hostname !== document.domain && navigator.userAgent.match(/msie/i)) {
	//Ugly IE hacks due to IE not inheriting document.domain from parent
	//checks if document.domain is set by comparing the host name against document.domain
	var iframeSrc = "javascript:document.write(\"<head><script>document.domain=\\\"" + document.domain + "\\\";</script></head><body></body>\")";
	var printI = document.createElement('iframe');
	printI.name = "printIframe";
	printI.id = strFrameName;
	printI.className = "MSIE";
	document.body.appendChild(printI);
	printI.src = iframeSrc;
	
	} else {
	//other browsers inherit document.domain, and IE works if document.domain is not explicitly set
	var $frame = $("<iframe id='" + strFrameName + "' name='printIframe' />");
	$frame.appendTo("body");
	}
	
	
	var $iframe = $("#" + strFrameName);
	
	//show frame if in debug mode
	if (!opt.debug) $iframe.css({
	position: "absolute",
	width: "0px",
	height: "0px",
	left: "-600px",
	top: "-600px"
	});
	
	
	//$iframe.ready() and $iframe.load were inconsistent between browsers 
	setTimeout(function() {
	
	//Add doctype to fix the style difference between printing and render
	function setDocType($iframe,doctype){
	var win, doc;
	win = $iframe.get(0);
	win = win.contentWindow || win.contentDocument || win;
	doc = win.document || win.contentDocument || win;
	doc.open();
	doc.write(doctype);
	doc.close();
	}
	if(opt.doctypeString){
	setDocType($iframe,opt.doctypeString);
	}
	
	var $doc = $iframe.contents(),
	$head = $doc.find("head"),
	$body = $doc.find("body");
	
	//add base tag to ensure elements use the parent domain
	$head.append('<base href="' + document.location.protocol + '//' + document.location.host + '">');
	
	//import page stylesheets
	if (opt.importCSS) $("link[rel=stylesheet]").each(function() {
	var href = $(this).attr("href");
	if (href) {
	var media = $(this).attr("media") || "all";
	$head.append("<link type='text/css' rel='stylesheet' href='" + href + "' media='" + media + "'>")
	}
	});
	
	//import style tags
	if (opt.importStyle) $("style").each(function() {
	$(this).clone().appendTo($head);
	//$head.append($(this));
	});
	
	//add title of the page
	if (opt.pageTitle) $head.append("<title>" + opt.pageTitle + "</title>");
	
	//import additional stylesheet(s)
	if (opt.loadCSS) {
	if( $.isArray(opt.loadCSS)) {
	jQuery.each(opt.loadCSS, function(index, value) {
	$head.append("<link type='text/css' rel='stylesheet' href='" + this + "'>");
	});
	} else {
	$head.append("<link type='text/css' rel='stylesheet' href='" + opt.loadCSS + "'>");
	}
	}
	
	//print header
	if (opt.header) $body.append(opt.header);
	
	//grab $.selector as container
	if (opt.printContainer) $body.append($element.outer());
	
	//otherwise just print interior elements of container
	else $element.each(function() {
	$body.append($(this).html());
	});
	
	//capture form/field values
	if (opt.formValues) {
	//loop through inputs
	var $input = $element.find('input');
	if ($input.length) {
	$input.each(function() {
	var $this = $(this),
	$name = $(this).attr('name'),
	$checker = $this.is(':checkbox') || $this.is(':radio'),
	$iframeInput = $doc.find('input[name="' + $name + '"]'),
	$value = $this.val();
	
	//order matters here
	if (!$checker) {
	$iframeInput.val($value);
	} else if ($this.is(':checked')) {
	if ($this.is(':checkbox')) {
	$iframeInput.attr('checked', 'checked');
	} else if ($this.is(':radio')) {
	$doc.find('input[name="' + $name + '"][value=' + $value + ']').attr('checked', 'checked');
	}
	}
	
	});
	}
	
	//loop through selects
	var $select = $element.find('select');
	if ($select.length) {
	$select.each(function() {
	var $this = $(this),
	$name = $(this).attr('name'),
	$value = $this.val();
	$doc.find('select[name="' + $name + '"]').val($value);
	});
	}
	
	//loop through textareas
	var $textarea = $element.find('textarea');
	if ($textarea.length) {
	$textarea.each(function() {
	var $this = $(this),
	$name = $(this).attr('name'),
	$value = $this.val();
	$doc.find('textarea[name="' + $name + '"]').val($value);
	});
	}
	} // end capture form/field values
	
	//remove inline styles
	if (opt.removeInline) {
	//$.removeAttr available jQuery 1.7+
	if ($.isFunction($.removeAttr)) {
	$doc.find("body *").removeAttr("style");
	} else {
	$doc.find("body *").attr("style", "");
	}
	}
	
	setTimeout(function() {
	if ($iframe.hasClass("MSIE")) {
	//check if the iframe was created with the ugly hack
	//and perform another ugly hack out of neccessity
	window.frames["printIframe"].focus();
	$head.append("<script> window.print(); </script>");
	} else {
	//proper method
	if (document.queryCommandSupported("print")) {
	$iframe[0].contentWindow.document.execCommand("print", false, null);
	} else {
	$iframe[0].contentWindow.focus();
	$iframe[0].contentWindow.print();
	}
	}
	
	//remove iframe after print
	if (!opt.debug) {
	setTimeout(function() {
	$iframe.remove();
	}, 1000);
	}
	
	}, opt.printDelay);
	
	}, 333);
	
	};
	
	//defaults
	$.fn.printThis.defaults = {
	debug: false, // show the iframe for debugging
	importCSS: true, // import parent page css
	importStyle: false, // import style tags
	printContainer: true, // print outer container/$.selector
	loadCSS: "", // load an additional css file - load multiple stylesheets with an array []
	pageTitle: "", // add title to print page
	removeInline: false, // remove all inline styles
	printDelay: 333, // variable print delay
	header: null, // prefix to html
	formValues: true, // preserve input/form values
	doctypeString: '<!DOCTYPE html>' // html doctype
	};
	
	//$.selector container
	jQuery.fn.outer = function() {
	return $($("<div></div>").html(this.clone())).html()
	}
})(jQuery);

/** 
 * 파일 업로드 Drag&Drop 동작
 * 산출물 기능에 맞게 개발된 Drag&Drop Upload 소스 - 20161006
 * gfnFileDragDropUpload
 * 
 * 현재 최대 5개 까지 멀티업로드 가능
 * 
 * obj : Drag&Drop 영역 오브젝트
 * returnFunction : 파일 업로드 후 실행될 함수
 *  ㄴ 파일 정보 files정보를 가지고 등록된 파일만큼 해당 함수를 호출한다. 
 */
function gfnFileDragDropUpload(obj,returnFunction){
	
	//등록 권한이 없는 경우 이벤트 중지
	if(typeof btnAuthInsertYn == "undefined" || btnAuthInsertYn != 'Y'){
		return false;
	}
    if(obj != null){
    	obj.on('dragenter', function (e){
    	    e.stopPropagation();
    	    e.preventDefault();
    	    $(obj).addClass('dragOn');
    	});
    	obj.on('dragover', function (e){
    	     e.stopPropagation();
    	     e.preventDefault();
    	});
    	obj.on('dragleave', function(e){
    		$(obj).removeClass('dragOn');
    	});
    	
    	obj.on('drop', function (e){
    		
    		e.preventDefault();
    		$(obj).removeClass('dragOn');
    		//.disabled인 경우 중지
			if($(obj).is('.disabled')){
				jAlert('미 사용중인 기능입니다.');
				return false;
			}
    	    var files = e.originalEvent.dataTransfer.files;
	   
    	    //멀티 업로드 5개 까지
    	    if(files.length > 5){
    	    	toast.push("한번에 5개만 전송하실 수 있습니다.");
    	    }else{
    	    	 //파일 AJAX 전송 함수 호출
	    	     $.each(files,function(idx,map){
	    	    	 if(!gfnIsNull(map)){
	    	    		 
	    	    		 // 파일 확장자 구하기.
	    	    		 ext =map.name.split(".").pop().toLowerCase();
	    	    		 
	    	    		 if(!gfnFileCheck(ext)){
	    	    			 toast.push("확장자가 [ " +ext + " ] 인 파일은 첨부가 불가능 합니다.");
	    	    			 return false;
	    	    		 };
	    	    		   
	    	    		 
	    	    		 if(map.size < 1){
	    	    			 toast.push(map.name+"<br>파일의 크기가 0Byte인 경우 업로드가 불가능합니다.");
	    	    		 }else{
	    	    			 eval(returnFunction(map));
	    	    		 }
	    	    	 }
	    	    	 
	    	     });
    	     }
    	});
    	$(document).on('dragenter', function (e){
    	    e.stopPropagation();
    	    e.preventDefault();
    	});
    	$(document).on('dragover', function (e){
    	  e.stopPropagation();
    	  e.preventDefault();
    	  $(obj).removeClass('dragOn');
    	});
    	
    	$(document).on('drop', function (e){
    	    e.stopPropagation();
    	    e.preventDefault();
    	});
	}
}

/**
 * 유효성 값 체크
 * @param arrObj 유효성 체크 하려는 객체와 타입 설정
 * var arrChkObj = {[Object],[SubType]}
 * var arrChkObj = {"email":{"type":"email","msg":"이메일 형식이 아닙니다."}};
 * 
 * [Type]
 * number	: 숫자인지 확인
 * length	: 문자열 최대 길이 체크
 * email	: 이메일 형식 체크
 * etc		: 정규 표현식 지정 값 체크
 * 
 * [SubType]
 * type		: [Type]
 * max		: [Type]이 length인 경우 문자열 최대 길이 지정 (한글은 2Byte로 계산)
 * min		: [Type]이 length인 경우 문자열 최소 길이 지정
 * msg		: 값이 형식에 맞지 않는 경우 출력하려는 메시지 내용
 * require=true	: 필수 입력 값 체크 (default = false)
 * +[Type]가 etc인 경우 [SubType]
 * pattern		: 매치하려는 정규 표현식
 * rpPattern	: 만약 값 치환이 필요한 경우 지정
 * 				  지정이 안된 경우 inputBox를 빨간 테두리로 변경하는 Class 삽입 동작
 * eventHandler	: 이벤트 발생 지정(key~, click, blur 등)
 * 
 * 		- 그 외 내용 추가 시 내용 삽입 - 
 * 2016-10-10			최초 작성(number,length,email)			진주영
 * 2018-08-06			숫자 parseInt 오류 수정					진주영
 */
function gfnInputValChk(arrObj){
    //오브젝트
    var chkObj;
    //조건 정규표현식
    var pattern;
    //치환 정규표현식
    var rpPattern;
    rtn = true;

    $.each(arrObj,function(objNm,objType){
        //오브젝트 구하기
        if(!gfnIsNull($('#'+objNm))){
            chkObj = $('#'+objNm);
        }else if(!gfnIsNull($('input[name='+objNm+']'))){
            //id값 없는경우 name으로 찾음
            chkObj = $('input[name='+objNm+']');
        }else{
        	//찾을 수 없는 오브젝트인 경우 each break;
            return false;
        }
        
        //숫자 인지 확인
        if(objType.type == "number"){
         //input 입력 후 input box 키 입력 시 발생
            rpPattern = /[^0-9]/g;
            
            $(chkObj).keyup(function(e){
                var onKeyVar = parseInt(e.key);
                if(rpPattern.test(onKeyVar)){
                    //숫자가 아닌 문자열을 공백으로 치환
                    $(this).val($(this).val().replace(rpPattern, ''));
			        $(this).focus();
			        return true;
                }
                else if(!gfnIsNull($(this).attr('min')) && parseInt($(this).val()) < parseInt($(this).attr('min'))){
                	//msg있는 경우 출력
                    if(!gfnIsNull(objType.msg)){
                    	jAlert(objType.msg,"알림");
                    }
                    //마지막 입력 값 지우기
                    $(this).val($(this).val().substring(0,$(this).val().length-1));
                    return true;
                }
                else if(!gfnIsNull($(this).attr('max')) && parseInt($(this).val()) > parseInt($(this).attr('max'))){
                	//msg있는 경우 출력
                    if(!gfnIsNull(objType.msg)){
                    	jAlert(objType.msg,"알림");
                    }
                    
                    //마지막 입력 값 지우기
                    $(this).val($(this).val().substring(0,$(this).val().length-1));
                    return true;
                }
                //max가 없는경우 9999999999999 기본값
                else if(gfnIsNull($(this).attr('max')) && parseInt($(this).val()) > 9999999999999){
                	jAlert("숫자 입력은 최대 9999999999999 까지 입력 가능합니다.","알림");
                	
                	//마지막 입력 값 지우기
                    $(this).val($(this).val().substring(0,$(this).val().length-1));
                    return true;
                }
                else{
                	//형식이 정상일 때 inputError Class가 존재한다면 제거
                    if($(this).hasClass("inputError")){
                        $(this).removeClass("inputError");
                    }
                }
                
            });
	     //영문 인지 확인
        }else if(objType.type == "english"){
	        	
	            $(chkObj).keyup(function(e){

					// 영문만 입력
                    if (!(event.keyCode >=37 && event.keyCode<=40)) {
                        var inputVal = $(this).val();
                        // 영문이 아닌것은 제거
                        var replaceVal = $(this).val(inputVal.replace(/[^a-z]/gi,'')); 
                    }else{
	                	//형식이 정상일 때 inputError Class가 존재한다면 제거
	                    if($(this).hasClass("inputError")){
	                        $(this).removeClass("inputError");
	                    }
	                }
	            });   
        }else if(objType.type == "length"){
        	
         //input 입력 후 input box 키 입력 시 발생
            $(chkObj).keyup(function(e){
            	//문자열 길이 확인
                if(!gfnIsNull($(this).val())){
                	//Byte값 저장 변수
                	var charByte = 0;
                	//실제 길이 변수
                    var len = 0;
                    for (i = 0; i < $(this).val().length; i++) {
                        var oneChar = $(this).val().charAt(i);
                        if (escape(oneChar).length > 4) {
                        	charByte += 2;
                        } else {
                        	charByte++;
                        }
                        if (charByte <= objType.max) {
                        	len = i+1;
                        }
                    }
                    if(charByte > objType.max){
                    	//msg있는 경우 출력
                        if(!gfnIsNull(objType.msg)){
                        	jAlert(objType.msg,"알림");
                        	
                        	//input box Class에 빨간 테두리 넣기
                        	//$(this).addClass("inputError");
                        }
                      //문자열 길이 오버된 경우 최대 문자 갯수 까지 자르기
                        $(this).val($(this).val().substring(0,len));
                    }else{
                    	//형식이 정상일 때 inputError Class가 존재한다면 제거
                        if($(this).hasClass("inputError")){
                            $(this).removeClass("inputError");
                        }
                    }
                }
            });
            
            //최소 길이 제한이 있는 경우 blur 조건
            if(!gfnIsNull(objType.min)){
            	$(chkObj).blur(function(e){
                    if($(this).val().length < objType.min){
                    	//msg있는 경우 출력
                        if(!gfnIsNull(objType.msg)){
                        	jAlert(objType.msg,"알림");
                        }
                        //input box Class에 빨간 테두리 넣기
                        $(this).addClass("inputError");
                    }else{
                    	//이메일 형식이 정상일 때 inputError Class가 존재한다면 제거
                        if($(this).hasClass("inputError")){
                            $(this).removeClass("inputError");
                        }
                    }
                });
            }
          //이메일 체크
        }else if(objType.type == "email"){
            //input 입력 후 input box 포커스를 잃었을 때 발생
            pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
            $(chkObj).blur(function(e){
                if(pattern.test($(this).val()) == false){
                	//msg있는 경우 출력
                    if(!gfnIsNull(objType.msg)){
                    	jAlert(objType.msg,"알림");
                    }
                    //input box Class에 빨간 테두리 넣기
                    $(this).addClass("inputError");
                }else{
                	//이메일 형식이 정상일 때 inputError Class가 존재한다면 제거
                    if($(this).hasClass("inputError")){
                        $(this).removeClass("inputError");
                    }
                }
            });
            //그외에 SubType 모두 지정 분기
        }else if(objType.type == "etc"){
        	//이벤트 모션
        	var eventHandler;
        	if(!gfnIsNull(objType.event)){
        		eventHandler = objType.event;
        	}
        	//정규식 패턴
        	if(!gfnIsNull(objType.pattern)){
        		//지역변수로 선언
        		var pattern = objType.pattern;
        	}
        	//정규식 치환 패턴
        	if(!gfnIsNull(objType.rpPattern)){
        		//지역변수로 선언
        		var rpPattern = objType.rpPattern;
        	}
        	$(chkObj).on(eventHandler,function(e){
        		if(pattern.test($(this).val()) == false){
                	//msg있는 경우 출력
                    if(!gfnIsNull(objType.msg)){
                    	jAlert(objType.msg,"알림");
                    }
                    
                    //rpPattern이 있는지 확인
                    if(!gfnIsNull(rpPattern)){
	                    $(this).val($(this).val().replace(rpPattern, ''));
				        $(this).focus();
                    }else{
                    	//input box Class에 빨간 테두리 넣기
                        $(this).addClass("inputError");
    			        return false;
                    }
                }else{
                	if(gfnIsNull(rpPattern)){
	                	//형식이 정상일 때 inputError Class가 존재한다면 제거
	                    if($(this).hasClass("inputError")){
	                        $(this).removeClass("inputError");
	                    }
                	}
                }
        	});
        }else if(objType.type == "regExp"){
        	if( objType.required  ||    										//required 속성  'true'  이면 필수값이므로 정규식 검사
         			( objType.required == false && !gfnIsNull($("#"+objNm).val()) ) ) { //required 속성  'false' 이면  필수값이 아니므로 빈값이면 정규식 미검사 , 빈값이 아니면 정규식 검사     
	        	//정규식 패턴
	        	if(!gfnIsNull(objType.pattern)) {
	        		var pattern = objType.pattern;
	        	}
	        	if(new RegExp(pattern).test($("#"+objNm).val()) == false){
	        		
	            	//msg있는 경우 출력
	                if(!gfnIsNull(objType.msg)){
	                	if(objType.msgType != "toast"){
	                		jAlert(objType.msg,"알림");
	                	}
	                	toast.push({body:objType.msg, type:'Warning'});
	                	rtn = false;
	                	$(chkObj).addClass("inputError");
	                }
	            }else{
	            	
	            	//형식이 정상일 때 inputError Class가 존재한다면 제거
	                if($(chkObj).hasClass("inputError")){
	                   $(chkObj).removeClass("inputError");
	                }
	            }
        	}
        }
      
    });
    return rtn;
}


/**
 * 데이터 저장 전 유효성 값 체크
 * @param arrObj 유효성 체크 하려는 객체와 타입 설정
 * var arrChkObj = {[Object],[SubType]}
 * var arrChkObj = {"email":{"type":"email","msg":"이메일 형식이 아닙니다."}};
 * 
 * [Type]
 * number	: 숫자인지 확인
 * length	: 문자열 최대 길이 체크
 * email	: 이메일 형식 체크
 * etc		: 정규 표현식 지정 값 체크
 * 
 * [SubType]
 * type		: [Type]
 * max		: [Type]이 length인 경우 문자열 최대 길이 지정 (한글은 2Byte로 계산)
 * min		: [Type]이 length인 경우 문자열 최소 길이 지정
 * msg		: 값이 형식에 맞지 않는 경우 출력하려는 메시지 내용
 * require=true	: 필수 입력 값 체크 (default = false)
 * +[Type]가 etc인 경우 [SubType]
 * pattern		: 매치하려는 정규 표현식
 * rpPattern	: 만약 값 치환이 필요한 경우 지정
 * 				  지정이 안된 경우 inputBox를 빨간 테두리로 변경하는 Class 삽입 동작
 * eventHandler	: 이벤트 발생 지정(key~, click, blur 등)
 */
function gfnSaveInputValChk(arrObj){
    //오브젝트
    var chkObj;
    //조건 정규표현식
    var pattern;
    //치환 정규표현식
    var rpPattern;
    var rtn = true;
  
    $.each(arrObj,function(objNm,objType){  
        //오브젝트 구하기
        if(!gfnIsNull($('#'+objNm))){
            chkObj = $('#'+objNm);
        }else if(!gfnIsNull($('input[name='+objNm+']'))){
            //id값 없는경우 name으로 찾음
            chkObj = $('input[name='+objNm+']'); 
        }else{
        	//찾을 수 없는 오브젝트인 경우 each break;
            return false;
        }
      
        // 숫자인지 확인
        if(objType.type == "number"){
        	pattern = /[^0-9]/g; 

                   if(pattern.test($(chkObj).val())){	
                	   // 문자인 경우
                	   $(chkObj).addClass("inputError");
                	   $(chkObj).focus();
                	   rtn = false;
                	   return false;
                   }
                  else if(!gfnIsNull(objType.min) && parseInt($(chkObj).val()) < parseInt(objType.min)){
                   	//msg있는 경우 출력
                       if(!gfnIsNull(objType.msg)){
                       		jAlert(objType.msg,"알림");
                       }
                       $(chkObj).addClass("inputError");
                	   $(chkObj).focus();
                       rtn = false;
                       return false;
                   }
                   else if(!gfnIsNull(objType.max) && parseInt($(chkObj).val()) > parseInt(objType.max)){
                   		//msg있는 경우 출력
                	   if(!gfnIsNull(objType.msg)){
                		   jAlert(objType.msg,"알림");
                       }
                	   
                       $(chkObj).addClass("inputError");
                	   $(chkObj).focus();
                       rtn = false;
                       //return false;
                   }
                   else{
                   	//형식이 정상일 때 inputError Class가 존재한다면 제거
                       if($(chkObj).hasClass("inputError")){
                    	   $(chkObj).removeClass("inputError");
                       }
                       rtn = true;
                   }
                   

   	     //영문 인지 확인
       }else if(objType.type == "english"){

    	   pattern = /[^a-zA-Z]/gi;
    	   
			// 영문이 아니라면
           if ( pattern.test($(chkObj).val()) ) {
        	   $(chkObj).addClass("inputError");
        	   $(chkObj).focus();
        	   rtn = false;
               return false;
           }else{
           	//형식이 정상일 때 inputError Class가 존재한다면 제거
               if($(chkObj).hasClass("inputError")){
            	   $(chkObj).removeClass("inputError");
               }
               rtn = true;
           }

       }else if(objType.type == "length"){
           //input 입력 후 input box 키 입력 시 발생
              	//문자열 길이 확인
                  if(!gfnIsNull($(chkObj).val())){
                  	//Byte값 저장 변수
                  	var charByte = 0;
                  	//실제 길이 변수
                      var len = 0;
                      for (i = 0; i < $(chkObj).val().length; i++) {
                          var oneChar = $(chkObj).val().charAt(i);
                          if (escape(oneChar).length > 4) {
                          	charByte += 2;
                          } else {
                          	charByte++;
                          }
                          if (charByte <= objType.max) {
                          	len = i+1;
                          }
                      }

                      // max값 보다 입력된 byte값이 크다면
                      if(charByte > objType.max){
                      	//msg있는 경우 출력
                          if(!gfnIsNull(objType.msg)){   
                          	jAlert(objType.msg,"알림");
                          }
                        //input box Class에 빨간 테두리 넣기
                         // $(chkObj).addClass("inputError");
	                   	  $(chkObj).focus();
	                      rtn = false;
	                      return false;
                      }else{
                      	//형식이 정상일 때 inputError Class가 존재한다면 제거
                          if($(chkObj).hasClass("inputError")){
                        	  $(chkObj).removeClass("inputError");
                          }
                          rtn = true;
                      }
                  }

              
              //최소 길이 제한이 있는 경우 blur 조건
              if(!gfnIsNull(objType.min)){

                      if($(chkObj).val().length < objType.min){
                      	//msg있는 경우 출력
                          if(!gfnIsNull(objType.msg)){
                          	jAlert(objType.msg,"알림");
                          }
                          //input box Class에 빨간 테두리 넣기
                          $(chkObj).addClass("inputError");
	                   	  $(chkObj).focus();
                          rtn = false;
                          return false;
                      }else{
                      	//이메일 형식이 정상일 때 inputError Class가 존재한다면 제거
                          if(chkObj.hasClass("inputError")){
                        	  chkObj.removeClass("inputError");
                          }
                      }

              }
            //이메일 체크
          }else if(objType.type == "email"){
              //input 입력 후 input box 포커스를 잃었을 때 발생
              pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
                  if(pattern.test($(chkObj).val()) == false){
                  	//msg있는 경우 출력
                      if(!gfnIsNull(objType.msg)){
                      	jAlert(objType.msg,"알림");
                      }
                      //input box Class에 빨간 테두리 넣기
                      $(chkObj).addClass("inputError");
                   	  $(chkObj).focus();
                   	  rtn = false;
                   	  return false;
                  }else{
                  	//이메일 형식이 정상일 때 inputError Class가 존재한다면 제거
                      if($(chkObj).hasClass("inputError")){
                    	  $(chkObj).removeClass("inputError");
                      }
                  }
              //그외에 SubType 모두 지정 분기
          }else if(objType.type == "etc"){
          	//이벤트 모션
          	var eventHandler;
          	if(!gfnIsNull(objType.event)){
          		eventHandler = objType.event;
          	}
          	//정규식 패턴
          	if(!gfnIsNull(objType.pattern)){
          		//지역변수로 선언
          		var pattern = objType.pattern;
          	}
          	//정규식 치환 패턴
          	if(!gfnIsNull(objType.rpPattern)){
          		//지역변수로 선언
          		var rpPattern = objType.rpPattern;
          	}
          	$($(chkObj)).on(eventHandler,function(e){
          		if(pattern.test($(chkObj).val()) == false){
                  	//msg있는 경우 출력
                      if(!gfnIsNull(objType.msg)){
                      	jAlert(objType.msg,"알림");
                      }
                      
                      //rpPattern이 있는지 확인
                      if(!gfnIsNull(rpPattern)){
                    	  $(chkObj).val($(chkObj).val().replace(rpPattern, ''));
                    	  $(chkObj).focus();
                      }else{
                      	//input box Class에 빨간 테두리 넣기
                          $(chkObj).addClass("inputError");
                       	  $(chkObj).focus();
                       	  rtn = false;
      			        return false;
                      }
                  }else{
                  	if(gfnIsNull(rpPattern)){
  	                	//형식이 정상일 때 inputError Class가 존재한다면 제거
  	                    if($(chkObj).hasClass("inputError")){
  	                    	$(chkObj).removeClass("inputError");
  	                    }
                  	}
                  }
          	});
          	// 정규식 
          }else if(objType.type == "regExp"){
        	if( objType.required  ||    										//required 속성  'true'  이면 필수값이므로 정규식 검사
         			( objType.required == false && !gfnIsNull($("#"+objNm).val()) ) ) { //required 속성  'false' 이면  필수값이 아니므로 빈값이면 정규식 미검사 , 빈값이 아니면 정규식 검사     
	        	//정규식 패턴
	        	if(!gfnIsNull(objType.pattern)) {
	        		var pattern = objType.pattern;
	        	}
	        	if(new RegExp(pattern).test($("#"+objNm).val()) == false){
	        		
	            	//msg있는 경우 출력
	                if(!gfnIsNull(objType.msg)){
	                	if(objType.msgType != "toast"){
	                		jAlert(objType.msg,"알림");
	                	}
	                	// keyUp 이벤트의 유효성 체크의 toast 중복으로 메시지 2번뜨는 현상 발생 으로 주석
	                	//toast.push({body:objType.msg, type:'Warning'});
	                	rtn = false;
	                	$(chkObj).addClass("inputError");
	                	$(chkObj).focus();
	                	return false;
	                }
	            }else{
	            	
	            	//형식이 정상일 때 inputError Class가 존재한다면 제거
	                if($(chkObj).hasClass("inputError")){
	                   $(chkObj).removeClass("inputError");
	                }
	            }
        	}
        }
    });
    return rtn;
}


/*****************************************************************************************/


function gfnInputValChk2(arrObj){
    //오브젝트
    var chkObj;
    //조건 정규표현식
    var pattern;
    //치환 정규표현식
    var rpPattern;
    $.each(arrObj,function(objNm,objType){
        //오브젝트 구하기
        if(!gfnIsNull($('#'+objNm))){
            chkObj = $('#'+objNm);
        }else if(!gfnIsNull($('input[name='+objNm+']'))){
            //id값 없는경우 name으로 찾음
            chkObj = $('input[name='+objNm+']');
        }else{
        	//찾을 수 없는 오브젝트인 경우 each break;
            return false;
        }
        if(objType.type == "etc"){
        	//이벤트 모션
        	var eventHandler;
        	if(!gfnIsNull(objType.event)){
        		eventHandler = objType.event;
        	}
        	//정규식 패턴
        	if(!gfnIsNull(objType.pattern)){
        		//지역변수로 선언
        		var pattern = objType.pattern;
        	}
        	//정규식 치환 패턴
        	if(!gfnIsNull(objType.rpPattern)){
        		//지역변수로 선언
        		var rpPattern = objType.rpPattern;
        	}
        	$(chkObj).on(eventHandler,function(e){
        		if(pattern.test($(this).val()) == false){
                	//msg있는 경우 출력
                    if(!gfnIsNull(objType.msg)){
                    	jAlert(objType.msg);
                    }
                    
                    //rpPattern이 있는지 확인
                    if(!gfnIsNull(rpPattern)){
	                    $(this).val($(this).val().replace(rpPattern, ''));
				        $(this).focus();
                    }else{
                    	//input box Class에 빨간 테두리 넣기
                        $(this).addClass("inputError");
    			        return false;
                    }
                }else{
                	if(gfnIsNull(rpPattern)){
	                	//형식이 정상일 때 inputError Class가 존재한다면 제거
	                    if($(this).hasClass("inputError")){
	                        $(this).removeClass("inputError");
	                    }
                	}
                }
        	});
        }else if(objType.type == "regExp"){
        	if( objType.required == true  ||    										//required 속성  'true'  이면 필수값이므로 정규식 검사
         			( objType.required == false && !gfnIsNull($("#"+objNm).val()) ) ) { //required 속성  'false' 이면  필수값이 아니므로 빈값이면 정규식 미검사 , 빈값이 아니면 정규식 검사     
        	//정규식 패턴
        	if(!gfnIsNull(objType.pattern)) {
        		var pattern = objType.pattern;
        	}
        	if(pattern.test($("#"+objNm).val()) == false){
            	//msg있는 경우 출력
                if(!gfnIsNull(objType.msg)){
                	jAlert(objType.msg);
                	return false;
                }
            }
        	}else{
        		return false;
        	}
        }else{
        	//number,length,email events = keyup, blur
        	$(chkObj).on("keyup blur",function(e){
        		//type이 빈 값이 아닐경우
        		if(!gfnIsNull(objType.type)){
        			//type이 2개 이상인 경우에만 루프
        			if(typeof objType.type == "object"){
        				$.each(objType.type,function(idx,map){
        					fnInCheck(e,objType,map);
        				});
        			}else{
        				fnInCheck(e,objType,objType.type);
        			}
        		}
        	});
        	
        }
    });
    //구문 체크 내부 함수
	function fnInCheck(e,objType,type){
		
		var targetObj = e.target
		if(type == "number"){
			var rpPattern = /[^0-9]/g;
			var onKeyVar = parseInt(e.key);
			if($.isNumeric(parseInt(onKeyVar)) == false){
            	//msg있는 경우 출력
                if(!gfnIsNull(objType.msg)){
                	jAlert(objType.msg);
                }
                //숫자가 아닌 문자열을 공백으로 치환
                $(targetObj).val($(targetObj).val().replace(rpPattern, ''));
            }
            if(!gfnIsNull($(targetObj).attr('min')) && $(targetObj).val() < $(targetObj).attr('min')){
            	//msg있는 경우 출력
                if(!gfnIsNull(objType.msg)){
                	jAlert(objType.msg);
                }
                $(targetObj).val('');
            }
		}else if(type == "length"){
			//문자열 길이 확인
            if(!gfnIsNull(targetObj.value)){
            	//Byte값 저장 변수
            	var charByte = 0;
            	//실제 길이 변수
                var len = 0;
                for (i = 0; i < targetObj.value.length; i++) {
                    var oneChar = targetObj.value.charAt(i);
                    if (escape(oneChar).length > 4) {
                    	charByte += 2;
                    } else {
                    	charByte++;
                    }
                    if (charByte <= objType.max) {
                    	len = i+1;
                    }
                }
                if(charByte > objType.max){
                	//msg있는 경우 출력
                    if(!gfnIsNull(objType.msg)){
                    	jAlert(objType.msg);
                    }
                  //문자열 길이 오버된 경우 최대 문자 갯수 까지 자르기
                    targetObj.value = targetObj.value.substring(0,len);
                }
            }
             //최소 길이 제한이 있는 경우 blur 조건
            if(!gfnIsNull(objType.min)){
                if(targetObj.value.length < objType.min){
                	//msg있는 경우 출력
                    if(!gfnIsNull(objType.msg)){
                    	jAlert(objType.msg);
                    }
                    //input box Class에 빨간 테두리 넣기
                    $(targetObj).addClass("inputError");
                }else{
                	//이메일 형식이 정상일 때 inputError Class가 존재한다면 제거
                    if($(targetObj).hasClass("inputError")){
                        $(targetObj).removeClass("inputError");
                    }
                }
            }
		}else if(type == "email"){
			//input 입력 후 input box 포커스를 잃었을 때 발생
			pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			if(pattern.test($(targetObj).val()) == false){
            	//msg있는 경우 출력
                if(!gfnIsNull(objType.msg)){
                	jAlert(objType.msg);
                }
                //input box Class에 빨간 테두리 넣기
                $(targetObj).addClass("inputError");
            }else{
            	//이메일 형식이 정상일 때 inputError Class가 존재한다면 제거
                if($(targetObj).hasClass("inputError")){
                    $(targetObj).removeClass("inputError");
                }
            }
		}
	}
}
/**
 * 특정 레이아웃 하이라이트 처리
 * (선택 영역 외에 영역을 마스크 처리 한다)
 * - 마스크 생성 후 선택 객체를 클릭하면 마스크가 제거된다.
 * - 마스크 생성 후 선택 영역이 10x10 이하 일경우 마스크 영역을 클릭하면 마스크가 제거된다.
 * - 마스크 생성 후 컨트롤 + 마우스 좌 클릭으로 마스크를 제거 할 수 있다.
 * @param objId 하이라이트 처리 하려는 객체
 * 예제:
 * gfnLayerHighLight('.button_complete.btn_exit')
 * gfnLayerHighLight('#REQ2016100700008')
 * gfnLayerHighLight('.reqChangeDiv.recentCmnt[reqid=REQ2016090300001]')
 * gfnLayerHighLight('#sprFlowChrtDiv')
 * 
 * 		- 그 외 내용 추가 시 내용 삽입 - 
 * 2016-11-03			최초 작성			진주영
 */
function gfnLayerHighLight(objId){
	//jQuery 객체 선택
    var obj = $(objId);
    
	//선택 객체의 offset
	var objIdOffset = $(obj).offset();
	
    //객체가 존재하지 않는 경우 구문 종료
    if(gfnIsNull($(obj))){ return false; }
    
    //마스크 객체 선언할 영역 선언
    holder = $('<div id="maskBox"/>').css({
            position: 'absolute',
            zIndex: 90240,
            opacity: 0.5,
            top:0,
    		left:0
          });
    //body에 삽입
    $(holder).appendTo('body');
    
    //이후 추가 옵션 설정을 위한 배열 < 변수 객체 선언
    maskBox = {
            top: createMaskBox('top'),
            left: createMaskBox('left'),
            right: createMaskBox('right'),
            bottom: createMaskBox('bottom'),
          };
    //해당 위치 가르키기
    //$(window).scrollTop($(obj).offset().top*0.7);
    //마스크 영역 생성
	function createMaskBox(position) {
		
		//마스크 영역의 css
		var maskStyle;
		
		//라인 영역의 css
		var lineStyle;
		
		/* 상단 마스크 영역
		 * (x,y) : (선택 객체 좌측 위치, 0) 
		 * (w x h) :  (선택 객체 실제 가로 길이 x 선택 객체 Top 위치)
		 */
		if(position == 'top'){
			maskStyle = {
					top: px(0),
			        left: px(objIdOffset.left),
			        width: px($(obj).outerWidth()),
			        height: px(objIdOffset.top)
			};
		    lineStyle = {width:'100%',
		    		height:'2px',
		    		bottom:0};
	    /* 좌측 마스크 영역
		 * (x,y) : (0,0) 
		 * (w x h) :  (선택 객체 좌측 위치 x 페이지 세로 길이)
		 */
		}else if(position == 'left'){
			maskStyle = {
					top: px(0),
			        left: px(0),
			        width: px(objIdOffset.left),
			        height: px($(document).height())
			};
		    lineStyle = {width:'2px',
		    		height:px($(obj).outerHeight()),
		    		right:0,
		    		top:objIdOffset.top};
	    /* 우측 마스크 영역
		 * (x,y) : ((선택 객체 좌측 위치 + 선택 객체의 실제 가로 길이), 0) 
		 * (w x h) :  (선택 객체 좌측 위치 x 페이지 세로 길이)
		 */
		}else if(position == 'right'){
			maskStyle = {
					top: px(0),
			        left: px((objIdOffset.left + $(obj).outerWidth())),
			        width: px(($(document).width() - (objIdOffset.left + $(obj).outerWidth()))),
			        height: px($(document).height())
			};
		    lineStyle = {width:'2px',
		    		height:px($(obj).outerHeight()),
		    		top:objIdOffset.top
		    		};
	    /* 하단 마스크 영역
		 * (x,y) : (선택 객체 좌측 위치, (선택 객체 상단 위치 + 선택 객체의 실제 세로 길이)) 
		 * (w x h) :  (선택 객체의 실제 가로 길이 x (페이지 세로 길이 - (선택 객체 상단 위치 + 선택 객체의 실제 세로 길이)))
		 */
		}else if(position == 'bottom'){
			maskStyle = {
					top: px((objIdOffset.top + $(obj).outerHeight())),
			        left: px(objIdOffset.left),
			        width: px($(obj).outerWidth()),
			        height: px(($(document).height()-(objIdOffset.top + $(obj).outerHeight())))
			};
		    lineStyle = {width:'100%',
		    		height:'2px'};
		}
		 return $('<div />').css(maskStyle).appendTo(holder).append($('<div class="maskLine" />').css(lineStyle));
	}
	$(window).resize(function(){
		//페이지 내용 로드 대기
		setTimeout(function(){
			maskBoxReSize();
			
			//해당 위치 가르키기
			//$(window).scrollTop(objIdOffset.top*0.7);
		},200);
	});
	
	//선택 객체의 position이 fixed인 경우 스크롤 시 마스크박스 재 정의 필요
	$(document).on('scroll touchmove mousewheel', function(e) {
		maskBoxReSize();
		});
	//선택 영역이 클릭할 수 없는 범위인 경우 마스크 클릭으로 마스크 제거
	if($(obj).outerWidth() <= 10 || $(obj).outerHeight() <= 10){
		$("#maskBox").on("click, mousedown",function(event){
			$(document).off('scroll touchmove mousewheel');
			$(this).remove();
		});
	}
	//하이라이트 처리된 영역 클릭, 마우스 누를 때 마스크 제거
	$(obj).on("click, mousedown",function(event){
			$(document).off('scroll touchmove mousewheel');
			$("#maskBox").remove();
    });
	
	//소수점 버리고 px 단위 추가
    function px(n) {
        return Math.round(n) + 'px';
    }
    //마스크 박스 위치 재 조정
    function maskBoxReSize(){
    	//선택 객체의 offset
		objIdOffset = $(obj).offset();
		
		//생성된 마스크 박스
		boxTop = this.maskBox.top;
		boxLeft = this.maskBox.left;
		boxRight = this.maskBox.right;
		boxBottom = this.maskBox.bottom;
		
		//박스 위치 조절 (css 수정과 함께 자식 객체 위치 값도 수정)
		$(boxTop).css({	//상단
	        left: px(objIdOffset.left),
	        width: px($(obj).outerWidth()),
	        height: px(objIdOffset.top), 
	        }).children('.maskLine').css({bottom:0});
		$(boxLeft).css({	//좌측
	        width: px(objIdOffset.left),
	        height: px($(document).height()), 
	        }).children('.maskLine').css({top:objIdOffset.top});
		
		$(boxRight).css({	//우측
	        left: px((objIdOffset.left + $(obj).outerWidth())),
	        width: px(($(document).width() - (objIdOffset.left + $(obj).outerWidth()))),
	        height: px($(document).height()), 
	        }).children('.maskLine').css({top:objIdOffset.top});
		
		$(boxBottom).css({	//하단
			top: px((objIdOffset.top + $(obj).outerHeight())),
			left: px(objIdOffset.left),
			width: px($(obj).outerWidth()),
			height: px(($(document).height()-(objIdOffset.top + $(obj).outerHeight()))), 
	        }).children('.maskLine').css({top:0});
		
    }
}

/**
 * 프로젝트 그룹, 프로젝트 데이터 변경 후에 상단 선택상자 다시 세팅
 * 
 * 		- 그 외 내용 추가 시 내용 삽입 - 
 * 2017-04-04			최초 작성				진주영
 * 2018-08-09			구조변경에따라 수정 필요	진주영
 */
function gfnPrjGrpSetting(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/cmm4000/cmm4000/selectCmm4000prjGrpSet.do"});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data) {
		data = JSON.parse(data);
    	
    	//조회에 실패하면 메인으로 보냄
    	if(data.errorYN == 'Y'){
    		//메인으로 이동해서, 프로젝트 그룹 및 그 외 정보들을 다시 세팅
    		location.href= "/cmm/cmm4000/cmm4000/selectCmm4000View.do";
    	}else{
    		//조회 성공인 경우 상단 선택상자 다시 세팅
    		//그룹 항목 제거
    		$('.prj_select_box > #header_grp_select > option').remove();
    		
    		//그룹 항목 추가
    		$.each(data.prjGrpList,function(){
    			$('.prj_select_box > #header_grp_select').append('<option value="'+this.prjGrpId+'">'+this.prjGrpNm+'</option>');
    		});
    		//선택 그룹 selected
    		$(".prj_select_box > #header_grp_select > option[value="+data.selPrjGrpId+"]").attr("selected", "true");
    		
    		//프로젝트 목록도 위와 같이 세팅
    		$('.prj_select_box > #header_select > option').remove();
    		//항목 추가
    		$.each(data.prjList,function(){
    			$('.prj_select_box > #header_select').append('<option value="'+this.prjId+'">'+this.prjNm+'</option>');
    		});
    		//선택 프로젝트 selected
    		$(".prj_select_box > #header_select > option[value="+data.selPrjId+"]").attr("selected", "true");
    	}
	});

	//AJAX 전송
	ajaxObj.send();
}

function searchEnterKey(e, obj){
	if(e.keyCode == 13) axdom("#" + obj.getItemId("btn_search")).click();
}



/**
 * 첨부파일 확장자 체크 ( 화이트 리스트 )
 * @param fileExt
 * @returns {Boolean}
 */
function gfnFileCheck( fileExt ){
	// 화이트 리스트가 아니라면 중지 업로드 중지.
	if( $.inArray(fileExt, ["doc","docx","hwp","pdf","ppt","pptx","xls","xlsx","zip","jpg","jpeg","png","gif","css","css2","csv","htm","htm2","html","js","avi","mp3","mpeg","mpg","psd","rar","spl","swf","tar","text","tga","tgz","tif","tiff","txt","wav","wav2","bmp","jar","zip","eml","cell"]) == -1) {
		return false;
   }
	return true;
}


/**
 * Byte수 구하여 문자열 자른 후 리턴
 *  
 * @param str
 * @param maxByte
 * @returns
 */
function gfnCutStrLen(str, maxByte, type) {
	//null 체크
	if(gfnIsNull(str)){
		return "";
	}
	for (b = i = 0; c = str.charCodeAt(i);) {
		b += c >> 7 ? 2 : 1;
		if (b > maxByte)
			break;
		i++;
	}
	//글자수 체크인경우 Byte 크기 리턴
	if(type == "byteLen"){
		return (b-1);
	}
	if( (b-1) < maxByte ){
		return str.substring(0, i);
	}else{
		return str.substring(0, i) + "...";
	} 
}

/**
 * 입력한 문자열이 maxByte를 초과할 경우 문자열을 잘라 리턴한다.
 *  
 * @param str 		입력받은 문자열
 * @param maxByte	최대 byte값
 * @returns
 */
function gfnByteLenCutStr(str, maxByte) {
	//null 체크
	if(gfnIsNull(str)){
		return "";
	}
	for (b = i = 0; c = str.charCodeAt(i);) {
		b += c >> 7 ? 2 : 1;
		if (b > maxByte)
			break;
		i++;
	}

	if( b > maxByte ){
		return str.substring(0, i);
	}
}

/**
 * Byte수 구하여 리턴
 *  
 * @param str
 * @param maxByte
 * @returns
 */
function gfnStrByteLen(str) {
	var byteLen = 0;
	for(i=0;i<str.length;i++){
		b = str.charCodeAt(i);
		byteLen += b >> 7 ? 2 : 1;
	}
	return byteLen;
}

/**
 * 링크값을 받아서 빈값인경우 '#'을 리턴
 * @param str
 * @returns str
 */
function gfnReqLink(reqLink){
	//링크 내용이 있는지 확인
	if(gfnIsNull(gfnReplace(reqLink, "http://","").trim()) || gfnIsNull(gfnReplace(reqLink, "https://","").trim())){
		reqLink = "#";
	}
	return reqLink;
}

/**
 * 지정된 사용자 쪽지 보내기
 * @param usrId	: 수신자 ID
 * reqId : 태그거는 요구사항 id
 * reqNm : 태그거는 요구사항 명
 * reqPrjId : 태그 거는 요구사항이 타 프로젝트인 경우(아닌 경우 null or 비움)
 * @returns
 */
function gfnAlarmOpen(usrId, reqId, reqNm, reqPrjId, reqPrjGrpId){
	//gfnLayerPopupClose();
	var data = {
		"sendChk": true,
		"usrIdChk": usrId,
		"arm_reqId":reqId,
		"arm_reqNm":reqNm,
		"reqPrjId":reqPrjId,
		"reqPrjGrpId":reqPrjGrpId
	};
	gfnLayerPopupOpen('/arm/arm1000/arm1000/selectArm1000View.do',data,"1000","700",'scroll');
}

/**
 * Date timestamp값으로 몇분전, 몇시간전, 몇일전 표기
 */
function gfnDtmAgoStr(dateTime){
	var subTime = new Date() - dateTime;
	
	var rtnStr = "";
	
	//시간
	var seqAgo = (60*1000);	//분
	var minAgo = seqAgo*60; //시
	var hourAgo = minAgo*24; //일
	
	subTime = parseInt(subTime);
	
	var formatDate = new Date(dateTime).format("yyyy-MM-dd HH:mm:ss");
	
	//1분 이내인 경우 방금
	if(subTime < seqAgo){
		rtnStr = "방금전 <small>("+formatDate+")</small>";
	}else if(subTime >= seqAgo && subTime < minAgo){
		rtnStr = parseInt(subTime/seqAgo)+"분 전 <small>("+formatDate+")</small>";
	}else if(subTime >= minAgo && subTime < hourAgo){
		rtnStr = parseInt(subTime/minAgo)+"시간 전 <small>("+formatDate+")</small>";
	}else if(subTime >= hourAgo && subTime < (hourAgo*28)){ //28일 까지
		rtnStr = parseInt(subTime/hourAgo)+"일 전 <small>("+formatDate+")</small>";
	}else{
		rtnStr = formatDate;
	}
	return rtnStr;
}

/**
 * 
 * 서버의 시간을 가져온다.
 * 
 * @param format
 * 예) yyyy-mm-dd
 * @returns {String}
 */
function gfnGetServerTime(format){
	var time="";
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/selectSelectServerTimeAjax.do"}
			,{"format" : format });
	//AJAX 전송 성공 함수
	ajaxObj.async = false;

	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		time=data.serverTime;		
				
	});
	
	//AJAX 전송 오류 함수 서버시간 조회 실패시 클라이언트 시간 조회
	ajaxObj.setFnError(function(xhr, status, err){
		var nowdate = new Date();
		if("yyyy-mm-dd"==format){
			time=nowdate.getFullYear()+"-"+ (nowdate.getMonth() + 1).zf(2) +"-"+nowdate.getDate().zf(2);
		}else if("yyyy-mm"==format){
			time=nowdate.getFullYear()+"-"+ (nowdate.getMonth() + 1).zf(2);
		}
		
		
 	});
	//AJAX 전송
	ajaxObj.send();

	return time;
}
/**
 * 
 * Token key 생성
 * 
 * @returns {String}
 */
function gfnGetApikey(){
	var key="";
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/selectApiKeyAjax.do"}
			,{  });
	//AJAX 전송 성공 함수
	ajaxObj.async = false;

	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		key=data.apiKey;		
				
	});
	
	//AJAX 전송 오류 함수 서버시간 조회 실패시 클라이언트 시간 조회
	ajaxObj.setFnError(function(xhr, status, err){
				
 	});
	//AJAX 전송
	ajaxObj.send();

	return key;
}

/**
 * 
 * 서버에 등록된 url정보를 조회한다.
 * 
 * @returns {Array}
 */
function gfnGetUrlList(){
	var list=[];
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/selectUrlListAjax.do"}
			,{  });
	//AJAX 전송 성공 함수
	ajaxObj.async = false;

	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		list=data.urlList;		
				
	});
	
	//AJAX 전송 오류 함수 서버시간 조회 실패시 클라이언트 시간 조회
	ajaxObj.setFnError(function(xhr, status, err){
				
 	});
	//AJAX 전송
	ajaxObj.send();

	return list;
}
/**
 * 
 * 엘리먼트의 byte입력 제한을 지정한다.
 * 엘리먼트의 maxlength 길이를 기준으로 byte수를 제한한다.
 * 예) 500 byte 제한
 *  <input type="text" title="서비스 명" class="input_txt" name="apiNm" id="apiNm" style="width:80%;" maxlength="500"  />
 * 
 * @param eventIdList
 */
function gfnByteCheckEvent(eventIdList){
	
	for(var i=0; i<eventIdList.length; i++ ){
		$('#' +eventIdList[i] ).keyup(function (e){
			var totalByte = 0;
			var content = $(this).val();
			var maxlength = $(this).attr("maxlength");
			
			var printContent = "";
			
			for(var j=0; j<content.length;j++){
				var currentByte = content.charCodeAt(j);
				
				
				if(currentByte > 128){
					totalByte +=2;
				}else{
					totalByte++;
				}
				
				if(totalByte <= maxlength){
					printContent+=content.substring(j,j+1);
				}
			}
			$(this).val(printContent);
		});
		
		$('#' +eventIdList[i] ).blur(function (e){
			var totalByte = 0;
			var content = $(this).val();
			var maxlength = $(this).attr("maxlength");
			var printContent = "";
			
			for(var j=0; j<content.length;j++){
				var currentByte = content.charCodeAt(j);
						
				if(currentByte > 128){
					totalByte +=2;
				}else{
					totalByte++;
				}
				
				if(totalByte <= maxlength){
					printContent+=content.substring(j,j+1);
				}
			}
			$(this).val(printContent);
		});
				
		$('#' +eventIdList[i] ).bind('paste',function (e){
			var totalByte = 0;
			var content = $(this).val();
			var maxlength = $(this).attr("maxlength");
			var printContent = "";
			
			for(var j=0; j<content.length;j++){
				var currentByte = content.charCodeAt(j);
				
				
				if(currentByte > 128){
					totalByte +=2;
				}else{
					totalByte++;
				}
				
				if(totalByte <= maxlength){
					printContent+=content.substring(j,j+1);
				}
			}
			$(this).val(printContent);
		});
	}
	
}


/**
 * 콜백함수
 */
var commonPopFunction ;
/**
 * 공통 코드를 조회할수 있는 팝업을 생성한다. 
 * 
 * 사용예)
 * gfnCommonPopup("공휴일", $('#holiday').val() ,false,"${sessionScope.loginVO.licGrpId}","CMM00002",function(objs){
			//objs 는 배열 오브젝트 구조로 ADM4100테이블의 컬럼명과 매칭된다.
   });
 * 
 * 
 * @param title 팝업의 타이틀명
 * @param param 텍스트박스에 기본셋팅될 정보
 * @param isMulti 멀티 선택가능여부 false 단일선택 , true 멀티선택가능
 * @param licGrpId 라이센스 그룹명
 * @param mstCd 공통코드 그룹명
 * @param pFunc 선택버튼을 눌렀을때 선택된정보를 가져오는 콜백함수
 */
function gfnCommonPopup(title,param,isMulti,licGrpId,mstCd,pFunc){
	commonPopFunction = pFunc;
	var data = {
			"title" : title , "param"  : param , "isMulti" : isMulti ,
			"licGrpId" : licGrpId , "mstCd" : mstCd  
		};
			
	gfnLayerPopupOpen('/cmm/cmm1000/cmm1300/selectCmm1300View.do',data, "480", "453",'scroll');
}

/**
 * 공통 함수의 선택버튼을 눌렀을때 사용되는 함수
 * @param selGrid
 */
function gfnCheckRow(selGrid,param){
	var chkList = selGrid.getList('selected');
	var pList = [];
	
	if(gfnIsNull(chkList)){
		jAlert("선택된 데이터가 없습니다. 그리드에서 데이터를 선택해주세요.", "알림창");
		return false;
	}
	
	$(chkList).each(function(idx, data) {
		if(data.__selected__){
			pList.push(data);
		}
	});
	if(gfnIsNull(param )){
		commonPopFunction(pList);
	}else{
		commonPopFunction(pList,param);
	}
	gfnLayerPopupClose();
}
/**
 * 
 * 사용자 정보를 조회할수 있는 팝업을 생성한다. 
 * 
 * 예)
 * 
 * gfnCommonUserPopup($('#dplUsrNm').val() ,false,function(objs){
				if(objs.length>0){
					$('#dplUsrId').val(objs[0].usrId);
					$('#dplUsrNm').val(objs[0].usrNm);
				}
			});
 * 
 * 
 * var data = { "usrNm" :  $('#dplUsrNm').val() , authGrpIds : [  ] };
 * gfnCommonUserPopup( data ,false,function(objs){
				if(objs.length>0){
					$('#dplUsrId').val(objs[0].usrId);
					$('#dplUsrNm').val(objs[0].usrNm);
				}
			});
 * 
 * @param param 텍스트박스에 기본셋팅될 정보 사용자 명 
 *          또는 오브젝트 형태로 처리할경우 { "usrNm" : 사용자명 , "authGrpIds" : [ 권한아이디1 , 권한아이디2... ]  }
 * @param isMulti 멀티 선택가능여부 false 단일선택 , true 멀티선택가능
 * @param pFunc
 */
function gfnCommonUserPopup(param,isMulti,pFunc){
	commonPopFunction = pFunc;
	var data={};
	var ajaxParam = "";
	var authParams = "";
	if(param instanceof Object){
		
		ajaxParam = "&searchPopTxt="+param.usrNm;
		if(!gfnIsNull(param.authGrpIds)){
			for(var i=0; i< param.authGrpIds.length; i++ ){
				if(i==0){
					authParams += param.authGrpIds[i];
				}else{
					authParams += "|"+param.authGrpIds[i];
				}
				ajaxParam += "&authGrpId="+param.authGrpIds[i];
			}
		}
		data = {
				"usrNm"  : param.usrNm , "authParams" : authParams ,   
				"isMulti" : isMulti   
		};
	}else{
		data = {
				"usrNm"  : param  , "isMulti" : isMulti   
			};
		ajaxParam = "&searchPopTxt="+param;
	}
	
	var userList = gfnSelectCmm1000CommonUserList(ajaxParam);
	if(gfnIsNull(userList )){
		gfnLayerPopupOpen('/cmm/cmm1000/cmm1000/selectCmm1000View.do',data, "680", "423",'scroll');
	}else{
		if(userList.list.length==1){
			commonPopFunction(userList.list);
		}else{
			gfnLayerPopupOpen('/cmm/cmm1000/cmm1000/selectCmm1000View.do',data, "680", "423",'scroll');
		}
	}
}

/**
 * 프로젝트 사용자 조회
 * @param ajaxParam
 * @returns {___anonymous99814_99815} 조회 결과 
 */
function gfnSelectCmm1000CommonUserList(ajaxParam){
	var retObj = {};
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/cmm1000/cmm1000/selectCmm1000CommonUserListAjax.do","loadingShow":false}
			,ajaxParam);
	//AJAX 전송 성공 함수
	ajaxObj.setProperty({"async":false});
	ajaxObj.setFnSuccess(function(data){
		retObj = JSON.parse(data);	
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
	return retObj;
}

/**
 * 
 * 테이블의 칼럼명과 테이블명을 전달하여 동적으로 콤보박스를 로드한다.
 * 
 * 
 * @param elementId 콤보박스가 생성될 콤보박스 엘리먼트 id
 * @param tableName 콤보내용의 조회 테이블 이름
 * @param idColumn 콤보의 코드정보로 지정될 칼럼 id
 * @param nameColumn 콤보의 뷰리스트업될 지정될 칼럼명
 * @param conditionColumnNames 조회시에 사용될 조건절 예) { 컬럼명1 : 조건값1 , 컬럼명2 : 조건값2 , 컬럼명3 : 조건값3 ..... }
 * 
 * 예)
 * var conditions = { 'LIC_GRP_ID' :'${sessionScope.loginVO.licGrpId}' };
	gfnInitDynamicComboBox("jenId", "JEN1000", " JEN_ID","JEN_NM",conditions);
 * 
 * 
 */
function gfnInitDynamicComboBox(elementId, tableName, idColumn,nameColumn,conditionColumnNames){
	
	var param = {  "tableName" : tableName , "idColumn" : idColumn , "nameColumn" : nameColumn       };
	var conditionColSize = 0;
	if(!gfnIsNull(conditionColumnNames)){
		$.each( conditionColumnNames, function(columnName, columnValue){
			param["condCol"+conditionColSize] = columnName;
			param["condVal"+conditionColSize] = columnValue;
			conditionColSize++;	
		});
	}
	
	param["conditionColSize"] = conditionColSize;
	
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/selectDynamicComboBoxAjax.do"}
			,param);
	//AJAX 전송 성공 함수
	ajaxObj.async = false;
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	var html = '';
    	html += '<option value="">선 택</option>';
    	$.each( data.comboList, function(idx, map){
    		html += '<option value="'+ map.comboId +'" >'+ map.comboName +'</option>';
    	});
    	$('#'+elementId).html(html);
	});
	//AJAX 전송
	ajaxObj.send();
	
}

/**
 * 선택한 조직의 상위 조직명을 조회 후 조회된 목록으로 조직명을 새로 만든다.
 * <br>
 * <br> ex) 현재 선택된 조직명이 기획1팀일 경우,
 * <br> '솔루션사업본부 > 기획사업부 > 기획1팀' 형태로 생성
 * 
 * @returns deptNames (상위 조직명)
 */
function gfnGetUpperDeptNames(selectDeptId){
	
	// 새로 생성한 조직명
	var deptNames = "";
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/adm/amd7000/adm7000/selectAdm7000UpperDeptListAjax.do"}
			,{ "deptId" : selectDeptId});
	
	ajaxObj.setProperty({"async":false});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		// 가져온 데이터를 역순으로 정렬
		var deptList = data.upperDeptList.reverse();
		
		// 이름 생성
		if( !gfnIsNull(deptList) ){
			for (var i = 0; i < deptList.length; i++){
				deptNames += deptList[i].deptName
				if(i != deptList.length-1 ){
					deptNames += " > ";
				}
			}
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		jAlert(data.message,"알림창");
		return;
	});
	
	//AJAX 전송
	ajaxObj.send();

	return deptNames;
}

/**
 * SVN Revision 선택 팝업
 * 
 * @param prjId 프로젝트 ID
 * @param selBtnChk 선택버튼 사용 유무
 * @param pFunc 선택시 CallBack 함수
 */
function gfnSvnRevisionPopup(prjId,selBtnChk,pFunc){
	commonPopFunction = pFunc;
	var data = {"prjId": prjId, "selBtnChk": selBtnChk  }; 
	 
	gfnLayerPopupOpen("/cmm/cmm1000/cmm1400/selectCmm1400View.do", data, '1300', '850','scroll');	
}


/**
 * 
 * 프로젝트 선택 팝업
 * 
 * @param param
 * @param pFunc
 */
function gfnCommonProjectPopup(param,pFunc){
	commonPopFunction = pFunc;
	var data = {
			"param"  : param   
		};
	gfnLayerPopupOpen("/cmm/cmm1000/cmm1100/selectCmm1100View.do", data, '640', '580','scroll');
}

/**
 * 공통 분류 선택 팝업
 * @param pFunc
 */
function gfnCommonClsPopup(pFunc){
	commonPopFunction = pFunc;
	var data = {};
	gfnLayerPopupOpen('/cmm/cmm1000/cmm1500/selectCmm1500View.do',data, "480", "540",'scroll');
}
/**  
 * 공통 분류 선택 닫기
 * @param reqClsId
 * @param reqClsNm
 */
function gfnSelectClsTree(reqClsId,reqClsNm){
	commonPopFunction(reqClsId,reqClsNm);
	gfnLayerPopupClose();
}


/**
 * 
 * 조직 정보를 조회할수 있는 팝업을 생성한다. 
 * 
 * 예)
		gfnCommonDeptPopup(selectDeptName, function(deptId,deptNm){
			$("#selectDeptName").val(deptNm);
			$('input[name=deptId]').attr('value',deptId);
		});
 * 
 * @param searchDeptNm 검색할 조직명 
 * @param pFunc
 */
function gfnCommonDeptPopup(searchDeptNm, pFunc){
	commonPopFunction = pFunc;
	var data = { "searchDeptNm"  : searchDeptNm };
	
	var retList = gefSelectAdm7000DeptList(searchDeptNm);
	if(gfnIsNull(retList.deptList)){
		gfnLayerPopupOpen('/cmm/cmm1000/cmm1200/selectCmm1200View.do', data, "850", "660", 'auto');
	}else{
		if(retList.deptList.length==1){
			
			var deptInfo = retList.deptList[0];
			
			// 루트가 아닐일경우
			if(deptInfo.lvl != 0){

				// 조직 ID
				var deptId = deptInfo.deptId;
				// 상위 조직을 포함한 조직명
				var deptNamesStr = gfnGetUpperDeptNames(deptInfo.deptId); 
				commonPopFunction(deptId, deptNamesStr);
			}
			else{
				// 선택된 조직이 루트라면 팝업 오픈
				gfnLayerPopupOpen('/cmm/cmm1000/cmm1200/selectCmm1200View.do', data, "850", "660", 'auto');
			}
			
		}else{
			gfnLayerPopupOpen('/cmm/cmm1000/cmm1200/selectCmm1200View.do', data, "850", "660", 'auto');
		}
	}
}

/**
 * 공통 - 조직 선택 팝업닫기
 * @param deptId 조직 ID
 * @param deptNm 조직 명
 */
function gfnSelectDeptTree(deptId,deptNm){
	commonPopFunction(deptId,deptNm);
	gfnLayerPopupClose();
}

/**
 * 조직명으로 조직을 검색한다. 검색된 조직 리스트를 리턴한다.
 * @param searchDeptNm 검색할 조직명
 * @return retObj 검색된 조직목록
 */
function gefSelectAdm7000DeptList(searchDeptNm){
	
	var retObj = {};
	var sendData = { "searchTxt" : searchDeptNm };
	
	var retObj = {};
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/adm/amd7000/adm7000/selectAdm7000DeptListAjax.do","loadingShow":false}
			,sendData);
	
	ajaxObj.setProperty({"async":false});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		retObj = JSON.parse(data);	
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
	
	return retObj;
}

/**
 * Object를 처리하고 팝업을 닫는다 
 * @param selGrid
 */
function gfnDataRow(selGrid){
	
	commonPopFunction(selGrid);
	
	gfnLayerPopupClose();
}
/**
 * 기준일 이전의 날짜를 조회처리 
 * 
 * @param date
 * @param day 예  7 7일이전 -7 7일 이후
 * @param format
 * @returns {String}
 */
function gfnGetDayAgo(date,day,format){
	
	var time;
	var retdate= new Date( date - (  day * 60 * 60 * 24  * 1000 ) );
	/**
	 * 사용되는 포멧을 추가하여 처리
	 */
	if("yyyy-mm-dd"==format){
		time=retdate.getFullYear()+"-"+ (retdate.getMonth() + 1).zf(2) +"-"+retdate.getDate().zf(2);
	}
	
	return time;
}
/**
 * 
 * 지정한 영역의 html을 PDF파일으로 축출한다.
 * 
 * @param obj 이미지 축출할 html obj
 * @param pdfName 출력할 PDF 명
 */
function gfnGetHtmlToPdf(obj,pdfName){
	var doc = new jsPDF();
	
	var layerIndex = $("#pdfEditor").length;
	if(layerIndex==0){
		$("body").prepend('<div id="pdfEditor" ><div>');
	}
				
	var specialElementHandlers = {
			'#pdfEditor': function (element, renderer) {
				 return true;
			}
	}
	gfnShowLoadingBar(true);
	html2canvas($(obj), {
		  onrendered: function(canvas) {
		 
		    // 캔버스를 이미지로 변환
		    var imgData = canvas.toDataURL('image/png');
		     
		    var imgWidth = 210; // 이미지 가로 길이(mm) A4 기준
		    var pageHeight = imgWidth * 1.414;  // 출력 페이지 세로 길이 계산 A4 기준
		    var imgHeight = canvas.height * imgWidth / canvas.width;
		    var heightLeft = imgHeight;
		     
		        var doc = new jsPDF('p', 'mm');
		        var position = 0;
		         
		        // 첫 페이지 출력
		        doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
		        heightLeft -= pageHeight;
		         
		        // 한 페이지 이상일 경우 루프 돌면서 출력
		        while (heightLeft >= 20) {
		          position = heightLeft - imgHeight;
		          doc.addPage();
		          doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
		          heightLeft -= pageHeight;
		        }
		        gfnShowLoadingBar(false);
		        // 파일 저장
		        doc.save(pdfName);
		  }
	});

}

/**
 * 
 * 날짜 기간을 검증한다.
 * 
 * @param fisrtDom
 * @param secondDom
 * @param term
 * @returns {Boolean}
 */
function gfnTermValid(fisrtDom,secondDom,term){
	var sFirstDay = $(fisrtDom).val();
	var sSecondDay = $(secondDom).val();
	var isValid = false;
	
	var dFirstDay= new Date( sFirstDay );
	var dSecondDay= new Date( sSecondDay );
	
	var diffDayMS = dFirstDay - dSecondDay;  
	
	if(Math.abs(diffDayMS)  >  Math.abs((term * 60 * 60 * 24  * 1000))){
		isValid = false;
	}else{
		isValid = true;
	}
		
	return isValid;
}



/**
 * 
 * 역할 정보를 조회할수 있는 팝업을 생성한다. 
 * 
 * 예)
 * 
 * gfnCommonAuthPopup($('#authGrpNm').val() ,false,function(objs){
				if(objs.length>0){
					$('#authGrpId').val(objs[0].authGrpId);
					$('#authGrpNm').val(objs[0].authGrpNm);
				}
			});
 * 
 * 
 * 
 * @param param 텍스트박스에 기본셋팅될 정보 사용자 명 
 * @param isMulti 멀티 선택가능여부 false 단일선택 , true 멀티선택가능
 * @param pFunc
 */
function gfnCommonAuthPopup(param,isMulti,pFunc){
	commonPopFunction = pFunc;
	var data={};
	var ajaxParam = "";
	
	data = {
			"authGrpNm"  : param  , "isMulti" : isMulti   
		};
	
	ajaxParam = "&searchPopTxt="+param;
	
	var authList = gfnSelectCmm1700CommonAuthList(ajaxParam);
	if(gfnIsNull(authList )){
		gfnLayerPopupOpen('/cmm/cmm1000/cmm1700/selectCmm1700View.do',data, "480", "423",'scroll');
	}else{
		if(authList.list.length==1){
			commonPopFunction(authList.list);
		}else{
			gfnLayerPopupOpen('/cmm/cmm1000/cmm1700/selectCmm1700View.do',data, "480", "423",'scroll');
		}
	}
}

/**
 * 역할 조회
 * @param ajaxParam
 * @returns {___anonymous99814_99815} 조회 결과 
 */
function gfnSelectCmm1700CommonAuthList(ajaxParam){
	var retObj = {};
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/cmm1000/cmm1700/selectCmm1700CommonAuthListAjax.do","loadingShow":false}
			,ajaxParam);
	//AJAX 전송 성공 함수
	ajaxObj.setProperty({"async":false});
	ajaxObj.setFnSuccess(function(data){
		retObj = JSON.parse(data);	
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
	return retObj;
}

/**
 * 
 * 배포 정보를 조회할수 있는 팝업을 생성한다. 
 * 
 * 예)
 * var data = { "dplNm" :  $('#dplNm').val() , "dplSts" : "01" }; 
 * dplSts 배포상태 01 : 대기, 02 : 승인, 03 : 종료 <- 코드는 미정
 * gfnCommonDplPopup(data ,false,function(objs){
				if(objs.length>0){
					$('#dplId').val(objs[0].dplId);
					$('#dplNm').val(objs[0].dplNm);
				}
			});
 * 
 * 
 * 
 * @param param 텍스트박스에 기본셋팅될 정보 사용자 명 
 * @param isMulti 멀티 선택가능여부 false 단일선택 , true 멀티선택가능
 * @param pFunc
 */
function gfnCommonDplPopup(param,isMulti,pFunc){
	commonPopFunction = pFunc;
	var data={};
	var ajaxParam = "";
	
	var dplNm = "";
	if(!gfnIsNull(param.dplNm)){
		dplNm = param.dplNm;
	}

	var dplSts = "";
	if(!gfnIsNull(param.dplSts)){
		dplSts = param.dplSts;
	}
	
	data = {
			"dplNm"  : dplNm  , "isMulti" : isMulti   
			 , "dplSts" : dplSts  
		};
	
	ajaxParam = "&searchPopTxt="+dplNm+"&dplSts="+dplSts;
	
	var dplList = gfnSelectCmm1600CommonDplList(ajaxParam);
	if(gfnIsNull(dplList )){
		gfnLayerPopupOpen('/cmm/cmm1000/cmm1600/selectCmm1600View.do',data, "680", "423",'scroll');
	}else{
		if(dplList.list.length==1){
			commonPopFunction(dplList.list);
		}else{
			gfnLayerPopupOpen('/cmm/cmm1000/cmm1600/selectCmm1600View.do',data, "680", "423",'scroll');
		}
	}
}

/**
 * 배포 조회
 * @param ajaxParam
 * @returns {___anonymous99814_99815} 조회 결과 
 */
function gfnSelectCmm1600CommonDplList(ajaxParam){
	var retObj = {};
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/cmm1000/cmm1600/selectCmm1600CommonDplListAjax.do","loadingShow":false}
			,ajaxParam);
	//AJAX 전송 성공 함수
	ajaxObj.setProperty({"async":false});
	ajaxObj.setFnSuccess(function(data){
		retObj = JSON.parse(data);	
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
	return retObj;
}

/**
 * 기준일 이전의 날짜를 조회처리 
 * 
 * @param date
 * @param day 예  7 7달이전 -7 7달 이후
 * @param format
 * @returns {String}
 */
function gfnGetMonthAgo(date,day,format){
	
	var time;
	
	var iDate =  new Date( date );
	
	var calMonth = 0;
	var calYear = 0 ;
	

	if(day>0){
		calYear = Math.floor( day  / 12 );
		if( (iDate.getMonth() + 1) < (day % 12)  ){
			calMonth = 12 + iDate.getMonth() + 1 - (day % 12) ;
			calYear++;
		}else{
			calMonth = iDate.getMonth() + 1 - (day % 12) ;
		}
	}else{
		calYear = Math.ceil( day  / 12 );
		if(iDate.getMonth() + 1 - (day % 12) > 12  ){
			calMonth = -12 + iDate.getMonth() + 1 - (day % 12) ;
			calYear--;
		}else{
			calMonth = iDate.getMonth() + 1 - (day % 12) ;
		}
	}
	
	/**
	 * 사용되는 포멧을 추가하여 처리
	 */
	if("yyyy-mm-dd"==format){
		var year = ( iDate.getFullYear() - calYear ) ;
		var month = (calMonth ).zf(2);
		var day = "";
		var end_day =0;
		if (month=="01" || month=="03" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12") {
			end_day = 31;
		}else if (month=="04" || month=="06" || month=="09" || month=="11") {
			end_day = 30;
		}else if (month=="02" && year%4 == 0) {
			end_day = 29;
		}else if (month=="02" && year%4 != 0) {
			end_day = 28;
		}
		
		if( Number(iDate.getDate().zf(2)) > end_day ){
			day = end_day;
		}else{
			day = iDate.getDate().zf(2);
		}
		
		time= year   +"-"+ month +"-"+day;
	}else if("yyyy-mm"==format){
		time=( iDate.getFullYear() - calYear ) +  "-"+ (calMonth ).zf(2);
	}

	//var retdate= new Date( date - (  day * 60 * 60 * 24  * 1000 ) );
	
	
	return time;
}


/**
 * 
 * 프로세스 목록 조회 팝업
 * 
 */
function gfnCommonProcessPopup(param,isMulti,pFunc){
	commonPopFunction = pFunc;
	var data={};
	var ajaxParam = "";
	
	var processNm = "";
	if(!gfnIsNull(param.processNm)){
		processNm = param.processNm;
	}


	data = {
			"processNm"  : processNm  , "isMulti" : isMulti   
			   
		};
	
	ajaxParam = "&searchPopTxt="+processNm;
	
	var dplList = gfnSelectCmm1800ProcessList(ajaxParam);
	if(gfnIsNull(dplList )){
		gfnLayerPopupOpen('/cmm/cmm1000/cmm1800/selectCmm1800View.do',data, "480", "423",'scroll');
	}else{
		if(dplList.list.length==1){
			commonPopFunction(dplList.list);
		}else{
			gfnLayerPopupOpen('/cmm/cmm1000/cmm1800/selectCmm1800View.do',data, "480", "423",'scroll');
		}
	}
}

/**
 * 프로세스 조회
 * @param ajaxParam
 * @returns {___anonymous99814_99815} 조회 결과 
 */
function gfnSelectCmm1800ProcessList(ajaxParam){
	var retObj = {};
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"/cmm/cmm1000/cmm1800/selectCmm1800ProcessListAjax.do","loadingShow":false}
			,ajaxParam);
	//AJAX 전송 성공 함수
	ajaxObj.setProperty({"async":false});
	ajaxObj.setFnSuccess(function(data){
		retObj = JSON.parse(data);	
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
	return retObj;
}