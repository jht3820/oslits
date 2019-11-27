<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/req.css'/>' type='text/css'>

<style>
	.req_right_contents{padding:19px;}
	input[type=text].search_txt { float: left; width: calc(100% - 436px); height: 28px; margin-right: 5px; font-size: 0.7em; border: 1px solid #ccc; }
	.search_select {    width: 124px;    height: 28px;    margin: 0 5px 5px 0;}
	.search_box_wrap {    width: calc(100% - 404px);} /* width:calc(100% - (.search_select너비 * 갯수 + 32px))  */
	/* 모바일 @media 제거 */
	/*
	@media screen and (max-width:1100px){
		input[type=text].search_txt {	width: calc(100% - px);	}
	}
	*/
</style>
<script>
	$(document).ready(function(){

				
		$('.m_req_list').click(function(){
			$('.main_mobile .req_right_table_wrap').fadeIn();
			backKeyEvent();
		});
		$('.search_btn').click(function(){
			$('.search_popup').fadeToggle();
		});
		
		$('.x_btn').click(function(){
			$('.layer_popup').fadeOut();
		});
		function backKeyEvent(){
			if (window.history && window.history.pushState) {
				window.history.pushState('forward', null, location.pathname);
				$(window).on('popstate', function() {
					if($('.layer_popup').is(":Visible")){
						$('.layer_popup').fadeOut();
						$("html, body").css("overflow-y", "auto");
					}
				});
			}
		}
		
		/* 버튼 선택 */
		$("#btn_all_ck").click(function() {
			if ($(this).is(':checked')) {
				$("input[name=btn_ck]").prop("checked", true);
			} else {
				$("input[name=btn_ck]").prop("checked", false);
			}
		});
	});
</script>

<div class="main_contents">
	<div class="main_pc"> <!-- pc, 테블릿 화면 -->
		<div class="req_title">${sessionScope.selMenuNm }</div>	
		<form id="searchFrm" name="searchFrm" method="post">
			<div class="search_box">
				<span class="search_select">
					<label for="search_select"></label>
					<select id="search_select1">
						<option selected>대프로젝트</option>
						<option>단위프로젝트</option>
					</select>
				</span>
				<span class="search_select">
					<label for="search_select"></label>
					<select id="search_select2">
						<option selected>대프로젝트</option>
						<option>단위프로젝트</option>
					</select>
				</span>
				<span class="search_select">
					<label for="search_select"></label>
					<select id="search_select3">
						<option selected>대프로젝트</option>
						<option>단위프로젝트</option>
					</select>
				</span>
				<span class="search_box_wrap">
					<input type="search" class="search_txt" title="검색창"/>
					<label></label>
					<span class="button_normal2">검색</span>
				</span>
			</div>
		</form>
		
		<div class="req_table_wrap">
			<div class="req_left_table_wrap">		
				<div class="btn_box">
					<span class="button_normal2">프린트</span>
					<span class="button_normal2">엑셀 저장</span>
				</div>
				<div class="req_table">
					<table style="width:100%;" class="select_table">
						<colgroup>
							<col style="width: 5%;">
							<col style="width: 10%;">
							<col style="width: 12.5%;">
							<col style="width: 12.5%;">
							<col style="width: 10%;">
							<col style="width: 10%;">
							<col style="width: 10%;">
							<col style="width: 10%;">
							<col style="width: 10%;">
							<col style="width: 10%;">
						</colgroup>
						<thead style="text-align:center;">
							<tr>
								<td class="req_thead" colspan="9">대프로젝트</td>
							</tr>
							<tr>
								<td class="req_thead ">
									<div class="req_chk">
										<input type="checkbox" title="체크박스" id="btn_all_ck" /><label for="btn_all_ck"></label>
									</div>
								</td>
								<td class="req_thead">개발주기</td>
								<td class="req_thead">분류</td>
								<td class="req_thead">요구사항</td>
								<td class="req_thead">기능</td>
								<td class="req_thead">담당자</td>
								<td class="req_thead">개발공수</td>
								<td class="req_thead">소요시간</td>
								<td class="req_thead">작업흐름</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
							
							<tr>
								<td class="search_td txt_of req_chk"><input type="checkbox" title="선택" name="btn_ck"><label></label></td>
								<td class="req_td"><span class="blue">Sprint1</span></td>
								<td class="req_td">로그인 및 화면</td>
								<td class="req_td">로그인 화면</td>
								<td class="req_td">로그인 기능</td>
								<td class="req_td">사용자1</td>
								<td class="req_td">4</td>
								<td class="req_td">1</td>
								<td class="req_td">진행 중</td>
							</tr>
						</tbody>
					</table>
					<div class="paginate paginate_font1">
						<a class="pre">&#60;&#60;</a>
						<a class="pre">&#60;</a>
						<strong><span>1</span></strong>
						<a><span>2</span></a>
						<a><span>3</span></a>
						<a><span>4</span></a>
						<a><span>5</span></a>
						<a><span>6</span></a>
						<a><span>7</span></a>
						<a><span>8</span></a>
						<a><span>9</span></a>
						<a><span>10</span></a>
						<a class="next">&#62;</a>
						<a class="next">&#62;&#62;</a>
					</div>
				</div>			
			</div>
			<form method="post" action="">	
				<div class="req_right_table_wrap">	
					<div class="req_right_box">
						<div class="req_right_topheader">
							<div class="req_right_header_title">(1depth)로그인 및 회원가입</div>
							<div class="req_right_header_title bold">&gt;(2depth)로그인 화면</div>
						</div>	
						<div class="req_right_contents">
							<div class="bold">상세정보</div>
						</div>
						<div class="req_right_contents">
							<div class="r_r_c_line">
								<span class="r_r_c_left">기능 :</span>
								<span class="r_r_c_right">로그인 기능</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">개발주기 :</span>
								<span class="r_r_c_right">로그인 기능</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">작업흐름 :</span>
								<span class="r_r_c_right">개발완료</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">중요도 :</span>
								<span class="r_r_c_right">보통</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">만든 날짜 :</span>
								<span class="r_r_c_right">2015.09.11 13:00</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">최종 수정 :</span>
								<span class="r_r_c_right">2015.09.11 13:00</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">링크 : </span>
								<span class="r_r_c_right"><a href="http://www.opensoftlab.kr">www.opensoftlab.kr</a></span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">담당자 :</span>
								<span class="r_r_c_right">이름</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">개발공수 :</span>
								<span class="r_r_c_right">0</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">소요시간 :</span>
								<span class="r_r_c_right">0(0.7 = 42분)</span>
							</div>
						</div>
						<div class="req_right_contents" style="border-bottom:none;">
							<div class="bold mb10">요구사항 설명</div>
							<textarea class="req_contents_box readonly" title="요구사항 설명" readonly="readonly"></textarea>
							<div class="bold mb10">첨부파일 목록</div>
							<textarea class="req_contents_box readonly" title="첨부파일 목록" readonly="readonly"></textarea>
							<div class="bold mb10">Comment</div>
								<div class="comment_wrap">
									<div class="comment_list">
										<span class="comment_user">사용자1</span>
										<span class="comment_date">16.01.11</span>
										<pre class="comment_contents">코멘트 내용이 들어갑니다.</pre>
									</div>
									<div class="comment_list">
										<span class="comment_user">사용자1</span>
										<span class="comment_date">16.01.11</span>
										<pre class="comment_contents">코멘트 내용이 들어갑니다.</pre>
									</div>
									<div class="comment_list">
										<span class="comment_user">사용자1</span>
										<span class="comment_date">16.01.11</span>
										<pre class="comment_contents">코멘트 내용이 들어갑니다.</pre>
									</div>
								</div>
							<div class="save_wrap"><input type="submit" class="button_normal2" value="저장" /></div>
						</div>
					</div>
				</div>
			</form>
		</div>		
	</div> <!-- pc, 테블릿 화면 -->
	<div class="main_mobile"> <!-- 모바일 화면 -->
		<span class="search_btn"><img src="<c:url value='/images/mobile/m_search_btn.png'/>" width=17 alt="검색버튼"/></span>
		<div class="search_wrap"><select><option selected>진행 중</option><option>개발 완료</option></select><span class="button_normal2">조회</span></div>
		<div class="m_req_table">
			<div class="m_req_list">
				<span class="m_req_left">
					<div class="grouping">로그인 화면</div>
					<div><span>개발주기&nbsp;:&nbsp;</span><span>Sprint1</span><span class="table_between">|</span><span>개발주기&nbsp;:&nbsp;</span><span>로그인 및 화면</span></div>
					<div><span>기능&nbsp;:&nbsp;</span><span>로그인 기능</span><span class="table_between">|</span><span>담당자&nbsp;:&nbsp;</span><span>사용자1</span></div>
					<div><span>개발공수&nbsp;:&nbsp;</span><span>4</span><span class="table_between">|</span><span>소요시간&nbsp;:&nbsp;</span><span>1</span></div>
				</span>
				<span class="m_req_right" data="1">진행 중</span>
			</div>
		</div>
		<div class="m_req_table">
			<div class="m_req_list">
				<span class="m_req_left">
					<div class="grouping">로그인 화면</div>
					<div><span>개발주기&nbsp;:&nbsp;</span><span>Sprint1</span><span class="table_between">|</span><span>개발주기&nbsp;:&nbsp;</span><span>로그인 및 화면</span></div>
					<div><span>기능&nbsp;:&nbsp;</span><span>로그인 기능</span><span class="table_between">|</span><span>담당자&nbsp;:&nbsp;</span><span>사용자1</span></div>
					<div><span>개발공수&nbsp;:&nbsp;</span><span>4</span><span class="table_between">|</span><span>소요시간&nbsp;:&nbsp;</span><span>1</span></div>
				</span>
				<span class="m_req_right" data="2">개발 완료</span>
			</div>
		</div>
		<div class="req_right_table_wrap"><!-- 모바일 레이어팝업 -->	
				<div class="req_right_box">
					<form>
						<div class="req_right_topheader">
							<!-- <span class="x_btn"><img src="<c:url value='/images/mobile/m_x_btn.png'/>" width=17 alt="닫기버튼"/></span> -->
							<div class="req_right_header_title">(1depth)로그인 및 회원가입</div>
							<div class="req_right_header_title bold">&gt;(2depth)로그인 화면</div>
						</div>	
						<div class="req_right_contents">
							<div class="bold">상세정보</div>
						</div>
						<div class="req_right_contents">
							<div class="r_r_c_line">
								<span class="r_r_c_left">기능&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">로그인 기능</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">개발주기&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">로그인 기능</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">작업흐름&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">개발완료</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">중요도 :</span>
								<span class="r_r_c_right">보통</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">만든 날짜&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">2015.09.11 13:00</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">최종 수정&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">2015.09.11 13:00</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">링크&nbsp;:&nbsp;</span>
								<span class="r_r_c_right"><a href="http://www.opensoftlab.kr">www.opensoftlab.kr</a></span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">담당자&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">이름</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">개발공수&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">0</span>
							</div>
							<div class="r_r_c_line">
								<span class="r_r_c_left">소요시간&nbsp;:&nbsp;</span>
								<span class="r_r_c_right">0(0.7 = 42분)</span>
							</div>
						</div>
						<div class="req_right_contents" style="border-bottom:none;">
							<div class="bold mb10">요구사항 설명</div>
							<textarea class="req_contents_box readonly" title="요구사항 설명" readonly="readonly"></textarea>
							<div class="bold mb10">첨부파일 목록</div>
							<textarea class="req_contents_box readonly" title="첨부파일 목록" readonly="readonly"></textarea>
							<div class="bold mb10">Comment</div>
							<textarea class="req_contents_box readonly" title="코멘트 내용" readonly="readonly"></textarea>
							<div class="save_wrap"><input type="submit" class="button_normal2" value="저장" /></div>
						</div>
					</form>
				</div>
			</div><!-- 레이어팝업 -->
			<div class="search_popup layer_popup"><!-- 검색 레이어팝업 -->
				<form method="post" action="">
					<div class="search_popup_title">검색<span class="x_btn"><img src="<c:url value='/images/mobile/m_x_btn.png'/>" width=17 alt="닫기버튼"/></span></div>
					<div class="search_require">
						<select class="require_list">
							<option selected>사용자 1</option>
							<option>사용자 2</option>
						</select>
						<label></label>
						<select class="require_list">
							<option selected>할 일</option>
							<option>할 일</option>
						</select>
						<label></label>
						<select class="require_list">
							<option selected>전체</option>
							<option>진행 중</option>
						</select>
						<label></label>
						<input class="require_input" /><label></label><input type="submit" class="button_normal2" value="검색"/>
					</div>
				</form>
			</div>
	</div>
	
</div>
	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />