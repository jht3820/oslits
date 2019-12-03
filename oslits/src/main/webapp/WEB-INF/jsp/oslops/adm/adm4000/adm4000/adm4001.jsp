<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<title>OpenSoftLab</title>

<style>
.layer_popup_box .pop_left, .layer_popup_box .pop_right { height: 50px; }
.layer_popup_box .pop_left { padding-left:10px !important;}
	.check_prj{padding:31px 25px !important;}
	.checking{color:#4b73eb;padding:5px 0px; display:inline-block;}
	.textarea_height{height:103px !important;}
	.lp10{padding-left: 10px}
	#ord { width:100%; height: 100%; }
	#useYn{ height: 100%; }
	#mstCdDesc { height: 100%; padding-left: 4px;}
	#useYn_txt {display:none;}
	.stmUseYn_div{display:none;}
	/* 필수 입력값 */
	.required_info{color:red; font-weight: bold; }
</style>
	
<script>

var mstMsg = "공통코드는 반드시 8자의  영문 대문자와 \n숫자의 조합으로 사용 가능합니다.";
var upperMsg = "상위코드는 반드시 8자의 영문 대문자와 \n숫자의 조합으로 사용 가능합니다.";

//유효성 체크
var initBindObjectValid = {
			"mstCd":{"type":"length","msg":"공통코드는 8byte까지 입력이 가능합니다.","max":8}
			,"upperMstCd":{"type":"length","msg":"상위코드는 8byte까지 입력이 가능합니다.","max":8}
			,"mstCdDesc":{"type":"length","msg":"공통코드 설명은 200byte까지 입력이 가능합니다.","max":200}
			,"ord":{"type":"number"}
}

var saveObjectValid = {
		"mstCd":{"type":"regExp","pattern":/^[A-Z|0-9]{8}$/g ,"msg":mstMsg, "required":true}
		,"upperMstCd":{"type":"regExp","pattern":/^[A-Z|0-9]{8}$/g ,"msg":upperMsg,"required":$("#upperMstCd").val()==""?false:true}
}

$(document).ready(function() {

	//화면 기본 정보 세팅
	fnSetFrm();
	
	// 권한그룹 추가버튼 레이어 팝업 - 취소버튼 클릭 시 팝업 창 사라지기
	$('#exitBtnMaster').click(function() {
		gfnLayerPopupClose();
	});
	
	// 입력값 유효성 체크
	gfnInputValChk(initBindObjectValid);
	
	// 권한그룹추가 레이어 팝업 - 저장버튼 클릭 시 롤 저장 처리 
	$('#saveBtnMaster').click(function() {
		
		/* 필수입력값 체크 공통 호출 */
	 	var strFormId = "masterCdFrm";
		var strCheckObjArr = ["mstCd", "mstCdNm","ord"];
		var sCheckObjNmArr = ["마스터코드", "마스터코드명","순서"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		} 
		
		//유효성 체크
		if( validCheck("mstCd", true,mstMsg) ) {
			return;	
		};
		if( validCheck("upperMstCd", $("#upperMstCd").val()==""?false:true,upperMsg) ) {
			return;
		}

		// 정규식 유효성 체크
		if(!gfnInputValChk(saveObjectValid)){
			return;	
		}
		
		if(gfnIsNumeric('ord')){
			
			// 유효성 체크
			if(!gfnSaveInputValChk(initBindObjectValid)){
				return false;	
			}
			
			// 정규식 유효성 체크
			if(!gfnSaveInputValChk(saveObjectValid)){
				return false;	
			}
			
			sendToServer();
		} 
	});
});
	
	var fd;
	function sendToServer(){
		// 저장 전 유효성 체크
			fd = new FormData();
			//FormData에 input값 넣기
			gfnFormDataAutoValue('masterCdFrm',fd);
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm4000/adm4000/saveAdm4000CommonCodeMasterInfoAjax.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false}
					,fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
		    	//등록 실패의 경우 리턴
		    	if(data.saveYN == 'N'){
		    		if(data.duplicateYN =='Y'){
		    			alert("이미 사용하고 있는 공통코드입니다.");
		    			return;
		    		}else{
		    			toast.push(data.message);
			    		return;	
		    		}
		    	}
				//그리드 새로고침
		    	fnAxGrid5View_first(); // Grid 초기화  설정
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
			        	gfnLayerPopupClose();
					}
				});
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				toast.push("ERROR STATUS("+data.status+")<br>"+data.statusText);
		    	gfnLayerPopupClose();
			});
			//AJAX 전송
			ajaxObj.send();
	
	}

	var validCheck = function(tagId, useFlag, msg) {
		if(!useFlag) {
			return;
		}
		var chk1 = ($("#"+tagId).val()).search(/[0-9]/g);
		var chk2 = ($("#"+tagId).val()).search(/[A-Z]/ig);
		if(chk1 < 0 || chk2 < 0 ) {
	        jAlert(msg);
	        return true;
	    }
		return false;
	}

	
	/* json 단건 정보를 form 자식 엘레먼트에 자동 세팅 */
	function fnSetFrm(){
		var infoMap = ${infoMap};
		
		//화면 타이틀 세팅
		$("#comCdTitle").text(infoMap.title);
	
		//수정모드일시 pk칼럼 readonly 처리
		if(infoMap.mode == 'update'){
			$("#mstCd").attr("readonly", true);
			$("#mstCd").addClass("readonly");
			$(".useYn_Div").removeClass("bottom_line");
			// 수정일 경우 시스템 사용여부 보여줌
			$(".stmUseYn_div").show();
		}
		
		gfnSetData2Form(infoMap, "masterCdFrm");
		
		// 코드 사용여부
		var useYn = infoMap.useYn;
		
		// 공통코드 시스템 사용여부
		var stmUseYn = infoMap.stmUseYn;
		// 시스템 사용유무가 사용(Y)일 경우 표시순서만 수정가능하도록 처리
		if(stmUseYn == "Y"){
			// 상위코드 readonly
			$("#upperMstCd").attr("readonly", true);
			$("#upperMstCd").addClass("readonly");
			
			// 코드명 readonly
			$("#mstCdNm").attr("readonly", true);
			$("#mstCdNm").addClass("readonly");
			
			// 코드설명 disabled
			$("#mstCdDesc").attr("disabled", true);

			// 사용유무 select hide처리
			$(".search_select").hide();
			$("#useYn_txt").show();
			
			// 사용유무 값
			var useYnTxt = useYn == 'Y' ? "사용" : "미사용";
			$("#useYn_txt").val(useYnTxt);

		}
		// 시스템 사용유무 값
		var stmUseTxt = stmUseYn == 'Y' ? "사용" : "미사용";
		$("#stmUseYn_txt").val(stmUseTxt);
	}

</script>
	
<div class="popup">
	<form id="masterCdFrm" name="masterCdFrm" method="post">
		<input type="hidden" name="mode" id="mode" value="${param.mode}"/>
		<input type="hidden" name="lvl" id="lvl" value="0"/>
		<input type="hidden" name="mstCdEngNm" id="mstCdEngNm" />
		<input type="hidden" name="stmUseYn" id="stmUseYn" value="N"/>
		
		<div class="pop_title" id="comCdTitle">공통코드 마스터</div>
		<div class="pop_sub">
		
			<div class="pop_left">공통코드<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right">
				<input type="text" title="공통코드" class="input_txt lp10" id="mstCd" name="mstCd" maxlength="8"/>
			</div>
			
			<div class="pop_left">상위코드</div>
			<div class="pop_right">
				<input type="text" title="상위코드" class="input_txt lp10" id="upperMstCd" name="upperMstCd" maxlength="8"/>
			</div>
			
			<div class="pop_left">코드명<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right">
				<input type="text" title="코드명" class="input_txt lp10" id="mstCdNm" name="mstCdNm" maxlength="25"/>
			</div>
			
			<div class="pop_left textarea_height">코드설명</div>
			<div class="pop_right textarea_height">
				<textarea title="공통코드설명" class="input_note lp10" id="mstCdDesc" name="mstCdDesc" maxlength="100"/>
			</div>
			
			<div class="pop_left">표시 순서<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right">
				<input type="number" min="1" title="표시 순서" class="authority_order lp10"   id="ord" name="ord"/>
			</div>
			
			<div class="pop_left bottom_line useYn_Div">사용 유무<span class="required_info">&nbsp;*</span></div>
			<div class="pop_right bottom_line useYn_Div">
				<span class="search_select">
					<select name="useYn" id="useYn">
						<option value="Y" selected>사용</option>
						<option value="N">미사용</option>
					</select>
				</span>
				<!-- 시스템 사용일경우만 readonly로 보여지는 부분 -->
				<input type="text" title="사용유무" class="input_txt lp10 readonly" id="useYn_txt" name="useYn_txt" readonly="readonly"/>
			</div>
			
			<div class="pop_left bottom_line stmUseYn_div">시스템 사용 유무</div>
			<div class="pop_right bottom_line stmUseYn_div">
				<input type="text" title="시스템 사용 유무" class="input_txt lp10 readonly" id="stmUseYn_txt" name="stmUseYn_txt" readonly="readonly" />
			</div>
			
			<div class="btn_div">
				<div class="button_normal save_btn complete_btn" id="saveBtnMaster">저장</div>
				<div class="button_normal exit_btn" id="exitBtnMaster">취소</div>
			</div>
		</div>
	</form>
</div>

</html>