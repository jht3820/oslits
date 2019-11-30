<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ko">
<title>OpenSoftLab</title>

<style>
	.check_prj{padding:31px 25px !important;}
	.checking{color:#4b73eb;padding:5px 0px; display:inline-block;}
	.textarea_height{height:103px !important;}
	.lp10{padding-left: 10px}
	
	/* 익스플로러 대응 !important; 추가 */ 
	/* 팝업에 따라 pop_menu_col1, pop_menu_col2 높이 변경 */
	.pop_menu_row .pop_menu_col1 {height: 45px !important;}
	.pop_menu_row .pop_menu_col2 {height: 45px !important;}
	.pop_menu_row .pop_oneRow_col1{height:130px !important;}
	.pop_menu_row .pop_oneRow_col2{height:130px !important;}
	
	#sub_useYn_txt {display:none;}
	#subCdDesc { height:100%; padding-left: 4px; }
	
	/* 필수 입력값 */
	.required_info{color:red; font-weight: bold; }
</style>
	
<script>

// 공통코드 디테일  유효성
var initBindObjectValid = {
			"subCd":{"type":"number"}
			,"ord":{"type":"number"}
			,"subCdDesc":{"type":"length","msg":"보조필드 설명은 200byte까지 입력가능합니다.", "max":200}
}

	$(document).ready(function() {
		//화면 기본 정보 세팅
		fnSetFrm();
		
		//유효성 체크
		var initBindObjectValid = {
					"subCd":{"type":"number"}
					,"ord":{"type":"number"}
					,"subCdDesc":{"type":"length","msg":"보조필드 설명은 200byte까지 입력가능합니다.", "max":200}
		}
		gfnInputValChk(initBindObjectValid);
		// 권한그룹 추가버튼 레이어 팝업 - 취소버튼 클릭 시 팝업 창 사라지기
		$('#exitBtnDetail').click(function() {
			gfnLayerPopupClose();
		});
		// 권한그룹추가 레이어 팝업 - 저장버튼 클릭 시 롤 저장 처리 
		$('#saveBtnDetail').click(function() {
			/* 필수입력값 체크 공통 호출 */
			var strFormId = "subCdFrm";
			var strCheckObjArr = ["subCd", "subCdNm", "ord"];
			var sCheckObjNmArr = ["서브코드", "서브코드명", "표시순서"];
			if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
				return;
			}
			
			if(gfnIsNumeric('ord')){
				
				// 저장전 유효성 검사
				if(!gfnSaveInputValChk(initBindObjectValid)){
					return false;	
				}
				
				sendToServer();
			}
		});
	});

	
	var fd = new FormData();
	function sendToServer(){

			//FormData에 input값 넣기
			gfnFormDataAutoValue('subCdFrm',fd);
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm4000/adm4000/saveAdm4000CommonCodeDetailInfoAjax.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false}
					,fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
		    	//등록 실패의 경우 리턴
		    	if(data.saveYN == 'N'){
		    		toast.push(data.message);
		    		return;
		    	}
				//그리드 새로고침
				var item = firstGrid.getList('selected')[0];
				fnInGridListSet_bottom(null,{grid:secondGrid,data:{mstCd:item.mstCd} });
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
	
	/* json 단건 정보를 form 자식 엘레먼트에 자동 세팅 */
	function fnSetFrm(){
		
		var infoMap = ${infoMap};
		//화면 타이틀 세팅
		$("#comCdTitle").text(infoMap.title);
		$("#mstCd").attr("readonly", true);
		$("#mstCd").addClass("readonly");
		
		//수정모드일시 pk칼럼 readonly 처리
		if(infoMap.mode == 'update') {	
			$("#subCd").attr("readonly", true);
			$("#subCd").addClass("readonly");
			// 공통코드 디테일 등록/수정시 사용할 구분값
			$("#pageType").val("update");
		}
		else{
			// 등록일 경우 구분값을 insert로 준다
			$("#pageType").val("insert");
		}
		
		gfnSetData2Form(infoMap, "subCdFrm");
	
		// 코드 사용여부
		var useYn = infoMap.useYn;
		
		// 공통코드 시스템 사용여부
		var stmUseYn = infoMap.stmUseYn;
		// 시스템 사용유무가 사용(Y)일 경우 표시순서만 수정가능하도록 처리
		if(stmUseYn == "Y"){
			
			// 사용유무 select hide처리
			$(".search_select").hide();
			$("#sub_useYn_txt").show();
			
			// 서브코드명 readonly
			$("#subCdNm").attr("readonly", true);
			$("#subCdNm").addClass("readonly");
			
			// 보조필드명 readonly
			$("#subCdRef1").attr("readonly", true);
			$("#subCdRef1").addClass("readonly");
			$("#subCdRef2").attr("readonly", true);
			$("#subCdRef2").addClass("readonly");
			$("#subCdRef3").attr("readonly", true);
			$("#subCdRef3").addClass("readonly");
			$("#subCdRef4").attr("readonly", true);
			$("#subCdRef4").addClass("readonly");
			$("#subCdRef5").attr("readonly", true);
			$("#subCdRef5").addClass("readonly");
			
			// 보조필드 설명 disabled
			$("#subCdDesc").attr("disabled", true);

			// 사용유무 값
			var useYnTxt = useYn == 'Y' ? "사용" : "미사용";
			$("#sub_useYn_txt").val(useYnTxt);
		}

	}
	
</script>
	
<div class="popup">
	<form id="subCdFrm" name="subCdFrm" method="post">
		<input type="hidden" name="stmUseYn" id="stmUseYn" value="N"/>
		<!-- 공통코드 디테일 등록/수정시 사용할 구분값 -->
		<input type="hidden" name="pageType" id="pageType"/>
	
		<div class="pop_title" id="comCodeDtl">${param.title}</div>
		<div class="pop_sub">
		
		<div class="pop_menu_row first_menu_row">
			<div class="pop_menu_col1"><label for="mstCd">공통코드</label></div>
			<div class="pop_menu_col2"><input type="text" title="공통코드" class="input_txt lp10" id="mstCd" name="mstCd" maxlength="8"/></div>
		</div>
		<div class="pop_menu_row first_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right">
				<label for="reqNo">사용유무<span class="required_info">&nbsp;*</span></label>
			</div>
			<div class="pop_menu_col2 sub_useYn_Div">
				<span class="search_select sub_useYn_Div">
					<select name="useYn" id="useYn" style="height: 100%;">
						<option value="Y" selected>사용</option>
						<option value="N">미사용</option>
					</select>
				</span>
				<!-- 시스템 사용일경우만 readonly로 보여지는 부분 -->
				<input type="text" title="사용유무" class="input_txt lp10 readonly" id="sub_useYn_txt" name="sub_useYn_txt" readonly="readonly"/>
			</div>
		</div>

		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="subCd">서브코드</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2"><input type="text" title="서브코드" class="input_txt lp10" id="subCd" name="subCd" maxlength="2"/></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="subCdNm">서브코드명</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2"><input type="text" title="서브코드명" class="input_txt  lp10" id="subCdNm" name="subCdNm" maxlength="25"/></div>
		</div>
		
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="ord">표시 순서</label><span class="required_info">&nbsp;*</span></div>
			<div class="pop_menu_col2"><input type="number" min="1" title="표시 순서" class="ord lp10"  id="ord" name="ord" maxlength="20"/></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="subCdRef1">보조필드1</label></div>
			<div class="pop_menu_col2"><input type="text" title="보조필드2" class="input_txt  lp10" id="subCdRef1" name="subCdRef1" maxlength="25"/></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="subCdRef2">보조필드2</label></div>
			<div class="pop_menu_col2"><input type="text" title="보조필드2" class="input_txt lp10" id="subCdRef2" name="subCdRef2" maxlength="25" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="subCdRef3">보조필드3</label></div>
			<div class="pop_menu_col2"><input type="text" title="보조필드3" class="input_txt  lp10" id="subCdRef3" name="subCdRef3" maxlength="25"/></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1"><label for="subCdRef4">보조필드4</label></div>
			<div class="pop_menu_col2"><input type="text" title="보조필드4" class="input_txt lp10" id="subCdRef4" name="subCdRef4" maxlength="25" /></div>
		</div>
		<div class="pop_menu_row">
			<div class="pop_menu_col1 pop_menu_col1_right"><label for="subCdRef5">보조필드5</label></div>
			<div class="pop_menu_col2"><input type="text" title="보조필드5" class="input_txt  lp10" id="subCdRef5" name="subCdRef5" maxlength="25"/></div>
		</div>
		<div class="pop_menu_row pop_menu_oneRow">
			<div class="pop_menu_col1 pop_oneRow_col1"><label for="subCdDesc">보조필드설명</label></div>
			<div class="pop_menu_col2 pop_oneRow_col2"><textarea title="보조필드설명" class="input_note lp10" id="subCdDesc" name="subCdDesc" /></div>
		</div>

			<div class="btn_div">
				<div class="button_normal save_btn complete_btn" id="saveBtnDetail">저장</div>
				<div class="button_normal exit_btn" id="exitBtnDetail">취소</div>
			</div>
		</div>
	</form>
</div>
</html>