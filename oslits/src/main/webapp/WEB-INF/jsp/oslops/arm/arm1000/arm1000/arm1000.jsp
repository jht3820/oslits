<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<html lang="ko">
<header>
	<title>OpenSoftLab</title>
	<link rel='stylesheet' href='<c:url value='/css/oslops/arm.css'/>' type='text/css'>
</header>
<style>

</style> 
<script type="text/javascript">

var alarmGrid;
var prjGrpId = "${sessionScope.selPrjGrpId}";

$(document).ready(function() {
	//쪽지 작성 페이지 체크
	var sendChk = "${sendChk}";
	if(sendChk == "true"){
		fnArmWriteOnOff(null,true);
	}else{
		$(".armWriteMain").hide();
	}
	fnSearchBoxControl_alarm();
	fnAxGrid5View_alarm();
	
	//레이어팝업 닫기
	$(".arm_pop_close").click(function(){
		gfnLayerPopupClose();
	});
	
	//받는이 검색 걸기
	$("#btn_armUser_select").click(function() {
		gfnCommonUserPopup( $('#writeSendUsrNm').val() ,false,function(objs){
			if(objs.length>0){
				$('#writeSendUsrId').val(objs[0].usrId);
				$('#writeSendUsrNm').val(objs[0].usrNm);
			}
		});
	});
	
	$('#writeSendUsrNm').keyup(function(e) {
		if($('#writeSendUsrNm').val() == ""){
			$('#writeSendUsrId').val("");
		}
		if(e.keyCode == "13" ){
			$('#btn_armUser_select').click();
		}
	});
});

//axisj5 그리드
function fnAxGrid5View_alarm(){
	alarmGrid = new ax5.ui.grid();
 
        alarmGrid.setConfig({
            target: $('[data-ax5grid="alarm-grid"]'),
            showRowSelector:true,
            sortable:true,
            header: {align:"center"},
            frozenColumnIndex: 0,
            columns: [
				{key: "sendUsrNm", label: "보낸이", width: 100, align: "center"},
                {key: "viewCheck", label: "상태", width: 80, align: "center",
					formatter:function(){
						var armIcon = "fa-envelope-open-o";
						//안읽은 상태
						if(this.item.viewCheck == "02"){
							armIcon = "fa-envelope";
						}
						return "<i class='fa fa-lg "+armIcon+"'></i>"
					}},
	            {key: "title", label: "제목", width: 640, align: "center"},
	            {key: "regDtm", label: "보낸 일자", width: 150, align: "center"
	            	,formatter:function(){return new Date(this.item.regDtm).format('yyyy-MM-dd hh:mm:ss')}},
            ],
            body: {
                align: "center",
                columnHeight: 35,
                onDBLClick:function () {
                	//쪽지 읽기 창 오픈
                	fnArmViewOnOff(this.item,true);
                }
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
                   fnInGridListSet_alarm(this.page.selectPage,alarm_Search.getParam());
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
                    {type: "reading", label: "읽기", icon:"<i class='fa fa-envelope' aria-hidden='true'></i>"},
                    {type: "readed", label: "읽음 처리", icon:"<i class='fa fa-envelope-open' aria-hidden='true'></i>"},
                    {type: "delete", label: "삭제", icon:"<i class='fa fa-trash' aria-hidden='true'></i>"},
                    {divide: true},
                    {type: "reply", label: "답장", icon:"<i class='fa fa-mail-reply' aria-hidden='true'></i>"},
                    {type: "sendUsrSearch", label: "보낸사람 검색", icon:"<i class='fa fa-search' aria-hidden='true'></i>"},
                ],
                popupFilter: function (item, param) {
                	var selItem = alarmGrid.list[param.doindex];
                	//선택 개체 없는 경우 중지
                	if(typeof selItem == "undefined"){
                		return false;
                	}
                	return true;
                },
                onClick: function (item, param) {
                	var selItem = alarmGrid.list[param.doindex];
                	
                    //읽기
                    if(item.type == "reading"){
						//쪽지 읽기 창 오픈
                		fnArmViewOnOff(param.item,true);
                    //읽음 처리
                    }else if(item.type == "readed"){
                    	var chkList = [];
                    	chkList.push({"armId":param.item.armId,"viewCheck":param.item.viewCheck});
                    	fnUpdateAlarm(chkList,"viewCheck");

					//삭제
                    }else if(item.type == "delete"){
                    	jConfirm("삭제 하시겠습니까?", "알림창", function( result ) {
                    		if(result){
		                    	var chkList = [];
		                    	chkList.push({"armId":param.item.armId});
		                    	fnUpdateAlarm(chkList,"delCheck");
                    		}
                    	});
                    //답장
                    }else if(item.type == "reply"){
                    	var data = {sendUsrId:param.item.sendUsrId,sendUsrNm:param.item.sendUsrNm};
                    	fnArmWriteOnOff(data,true);
                    //보낸사람 검색
                    } else if(item.type == "sendUsrSearch"){
                    	//상위그룹 Id 세팅
                    	axdom("#" + alarm_Search.getItemId("searchSelect")).val('sendUsrNm');
                    	axdom("#" + alarm_Search.getItemId("searchTxt")).removeAttr("readonly");
                    	axdom("#" + alarm_Search.getItemId("searchTxt")).val(param.item.sendUsrNm);
                    	
                    	//조회 클릭
                    	axdom("#" + alarm_Search.getItemId("arm_button_search")).click();
                    	
                    } 
                    alarmGrid.contextMenu.close();
                    
                }
            }
        });
      //읽지 않은 상태의 쪽지 검색 체크
		var viewCheck = "${viewChk}";
		if(viewCheck != "true"){
	        //그리드 데이터 불러오기
	 		fnInGridListSet_alarm();
        }
}
//그리드 데이터 넣는 함수
function fnInGridListSet_alarm(_pageNo,ajaxParam){
	
   	/* 그리드 데이터 가져오기 */
   	//파라미터 세팅
   	if(gfnIsNull(ajaxParam)){
 			ajaxParam = $('form#alarm_searchFrm').serialize();
 		}
   	
   	//페이지 세팅
   	if(!gfnIsNull(_pageNo)){
   		ajaxParam += "&pageNo="+_pageNo;
   	}else if(typeof alarmGrid.page.currentPage != "undefined"){
   		ajaxParam += "&pageNo="+alarmGrid.page.currentPage;
   	}
   	
    //AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/arm/arm1000/arm1000/selectArm1000ListAjax.do'/>","loadingShow":true}
			,ajaxParam);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		var list = data.list;
		var page = data.page;
		
	   	alarmGrid.setData({
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
function fnSearchBoxControl_alarm(){
	alarm_Search = new AXSearch();

	var fnAlarmSearch = {
		pageStart: function(){
			//검색도구 설정 01 ---------------------------------------------------------
			alarm_Search.setConfig({
				targetID:"AXAlarmSearchTarget",
				theme : "AXSearch",
				rows:[{display:true, addClass:"", style:"", list:[
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"arm_delete_req",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-trash-alt' aria-hidden='true'></i>&nbsp;<span>삭제</span>",
						onclick:function(){
							var chkList = alarmGrid.getList('selected');
							if(gfnIsNull(chkList)){
								jAlert("선택한 쪽지가 없습니다.","알림창");
								return false;
							}
							jConfirm("삭제 하시겠습니까?", "알림창", function( result ) {
								if(result){
									
									fnUpdateAlarm(chkList,"delCheck");
								}
							});
						}},
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"arm_update",valueBoxStyle:"padding:5px;", value:"<i class='fa fa-envelope-o' aria-hidden='true'></i>&nbsp;<span>읽음</span>",
						onclick:function(){
							var chkList = alarmGrid.getList('selected');
							if(gfnIsNull(chkList)){
								jAlert("선택한 쪽지가 없습니다.","알림창");
								return false;
							}
							fnUpdateAlarm(chkList,"viewCheck");
						}},
						{label:"", labelWidth:"", type:"button", width:"55",style:"float:right;", key:"arm_button_search",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
						onclick:function(){
							/* 검색 조건 설정 후 reload */
 							var pars = alarm_Search.getParam();
						    var ajaxParam = $('form#alarm_searchFrm').serialize();

						    if(!gfnIsNull(pars)){
						    	ajaxParam += "&"+pars;
						    }
							
				            fnInGridListSet_alarm(0,ajaxParam);
				            
				            //폼 데이터 변경
							$('#searchSelect').val(axdom("#" + alarm_Search.getItemId("searchSelect") ).val());
							$('#searchCd').val(axdom("#" + alarm_Search.getItemId("searchCd")).val() );
							$('#searchTxt').val(axdom("#" + alarm_Search.getItemId("searchTxt")).val() );
						}},
					    {label:"<i class='fa fa-search'></i>&nbsp;", labelWidth:"30", type:"selectBox", width:"", key:"searchSelect", addClass:"", valueBoxStyle:"", value:"all",
							options:[
	                                {optionValue:"0", optionText:"전체 보기",optionAll:true},
	                                {optionValue:'sendUsrNm', optionText:'보낸 사람'},
	                                {optionValue:'title', optionText:'제목'},
	                                {optionValue:"content", optionText:"내용"},
	                                {optionValue:'reqId', optionText:'요구사항 ID'},
	                                {optionValue:'viewCheck', optionText:'읽음 상태', optionCommonCode:"CMM00001"}
	                            ],onChange: function(selectedObject, value) {
	                            	//선택 값이 전체목록인지 확인 후 입력 상자를 readonly처리
	    							if(!gfnIsNull(selectedObject.optionAll) && selectedObject.optionAll == true){
										axdom("#" + alarm_Search.getItemId("searchTxt")).attr("readonly", "readonly");	
										axdom("#" + alarm_Search.getItemId("searchTxt")).val('');	
									}else{
										axdom("#" + alarm_Search.getItemId("searchTxt")).removeAttr("readonly");
									}
	    							
	    							//공통코드 처리 후 select box 세팅이 필요한 경우 사용
									if( !gfnIsNull(selectedObject.optionCommonCode) ){
										gfnCommonSetting(alarm_Search,selectedObject.optionCommonCode,"searchCd","searchTxt");
									} else {
										//공통코드 처리(추가 selectbox 작업이 아닌 경우 type=text를 나타낸다.)
										axdom("#" + alarm_Search.getItemId("searchTxt")).show();
										axdom("#" + alarm_Search.getItemId("searchCd")).hide();
									}
	    						}
						},
						{label:"", labelWidth:"", type:"inputText", width:"150", key:"searchTxt", addClass:"secondItem sendBtn", valueBoxStyle:"padding-left:0px;", value:"",
							onkeyup:function(e){
								if(e.keyCode == '13' ){
									axdom("#" + alarm_Search.getItemId("arm_button_search")).click();
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
		                            	fnInGridListSet_alarm(0,$('form#alarm_searchFrm').serialize()+"&"+alarm_Search.getParam());
		    						}
						}
					  ]}
				]
			});
		}
	};
	
	jQuery(document.body).ready(function(){
		fnAlarmSearch.pageStart();
		//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
		axdom("#" + alarm_Search.getItemId("searchTxt")).attr("readonly", "readonly");
		
		//공통코드 selectBox hide 처리
		axdom("#" + alarm_Search.getItemId("searchCd")).hide();
		
		//읽지 않은 상태의 쪽지 검색 체크
		var viewCheck = "${viewChk}";
		if(viewCheck == "true"){
			axdom("#" + alarm_Search.getItemId("searchSelect")).val("viewCheck");
			gfnCommonSetting(alarm_Search,"CMM00001","searchCd","searchTxt");
            axdom("#" + alarm_Search.getItemId("searchCd")).val("02");
			axdom("#" + alarm_Search.getItemId("arm_button_search")).click();
		}
	});
}

//쪽지 수정(삭제 또는 읽음 처리)
function fnUpdateAlarm(chkList,checkType){
	var params = "";
	
	var chkCount = 0;
	var viewCheckStr = false;
	
	$(chkList).each(function(idx, val){
		//이미 읽은상태인 쪽지는 넘어감
		if(checkType == "viewCheck" && val.viewCheck == "01"){
			viewCheckStr = true;
			return true;
		}
		
		if(chkCount==0){
			params ="armId="+val.armId;
		}else{
			params +="&armId="+val.armId;
		}
		
		chkCount++;
	});
	if(viewCheckStr && chkCount == 0){
		toast.push("이미 읽은 상태의 쪽지입니다.");
		return false;
	}

	var paramValue = "Y";
	//type별 값
	if(checkType == "viewCheck"){
		paramValue = "01";
	}
	
	params += "&"+checkType+"="+paramValue;
	
    //AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/arm/arm1000/arm1000/updateArm1000AlarmInfo.do'/>","loadingShow":true}
			,params);
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		if(data.errorYn == "N"){
			fnInGridListSet_alarm(alarmGrid.page.currentPage,alarm_Search.getParam());
			
			//쪽지 카운트 재 세팅
			var alarmCnt = data.alarmCnt;
			
			var viewNum = parseInt(alarmCnt.viewCnt);
			
			//읽지 않은 쪽지 존재시 Class추가
			if(viewNum > 0 && !$("#dtAlarmMsg").hasClass("newAlarm")){
				$("#dtAlarmMsg").addClass("newAlarm");
			}else if($("#dtAlarmMsg").hasClass("newAlarm")){
				$("#dtAlarmMsg").removeClass("newAlarm");
			}
			
			//읽지 않은 쪽지 갯수 표시
			$("#spanAlarmMsg").html(viewNum);
			
			//하위 영역 세팅
			$("#alarm_allNumSpan").html(alarmCnt.allCnt);
			$("#alarm_viewNumSpan").html(viewNum);
		}
		toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
}

//쪽지 읽기
function fnArmViewOnOff(data,onOff){
	//쪽지 읽기창 닫는 경우
	if(!onOff){
		$(".armViewMain").hide();
		$("#headerTitle").text("쪽지 관리");
		return false;
	}
	$(".armWriteMain").hide();
	$("#headerTitle").text("쪽지 읽기");
	
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/arm/arm1000/arm1000/selectArm1000InfoAjax.do'/>","loadingShow":true}
			,{"armId":data.armId,"viewCheck":data.viewCheck});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		if(data.errorYn == "N"){
			//읽음 처리 확인
			if(data.viewAction == "01"){
				fnInGridListSet_alarm();
			
				//쪽지 카운트 재 세팅
				var alarmCnt = data.alarmCnt;
				
				var viewNum = parseInt(alarmCnt.viewCnt);
				
				//읽지 않은 쪽지 존재시 Class추가
				if(viewNum > 0 && !$("#dtAlarmMsg").hasClass("newAlarm")){
					$("#dtAlarmMsg").addClass("newAlarm");
				}else if($("#dtAlarmMsg").hasClass("newAlarm")){
					$("#dtAlarmMsg").removeClass("newAlarm");
				}
				
				//읽지 않은 쪽지 갯수 표시
				$("#spanAlarmMsg").html(viewNum);
				
				//하위 영역 세팅
				$("#alarm_allNumSpan").html(alarmCnt.allCnt);
				$("#alarm_viewNumSpan").html(viewNum);
			}
			var armInfo = data.armInfo;
			
			//데이터 세팅
			$("span#sendUsrNm").html(armInfo.sendUsrNm);
			$("span#regDtm").html(new Date(armInfo.regDtm).format("yyyy-MM-dd HH:mm:ss"));
			$("div#title").html(armInfo.title);
			$("span#content").html(armInfo.content);
			$("div#armOpenDelBtn").attr("del-arm-id",armInfo.armId);
			$("div#armOpenReplyBtn").attr("reply-usr-id",armInfo.sendUsrId);
			$("div#armOpenReplyBtn").attr("reply-usr-nm",armInfo.sendUsrNm);
			$("div.armViewHeaderRight").html(armInfo.reqIds);
			
			//요구사항 프로젝트 다른지 확인
			var tagList = $("span[name=tagReqId]");
			$.each(tagList, function(){
				if($(this).attr("prj-id") != "${sessionScope.selPrjId}"){
					$(this).css({"border":"1px solid red","color":"#ccc"});
				}	
			});
			
			//쪽지 읽기창 오픈
			$(".armViewMain").show();
		}
		toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
}

//쪽지 오픈 상태에서 삭제 처리
function fnArmInfoDel(thisObj){
	jConfirm("삭제 하시겠습니까?", "알림창", function( result ) {
   		if(result){
	     	//쪽지오픈창 닫기
			$(".armViewMain").hide();
			$("#headerTitle").text("쪽지 관리");
			
			//삭제처리
			var chkList = [];
		   	chkList.push({"armId":$(thisObj).attr("del-arm-id"),"delCheck":"Y"});
		   	fnUpdateAlarm(chkList,"delCheck");
   		}
   	});
}

//쪽지 작성창 오픈
function fnArmWriteOnOff(data, onOff){
	//쪽지 쓰기창 닫는 경우
	if(!onOff){
		$(".armWriteMain").hide();
		$("#headerTitle").text("쪽지 관리");
		return false;
	}

	//입력 데이터 초기화
	$("input#reqIds").val('');
	$("#writeSendUsrId").val('');
	$("#writeSendUsrNm").val('');
	$("input#title").val('');
	$("textarea#content").val('');
	$("#reqIdSpan").html('');
	
	$(".armViewMain").hide();
	
	//답장일경우 기초 데이터 세팅
	if(!gfnIsNull(data)){
		//select 강제 세팅
		$("#writeSendUsrId").val(data.sendUsrId);
		$("#writeSendUsrNm").val(data.sendUsrNm);
		$("#headerTitle").text("쪽지 답장");
	}else{
		$("#headerTitle").text("쪽지 작성");
		
		//사용자 지정
		var arm_reqId = "${arm_reqId}";
		var arm_reqNm = "${arm_reqNm}";
		var reqPrjId = "${reqPrjId}";
		
		//요구사항 링크
		if(!gfnIsNull(arm_reqId)){
			//타 프로젝트가 아닌 경우, 현재 선택된 프로젝트
			if(gfnIsNull(reqPrjId)){
				reqPrjId = "${sessionScope.selPrjId}";
			}
			$("#reqIdSpan").append("<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+reqPrjId+"' req-id='"+arm_reqId+"' onclick='fnSpanDelete(this)'>"+arm_reqNm+"<li class='fa fa-times'></li></span>");
		}
		//자기자신 ID지우기
		//$("#sendUsrId>option[value=${sessionScope.loginVO.usrId}]").remove();
	}
	var autoData = [];
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/arm/arm1000/arm1000/selectArm1000AutoCompleReqAjax.do'/>","loadingShow":true});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		if(data.errorYn == "N"){
			autoData = data.reqList;
			//요구사항 자동완성
			$("input#searchReqIds").autocomplete({
		        source: function( request, response ) {
			        response( $.map( autoData, function( item ) {
			        	var reqIdStr = $("input#searchReqIds").val();
			        	var subStr = reqIdStr.substring(reqIdStr.lastIndexOf(',')+1,reqIdStr.length).trim();
			            if (item.reqNm.toLowerCase().indexOf(subStr.toLowerCase()) >= 0){
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
		                $("#reqIdSpan").append("<span name='tagReqId' id='tagReqId' prj-grp-id='"+prjGrpId+"' prj-id='"+ui.item.prjId+"' req-id='"+ui.item.reqId+"' onclick='fnSpanDelete(this)'>"+ui.item.value+"<li class='fa fa-times'></li></span>");
		        },
		        open: function() {
		            $( this ).autocomplete("widget").width("689px").css({"z-index":191998,"font-size":"0.8em"});
		            $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
		        },
		        close: function() {
		            $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		            // 쪽지 작성 후 쪽지의 요구사항 태그 검색 부분의 검색어 초기화
		            $("#searchReqIds").val("");
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
	});
	
	//AJAX 전송
	ajaxObj.send();

	//쪽지 쓰기창 보이기
	$(".armWriteMain").show();
}

//쪽지 작성 완료
function fnArmWriteAction(){
	var sendUsrId = $("#writeSendUsrId").val();
	var title = $("input#title").val();
	var content = $("textarea#content").val().replace(/\n/gi,"</br>");
	var reqIds = $("#reqIdSpan").html();
	
	if(gfnIsNull(sendUsrId)){
		jAlert("프로젝트에 배정된 사용자가 존재하지 않습니다.");
		return false;
	}
	else if(gfnIsNull(title)){
		jAlert("쪽지 제목을 입력해주세요.");
		return false;
	}
	else if(gfnIsNull(content)){
		jAlert("쪽지 내용을 입력해주세요.");
		return false;
	}
	else if(reqIds.length > 4000){
		jAlert("요구사항 태그가 너무 많습니다.");
		return false;
	}
	//요구사항 태그 함수명 변경
	reqIds = reqIds.replace(/fnSpanDelete/gi,"fnSpanReqDetailOpen");
	
	//아이콘 변경
	reqIds = reqIds.replace(/fa-times/gi,"fa-share");
	
	//쪽지 등록 Ajax
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/arm/arm1000/arm1000/insertArm1000InfoAjax.do'/>","loadingShow":true}
			,{"usrId":sendUsrId,"title":title,"content":content,"reqIds":reqIds});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		
		if(data.errorYn == "N"){
			//쪽지 리로드
			fnInGridListSet_alarm();
			fnArmWriteOnOff(null,false);

		}
		toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
	
}

//쪽지 답장 중간 전달 함수
function fnArmWriteSub(obj, onOff){
	var sendUsrId = $(obj).attr("reply-usr-id");
	var sendUsrNm = $(obj).attr("reply-usr-nm");
	var data = {sendUsrId:sendUsrId,sendUsrNm:sendUsrNm};
	fnArmWriteOnOff(data,onOff);
}

//요구사항 태그 제거
function fnSpanDelete(obj){
	$(obj).remove();
}

//요구사항 상세정보 오픈
function fnSpanReqDetailOpen(obj){
	var selPrjGrpId = "${sessionScope.selPrjGrpId}";
	var selPrjId = "${sessionScope.selPrjId}";
	var viewer = null;
	if(selPrjGrpId != $(obj).attr("prj-grp-id") || selPrjId != $(obj).attr("prj-id")){
		viewer = "Y";
	}
	var data = {"mode": "arm", "popupPrjId":$(obj).attr("prj-id"),"reqPrjGrpId":$(obj).attr("prj-grp-id"), "reqId": $(obj).attr("req-id"), "viewer":viewer}; 
	
	// 세션에 있는 선택된프로젝트의 Id와 쪽지의 요구사항 프로젝트 Id가 다를경우 
	// 요구사항 상세보기 팝업 호출 시 callView 값을 넘겨준다.
	if(selPrjId != $(obj).attr("prj-id")){
		$.extend(data, {"callView":"arm"});
	}
	
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
}

//요구사항 작업흐름 변경 팝업 오픈
function fnSpanReq4105Open(obj){
	var selPrjGrpId = "${sessionScope.selPrjGrpId}";
	var selPrjId = "${sessionScope.selPrjId}";
	var viewer = null;
	if(selPrjGrpId != $(obj).attr("prj-grp-id") || selPrjId != $(obj).attr("prj-id")){
		viewer = "Y";
		var data = {"mode": "arm", "popupPrjId":$(obj).attr("prj-id"),"reqPrjGrpId":$(obj).attr("prj-grp-id"), "reqId": $(obj).attr("req-id"), "viewer":viewer}; 
		
		// 세션에 있는 선택된프로젝트의 Id와 쪽지의 요구사항 프로젝트 Id가 다를경우 
		// 요구사항 상세보기 팝업 호출 시 callView 값을 넘겨준다.
		if(selPrjId != $(obj).attr("prj-id")){
			$.extend(data, {"callView":"arm"});
		}
		
		gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
		return false;
	}
	var data = {"reqId": $(obj).attr("req-id")}; 
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4105View.do", data, '1300', '900','scroll',false);
}

//상세 요청사항 팝업 오픈
function fnSpanReq4108Open(obj){
	
	var selPrjGrpId = "${sessionScope.selPrjGrpId}";
	var selPrjId = "${sessionScope.selPrjId}";
	var viewer = null;
	if(selPrjGrpId != $(obj).attr("prj-grp-id") || selPrjId != $(obj).attr("prj-id")){
		viewer = "Y";
		var data = {"mode": "arm", "popupPrjId":$(obj).attr("prj-id"),"reqPrjGrpId":$(obj).attr("prj-grp-id"), "reqId": $(obj).attr("req-id"), "viewer":viewer}; 
		gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
		return false;
	}
	/*
  	 * reqPageType 추가
  	 * 요구사항 상세보기(req1002.jsp)에서 항목명 구분을 위해 사용
  	 * usrReqPage - 요구사항 요청(사용자) 
  	 * admReqPage - 전체 요구사항 목록, 요구사항 생성관리(관리자)
  	 */
  	var data = {
  			"mode": "req", 
  			"reqId": $(obj).attr("req-id"),
  			"reqProType": $(obj).attr("reqProType"),
  			"reqPageType" : "usrReqPage"
  	}; 
  	gfnLayerPopupOpen("/req/req1000/req1000/selectReq1002View.do", data, '640', '920','scroll');
}

//배포계획 팝업 오픈
function fnSpanDplDetailOpen(obj){
	var data = {"prjId" : $(obj).attr("prj-id"), "dplId" : $(obj).attr("dpl-id")};
	gfnLayerPopupOpen('/dpl/dpl1000/dpl1000/selectDpl1003View.do',data, "415", "690",'scroll');
}

</script>
<div class="armHeader">
	<span id="headerTitle">쪽지 관리</span>
</div>
<div class="armBody">
	<input type="hidden" id="alarm_pageIndex" name="alarm_pageIndex"  value="1"/>
	<form:form commandName="arm1000VO" id="alarm_searchFrm" name="alarm_searchFrm" method="post" onsubmit="return false;">
		 <form:hidden path="pageIndex" id="pageIndex" name="pageIndex"  />
	</form:form>
	<div id="AXAlarmSearchTarget"></div>
	<div data-ax5grid="alarm-grid" data-ax5grid-config="{}" style="height: 540px;"></div>
</div>
<div class="armFooter">
	<div class="arm_close_btn" onclick="fnArmWriteOnOff(null,true)">쪽지 작성</div>
	<div class="arm_close_btn arm_pop_close">쪽지 닫기</div>
</div>
<div class="armViewMain">
	<div class="armViewHeaderLeft">
		<div class="viewHeaderLeft"><span>보낸이</span></div>
		<div class="viewHeaderRight">&nbsp;<span id="sendUsrNm"></span></div>
		<div class="viewHeaderLeft"><span>보낸시간</span></div>
		<div class="viewHeaderRight">&nbsp;<span id="regDtm"></span></div>
		<div class="viewHeaderLeft" style="height:60px;border-bottom:none;"><span>제목</span></div>
		<div class="viewHeaderRight" style="height:60px;border-bottom:none;"><div id="title"></div></div>
	</div>
	<div class="armViewHeaderRight">
		
	</div>
	<div class="armViewBody">
		<span id="content"></span>
	</div>
	<div class="armViewFooter">
		<div class="arm_close_btn" onclick="fnArmViewOnOff(null,false)">쪽지 목록</div>
		<div id="armOpenDelBtn" class="arm_close_btn" onclick="fnArmInfoDel(this)" del-arm-id="">쪽지 삭제</div>
		<div id="armOpenReplyBtn" class="arm_close_btn" onclick="fnArmWriteSub(this,true)" reply-usr-id="" reply-usr-nm="">쪽지 답장</div>
		<div class="arm_close_btn arm_pop_close">쪽지 닫기</div>
	</div>
</div>
<div class="armWriteMain">
	<div class="armWriteHeader">
		<div class="writeHeaderLeft"><span>받는이</span></div>
		<div class="writeHeaderRight">
			<input type="hidden" name="writeSendUsrId" id="writeSendUsrId"/>
			<input type="text" title="받는이" class="arm_input_text" name="writeSendUsrNm" id="writeSendUsrNm"/>
			<span class="button_normal2 fl" id="btn_armUser_select" style="height: 31px;"><li class="fa fa-search"></li></span>
		</div>
		<div class="writeHeaderLeft"><span>제목</span></div>
		<div class="writeHeaderRight"><input id="title"></input></div>
		<div class="writeHeaderLeft"><span>요구사항 태그 검색</span></div>
		<div class="writeHeaderRight"><input id="searchReqIds" placeholder="요구사항 검색 키워드를 입력해주세요."></input></div>
		<div class="writeHeaderCenter" id="reqIdSpan">&nbsp;</div>
	</div>
	<div class="armWriteBody">
		<textarea class="subContent" id="content" placeholder="쪽지 내용을 입력해주세요."></textarea>
	</div>
	<div class="armWriteFooter">
		<div class="arm_close_btn" onclick="fnArmWriteOnOff(null,false)">쪽지 목록</div>
		<div class="arm_close_btn" onclick="fnArmWriteAction()">작성 완료</div>
		<div class="arm_close_btn arm_pop_close">쪽지 닫기</div>
	</div>
</div>
</html>