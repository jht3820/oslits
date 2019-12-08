<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslops/req.css'/>' type='text/css'>
 
<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<style>

.historyUsrDiv:hover{background-color:#323a47;color:#fff;cursor:pointer;}

.chgDetailRightBottomDiv {display: block;min-height: 50px;width: 100%;padding: 5px 0;line-height: 20px;}
.chgDetailRightBottomDiv > span {display: block;font-size: 10pt;border-bottom: 1px solid #f9f9f9;}
.chgDetailRightBottomDiv > span > b {font-weight: bold;}
/* .chgDetailRightBottomDiv > span.chgDetailDiff:hover {border-bottom: 1px solid #323a47;cursor: pointer;}
.chgDetailRightBottomDiv > span.chgDetailDiff:active {color: #bbbdbf;} */
.chgDetailRightBottomDiv > span.chgDetailDiff::before{font-family: "Font Awesome 5 Free";content:"\f044\00a0"}
.chgDetailRightBottomDiv > span.chgDetailFileDel::before{font-family: "Font Awesome 5 Free";content:"\f2ed\00a0"}
/* 담당자 변경*/
.chgInfoBox > .chgNmBox {float: left; box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24); color: #000; text-align: center;padding: 3px 0; min-width: 100px; margin: 0 5px;}

</style>
<script>

// 수정이력 마스터
var modMasterList = [];

var selReqChgDetailList = [];
var modDetailList = [];

var reqSearch;

var reqAllList=[];

var autoSearch = false;

var nextPage = 0;

var Grid = {
		init : function() {
			//그리드 및 검색 상자 호출
			fnSearchBoxControl(); // Search Grid 초기화 설정
			},
			columnOption : {
				req4500Search : [ 
		                 {optionValue : "rn",optionText : "전체 보기",optionAll : true},
		             
		                 {optionValue : "reqUsrNm",optionText : '담당자명'}, 
		                 {optionValue : "type",optionText : "이력 유형", optionCommonCode:"REQ00013"} 
		                 ]
			}
		};

$(document).ready(function(){

	//목록 조회
	fnInReqNmListSet();
	Grid.init();
	
	fnReqListDataSelect("SEL",1); //소스 수정 전 임시 주석 
	$('#reqHistoryMore').hide();
	
	$("#reqHistoryMore").click(function(){
		$('#pageNo').val(nextPage);
		var pars = reqSearch.getParam();
	    var ajaxParam = $('form#searchFrm').serialize();

	    if(!gfnIsNull(pars)){
	    	ajaxParam += "&"+pars;
	    }
	    
		fnReqListDataSelect("MORE",nextPage,ajaxParam); 
	});
});

// 기본 설정 날짜
var defaultEndDt;
var defaultStDt;

//요구사항 이력 목록 조회
function fnReqListDataSelect(type,pageNo,ajaxParam){ 

	//검색 스위치 끄기
	autoSearch = false;
	
	if(gfnIsNull(ajaxParam)){
		// 페이지 로드시 검색 날짜 세팅 : 현재날짜, 현재날짜 -30일
		var endDate = gfnGetServerTime('yyyy-mm-dd');
		var nowdate = new Date(endDate);
		var startDate = gfnGetDayAgo(nowdate,30,'yyyy-mm-dd');
		ajaxParam = "&endDt="+endDate+"&startDt="+startDate+"&pageNo="+pageNo+"&pageSize=10";
	}
	
	if(type!="MORE"){
		modMasterList=[];
		$("#historyDataMain").html("");	
	}
	
	modDetailList=[];
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4500/selectReq4500ListAjax.do'/>", "loadingShow":true}
			,ajaxParam );
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
 	
       	//조회 실패의 경우 리턴
       	if(data.errorYN == 'Y'){
       		jAlert(data.message);
       		return false;
       	}else{
       		var reqList = data.reqList;

       		// 데이터가 더 존재할 경우 더보기 버튼 보여준다.
       		if(data.isNextView==true){
       			$('#reqHistoryMore').show();
       		}else{
       			$('#reqHistoryMore').hide();
       		}

       		nextPage = data.nextPage;

       		// 데이터 없을경우 toast 메시지
       		if(gfnIsNull(reqList)){
				toast.push("조회된 요구사항 이력이 없습니다."); 	
	       	}
       		
       		//데이터 표시
       		$.each(reqList,function(idx,item){
       			//변경이력 - 작업흐름변경
       			if(this.type == "CHG"){
       				appendList("CHG",item);
       			//변경이력 - 검수
       			}else if(this.type == "CHK"){
       				appendList("CHK",item);
       			//수정이력
       			}else if(this.type == "MOD"){
       				//이력 담당자 명단에 있는지 확인, 없으면 push
       				makeModifyListHtml(item);     				
       			}
       		});
       		
       		setDetailhistory(modDetailList);	
      		
       		//요구사항 변경이력 클릭 이벤트
			$(".historyDataBox > .reqInfoBox").click(function(){
				var pop_reqId = $(this).attr("req-id");
				var data = {"mode": "prj", "reqId": pop_reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			});
       		
			if(!gfnIsNull(reqList)){
				toast.push(data.message); 	
	       	}
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

	/**
	 수정 이력 목록 처리
	*/
	function makeModifyListHtml(obj){
		/* 최초 행에 저장시는 비교되상이 없음   */
		if(modMasterList.length==0){
			modMasterList.push(obj.reqId+"|"+obj.chgDetailId);
			appendList('MOD',obj);
		}else{
			/* 마스터 항목이 신규로 처리될 때   */
			if(modMasterList.indexOf(obj.reqId+"|"+obj.chgDetailId) == -1){
				modMasterList.push(obj.reqId+"|"+obj.chgDetailId);
				appendList('MOD',obj);
			}
		}
		modDetailList.push(obj);
	}

	function appendList(type,obj){
   		var preTrsGrpSeq = "";
   		var modifyLogStr = "";
		var reqNo = "";
		var realDevTmStr ="";
		var chk_preFlowMainColor ;
		var chk_chgFlowMainColor ;
		
		if(!gfnIsNull(obj.reqNo)){
  			reqNo = "["+obj.reqNo+"]&nbsp;";
  		}
		var armReqNm = gfnCheckStrLength(gfnReplace(obj.reqNm, null, ""), 26);
		
		if(type=="CHG"){
		   	if(!gfnIsNull(obj.realDevTime)){
		   		var v_hour = parseInt(obj.realDevTime);
		   		var v_min = parseInt(60*(obj.realDevTime-v_hour))+"분 소요";
		   		
		   		realDevTmStr = v_hour+"시간 "+v_min;
		   	}
				
		   	modifyLogStr += '<div class="historyDataBox chgTypeBox" style="border-left: 12px solid '+obj.preFlowContentBgColor+';"  req-id="'+obj.reqId+'" usr-id="'+obj.chgUsrId+'">';
		   	modifyLogStr +='	<div class="usrInfoBox" style="border-left: 12px solid '+obj.chgFlowContentBgColor+';"><span onclick="gfnAlarmOpen(\''+obj.chgUsrId+'\',\''+obj.reqId+'\',\''+armReqNm+'\')"><i class="fa fa-envelope-o"></i>'+obj.chgUsrNm+'</span></div>';
		   	modifyLogStr +='		<div class="reqInfoBox" req-id="'+obj.reqId+'" log-seq="'+obj.wkTmSeq+'">';
		   	modifyLogStr +='			<span>'+reqNo+armReqNm+'</span>';
		   	modifyLogStr +='		</div>';
		   	modifyLogStr +='		<div class="dtmInfoBox" style="border-left: 12px solid '+obj.chgFlowContentBgColor+';"><span>'+new Date(obj.regDtm).format("yyyy-MM-dd HH:mm:ss")+'</span></div>';
		   	modifyLogStr +='		<div class="chgInfoBox">';
			
	   		// 요구사항 접수일 경우
	   		if(obj.reqChgType == "03"){
	   			modifyLogStr +='	<div class="flowNmBox" style="background-color:'+obj.chgFlowTitleBgColor+';color:'+obj.chgFlowTitleColor+';">';
				modifyLogStr +=		obj.chgFlowNm+'</div>';
	   		}
	   		// 작업흐름 변경일 경우
	   		else if(!gfnIsNull(obj.preFlowId) && obj.reqChgType == "01"){
				modifyLogStr += '		<div class="flowNmBox" style="background-color:'+obj.preFlowTitleBgColor+';color:'+obj.preFlowTitleColor+';">';
				modifyLogStr += obj.preFlowNm+'</div><div class="flowArrow"><i class="fa fa-hand-o-right"></i></div>';	
				modifyLogStr +='	<div class="flowNmBox" style="background-color:'+obj.chgFlowTitleBgColor+';color:'+obj.chgFlowTitleColor+';">';
				modifyLogStr +=		obj.chgFlowNm+'</div>';
			}
		   	// 담당자 변경일경우
		   	else if(obj.reqChgType == "02"){
		   		modifyLogStr +='<div class="chgNmBox" title="변경전 담당자 : '+obj.preChargerNm+'">';
				modifyLogStr +='	<i class="fa fa-user-edit"></i> '+obj.preChargerNm;	
				modifyLogStr +='</div>';	
				modifyLogStr +='<div class="flowArrow"><i class="fa fa-hand-o-right"></i></div>';	
				modifyLogStr +='<div class="chgNmBox" title="변경후 담당자 : '+obj.chgChargerNm+'">';
				modifyLogStr +='	<i class="fa fa-user-edit"></i> '+obj.chgChargerNm;
				modifyLogStr +='</div>';
		   	}
			
			modifyLogStr +='</div>';

		}else if(type=="CHK"){
			
			var addStr = "";
			var addContentStr = "";
				//검수 승인&거부 일시
			var chkDtm = new Date(obj.modifyDtm).format("yyyy-MM-dd HH:mm:ss");
				
			if(obj.signCd == '01'){	//검수 요청
				//색상 지정
					   	
				addContentStr = '<div class="flowNmBox" style="background-color:'+obj.signFlowTitleBgColor+';color:'+obj.signFlowTitleColor+'; ">'
				+'[결재 요청]  </div>' 
				+ '<div class="flowNmBox" style="background-color:'+obj.signFlowTitleBgColor+';color:'+obj.signFlowTitleColor+'; ">'
				+' [요청자 : '+obj.regUsrNm+' ]  </div>' 
				+ '<div class="flowNmBox" style="background-color:'+obj.signFlowTitleBgColor+';color:'+obj.signFlowTitleColor+'; ">'
				+' [결재자 : '+obj.signUsrNm+' ] </div>'
				;
			}else if(obj.signCd == '02'){	//검수 승인
					//색상 지정
				addContentStr = '<div class="flowNmBox" style="background-color:'+obj.signFlowTitleBgColor+';color:'+obj.signFlowTitleColor+';" >'
				+'[결재 승인] </div>' 
				+ '<div class="flowNmBox" style="background-color:'+obj.signFlowTitleBgColor+';color:'+obj.signFlowTitleColor+';" >'
				+' [요청자 : '+obj.regUsrNm+'] </div>' 
				;					
	
			}else if(obj.signCd == '03'){	//검수 거부
			
				addContentStr = '<div class="flowNmBox" style="background-color:'+obj.signFlowTitleBgColor+';color:'+obj.signFlowTitleColor+'; ">'
				+'[결재 반려] </div>' 
				+ '<div class="flowNmBox" style="background-color:'+obj.signFlowTitleBgColor+';color:'+obj.signFlowTitleColor+'; ">'
				+'[요청자 : '+obj.regUsrNm+'] </div>' 
				+ '<div class="chkReject">'+obj.signRejectCmnt+'</div>';
			}
			
			modifyLogStr +='<div class="historyDataBox chgTypeBox" style="border-left: 12px solid '+obj.signFlowTitleBgColor+';"  req-id="'+obj.reqId+'" usr-id="'+obj.regUsrId+'">';
			modifyLogStr +='		<div class="usrInfoBox" style="border-left: 12px solid '+obj.signFlowTitleBgColor+';"><span onclick="gfnAlarmOpen(\''+obj.regUsrId+'\',\''+obj.reqId+'\',\''+armReqNm+'\')"><i class="fa fa-envelope-o"></i>'+obj.regUsrNm+'</span></div>';
			modifyLogStr +='		<div class="reqInfoBox" req-id="'+obj.reqId+'" log-seq="'+obj.chkSeq+'">';
			modifyLogStr +='			<span>'+reqNo+armReqNm+'</span>';
			modifyLogStr +='		</div>';
			modifyLogStr +='		<div class="dtmInfoBox" style="border-left: 12px solid '+obj.signFlowTitleBgColor+';"><span>'+new Date(obj.regDtm).format("yyyy-MM-dd HH:mm:ss")+'</span></div>';
			modifyLogStr +='		<div class="chgInfoBox">';
										
			modifyLogStr += addContentStr ;
										
		    modifyLogStr +='</div>';
			
		
		}else if(type=="MOD"){
			modifyLogStr +='<div class="historyDataBox modifyTypeBox"  req-id="'+obj.reqId+'" usr-id="'+obj.regUsrId+'" chg-detail-id="'+obj.chgDetailId+'" >';
			modifyLogStr +='	<div class="usrDtmInfoBox mod_usrDtmInfoBox">';
			modifyLogStr +='		<div class="mod_usrInfoBox"><span onclick="gfnAlarmOpen(\''+obj.regUsrId+'\',\''+obj.reqId+'\',\''+armReqNm+'\')"><i class="fa fa-envelope-o"></i>'+obj.regUsrNm+'</span></div>';
			modifyLogStr +='		<div class="mod_dtmInfoBox"><span>'+new Date(obj.regDtm).format("yyyy-MM-dd HH:mm:ss")+'</span></div>';
			modifyLogStr +='	</div><div class="reqInfoBox mod_reqInfoBox" req-id="'+obj.reqId+'">';
			modifyLogStr +='		<span>'+reqNo+armReqNm+'</span>';
			modifyLogStr +='	</div><div class="chgDetailRightBottomDiv" id ="'+obj.reqId+'_'+obj.chgDetailId+'" >';
			modifyLogStr += '	</div></div></div>';
			
		}
		$(".historyDataMain").append(modifyLogStr);
	}

function setDetailhistory(list){
	
	$.each(list,function(idx,item){
		var chgDetailType = item.chgDetailType;
		var chgDetailContent ="";
		//요구사항 수정+ 요구사항 추가 항목 수정
		if(chgDetailType == "01" || chgDetailType == "02"){
			chgDetailContent += "<span class='chgDetailDiff' num='' onclick='fnOptDiff(this)'><b>"+item.chgDetailNm+"</b>(이)가 수정되었습니다.</span>";
		}
		//첨부파일 추가
		else if(chgDetailType == "03"){
			chgDetailContent += "<span><li class='fas fa-file-upload'></li>&nbsp;첨부파일 <b>"+item.chgDetailVal+"</b>(이)가 추가되었습니다.</span>";
		}
		//첨부파일 삭제
		else if(chgDetailType == "04"){
			chgDetailContent += "<span class='chgDetailFileDel'>첨부파일 <b>"+item.chgDetailVal+"</b>(이)가 삭제되었습니다.</span>";
		}

		var html  = chgDetailContent;							
		var divHtml=$('#'+item.reqId+'_'+item.chgDetailId).html();
		divHtml+= html;
		$('#'+item.reqId+'_'+item.chgDetailId).html(divHtml);
		
	});
	
}


function fnSearchBoxControl() {
	var pageID = "AXSearch";
	reqSearch = new AXSearch();

	// 기본 세팅 날짜는 현재 날짜, 현재날짜 -30일
	defaultEndDt = gfnGetServerTime('yyyy-mm-dd');
	var nowdate = new Date(defaultEndDt);
	defaultStDt = gfnGetDayAgo(nowdate, 30, 'yyyy-mm-dd');
	
	var fnObjSearch = {
		pageStart : function() {
			//검색도구 설정 01 ---------------------------------------------------------
			reqSearch.setConfig({
				targetID : "AXSearchTarget",
				theme : "AXSearch",
				rows : [
				   {display : true,
					addClass : "",
					style : "",
					list : [
								{label:"<i class='fa fa-search'></i>&nbsp;<i>기간</i>&nbsp;&nbsp;", labelWidth:"70", type:"inputText", width:"120", key:"startDt", addClass:"secondItem readonly", valueBoxStyle:"", value:defaultStDt,
									onChange: function(){}
								},
								{label:"", labelWidth:"", type:"inputText", width:"120", key:"endDt", addClass:"secondItem readonly", valueBoxStyle:"padding-left:0px;", value:defaultEndDt
								},
								
								{label : "<i>요구사항명</i>&nbsp;",labelWidth : "",type : "inputText",width : "150",key : "reqNm",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:10px;",value : ""
									, onkeyup:function(e){
										if(e.keyCode == '13' ){
											axdom("#" + reqSearch.getItemId("btn_search_req")).click();
										}else{
											if(  axdom("#" + reqSearch.getItemId("reqNm")).val() =="" ){
												$('#reqId').val("");
											}
										}
									}
								}
								,
								{label : "</i>&nbsp;",labelWidth : "50",type : "selectBox",width : "",key : "searchSelect",addClass : "",valueBoxStyle : "",value : "all",
									options : Grid.columnOption.req4500Search,
										onChange : function(selectedObject,value) {
										
											//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
											if (!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true) {
												axdom("#"+ reqSearch.getItemId("searchTxt")).attr("readonly","readonly");
												axdom("#"+ reqSearch.getItemId("searchTxt")).val('');
											} else {
												axdom("#"+ reqSearch.getItemId("searchTxt")).removeAttr("readonly");
											}
							
											//공통코드 처리 후 select box 세팅이 필요한 경우 사용
											if (!gfnIsNull(selectedObject.optionCommonCode)) {
												gfnCommonSetting(reqSearch,selectedObject.optionCommonCode,"searchCd","searchTxt");
											}else {
												//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
												axdom("#"+ reqSearch.getItemId("searchTxt")).show();
												axdom("#"+ reqSearch.getItemId("searchCd")).hide();
											}
										}
									},
								
								{label : "",labelWidth : "",type : "inputText",width : "150",key : "searchTxt",addClass : "secondItem sendBtn",valueBoxStyle : "padding-left:0px;",value : ""
									, onkeyup:function(e){
										if(e.keyCode == '13' ){
											axdom("#" + reqSearch.getItemId("btn_search_req")).click();
										}
									}
								},
								{label : "",labelWidth : "",type : "selectBox",width : "100",key : "searchCd",addClass : "selectBox",valueBoxStyle : "padding-left:0px;",value : "01",options : []}
								,
								{label:"<i class='fas fa-list-ol'></i>&nbsp;목록 수&nbsp;", labelWidth:"60", type:"selectBox", width:"", key:"pageSize", addClass:"", valueBoxStyle:"", value:"10",
									options:[
									         	{optionValue:10, optionText:"10"},
				                                {optionValue:20, optionText:"20"},
				                                {optionValue:30, optionText:"30"},
				                                {optionValue:50, optionText:"50"},
				                                {optionValue:100, optionText:"100"},
				                                {optionValue:100, optionText:"150"},
				                                {optionValue:100, optionText:"200"}
				                                
				                            ],onChange: function(selectedObject, value){
				                            	//fnInGridListSet(0,$('form#searchFrm').serialize()+"&pageSize="+value);
				    						}
								}, 
								{label : "",labelWidth : "",type : "button",width : "55",key : "btn_search_req",style:"float:right;",valueBoxStyle : "padding-left:10px;padding-right:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
									onclick : function() {
										
										// 조회시 페이지번호 1 세팅
			 							$('#pageNo').val(1);
										
										// 검색 파라미터 세팅
										var pars = reqSearch.getParam();
									    var ajaxParam = $('form#searchFrm').serialize();
			
									    if(!gfnIsNull(pars)){
									    	ajaxParam += "&"+pars;
									    }
									    
									   	// 조회기간 체크, 조회기간은 30일
									   	if( gfnTermValid(axdom("#"+ reqSearch.getItemId("startDt")), axdom("#"+ reqSearch.getItemId("endDt")), 30) ){
											fnReqListDataSelect("SEL",1,ajaxParam);   
									   	}else{
										  	jAlert("조회기간은 30일 이내로 조회 가능합니다.", "알림");
									   	}
									}
								}	
						]
					}
				]
			});
		}
	};

	jQuery(document.body).ready(
		function() {
			fnObjSearch.pageStart();
				//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			axdom("#" + reqSearch.getItemId("searchTxt")).attr("readonly", "readonly");

				//공통코드 selectBox hide 처리
			axdom("#" + reqSearch.getItemId("searchCd")).hide();

			// 검색상자에서 시작날짜, 종료날짜 input의 ID값을 가져온다. 
			var startDtId = reqSearch.getItemId("startDt");
			var endDtId = reqSearch.getItemId("endDt");
			
			// 날짜 input readonly 추가, CSS 수정
			axdom("#" + startDtId).attr("readonly", "readonly");
			axdom("#" + endDtId).attr("readonly", "readonly");
			axdom("#" + startDtId).css("box-shadow", "none");
			axdom("#" + endDtId).css("box-shadow", "none");
			
			// daterangepicker 세팅
			gfnCalRangeSet(startDtId, endDtId);
				
			//버튼 권한 확인
			fnBtnAuthCheck(reqSearch);

	});
}

function fnInReqNmListSet(){
 	/* 그리드 데이터 가져오기 */
 	//파라미터 세팅
 	
 	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4500/selectReq4500ReqNmListAjax.do'/>","loadingShow":true}
			);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		reqAllList = data.reqAllList;		
		fnInSetAutoComplete(axdom("#" + reqSearch.getItemId("reqNm")));
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



function fnInSetAutoComplete(obj){
	//요구사항 자동완성
	$(obj).autocomplete({
        source: function( request, response ) {
	        response( $.map( reqAllList, function( item ) {
	        	var reqIdStr = $(obj).val();
	        	var subStr = reqIdStr.substring(reqIdStr.lastIndexOf(',')+1,reqIdStr.length).trim();
	            if (!gfnIsNull(item.reqNm) && item.reqNm.toLowerCase().indexOf(subStr.toLowerCase()) >= 0){
	            	//검색 스위치 켜기
					autoSearch = true;

	                return {
	                    label: "["+item.reqId+"]"+item.reqNm.toLowerCase().replace(subStr.toLowerCase(),"<span style='font-weight:bold;color:Blue;'>" + subStr.toLowerCase() + "</span>"),
	                    value: item.reqNm,
	                    reqId: item.reqId,
	                    prjId: item.prjId
	                }
	            }
	        }));
        },
        minLength: 1,
        select: function( event, ui ) {
                $("#reqId").val(ui.item.reqId);
        },
        open: function() {
            $( this ).autocomplete("widget").width("689px").css({"z-index":191998,"font-size":"0.8em"});
            $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
        },
        close: function() {
            $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
        },
        error: function(xhr, ajaxOptions, thrownError){ alert(thrownError);  alert(xhr.responseText); }
    })
    .data('uiAutocomplete')._renderItem = function( ul, item ) {
        return $( "<li style='cursor:hand; cursor:pointer;'></li>" )
            .data( "item.autocomplete", item )
            .append("<a>"  + unescape(item.label) + "</a>")
        .appendTo( ul );
    };
}

</script>
<div class="main_contents" style="height: auto;">
	<div class="req_title">${sessionScope.selMenuNm}</div>
	<div class="tab_contents menu" style="max-width: 1500px;">
		<form id="searchFrm" name="searchFrm" method="post" onsubmit="return false">
			<input type="hidden" id="reqId" name="reqId" >
			<input type="hidden" id="pageNo" name="pageNo" >
		</form>
		<div id="AXSearchTarget" style="border-top: 1px solid #ccc;"></div>
		
	</div>
	<div class="tab_contents menu historyDataMain" id="historyDataMain">
	</div>
	<div class="tab_contents menu" style="text-align: center;" id="reqHistoryMore">
		<span>더보기 <i class="fas fa-angle-down"></i></span>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />