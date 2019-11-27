<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/adm.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<style>
	.menu_info_wrap input[type="text"] {width: 60%;}
	.menu_row .menu_col2 { padding: 15px 20px 15px 20px; }
	.code_select {max-width: 60%;}
	.auth_table_title { font-weight: bold; text-align: center;}
</style>

<script>
//zTree
var zTree;

// 메뉴 유효성		
var arrChkObj = {"menuNm":{"type":"length","msg":"메뉴 명은 200byte까지 입력이 가능합니다.",max:200}
				 ,"menuPath":{"type":"length","msg":"메뉴 경로는 500byte까지 입력이 가능합니다.",max:500}
				 ,"menuUrl":{"type":"length","msg":"메뉴 URL은 500byte까지 입력이 가능합니다.",max:500}
				 ,"ord":{"type":"number"}
				 ,"menuDesc":{"type":"length","msg":"메뉴설명은 500byte까지 입력이 가능합니다.", "max":500}
				};

//페이지 로드 될때 이벤트 세팅
$(document).ready(function() {
	//가이드 상자 호출
	gfnGuideStack("add",fnAdm1000GuideShow);
	
	gfnInputValChk(arrChkObj);
	
	
	$("#tabMenu").click(function(){
		gfnGuideStack("add",fnAdm1000GuideShow);
	});
	
	$("#tabAuth").click(function(){
		gfnGuideStack("add",fnAdm1000AuthGuideShow);
	});
	
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
	*	공통기능 부분 정의 시작													*
	*********************************************************************/
	
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = ADM00001:메뉴타입, CMM00001:사용여부 
	*/
	var mstCdStrArr = "ADM00001|CMM00001|ADM00006";
	var strUseYn = 'Y';
	var arrObj = [$("#menuTypeCd"), $("#useCd"),$("#selPrjType") ];
	var arrComboType = ["", "" , ""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , true);

	//초기 메뉴 세팅
	fnSearchMenuList();
	
	//	메뉴, 권한 그룹 탭 전환
	$(".tab_box li").click(function(){
		var idx = $(this).index();
		
		switch(idx){
		case 0:
			$(".tab_contents.menu").show();
			$(".tab_contents.authority").hide();
			break;
		case 1:
			$(".tab_contents.menu").hide();
			$(".tab_contents.authority").show();
			break;
		}
		
		$(this).addClass("on").siblings().removeClass("on");
	});
	/********************************************************************
	*	공통기능 부분 정의 종료													*
	*********************************************************************/
	
	
	
	/********************************************************************
	*	메뉴 관리 기능 부분 정의 시작												*
	*********************************************************************/
	// 메뉴 관리 전체 열기
	$(".menu .menu_expand_all").click(function(){
		zTree.expandAll(true);
	});
	
	// 메뉴 관리 전체 닫기
	$(".menu .menu_collapse_all").click(function(){
		zTree.expandAll(false);
	});
	
	// 메뉴 관리 조회 버튼 클릭 - 조회
	$(".menu .btn_inquery").click(function(){
		fnSearchMenuList();
	});
	
	//	메뉴 수정 버튼 클릭 이벤트
	$(".menu .btn_save").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var menu = zTree.getSelectedNodes()[0];

		//선택 메뉴 없는경우 경고
		if(gfnIsNull(menu)){
			jAlert("선택된 메뉴가 없습니다.");
			return;
		}

		/* 필수입력값 체크 공통 호출 */
		var strFormId = "menuInfoFrm";
		var strCheckObjArr = [];
		var CheckObjNmArr = [];
		/* 소메뉴 일 때   */
		if(menu.level == 3){
			strCheckObjArr = ["menuNm", "menuPath" , "menuUrl" , "useCd","selPrjType", "ord"];
			CheckObjNmArr = ["메뉴명", "메뉴 경로" , "메뉴 URL" , "사용 여부","프로젝트 유형", "순번"];
		}else{
			strCheckObjArr = ["menuNm", "useCd","selPrjType", "ord"];
			CheckObjNmArr = ["메뉴명", "사용 여부" ,  "프로젝트 유형",    "순번"];
		}
		
		
		if(gfnRequireCheck(strFormId, strCheckObjArr, CheckObjNmArr)){
			return;	
		}
		if(gfnIsNumeric('ord')){
			
			// 수정전 유효성 체크
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}

			//메뉴정보 수정
			fnUpdateMenuInfoAjax($("#menuInfoFrm").serializeArray(),"normal", false);	
		}
	});
	
	//  메뉴 추가 버튼 클릭 이벤트
	//	메뉴 추가시 DB 인서트 처리를 실행하며 등록이 성공되면 등록된 기본정보를 이용하여 메뉴 트리에 추가한다.
	$(".menu .btn_menu_add").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var menu = zTree.getSelectedNodes()[0];
		
		//선택 메뉴 없는경우 경고
		if(gfnIsNull(menu)){
			jAlert("선택된 메뉴가 없습니다.");
			return;
		}
		
		//메뉴 선택시 폴더가 접혀있으면 열고 선택되어 있던 폴더 선택 해제후 추가한 폴더를 삽입한다.
		if( menu.level >= 3){
			jAlert("3뎁스 이상 추가할 수 없습니다.");
			return;
		} else{
			//인서트 로직 정상적으로 동작했을때 선택되어 있던 폴더 선택해제하고 DB 인서트된 정보를 이용하여 하위엘레먼트로 추가한다.
			//선택한 로우의 메뉴ID를 인자로 보냄
		    fnInsertMenuInfoAjax(menu);	
		}
	});
	
	//	메뉴 삭제 버튼 
	$(".menu .btn_menu_del").click(function(){
		//선택 메뉴 가져오기
		var menu = zTree.getSelectedNodes()[0];
		
		//선택 메뉴 없는경우 경고
		if(gfnIsNull(menu)){
			jAlert("선택된 메뉴가 없습니다.");
			return;
		}
		
		if(menu.level == 0){
			jAlert("루트 디렉터리는 삭제 할 수 없습니다.");
		} else {
			//선택한 div의 부모영역이 가진 자식 노드의 갯수로 하위메뉴 존재 여부를 판단한다.
			if(menu.length == 0){
				toast.push("메뉴를 선택하지 않았습니다. 메뉴를 선택해 주세요.");
			}
			else{
				if(menu.check_Child_State != -1){
					toast.push("하위 메뉴가 존재하기때문에 삭제할 수 없습니다. 하위메뉴를 먼저 삭제해주세요.");
				}else{
					//삭제 컨펌
					if(confirm("삭제 하면 되돌릴 수 없습니다. 삭제 하시겠습니까?")){
						fnDeleteMenuInfoAjax(menu);
					}
				}
			}
		}
	});
	/********************************************************************
	*	메뉴 관리 기능 부분 정의 종료												*
	*********************************************************************/
	
	
	
	/********************************************************************
	*	권한 관리 기능 부분 정의 시작												*
	*********************************************************************/

	
	// 그룹메뉴명 전체 체크/전체 해제 이벤트 처리
	// 전체체크박스는 form에 담지 않기 위해 밸류값을 변경하지 않는다.
	$("input[type='checkbox']").click(function() {
		var allChkId = $(this).attr("id").substring(0,3);
		if(allChkId == "all"){
			if ($(this).is(':checked')) {
				//start with selector는 ^= , end with selector는 $=
				var allName = $(this).attr("name");
				$("input[name$=" + allName + "]").prop("checked", true);
				$("input[name$=" + allName + "]").val("Y");
				$("input[name^=status]").val("U");
			} else {
				//start with selector는 ^= , end with selector는 $=
				var allName = $(this).attr("name");
				$("input[name$=" + allName + "]").prop("checked", false);
				$("input[name$=" + allName + "]").val("N");
				$("input[name^=status]").val("U");
			}
			
			//전체선택 체크박스 값을 초기화 하지 않으면 오류 발생함. substring으로 자를수 없기 때문
			$("[id^=all_ch]").val("");
		}
	});
	
	// 권한그룹 - 추가버튼 클릭 시 팝업 창 나타나기
	$('#btn_insert_authGrpAddBtn').click(function() {
		/*var data = { "gb":"insert" };
		
		gfnLayerPopupOpen("/adm/adm1000/adm1000/selectAdm1001View.do", data, '660', '630','auto');
		*/
		var data = { "type":"admin" };
		gfnLayerPopupOpen('/prj/prj2000/prj2000/selectPrj2001View.do', data, '730', '730','auto');
	});
	
	// 권한그룹 - 추가버튼 클릭 시 팝업 창 나타나기
	$('#btn_update_authGrpUdtBtn').click(function() {
		var authGrpId = $('.left_con.table_active').attr("id");
		
		//삭제할 롤을 선택했는지 확인하고 진행
		if(!($('.left_con.table_active').hasClass("table_active"))){
			toast.push('수정할 롤을 선택하고 수정버튼을 눌러주세요.');                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
			return;
		}
		
		
		var data = { "type":"admin" ,"gb" : "update" , "authGrpId" : authGrpId};
		gfnLayerPopupOpen('/prj/prj2000/prj2000/selectPrj2001View.do', data , '730', '730','scroll');
	
	});
	
	
	/* 추가버튼 클릭시 팝업창 레이어 팝업으로 */
	/*$('#btn_insert_authGrpAddBtn').click(function() {
		layer_popup('/adm/adm1000/adm1000/adm1001.jsp.do');
		$('.layer_popup_box').css({	
			"width" : "594",
			"height" : "502"
		});
	});*/
	
	// 권한그룹 - 삭제버튼 클릭시
	$("#btn_delete_authGrpDelBtn").click(function(){
		fnDeleteAuthGrp();
	});
	
	// 권한그룹 - 저장버튼 클릭시
	$("#btn_insert_systemAuthSaveBtn").click(function(){
		//권한롤에 메뉴권한정보들을 저장한다.
		fnSaveAuthGrpMenuAuthListAjax();
	});
	
	$("#selPrjType").change(function(){
		//권한롤에 메뉴권한정보들을 저장한다.
		$("#prjType").val($(this).val()); 
	});
	
	
	fnSelectAdm1000PrjAuthGrpList();
	/********************************************************************
	*	권한 관리 기능 부분 정의 종료												*
	*********************************************************************/
});
	
	
	
/********************************************************************
* 메뉴 관리 기능 부분 정의 시작												*
*********************************************************************/
/**
 * 	좌측 메뉴 선택했을때 메뉴 정보 표시 함수
 */
function fnGetMenuInfoAjax(menuId){

	var licGrpId = '${sessionScope.loginVO.licGrpId}'; 
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/selectAdm1000MenuInfoAjax.do'/>","loadingShow":false}
			,{ "menuId":menuId, "licGrpId":licGrpId });
	
	fnInit();
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
			
		//디테일폼 세팅
    	gfnSetData2Form(data, "menuInfoFrm");

		// 트리에서 메뉴 노드 선택시 inputError로 붉게 표시된 부분 초기화
		var menuChild = $('.menu_col2').children();
		$(menuChild).each(function(idx, val){
    		if($(this).hasClass("inputError")){
            	$(this).removeClass("inputError");
      		}
    	});

		if(data.prjType==""){
			$('#selPrjType').val('03');	
			$('#prjType').val('03');
		}else{
			$('#selPrjType').val(data.prjType);
		}
    	
    	
    	if(data.autCnt==1){
    		$('#selPrjType').attr( 'disabled', true );	
    	}else{
    		$('#selPrjType').attr( 'disabled', false );
    	}
		
		if( data.lvl == 1 ){
			$('#menuImgUrl').attr( 'disabled', false );
		}else{
			$('#menuImgUrl').attr( 'disabled', true );
		}
	});
	
	//AJAX 전송
	ajaxObj.send();
}

/**
 * 조회버튼 클릭시 메뉴 리스트 조회 AJAX
 */
function fnSearchMenuList(){
	
	var licGrpId = '${sessionScope.loginVO.licGrpId }'; 
	var authGrpId = '${sessionScope.selAuthGrpId }';
	//AJAX 설정
	fnInit();
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/selectAdm1000MenuListAjax.do'/>"}
			,{ "licGrpId":licGrpId, "authGrpId":authGrpId });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	toast.push(data.message);
    	
    	// zTree 설정 
	    var setting = {
	        data: {
	        	key: {
					name: "menuNm"
				},
	            simpleData: {
	                enable: true,
	                idKey: "menuId",
					pIdKey: "upperMenuId",
					rootPId: "000"
	            }
	        },
			callback: {
				onClick: function(event, treeId, treeNode){
					//우측 메뉴 정보
					fnGetMenuInfoAjax(treeNode.menuId);
					if(treeNode.level==3){
						$("#lbMenuPath").html('메뉴 경로<span class="required_info">&nbsp;*</span>');
						$("#lbMenuUrl").html('메뉴 URL<span class="required_info">&nbsp;*</span>');
					}else{
						$("#lbMenuPath").html('메뉴 경로');
						$("#lbMenuUrl").html('메뉴 URL');
					}					
				}
			},
			view : {
				fontCss: function(treeId, treeNode){
					return (treeNode.useCd == "02")? {color:"#ddd"} :{};
				},
				showIcon : function(treeId, treeNode) {
					if(typeof zTree != "undefined" && treeNode.level != 3 && !treeNode.isParent){
						treeNode.isParent = true;
						//zTree.updateNode(treeNode);
						zTree.refresh();
					}
					return true;
				}
			}
	    };
    
	    // zTree 초기화
	    zTree = $.fn.zTree.init($("#admMenuJson"), setting, data.baseMenuList);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		jAlert(data.message,"알림창");
 	});
	//AJAX 전송
	ajaxObj.send();
}

/**
 * 	신규 메뉴 등록 함수
 *	해당 함수 호출시 상위메뉴의 정보를 이용하여 새로운 하위 메뉴를 등록한다.
 */
function fnInsertMenuInfoAjax(menuObj){

	var licGrpId = '${sessionScope.loginVO.licGrpId}'; 
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/insertAdm1000MenuInfoAjax.do'/>"}
			,{ "menuId":menuObj.menuId, "licGrpId":licGrpId });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//등록이 실패하면 실패메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//3뎁스가 아니라면 부모형 메뉴
    		if(data.lvl != 3){
    			data.isParent = true;
    		}
    		//산출물 추가
    		zTree.addNodes(zTree.getSelectedNodes()[0], data);
    	}
    	
    	toast.push(data.message);
	});
	//AJAX 전송
	ajaxObj.send();
}

/**
*	메뉴 삭제 함수
*	선택한 메뉴를 삭제한다.(DB에서 삭제 처리시 자식 메뉴들이 존재하면 삭제하지 않고 알림)
*/
function fnDeleteMenuInfoAjax(menuObj){
	var licGrpId = '${sessionScope.loginVO.licGrpId}'; 
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/deleteAdm1000MenuInfoAjax.do'/>"}
			, { "menuId":menuObj.menuId, "licGrpId":licGrpId });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//삭제가 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//해당 노드 제거
			zTree.removeNode(menuObj);
			
    		//삭제 후 부모노드의 자식 수가 0일 경우 폴더형 메뉴로 변경
    		var parentNode = menuObj.getParentNode();
    		
    		//메뉴 뎁스가 3이 아닌데, 자식 노드가 없는 경우
    		if(parentNode.level != 3 && parentNode.children.length == 0){
    			
    			//부모형 노드로 변경하고, 업데이트
    			parentNode.isParent = true;
    			zTree.updateNode(parentNode);
    		}
    		/*삭제후 form 초기화*/
    		fnInit();
    	}
    	
    	toast.push(data.message);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		
 	});
	//AJAX 전송
	ajaxObj.send();
}

/**
*	메뉴 정보 수정 함수
*	선택한 메뉴정보를 수정한다.
*/
function fnUpdateMenuInfoAjax(menuObj, updateType, updateAsync){
	//객체 넘어왔는지 확인
	if(typeof menuObj == "undefined" && menuObj == null){
		toast.push("선택된 메뉴가 없습니다.");
		return false;
	}
	
	if(updateType == "editUseCd"){ //사용유무 수정
		var parentNodeObj = menuObj.getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 메뉴의 사용여부를 변경해주세요.");
				return false;
			}
		}
		
		//사용유무 반대로 변경
		menuObj.useCd = (menuObj.useCd=="01")?"02":"01";
	}else if(updateType == "normal"){
		var parentNodeObj = zTree.getSelectedNodes()[0].getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 메뉴의 사용여부를 변경해주세요.");
				return false;
			}
		}
	}
	
	var licGrpId = '${sessionScope.loginVO.licGrpId}';
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/updateAdm1000MenuInfoAjax.do'/>", "async":updateAsync,"loadingShow":false}
			,menuObj);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data) {
		data = JSON.parse(data);
    	
    	//수정이 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//메뉴명 변경인 경우
    		if(updateType == "editRename"){
    			//우측 메뉴 정보
				fnGetMenuInfoAjax(menuObj.menuId);
    		
    		//더블 클릭으로 사용유무 변경하는 경우
    		}else if(updateType == "editUseCd"){
    			//현재 선택된 메뉴가 수정메뉴와 같은 경우 폼 세팅
    			if(menuObj.menuId == document.menuInfoFrm.menuId.value){
    				$("#useCd").val(menuObj.useCd);
    			}
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (menuObj.useCd == "01")?"#000":"#ccc";
    			$("#"+menuObj.tId+"_a").css({color:useColor});
    			
    			//우측 메뉴 정보
				fnGetMenuInfoAjax(menuObj.menuId);
    			
    		//폼으로 정보 수정인 경우
    		}else if(updateType == "normal"){
    			//메뉴명이 변경된 경우
    			if(zTree.getSelectedNodes()[0].menuNm != $("#menuNm").val()){
    				//폼값 수정이기 때문에 메뉴값 수정 필요
    				zTree.getSelectedNodes()[0].menuNm = $("#menuNm").val();
    			}
    			//사용유무 변경된 경우
    			else if(zTree.getSelectedNodes()[0].useCd != $("#useCd").val()){
    				var useColor = ($("#useCd").val() == "01")?"#000":"#ccc";
    				$("#"+zTree.getSelectedNodes()[0].tId+"_a").css({color:useColor});
    				
    				//폼값 수정이기 때문에 메뉴값 수정 필요
    				zTree.getSelectedNodes()[0].useCd = $("#useCd").val();
    				
    				//자식 객체가 있는 경우에만 동작
    				if(typeof zTree.getSelectedNodes()[0].children != "undefined"){
	    				//자식 객체 사용유무 전체 변경
	    				$.each(zTree.getSelectedNodes()[0].children,function(){
	    					//사용유무는 부모값
	    					var subMenuObj = this;
	    					subMenuObj.useCd = $("#useCd").val();
	    					
	    					//수정 재귀
		    				fnUpdateMenuInfoAjax(subMenuObj,"editSubUseCd",true);
		    			});
    				}
    			}
    		//하위 메뉴 사용유무 수정인경우 CSS 변경
    		}else if(updateType == "editSubUseCd"){
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (menuObj.useCd == "01")?"#333":"#ccc";
    			$("#"+menuObj.tId+"_a").css({color:useColor});
    			
    			//자식 객체가 있는 경우에만 동작 (재귀)
   				if(typeof menuObj.children != "undefined"){
    				//자식 객체 사용유무 전체 변경
    				$.each(menuObj.children,function(){
    					//사용유무는 부모값
    					var subMenuObj = this;
    					subMenuObj.useCd = $("#useCd").val();
    					
    					//수정 재귀
	    				fnUpdateMenuInfoAjax(subMenuObj,"editSubUseCd",true);
	    			}); 
   				}
    		}
    		
    		//해당 노드 갱신
    		if(updateType == "normal"){	//폼값으로 수정
    			zTree.updateNode(zTree.getSelectedNodes()[0]);
    		}else{	//Json Object로 수정
    			zTree.updateNode(menuObj);
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
* 메뉴 관리 기능 부분 정의 종료												*
*********************************************************************/



/********************************************************************
* 권한 관리 기능 부분 정의 시작												*
*********************************************************************/
/* 권한정보 클릭시 이벤트 호출 */
function fnAuthGrpSmallMenuList(authGrpId, selPrjId){
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/selectAdm1000AuthGrpMenuListAjax.do'/>"}
			,{"authGrpId" : authGrpId, "prjId" : selPrjId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	//기존 조회 정보 모두 제거
    	$("#authTblBody").children().remove();
    	
    	//rowspan 대상 ID
    	var prevUpupMenuId = '';
    	
    	//메뉴 rowspan 담기
    	var menuIdRowSpan = {};
    	
    	//반복하며 그리기
    	$.each(data.authGrpSmallMenuList,function(idx, data){
    		//tr 태그 id 부여하여 생성
    		$("#authTblBody").append("<tr id='tr" + data.menuId + "'>");
    		
    		//생성한 tr태그 객체 얻기
    		var trObj = $("#tr" + data.menuId );
    		
    		//새로운 대 메뉴인경우
    		if(gfnIsNull(prevUpupMenuId) || prevUpupMenuId != data.upupMenuId){
    			trObj.append("<td class='right_con wd1' id='menuRow_"+data.upupMenuId+"' rowspan='1'>" + data.upupMenuNm + "</td>");	
    		}else{
    			//menuId별 rowspan 생성
    			if(gfnIsNull(menuIdRowSpan[data.upupMenuId])){
    				menuIdRowSpan[data.upupMenuId] = 1;
    			}
    			
    			//오류 처리 안함
   				try{
   					//대메뉴 rowspan +1
    				var upObjRowspan = parseInt(menuIdRowSpan[data.upupMenuId]);
    				menuIdRowSpan[data.upupMenuId] = (upObjRowspan+1);
   				}catch(err){console.log(err);}
    		}
    		
    		prevUpupMenuId = data.upupMenuId;
    		
    		trObj.append("<td class='right_con wd2'>" + data.upMenuNm + " &#62; " + data.menuNm + "</td>");
    		
    		//배열로 체크박스 컬럼명 저장하여 반복 루프 처리함.
    		var strArrYn = ["accessYn", "selectYn", "regYn", "modifyYn", "delYn", "excelYn", "printYn"];
    		
			//해당 로우 상태 담기
			trObj.append("<input type='hidden' name='" + "status" + data.menuId + "' id='" + "status" + data.menuId + "' value='" + data.status + "' />");
    		
    		//메뉴별 권한 체크박스 생성
    		$.each(strArrYn, function(idx, val){
    			var cnt = idx + 3;
    			var strMenuId = data.menuId + val;
    			var hidMenuId = "hidden" + data.menuId + val ;
    			var hidStatus = "status" + data.menuId;	//수정상태인지 그냥 수정전 상태인지 상태값 가진 인풋
    			trObj.append("<td class='right_con wd" + cnt + " adm_chk'><input type='hidden' name='" + hidMenuId + "' id='" + hidMenuId + "' value='" + eval("data." + val) + "' /> <input type='checkbox' title='체크박스' onclick='fnValToChk(this);' name='" + strMenuId + "' id='" + strMenuId + "' value='" + eval("data." + val) + "' /><label for='chk" + data.menuId + "'></label></td>");
    		});
    		
    		// 가로로 전체 체크가능한 체크박스 
    		trObj.append("<td class='right_con wd10 adm_chk' style='text-align: center;' ><input type='checkbox' title='체크박스'  name="+data.menuId+"'_prjAuthHorizon' id='"+data.menuId+"_prjAuthHorizon' onclick='fnHorizonChk(this);' /><label for=''></label></td>");
    		
    		//밸류값 확인하여 체크 상태 변경
    		$.each(strArrYn, function(idx, val){
    			var objYn = $("#" + data.menuId + val);
        		if($(objYn).val() == 'Y'){
        			$(objYn).prop("checked", true);
        		}
        		else{
        			$(objYn).prop("checked", false);
        		}	
    		});
    		
    	});

    	//rowspan 걸기
    	$.each(menuIdRowSpan, function(idx, map){
    		$("#menuRow_"+idx).attr("rowspan",map);
    	});
    	
    	toast.push(data.message);

    	//출력 감추기
    	$("input[name$=printYn]").parent().hide();
	});
	
	//AJAX 전송
	ajaxObj.send();
}


/*
 *	권한관리의 해당 메뉴의 가로 전체 체크/전체 해제 처리
 * @param chkObj 가로 전체체크 체크박스
 */
function fnHorizonChk(chkObj){
	
	// 체크박스의 ID를 가져온다
	var horizonChkId = $(chkObj).attr("id"); 
	// 체크박스 ID에서 메뉴 ID를 추출
	var horizonMenuId = horizonChkId.split("_")[0];
	
	// 가로 전체체크 할 경우
	if($(chkObj).is(':checked')){
		$("input[name^=" + horizonMenuId + "]").prop("checked", true);
		$("input[name^=" + horizonMenuId + "]").val("Y");
		$("input[name^=hidden" + horizonMenuId + "]").val("Y");
		$("input[name^=status" + horizonMenuId + "]").val("U");
	// 가로 전체 체크해제 할 경우	
	}else{
		$("input[name^=" + horizonMenuId + "]").prop("checked", false);
		$("input[name^=" + horizonMenuId + "]").val("N");
		$("input[name^=hidden" + horizonMenuId + "]").val("N");
		$("input[name^=status" + horizonMenuId + "]").val("U");
	}
}

/**
*	권한관리 체크박스 클릭시 체크상태를 확인하여 밸류값을 변환 
*/
function fnValToChk(chkObj){
	var strMenuId = $(chkObj).attr("id").substring(0,12);
	
	//만약 체크 상태가 체크이면 value에 Y 세팅, 상태를 수정모드로 변경
	if(	$(chkObj).prop('checked')){
		$(chkObj).val("Y");
		$("#hidden" + $(chkObj).attr("id")).val("Y");
		$("#status" + strMenuId).val("U");
	}
	//체크 상태가 아니라면 value에 N 세팅, 상태를 수정모드로 변경
	else{
		$(chkObj).val("N");
		$("#hidden" + $(chkObj).attr("id")).val("N");
		$("#status" + strMenuId).val("U");
	}
}

/**
 * 	권한그룹의 메뉴권한 정보들을 저장한다. 
 */
function fnSaveAuthGrpMenuAuthListAjax(){
	
	var $authGrpObj = $('.left_con.table_active');

	//menuAuthFrm에 현재 선택한 롤 id를 저장한다.
	$("#menuAuthGrpId").val($authGrpObj.attr("id"));
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/saveAdm1000AuthGrpMenuAuthListAjax.do'/>"}
			,$("#menuAuthFrm").serialize());
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	toast.push(data.message);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		
 	});
	//AJAX 전송
	ajaxObj.send();
}

/**
 * 	권한그룹의 신규 롤을 추가한다.
 */
function fnInsertAuthGrp(newAuthGrpId){
	var $authGrpObj = $('.left_con.table_active');
	
	$("#newAuthGrpId").val(newAuthGrpId);
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/insertAdm1000AuthGrpInfoAjax.do'/>"}
			,$("#authGrpFrm").serialize());
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	if(data.saveYN == 'N'){
    		jAlert(data.message,"알림창");
    		return;
    	}else{
    		//권한롤 선택 창에 새권한 추가
    		$(".left_table tbody").append("<tr class='left_con' id='" + newAuthGrpId + "'><td class='left_con'>" + newAuthGrpId + "</td><td class='left_con right_line'>" + $("#authGrpNm").val() + " - " + $("#authGrpDesc").val());
    		
    		// 권한 목록 클릭시 소메뉴 권한 목록 조회
    		$('.left_table tbody tr').on("click",function(){
    		    $('.left_table tbody tr').removeClass("table_active");
    		    $('.left_table tbody tr').addClass("table_inactive");
    		    $(this).addClass("table_active");
    		    $(this).removeClass("table_inactive");
    		    
    		    //소메뉴 권한 목록 조회
    		    fnAuthGrpSmallMenuList($(this).attr("id"), 'ROOTSYSTEM_PRJ');
    		});
    		
    		//레이어 팝업창 클리어 및 닫기
    		gfnLayerPopupClose();
    		
    		toast.push(data.message);
    	}
	});
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		
 	});
	//AJAX 전송
	ajaxObj.send();	
}

/**
 * 	권한그룹의 선택한 권한롤을 삭제한다.
 */
function fnDeleteAuthGrp(){
	var authGrpId = $('.left_con.table_active').attr("id");
	
	//삭제할 롤을 선택했는지 확인하고 진행
	if(!($('.left_con.table_active').hasClass("table_active"))){
		toast.push('삭제할 롤을 선택하고 삭제버튼을 눌러주세요.');                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
		return;
	}
	
	//사업 담당자 삭제 불가능
	if(authGrpId == "AUT0000000000001"){
		jAlert("사업 담당자 권한은 관리자 필수 권한입니다.</br>삭제 할 수 없습니다.","경고");
		return false;
	}
	
	if(!confirm('삭제하면 되돌릴 수 없습니다. 삭제하시겠습니까?')){
		return;
	}
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/deleteAdm1000AuthGrpInfoAjax.do'/>"}
			,{"authGrpId" : authGrpId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	if(data.saveYN == 'N'){
    		jAlert(data.message,"알림창");
    		return;
    	}else{
    		//삭제 성공시 권한그룹 목록, 메뉴별접근권한 목록 초기화
    		$(".left_table tbody #" + authGrpId).remove();
    		$("#authTblBody").children().remove();
    		
    		toast.push(data.message);
    	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		
 	});
	//AJAX 전송
	ajaxObj.send();
}

function fnInit(){
	$('#selPrjType').attr( 'disabled', false );
	$('#menuInfoFrm')[0].reset();
}

/* 권한그룹 목록 조회 */
function fnSelectAdm1000PrjAuthGrpList(){
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/adm/adm1000/adm1000/selectAdm1000PrjAuthGrpList.do'/>"}
			,{});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	//기존 조회 정보 모두 제거
    	$("#prjAuthGrpList").children().remove();
    	
    	//반복하며 그리기
    	$.each(data.prjAuthGrpList,function(idx, data){
    		//tr 태그 id 부여하여 생성
    		var html = '';
    		html += '<tr class="left_con" id="'+data.authGrpId+'">';
    		html += '	<td class="left_con">'+data.authGrpNm+'</td>';
    		html += '	<td class="left_con" title="'+data.authGrpDesc+'">'+gfnCutStrLen(data.authGrpDesc, 60)+'</td>';
    		html += '	<td class="left_con">'+data.usrTypNm+'</td>';
    		/* html += '	<td class="left_con">'+fnNvl(data.acceptUseNm)+'</td>'; */
    		html += '</tr>';
    		
			$("#prjAuthGrpList").append(html);
    		
    	});
    	
    	// 권한 목록 클릭시 소메뉴 권한 목록 조회
    	$('.left_table tbody tr').on("click",function(){
    	    $('.left_table tbody tr').removeClass("table_active");
    	    $('.left_table tbody tr').addClass("table_inactive");
    	    $(this).addClass("table_active");
    	    $(this).removeClass("table_inactive");
    	    
    	    //소메뉴 권한 목록 조회
    	    fnAuthGrpSmallMenuList($(this).attr("id"), 'ROOTSYSTEM_PRJ');
    	});

    	toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
}

function fnNvl(str){
	if(str==null){
		return "";
	}else{
		return str;
	}
}

//가이드 상자
function fnAdm1000GuideShow(){
	var mainObj = $(".main_contents");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["adm1000"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}

//가이드 상자
function fnAdm1000AuthGuideShow(){
	var mainObj = $(".main_contents");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["adm1000"];
	//gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}
/********************************************************************
* 권한 관리 기능 부분 정의 종료												*
*********************************************************************/
</script>

<div class="pop_wrap"></div>

<div class="main_contents">
	<div class="tab_title">${sessionScope.selMenuNm }</div>
	<div class="tab_menu">
		<ul class="tab_box">
			<li id="tabMenu" class="ok_bottom_line on"><a>메뉴 관리</a></li>
			<li id="tabAuth" ><a>권한 그룹 관리</a></li>
		</ul>
		
		<div class="tab_contents authority">
			
			<div class="left_div">
			<div class="left_title">권한 그룹
				<span class="button_normal btn_one" id="btn_insert_authGrpAddBtn">추가</span>
 				<span class="button_normal btn_two" id="btn_update_authGrpUdtBtn">수정</span>
				<span class="button_normal btn_three" id="btn_delete_authGrpDelBtn">삭제</span>
			</div>
				<table class="left_table">
					<caption>권한 그룹</caption>
					<thead>
						<tr>
							<th class="left_sub_title" style="width: 30%;padding-left: 0px;">그룹 명</th>
							<th class="left_sub_title" style="width: 30%;padding-left: 0px;">설명</th>
							<th class="left_sub_title" style="width: 20%;padding-left: 0px;">사용자<br/>유형</th>
							<!-- 모든 권한 접수 허용 -->
							<!-- <th class="left_sub_title non_right_line" style="width: 20%;padding-left: 0px;">접수권한<br/>유무</th> -->
						</tr>
					</thead>
					
					<tbody id="prjAuthGrpList" >
						
					</tbody>
				</table>
			</div>

		
			<div class="right_div">
				<div class="right_title">시스템 권한</div>
				<span class="button_normal btn_three" id="btn_insert_systemAuthSaveBtn">저장</span>
				<form id="menuAuthFrm" name="menuAuthFrm">		
					<input type="hidden" id="menuAuthGrpId" name="menuAuthGrpId" >
					
					<table class="right_table">
						<caption>시스템 권한</caption>
						<thead>
							<tr class="auth_table_title">
								<th class="right_sub_title wd1" style="text-align: center;">대 메뉴명</th>
								<th class="right_sub_title wd2 sub_two" style="text-align: center;">중 메뉴명 > 소 메뉴명</th>
								<th class="right_sub_title wd3 adm_chk"><input type="checkbox" title="체크박스" name="accessYn" id="all_ch1"/><label for="all_ch1"></label><span class="title_align">접근</span></th>
								<th class="right_sub_title wd4 adm_chk"><input type="checkbox" title="체크박스" name="selectYn" id="all_ch2"/><label for="all_ch2"></label><span class="title_align">조회</span></th>
								<th class="right_sub_title wd5 adm_chk"><input type="checkbox" title="체크박스" name="regYn" id="all_ch3"/><label for="all_ch3"></label><span class="title_align">등록</span></th>
								<th class="right_sub_title wd6 adm_chk"><input type="checkbox" title="체크박스" name="modifyYn" id="all_ch4"/><label for="all_ch4"></label><span class="title_align">수정</span></th>
								<th class="right_sub_title wd7 adm_chk"><input type="checkbox" title="체크박스" name="delYn" id="all_ch5"/><label for="all_ch5"></label><span class="title_align">삭제</span></th>
								<th class="right_sub_title wd8 adm_chk"><input type="checkbox" title="체크박스" name="excelYn" id="all_ch6"/><label for="all_ch6"></label><span class="title_align">엑셀</span></th>
								<!-- <th class="right_sub_title wd9 adm_chk"><input type="checkbox" title="체크박스" name="printYn" id="all_ch7"/><label for="all_ch7"></label><span class="title_align">출력</span></th> -->
								<th class="right_sub_title wd10 non_right_line"><label for="chk8"></label><span class="title_align">전체체크</span></th>
							</tr>
						</thead>
						
						<tbody id="authTblBody">
							<!-- 테이블 바디 내용 들어감 AJAX로 조회시 삽입됨. -->
							
						</tbody>
									
					</table>
				</form>		
			</div>	
			
		
		</div>
		
		<div class="tab_contents menu">
			<span style="float:left;">*솔루션 메뉴를 설정할 수 있습니다.</span>
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
			<div class="top_control_wrap">
				<span class="button_normal2 btn_inquery" id="btn_search_menuInfo"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;조회</span>
				<span class="button_normal2 btn_save" id="btn_update_menuInfo"><i class='fa fa-save' aria-hidden='true'></i>&nbsp;저장</span>
			</div>
			
			<div class="menu_wrap">
				<div class="menu_ctrl_wrap">
					<div class="menu_ctrl_btn_wrap" guide="addDelMenu" >
						<span class="button_normal2 btn_menu_add" id="btn_inseret_menuAddInfo"><i class='fa fa-edit' aria-hidden='true'></i>&nbsp;추가</span>
						<span class="button_normal2 btn_menu_del" id="btn_delete_menuDelInfo"><i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;삭제</span>
						<div class="menu_all_wrap">
							<span class="menu_expand_all" title="전체 열기"></span><span class="menu_collapse_all" title="전체 닫기"></span>
						</div>
					</div>
					
					<div class="menu_lists_wrap">
						<ul id="admMenuJson" class="ztree"></ul>
					</div>
				</div>
				
				<div class="menu_info_wrap">
					<form id="menuInfoFrm" name="menuInfoFrm" method="post">
						<input type="hidden" id="licGrpId" name="licGrpId"/>
						<input type="hidden" id="prjType" name="prjType"/>

						<div class="menu_row top_menu_row">
							<div class="menu_col1"><label for="menuPath">메뉴 ID</label></div>
							<div class="menu_col2"><input id="menuId" type="text" name="menuId" value="" class="readonly" readonly tabindex=1 /></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="menuNm" >메뉴명<span class="required_info">&nbsp;*</span></label></div>
							<div class="menu_col2"><input id="menuNm" type="text" name="menuNm" value="" tabindex=2 /></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="menuPath" id="lbMenuPath" >메뉴 경로</label></div>
							<div class="menu_col2"><input id="menuPath" type="text" name="menuPath" value="" tabindex=3 ></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="menuUrl" id="lbMenuUrl" >메뉴 URL</label></div>
							<div class="menu_col2"><input id="menuUrl" type="text" name="menuUrl" value=""></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="menuTypeCd">메뉴 타입</label></div>
							<div class="menu_col2"><span class="search_select"><select name="menuTypeCd" id="menuTypeCd" class="code_select" style="max-width: 60%;"></select></span></div> <!-- class="w200" -->
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="menuImgUrl">메뉴 이미지 URL</label></div>
							<div class="menu_col2"><input id="menuImgUrl" type="text" name="menuImgUrl" value=""></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="lvl">메뉴 레벨</label></div>
							<div class="menu_col2"><input id="lvl" type="text" name="lvl" value="" class="readonly" readonly></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="upperMenuId">상위 메뉴 ID</label></div>
							<div class="menu_col2"><input id="upperMenuId" type="text" name="upperMenuId" value="" class="readonly" readonly></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="ord">순번<span class="required_info">&nbsp;*</span></label></div>
							<div class="menu_col2"><input id="ord" type="text" name="ord" value=""></div>
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="useCd">사용여부<span class="required_info">&nbsp;*</span></label></div>
							<div class="menu_col2"><span class="search_select"><select name="useCd" id="useCd" class="code_select" style="max-width: 60%;"></select></span></div> <!-- class="w200" -->
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="prjType">프로젝트유형<span class="required_info">&nbsp;*</span></label></div>
							<div class="menu_col2" guide="projectType" ><span class="search_select"><select name="selPrjType" id="selPrjType" class="code_select" style="max-width: 60%;"></select></span></div> <!-- class="w200" -->
						</div>
						<div class="menu_row">
							<div class="menu_col1"><label for="menuDesc">메뉴설명</label></div>
							<div class="menu_col2">
								<textarea title="메뉴설명" id="menuDesc" name="menuDesc" value=""></textarea>
							</div>
						</div>
					</form>
				</div>
				
			</div>
		</div>
		
	</div>

	
	
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />