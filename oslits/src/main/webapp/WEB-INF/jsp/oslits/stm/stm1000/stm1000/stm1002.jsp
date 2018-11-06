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

	var restfulPopSearch;
	var restfulPopGrid  = {
			init : function() {
				//그리드 및 검색 상자 호출
				
				fnAxGridSet();  // Grid 초기화  설정\
				
				fnSearchBoxControl(); // Search Grid 초기화 설정
			}
			
	}


	$(document).ready(function() {
		//달력 세팅 (배포일)
		restfulPopGrid.init(); // AXISJ Grid 초기화 실행 부분들

		$('#btnPopApiListCancle').click(function() {
			gfnLayerPopupClose();
		});
		
		$('#btnPopApiListSelect').click(function() {
			
			var chkList = restfulPopGrid.getList('selected');
			
			$(chkList).each(function(idx, data) {
				if(data.__selected__){
					
					$('#apiUrl').val(data.url);
				}
			});
			
			
			gfnLayerPopupClose();
		});
	});
	

	function fnSearchBoxControl(){
		var pageID = "AXSearchPop";
		restfulPopSearch = new AXSearch();

		var fnPopObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				restfulPopSearch.setConfig({
					targetID:"AXSearchPopTarget",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
														
					
														
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_searchPop",valueBoxStyle:"padding-left:0px;padding-right:2px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
									
								var pars = restfulPopSearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();
	
							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }
								
							    fnInGridPopListSet(ajaxParam);
								 
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
			fnBtnAuthCheck(restfulPopSearch);
		});
		
	}


	//axisj5 그리드
	function fnAxGridSet(){
		var isMulti =false;
		
		restfulPopGrid = new ax5.ui.grid();
 
        restfulPopGrid.setConfig({
            target: $('[data-ax5grid="pop-grid"]'),
            sortable:false,
            showRowSelector: true,
            multipleSelect: isMulti  ,
            header: {align:"center", selector : isMulti  },
            //frozenColumnIndex: 2,
            columns: [
				{key: "url", label: "서비스 주소(URL)", width: 500, align: "left"}				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	restfulPopGrid.select(this.doindex, {selected: true});
                }
            }
        });
        var ajaxParam = "";
		
        fnInGridPopListSet(ajaxParam);
	}
	//그리드 데이터 넣는 함수
	
	function fnInGridPopListSet(ajaxParam){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
         	
		if(gfnIsNull(ajaxParam)){		
			ajaxParam = $('form#searchFrm').serialize();
		}
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm1000/stm1000/selectStm1000ValidRestfulApiListAjax.do'/>","loadingShow":false}
				,ajaxParam);
     	
    	//AJAX 전송 성공 함수
    
    	ajaxObj.setFnSuccess(function(data){
    		data = JSON.parse(data);
    		
    		var list = data.urlList;
    		
    		
    		restfulPopGrid.setData( list );
                
    	
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
	


</script>

<div class="popup">
		
	<div class="pop_title">Restful API 조회</div>
	<div class="pop_sub">
		<div class="tab_contents menu" style="max-width:520px;">
			<form:form commandName="adm2000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			<div id="AXSearchPopTarget" style="border-top:1px solid #ccc;"></div>
			<br />
			<div data-ax5grid="pop-grid" data-ax5grid-config="{}" style="height: 200px;"></div>		
		</div>
		<div class="btn_div">
			<div class="button_normal save_btn" id="btnPopApiListSelect" >선택</div>
			<div class="button_normal exit_btn" id="btnPopApiListCancle" >취소</div>
		</div>
	</div>
	</form>
</div>
</html>