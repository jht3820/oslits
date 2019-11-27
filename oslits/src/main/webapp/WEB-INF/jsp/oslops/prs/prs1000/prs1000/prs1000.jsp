<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/oslops/top/header.jsp" %>
<jsp:include page="/WEB-INF/jsp/oslops/top/aside.jsp" />

<link rel='stylesheet' href='<c:url value='/css/oslits/prs.css'/>' type='text/css'>
<style>
</style>
<script>
	$(document).ready(function(){
		var inventory_page = $('.now_inventory_page').length;
		$('.now_inventory_list').css('width', inventory_page * 100 + '%');
		
		var page_view=8;	
		var maxstage=$('.tutorial_maxstage').html();
		
		var active_page=$('.now_inventory_page').has('.active').attr('data');		
		var total_page = Math.round( maxstage/page_view );
		var view_page = active_page;
			
		var inventory_sort = $('.now_inventory_sort').width();
		/*진행 단계 페이지*/
		$('.now_inventory_sort').animate({scrollLeft: '+='+inventory_sort*(active_page-1)}, 680);
		
		/* 튜토리얼 진행 단계*/
		var progress= $('.active .now_list_num').html();
		$('.tutorial_progress').html(progress);
		var percent = (progress/maxstage)*100;	

		$('.tutorial_stage > span').css('width',  + percent + '%');
		$('.arrow_position').css('margin-left', 'calc('+ percent + '% - 34px)' );
		$('.arrow_position').css('margin-left', '-webkit-calc('+ percent + '% - 17px)' );
		$('.tutorial_stage > span').css('background', '#4b73eb');
		
		/*상세 단계 넘기기*/
		$('.now_next_btn').click(function(){						
			if($('.active').next().html() != null){
				$('.active').next().addClass('title_progress active');
				$('.active').first().removeClass('title_progress active');
				$('.active').prevAll().addClass('title_end');$('.now_inventory_sort');
					progress++;
					$('.tutorial_progress').html(progress);
					var percent = (progress/maxstage)*100;	
					$('.tutorial_stage > span').css('width',  + percent + '%');
					$('.arrow_position').css('margin-left', 'calc('+ percent + '% - 34px)' );
					$('.arrow_position').css('margin-left', '-webkit-calc('+ percent + '% - 17px)' );
					$('.tutorial_stage > span').css('background', '#4b73eb');
					if(progress == maxstage){
						$('.tutorial_stage > span').css('border-radius', '15px 15px');
					}
			}else{
				$('.active').parent().next().children().first('.now_list').addClass('title_progress active');
				$('.active').parent().prev().last().children('.now_list').last().removeClass('title_progress active');
				$('.active').parent().prev().last().children('.now_list').last().addClass('title_end');
				view_page++;
				$('.now_inventory_sort').animate({scrollLeft: '+='+inventory_sort}, 680);
				if(view_page ==total_page){
					$('.now_inventory_next').css('border', '1px solid #ccc');
					$('.now_inventory_next').css('background', '#f9f9f9');
					$('.now_inventory_next').css('color', '#ccc');
				}
				if(progress < maxstage){
					progress++;
					$('.tutorial_progress').html(progress);
					var percent = (progress/maxstage)*100;	
					$('.tutorial_stage > span').css('width',  + percent + '%');
					$('.arrow_position').css('margin-left', 'calc('+ percent + '% - 34px)' );
					$('.arrow_position').css('margin-left', '-webkit-calc('+ percent + '% - 17px)' );
					$('.tutorial_stage > span').css('background', '#4b73eb');
					if(progress == maxstage){
						$('.tutorial_stage > span').css('border-radius', '15px 15px');
					}
				}
			}
		});

		/*이전 페이지*/
		$('.now_inventory_prev').click(function() {
			if(view_page > 1){
				view_page--;
				$('.now_inventory_sort').animate({scrollLeft: '-='+inventory_sort}, 680);
			}else{
				jAlert('첫','알림창');
			}
			
			if(view_page ==1){
				$('.now_inventory_prev').css('border', '1px solid #ccc');
				$('.now_inventory_prev').css('background', '#f9f9f9');
				$('.now_inventory_prev').css('color', '#ccc');
			}else{
				$('.now_inventory_prev').css('border', '1px solid #b8b8b8');
				$('.now_inventory_prev').css('background', '#fff');
				$('.now_inventory_prev').css('color', '#ababab');
			}
			if(view_page >=total_page){
				$('.now_inventory_next').css('border', '1px solid #ccc');
				$('.now_inventory_next').css('background', '#f9f9f9');
				$('.now_inventory_next').css('color', '#ccc');
			}else{
				$('.now_inventory_next').css('border', '1px solid #b8b8b8');
				$('.now_inventory_next').css('background', '#fff');
				$('.now_inventory_next').css('color', '#ababab');
			}
		});
		/*다음 페이지*/
		$('.now_inventory_next').click(function() {			
			if(view_page < total_page){
				view_page++;
				$('.now_inventory_sort').animate({scrollLeft: '+='+inventory_sort}, 680);
			}else{
				jAlert('끝','알림창');
			}
			
			if(view_page ==1){
				$('.now_inventory_prev').css('border', '1px solid #ccc');
				$('.now_inventory_prev').css('background', '#f9f9f9');
				$('.now_inventory_prev').css('color', '#ccc');
			}else{
				$('.now_inventory_prev').css('border', '1px solid #b8b8b8');
				$('.now_inventory_prev').css('background', '#fff');
				$('.now_inventory_prev').css('color', '#ababab');
			}
			if(view_page ==total_page){
				$('.now_inventory_next').css('border', '1px solid #ccc');
				$('.now_inventory_next').css('background', '#f9f9f9');
				$('.now_inventory_next').css('color', '#ccc');
			}else{
				$('.now_inventory_next').css('border', '1px solid #b8b8b8');
				$('.now_inventory_next').css('background', '#fff');
				$('.now_inventory_next').css('color', '#ababab');
			}
		});
	});
</script>
<div class="main_contents">
	<div class="prs_title">${sessionScope.selMenuNm }</div>
	<div class="set_prs1000">
		<div class="title_prs1000">
			<div class="tutorial">
				<span class="tutorial_title">튜토리얼 진행 단계</span>
				<div class="tutorial_stage">
					<span></span>
				</div>
				<div class="tutorial_stage_num">
					<span class="tutorial_progress"></span><span>/</span><span class="tutorial_maxstage">23</span><!-- 최대 갯수 바뀌면 변경 -->
				</div>
				<span class="tutorial_next_btn">다음 단계</span>
			</div>
			<div class="arrow">
				<div class="arrow_position">
					<div class="arrow_border" >&nbsp;</div>
					<div class="arrow_inside" >&nbsp;</div>
				</div>
			</div>
			<div class="now_stage">
				<div class="now_stage_title">
					<span> 현재 진행 단계 : </span><span>요구사항 입력</span>
				</div>
				<div class="now_inventory">
					<span class="now_inventory_prev">&#60;</span>
					<div class="now_inventory_sort">
						<div class="now_inventory_list">
							<div class="now_inventory_page" data="1">
							<span class="now_list title_end">
								<span class="now_list_num">01</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">02</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">03</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">04</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">05</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">06</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">07</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">08</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							</div>
							<div class="now_inventory_page" data="2">
							<span class="now_list title_end">
								<span class="now_list_num">09</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_end">
								<span class="now_list_num">10</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list title_progress active">
								<span class="now_list_num">11</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">12</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">13</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">14</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">15</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">16</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							</div>
							<div class="now_inventory_page" data="3">
							<span class="now_list">
								<span class="now_list_num">17</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">18</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">19</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">20</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">21</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">22</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							<span class="now_list">
								<span class="now_list_num">23</span>
								<span class="now_list_title">요구사항 양식 업로드</span>
							</span>
							</div>
						</div>
					</div>
					<span class="now_inventory_next">&#62;</span>
					<div class="detail_next">
						<span class="now_next_btn">상세 단계 넘기기</span>
					</div>
				</div>
			</div>
		</div>
		<div class="btn_prs1000">
			<span class="set_btn exit_btn">튜토리얼 종료</span>
			<span class="set_btn restart_btn">튜토리얼 재시작</span>
		</div>
	</div>
</div>
	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />