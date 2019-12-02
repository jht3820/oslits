<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/prj.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/vendors/smartWizrd/css/smart_wizard.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/vendors/smartWizrd/css/smart_wizard_theme_arrows.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslops/flw.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/common/spectrum.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/flowchart/jquery.flowchart.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="/vendors/smartWizrd/js/jquery.smartWizard.js"></script>
<script src="<c:url value='/js/common/spectrum.js'/>" ></script>
<script src="<c:url value='/js/flowchart/jquery.flowchart.js'/>"></script>
<script src="<c:url value='/js/panzoom/jquery.panzoom.min.js'/>"></script>

<style>
	/* 달력 */
	.ui-datepicker-trigger {height: 30px; width: 30px;}
	.fl {height:100%;}
	#startDt ,#endDt{ min-width: 100px;}
	#prjDesc { height:100%; padding: 8px 4px 8px 4px; font-size: 12px;}
	.sel_menu{ width: 60%; height: 100%;}
	.required_info{color:red; font-weight: bold; }
	.prjAcrm {ime-mode:disabled; }
	
</style>

<script>
//zTree, mask
var zTree;
var ax5Mask = new ax5.ui.mask();

//유효성 체크
var arrChkObj = {"prjNm":{"type":"length","msg":"프로젝트 (그룹)명은 200byte까지 입력이 가능합니다.",max:200}
				,"prjAcrm":{"type":"length","msg":"프로젝트 약어는 10byte까지 입력이 가능합니다.",max:10}
				,"prjDesc":{"type":"length","msg":"프로젝트 설명은 4000byte까지 입력이 가능합니다.",max:4000}
				,"ord":{"type":"number"}};

// 프로젝트 약어 영문 숫자여부 체크
var arrChkObj2 = {"prjAcrm":{"type":"english","engOption":"includeNumber"}};

// 프로젝트 약어 유효성 검증
var saveObjectValid = {
		"prjAcrm":{"type":"regExp","pattern":/^(?=.*?[A-Z])(?=.*?[0-9])|[A-Z]{3,10}$/ ,"msg":"프로젝트 약어는 영문 대문자 또는 영문 대문자, 숫자 조합으로 3~10자만 사용 가능합니다.", "required":true} 
};

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
	var mstCdStrArr = "CMM00001";
	var strUseYn = 'Y';
	var arrObj = [$("#useCd")];
	var arrComboType = ["",""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	// 유효성 체크
	gfnInputValChk(arrChkObj);
	
	// 프로젝트 약어 영문 숫자여부 체크
	gfnInputValChk(arrChkObj2);
	
	//프로젝트 목록 조회
	fnSelectPrjList();
	
	/* 달력 세팅(시작일, 종료일) */
	gfnCalRangeSet("startDt", "endDt");
	
	// 프로젝트 관리 전체 열기
	$(".menu .menu_expand_all").click(function(){
		zTree.expandAll(true);
	});

	// 프로젝트 관리 전체 닫기
	$(".menu .menu_collapse_all").click(function(){
		zTree.expandAll(false);
	});
	
	//프로젝트 목록 조회(좌측 트리)
	$("#btn_select_prjList").click(function(){
		//프로젝트 목록 조회
		fnSelectPrjList();
	});
	
	//프로젝트 추가 버튼
	$("#btn_insert_prjAddInfo").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var selZtree = zTree.getSelectedNodes()[0];
	
		//선택 객체가 없는경우 오류
		if(gfnIsNull(selZtree)){
			jAlert("프로젝트 그룹 또는 프로젝트를 선택해주세요.", "알림창");
			return false;
		}
		
		//사용유무 '02'인경우 하위 노드 추가 불가
		if(selZtree.useCd == "02"){
			jAlert("미 사용 프로젝트 그룹에는 추가할 수 없습니다.", "알림창");
			return false;
		}
		
		
		//프로젝트 그룹 > 프로젝트
		if( selZtree.level >= 2){
			jAlert("2뎁스 이상 추가할 수 없습니다.","알림창");
			return false;
		}else{
			//루트 선택의경우 프로젝트 그룹 추가
			if(gfnIsNull(selZtree.prjGrpCd)){
				var data = { "type":"group",prjGrpId:null};
				var popupHeight = "430";
			}
			//프로젝트 그룹 선택 시 프로젝트 추가
			else if(selZtree.prjGrpCd == "01"){
				var data = { "type":"project","prjGrpId":selZtree.prjId, "prjGrpNm":selZtree.prjNm,"startDt":selZtree.startDt,"endDt":selZtree.endDt};
				var popupHeight = "630";
			}
				gfnLayerPopupOpen('/prj/prj1000/prj1000/selectPrj1001View.do', data, '700', popupHeight, 'scroll');
			
		}
	});
	
	//프로젝트 수정 버튼 클릭 이벤트
	$("#btn_update_prjInfo").click(function(){
		//선택된 프로젝트 객체 저장
		var selZtree = zTree.getSelectedNodes()[0];
		
		//선택 객체가 없는경우 오류
		if(gfnIsNull(selZtree)){
			jAlert("프로젝트 그룹 또는 프로젝트를 선택해주세요.");
			return false;
		}
		
		//error있는경우 오류
		if($(".inputError").length > 0){
			jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
			$(".inputError")[0].focus()
			return false;
		}
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "prjInfoFrm";
		var strCheckObjArr = ["prjNm","ord"];
		var sCheckObjNmArr = ["프로젝트 (그룹)명","정렬 순서"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return false;	
		}

		// 단위 프로젝트인 경우
		if(selZtree.prjGrpCd == "02"){
			
			// 프로젝트 약어는 필수값
			var strCheckObjArr2 = ["prjAcrm"];
			var sCheckObjNmArr2 = ["프로젝트 약어"];
			if(gfnRequireCheck(strFormId, strCheckObjArr2, sCheckObjNmArr2)){
				return false;	
			}

			// 약어 유효성 검사
			if(!gfnInputValChk(saveObjectValid)){
				return false;	
			}
		}

		//jsonArray to json Map
		var prjInfo = {};
		$.each($("#prjInfoFrm").serializeArray(),function(idx, map){
			prjInfo[map.name] = map.value;
		});
			
		
		// 단위 프로젝트인경우 저장 전 약어 유효성 체크
		if(selZtree.prjGrpCd == "02"){
			// 유효성 검사
			if(!gfnSaveInputValChk(saveObjectValid)){
				return false;	
			}
		}
		
		// 프로젝트 정보 저장 전 유효성 검사
		if(!gfnSaveInputValChk(arrChkObj)){
			return false;	
		}

		//프로젝트정보 수정
		fnUpdatePrjInfoAjax(prjInfo,"normal", false);
	});
	
	//프로젝트 제거
	$("#btn_delete_prjDeleteInfo").click(function(){
		//선택된 프로젝트 객체 저장
		var selZtree = zTree.getSelectedNodes()[0];
		
		//선택 객체가 없는경우 오류
		if(gfnIsNull(selZtree)){
			jAlert("프로젝트 그룹 또는 프로젝트를 선택해주세요.");
			return false;
		}
		
		//루트 디렉토리 제거 불가
		if(selZtree.level == 0){
			jAlert("루트 디렉터리는 삭제 할 수 없습니다.","알림창");
		} else {
			if(selZtree.check_Child_State != -1){
				jAlert("프로젝트가 존재하기때문에 삭제할 수 없습니다. 프로젝트를 먼저 삭제해주세요.","경고");
			}else{
				//삭제 컨펌
				jConfirm("\n프로젝트를 삭제하시면 프로젝트에 생성한 모든 자료가 소멸됩니다.\n 그래도 삭제하시겠습니까?", "알림창", function( result ) {
					if( result ){
						jConfirm("프로젝트 삭제는 되돌릴 수 없습니다. \n그래도 삭제하시겠습니까?", "경고", function( result ) {
						if( result ){
							//프로젝트 삭제
							fnDeletePrjInfoAjax(selZtree);
						}
					});
					}
				});
			}
		}
	});
	
	// 프로젝트 약어 입력 keyup 이벤트
	$("#prjAcrm").keyup(function(e){
		 var inputVal = $("#prjAcrm").val();
		 // 입력된 값을 대문자로 변환
	 	$("#prjAcrm").val(inputVal.toUpperCase());
	});
	
});

/**
 * 조회버튼 클릭시 프로젝트 리스트 조회 AJAX
 */
function fnSelectPrjList(){
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1000/selectPrj1000ViewAjax.do'/>","loadingShow":false},{viewType:"all"});
	ajaxObj.async = false;
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	toast.push(data.message);
    	// zTree 설정 
	    var setting = {
	        data: {
	        	key: {
					name: "prjNm"
				},
	            simpleData: {
	                enable: true,
	                idKey: "prjId",
					pIdKey: "prjGrpId",
	            }
	        },
			callback: {
				onClick: function(event, treeId, treeNode){
					//입력 오류 초기화
					$(".inputError").removeClass("inputError");
					
					//우측 프로젝트 정보
					fnGetPrjInfoAjax(treeNode);
				}
			},
			view : {
				fontCss: function(treeId, treeNode){
					return (treeNode.useCd == "02")? {color:"#ddd"} :{};
				},
				showIcon : function(treeId, treeNode) {
					//그룹인경우 폴더형 아이콘으로 교체
					if(treeNode.level == 1){
						treeNode.isParent = true;
					}
					return true;
				}
			}
	    };
    	
    	var prjList = data.list;
    	prjList.unshift({prjId:null,prjGrpId:null,prjNm:"SW형상관리시범프로젝트",useCd:"01",open:"true"});
    	
		// zTree 초기화
	    zTree = $.fn.zTree.init($("#prjListJson"), setting, data.list);
		
		//폴더형 아이콘 세팅을 위해 zTree 갱신
		zTree.refresh();
		
		//목록 조회시 우측 정보 창 mask
		ax5Mask.open({
			zIndex:90,
			target: $("#selPrjInfoDiv"),
			content: "프로젝트 그룹 또는 프로젝트를 선택해주세요."
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

//zTree 선택 프로젝트 노드 - 정보 불러오기
function fnGetPrjInfoAjax(treeNode){
	
	// 노드 선택시 프로젝트 약어는 readonly
	$("#prjAcrm").attr("readonly",true);
	
	//최상위 노드인 경우
	if(gfnIsNull(treeNode.prjId)){
		//우측 정보 Mask
		ax5Mask.open({
			zIndex:90,
			target: $("#selPrjInfoDiv"),
			content: "프로젝트 그룹 또는 프로젝트를 선택해주세요."
		});
		
		//입력 정보 지우기
		$("#prjInfoFrm")[0].reset();
		return false;
	}
	
	var prjId = treeNode.prjId;
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1000/selectPrj1000SelPrjInfoAjax.do'/>","loadingShow":false}
			,{selPrjId: prjId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는 경우
		if(data.errorYN == "N"){
			//프로젝트 정보
			var prjInfo = data.prjInfo;
			
			//Mask 제거
			ax5Mask.close();
			
			// 우측 상세정보 화면초기화
			fnFormReset();
			
			//form data 채우기
			gfnSetData2Form(prjInfo,"prjInfoFrm");
			
			//그룹인경우
			if(treeNode.prjGrpCd == "01"){
				//그룹 정보 표시
				$(".prjGrpNameSpan").show();
				
				// 프로젝트 약어 숨김
				$(".prjAcrmDiv").hide();
				
				/* 달력 세팅(시작일, 종료일) */
				gfnCalRangeSet("startDt", "endDt");
			}
			//단위 프로젝트인경우
			else if(treeNode.prjGrpCd == "02"){
				//그룹 정보 숨김
				$(".prjGrpNameSpan").hide();
				
				// 프로젝트 약어 표시
				$(".prjAcrmDiv").show();
				// 프로젝트 약어가 없는 경우
				if( gfnIsNull(treeNode.prjAcrm) ){
					
					$("#prjAcrm").attr("readonly",false);
					//$("#prjAcrm").attr("class","fl readonly");
				}
				
				//부모 노드의 기간정보 가져오기
				var parentNode = treeNode.getParentNode();
				var prjGrpStartDt = parentNode.startDt;
				var prjGrpEndDt = parentNode.endDt;
				
				/* 달력 세팅(시작일, 종료일) */
				gfnCalRangeSet("startDt", "endDt", prjGrpStartDt, prjGrpEndDt);
			}
			
		}else{
			jAlert(data.message,"알림창");
		}
	});
	
	//AJAX 전송
	ajaxObj.send();
	
}

/**
*	프로젝트 정보 수정 함수
*	선택한 프로젝트 정보를 수정한다.
*/
function fnUpdatePrjInfoAjax(prjInfoObj, updateType, updateAsync){
	//프로젝트가 1개만 존재하는지 체크
	function inFnTreeChildChk(){
		//프로젝트 1개이상 존재 유무 체크
		var rootNode = zTree.getNodes()[0].children;
		var childCnt = 0;
		
		$.each(rootNode,function(idx, map){
			if(map.isParent){
				//자식 노드 없는경우 skip
				if(gfnIsNull(map.children)){
					return true;
				}
				
				//검색 노드의 자식 노드 수 합산
				childCnt += map.children.length;
			}
		});
		
		//프로젝트가 1개인경우 삭제 불가
		if(childCnt == 1){
			jAlert("로그인 사용자는 1개 이상의 프로젝트가 존재해야합니다.","경고");
			return false;
		}else{
			return true;
		}
	}
	
	//객체 넘어왔는지 확인
	if(gfnIsNull(prjInfoObj)){
		toast.push("프로젝트 그룹 또는 프로젝트를 선택해주세요.");
		return false;
	}
	
	if(updateType == "editUseCd"){ //사용유무 수정
		var parentNodeObj = prjInfoObj.getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 프로젝트의 사용여부를 변경해주세요.");
				return false;
			}
		}
		
		//변경 사용유무가 '아니요'인경우 프로젝트 1개 체크
		if(parentNodeObj.useCd == "01" && !inFnTreeChildChk()){
			return false;
		}
		
		//사용유무 반대로 변경
		prjInfoObj.useCd = (prjInfoObj.useCd=="01")?"02":"01";
	}else if(updateType == "normal"){
		var parentNodeObj = zTree.getSelectedNodes()[0].getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 프로젝트의 사용여부를 변경해주세요.");
				return false;
			}
		}
		
		//프로젝트 1개 체크
		if($("#useCd").val() == "02" && !inFnTreeChildChk()){
			return false;
		}
	}
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1000/updatePrj1000Ajax.do'/>", "async":updateAsync,"loadingShow":false}
			,prjInfoObj);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//수정이 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//이름 변경인 경우
    		if(updateType == "editRename"){
    			//우측 프로젝트 정보
				fnGetPrjInfoAjax(prjInfoObj);
    			
    			//상단 콤보박스 정보 변경
				fnHeaderHandle(prjInfoObj,"update_editName");
    			
    		//더블 클릭으로 사용유무 변경하는 경우
    		}else if(updateType == "editUseCd"){
    			//현재 선택된 프로젝트가 수정프로젝트와 같은 경우 폼 세팅
    			if(prjInfoObj.prjId == $("#prjId").val()){
    				$("#useCd").val(prjInfoObj.useCd);
    			}
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (prjInfoObj.useCd == "01")?"#000":"#ccc";
    			$("#"+prjInfoObj.tId+"_a").css({color:useColor});
    			
    			//우측 프로젝트 정보
				fnGetPrjInfoAjax(prjInfoObj);
    			
    			//상단 콤보박스 정보 변경
				fnHeaderHandle(prjInfoObj,"update_useCd");
    		//폼으로 정보 수정인 경우
    		}else if(updateType == "normal"){
    			//그룹 수정인경우
    			if($("#prjGrpCd").val() == "01"){
    				$("select#header_select optgroup[value="+$("#prjId").val()+"]").attr("data-ord",$("#ord").val());
    			}
    			//단위 수정인경우
    			else{
    				$("select#header_select option[value="+$("#prjId").val()+"]").attr("data-ord",$("#ord").val());
    			}
    			
    			//프로젝트명이 변경된 경우
    			if(zTree.getSelectedNodes()[0].prjNm != $("#prjNm").val()){
    				//폼값 수정이기 때문에 프로젝트값 수정 필요
    				zTree.getSelectedNodes()[0].prjNm = $("#prjNm").val();
    				
    				//상단 콤보박스 명 변경 - 그룹
	    			if($("#prjGrpCd").val() == "01"){
	    				$("select#header_select optgroup[value="+$("#prjId").val()+"]").attr("label","[그룹]"+$("#prjNm").val());
	    			}
	    			//상단 콤보박스 명 변경 - 단위
	    			else{
	    				$("select#header_select option[value="+$("#prjId").val()+"]").text($("#prjNm").val());
	    			}
    				//상단 콤보박스 정보 변경
	    			fnHeaderHandle(prjInfoObj,"update_editName");
    			}
    			//사용유무 변경된 경우
    			if(zTree.getSelectedNodes()[0].useCd != $("#useCd").val()){
    				var useColor = ($("#useCd").val() == "01")?"#000":"#ccc";
    				$("#"+zTree.getSelectedNodes()[0].tId+"_a").css({color:useColor});
    				
    				//폼값 수정이기 때문에 프로젝트값 수정 필요
    				zTree.getSelectedNodes()[0].useCd = $("#useCd").val();
    				
    				//자식 객체가 있는 경우에만 동작 - 그룹
    				if(typeof zTree.getSelectedNodes()[0].children != "undefined"){
	    				//자식 객체 사용유무 전체 변경
	    				$.each(zTree.getSelectedNodes()[0].children,function(){
	    					//사용유무는 부모값
	    					var subPrjInfoObj = this;
	    					subPrjInfoObj.useCd = $("#useCd").val();
	    					
	    					//상단 콤보박스 정보 변경
							fnHeaderHandle(prjInfoObj,"update_useCd");
	    					
	    					//수정 재귀
		    				fnUpdatePrjInfoAjax(subPrjInfoObj,"editSubUseCd",true);
		    			});
    				}
    				//상단 콤보박스 정보 변경
					fnHeaderHandle(prjInfoObj,"update_useCd");
    			}
    		//하위 프로젝트 사용유무 수정인경우 CSS 변경
    		}else if(updateType == "editSubUseCd"){
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (prjInfoObj.useCd == "01")?"#000":"#ccc";
    			$("#"+prjInfoObj.tId+"_a").css({color:useColor});
    			
    			//자식 객체가 있는 경우에만 동작 (재귀)
   				if(typeof prjInfoObj.children != "undefined"){
    				//자식 객체 사용유무 전체 변경
    				$.each(prjInfoObj.children,function(){
    					//사용유무는 부모값
    					var subPrjInfoObj = this;
    					subPrjInfoObj.useCd = $("#useCd").val();
    					
    					//수정 재귀
	    				fnUpdatePrjInfoAjax(subPrjInfoObj,"editSubUseCd",true);
	    			}); 
   				}
    		}
    		
    		//해당 노드 갱신
    		if(updateType == "normal"){	//폼값으로 수정
    			//그룹 기간 갱신
    			zTree.getSelectedNodes()[0].startDt = prjInfoObj.startDt;
    			zTree.getSelectedNodes()[0].endDt = prjInfoObj.endDt;
    			zTree.updateNode(zTree.getSelectedNodes()[0]);
    		}else{	//Json Object로 수정
    			zTree.updateNode(prjInfoObj);
    		}
    	}
    	
    	//재귀인경우 메시지 노출 안함
    	if(updateType != "editSubUseCd"){
    		toast.push(data.message);
    	}
	});

	//AJAX 전송
	ajaxObj.send();

}

//프로젝트 삭제 함수
//그룹의경우 존재하는 프로젝트가 있는지 확인(사용자 권한 외에 전체 사용자 대상)
function fnDeletePrjInfoAjax(prjInfo){
	
	//프로젝트 삭제인경우 프로젝트 1개 조건 체크
	if(prjInfo.level == 2){
		//프로젝트 1개이상 존재 유무 체크
		var rootNode = zTree.getNodes()[0].children;
		var childCnt = 0;
		
		$.each(rootNode,function(idx, map){
			if(map.isParent){
				//자식 노드 없는경우 skip
				if(gfnIsNull(map.children)){
					return true;
				}
				//검색 노드의 자식 노드 수 합산
				childCnt += map.children.length;
			}
		});
		
		//프로젝트가 1개인경우 삭제 불가
		if(childCnt == 1){
			jAlert("로그인 사용자는 1개 이상의 프로젝트가 존재해야합니다.","경고");
			return false;
		}
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1000/deletePrj1001Ajax.do'/>"}
			,{"prjId":prjInfo.prjId,"prjGrpCd": prjInfo.prjGrpCd, "prjGrpId":prjInfo.prjGrpId});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
       	if( data.delYN == "N" ){
       		jAlert(data.message,"경고");
       	}else{
       		toast.push(data.message, '알림창');
       		
       		//현재 선택된 프로젝트가 삭제된경우 새로고침
			if(prjInfo.prjId == "${sessionScope.selPrjId}"){
				jAlert("현재 선택된 프로젝트가 삭제되어 새로고침됩니다.","알림창",function(result){
					if($("#header_select option:eq(0)").length > 0){
						fnGoPrjChg($("#header_select option:eq(0)")[0]);
					}else{
						location.reload();
					}
				});
			}
       		
       		//상단 콤보박스 정보 변경
			fnHeaderHandle(prjInfo,"delete");
			 
       		var parentNode = prjInfo.getParentNode(); 
			//선택 노드 제거
			zTree.removeNode(zTree.getSelectedNodes()[0]);
			 
			//프로젝트 그룹의 프로젝트 수가 0인경우 부모 개체 isParent 조정
			if(gfnIsNull(parentNode.children) || parentNode.children.length == 0){
				parentNode.isParent = true;
				zTree.updateNode(prjInfo.getParentNode());
			}
			
			//목록 조회시 우측 정보 창 mask
			ax5Mask.open({
				zIndex:90,
				target: $("#selPrjInfoDiv"),
				content: "프로젝트 그룹 또는 프로젝트를 선택해주세요."
			});
       	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
       	toast.push(data.message);
	});
	//AJAX 전송
	ajaxObj.send();
}

function fnHeaderHandle(objInfo,type){
	//그룹
	if(objInfo.prjGrpCd == "01"){
		//그룹 - 추가
		if(type == "insert"){
			//정렬 순서로 추가됬는지 체크
			var ordFlag = false;
			
			//ord에 맞게 위치 추가
			$.each($("select#header_select > optgroup"), function(idx, map){
				//정렬 순서
				var ord = $(map).data("ord");
				
				if(!gfnIsNull(ord)){
					ord = parseInt(ord);
					
					//현재 추가된 프로젝트 그룹 정렬 순서보다 높은경우
					if(parseInt(objInfo.ord) < ord){
						$(map).before('<optgroup label="[그룹]'+objInfo.prjNm+'" value="'+objInfo.prjId+'" data-ord="'+objInfo.ord+'" disabled="disabled"></optgroup>');
						ordFlag = true;
						return false;
					}
					
				}
			});

			//정렬 추가 안된경우 마지막에 추가
			if(!ordFlag){
				$("select#header_select").append('<optgroup label="[그룹]'+objInfo.prjNm+'" value="'+objInfo.prjId+'" data-ord="'+objInfo.ord+'" disabled="disabled"></optgroup>');
			}
		}
		//이름 수정
		else if(type == "update_editName"){
			$("select#header_select optgroup[value="+objInfo.prjId+"]").attr("label","[그룹]"+objInfo.prjNm);
		}
		//사용유무 수정
		else if(type == "update_useCd"){
			if(objInfo.useCd == "02"){//사용
				$("select#header_select optgroup[value="+objInfo.prjId+"]").removeAttr("disabled");
			}else{//미사용
				$("select#header_select optgroup[value="+objInfo.prjId+"]").attr("disabled","disabled");
			}
		}
		//제거
		else if(type == "delete"){
			$("select#header_select optgroup[value="+objInfo.prjId+"]").remove();
		}
		//프로젝트 목록 select2
		headerPrjListSetting();
	}
	//단위
	else{
		//단위 - 추가
		if(type == "insert"){
			//프로젝트 최초 추가인경우 그룹 show
			if($("select#header_select optgroup[value="+objInfo.prjGrpId+"]").attr("disabled") == "disabled"){
				$("select#header_select optgroup[value="+objInfo.prjGrpId+"]").removeAttr("disabled");
			}
			
			//정렬 순서로 추가됬는지 체크
			var ordFlag = false;
			
			//ord에 맞게 위치 추가
			$.each($("select#header_select optgroup[value="+objInfo.prjGrpId+"] > option"), function(idx, map){
				//정렬 순서
				var ord = $(map).data("ord");
				
				if(!gfnIsNull(ord)){
					ord = parseInt(ord);
					
					//현재 추가된 프로젝트 그룹 정렬 순서보다 높은경우
					if(parseInt(objInfo.ord) < ord){
						$(map).before('<option value="'+objInfo.prjId+'" data-ord="'+objInfo.ord+'">'+objInfo.prjNm+'</option>');
						ordFlag = true;
						return false;
					}
					
				}
			});
			
			if(!ordFlag){
				//프로젝트 항목 추가
				$("select#header_select optgroup[value="+objInfo.prjGrpId+"]").append('<option value="'+objInfo.prjId+'" data-ord="'+objInfo.ord+'">'+objInfo.prjNm+'</option>');	
			}
		}
		//이름 수정
		else if(type == "update_editName"){
			$("select#header_select option[value="+objInfo.prjId+"]").text(objInfo.prjNm);
		}
		//사용유무 수정
		else if(type == "update_useCd"){
			if(objInfo.useCd == "01"){//사용
				//해당 프로젝트가 존재하는지 체크
				if($("select#header_select option[value="+objInfo.prjId+"]").length == 0){
					
					//없는경우 새로 추가
					fnHeaderHandle(objInfo,"insert");
				}else{
					$("select#header_select option[value="+objInfo.prjId+"]").show();
				}
			
				//상위 그룹이 hidden인지 체크
				var parentNode = zTree.getSelectedNodes()[0].getParentNode();
				if($("select#header_select optgroup[value="+objInfo.prjGrpId+"]").attr("disabled") == "disabled"){
					$("select#header_select optgroup[value="+objInfo.prjGrpId+"]").removeAttr("disabled");
				}
				
			}else{//미사용
				$("select#header_select option[value="+objInfo.prjId+"]").hide();
			
				//현재 선택된 프로젝트가 변경경우 새로고침
				if(objInfo.prjId == "${sessionScope.selPrjId}"){
					jAlert("현재 선택된 프로젝트가 변경되어 새로고침됩니다.","알림창",function(result){
						location.reload();
					});
				}
				//수정 또는 제거할때 그룹에 자식 개체가 없는경우 그룹 가리기
				inFnPrjGrpObjChk();
			}
		}
		//제거
		else if(type == "delete"){
			$("select#header_select option[value="+objInfo.prjId+"]").remove();
			
			//수정 또는 제거할때 그룹에 자식 개체가 없는경우 그룹 가리기
			inFnPrjGrpObjChk();
		}
		
		//프로젝트 목록 select2
		headerPrjListSetting();
	}
	
	function inFnPrjGrpObjChk(){
		//상위 그룹 프로젝트 수가 0인경우 hidden
		var parentNode = zTree.getSelectedNodes()[0].getParentNode();
		
		//빈 값이 아닌 경우
		if(!gfnIsNull(parentNode.children)){
			var useObjArray = [];
			var useObjCnt = 0;
			//사용중인 프로젝트 수 체크
			$.each(parentNode.children,function(idx,map){
				if(map.useCd == "01"){
					useObjCnt++;
					useObjArray.push(map.prjId);
				}
			});
			
			//한개의 프로젝트와 현재 미사용처리 프로젝트가 같은경우 가리기
			if(useObjCnt == 1 && useObjArray.indexOf(objInfo.prjId) == 0){
				//재 호출
				fnHeaderHandle(parentNode,"update_useCd");
			}
			//사용유무가 없는 경우 가리기
			else if(useObjCnt == 0){
				//재 호출
				fnHeaderHandle(parentNode,"update_useCd");
			}
		}else{ //빈값인경우 그룹 가리기
			//재 호출
			fnHeaderHandle(parentNode,"update_useCd");
		}
	}
}

/** 
 *	우측 상세정보 화면을 초기화 시킨다.
 */
function fnFormReset(){
	$('#searchFrm')[0].reset();
	$('#prjDesc').val('');
}

</script>
<div class="main_contents" style="position: relative;">
	<div class="prj_title">${sessionScope.selMenuNm }</div>
	<form id="searchFrm" ></form>
	<div class="tab_contents menu" id="projectInfoCreate">
		<div class="top_control_wrap">
			<span class="button_normal2 btn_select" id="btn_select_prjList"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;조회</span>
			<span class="button_normal2 btn_save" id="btn_update_prjInfo"><i class='fa fa-edit' aria-hidden='true'></i>&nbsp;저장</span>
		</div>
		<div class="menu_wrap">
			<div class="menu_ctrl_wrap">
				<div class="menu_ctrl_btn_wrap">
					<span class="button_normal2" id="btn_insert_prjAddInfo"><i class='fa fa-save' aria-hidden='true'></i>&nbsp;추가</span>
					<span class="button_normal2" id="btn_delete_prjDeleteInfo"><i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;삭제</span>
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span>
						<span class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>

				<div class="menu_lists_wrap" id="divMenu">
					<ul id="prjListJson" class="ztree"></ul>
				</div>
			</div>

			<div class="menu_info_wrap" id="selPrjInfoDiv">
				<form id="prjInfoFrm" name="prjInfoFrm" method="post">
					<input type="hidden" id="prjGrpId" name="prjGrpId"/>
					<input type="hidden" id="prjId" name="prjId"/>
					<input type="hidden" id="prjGrpCd" name="prjGrpCd"/>
					<input type="hidden" id="prjType" name="prjType" value="01"/>
					<div class="menu_row top_menu_row">
						<div class="menu_col1"><label for="prjNm">프로젝트 <span class="prjGrpNameSpan">그룹</span>명</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="prjNm" type="text"  name="prjNm" tabindex=1 /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for=startDt>프로젝트  <span class="prjGrpNameSpan">그룹</span>기간</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2" style="padding: 17px 20px 13px 10px;">
							<span class="fl"><input type="text" id="startDt" name="startDt" class="calendar_input" readonly="readonly" title="프로젝트 시작일" value="" style="height:90%;" /></span>
							<span class="calendar_bar fl">~</span>
							<span class="fl"><input type="text" id="endDt" name="endDt" class="calendar_input" readonly="readonly" title="프로젝트 종료일" value="" style="height:90%;" /></span>
						</div>
					</div>
					<div class="menu_row prjAcrmDiv">
						<div class="menu_col1"><label for="prjAcrm">프로젝트 약어</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="prjAcrm" type="text"  readonly="readonly" name="prjAcrm" maxlength="10" /></div>
					</div>
					<div class="menu_row prjAcrmDiv">
						<div class="menu_col1"><label for="prjTaskTypeNm">프로젝트 사업 구분</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="prjTaskTypeNm" type="text"  readonly="readonly" name="prjTaskTypeNm"/></div>
					</div>
					<div class="menu_row menu_col_textarea">
						<div class="menu_col1"><label for="prj_dsecription">프로젝트  <span class="prjGrpNameSpan">그룹</span>설명</label></div>
						<div class="menu_col2">
							<textarea class="input_note" id="prjDesc" name="prjDesc"></textarea>
						</div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="ord">정렬 순서</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="ord" type="number" min="1" name="ord" title="정렬 순서"  maxlength="10"></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="ord">사용유무</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><select class="sel_menu" name="useCd" id="useCd"></select></div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />