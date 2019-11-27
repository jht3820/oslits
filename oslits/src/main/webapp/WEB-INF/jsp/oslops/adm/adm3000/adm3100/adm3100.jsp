<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/adm.css'/>' type='text/css'>

<div class="main_contents">
	<div class="adm3000_title">${sessionScope.selMenuNm }</div>
	
	<div class="adm3000_title_td">
		<div class="title_td1">라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시</div>
		<div class="title_td2">
			라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시<br>
			라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시
		</div>
		<div class="title_td3">라이선스 정보 표시 및 라이선스 등록 방법, 라이선스 업데이트 방법 표시, 라이선스 정보 표시 및 라이선스 등록 방법</div>
	</div>
		
	<div class="adm3000_sub_td">
		<div class="sub_left_td">
			<div class="left_td_title">라이선스</div>
			<div class="left_td_sub">License Type - Demo</div>
		</div>
		
		<div class="sub_righ_td">
			<div class="subbox">
				<div class="righ_td_title">그룹 ID</div><div class="righ_td_sub">gruopldkey1000</div>
				<div class="righ_td_title">그룹 이름</div><div class="righ_td_sub">OpenSoftLab</div>
			</div>
			<div class="subbox">
				<div class="righ_td_title">구매 날짜</div><div class="righ_td_sub">2015-12-21</div>
				<div class="righ_td_title">만료 날짜</div><div class="righ_td_sub">2015-12-27<span class="red_font">(+7일)</span></div>
			</div>
			<div class="subbox fl">
				<div class="righ_td_title2">사용자 제한</div><div class="righ_td_sub2 fl">1/10</div>				
			</div>
		</div>
	</div>
	
	<div class="adm3100_sub">
		<div class="adm3100_sub_style">
			<div class="adm3100_sub_left fl">기간</div>
			<div class="adm3100_sub_right">
				<span class="adm3100_select">
					<select title="셀렉트 박스">
						<option selected>6개월</option>
						<option>1년</option>
						<option>2년</option>
					</select>
				</span>
			</div>
			<div class="adm3100_sub_left fl">사용자</div>
			<div class="adm3100_sub_right">
				<span class="adm3100_select">
					<select title="셀렉트 박스">
						<option selected>10명</option>
						<option>15명</option>
						<option>20명</option>
					</select>
				</span>
			</div>
			
			<div class="adm3100_sub_middle">
				<div class="middle_sub fl">
					<div class="mb_sub_note fl">
						<div class="mb_sub_title">기간 6개월</div>
						<div><span class="money_size">1,030,000</span><span>원</span></div>
					</div>
					<div class="mb_sub_note plus_img fl"></div>
					<div class="mb_sub_note fl">
						<div class="mb_sub_title">사용자 1명</div>
						<div><span class="money_size">1,030,000</span><span>원</span></div>
					</div>
				</div>
				<div class="middle_line fl"></div>
				<div class="middle_sub fl" style="color: red; margin-top: 5px;">					
					<span>최종 결제 금액</span>
					<span class="money_size2">2,060,000</span><span>원</span>
				</div>
			</div>
			
			<div class="adm3100_sub_left fl ok_line">결제 방법 선택</div>
			<div class="adm3100_sub_right ok_line sub_right_pd">
				<input id="available" type="radio" name="avaliable" checked><label for="available">신용카드</label>
				<input id="notAvailable" type="radio" name="avaliable"><label for="notAvailable">무통장 입금</label>
			</div>
			<div class="adm3100_sub_left fl no_line"></div><div class="adm3100_sub_right no_line no_pd">이하 결제 정보가 들어갑니다.</div>
		</div>
		<div class="button_complete" style="font-size: 1.1em;">유형변경</div>	
	</div>	
</div>

<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />