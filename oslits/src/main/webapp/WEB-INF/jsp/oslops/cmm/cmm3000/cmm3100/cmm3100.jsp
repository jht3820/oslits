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
		<link rel="stylesheet" href="<c:url value='/css/common/reset.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/font/font.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/common/common.css'/>">
		<link rel="stylesheet" href="<c:url value='/css/oslits/cmm.css'/>">
			
		<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>"></script>

		<script src="<c:url value='/js/common/common.js'/>"></script>
		<script src="<c:url value='/js/jquery/selectordie.min.js'/>"></script>
	</head>
	<style>
	
	</style>
	<Script>
	$(document).ready(function(){
		$("#all_agree").click(function() {
			if($("#all_agree").prop("checked")) {
				$("input[name=join_agree]").prop("checked",true);
			} else {
				$("input[name=join_agree]").prop("checked",false);
			}
		});
		
		/*동의 클릭시 회원가입 폼으로 이동*/
		$(".button_agree").click(function(){
			if($("input:checkbox[class='personal']").is(":checked") == false) {
				jAlert("개인정보 수집 및 이용에 대한 안내를 확인하시고 동의해주세요.","알림창");
				return;
			}
			if($("input:checkbox[class='use']").is(":checked") == false) {
				jAlert("이용약관 동의를 확인하시고 동의해주세요.","알림창");
				return;
			}
			$(location).attr('href', '/cmm/cmm3000/cmm3200/selectCmm3200View.do');
		});
		
		/*비동의 홈으로 이동*/
		$(".button_agree_cancel").click(function(){
			$(location).attr('href',"/cmm/cmm4000/cmm4000/selectCmm4000View.do");		
		});
		
		
	});
	</Script>
	<body style="background-color:#e5e9ec; width:100%; height:100%;">
		<div class="join_wrap">
			<div class="header_img">
				<!-- <img src="/images/login/login_logo2.png" alt="OpenSoftLab 로고" />  -->
			</div>
			
			<!-- 회원 가입 약관동의 -->
			<div class="agree_box_wrap" style="display:block;">
				<div class="join_border">
					<div class="header_line">
						<span class="lc">개인정보 수집 및 이용, OSL 이용약관에<br/>모두 동의합니다.</span>
						<span class="rc">
							<input type="checkbox" name="join_agree" id="all_agree" title="개인정보 수집 및 이용, OSL 이용약관에 모두 동의하는 체크박스"/>
							<label style="margin-top:7px;"></label>
						</span>
					</div>
					<div class="join_notice">
						<span class="lc">개인정보 수집 및 이용에 대한 안내<font style="color:#4b73eb; font-size:12px;">(필수)</font></span>	
						<span class="rc">
							<input type="checkbox" name="join_agree" class="personal" title="개입정보 수집 및 이용에 대한 안내 체크박스"/>
							<label style="margin-top:-5px;"></label>	
						</span>
						<div class="textarea_wrap">
							<textarea class="terms readonly" readonly title="개인정보 수집 및 이용에 대한 안내" readonly="readonly">
								'OpenSoftLab'은 (이하 '회사'는) 고객님의 개인정보를 중요시하며,
								"정보통신망 이용촉진 및 정보보호"에 관한 법률을 준수하고 있습니다.
회사는 개인정보취급방침을 통하여 고객님께서 제공하시는 개인정보가 어떠한 용도와 방식으로 이용되고 있으며, 
개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려드립니다.
회사는 개인정보취급방침을 개정하는 경우 웹사이트 공지사항(또는 개별공지)을 통하여 공지할 것입니다.

※ 본 방침은 2016 년 3 월 20 일 부터 시행됩니다.

·수집하는 개인정보 항목
회사는 회원가입, 상담, 서비스 신청 등등을 위해 아래와 같은 개인정보를 수집하고 있습니다.
ο 수집항목 : 이름, 전화번호, 이메일
ο 개인정보 수집방법 : 홈페이지(온라인 문의)

·개인정보의 수집 및 이용목적
회사는 수집한 개인정보를 다음의 목적을 위해 활용합니다.
ο 온라인 문의에 대한 답변 이행
ο 컨텐츠 제공

·개인정보의 보유 및 이용기간회사는 개인정보 수집 및 이용목적이 달성된 후에는 예외 없이 해당 정보를 지체 없이 파기합니다.
							</textarea>
						</div>					
					</div>
					<div class="join_notice">
						<span class="lc">OSL -------- 이용약관 동의<font style="color:#4b73eb; font-size:12px;">(필수)</font></span>	
						<span class="rc">
							<input type="checkbox" name="join_agree" class="use" title="OSL 이용약관 동의에 대한 체크박스"/>
							<label style="margin-top:-5px;"></label>	
						</span>
						<div class="textarea_wrap">
							<textarea class="terms readonly" readonly title="OSL 이용약관 안내" readonly="readonly">
								이 약관은 ..
							</textarea>
						</div>					
					</div>
					<div class="join_notice">
						<span class="lc">알림 메일 수신<font style="color:#999; font-size:12px;">(선택)</font></span>	
						<span class="rc">
							<input type="checkbox" name="join_agree" title="알림 메일 수신 안내 체크박스"/>
							<label style="margin-top:-5px;"></label>	
						</span>				
					</div>
				</div>	
				<div class="agree_btn_box">
					<span class="button_agree">동의</span><span class="button_agree_cancel">비동의</span>	
				</div>		
			</div>
			<!-- 회원 가입 약관동의 -->

			<div class="join_footer">Copyright ⓒ<span class="bold">Open Soft Lab</span> Corp. All Rights Reserved.</div>
		</div>				
	</body>
</html>