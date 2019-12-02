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
	var pop_JobListGrid  = {
			init : function() {
				//그리드 및 검색 상자 호출
				
				fnAxGridSet();  // Grid 초기화  설정\
				
				fnSearchBoxControl(); // Search Grid 초기화 설정
				
			}
			
	}


	$(document).ready(function() {
		pop_JobListGrid.init(); // AXISJ Grid 초기화 실행 부분들
		
		/* 취소 */
		$('#btnPopDpl1002Cancle').click(function() {
			gfnLayerPopupClose();
		});
		
		//선택 버튼 클릭
		$('#btnPopDpl1002Select').click(function() {
			if(gfnIsNull(pop_JobListGrid.selectedDataIndexs)){
				jAlert("선택된 JOB이 없습니다.");
				return false;
			}
			
			var initAJL_size = ADD_JOB_LIST.length;

			//추가된 idx
			var appendIdx = 0;
			
			//그리드 list loop
			$.each(pop_JobListGrid.getList(),function(idx, map){
				//선택된 job
				if(!gfnIsNull(map.__selected__) && map.__selected__){
					//job 추가 리스트
					ADD_JOB_LIST.push({jenId: map.jenId, jobId: map.jobId});
					
					//실행 순서 자동 입력
					var ord = (initAJL_size+(appendIdx+1));
					
					//div 추가
					$("#dpl_bottom_job_content").append(
							'<div class="dpl_middle_row dpl_job_row" jenid="'+map.jenId+'" jobid="'+map.jobId+'" ord="'+ord+'" onclick="fnJobInfoClick(this,event)">'
							+'	<div class="dpl_middle_cell">'
							+'		<div class="dpl_chk">'
							+'			<input type="checkbox" title="체크박스" name="addJobDelChk" id="addJobDelChk_'+map.jenId+'_'+map.jobId+'" jenid="'+map.jenId+'" jobid="'+map.jobId+'"/><label for="addJobDelChk_'+map.jenId+'_'+map.jobId+'"></label>'
							+'		</div>'
							+'	</div>'
							+'	<div class="dpl_middle_cell dplStartOrdCell" ord="'+ord+'">'+ord+'</div>'
							+'	<div class="dpl_middle_cell" title="'+map.jenNm+'">'+map.jenNm+'</div>'
							+'	<div class="dpl_middle_cell" title="'+map.jenUrl+'">'+map.jenUrl+'</div>'
							+'	<div class="dpl_middle_cell">'+map.jobTypeNm+'</div>'
							+'	<div class="dpl_middle_cell">'+map.jobId+'</div>'
							+'	<div class="dpl_middle_cell">'+map.jobRestoreId+'</div>'
							+'</div>');
					
					appendIdx++;
				}else{ //selected false
					return true;
				}
			});
			
			//팝업 창 닫기
			gfnLayerPopupClose();
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
									{optionValue:'jobId', optionText:'JOB ID'},
									{optionValue:'jobNm', optionText:'JOB 명'},
									{optionValue:'jobDesc', optionText:'JOB 설명'}                             
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
					            fnInGridPopListSet(0,popSearch.getParam());
							    
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
				
		pop_JobListGrid = new ax5.ui.grid();
 
        pop_JobListGrid.setConfig({
            target: $('[data-ax5grid="pop-grid-jobList"]'),
            sortable:false,
            showRowSelector: true,
            multipleSelect: true ,
            header: {align:"center" },
            //frozenColumnIndex: 2,
            columns: [
                {key: "jobTypeNm", label: "JOB TYPE", width: 80, align: "center"},
				{key: "jobId", label: "JOB ID", width: 150, align: "left"},
				{key: "jobRestoreId", label: "원복 JOB ID", width: 150, align: "center"},
				{key: "jobDesc", label: "JOB 설명", width: 210, align: "center"},
				{key: "jenNm", label: "JENKINS NAME", width: 170, align: "center"},
				{key: "jenUrl", label: "JENKINS URL", width: 270, align: "left"},
				{key: "useNm", label: "사용유무", width: 80, align: "center"}
            ],
            body: {
                align: "center",
                columnHeight: 30
            },
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                   fnInLeftGridListSet(this.page.selectPage,popSearch.getParam());
                }
            } 
        });
        fnInGridPopListSet();
	}
	
	//그리드 데이터 넣는 함수
	function fnInGridPopListSet(_pageNo,ajaxParam){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
         	
		if(gfnIsNull(ajaxParam)){		
			ajaxParam = $('form#searchFrm').serialize();
		}
     	
     	//페이지 세팅
     	if(!gfnIsNull(_pageNo)){
     		ajaxParam += "&pageNo="+_pageNo;
     	}else if(typeof pop_JobListGrid.page.currentPage != "undefined"){
     		ajaxParam += "&pageNo="+pop_JobListGrid.page.currentPage;
     	}
     	
     	//사용유무 '01'인경우만
     	ajaxParam += '&useCd=01';
     	
     	//배포 배정된 JOB만
     	ajaxParam += '&projectJenkinsCheck=Y';
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/stm/stm3000/stm3000/selectStm3000JobListAjax.do'/>","loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			var page = data.page;
			
			//list 값
			var jobList = [];
			
			
			//이미 추가된 Job 거르기
			$.each(list,function(idx, map){
				//해당하는 jenId, jobId 찾기
				//var findItem = ADD_JOB_LIST.findIndex(function(item, idx){return (item.jenId == map.jenId && item.jobId == map.jobId)});
				var findItem = -1;
				
				$.each(ADD_JOB_LIST, function(idx2, map2){
					if(map.jenId == map2.jenId && map.jobId == map2.jobId){
						findItem = idx2;
						return false;
					}
				});
				
				
				//타입이 원복인경우 제외
				if(map.jobTypeCd == "03"){
					return true;
				}
				//목록에 없는경우 추가
				if(findItem == -1){
					jobList.push(map);
				}
			});
			
		   	pop_JobListGrid.setData({
		             	list: jobList,
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
</script>

<div class="popup">
		
	<div class="pop_title">JOB 등록</div>
	<div class="pop_sub">
		<div class="tab_contents menu" style="max-width:600px;">
			<form:form commandName="jen1100VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			<div id="AXSearchPopTarget" style="border-top:1px solid #ccc;"></div>
			<br />
			<div data-ax5grid="pop-grid-jobList" data-ax5grid-config="{}" style="height: 350px;"></div>
		</div>
		<div class="btn_div">
			<div class="button_normal save_btn" id="btnPopDpl1002Select" >선택</div>
			<div class="button_normal exit_btn" id="btnPopDpl1002Cancle" >취소</div>
		</div>
	</div>
	</div>
	</form>
</div>
</html>