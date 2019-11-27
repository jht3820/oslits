<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
<style type="text/css">
	.accptFont{color:#4b73eb !important;text-shadow: none !important;}
	.rejectFont{color:#eb4b6a !important;text-shadow: none !important;}
	.defaultFont{color:#000 !important;}
</style>
<script>
var mySearch;


$(function(){
	//타겟이 상세정보 창이 아닌 경우 상세 정보 창 닫기
	$(document).click(function(event){
		//타겟이 상세정보 창, 그리드 노드가 아닌 경우 확인
		if(!$(event.target).is('.slideBox') && !$(event.target.offsetParent).is('.slideBox, .req_right_table_wrap')){
			//디테일 창이 표시되어 있는지 확인, 그리드 선택인지 확인
		    if((!$(event.target).is($('[data-ax5grid] *')) || !$(event.target.offsetParent).is($('[data-ax5grid] *'))) && !$('.req_right_table_wrap').hasClass('on')){
				$('.req_right_table_wrap, .slideBox, .slideBox > img').toggleClass('on');
	    	} 
		}
	});	
	
	//우측 디테일 창 'ESC'버튼으로도 닫히기
	$(document).on('keydown', function(e){
		if(e.keyCode==27 && !$('.req_right_table_wrap').hasClass('on')){
			$('.req_right_table_wrap, .slideBox, .slideBox > img').toggleClass('on');	
		};
	});
	
	//우측 디테일 창
	$('.slideBox').click(function(){
		$('.req_right_table_wrap, .slideBox, .slideBox > img').toggleClass('on');
	});
	
	//그리드 검색 호출
	fnAxGrid5View();
	fnSearchBoxControl();
});


//검색조건 변경되었을때 이벤트
function fnSelectChg(){
	var selVal = $("#searchSelect option:selected").val();
	if(selVal == '0'){
		$("#searchTxt").val("");
		$("#searchTxt").attr("readonly", true);
	}else{
		$("#searchTxt").attr("readonly", false);
	}
}

//axisj5 그리드
function fnAxGrid5View(){
	firstGrid = new ax5.ui.grid();
 
        firstGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            
            sortable:false,
            header: {align:"center"},
            columns: [
				
				{key: "apiNm", label: "서비스명", width: 150, align: "left"},
				{key: "apiUrl", label: "URL", width: 500, align: "center"},
				{key: "apiTok", label: "API KEY", width: 450, align: "center"},
				{key: "useNm", label: "사용여부", width: 100, align: "center"},
				{key: "regUsrId", label: "요청자", width: 120, align: "center"},
				{key: "regDtm", label: "등록일자", width: 180, align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30                
            } ,
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                   fnInGridListSet(this.page.selectPage,mySearch.getParam());
                }
            } 
        });
        //그리드 데이터 불러오기
 		fnInGridListSet();

}
//그리드 데이터 넣는 함수
function fnInGridListSet(_pageNo,ajaxParam){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	if(gfnIsNull(ajaxParam)){
   			ajaxParam = $('form#searchFrm').serialize();
   		}
     	
     	//페이지 세팅
     	if(!gfnIsNull(_pageNo)){
     		ajaxParam += "&pageNo="+_pageNo;
     	}else if(typeof firstGrid.page.currentPage != "undefined"){
     		ajaxParam += "&pageNo="+firstGrid.page.currentPage;
     	}
     	
     	ajaxParam += "&prjId=${sessionScope.selPrjId}";
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm1000/stm1200/selectStm1200ProjectListAjax.do'/>","loadingShow":true}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			var page = data.page;
			for(var i=0; i< list.length; i++){
				list[i].apiTok = encodeURIComponent(list[i].apiTok);
			}
			
		   	firstGrid.setData({
		             	list:list,
		             	page: {
		                  currentPage: _pageNo || 0,
		                  pageSize: page.pageSize,
		                  totalElements: page.totalElements,
		                  totalPages: page.totalPages
		              }
		             });
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
function fnSearchBoxControl(){
	var pageID = "AXSearch";
	mySearch = new AXSearch();

	var fnObjSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			mySearch.setConfig({
				targetID:"AXSearchTarget",
				theme : "AXSearch",
				rows:[
					{display:true, addClass:"", style:"", list:[
						{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
							options:[
                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
                                {optionValue:'apiNm', optionText:'서비스 명'},
                                {optionValue:'useCd', optionText:'사용 여부' , optionCommonCode:"CMM00001" }                                
                                
                            ],onChange: function(selectedObject, value){
                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
									axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");	
									axdom("#" + mySearch.getItemId("searchTxt")).val('');	
								}else{
									axdom("#" + mySearch.getItemId("searchTxt")).removeAttr("readonly");
								}
								
								//공통코드 처리 후 select box 세팅이 필요한 경우 사용
								if(!gfnIsNull(selectedObject.optionCommonCode)){
									gfnCommonSetting(mySearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
								}else{
									//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
									axdom("#" + mySearch.getItemId("searchTxt")).show();
									axdom("#" + mySearch.getItemId("searchCd")).hide();
								}
    						},

						},
						{label:"", labelWidth:"", type:"inputText", width:"120", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
							onkeyup:function(e){
								if(e.keyCode == '13' ){
									axdom("#" + mySearch.getItemId("btn_search_api")).click();
								}
							} 
						},
						{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
							options:[]
						},
							
						{label:"<i class='fas fa-list-ol'></i>&nbsp;목록 수&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"pageSize", addClass:"", valueBoxStyle:"", value:"30",
							options:[
							         	{optionValue:15, optionText:"15"},
		                                {optionValue:30, optionText:"30"},
		                                {optionValue:50, optionText:"50"},
		                                {optionValue:100, optionText:"100"},
		                                {optionValue:300, optionText:"300"},
		                                {optionValue:600, optionText:"600"},
		                                {optionValue:1000, optionText:"1000"},
		                                {optionValue:5000, optionText:"5000"},
		                                {optionValue:10000, optionText:"10000"},
		                                
		                            ],onChange: function(selectedObject, value){
		                            	fnInGridListSet(0,$('form#searchFrm').serialize()+"&"+mySearch.getParam());
		    						}
						},
						{label:"<i class='fa fa-arrows-v'></i>&nbsp;목록 높이", labelWidth:"100", type:"selectBox", width:"", key:"gridHeight", addClass:"", valueBoxStyle:"", value:"600",
							options:[
							         	{optionValue:300, optionText:"300px"},
		                                {optionValue:600, optionText:"600px"},
		                                {optionValue:1000, optionText:"1000px"},
		                                {optionValue:1200, optionText:"1200px"},
		                                {optionValue:2000, optionText:"2000px"},
		                                
		                            ],onChange: function(selectedObject, value){
		                            	firstGrid.setHeight(value);
		    						}
						},
						
						{label:"", labelWidth:"", type:"button", width:"60",style:"float:right;", key:"btn_print_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-print' aria-hidden='true'></i>&nbsp;<span>프린트</span>",
							onclick:function(){
								$(firstGrid.exportExcel()).printThis();
						}},
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"btn_excel_api",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-file-excel' aria-hidden='true'></i>&nbsp;<span>엑셀</span>",
							onclick:function(){
								firstGrid.exportExcel("${sessionScope.selMenuNm}.xls");
						}},
						{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search_api",  style:"float:right;" , valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								/* 검색 조건 설정 후 reload */
	 							var pars = mySearch.getParam();
							    var ajaxParam = $('form#searchFrm').serialize();

							    if(!gfnIsNull(pars)){
							    	ajaxParam += "&"+pars;
							    }
								
					            fnInGridListSet(0,ajaxParam);
					            
					            //폼 데이터 변경
								$('#searchSelect').val(axdom("#" + mySearch.getItemId("searchSelect")).val());
								$('#searchCd').val(axdom("#" + mySearch.getItemId("searchCd")).val());
								$('#searchTxt').val(axdom("#" + mySearch.getItemId("searchTxt")).val());
					            
						}}
					]}
				]
			});
		}
		/*,
		search1: function(){
			var pars = mySearch.getParam();
			fnAxGridView(pars);
		}
		*/
	};
	
	jQuery(document.body).ready(function(){
		
		fnObjSearch.pageStart();
		//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
		axdom("#" + mySearch.getItemId("searchTxt")).attr("readonly", "readonly");
		
		//공통코드 selectBox hide 처리
		axdom("#" + mySearch.getItemId("searchCd")).hide();

		//버튼 권한 확인
		fnBtnAuthCheck(mySearch);
	});
}
</script>


<div class="main_contents" style="height: auto;">
	<form:form commandName="req1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false;">
	</form:form>
	<div class="req_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu">
		<input type="hidden" name="strInSql" id="strInSql" />
		<div id="AXSearchTarget" style="border-top:1px solid #ccc;"></div>
		<br />
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 600px;"></div>	
	</div>
</div>
		
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />