<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ko">
<title>OpenSoftLab</title>

<style type="text/css">

#repoftPdf{ width:100%;    padding: 20px 20px;   text-align: center;}
#reportMain{  margin-top: 15px; margin-bottom: 15px; margin-left: 20px;margin-right: 25px; }

.report_title{  width:100%;text-align:center;font-size: 24px;display:inline-block; }
.report_footer{  width:100%;text-align:left;margin-top: 20px; ;font-size: 17px;display:inline-block; }

.sign_div{  width: 100%; margin-top: 20px;display:inline-block; }

.report_main{  width: 100%; margin-top: 20px;display:inline-block; }

.sign_table{ border-collapse: collapse; width: 65%;float:right; }
.sign_table td,th{   border: 1px solid  #878787;  }
.sign_table .sign_td1{ height: 25px; }
.sign_table .sign_td2{ height: 25px;text-align:left; }

.report_table{ border-collapse: collapse; width: 100%; }
.report_table td{   border: 1px solid  #878787; height: 22px; }

</style>
<script type="text/javascript">
	var popSearch;
	var itemList  = [];
	$(document).ready(function() {
		
		// 팝업 화면 유형 구분값 - 등록/수정/상세
		
		//$("#year").focus();


	
		/* 배포 버전 텍스트 숫자만 입력가능 */
		
		$('#btn_print_pdf').click(function() {
			
			window.open("<c:url value='/adm/adm8000/adm8100/selectAdm8104View.do'/>"
					,"report"
					,"width=1000px, height=1000px, menubar=no,status=no,toolbar=no,resizable=no");		

			
		});
		
		/* 취소 */
		$('#btn_cancle').click(function() {
			gfnLayerPopupClose();
		});
				
		fnSelectAdm8100ReportInfo('${param.reportCd}', '${param.meaDtm}' );
		
	});
	
	
	
	function fnSelectAdm8100ReportInfo(reportCd, meaDtm ){
		
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/adm/adm8000/adm8100/selectAdm8100ReportInfoAjax.do'/>"}
				,{ "reportCd" : reportCd , "meaDtm" : meaDtm });
		//AJAX 전송 성공 함수
		ajaxObj.async = false;

		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	var reportInfo = data.reportInfo;
			
			$('#tmNm').html(reportInfo.tmNm);
			$('#chargerNm').html(reportInfo.chargerNm);
			$('#pmNm').html(reportInfo.pmNm);
			var list = data.list;
			itemList = data.list;
			var itemCd = "";
			var lastItemCd = "";
			var indexCd = "";
			var rowSpanNum = 0;
			var html = '';
			var spanMap = {}; 
			for(var i=0; i< list.length; i++){
												
				html += '<tr class="dataRow" >';
				if(i==0){
					itemCd = list[i].itemCd;
				}
				if(indexCd != list[i].indexCd){
					html += '	<td id="1st_td_'+list[i].itemCd+'" >'+lineBreack(list[i].indexNm , 2 )+'</td>';
					if(i!=0){
						spanMap[itemCd]=rowSpanNum;
					}
					rowSpanNum = 1;
					itemCd = list[i].itemCd;
				}else{
					rowSpanNum ++;
				}
				
				html += '	<td style="text-align: left;" >';
				html += 	 list[i].itemNm+'</td>';
				html += '	<td>'+list[i].weightVal+'</td>';
				html += '	<td>'+list[i].periodNm+'</td>';
				html += '	<td>'+nvl(list[i].meaVal)+'</td>';
				html += '	<td>'+nvl(list[i].apprVal)+'</td>';
				html += '	<td>'+nvl(list[i].optVal)+'</td>';
				html += '	<td>'+nvl(list[i].modifyApprVal)+'</td>';
				html += '	<td id="rep_data_td'+i+'" >'+nvl(list[i].modifyOptVal)+'</td>';
								
				
				html += '</tr>';
			
				indexCd = list[i].indexCd;
			}
			if(rowSpanNum!=1){
				spanMap[ itemCd]=rowSpanNum;
			}
			$("#repTbody").append(html);
			$(".itext").css("width","40px");
			$(".itext").css("min-width","40px");
			
			$.each(spanMap,function(item,val){
				$("#1st_td_"+item).attr("rowspan",val);
			});	
		

			
		});
		
		//AJAX 전송 오류 함수 서버시간 조회 실패시 클라이언트 시간 조회
		ajaxObj.setFnError(function(xhr, status, err){
					
	 	});
		//AJAX 전송
		ajaxObj.send();

	}
	
	function nvl(obj){
		if(obj==null){
			return "";
		}
		return obj;
	}
	
	function lineBreack(obj,index){
		var rStr="";
		for(var i=0; i < obj.length;i++){
			if(i==  obj.length-1 ){
				rStr+=obj.charAt(i);
			}else if(( i + 1) % index == 0){
				rStr+=obj.charAt(i)+'<br>';
			}else{
				rStr+=obj.charAt(i);
			}			
		}
		
		return 	rStr;	
	}
	

</script>

<div class="popup">
	<form id="admInsertForm"  onsubmit="return false;">
	
	<div class="pop_title">보고서 상세</div>
	<div class="pop_sub">
		<div id="repoftPdf">
		<div id="reportMain" >
			<div class="report_title" >
				<span style="font-size: 34px;border-bottom:1px solid  #000000;" >${param.reportNm}</span>
				</br>
				<span style="font-size: 24px;" >(측정월: ${param.meaFmDtm})</span>
			</div>
			<div class="sign_div" >
				<table class="sign_table" >
					<caption style="display:none;" >보고서</caption>
					<thead>
						<tr style="display:none;" >
							<th style="width:20%;" ></th>
							<th style="width:60%;" ></th>						
							<th style="width:20%;" ></th>						
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="sign_td1" id="sign_tr1_td1"  >팀장</td>
							<td class="sign_td2" id="sign_tr1_td2"  >
								<span id="tmNm" style="margin-left: 20px;" ></span>
							</td>
							<td class="sign_td3" id="sign_tr1_td3"  >(서명)</td>
						</tr>
						<tr>
							<td class="sign_td1" id="sign_tr2_td1" >담당</td>
							<td class="sign_td2" id="sign_tr2_td2"  >
								<span id="chargerNm" style="margin-left: 20px;" ></span>
							</td>
							<td class="sign_td3" id="sign_tr2_td3"  >(서명)</td>
						</tr>
						<tr>
							<td class="sign_td1" id="sign_tr3_td1" >PM</td>
							<td class="sign_td2" id="sign_tr3_td2"  >
								<span id="pmNm" style="margin-left: 20px;" ></span>
							</td>
							<td class="sign_td3" id="sign_tr3_td3"  >(서명)</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="report_main" >
				<table class="report_table" id="repTbody" >
					
					<tr style="display:none;" >
							<td style="width:45px;"></td>
							<td style="width:166px;"></td>
							<td style="width:49px;"></td>
							<td style="width:71px;"></td>
							<td style="width:41.5px;"></td>
							<td style="width:41.5px;"></td>
							<td style="width:37.7px;"></td>
							<td style="width:37.7px;"></td>
							<td style="width:41.5px;"></td>													
					</tr>
					<tr class="headerRow" >
						<td id="rep_tr1_td1" style="hight:75px;" rowspan="3">지표<br>구분</td>
						<td id="rep_tr1_td2" style="hight:75px;" rowspan="3">측&nbsp;정&nbsp;항&nbsp;목</td>
						<td id="rep_tr1_td3" style="hight:75px;" rowspan="3">가중치<br>(%)</td>
						<td id="rep_tr1_td4" style="hight:75px;" rowspan="3">보고/<br>평가주기</td>
						<td id="rep_tr1_td5" style="hight:25px;" colspan="2">KIDA&nbsp;보고서</td>
						<td id="rep_tr1_td6" style="hight:25px;" colspan="3">검토결과</td>
					</tr>
					<tr class="headerRow">
						<td id="rep_tr2_td1" style="hight:50px;" rowspan="2">측정값</td>
						<td id="rep_tr2_td2" style="hight:50px;" rowspan="2">평가수준</td>
						<td id="rep_tr2_td3" style="hight:50px;" rowspan="2">적정<br>/부정적</td>
						<td id="rep_tr2_td4" style="hight:25px;" colspan="2">변경값(부적정시)</td>
					</tr>
					<tr class="headerRow">
						<td id="rep_tr3_td1" style="hight:25px;" >측정값</td>
						<td id="rep_tr3_td2" style="hight:25px;" >평가수준</td>
					</tr>
					

				</table>
			</div>
			<div class="report_footer" >
				* "보안절차 준수현황, 인력교체율" 측정값은 '월 실적값 (누적값)'으로 표시하고,<br>
				평가는 각각 분기 및 12월에만 실시
			</div>
		</div>
		</div>
		
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_print_pdf" >인쇄</div>
			<div class="button_normal exit_btn" id="btn_cancle">취소</div>
		</div>
	</div>
	</form>
</div>
</html>