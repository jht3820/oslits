<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp"%>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>

<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>
<style>
.prj_title { font-size: 2em; font-weight: bold; margin-bottom: 30px; }
.documentSpanImg{content: "";display: inline-block;vertical-align: middle;margin: -3px 5px 0 0;width: 14px;height: 17px;background: url(/images/contents/document_icon.png);}
.left_con_div{width:100%;padding-top: 15px; }
.right_con_div{width:50%;text-align:right;float:left;}
.ok_document{clear:both;width:100%;height:48px;border:1px solid #ddd;margin-top:10px;display: inline-block;padding-top:5px;}
.documentFileList{margin-top:10px;height:224px;min-height:80px;overflow-y: auto;border:1px solid #ddd;}
.documentFileList:hover{border:1px solid #4b73eb;}
.documentFileList.dragOn{border:1px solid #0B20A1;}
.okDiv{border: 0;}
.menu_info_wrap{float: left;width: 70%;height: 600px;padding: 15px 36px;margin-left: 6px;vertical-align: middle;font-size: 0.75em;border-left: 1px solid #ccc;box-sizing: border-box;}
.menu_row {float: left;width: 50%;}
.menu_row .menu_col1 {float: left;clear: both;padding-left: 25px;width: 27%;height: 30px;line-height: 30px;background: #f9f9f9;border-width: 0 1px 1px 0;border-style: solid;border-color: #ddd;}
.menu_row .menu_col2{float: left;padding: 5px;width: 73%;height: 30px;line-height: 0px;border-bottom: 1px solid #ddd;}
.btn_con_div{width: 100%;text-align: center;clear: both;}
.fileDelBtn{float: right;padding: 4px 10px;border: 1px solid #ddd;background-color: #FE5454;color: #fff;cursor: pointer;z-index:2;}
.fileDivBoth{height:37px !important;}
#btn_download_formFileZip{display:none;}
</style>
<script>
//파일 Sn 변수 불러오기
var fileSnVar = 0;
//선택 메뉴 사용여부
var menuUseCd = '01';
//zTree
var zTree;

function sortableFile(){	
	var beforeFileDiv = $('.ok_document').children('div');
	//산출물 div 확정 처리
	$(".ok_document, .documentFileList").sortable({
			items: ".file_frame_box",
			placeholder: "ui-state-highlight portlet-placeholder",
			connectWith: ".ok_document, .documentFileList",
			scroll: false,
			stop: function(event, ui){
				//수정 권한 확인
				if(typeof btnAuthUpdateYn == "undefined" || btnAuthUpdateYn != 'Y'){
					jAlert('수정 권한이 없습니다.', '알림창', function( result ) {
						if( result ){
							$(this).sortable('cancel');
						}
					});
					return false;
				}
				//메뉴 사용여부가 미사용인 경우 이벤트 중지
				if(menuUseCd == '02'){
					jAlert('미 사용중인 메뉴입니다.');
					return false;
				}
				//일반 산출물간 이동인 경우
				if($(this).hasClass('documentFileList')){
					if($(ui.item).parent().hasClass('ok_document')){
						$(beforeFileDiv).removeClass('okDiv');
						$('.documentFileList').append(beforeFileDiv);
						$(ui.item).addClass('okDiv');
						beforeFileDiv = $('.ok_document').children('div');
						fnFileSelectSuccess($(ui.item));

					}
				 }
				
				//확정 산출물 이동인경우
				if($(this).hasClass('ok_document')){
						if($(ui.item).parent().hasClass('documentFileList')){
							$(ui.item).removeClass('okDiv');
							$('.documentFileList').append($(ui.item));
							beforeFileDiv = null;
							
							//확정 산출물 제거 처리
							fnFileSelectDelete();
						}
						
				 }
				 
				//산출물 파일번호로 정렬하기
				var listitems = $('.documentFileList').children('div').get();
				listitems.sort(function(a, b) {
				       var compA = parseInt($(a).attr('fileSn'));
				       var compB = parseInt($(b).attr('fileSn'));
				       return (compA > compB) ? -1 : (compA < compB) ? 1 : 0;
				       
				});
				$.each(listitems, function(idx, itm) {
			    	$('.documentFileList').append(itm);
				});
			}
		}).disableSelection();
}

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
	
	//파일 Drag&Drop 이벤트 걸기
	gfnFileDragDropUpload($('#documentFileList'),fnFileAjaxUpload);
	
	/********************************************************************
	*	공통기능 부분 정의 시작													*
	*********************************************************************/
	
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = CMM00001:사용여부 
	*/
	var mstCdStrArr = "CMM00001";
	var strUseYn = 'Y';
	var arrObj = [$("#useCd")];
	var arrComboType = ["S"];
	gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , true);
	
	//초기 메뉴 세팅
	fnSearchMenuList();
	/********************************************************************
	*	공통기능 부분 정의 종료													*
	*********************************************************************/
	
	/********************************************************************
	 *	메뉴 관리 기능 부분 정의 시작												*
	 *********************************************************************/

	// 메뉴 관리 전체 열기
	$(".menu .menu_expand_all").click(function(){
		zTree.expandAll(true);
	});

	// 메뉴 관리 전체 닫기
	$(".menu .menu_collapse_all").click(function(){
		zTree.expandAll(false);
	});

	// 메뉴 관리 조회 버튼 클릭 - 조회
	$("#btn_search_menuInfo").click(function(){
		fnSearchMenuList();
	});
	//	메뉴 수정 버튼 클릭 이벤트
	$(".menu .btn_save").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var menu = zTree.getSelectedNodes()[0];

		//선택 메뉴 없는경우 경고
		if(gfnIsNull(menu)){
			jAlert("선택된 메뉴가 없습니다.");
			return;
		}
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "docMenuInfoForm";
		var strCheckObjArr = ["docNm", "useCd", "ord"];
		var sCheckObjNmArr = ["산출물 메뉴명", "사용 여부", "순번"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		if(gfnIsNumeric("ord")){
			//메뉴정보 수정
			fnUpdateMenuInfoAjax($("#docMenuInfoForm").serializeArray(),"normal", false);
		}
	});

//  메뉴 추가 버튼 클릭 이벤트
	//	메뉴 추가시 DB 인서트 처리를 실행하며 등록이 성공되면 등록된 기본정보를 이용하여 메뉴 트리에 추가한다.
	$(".menu .btn_menu_add").click(function(){
		//선택된 메뉴 엘레먼트 객체 저장
		var menu = zTree.getSelectedNodes()[0];

		//선택 메뉴 없는경우 경고
		if(gfnIsNull(menu)){
			jAlert("선택된 메뉴가 없습니다.");
			return;
		}
		
		if( menu.level >= 3){
			jAlert("3뎁스 이상 추가할 수 없습니다.","알림창");
			return;
		} else{
			//인서트 로직 정상적으로 동작했을때 선택되어 있던 폴더 선택해제하고 DB 인서트된 정보를 이용하여 하위엘레먼트로 추가한다.
			//선택한 로우의 메뉴ID를 인자로 보냄
		    fnInsertMenuInfoAjax(menu);	
		}
	});

	//	메뉴 삭제 버튼 
	$("#btn_delete_menuDeleteInfo").click(function(){
		//선택 메뉴 가져오기
		var menu = zTree.getSelectedNodes()[0];
		
		//선택 메뉴 없는경우 경고
		if(gfnIsNull(menu)){
			jAlert("선택된 메뉴가 없습니다.");
			return;
		}
		
		if(menu.level == 0){
			jAlert("루트 디렉터리는 삭제 할 수 없습니다.","알림창");
		} else {
			//선택한 div의 부모영역이 가진 자식 노드의 갯수로 하위메뉴 존재 여부를 판단한다.
			if(menu.length == 0){
				toast.push("메뉴를 선택하지 않았습니다. 메뉴를 선택해 주세요.");
			}
			else{
				if(menu.check_Child_State != -1){
					toast.push("하위 메뉴가 존재하기때문에 삭제할 수 없습니다. 하위메뉴를 먼저 삭제해주세요.");
				}else{
					//삭제 컨펌
					jConfirm("삭제 하면 되돌릴 수 없습니다. 삭제 하시겠습니까?", "알림창", function( result ) {
						if( result ){
							fnDeleteMenuInfoAjax(menu);
						}
					});
					
				}
			}
		}
	});

	/********************************************************************
	 *	메뉴 관리 기능 부분 정의 종료												*
 	*********************************************************************/

});


/********************************************************************
* 메뉴 관리 기능 부분 정의 시작												*
*********************************************************************/
/**
 * 	좌측 메뉴 선택했을때 메뉴 정보 표시 함수
 */
 function fnGetMenuInfoAjax(docId){
	var fileObj;
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/prj/prj3000/prj3000/selectPrj3000MenuInfoAjax.do'/>","loadingShow":false}
				,{ docId:docId, docCommonCd:"02" });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
        	//디테일폼 세팅
        	gfnSetData2Form(data, "docMenuInfoForm");
        	
        	$('.ok_document').html('');
        	$('.documentFileList').html('');
        	var beforeFileDiv = $('.ok_document').children('div');
        	if(data.fileList){
        		//첨부파일 리스트
	        	$.each(data.fileList,function(idx,map){
	        		//확정 산출물 양식
	        		if(map.fileSn == data.docFormFileSn){
	        			fileObj = gfnFileListReadDiv(map,'.ok_document','doc');
	        		}else{
	        			//일반 산출물 양식
		        		 fileObj = gfnFileListReadDiv(map,'.documentFileList','doc');
	        		}
	        		//산출물 삭제 function 대체 하기위한 구문
	        	 	if(!gfnIsNull(fileObj)){
	        			$(fileObj).children('.file_main_box').children('.file_delete').attr('onclick','fnFileItemDelete(this)');
	        		} 
				});
        		$('.file_progressBar').remove(); //초기에 progressBar열려있으면 제거
	        	sortableFile();
	        	
	        	
				//정렬
		        var listitems = $('.documentFileList').children('div');
				listitems.sort(function(a, b) {
				       var compA = parseInt($(a).attr('fileSn'));
				       var compB = parseInt($(b).attr('fileSn'));
				       return (compA > compB) ? -1 : (compA < compB) ? 1 : 0;
				       
				});
				$.each(listitems, function(idx, itm) {
					$('.documentFileList').append(itm);
				});

        	}
        	//최하위 뎁스가 아니라면
        	if(data.lvl != 3){
        		$('#btn_insert_fileSelect').hide();
        		$('#fileListDiv').hide();
        		$('#btn_download_formFileZip').css({display:"inline-block"});
        	}else{
        		btnAuthInsertYn == 'Y' ? $("[id^=btn_insert]").show() : $("[id^=btn_insert]").hide();
        		$('#fileListDiv').show();
        		$('#btn_download_formFileZip').hide();
        	}
        	
        	//파일 Sn
        	fileSnVar = data.fileSn;
        	
        	//사용 여부
        	menuUseCd = data.useCd;
        	if(menuUseCd == '01'){
        		if($('#documentFileList').is('.disabled')){
        			$('#documentFileList').removeClass('disabled');
        		}
        	}else{
        		if(!$('#documentFileList').is('.disabled')){
        			$('#documentFileList').addClass('disabled');
        		}
        	}
		});

		//AJAX 전송
		ajaxObj.send();
	}
	
//산출물 확정 제거
function fnFileSelectDelete(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/updatePrj3000FileSnAjax.do'/>"}
			,{ docFileSn:null, docId:$('#docId').val(),docCommonCd:"01" });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//수정이 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//hidden input value null
    		$('#docFormFileSn').val('');
    		toast.push(data.message);
    	}
	});

	//AJAX 전송
	ajaxObj.send();
}
//산출물 삭제 처리
function fnFileItemDelete(thisObj){
	//이벤트 중복실행 방지
	window.event.cancelBubble=true;
	
	var uiItem = $(thisObj).siblings('.file_contents');

	if(btnAuthDeleteYn != 'Y'){
		
		jAlert('삭제 권한이 없습니다.', '알림창', function( result ) {
			if( result ){
				return false;
			}
		});
		
	}else if(!confirm('산출물을 삭제하시겠습니까?')){
		return false;
	 }else{
		var atchFileId = $(uiItem).attr('atchId');
		var fileSn = $(uiItem).attr('fileSn');
		if(gfnIsNull(atchFileId)){
			$(uiItem).parent().parent().remove();
		}else{
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/com/fms/FileDelete.do'/>"}
					,{ "atchFileId": atchFileId, "fileSn" : fileSn });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
	        	if(data.Success == 'Y'){
	        		toast.push(data.message);
	        		if($(uiItem).parent().parent().parent().hasClass('ok_document')){
						 fnFileSelectDelete();
					 }
	        		$(uiItem).parent().parent().remove();
	        		
	        		//파일 Sn -1
		        	fileSnVar--;
	        	}else{
	        		toast.push(data.message);
	        	}
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
		 	});
			//AJAX 전송
			ajaxObj.send();
		}
	 }
}
/**
 * 조회버튼 클릭시 메뉴 리스트 조회 AJAX
 */
function fnSearchMenuList(){

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/selectPrj3000MenuListAjax.do'/>","loadingShow":false}
			,{docCommonCd:"02" });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	$('#docMenuInfoForm')[0].reset();
    	$('.ok_document').html('');
    	$('.documentFileList').html('');
    	var beforeFileDiv = $('.ok_document').children('div');
    	
    	$('.ok_document').html('');
    	$('.documentFileList').html('');
    	toast.push(data.message);
    	
    	// zTree 설정 
	    var setting = {
	        data: {
	        	key: {
					name: "docNm"
				},
	            simpleData: {
	                enable: true,
	                idKey: "docId",
					pIdKey: "upperDocId",
					rootPId: "000"
	            }
	        },
			callback: {
				onClick: function(event, treeId, treeNode){
					//우측 메뉴 정보
					fnGetMenuInfoAjax(treeNode.docId);
				},
				onRightClick : function(event, treeId, treeNode){
					//메뉴명 변경 상자 나타내기
					zTree.editName(treeNode);
				},
				onRename : function(event, treeId, treeNode){
					//메뉴명 변경 이벤트 일어 날 경우, 메뉴명 수정 이벤트 
					fnUpdateMenuInfoAjax(treeNode,"editRename");
				},
				onDblClick : function(event, treeId, treeNode){
					//노드 더블 클릭시 발생
					if(!gfnIsNull(treeNode)){
						//자식노드가 없는 노드 더블 클릭시 사용유무 변경
						if(!treeNode.isParent && typeof treeNode.children == "undefined"){
							fnUpdateMenuInfoAjax(treeNode,"editUseCd",false);
						}
					}
				}
			},
			view : {
				showIcon : function(treeId, treeNode) {
					
					if(typeof zTree != "undefined" && treeNode.level != 3 && !treeNode.isParent){
						treeNode.isParent = true;

						zTree.refresh();
						//폴더의 계층구조가 3단계가 아니면  tree전체 펼침 시에 일회적 동작 안함(좋은방법같진않고 임시방편 추후개선)
						
					}
					return true;
				}
			}
	    };
    
	    // zTree 초기화
	    zTree = $.fn.zTree.init($("#prjDocJson"), setting, data.baseDocList);
	    zTree.expandAll(false);
	
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
	});
	//AJAX 전송
	ajaxObj.send();
}
//산출물 확정 처리
function fnFileSelectSuccess(obj){
	obj = $(obj).children().children('.file_contents');
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/updatePrj3000FileSnAjax.do'/>"}
			,{ atchId:$(obj).attr("atchId"), docFileSn:$(obj).attr("fileSn"), docId:$('#docId').val(), docCommonCd:"02" });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//수정이 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//hidden input value insert
        	$('#docFormFileSn').val($(obj).attr("fileSn"));
    		toast.push(data.message);
    	}
	});
	//AJAX 전송
	ajaxObj.send();
}

/**
 * 	신규 메뉴 등록 함수
 *	해당 함수 호출시 상위메뉴의 정보를 이용하여 새로운 하위 메뉴를 등록한다.
 */
function fnInsertMenuInfoAjax(docObj){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/insertPrj3000MenuInfoAjax.do'/>","loadingShow":false}
			,{ docId:docObj.docId, docCommonCd:"02" });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//등록이 실패하면 실패메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//3뎁스가 아니라면 부모형 메뉴
    		if(data.lvl != 3){
    			data.isParent = true;
    		}
    		//산출물 추가
    		zTree.addNodes(zTree.getSelectedNodes()[0], data);
    		
    	}
    	
    	toast.push(data.message);
	});

	//AJAX 전송
	ajaxObj.send();
}
/**
*	메뉴 정보 수정 함수
*	선택한 메뉴정보를 수정한다.
*/
function fnUpdateMenuInfoAjax(docObj, updateType, updateAsync){	
	
	//객체 넘어왔는지 확인
	if(typeof docObj == "undefined" || docObj == null){
		toast.push("선택된 산출물 메뉴가 없습니다.");
		return false;
	}
	
	if(updateType == "editUseCd"){ //사용유무 수정
		var parentNodeObj = docObj.getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 메뉴의 사용여부를 변경해주세요.");
				return false;
			}
		}
		
		//사용유무 반대로 변경
		docObj.useCd = (docObj.useCd=="01")?"02":"01";
	}else if(updateType == "normal"){
		var parentNodeObj = zTree.getSelectedNodes()[0].getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 메뉴의 사용여부를 변경해주세요.");
				return false;
			}
		}
	}

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/updatePrj3000MenuInfoAjax.do'/>", "async":updateAsync,"loadingShow":false}
			,docObj);
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);

    	//수정이 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//메뉴명 변경인 경우
    		if(updateType == "editRename"){
    			//우측 메뉴 정보
				fnGetMenuInfoAjax(docObj.docId);
    		
    		//더블 클릭으로 사용유무 변경하는 경우
    		}else if(updateType == "editUseCd"){
    			//현재 선택된 메뉴가 수정메뉴와 같은 경우 폼 세팅
    			if(docObj.docId == document.docMenuInfoForm.docId.value){
    				$("#useCd").val(docObj.useCd);
    			}
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (docObj.useCd == "01")?"#000":"#ccc";
    			$("#"+docObj.tId+"_a").css({color:useColor});
    			
    			//우측 메뉴 정보
				fnGetMenuInfoAjax(docObj.docId);
    			
    		//폼으로 정보 수정인 경우
    		}else if(updateType == "normal"){
    			//메뉴명이 변경된 경우
    			if(zTree.getSelectedNodes()[0].docNm != $("#docNm").val()){
    				//폼값 수정이기 때문에 메뉴값 수정 필요
    				zTree.getSelectedNodes()[0].docNm = $("#docNm").val();
    			}
    			//사용유무 변경된 경우
    			if(zTree.getSelectedNodes()[0].useCd != $("#useCd").val()){
    				var useColor = ($("#useCd").val() == "01")?"#000":"#ccc";
    				$("#"+zTree.getSelectedNodes()[0].tId+"_a").css({color:useColor});
    				
    				//폼값 수정이기 때문에 메뉴값 수정 필요
    				zTree.getSelectedNodes()[0].useCd = $("#useCd").val();
    				
    				//노드 순번 1부터 시작
    				var nodeOrd = 1;
    				
    				//자식 객체가 있는 경우에만 동작
    				if(typeof zTree.getSelectedNodes()[0].children != "undefined"){
	    				//자식 객체 사용유무 전체 변경
	    				$.each(zTree.getSelectedNodes()[0].children,function(){
	    					//사용유무는 부모값
	    					var subDocObj = this;
	    					subDocObj.useCd = $("#useCd").val();
	    					
	    					//순번이 null인 경우 세팅된 순번으로 변경
	    					if(gfnIsNull(subDocObj.ord)){
	    						subDocObj.ord = nodeOrd++;
	    					}else{
	    						//null이 아닌 경우 최대 순번으로 교체
	    						nodeOrd = (subDocObj.ord>nodeOrd)?subDocObj.ord:nodeOrd;
	    					}
	    					//수정 재귀
		    				fnUpdateMenuInfoAjax(subDocObj,"editSubUseCd",true);
		    			});
    				}
    			}
    		//하위 메뉴 사용유무 수정인경우 CSS 변경
    		}else if(updateType == "editSubUseCd"){
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (docObj.useCd == "01")?"#000":"#ccc";
    			$("#"+docObj.tId+"_a").css({color:useColor});
    			
    			//자식 객체가 있는 경우에만 동작 (재귀)
   				if(typeof docObj.children != "undefined"){
   					
   					//노드 순번 1부터 시작
    				var nodeOrd = 1;
   					
    				//자식 객체 사용유무 전체 변경
    				$.each(docObj.children,function(){
    					//사용유무는 부모값
    					var subDocObj = this;
    					subDocObj.useCd = $("#useCd").val();
    					
    					//순번이 null인 경우 세팅된 순번으로 변경
    					if(gfnIsNull(subDocObj.ord)){
    						subDocObj.ord = nodeOrd++;
    					}else{
    						//null이 아닌 경우 최대 순번으로 교체
    						nodeOrd = (subDocObj.ord>nodeOrd)?subDocObj.ord:nodeOrd;
    					}
    					
    					//수정 재귀
	    				fnUpdateMenuInfoAjax(subDocObj,"editSubUseCd",true);
	    			}); 
   				}
    		}
    		
    		//해당 노드 갱신
    		if(updateType == "normal"){	//폼값으로 수정
    			zTree.updateNode(zTree.getSelectedNodes()[0]);
    		}else{	//Json Object로 수정
    			zTree.updateNode(docObj);
    		}
    	}
    	
    	//재귀인경우 메시지 노출 안함
    	if(updateType != "editSubUseCd"){
    		toast.push(data.message);
    	}
	});
	//AJAX 전송
	ajaxObj.send();
}

/**
*	메뉴 삭제 함수
*	선택한 메뉴를 삭제한다.(DB에서 삭제 처리시 자식 메뉴들이 존재하면 삭제하지 않고 알림)
*/
function fnDeleteMenuInfoAjax(docObj){
	var licGrpId = '${sessionScope.loginVO.licGrpId}'; 
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/deletePrj3000AuthGrpInfoAjax.do'/>","loadingShow":false}
			,{ docId:docObj.docId, docCommonCd:"02" });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	
    	//삭제가 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		//해당 노드 제거
			zTree.removeNode(docObj);
			
    		//삭제 후 부모노드의 자식 수가 0일 경우 폴더형 메뉴로 변경
    		var parentNode = docObj.getParentNode();
    		
    		//메뉴 뎁스가 3이 아닌데, 자식 노드가 없는 경우
    		if(parentNode.level != 3 && parentNode.children.length == 0){
    			
    			//부모형 노드로 변경하고, 업데이트
    			parentNode.isParent = true;
    			zTree.updateNode(parentNode);
    		}
    	}
    	
    	toast.push(data.message);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message,"알림창");
	});
	//AJAX 전송
	ajaxObj.send();
}
//업로드 버튼 클릭
function fnOslDocUploadClick(){
	if($('#docId').val() == null || $('#docId').val() == ""){
		toast.push("선택된 산출물 메뉴가 없습니다.");
	}else if(menuUseCd == '02'){
		jAlert('미 사용중인 메뉴입니다.');
		return false;
	//등록 권한이 있는지 확인
	}else if(typeof btnAuthInsertYn != "undefined" && btnAuthInsertYn == 'Y'){
		var oslUpload_btn = document.getElementById('oslDocFileUpload');
		oslUpload_btn.click();
		fnOslDocUpload(oslUpload_btn);
	}
}

//실시간 업로드 처리
function fnOslDocUpload(ele){
	ele.onchange = function(){
		fnFileAjaxUpload(ele.files[0]);
		ele.value = null;
	}
}

//파일 업로드 AJAX 처리
function fnFileAjaxUpload(files){
	var docId = $("#docId").val();
	if(docId == null || docId == ""){
		toast.push("선택된 산출물 메뉴가 없습니다.");
		return false;
	}
	
	// 파일 확장자 추출( 소문자 )
	var ext = files.name.split(".").pop().toLowerCase();

	// 화이트 리스트가 아니라면 중지 업로드 중지.
	   if(!gfnFileCheck(ext)){
		   toast.push("확장자가 [ " +ext + " ] 인 파일은 첨부가 불가능 합니다.");
		   return false;
	   };
	
	
	//fileSn 기록 변수
	var fileSnTemp = fileSnVar;
	//전송 전 시간 기록 변수
	var beforeSendTime;
	//전송 시간 기록 변수
	var sendTime;
	
	//FormData에 input값, File값, FileSn값 넣기
	var fd = new FormData();
	fd.append('file', files);
	fd.append('uploadFileSn',fileSnTemp);
	
	//파일 정보 세팅
	var fileVo = {};
	var fileName = files.name;
	var fileExtsn = fileName.substring(fileName.lastIndexOf('.')+1);
	
	fileVo.orignlFileNm = fileName;
	fileVo.fileExtsn = fileExtsn;
	fileVo.fileMg = files.size;
	
	//파일 목록 먼저 출력
	var upFileObj = gfnFileListReadDiv(fileVo,'.documentFileList','doc');
	
	//업로드 게이지 바 0부터 시작
	$(upFileObj).children('.file_progressBar').children('div').css({width:0});
	
	gfnFormDataAutoValue('docMenuInfoForm',fd);
	
	//AJAX 설정 
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/insertPrj3000FileUploadAjax.do'/>"
				,"contentType":false
				,"processData":false
				,"cache":false
				,"pgBarObj":$(upFileObj).children('.file_progressBar').children('div')}
			,fd);
	//AJAX 통신 전 실행 함수
	ajaxObj.setFnbeforeSend(function(){
		beforeSendTime = new Date().getTime();
	});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
  	
  	if(data.saveYN == 'N'){
  		toast.push(data.message);
  		innerFail();
  		return false;
  	} 
  	else{
  		//첫 등록인 경우 docId세팅
   		if(data.firstInsert == 'Y'){
   			$('#docFormFileId').val(data.addFileId);
   		}
  		sendTime = (new Date().getTime())-beforeSendTime;
  		toast.push("업로드 완료 "+(sendTime/1000)+"초");
  		
  		upFileObj = $(upFileObj).children().children('.file_contents');
  		//첨부파일 세팅
   		$(upFileObj).attr('atchId',data.addFileId);
   		$(upFileObj).attr('fileSn',data.addFileSn);
    		
  		sortableFile();
  	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push("ERROR STATUS("+status+")<br>"+err);
  	innerFail();
	});
	
	//AJAX 전송
	ajaxObj.send();
	function innerFail(){
		pgBarObj.stop().animate({width:'100%'},500,function(){
  		pgBarObj.css('background-color','#FE5454');
  	});
	}
	
	//파일 Sn +1
	fileSnVar++;
}
/********************************************************************
* 메뉴 관리 기능 부분 정의 종료												*
*********************************************************************/


//확정 산출물 양식 전체 다운로드
function fnOslDocFormZipDownload(){
	var selMenu = zTree.getSelectedNodes();
	if(zTree.getSelectedNodes().length == 0){
		toast.push("선택된 메뉴가 없습니다.");
	}else{
		//AJAX 설정 
		var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/selectPrj3000MenuTreeZipDownload.do'/>","loadingShow":true}
			,{docId:selMenu[0].docId,docCommonCd:"02"});
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			//확정 산출물 갯수 체크
			var fileChk = 0;
			
			var fileInfo = "";
			$.each(data.docMenuList,function(){
				if(this.docFormFileSn != null && this.docFormFileSn != ""){
					//배열로 세팅
					fileInfo = fileInfo+"downFile="+this.docFormFileId+";"+this.docFormFileSn+"&";
					fileChk++;
				}
			});
			
			if(fileChk == 0){
				toast.push("첨부파일이 없습니다.");
				return false;
			}else if(fileChk < 2){
				toast.push("2개 이상의 첨부파일이 존재해야 합니다.");
				return false;
			}else{
				//압축 다운로드
				$.download('/com/fms/ZipFileDown.do',fileInfo.slice(0,-1),'post');
			}
	    	

		});

		//AJAX 전송
		ajaxObj.send();
	}
}
</script>

<div class="main_contents">
	<div class="prj_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
			<div class="top_control_wrap">
				<span style="float:left;">*품질 산출물 양식을 설정할 수 있습니다.</span>
				<span class="menu_tree_help"><i class="fa fa-question"></i>
					<div class="menu_tree_helpBox">
						<span>
							[좌측 트리 메뉴 기능 안내]<br/>
							<br/>
							&nbsp;-메뉴 클릭: 메뉴 상세 정보 보기<br/>
							&nbsp;-더블 클릭: 폴더형 메뉴의 경우 하위 메뉴 보기<br/>
							&nbsp;<span style="margin-left: 71px;"></span>하위 메뉴의 경우 사용여부 변경<br/>
							&nbsp;-우측 클릭: 메뉴명 변경
						</span>
					</div>
				</span>
				<span class="button_normal2" id="btn_download_formFileZip" onclick="fnOslDocFormZipDownload()"><i class='fa fa-file-zip-o' aria-hidden='true'></i>&nbsp;확정 산출물 양식 전체 다운로드</span>
				<span class="button_normal2 btn_inquery" id="btn_search_menuInfo"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;조회</span>
				<span class="button_normal2 btn_save" id="btn_update_menuInfo" tabindex=5><i class='fa fa-edit' aria-hidden='true'></i>&nbsp;정보 수정</span>
				<span class="button_normal2" onclick="fnOslDocUploadClick()" id="btn_insert_fileSelect">
				<input type="file" style="display: none" id="oslDocFileUpload" name="oslDocFileUpload" /><i class='fa fa-upload' aria-hidden='true'></i>&nbsp;산출물 양식 업로드
				</span>
			</div>		
		<div class="menu_wrap">
			<div class="menu_ctrl_wrap">
				<div class="menu_ctrl_btn_wrap">
					<span class="button_normal2 btn_menu_add" id="btn_update_menuAddInfo"><i class='fa fa-save' aria-hidden='true'></i>&nbsp;추가</span>
					<span class="button_normal2 btn_menu_del" id="btn_delete_menuDeleteInfo"><i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;삭제</span>
					<!-- <span class="button_normal2 btn_menu_reset" id="btn_delete_menuInsertAllDefault">초기화</span> -->
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span><span
							class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>

				<div class="menu_lists_wrap">
					<ul id="prjDocJson" class="ztree"></ul>
				</div>
			</div>

			<div class="menu_info_wrap">
				<form id="docMenuInfoForm" name="docMenuInfoForm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="docCommonCd" id="docCommonCd" value="02"/>
				<input id="lvl" type="hidden" name="lvl" value=""/>
					<div class="left_con_div" style="float:none; padding-bottom: 10px; border-bottom: 1px solid #ddd;" >
						<span class="documentSpanImg"></span><span>선택 산출물 메뉴 정보</span>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docId">산출물 ID</label></div>
						<div class="menu_col2"><input id="docId" type="text" name="docId" value="" class="readonly" readonly/></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docNm">상위 ID</label></div>
							<div class="menu_col2"><input id="upperDocId" type="text" name="upperDocId" value=""class="readonly" readonly /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docNm">산출물명</label></div>
							<div class="menu_col2"><input id="docNm" type="text" name="docNm" value="" tabindex=1 /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="ord">순번</label></div>
						<div class="menu_col2"><input id="ord" type="text" name="ord" value="" tabindex=2></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="useCd">사용여부</label></div>
						<div class="menu_col2"><span class="search_select"><select class="w200" name="useCd" id="useCd" tabindex=3 style="    width: 168px !important;"></select></span></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docFormFileId">파일 ID</label></div>
						<div class="menu_col2"><input id="docFormFileId" type="text" name="docFormFileId" value="" tabindex=1  class="readonly" readonly/></div>
					</div>
					<div class="menu_row" style="margin-bottom:10px;width:100%;">
						<div class="menu_col1" style="width: 99.83px;height: 80px;">산출물 설명</div>
						<div class="menu_col2" style="height: 80px;">
							<textarea title="산출물 설명" id="docDesc" name="docDesc" value="" tabindex=4></textarea>
						</div>
					</div>
					<div id="fileListDiv">
						<div class="left_con_div" style="float:none;">
							<span class="documentSpanImg"></span><span>확정 산출물 양식</span>
						</div>
						<input type="hidden" name="docFormFileSn" id="docFormFileSn" value=""/>
						<input type="hidden" name="docAtchFileId" id="docAtchFileId" value=""/>
						<input type="hidden" name="docFileSn" id="docFileSn" value=""/>
						<div class="ok_document">
	
						</div>
						<div class="left_con_div">
							<span class="documentSpanImg"></span><span>산출물 양식 업로드 목록 (Drag&Drop 한번에 최대 5개 제한)</span>
						</div>
						<div class="documentFileList" id="documentFileList" onclick="fnOslDocUploadClick()">
						</div>
						
					</div>
				</form>

			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />