<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="<c:url value='/css/menu/aside.css'/>">

<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>" ></script>
<script src="<c:url value='/js/menu/aside.js'/>" ></script>

<%-- 
<jsp:include page="/WEB-INF/jsp/oslops/top/quickmenu.jsp" />		<!-- 퀵메뉴 -->
 --%>
<script>
	//페이지 로딩시 좌측 메뉴의 알림, 검수목록, 담당요구사항, 개발주기에 속한 요구사항 등 정보를 조회해 세팅한다.
	$(document).ready(function() {
		fnSelectLeftMenuInfos(); 
	});

	//intCnt 에 따라 보내는 페이지 세팅을 달리함.
	function fnMovePage(intCnt, val){	
		if(intCnt == 1){
			// 프로젝트가 개발일 경우    
	       	if(val == "01"){
    			document.hidAsidePrjFrm.menuUrl.value = '/dsh/dsh2000/dsh2000/selectDsh2000View.do';
    			document.hidAsidePrjFrm.menuId.value = '000100020001'
        	}else if(val == "02"){ 
        		// 프로젝트가 운영일 경우
        		document.hidAsidePrjFrm.menuUrl.value = '/dsh/dsh1000/dsh1000/selectDsh1000View.do';
    			document.hidAsidePrjFrm.menuId.value = '000100010001';
        	}
		}
		else if(intCnt == 2){
			document.hidAsidePrjFrm.menuUrl.value = '/req/req2000/req2000/selectReq2000View.do';
			document.hidAsidePrjFrm.menuId.value = '000300020001';
			document.hidAsidePrjFrm.flowId.value = val; 
			
		}
		else if(intCnt == 3){
			document.hidAsidePrjFrm.menuUrl.value = '/req/req4000/req4100/selectReq4100View.do';
			document.hidAsidePrjFrm.menuId.value = '000300040001';
			document.hidAsidePrjFrm.reqChargerNm.value =  "<c:out value='${sessionScope.loginUsrInfo.usrNm}'/>"; 
			
		}
		else if(intCnt == 4){ //전체 쪽지 목록
			var data = {
				"viewChk": false
			};
			gfnLayerPopupOpen('/arm/arm1000/arm1000/selectArm1000View.do',data,"1000","700",'scroll');
			
			return false;
		}else if(intCnt == 5){ //신규 쪽지 목록
			var data = {
				"viewChk": true
			};
			gfnLayerPopupOpen('/arm/arm1000/arm1000/selectArm1000View.do',data,"1000","700",'scroll');
			
			return false;
		}else if(intCnt == 6){ //쪽지 작성
			var data = {
				"sendChk": true
			};
			gfnLayerPopupOpen('/arm/arm1000/arm1000/selectArm1000View.do',data,"1000","700",'scroll');
			
			return false;
		}else{
			toast.push('이동할 페이지가 없습니다.');
			return;
		}

		document.hidAsidePrjFrm.action="<c:url value='/cmm/cmm9000/cmm9000/selectCmm9000MenuChgView.do'/>";
		document.hidAsidePrjFrm.submit();
	}
	
	//좌측 메뉴에 필요한 정보들을 조회(알림, 검수목록, 담당요구사항, 개발주기에 속한 요구사항)
	function fnSelectLeftMenuInfos(){
		var selMainAuthGrpId = '${sessionScope.selMainAuthGrpId}';
		
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/cmm/cmm9000/cmm9000/selectCmm9000LeftMenuInfos.do'/>"});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			var rtnMap = data.rtnMap;
			var alarmCnt = data.alarmCnt;
			/*******************************************************************
        	*	쪽지영역 세팅 시작
        	*******************************************************************/
			var viewNum = parseInt(alarmCnt.viewCnt);
			
			//읽지 않은 쪽지 존재시 Class추가
			if(viewNum > 0){
				$("#dtAlarmMsg").addClass("newAlarm");
			}
			//읽지 않은 쪽지 갯수 표시
			$("#spanAlarmMsg").html(viewNum);
			
			//하위 영역 세팅
			var strAllArmCnt = "<div class='asideHover'><span onclick='fnMovePage(4,"+viewNum+")'>전체 쪽지 목록</span><span id='alarm_allNumSpan'>" + alarmCnt.allCnt + "</span></div>";
			var strNewArmCnt = "<div class='asideHover'><span onclick='fnMovePage(5,"+viewNum+")'>신규 쪽지 목록</span><span id='alarm_viewNumSpan'>" + viewNum + "</span></div>";
			var strSendArmCnt = "<div class='asideHover'><span onclick='fnMovePage(6)'>쪽지 보내기</span></div>";
			$("#ddAlarmMsg").append(strAllArmCnt);
			$("#ddAlarmMsg").append(strNewArmCnt);
			$("#ddAlarmMsg").append(strSendArmCnt);
        	
        	/*******************************************************************
        	*	알림영역 세팅 시작
        	*******************************************************************/
        	var selectProjectType = rtnMap.selPrjType;
        	
        	var strNewReqCnt = "<div class='asideHover'><span onclick='fnMovePage(1," + selectProjectType + ")'>접수대기건</span><span>" + rtnMap.alarmMap.reqCnt + "</span></div>";

			//알림영역 전체 건수 표시
			$("#spanAlarm").html( Number(rtnMap.alarmMap.reqCnt));
   			$("#ddAlarm").append(strNewReqCnt);
        	
   			// 현재 선택된 프로젝트의 유형이 운영이 아닐경우 알림 영역 hide()
   			/* if(selectProjectType != '02'){
   				$("#dtAlarm").hide();
   			} */
        	
    		/*******************************************************************
        	*	담당 요구사항 세팅 시작
        	*******************************************************************/
        	//담당 요구사항 세팅
        	var allChargeReqCnt = 0;
    		
    		if(!gfnIsNull(rtnMap.chargeReqList)){
    			allChargeReqCnt = Number(rtnMap.chargeReqList.cnt);
    		}
	        $("#spanChargerReq").html(allChargeReqCnt);
        	
        	/*******************************************************************
        	*	전체 요구사항 세팅 시작
        	*******************************************************************/
        	//전체 요구사항 세팅
        	var allReqCnt = 0;
        	if(!gfnIsNull(rtnMap.allReqList)){
        		allReqCnt = Number(rtnMap.allReqList.cnt);
        	}
        	
        	$("#spanAllReq").html(allReqCnt);
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			toast.push('좌측 정보 조회 실패');
		});
		//AJAX 전송
		ajaxObj.send();
	}
	
	//권한롤 드롭다운 변경시 체크하여 메인페이지 이동
	function fnGoAuthChg(authGrpId){
		document.hidAuthForm.authGrpId.value = authGrpId;
		document.hidAuthForm.action="<c:url value='/cmm/cmm9000/cmm9000/selectCmm9000AuthGrpChgView.do'/>";
		document.hidAuthForm.submit();
	}
	//로그아웃 처리
	function fnGoLogout(){
		jConfirm("로그아웃 하시겠습니까?", "알림창", function( result ) {
			if( result ){
				// 로그인 시 생성한 비밀번호 만료 쿠키 삭제
				deleteCookie("pwExpire");
				// 로그인 시 생성한 접속IP체크 쿠키 삭제
				deleteCookie("accessIp");
				$(location).attr('href',"/cmm/cmm4000/cmm4000/selectCmm4000Logout.do");
			}
		});
	}
	
	/*
	 * 비밀번호 만료일 alert이 로그인 시 한번만 나타나게 하기해 생성한 쿠키를 삭제
	 * @param cookieName 쿠키이름
	 */
	function deleteCookie(cookieName){
		var expireDate = new Date();
	  
	  	//어제 날짜를 쿠키 소멸 날짜로 설정
		expireDate.setDate( expireDate.getDate() - 1 );
		document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString() + "; path=/";
	 }
	
</script>

<section>

<form name="hidAuthForm" method="post">
	<input type="hidden" name="url">
	<input type="hidden" name="authGrpId">
</form>

<form name="hidAsidePrjFrm" method="post">
	<input type="hidden" name="menuUrl">
	<input type="hidden" name="menuId">
	<input type="hidden" name="menuNm">
	<input type="hidden" name="sprintId">
	<input type="hidden" name="flowId">
	<input type="hidden" name="reqChargerNm">
</form>

<aside>
	<!--  TODO aside.jsp m_btn버튼 제거
	<div class="m_btn">···</div>
	-->
	<div class="title_wrap">
		<div class="title">${sessionScope.selMenuNm }</div><!-- <div class="aside_toggle_btn"><img src="/images/header/GNB/GNB-icon_left.png" alt=""></div> -->
	</div>
	<dl>
		<dt class="prj_select_box">
			<label for="LNB_project_select">LNB_project_select</label>
			<select class="LNB_project_select" id="LNB_project_select" onchange="fnGoAuthChg(this.options[this.selectedIndex].value)">
				<c:if test="${!empty sessionScope.authList }">
					<c:forEach items="${sessionScope.authList }" var="map">
						<c:if test="${sessionScope.selAuthGrpId == map.authGrpId}" >
							<option selected value="${map.authGrpId }">${map.authGrpNm }</option>
						</c:if>
						<c:if test="${sessionScope.selAuthGrpId != map.authGrpId}" >
							<option value="${map.authGrpId }">${map.authGrpNm }</option>
						</c:if>
					</c:forEach>
				</c:if>
			</select>
			<img class="aside_close_btn" src="/images/header/topMenu/x.png" alt="X">
		</dt>
		
		<dt class="usrInfo">
			<span>[접속자 정보]</span>
			<span class="usrInfo_txt">&nbsp;&nbsp;> 아이디 : <c:out value="${sessionScope.loginUsrInfo.usrId}"/></span>
			<span class="usrInfo_txt">&nbsp;&nbsp;> 성&nbsp;&nbsp;&nbsp;명 : <c:out value="${sessionScope.loginUsrInfo.usrNm}"/></span>
			<span class="usrInfo_txt">&nbsp;&nbsp;> 소&nbsp;&nbsp;&nbsp;속 : <c:out value="${sessionScope.loginUsrInfo.usrDeptNm}"/></span>
			<span class="usrInfo_txt">&nbsp;&nbsp;> 사&nbsp;&nbsp;&nbsp;업 : <c:out value="${sessionScope.selPrjTaskTypeNm}"/></span>
		</dt>
		
		<dt class="logintime">
			<span>[최근 접속일시]</span>
			<span class="logintime_txt">&nbsp;&nbsp;<c:out value="${sessionScope.recentLogin}"/></span>
		</dt>
		
		<dt class="ipInfo">
			<span>[이전 접속 IP]</span>
			<span class="ipInfo_txt">&nbsp;&nbsp;> <c:out value="${sessionScope.userAccessIpInfoList[1].loginIp}"/></span>
			<span class="currentIp">[현재 접속 IP]</span>
			<span class="ipInfo_txt">&nbsp;&nbsp;> <c:out value="${sessionScope.userAccessIpInfoList[0].loginIp}"/></span>
		</dt>
		
		<dt class="option">
			<span data="open">전체 열기</span><span data="close">전체 닫기</span>
		</dt>
		 
		<dt class="menu" style="padding-left:10px;" onclick="javascript:fnGoLogout()">
			<span style="line-height: 0px;"><img src="/images/quickmenu/quick_logout.png" alt="로그아웃" class="logoutImg"></span>
			<span>로그아웃</span>
		</dt>
		
		<dt class="menu" id="dtAlarmMsg"><span><i class="fa fa-envelope-o"></i></span><span>쪽지</span><span id="spanAlarmMsg"></span></dt>
			<dd id="ddAlarmMsg"/>	
		
		<!-- 권한의 사용자 유형이 관리자일 경우만 알림, 담당 요구사항, 전체 요구사항을 보여줌 -->
		<c:if test="${sessionScope.usrTyp eq '02'}" >	
		<dt class="menu" id="dtAlarm"><span><img src="/images/header/LNB/LNB-icon_02.png" alt="알림"></span><span>알림</span><span id="spanAlarm"></span></dt>
			<dd id="ddAlarm"/>

		<dt class="menu" onclick="fnMovePage(3)" ><span><img src="/images/header/LNB/LNB-icon_05.png" alt="담당 요구사항"></span><span>담당 요구사항</span><span id="spanChargerReq"></span></dt>
		<!-- <dd id="ddChargeReqListZone"/>  -->
		
		<dt class="menu" onclick="fnMovePage(2)" ><span><img src="/images/header/LNB/LNB-icon_06.png" alt="전체요구사항"></span><span>전체 요구사항</span><span id="spanAllReq"></span></dt>
		<!-- <dd id="ddAllReqListZone"/>  -->
		</c:if>
	</dl>
</aside>