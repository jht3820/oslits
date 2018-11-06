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

	var popSearch;
	var projectPopGrid  = {
			init : function() {
				//그리드 및 검색 상자 호출
				
				fnAxProjectPopGrid5View();  // Grid 초기화  설정\
				
				fnSearchProjectPopBoxControl(); // Search Grid 초기화 설정
			}
			
	}


	$(document).ready(function() {
		//달력 세팅 (배포일)
		projectPopGrid.init(); // AXISJ Grid 초기화 실행 부분들
		
		/* 취소 */
		$('#btnPopCmm1100Cancle').click(function() {
			gfnLayerPopupClose();
		});
		
		$('#btnPopCmm1100Select').click(function() {
			gfnCheckRow(projectPopGrid);
		});
	});
	


	//axisj5 그리드
	function fnAxProjectPopGrid5View(){
		projectPopGrid = new ax5.ui.grid();
	 
	        projectPopGrid.setConfig({
	            target: $('[data-ax5grid="project-pop-grid"]'),
	            sortable:true,
	            showRowSelector: true,
	            multipleSelect: false  ,
	            header: {align:"center", selector : false  },
	     
	            columns: [
					{key: "upPrjNm", label: "프로젝트 그룹 명", width: 120, align: "center"},
					{key: "prjId", label: "프로젝트 ID", width: 120, align: "center"},
		            {key: "prjNm", label: "프로젝트 명", width: 250, align: "center"},	 
		            {key: "startDt", label: "프로젝트 시작 기간", width: 130, align: "center"},
		            {key: "endDt", label: "프로젝트 종료 기간", width: 130, align: "center"}
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30,
	                onClick: function () {
	                	projectPopGrid.select(this.doindex, {selected: true});
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
	                    {type: "rowFrozen", label: "열 고정", icon:"<i class='fa fa-lock' aria-hidden='true'></i>"}
	                ],
	                popupFilter: function (item, param) {
	                	var selItem = projectPopGrid.list[param.doindex];
	                	//선택 개체 없는 경우 중지
	                	if(typeof selItem == "undefined"){
	                		return false;
	                	}
	                	return true;
	                },
	                onClick: function (item, param) {
	                    if(item.type == "rowFrozen"){
	                    	//이미 해당 열에 고정인경우 중지
	                    	if(projectPopGrid.config.frozenColumnIndex != (param.colIndex+1)){
	                    		projectPopGrid.setConfig({frozenColumnIndex:param.colIndex+1});
	                    		fnInGridListSet();
	                    	}
	                    }
	                    projectPopGrid.contextMenu.close();
	                }
	            }
	        });
	        //그리드 데이터 불러오기
	 		fnInProjectPopGridListSet();

	}
	//그리드 데이터 넣는 함수
	function fnInProjectPopGridListSet(_pageNo,ajaxParam){
	     	/* 그리드 데이터 가져오기 */
	     	//파라미터 세팅
	     	if(gfnIsNull(ajaxParam)){
	   			ajaxParam = $('form#searchFrm').serialize();
	   		}
	     	//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/cmm/cmm1000/cmm1100/selectCmm1100ViewAjax.do'/>","loadingShow":true}
					,ajaxParam);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				var list = data.list;
			   	projectPopGrid.setData(list);
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
	//검색 상자
	function fnSearchProjectPopBoxControl(){
		var pageID = "AXSearch";
		projectPopSearch = new AXSearch();

		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				projectPopSearch.setConfig({
					targetID:"AXSearchProjectPopTarget",
					theme : "AXSearch",
					
					rows:[
						{display:true, addClass:"", style:"", list:[
														
							
							{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"selectSearch", addClass:"", valueBoxStyle:"", value:"all",
								options:[
	                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
	                                {optionValue:"prjId", optionText:"프로젝트 ID"},
	                                {optionValue:"prjNm", optionText:"프로젝트 명"}
	                                /* {optionValue:"ord", optionText:"정렬 순서"}, */
	                            ],onChange: function(selectedObject, value){
	                            	
	                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
	    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
										axdom("#" + projectPopSearch.getItemId("txtSearch")).attr("readonly", "readonly");	
										axdom("#" + projectPopSearch.getItemId("txtSearch")).val('');	
									}else{
										axdom("#" + projectPopSearch.getItemId("txtSearch")).removeAttr("readonly");
									}
											
									//공통코드 처리 후 select box 세팅이 필요한 경우 사용
									if(!gfnIsNull(selectedObject.optionCommonCode)){
										gfnCommonSetting(projectPopSearch,selectedObject.optionCommonCode,"search_useCd","txtSearch");
									}else{
										axdom("#" + projectPopSearch.getItemId("txtSearch")).show();
										axdom("#" + projectPopSearch.getItemId("search_useCd")).hide();
									}
								}
							},
							{label:"", labelWidth:"", type:"inputText", width:"150", key:"txtSearch", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
								onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + projectPopSearch.getItemId("btn_search")).click();
									}
							}},
							{label:"", labelWidth:"", type:"selectBox", width:"100", key:"search_useCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",options:[]},
							{label:"", labelWidth:"", type:"button", width:"60", key:"btn_search",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",

							onclick:function() {
								/* 검색 조건 설정 후 reload */
	 							var pars = projectPopSearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();

							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }
								
							    fnInProjectPopGridListSet(0,ajaxParam);
					            
					            //폼 데이터 변경
								$('#searchSelect').val(axdom("#" + projectPopSearch.getItemId("searchSelect")).val());
								$('#searchCd').val(axdom("#" + projectPopSearch.getItemId("searchCd")).val());
								$('#searchTxt').val(axdom("#" + projectPopSearch.getItemId("searchTxt")).val());
							    
							}}
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			fnObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + projectPopSearch.getItemId("txtSearch")).attr("readonly", "readonly");
			
			//사용 유무 hide() 처리
			axdom("#" + projectPopSearch.getItemId("search_useCd")).hide();
			
			//버튼 권한 확인
			fnBtnAuthCheck(projectPopSearch);
			
		});
	}


</script>

<div class="popup">
		
	<div class="pop_title">프로젝트 목록</div>
	<div class="pop_sub">
		<div class="tab_contents menu" style="max-width:800px;">
			<form:form commandName="prj1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			<div id="AXSearchProjectPopTarget" style="border-top:1px solid #ccc;"></div>
			<br />
			<div data-ax5grid="project-pop-grid" data-ax5grid-config="{}" style="height: 400px;"></div>		
		</div>
		<div class="btn_div">
			<div class="button_normal save_btn"  id="btnPopCmm1100Select" >선택</div>
			<div class="button_normal exit_btn" id="btnPopCmm1100Cancle" >취소</div>
		</div>
	</div>
	</form>
</div>
</html>