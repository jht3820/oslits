<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<style type="text/css">
	.accptFont{color:#4b73eb !important;text-shadow: none !important;}
	.rejectFont{color:#eb4b6a !important;text-shadow: none !important;}
	.defaultFont{color:#000 !important;}
</style>
<script>
var mySearch;

var zTreeStm2100;

var currentNode={};

var selSvnId;
$(function(){
	//타겟이 상세정보 창이 아닌 경우 상세 정보 창 닫기
		
	//그리드 검색 호출
	fnAxGrid5View();
	fnSearchBoxControl();
	fnSearchProjectList();

	$(".menu_expand_all").click(function(){
		zTreeStm2100.expandAll(true);
	});
	
	// 메뉴 관리 전체 닫기
	$(".menu_collapse_all").click(function(){
		zTreeStm2100.expandAll(false);
	});
	

	//가이드 상자 호출
	gfnGuideStack("add",fnStm2100GuideShow);
	
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
            headerSelect: false,
            columns: [
				
				{key: "svnRepNm", label: "REPOSITORY 명", width: 150, align: "left"},
				{key: "svnTxt", label: "REPOSITORY 설명", width: 200, align: "left"},
				{key: "svnRepUrl", label: "URL", width: 250, align: "left"},
				{key: "useNm", label: "사용여부", width: 100, align: "center"},
				{key: "regUsrId", label: "요청자", width: 100, align: "center"},
				{key: "regDtm", label: "등록일자", width: 150, align: "center"}
				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	selSvnId = this.item.svnRepId;
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
				{"url":"<c:url value='/stm/stm2000/stm2100/selectStm2100ProjectListAjax.do'/>","loadingShow":true}
				, {"prjId" : prjId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.svnList;
		
			
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
      						{label:"", labelWidth:"", type:"button", width:"120",style:"float:right;", key:"btn_search_revision",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>SVN리비젼 확인</span>",
      							onclick:function(){
      								var selNode = zTreeStm2100.getSelectedNodes()[0];
									if(gfnIsNull(selNode)){
										jAlert("프로젝트를 선택해주세요.","알림");
										return false;
									}
									
									var prjId = selNode.prjId;
      								gfnSvnRevisionPopup(prjId,"admin",false,function(data){
      								});								
      						}}
                    						
                    ]},				      
					{display:true, addClass:"", style:"", list:[
						
					
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_print_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
							onclick:function(){
								$(firstGrid.exportExcel()).printThis({importCSS: false,importStyle: false,loadCSS: "/css/common/printThis.css"});
						}},
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_excel_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
							onclick:function(){
								firstGrid.exportExcel("REPOSITORY 관리 목록.xls");
						}},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_insert_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
							onclick:function(){
								var selNode = zTreeStm2100.getSelectedNodes()[0];
								if(gfnIsNull(selNode)){
									jAlert("프로젝트를 선택해주세요.","알림");
									return false;
								}
								
								var prjId = selNode.prjId;
								
								var data = {
									"prjId": prjId
								};
								gfnLayerPopupOpen('/stm/stm2000/stm2100/selectStm2101View.do',data,"640","583",'scroll');
						}},
						{label:"", labelWidth:"", type:"button", width:"120",style:"float:right;", key:"btn_insert_auth",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>역할그룹 설정</span>",
							onclick:function(){
								var selNode = zTreeStm2100.getSelectedNodes()[0];
								if(gfnIsNull(selNode)){
									jAlert("프로젝트를 선택해주세요.","알림");
									return false;
								}
								if(firstGrid.selectedDataIndexs.length == 0){
									jAlert("REPOSITORY를 선택해주세요.","알림");
									return false;
								}
								var prjId = selNode.prjId;
								
								var data = {
									"prjId": prjId,
									"svnRepId":selSvnId
								};
								gfnLayerPopupOpen('/stm/stm2000/stm2100/selectStm2102View.do',data,"640","370",'scroll');
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
	    	
	    	// zTreeStm2100 설정 
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

		    // zTreeStm2100 초기화
		    zTreeStm2100 = $.fn.zTree.init($("#prjTreeJson"), setting, data.prjList);
		    
		    var treeNode = [];
		    // 프로젝트 목록이 있을 경우
		    if(data.prjList.length>0){
		    	treeNode =  zTreeStm2100.getNodes();
		    	// 프로젝트 트리를 모두 펼친다.
		    	zTreeStm2100.expandAll(true);
		    	// 트리노드가 존재하고 트리노드가 1개 이상 있을경우
		    	if(!gfnIsNull(treeNode) && treeNode.length>0){
		    		// 첫번째 트리노드의 자식 노드를 가져온다.
		    		var childNodes = treeNode[0].children;
		    		// 자식노드가 존재하고, 자식노드가 1개이상 있을경우
		    		if(!gfnIsNull(childNodes) && childNodes.length>0){
		    			currentNode=childNodes[0];
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
	
	//가이드 상자
	function fnStm2100GuideShow(){
		var mainObj = $(".main_contents");
		
		//mainObj가 없는경우 false return
		if(mainObj.length == 0){
			return false;
		}
		//guide box setting
		var guideBoxInfo = globals_guideContents["stm2100"];
		gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
	}
</script>


<div class="main_contents"  style="height: auto;" >
			<form:form commandName="svn1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
			</form:form>
			<div class="req_title">${sessionScope.selMenuNm }</div>
			<div class="tab_contents menu">
				<input type="hidden" name="strInSql" id="strInSql" />
				<div id="AXSearchTarget" style="border-top:1px solid #ccc;" guide="stm2100button"  ></div>
				<br />
				<div class="menu_wrap">
				<div class="menu_ctrl_wrap">
					<div class="menu_ctrl_btn_wrap">	
						<div class="menu_all_wrap">
							<span class="menu_expand_all" title="전체 열기"></span>
							<span class="menu_collapse_all" title="전체 닫기"></span>
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