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
	var popGrid  = {
			init : function() {
				//그리드 및 검색 상자 호출
				fnAxGridSet();  // Grid 초기화  설정\
				fnSearchBoxControl(); // Search Grid 초기화 설정
			}
			
	};

	$(document).ready(function() {
		//달력 세팅 (배포일)
		popGrid.init(); // AXISJ Grid 초기화 실행 부분들
		
		/* 취소 */
		$('#btnPopCmm1700Cancle').click(function() {
			gfnLayerPopupClose();
		});
		
		$('#btnPopCmm1700Select').click(function() {
			gfnCheckRow(popGrid);
		});
	});
	

	function fnSearchBoxControl(){
		var pageID = "AXSearchPop";
		popSearch = new AXSearch();

		var fnPopObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				popSearch.setConfig({
					targetID:"AXSearchPopTarget",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
														
							{label:"", labelWidth:"", type:"inputText", width:"120", key:"searchPopTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"${param.param}",
								onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + popSearch.getItemId("btn_searchPop")).click();
									}
								} 
							},
														
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_searchPop",valueBoxStyle:"padding-left:0px;padding-right:2px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
									
								var pars = popSearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();
	
							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }
								
							    fnInGridPopListSet(0,ajaxParam);
								 
							}}
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			fnPopObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			
			
			//버튼 권한 확인
			fnBtnAuthCheck(popSearch);
		});
		
	}


	//axisj5 그리드
	function fnAxGridSet(){
		var isMulti =${param.isMulti};
		
		popGrid = new ax5.ui.grid();
 
        popGrid.setConfig({
            target: $('[data-ax5grid="pop-grid"]'),
            sortable:false,
            showRowSelector: true,
            multipleSelect: isMulti  ,
            header: {align:"center", selector : isMulti  },
            //frozenColumnIndex: 2,
            columns: [
				{key: "authGrpId", label: "역할 그룹 ID", width: 150, align: "left"},
				{key: "authGrpNm", label: "역할 그룹 명", width: 200, align: "left"}                      
				
				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	popGrid.select(this.doindex, {selected: true});
                }
            }
        });
        var ajaxParam = "&searchPopTxt=${param.authGrpNm}";
		
        fnInGridPopListSet(0,ajaxParam);
	}
	//그리드 데이터 넣는 함수
	
	function fnInGridPopListSet(_pageNo,ajaxParam){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
         	
		if(gfnIsNull(ajaxParam)){		
			ajaxParam = $('form#searchFrm').serialize();
			if("" == ajaxParam){
				ajaxParam += "&searchPopTxt=${param.authGrpNm}";
			}
		}
	
     	//페이지 세팅
    	if(!gfnIsNull(_pageNo)){
    		ajaxParam += "&pageNo="+_pageNo;
    	}else if(typeof popGrid.page.currentPage != "undefined"){
    		ajaxParam += "&pageNo="+popGrid.page.currentPage;
    	}

     	
    	var data=gfnSelectCmm1700CommonAuthList(ajaxParam);
		var list = data.list;
	
		var page = data.page;
		if(list.length == 1){
			list[0]["__selected__"]=true;
			popGrid.setData({
             	list:list,
             	page: {
                  currentPage: _pageNo || 0,
                  pageSize: page.pageSize,
                  totalElements: page.totalElements,
                  totalPages: page.totalPages
              }
            });
			gfnCheckRow(popGrid);
				
		}else{
			popGrid.setData({
	           	list:list,
	            	page: {
	                  currentPage: _pageNo || 0,
	                  pageSize: page.pageSize,
	                  totalElements: page.totalElements,
	                  totalPages: page.totalPages
	            }
	        });	
		}				   	
		
	}
	


</script>

<div class="popup">
		
	<div class="pop_title">역할 그룹 조회</div>
	<div class="pop_sub">
		<div class="tab_contents menu" style="max-width:400px;">
			<form:form commandName="adm2000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			<div id="AXSearchPopTarget" style="border-top:1px solid #ccc;"></div>
			<br />
			<div data-ax5grid="pop-grid" data-ax5grid-config="{}" style="height: 200px;"></div>		
		</div>
		<div class="btn_div">
			<div class="button_normal save_btn" id="btnPopCmm1700Select" >선택</div>
			<div class="button_normal exit_btn" id="btnPopCmm1700Cancle" >취소</div>
		</div>	
	</div>
	</form>
</div>
</html>