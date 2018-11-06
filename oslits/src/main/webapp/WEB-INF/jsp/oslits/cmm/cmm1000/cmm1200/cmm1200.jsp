<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>

<link rel='stylesheet' href='<c:url value='/css/oslits/adm.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree.core.js"></script>


<style type="text/css">
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
.button_normal { width: 39px; height: 22px; line-height: 22px; text-align: center; font-weight: bold; font-size: 1em; border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; margin: 0 auto; border: 1px solid #b8b8b8; cursor: pointer; }
div.pop_sub .pop_left {width:28%;} /* common.css pop_left width값 오버라이딩 */
div.pop_sub .pop_right {width:72%;} /* common.css pop_left width값 오버라이딩 */
.input_txt {padding-left: 5px;}

.pop_menu_ctrl_wrap { float: left; width: 100%; height: 100%; min-height: 475px; font-size: 0.875em; box-sizing: border-box; border-right: 1px solid #ccc; }
#pop_sub_wrap{ height: 600px; padding: 0; max-hight:600px; }
#pop_left_wrap{ padding: 0; border-right: none; width: 33%; }
#pop_right_wrap{ width: 67%; padding-top: 16px;}
.pop_top_wrap{ height: 35px; margin-top: 15px; text-align: right; margin-right: 10px; }
#pop_menu_btn { height: 54px; line-height: 55px; }
.searchItem{ float: right; padding-right: 10px; } 
#ord, #regDtm{ width: 100%; }
#pop_radio { padding: 20px 15px 12px 3px; }
#deptEtc{ height: 123px; background: #eee; }
#selUseCd{ display: none; width: 100px; }
#searchSelect{ box-sizing: border-box; width:100px; margin: 0px; }
#searchTxt{float: left; width: 150px; height: 28px; border: 1px solid #ccc; margin-right: 1%; font-size: 12px; box-shadow: inset 0px 1px 2px #dddddd; padding-left: 6px; }
.span_text_search{ color: #616161; border-right: 1px solid #d8d8d8; min-height: 20px; line-height: 20px; padding: 10px 0px; box-sizing: content-box; font-size : 13px; font-family: "NanumBarunGothic"; font-weight: bold; }
.span_searchSelect{ padding-left: 10px; }
#divMenu { padding-top: 10px; border-bottom: 1px solid #ccc; }
.layer_popup_box input[type="text"].input_txt { max-width: 100%; }

</style>


<script type="text/javascript">
	$(document).ready(function() {
		
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
		var arrObj = [$("#selUseCd")];
		var arrComboType = ["S"];
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , true);
		
		// 조직 검색 및 ztree 추가
		fnGetDeptList();

		// ztree 전체 열기
		$(".menu_expand_all").click(function(){
			zTree.expandAll(true);
		});

		// ztree 전체 닫기
		$(".menu_collapse_all").click(function(){
			zTree.expandAll(false);
		});
		
		// 조회 버튼 클릭 - 조직 조회
		$("#btn_search_dept_pop").click(function(){
			fnSearchDeptList();
		});
		
		// 검색창 엔터키 이벤트
		$('#searchTxt').keyup(function(e) {
			if(e.keyCode == '13' ){
				$('#btn_search_dept_pop').click();
			}
		});
		
		// 선택 버튼 클릭 - 조직 선택
		$("#btn_select_dept").click(function(){
			fnSelectDept();
		});
		
		// 취소 버튼 클릭 - 팝업 close
		$('.exit_btn').click(function() {
			gfnLayerPopupClose();
		});
		
		// 검색옵션 선택 (select)
		$("#searchSelect").change(function() {
			fnChangeElement();
		});

	});

	

	
/************************************************************
* 조직검색 함수 정의 시작			 								*
*************************************************************/

	// 검색된 조직 목록
	var deptNodeList = [];
	// 선택된 조직 및 상위 조직명을 담을 배열
	var parentDeptNames = [];	
	
	/**
	 * 팝업 오픈 또는 조회버튼 클릭시 조직목록 조회 AJAX
	 */
	function fnGetDeptList(){

		// 검색 select를 조직명으로 세팅
		$("#searchSelect").val("deptName");
		$("#searchTxt").removeAttr("readonly");
		
		// 조직 목록은 모두 가져오고, 화면에서 검색에 대해 필터링 한다.
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/adm/amd7000/adm7000/selectAdm7000DeptListAjax.do'/>"
							,"loadingShow":true});
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	    	
	    	//toast.push(data.message);
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
						// 상위조직명을 담는 배열 초기화
						parentDeptNames.length = 0;
						//우측 메뉴 정보
						fnGetDeptInfoAjax(treeNode.deptId);
					}
				},
				view : {
					fontCss : getFontCss,	// 폰트 설정
					expandSpeed: "fast",
					showIcon : function(treeId, treeNode) {
						if(typeof zTree != "undefined" && !treeNode.isParent){
							treeNode.isParent = true;
							zTree.updateNode(treeNode);
							zTree.refresh();
						}
						return true;
					}
				}
		    };

		    // zTree 초기화
		    zTree = $.fn.zTree.init($("#deptJson"), setting, data.deptList);
		    
		  	// 조직 선택 팝업이 열릴 때 등록된 조직 목록(zTree)을 모두 펼쳐서 보여준다.
			zTree.expandAll(true);	

		  	// 검색창에 입력된 값을 가져온다.
		  	var searchDeptName = $("#searchTxt").val();
		  	
			// 사용자 등록에서 검색한 조직명 값이 null이 아닐경우
		  	if( !gfnIsNull(searchDeptName) ){
				// 조직 목록 트리에서 검색
		  		fnSearchDeptList();
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


	/**
	 * 	좌측 조직명을 선택 시 우측에 상세정보 보여줌
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
			
	    	// 디테일폼 세팅
	    	gfnSetData2Form(data, "searchDeptFrm");
	    	// 사용여부 세팅
	    	data.useCd == "01" ? $("#useCd").val("사용") : $("#useCd").val("미사용")
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
	 * 	조직 목록에서 조직을 검색한다. 검색된 조직은 좌측 zTree에 표시된다.
	 */
	function fnSearchDeptList(){

		var zTreeObj = $.fn.zTree.getZTreeObj("deptJson");	
	
		// 검색옵션과 검색어를 가져온다
		var selectType = $("#searchSelect option:selected").val();
		var searchVal = $("#searchTxt").val();

		// 검색옵션이 전체보기일 경우
		if(selectType == "rn"){	
			// 트리확장, 하이라이트 초기화, 트리 및 오른쪽 초기화
			zTreeObj.expandAll(true);
			fnSearchDeptUpdate(false);
			fnDataReset();
			toast.push("조직 전체가 조회되었습니다.");
			
		}else if(selectType == "useCd"){	// 검색옵션이 사용유무일 경우	
			searchVal =  $("#selUseCd option:selected").val();
		}

		// 전체검색이 아니고 검색어가 없을경우
		if( selectType != "rn" && gfnIsNull(searchVal) ){
			toast.push("검색어를 입력하세요.");
			return;
		}

		// 하이라이트 초기화
		fnSearchDeptUpdate(false);

		// 검색된 노드
		deptNodeList = zTreeObj.getNodesByParamFuzzy(selectType, searchVal);

		// 전체검색이 아니고 검색결과가 없을경우
		if(selectType != "rn" && deptNodeList.length == 0){
			toast.push("검색된 조직이 없습니다.");
		}

		// 검색된 트리를 확장 및 포커스
		for(var i = 0; i < deptNodeList.length; i++){
			zTree.expandNode(deptNodeList[i], true, true, null, false);
		}

		// 하이라이트 활성화
		fnSearchDeptUpdate(true);

		// 검색결과가 하나라면 자동선택
		if(deptNodeList.length == 1){
			// 검색된 조직명 Node를 선택한다.
			zTreeObj.selectNode(deptNodeList[0], false, false);
			// 우측에 상세정보 표시
			fnGetDeptInfoAjax(deptNodeList[0].deptId);
		}else{
			// 여러개일 경우 트리 및 우측 상세정보 표시부분 초기화
			fnDataReset();
		}
	}
	

	/**
	 * 	 검색된 조직 highlight 처리 
	 *	@param highlight 하이라이트 설정 값(true, false)
	 */
	function fnSearchDeptUpdate(highlight) {
		var zTreeObj = $.fn.zTree.getZTreeObj("deptJson");

		for(var i = 0; i < deptNodeList.length; i++) {
			deptNodeList[i].highlight = highlight;
			zTreeObj.updateNode(deptNodeList[i]);
		}
	}
	
	/**
	 * 	선택된 조직의 상위 조직 명을 모두 가져오는 함수
	 *	@param selectedNode 선택된 조직 Node
	 */
	function fnGetSelectedDeptParentNodes(selectedNode){
		
		// 선택된 조직명을 배열에 추가
		parentDeptNames.push(selectedNode.deptName);
		
		// 부모가 있고, 상위 조직ID가 존재할 경우
		if( selectedNode.isParent && selectedNode.upperDeptId != null ){
			// 부모를 가져오고, 재귀호출
			var parentNode = selectedNode.getParentNode();
			fnGetSelectedDeptParentNodes(parentNode);
		}
	}
	
	/**
	 * 	조직을 선택한다.
	 */
	function fnSelectDept(){
		
		var zTreeObj = $.fn.zTree.getZTreeObj("deptJson");
		
		// zTree에서 현재 선택된 조직을 가져온다.
		var selectedNode = zTreeObj.getSelectedNodes()[0];
		
		// 조직을 선택하지 않을 경우
		if(selectedNode == null){
			jAlert("조직을 선택해주세요", "알림창");
			return false;
		}
		
		// 미사용중인 조직을 선택할 경우
		if(selectedNode.useCd == "02"){
			jAlert("미사용중인 조직은 선택할 수 없습니다.", "알림창");
			return false;
		}
		
		// 선택된 조직의 상위 조직명을 구한다.
		fnGetSelectedDeptParentNodes(selectedNode);
		
		// 선택된 조직부터 최상위 조직까지 조직명이 담긴 배열의 순서를 뒤집는다.
		// (최상위 > 1번 > 2번, 이렇게 선택한 조직명이 맨 마지막에 오도록)
		var deptArr = parentDeptNames.reverse();
		var deptNames = "";
		
		for (var i = 0; i < deptArr.length; i++){
			deptNames += deptArr[i];
			if(i != deptArr.length-1 ){
				deptNames += " > ";
			}
		}

		gfnSelectDeptTree(selectedNode.deptId, deptNames);
	}
	


	
	/**
	 * 	데이터 초기화, 트리 및 우측 상세정보 표시부분을 초기화
	 */
	function fnDataReset(){
		$("#deptId").val("");
		$("#upperDeptId").val("");
		$("#deptName").val("");
		$("#ord").val("");
		$("#input:radio[name=useCd]").removeAttr("checked");
		$("#regDtm").val("");
		$("#deptEtc").val("");
		zTree.refresh();
	}
	
	/**
	 * 	검색옵션(select) 변경시 이벤트 처리함수
	 */
	function fnChangeElement(){
		
		var txtElmt = $("#searchTxt");
		var useElmt = $("#selUseCd");
		var searchOpt = $("#searchSelect").val();
		
		if(searchOpt == "rn"){
			useElmt.css("display","none");
			txtElmt.css("display","block");
			txtElmt.attr("readonly","readonly");
			txtElmt.val("");
		}else if(searchOpt == "useCd"){
			useElmt.css("display","block");
			txtElmt.css("display","none");
		}else{
			useElmt.css("display","none");
			txtElmt.css("display","block");
			txtElmt.removeAttr("readonly");
			txtElmt.val("");
		}
	}
	
	/**
	 * 	 zTree View Font 설정 함수
	 */
	function getFontCss(treeId, treeNode) {

		// 검색된 결과가 있을 경우
		if(treeNode.highlight){
			return {color:"#F40404", "font-weight":"bold"};
		// 검색된 결과가 없고, 사용유무가 미사용일 경우	
		}else if( !treeNode.highlight && treeNode.useCd == "02"){
			return {color:"#ddd", "font-weight":"normal"};
		// 검색된 결과가 없고, 사용유무가 사용일 경우	
		}else if( !treeNode.highlight && treeNode.useCd == "01" ){
			return {color:"#333", "font-weight":"normal"};
		}
	}

	
</script>

<div class="popup">
	<form id="searchDeptFrm"  onsubmit="return false;">
	<div class="pop_title">조직 검색</div>
	
	<div class="pop_sub" id="pop_sub_wrap">
	
		<div class="pop_top_wrap">
			<div class="searchItem">
				<span class="button_normal2 btn_inquery" id="btn_search_dept_pop"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;검색</span>
			</div>
			<div class="searchItem">
				<input type="text" name="searchTxt" id="searchTxt" title="" value="${param.searchDeptNm}" readonly="readonly" class="searchInputTextItem">
				<select name="selUseCd" id="selUseCd"></select>
			</div>
			<div class="searchItem">
				<span class="span_searchSelect">
					<select id="searchSelect" name="searchSelect" style="float: right;">
						<option value="rn">전체 보기</option>
						<option value="deptName">조직명</option>
						<option value="useCd">사용유무</option>
						<option value="deptEtc">비고</option>	
					</select>
				</span>
			</div>
		</div>
		<div class="pop_sub" style="padding: 10px 20px 0 20px;">
			<div class="pop_left" id="pop_left_wrap">
	 			<div class="pop_menu_ctrl_wrap">
					<div class="menu_ctrl_btn_wrap" id="pop_menu_btn">
						 <div class="menu_all_wrap">
							<span class="menu_expand_all" title="전체 열기"></span>
							<span class="menu_collapse_all" title="전체 닫기"></span>
						</div>
					</div>
	
					<div class="menu_lists_wrap" id="divMenu">
						<ul id="deptJson" class="ztree"></ul>
					</div>
				</div>
			</div>
			
			<div class="pop_right" id="pop_right_wrap">
				<div class="pop_left">조직 ID</div>
				<div class="pop_right">
					<input type="text" id="deptId" name="deptId" title="조직 ID" class="input_txt readonly" readonly="readonly">
				</div>
			
				<div class="pop_left">상위 조직 ID</div>
				<div class="pop_right">
					<input type="text" id="upperDeptId" name="upperDeptId" title="상위 조직 ID" class="input_txt readonly" readonly="readonly">
				</div>
				
				<div class="pop_left">조직명</div>
				<div class="pop_right">
					<input type="text" id="deptName" name="deptName" title="조직명" class="input_txt readonly" readonly="readonly" maxlenth="100" >
				</div>
		
				<div class="pop_left">순번</div>
				<div class="pop_right">
					<input type="number" id="ord"  name="ord" min="1"  value="" class="readonly" readonly="readonly">	
				</div>
				
				<div class="pop_left">사용유무</div>
				<div class="pop_right">
					<!-- 
					<input id="available" type="radio" name="useCd"  value="01" disabled="disabled"/><label for="available">사용</label>
					<input id="notAvailable" type="radio" name="useCd" value="02" disabled="disabled"/><label for="notAvailable">미사용</label> 
					-->
					<input type="text" id="useCd" name="useCd" title="사용유무" class="input_txt readonly" readonly="readonly" maxlenth="10" >
				</div>
		
				<div class="pop_left">생성일</div>
				<div class="pop_right">
					<input type="text" id="regDtm"  name="regDtm" value="" class="input_txt readonly" readonly="readonly">	
				</div>
		
				<div class="pop_left textarea_height bottom_line" style="height: 140px; margin-bottom:10px;">비고</div>
				<div class="pop_right bottom_line" style="height: 140px; margin-bottom:10px;">
					<textarea class="input_note readonly" title="비고" id="deptEtc" name="deptEtc" maxlenth="4000" disabled="disabled"></textarea>
				</div>
			</div>
		</div>
		
		<div class="btn_div" style="padding-top:10px">
			<div class="button_normal save_btn" id="btn_select_dept" >선택</div>
			<div class="button_normal exit_btn">취소</div>
		</div>
	</form>	
	</div>
</div>	
</html>