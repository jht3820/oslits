<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head> 
	<meta charset="UTF-8">
	<!-- 모바일 : viewport 제거 -->
	<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
	<title>Open Soft Lab</title>
	
<link rel='stylesheet' href='<c:url value='/css/oslits/adm.css'/>' type='text/css'>   
<link rel="stylesheet" href="<c:url value='/css/oslits/cmm.css'/>">
<link rel="stylesheet" href="<c:url value='/css/common/common.css'/>">

	<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>"></script>

<script src="<c:url value='/js/common/common.js'/>" ></script>

<script type="text/javascript">
	$(function(){
		$('.cmm_table').scrolltable({
			stripe: true
		});
	});
</script>

<style>
.cmm_chk input[type="checkbox"] { top: 13px; }

	.search_select { width: 124px; height: 28px; margin: 0 5px 5px 0;}
	.search_box_wrap { width: calc(100% - 404px);} /* width:calc(100% - (.search_select너비 * 갯수 + 32px))  */
	
	.layer_popup_box .close_btn{top:12px; width:18px; height:18px; background:url(/images/login/x_white.png) no-repeat}
	.cmm_table td:first-child{text-align:center;}
	table tr:hover{ background:none; }
	.st-body-scroll tr:hover{ background:#f9f9f9; }
</style>

</head>
<body>
<div class="user_pop_wrap">
	<div class="pop_header">프로젝트 팝업</div>
	<div class="pop_contents_wrap">
		<div class="pop_first_wrap">
			<div class="first_contents_line">
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
		</div>
		<div class="pop_second_box">
			<table class="cmm_table">
				<colgroup>
					<col style="width:5%">	
					<col style="width:20%">	
					<col style="width:25%; text-align:left;">	
					<col style="width:15%; text-align:left;">	
					<col style="width:10%">	
					<col style="width:25%">		
				</colgroup>
				<thead style="text-align:center;">
					<tr>
						<td class="back_color">선택</td>
						<td class="back_color">프로젝트 ID</td>
						<td class="back_color">대 프로젝트</td>
						<td class="back_color">단위 프로젝트</td>
						<td class="back_color">이름</td>
						<td class="back_color">이메일</td>
					</tr>
				</thead>
				<tbody style="text-align:center;">
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td> 
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
					<tr>
						<td><div class="cmm_chk"><input type="checkbox" title="체크박스" name=""/><label></label></div></td>
						<td>대프로젝트 타이틀1</td>
						<td>대프로젝트</td>
						<td></td>
						<td>산다라박</td>
						<td>charismauky@naver.com</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="pop_button_box">
			<span class="pop_c_button">완료</span>
			<span class="pop_button">닫기</span>	
		</div>
	</div>
</div>
</body>
</html>