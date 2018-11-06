<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>


<script src="/js/jquery/jquery-1.11.2.min.js" ></script>
<script src="/js/common/common.js" ></script>
<script type="text/javascript" src="/js/html2canvas/html2canvas.js"></script>
<script type="text/javascript" src="/js/jspdf/jspdf.min.js"></script>
<script type="text/javascript" src="/js/bluebird/bluebird.min.js"></script>
<style type="text/css">

#repoftPdf{ width:100%;    padding: 20px 20px;   text-align: center;}
#reportMain{  
margin-top: 77px;
margin-left: 79px;
margin-right: 79px;
margin-bottom: 86px; 
 font-family :  "굴림", Gulim, Arial, sans-serif; 
/*font-family : "궁서체", GungsuhChe, Arial, sans-serif; */ 
/*font-family : "Arial, Dotum, "돋움", sans-serif; */ 

}


.report_title{  width:100%;text-align:center;font-size: 24px;display:inline-block;    margin-top: 68px; }
.report_footer{  width:100%;text-align:left;margin-top: 20px; ;font-size: 17px;display:inline-block; }

.sign_div{  width: 100%; margin-top: 20px;display:inline-block; }

.report_main{  width: 100%; display:inline-block;
margin-top: 8px;
 }

.sign_table{ border-collapse: collapse; width:415px;float:right; border: 1px }
.sign_table td,th{  
 border: 1px solid  #000000;
 border-right:none;
 border-top:none;
 
  }
.sign_table .sign_td1{ height: 43px; 
	text-align: center;
  	vertical-align: middle;
}
.sign_table .sign_td2{ height: 43px; }

.report_table{ border-collapse: collapse;width: 100%;font-size:12px; border: 1px}
.report_table td{   border: 1px solid  #000000; height: 28px;
	text-align: center;
  	vertical-align: middle;
  	border-right:none;
  	border-top:none;  
 }
 .headerRow{ height: 27.5px; }
 .dataRow{ height: 38px; }
 #printHtml{ 
 width: 793px;
 height: 1118.5px;
 }
</style>
<script type="text/javascript">

	$(document).ready(function() {
		$("#printHtml").html( $("#repoftPdf",opener.document).html());
		
		
		gfnGetHtmlToPdf($("#printHtml"),"보고서.pdf");
		
		/*
		document.body.style.overflow='hidden';  
	    if (navigator.userAgent.indexOf('Chrome')>-1) {  
				    window.resizeTo(698, 900);   
	    }
	    */
		
	});
	
	</script>
</head>
<body>
	<div id="printHtml">
	</div>
	<div id="editor"></div>
</body>
</html>