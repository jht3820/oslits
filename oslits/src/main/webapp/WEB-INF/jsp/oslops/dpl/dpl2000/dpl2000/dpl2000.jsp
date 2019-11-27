<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp"%>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/dpl.css'/>' type='text/css'>
<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>

<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<style type="text/css">
	.search_select select {font-size: 0.85em;}
	.search_select {width: 124px;height: 28px;margin: 0 5px 5px 0;}
	.search_box_wrap {width: calc(100% - 404px);} /* width:calc(100% - (.search_select너비 * 갯수 + 32px))  */
	.req_right_box {border-radius: 5px;}
	.req_left_table_wrap {width: 73%;}
	
	@media screen and (max-width: 1100px) {
		.search_box {height: 100%;}
		.search_select {width: 124px;}
		.search_select select {max-width: 124px;}
		.req_left_table_wrap {width: 100%;}
		.approval_btn {margin-right: 1%;}
	}
	.sub_title{ font-weight: bold; background: #f9f9f9; border-bottom: 1px solid #ccc; text-align: left; padding: 5px 0 23px 0; height: 25px;border-radius: 5px 5px 0 0;}
</style>

<script type="text/javascript">
	var dplSearch;
	var threeGrid;
	var vPrjId;
	var vDplId;
	var Grid = {
		init : function() {
			//그리드 및 검색 상자 호출
			fnAxGrid5View(); // Grid 초기화  설정
			fnSearchBoxControl(); // Search Grid 초기화 설정
		},
		columnOption : {
			dpl1000Search : [ 
	                 {optionValue : "rn",optionText : "전체 보기",optionAll : true}, 
	                
	                 {optionValue : "dplNm",optionText : '배포 명'}, 
	                 {optionValue : "dplUsrId",optionText : "배포자"} 
	                 ]
			}
		};

	$(document).ready(function() {
		// AXISJ Grid 초기화 실행 부분들
		fnAxGrid5View_first();
		
		Grid.init(); 
		//달력 세팅 (배포일)
		fnAxsecondJobGrid5View();

	});

//axisj5 그리드
function fnAxGrid5View(){
	dplGrid = new ax5.ui.grid();
 
        dplGrid.setConfig({
            target: $('[data-ax5grid="first-grid"]'),
            showRowSelector: true,
            sortable:false,
            multipleSelect: false  ,
            header: {align:"center", selector : false  },	
    
            columns: [
				 {key : "dplStsNm",label : "배포 상태",width : 100,align : "center"},
				{key : "dplNm",label : "배포 명",width : '40%',align : "left"},
				{key : "dplDt",label : "배포 날짜",width : '16%',align : "center",
					formatter : function() {
						var fmtDt = this.item.dplDt;
						return new Date(fmtDt.substr(0, 4), fmtDt.substr(4,2) - 1, fmtDt.substr(6, 2)).format("yyyy-MM-dd", true);
					}
				},
				{key : "dplId",label : "배포 ID",width : '19%',align : "center"},
				{key : "dplUsrId",label : "배포자",width :'17%',align : "center"}				 
				
            ],
            page: {
                navigationItemCount: 9,
                height: 30,
                display: true,
                firstIcon: '<i class="fa fa-step-backward" aria-hidden="true"></i>',
                prevIcon: '<i class="fa fa-caret-left" aria-hidden="true"></i>',
                nextIcon: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
                lastIcon: '<i class="fa fa-step-forward" aria-hidden="true"></i>',
                onChange: function () {
                   fnInGridListSet(this.page.selectPage,dplSearch.getParam());
                }
            },
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	
                	var selItem = dplGrid.list[this.doindex];
                	vPrjId = selItem.prjId;
                	vDplId = selItem.dplId;
                	
                	
                	
                	fnInsecondJobGridListSet(vPrjId,vDplId);
                	 //그리드 데이터 불러오기
        	 		fnReqGridListSet(vDplId);
                	dplGrid.select(this.doindex, {selected: true});
                },
                 onDBLClick:function(){

            		var item = this.item;
            		var data = {"prjId": item.prjId , "dplId": item.dplId  }; 
            	
					gfnLayerPopupOpen('/dpl/dpl2000/dpl2000/selectDpl2001View.do',data, '1300', '957','scroll');
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
     	}else if(typeof dplGrid.page.currentPage != "undefined"){
     		ajaxParam += "&pageNo="+dplGrid.page.currentPage;
     	}
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl1000/dpl1000/selectDpl1000BuildInfoListAjax.do'/>","loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			var page = data.page;
			
		   	dplGrid.setData({
		             	list:list,
		             	page: {
		                  currentPage: _pageNo || 0,
		                  pageSize: page.pageSize,
		                  totalElements: page.totalElements,
		                  totalPages: page.totalPages
		              }
		             });
		   	if(list.length>0){
		   		vPrjId = list[0].prjId;
		   		vDplId = list[0].dplId;
		   		fnInsecondJobGridListSet(vPrjId,vDplId);
		   		fnReqGridListSet(vDplId);
		   		dplGrid.select(0, {selected: true});
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
	//검색 상자
	function fnSearchBoxControl() {
		var pageID = "AXSearch";
		dplSearch = new AXSearch();

		var fnObjSearch = {
			pageStart : function() {
				//검색도구 설정 01 ---------------------------------------------------------
				dplSearch.setConfig({
					targetID : "AXSearchTarget",
					theme : "AXSearch",
					rows : [
					   {display : true,
						addClass : "",
						style : "",
						list : [{label : "<i class='fa fa-search'></i>&nbsp;",labelWidth : "50",type : "selectBox",width : "",key : "searchSelect",addClass : "",valueBoxStyle : "",value : "all",
							options : Grid.columnOption.dpl1000Search,
								onChange : function(selectedObject,value) {
										//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
										if (!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true) {
											axdom("#"+ dplSearch.getItemId("searchTxt")).attr("readonly","readonly");
											axdom("#"+ dplSearch.getItemId("searchTxt")).val('');
										} else {
											axdom("#"+ dplSearch.getItemId("searchTxt")).removeAttr("readonly");
										}
		
										//공통코드 처리 후 select box 세팅이 필요한 경우 사용
										if (!gfnIsNull(selectedObject.optionCommonCode)) {
											gfnCommonSetting(dplSearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
										} else if (value == "flowId") {
											//option 초기화
											axdom("#"+ dplSearch.getItemId("searchCd")).html('');
											//목록 불러오기
											axdom("#"+ dplSearch.getItemId("searchCd")).append('<option value="ALL">전체</option>');
											$.each(JSON.parse(flowList),function() {
												axdom("#"+ dplSearch.getItemId("searchCd")).append('<option value="'+this.flowId+'">'+ this.flowNm+ '</option>');
											});
											axdom("#"+ dplSearch.getItemId("searchCd")).append('<option value="FLW">미분류</option>');
											axdom("#"+ dplSearch.getItemId("searchTxt")).hide();
											axdom("#"+ dplSearch.getItemId("searchCd")).show();
										} else if (value == "sprintId") {//option 초기화
											axdom("#"+ dplSearch.getItemId("searchCd")).html('');
										//개발주기가 있는 경우 목록 조회
										if (gfnIsNull(sprintList)) {
											axdom("#"+ dplSearch.getItemId("searchCd")).append('<option value="">없음</option>');
										} else {
											//목록 불러오기
											$.each(JSON.parse(sprintList),function() {
												axdom("#"+ dplSearch.getItemId("searchCd")).append('<option value="'+this.sprintId+'">'+ this.sprintNm+ '</option>');
											});
										}
											axdom("#"+ dplSearch.getItemId("searchTxt")).hide();
											axdom("#"+ dplSearch.getItemId("searchCd")).show()
										} else {
											//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
											axdom("#"+ dplSearch.getItemId("searchTxt")).show();
											axdom("#"+ dplSearch.getItemId("searchCd")).hide();
										}
									}
									},
									{label : "",labelWidth : "",type : "inputText",width : "150",key : "searchTxt",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:0px;",value : ""
										, onkeyup:function(e){
											if(e.keyCode == '13' ){
												axdom("#" + dplSearch.getItemId("btn_search_dlp")).click();
											}
										}
									},
									{label : "",labelWidth : "",type : "selectBox",width : "100",key : "searchCd",addClass : "selectBox",valueBoxStyle : "padding-left:0px;",value : "01",options : []},
									
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
					                            	fnInGridListSet(0,$('form#searchFrm').serialize()+"&pageSize="+value);
					    						}
									},
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_insert_build",style : "float:right;",valueBoxStyle : "padding-left:10px;padding-right:5px;",value : "<span>배포</span>",
										onclick : function() {
											var item = (!gfnIsNull(Object.keys(dplGrid.focusedColumn)))? dplGrid.list[dplGrid.focusedColumn[Object.keys(dplGrid.focusedColumn)].doindex]: fnGetSelectRow(dplGrid);
									
											if(item==null){
												toast.push(' 배포 버전을 선택세요.');
												return;
											}
											var data = {"prjId": item.prjId , "dplId": item.dplId  }; 
											gfnLayerPopupOpen("/dpl/dpl2000/dpl2000/selectDpl2002View.do", data, '1300', '770','scroll');
											axdom("#" + dplSearch.getItemId("btn_search_dlp")).click();
										}
									}, 
									{label : "",labelWidth : "",type : "button",width : "55",key : "btn_search_dlp",style:"float:right;",valueBoxStyle : "padding-left:10px;padding-right:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
										onclick : function() {
											/* 검색 조건 설정 후 reload */
				 							var pars = dplSearch.getParam();
										    var ajaxParam = $('form#searchFrm').serialize();
				
										    if(!gfnIsNull(pars)){
										    	ajaxParam += "&"+pars;
										    }
											
								            fnInGridListSet(0,ajaxParam);
								            
								            //폼 데이터 변경
											$('#searchSelect').val(axdom("#" + dplSearch.getItemId("searchSelect")).val());
											$('#searchCd').val(axdom("#" + dplSearch.getItemId("searchCd")).val());
											$('#searchTxt').val(axdom("#" + dplSearch.getItemId("searchTxt")).val());
										}
									}
									
							]}]
						});
			}
		};

		jQuery(document.body).ready(
				function() {
					fnObjSearch.pageStart();
					//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
					axdom("#" + dplSearch.getItemId("searchTxt")).attr("readonly", "readonly");

					//공통코드 selectBox hide 처리
					axdom("#" + dplSearch.getItemId("searchCd")).hide();

					//버튼 권한 확인
					fnBtnAuthCheck(dplSearch);

				});
	}
	
	var secondJobGrid;

	function fnAxsecondJobGrid5View(){
		secondJobGrid = new ax5.ui.grid();
	 
	        secondJobGrid.setConfig({
	            target: $('[data-ax5grid="second-job-grid"]'),
	 
	            sortable:false,
	            header: {align:"center"},
	 
	            columns: [
	               
					{key: "jenNm", label: "JOB 명", width: 100, align: "center"},
					{key: "jenTxt", label: "JOB 설명", width: 250, align: "left"},
					{key: "bldSts", label: "빌드결과", width: 150, align: "center"}
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30
	            }
	        });

	}
	//그리드 데이터 넣는 함수
	function fnInsecondJobGridListSet(prjId,dplId){
	     	
	     	//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JobBuildListAjax.do'/>","loadingShow":true}
					,{"prjId" : prjId , "dplId" : dplId });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				var list = data.jobList;
				if(data.messageCode=="2001"){
					toast.push(data.message);
				}
			
			   	secondJobGrid.setData(list);
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
	function fnGetSelectRow(selGrid){
		var chkList = selGrid.getList('selected');
		var pList = {};
		$(chkList).each(function(idx, data) {
			if(data.__selected__){
				pList=data;
			}
		});
		
		return pList;
	}
	
	
	function fnAxGrid5View_first(){
		threeGrid = new ax5.ui.grid();
	 
	    threeGrid.setConfig({
	            target: $('[data-ax5grid="three-grid"]'),
	            showRowSelector: false,
	            sortable:false,
	            header: {align:"center"},
	            columns: [
					{key: "processNm", label: "프로세스 명", width: 120, align: "center"}
	                ,{key: "flowNm", label: "작업흐름 명", width: 120, align: "center"}
					,{key: "reqNm", label: "요구사항 명", width: '35%', align: "left"}
					,{key: "reqDesc", label: "요구사항 설명", width: '40%', align: "left"}
					,{key: "reqNo", label: "공문 번호", width: '11%', align: "center"}
					,{key: "reqUsrNm", label: "요청자", width: '11%', align: "center"}
					,{key: "reqChargerNm", label: "담당자", width: '11%', align: "center"}
					,{key: "reqDtm", label: "요청일자", width: '14%', align: "center"}
					
					,{key: "reqStDtm", label: "시작 기간", width: '14%', align: "center"}
					,{key: "reqEdDtm", label: "종료 기간", width: '14%', align: "center"}
					,{key: "reqStDuDtm", label: "시작 예정일자", width: '15%', align: "center"}
					,{key: "reqEdDuDtm", label: "종료 예정일자", width: '15%', align: "center"}
					
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30,
	                onDBLClick:function(){
	                	var data = {"mode": "req", "reqId": this.item.reqId};
						gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '887','scroll');
	                }
	            }
	        });
	        
	       
	}
	
	function fnReqGridListSet(dplId){
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl1000/dpl1100/selectDpl1100DplListAjax.do'/>","loadingShow":true}
				,{"clsMode":"clsAdd","dplId":dplId});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			var list =[];
			if(!gfnIsNull(data)){
				data = JSON.parse(data);
				list = data.list;	
			}
			
			threeGrid.setData(list);
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
<div class="main_contents" style="height: auto;">
	<div class="dpl_title">${sessionScope.selMenuNm }</div>
	<div class="tab_contents menu" style="max-width: 1500px;">
		<form:form commandName="dpl1000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
		<div id="AXSearchTarget" style="border-top: 1px solid #ccc;"></div>
		<br />
		<table>
			<tr>
				<td style="width: 900px;" >
					<div class="sub_title">
							배포 버젼 목록
					</div>
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 290px;"></div>		
				</td>
				<td style="width: 30px;" rowspan="2"></td>
				<td style="width: 500px;" rowspan="2" >
					<div class="sub_title">
							할당된 JOB 목록
					</div>
					<div data-ax5grid="second-job-grid" data-ax5grid-config="{}" style="height: 616px;"></div>
				</td>
			</tr>
			<tr>
				<td style="width: 900px; padding-top: 10px; background: #f9f9f9;" >
					<div class="sub_title">
							배포 추가된 요구사항 목록
					</div>
					<div data-ax5grid="three-grid" data-ax5grid-config="{}" style="height: 285px;"></div>  			
				</td>
				
				
			</tr>
		</table>
		
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />