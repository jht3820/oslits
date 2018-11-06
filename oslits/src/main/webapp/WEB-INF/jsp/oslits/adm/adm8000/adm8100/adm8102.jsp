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
.sign_table td,th{   border: 2px solid  #878787;  }
.sign_table .sign_td1{ height: 28px; }
.sign_table .sign_td2{ height: 28px; }

.report_table{ border-collapse: collapse; width: 100%; }
.report_table td{   border: 2px solid  #878787; height: 28px; }

</style>
<script type="text/javascript">
	var popSearch;
	
	//유효성	
	var arrChkObj = {"tmNm":{"type":"length","msg":"팀장 명은 50byte까지 입력이 가능합니다.",max:50}
					 ,"chargerNm":{"type":"length","msg":"담당자 명은 50byte까지 입력이 가능합니다.", "max":50}
					 ,"pmNm":{"type":"length","msg":"PM 명은 50byte까지 입력이 가능합니다.", "max":50}
					};
	
	$(document).ready(function() {
		
		// 팝업 화면 유형 구분값 - 등록/수정/상세
		var pageType = '${param.popupGb}';
		
		//$("#year").focus();
		
		//유효성 체크		
		gfnInputValChk(arrChkObj);


		fnSelectAdm8100ReportInfo('${param.reportCd}', '${param.meaDtm}' );
		
	
		/* 배포 버전 텍스트 숫자만 입력가능 */
		
		$('#btn_update_item').click(function() {
			
			var txts = $('.itext');
			var isCheck = true;
			for(var i = 0; i<txts.length; i++){
				if(isCheck  ){
					isCheck =fnValidWord(txts[i]);		
				}
			}
			if(isCheck  ){			
				// 보고서 수정 전 유효성 체크
		 		if(!gfnSaveInputValChk(arrChkObj)){ 
		 			return false;	
		 		}	
				// 보고서 수정
				fnUpdateAdm800ReportInfo();
			}
			
		});
		
		/* 취소 */
		$('#btn_cancle').click(function() {
			gfnLayerPopupClose();
		});
		
		
		function fnUpdateAdm800ReportInfo(){
			//FormData에 input값 넣기
			
			var fd =$('#admInsertForm').serialize();
			
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/adm/adm8000/adm8100/updateAdm8100ReportInfo.do'/>","loadingShow":true}
					,fd);
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
	        	//등록 실패의 경우 리턴
	        	if(data.saveYN == 'N'){
	        		toast.push(data.message);
	        		return;
	        	}
	        	
	        	var pars = mySearch.getParam();
			    var ajaxParam = $('form#searchFrm').serialize();

			    if(!gfnIsNull(pars)){
			    	ajaxParam += "&"+pars;
			    }
        		//그리드 새로고침
            	fnInGridListSet( firstGrid.page.currentPage,ajaxParam);
        		
				jAlert(data.message, '알림창', function( result ) {
					if( result ){
			        	gfnLayerPopupClose();
					}
				});
			});
			
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				toast.push("ERROR STATUS("+data.status+")<br>"+data.statusText);
	        	gfnLayerPopupClose();
			});
			//AJAX 전송
			ajaxObj.send();
		}
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
			
			$('#tmNm').val(reportInfo.tmNm);
			$('#chargerNm').val(reportInfo.chargerNm);
			$('#pmNm').val(reportInfo.pmNm);

			var list = data.list;
			var itemCd = "";
			var indexCd = "";
			var rowSpanNum = 0;
			var html = '';
			var spanMap = {}; 
			for(var i=0; i< list.length; i++){
												
				html += '<tr>';
				if(i==0){
					itemCd = list[i].itemCd;
				}
				if(indexCd != list[i].indexCd){
					html += '	<td id="1st_td_'+list[i].itemCd+'" >'+list[i].indexNm+'</td>';
					if(i!=0){
						spanMap[itemCd]=rowSpanNum;
					}
					rowSpanNum = 1;
					itemCd = list[i].itemCd;
				}else{
					rowSpanNum ++;
				}
								
				html += '	<td><input type="hidden" name="itemCd" value="'+list[i].itemCd+'" />';
				html += 	list[i].itemNm+'</td>';
				html += '	<td>'+list[i].weightVal+'</td>';
				
				html += '	<td>'+list[i].periodNm+'</td>';
				html += '	<td>'+nvl(list[i].calVal)+'</td>';
				
				var month =meaDtm.substring(4);
				
				if(list[i].periodCd=='03' && ( month !='03' && month !='06' && month !='09' && month != '12'   ) ){// 분기
					html += '	<td><input class="itext" type="text" name="meaVal" size="5" readonly onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].meaVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="apprVal" size="5" readonly onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].apprVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="optVal" size="5" readonly onblur="fnValidWord(this);"maxlength="50" value="'+nvl(list[i].optVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="modifyApprVal" readonly onblur="fnValidWord(this);" maxlength="50" size="5" value="'+nvl(list[i].modifyApprVal)+'"/></td>';
					html += '	<td><input class="itext" type="text" name="modifyOptVal" readonly onblur="fnValidWord(this);" maxlength="50" size="5" value="'+nvl(list[i].modifyOptVal)+'" /></td>';
				}else if(list[i].periodCd=='04' && ( month !='06' && month != '12'   ) ){ //반기
					html += '	<td><input class="itext" type="text" name="meaVal" readonly size="5" onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].meaVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="apprVal" readonly size="5" onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].apprVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="optVal" readonly size="5" onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].optVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="modifyApprVal" readonly  onblur="fnValidWord(this);" maxlength="50" size="5" value="'+nvl(list[i].modifyApprVal)+'"/></td>';
					html += '	<td><input class="itext" type="text" name="modifyOptVal"  readonly onblur="fnValidWord(this);" maxlength="50" size="5" value="'+nvl(list[i].modifyOptVal)+'" /></td>';
				}else{
					html += '	<td><input class="itext" type="text" name="meaVal" size="5" onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].meaVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="apprVal" size="5" onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].apprVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="optVal" size="5" onblur="fnValidWord(this);" maxlength="50" value="'+nvl(list[i].optVal)+'" /></td>';
					html += '	<td><input class="itext" type="text" name="modifyApprVal" onblur="fnValidWord(this);" maxlength="50" size="5" value="'+nvl(list[i].modifyApprVal)+'"/></td>';
					html += '	<td><input class="itext" type="text" name="modifyOptVal" onblur="fnValidWord(this);" maxlength="50" size="5" value="'+nvl(list[i].modifyOptVal)+'" /></td>';
				}
				html += '</tr>';				
				indexCd = list[i].indexCd;
			}
			if(rowSpanNum!=1){
				spanMap[ itemCd]=rowSpanNum;
			}
			$("#repTbody").html(html);
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
	
	function fnValidWord(obj){
		var str = $(obj).val();
		var length = gfnStrByteLen(str);
		
		if(length> 50){
			alert("입력항목이 50Byte를 초과 하였습니다");
			//$(obj).focus();
			return false;
		}
		return true;
	}

	
</script>

<div class="popup">
	<form id="admInsertForm"  onsubmit="return false;">
		<input type="hidden"  name="popupGb" id="popupGb" value="${param.popupGb}" />
		<input type="hidden"  name="reportCd" id="reportCd" value="${param.reportCd}" />
		<input type="hidden"  name="meaDtm" id="meaDtm" value="${param.meaDtm}" />
	<div class="pop_title">보고서 상세</div>
	<div class="pop_sub">
		<div id="repoftPdf">
		<div id="reportMain" >
			<div class="report_title" >
				<u>${param.reportNm}</u><br>
				(측정월: ${param.meaFmDtm})
			</div>
			<div class="sign_div" >
				<table class="sign_table" >
					<caption>보고서</caption>
					<thead>
						<tr style="display:none;"  >
							<th style="width:20%;" ></th>
							<th style="width:80%;" ></th>						
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="sign_td1" >팀장</td>
							<td class="sign_td2" ><input type="text" class="repInput" name="tmNm" id="tmNm" value="" 
							style="height: 21px;min-width: 190px;width: 190px;"
							/>(서명)</td>
						</tr>
						<tr>
							<td class="sign_td1" >담당</td>
							<td class="sign_td2" ><input type="text" class="repInput" name="chargerNm" id="chargerNm" value=""
							style="height: 21px;min-width: 190px;width: 190px;"
							 />(서명)</td>
						</tr>
						<tr>
							<td class="sign_td1" >PM</td>
							<td class="sign_td2" ><input type="text" class="repInput" name="pmNm" id="pmNm" value=""
							style="height: 21px;min-width: 190px;width: 190px;"
							 />(서명)</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="report_main" >
				<table class="report_table" >
					<caption>보고서</caption>
					<thead>
						<tr style="display:none;"  >
							<th style="width:10%;"></th>
							<th style="width:22%;"></th>
							<th style="width:9%;"></th>
							<th style="width:9%;"></th>
							<th style="width:7%;"></th>
							<th style="width:9%;"></th>
							<th style="width:9%;"></th>
							<th style="width:9%;"></th>
							<th style="width:9%;"></th>
							<th style="width:9%;"></th>														
						</tr>
						<tr>
							<th style="hight:75px;" rowspan="3">지표<br>구분</th>
							<th style="hight:75px;" rowspan="3">측&nbsp;정&nbsp;항&nbsp;목</th>
							<th style="hight:75px;" rowspan="3">가중치<br>(%)</th>
							
							<th style="hight:75px;" rowspan="3">보고/<br>평가주기</th>
							<th style="hight:75px;" rowspan="3">측정<br>참조</th>
							<th style="hight:25px;" colspan="2">KIDA&nbsp;보고서</th>
							<th style="hight:25px;" colspan="3">검토결과</th>
						</tr>
						<tr>
							<th style="hight:50px;" rowspan="2">측정값</th>
							<th style="hight:50px;" rowspan="2">평가수준</th>
							<th style="hight:50px;" rowspan="2">적정<br>/부정적</th>
							<th style="hight:25px;" colspan="2">변경값(부적정시)</th>
						</tr>
						<tr>
							<th style="hight:25px;" >측정값</th>
							<th style="hight:25px;" >평가수준</th>
						</tr>
					</thead>
					<tbody id="repTbody" >
						
					</tbody>
				</table>
			</div>
		</div>
		</div>
		
		<div class="btn_div">
			<div class="button_normal save_btn" id="btn_update_item" >저장</div>
			<div class="button_normal exit_btn" id="btn_cancle">취소</div>
		</div>
	</div>
	</form>
</div>
</html>