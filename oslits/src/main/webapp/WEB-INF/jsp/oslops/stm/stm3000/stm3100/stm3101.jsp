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
			
	}


	$(document).ready(function() {
		//달력 세팅 (배포일)
		popGrid.init(); // AXISJ Grid 초기화 실행 부분들
		
		/* 취소 */
		$('#btnPopStm3101Cancle').click(function() {
			gfnLayerPopupClose();
		});
		
		$('#btnPopStm3101Select').click(function() {
			fnInSaveGrid(popGrid.list,'${param.prjId}');
			
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
														
							{label:"검색", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
								options:[
									{optionValue:"0", optionText:"전체 보기",optionAll:true},
									{optionValue:'jenNm', optionText:'JOB 명'},
									{optionValue:'jenTxt', optionText:'JOB 설명'}                             
							        
							    ],onChange: function(selectedObject, value){
							    	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
									if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
										axdom("#" + popSearch.getItemId("searchTxt")).attr("readonly", "readonly");	
										axdom("#" + popSearch.getItemId("searchTxt")).val('');	
									}else{
										axdom("#" + popSearch.getItemId("searchTxt")).removeAttr("readonly");
									}
									
									//공통코드 처리 후 select box 세팅이 필요한 경우 사용
									if(!gfnIsNull(selectedObject.optionCommonCode)){
										gfnCommonSetting(popSearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
									}else{
										//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
										axdom("#" + popSearch.getItemId("searchTxt")).show();
										axdom("#" + popSearch.getItemId("searchCd")).hide();
									}
								},
							
							},
							{label:"", labelWidth:"", type:"inputText", width:"120", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
								onkeyup:function(e){
									if(e.keyCode == '13' ){
										axdom("#" + popSearch.getItemId("btn_search_jenkins")).click();
									}
								} 
							},
							{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
								options:[]
							},
							
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_jenkins",valueBoxStyle:"padding-left:0px;padding-right:2px;", value:"<i class='fa fa-list' id='btn_search_api' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
									var pars = popSearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();
							
							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }
								
							    fnInGridPopListSet(ajaxParam);
							    
							    //폼 데이터 변경
								$('#searchSelect').val(axdom("#" + popSearch.getItemId("searchSelect")).val());
								$('#searchCd').val(axdom("#" + popSearch.getItemId("searchCd")).val());
								$('#searchTxt').val(axdom("#" + popSearch.getItemId("searchTxt")).val());
							    
							}}
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			fnPopObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + popSearch.getItemId("searchTxt")).attr("readonly", "readonly");	
			axdom("#" + popSearch.getItemId("searchCd")).hide();
			
			//버튼 권한 확인
			fnBtnAuthCheck(popSearch);
		});
		
	}


	//axisj5 그리드
	function fnAxGridSet(){
				
		popGrid = new ax5.ui.grid();
 
        popGrid.setConfig({
            target: $('[data-ax5grid="pop-grid"]'),
            sortable:false,
            showRowSelector: false,
            //multipleSelect: true ,
            header: {align:"center" },
            //frozenColumnIndex: 2,
            columns: [
                {
                          key: "isChecked", label: "권한", width: 50, sortable: false, align: "center", editor: {
                          type: "checkbox", config: {height: 17, trueValue: "Y", falseValue: "N"}
                      	}
                },
				{key: "jenNm", label: "JOB 명", width: 100, align: "left"},
				{key: "jenTxt", label: "JOB 설명", width: 200, align: "left"},
				{key: "jenUrl", label: "URL", width: 100, align: "left"},
				{key: "useNm", label: "사용여부", width: 100, align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30
            }
        });
        fnInGridPopListSet();
	}
	//그리드 데이터 넣는 함수
	
	function fnInGridPopListSet(ajaxParam){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
         	
		if(gfnIsNull(ajaxParam)){		
			ajaxParam = $('form#searchFrm').serialize();
		}
		
		ajaxParam += "&prjId=${param.prjId}";
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm3000/stm3100/selectStm3100JenkinsProjectAuthListAjax.do'/>","loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			
			
			data = JSON.parse(data);
		
			var list = data.jenList;
			
			var page = data.page;
				
			popGrid.setData( list );
					   	
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
	

	function fnInSaveGrid(objects,prjId){
		
		var params = "";
		params += "&prjId="+prjId;
		for(var i=0; i<objects.length; i++){
			params += "&jenId="+objects[i].jenId;
			params += "&isChecked="+objects[i].isChecked;
			params += "&orgChecked="+objects[i].orgChecked;
			
		}
			

	 	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm3000/stm3100/saveStm3100Ajax.do'/>","loadingShow":true}
				,params);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			toast.push(data.message);
			
			fnInGridListSet(currentNode.prjId);
			gfnLayerPopupClose();
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
	       	toast.push(data.message);
		});
		//AJAX 전송
		ajaxObj.send();
		

	}
</script>

<div class="popup">
		
	<div class="pop_title">JENKINS 허용 설정</div>
	<div class="pop_sub">
		<div class="tab_contents menu" style="max-width:600px;">
			<form:form commandName="jen1100VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			<div id="AXSearchPopTarget" style="border-top:1px solid #ccc;"></div>
			<br />
			<div data-ax5grid="pop-grid" data-ax5grid-config="{}" style="height: 350px;"></div>
		</div>
		<div class="btn_div">
			<div class="button_normal save_btn" id="btnPopStm3101Select" >선택</div>
			<div class="button_normal exit_btn" id="btnPopStm3101Cancle" >취소</div>
		</div>
	</div>
	</div>
	</form>
</div>
</html>