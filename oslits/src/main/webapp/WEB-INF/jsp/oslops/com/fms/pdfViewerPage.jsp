<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>OSL - PDF Viewer</title>
	<link rel="stylesheet" href="<c:url value='/css/common/common.css'/>">
	<script src="<c:url value='/js/jquery/jquery-1.11.2.min.js'/>"></script>

	<script src="<c:url value='/js/common/common.js'/>"></script>
	<script src="<c:url value='/js/pdfobject/pdf.js'/>"></script>
	<script type="text/javascript">
		//pdf base64 데이터
		var pdfData = atob("${pdfData}");
		
		// The workerSrc property shall be specified.
		PDFJS.workerSrc = "<c:url value='/js/pdfobject/pdf.worker.js'/>";
		
		//필요 변수 선언
		var pdfDoc = null;
		var pageNum = 1;
		var pageRendering = false;
		var pageNumPending = null;
		var scale = 2;
		var canvas;
		var ctx;
		
		$(document).ready(function(){
			canvas = document.getElementById('pdfViewDiv');
			ctx = canvas.getContext('2d');
			
			/**
			 * If another page rendering in progress, waits until the rendering is
			 * finised. Otherwise, executes rendering immediately.
			 */
			function queueRenderPage(num) {
			  if (pageRendering) {
			    pageNumPending = num;
			  } else {
			    renderPage(num);
			  }
			}
			
			/**
			 * Displays previous page.
			 */
			function onPrevPage() {
			  if (pageNum <= 1) {
			    return;
			  }
			  pageNum--;
			  queueRenderPage(pageNum);
			}
			document.getElementById('prev').addEventListener('click', onPrevPage);
			
			function onStartPage() {
			  pageNum=1;
			  queueRenderPage(pageNum);
			}
			document.getElementById('start').addEventListener('click', onStartPage);
			
			/**
			 * Displays next page.
			 */
			function onNextPage() {
			  if (pageNum >= pdfDoc.numPages) {
			    return;
			  }
			  pageNum++;
			  queueRenderPage(pageNum);
			}
			document.getElementById('next').addEventListener('click', onNextPage);
			
			function onEndPage() {
			  pageNum=pdfDoc.numPages;
			  queueRenderPage(pageNum);
			}
			document.getElementById('end').addEventListener('click', onEndPage);
		});
		
		/**
		 * Get page info from document, resize canvas accordingly, and render page.
		 * @param num Page number.
		 */
		function renderPage(num) {
		  pageRendering = true;
		  // Using promise to fetch the page
		  pdfDoc.getPage(num).then(function(page) {
			  gfnShowLoadingBar(true);
		    var viewport = page.getViewport(scale);
		    canvas.height = viewport.height;
		    canvas.width = viewport.width;
		
		    // Render PDF page into canvas context
		    var renderContext = {
		      canvasContext: ctx,
		      viewport: viewport
		    };
		    var renderTask = page.render(renderContext);
		
		    // Wait for rendering to finish
		    renderTask.promise.then(function() {
		      pageRendering = false;
		      if (pageNumPending !== null) {
		        // New page rendering is pending
		        renderPage(pageNumPending);
		        pageNumPending = null;
		      }
		      gfnShowLoadingBar(false);
		    });
		    
		  });
		
		  // Update page counters
		  document.getElementById('page_num').textContent = pageNum;
		}
		
		
		// Using DocumentInitParameters object to load binary data.
		var loadingTask = PDFJS.getDocument({data: pdfData});
		loadingTask.promise.then(function(pdf) {
			pdfDoc = pdf;
 			//페이지 총 수
		    document.getElementById('page_count').textContent = pdf.numPages;

			//페이지 그리기
			renderPage(pageNum);
		}, function (reason) {
		  // PDF loading error
		  console.error(reason);
		  gfnShowLoadingBar(false);
		});
		
		
	</script>
	<style type="text/css">
		.button_normal2 {min-width: 44px;background: #fff;font-size: 0.9em !important;margin: 10px 2px;}
	</style>
</head>
<body>
	<div>
		<span id="pdfDownLink" class="button_normal2" onclick="javascript:gfnFileDownload($('#pdfDownLink'),true)" atchId="${atchFileId}" fileSn="${fileSn}">다운로드</span>&nbsp;&nbsp;
		<span id="start" class="button_normal2"><<</span>
		<span id="prev" class="button_normal2">이전</span>
		<span class="button_normal2">Page: <span id="page_num"></span> / <span id="page_count"></span></span>
		<span id="next" class="button_normal2">다음</span>
		<span id="end" class="button_normal2">>></span>
	</div>
	<canvas id="pdfViewDiv" style="width:100%;height:910px"></canvas>
	
<jsp:include page="/WEB-INF/jsp/oslops/bottom/footer.jsp" />