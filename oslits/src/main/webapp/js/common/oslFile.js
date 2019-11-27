var rowCount=0;
var fileAppendList = [];
//Drag&Drop 활성화 버튼 클릭 시
function createStatusbar(fileVo, obj) {
	//파일 첨부시 전체 초기화 버튼 활성화
	$('#dndCancel').show();
	
	var fileSize = gfnByteCalculation(fileVo.size);
	/*
	$('#dragandrophandler').append(
			'<div class="file_frame_box">'
				+'<div class="file_main_box">'
					+'<div class="file_contents">'
						+gfnFileExtImages(fileVo.name.substr(fileVo.name.lastIndexOf('.') ))
						+gfnCheckStrLength(fileVo.name, 13)
						+"(" + fileSize+ ")"
					+'</div>'
					+'<div class="file_btn file_delete" data-file-data="'+fileVo.name+':'+fileVo.size+'" onclick="delElementNoServerData()">'
						+'삭제'
					+'</div>'
				+'</div>'
			+'</div>'
			);
*/
    $('<div>',{class:'file_frame_box'})
    	.append($('<div>',{class:'file_main_box'})
    			.append($('<div>',{class:'file_contents'})
    					.append(gfnFileExtImages(fileVo.name.substr(fileVo.name.lastIndexOf('.')+1)))
    					.append(gfnCheckStrLength(fileVo.name, 30))
    					.append("(" + fileSize+ ")"))
    			.append($('<div>',{class:"file_btn file_delete", "data-file-data" : fileVo.name+":"+fileVo.size,  click: delElementNoServerData, text:'삭제'}))
    	)
    	//.appendTo($('#dragandrophandler'))
    	.appendTo(obj)

    this.setFileNameSize = function(name,size)
    {
      //스크롤 최하단으로 내리기
		$(".pop_file").scrollTop($(".pop_file")[0].scrollHeight);
    }

    this.setAbort = function(jqxhr)
    {
        var sb = this.statusbar;
        this.abort.click(function()
        {
            jqxhr.abort();
            sb.hide();
        });
    }
}

function handleFileUpload(files,obj)
{
	var ext ;
	for (var i = 0; i < files.length; i++) {
	   // 파일 확장자 구하기.
	   ext =files[i].name.split(".").pop().toLowerCase();
	   
	   // 화이트 리스트가 아니라면 중지 업로드 중지.
	/*   if(!gfnFileCheck(ext)){
		   toast.push("확장자가 [ " +ext + " ] 인 파일은 첨부가 불가능 합니다.");
		   return false;
	   }
	   if(fileChk.getObj(files[i].name+":"+files[i].size).index != null){
			toast.push("중복된 첨부파일은 제외 됩니다.<br>"+files[i].name);
	   }else if(files[i].size <= 0){
		   toast.push("크기가 0 Byte인 파일은 업로드가 불가능 합니다.<br>"+files[i].name);
	   }else{*/
		   fileChk.push(files[i].name+":"+files[i].size);
		   // var fd = new FormData();
		   
		   /*fd.append('file', files[i]);*/
		   fileAppendList.push(files[i]);
		   var status = new createStatusbar(files[i], obj); //Using this we can set progress.
		   status.setFileNameSize(files[i].name,files[i].size);
		   // sendFileToServer(fd,status);
		//}
   }
}

$(document).ready(function()
{
	var objTemp = obj;
	if(objTemp == null){
		var obj = $("#dragandrophandler");
		
	}
	fnDragAndDropEventSet(obj);
});


/**
 * 파일첨부에 대한 Drag & Drop 이벤트를 세팅한다.
 * req4105의 추가항목의 경우 {auth:"opt", obj:$(".opt_drop_file"), rtnFunc:function 명} 으로
 * fnDragAndDropEventSet 에 전달
 * @param objList 이벤트를 세팅할 object 목록
 */
function fnDragAndDropEventSet(objList){

	// objList에 이벤트를 세팅한다.
	$.each(objList, function(idx, map){

		var dragAndDropObj = map;
		
		// objList의 map에 obj가 있을경우
		if(!gfnIsNull(map.obj)){
			// dragAndDropObj로 세팅
			dragAndDropObj = map.obj;
		}
		
		var dragging = 0;
		$(dragAndDropObj).on('dragenter', function (e)  
		{
			dragging++;
		    e.stopPropagation();
		    e.preventDefault();
		    $(this).css('border', '1px solid #4b73eb');
		});
		$(dragAndDropObj).on('dragover', function (e)   
		{
		     e.stopPropagation();
		     e.preventDefault();
		});
		$(dragAndDropObj).on('dragleave ', function (e)   
		{
			dragging--;
		    if (dragging === 0) {
		    	$(this).css('border', '1px solid #fff');
		    }
		});
		$(dragAndDropObj).on('drop', function (e) 
		{
			$(this).css('border', '1px dotted #4b73eb');
		    e.preventDefault();
		    var files = e.originalEvent.dataTransfer.files;

		    // 권한이 없을경우 handleFileUpload 실행
		    if(gfnIsNull(map.auth)){
		    	// We need to send dropped files to Server
		    	handleFileUpload(files,$(this));
		    }
		    // 지정한 권한이 있을경우 returnFunction 호출
		    else if( !gfnIsNull(map.auth) && map.auth == "opt" ){
		    	// atchFileId를 가져온다.
		    	var atchFileId = $(this).attr("fileid");
		    	// 전달받은 returnFunction 호출
		    	var returnFunction = map.rtnFunc;
		    	returnFunction(files, atchFileId);
		    }
		});
	});
}

/* Drag&Drop으로 첨부한 파일 목록 초기화*/
function dndCancel(YN){
	// 추가된 파일 갯수 만큼 루프 (데이터베이스에 저장되어있는 파일들의 목록을 지움)
	if(YN!="Y"){
		$(".pop_file .file_frame_box .file_contents").each(function(i,v){
			gfnFileDelete(this);
		});
	}
	$(".pop_file").find('.file_frame_box').remove();
	//FormData 초기화
	fd = new FormData();
	//array 초기화
	fileChk.clear();
	fileAppendList.clear();
	
	$('#dndCancel').hide();
}

//삭제 시 로직
function delElementNoServerData(e){
	e.stopPropagation();
	$(this).closest('.file_frame_box').remove();
	var delIdx = fileChk.getObj($(this).data('fileData')).index;
	
	/*
	var temp = new FormData();
	$(fd.getAll('file')).each(function(i, v) {	
		 if(i != delIdx){
			 temp.append('file',this);
		 }
	});*/
	/*
	fd = new FormData();
	
	$(temp.getAll('file')).each(function(i, v) {
		fd.append('file',this);
	});
	*/
	fileAppendList.splice(delIdx,1);
	fileChk.splice(delIdx,1);
	if(fileChk.length <= 0) {
		$('#dndCancel').hide();
	}
}



//업로드 목록 구하기
function fnFileUploadStrData(){
	//파일 목록
	var fileUploadList = "";
	
	if(!gfnIsNull(fileAppendList)){
		fileUploadList += "</br></br>[업로드 파일 목록]<div id='popup_fileList'>";
		//파일크기 총 합
		var sumFileSize = 0;
		
		//실제 업로드되는 파일 목록 구하기
		$.each(fileAppendList,function(idx, map){
			// 파일 확장자 추출( 소문자 )
			var ext = map.name.split(".").pop().toLowerCase();
			var fileName = gfnCutStrLen(map.name,45);
			
			if(map.size > FILE_INFO_MAX_SIZE){
				var fileInfoMaxSizeStr = gfnByteCalculation(FILE_INFO_MAX_SIZE);
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> ('+fileInfoMaxSizeStr+' 용량 초과)</br>';
			}else if(sumFileSize > FILE_SUM_MAX_SIZE){
				var fileSumMaxSizeStr = gfnByteCalculation(FILE_SUM_MAX_SIZE);
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> ('+fileSumMaxSizeStr+' 전체 용량 초과)</br>';
			}else if(!gfnFileCheck(ext)){
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> ([ ' +ext + ' ] 확장자 불가)</br>';
			}else if(fileChk.getObj(map.name+":"+map.size).index != idx){
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> (중복 파일)</br>';
			}else if(map.size <= 0){
				fileUploadList += '<i class="fa fa-file"></i>&nbsp;<s>'+fileName+'</s> (0 Byte인 파일)</br>';
			}else{
				fileUploadList += '<i class="far fa-file"></i>&nbsp;'+gfnCutStrLen(map.name,90)+'</br>';
				sumFileSize += map.size;
				fileUploadChk = true;
				
			}
		});
		fileUploadList += "</div>";
	}
	
	return fileUploadList;
}

//실제 FormData적재
//paramName : FormData에 적재되는 파라미터 이름
function fnFileUploadAppendData(paramName){
	//파일 목록
	if(!gfnIsNull(fileAppendList)){
		//파일크기 총 합
		var sumFileSize = 0;
		
		//실제 업로드되는 파일 목록 구하기
		$.each(fileAppendList,function(idx, map){
			// 파일 확장자 추출( 소문자 )
			var ext = map.name.split(".").pop().toLowerCase();
			
			if(map.size > FILE_INFO_MAX_SIZE){
				return true;
			}else if(sumFileSize > FILE_SUM_MAX_SIZE){
				return true;
			}else if(!gfnFileCheck(ext)){
				return true;
			}else if(fileChk.getObj(map.name+":"+map.size).index != idx){
				return true;
			}else if(map.size <= 0){
				return true;
			}else{
				fd.append(paramName,map);
			}
		});
	}
}



