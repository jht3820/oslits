<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp"%>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/common/fileUpload.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<style>
/* 
 * doc_title
 *    - 화면 사이즈 변경에 관계없이 타이틀 부분 사이즈 고정
 *	  - min-width: 1280px; 추가
 */
.doc_title { font-size: 2em; font-weight: bold; margin-bottom: 30px; width:100%; min-width: 1280px;}
.documentSpanImg{content: "";display: inline-block;vertical-align: middle;margin: -3px 5px 0 0;width: 14px;height: 17px;background: url(/images/contents/document_icon.png);}
.left_con_div{width:100%;padding-top: 15px; }
.right_con_div{width:50%;text-align:right;float:left;}
.ok_document{clear:both;width:100%;height:48px;border:1px solid #ddd;margin-top:10px;display: inline-block;padding-top:5px;}
.documentFileList{margin-top:10px;height:224px;min-height:80px;overflow-y: auto;border:1px solid #ddd;}
.documentFileList:hover{border:1px solid #4b73eb;}
/* .documentFileList.dragOn{border:1px solid #0B20A1;} */
.okDiv{border: 0;}
.menu_row {float: left;width: 50%;}
.menu_row .menu_col1 {float: left;clear: both;padding-left: 25px;width: 27%;height: 40px;line-height: 30px;background: #f9f9f9;border-width: 0 1px 1px 0;border-style: solid;border-color: #ddd;}
.menu_row .menu_col2{float: left;padding: 5px;width: 73%;height: 40px;line-height: 0px;border-bottom: 1px solid #ddd;}
.menu_oneRow {width: 100%;}
.menu_oneRow .oneRow_col1 {width: 13.5%;}
.menu_oneRow .oneRow_col2 {width: 86%;}
.btn_con_div{width: 100%;text-align: center;clear: both;}
.fileDelBtn{float: right;padding: 4px 10px;border: 1px solid #ddd;background-color: #FE5454;color: #fff;cursor: pointer;z-index:2;}
.fileDivBoth{height:37px !important;}
#btn_download_formFileZip{display:none;}
#empty_col1{ border: none; background-color: white;}
.ui-datepicker-trigger {height: 24px;}
.menu_info_wrap input[type="number"] { vertical-align: middle; margin-top: 0; height: 100%; font-size: 1em; border: 1px solid #ccc; width: 100%; min-width: 150px;}
.menu_col2 input[type="text"] {width: 100%; font-size: 12px;}
.menu_ctrl_wrap {/* min-height: 665px; */  min-height: 790px; }
#docDesc {padding: 7px 5px 5px 6px; font-size: 12px;}
.confirm_document_desc{ clear: both; width: 100%; height: 80px; margin-top:10px; display: inline-block; padding-top: 5px; }
/* 오른쪽만 선이 들어가도록 */
.menu_row .menu_col1_right {border-left: 1px solid #ddd;}

/* 필수 입력값 */
.required_info{color:red; font-weight: bold; }
</style>
<script>
//파일 Sn 변수 불러오기
var fileSnVar = 0;
//선택 메뉴 사용여부
var menuUseCd = '01';
//zTree
var zTree;
//선택된 산출물 및 하위 산출물 노드가 담긴 배열, 산출물 삭제 시 사용
var chkDocNodeArr = [];
// 노드에서 마우스 우클릭 시 산출물 명을 담아둘 변수 
// 마우스 우클릭 후 산출물 명을 공백으로 입력하지 못하도록 하기 위해 사용
var rightClickDocName = "";

//유효성 체크
var arrChkObj = {"docNm":{"type":"length","msg":"개발문서 명은 200byte까지 입력이 가능합니다.",max:200}
				,"ord":{"type":"number"}
				,"docDesc":{"type":"length","msg":"개발문서 설명은 500byte까지 입력이 가능합니다.", "max":500}
				,"docConfDesc":{"type":"length","msg":"확정 개발문서 비고는 500byte까지 입력이 가능합니다.", "max":500}
				};

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
					jAlert('미 사용중인 개발문서입니다.');
					return false;
				}
				//일반 산출물간 이동인 경우
				 if($(this).hasClass('documentFileList')){
					if($(ui.item).parent().hasClass('ok_document')){
						//확정 산출물이 존재하는 경우에만 스왑
						if($(".ok_document").children("div.file_frame_box").length > 1){
							$(beforeFileDiv).removeClass('okDiv');
							$('.documentFileList').append(beforeFileDiv);
						}
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
	
	//가이드 상자 호출
	gfnGuideStack("add",fnPrj3000GuideShow);
	
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
	
	// 유효성 체크
	gfnInputValChk(arrChkObj);
	
	var serverTime=gfnGetServerTime('yyyy-mm-dd');
	// 최소날짜 지정이 필요할때 옵션사용
	//var dataOptions = { "minDate" : serverTime };
		
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
			jAlert("선택된 개발문서가 없습니다.");
			return;
		}
		
		/* 필수입력값 체크 공통 호출 */
		var strFormId = "docMenuInfoForm";
		var strCheckObjArr = ["docNm", "useCd", "ord"];
		var sCheckObjNmArr = ["개발문서 명", "사용 여부", "순번"];
		if(gfnRequireCheck(strFormId, strCheckObjArr, sCheckObjNmArr)){
			return;	
		}
		if(gfnIsNumeric("ord")){
			
			// 수정 전 유효성 검사
			if(!gfnSaveInputValChk(arrChkObj)){
				return false;	
			}
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
			jAlert("선택된 개발문서가 없습니다.");
			return;
		}
		
		 // 산출물  양식 무한뎁스로 추가
		fnInsertMenuInfoAjax(menu);
	});

	//	메뉴 삭제 버튼 
	$("#btn_delete_menuDeleteInfo").click(function(){
		//선택 메뉴 가져오기
		var menu = zTree.getSelectedNodes()[0];
		
		//선택 메뉴 없는경우 경고
		if(gfnIsNull(menu)){
			jAlert("선택된 개발문서가 없습니다.");
			return;
		}
		
		if(menu.level == 0){
			jAlert("루트 디렉터리는 삭제 할 수 없습니다.","알림창");
		} else{
			//선택한 div의 부모영역이 가진 자식 노드의 갯수로 하위메뉴 존재 여부를 판단한다.
			if(menu.length == 0){
				toast.push("개발문서를 선택하지 않았습니다. 개발문서를 선택해 주세요.");
			}
			else{
				jConfirm("삭제 시 개발문서에 업로드된 파일도 삭제됩니다. \n\n 해당 개발문서 및 하위 개발문서을 모두 삭제하시겠습니까? \n", "알림창", function( result ) {
		   			if( result ){
		   				jConfirm("선택된 개발문서 및 하위 개발문서, 업로드 된 파일도 삭제되며 삭제 시 되돌릴 수 없습니다. \n\n그래도 삭제 하시겠습니까?", "알림창", function( result ) {
		   	   				if( result ){
		   	   					// 산출물 노드를 담은 배열 초기화
		   	   					chkDocNodeArr.length = 0;
		   	   						
		   	   					// 선택된 산출물 및 하위 산출물 노드를 배열 추가
		   	   					fnGetDocList(menu);
		   	   					
		   	   					var ajaxParam = "";
		   	   					
		   	   					// 산출물 ID, 첨부파일 ID, 확정 산출물 파일 ID를 문자열로 만들어 삭제시 넘긴다.
			   	   				$(chkDocNodeArr).each(function(idx, map){
			   	   					ajaxParam +="&docId="+map.docId+"&docAtchFileId="+map.docAtchFileId+"&docFormFileId="+map.docFormFileId;
			   	   				});
		   	   					
		   	   					// 산출물 삭제
		   	   					fnDeleteMenuInfoAjax(chkDocNodeArr, ajaxParam);
		   	   				}
		   	   			});
		   			}
		   		}); // end jConfirm
			}
		}
	});

	/********************************************************************
	 *	메뉴 관리 기능 부분 정의 종료												*
 	*********************************************************************/
 	
    //파일 Drag&Drop 이벤트 걸기
	gfnFileDragDropUpload($('#documentFileList'),fnFileAjaxUpload);
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
				,{ docId:docId } );
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
			//function fnFormReset(){

			$('#docMenuInfoForm')[0].reset();
			$("#docDesc").val("");
			$("#search_useCd").show();
			$("#readonly_useCd").hide();
			
    		var docLvl = data.lvl;
    		// ROOT일경우 사용여부 수정불가
    		if( docLvl == 0 ){
    			$("#search_useCd").hide();
    			$("#readonly_useCd").show();
    		}
			
        	//디테일폼 세팅
        	gfnSetData2Form(data, "docMenuInfoForm");

        	// 산출물 마감일 달력 세팅
			gfnCalendarSet('YYYY-MM-DD',['docEdDtm']);
        	
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
	        			$(fileObj).children('.file_main_box').children('.file_delete').attr('onclick','fnFileItemDelete(this, event)');
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
        	
        	$('#btn_download_formFileZip').show();	// 확정 산출물 양식 전체 다운로드 버튼
        	
        	// 루트를 제외하고 모두 파일업로드 버튼 보이도록
        	if(data.lvl == 0){
        		$('#btn_download_formFileZip').css({display:"inline-block"});
        		$('#btn_insert_fileSelect').hide();
        		$('#fileListDiv').hide();
        		// 가이드 hide
        		$('#confirmDocFile_guide').hide();
        		$('#docFileList_guide').hide();
        	}else{
        		btnAuthInsertYn == 'Y' ? $("[id^=btn_insert]").show() : $("[id^=btn_insert]").hide();
        		$('#btn_download_formFileZip').css({display:"inline-block"});
        		$('#fileListDiv').show();
        		// 가이드 show
        		$('#confirmDocFile_guide').show();
        		$('#docFileList_guide').show();
        	}
        	
        	var zTreeObj = $.fn.zTree.getZTreeObj("prjDocJson");	
        	var selDocNode = zTreeObj.getSelectedNodes()[0];	// 현재 선택한 노드
        	var docChilds = selDocNode.children;				// 선택한 노드의 자식노드 들
        	
        	// 자식노드가 없을 경우 - 확정 산출물 양식 전체 다운로드 버튼 hide
        	if( gfnIsNull(docChilds) ){
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
			{"url":"<c:url value='/prj/prj3000/prj3000/updatePrj3000FileSnAjax.do'/>","loadingShow":false}
			,{ docFileSn:null, docId:$('#docId').val() });
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
function fnFileItemDelete(thisObj, event){
	//이벤트 중복실행 방지
	window.event.cancelBubble=true;
	event.stopPropagation();
	
	var uiItem = $(thisObj).siblings('.file_contents');

	if(btnAuthDeleteYn != 'Y'){
		
		jAlert('삭제 권한이 없습니다.', '알림창', function( result ) {
			if( result ){
				return false;
			}
		});

	}else{
		
		jConfirm("파일을 삭제하시겠습니까?", "알림창", function( result ) {
			if( result ){
			
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
				        	//fileSnVar--;
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
		});	// end jConfirm
	 }
}

/**
 * 조회버튼 클릭시 메뉴 리스트 조회 AJAX
 */
function fnSearchMenuList(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/selectPrj3000MenuListAjax.do'/>","loadingShow":false});
	//AJAX 전송 성공 함수
	ajaxObj.async = false;
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		var listSize = data.baseDocList.length;
		// 우측 상세정보 화면 초기화
		fnFormReset();
    	
    	var beforeFileDiv = $('.ok_document').children('div');
    	
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
				}
			},
			view : {
				fontCss: function(treeId, treeNode){
					return (treeNode.useCd == "02")? {color:"#ddd"} :{};
				},
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
	    zTree = $.fn.zTree.init($("#prjDocJson"), setting, data.baseDocList);
	  //폴더의 계층구조가 3단계가 아니면  tree전체 펼침 시에 일회적 동작 안함(좋은방법같진않고 임시방편 추후개선)
		zTree.expandAll(false);
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
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
			,{ atchId:$(obj).attr("atchId"), docFileSn:$(obj).attr("fileSn"), docId:$('#docId').val() });
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
			,{ docId:docObj.docId });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	//등록이 실패하면 실패메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	}
    	else{
    		fnSearchMenuList();
    		// 2뎁스 미만 산출물 메뉴는 폴더형 아이콘으로(부모 아이콘)
    		// 그외 산출물 메뉴는 추가될 경우 문서 아이콘으로(자식 아이콘) 
    		if(data.lvl < 2){
    			data.isParent = true;
    		}
    		
    		var treeNodes = zTree.getNodesByParam("docId", data.docId);
    		var pNode = treeNodes[0].getParentNode();
    		zTree.expandNode(pNode, true, true, null, false);   		
    		zTree.selectNode(treeNodes[0]);
    		fnGetMenuInfoAjax(treeNodes[0].docId);

    		//var selectNode = zTree.getSelectedNodes()[0];
    		
    		// 산출물 추가
    		//zTree.addNodes(selectNode, data);
    	}
    	
    	toast.push(data.message);
	});
	//AJAX 전송
	ajaxObj.send();
}

/**
*	메뉴 정보 수정 함수
*	선택한 메뉴정보를 수정한다.
*	@param:	docObj - 수정 타겟
*			updateType - 수정형식 (normal:상세메뉴정보에서 수정, editRename:메뉴명만 수정, editUseCd:뎁스3 더블클릭으로 사용유무 수정, editSubUseCd:뎁스1,2 하위 메뉴 사용유무 수정)
*			updateAsync - 동기방식(editSubUseCd일 경우 동기방식 동작)
*/
function fnUpdateMenuInfoAjax(docObj, updateType, updateAsync){	

	//객체 넘어왔는지 확인
	if(typeof docObj == "undefined" || docObj == null){
		toast.push("선택된 개발문서가 없습니다.");
		return false;
	}
	
	if(updateType == "editUseCd"){ //사용유무 수정
		var parentNodeObj = docObj.getParentNode();
		//부모노드 존재하는 경우
		if(!gfnIsNull(parentNodeObj)){
			//부모노드의 사용유무값이 "02"인경우 "01"로 수정 불가
			if(parentNodeObj.useCd == "02"){
				toast.push("상위 개발문서의 사용여부를 변경해주세요.");
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
				toast.push("상위 개발문서의 사용여부를 변경해주세요.");
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
    			/* 
    			var docId = $('#docId').val();
    			fnSearchMenuList();

        		var treeNodes = zTree.getNodesByParam("docId", docId );        		
        		var pNode = treeNodes[0].getParentNode();
        		zTree.expandNode(pNode, true, true, null, false);   		
        		zTree.selectNode(treeNodes[0]);
        		fnGetMenuInfoAjax(treeNodes[0].docId);
    			 */
    			 
        		// 2뎁스 미만 산출물 메뉴는 폴더형 아이콘으로(부모 아이콘)
        		// 그외 산출물 메뉴는 추가될 경우 문서 아이콘으로(자식 아이콘) 
        		
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
    				
    				//자식 객체가 있는 경우에만 동작
    				if(typeof zTree.getSelectedNodes()[0].children != "undefined"){
	    				//자식 객체 사용유무 전체 변경
	    				$.each(zTree.getSelectedNodes()[0].children,function(){
	    					//사용유무는 부모값
	    					var subDocObj = this;
	    					subDocObj.useCd = $("#useCd").val();
	    					
	    					//수정 재귀
		    				fnUpdateMenuInfoAjax(subDocObj,"editSubUseCd",true);
		    			});
    				}
    				
    				// 사용유무 수정 후 우측정보 재조회
    				fnGetMenuInfoAjax(zTree.getSelectedNodes()[0].docId);
    			}

    		//하위 메뉴 사용유무 수정인경우 CSS 변경
    		}else if(updateType == "editSubUseCd"){
    			//사용유무에 따른 폰트 색상 수정
    			var useColor = (docObj.useCd == "01")?"#000":"#ccc";
    			$("#"+docObj.tId+"_a").css({color:useColor});
    			
    			//자식 객체가 있는 경우에만 동작 (재귀)
   				if(typeof docObj.children != "undefined"){
    				//자식 객체 사용유무 전체 변경
    				$.each(docObj.children,function(){
    					//사용유무는 부모값
    					var subDocObj = this;
    					subDocObj.useCd = $("#useCd").val();
    					
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
function fnDeleteMenuInfoAjax(chkDocNodeArr, ajaxParam){

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/deletePrj3000AuthGrpInfoAjax.do'/>","loadingShow":false}
			, ajaxParam);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	
    	//삭제가 실패하면 실패 메시지 후 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		
    		// 우측 상세정보 화면 초기화
    		fnFormReset();
    		
    		// 삭제할 산출물 노드 중 최상위 산출물 노드를 가져온다.
    		var firstDocNode = chkDocNodeArr[0];	
    		// 삭제처리를 위해 역순 - 최하위 산출물부터 배열에 배치
    		chkDocNodeArr.reverse();
    		
    		// 최하위 산출물부터 삭제처리
    		$(chkDocNodeArr).each(function(idx, docNode) {
    				zTree.removeNode(docNode);
    		});
    		
    		//삭제 후 부모노드의 자식 수가 0일 경우 폴더 아이콘으로(부모형) 변경
    		var parentNode = firstDocNode.getParentNode();
    		
    		// 2뎁스 미만, 자식 노드가 없는 경우
    		if( parentNode.level < 2 &&  parentNode.children.length == 0){
    			//부모형 노드로 변경하고, 업데이트
    			parentNode.isParent = true;
    			
    		}else if(parentNode.children.length == 0){
    			// 그외에는 자식형 노드로 변경(문서 아이콘으로 변경)
    			parentNode.isParent = false;
    		}
    		
    		zTree.updateNode(parentNode);
    	}
    	
    	toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
}

//업로드 버튼 클릭
function fnOslDocUploadClick(){
	if(zTree.getSelectedNodes().length == 0){
		toast.push("선택된 개발문서가 없습니다.");
	//사용중인 메뉴인지 확인
	}else if(menuUseCd == '02'){
		jAlert('미 사용중인 개발문서입니다.');
		return false;
	//등록 권한이 있는지 확인
	}else if(typeof btnAuthInsertYn != "undefined" && btnAuthInsertYn == 'Y'){
		var oslUpload_btn = document.getElementById('oslDocFileUpload');
		oslUpload_btn.click();
		fnOslDocUpload(oslUpload_btn);
	}
}

//파일 업로드 버튼 선택 동작
function fnOslDocUpload(ele){
	ele.onchange = function(){
		fnFileAjaxUpload(ele.files[0]);
		ele.value = null;
	};
}

//파일 업로드 AJAX 처리
function fnFileAjaxUpload(files){
	var selZtree = zTree.getSelectedNodes()[0];
	
	if(selZtree == null || selZtree == ""){
		toast.push("선택된 개발문서가 없습니다.");
		return false;
	}
	
	var docId = selZtree.docId;

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
    		
    		//삭제 버튼 onclick 및 atchid 세팅
    		$(upFileObj).siblings("#btn_delete_file").attr("onclick","fnFileItemDelete(this, event)");
    		$(upFileObj).siblings("#btn_delete_file").attr("atchid",data.addFileId);
    		sortableFile();
    	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push("ERROR STATUS("+status+")<br>"+err);
    	innerFail();
 	});
	
	//AJAX 전송file_progressBar
	ajaxObj.send();
	function innerFail(){
		var pgBarObj = $(upFileObj).children('.file_progressBar');
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
		toast.push("선택된 개발문서가 없습니다.");
	}else{
		//AJAX 설정 
		var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3000/selectPrj3000MenuTreeZipDownload.do'/>","loadingShow":true}
			,{docId:selMenu[0].docId });
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			//확정 산출물 갯수 체크
			var fileChk = 0;

			var fileInfo = "";
			$.each(data.docMenuList,function(){
				if(this.docFormFileSn != null && this.docFormFileSn != ""){
					//배열로 세팅
					fileInfo = fileInfo+"downFile="+this.docFormFileId+";"+this.docFormFileSn+";"+this.docNm+"&";
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


/** 
 *	선택된 산출물 및 하위 산출물을 모두 가져오는 함수
 *	@param selectDocOjb 현재 선택된 조직
 */
function fnGetDocList(selectDocOjb){
	
	// 배열에 산출물 노드를 담는다.
	chkDocNodeArr.push(selectDocOjb);
	
	//자식 객체가 있는 경우에만 동작
	if(typeof selectDocOjb.children != "undefined"){
		
		$.each(selectDocOjb.children,function(){
			var subDocObj = this;
			// 재귀
			fnGetDocList(subDocObj);
		});
	}
}


/** 
 *	우측 상세정보 화면을 초기화 시킨다.
 */
function fnFormReset(){

	$('#docMenuInfoForm')[0].reset();
	$('#docEdDtm').val('');
	$('.ok_document').html('');
	$('.documentFileList').html('');
	var beforeFileDiv = $('.ok_document').children('div');
	
	$('.ok_document').html('');
	$('.documentFileList').html('');
	
	//
	//$('#docConfDesc').val('');
}

//가이드 상자
function fnPrj3000GuideShow(){
	var mainObj = $(".main_contents");
	
	//mainObj가 없는경우 false return
	if(mainObj.length == 0){
		return false;
	}
	//guide box setting
	var guideBoxInfo = globals_guideContents["prj3000"];
	gfnGuideBoxDraw(true,mainObj,guideBoxInfo);
}

</script>

<div class="main_contents">
	<div class="doc_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
			<div class="top_control_wrap"><!-- 
				<span style="float:left;">*개발문서 양식을 설정할 수 있습니다.</span>
				<span class="menu_tree_help"><i class="fa fa-question"></i>
					<div class="menu_tree_helpBox">
						<span>
							[좌측 트리 메뉴 기능 안내]<br/>
							<br/>
							&nbsp;-개발문서 클릭: 개발문서 상세 정보 보기<br/>
							&nbsp;-더블 클릭: 폴더형 개발문서의 경우 하위 메뉴 보기<br/>
							&nbsp;<span style="margin-left: 71px;"></span>하위 개발문서의 경우 사용여부 변경<br/>
						</span>
					</div>
				</span>
				 -->
				<div class="button_normal2" id="btn_download_formFileZip" onclick="fnOslDocFormZipDownload()" guide="formFileZip" ><i class='fa fa-file-zip-o' aria-hidden='true'></i>&nbsp;확정 개발문서 양식 전체 다운로드</div>
				<span class="button_normal2 btn_inquery" id="btn_search_menuInfo"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;조회</span>
				<span class="button_normal2 btn_save" id="btn_update_menuInfo" tabindex=5><i class='fa fa-edit' aria-hidden='true'></i>&nbsp;정보 수정</span>
				<div class="button_normal2" onclick="fnOslDocUploadClick()" id="btn_insert_fileSelect" guide="docFileUpload">
					<input type="file" style="display: none" id="oslDocFileUpload" name="oslDocFileUpload" /><i class='fa fa-upload' aria-hidden='true'></i>&nbsp;개발문서 양식 업로드
				</div>
			</div>		
		<div class="menu_wrap">
			<div class="menu_ctrl_wrap">
				<div class="menu_ctrl_btn_wrap" guide="leftMenu">
					<span class="button_normal2 btn_menu_add" id="btn_insert_menuAddInfo"><i class='fa fa-save' aria-hidden='true'></i>&nbsp;추가</span>
					<span class="button_normal2 btn_menu_del" id="btn_delete_menuDeleteInfo"><i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;삭제</span>
					<!-- <span class="button_normal2 btn_menu_reset" id="btn_delete_menuInsertAllDefault">초기화</span> -->
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span>
						<span class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>

				<div class="menu_lists_wrap">
						<ul id="prjDocJson" class="ztree"></ul>
				</div>
			</div>

			<div class="menu_info_wrap">
				<form id="docMenuInfoForm" name="docMenuInfoForm" method="post" enctype="multipart/form-data">
				<input id="lvl" type="hidden" name="lvl" value=""/>
					<div class="left_con_div" style="float:none; padding-bottom: 10px; border-bottom: 1px solid #ddd;" >
						<span class="documentSpanImg"></span><span>선택 개발문서 메뉴 정보</span>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docId">개발문서 ID</label></div>
						<div class="menu_col2"><input id="docId" type="text" name="docId" value="" class="readonly" readonly/></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1 menu_col1_right"><label for="docNm">상위 개발문서 ID</label></div>
							<div class="menu_col2"><input id="upperDocId" type="text" name="upperDocId" value=""class="readonly" readonly /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docNm">개발문서 명</label><span class="required_info">&nbsp;*</span></div>
							<div class="menu_col2"><input id="docNm" type="text" name="docNm" value="" maxlength="150" tabindex=1 /></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1 menu_col1_right"><label for="ord">순번</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2"><input id="ord" name="ord" type="number" min="1" value="" maxlength="15" tabindex=2></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="useCd">사용여부</label><span class="required_info">&nbsp;*</span></div>
						<div class="menu_col2" id="search_useCd">
							<span class="search_select"><select name="useCd" id="useCd" tabindex=3 style="width: 100%;"></select></span>
						</div>
						<div class="menu_col2" id="readonly_useCd" style="display:none;">
							<input id="useNm" type="text" name="useNm" value="" tabindex=1  class="readonly" readonly/>
						</div>
					</div>
					<div class="menu_row">
						<div class="menu_col1 menu_col1_right"><label for="docFormFileId">파일 ID</label></div>
						<div class="menu_col2"><input id="docFormFileId" type="text" name="docFormFileId" value="" tabindex=1  class="readonly" readonly/></div>
					</div>
					<div class="menu_row menu_oneRow">
						<div class="menu_col1 oneRow_col1" ><label for="docFormFileId">개발문서 마감일</label></div>
						<div class="menu_col2 oneRow_col2" ><input id="docEdDtm" type="text" name="docEdDtm" class="calendar_input" readonly="readonly" tabindex=1 style="width: 38%;"/></div>
 					</div>
 					<!-- 
					<div class="menu_row">
						<div class="menu_col1" id="empty_col1"><label></label></div>
					</div> 
					 -->
					<div class="menu_row menu_oneRow" style="margin-bottom:15px;">
						<div class="menu_col1 oneRow_col1" style="height: 80px; font-weight: bold;">개발문서 설명</div>
						<div class="menu_col2 oneRow_col2" style="height: 80px;">
							<textarea title="개발문서 설명" id="docDesc" name="docDesc" value="" tabindex=4 maxlength="500"></textarea>
						</div>
					</div>
					<div id="fileListDiv">
						<div class="left_con_div" style="float:none;">
							<span class="documentSpanImg"></span><span>확정 개발문서 양식</span>
						</div>
						<input type="hidden" name="docFormFileSn" id="docFormFileSn" value=""/>
						<input type="hidden" name="docAtchFileId" id="docAtchFileId" value=""/>
						<input type="hidden" name="docFileSn" id="docFileSn" value=""/>
						<div id="confirmDocFile_guide" guide="confirmDocFile" >
							<div class="ok_document" >
							</div>
						</div>
						
						<div class="left_con_div" style="float:none;">
							<span class="documentSpanImg"></span><span>확정 개발문서 비고</span>
						</div>
						<div class="confirm_document_desc">
							<textarea class="input_note" title="확정 개발문서 비고" id="docConfDesc" name="docConfDesc" value=""></textarea>
						</div>
						
						<div class="left_con_div">
							<span class="documentSpanImg"></span><span>개발문서 양식 업로드 목록 (Drag&Drop 한번에 최대 5개 제한)</span>
						</div>
						<div id="docFileList_guide" guide="docFileList" >
							<div class="documentFileList" id="documentFileList" onclick="fnOslDocUploadClick()" guide="docFileList">
						</div>
						
					</div>
				</form>

			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />