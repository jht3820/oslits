<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style type="text/css">
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 54px; }
.button_normal { width: 39px; height: 22px; line-height: 22px; text-align: center; font-weight: bold; font-size: 1em; border-radius: 5px; box-shadow: 1px 1px 1px #ccd4eb; margin: 0 auto; border: 1px solid #b8b8b8; cursor: pointer; }
div.pop_sub .pop_left {width:28%;} /* common.css pop_left width값 오버라이딩 */
div.pop_sub .pop_right {width:72%;} /* common.css pop_left width값 오버라이딩 */
.input_txt {padding-left: 5px;}
.pop_menu_row { width: 100%; }


</style>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<script type="text/javascript">

	var zTree;
	// 선택된 분류 및 상위 분류명을 담을 배열
	var parentReqClsNames = [];	

	$(document).ready(function() {
		//달력 세팅 (배포일)
		fnSearchReqClsList();
		
		/* 취소 */
		$('#btnPopCmm1500Cancle').click(function() {
			gfnLayerPopupClose();
		});
		
		$('#btnPopCmm1500Select').click(function() {
			//gfnCheckRow(popGrid);
			var nodes = zTree.getSelectedNodes();
			
			// 최상위 분류부터 선택된 분류까지 분류명을 하나의 문자열로 가져온다.
			var reqClsNmStr = $("#popReqClsNm").val();

			gfnSelectClsTree(nodes[0].reqClsId, reqClsNmStr);
		});
		
		$(".menu_wrap .menu_expand_all").click(function(){
			zTree.expandAll(true);
		});
		
		// 메뉴 관리 전체 닫기
		$(".menu_wrap .menu_collapse_all").click(function(){
			zTree.expandAll(false);
		});
	});
	

	/**
	 * 조회버튼 클릭시 요구사항 분류 리스트 조회 AJAX
	 */
	function fnSearchReqClsList(){
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4200/selectReq4200ReqClsListAjax.do'/>"});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	    	
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
						
						// 분류명을 담을 배열 초기화
						parentReqClsNames.length = 0;
						
						// 선택된 분류의 상위 분류 명을 모두 가져온다.
						fnGetSelectedReqClsParentNodes(treeNode);
							
						// 최상위 분류부터 선택된 분류까지 분류명을 하나의 문자열로 가져온다.
						var reqClsNmStr = fnGetReqClsNameListStr(parentReqClsNames);
							
						//$("#popReqClsNm").val(treeNode.reqClsNm);
						$("#popReqClsNm").val(reqClsNmStr);
					
					}
				},
				view : {
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
		    zTree = $.fn.zTree.init($("#reqClsJson"), setting, data.reqClsList);
		    
	    
		  	//폴더의 계층구조가 3단계가 아니면  tree전체 펼침 시에 일회적 동작 안함(좋은방법같진않고 임시방편 추후개선)
			zTree.expandAll(false);
		});
		
		//AJAX 전송
		ajaxObj.send();
		
		
		
	}

	/**
	 * 	선택된 분류의 상위 분류 명을 모두 가져오는 함수
	 *	@param selectedNode 선택된 분류 Node
	 */
	function fnGetSelectedReqClsParentNodes(selectedNode){
		
		// 선택된 분류명을 배열에 추가
		parentReqClsNames.push(selectedNode.reqClsNm);
	
		// 상위 분류ID가 존재할 경우
		if( selectedNode.upperReqClsId != null ){
			// 부모를 가져오고, 재귀호출
			var parentNode = selectedNode.getParentNode();
			fnGetSelectedReqClsParentNodes(parentNode);
		}
	}

	/**
	 * 	선택된 분류의 상위 분류 명 하나의 문자열로 만들어서 리턴
	 *	@param selectedNode 선택된 분류 Node
	 */
	function fnGetReqClsNameListStr(reqClsNmArr){
		
		// 재귀호출로 담은 요구사항 분류를 역순으로 정렬
		var reqClsNmArr = parentReqClsNames.reverse();
		var reqClsNames = "";
		
		for (var i = 0; i < reqClsNmArr.length; i++){
			reqClsNames += reqClsNmArr[i];  
			if(i != reqClsNmArr.length-1 ){
				reqClsNames += " > ";
			}
		}
		
		return reqClsNames;
	}
	

</script>

<div class="popup">
		
	<div class="pop_title">요구사항 분류 조회</div>
	<div class="pop_sub">
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="popReqClsNm">요구사항 분류</label></div>
			<div class="pop_menu_col2"><input id="popReqClsNm" type="text" name="popReqClsNm" title="요구사항 분류" readonly maxlength="11" value="" /></div>
		</div>
		<div class="menu_wrap">
			<div class="menu_ctrl_btn_wrap" >
				<div class="menu_all_wrap">
					<span class="menu_expand_all" title="전체 열기"></span>
					<span class="menu_collapse_all" title="전체 닫기"></span>
				</div>
			</div>
				
			<div class="menu_lists_wrap" style ="overflow-y:scroll;height:300px;">
				<ul id="reqClsJson" class="ztree"></ul>
			</div>
		</div>
		<div class="btn_div">
			<div class="button_normal save_btn" id="btnPopCmm1500Select" >선택</div>
			<div class="button_normal exit_btn" id="btnPopCmm1500Cancle" >취소</div>
		</div>
		
	</div>
	</form>
</div>
</html>