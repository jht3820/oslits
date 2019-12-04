<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp"%>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<style>
	#useCd { height: 100%; width: 60%;}
	/* 필수 입력값 */
	.required_info{color:red; font-weight: bold; }
</style>
<script>
//zTree
var zTree;
//선택된 분류 및 하위 분류 노드가 담긴 배열, 분류 삭제 시 사용
var chkReqClsNodeArr = [];
// mask
var ax5Mask = new ax5.ui.mask();

// 유효성
var arrChkObj = {"reqClsNm":{"type":"length","msg":"요구사항 분류명은 200byte까지 입력이 가능합니다.","max":200}
				,"ord":{"type":"number"}
				};

$(document).ready(function() {
	
	$("[id*='btn_print']").hide();
	//유효성 체크
	gfnInputValChk(arrChkObj);
	
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
	
	//초기 메뉴 세팅
	fnSearchMenuList();

	// 메뉴 관리 전체 열기
	$(".menu .menu_expand_all").click(function(){
		zTree.expandAll(true);
	});

	// 메뉴 관리 전체 닫기
	$(".menu .menu_collapse_all").click(function(){
		zTree.expandAll(false);
	});

	// 메뉴 관리 조회 버튼 클릭 - 조회
	$("#btn_search_menuInfo").click(function(){
		fnSearchMenuList();
	});

	//	메뉴 수정 버튼 클릭 이벤트
	$("#btn_update_menuInfo").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var selZtree = zTree.getSelectedNodes()[0];
		
		if(selZtree == null || selZtree == ""){
			jAlert("선택된 분류가 없습니다.");
			return false;
		}
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "reqClsInfoFrm";
		var strCheckObjArr = ["reqClsNm","ord"];
		var sCheckObjNmArr = ["분류명","순번"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		if(gfnIsNumeric("ord")){
			// 저장전 유효성 체크
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}

			//메뉴정보 수정
			fnUpdateMenuInfoAjax($("#reqClsInfoFrm").serializeArray(),"normal", false);
		}
	});

	//  메뉴 추가 버튼 클릭 이벤트
	//	메뉴 추가시 DB 인서트 처리를 실행하며 등록이 성공되면 등록된 기본정보를 이용하여 메뉴 트리에 추가한다.
	$("#btn_insert_menuAddInfo").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var selZtree = zTree.getSelectedNodes()[0];
	
		if(selZtree == null || selZtree == ""){
			jAlert("선택된 분류가 없습니다.");
			return false;
		}

		// 요구사항 분류추가 - 무한뎁스 추가 가능
		fnInsertMenuInfoAjax(selZtree);	
	});

		//	메뉴 삭제 버튼 
		$("#btn_delete_menuDeleteInfo").click(function(){
			//선택 메뉴 가져오기
			var menu = zTree.getSelectedNodes()[0];
			
			//선택 메뉴 없는경우 경고
			if(gfnIsNull(menu)){
				jAlert("선택된 분류가 없습니다.");
				return;
			}
				
			if(menu.level == 0){
				jAlert("루트 디렉터리는 삭제 할 수 없습니다.","알림창");
			} else {
				//선택한 div의 부모영역이 가진 자식 노드의 갯수로 하위메뉴 존재 여부를 판단한다.
				if(menu.length == 0){
					toast.push("메뉴를 선택하지 않았습니다. 메뉴를 선택해 주세요.");
				}
				else{
					//삭제 컨펌
					jConfirm("삭제시 하위 분류까지 모두 삭제되며 되돌릴 수 없습니다. \n요구사항 분류를 삭제 하시겠습니까?", "알림창", function( result ) {
						if( result ){
		   	   				// 분류 노드를 담은 배열 초기화
		   	   				chkReqClsNodeArr.length = 0;
		   	   						
		   	   				// 선택된 분류 및 하위 분류 노드를 배열 추가
		   	   				fnGetReqClsList(menu);
		
							fnDeleteMenuInfoAjax(chkReqClsNodeArr);
						}
					});
				}
			}
		});
	
	//프린트
	$("#btn_print_menuInfo").click(function(){
		$("#divMenu").printThis({importCSS: false,importStyle: false,loadCSS: "/css/common/printThis.css"});
	});
	
	/* 엑셀 조회 버튼 클릭 이벤트 */
	$("#btn_excel_menuInfo").click(function(){
		document.getElementById("searchFrm").action = "<c:url value='/req/req4000/req4000/selectReq4000ExcelList.do'/>";
		document.getElementById("searchFrm").submit();
		return false;
	});
	

	/********************************************************************
	 *	메뉴 관리 기능 부분 정의 종료												*
 	*********************************************************************/
});



/********************************************************************
* 메뉴 관리 기능 부분 정의 시작												*
*********************************************************************/
/**
 * 	좌측 메뉴 선택했을때 메뉴 정보 표시 함수
 */
function fnGetMenuInfoAjax(reqClsId){
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4000/selectReq4000ReqClsInfoAjax.do'/>","loadingShow":false}
			,{ "reqClsId":reqClsId});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	//디테일폼 세팅
    	gfnSetData2Form(data, "reqClsInfoFrm");
    	
    	//Mask 제거
		ax5Mask.close();
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
 * 조회버튼 클릭시 메뉴 리스트 조회 AJAX
 */
 function fnSearchMenuList(){
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4000/selectReq4000ReqClsListAjax.do'/>","loadingShow":false});
		//AJAX 전송 성공 함수

		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			var listSize = data.reqClsList.length;
	    	
	    	toast.push(data.message);
	    	// zTree 설정 
		    var setting = {
		        data: {
		        	key: {
						name: "reqClsNm"
					},
		            simpleData: {
		                enable: true,
		                idKey: "reqClsId",
						pIdKey: "upperReqClsId",
		            }
		        },
				callback: {
					onClick: function(event, treeId, treeNode){
						//우측 메뉴 정보
						fnGetMenuInfoAjax(treeNode.reqClsId);
					}
				},
				view : {
					fontCss: function(treeId, treeNode){
						return (treeNode.useCd == "02")? {color:"#ddd"} :{};
					},
					showIcon : function(treeId, treeNode) {
						// 트리가 undefined, 노드가 2레벨(뎁스) 미만, isParent 값이 없을경우
						if(typeof zTree != "undefined" && treeNode.level < 2 && !treeNode.isParent){
							// 노드를 부모형 (폴더 아이콘)으로 변경
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
		    zTree = $.fn.zTree.init($("#reqClsJson"), setting, data.reqClsList);

		  //폴더의 계층구조가 3단계가 아니면  tree전체 펼침 시에 일회적 동작 안함(좋은방법같진않고 임시방편 추후개선)
			zTree.expandAll(false);
		  
			//목록 조회시 우측 정보 창 mask
			ax5Mask.open({
				zIndex:90,
				target: $("#selReqClsInfoDiv"),
				content: "요구사항 분류를 선택해주세요."
			});
			
			// 우측정보 리셋
			$("#reqClsInfoFrm")[0].reset();
		  
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
 * 	신규 메뉴 등록 함수
 *	해당 함수 호출시 상위메뉴의 정보를 이용하여 새로운 하위 메뉴를 등록한다.
 */
function fnInsertMenuInfoAjax(reqClsObj){
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4000/insertReq4000ReqClsInfoAjax.do'/>","loadingShow":false}
			,{ "reqClsId":reqClsObj.reqClsId });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	//등록이 실패하면 실패메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		// 2뎁스 이하는 폴더 아이콘
    		if(data.lvl < 2){
    			data.isParent = true;
    		}
    		
    		// 요구사항 분류 추가
    		zTree.addNodes(zTree.getSelectedNodes()[0], data);
    	}
    	
    	toast.push(data.message);
	});

	//AJAX 전송
	ajaxObj.send();
}

/**
 *	요구사항 분류를 삭제한다.
 *	reqClsObjList 삭제할 요구사항 분류 List
 */
function fnDeleteMenuInfoAjax(reqClsObjList){
	
	// 삭제하려는 분류가 없을경우
	if(gfnIsNull(reqClsObjList)){
		toast.push("선택된 분류가 없습니다. 삭제하려는 분류를 선택해주세요.");
		return false;
	}
	
	var params = "";
	
	// 항목으로부터 분류 ID세팅, 분류명
	$(reqClsObjList).each(function(idx, map){
		params += "&reqClsId="+map.reqClsId+"&reqClsNm="+map.reqClsNm;
	});

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4000/deleteReq4000ReqClsInfoAjax.do'/>","loadingShow":false}
			,params);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	//삭제가 실패하면 실패 메시지 후 리턴
    	if(data.errorYn == 'Y'){
    		
    		// 메시지를 가져온다.
    		var message = data.message;
    		// 요구사항이 배정된 분류를 가져온다.
    		var notDelReqClsList = data.notDelReqClsList;
  			// 요구사항 분류명 문자열
    		var clsNmStr = "";
    		
    		// 요구사항이 배정된 분류가 있을 경우 
    		if(!gfnIsNull(notDelReqClsList)){
    			// 분류명 문자열로
    			$(notDelReqClsList).each(function(idx, map){
    				clsNmStr += ", "+map;
    			});
				// 메시지 세팅
    			message = clsNmStr.substring(2)+"\n\n"+message;
    		}

    		jAlert(message, "알림");
    		return false;
    	} 
    	else{
    		// 삭제할 분류 노드 중 최상위 분류 노드를 가져온다.
    		var firstReqClsNode = chkReqClsNodeArr[0];	
    		
    		// 삭제처리를 위해 역순 - 최하위 요구사항 분류부터 배열에 배치
    		chkReqClsNodeArr.reverse();
    		
    		// 최하위 분류부터 삭제처리
    		$(chkReqClsNodeArr).each(function(idx, clsNode) {
    			zTree.removeNode(clsNode);
    		});
    		
    		// 삭제 후 부모노드의 자식 수가 0일 경우 폴더 아이콘으로(부모형) 변경
    		var parentNode = firstReqClsNode.getParentNode();
    		
    		// 2뎁스 미만, 자식 노드가 없는 경우
    		if( parentNode.level < 2 &&  parentNode.children.length == 0){
    			//부모형 노드로 변경하고, 업데이트
    			parentNode.isParent = true;
    			
    		}else if(parentNode.children.length == 0){
    			// 그외에는 자식형 노드로 변경(문서 아이콘으로 변경)
    			parentNode.isParent = false;
    		}
    		
    		// 트리 노드 업데이트
    		zTree.updateNode(parentNode);
    		
    		// 분류 삭제 후 우측 상세정보 화면 리셋
    		$("#reqClsInfoFrm")[0].reset();
    		
    		// 분류 삭제 후 우측 정보 창 mask
			ax5Mask.open({
				zIndex:90,
				target: $("#selReqClsInfoDiv"),
				content: "요구사항 분류를 선택해주세요."
			});
    	}
    	
    	toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
}

/**
*	메뉴 정보 수정 함수
*	선택한 메뉴정보를 수정한다.
*/
function fnUpdateMenuInfoAjax(reqClsObj, updateType, updateAsync){
	
	//객체 넘어왔는지 확인
	if(typeof reqClsObj == "undefined" || reqClsObj == null){
		toast.push("선택된 분류 메뉴가 없습니다.");
		return false;
	}
	
	if(updateType == "editUseCd"){ //사용유무 수정
		var parentNodeObj = reqClsObj.getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 메뉴의 사용여부를 변경해주세요.");
				return false;
			}
		}
		
		//사용유무 반대로 변경
		reqClsObj.useCd = (reqClsObj.useCd=="01")?"02":"01";
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
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4000/updateReq4000ReqClsInfoAjax.do'/>", "async":updateAsync,"loadingShow":false}
			,reqClsObj);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
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
				fnGetMenuInfoAjax(reqClsObj.reqClsId);
    		//더블 클릭으로 사용유무 변경하는 경우
    		}else if(updateType == "editUseCd"){
    			//현재 선택된 메뉴가 수정메뉴와 같은 경우 폼 세팅
    			if(reqClsObj.reqClsId == document.reqClsInfoFrm.reqClsId.value){
    				$("#useCd").val(reqClsObj.useCd);
    			}
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (reqClsObj.useCd == "01")?"#000":"#ccc";
    			$("#"+reqClsObj.tId+"_a").css({color:useColor});
    			
    			//우측 메뉴 정보
				fnGetMenuInfoAjax(reqClsObj.reqClsId);
    			
    		//폼으로 정보 수정인 경우
    		}else if(updateType == "normal"){
    			//메뉴명이 변경된 경우
    			if(zTree.getSelectedNodes()[0].reqClsNm != $("#reqClsNm").val()){
    				//폼값 수정이기 때문에 메뉴값 수정 필요
    				zTree.getSelectedNodes()[0].reqClsNm = $("#reqClsNm").val();
    			}
    			//사용유무 변경된 경우
    			if(zTree.getSelectedNodes()[0].useCd != $("#useCd").val()){
    				var useColor = ($("#useCd").val() == "01")?"#000":"#ccc";
    				$("#"+zTree.getSelectedNodes()[0].tId+"_a").css({color:useColor});
    				
    				//폼값 수정이기 때문에 메뉴값 수정 필요
    				zTree.getSelectedNodes()[0].useCd = $("#useCd").val();
    				
    				//자식 객체가 있는 경우에만 동작
    				if(typeof zTree.getSelectedNodes()[0].children != "undefined"){
	    				//자식 객체 사용유무 전체 변경
	    				$.each(zTree.getSelectedNodes()[0].children,function(){
	    					//사용유무는 부모값
	    					var subReqClsObj = this;
	    					subReqClsObj.useCd = $("#useCd").val();
	    					
	    					//수정 재귀
		    				fnUpdateMenuInfoAjax(subReqClsObj,"editSubUseCd",true);
		    			});
    				}
    			}
    		//하위 메뉴 사용유무 수정인경우 CSS 변경
    		}else if(updateType == "editSubUseCd"){
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (reqClsObj.useCd == "01")?"#000":"#ccc";
    			$("#"+reqClsObj.tId+"_a").css({color:useColor});
    			
    			//자식 객체가 있는 경우에만 동작 (재귀)
   				if(typeof reqClsObj.children != "undefined"){
    				//자식 객체 사용유무 전체 변경
    				$.each(reqClsObj.children,function(){
    					//사용유무는 부모값
    					var subReqClsObj = this;
    					subReqClsObj.useCd = $("#useCd").val();
    					
    					//수정 재귀
	    				fnUpdateMenuInfoAjax(subReqClsObj,"editSubUseCd",true);
	    			}); 
   				}
    		}
    		
    		//해당 노드 갱신
    		if(updateType == "normal"){	//폼값으로 수정
    			zTree.updateNode(zTree.getSelectedNodes()[0]);
    		}else{	//Json Object로 수정
    			zTree.updateNode(reqClsObj);
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

/** 
 *	선택된 요구사항 분류 및 및 하위 분류를 모두 가져오는 함수
 *	@param selectClsOjb 현재 선택된 분류
 */
function fnGetReqClsList(selectClsOjb){

	// 배열에 분류 노드를 담는다.
	chkReqClsNodeArr.push(selectClsOjb);

	//자식 객체가 있는 경우에만 동작
	if(typeof selectClsOjb.children != "undefined"){
		
		$.each(selectClsOjb.children,function(){
			var subClsObj = this;
			// 재귀
			fnGetReqClsList(subClsObj);
		});
	}
}

/********************************************************************
* 메뉴 관리 기능 부분 정의 종료												*
*********************************************************************/
</script>

<div class="main_contents">
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<form id="searchFrm" ></form>
	<div class="tab_contents menu">
		<div class="top_control_wrap">
			<span style="float:left;margin-right: 20px;">*요구사항 분류를 설정할 수 있습니다.</span>
			<span class="menu_tree_help"><i class="fa fa-question"></i>
					<div class="menu_tree_helpBox">
						<span>
							[좌측 트리 메뉴 기능 안내]<br/>
							<br/>
							&nbsp;-메뉴 클릭: 메뉴 상세 정보 보기<br/>
							&nbsp;-더블 클릭: 폴더형 메뉴의 경우 하위 메뉴 보기<br/>
						</span>
					</div>
				</span>
				<span class="button_normal2 btn_inquery" id="btn_search_menuInfo"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;조회</span>
				<span class="button_normal2 btn_save" id="btn_update_menuInfo"><i class='fa fa-edit' aria-hidden='true'></i>&nbsp;저장</span>
				<span class="button_normal2 btn_excel" id="btn_excel_menuInfo"><i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;엑셀</span>
				<span class="button_normal2 btn_print" id="btn_print_menuInfo"><i class='fa fa-print' aria-hidden='true'></i>&nbsp;프린트</span>
		</div>

		<div class="menu_wrap">
			<div class="menu_ctrl_wrap">
				<div class="menu_ctrl_btn_wrap">
					<span class="button_normal2 btn_menu_add" id="btn_insert_menuAddInfo"><i class='fa fa-save' aria-hidden='true'></i>&nbsp;추가</span>
					<span class="button_normal2 btn_menu_del" id="btn_delete_menuDeleteInfo"><i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;삭제</span>
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span>
						<span class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>

				<div class="menu_lists_wrap" id="divMenu">
					<ul id="reqClsJson" class="ztree"></ul>
				</div>
			</div>

			<div class="menu_info_wrap" id="selReqClsInfoDiv">
				<form id="reqClsInfoFrm" name="reqClsInfoFrm" method="post">
					<input type="hidden" id="licGrpId" name="licGrpId"/>
					
					<div class="menu_row top_menu_row">
						<div class="menu_col1"><label for="reqClsId">분류ID</label></div>
						<div class="menu_col2"><input id="reqClsId" type="text"  name="reqClsId" class="readonly" readonly="readonly" tabindex=1 /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="reqClsNm">분류명</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="reqClsNm" type="text" name="reqClsNm" tabindex=2 maxlength="200"/></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="upperReqClsId">상위 분류 ID</label></div>
						<div class="menu_col2"><input id="upperReqClsId" type="text" name="upperReqClsId" value="" class="readonly" readonly></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="lvl">분류 레벨</label></div>
						<div class="menu_col2"><input id="lvl" type="text" name="lvl" value="" class="readonly" readonly></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="ord">순번</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="ord" type="number" min="1" name="ord" value=""></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="ord">사용유무</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><select name="useCd" id="useCd"></select></div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />