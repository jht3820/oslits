<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<style type="text/css">
	.accptFont{color:#4b73eb !important;text-shadow: none !important;}
	.rejectFont{color:#eb4b6a !important;text-shadow: none !important;}
	.defaultFont{color:#000 !important;}
</style>
<script>
var mySearch;

var zTree;

var currentNode={};

var selJenId;

$(function(){
	//타겟이 상세정보 창이 아닌 경우 상세 정보 창 닫기
	
	//그리드 검색 호출
	fnAxGrid5View();
	fnSearchBoxControl();
	fnSearchProjectList();
	
	
	$(".menu_expand_all").click(function(){
		zTree.expandAll(true);
	});
	
	// 메뉴 관리 전체 닫기
	$(".menu_collapse_all").click(function(){
		zTree.expandAll(false);
	});
	
	
});


//검색조건 변경되었을때 이벤트
function fnSelectChg(){
	var selVal = $("#searchSelect option:selected").val();
	if(selVal == '0'){
		$("#searchTxt").val("");
		$("#searchTxt").attr("readonly", true);
	}else{
		$("#searchTxt").attr("readonly", false);
	}
}

//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            showRowSelector: false,
            sortable:false,
            header: {align:"center"},
           multipleSelect: false ,
            columns: [
				
				{key: "jenNm", label: "JOB 명", width: '25%', align: "left"},
				{key: "jenTxt", label: "JOB 설명", width: '35%', align: "center"},
				{key: "jenUrl", label: "URL", width: '32%', align: "center"},
				{key: "useNm", label: "사용여부", width: '10%', align: "center"}
				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	selJenId = this.item.jenId;
                	firstGrid.select(this.doindex, {selected: true});
                }
            }
        });

}
//그리드 데이터 넣는 함수
function fnInGridListSet(prjId){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm3000/stm3100/selectStm3100JenkinsProjectListAjax.do'/>","loadingShow":true}
				, {"prjId" : prjId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.jenList;
		
			
		   	firstGrid.setData(list);
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
}
//검색 상자
function fnSearchBoxControl(){
	var pageID = "AXSearch";
	mySearch = new AXSearch();

	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySearch.setConfig({
				targetID:"AXSearchTarget",
				theme : "AXSearch",
				rows:[
					{display:true, addClass:"", style:"", list:[
						
					
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_print_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
							onclick:function(){
								$(firstGrid.exportExcel()).printThis();
						}},
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_excel_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
							onclick:function(){
								firstGrid.exportExcel("신규 요구사항 요청 목록.xls");
						}},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_insert_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
							onclick:function(){
								var selNode = zTree.getSelectedNodes()[0];
								if(gfnIsNull(selNode)){
									jAlert("프로젝트를 선택해주세요.","알림");
									return false;
								}
								
								var prjId = currentNode.prjId;
								
								var data = {
									"prjId": prjId,
								};
								gfnLayerPopupOpen('/stm/stm3000/stm3100/selectStm3101View.do',data,"640","583",'scroll');
						}},
						{label:"", labelWidth:"", type:"button", width:"120",style:"float:right;", key:"btn_insert_auth",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>역할그룹 설정</span>",
							onclick:function(){
								var selNode = zTree.getSelectedNodes()[0];
								if(gfnIsNull(selNode)){
									jAlert("프로젝트를 선택해주세요.","알림");
									return false;
								}
								if(firstGrid.selectedDataIndexs.length == 0){
									jAlert("Jenkins을 선택해주세요.","알림");
									return false;
								}
								var prjId = selNode.prjId;
								
								var data = {
									"prjId": prjId,
									"jenId":selJenId
								};
								gfnLayerPopupOpen('/stm/stm3000/stm3100/selectStm3102View.do',data,"640","330",'scroll');
						}}
						
					]}
				]
			});
		}
		/*,
		search1: function(){
			var pars = mySearch.getParam();
			fnAxGridView(pars);
		}
		*/
	};
	
	jQuery(document.body).ready(function(){
		
		fnObjSearch.pageStart();
		//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
		axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");
		
		//공통코드 selectBox hide 처리
		axdom("#" + mySearch.getItemId("searchCd")).hide();

		//버튼 권한 확인
		fnBtnAuthCheck(mySearch);
		
	});
}

	function fnSearchProjectList(){
		
		var licGrpId = '${sessionScope.loginVO.licGrpId }'; 
		var authGrpId = '${sessionScope.selAuthGrpId }';
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/prj/prj1000/prj1000/selectPrj1000ProjectGroupListAjax.do'/>"}
				,{ "licGrpId":licGrpId, "authGrpId":authGrpId });
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
						pIdKey: "upperId",
						rootPId: '000'
		            }
		        },
				callback: {
					onClick: function(event, treeId, treeNode){
						//우측 메뉴 정보
						if(treeNode.level==1){
							currentNode =treeNode;
							fnInGridListSet(currentNode.prjId);
						}
						
					}
				},
				view : {
					showIcon : function(treeId, treeNode) {
						if(treeNode.level ==0){
							treeNode.isParent = true;
						}
						return true;
					}
				}
		    };

		    // zTree 초기화
		    zTree = $.fn.zTree.init($("#prjTreeJson"), setting, data.prjList);
		    
		    var treeNode = [];
		    if(data.prjList.length>0){
		    	treeNode =  zTree.getNodes();
		    	if(treeNode.length>0){
		    		var childNodes = treeNode[0].children;
		    		if(childNodes.length>0){
		    			currentNode=childNodes[0];
		    			var treeObj = $.fn.zTree.getZTreeObj("prjTreeJson");
		    			
		    			treeObj.expandAll(true);
		    			
		    			//fnInGridListSet(childNodes[0].prjId);
		    		}
			    	
			    }
		    }
		    
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			jAlert(data.message,"알림창");
	 	});
		//AJAX 전송
		ajaxObj.send();
	}
</script>


		<div class="main_contents" style="height: auto;">
			<form:form commandName="stm3100VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
			</form:form>
			<div class="req_title">${sessionScope.selMenuNm }</div>
			<div class="tab_contents menu">
				<input type="hidden" name="strInSql" id="strInSql" />
				<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
				<br />
				<div class="menu_wrap">
				<div class="menu_ctrl_wrap">
					<div class="menu_ctrl_btn_wrap">	
						<div class="menu_all_wrap">
							<span class="menu_expand_all" title="전체 열기"></span><span class="menu_collapse_all" title="전체 닫기"></span>
						</div>
					</div>
					
					<div class="menu_lists_wrap">
						<ul id="prjTreeJson" class="ztree"></ul>
					</div>
				</div>
				
				<div class="menu_info_wrap">
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>
				</div>
					
			</div>
		</div>
		
		
		
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />