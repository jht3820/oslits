<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<style>
.req4105_option_title {width: 25%;float: left;height: 50px;padding-left: 10px;line-height: 40px;background-color: #f9f9f9;border: 1px solid #ccc;font-weight: bold;border-top: none;}
.req4105_option_all {float: left;height: 50px;line-height: 30px;width: 75%;border-bottom: 1px solid #ccc;padding: 5px;}
.req4105_option_half {float: left;height: 50px;line-height: 30px;width: 25%;border-bottom: 1px solid #ccc;padding:5px;}
.req4105_desc {height: 100px;}
.req4105_file {height: 150px;}
.req4105_clear{clear:both;}
</style>
<script>

//프로세스, 작업흐름 Id
var processId = "${processId}";
var flowId = "${flowId}";


var flwOptGrid;

//공통코드 목록
var commonMstList;
//공통코드 목록
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
	var mstCdStrArr = "FLW00001|FLW00002|FLW00003";
	var strUseYn = 'Y';
	var arrObj = [$("#itemCode"), $("#itemRowNum"), $("#itemType")];
	var arrComboType = ["", "", ""];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	
	//추가항목 목록, 공통코드 목록 세팅
	fnOptInfoListAjax();
	
	//그리드 세팅
	fnPopupFlwAxGrid5View();
	
	//추가 항목 관리 팝업 창 닫기
	$("#btn_close_optClose").click(function(){
		//팝업 창 닫기
		gfnLayerPopupClose();
	});
	
	//추가 항목 조회
	$("#btn_select_optSelect").click(function(){
		fnOptInfoListAjax();
	});
	
	//추가 항목 추가
	$("#btn_insert_optInsert").click(function(){
		//팝업창 오픈
		var data = {processId: processId, flowId: flowId, type: "insert"};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1103View.do", data, '800', '340','scroll');
	});
	
	//추가 항목 수정
	$("#btn_update_optUpdate").click(function(){
		var selIndex = flwOptGrid.selectedDataIndexs[0];
		
		if(gfnIsNull(selIndex)){
			jAlert("수정하려는 추가 항목을 선택해주세요.","알림");
			return false;
		}
		//팝업창 오픈
		var data = {processId: processId, flowId: flowId, type: "update"};
		gfnLayerPopupOpen("/prj/prj1000/prj1100/selectPrj1103View.do", data, '800', '340','scroll');
	});
	
	//추가 항목 삭제
	$("#btn_delete_optDelete").click(function(){
		var item = (!gfnIsNull(Object.keys(flwOptGrid.focusedColumn)))? flwOptGrid.list[flwOptGrid.focusedColumn[Object.keys(flwOptGrid.focusedColumn)].doindex]:null;
		
		if(gfnIsNull(item)){
			jAlert("삭제할 추가 항목을 선택해주세요.","알림");
			return false;
		}
		else{
			var selIndex = flwOptGrid.focusedColumn[Object.keys(flwOptGrid.focusedColumn)].doindex;
			jConfirm(item.itemNm+" 추가 항목을 삭제 하시겠습니까?", "알림", function( result ) {
	 			if( result ){
	 				fnDeleteOptInfoAjax(item,selIndex);
	 			}
			});
			
		}
	});
});
//선택 작업흐름 추가 항목 갯수 갱신
function fnFlowOptCntUpdate(type){
	//작업흐름 데이터에 추가 항목 갯수 세팅
	var selFLowInfo = $flowchart.flowchart("getOperatorData",flowId);
	
	//추가 항목 갯수
	var flowOptCnt = selFLowInfo.properties.flowOptCnt;
	if(gfnIsNull(flowOptCnt) || flowOptCnt < 0){flowOptCnt = 0;}
	
	
	if(type == "insert"){
		flowOptCnt += 1;
	}
	else if(type == "delete"){
		flowOptCnt -= 1;
	}
	
	//추가 항목 갯수
	selFLowInfo.properties.flowOptCnt = flowOptCnt;
	
	//작업흐름 데이터 교체
	$flowchart.flowchart('setOperatorData', flowId, selFLowInfo);
	
	//데이터 갱신
	var flowGetData = $flowchart.flowchart('getData');
	processJsonList[processId] = JSON.stringify(flowGetData);
	
}

//추가 항목 제거
function fnDeleteOptInfoAjax(item,selIndex){
	//선택 작업흐름 추가 항목 갯수 갱신
	fnFlowOptCntUpdate("delete");
	
	//프로세스 json Data
	var flowGetData = $flowchart.flowchart('getData');
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/deletePrj1100OptInfoAjax.do'/>"},
			{processJsonData:JSON.stringify(flowGetData),processId:processId, flowId:flowId, itemId:item.itemId, itemCode:item.itemCode});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		toast.push(data.message);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//선택 항목 제거
			flwOptGrid.deleteRow(selIndex);
			
			//부모창(prj1100) 추가항목 그리드에 정보 갱신
			if(typeof firstGrid != "undefined"){
				fnFlowOptList(flowId);
			}
			
			//추가 항목 정보 리셋
			$("#prj1101PopupFrm")[0].reset();
		}else{
			//에러 발생시 rollback
			fnFlowOptCntUpdate("insert");
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림");
 	});
	//AJAX 전송
	ajaxObj.send();
}

//추가항목 목록, 공통코드 목록 조회
function fnOptInfoListAjax(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/selectPrj1100OptListAjax.do'/>"},
			{processId: processId, flowId: flowId, type:"prj1101"});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		toast.push(data.message);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//공통코드 세팅
			commonMstList = data.commonMstList;
			
			//추가 항목 목록 세팅
			if(!gfnIsNull(data.optList)){
				flwOptGrid.setData(data.optList);
			}
			
			//추가 항목 정보 리셋
			$("#prj1101PopupFrm")[0].reset();
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림");
 	});
	//AJAX 전송
	ajaxObj.send();
}
	
//axisj5 그리드
function fnPopupFlwAxGrid5View(){
		flwOptGrid = new ax5.ui.grid();
 
	flwOptGrid.setConfig({
		target: $('[data-ax5grid="flwOpt-grid"]'),
		sortable:true,
		header: {align:"center"},
		columns: [
			{key: "itemCode", label: "추가 항목 분류", width: 150, align: "center"
				,formatter:function(){
				//02 '공통코드'인경우 해당 공통코드로 공통코드명 가져오기
				var rtnStr = "";
				//공통 코드 인경우
				if(this.item.itemCode == "01"){
					rtnStr = "기타";
				}
				//공통 코드 인경우
				else if(this.item.itemCode == "02"){
					rtnStr = "[공통]"+this.item.itemCommonCodeStr;
				}
				//기타
				else{
					rtnStr = this.item.itemCodeStr;
				}
				return rtnStr;
				}},
			{key: "itemNm", label: "추가 항목 명", width: 290, align: "center"},
			{key: "itemTypeStr", label: "추가 항목 타입", width: 150, align: "center"
				,formatter:function(){
					//02 '공통코드'인경우 해당 공통코드로 공통코드명 가져오기
					var rtnStr = this.item.itemTypeStr;
					
					//공통 코드 인경우
					if(this.item.itemCode == "02"){
						rtnStr = "콤보 박스(select)";
					}
					//타입 없는경우
					else if(gfnIsNull(rtnStr)){
						rtnStr = this.item.itemCodeStr;
					}
					return rtnStr;
				}},
			{key: "itemRowNumStr", label: "열 넓이", width: 80, align: "center"},
			{key: "itemOrd", label: "순서", width: 100, align: "center"},
			{key: "itemEssentialCd", label: "필수", width: 100, align: "center"
				,formatter:function(){return (this.item.itemEssentialCd=="01")?"Y":"N";}},
			{key: "itemModifyCd", label: "수정", width: 100, align: "center"
				,formatter:function(){return (this.item.itemModifyCd=="01")?"Y":"N";}},
			{key: "itemLength", label: "길이 제한", width: 120, align: "center"}
		],
		body: {
			align: "center",
			columnHeight: 30,
			onClick: function () {
				//이전 선택 row 해제
                this.self.select(flwOptGrid.selectedDataIndexs[0]);
              	
              	//현재 선택 row 전체 선택
                this.self.select(this.doindex);
              }
          },
          page:{display:false}
      });
}
</script>

<div class="popup" style="height:100%;">
<form id="prj1101PopupFrm" name="prj1101PopupFrm" method="post">
	<div class="pop_title">추가 항목 관리</div>
	<div class="pop_sub" style="padding:15px;display: inline-block;">
		<div class="flwOptBtnDiv">
			<span class="button_normal2" id="btn_select_optSelect"><i class="fa fa-list"></i>&nbsp;조회</span>
			<c:if test="${not empty processConfirmCd and processConfirmCd == '01' }">
				<span class="button_normal2" id="btn_insert_optInsert"><i class="fa fa-plus"></i>&nbsp;추가</span>
				<span class="button_normal2" id="btn_update_optUpdate"><i class="fa fa-edit"></i>&nbsp;수정</span>
				<span class="button_normal2" id="btn_delete_optDelete"><i class="fa fa-times"></i>&nbsp;삭제</span>
			</c:if>
		</div>
		<div class="flw_box_clear"></div>
		<div class="flwOptGridDiv flw_line_right">
			<div data-ax5grid="flwOpt-grid" data-ax5grid-config="{}" style="height: 420px;"></div>	
		</div>
		<div class="flw_btn_box">
			<div class="button_complete" id="btn_close_optClose"><i class="fa fa-times"></i>&nbsp;닫기</div>
		</div>
	</div>
</div>
</form>
</div>
</html>
