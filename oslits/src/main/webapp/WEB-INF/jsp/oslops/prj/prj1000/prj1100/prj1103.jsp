<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<title>OpenSoftLab</title>
<script>

//프로세스, 작업흐름 Id
var processId = "${processId}";
var flowId = "${flowId}";
var type = "${type}";

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
	
	//공통코드 세팅
	var $itemCommonCode = $("#itemCommonCode");
	var itemCommonCodeStr = '';
	
	//공통코드 콤보박스 세팅
	$.each(commonMstList,function(idx,map){
		itemCommonCodeStr += "<option value='"+map.mstCd+"'>"+map.mstCdNm+"</option>";
	});
	$itemCommonCode.html(itemCommonCodeStr);
	
	
	//유효성 체크
	var arrChkObjPrj1103 = {"itemNm":{"type":"length","msg":"추가 항목 명은 100byte까지 입력이 가능합니다.",max:100}
					,"itemOrd":{"type":"number"}
					,"itemLength":{"type":"length","msg":"추가 항목 길이 값은 4000까지 입력이 가능합니다."}
					,"itemLength":{"type":"number","msg":"추가 항목 길이 값은 1~4000까지 입력이 가능합니다.",min:1,max:4000}};
	gfnInputValChk(arrChkObjPrj1103);

	
	//수정인경우 처리
	if(type == "update"){
		$(".flowOpt_action_type").html("수정");
		
		var selIndex = flwOptGrid.selectedDataIndexs[0];
		var item = flwOptGrid.list[selIndex];
		
		gfnSetData2Form(item,"prj1103PopupFrm");
           	   
		//항목 분류 세팅
		var itemCode = {value:item.itemCode};
		fnItemCodeChg(itemCode);
		
		//항목 타입 세팅
		var itemType = {value:item.itemType};
		fnItemTypeChg(itemType);
		
		//필수 체크 박스
		if(item.itemEssentialCd == "01"){	//필수
			$("#itemEssential")[0].checked = true;
		}else{
			$("#itemEssential")[0].checked = false;
		}
		
		//수정 체크 박스
		if(item.itemModifyCd == "01"){	//수정
			$("#itemModify")[0].checked = true;
		}else{
			$("#itemModify")[0].checked = false;
		}
	}else{
		$(".flowOpt_action_type").html("추가");
		
		//순서 값 자동 입력
		var nextOrd = flwOptGrid.list.length+1;
		$("#itemOrd").val(nextOrd);
	}
	
	//작업흐름 항목 추가&수정
	$("#btn_flowOpt_save").click(function(){
		var checkObjArr = ["itemNm","itemOrd"];
		var checkObjNmArr = ["추가 항목 명","추가 항목 순서"];
		
		//유효성 체크
		var chkRtn = gfnRequireCheck("prj1103PopupFrm", checkObjArr, checkObjNmArr);
		
		//유효성 오류있는 경우
		if(chkRtn){
			return false;
		}

		
		//error있는경우 오류
		if($(".inputError").length > 0){
			jAlert("유효하지 않은 값이 입력된 항목이 존재합니다.<br>항목을 확인해주세요.","알림");
			$(".inputError")[0].focus()
			return false;
		}

		// 저장전 유효성 체크
		if(!gfnSaveInputValChk(arrChkObjPrj1103)){
			return false;
		}
		
		//작업흐름 추가항목 추가인경우
		if(type == "insert"){
			//추가 항목 추가
			fnInsertOptInfo();
		}
		//작업흐름 추가항목 수정
		else if(type == "update"){
			var selIndex = flwOptGrid.selectedDataIndexs[0];
			var item = flwOptGrid.list[selIndex];
			fnUpdateOptInfo(item);
		}else{
			jAlert("잘못된 접근입니다.","경고");
		}
     	
     	
	});
	
	//팝업 창 닫기
	$("#btn_close_flowClose").click(function(){
		//팝업 창 닫기
		gfnLayerPopupClose();
	});
	
	//유효성 체크
	var arrChkObj = {"flowNm":{"type":"length","msg":"작업흐름 명은 100byte까지 입력이 가능합니다.",max:100}};
	gfnInputValChk(arrChkObj);
	
	
	//칼라값 입력
	$("#flowTitleBgColor").change(function(){fnColorFixSet("title",{"background-color":$(this).val()});});
	$("#flowTitleColor").change(function(){fnColorFixSet("title",{"color":$(this).val()});});
	$("#flowContentBgColor").change(function(){fnColorFixSet("content",{"background-color":$(this).val()});});
	$("#flowContentColor").change(function(){fnColorFixSet("content",{"color":$(this).val()});});
	
	//체크박스 변경되는경우 미리보기 값 변경
	$(".optPreviewCdChg").click(function(){
		var opt = $(this).attr("opt");
		var chkVal = this.checked;
		
		//아이콘 onOff
		fnOptPreviewOnOff(opt,chkVal);
	});
});

//추가 항목 추가
function fnInsertOptInfo(){
	//입력 항목 값
	var formArray = $("#prj1103PopupFrm").serializeArray();
	
	var itemEssentialCd = "02";
	//필수 값 체크
	if($("#itemEssential")[0].checked){
		itemEssentialCd = "01";
	}
	
	//수정 유무
	var itemModifyCd = "02"
	
	//수정 유무 값 체크
	if($("#itemModify")[0].checked){
		itemModifyCd = "01";
	}
	
	//프로세스, 작업흐름 Id, 필수 추가
	formArray.push({name:"processId",value: processId});
	formArray.push({name:"flowId",value: flowId});
	formArray.push({name:"itemEssentialCd",value: itemEssentialCd});
	formArray.push({name:"itemModifyCd",value: itemModifyCd});
	
	//선택 작업흐름 추가 항목 갯수 갱신
	fnFlowOptCntUpdate("insert");
	
	//프로세스 json Data
	var flowGetData = $flowchart.flowchart('getData');
	formArray.push({name:"processJsonData",value: JSON.stringify(flowGetData)});
	
	var paramInfo = {};
	$.each(formArray,function(idx, map){
		paramInfo[map.name] = map.value;
	});
			
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/insertPrj1100OptInfoAjax.do'/>"},
			paramInfo);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		toast.push(data.message);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//추가 항목 목록 불러오기
			fnOptInfoListAjax();
			
			//부모창(prj1100) 추가항목 그리드에 정보 삽입
			if(typeof firstGrid != "undefined"){
				//입력 폼값
				var flowInfoArray = $("#prj1103PopupFrm").serializeArray();
				
				//jsonArray to json Map
				var flowInfo = {};
				$.each(flowInfoArray,function(idx, map){
					flowInfo[map.name] = map.value;
				});
				
				//길이제한 null인경우 255
				var itemLength = flowInfo.itemLength;
				if(gfnIsNull(flowInfo.itemLength)){
					itemLength = 255;
				}
				firstGrid.addRow({itemNm: flowInfo.itemNm,itemEssentialCd: itemEssentialCd
					, itemNm: flowInfo.itemNm,itemEssentialCd: itemEssentialCd
					, itemTypeStr:$("#itemType > option[value="+flowInfo.itemType+"]").text()
					, itemRowNumStr:$("#itemRowNum > option[value="+flowInfo.itemRowNum+"]").text()
					, itemLength: itemLength});
			}
			
			gfnLayerPopupClose();
			
		}else{
			//에러 발생시 rollback
			fnFlowOptCntUpdate("delete");
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


//추가 항목 수정
function fnUpdateOptInfo(item){

	//입력 항목 값
	var queryString = $("#prj1103PopupFrm").serialize();
	
	var itemEssentialCd = "02";
	var itemModifyCd = "02";
	//필수 값 체크
	if($("#itemEssential")[0].checked){
		itemEssentialCd = "01";
	}
	//수정유무 값 체크
	if($("#itemModify")[0].checked){
		itemModifyCd = "01";
	}
	
	//프로세스, 작업흐름 Id, 필수 추가
	queryString += "&processId="+processId+"&flowId="+flowId+"&itemId="+item.itemId+"&itemEssentialCd="+itemEssentialCd+"&itemModifyCd="+itemModifyCd;
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj1000/prj1100/updatePrj1100OptInfoAjax.do'/>"},
			queryString);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		toast.push(data.message);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//추가 항목 목록 불러오기
			fnOptInfoListAjax();
			
			//부모창(prj1100) 추가항목 그리드에 정보 갱신
			if(typeof firstGrid != "undefined"){
				fnFlowOptList(flowId);
			}
			
			gfnLayerPopupClose();
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

//항목 분류 변경
function fnItemCodeChg(thisObj){
	//기타
	if(thisObj.value == "01"){
		//공통코드 값 닫기
		$("#itemCommonCode").attr("disabled","disabled");
		$("#itemLength").removeAttr("disabled");
		$("#itemType").removeAttr("disabled");
	}
	//공통코드
	else if(thisObj.value == "02"){
		//공통코드 항목 타입은 select box 고정
		$("#itemType").attr("disabled","disabled");
		$("#itemLength").attr("disabled","disabled");
		
		//공통코드 값 열기
		$("#itemCommonCode").removeAttr("disabled");
	}
	//첨부파일
	else if(thisObj.value == "03"){
		//열 넓이 전체 고정
		$("#itemRowNum").val("02");
		
		//첨부파일 항목 타입은 타입, 길이 선택 불가
		$("#itemType").attr("disabled","disabled");
		$("#itemLength").attr("disabled","disabled");
		$("#itemCommonCode").attr("disabled","disabled");
	}
	//담당자, 분류
	else if(thisObj.value == "04" || thisObj.value == "05" || thisObj.value == "06"){
		//타입, 길이 선택 불가
		$("#itemType").attr("disabled","disabled");
		$("#itemLength").attr("disabled","disabled");
		$("#itemCommonCode").attr("disabled","disabled");
	}
}

//항목 타입 변경
function fnItemTypeChg(thisObj){
	//text, textarea아닌 경우 길이제한 disabled
	if(thisObj.value != "01" && thisObj.value != "02"){
		//체크박스인경우 필수 체크 disabled
		if(thisObj.value == "03"){
			//체크 풀기
			$("#itemEssential")[0].checked = false;
			$("#itemEssential").attr("disabled","disabled");
		}else{
			//disblaed 제거
			$("#itemEssential").removeAttr("disabled");
		}
		$("#itemLength").attr("disabled","disabled");
		$("#itemLength").val('');
	}else{
		$("#itemLength").removeAttr("disabled");
		//text box = 255
		if(thisObj.value == "01"){
			$("#itemLength").val(255);
		}
		//textarea = 4000
		else if(thisObj.value == "02"){
			$("#itemLength").val(4000);
		}
	}
}

//열 넓이 변경
function fnItemRowChg(thisObj){
	//첨부파일인경우
	if($("#itemCode").val() == "03"){
		//부분 인경우 toast 출력
		if(thisObj.value == "01"){
			toast.push("첨부파일은 부분으로 설정 할 수 없습니다.");
		}
		//전체 고정
		$("#itemRowNum").val("02");
	}
}

</script>

<div class="popup" style="height:100%;">
<form id="prj1103PopupFrm" name="prj1103PopupFrm" method="post">
	<div class="pop_title">작업흐름 항목 <span class="flowOpt_action_type">추가</span></div>
	<div class="pop_sub" style="padding:15px;display: inline-block;">
		<div class="flw_optPop_box">
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right flw_line_left">
				추가 항목 분류
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<select name="itemCode" id="itemCode" onchange="fnItemCodeChg(this)">
				</select>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				공통코드
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<select name="itemCommonCode" id="itemCommonCode" disabled="disabled">
				</select>
			</div>
			<div class="flw_box_clear"></div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right flw_line_left">
				추가 항목 명 (*)
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<input type="text" name="itemNm" id="itemNm" placeholder="항목명을 입력해주세요."/>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				열 넓이
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<select name="itemRowNum" id="itemRowNum" onchange="fnItemRowChg(this)">
				</select>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right flw_line_left">
				추가 항목 타입
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<select name="itemType" id="itemType" onchange="fnItemTypeChg(this)">
				</select>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				필수
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<div class="flw_chk"> 
					<input type="checkbox" title="체크박스" name="itemEssential" id="itemEssential"/><label></label>
				</div>
			</div>
			<div class="flw_box_clear"></div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right flw_line_left">
				순서 (*)
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<input type="number" name="itemOrd" id="itemOrd" placeholder="순서를 입력해주세요." title="순서" min="0" onkeydown="javascript: return event.keyCode == 69 ? false : true"/>
			</div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right">
				수정
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<div class="flw_chk"> 
					<input type="checkbox" title="체크박스" name="itemModify" id="itemModify"/><label></label>
				</div>
			</div>
			<div class="flw_box_clear"></div>
			<div class="flw_sub_box flw_sub_title flw_sub1 flw_line_right flw_line_left">
				길이 제한(byte)
			</div>
			<div class="flw_sub_box flw_sub1 flw_line_right">
				<input type="number" name="itemLength" id="itemLength" placeholder="길이 값을 입력해주세요." title="추가 항목 길이 제한" min="1" max="4000" value="255"/>
			</div>
		</div>
		<div class="flw_btn_box">
			<div class="button_complete" id="btn_flowOpt_save"><i class="fa fa-save"></i>&nbsp;<span class="flowOpt_action_type">추가</span></div>
			<div class="button_complete" id="btn_close_flowClose"><i class="fa fa-times"></i>&nbsp;닫기</div>
		</div>
	</div>
</div>
</form>
</div>
</html>
