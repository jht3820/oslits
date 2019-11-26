<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslits/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslits/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>
 
<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<style>

.historyUsrDiv:hover{background-color:#323a47;color:#fff;cursor:pointer;}

.chgDetailRightBottomDiv {display: block;min-height: 50px;width: 100%;padding: 5px 0;line-height: 20px;}
.chgDetailRightBottomDiv > span {display: block;font-size: 10pt;border-bottom: 1px solid #f9f9f9;}
.chgDetailRightBottomDiv > span > b {font-weight: bold;}
.chgDetailRightBottomDiv > span.chgDetailDiff:hover {border-bottom: 1px solid #323a47;cursor: pointer;}
.chgDetailRightBottomDiv > span.chgDetailDiff:active {color: #bbbdbf;}
.chgDetailRightBottomDiv > span.chgDetailDiff::before{font-family: "Font Awesome 5 Free";content:"\f044\00a0"}
.chgDetailRightBottomDiv > span.chgDetailFileDel::before{font-family: "Font Awesome 5 Free";content:"\f2ed\00a0"}
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
		                 {optionValue : "type",optionText : "유형", optionCommonCode:"REQ00013"} 
		                 ]
			}
		};

$(document).ready(function(){

	//목록 조회
	fnInReqNmListSet();
	Grid.init();
	
	fnReqListDataSelect("SEL",1); //소스 수정 전 임시 주석 
	$('#more').hide();
	
	$("#more").click(function(){
		$('#pageNo').val(nextPage);
		var pars = reqSearch.getParam();
	    var ajaxParam = $('form#searchFrm').serialize();

	    if(!gfnIsNull(pars)){
	    	ajaxParam += "&"+pars;
	    }
	    
		fnReqListDataSelect("MORE",nextPage,ajaxParam); 
	});
});


//요구사항 이력 목록 조회
function fnReqListDataSelect(type,pageNo,ajaxParam){
	//검색 스위치 끄기
	autoSearch = false;
	
	if(gfnIsNull(ajaxParam)){
		var endDate = gfnGetServerTime('yyyy-mm-dd');
		var nowdate = new Date(endDate);
		var startDate =gfnGetDayAgo(nowdate,7,'yyyy-mm-dd');
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
		//재 조회시 클리어
		
		       	
       	//조회 실패의 경우 리턴
       	if(data.errorYN == 'Y'){
       		jAlert(data.message);
       		return false;
       	}else{
       		var reqList = data.reqList;
	
       		if(data.isNextView==true){
       			$('#more').show();
       		}else{
       			$('#more').hide();
       		}
       		
       		nextPage = data.nextPage;
       		
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
				//prj:프로젝트OSL보드, pop:팝업OSL보드, spr:스프린트OSL보드, dal:데일리스크럼보드
				var data = {"mode": "prj", "reqId": pop_reqId}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			});
       	}
		toast.push(data.message);       	
       	
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
			if(!gfnIsNull(obj.preFlowId)){
				modifyLogStr += '		<div class="flowNmBox" style="background-color:'+obj.preFlowTitleBgColor+';color:'+obj.preFlowTitleColor+';">';
				modifyLogStr += obj.preFlowNm+'</div><div class="flowArrow"><i class="fa fa-hand-o-right"></i></div>';	
			}
			
			modifyLogStr +='	<div class="flowNmBox" style="background-color:'+obj.chgFlowTitleBgColor+';color:'+obj.chgFlowTitleColor+';">';
			modifyLogStr +=		obj.chgFlowNm+'</div>';
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
			modifyLogStr +='		<div class="mod_usrInfoBox"><span onclick="gfnAlarmOpen(\''+obj.regUsrId+'\',\''+obj.reqId+'\',\''+obj.regUsrNm+'\')"><i class="fa fa-envelope-o"></i>'+obj.regUsrNm+'</span></div>';
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
			chgDetailContent += "<span class='chgDetailDiff' num='' onclick='fnOptDiff(this)'><b>"+item.chgDetailNm+"</b>(이)가 수정되었습니다.</span></br>";
		}
		//첨부파일 추가
		else if(chgDetailType == "03"){
			chgDetailContent += "<span><li class='fas fa-file-upload'></li>&nbsp;첨부파일 <b>"+item.chgDetailVal+"</b>(이)가 추가되었습니다.</span></br>";
		}
		//첨부파일 삭제
		else if(chgDetailType == "04"){
			chgDetailContent += "<span class='chgDetailFileDel'>첨부파일 <b>"+item.chgDetailVal+"</b>(이)가 삭제되었습니다.</span></br>";
		}
				   		
			//ago Time구하기
		//var agoTime = gfnDtmAgoStr(new Date(this.regDtm).getTime());
		var agoTime = "";
			//요구사항 명
		var reqNm = "";
			
			//사용자 img
		var regUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+item.regUsrImg;

		var html  = chgDetailContent;							
		var divHtml=$('#'+item.reqId+'_'+item.chgDetailId).html();
		divHtml+= html;
		$('#'+item.reqId+'_'+item.chgDetailId).html(divHtml);
		
	});
	
}


function fnSearchBoxControl() {
	var pageID = "AXSearch";
	reqSearch = new AXSearch();
	
	var defaultEndDt = gfnGetServerTime('yyyy-mm-dd');
	var nowdate = new Date(defaultEndDt);
	var defaultStDt = gfnGetDayAgo(nowdate,7,'yyyy-mm-dd');
	
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
							
							
								
								{label:"<i class='fa fa-search'></i>&nbsp;<i>기간</i>&nbsp;&nbsp;", labelWidth:"70", type:"inputText", width:"70", key:"startDt", addClass:"secondItem readonly", valueBoxStyle:"", value:defaultStDt,
									onChange: function(){}
								},
								{label:"", labelWidth:"", type:"inputText", width:"90", key:"endDt", addClass:"secondItem readonly", valueBoxStyle:"padding-left:0px;", value:defaultEndDt,
									AXBind:{
										type:"twinDate", config:{
											align:"right", valign:"top", startTargetID:"startDt"
										}
									}
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
				                              
				                                
				                            ],onChange: function(selectedObject, value){
				                            	//fnInGridListSet(0,$('form#searchFrm').serialize()+"&pageSize="+value);
				    						}
								}, 
								{label : "",labelWidth : "",type : "button",width : "55",key : "btn_search_req",style:"float:right;",valueBoxStyle : "padding-left:10px;padding-right:5px;",value : "<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
									onclick : function() {
										/* 검색 조건 설정 후 reload */
			 							$('#pageNo').val(1);
										var pars = reqSearch.getParam();
									    var ajaxParam = $('form#searchFrm').serialize();
			
									    if(!gfnIsNull(pars)){
									    	ajaxParam += "&"+pars;
									    }
									   if( gfnTermValid(axdom("#"+ reqSearch.getItemId("startDt")),axdom("#"+ reqSearch.getItemId("endDt")),7) ){
										   fnReqListDataSelect("SEL",1,ajaxParam);   
									   }else{
										   alert('조회기간은 7일 이내로 조회 가능합니다.');
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
	<div class="tab_contents menu" style="text-align: center;" id="more">
		<span>더보기</span>
	</div>
</div>
<jsp:include page="/WEB-INF/jsp/oslits/bottom/footer.jsp" />