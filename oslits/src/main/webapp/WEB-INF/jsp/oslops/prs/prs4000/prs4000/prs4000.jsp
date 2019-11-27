<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/prs.css'/>' type='text/css'>

<!-- 일정관리 캘린더 관련 -->
<link rel='stylesheet' href='<c:url value='/css/fullcalendar/fullcalendar.css'/>' rel='stylesheet' />
<link rel='stylesheet' href='<c:url value='/css/fullcalendar/fullcalendar.print.css'/>' rel='stylesheet' media='print' />

<script src="<c:url value='/js/fullcalendar/moment.min.js'/>"></script>
<script src="<c:url value='/js/fullcalendar/fullcalendar.min.js'/>"></script>
<script src="<c:url value='/js/fullcalendar/lang-all.js'/>"></script>
<!-- 일정관리 캘린더 관련 -->

<style>
/* 공통에서 적용된 css 무시 */
tbody tr:hover { background: none !important; }
h2{ 
		font-weight: bold !important;
		font-size: 1.5em !important;
}
/* 공통에서 적용된 css 무시 */

.layer_popup_box{ width:590px !important; height:765px !important; }
</style>


<script type="text/javascript">

$(document).ready(function() { 	
	
	/* 달력을 그려준다. fullCalendar */
	$('#calendar').fullCalendar({
		header: {
			left: '',
			center: 'title',
			right: 'prev, next today'
		},
		lang: 'ko',			// 언어 선택
		editable: true, 	// 일정 수정 선택 ( true : 수정가능, false : 수정불가능 )
		
		selectable: true,
		selectHelper: true,
		
		
		/* 최초 로딩 및 월 이동시 호출 하는 function */
		events: function(start, end, timezone, callback) {
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/prs/prs4000/prs4000/selectPrs4000ListAjax.do'/>"}
					,{ "startDt":start.format(), "endDt": end.format() });
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
               	var tList = data.schdList ;
				
               	/* db에서 가져온 일정 데이터 달력에 세팅 시작 */
               	var events = [];
                   
				$.each(tList, function( idx, value ) {
					events.push({
						id 		: tList[idx].schdSeq,			// id
						title	: tList[idx].schdContents,	// 일정내용
						start	: tList[idx].schdYmdSt,		// 일정시작일
						end	: tList[idx].schdYmdEd,		// 일정종료일
					    color	: '#6799FF',   					// 일정박스 색상
					    textColor: '#FFFFFF' 					// 글자 색상
					});
				});
				   
                callback(events);
                /* db에서 가져온 일정 데이터 달력에 세팅 끝 */
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
					data = JSON.parse(data);
					jAlert(data.message,"알림창");
			});
			
			//AJAX 전송
			ajaxObj.send();
		},
		
		/* 일정 등록시 사용하는 function */
		select: function(start, end) {
			
			startDay = fn_chDate( start, "s");
			endDay = fn_chDate(end, "e");

			jPrompt('', '','일정등록', function( result ) {

				if( !gfnIsNull( result ) ){		// 등록 내용이 있다면
					//AJAX 설정
					var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/prs/prs4000/prs4000/insertPrs4000SchdInfoAjax.do'/>"}
							,{ "startDt":startDay, "endDt": endDay , "schdContents":result });
					//AJAX 전송 성공 함수
					ajaxObj.setFnSuccess(function(data){
						data = JSON.parse(data);
			    		
						eventData = {
			    				id		: data.schdSeq,
								title	: result,
								start	: start,
								end	: end,
								color	: '#6799FF',
								textColor: '#FFFFFF'
						};
						
						$('#calendar').fullCalendar('renderEvent', eventData, false);
					});
					
					//AJAX 전송 오류 함수
					ajaxObj.setFnError(function(xhr, status, err){
						data = JSON.parse(data);
						jAlert(data.message, "알림창");
					});
					
					//AJAX 전송
					ajaxObj.send();
				}

				$('#calendar').fullCalendar('unselect');
				
			});
			
			
		},
		
		/* 일정 길이 조절 시 호출 하는 function */
	    eventResize: function(event, delta, revertFunc) {
			schdSeq = event.id;	// 순번 키
			startDay = fn_chDate( event.start, "s");	// 수정될 날짜 시작일
			endDay = fn_chDate(event.end, "e");		// 수정될 날짜 종료일
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/prs/prs4000/prs4000/updatePrs4000SchdInfoAjax.do'/>"}
					,{ "schdSeq":schdSeq, "startDt":startDay, "endDt": endDay });
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				data = JSON.parse(data);
				jAlert(data.message, "알림창");
			});
			//AJAX 전송
			ajaxObj.send();			    	
	    },
		
	    
	    /* 일정 이동 시 호출 */
	    eventDrop: function(event, delta, revertFunc) {
	    	
			schdSeq = event.id;	// 순번 키
			startDay = fn_chDate( event.start, "s");	// 수정될 날짜 시작일
			endDay = fn_chDate(event.end, "e");		// 수정될 날짜 종료일
			
			//AJAX 설정
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/prs/prs4000/prs4000/updatePrs4000SchdInfoAjax.do'/>"}
					, { "schdSeq":schdSeq, "startDt":startDay, "endDt": endDay });
			//AJAX 전송
			ajaxObj.send();
	    },
 		
	    /* 내용 수정 처리. */
	    eventClick: function(calEvent, jsEvent, view) {

	    	title 			= calEvent.title;
	    	schdSeq 	= calEvent.id;
			startDay 	= fn_chDate( calEvent.start, "s");	// 수정될 날짜 시작일
			endDay 		= fn_chDate(calEvent.end, "e");		// 수정될 날짜 종료일
			
			jPrompt('', title,'일정등록', function( result ) {
	
				if( !gfnIsNull( result ) ){		// 등록 내용이 있다면
					//AJAX 설정
					var ajaxObj = new gfnAjaxRequestAction(
							{"url":"<c:url value='/prs/prs4000/prs4000/updatePrs4000SchdInfoAjax.do'/>"}
							,{ "schdSeq":schdSeq, "startDt":startDay, "endDt": endDay , "schdContents":result });
					//AJAX 전송 성공 함수
					ajaxObj.setFnSuccess(function(data){
						// RELOAD
						$('#calendar').fullCalendar( 'refetchEvents');
					});

					//AJAX 전송
					ajaxObj.send();

					
				}
				
				$('#calendar').fullCalendar('unselect');
	
			});
		
    	},
 		
    	eventDragStop: function(event, jsEvent, ui, view) {
    		var schdSeq = event.id;
    	    var trashEl = $(".fc-view-container");
    	    var ofs = trashEl.offset();

    	    var x1 = Number( ofs.left );
    	    var x2 = Number( ofs.left + trashEl.outerWidth(true) );
    	    var y1 = Number( ofs.top );
    	    var y2 = Number( ofs.top + trashEl.outerHeight(true) );

    	    var pageX = Number(jsEvent.pageX);
    	    var pageY = Number(jsEvent.pageY);
    	    
    	    /* 달력 범위 에서 벗어나면 삭제 처리 한다. */
    	    if ( !((pageX >= x1) && (pageX <= x2) && (pageY >= y1) && (pageY <= y2)) ) {
	        	
	        	jConfirm( '일정을 삭제 하시겠습니까?', '알림창', function( result ) {
	        		
	        		if( result ){
	        			//AJAX 설정
	        			var ajaxObj = new gfnAjaxRequestAction(
	        					{"url":"<c:url value='/prs/prs4000/prs4000/deletePrs4000SchdInfoAjax.do'/>"}
	        					,{ "schdSeq":schdSeq });
	        			//AJAX 전송 성공 함수
	        			ajaxObj.setFnSuccess(function(data){
	        				$('#calendar').fullCalendar('removeEvents', event.id);	
	        			});
	        			
	        			//AJAX 전송 오류 함수
	        			ajaxObj.setFnError(function(xhr, status, err){
	        				data = JSON.parse(data);
				        	jAlert( data.message , "알림창" );
	        			});
	        			//AJAX 전송
	        			ajaxObj.send();
	        		}
	        	});
	        }
         }
    	/* 일정 색상 처리. */


		
	});

	


	
	/* 날짜 변환 */
	function fn_chDate( date , status ){
		
		var rDate = new Date( date );
		var rYear = rDate.getUTCFullYear();
		var rMonth = rDate.getUTCMonth()+1;
		var rDay = rDate.getUTCDate();
		
		if( status == "s"){
			rDate = rYear.toString() +"-"+ (("0"+rMonth.toString()).slice(-2))  +"-"+ (("0"+(rDay).toString()).slice(-2)) ;
		}
		
		if( status == "e"){
			rDate = rYear.toString() +"-"+ (("0"+rMonth.toString()).slice(-2))  +"-"+ (("0"+(rDay-1).toString()).slice(-2));
		}
		
		return rDate;	
	}
	


});


	
</script>
	

<div class="main_contents">
	<div class="prs_title">${sessionScope.selMenuNm }</div>
	<div class="prs4000_info_table">
		<!-- 달력을 그려준다. -->
		<div id='calendar'></div>
	</div>
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />
    