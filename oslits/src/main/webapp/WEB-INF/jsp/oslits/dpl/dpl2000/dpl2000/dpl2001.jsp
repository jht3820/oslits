<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<html lang="ko">
<title>OpenSoftLab</title>

<head>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/highlight/styles/monokai-sublime.css'/>">
<script type="text/javascript" src="<c:url value='/js/highlight/highlight.pack.js'/>"></script>

<style type="text/css">
.sub_title{ font-weight: bold; background: #f9f9f9; border-bottom: 1px solid #ccc; text-align: left;padding: 5px 0;height: 25px;border-radius: 5px 5px 0 0;}

.dpl2000_dpl_maxkBox {display:none;position: absolute;right: 18px;left: 23px;width: 1259px;height: 400px;min-height: 370px;background-color: rgba(0, 0, 0, 0.6);z-index: 2;color: #fff;padding-top: 150px;text-align: center;line-height: 30px;}
div#contentsFrame {
    width: 100%;
    height: 360px;
    text-align: left;
    line-height:20px;
}
div#contentsFrame > code{    height: 100%;width: 100%;font-size: 10pt;}
</style>
<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<script>
	// 초기화

	//선택 요구사항 ID
	var sel_reqId = null;
	var jobGrid  = {
			init : function() {
				
				
			}
			, columnOption : {
				
			},
			cbList : {
				
			}
	}
				
	$(document).ready(function(){
		//jobGrid.init(); // AXISJ Grid 초기화 실행 부분들
		fnSearchDplInfo();
		fnAxDetailJobGrid5View();
		/* 취소버튼 클릭 시 팝업 창 사라지기*/
		$('#btn_cancle_popup').click(function() {
			gfnLayerPopupClose();
		});
		
		$(".dpl2000_dpl_maxkBox").show();		
	});
	
	
	function fnSearchDplInfo(){
		
		
		//AJAX 설정
		var ajaxBuildObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl1000/dpl1000/selectDpl1000BuildDetailAjax.do'/>","loadingShow":false}
				,{ "prjId": '${param.prjId}', "dplId": '${param.dplId}' });
		//AJAX 전송 성공 함수
		ajaxBuildObj.async = false;
		ajaxBuildObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			
			gfnSetData2ParentObj(data.buildMap, "detailDiv");
			
			fnAxGrid5View_first(data.buildMap.dplId);
			
			fnIndetailJobGridListSet(data.buildMap.prjId,data.buildMap.dplId);
			
			
			
		});
		
		//AJAX 전송 오류 함수
		ajaxBuildObj.setFnError(function(xhr, status, err){
			
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
	 	});
		//AJAX 전송
		ajaxBuildObj.send();
	}
	
	
	function fnSearchLogList(jobname,buildnumber){
		
		//AJAX 설정
		var ajaxLogObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildDetailAjax.do'/>","loadingShow":false}
				,{"jobname" : jobname ,"buildnumber": buildnumber ,"prjId": '${param.prjId}', "dplId": '${param.dplId}' });
		//AJAX 전송 성공 함수
		ajaxLogObj.async = false;
		ajaxLogObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var logHtml = "";
			//var hisInfo = JSON.stringify(data.actions); 
			if(data.messageCode == undefined ){
				if(data.errorYn != "N"){
					// 조회 실패할경우
					jAlert(data.message, "알림창");
					logHtml = gfnReplace(  data.consoleText , "\n" , "</br>");
					$('#fileContent').html(logHtml);
				}else{
					logHtml += "result : "+data.actions.result+"</br>";
					var actionList = data.actions.actions;
					
					$(actionList).each(function(index, item){
						if(!gfnIsNull(item)){
							var causesList = item.causes;
							$(causesList).each(function(index, itm){
								logHtml += "userId : "+itm.userId+"</br>";			
								logHtml += "userName : "+itm.userName+"</br>";
								logHtml += "shortDescription : "+itm.shortDescription+"</br>";
							})
						}
					
					})
					
					logHtml += "</br>";
					logHtml += "</br>";
					logHtml += "</br>";
					logHtml += "==============================================Console Output==============================================</br>";
					
					logHtml  += gfnReplace( data.consoleText , "\n" , "</br>");
						
					$('#result').val(data.actions.result);
					
					$('#fileContent').html(logHtml);
					//코드 뷰어 달기
					$('#fileContent').each(function(i, block) {hljs.highlightBlock(block);});
				}
				$(".dpl2000_dpl_maxkBox").hide();	
			}else{
				jAlert(data.message, "알림창");
			}
			
		});
		
		//AJAX 전송 오류 함수
		ajaxLogObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			gfnShowLoadingBar(false);
			jAlert(data.message, "알림창");
	 	});
		//AJAX 전송
		ajaxLogObj.send();
	}
	
	
	function fnAxGrid5View_first(dplId){
		secondGrid = new ax5.ui.grid();
	 
	    secondGrid.setConfig({
	            target: $('[data-ax5grid="second-req-grid"]'),
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
	                columnHeight: 30
	            }
	        });
	        
	        //그리드 데이터 불러오기
	 		fnInReqGridListSet(null,{grid:secondGrid,data:{"clsMode":"clsAdd","dplId":dplId}});

	}
	
	function fnInReqGridListSet(ajaxParam,gridTarget){
		
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
				{"url":"<c:url value='/dpl/dpl1000/dpl1100/selectDpl1100DplListAjax.do'/>","loadingShow":true}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.list;
			
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
	
	var detailJobGrid;

	function fnAxDetailJobGrid5View(){
		detailJobGrid = new ax5.ui.grid();
	 
	        detailJobGrid.setConfig({
	            target: $('[data-ax5grid="detail-job-grid"]'),
	 
	            sortable:false,
	            showRowSelector: true,
	            multipleSelect: false  ,
	         	header: {align:"center", selector : false  },	 
	            columns: [
	               
					{key: "jenNm", label: "JOB 명", width: 100, align: "center"},
					{key: "bldNum", label: "빌드번호", width: 80, align: "right"},
					{key: "bldSts", label: "빌드결과", width: 100, align: "center"}
	            ],
	            body: {
	                align: "center",
	                columnHeight: 30,
	                onClick: function () {
	                	var selItem = detailJobGrid.list[this.doindex];
	                	if(gfnIsNull(selItem.bldNum)){
	                		$(".dpl2000_dpl_maxkBox").show();
	                		$("#fileContent").html('');
	                		$("#dplMaskBox").html("빌드된 이력이 없습니다.");
	                	}else{
	                		$(".dpl2000_dpl_maxkBox").hide();
	                		fnSearchLogList(selItem.jenNm ,selItem.bldNum);		 
	                	}
	                	detailJobGrid.select(this.doindex, {selected: true});
	                }
	            }
	        });

	}
	//그리드 데이터 넣는 함수
	function fnIndetailJobGridListSet(prjId,dplId){
	     	
	     	//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JobBuildList.do'/>","loadingShow":true}
					,{"prjId" : prjId , "dplId" : dplId });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				var list = data.jobList;
			
			   	detailJobGrid.setData(list);
			   	if(list.length>0){
			   		if(null!=list[0].bldNum){
			   			//fnSearchLogList(list[0].jenNm ,list[0].bldNum);	
			   			detailJobGrid.select(0, {selected: true});
			   		}

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
	
</script>
<style type="text/css">
	.dlp_right_contents{float:left; width:100%;border-bottom: 1px solid #ddd;padding:17.5px 19px; font-size:0.75em;}
			.r_r_c_line{margin-bottom: 10px;width: 100%; float: left;}
				.r_r_c_left{display:inline-block; width:30%; float:left;}
				.r_r_c_right{display:inline-block; width:70%; float:left;}
</style>
</head>
<div class="popup">
	<div class="pop_title">
		${sessionScope.selMenuNm }
	</div>

	<div class="pop_sub">
	<div class="menu_wrap" style="max-width:1250px;height:400px;" >
		<table>
			<tr>
				<td style="width:20px;" >					
				</td>
				<td style="width:290px;" >
					<div class="" id="detailDiv">	
						<div class="dlp_right_contents">
							<div class="r_r_c_line">
								<span class="r_r_c_left">배포명 :</span>
								<span class="r_r_c_right" id="dplNm"></span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">배포예정일 :</span>
								<span class="r_r_c_right" id="dplDt"></span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">배포일 :</span>
								<span class="r_r_c_right" id="dplDt"></span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">배포ID :</span>
								<span class="r_r_c_right" id="dplId"></span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">배포자 :</span>
								<span class="r_r_c_right" id="dplUsrNm"></span>
							</div>
							
						</div>
					</div>
					
				</td>
				<td style="width:20px;" >					
				</td>
				<td rowspan = "2" >
					<div class="sub_title">
						배포 추가된 요구사항 목록
					</div>
					<div data-ax5grid="second-req-grid" data-ax5grid-config="{}" style="height: 348px;width:918px;"></div>					
				</div>
				</td>
			</tr>
			<tr>
				<td style="width:20px;" >					
				</td>
				<td style="width:290px;height: 253px;" >
					<div class="sub_title">
						JOB별 빌드 결과
					</div>
					<div data-ax5grid="detail-job-grid" data-ax5grid-config="{}" style="height: 198px;width:280px;"></div>	
				</td>
				<td style="width:20px;" >					
				</td>
				
			</tr>
		</table>
	</div>
		
			
	<div class="menu_wrap" style="max-width:1250px;height:400px;" >
		<div class="dpl2000_dpl_maxkBox" id="dplMaskBox">빌드 결과 선택하세요</div>
			<div class="sub_title">
				배포된 콘솔 내용
			</div>
			<!-- <div class="menu_row" id="detaillog" style ="overflow-y:scroll;height:360px;width:1230px;text-align: left;"> -->
			<div id="contentsFrame">
					<code id="fileContent">
					
					</code>
			</div>
		</div>
		<div class="btn_div">
			<div class="button_normal exit_btn" id="btn_cancle_popup">닫기</div>
		</div>
	</div>
</div>
</html>