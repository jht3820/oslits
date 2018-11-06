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
</style>
<script type="text/javascript">

	$(document).ready(function() {
		
		//역할 목록 그리드 세팅
		fnAuthListGrid();
		fnAuthRefresh();
		
		/* 취소 */
		$('#btnPopStm3102Cancle').click(function() {
			gfnLayerPopupClose();
		});
	});
	

//역할 그룹 그리드
function fnAuthListGrid(){
	authGrid = new ax5.ui.grid();
 
    authGrid.setConfig({
        target: $('[data-ax5grid="auth-grid"]'),
        sortable:true,
        showRowSelector: true,
        header: {align:"center"},
        multipleSelect: false ,
        columns: [
         {key: "authGrpNm", label: "역할그룹 명", width: 160, align: "center"},
         {key: "usrTypNm", label: "사용자유형", width: 120, align: "center"},
         {key: "authGrpDesc", label: "역할그룹 설명", width: 225, align: "center"},
        ],
        body: {
            align: "center",
            columnHeight: 30
        },
        page:{display:false}
    });
}

//역할그룹 정보 조회
function fnAuthRefresh(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/stm/stm3000/stm3000/selectStm3000JenAuthGrpList.do'/>","loadingShow":false},
			{prjId:'${param.prjId}',jenId: "${param.jenId}"});
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			authGrid.setData(data.jenAuthGrpList);
		}else{
			toast.push(data.message);
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

//역할 추가
function fnAuthInsert(){
	//cmm1700 공통팝업 호출
	gfnCommonAuthPopup("" ,true,function(objs){
		if(objs.length>0){
			var selAuthFd = new FormData();
			
			//기본 정보 대입
			selAuthFd.append("prjId","${param.prjId}");
			selAuthFd.append("jenId","${param.jenId}");
			
			//callback 선택 역할그룹 대입
			for(var i=0; i<objs.length; i++){
				selAuthFd.append("selAuth",JSON.stringify({authGrpId: objs[i].authGrpId}));
			}
			
			//선택 역할그룹갯수 넘기기
			selAuthFd.append("selAuthCnt",objs.length);
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/stm/stm3000/stm3000/insertStm3000JenAuthGrpList.do'/>"
						,"contentType":false
						,"processData":false
						,"cache":false},
					selAuthFd)
			
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				
				//에러 없는경우
				if(data.errorYN != "Y"){
					//정보 갱신
					fnAuthRefresh();
				}
				jAlert(data.message,"알림");
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
				jAlert(data.message, "알림");
		 	});
			//AJAX 전송
			ajaxObj.send();
		}
	});
}

//역할그룹 삭제
function fnAuthDelete(){
	var chkList = authGrid.getList('selected');
	
	if(gfnIsNull(chkList)){
		jAlert("선택한 역할그룹이 없습니다.","알림창");
		return false;
	}
	
	var selAuthFd = new FormData();
	//기본 정보 대입
	selAuthFd.append("prjId","${param.prjId}");
	selAuthFd.append("jenId","${param.jenId}");
	
	//callback 선택 역할그룹 대입
	$.each(chkList,function(idx, map){
		selAuthFd.append("selAuth",JSON.stringify({authGrpId: map.authGrpId}));
	});
	
	//선택 역할그룹 갯수 넘기기
	selAuthFd.append("selAuthCnt",chkList.length);
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/stm/stm3000/stm3000/deleteStm3000JenAuthGrpList.do'/>"
				,"contentType":false
				,"processData":false
				,"cache":false},
			selAuthFd)
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		//에러 없는경우
		if(data.errorYN != "Y"){
			//리비전 정보 갱신
			fnAuthRefresh();
		}
		jAlert(data.message,"알림");
	});
	
	//AJAX 전송
	ajaxObj.send();
}

</script>

<div class="popup">
		
	<div class="pop_title">JENKINS 허용 역할그룹 설정</div>
	<div class="pop_sub">
		<div class="tab_contents menu" style="max-width:600px;height:230px;">
			<div class="pop_note" style="margin-bottom:0px;">
			<div class="note_title">
				<div class="note_leftBtn">접근 허용 역할 목록</div>
				<div class="note_rightBtn">
					<div class="button_normal note_btn" id="btn_insert_auth" onclick="fnAuthInsert();">추가</div>
					<div class="button_normal note_btn" id="btn_update_auth" onclick="fnAuthDelete();">삭제</div>
				</div>
			</div>
			<div data-ax5grid="auth-grid" data-ax5grid-config="{}" style="height: 130px;"></div>	
		</div>		
		</div>
		<div class="btn_div">
			<div class="button_normal exit_btn" id="btnPopStm3102Cancle" >닫기</div>
		</div>
	</div>
	</form>
</div>
</html>