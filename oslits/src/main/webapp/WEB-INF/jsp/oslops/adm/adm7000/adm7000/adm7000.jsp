<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp"%>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/adm.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslits/dept.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<style>


#ord {border: 1px solid #ccc; font-size: 1em; width: 60%; min-width: 150px; height: 100%; padding-left: 4px;}
#useCd { width: 60%; min-width: 150px; height: 100%; }
/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
</style>

<script>
//zTree, mask
var zTree;
var ax5Mask = new ax5.ui.mask();
// 선택된 조직 및 하위조직이 담긴 배열, 조직 삭제시 사용
var chkDeptIdArr = [];
//선택된 조직 및 하위조직 노드가 담긴 배열, 조직 삭제시 사용
var chkDeptNodeArr = [];


//유효성 체크
var arrChkObjAdm7000 = {"deptName":{"type":"length","msg":"조직명은 200byte까지 입력이 가능합니다.",max:200}
				,"ord":{"type":"number"}
				,"deptEtc":{"type":"length","msg":"비고는 2000byte까지 입력이 가능합니다.", "max":2000}
				};

$(document).ready(function() {
	//트리메뉴 도움말 클릭
	$(".menu_tree_help").click(function(){
		if($(".menu_tree_helpBox").hasClass("boxOn")){
			$(".menu_tree_helpBox").hide();
			$(".menu_tree_helpBox").removeClass("boxOn");
		}else{
			$(".menu_tree_helpBox").show();
			$(".menu_tree_helpBox").addClass("boxOn");
		}
	});
	
	
	/********************************************************************
	 *	메뉴 관리 기능 부분 정의 시작												*
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
	var mstCdStrArr = "CMM00001";
	var strUseYn = 'Y';
	var arrObj = [$("#useCd")];
	var arrComboType = [""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , true);
	
	gfnInputValChk(arrChkObjAdm7000);
	
	//초기 메뉴 세팅
	fnSearchDeptList();

	// 메뉴 관리 전체 열기
	$(".menu .menu_expand_all").click(function(){
		zTree.expandAll(true);
	});

	// 메뉴 관리 전체 닫기
	$(".menu .menu_collapse_all").click(function(){
		zTree.expandAll(false);
	});

	// 조직 관리 조회 버튼 클릭 - 조회
	$("#btn_search_deptInfo").click(function(){
		fnSearchDeptList();
	});

	//	조직 수정 버튼 클릭 이벤트
	$("#btn_update_DeptInfo").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var selZtree = zTree.getSelectedNodes()[0];
		
		if(selZtree == null || selZtree == ""){
			jAlert("선택된 조직이 없습니다.", "알림창");
			return false;
		}
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "deptInfoFrm";
		var strCheckObjArr = ["deptName","ord"];
		var sCheckObjNmArr = ["조직명","순번"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		if(gfnIsNumeric("ord")){
			
			// 저장 전 유효성 체크
			if(!gfnSaveInputValChk(arrChkObjAdm7000)){
				return false;	
			}
			
			//메뉴정보 수정
			fnUpdateDeptInfoAjax($("#deptInfoFrm").serializeArray(),"normal", false);
		}
	});

		//  조직 추가 버튼 클릭 이벤트
		//	조직 추가시 DB 인서트 처리를 실행하며 등록이 성공되면 등록된 기본정보를 이용하여 조직 트리에 추가한다.
		$("#btn_insert_deptAddInfo").click(function(){
			//선택된 트리 엘레먼트 객체 저장
			var selZtree = zTree.getSelectedNodes()[0];
		
			if(selZtree == null || selZtree == ""){
				jAlert("선택된 조직이 없습니다.", "알림창");
				return false;
			}
			// 조직은 요구사항 분류와 달리 무한뎁스로 생성 가능 
			var deptId = selZtree.deptId;
			
			//인서트 로직 정상적으로 동작했을때 선택되어 있던 폴더 선택해제하고 DB 인서트된 정보를 이용하여 하위엘레먼트로 추가한다.
			//선택한 로우의 메뉴ID를 인자로 보냄
			fnInsertDeptPopupOpenAjax(selZtree);	
		});

		//	조직 삭제 버튼 
		$("#btn_delete_deptDeleteInfo").click(function(){
			//선택 조직 가져오기
			var menu = zTree.getSelectedNodes()[0];
			
			//선택 조직 없는경우 경고
			if(gfnIsNull(menu)){
				jAlert("선택된 분류가 없습니다.");
				return;
			}

			if(menu.level == 0){
				jAlert("루트 디렉터리는 삭제 할 수 없습니다.","알림창");
			} else {
				//선택한 div의 부모영역이 가진 자식 노드의 갯수로 하위메뉴 존재 여부를 판단한다.
				if(menu.length == 0){
					toast.push("조직을 선택하지 않았습니다. 조직을 선택해 주세요.");
				}
				else{
					// 사용자가 소속된 조직을 체크
					var result = fnDeptInUserChk(menu);
					var resultArr = result.split("&");
					
					// 삭제할 조직ID 문자열
					var strDeptId = resultArr[0];
					// 삭제 flag
					var delChk = resultArr[1];

					if(delChk == "Y"){
						jConfirm("선택된 조직에는 소속된 사용자가 없습니다. \n\n 해당 조직및 하위조직을 삭제하시겠습니까? \n", "알림창", function( result ) {
			   				if( result ){
			   					jConfirm("선택된 조직 및 하위조직까지 삭제되며 삭제 시 되돌릴 수 없습니다. \n그래도 삭제 하시겠습니까?", "알림창", function( result ) {
			   	   					if( result ){
			   	   						fnDeleteDeptInfoAjax(strDeptId);
			   	   					}
			   	   				});
			   				}
			   			});
					}
				}
			}
		});	

		
	/* 엑셀 조회 버튼 클릭 이벤트 */
	$("#btn_excel_menuInfo").click(function(){
		document.getElementById("searchFrm").action = "<c:url value='/adm/amd7000/adm7000/selectAdm7000ExcelList.do'/>";
		document.getElementById("searchFrm").submit();
		return false;
	});	
		
	/********************************************************************
	 *	조직 관리 기능 부분 정의 종료												*
 	*********************************************************************/
});



/********************************************************************
* 조직 관리 기능 부분 정의 시작												*
*********************************************************************/
/**
 * 	좌측 조직 선택했을때 조직 정보 표시 함수
 *	@param deptId 조직ID
 */
function fnGetDeptInfoAjax(deptId){

	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm7000/adm7000/selectAdm7000DeptInfoAjax.do'/>","loadingShow":false}
			,{ "deptId":deptId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		// 오류없을경우 Mask 제거
		ax5Mask.close();
		
		$("#deptEtc").val("");
		
    	//디테일폼 세팅
    	gfnSetData2Form(data, "deptInfoFrm");
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message,"알림창");
	});
	
	//AJAX 전송
	ajaxObj.send();
}

/**
 * 조회버튼 클릭시 조직 리스트 조회 AJAX
 */
function fnSearchDeptList(){

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/amd7000/adm7000/selectAdm7000DeptListAjax.do'/>","loadingShow":true});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		var listSize = data.deptList.length;
    	
		$('#deptInfoFrm')[0].reset();
		
    	toast.push(data.message);
    	// zTree 설정 
	    var setting = {
	        data: {
	        	key: {
					name: "deptName"
				},
	            simpleData: {
	                enable: true,
	                idKey: "deptId",
					pIdKey: "upperDeptId",
	            }
	        },
			callback: {
				onClick: function(event, treeId, treeNode){
					//우측 조직 정보
					fnGetDeptInfoAjax(treeNode.deptId);
				},
				/* onRightClick : function(event, treeId, treeNode){
					//조직명 변경 상자 나타내기
					zTree.editName(treeNode);
				},
				onRename : function(event, treeId, treeNode){
					//조직명 변경 이벤트 일어 날 경우, 조직 수정 이벤트 
					fnUpdateDeptInfoAjax(treeNode,"editRename",false);
				}, */
				onDblClick : function(event, treeId, treeNode){
					//노드 더블 클릭시 발생
				}
			},
			view : {
				fontCss: function(treeId, treeNode){
					return (treeNode.useCd == "02")? {color:"#ddd"} :{};
				},
				showIcon : function(treeId, treeNode) {
					
					if(typeof zTree != "undefined" && !treeNode.isParent){
						if(listSize>1){
							treeNode.isParent = true;
							zTree.updateNode(treeNode);
							zTree.refresh();	
						}
					}
					return true;
				}
			}
	    };

	    // zTree 초기화
	    zTree = $.fn.zTree.init($("#deptJson"), setting, data.deptList);
	    
	    // expandAll(false)를 추가해야 트리의 폴더를 한번 클릭 시 하위 메뉴가 보여진다.
	    // 추가하지 않을 경우 두번 클릭을 해야 폴더가 펼쳐진다.
	    zTree.expandAll(false);
	    
		//목록 조회시 우측 정보 창 mask
		ax5Mask.open({
			zIndex:90,
			target: $("#selDeptInfoDiv"),
			content: "조직을 선택해주세요."
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

/**
 * 	신규 조직 등록오픈 오픈
 *	해당 함수 호출시 새로운 조직을 등록할 수 있는 팝업을 오픈한다.
 *  @param deptObj 현재 선택된 조직
 */
function fnInsertDeptPopupOpenAjax(deptObj){
	
	var selDept = zTree.getSelectedNodes()[0];

	//사용유무 '02'인경우 하위 노드 추가 불가
	if(selDept.useCd == "02"){
		jAlert("미 사용 조직에는 추가할 수 없습니다.");
		return false;
	}
	
	// 팝업 오픈 시 조직ID, 순번, 레벨 정보를 팝업창에 전달한다.
	var sendData = { "deptId" : deptObj.deptId,
					 "ord" : deptObj.ord,
					 "lvl" : deptObj.lvl};
	
	// 조직 추가 팝업창 open
	gfnLayerPopupOpen('/adm/adm7000/adm7000/selectAdm7001View.do', sendData, "540", "430", "auto");
}

/**
 * 	신규 조직 등록 함수
 *	등록창에서 등록된 데이터(조직)를 zTree에 추가한다.
 *	등록 성공시 호출됨
 */
function fnInsertDeptInfoAjax(newDeptData){
	
	// 추가되는 조직은 모두 폴더 아이콘(부모형 메뉴)으로
	newDeptData.isParent = true;
	// 하위조직 추가
	zTree.addNodes(zTree.getSelectedNodes()[0], newDeptData);
	// 결과 메시지 출력	
	//toast.push(newDeptData.message);
}


/**
 * 	사용자가 소속되어 있는 조직들을 찾는다.
 *  @param selectDeptOjb 현재 선택된 조직
 */
function fnDeptInUserChk(selectDeptOjb){

	// 조직ID를 담은 배열초기화
	chkDeptIdArr.length = 0;
	// 조직 노드를 담은 배열 초기화
	chkDeptNodeArr.length = 0;
	
	// 선택된 조직 및 하위조직을 모두 가져온다.
	fnGetDeptList(selectDeptOjb);
	
	// deptId 문자열
	var strDeptId = "";
	
	// 배열에 담긴 조직ID를 문자열로 변환
	$(chkDeptIdArr).each(function(idx, chkDept) {
		strDeptId += ",'"+chkDept+"'";
	});
	
	// 처음 , 자르기
	strDeptId = strDeptId.substring(1);

	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm2000/adm2000/selectAdm2000ExistUsrInDeptAjax.do'/>","loadingShow":false}
			,{ "deptId" : strDeptId });

	ajaxObj.setProperty({"async":false});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
		var msg = "";
    	var list = data.userDeptList;
    	// 사용자가 속한 조직이 있는경우
   		if( !gfnIsNull(list) ){
   			$(list).each(function(idx, dept) {
   				msg += ", "+dept.deptName+" ";
   			});
   			
   			msg = msg.substring(1);
   			// 조직ID 문자열에 결과값 붙임
   			strDeptId += "&N";
   			
   			jAlert( "\n"+msg+"\n\n조직에는 사용자가 소속되어 있어 삭제할 수 없습니다. ","알림창");
   		}
   		else{
   			// 조직ID 문자열에 결과값 붙임
   			strDeptId += "&Y";
   		}
	});
	
	ajaxObj.setFnError(function(xhr, status, err){
    	data = JSON.parse(data);
    	jAlert(data.message,"알림창");
 	});
	
	//AJAX 전송
	ajaxObj.send();
	
	return strDeptId;
}


/**
 *	선택된 조직 및 하위조직을 모두 가져오는 함수
 *	@param selectDeptOjb 현재 선택된 조직
 */
function fnGetDeptList(selectDeptOjb){
	
	// 배열에 조직 노드를 담는다.
	chkDeptNodeArr.push(selectDeptOjb);
	// 배열에 조직ID를 담는다.
	chkDeptIdArr.push(selectDeptOjb.deptId);
	
	//자식 객체가 있는 경우에만 동작
	if(typeof selectDeptOjb.children != "undefined"){
		
		$.each(selectDeptOjb.children,function(){
			var subDeptObj = this;
			// 재귀
			fnGetDeptList(subDeptObj);
		});
	}
}

/** 
*	조직 삭제 함수
*	선택한 조직을 삭제한다.
*	@param strDeptId 삭제할 조직ID들의 문자열
*/
function fnDeleteDeptInfoAjax(strDeptId){

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm7000/amd7000/deleteAdm7000DeptInfoAjax.do'/>","loadingShow":false}
			,{ "deptId":strDeptId });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//삭제가 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		jAlert(data.message,"경고");
    		return false;
    	} 
    	else{
    		
    		$('#deptInfoFrm')[0].reset();
    		
    		// 삭제할 조직노드 중 최상위 조직노드를 가져온다.
    		var firstDeptNode = chkDeptNodeArr[0];	
    		// 삭제처리를 위해 역순 - 최하위 조직부터 배열에 배치
    		chkDeptNodeArr.reverse();
    		
    		// 최하위 조직부터 삭제처리
    		$(chkDeptNodeArr).each(function(idx, deptNode) {
    				zTree.removeNode(deptNode);
    		});
    		
    		// 삭제할 조직 중 최상위 조직의 부모노드를 가져옴
    		var parentNode = firstDeptNode.getParentNode();
    		// 폴더형으로 변경
    		parentNode.isParent = true;
    		// 트리 업데이트
    		zTree.updateNode(parentNode);
    	}
    	
    	toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
}

/**
*	조직 정보 수정 함수
*	선택한 조직정보를 수정한다.
*	@param deptObj 수정할 조직노드
*	@param updateType 수정 타입
*	@param updateAsync 동기/비동기
*/
function fnUpdateDeptInfoAjax(deptObj, updateType, updateAsync){
	
	//객체 넘어왔는지 확인
	if(typeof deptObj == "undefined" || deptObj == null){
		toast.push("선택된 조직이 없습니다.");
		return false;
	}
	
	//error있는경우 오류
	if($(".inputError").length > 0){
		jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.\n항목을 확인해주세요.","알림");
		$(".inputError")[0].focus()
		return false;
	}
	
	/* 필수입력값 체크 공통 호출 */
	var strFormId = "deptInfoFrm";
	var strCheckObjArr = ["deptName","ord"];
	var sCheckObjNmArr = ["조직명","순번"];
	if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
		return false;	
	}
	
	// 루트(최상위 조직)는 순번 0이므로 순번은 1부터 입력가능
	var chkOrd = $('#ord').val();
	if(chkOrd < 1){
		jAlert("순번은 1 이상 입력가능합니다.");
		$('#ord').val("");
		return false;
	}
	
	if(updateType == "editUseCd"){ //사용유무 수정
		var parentNodeObj = deptObj.getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 조직의 사용여부를 변경해주세요.");
				return false;
			}
		}
		
		//사용유무 반대로 변경
		deptObj.useCd = (deptObj.useCd=="01")?"02":"01";
	}else if(updateType == "normal"){
		var parentNodeObj = zTree.getSelectedNodes()[0].getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 조직의 사용여부를 변경해주세요.");
				return false;
			}
		}
	}
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm7000/adm7000/updateAdm7000DpteInfoAjax.do'/>", "async":updateAsync,"loadingShow":false}
			,deptObj);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//수정이 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		// 조직명 변경인 경우
    		if(updateType == "editRename"){
    			//우측 조직 정보
				fnGetDeptInfoAjax(deptObj.deptId);
    		//더블 클릭으로 사용유무 변경하는 경우
    		}else if(updateType == "editUseCd"){
    			//현재 선택된 조직이 수정조직과 같은 경우 폼 세팅
    			if(deptObj.deptId == document.deptInfoFrm.deptObj.value){
    				$("#useCd").val(deptObj.useCd);
    			}
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (deptObj.useCd == "01")?"#000":"#ccc";
    			$("#"+deptObj.tId+"_a").css({color:useColor});
    			
    			//우측 조직 정보
				fnGetDeptInfoAjax(deptObj.deptId);
    			
    		//폼으로 정보 수정인 경우
    		}else if(updateType == "normal"){
    			//조직명이 변경된 경우
    			if(zTree.getSelectedNodes()[0].deptName != $("#deptName").val()){
    				//폼값 수정이기 때문에 조직값 수정 필요
    				zTree.getSelectedNodes()[0].deptName = $("#deptName").val();
    			}
    			//사용유무 변경된 경우
    			if(zTree.getSelectedNodes()[0].useCd != $("#useCd").val()){
    				var useColor = ($("#useCd").val() == "01")?"#000":"#ccc";
    				$("#"+zTree.getSelectedNodes()[0].tId+"_a").css({color:useColor});
    				
    				//폼값 수정이기 때문에 조직값 수정 필요
    				zTree.getSelectedNodes()[0].useCd = $("#useCd").val();
    				
    				//자식 객체가 있는 경우에만 동작
    				if(typeof zTree.getSelectedNodes()[0].children != "undefined"){
	    				//자식 객체 사용유무 전체 변경
	    				$.each(zTree.getSelectedNodes()[0].children,function(){
	    					//사용유무는 부모값
	    					var subDeptObj = this;
	    					subDeptObj.useCd = $("#useCd").val();
	    					
	    					//수정 재귀
		    				fnUpdateDeptInfoAjax(subDeptObj,"editSubUseCd",true);
		    			});
    				}
    			}
    		//하위 조직 사용유무 수정인경우 CSS 변경
    		}else if(updateType == "editSubUseCd"){
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (deptObj.useCd == "01")?"#000":"#ccc";
    			$("#"+deptObj.tId+"_a").css({color:useColor});
    			
    			//자식 객체가 있는 경우에만 동작 (재귀)
   				if(typeof deptObj.children != "undefined"){
    				//자식 객체 사용유무 전체 변경
    				$.each(deptObj.children,function(){
    					//사용유무는 부모값
    					var subDeptObj = this;
    					subDeptObj.useCd = $("#useCd").val();
    					
    					//수정 재귀
	    				fnUpdateDeptInfoAjax(subDeptObj,"editSubUseCd",true);
	    			}); 
   				}
    		}
    		
    		//해당 노드 갱신
    		if(updateType == "normal"){	//폼값으로 수정
    			zTree.updateNode(zTree.getSelectedNodes()[0]);
    		}else{	//Json Object로 수정
    			zTree.updateNode(deptObj);
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

/********************************************************************
* 조직 관리 기능 부분 정의 종료												*
*********************************************************************/
</script>

<div class="main_contents">
	<div class="dept_title">${sessionScope.selMenuNm }</div>
	<form id="searchFrm" ></form>
	<div class="tab_contents menu">
		<div class="top_control_wrap">
			<span style="float:left;margin-right: 20px;">*조직을 설정할 수 있습니다.</span>
			<span class="menu_tree_help"><i class="fa fa-question"></i>
					<div class="menu_tree_helpBox">
						<span>
							[좌측 트리 메뉴 기능 안내]<br/>
							<br/>
							&nbsp;-메뉴 클릭: 메뉴 상세 정보 보기<br/>
							&nbsp;-더블 클릭: 폴더형 메뉴의 경우 하위 메뉴 보기<br/>
							&nbsp;<span style="margin-left: 71px;"></span>하위 메뉴의 경우 사용여부 변경<br/>
							&nbsp;-우측 클릭: 메뉴명 변경
						</span>
					</div>
				</span>
				<span class="button_normal2 btn_inquery" id="btn_search_deptInfo"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;조회</span>
				<span class="button_normal2 btn_save" id="btn_update_DeptInfo"><i class='fa fa-edit' aria-hidden='true'></i>&nbsp;정보수정</span>
				<span class="button_normal2 btn_excel" id="btn_excel_menuInfo"><i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;엑셀</span>
		</div>

		<div class="menu_wrap">
			<div class="menu_ctrl_wrap">
				<div class="menu_ctrl_btn_wrap">
					<span class="button_normal2 btn_menu_add" id="btn_insert_deptAddInfo"><i class='fa fa-save' aria-hidden='true'></i>&nbsp;추가</span>
					<span class="button_normal2 btn_menu_del" id="btn_delete_deptDeleteInfo"><i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;삭제</span>
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span>
						<span class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>

				<div class="menu_lists_wrap" id="divMenu">
					<ul id="deptJson" class="ztree"></ul>
				</div>
			</div>

			<div class="menu_info_wrap" id="selDeptInfoDiv">
				<form id="deptInfoFrm" name="deptInfoFrm" method="post">
 					<input type="hidden" id="licGrpId" name="licGrpId"/>
 					<input type="hidden" id="lvl" name="lvl"/>
					<div class="menu_row top_menu_row">
						<div class="menu_col1"><label for="deptId">조직 ID</label></div>
						<div class="menu_col2"><input id="deptId" type="text"  name="deptId" class="readonly" readonly="readonly" tabindex=1 /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="upperDeptId">상위 조직 ID</label></div>
						<div class="menu_col2"><input id="upperDeptId" type="text" name="upperDeptId" class="readonly" readonly="readonly" tabindex=2 /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="deptName">조직명</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="deptName" type="text" name="deptName" value="" maxlength="200"></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="ord">순번</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input class="ord" id="ord" type="number" min="1" name="ord" value="" maxlength="20"></div>
					</div>
					<div class="menu_row">
 						<div class="menu_col1"><label for="useCd">사용유무</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><select class="useCd" name="useCd" id="useCd"></select></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="regDtm">생성일</label></div>
						<div class="menu_col2"><input id="regDtm" type="text" name="regDtm" value="" class="readonly" readonly="readonly"></div>
					</div>
					<div class="menu_row" style="margin-bottom:10px;width:100%;">
						<div class="menu_col1" style="height: 120px;" ><label for="deptEtc">비고</label></div>
						<div class="menu_col2" style="height: 120px;">
							<textarea title="비고" id="deptEtc" name="deptEtc" value="" tabindex=4 maxlength="2000"></textarea>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />