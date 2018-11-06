<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<html lang="ko">
<title>OpenSoftLab</title>
<head>
	
</head>
<style type="text/css">
.svn_contents.menu {
    width: 930px;
    height: 330px;
    padding: 10px;
    background: #fff;
    border: 1px solid #ccc;
    position: relative;
    float: left;
    max-width: 940px;
    margin: 5px;
}
.svn_menu_wrap {
    width: 100%;
    margin-top: 10px;
    margin-left: 20px;
    border: 1px solid #ccc;
    float: left;
}
.svn_mask_content {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.7);
    z-index: 2;
    color: #fff;
    font-size: 10pt;
    text-align: center;
    padding-top: 120px;
    font-weight: bold;
    display:none;
}
.repListDiv {
    width: 290px;
    height: 330px;
    display: inline-block;
    float: left;
    border-radius: 5px;
    margin: 5px 5px 0 5px;
}
.svn_bottom_frame {
    float: left;
    display: inline-block;
    width: 100%;
    height: 100%;
    position: relative;
}
.menu_ctrl_btn_wrap {
    padding: 0 10px;
    height: 48px;
    line-height: 42px;
}
.menu_lists_wrap {
    padding: 12px 0;
}
#revisionFileList{display:block;}
</style>
<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>

<script>
	//초기화
	var myRevisionSearch;
	var repositoryGrid;
	var zTree;
	var treeList ;
	var isSelValid = false;
	var currentRevision = 0;
	var lastRevision=0;
	var selList = [];
	var selBtnChk = "${param.selBtnChk}";
	
	var PAGE_VIEW_SIZE = 50;
				
	$(document).ready(function(){
		// 초기화
		fnAxSVNRepositoryGridView();
		fnSearchRevisionBoxControl();
		fnAxSVNRevisionPopGridView(); 
   		fnFileView();
		//Grid.init(); // AXISJ Grid 초기화 실행 부분들
		// 이벤트
		$(".menu_expand_all").click(function(){
			zTree.expandAll(true);
		});
		
		// 메뉴 관리 전체 닫기
		$(".menu_collapse_all").click(function(){
			zTree.expandAll(false);
		});
		
		/* 닫기버튼 클릭 시 팝업 창 사라지기*/
		$('.exit_btn').click(function() {
			gfnLayerPopupClose();
		});
					
	});
	
	function verifyNumber(objects){
		var sNum="";
		for(var i=0;  i<objects.length; i++){
			sNum=$(objects[i]).val();
			if(isNaN(sNum)){
				toast.push("입력란에 숫자만 입력가능합니다");
				$(objects[i]).val(0);
				$(objects[i]).focus();
				return false;
			}else if(parseInt(sNum)<0){
				toast.push("0 보다 큰 숫자만 입력가능합니다");
				$(objects[i]).val(0);
				$(objects[i]).focus();
				return false;
			}		
		}
		return true;
	}

	function fnAxSVNRepositoryGridView(){
		repositoryGrid = new ax5.ui.grid();
 
		repositoryGrid.setConfig({
            target: $('[data-ax5grid="repository-grid"]'),
            sortable:false,
            showRowSelector: true,
            multipleSelect: false  ,
            header: {align:"center", selector : false  },
            columns: [
				{key: "svnRepNm", label: "REPOSITORY", width: 100, align: "left"},
				{key: "result", label: "접속가능결과", width: 200, align: "left"}
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
					//시작 리비전 번호 초기화
                	axdom("#" + myRevisionSearch.getItemId("startRevision")).val(0);
                	
                	var selItem = repositoryGrid.list[this.doindex];
                	
                	$('#svnRepId').val(selItem.svnRepId);
                	fnSearchLastRevision(selItem.svnRepId,this.doindex);
                	fnInRevisionPopGridListSet();
                	repositoryGrid.select(this.doindex, {selected: true});
                	
                	
                }
            }
        });
		fnInSVNRepositoryGridListSet('${param.prjId}');
	}
	
	function fnInSVNRepositoryGridListSet(prjId){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	     	
     	     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm1000/cmm1400/selectCmm1400SVNRepositoryList.do'/>","loadingShow":true}
				,{ "prjId" : prjId });
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			var list = data.list;
			
		
			repositoryGrid.setData(list);
		   	   	
		   	if(list.length > 0){
		   		$('#svnRepId').val(list[0].svnRepId);
            	fnSearchLastRevision(list[0].svnRepId,0);
            	fnInRevisionPopGridListSet();
            	repositoryGrid.select(0, {selected: true});
		   	}
		   			   	
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
	
	//axisj5 그리드
	
	function fnAxSVNRevisionPopGridView(){
		revisionGrid = new ax5.ui.grid();
 
        revisionGrid.setConfig({
            target: $('[data-ax5grid="first-svnpop-grid"]'),
            sortable:false,
            showRowSelector: false,
            multipleSelect: false  ,
            header: {align:"center", selector : false  },
            frozenColumnIndex: 2,
            columns: [
                {
                          key: "isChecked", label: "선택", width: 50, sortable: false, align: "center", editor: {
                          type: "checkbox", config: {height: 17, trueValue: "Y", falseValue: "N"}
                      	}
                },
				{key: "revision", label: "Revision", width: 100, align: "right"},
				{key: "author", label: "Author", width: 150, align: "left"},
				{key: "sDate", label: "Date", width: 300, align: "center"},
				{key: "comment", label: "Comment", width: 900, align: "left"}
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	currentRevision = this.item.revision;  
                	fnSearchFileDirTree(currentRevision);
                	secondGrid.setData([]);
                	//revisionGrid.select(this.doindex, {selected: true});
                },
                onDataChanged: function(){
                	var selectRowData = this.item;
                	selList.push(selectRowData);
                	
                }
            }
        });
   	        
 		
	}
	//그리드 데이터 넣는 함수
	function fnInRevisionPopGridListSet(){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
   		var data = {"startRevision" : axdom("#" + myRevisionSearch.getItemId("startRevision")).val() ,"lastRevision": axdom("#" + myRevisionSearch.getItemId("lastRevision")).val()
    			, "svnRepId" : $("#svnRepId").val()}; 
   		gfnShowLoadingBar(true);
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm1000/cmm1400/selectCmm1400RevisionAjaxList.do'/>","loadingShow":false}
				,data);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			if(data.MSG_CD =="SVN_OK"){
				var list = data.list;
				
				for(var i=0; i<list.length; i++){
					list[i]["isChecked"]="N";
					list[i]["svnRepId"]=$("#svnRepId").val();
					for(var j=0; j<selList.length; j++){
						if(list[i].svnRepId == selList[j].svnRepId &&
								list[i].revision == selList[j].revision	&&
								selList[j].isChecked == "Y"
						){
							list[i].isChecked="Y";
						}
					}
				}
			   	revisionGrid.setData(list);
			   	   	
			   	if(list.length > 0){
			   		currentRevision= list[0].revision;
			   		//fnSearchFileDirTree(list[0].revision);
			   		//fnFileView();
			   		//revisionGrid.select(0, {selected: true});
			   	}
			   	
			   	$("#revisionList").hide();
	    	}else if(data.MSG_CD =="SVN_AUTHENTICATION_EXCEPTION"){
	    		$("#revisionList, #revisionFileList").show();
	    		
	    	}else{
	    		$("#revisionList, #revisionFileList").show();
	    	}
		   	gfnShowLoadingBar(false);			   	
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			//세션이 만료된 경우 로그인 페이지로 이동
			gfnShowLoadingBar(false);
           	if(status == "999"){
           		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
        		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
        		return;
           	}
		});
		
		//AJAX 전송
		ajaxObj.send();
	}
	
	//axisj5 그리드
	function fnFileView(){
		secondGrid = new ax5.ui.grid();
 
		secondGrid.setConfig({
            target: $('[data-ax5grid="second-svnpop-grid"]'),
            sortable:false,
            header: {align:"center"},
            frozenColumnIndex: 2,
            columns: [
				{key: "type", label: "type", width: 100, align: "left"},
				{key: "name", label: "name", width: 300, align: "left"},
				{key: "path", label: "path", width: 600, align: "left"}
				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onDBLClick:function(){
                	                	
                	var data = {"revision" : currentRevision ,"path": this.item.path,"name": this.item.name
                			, "svnRepId" : $("#svnRepId").val()}; 
					gfnLayerPopupOpen("/cmm/cmm1000/cmm1400/selectCmm1401FileContentView.do", data, "1200", "780",'auto');
                }
            }
           
        });
	}
	
	//검색 상자
	function fnSearchRevisionBoxControl(){
		var pageID = "AXSearch";
		myRevisionSearch = new AXSearch();

		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				myRevisionSearch.setConfig({
					targetID:"AXSearchSVNPopTarget",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
			
							{label:"&nbsp;&nbsp;<i class='fa fa-arrows-v'></i>&nbsp;시작 리비젼번호&nbsp;&nbsp;", labelWidth:"", type:"inputText", width:"150", key:"startRevision", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:5px;", value:"0"
							,	onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + myRevisionSearch.getItemId("btn_search_svn")).click();
									}
								} 
							},
							{label:"&nbsp;&nbsp;<i class='fa fa-arrows-v'></i>&nbsp;종료 리비젼번호&nbsp;&nbsp;", labelWidth:"", type:"inputText", width:"150", key:"lastRevision", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:5px;", value:"0"
								,	onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + myRevisionSearch.getItemId("btn_search_svn")).click();
									}
								}
							},							
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_svn",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
								onclick:function(){					
								/* 검색 조건 설정 후 reload */
									if(isSelValid){
										if(verifyNumber([ axdom("#" + myRevisionSearch.getItemId("startRevision")) ,axdom("#" + myRevisionSearch.getItemId("lastRevision") )  ])){
									    	if(lastRevision < parseInt( axdom("#" + myRevisionSearch.getItemId("startRevision") ).val())){
									    		axdom("#" +myRevisionSearch.getItemId("startRevision") ).val(0);
									    		alert('시작 리비젼이 리비젼 범위보다 큽니다. 마지막 리비젼번호 : '+lastRevision);
									    		axdom("#" +myRevisionSearch.getItemId("startRevision") ).focus();
									    	}else if(lastRevision < parseInt( axdom("#" + myRevisionSearch.getItemId("lastRevision") ).val())){
									    		axdom("#" +myRevisionSearch.getItemId("lastRevision") ).val(lastRevision);
									    		alert('종료 리비젼이 리비젼 범위보다 큽니다. 마지막 리비젼번호 : '+lastRevision);
									    		axdom("#" +myRevisionSearch.getItemId("lastRevision") ).focus();
									    	}else if( parseInt( axdom("#" + myRevisionSearch.getItemId("startRevision")).val() ) 
									    			>
									    		parseInt( axdom("#" + myRevisionSearch.getItemId("lastRevision") ).val() )
									    	){
									    		alert('시작 리비전이 종료 리비젼보다 큽니다.');
									    	}else{
									    		selList=[];
									    		fnInRevisionPopGridListSet();	
									    	}
												
									    }  
									}else{
										alert('SVN 설정 정보를 확인하세요');
									}    
								      					           
								}
							},							
							 {label:"", labelWidth:"", type:"button", width:"55", key:"btn_select_svn",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>선택</span>",
								onclick:function(){	
									var selGrid =[];
									for(var i=0; i<selList.length; i++){
										if(selList[i].isChecked=="Y"){
											selGrid.push(selList[i]);
										}
									}
									
									gfnDataRow(selGrid);		            					           
								}
							} 
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			fnObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			
			//선택 버튼 표시 유무
			if(!gfnIsNull(selBtnChk) && selBtnChk == "false"){
				axdom("#" + myRevisionSearch.getItemId("btn_select_svn")).hide();
			}
			
			//버튼 권한 확인
			fnBtnAuthCheck(myRevisionSearch);
		});
		
	}
	
	function fnSearchFileDirTree(revisionIndex){
		//AJAX 설정
		
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm1000/cmm1400/selectCmm1400FileDirAjaxList.do'/>","loadingShow":false}
				,{"revision" : revisionIndex , "svnRepId" : $('#svnRepId').val() 
				});
		
		
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			
			data = JSON.parse(data);
	    
			//오류
			if(data.errorYn == "Y"){
				$("#revisionFileList").html(data.consoleText);
				$("#revisionFileList").show();
			}else{
		    	$('.ok_document').html('');
		    	var beforeFileDiv = $('.ok_document').children('div');    	
		    	$('.ok_document').html('');
		    	
		    	toast.push(data.message);
		    	
		    	// zTree 설정 
			    var setting = {
			        data: {
			        	key: {
							name: "name"
						},
			            simpleData: {
			                enable: true,
			                idKey: "currentKey",
							pIdKey: "parentKey",
							rootPId: "Root"
			            }
			        },
					callback: {
						onClick: function(event, treeId, treeNode){
							//우측 메뉴 정보
							getSVNRevisionFileList(treeNode,treeList);
						}
					},
					view : {
						fontCss: function(treeId, treeNode){
							return {};
						},
						showIcon : function(treeId, treeNode) {
							if(typeof zTree != "undefined" && treeNode.level != 3 && !treeNode.isParent){
								treeNode.isParent = true;
								zTree.refresh();	
							}
							return true;
						}
					}
			    };
		    	
			    treeList = data.baseDocList;
			    var dirList = getDirectory(treeList);
			    // zTree 초기화
			    zTree = $.fn.zTree.init($("#fileDirJson"), setting, dirList);
			    
			    if(dirList.length >0){
			    	//리비전 파일목록 mask hide
			    	$("#revisionFileList").hide();
			    	getSVNRevisionFileList(null,treeList);
			    	zTree.expandAll(true);
			    }else{
			    	//리비전 파일목록 mask show
			    	$("#revisionFileList").show();
			    }
			    
			  //폴더의 계층구조가 3단계가 아니면  tree전체 펼침 시에 일회적 동작 안함(좋은방법같진않고 임시방편 추후개선)
				//zTree.expandAll(false);
			}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
	 	});
		//AJAX 전송
		ajaxObj.send();
	}
	
	function getDirectory(dataList){
		var returnList = [];

		$(dataList).each(function(index, item){
			if(item.kind=="dir"){
				returnList.push(item);
			}	
		})
		
		return returnList;
	}
	
		
	function getSVNRevisionFileList(node,dataList){
		var returnList = [];
		
		if(node == null){
			$(dataList).each(function(index, item){
				if(item.kind=="file"){
					returnList.push(item);	
				}	
			})
		}else{
			var selectKey = node.currentKey;
			$(dataList).each(function(index, item){
				if(item.kind=="file"){
					if(item.currentKey.indexOf(selectKey)==0){
						returnList.push(item);	
					}				
				}	
			})
		}
		
		secondGrid.setData(returnList);
		return returnList;
	}
	
	
	function fnSearchLastRevision(svnRepId,index){
		//AJAX 설정
		
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm1000/cmm1400/selectCmm1400LastRevisionAjax.do'/>","loadingShow":true}
				,{"svnRepId" : svnRepId });
		ajaxObj.async = false;
		//AJAX 전송 성공 함수
		lastRevision = 0;
		var startRevision = 0;
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			$("#revisionFileList").html("리비전을 선택해주세요.");
	    	if(data.MSG_CD =="SVN_OK"){
	    		lastRevision = Number(data.lastRevision);
	    		startRevision = lastRevision < PAGE_VIEW_SIZE ? 0 : lastRevision - PAGE_VIEW_SIZE;  	
	    		axdom("#" + myRevisionSearch.getItemId("lastRevision")).val(data.lastRevision);
	    		axdom("#" + myRevisionSearch.getItemId("startRevision")).val(startRevision);
	    		repositoryGrid.setValue(index, "result", "접속성공");
	    		isSelValid = true;
	    	}else if(data.MSG_CD =="SVN_AUTHENTICATION_EXCEPTION"){
	    		$("#revisionList, #revisionFileList").show();
	    		repositoryGrid.setValue(index, "result", "접속실패 SVN 접속오류");
	    		fnInit();
	    		isSelValid = false;
	    	}else if(data.MSG_CD =="SVN_EXCEPTION"){
	    		$("#revisionList, #revisionFileList").show();
	    		repositoryGrid.setValue(index, "result", "접속실패 SVN 사용자 권한없음");
	    		fnInit();
	    		isSelValid = false;
	    	}
	    	 	
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
	 	});
		//AJAX 전송
		ajaxObj.send();
	}
	
	function fnInit(){
		revisionGrid.setData([]);
		zTree = $.fn.zTree.init($("#fileDirJson"), {}, []);
		secondGrid.setData([]);
		axdom("#" + myRevisionSearch.getItemId("lastRevision")).val(0);
	}
	
</script>

<div class="popup">
	<div class="pop_title">
		SVN 리비젼 선택
	</div>

	<div class="svn_menu_wrap" style="max-width:1250px;" >
		<div class="repListDiv">
			<div data-ax5grid="repository-grid" data-ax5grid-config="{}" style="height: 330px;width:280px;"></div>
		</div>
		<div class="svn_contents menu">
			<div class="svn_mask_content" id="revisionList">리비전 목록 접근 권한이 없습니다.</div>
			<form:form commandName="svn1000VO" id="searchPopFrm" name="searchPopFrm" method="post" onsubmit="return false">
				<input type="hidden" id="svnRepId" name="svnRepId" value="" >
			</form:form>
			<div id="AXSearchSVNPopTarget" style="border-top:1px solid #ccc;"></div>
			<br>
			<div data-ax5grid="first-svnpop-grid" data-ax5grid-config="{}" style="height: 250px;"></div>						
		</div>
		
		<div class="svn_bottom_frame">
			<div class="svn_mask_content" id="revisionFileList">리비전을 선택해주세요.</div>
			<div class="menu_ctrl_wrap" style="width:280px;height:350px;min-height:250px;margin:5px;border-right:none;margin-right: 15px;float: left;" >
				<div class="menu_ctrl_btn_wrap" style="border: 1px solid #ccc;" >
					<div class="menu_all_wrap">
						<span class="menu_expand_all" title="전체 열기"></span>
						<span class="menu_collapse_all" title="전체 닫기"></span>
					</div>
				</div>
	
				<div class=" menu_lists_wrap" style="border: 1px solid #ccc;overflow-y:scroll;height: 300px;border-top:none" >
						<ul id="fileDirJson" class="ztree"></ul>
				</div>
			</div>	
			<div class="svn_contents menu" style="height:350px;">
				<div data-ax5grid="second-svnpop-grid" data-ax5grid-config="{}" style="height: 330px;"></div>
			</div>
		</div>
	</div>
		
	<div class="pop_sub">
		<div class="btn_div">
			<div class="button_normal exit_btn" id="btn_cancle_popup">닫기</div>
		</div>
	</div>
</div>
</html>