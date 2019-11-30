/**
 * 공통 JS 인크루드
 */
$(function(){
	//$("head").append("<script src='/js/jquery/jquery.json-2.5.1.min.js'></script>");
	//$("head").append("<script src='/js/jquery/selectordie.min.js'></script>");

	var jQueryAjaxSettingsCache = jQuery.ajaxSettings.cache;

	// Disable the ?_=TIMESTAMP addition
	jQuery.ajaxSettings.cache = true;

	$("head").append("<script src='/js/jquery/jquery-ui.js'></script>");
	/*$("head").append("<script src='/js/jquery/jquery.mCustomScrollbar.concat.min.js'></script>");*/
	$("head").append("<script src='/js/jquery/jquery.scrolltable.js'></script>");
	$("head").append("<script src='/js/common/layerPopup.js'></script>");	
	$("head").append("<script src='/js/common/comOslops.js'></script>");		//OSL ITS 공통 스크립트 import
	$("head").append("<script src='/js/axisj/dist/AXJ.min.js'></script>"); 		//AXJ import
	$("head").append("<script src='/js/axisj/lib/AXSearch.js'></script>");
	/*$("head").append("<script src='/js/axisj/lib/AXGrid.js'></script>");*/
	$("head").append("<script src='/js/axisj/lib/AXSelect.js'></script>");
	$("head").append("<script src='/js/axisj/lib/AXInput.js'></script>");
	$("head").append("<script src='/js/common/EgovMultiFile.js'></script>"); 	//select 파일첨부 egov JS
	$("head").append("<script src='/js/jquery/jquery.alerts.js'></script>");		// jquery alerts import 
	$("head").append("<script src='/js/jquery/jquery.timepicker.min.js'></script>");		// jquery alerts import 
	$("head").append("<script src='/js/jquery/jquery.base64.js'></script>");
	$("head").append("<script src='/vendors/moment/min/moment.min.js'></script>");
	$("head").append("<script src='/vendors/bootstrap-daterangepicker/daterangepicker.js'></script>");
	$("head").append("<script src='/js/jquery/jquery.curvedarrow.js'></script>");
	$("head").append("<script src='/js/common/oslGuideContents.js'></script>");
	$("head").append("<script src='/js/common/printThis.js'></script>");
	$("head").append("<script src='/vendors/select2/js/select2.full.min.js'></script>");
	$("head").append("<script src='/vendors/select2/js/i18n/ko.js'></script>");
	
	// Restore the jquery ajax cache setting
	jQuery.ajaxSettings.cache = jQueryAjaxSettingsCache;

	/* 모든 select 박스에 플러그인 적용 */
	//$("select").selectOrDie();
	$('.main_contents').css('min-height',$(window).innerHeight()-160);
	
	//프로그래스바용 백그라운드 div 지정
	$("body").prepend('<div class="top_fixed"><div class="top_bg"><div class="top_str"></div><img class="fixed_loading" src="/images/loading.gif"/></div></div>');

   $('.user_img').each(function(n){
       $(this).error(function(){
         $(this).attr('src', '/images/contents/sample.png');
       });
    });
   
   //가이드 상자 호출
	gfnGuideKeyAction();
});

//공통 마우스 단축키 등록 이벤트
/*$(document).on('mousedown', function(){
	
	var ctrlCloseVar = ctrlCloseVar;
	//CTRL + 마우스 좌클릭 = 레이어 팝업창 닫기.
  	if(event.ctrlKey && (event.button == 0) && gfnIsNull(ctrlCloseVar)){
    	gfnLayerPopupClose();
    	
    	//마스크 박스가 있는 경우 컨트롤 클릭으로 제거
    	if(!gfnIsNull($('#maskBox'))){
    		$('#maskBox').remove();
    		$(document).off('scroll touchmove mousewheel');
    	}
    	return;
  	}
});*/
$(document).keyup(function(event){
	//ESC = 레이어 팝업창 닫기
  	if(event.keyCode == 27){
  		//레이어 팝업이 있는 경우
  		if($(".layer_popup_box").length > 0){
  			jConfirm("현재 화면을 닫으시겠습니까?","경고",function(result){
  				if(result){
  					gfnLayerPopupClose();	
  				}
  				
  			});
  		}
  	}
});

$(document).ready(function() {	
	 $('.select_table tbody tr').not('.selectNotTr').click(function(){
		$('.table_active').removeClass('table_active');
		$(this).addClass('table_active'); 
	});
});

/**
 * 헤드 타이틀 세팅
 * @param text
 */
function setTitle(text){
	$("head title").text(text + " - Open Soft Lab");
}