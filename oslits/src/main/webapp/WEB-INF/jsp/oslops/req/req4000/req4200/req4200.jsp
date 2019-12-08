<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>
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
.menu_ctrl_wrap {min-height: 810px !important;}
#selReqClsInfoAssignDiv{min-height: 810px; padding: 20px 20px 15px 36px; }
.sub_title{ font-weight: bold; background: #f9f9f9; border-top: 1px solid #ccc; border-bottom: 1px solid #ccc; text-align: left; padding: 6px 5px; height: 30px; border-radius: 5px 5px 0 0; font-size: 11pt; margin-bottom: 2px; line-height: 20px;}
/* 배정/미배정 요구사항 그리드 검색 영역 */
.req_search_area{padding: 3px 0 7px 0; font-size:12px !important;}
/* 그리드 검색어 입력창 높이 고정 */
.AXInput{height:25px !important;}
</style>

<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<script>
//zTree
var zTree;
var btnAuthInsertYn = '${sessionScope.selBtnAuthInsertYn}';
var btnAuthDeleteYn = '${sessionScope.selBtnAuthDeleteYn}';
//mask
var ax5Mask = new ax5.ui.mask();
// 현재 선택한 분류의 상위 분류명들을 담을 배열
var upperClsNameArr = [];

$(document).ready(function() {
	
	//가이드 상자 호출
	gfnGuideStack("add",fnReq4200GuideShow);
	
	//트리메뉴 도움말 클릭
/* 	$(".menu_tree_help").click(function(){
		if($(".menu_tree_helpBox").hasClass("boxOn")){
			$(".menu_tree_helpBox").hide();
			$(".menu_tree_helpBox").removeClass("boxOn");
		}else{
			$(".menu_tree_helpBox").show();
			$(".menu_tree_helpBox").addClass("boxOn");
		}
	}); */
	
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
		
		// 분류명을 담은 배열 초기화
		upperClsNameArr.length = 0;
		
		// 선택된 트리 노드를 가져온다.
		var selClsNode = zTree.getSelectedNodes()[0];
		
		// 선택한 분류가 없다면 알림
		if(gfnIsNull(selClsNode)){
			toast.push('왼쪽 트리에서 요구사항 분류를 선택해주세요.'); 
			return;
		}
		
		// 선택한 분류 트리노드에서 분류id를 가져온다.
		var selClsId = selClsNode.reqClsId;
		
		// 선택한 분류의 상위 분류명을 모두 배열에 담는다.
		fnGetUpperReqClsName(selClsNode);
		
		// 상위 분류명을 포함하는 분류명을 가져온다.
		var reqClsNmStr = fnGetReqClsName(upperClsNameArr);

		// 미배정 요구사항 그리드에서 체크된 요구사항을 가져온다.
		var chkReqList = secondGrid.getList('selected');

		// 체크된 요구사항이 없으면 알림
		if(gfnIsNull(chkReqList)) {
			toast.push('추가할 요구사항을 체크하세요.'); 
			return;
		}
		
		// 분류에 요구사항을 배정한다.
		fnReqClsAssignAddAndDel(selClsId, reqClsNmStr, chkReqList, "add");

	});
	
	//삭제 버튼 이벤트
	$('#btn_delete_reqClsDel').click(function(){
			
		// 분류명을 담은 배열 초기화
		upperClsNameArr.length = 0;
		
		// 선택된 트리 노드를 가져온다.
		var selClsNode = zTree.getSelectedNodes()[0];
		
		// 선택한 분류가 없다면 알림
		if(gfnIsNull(selClsNode)){
			toast.push('왼쪽 트리에서 요구사항 분류를 선택해주세요.'); 
			return;
		}
		
		// 선택한 분류 트리노드에서 분류id를 가져온다.
		var selClsId = selClsNode.reqClsId;

		// 선택한 분류의 상위 분류명을 모두 배열에 담는다.
		fnGetUpperReqClsName(selClsNode);
		
		// 상위 분류명을 포함하는 분류명을 가져온다.
		var reqClsNmStr = fnGetReqClsName(upperClsNameArr);
		
		// 배정 요구사항 목록 그리드에서 체크된 요구사항을 가져온다.
		var chkReqList = firstGrid.getList('selected');

		// 체크한 요구하상이 없으면 알림
		if(gfnIsNull(chkReqList)) {
			toast.push('삭제할 요구사항을 체크하세요.'); 
			return;
		}
		
		// 분류에서 요구사항을 제외한다.
		fnReqClsAssignAddAndDel(selClsId, reqClsNmStr, chkReqList, "del");
		
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
					/* ROOT 분류에도 요구사항 배정 가능 */
						
					//선택한 요구사항 분류의 배정 및 미배정 정보 조회
					//fnSearchReqClsAddDelListAjax($(this).attr('id'));
					// 그리드 보이기
					$('.bottom_one_table').show();
					$('.bottom_two_table').show();
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
				
					//Mask 제거
					ax5Mask.close();
				}
			},
			view : {
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
			target: $("#selReqClsInfoAssignDiv"),
			content: "요구사항 분류를 선택해주세요."
		});
	  	
	});
	
	//AJAX 전송
	ajaxObj.send();
	
	$('#defaultBtn').show();
	//표시되있던 정보 감추기
	$('.button_check').hide();
	$('.bottom_one_table').hide();
	$('.bottom_two_table').hide();
	$('#AXGridTargetUp').html('');
	$('#AXGridTargetDown').html('');
	
	//루트 클릭
	$('.menu_root_info').click();
}

/*
 * 선택한 분류의 상위 분류명를 모두 가져온다. 
 * @param selClsNode 선택한 분류 트리 노드
 */
function fnGetUpperReqClsName(selClsNode){
	
	// 선택한 분류명을 
	upperClsNameArr.push(selClsNode.reqClsNm);
	
	// 상위 분류ID가 존재할 경우
	if( selClsNode.upperReqClsId != null ){
		// 부모를 가져오고, 재귀호출
		var parentNode = selClsNode.getParentNode();
		fnGetUpperReqClsName(parentNode);
	}
}

/*
 * 상위분류 명을 포함하는 분류명을 가져온다.
 * @param upperClsNameArr 선택한 분류의 상위 분류명을 담은 배열
 */
function fnGetReqClsName(upperClsNameArr){
	
	// 선택된 분류부터 최상위 분류까지 분류명이 담긴 배열의 순서를 뒤집는다.
	// (최상위 > 1번 > 2번, 이렇게 선택한 분류명이 맨 마지막에 오도록)
	var reqClsNmArr = upperClsNameArr.reverse();
	var reqCslNms = "";
	
	// 분류명 배열을 이용하여 상위 분류명을 포함하는 분류명을 만든다.
	for (var i = 0; i < reqClsNmArr.length; i++){
		reqCslNms += reqClsNmArr[i];
		if(i != reqClsNmArr.length-1 ){
			reqCslNms += " > ";
		}
	}

	return reqCslNms;
}


/*
 * 분류에 요구사항을 배정/배정제외 한다
 * @param selClsId 선택한 분류 Id
 * @param selClsNm 선택한 분류 명
 * @param reqList 분류에 배정/배정제외할 요구사항 리스트
 * @param option 배정, 배정제외 구분값 (add:배정, del:배정제외)
 */
function fnReqClsAssignAddAndDel(selClsId, selClsNm, reqList, option){
	
	// 체크된 요구사항 정보를 담을 FormData
	var selReqFd = new FormData();

	// 체크된 요구사항 List
	var sleReqlist = [];
	
	// 체크된 요구사항으로 부터 요구사항 정보를 FormData에 세팅한다.
	$.each(reqList, function(idx, map){ 
		// 요구사항 정보 추가
		sleReqlist.push({"reqClsId": selClsId, "reqClsNm": selClsNm, "reqId" : map.reqId, "type":option});
	});
	
	// formdata에 요구사항 List 세팅
	selReqFd.append("selReqList",JSON.stringify(sleReqlist));
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4200/updateReq4200ReqClsAddDelListAjax.do'/>"
			,"loadingShow":false
			,"contentType":false
			,"processData":false
			,"cache":false}
			,selReqFd);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//로딩바 닫기
		//gfnShowLoadingBar(false);
    	
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
	
}


	/****************** 배정 요구사항, 미배정 요구사항 그리드&검색상자 설정 시작 *****************************/
 

//axisj5 그리드  배정된 요구사항
function fnAxGrid5View_first(reqClsId){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            showRowSelector: true,
            sortable:false,
            header: {align:"center"},
            //frozenColumnIndex: 2,
            columns: [
					{key: "reqOrd", label: "순번", width: '11%', align: "center"}    
  					,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left" }
					,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
					,{key: "reqNewTypeNm", label: "접수유형", width: '10%', align: "center"}
	                ,{key: "reqProTypeNm", label: "처리상태", width: '10%', align: "center"}
	                ,{key: "processNm", label: "프로세스 명", width: '15%', align: "center"}
		            ,{key: "flowNm", label: "작업흐름 명", width: '15%', align: "center"}
					,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
					,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
					,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
					,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"
						// 날짜 포맷 변경
						,formatter:function(){return new Date(this.item.reqDtm).format('yyyy-MM-dd',true)}
					}
					,{key: "reqStDtm", label: "시작 기간", width: '14%', align: "center"}
					,{key: "reqEdDtm", label: "종료 기간", width: '14%', align: "center"}
					,{key: "reqStDuDtm", label: "시작 예정일자", width: '15%', align: "center"}
					,{key: "reqEdDuDtm", label: "종료 예정일자", width: '15%', align: "center"}
		            ,{key: "reqTypeNm", label: "요구사항 유형", width: '15%', align: "center"}
					,{key: "reqCompleteRatio", label: "진척률", width: '15%', align: "center"}
					,{key: "reqExFp", label: "예상 FP", width: '15%', align: "center"}
					,{key: "reqFp", label: "FP", width: '15%', align: "center"}
					,{key: "sclNm", label: "시스템 구분", width: '15%', align: "center"}
					,{key: "piaNm", label: "성능 개선활동 여부", width: '15%', align: "center"}
					,{key: "labInp", label: "투입인력", width: '15%', align: "center"}
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
            },
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                	// 그리드 데이터 조회
                   fnInGridListSet(this.page.selectPage, mySearchUp.getParam(),{grid:firstGrid, data:{ "clsMode":"clsAdd","reqClsId":reqClsId}});
                }
            }
        });
        //그리드 데이터 불러오기
 		fnInGridListSet(0, null,{grid:firstGrid, pageNo:0, data:{ "clsMode":"clsAdd","reqClsId":reqClsId}});

}

//axisj5 그리드  미배정 요구사항
function fnAxGrid5View_second(reqClsId){
	secondGrid = new ax5.ui.grid();
 
        secondGrid.setConfig({
            target: $('[data-ax5grid="second-grid"]'),
            showRowSelector: true,
            sortable:false,
            header: {align:"center"},
            //frozenColumnIndex: 2,
            columns: [
					{key: "reqOrd", label: "순번", width: '11%', align: "center"}    
  					,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left" }
					,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
					,{key: "reqNewTypeNm", label: "접수유형", width: '10%', align: "center"}
	                ,{key: "reqProTypeNm", label: "처리상태", width: '10%', align: "center"}
	                ,{key: "processNm", label: "프로세스 명", width: '15%', align: "center"}
		            ,{key: "flowNm", label: "작업흐름 명", width: '15%', align: "center"}
					,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
					,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
					,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
					,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"
						// 날짜 포맷 변경
						,formatter:function(){return new Date(this.item.reqDtm).format('yyyy-MM-dd',true)}
					}
					,{key: "reqStDtm", label: "시작 기간", width: '14%', align: "center"}
					,{key: "reqEdDtm", label: "종료 기간", width: '14%', align: "center"}
					,{key: "reqStDuDtm", label: "시작 예정일자", width: '15%', align: "center"}
					,{key: "reqEdDuDtm", label: "종료 예정일자", width: '15%', align: "center"}
		            ,{key: "reqTypeNm", label: "요구사항 유형", width: '15%', align: "center"}
					,{key: "reqCompleteRatio", label: "진척률", width: '15%', align: "center"}
					,{key: "reqExFp", label: "예상 FP", width: '15%', align: "center"}
					,{key: "reqFp", label: "FP", width: '15%', align: "center"}
					,{key: "sclNm", label: "시스템 구분", width: '15%', align: "center"}
					,{key: "piaNm", label: "성능 개선활동 여부", width: '15%', align: "center"}
					,{key: "labInp", label: "투입인력", width: '15%', align: "center"}
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
                	var selItem = secondGrid.list[param.doindex];
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
            }, 
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                   fnInGridListSet(this.page.selectPage, mySearchDown.getParam(),{grid:secondGrid, data:{ "clsMode":"clsDel","reqClsId":reqClsId}});
                }
            }
        });
        //그리드 데이터 불러오기
 		fnInGridListSet(0, null,{grid:secondGrid, data:{ "clsMode":"clsDel","reqClsId":reqClsId}});

}

//그리드 데이터 넣는 함수
function fnInGridListSet(_pageNo, ajaxParam, gridTarget){
     	/* 그리드 데이터 가져오기 */
     	
     	//파라미터 세팅
     	if(gfnIsNull(ajaxParam)){
   			ajaxParam = $('form#searchFrm').serialize();
   		}
     	
     	//페이지 세팅
     	if(!gfnIsNull(_pageNo)){
     		ajaxParam += "&pageNo="+_pageNo;
     	}else if(typeof gridTarget.grid.page.currentPage != "undefined"){
     		ajaxParam += "&pageNo="+gridTarget.grid.page.currentPage;
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
			var page = data.page;

			// 그리드 데이터, 페이지 정보 세팅
		   	gridTarget.grid.setData({
					list:list,
					page: {
	                  		currentPage: _pageNo || 0,
							pageSize: page.pageSize,
	                  		totalElements: page.totalElements,
	                  		totalPages: page.totalPages
	              	}
             });
		   	
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

// 배정된 요구사항 검색 상자
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
					{display:true, addClass:"", style:"", list:[
						// 검색 조건 콤보박스 세팅
						{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
							options:[
		                                {optionValue:"0", 				optionText:"전체 보기",	optionAll:true},
			                            {optionValue:'reqOrd', 			optionText:'요구사항 순번'},
			                            {optionValue:'reqNm', 			optionText:'요구사항 명'},
			                            {optionValue:'reqDesc', 		optionText:'요구사항 설명'},
			                            {optionValue:'reqUsrNm', 		optionText:'요청자'},
			                            {optionValue:'reqChargerNm', 	optionText:'담당자'},
			                            {optionValue:'reqNo', 			optionText:'공문번호'},
			                            {optionValue:'processNm', 		optionText:'프로세스 명'},
			                            {optionValue:'flowNm', 			optionText:'작업흐름 명'},
			                            {optionValue:"reqProType", 		optionText:"처리상태", 	optionCommonCode:"REQ00008"},
			                            {optionValue:"reqNewType", 		optionText:"접수유형", 	optionCommonCode:"REQ00009"},
			                            {optionValue:"reqTypeCd",	 	optionText:"요구사항 유형", 	optionCommonCode:"REQ00012"}
			                                
                            ],onChange: function(selectedObject, value){
                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
									axdom("#" + mySearchUp.getItemId("searchTxt")).attr("readonly", "readonly");	
									axdom("#" + mySearchUp.getItemId("searchTxt")).val('');	
								}else{
									axdom("#" + mySearchUp.getItemId("searchTxt")).removeAttr("readonly");
								}
								
								//공통코드 처리 후 select box 세팅이 필요한 경우 사용
								if(!gfnIsNull(selectedObject.optionCommonCode)){
									gfnCommonSetting(mySearchUp,selectedObject.optionCommonCode,"searchCd","searchTxt");
								
								}else{
									//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
									axdom("#" + mySearchUp.getItemId("searchTxt")).show();
									axdom("#" + mySearchUp.getItemId("searchCd")).hide();
								}
    						},
						},
						// 검색어 입력 input 세팅
						{label:"", labelWidth:"", type:"inputText", width:"120", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
							onkeyup:function(e){
								if(e.keyCode == '13' ){
									axdom("#" + mySearchUp.getItemId("btn_search_clsAssignReq")).click();
								}
							} 
						},
						// 코드 검색 selectbox 세팅
						{label:"", labelWidth:"", type:"selectBox", width:"100%", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
							options:[]
						},
						{label:"<i class='fas fa-list-ol'></i>&nbsp;목록 수&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"pageSize", addClass:"", valueBoxStyle:"", value:"30",
							options:[
							         	{optionValue:15, optionText:"15"},
		                                {optionValue:30, optionText:"30"},
		                                {optionValue:50, optionText:"50"},
		                                {optionValue:100, optionText:"100"},
		                                {optionValue:300, optionText:"300"}
		                                
		                            ],onChange: function(selectedObject, value){

		                            	// 트리에서 선택된 요구사항 분류
										var selZtree = zTree.getSelectedNodes()[0];

										// 선택한 분류가 없을경우
										if(gfnIsNull(selZtree)){
											jAlert("요구사항 분류를 선택해주세요.", "알림"); 
											return false;
										}
										
										// 검색조건 세팅
			 							var pars = mySearchUp.getParam();

								 		//미배정 요구사항 목록 조회
										fnInGridListSet(0, pars,{grid:firstGrid, data:{ "clsMode":"clsAdd","reqClsId":selZtree.reqClsId}});
		    						}
						},
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_excel_clsAssignReq",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
							onclick:function(){
								
								// 트리에서 선택된 요구사항 분류
								var selZtree = zTree.getSelectedNodes()[0];

								// 선택한 분류가 없을경우
								if(gfnIsNull(selZtree)){
									jAlert("요구사항 분류를 선택해주세요.", "알림"); 
									return false;
								}
								
								firstGrid.exportExcel("요구사항 분류 배정 요구사항.xls");
						}},
           				{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_search_clsAssignReq",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								
								// 트리에서 선택된 요구사항 분류
								var selZtree = zTree.getSelectedNodes()[0];

								// 선택한 분류가 없을경우
								if(gfnIsNull(selZtree)){
									jAlert("요구사항 분류를 선택해주세요.", "알림"); 
									return false;
								}
								
								// 검색조건 세팅
	 							var pars = mySearchUp.getParam();

					            //폼 데이터 변경
								$('#searchSelect').val(axdom("#" + mySearchUp.getItemId("searchSelect")).val());
								$('#searchCd').val(axdom("#" + mySearchUp.getItemId("searchCd")).val());
								$('#searchTxt').val(axdom("#" + mySearchUp.getItemId("searchTxt")).val());
								
						 		//미배정 요구사항 목록 조회
								fnInGridListSet(0, pars,{grid:firstGrid, data:{ "clsMode":"clsAdd","reqClsId":selZtree.reqClsId}});
						}}
					]}
				]
			});
		}	
	};
	
	jQuery(document.body).ready(function(){

		fnObjSearch.pageStart();
		//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
		axdom("#" + mySearchUp.getItemId("searchTxt")).attr("readonly", "readonly");
		// 검색 searchCd hide 처리
		axdom("#" + mySearchUp.getItemId("searchCd")).hide();
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


	// 분류 미배정된 요구사항 목록 검색상자
	function fnSearchDownBoxControl(){
		var pageID = "AXSearch";
		mySearchDown = new AXSearch();
	
		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				mySearchDown.setConfig({
					targetID:"AXSearchTargetDown",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
							// 검색 조건 콤보박스 세팅
							{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
								options:[
			                            {optionValue:"0", 				optionText:"전체 보기",	optionAll:true},
			                            {optionValue:'reqOrd', 			optionText:'요구사항 순번'},
			                            {optionValue:'reqNm', 			optionText:'요구사항 명'},
			                            {optionValue:'reqDesc', 		optionText:'요구사항 설명'},
			                            {optionValue:'reqUsrNm', 		optionText:'요청자'},
			                            {optionValue:'reqChargerNm', 	optionText:'담당자'},
			                            {optionValue:'reqNo', 			optionText:'공문번호'},
			                            {optionValue:'processNm', 		optionText:'프로세스 명'},
			                            {optionValue:'flowNm', 			optionText:'작업흐름 명'},
			                            {optionValue:"reqProType", 		optionText:"처리상태", 	optionCommonCode:"REQ00008"},
			                            {optionValue:"reqNewType", 		optionText:"접수유형", 	optionCommonCode:"REQ00009"},
			                            {optionValue:"reqTypeCd",	 	optionText:"요구사항 유형", 	optionCommonCode:"REQ00012"}
			                                
	                            ],onChange: function(selectedObject, value){
	                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
	    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
										axdom("#" + mySearchDown.getItemId("searchTxt")).attr("readonly", "readonly");	
										axdom("#" + mySearchDown.getItemId("searchTxt")).val('');	
									}else{
										axdom("#" + mySearchDown.getItemId("searchTxt")).removeAttr("readonly");
									}
									
									//공통코드 처리 후 select box 세팅이 필요한 경우 사용
									if(!gfnIsNull(selectedObject.optionCommonCode)){
										gfnCommonSetting(mySearchDown,selectedObject.optionCommonCode,"searchCd","searchTxt");
									
									}else{
										//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
										axdom("#" + mySearchDown.getItemId("searchTxt")).show();
										axdom("#" + mySearchDown.getItemId("searchCd")).hide();
									}
	    						},
	
							},
							// 검색어 입력 input 세팅
							{label:"", labelWidth:"", type:"inputText", width:"120", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
								onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + mySearchDown.getItemId("btn_search_clsNotAssignReq")).click();
									}
								} 
							},
							// 코드 검색 selectbox 세팅
							{label:"", labelWidth:"", type:"selectBox", width:"100%", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
								options:[]
							},
							{label:"<i class='fas fa-list-ol'></i>&nbsp;목록 수&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"pageSize", addClass:"", valueBoxStyle:"", value:"30",
								options:[
								         	{optionValue:15, optionText:"15"},
			                                {optionValue:30, optionText:"30"},
			                                {optionValue:50, optionText:"50"},
			                                {optionValue:100, optionText:"100"},
			                                {optionValue:300, optionText:"300"}
			                                
			                            ],onChange: function(selectedObject, value){

			                            	// 트리에서 선택된 요구사항 분류
											var selZtree = zTree.getSelectedNodes()[0];

											// 선택한 분류가 없을경우
											if(gfnIsNull(selZtree)){
												jAlert("요구사항 분류를 선택해주세요.", "알림"); 
												return false;
											}
											
											// 검색조건 세팅
				 							var pars = mySearchDown.getParam();

									 		//미배정 요구사항 목록 조회
											fnInGridListSet(0, pars,{grid:secondGrid, data:{ "clsMode":"clsDel","reqClsId":selZtree.reqClsId}});
			    						}
							},
							{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_excel_clsNotAssignReq",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
								onclick:function(){
									
									// 트리에서 선택된 요구사항 분류
									var selZtree = zTree.getSelectedNodes()[0];

									// 선택한 분류가 없을경우
									if(gfnIsNull(selZtree)){
										jAlert("요구사항 분류를 선택해주세요.", "알림"); 
										return false;
									}
									
									secondGrid.exportExcel("요구사항 분류 미배정 요구사항.xls");
							}},
							{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_search_clsNotAssignReq",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
								onclick:function(){
									
									// 트리에서 선택된 요구사항 분류
									var selZtree = zTree.getSelectedNodes()[0];

									// 선택한 분류가 없을경우
									if(gfnIsNull(selZtree)){
										jAlert("요구사항 분류를 선택해주세요.", "알림"); 
										return false;
									}
									
									// 검색조건 세팅
		 							var pars = mySearchDown.getParam();

						            //폼 데이터 변경
									$('#searchSelect').val(axdom("#" + mySearchDown.getItemId("searchSelect")).val());
									$('#searchCd').val(axdom("#" + mySearchDown.getItemId("searchCd")).val());
									$('#searchTxt').val(axdom("#" + mySearchDown.getItemId("searchTxt")).val());
									
							 		//미배정 요구사항 목록 조회
									fnInGridListSet(0, pars,{grid:secondGrid, data:{ "clsMode":"clsDel","reqClsId":selZtree.reqClsId}});
							}}
						]}
					]
				});
			}	
		};
		
		jQuery(document.body).ready(function() {
			fnObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + mySearchDown.getItemId("searchTxt")).attr("readonly", "readonly");
			// 검색 searchCd hide 처리
			axdom("#" + mySearchDown.getItemId("searchCd")).hide();
			//버튼 권한 확인
			fnBtnAuthCheck(mySearchDown);
		});
	}
 
	/****************** 배정 요구사항, 미배정 요구사항 그리드&검색상자 설정 종료 *****************************/	
	

//가이드 상자
function fnReq4200GuideShow(){
	var mainObj = $(".main_contents");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["req4200"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}

</script>

<div class="main_contents">
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
	<!-- 
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
		 -->
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
			
			<div class="menu_info_wrap" id="selReqClsInfoAssignDiv">
				<form:form commandName="req4200VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false">
				</form:form>
				<div class="bottom_one_table" guide="assignReqList">
					<div class="sub_title">배정 요구사항 목록</div>
					<div class="req_search_area" id="AXSearchTargetUp"></div>
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 260px;"></div>
				</div>
				
				<div class="middle_table">
					<div class="button_check upMove" style="font-size: 1em;" id="btn_insert_reqClsAdd"><img src="/images/contents/top_blue.png" alt="위쪽 화살표" style="margin-right: 5px;">추가</div>
					<div class="button_check downMove" style="font-size: 1em;" id="btn_delete_reqClsDel">삭제<img src="/images/contents/bottom_red.png" alt="아래쪽 화살표" style="margin-left: 5px;"></div>
					<!-- <span id="rootBtnNone">루트 분류에는 배정이 불가능합니다.</span> -->
				</div>
				
				<div class="bottom_two_table" guide="notAssignReqList">
					<div class="sub_title">미배정 요구사항 목록</div>
					<div class="req_search_area" id="AXSearchTargetDown"></div>
					<div data-ax5grid="second-grid" data-ax5grid-config="{}" style="height: 260px;"></div>
				</div>
			</div>
		</div>
	</div>
</div>
	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />