<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<html lang="ko">
<title>OpenSoftLab</title>
<head>
	
</head>
<style type="text/css">
.sub_title{ font-weight: bold; background: #f9f9f9; border-bottom: 1px solid #ccc; text-align: left;padding: 5px 0;height: 25px;border-radius: 5px 5px 0 0;}
div#dpl2002_mask_frmae {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7);
    z-index: 2;
    font-size: 10pt;
    color: #fff;
    font-weight: bold;
    padding-top: 150px;
    display:none;
}
</style>
<script src="<c:url value='/js/common/layerPopup.js'/>"></script>
<script>
	// 초기화
	var jenJobSearch;
	var secondGrid;
	//선택 요구사항 ID
	var sel_reqId = null;
	var jobGrid  = {};
	var isOnLoad = false;	
				
	$(document).ready(function(){
		fnJobListView();	
		fnJenkinsJobGridView();  // Grid 초기화  설정\
		fnSearchJenkinsBoxControl(); // Search Grid 초기화 설정
		
		/* 취소버튼 클릭 시 팝업 창 사라지기*/
		$('#btn_cancle_popup').click(function() {
			gfnLayerPopupClose();
		});
		setTimeout(function(){
			fnOnLoadSelect();
			}, 500
		);	
	});
	
	
	function fnOnLoadSelect(){
		if(!isOnLoad){
			isOnLoad = true;
			fnInJenkinsGridListSet();
		}else{
			return;
		}
	}

	//axisj5 그리드
	function fnJenkinsJobGridView(){
		jenJobListGrid = new ax5.ui.grid();
 
        jenJobListGrid.setConfig({
            target: $('[data-ax5grid="jenkins-first-grid"]'),
            sortable:false,
            showRowSelector: true,
            multipleSelect: false  ,
            header: {align:"center", selector : false  },
            frozenColumnIndex: 2,
            columns: [
				{key: "_class", label: "유형", width: 250, align: "center"}
				            ,{key: "name",label: "JOB이름", width: 210, align: "center"}
				            
				            ,{key: "url",label: "URL", width: 600, align: "center"}

				            ,{key: "",label: "빌드", width: 150, align: "center"
				               	,formatter:function(){
			                		//상위 요구사항 ID가 존재하는 경우 요구사항 명 앞쪽에 아이콘 추가
			                		var beforeIcon = "";
			                
			                			beforeIcon =  '<img src="/images/icon/cog-solid.svg" class="iconRequestSub">&nbsp;';
				            
			                		return beforeIcon;
			                	}}
				            
				            	
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	if(this.colIndex != 3){
	                	$('#detaillog').html("");
	                	fnSearchJobList(this.item.name);
	                	jenJobListGrid.select(this.doindex, {selected: true});
                	}
                },onDBLClick:function(){
                	
                	if(this.colIndex == 3){
                		var name = this.item.name;
                		var number = this.item.number;
                		var jenId = this.item.jenId;
                		fnInJenkinsGridListSet();
                		buildCheck(name,number,jenId);
                	}
                }
            }
        });
        //그리드 데이터 불러오기
 		//

	}

	//그리드 데이터 넣는 함수
	function fnInJenkinsGridListSet(){
     	
		gfnShowLoadingBar(true);
     	     	
     	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsJobListAjax.do'/>","loadingShow":false}
				, {"prjId": '${param.prjId}', "dplId": '${param.dplId}' });
		//AJAX 전송 성공 함수
		var list = [];
		ajaxObj.async = false;
		ajaxObj.setFnSuccess(function(data){
			
			data = JSON.parse(data);

			if(data.messageCode == undefined ){
				list = data.joblist;
				
			   	jenJobListGrid.setData(list);
			   	gfnShowLoadingBar(false);			   	   	
			   	if(list.length > 0){
			   	
			   		jenJobListGrid.select(0, {selected: true});
			   		fnSearchJobList(list[0].name);
			   	}	
			}else{
				gfnShowLoadingBar(false);
				jAlert(data.message, "알림창");
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
	
	//axisj5 그리드
	function fnJobListView(){
		secondGrid = new ax5.ui.grid();
 
		secondGrid.setConfig({
            target: $('[data-ax5grid="second-grid"]'),
            sortable:false,
            showRowSelector: true,
            multipleSelect: false  ,
         	header: {align:"center", selector : false  },
            frozenColumnIndex: 2,
            columns: [
				{key: "number", label: "빌드번호", width: 100, align: "left"},
				{key: "jobname", label: "JOB이름", width: 200, align: "left"}
				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onClick: function () { 
                	
                	fnSearchLogList(this.item.jobname,this.item.number);
                	secondGrid.select(this.doindex, {selected: true});
                }
            }
        });
	}
	
	//검색 상자
	function fnSearchJenkinsBoxControl(){
		var pageID = "AXSearch";
		jenJobSearch = new AXSearch();

		var fnObjSearch = {
			pageStart: function(){
				//검색도구 설정 01 ---------------------------------------------------------
				jenJobSearch.setConfig({
					targetID:"AXSearchJobTarget",
					theme : "AXSearch",
					rows:[
						{display:true, addClass:"", style:"", list:[
							{label:"", labelWidth:"", type:"button", width:"55", key:"button_search",style:"float:right;",valueBoxStyle:"padding-left:0px;padding-right:5px;", value:"<i class='fa fa-list' aria-hidden='true'></i>&nbsp;<span>조회</span>",
								onclick:function(){					
									/* 검색 조건 설정 후 reload */
									
									
							        fnInJenkinsGridListSet();
							        
							       
								}}
							
						]}
					]
				});
			}
		};
		
		jQuery(document.body).ready(function(){
			fnObjSearch.pageStart();
			//검색 상자 로드 후 텍스트 입력 폼 readonly 처리
			
			
			//버튼 권한 확인
			fnBtnAuthCheck(jenJobSearch);
		});
		
	}
	
	function fnSearchJobList(jobname){
		
		gfnShowLoadingBar(true);
		//AJAX 설정
		var ajaxJobObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildListAjax.do'/>","loadingShow":false}
				,{"jobname" : jobname , "prjId": '${param.prjId}', "dplId": '${param.dplId}' });
		//AJAX 전송 성공 함수
		ajaxJobObj.async = false;
		ajaxJobObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			if(data.errorYn == "Y"){
				$("#dpl2002_mask_frmae").html(data.consoleText);
				$("div#dpl2002_mask_frmae").show();
				fnJobListView();
				$("#detaillog").html('');
			}else{
				$(data.buildlist).each(function(index, item){
					item['jobname'] = jobname;
				})
				secondGrid.setData(data.buildlist);
				
				if(data.buildlist.length>0){
					fnSearchLogList(jobname,data.buildlist[0].number);
					secondGrid.select(0, {selected: true});
				}
				$("div#dpl2002_mask_frmae").hide();
			}
			gfnShowLoadingBar(false);
		});
		
		//AJAX 전송 오류 함수
		ajaxJobObj.setFnError(function(xhr, status, err){
			gfnShowLoadingBar(false);
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
	 	});
		//AJAX 전송
		ajaxJobObj.send();
	}
	
	
	function fnSearchLogList(jobname,buildnumber){
		gfnShowLoadingBar(true);
		//AJAX 설정
		var ajaxLogObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildDetailAjax.do'/>","loadingShow":false}
				,{"jobname" : jobname ,"buildnumber": buildnumber ,"prjId": '${param.prjId}', "dplId": '${param.dplId}' });
		//AJAX 전송 성공 함수
		ajaxLogObj.async = false;
		ajaxLogObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			//var hisInfo = JSON.stringify(data.actions); 
		
			if(data.errorYn == "Y"){
				fnJobListView();
				$("#detaillog").html('');
				logHtml = gfnReplace(  data.consoleText , "\n" , "</br>");
			}else{
				var logHtml = "";
				logHtml += "result : "+data.actions.result+"</br>";
				var actionList = data.actions.actions;
				$(actionList).each(function(index, item){
					var causesList = item.causes;
					$(causesList).each(function(index, itm){
						logHtml += "userId : "+itm.userId+"</br>";			
						logHtml += "userName : "+itm.userName+"</br>";
						logHtml += "shortDescription : "+itm.shortDescription+"</br>";
					})
					
				
				})
				
				logHtml += "</br>";
				logHtml += "</br>";
				logHtml += "</br>";
				logHtml += "==============================================Console Output==============================================</br>";
				
				logHtml  += gfnReplace(  data.consoleText , "\n" , "</br>");
				$('#result').val(data.actions.result);
			}
			
			$('#detaillog').html(logHtml);
			gfnShowLoadingBar(false);
			
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
	
	function fnRunBuild(jobname,number,jenId){
		
		gfnShowLoadingBar(true);
		var ajaxRunBuildObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2000/runDpl2000JenkinsBuildAjax.do'/>","loadingShow":false}
				,{"jobname" : jobname ,"jenId" : jenId , "prjId": '${param.prjId}', "dplId": '${param.dplId}' });
		ajaxRunBuildObj.async = false;
		//AJAX 전송 성공 함수
	
		
		ajaxRunBuildObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	
			if(data.content==""){
				var nextNumber = number +1 ;
        		var data = { "name": jobname  , "number" : nextNumber, "prjId": '${param.prjId}', "dplId": '${param.dplId}'}; 
        		gfnShowLoadingBar(false);
        		fnInGridListSet();
        		gfnLayerPopupOpen("/dpl/dpl2000/dpl2000/selectDpl2003BulidConsoleView.do", data, "560", "620",'auto');
        					
			}else{
				jAlert("빌드중 JENKINS 설정 오류<br>JEKINS 설정정보를 확인하세요", "알림창");
				console.log(data.content);
				gfnShowLoadingBar(false);
			}
		});
		
		//AJAX 전송 오류 함수
		ajaxRunBuildObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림창");
	 	});
		//AJAX 전송
	
		ajaxRunBuildObj.send();
		
	}
	
	
	function buildCheck(jobname, number,jenId){
	
		gfnShowLoadingBar(true);
		var ajaxBuildCheckObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildListAjax.do'/>","loadingShow":false}
				,{"jobname" : jobname, "prjId": '${param.prjId}', "dplId": '${param.dplId}' });
		//AJAX 전송 성공 함수
		ajaxBuildCheckObj.async = false;

		ajaxBuildCheckObj.setFnSuccess(function(buildData){
			buildData = JSON.parse(buildData);
			
			if(buildData.MSG_CD=="JENKINS_FAIL"){
				jAlert(buildData.message, "알림창");
				gfnShowLoadingBar(false);
			}else{
				if( buildData.lastBuild.number == buildData.lastCompletedBuild.number ){
					gfnShowLoadingBar(false);
	    			if(number==buildData.buildlist.length){
	    				fnRunBuild(jobname,number +1 ,jenId);
	    			}else{
	    				fnRunBuild(jobname,buildData.buildlist.length,jenId);	
	    			}
	        		
				}else{
					jAlert("빌드중 입니다.", "알림창");
					gfnShowLoadingBar(false);
				}	
			}
	
		});
		

		
		//AJAX 전송 오류 함수
		ajaxBuildCheckObj.setFnError(function(xhr, status, err){
			buildData = JSON.parse(buildData);
			jAlert(buildData.message, "알림창");
			gfnShowLoadingBar(false);
	 	});
		//AJAX 전송
		ajaxBuildCheckObj.send();

	}
	

	
</script>
<div class="popup">
	<div class="pop_title">
		빌드 이력 조회
	</div>
	
	<div class="pop_sub">	
	
	<div class="tab_contents menu" style="max-width:1250px;">
		<form:form commandName="dpl2000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false">
			<input type="hidden" id="result" name="result" >
		</form:form>
		<div id="AXSearchJobTarget" style="border-top:1px solid #ccc;"></div>
		<br/>
		<div class="sub_title">
			JOB 목록
		</div>
		<div data-ax5grid="jenkins-first-grid" data-ax5grid-config="{}" style="height: 150px;"></div>		
	</div>
	
	<div class="menu_wrap" style="max-width:1250px;height:400px;position:relative;" >
		<div class="dpl2002_mask_frmae" id="dpl2002_mask_frmae"></div>
		<table>
			<tr>
				<td style="width:20px;" >					
				</td>
				<td style="width:290px;" >
					<div class="sub_title">
						빌드 이력
					</div>
					<div data-ax5grid="second-grid" data-ax5grid-config="{}" style="height: 365px;width:290px;"></div>
				</td>
				<td style="width:20px;" >					
				</td>
				<td style="vertical-align: top;"> 
					<div class="sub_title">
						빌드 콘솔 내용
					</div>
					<div class="menu_row" id="detaillog" style ="overflow-y:scroll;height:345px;width:910px;text-align: left;"> 							
				</div>
				</td>
			</tr>
		</table>
	</div>
	
		<div class="btn_div">
			<div class="button_normal exit_btn" id="btn_cancle_popup">닫기</div>
		</div>
	</div>
</div>
</html>