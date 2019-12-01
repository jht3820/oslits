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
	
			}
			
	};

	var processNm = "${param.processNm}";


	$(document).ready(function() {
		//달력 세팅 (배포일)
		popGrid.init(); // AXISJ Grid 초기화 실행 부분들
		
		/* 취소 */
		$('#btnPopDsh2001Cancle').click(function() {
			gfnLayerPopupClose();
		});

	});
	


	//axisj5 그리드
	function fnAxGridSet(){
		
		
		popGrid = new ax5.ui.grid();
 
        popGrid.setConfig({
            target: $('[data-ax5grid="pop-grid"]'),
            sortable:false,
            showRowSelector: false,
            
            header: {align:"center" },
            //frozenColumnIndex: 2,
            columns: [
				{key: "reqOrd", label: "요구사항 순번", width: '13%', align: "center"},	          
				{key: "reqDtm", label: "요청일자", width: '12%', align: "center",formatter:function(){
					return new Date(this.item.regDtm).format("yyyy-MM-dd");
				}},
				{key: "reqExistUsrNm", label: "요청자", width: '10%', align: "center"},
				{key: "reqUsrEmail", label: "이메일", width: '18%', align: "center"},
				{key: "reqUsrNum", label: "연락처", width: '13%', align: "center"},
				{key: "reqNm", label: "요청 명", width: '30%', align: "left"},
				{key: "reqDesc", label: "요청 내용", width: '35%', align: "left"},
				{key: "calcReqDay", label: "초과 일수", width: 120, align: "center",formatter:function(){
					return this.item.calcReqDay+"일";
				}},
				{key: "reqStDtm", label: "작업 시작일자", width: 140, align: "center",formatter:function(){
					return new Date(this.item.reqStDtm).format("yyyy-MM-dd HH:mm");
				}},
				{key: "reqEdDtm", label: "작업 종료일자", width: 140, align: "center",formatter:function(){
					return new Date(this.item.reqEdDtm).format("yyyy-MM-dd HH:mm");
				}},
				{key: "reqStDuDtm", label: "작업 시작 예정일자", width: 140, align: "center",formatter:function(){
					return new Date(this.item.reqStDuDtm).format("yyyy-MM-dd");
				}},
				{key: "reqEdDuDtm", label: "작업 종료 예정일자", width: 140, align: "center",formatter:function(){
					return new Date(this.item.reqEdDuDtm).format("yyyy-MM-dd");
				}}
				
            ],
            body: {
                align: "center",
                columnHeight: 30,
                onDBLClick: function () {console.log(this.item.reqId);
  
       				var data = {"mode":"newReq","reqId": this.item.reqId, "reqProType":"02"}; 
       				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
  
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
                	fnInGridPopListSet(this.page.selectPage,"");
                }
            }
        });
        var ajaxParam = "";//"&searchPopTxt=${param.usrNm}";
		
        fnInGridPopListSet(0,ajaxParam);
	}
	//그리드 데이터 넣는 함수
	
	function fnInGridPopListSet(_pageNo, ajaxParam){
     	/* 그리드 데이터 가져오기 */
     	//파라미터 세팅
 
     	//페이지 세팅
    	if(!gfnIsNull(_pageNo)){
    		ajaxParam += "&pageNo="+_pageNo;
    	}else if(typeof popGrid.page.currentPage != "undefined"){
    		ajaxParam += "&pageNo="+popGrid.page.currentPage;
    	}

     	if(!gfnIsNull(processNm)){
    		ajaxParam += "&processNm="+processNm;
    	}     	

    	//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/dsh/dsh2000/dsh2000/selectDsh2000ReqDtmOverAlertListAjax.do'/>","loadingShow":false}
				,ajaxParam);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			if(data.errorYn == "Y"){
				jAlert(data.message, '알림');
				return false;
			}
			
			var list = data.list;
			var page = data.page;
			
			popGrid.setData({
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
           		jAlert('세션이 만료되어 로그인 페이지로 이동합니다.', '알림');
        		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
        		return;
           	}
		});
		
		//AJAX 전송
		ajaxObj.send();
		
	}
	


</script>

<div class="popup">
		
	<div class="pop_title"><c:out value="${param.popTitleMsg}" /> 미처리 요구사항 목록</div>
	<div class="pop_sub">
		<div class="tab_contents menu" > <!-- style="max-width:800px;" -->
			<form:form commandName="dsh2000VO" id="searchFrm" name="searchFrm" method="post" onsubmit="return false"></form:form>
			
			<br />
			<div data-ax5grid="pop-grid" data-ax5grid-config="{}" style="height: 370px;"></div>		
		</div>
		<div class="btn_div">		
			<div class="button_normal exit_btn" id="btnPopDsh2001Cancle" >닫기</div>
		</div>
	</div>
	</form>
</div>
</html>