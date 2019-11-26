<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslits/top/aside.jsp" />
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<style>
/* 모바일 @media 제거 */
/*
@media screen and (max-width: 1024px) {
	.menu_wrap { height: 100%; }
	.menu_ctrl_wrap { float: none; width: 100%; height: 30%; border-right: transparent; border-bottom: 1px solid #ccc; }
		.menu_ctrl_wrap { height: 240px !important; }
	.menu_info_wrap { width: 100%; height: 70%; padding: 20px; margin-left: 0; border-left: none;}
		.bottom_one_table { height: 169px; }
		.middle_table { height: 14%; padding: 12px; }
		.bottom_one_table { height: 43%; }
}
*/
.tab_contents.menu {height:100%;}
.bottom_one_table{height:auto;}
#rootBtnNone,#defaultBtn{text-align:center;width:100%;}
/* #rootBtnNone, .button_check{display:none;} */
 #rootBtnNone{display:none;} 
.menu_ctrl_wrap {min-height: 700px !important;}
</style>

<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<script>
//zTree
var zTree;
var btnAuthInsertYn = '${sessionScope.selBtnAuthInsertYn}';
var btnAuthDeleteYn = '${sessionScope.selBtnAuthDeleteYn}';

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
	
	//초기 메뉴 세팅
	fnSearchReqClsList();

	$('.button_check').hide(); 
	$('[data-ax5grid]').hide();

	// 메뉴 관리 전체 열기
	$(".menu .menu_expand_all").click(function(){
		zTree.expandAll(true);
	});
	
	// 메뉴 관리 전체 닫기
	$(".menu .menu_collapse_all").click(function(){
		zTree.expandAll(false);
	});
	
	// 메뉴 관리 조회 버튼 클릭 - 조회
	$("#btn_search_reqCls").click(function(){
		fnSearchReqClsList();
	});

	//추가 버튼 이벤트
	$('#btn_insert_reqClsAdd').click(function(){
		var chkArr = secondGrid.getList('selected');
		
		var strInSql = "";

		//체크한 행이 없으면 메시지 띄우고 리턴
		if(gfnIsNull(chkArr)) {
			toast.push('추가할 요구사항을 체크하세요.'); 
			return;
		}

		//체크된 요구사항의 ID를 IN 쿼리로 만들수 있도록 'reqId', 'reqId' 형식으로 감싸기
		$(chkArr).each(function(idx, data){
			strInSql += "'" + data.reqId + "',";
		});
		
		//마지막 , 자르기
		strInSql = strInSql.substring(0,strInSql.length-1);
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4200/updateReq4200ReqClsAddDelListAjax.do'/>","loadingShow":false}
				,{ "strInSql":strInSql, "mode":"add", "reqClsId":zTree.getSelectedNodes()[0].reqClsId});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
        	//로딩바 닫기
    		gfnShowLoadingBar(false);
        	
        	//처리 실패시 빠져 리턴
        	if(data.saveYN == "N"){
        		toast.push(data.message);
        		return;
        	}
        	else{
        		//그리드 새로고침
        		fnAxGrid5View_first(zTree.getSelectedNodes()[0].reqClsId);
				fnAxGrid5View_second(zTree.getSelectedNodes()[0].reqClsId);
        	}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
	});
	
	//삭제 버튼 이벤트
	$('#btn_delete_reqClsDel').click(function(){
		var chkArr = firstGrid.getList('selected');
		var strInSql = "";

		//체크한 행이 없으면 메시지 띄우고 리턴
		if(gfnIsNull(chkArr)) {
			toast.push('추가할 요구사항을 체크하세요.'); 
			return;
		}

		//체크된 요구사항의 ID를 IN 쿼리로 만들수 있도록 'reqId', 'reqId' 형식으로 감싸기
		$(chkArr).each(function(idx, data){
			strInSql += "'" + data.reqId + "',";
		});
		
		//마지막 , 자르기
		strInSql = strInSql.substring(0,strInSql.length-1);
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4200/updateReq4200ReqClsAddDelListAjax.do'/>","loadingShow":false}
				,{ "strInSql":strInSql, "mode":"del", "reqClsId":zTree.getSelectedNodes()[0].reqClsId});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
        	//처리 실패시 빠져 리턴
        	if(data.saveYN == "N"){
        		toast.push(data.message);
        		return;
        	}
        	else{
        		//그리드 새로고침
        		fnAxGrid5View_first(zTree.getSelectedNodes()[0].reqClsId);
				fnAxGrid5View_second(zTree.getSelectedNodes()[0].reqClsId);
        	}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
		});
		
		//AJAX 전송
		ajaxObj.send();
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
					/* ROOT 분류에도 요구사항 배정 가능 */
						
					//선택한 요구사항 분류의 배정 및 미배정 정보 조회
					//fnSearchReqClsAddDelListAjax($(this).attr('id'));
					$('[data-ax5grid]').show();
			
					fnAxGrid5View_first(treeNode.reqClsId);
					fnAxGrid5View_second(treeNode.reqClsId);
			
					fnSearchUpBoxControl();
					fnSearchDownBoxControl();
					//$('.button_check').show();
			
					// 버튼 권한 체크
					btnAuthInsertYn == 'Y' ? $("[id^=btn_insert]").show() : $("[id^=btn_insert]").hide();
					btnAuthDeleteYn == 'Y' ? $("[id^=btn_delete]").show() : $("[id^=btn_delete]").hide();
					
					$('#rootBtnNone').hide();
					$('#defaultBtn').hide();
					//상세정보 보이기
					$('button[id$=btn_search_reqCls]').show();
					$('button[id$=btn_select_detailInfo]').show();
				
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
	
	$('#defaultBtn').show();
	//표시되있던 정보 감추기
	$('.button_check').hide();
	$('#AXGridTargetUp').html('');
	$('#AXGridTargetDown').html('');
	
	//루트 클릭
	$('.menu_root_info').click();
}


//axisj5 그리드 개발주기 배정된 요구사항
function fnAxGrid5View_first(reqClsId){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            showRowSelector: true,
            sortable:false,
            header: {align:"center"},
            frozenColumnIndex: 2,
            columns: [
					{key: "reqOrd", label: "순번", width: '11%', align: "center"}  
					,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left"}
					,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
					,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
					,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
					,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
					,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"}
					,{key: "regDtmDay", label: "등록일자", width: '14%', align: "center"}
					,{key: "reqStDtm", label: "시작 기간", width: '14%', align: "center"}
					,{key: "reqEdDtm", label: "종료 기간", width: '14%', align: "center"}
					,{key: "reqStDuDtm", label: "시작 예정일자", width: '15%', align: "center"}
					,{key: "reqEdDuDtm", label: "종료 예정일자", width: '15%', align: "center"}
					,{key: "orgReqId", label: "체계별 요구사항ID", width: '15%', align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30
                , onClick: function () {
                	firstGrid.select(this.doindex, {selected: true});
                }
                ,onDBLClick:function(){
                	var data = {"mode": "req", "reqId": this.item.reqId}; 
					gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
                }
            },
            contextMenu: {
                iconWidth: 20,
                acceleratorWidth: 100,
                itemClickAndClose: false,
                icons: {
                    'arrow': '<i class="fa fa-caret-right"></i>'
                },
                items: [
                    {type: "reply", label: "쪽지 전송", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
                   	{divide: true},
                    {type: "rowFrozen", label: "열 고정", icon:"<i class='fa fa-lock' aria-hidden='true'></i>"}
                ],
                popupFilter: function (item, param) {
                	var selItem = firstGrid.list[param.doindex];
                	//선택 개체 없는 경우 중지
                	if(typeof selItem == "undefined"){
                		return false;
                	}
                	return true;
                },
                onClick: function (item, param) {
					//열 고정
                    if(item.type == "rowFrozen"){
                    	//이미 해당 열에 고정인경우 중지
                    	if(firstGrid.config.frozenColumnIndex != (param.colIndex+1)){
                    		firstGrid.setConfig({frozenColumnIndex:param.colIndex+1});
                    		fnInGridListSet();
                    	}
                    //쪽지 전송
                    }else if(item.type == "reply"){
                    	gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
                    }
                    firstGrid.contextMenu.close();
                }
            }
        });
        //그리드 데이터 불러오기
 		fnInGridListSet(null,{grid:firstGrid,data:{ "clsMode":"clsAdd","reqClsId":reqClsId}});

}

//axisj5 그리드 개발주기 미 배정 요구사항
function fnAxGrid5View_second(reqClsId){
	secondGrid = new ax5.ui.grid();
 
        secondGrid.setConfig({
            target: $('[data-ax5grid="second-grid"]'),
            showRowSelector: true,
            sortable:false,
            header: {align:"center"},
            frozenColumnIndex: 2,
            columns: [
					{key: "reqOrd", label: "순번", width: '11%', align: "center"}    
  					,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left" }
					,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
					,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
					,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
					,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
					,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"}
					,{key: "regDtmDay", label: "등록일자", width: '14%', align: "center"}
					,{key: "reqStDtm", label: "시작 기간", width: '14%', align: "center"}
					,{key: "reqEdDtm", label: "종료 기간", width: '14%', align: "center"}
					,{key: "reqStDuDtm", label: "시작 예정일자", width: '15%', align: "center"}
					,{key: "reqEdDuDtm", label: "종료 예정일자", width: '15%', align: "center"}
					,{key: "orgReqId", label: "체계별 요구사항ID", width: '15%', align: "center"}

            ],
            body: {
                align: "center",
                columnHeight: 30
                , onClick: function () {
                	secondGrid.select(this.doindex, {selected: true});
                }
                ,onDBLClick:function(){
                	var data = {"mode": "req", "reqId": this.item.reqId}; 
					gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
                }
            },
            contextMenu: {
                iconWidth: 20,
                acceleratorWidth: 100,
                itemClickAndClose: false,
                icons: {
                    'arrow': '<i class="fa fa-caret-right"></i>'
                },
                items: [
                    {type: "reply", label: "쪽지 전송", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
                   	{divide: true},
                    {type: "rowFrozen", label: "열 고정", icon:"<i class='fa fa-lock' aria-hidden='true'></i>"}
                ],
                popupFilter: function (item, param) {
                	var selItem = firstGrid.list[param.doindex];
                	//선택 개체 없는 경우 중지
                	if(typeof selItem == "undefined"){
                		return false;
                	}
                	return true;
                },
                onClick: function (item, param) {
					//열 고정
                    if(item.type == "rowFrozen"){
                    	//이미 해당 열에 고정인경우 중지
                    	if(secondGrid.config.frozenColumnIndex != (param.colIndex+1)){
                    		secondGrid.setConfig({frozenColumnIndex:param.colIndex+1});
                    		fnInGridListSet();
                    	}
                    //쪽지 전송
                    }else if(item.type == "reply"){
                    	gfnAlarmOpen(param.item.reqChargerId,param.item.reqId,param.item.reqNm);
                    }
                    secondGrid.contextMenu.close();
                }
            }
        });
        //그리드 데이터 불러오기
 		fnInGridListSet(null,{grid:secondGrid,data:{ "clsMode":"clsDel","reqClsId":reqClsId}});

}
//그리드 데이터 넣는 함수
function fnInGridListSet(ajaxParam,gridTarget){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	if(gfnIsNull(ajaxParam)){
   			ajaxParam = $('form#searchFrm').serialize();
   		}
     	
     	//데이터 세팅
     	if(!gfnIsNull(gridTarget.data)){
     		ajaxParam += "&"+$.param(gridTarget.data);
     	}
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4200/selectReq4200ReqClsAddDelListAjax.do'/>","loadingShow":true}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			
		   	gridTarget.grid.setData(list);
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
function fnSearchUpBoxControl(){
	var pageID = "AXSearch";
	mySearchUp = new AXSearch();

	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySearchUp.setConfig({
				targetID:"AXSearchTargetUp",
				theme : "AXSearch",
				rows:[
					{display:true, addClass:"top_searchGroup", style:"", list:[
           				{label:"", labelWidth:"", type:"button", width:"100",style:"float:right;", key:"btn_search_reqCls",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>요구사항 조회</span>",
						onclick:function(){
							fnAxGrid5View_first(zTree.getSelectedNodes()[0].reqClsId);
							fnAxGrid5View_second(zTree.getSelectedNodes()[0].reqClsId);
						}}
						
					]}
				]
			});
		}	
	};
	
	jQuery(document.body).ready(function(){
		fnObjSearch.pageStart();
		//버튼 권한 확인
		fnBtnAuthCheck(mySearchUp);
		
		// 상단 요구사항 조회 버튼이 권한이 없어서 hide일 경우
		// 해당 버튼이 있는 div도 hide 처리해야함 
		// $(".top_searchGroup") div의 하위에 있는 버튼 목록을 가져온다.
		var childList = $(".top_searchGroup").children('.searchItem');

		var childCnt = 0;
		
		// 가져온 버튼의 hide 여부 체크
		$.each(childList,function(idx, child){
			if($(this).is(':visible') == false ){
				childCnt ++;
			}
		});
		
		// 버튼의 개수와 hide된 버튼의 수가 같다면 
		// $(".top_searchGroup") div를 hide 처리한다.
		if(childList.length == childCnt){
			$(".top_searchGroup").hide();
		}
		
	});
}
//검색 상자
function fnSearchDownBoxControl(){
	var pageID = "AXSearch";
	mySearchDown = new AXSearch();
	
	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySearchDown.setConfig({
				targetID:"AXSearchTargetDown",
				theme : "AXSearch"
			});
		}	
	};
	
	jQuery(document.body).ready(function() {
		fnObjSearch.pageStart();
		//버튼 권한 확인
		fnBtnAuthCheck(mySearchDown);
	});
}
</script>

<div class="main_contents">
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
		<span style="float:left;margin-right: 20px;">*분류메뉴에 요구사항을 배정합니다.</span>
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
		<div class="top_control_wrap">
			<span class="button_normal2 btn_save" id="btn_search_reqCls"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;분류 조회</span>
		</div>
			
		<div class="menu_wrap">
			<div class="menu_ctrl_wrap">
				<div class="menu_ctrl_btn_wrap">
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span><span class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>
				
				<div class="menu_lists_wrap">
					<ul id="reqClsJson" class="ztree"></ul>
				</div>
			</div>
			
			<div class="menu_info_wrap">
				<div class="bottom_one_table">
					<div id="AXSearchTargetUp" style="border-top:1px solid #ccc;"></div><br>
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 260px;"></div>
				</div>
				
				<div class="middle_table">
					<div class="button_check upMove" style="font-size: 1em;" id="btn_insert_reqClsAdd"><img src="/images/contents/top_blue.png" alt="위쪽 화살표" style="margin-right: 5px;">추가</div>
					<div class="button_check downMove" style="font-size: 1em;" id="btn_delete_reqClsDel">삭제<img src="/images/contents/bottom_red.png" alt="아래쪽 화살표" style="margin-left: 5px;"></div>
					<span id="rootBtnNone">루트 분류에는 배정이 불가능합니다.</span>
					<span id="defaultBtn">분류를 선택해주세요.</span>
				</div>
				
				<div class="bottom_two_table">
					<div id="AXSearchTargetDown"></div>
					<div data-ax5grid="second-grid" data-ax5grid-config="{}" style="height: 260px;"></div>
				</div>
			</div>
		</div>
	</div>
</div>
	
<jsp:include page="/WEB-INF/jsp/oslits/bottom/footer.jsp" />