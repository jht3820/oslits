<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>

<script src="<c:url value='/js/common/layerPopup.js'/>" ></script>
<style type="text/css">
	.tab_contents {font-size:0.75em;}
</style>
<script type="text/javascript">
	/* 
		공통 코드  삭제
		@Param  : type ["master" : "detail"] : 마스터이면 부모 공통코드 삭제, 디테일이면 부모 공통코드 삭제
		@Param  : data 마스터/디테일 데이터
	*/
	var mySearch;
	var fnDeleteInfo = function(type,items) {
		
			if(gfnIsNull(items)){
				alert("공통코드를 선택해주세요");
				return false;
			}
			var data = {};
			if(type == "master"){
				$(items).each(function(i,v){
					data = {"mstCd":this.mstCd,"type":"master" ,"stmUseYn":this.stmUseYn , url : "/adm/adm4000/adm4000/deleteAdm4000CommonCodeMasterAjax.do"};
				})
			}else if(type == "detail"){
				$(items).each(function(i,v){
					data = {"mstCd":this.mstCd, "subCd":this.subCd, "type":"detail", "stmUseYn":this.stmUseYn, url: "/adm/adm4000/adm4000/deleteAdm4000CommonCodeDetailAjax.do"};
				});
			}else{
				alert("데이터 오류");
				return false;
			}

			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":data.url}
					,data);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data) {
				data = JSON.parse(data);
		    	
		    	//수정이 실패하면 실패 메시지 후 리턴
		    	if(data.errorYN == 'Y'){
		    		toast.push(data.message);
		    		return false;
		    	} 
		    	else{
		    		if(type == "master"){
						//그리드 세팅
		    			fnAxGrid5View_first(); // Grid 초기화  설정
						fnAxGrid5View_second(); // Grid 초기화  설정
					}else if(type == "detail"){
						//그리드 세팅
						var item = firstGrid.getList('selected')[0];
						fnInGridListSet_bottom(null,{grid:secondGrid,data:{mstCd:item.mstCd} });
					}
		    	}
		    	toast.push(data.message);
			});
			//AJAX 전송
			ajaxObj.send();
	};

	$(document).ready(function() {
		//그리드 및 검색 상자 호출
		fnAxGrid5View_first(); // Grid 초기화  설정
		fnAxGrid5View_second(); // Grid 초기화  설정
		fnSearchBoxTopControl(); // Search Grid 초기화 설정
		fnSearchBoxBottomControl();

	});
	
	//axisj5 그리드 개발주기 배정된 요구사항
	function fnAxGrid5View_first(dplId){
	firstGrid = new ax5.ui.grid();
	
	    firstGrid.setConfig({
	        target: $('[data-ax5grid="first-grid"]'),
	        sortable:false,
	        header: {align:"center"},
	        columns: [
			              {key: "mstCd", label: "공통코드", width: '14%', align: "center"}
			             ,{key: "upperMstCd", label: "상위 코드", width: '14%', align: "center"}
			             ,{key: "mstCdNm", label: "코드 명", width: '24%', align: "center"}
			             ,{key: "mstCdDescGrid", label: "코드 설명", width: '25%', align: "center"}
			             ,{key: "ord", label: "표시 순서", width: '12%', align: "center",formatter:function(){return parseInt(this.item.ord);}}
			             ,{key: "useYn", label: "사용 유무", width: '12%', align: "center"
			            	 ,formatter:function(){ return this.item.useYn == "Y"?"사용":"미사용"; }}
			             ,{key: "stmUseYn", label: "시스템 사용 유무", width: '12%', align: "center"
			            	 ,formatter:function(){ return this.item.stmUseYn == "Y"?"사용":"미사용"; }}
		                ],
	        body: {
	            align: "center",
	            columnHeight: 30
	            ,onClick:function(){
					//이전 선택 row 해제
                    this.self.select(firstGrid.selectedDataIndexs[0]);
                	
                	//현재 선택 row 전체 선택
                    this.self.select(this.doindex);
                	
					fnInGridListSet_bottom(null,{grid:secondGrid,data:{mstCd:this.item.mstCd} });
	            },onDataChanged: function () {
	            	//하위 요구사항인 경우
	            	if(this.item.upperReqId != "top"){
	            		//강제 선택(선택해제, 선택 막기)
	            		this.self.select(this.doindex);
	            	}
	                if (this.key == 'isChecked') {
	                    this.self.updateChildRows(this.doindex, {isChecked: this.item.isChecked});
	                }
	                else if(this.key == '__selected__'){
	                    this.self.updateChildRows(this.doindex, {__selected__: this.item.__selected__});
	                }
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
	            	var selItem = firstGrid.list[param.doindex];
	            	//선택 개체 없는 경우 중지
                	if(typeof selItem == "undefined"){
                		return false;
                	}
	            	return true;
	            },
	            onClick: function (item, param) {
	            	
					//열 고정
	                if(item.type == "rowFrozen"){
	                	//이미 해당 열에 고정인경우 중지
                    	if(firstGrid.config.frozenColumnIndex != (param.colIndex+1)){
                    		firstGrid.setConfig({frozenColumnIndex:param.colIndex+1});
                    		fnInGridListSet();
                    	}
	                }
	                firstGrid.contextMenu.close();
	            }
	        }
	    });
	    
	    //그리드 데이터 불러오기
		fnInGridListSet(null,{grid:firstGrid});
	
	}

	//그리드 데이터 넣는 함수
	function fnInGridListSet(ajaxParam,gridTarget){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
     	if(gfnIsNull(ajaxParam)){
   			ajaxParam = $('form#searchFrm').serialize();
   		}
     	
     	//데이터 세팅
     	if(!gfnIsNull(gridTarget.data)){
     		ajaxParam += "&"+$.param(gridTarget.data);
     	}
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm4000/adm4000/selectAdm4000CommonCodeMasterListAjax.do'/>","loadingShow":true}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			// 조회 에러일 경우
	    	if(data.errorYN == 'Y'){
	    		toast.push(data.message);
	    		return;
	    	}
			
		   	gridTarget.grid.setData(list);
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
	
	//상단 마스터 검색 상자
	function fnSearchBoxTopControl(){
		mySearch_top = new AXSearch();

		var fnObjSearch_top = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				mySearch_top.setConfig({
					targetID:"AXSearchTargetUp",
					theme : "AXSearch",
					rows:[
						{display:true, 
						addClass:"", 
						style:"", 
						list:[
							{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
								options:[
                             {optionValue:"0", optionText:"전체 보기",optionAll:true}
                             ,{optionValue:"mstCd", optionText:"공통 코드"}
                             ,{optionValue:"mstCdNm" , optionText:"코드 명"}
                             ,{optionValue:"mstCdDesc", optionText:"코드 설명"}
                             ,{optionValue:"useYn", optionText:"사용 여부"}
                         	],onChange: function(selectedObject, value){
	                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
	    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
										axdom("#" + mySearch_top.getItemId("searchTxt")).attr("readonly", "readonly");	
										axdom("#" + mySearch_top.getItemId("searchTxt")).val('');	
									}else{
										axdom("#" + mySearch_top.getItemId("searchTxt")).removeAttr("readonly");
									}
	    							//공통코드 처리 후 select box 세팅이 필요한 경우 사용
									if(value=="useYn"){
										axdom("#" + mySearch_top.getItemId("searchCd")).empty();
										axdom("#" + mySearch_top.getItemId("searchCd")).append($("<option>",{text:'사용', value:"Y"}));
										axdom("#" + mySearch_top.getItemId("searchCd")).append($("<option>",{text:'미사용', value:"N"}));
										axdom("#" + mySearch_top.getItemId("searchCd")).show();
										axdom("#" + mySearch_top.getItemId("searchTxt")).hide();
									}else{
										//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
										axdom("#" + mySearch_top.getItemId("searchTxt")).show();
										axdom("#" + mySearch_top.getItemId("searchCd")).hide();
									}
	    						}
							},
							{label:"", labelWidth:"", type:"inputText", width:"150", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"", value:"",
								onkeydown: function(e){
									searchEnterKey(e, mySearch_top);
								}	
							},
							{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
								options:[]
							},					
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_delete", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
								onclick:function(){
									var chkList = firstGrid.getList('selected');

									if(gfnIsNull(chkList)){
										jAlert("삭제할 공통코드를 선택해주세요", "알림창");
										return false;
									}
									
									var rowChk = false;
									
									// 공통코드 시스템 사용여부 체크
									$(chkList).each(function(i,v){
										// 공통코드 마스터가 시스템 사용이라면 
										if( v.stmUseYn == "Y"){
											rowChk = true;
											return false;
										}
									})
									
									if(rowChk){
										jAlert('시스템에서 사용중인 공통코드는 삭제할 수 없습니다.','알림창');
										return false;
									}
									
									jConfirm("공통코드 마스터 삭제시 하위의 공통코드 디테일도 같이 삭제됩니다. \n\n선택한 공통코드를 삭제하시겠습니까?", "알림창", function( result ) {
										if( result ){
											fnDeleteInfo("master",chkList);	
										}
									});	
							}},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_update", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
								onclick:function(){
									var item = firstGrid.getList('selected')[0];
									
									if(gfnIsNull(item)){
										jAlert("수정할 공통코드를 선택해주세요", "알림창");
										return false;
									}
									
									var data = {
											 "mode" : "update"
											,"title" : "공통코드 마스터 수정" 
											,"mstCd" : item.mstCd
											,"upperMstCd" : item.upperMstCd
											,"mstCdNm" : item.mstCdNm
											,"mstCdEngNm" : item.mstCdEngNm
											,"mstCdDesc" : item.mstCdDesc
											,"ord" : item.ord
											,"lvl" : item.lvl
											,"useYn" : item.useYn
											,"stmUseYn" : item.stmUseYn
									};
									gfnLayerPopupOpen("/adm/adm4000/adm4000/selectAdm4001View.do", data, "550", "540",'auto');
								}},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_insert", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
								onclick:function(){
									var data = {"mode": "insert", "title" : "공통코드 마스터 등록"};
									gfnLayerPopupOpen("/adm/adm4000/adm4000/selectAdm4001View.do", data, "550", "490",'auto');
								}
							},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
							onclick:function(){
								fnInGridListSet(mySearch_top.getParam(),{grid:firstGrid,data:""});
							}}
						]
						}
					]
				});
			}
		};
		jQuery(document.body).ready(function(){
			//검색상자 및 버튼 세팅
			fnObjSearch_top.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + mySearch_top.getItemId("searchTxt")).attr("readonly", "readonly");
			//공통코드 selectBox hide 처리
			axdom("#" + mySearch_top.getItemId("searchCd")).hide();
			//버튼 권한 확인
			fnBtnAuthCheck(mySearch_top);
		});
	}
	
	//axisj5 그리드 개발주기 배정된 요구사항
	function fnAxGrid5View_second(){
		secondGrid = new ax5.ui.grid();

		secondGrid.setConfig({
	        target: $('[data-ax5grid="second-grid"]'),
	        sortable:false,
	        header: {align:"center"},
	        columns: [
			             {key: "mstCd", label: "공통코드", width: '12%', align: "center"}
			             ,{key: "subCd", label: "서브 코드", width: '12%', align: "center"}
			             ,{key: "subCdNm", label: "서브 코드 명", width: '15%', align: "center"}
			             ,{key: "subCdRef1", label: "보조필드1", width: '12%', align: "center"}
			             ,{key: "subCdRef2", label: "보조필드2", width: '12%', align: "center"}
			             ,{key: "subCdRef3", label: "보조필드3", width: '12%', align: "center"}
			             ,{key: "subCdRef4", label: "보조필드4", width: '12%', align: "center"}
			             ,{key: "subCdRef5", label: "보조필드5", width: '12%', align: "center"}
			             ,{key: "subCdDescGrid", label: "보조필드 설명", width: '25%', align: "center"}
			             ,{key: "ord", label: "표시\n순서", width: '8%', align: "center",formatter:function(){return parseInt(this.item.ord);}}
			             ,{key: "useYn", label: "사용\n유무", width: '8%', align: "center"
			            	 ,formatter:function(itemIndex, item){ return this.item.useYn == "Y"?"사용":"미사용"; }}
		                ],
	        body: {
	            align: "center",
	            columnHeight: 30
	            ,onClick:function(){
	            	//이전 선택 row 해제
                    this.self.select(secondGrid.selectedDataIndexs[0]);
                	
                	//현재 선택 row 전체 선택
                    this.self.select(this.doindex);
	            }
	        },
	        tree: {
	            use: true,
	            indentWidth: 10,
	            arrowWidth: 15,
	            iconWidth: 18,
	            icons: {
	                openedArrow: '<i class="fa fa-caret-down" aria-hidden="true"></i>',
	                collapsedArrow: '<i class="fa fa-caret-right" aria-hidden="true"></i>',
	                groupIcon: '<i class="fa fa-cubes" aria-hidden="true"></i>',
	                collapsedGroupIcon: '<i class="fa fa-cubes" aria-hidden="true"></i>',
	                itemIcon: '<i class="fa fa-file-o" aria-hidden="true"></i>'
	            },
	            columnKeys: {
	                parentKey: "upperReqId",
	                selfKey: "reqId"
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
	            	var selItem = firstGrid.list[param.doindex];
	            	//선택 개체 없는 경우 중지
                	if(typeof selItem == "undefined"){
                		return false;
                	}
	            	return true;
	            },
	            onClick: function (item, param) {
					//열 고정
	                if(item.type == "rowFrozen"){
	                	//이미 해당 열에 고정인경우 중지
                    	if(secondGrid.config.frozenColumnIndex != (param.colIndex+1)){
                    		secondGrid.setConfig({frozenColumnIndex:param.colIndex+1});
                    		fnInGridListSet();
                    	}
	                }
	                secondGrid.contextMenu.close();
	            }
	        }
	    });
	    //그리드 데이터 불러오기
		fnInGridListSet_bottom(null,{grid:secondGrid});

	}
	

	//그리드 데이터 넣는 함수
	function fnInGridListSet_bottom(ajaxParam,gridTarget){
     	/* 그리드 데이터 가져오기 */
     	
     	//파라미터 세팅
     	if(gfnIsNull(ajaxParam)){
   			ajaxParam = $('form#searchFrm').serialize();
   		}
     	
     	//데이터 세팅
     	if(!gfnIsNull(gridTarget.data)){
     		ajaxParam += "&"+$.param(gridTarget.data);
     	}
     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm4000/adm4000/selectAdm4000CommonCodeDetailListAjax.do'/>","loadingShow":true}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			// 조회 에러일 경우
	    	if(data.errorYN == 'Y'){
	    		toast.push(data.message);
	    		return;
	    	}
			
		   	gridTarget.grid.setData(list);
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
	//하단 마스터 검색 상자
	function fnSearchBoxBottomControl(){
		mySearch_bottom = new AXSearch();

		var fnObjSearch_bottom = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				mySearch_bottom.setConfig({
					targetID:"AXSearchTargetDown",
					theme : "AXSearch",
					rows:[ {
							display:true, 
							addClass:"", 
							style:"", 
							list:[
							{label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"50", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
								options:[
		                      {optionValue:"0", optionText:"전체 보기",optionAll:true}
		                     ,{optionValue:"subCdNm", optionText:"서브 코드 명"}
		                     ,{optionValue:"subCdRef", optionText:"보조 필드 1&2"}
		                     ,{optionValue:"subCdDesc", optionText:"보조필드 설명"}
		                     ,{optionValue:"useYn", optionText:"사용 여부"}
               				  ] , 
									onChange: function(selectedObject, value){
			                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
			    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
												axdom("#" + mySearch_bottom.getItemId("searchTxt")).attr("readonly", "readonly");	
												axdom("#" + mySearch_bottom.getItemId("searchTxt")).val('');	
											}else{
												axdom("#" + mySearch_bottom.getItemId("searchTxt")).removeAttr("readonly");
											}
			                            	
			    							//공통코드 처리 후 select box 세팅이 필요한 경우 사용
			    							if(value=="useYn"){
												axdom("#" + mySearch_bottom.getItemId("searchCd")).empty();
												axdom("#" + mySearch_bottom.getItemId("searchCd")).append($("<option>",{text:'사용', value:"Y"}));
												axdom("#" + mySearch_bottom.getItemId("searchCd")).append($("<option>",{text:'미사용', value:"N"}));
												axdom("#" + mySearch_bottom.getItemId("searchCd")).show();
												axdom("#" + mySearch_bottom.getItemId("searchTxt")).hide();
											}else{
												//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
												axdom("#" + mySearch_bottom.getItemId("searchTxt")).show();
												axdom("#" + mySearch_bottom.getItemId("searchCd")).hide();
											}
			    						}
							},
							{label:"", labelWidth:"", type:"inputText", width:"150", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"", value:"",
								onkeydown: function(e){
									searchEnterKey(e, mySearch_bottom);
								}	
							},
							{label:"", labelWidth:"", type:"selectBox", width:"100", key:"searchCd", addClass:"selectBox", valueBoxStyle:"padding-left:0px;", value:"01",
								options:[]
							},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_delete", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
								onclick:function(){
									var chkList = secondGrid.getList('selected');

									if(gfnIsNull(chkList)){
										jAlert("삭제할 공통코드 디테일을 선택해주세요", "알림창");
										return false;
									}
									
									var rowChk = false;
									
									// 공통코드 마스터 시스템 사용여부 체크
									$(chkList).each(function(i,v){
										// 공통코드 마스터가 시스템 사용이라면 
										if( v.stmUseYn == "Y"){
											rowChk = true;
											return false;
										}
									})
									
									if(rowChk){
										jAlert('시스템에서 사용중인 공통코드는 삭제할 수 없습니다.','알림창');
										return false;
									}
									
									jConfirm("공통코드를 디테일을 삭제하시겠습니까?", "알림창", function( result ) {
										if( result ){
											fnDeleteInfo("detail",chkList);
										}
									});		
							}},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_update", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-edit' aria-hidden='true'></i>&nbsp;<span>수정</span>",
								onclick:function(){
									var detailItem = secondGrid.getList('selected')[0];
							
									if(gfnIsNull(detailItem)){
										jAlert("수정할 공통코드 디테일을 선택해주세요", "알림창");
										return false;
									}
									var data = {
											"mode" : "update"
											,"title" : "공통코드 디테일 수정" 
											,"mstCd" : detailItem.mstCd 
											,"subCd" : detailItem.subCd
											,"subCdNm" : detailItem.subCdNm
											,"subCdRef1" : detailItem.subCdRef1 
											,"subCdRef2" : detailItem.subCdRef2
											,"subCdRef3" : detailItem.subCdRef3 
											,"subCdRef4" : detailItem.subCdRef4
											,"subCdRef5" : detailItem.subCdRef5 
											,"subCdDesc" : detailItem.subCdDesc
											,"ord" : detailItem.ord
											,"useYn" : detailItem.useYn
											,"stmUseYn" : detailItem.stmUseYn
										};
									gfnLayerPopupOpen("/adm/adm4000/adm4000/selectAdm4002View.do", data, "660", "490",'auto');
							}},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_insert", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-save' aria-hidden='true'></i>&nbsp;<span>등록</span>",
								onclick:function(){
									var item = firstGrid.getList('selected')[0];
									if(gfnIsNull(item)){
										jAlert("공통코드 마스터를 선택해주세요", "알림창");
										return false;
									}

									if(item.stmUseYn == "Y"){
										jAlert("공통코드 마스터가 시스템에서 사용중인 경우 공통코드 디테일을 추가 할 수 없습니다.", "알림창");
										return false;
									}
									
									var data = {
											"mode" : "insert",
											"title" : "공통코드 디테일 등록", 
											"mstCd" : item.mstCd,
											"stmUseYn" : item.stmUseYn
										};

									gfnLayerPopupOpen("/adm/adm4000/adm4000/selectAdm4002View.do", data, "660", "490",'auto');
							}},
							{label:"", labelWidth:"", type:"button", width:"55", key:"btn_search", style:"float:right;", valueBoxStyle:"padding:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
								onclick:function(){
									var item = firstGrid.getList('selected')[0];
									fnInGridListSet_bottom(mySearch_bottom.getParam(),{grid:secondGrid,data:{mstCd:item.mstCd} });
							}}
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			//검색상자 및 버튼 세팅
			fnObjSearch_bottom.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + mySearch_bottom.getItemId("searchTxt")).attr("readonly", "readonly");
			//공통코드 selectBox hide 처리
			axdom("#" + mySearch_bottom.getItemId("searchCd")).hide();
			//버튼 권한 확인
			fnBtnAuthCheck(mySearch_bottom);
		});
	}
	</script>

<div class="main_contents" style="height: auto;">
	<div class="req_title">${sessionScope.selMenuNm }</div>
				<form:form commandName="adm4000VO" id="excelForm" name="excelForm" method="post" onsubmit="return false;">
                        <input path="searchTxt" type="hidden" id="searchTxt" name="searchTxt"/>
                        <input path="searchCd" type="hidden" id="searchCd" name="searchCd"/>
                        <input path="searchSelect" type="hidden" id="searchSelect" name="searchSelect"/>
                        <input type="hidden" id="MasterOrDetail" name="MasterOrDetail"/>
                        <input path="mstCd" type="hidden" id="mstCd" name="mstCd"/>
                </form:form>
	<div class="tab_contents menu" style="max-width: 1500px;">
		<div id="AXSearchTargetUp" style="border-top: 1px solid #ccc;"></div>
		<br />
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 300px;"></div>
	</div>
	<div class="tab_contents menu" style="max-width: 1500px; margin-top: 10px;">
		<div id="AXSearchTargetDown" style="border-top: 1px solid #ccc;"></div>
		<br />
		<div data-ax5grid="second-grid" data-ax5grid-config="{}" style="height: 300px;"></div>
	</div>
</div>
	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />