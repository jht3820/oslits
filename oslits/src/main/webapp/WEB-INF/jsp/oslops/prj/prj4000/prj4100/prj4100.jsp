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
#btn_download_fileZip{display:none;}
</style>
<script>
//파일 Sn 변수 불러오기
var fileSnVar = 0;

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
	
	//초기 메뉴 세팅
	fnSearchMenuList();
	
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
				{"url":"<c:url value='/prj/prj3000/prj3100/selectPrj3100MenuInfoAjax.do'/>","loadingShow":false}
				,{ docId:docId,docCommonCd:"02" });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
        	
        	//디테일폼 세팅
        	gfnSetData2Form(data, "docMenuInfoForm");
        	
        	$('#btn_download_formFile').attr('atchId',data.docFormFileId);
        	$('#btn_download_formFile').attr('fileSn',data.docFormFileSn);
        	
        	$('.ok_document').html('');
        	$('.documentFileList').html('');
        	var beforeFileDiv = $('.ok_document').children('div');
        	
        	if(data.fileList){
        		//첨부파일 리스트
	        	$.each(data.fileList,function(idx,map){
	        		//확정 산출물 양식
	        		if(map.fileSn == data.docFileSn){
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
        	//최하위 뎁스가 아니라면
        	if(data.lvl != 3){
        		$('#btn_insert_fileSelect').hide();
        		$('#fileListDiv').hide();
        		$('#btn_download_formFile').hide();
        		$('#btn_download_fileZip').css({display:"inline-block"});
        	}else{
        		btnAuthInsertYn == 'Y' ? $("[id^=btn_insert]").show() : $("[id^=btn_insert]").hide();
        		$('#fileListDiv').show();
        		$('#btn_download_formFile').show();
        		$('#btn_download_fileZip').hide();
        	}
        	
        	//파일 Sn
        	fileSnVar = data.fileSn;
		});

		//AJAX 전송
		ajaxObj.send();
	}
/**
 * 조회버튼 클릭시 메뉴 리스트 조회 AJAX
 */
function fnSearchMenuList(){

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3100/selectPrj3100MenuListAjax.do'/>","loadingShow":false}
			,{docCommonCd:"02" });
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	$('#docMenuInfoForm')[0].reset();
    	$('.ok_document').html('');
    	$('.documentFileList').html('');
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
			view: {
				showIcon : function(treeId, treeNode) {
					
					if(typeof zTree != "undefined" && treeNode.level != 3 && !treeNode.isParent){
						treeNode.isParent = true;
						zTree.refresh();
						
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
			{"url":"<c:url value='/prj/prj3000/prj3100/updatePrj3100FileSnAjax.do'/>"}
			,{ atchId:$(obj).attr("atchId"), docFileSn:$(obj).attr("fileSn"), docId:$('#docId').val(),docCommonCd:"02"});
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

//산출물 확정 제거
function fnFileSelectDelete(){
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/prj/prj3000/prj3100/updatePrj3100FileSnAjax.do'/>"}
			,{ docFileSn:null, docId:$('#docId').val(),docCommonCd:"02" });
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
//업로드 버튼 클릭
function fnOslDocUploadClick(){
	if($('#docId').val() == null || $('#docId').val() == ""){
		toast.push("선택된 산출물 메뉴가 없습니다.");
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
			{"url":"<c:url value='/prj/prj3000/prj3100/insertPrj3100FileUploadAjax.do'/>"
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
    		pgBarObj.html("실패");
    		pgBarObj.css('background-color','#FE5454');
    	});
	}
	
	//파일 Sn +1
	fileSnVar++;
}
/********************************************************************
* 메뉴 관리 기능 부분 정의 종료												*
*********************************************************************/

//확정 산출물 전체 다운로드
function fnOslDocZipDownload(){
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
				if(this.docFileSn != null && this.docFileSn != ""){
					//배열로 세팅
					fileInfo = fileInfo+"downFile="+this.docAtchFileId+";"+this.docFileSn+"&";
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
				<span style="float:left;">*품질 산출물 파일을 관리합니다.</span>
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
				<span class="button_normal2" id="btn_download_fileZip" onclick="fnOslDocZipDownload()"><i class='fa fa-file-zip-o' aria-hidden='true'></i>&nbsp;확정 산출물 전체 다운로드</span>
				<span class="button_normal2 btn_inquery" id="btn_search_menuInfo"><i class='fa fa-list' aria-hidden='true'></i>&nbsp;조회</span>
				<span class="button_normal2 btn_menu_form_down" id="btn_download_formFile" onclick='gfnFileDownload(this,true)' atchId="" fileSn=""><i class='fa fa-download' aria-hidden='true'></i>&nbsp;양식 다운로드</span>
					<span class="button_normal2" onclick="fnOslDocUploadClick()" id="btn_insert_fileSelect">
					<input type="file" style="display: none" id="oslDocFileUpload" name="oslDocFileUpload"/><i class='fa fa-upload' aria-hidden='true'></i>&nbsp;산출물 업로드
				</span>
			</div>		
		<div class="menu_wrap">
			<div class="menu_ctrl_wrap">
				<div class="menu_ctrl_btn_wrap">
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span><span
							class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>
				<ul id="prjDocJson" class="ztree"></ul>
			</div>

			<div class="menu_info_wrap">
				<form id="docMenuInfoForm" name="docMenuInfoForm" method="post" enctype="multipart/form-data">
				<input type="hidden" id="docCommonCd" name="docCommonCd" value="02"/>
				<input id="lvl" type="hidden" name="lvl" value=""/>
					<div class="left_con_div" style="float:none; padding-bottom: 10px; border-bottom: 1px solid #ddd;" >
						<span class="documentSpanImg"></span><span>선택 산출물 메뉴 정보</span>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docId">산출물 ID</label></div>
						<div class="menu_col2"><input id="docId" type="text" name="docId" value="" class="readonly" readonly/></div>
					</div>
					<div class="menu_row">
						<div class="menu_col1"><label for="docNm">산출물명</label></div>
							<div class="menu_col2"><input id="docNm" type="text" name="docNm" value="" class="readonly" readonly /></div>
					</div>
					<div class="menu_row" style="margin-bottom:10px;height:70px;width: 100%;">
						<div class="menu_col1" style="height: 70px;width: 99.38px;">산출물 설명</div>
						<div class="menu_col2" style="height: 70px;">
							<textarea title="산출물 설명" id="docDesc" name="docDesc" value="" disabled="disabled"></textarea>
						</div>
					</div>
					<div id="fileListDiv">
						<div class="left_con_div" style="float:none;">
							<span class="documentSpanImg"></span><span>확정 산출물 목록</span>
						</div>
						<input type="hidden" name="docFormFileId" id="docFormFileId" value=""/>
						<input type="hidden" name="docFormFileSn" id="docFormFileSn" value=""/>
						<input type="hidden" name="docAtchFileId" id="docAtchFileId" value=""/>
						<input type="hidden" name="upperDocId" id="upperDocId" value=""/>
						<input type="hidden" name="docFileSn" id="docFileSn" value=""/>
						<input type="hidden" name="ord" id="ord" value="">
						<div class="ok_document">
	
						</div>
						<div class="left_con_div">
							<span class="documentSpanImg"></span><span>산출물 업로드 목록 (Drag&Drop 한번에 최대 5개 제한)</span>
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