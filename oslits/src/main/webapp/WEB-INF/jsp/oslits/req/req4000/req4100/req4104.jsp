<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<html lang="ko">
<title>SW 형상관리시범체계</title>
<head>
	<script type="text/javascript" src="<c:url value='/js/diff/diff_match_patch_uncompressed.js'/>"></script>
	<link rel='stylesheet' href='<c:url value='/css/ztree/zTreeStyle/zTreeStyle.css'/>' type='text/css'>
	<script type="text/javascript" src="/js/ztree/jquery.ztree.all.min.js"></script>
</head>
<style>
.layer_popup_box {font-size: 0.9em; }
.popup_title_box { width: 100%; height: 44px; padding: 15px; font-size: 1.05em; font-weight: bold; color: #fff; background-color: #4b73eb;}
/* .top_box { width: 100%; height: 322px; border-bottom: 1px solid #ccc;    padding: 3px 0; } 2018.10.02 김정환 */
.top_box { width: 100%; height: 279px; padding: 3px 0; }
/* .middle_box{width: 100%;height:410px;border-bottom:1px solid #ccc;} 2018.10.02 김정환 */
.middle_box{width: 100%;height:409px;border-bottom:1px solid #ccc;}
.btn_box { width: 100%; height: 60px;padding: 5px 0;margin-bottom: 10px;float: left;text-align: center;}
.pop_close {width: 120px;height: 40px;line-height: 36px;text-align: center;font-weight: bold;display: inline-block;border-radius: 5px;box-shadow: 1px 1px 1px #ccd4eb;border: 1px solid #b8b8b8;cursor: pointer;margin-right:20px;background-color: #fff;color: #000;}
/* 거부사유 */
.newReqRejectCmnt{display: none;}
#accptRejctTxt{overflow: hidden;max-height: 90px; white-space: pre-wrap; width: 68%;}

/* div css 설계 */
/* .req_top_div {width: 417.66px;margin: 0 5px;float: left;height: 315px;border: 1px solid #ccc;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);border-radius: 5px;} 2018.10.02 김정환*/
.req_top_div {
    width: 417.66px;
    margin: 0 5px;
    float: left;
    height: 270px;
    border: 1px solid #ccc;
    box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
    border-radius: 5px;
}
.req_sub_title{ font-weight: bold; background: #f9f9f9; border-bottom: 1px solid #ccc; text-align: center;padding: 5px 0;height: 25px;border-radius: 5px 5px 0 0;}
.req_sub_left_title{float: left;margin-left: 1px;line-height: 12px;}
.req_sub_left_title > span{font-size: 0.8em;border: 1px solid #ccc;background-color: #fff;padding: 2px;font-weight: normal;}
.req_sub_left_title > span:hover{background-color:#0f73d0;color:#fff;cursor:pointer;}
.req_sub_middle_title{float: left;text-align: center;width: 200px;}
.req_sub_right_title{margin-right: 1px;line-height: 12px;float: right;}
.req_sub_right_title > span{font-size: 0.8em;border: 1px solid #ccc;background-color: #fff;padding: 2px;font-weight: normal;}
.req_sub_right_title > span:hover{background-color:#0f73d0;color:#fff;cursor:pointer;}
.req_sub_contents{height: calc(100% - 25px);height: -webkit-calc(100% - 25px);padding: 5px;font-size: 10pt;overflow-y:auto;}
/* .req_sub_desc{height:150px;border-bottom: 1px solid #ccc;} 2018.10.02 김정환 */
.req_sub_desc{height:130px;border-bottom: 1px solid #ccc;}

/* #reqDesc{height: 140px;margin: 5px 5px;width: calc(100% - 10px);width: -webkit-calc(100% - 10px);resize: none;border:none;} 2018.10.02 김정환*/
#reqDesc {
    height: 124px !important;
    margin: 5px 5px;
    width: calc(100% - 10px);
    width: -webkit-calc(100% - 10px);
    resize: none;
    border: none;
}

/* .req_sub_fileList{height:calc(100% - 200px);height:-webkit-calc(100% - 200px);overflow: auto;} */
.req_sub_fileList{height:calc(100% - 200px);height:-webkit-calc(100% - 180px);overflow: auto;}
.cmntInsert{height: 52px;resize: none;width: calc(100% - 58px);width: -webkit-calc(100% - 55px);float: left;margin: 0 3px;}
.req_sub_inCmt{height: 40px;padding: 5px 0;}
.req_sub_cmtList{height: calc(100% - 85px);overflow: auto;border-bottom: 1px solid #ccc;}


.sub_line {width: 100%;float: left;display: inline-block;}
.req_sub_left {width: 130px;height: 100%;line-height: 25px;float: left;padding-left: 5px;font-weight: bold;}
.req_sub_right {width: 256px;float: left;line-height: 25px;padding-left: 10px;}
.req_sub_right#reqUsrDept{line-height: 17px;}

.m_t_title{font-weight: bold;margin-bottom: 5px;}
.slideChg_box{width: 100%;height: 100%;overflow-x: scroll; white-space: nowrap;}
/* .slide_box_title{border-bottom: 1px solid #ccc;text-align: center;padding: 5px 0;background-color: #4b73eb;color: #fff;} */
.slide_box_title {
    border-bottom: 1px solid #ccc;
    text-align: center;
    padding: 5px 0;
    background-color: #f9f9f9;
    color: #000;
    font-weight: bold;
}
.process_box {margin: 5px;height: calc(100% - 10px);height: -webkit-calc(100% - 10px);min-width: 190px;outline: 1px solid #ccc;display: inline-block;box-shadow: 0 1px 4px rgba(0,0,0,0.12), 0 1px 3px rgba(0,0,0,0.24);}
.process_box ul, .process_arrow_box ul{height:100%;}

.process_top_box{height: calc((100%/2) - 10px);height: -webkit-calc((100%/2) - 10px);border-bottom:2px solid #0f73d0;}
.process_bottom_box{height: calc((100%/2) - 10px);height: -webkit-calc((100%/2) - 10px);border-top:2px solid #0f73d0;}
.process_top_title{height: 20px;line-height: 20px;border-bottom: 1px solid #ccc;text-align: center;background-color: #f9f9f9;}

.process_arrow_box{width: 44px;display: inline-block;padding-top: 50px;height: 170px;float: left;}
.process_arrow_box > div{float:left;}
.req_main_box{height: 170px;width: 150px;display: inline-block;float: left;}
.req_top{margin: 8px 5px 0 5px;}
.req_bottom{margin: 0 5px 8px 5px;}
.totalTm{background-color:black !important;color:#fff !important;}

/* 상단 요구사항 div */
.req_top > .req_top_box {height: 150px;border: 1px solid #a4a7bb;border-radius: 5px 5px 0 0;padding: 4px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);font-size: 0.85em;}
.req_top > .req_bottom_box{border-left: 1px dotted #a4a7bb;height: 20px;padding-left: 5px;font-size: 0.8em;line-height: 20px;}
/* 작업흐름 변경내역에 사용자 이름 클릭 시 쪽지 기능 제거로인해 해당 CSS 주석처리 */
/* .historyUsrDiv:hover{background-color:#323a47;color:#fff;cursor:pointer;} */
/* 하단 요구사항 div */
.req_bottom > .req_top_box{border-left: 1px dotted #a4a7bb;height: 20px;padding-left: 5px;font-size: 0.8em;line-height: 20px;}
.req_bottom > .req_bottom_box{height: 150px;border: 1px solid #a4a7bb;border-radius: 0 0 5px 5px;padding: 5px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);font-size: 0.85em;}

.reqFrameBox {display: inline-block;}
/* 파일 리스트 */
#fileListDiv{height:100%;overflow-y: auto;border:1px solid #fff;}
/* #fileListDiv:hover{border:1px solid #4b73eb;}
#fileListDiv.dragOn{border:1px solid #0B20A1;} */

/* 요구사항 상자 style */
.req_box_title{padding-bottom: 4px;margin-bottom: 1px;text-align: center;}
.req_box_title.signDiv{height: 20px;line-height: 18px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);}
.req_box_main{height: 20px;line-height: 15px;text-align: center;padding: 2px 0;position: relative;}
.req_box_main.signRejectDiv{height: 80px;text-align: left;white-space: normal;width: 138px;resize:none;overflow-y: auto;font-size: 10pt;}
.req_box_main.signRejectDiv:hover {cursor: pointer;border: 1px solid #4b73eb;}
.req_box_main.signRejectUsrDiv{height: 30px;line-height: 25px;border: 1px solid #ccc;border-radius: 5px;}
.req_box_main.prev{box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);color:#fff;margin-top: 22px;border-radius: 0 0 5px 5px;}
.req_box_main.chg{ box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);margin-bottom: 2px;color:#fff;margin-top: 15px;border-radius: 0 0 5px 5px;}
.req_box_main.arrow{height: 25px;padding: 0;color:#424c56;    font-size: 7pt;}
.req_box_main.regUsrImg {width: 100%;height: 54px;display: block;text-align: left;clear: both;}
.req_box_main.regUsrImg > img {width: 50px;height: 50px;float: left;}
.req_box_main.regUsrImg > div {float: left;height: 25px;width: calc(100% - 50px);text-align: center;line-height: 25px;}
.req_box_main.signAcceptDiv {height: calc(100% - 20px);padding-top: 10px;}
.req_box_main.signAcceptDiv > div {height: 30px;line-height: 25px;border: 1px solid #ccc;margin-top: 10px;border-radius: 5px;}
.req_box_main.signAcceptDiv > img {width: 60px;height: 60px;}
.req_box_main.bottom:nth-child(1){margin-top:5px;}
.req_box_main.bottom{border: 1px solid #ccc;margin-bottom: 1px;}

.flowGradient{width:100%;}

.uploadOverFlow{font-size:0.8em;}
.subRCD:nth-child(1){text-overflow: ellipsis; white-space: pre-line; line-height:13px; }

/* 하단 탭 style*/
.footerTab_child {float: left;min-width:160px;height: 26px;border: 1px solid #ccc;padding: 6px 5px 2px 5px;cursor: pointer;background: #f9f9f9;border-radius: 0 0 5px 5px;border-top: none;text-align:center;}
.footerTab_child:hover, .footerTab_child.footerTab_active {background-color: #4b73eb;color: #fff;}
.content_child{display:none;}
/* .content_child.footerTab_active{display:block;} 2018.10.02 김정환 */
.content_child.footerTab_active {
    display: block;
    margin-top: 7px;
    border-top: 1px solid #ccc;
}

.footerTab_content{height:435px;}
#clipBoardContents{overflow-y:auto;}

/* 클립보드 판 */
/* .pasteContent_main{display: inline-block;width:99%;margin-bottom: 5px;} */
/* .pasteContent_top{float: left;min-width: 400px;min-height: 75px;border: 1px solid #0f73d0;border-radius: 10px 0 0 10px;padding: 5px;margin: 2px 0 2px 2px;max-width: 88%;box-shadow: 0 1px 0 rgba(0,0,0,0.12), 0 1px 0 rgba(0,0,0,0.24);} */
/* .pasteContent_bottom{float: left;width: 150px;text-align: center;border: 1px solid #0f73d0;border-radius: 0 10px 10px 0;border-left: 0;height: 75px;margin-top: 2px;box-shadow: 0 1px 0 rgba(0,0,0,0.12), 0 1px 0 rgba(0,0,0,0.24);} */
/* .cb_bottom_usrNm{border-bottom: 1px solid #0f73d0;padding: 2px 0;height: 25px;} */
/* .cb_bottom_regDtm{border-bottom: 1px solid #0f73d0;height: 25px;padding: 2px 0;} */

.pasteContent_main{display: inline-block;width:99%;margin: 5px 0 0 5px;}
.pasteContent_top{float: left;min-width: 400px;min-height: 75px;border: 1px solid #a9a9a9;border-radius: 10px 0 0 10px;padding: 5px;margin: 2px 0 2px 2px;max-width: 88%;box-shadow: 0 1px 0 rgba(0,0,0,0.12), 0 1px 0 rgba(0,0,0,0.24);}
.pasteContent_bottom{float: left;width: 150px;text-align: center;border: 1px solid #a9a9a9;border-radius: 0 10px 10px 0;border-left: 0;height: 75px;margin-top: 2px;box-shadow: 0 1px 0 rgba(0,0,0,0.12), 0 1px 0 rgba(0,0,0,0.24);}

.cb_bottom_usrNm{border-bottom: 1px solid #a9a9a9;padding: 2px 0;height: 25px;}
.cb_bottom_regDtm{border-bottom: 1px solid #a9a9a9;height: 25px;padding: 2px 0;}
.cb_bottom_addition{height: 25px;padding: 2px 0;border-radius: 0 0 10px 0;cursor:pointer;}
.cb_bottom_addition:hover {background-color: #4b73eb;color: #fff;}
#clipBoard_help {visibility: hidden;position: absolute;bottom: 0px;width: 20%;text-align: left;padding: 5px 5px 5px 15px;font-size: 1.2em;line-height: 53px;opacity: 0.5;}
#clipBoard_help .addComment {border: 2px solid;border-radius: 9px;padding: 3px;opacity: 0.5;font-weight: bold;}
.cbImg{width: 100%;}

/* 메모장 입력 */
/* .notepad_input {display:none;position: absolute;width: 1300px;height: 460px;top: 370px;background: #fff;box-shadow: 0 1px 3px rgb(15, 115, 208), 0 1px 2px rgb(15, 115, 208);} */
.notepad_input {display:none;position: absolute;width: 1300px;height: 460px;top: 331px;background: #fff;}
/* .notepad_title {width: 100%;background-color: #4b73eb;color: #fff;height: 25px;text-align: center;line-height: 22px;} */
.notepad_title {width: 100%; background-color: #f9f9f9; color: #000; height: 25px; text-align: center;line-height: 22px;font-weight: bold;border-bottom: 1px solid #ccc;}
.notepad_contents {height: 405px;overflow-y: scroll;padding: 10px;}
/* .notepad_footer {height: 30px;background-color: #4b73eb;color: #fff;line-height: 25px;text-align: center;} */
.notepad_footer {height: 30px;background-color: #f9f9f9;color: #fff;line-height: 25px;text-align: center; border-top: 1px solid #ccc; border-bottom: 1px solid #ccc;}

.notepad_footer_userNm {float: left;width: 600px;text-align: center;}
/* .notepad_footer_btn {display: inline-block;margin: 2px;} */
.notepad_footer_btn {display: inline-block;}

.notepad_img_resize_main {display:none;background:#fff;position: absolute;top: 60%;left: 45%;width: 80px;border: 2px solid #0f73d0;border-top: 10px solid #0f73d0;padding: 5px;z-index: 1;}
.notepad_img_resize_div{text-align: center;margin-bottom: 5px;}
.notepad_contents_imgDiv{max-width: 100%;overflow: hidden;}
/* .notepad_update {z-index: 90501;display:none;position: absolute;width: 1000px;height: 780px;top: 100px;left: 160px;background: #fff;box-shadow: 0 1px 3px rgb(15, 115, 208), 0 1px 2px rgb(15, 115, 208);} */
.notepad_update {z-index: 90501;display:none;position: absolute;width: 1000px;height: 717px;top: 100px;left: 160px;background: #fff;box-shadow: 0 1px 3px rgb(52, 52, 52), 0 1px 2px rgb(189, 193, 196);}
/* .notepad_detailInfo {height: 100px;border-bottom: 1px solid #0f73d0;} */
.notepad_detailInfo {height: 112px;border-bottom: 1px solid #5a5a5a;}
.notepad_detailInfo > div {float: left;padding: 5px;height: 100%;}
.notepad_detailInfo_left {width: 700px;}

.detail_btn{display:block;margin-bottom:3px !important;width:150px;}
#notepad_img_download{display:none;}

/* 수정이력 style */
.reqHistory > .middle_box {padding-right: 15px;overflow-y: auto;}
.chgDetailMainDiv {width: 670px;min-height: 80px;height: auto;border: 1px solid #ccc;margin: 5px 0;border-radius: 6px;display: block;float: right;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);background: #f9f9f9;}
.chgDetailLeftDiv {float: left;display: inline-block;width: 100px;text-align: center;padding: 10px 0;}
.chgDetailLeftDiv > img {width:60px;height:60px;}
.chgDetailRightDiv {float: left;display: inline-block;width: 568px;min-height: 80px;padding: 5px;}
.chgDetailRightTopDiv {width: 100%;height: 25px;line-height: 25px;}
.chgDetailRightBottomDiv {display: block;min-height: 50px;width: 100%;padding: 5px 0;line-height: 20px;}
.chgDetailRightBottomDiv > span {display: block;font-size: 10pt;border-bottom: 1px solid #f9f9f9;}
.chgDetailRightBottomDiv > span > b {font-weight: bold;}
.chgDetailRightBottomDiv > span.chgDetailDiff:hover {border-bottom: 1px solid #323a47;cursor: pointer;}
.chgDetailRightBottomDiv > span.chgDetailDiff:active {color: #bbbdbf;}
.chgDetailRightBottomDiv > span.chgDetailDiff::before{font-family: "Font Awesome 5 Free";content:"\f044\00a0"}
.chgDetailRightBottomDiv > span.chgDetailFileDel::before{font-family: "Font Awesome 5 Free";content:"\f2ed\00a0"}
.chgDetailUsrDiv {float: left;width: 50%;display: inline-block;font-weight: bold;}
.chgDetailUsrDiv:hover {border-bottom: 1px solid #323a47;cursor: pointer;}
.chgDetailUsrDiv:hover:after {font-family: "Font Awesome 5 Free";content: "\00a0\00a0\00a0\f0e0";}
.chgDetailAgoDiv {float: right;width: 50%;text-align: right;display: inline-block;padding-right: 20px;color: #424242;font-size: 8pt;}
.chgDetailUsrDiv > small, .chgDetailAgoDiv > small {color: #828282;font-size: 0.9em;font-weight: normal;}

/* diff style */
/* .diffDiv {position: absolute;left: 10px;width: 44%;top: 396px;height: 395px;border: 1px solid #ccc;border-radius: 6px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);} 2018.10.02 김정환*/
.diffDiv {position: absolute;left: 10px;width: 44%;top: 361px; height: 395px;border: 1px solid #ccc;border-radius: 6px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);}
.diffOptInfo {display: block;height: 30px;line-height: 30px;text-align: center;border-bottom: 1px solid #ccc;font-weight: bold;background: #f9f9f9;border-radius: 6px;}
.diffOldDiv, .diffNewDiv {width: 50%;float: left;display: inline-block;height: 365px;}
.diffTitleDiv {text-align: center;height: 30px;line-height: 30px;border-bottom: 1px solid #ccc;background: #f9f9f9;}
/* .diffTitleDiv.diffAdd{background-color: #ff9a8f;}
.diffTitleDiv.diffDel{background-color: #5dff71;} */
div.diffContentDiv {height: 335px;padding: 5px;line-height: 22px;}
span.text-add {background-color: #5dff71;}
span.text-del {background-color: #ff9a8f;}
.diffOldDiv{border-right: 1px solid #ccc;}
.flowOptionDiv > .fa {font-size: 10pt;margin:0 2px;}
.flowOptionDiv{position: absolute;top: -21px;height: 21px;left: -1px;background-color: inherit;padding: 2px;border-radius: 5px 5px 0 0;border: 1px solid transparent;border-bottom: 1px dashed #fff;}
.flowOptionDiv.hideOptDiv {width: 140px;left: 0;overflow: hidden;}
/* 작업 */

#req4105_work{display:none;border-right: none;}
.req4105_work_btnBox {height: 45px;line-height: 45px;border: 1px solid #ccc;border-bottom: none;border-top: none;background-color: #f9f9f9;font-weight: bold;padding-left: 10px;}
.req4105_work_frame {width: 100%;height: 210px;padding: 5px;border: 1px solid #ccc;border-bottom: none;}
div.req4105_work_btn {float: right;margin: 5px 10px;border: 1px solid #b8b8b8;width: 100px;height: 35px;line-height: 35px;text-align: center;box-shadow: 1px 1px 1px #ccd4eb;background-color: #fff;border-radius: 5px;cursor:pointer;}
.req4105_frameTitleDiv {width: 100%;height: 38px;background-color: #f9f9f9;border: 1px solid #ccc;border-radius: 5px 5px 0 0;border-bottom: none;padding-left: 10px;line-height: 40px;font-weight: bold;float: left;display: block;margin-top:10px;}
.req4105_option_title {width: 25%;float: left;height: 45px;padding-left: 10px;line-height: 40px;background-color: #f9f9f9;border: 1px solid #ccc;font-weight: bold;border-top: none;}
.req4105_option_all {float: left;height: 45px;line-height: 30px;width: 75%;border-bottom: 1px solid #ccc;padding: 5px;}
.req4105_option_half {float: left;height: 45px;line-height: 30px;width: 25%;border-bottom: 1px solid #ccc;padding:5px;}
.req4105_optionDiv {width: 100%;display: block;float: left;border-right: 1px solid #ccc;}

/* 추가 항목 */
input.req4105_input_text {min-width: 190px;height: 100%;border: 1px solid #ccc;display: block;padding-left: 15px;border-radius: 1px;}
textarea.req4105_textarea {width: 100%;height: 100%;resize: none;padding: 5px;border: 1px solid #ccc;border-radius: 1px;}
input.req4105_input_date {width: 195px;float: left;display: block;border-radius: 1px;height: 100% !important;background-color: #fff !important;border: 1px solid #ccc;text-align: center;}
input.req4105_charger {width: 195px !important;min-width: 100px;display: inline-block;float: left;margin-right: 5px;}
span.req4105_charger{height: 34px;line-height: 30px;width:30px;}
input.req4105_input_check {width: 100%;height: 80%;}
textarea.req4105_processBox_bottom[disabled] {background-color: #fff;white-space:normal;}
select.req4105_select {width: 100%;height: 100%;border-radius: 1px;border: 1px solid #ccc;text-align: center;}
input.req4105_input_text.req4105_readonly, textarea.req4105_textarea.req4105_readonly, select.req4105_select.req4105_readonly
,input.req4105_input_date[disabled]{cursor: default;background-color:#eee !important;}
img.ui-datepicker-trigger{float: left;margin-top: 2px;}
span.req4105_charger{height: 34px;line-height: 30px;}
.req4105_option_half input.req4105_input_text
, .req4105_option_half textarea.req4105_textarea
, .req4105_option_half select.req4105_select {width:100%;height:100%;}
.req4105_option_all input.req4105_input_text, .req4105_option_all textarea.req4105_textarea, .req4105_option_all select.req4105_select{width: 100% !important;height:100% !important;}
.req4105_desc {height: 100px;}
.req4105_file {height: 150px;}
.req4105_clear{clear:both;}
.req4105_top_line{border-top:1px solid #ccc;}
.req4105_right_line{border-right:1px solid #ccc;}
input.req4105_input_date[disabled] {width: 100%;}
input.req4105_charger[readonly] {width: 100% !important;}
.req4105_clear{clear:both;}
.reqAddOptFrame {width: 100%;height: 100%;overflow-x:none;overflow-y: auto;white-space: nowrap;padding: 10px;}
div#reqAddOptList {border: 1px solid #ccc;display: inline-block;width: 100%;border-left: none;border-bottom: none;font-size:10pt;}
div#req4105_reqTopDataList{height: 120px;border: 1px solid #ccc;overflow-x: auto;}

/* 배포계획 */
.req4105_dplBox {float:left;width: 100%;border-right: 1px solid #ccc;display: none;}
.req4105_dplBox input.req4105_deploy {width: 244px !important;}

/* 작업 목록 */
.req4104_work_frame {padding: 10px;}


/* div css 설계 */
.req_top_div_fold1_line{
    width: 417.66px;
    margin: 0 5px;
    float: left;
    height: 227px;
}

/* .req_top_div_fold1{width: 417.66px;margin: 0 5px;float: left;height: 315px;} 2018.10.02 김정환*/
.req_top_div_fold1{
    width: 417.66px;
    margin: 0 5px;
    float: left;
    height: 227px;
    border: 1px solid #ccc;
    box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
    border-radius: 5px;
}

/* .req_sub_fold_contents{height: calc(100% - 25px);height: -webkit-calc(100% - 25px);padding: 5px;font-size: 10pt; background-color: #FFFFFF;border:1px solid #ccc;} 2018.10.02 김정환*/

.fold_button{font-size:1.5em;width:100%;height: 40px;border:1px solid #ccc;background-color:#fff;padding:2px;font-weight:normal;text-align: center;}
.req_sub_fold_contents {
    height: calc(100% - 25px);
    height: -webkit-calc(100% - 25px);
    /* padding: 5px; */
    font-size: 10pt;
    background-color: #FFFFFF;
   	display: flow-root;
   	overflow-y : hidden;
}

/* 프로세스 또는 이전 작업흐름 이력 목록 */
.req4105_processBox {width: 150px;display: inline-block;margin: 10px;margin-bottom: 0;border: 1px solid #ccc;border-radius: 5px;box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);height: 100px;padding: 5px;cursor: pointer;}
.req4105_processBox_top {width: 100%;height: 20px;line-height: 20px;text-align: center;font-weight: bold;border-bottom: 1px solid #ccc;font-size: 10pt;}
.req4105_processBox_bottom {height: 70px;padding: 2px 5px;font-size: 9pt;width: 138px;resize: none;border: none;overflow: hidden;cursor: pointer;}
.req4105_processBox:hover, .req4105_processBox.active {border: 1px solid #ff5643;}

/* 우측 작업흐름 목록 */
.req4105_flowBox {position: relative;width: 185px;height: 65px;text-align: center;border-radius: 0 0 10px 10px;border: 3px solid transparent;margin: 30px 10px 0 10px;display:inline-block;background-color: #F0F0F0;color: #000000;}
.req4105_flowOptionDiv {position: absolute;top: -24px;height: 24px;left: -3px;background-color: inherit;padding: 2px;border: 3px solid transparent;border-radius: 5px 5px 0 0;font-size: 10pt;border-bottom: 1px dashed #fff;width: 185px;text-align: left;}
.req4105_flowBox_title {width: 100%;height: 30px;line-height: 20px;font-weight: bold; padding-top: 5px;border-bottom: none;float:left;}
.req4105_flowBox_contents {width: 100%;height: 30px;border-radius: 0 0 8px 8px;line-height: 33px;background-color:#fff;color:#000;float:left;}
.req4105_flowBox.topFlowBox:hover, .req4105_flowBox.topFlowBox.flowActive{cursor:pointer;border: 3px solid #4b73eb;}
.req4105_flowBox.topFlowBox:hover .req4105_flowOptionDiv, .req4105_flowBox.topFlowBox.flowActive .req4105_flowOptionDiv{cursor:pointer;border: 3px solid #4b73eb;border-bottom: 1px dashed #fff;}


.req4105_flowFrameBox {position: relative;text-align: center;height: 130px;margin-bottom: 1px;border: 5px solid #f0efff;padding-top: 15px;}
/* .req4105_flowFrameBox:hover{cursor:pointer;} */
.req4105_flowFrameBox.active{display:block;}
.req4105_flowTopArrowBox {position: absolute;top: 0px;left: 100px;background: url(/images/right_arrow2.png);background-size: 44px 44px;width: 44px;height: 35px;transform: rotate(90deg);}
.req4105_flowBox_contents > span {font-size: 9pt;}
.req4105_flowOptionDiv > li {margin: 0 2px;}
.req4105_flow_topArrowBox {background: url(/images/right_arrow2.png);background-size: 44px 44px;display: inline-block;width: 44px;height: 44px;margin: 40px 10px 0 10px;background-repeat: no-repeat;}

/* 결재 */
.req4105_signFrame{display: inline-block;}
.req4105_flow_topArrowBox.signArrow{height:100px;}
.req4105_sign_TitleBox {
    height: 45px;
    line-height: 45px;
    border: 1px solid #ccc;
    border-bottom: none;
    border-top: none;
    background-color: #f9f9f9;
    font-weight: bold;
    padding-left: 10px;
    float: left;
    width: 100%;
}
.req4105_signHistoryDiv {width: 100%;height: 200px;border: 1px solid #ccc;overflow-y: hidden;overflow-x: auto;padding:0 10px;white-space:nowrap;position: relative;}
/* .req_fold_title_box {
    width: 100%;
    height: 44px;
    line-height: 44px;
    padding-left: 15px;
    font-size: 1.05em;
    font-weight: bold;
    color: #fff;
    background-color: #FFFFFF;
    border-radius: 0 0 10px 10px ;
    float: left;
    display: block;
    padding: 5px;
    border:1px solid #ccc;
    text-align : center;
} 
2018.10.02 김정환*/

.req_fold_title_box {
    width: 417.66px;
    height: 44px;
    line-height: 44px;
    padding-left: 15px;
    font-size: 1.05em;
    font-weight: bold;
    color: #fff;
    background-color: #FFFFFF;
    border-radius: 0 0 5px 5px;
    margin-left: -1px;
    float: left;
    display: block;
    padding: 5px;
    border: 1px solid #ccc;
    text-align: center;
    box-shadow: 0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24);
}



/* 프로세스 폴딩 버튼 */
.reqFoldingBtn {
    height: 32px;
    line-height: 30px;
    display : inline-block;
    width: 45px;
    border: 2px solid #fff;
    background-color: #FFFFFF;
    padding: 0 10px;
    cursor: pointer;
    /* margin: 5px 10px 2px 2px; 2018.10.02 김정환 */
    margin: 0px 10px 2px 2px;
    border-radius: 3px;
}
.reqFoldingBtn:hover{background-color: #FFFFFF;}
.reqFoldingCursor {font-family: "Font Awesome 5 Free";font-size: 2em;}
/* .reqFoldingCursor.down::before{content: "\f107";color: #000000;} 2018.10.02 김정환 */
.reqFoldingCursor.down::before{content: "\f107";color: #b9b7b7;}
/* .reqFoldingCursor.up::before{content: "\f106";color: #000000; } */
.reqFoldingCursor.up::before{content: "\f106";color: #b9b7b7; }
.titleFolded{border-radius:10px;margin-bottom: 5px;}

.fa-angle-down:before { content: "\f107"; color: #b9b7b7;}

.border{border: 1px solid #ccc;}
.file_contents{width:100%;}


.menu_ctrl_wrap{ float:left; width: 29%; height: 100%; margin-right: -6px; font-size: 0.875em; box-sizing:border-box;border-right: 1px solid #ccc;min-height: 600px;}
		.menu_ctrl_btn_wrap{ padding:0 10px; height:48px; line-height:42px; border-bottom:1px solid #ccc; }
			.menu_ctrl_btn_wrap .btn_menu_add{ margin-right:5px; }
			.menu_ctrl_btn_wrap .menu_all_wrap{ float:right; height:100%; }
			.menu_ctrl_btn_wrap .menu_expand_all{ display:inline-block; vertical-align:middle; margin-right:5px; width:17px; height:17px; background:url(/images/header/GNB/font_plus_btn.png) no-repeat center; border:1px solid #ccc; cursor:pointer; }
			.menu_ctrl_btn_wrap .menu_collapse_all{ display:inline-block; vertical-align:middle; width:17px; height:17px; background:url(/images/header/GNB/font_minus_btn.png) no-repeat center; border:1px solid #ccc; cursor:pointer; }
			
.req4104_svn_maskBox {display:none;position: absolute;right: 0;width: 960px;height: 370px;min-height: 370px;background-color: rgba(0, 0, 0, 0.7);z-index: 2;color: #fff;padding-top: 150px;text-align: center;line-height: 30px;font-size: 10pt;font-weight: bold;}			
.req4104_reqDep_maskBox {display:none;position: absolute;right: 0;width: 100%;height: 370px;min-height: 370px;background-color: rgba(0, 0, 0, 0.7);z-index: 2;color: #fff;padding-top: 150px;text-align: center;line-height: 30px;font-size: 10pt;font-weight: bold;}			
.content_child.svnRevision.footerTab_active {height: 100%;border-bottom: 1px solid #ccc;}
			
/* 베포 */
.req4104_reqDep_leftFrame {
    width: 705px;
    height: 100%;
    padding: 10px 20px;
    display: inline-block;
    float: left;
}
.req4104_reqDep_rightFrame {
    width: 575px;
    height: 100%;
    float: left;
    display: inline-block;
    padding: 10px 0 10px 0;
    position: relative;
}
.req4104_reqDep_jobGrid {
    width: 340px;
    float: left;
    margin-right: 20px;
    height: 100%;
    display: inline-block;
}
.req4104_reqDep_jobHistoryGrid {
    width: 215px;
    float: left;
    height: 100%;
    display: inline-block;
    position: relative;
}
.req4104_reqDep_titleBox {
    height: 30px;
    text-align: center;
    line-height: 30px;
    background-color: #f9f9f9;
    border: 1px solid #ccc;
    border-bottom: none;
    font-weight: bold;
    font-size: 10pt;
}
div#req4104_dpl_mask_frmae{
position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.7);
    z-index: 2;
    font-size: 10pt;
    color: #fff;
    font-weight: bold;
    padding-top: 150px;
    display:none;
    text-align: center;
}
</style> 
<script type="text/javascript">
	//전역변수 세팅
	var viewer = "${viewer}";

	//파일 업로드시 필요한 변수 세팅
	var atchFileId;
	var cbAtchFileId;
	var fileSn = "${fileSn}";
	var reqChgList;
	var reqInfoMap;
	var reqChgDetailList = ${reqChgDetailList};
	var reqChgDetailList;
	var reqSubList;
	var functionMenu; //확장 메뉴 변수
	
	//추가 항목 목록
	var addOptList = ${addOptList};
	
	//수정 불가능한 첨부파일 Id리스트
	var readonlyFileIdList = [];
	
	//필수 첨부파일 id리스트
	var essentialCdFileIdList = [];
	/**
	* start
	**/
	//현재 작업흐름 정보
	var flowInfo;
	
	//결재 대기중 상태 체크
	var signWaitChk = false;
	
	//선택 프로세스 Id
	var processId;
	//작업 그리드
	var add_work_grid;
	
	var reqId = '${reqId}';
	
	//현재 요구사항 담당자 Id
	var reqUsrId;
	
	var flowId ;
	/**
	* end
	**/
	//작업
	var workList = ${workList};
	var work_grid;
	//리비젼 목록
	var revisionGrid;

	var secondGrid;
	
	var zTree;
	
	//리비전
	var svnRepId;
	var revisionNum;
	
	//배포
	var reqDplList = ${reqDplList};
	
	$(document).ready(function(){
		
		// 이벤트
		$(".menu_expand_all").click(function(){
			zTree.expandAll(true);
		});
		
		// 메뉴 관리 전체 닫기
		$(".menu_collapse_all").click(function(){
			zTree.expandAll(false);
		});
		
		$('#sub_req_div_view').hide();		

		$(document).on('keydown', function(e) { 
			if(e.altKey && e.which == 49){
				$(".footerTab > .footerTab_child")[0].click();
			}else if(e.altKey && e.which == 50){
				$(".footerTab > .footerTab_child")[1].click();
			}else if(e.altKey && e.which == 51){
				$(".footerTab > .footerTab_child")[2].click();
			}else if(e.altKey && e.which == 52 && !gfnIsNull($(".footerTab > .footerTab_child")[3])){
				$(".footerTab > .footerTab_child")[3].click();
			}
			/*else if(e.altKey && e.which == 53 ){
				$(".footerTab > .footerTab_child")[4].click();
			}*/
		});
	/* -- 20181008 파일업로드 제거 //진주영 	
		//Insert권한 확인
		if(typeof btnAuthInsertYn == "undefined" || btnAuthInsertYn != 'Y' || viewer == "Y"){
			//권한이 없는 경우 업로드 버튼 제거
			$('#btn_insert_fileSelect').remove();
		}else{
			//파일 Drag&Drop 이벤트 걸기
			gfnFileDragDropUpload($('#fileListDiv'),fnFileAjaxUpload);
		}
		 */
		//데이터 세팅
		reqInfoMap 		= ${reqInfoMap};
		reqChgList 		= ${reqChgList};
		var reqCommentList 	= ${reqCommentList};
		var fileList 		= ${fileList};
		var cbContentList 	= ${cbContentList};
		
		fnReq4105DataLoad(reqInfoMap.reqId);
		
		//요구사항 변경 이력 세팅
		fnReqChgView(reqChgList,reqInfoMap);
		
		//요구사항 수정 이력 세팅
		fnReqChgDetailView(reqChgDetailList);
		
		//디테일폼 세팅
		reqInfoMap.reqDevWkTm = gfnIsNull(reqInfoMap.reqDevWkTm)?0:reqInfoMap.reqDevWkTm; //개발공수는 null로 오기때문에 명시적으로 '0' 값을 표기해줘야 해서 치환해서 세팅함
		gfnSetData2ParentObj(reqInfoMap, "reqDetailDiv");
		
		//담당자 있는지 체크
		if(gfnIsNull(reqInfoMap.reqChargerId)){
			$(".usrNmAlarm").hide();
		}
			
		//atchFileId 세팅
		atchFileId = $('#atchFileId').val();
		cbAtchFileId = $('#cbAtchFileId').val();
		/* 
		//클립보드 데이터 세팅
		if(!gfnIsNull(cbContentList)){
			$('#clipBoardContents').html('');
			var add_str = "";
			//데이터 루프
			$.each(cbContentList,function(){
				var cbContent = "";
	    		
	    		cbContentText = this.cbContentText;
	    		//cbContentText = $.parseHTML(this.cbContentText);
				//	var title = $('<div/>').html(cbContentText).text();
								//+gfnCutStrLen(cbContentText,4000).replace(/\n/gi, "<br>")
					
	    		$('#clipBoardContents').append(
						'<div class="pasteContent_main" contentEditable="false" reqCbSeq="'+this.reqCbSeq+'">'
							+'<div class="pasteContent_top">'
								+cbContentText
							+'</div>'
							+'<div class="pasteContent_bottom">'
								+'<div class="cb_bottom_usrNm">'
									+'['+this.modifyUsrNm+']'
								+'</div>'
								+'<div class="cb_bottom_regDtm">'
									+new Date(this.modifyDtm).format('yyyy-MM-dd HH:mm:ss')
								+'</div>'
								+'<div class="cb_bottom_addition" reqCbSeq="'+this.reqCbSeq+'" onclick="fnNotepadDetail(this)">'
									+'상세 정보'
								+'</div>'
							+'</div>'
						+'</div>'
				); 
			});
			fnNotepadImgDeleteBtn('download');
		}
		 */
		//코멘트 리스트 세팅
		gfnSetData2CommentsArea(reqCommentList, "reqCmntListDiv_Detail", "BRD");
		
		if(!gfnIsNull(fileList)){
			//첨부파일 리스트 세팅
	 		var reqId = '${reqId}';
		 	$.each(fileList, function(idx, fileVo){
		 		fileVo['reqId'] = reqId;
		 		gfnFileListReadDiv(fileVo,'#fileListDiv', 'req4104');
		    });
		}
		
		//거절메시지 있는지 확인 후 레이아웃 변경
		var accptRejctTxt = $('#accptRejctTxt').val();
		if(!gfnIsNull(accptRejctTxt)){
			$('.newReqReject').hide();
			$('.newReqRejectCmnt').show();
			
			//업로드 제거
			$('#btn_insert_fileSelect').remove();
			$('#fileListDiv').attr('onclick','');
	
			$('.file_btn.file_delete').attr('onclick','');
			$('.file_btn.file_delete').addClass('delNone');
		}
		
		//코멘트 입력창 클리어
		$("#reqCmnt").val("");
		
		$('.file_progressBar').remove(); //초기에 progressBar열려있으면 제거
		
		
		//하단 탭 이벤트 설정
		$('.footerTab_child').click(function(){
			
			/* 요청 정보 레이어가 길게 내려와 있을 경우 원래 사이즈로 접음 */
			var height = $(".req_top_div_fold1").css('height');
			
			if(height=="690px"){
				$(".req_top_div_fold1").css('height','227px');
				$('#foldSpan').attr('class','reqFoldingCursor down');
				$(".req_fold_title_box").css('border-radius','0 0 5px 5px');
				$('#sub_req_div_view').hide();
				$(".req_sub_fold_contents").animate({scrollTop:0},0);	// 스크롤 최상단으로 이동
				$(".req_sub_fold_contents").css('overflow-y','hidden'); // 세로 스크롤 숨기기
			}
			
			$thisObj = $(this);
			
			var objTab = $thisObj.data('tab');
			/* 
			//클립보드
			if(objTab == "clipBoard"){
				$("#clipBoard_help").css({visibility:"visible"});
			}else{
				$("#clipBoard_help").css({visibility:"hidden"});
			}
			 */
			 
			//추가 항목
			if(objTab == "reqAddOpt"){
				$(".req4105_flowBox.topFlowBox.flowActive").click();
			}
			 
			//현재 선택되어있는 탭이 아닌 경우 탭 전환
			if(!$('.content_child.'+objTab).is('.footerTab_active')){
				//active 클래스 제거
				$('.content_child.footerTab_active, .footerTab_child.footerTab_active').removeClass('footerTab_active');
				
				//선택 탭 전환
				$('.content_child.'+objTab+', .footerTab_child.'+objTab).addClass('footerTab_active');
				
				//탭 focus주기
				$('.content_child.'+objTab).children('.middle_box').focus();
				
				//작업인경우 작업 내용 불러오기
				if(objTab == "reqwork"){
					//요구사항 작업 내용 세팅
					fnReqWorkView(workList);
				}else if(objTab == "svnRevision"){
					//리비전 mask
					$(".req4104_svn_maskBox").show();
					// SVN 리비젼 이력
					fnReqSvnRevisionViewGridSet();	
				}
				//배포 정보인경우 그리드 정보 불러오기
				else if(objTab == "reqDep"){
					//배포 mask
					//$(".req4104_svn_maskBox").show();
					// SVN 리비젼 이력
					fnReqDeployViewGridSet();	
					
				}
			}
		});
		/* 
		//클립보드판 타이핑 제어
		 $('div#clipBoardContents.middle_box').bind("keypress",function(e){
			e.stopPropagation();
			e.preventDefault();
			return false;
		}); 
		//메모장 Ctrl + V감추기
		if(viewer == 'Y'){
			$("#clipBoard_help").hide();
			$("#clipBoardContents").attr("contenteditable","false");
		}else{
			//클립보드판 붙여넣기
			document.getElementById('clipBoardContents').addEventListener('paste', handlePaste);
			document.getElementById('notepad_contents').addEventListener('paste', handlePaste);
			document.getElementById('notepad_contents_update').addEventListener('paste', handlePasteUpdate);
		}
		 */
		
		//클릭 이벤트
		$(document).click(function(e){
			//그림 사이즈 조절 창이 켜져있을 때
			if($('.notepad_img_resize_main').css('display') != "none"){
				//사이즈 조절 외에 공간 클릭 시
				if(!$(event.target).is('img[name=paste_img], .notepad_img_resize_main') && 
						!$(event.target.offsetParent).is('.notepad_img_resize_main, .notepad_img_resize_div')){
					
					//클릭 이벤트 제거
					$('.notepad_img_resize_div > .button_normal').off('click');
					
					//창 숨기기
					$('.notepad_img_resize_main').hide();
				}
			}
			//요구사항 정보 불러오기인경우 원래대로
			if($(".req_top_div.req_left_div").hasClass("reqInfoShow")){
				//사이즈 조절 외에 공간 클릭 시
				if(!$(event.target).is(".pair_boardReqViewBtn, .fa-address-card-o") && 
						!$(event.target.offsetParent).is('.req_top_div.req_left_div, .ajax_box.ui-draggable')){
					$(".req_top_div.req_left_div").removeClass("reqInfoShow");
				} 
			}
		});
		
		//코멘트 입력 글자수  제한
		var arrChkObj = {"reqCmnt":{"type":"length","max":4000,"msg":"4000Byte 이하만 등록이 가능합니다."}};
		gfnInputValChk(arrChkObj);
		
		//이벤트 초기화
		$("div.reqFoldingBtn").off("click");
	
		//폴딩 버튼 동작
		$("div.reqFoldingBtn").click(function(){
			//폴더 타겟
			var $foldBtn = $(this).children(".reqFoldingCursor");
			
			//폴드 번호
			var foldNum = $foldBtn.attr("folding");
			
			//true - 닫혀있음, false - 열려있음
			var foldLayoutDown = $foldBtn.hasClass("down");
			
			//foldDiv target
			var $foldDiv = $(".foldDiv[folding="+foldNum+"]");
			
			//foldDiv toggle
			$foldDiv.slideToggle();
			
			//div on/off
			if(foldLayoutDown){
				$(this).parent(".req_fold_title_box").removeClass("titleFolded");
				$foldBtn.removeClass("down").addClass("up");
			}else{
				$(this).parent(".req_fold_title_box").addClass("titleFolded");
				$foldBtn.removeClass("up").addClass("down");
			}
		});
		
		$("#btnFolding").click(function(){
			fnOnFolding();			
		});
	
	});
	
	var fileList = [];
	var fileCnt;

/**********
*  클립보드
***********/
//클립보드판 붙여넣기 이벤트 해들링
	function handlePaste(e) {
		
		
		var clipboardData, pastedData;
		var agent = navigator.userAgent.toLowerCase();
		
		//Div에 붙여넣기 중지
		e.stopPropagation();
		e.preventDefault();
		
		//입력창이 닫혀있으면 입력창 켜기
		if($('.notepad_input').css('display') == "none"){
			//메모장 내용 지우기
			$('.notepad_contents').html('');
			//메모장 입력창 켜기
			$('.notepad_input').show();
			
			$('.notepad_contents').focus();
			
			//파일 정보 세팅
			fileList = [];
			fileCnt = 0;
		}
		
		clipboardData = e.clipboardData || window.clipboardData;

		//크롬, 사파리, 파이어폭스 체크
		if (agent.indexOf("chrome") != -1 || agent.indexOf("safari") != -1 || agent.indexOf("firefox") != -1) {
			
			var items = clipboardData.items;

			
			if (items.length > 0) {
				/* 텍스트  */
				if (items[0].type == "text/plain") {
					$('.notepad_contents').prepend('<div>'+clipboardData.getData('Text').replace(/\n/gi,'<br/>')+'</div>');
				/* 파일  */
				} else if(items[0].kind == "file"){
					
					//파일 읽기
					for (var i = 0; i < items.length; i++) {
						var file = items[i].getAsFile();
						var converter_file = new File([file],'image'+fileCnt+'_'+new Date().format('yyyyMMddHHmmss')+'.png',{type: "image/png"});

						fileList.push(converter_file);
						//파일 데이터로 img 만들기
						var imgSrc = window.URL.createObjectURL(file);
						$('.notepad_contents').append('<div class="notepad_contents_imgDiv"><img src="'+imgSrc+'" name="paste_img" data-filesn="'+fileCnt+'"/></div><br/>');
					}
					
					fnNotepadImgDeleteBtn('insert');
					fileCnt++;
				/* 그 외에 타입  */
				}else if(items[0].type == "text/html"){
					$('.notepad_contents').append($('<div><xmp>'+clipboardData.getData(items[0].type)+'</xmp></div>'));
				}
				else {
					$('.notepad_contents').append('<div>'+clipboardData.getData(items[0].type).replace(/\n/gi,'<br/>')+'</div>');
				}
			}
			
		//ie 체크
		} else {
			//파일 첨부
			if (clipboardData.files.length > 0) {
				var file = clipboardData.files[0];
				fileList.push(file);

				//파일 데이터로 img 만들기
				var imgSrc = window.URL.createObjectURL(file);
				$('.notepad_contents').append('<div class="notepad_contents_imgDiv"><img src="'+imgSrc+'" name="paste_img" data-filesn="'+fileCnt+'"/></div><br/>');
				
				fnNotepadImgDeleteBtn('insert');
				fileCnt++;
			} else {
				$('.notepad_contents').append('<div>'+clipboardData.getData('Text').replace(/\n/gi,'<br/>')+'</div>');
			}
		}
	}
	
	//클립보드판 붙여넣기 수정 이벤트 해들링
	function handlePasteUpdate(e) {
		
		
		var clipboardData, pastedData;
		var agent = navigator.userAgent.toLowerCase();
		
		//Div에 붙여넣기 중지
		e.stopPropagation();
		e.preventDefault();
		
		//파일 정보 세팅
		fileList = [];
		fileCnt = 0;
			
		clipboardData = e.clipboardData || window.clipboardData;

		//크롬, 사파리, 파이어폭스 체크
		if (agent.indexOf("chrome") != -1 || agent.indexOf("safari") != -1 || agent.indexOf("firefox") != -1) {
			
			var items = clipboardData.items;

			if (items.length > 0) {
				/* 텍스트  */
				if (items[0].type == "text/plain") {
					$('#notepad_contents_update').prepend('<div>'+clipboardData.getData('Text').replace(/\n/gi,'<br/>')+'</div>');
				/* 파일  */
				} else if(items[0].kind == "file"){
					
					//파일 읽기
					for (var i = 0; i < items.length; i++) {
						var file = items[i].getAsFile();
						var converter_file = new File([file],'image'+fileCnt+'_'+new Date().format('yyyyMMddHHmmss')+'.png',{type: "image/png"});

						fileList.push(converter_file);
						//파일 데이터로 img 만들기
						var imgSrc = window.URL.createObjectURL(file);
						$('#notepad_contents_update').append('<div class="notepad_contents_imgDiv"><img src="'+imgSrc+'" name="paste_img" data-filesn="'+fileCnt+'"/></div><br/>');
					}
					
					fnNotepadImgDeleteBtn('update');
					fileCnt++;
				/* 그 외에 타입  */
				}else {
					$('#notepad_contents_update').append('<div>'+clipboardData.getData(items[0].type).replace(/\n/gi,'<br/>')+'</div>');
				}
			}
			
		//ie 체크
		} else {
			//파일 첨부
			if (clipboardData.files.length > 0) {
				var file = clipboardData.files[0];
				fileList.push(file);

				//파일 데이터로 img 만들기
				var imgSrc = window.URL.createObjectURL(file);
				$('#notepad_contents_update').append('<div class="notepad_contents_imgDiv"><img src="'+imgSrc+'" name="paste_img" data-filesn="'+fileCnt+'"/></div><br/>');
				
				fnNotepadImgDeleteBtn('update');
				fileCnt++;
			} else {
				$('#notepad_contents_update').append('<div>'+clipboardData.getData('Text').replace(/\n/gi,'<br/>')+'</div>');
			}
		}

	}
	
	//클립보드판 메모장 입력 등록
	$('#notepad_content_insert').click(function(){
		fnClipBoardPasteAjax();
	});
	//클립보드판 메모장 단건 수정 등록
	$('#notepad_detail_update').click(function(){
		fnClipBoardUpdateAjax();
	});
	
	
	//클립보드판 붙여넣기 내용 컨트롤러 전달
	function fnClipBoardPasteAjax(){
		//FormData 초기화
		var fd = new FormData();
		
		fd.append('prjId',$("#prjId").val());
		fd.append('reqId',"${reqId}");
		fd.append('cbAtchFileId',cbAtchFileId);
		
		//파일 목록
		var notepad_img = $('#notepad_contents > div > img[name=paste_img]');
		$.each(notepad_img,function(idx,map){
			var fileObj = fileList[$(map).data('filesn')]; 
			fileObj.name = 'image'+idx+'.png'
			fd.append('file',fileObj,'image'+idx+'.png');
		});

		//내용 전달
		fd.append('notepad_contents',$('#notepad_contents').html());
		
		//삽입 or 수정
		fd.append('type','insert');
		//AJAX 설정 
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4100/insertReq4104CBPaste.do'/>"
					,"contentType":false
					,"processData":false
					,"cache":false
					,"loadingShow":false
					,"async":true}
				,fd);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	    	
	    	if(data.errorYN == 'Y'){
	    		toast.push(data.message);
	    		return false;
	    	} 
	    	else{
	    		 $('#clipBoardContents').prepend(
					'<div class="pasteContent_main" contentEditable="false" reqCbSeq="'+data.reqCbSeq+'">'
						+'<div class="pasteContent_top">'
							+data.notepad_contents
						+'</div>'
						+'<div class="pasteContent_bottom">'
							+'<div class="cb_bottom_usrNm">'
								+'[${sessionScope.loginVO.usrNm}]'
							+'</div>'
							+'<div class="cb_bottom_regDtm">'
								+new Date().format('yyyy-MM-dd HH:mm:ss')
							+'</div>'
							+'<div class="cb_bottom_addition" reqCbSeq="'+data.reqCbSeq+'" onclick="fnNotepadDetail(this)">'
								+'상세 정보'
							+'</div>'
						+'</div>'
					+'</div>'
				); 
	    		
	    		//스크롤 위치 최상단
	    		$('#clipBoardContents').scrollTop(0);
	    		
	    		//메모장 입력창 끄기
				$('.notepad_input').hide();	
	    		
	    	}
	    	fnNotepadImgDeleteBtn('download');
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			toast.push("ERROR STATUS("+status+")<br>"+err);
	 	});
		
		//AJAX 전송
		ajaxObj.send();
	}
	
	//클립보드판 붙여넣기 내용 수정 컨트롤러 전달
	function fnClipBoardUpdateAjax(){
		
		//FormData 초기화
		var fd = new FormData();
		
		fd.append('prjId',$("#prjId").val());
		fd.append('reqId',"${reqId}");
		fd.append('cbAtchFileId',cbAtchFileId);
		
		//파일 목록
		var notepad_img = $('#notepad_contents_update > div > img[srcchgchk!="Y"][name=paste_img]');
		$.each(notepad_img,function(idx,map){
			var fileObj = fileList[$(map).data('filesn')]; 
			fileObj.name = 'image'+idx+'.png'
			fd.append('file',fileObj,'image'+idx+'.png');
		});
		
		//수정 내용
		var update_contents = $('#notepad_contents_update');
		
		//내용 전달
		fd.append('notepad_contents',$(update_contents).html());
	
		//삽입 or 수정
		fd.append('type','update');
		
		//단건 seq
		var reqCbSeq = $('#reqCbSeq').val();
		fd.append('reqCbSeq',reqCbSeq);
		
		//AJAX 설정 
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4100/insertReq4104CBPaste.do'/>"
					,"contentType":false
					,"processData":false
					,"cache":false
					,"async":true}
				,fd);
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	    	
	    	if(data.errorYN == 'Y'){
	    		toast.push(data.message);
	    		return false;
	    	} 
	    	else{
	    		$('.pasteContent_main[reqcbseq='+reqCbSeq+'] .pasteContent_top').html(data.notepad_contents);
	    		$('.pasteContent_main[reqcbseq='+reqCbSeq+'] .cb_bottom_regDtm').html(new Date().format('yyyy-MM-dd HH:mm:ss'));
	    		$('.pasteContent_main[reqcbseq='+reqCbSeq+'] .cb_bottom_usrNm').html("${sessionScope.loginVO.usrNm}");
	    		
	    		
	    		
	    		//스크롤 위치 최상단
	    		$('#clipBoardContents').scrollTop(0);
	    		
	    		//메모장 입력창 끄기
				$('#notepad_detail_cancel').click();
	    	}
	    	fnNotepadImgDeleteBtn('download');
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			toast.push("ERROR STATUS("+status+")<br>"+err);
	 	});
		
		//AJAX 전송
		ajaxObj.send();
	}
//클립보드 이미지 삭제 버튼
	function fnNotepadImgDeleteBtn(type){
		//기본 삭제버튼 (다운버튼 숨김)
		$('#notepad_img_download').hide();
		$('#notepad_img_delete').show();
		var classTarget = ".notepad_contents ";
		if(type == "update"){
			classTarget = "#notepad_contents_update ";
		}
		else if(type == "download"){
			classTarget = "#clipBoardContents ";
			$('#notepad_img_download').show();
			$('#notepad_img_delete').hide();
		}
		//중복 제거를 위해 이벤트 모두 제거
		$(classTarget+'img[name=paste_img]').off('click');
		
		//클릭이벤트 새로 생성
		$(classTarget+'img[name=paste_img]').click(function(e){
			var $imgObj = $(e.target);
			
			//가로 x 세로 픽셀 입력
			$('#resize_width').val($imgObj.width());
			$('#resize_height').val($imgObj.height());
			
			//마우스 클릭 위치
			var cursor_x = (e.clientX-$('.layer_popup_box').offset().left);
			var cursor_y = (e.clientY-($('.layer_popup_box').offset().top-$(window).scrollTop()));
			
			$('.notepad_img_resize_main').css({'left':cursor_x+'px','top':cursor_y+'px'});
			$('.notepad_img_resize_main').show();
			
			$('.button_normal').off('click');
			//버튼 이벤트 입력
			$('.button_normal').click(function(){
				if($(this).is('#notepad_img_delete')){
					$imgObj.remove();
				}
				if($(this).is('#notepad_img_download')){
					//파일 다운로드
					gfnFileDownload(e.target,false,'png');
				}
				
				//삭제 클릭 이벤트 버리기
				$('.button_normal').off('click');
				$('.notepad_img_resize_main').hide();
			});
		});
	}
//클립보드판 텍스트 붙여넣기
function fnTextCopyCall(obj){
	fnRecentCmnt($(obj).parent().siblings(".pasteContent_top"));
}
// 클립 보드 복사 
function fnRecentCmnt( t ) {
	//복사 후 remove될 때 스크롤 움직이지 않도록 포지션 지정 
    var $temp = $("<textarea style='position:fixed;right:0;'>");
    $("body").append($temp);

    $temp.val( $(t).attr('title') ).select();
    
    document.execCommand("copy");
    $temp.remove();
    toast.push('클립보드에 복사되었습니다.');
}
 
 /**********
  *  메모장     
 ***********/
//메모장 작성 취소
$('#notepad_cancel').click(function(){
	fnNotepadImgDeleteBtn('download');
	$('.notepad_input').hide();
	$('.notepad_contents').html('');
	fd = new FormData;
});

//메모장 상세정보 창 닫기
$('#notepad_detail_cancel').click(function(){
	fnNotepadImgDeleteBtn('download');
	$('.notepad_update').hide();
	$('.notepad_update .notepad_contents').html('');
	fd = new FormData;
});

//메모장 상세 정보 삭제
$('#notepad_detail_delete').click(function(){
	fnDeleteDetail('삭제하시겠습니까?');
});
//메모 상세정보창 오픈
	function fnNotepadDetail(obj){
		if(viewer == "Y"){
			jAlert("현재 오픈된 프로젝트가 다르기 때문에 수정 및 삭제가 불가능합니다.","알림");
			return false;
		}
		
		var $md_notepad = $('.notepad_update');
		$md_notepad.show();

		//AJAX 설정 
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4100/selectReq4104CBPaste.do'/>"}
				,{'prjId':"${sessionScope.selPrjId}", 'reqId':reqId, 'reqCbSeq':$(obj).attr('reqcbseq')});
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
	    	
	    	if(data.errorYN == 'Y'){
	    		toast.push(data.message);
	    		return false;
	    	} 
	    	else{
	    		var cbData = data.cbData;
	    	
	    		cbData.regDtm = new Date(cbData.regDtm).format('yyyy-MM-dd HH:mm:ss');
	    		cbData.modifyDtm = new Date(cbData.modifyDtm).format('yyyy-MM-dd HH:mm:ss');
	    		
	    		//등록정보, 수정정보 채우기
	    		gfnSetData2ParentObj(cbData, "notepad_detailInfo");
	    		
	    		//내용 채우기
	    		$('.notepad_update .notepad_contents').html(cbData.cbContentText);
	    		
	    		//이미지 삭제 등록
	    		fnNotepadImgDeleteBtn('update');
	    	}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			toast.push("ERROR STATUS("+status+")<br>"+err);
	 	});
		
		//AJAX 전송
		ajaxObj.send();
	}
//메모장 삭제
function fnDeleteDetail(msg){
	var deleteChk = false;
	jConfirm(msg, '알림창', function( result ) {
		if(result){
			//AJAX 설정 
			var ajaxObj = new gfnAjaxRequestAction(
					{"url":"<c:url value='/req/req4000/req4100/deleteReq4104CBPaste.do'/>","async":true}
					,{'prjId':"${sessionScope.selPrjId}", 'reqId':reqId, 'reqCbSeq':$('.notepad_detailInfo_left input#reqCbSeq').val()});
			//AJAX 전송 성공 함수
			ajaxObj.setFnSuccess(function(data){
				data = JSON.parse(data);
				//삭제 성공인 경우
				if(data.errorYN == "N"){
					//상세정보 제거
					$('.pasteContent_main[reqCbSeq='+$('.notepad_detailInfo_left input#reqCbSeq').val()+']').remove();
					$('.notepad_update').hide();
					$('.notepad_update .notepad_contents').html('');
					fd = new FormData;
					deleteChk = true;
				}
		    	toast.push(data.message);
		    	fnNotepadImgDeleteBtn('download');
			});
			//AJAX 전송 오류 함수
			ajaxObj.setFnError(function(xhr, status, err){
				toast.push("ERROR STATUS("+status+")<br>"+err);
		 	});
			//AJAX 전송
			ajaxObj.send();
		}
	});
	
	return deleteChk;
}
 //업로드된 첨부파일 전체 압축 후 다운로드
function fnZipDownload(){
	if($('.file_contents').length == 0){
		toast.push("첨부파일이 없습니다.");
		return false;
	}
	else if($('.file_contents').length < 2){
		toast.push("2개 이상의 첨부파일이 존재해야 합니다.");
		return false;
	}
	
	//atchFileId 세팅
	var fileAtchFileId = $('.file_contents').eq(0).attr("atchid");
	var fileInfo = "";
	$.each($('.file_contents'),function(){
		//배열로 세팅
		fileInfo = fileInfo+"downFile="+fileAtchFileId+";"+$(this).attr('filesn')+";zipDownload&";
	});
	
	$.download('/com/fms/ZipFileDown.do',fileInfo.slice(0,-1),'post');
}
 
 /**********
  *  기타
 ***********/
//하위 요구사항 정보 레이어팝업 다시 불러오기
 function fnReqSubChange(target_this){
	gfnLayerPopupClose();
	
	//개발주기 종료건인지 체크
	sprEndDt = null;
	if(typeof sprintInfo != "undefined" && gfnIsNull(sprEndDt)){
		var sprEndDt = sprintInfo.modifyDtm;
	}
	
	//신규 요구사항인지 체크
	var newReqChk = "${newReqChk}";
	
	//prj:프로젝트OSL보드, pop:팝업OSL보드, spr:스프린트OSL보드, dal:데일리스크럼보드 
	var data = {"mode": "${mode}", "sprintId": "${sprintId}", "reqId": target_this.value, "newReqChk":newReqChk}; 
	gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
} 

 
//요구사항 코멘트 저장 이벤트
function fnPopInsertReqCmntInfo(){
	var prjId = $("#reqCmntFrm #prjId").val();
	var reqId = $("#reqCmntFrm #reqId").val();
	var reqCmnt = $("#reqCmntFrm #reqCmnt").val();
	
	//입력내용이 없으면
	if( reqCmnt == "" || reqCmnt == null ){
		toast.push('코멘트를 입력하시고 저장하세요.');
		return;
	}
	//태그 제거
	reqCmnt = reqCmnt.replace(/</gi,'&lt;').replace(/>/gi,'&gt;');

	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req2000/req2000/insertReq2000ReqCommentInfoAjax.do'/>", "loadingShow":false}
			,{"prjId" : prjId, "reqId" : reqId, "reqCmnt" : reqCmnt});

	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		
		data = JSON.parse(data);
    	//코멘트 등록 실패의 경우 리턴
    	if(data.saveYN == 'N'){
    		toast.push(data.message);
    		return;
    	}
    	reqInfo = data.reqCommentList[0];

    	//개발주기 OSL보드 코멘트 최상위 추가
    	if($('.third_wrap > .title').length > 0){
	    	$('.third_wrap > .title').after("<div class='reqChangeDiv recentCmnt' reqId='"+reqInfo.reqId+"'>"
					+" <div class='subRCD' title='"+reqInfo.reqCmnt+"'>"
					+	gfnCheckStrLength(reqInfo.reqCmnt,22)
					+"</div>"
					+		" <div class='subRCD'>"
					+"<span class='bold'>["+reqInfo.reqId+"]</span> "
					+reqInfo.regUsrNm+" - "
					+ (reqInfo.regDtm).substring(0, 16) + "</div>"
					+	"</div>");
    	
	    	//코멘트 수가 8개 이상인 경우 마지막 코멘트 제거 
	     	if($('.third_wrap > .reqChangeDiv').length >= 8){
	    		$('.third_wrap > .reqChangeDiv:last-child').remove();
	    	} 
	    	//요구사항 코멘트 클릭 이벤트 제거
	    	$(".recentCmnt").off('click');
	    	
	    	//요구사항 코멘트 클릭 이벤트
			$(".recentCmnt").click(function(){
				//prj:프로젝트OSL보드, pop:팝업OSL보드, dal:데일리스크럼보드
				var data = {"mode": "prj",  "reqId": $(this).attr("reqId")}; 
				gfnLayerPopupOpen("/req/req4000/req4100/selectReq4104View.do", data, '1300', '850','scroll');
			});
    	}
    	//코멘트 리스트 세팅
    	gfnSetData2CommentsArea(data.reqCommentList, "reqCmntListDiv_Detail", "BRD");
    	
    	//코멘트 입력창 클리어
    	$("#reqCmnt").val("");
    	toast.push(data.message);
	});
	
	//AJAX 전송
	ajaxObj.send();
}

/**********
  *  요구사항 변경 이력
 ***********/
function fnReqChgView(reqChgList, reqInfoMap, reqSubId){

	var viewStr = "",proViewStr = "",reqViewStr = "";

	//상단, 하단 요구사항 수
	var topIndex=0,bottomIndex=0,allIndex=0;
	
	//상단, 하단 margin-left 총 합
	var topMargin=0,bottomMargin=0,allMargin=0;
	
	//요구사항 카드 width 기준 (CSS값과 동일 값으로 세팅)
	var reqBoxWidth = 150+10;
	
	//요구사항 카드 width기준을 몇 시간으로 분배할 것인지 (기준 = hour)
	var defaultHourMargin = 1;
	
	// 시간차이/calcMargin = margin-left 값
	var calcMargin = ((defaultHourMargin*60*60)/reqBoxWidth);
	var calcDtm = 0,calcDtmMargin=0;
	
	//이전 요구사항 시간값
	var prev_reqDtm = 0;
	
	//내용 초기화
	$('.slideChg_box#reqChgViewDiv').html('');

	//변경이력 Loop
	$.each(reqChgList,function(idx,map){
		//프로세스 상자 생성
		if(idx == 0){
			proViewStr = 	'<div class="process_box select_process">'
								+'<ul>'
									+'<li class="process_top_title">'+map.processNm+'</li>'
									+'<li class="process_top_box"></li>'
									+'<li class="process_bottom_box"></li>'
								+'</ul>'
							+'</div>';
			$('#reqChgViewDiv').append(proViewStr);
		}
		
		//요구사항 그룹, 하위 요구사항 항목 선택인경우 해당 요구사항만 표시
		if(typeof reqSubId != "undefined"){
			//전체 보기가 아닌 경우
			if(reqSubId != "all"){
				if(map.reqId != reqSubId){
					//해당 요구사항 Id가 아닌경우 skip
					return true;
				}
			}
		}
		
		//글자 수 제한
		var chgUsrNm = gfnCutStrLen(map.chgUsrNm,18);
		var chkUsrNm = gfnCutStrLen(map.chkUsrNm,18);
		var signRejectCmnt = map.signRejectCmnt;
		if(!gfnIsNull(signRejectCmnt)){
			signRejectCmnt = signRejectCmnt.replace(/&nbsp;&nbsp;&nbsp;&nbsp;/gi,"</br>");
		}
		
		var preFlowNm = gfnCheckStrLength(map.preFlowNm,10);
		var chgFlowNm = gfnCheckStrLength(map.chgFlowNm,10);
		var armReqNm = map.reqNm.replace(/'/gi,"\'");

		//요구사항 시간 차이 값 계산하기
		calcDtm = 0;
		calcDtmMargin = 0;
		
		//요구사항 그리기 {CHG = 상단(req_top), CHK = 하단(req_bottom)}
		if(map.type == "CHG"){
			reqViewStr = "";
			calcDtmMargin = 5;
			//상단 요구사항에 맞게 margin-left 조절
			if(bottomIndex > 0){
				calcDtmMargin = calcDtmMargin+((bottomIndex)*reqBoxWidth)+calcDtm+bottomMargin;
				allMargin += calcDtm;
				topMargin += calcDtm;
				bottomIndex = 0;
				bottomMargin = 0;
			}

			//변경 구분타입이 작업흐름 변경
			if(map.reqChgType == "01"){
				var chgListTitle = "작업흐름 변경";
				
				//작업흐름 icon 구하기
				var preFlowIcon = fnFlowOptionIcon("pre",map);
				var chgFlowIcon = fnFlowOptionIcon("chg",map);
				
				reqViewStr = '<div class="req_box_title" title="'+map.reqNm+'">['+chgListTitle+']</div>' 
							+'<div class="req_box_main prev" style="background-color:'+map.preFlowTitleBgColor+';color:'+map.preFlowTitleColor+'">'+preFlowIcon+preFlowNm+'</div>'
							+'<div class="req_box_main arrow"><li class="fa fa-angle-down fa-2x"></li></div>'
							+'<div class="req_box_main chg" style="background-color:'+map.chgFlowTitleBgColor+';color:'+map.chgFlowTitleColor+'">'+chgFlowIcon+chgFlowNm+'</div>'
							/* +'<div class="req_box_main bottom historyUsrDiv" style="margin-top: 5px;" title="'+map.chgUsrNm+'" onclick="gfnAlarmOpen(\''+map.chgUsrId+'\',\''+map.reqId+'\',\''+armReqNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-edit"></i>&nbsp;'+chgUsrNm+'</div>'; */
							+'<div class="req_box_main bottom historyUsrDiv" style="margin-top: 5px;" title="'+map.chgUsrNm+'"><i class="fa fa-user-edit"></i>&nbsp;'+chgUsrNm+'</div>';
			}
			//담당자 변경
			else if(map.reqChgType == "02"){
				var chgListTitle = "담당자 변경";
				
				//이전 담당자 img
				var preChargerImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.preChargerImg;
				
				//변경 담당자 img
				var chgChargerImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.chgChargerImg;
				
				reqViewStr = '<div class="req_box_title" title="'+map.reqNm+'">['+chgListTitle+']</div>' 
							+'<div class="req_box_main regUsrImg">'
								+'<img src="'+preChargerImgSrc+'">'
								+'<div>[현재]</div>'
								/* +'<div class="historyUsrDiv" title="'+map.preChargerNm+'" onclick="gfnAlarmOpen(\''+map.preChargerId+'\',\''+map.reqId+'\',\''+map.preChargerNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-edit"></i>&nbsp;'+map.preChargerNm+'</div>' */
								+'<div class="historyUsrDiv" title="'+map.preChargerNm+'"><i class="fa fa-user-edit"></i>&nbsp;'+map.preChargerNm+'</div>'
							+'</div>'
							+'<div class="req_box_main arrow" style="height: 10px;"><li class="fa fa-angle-double-down fa-lg"></li></div>'
							+'<div class="req_box_main regUsrImg">'
								+'<img src="'+chgChargerImgSrc+'">'
								+'<div>[변경]</div>'
								/* +'<div class="historyUsrDiv" title="'+map.chgChargerNm+'" onclick="gfnAlarmOpen(\''+map.chgChargerId+'\',\''+map.reqId+'\',\''+map.chgChargerNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-check"></i>&nbsp;'+map.chgChargerNm+'</div>' */
								+'<div class="historyUsrDiv" title="'+map.chgChargerNm+'"><i class="fa fa-user-check"></i>&nbsp;'+map.chgChargerNm+'</div>'
							+'</div>';
			}
			//접수 완료 처리
			else if(map.reqChgType == "03"){
				var chgListTitle = "접수 완료";
				//작업흐름 icon 구하기
				var chgFlowIcon = fnFlowOptionIcon("chg",map);
				
				reqViewStr = '<div class="req_box_title" title="'+map.reqNm+'">['+chgListTitle+']</div>'
							+'<div class="req_box_main prev" style="color:#000;">요청 접수</div>'
							+'<div class="req_box_main arrow"><li class="fa fa-angle-down fa-2x"></li></div>'
							+'<div class="req_box_main chg" style="background-color:'+map.chgFlowTitleBgColor+';color:'+map.chgFlowTitleColor+'">'+chgFlowIcon+chgFlowNm+'</div>'
							/* +'<div class="req_box_main bottom historyUsrDiv" style="margin-top: 5px;" title="'+map.chgUsrNm+'" onclick="gfnAlarmOpen(\''+map.chgUsrId+'\',\''+map.reqId+'\',\''+armReqNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-edit"></i>&nbsp;'+chgUsrNm+'</div>'; */
							+'<div class="req_box_main bottom historyUsrDiv" style="margin-top: 5px;" title="'+map.chgUsrNm+'"><i class="fa fa-user-edit"></i>&nbsp;'+chgUsrNm+'</div>';
			}
			//접수 반려 처리
			else if(map.reqChgType == "04"){
				var chgListTitle = "접수 반려";
				reqViewStr = '<div class="req_box_title" title="'+map.reqNm+'">['+chgListTitle+']</div>'
							+'<div class="req_box_main bottom signRejectDiv" readonly="readonly" title="'+map.signRejectCmnt+'" onclick="signRejectPopupOpen(this)">사유: '+signRejectCmnt+'</div>'
							/* +'<div class="req_box_main bottom historyUsrDiv" style="margin-top: 5px;" title="'+map.chgUsrNm+'" onclick="gfnAlarmOpen(\''+map.chgUsrId+'\',\''+map.reqId+'\',\''+armReqNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-edit"></i>&nbsp;'+chgUsrNm+'</div>'; */
							+'<div class="req_box_main bottom historyUsrDiv" style="margin-top: 5px;" title="'+map.chgUsrNm+'"><i class="fa fa-user-edit"></i>&nbsp;'+chgUsrNm+'</div>';
			}
			
			//상단 요구사항 그리기
			viewStr =	'<div class="reqFrameBox"><div class="req_main_box req_top reqSeq_'+map.reqChgId+'" style="margin-left:'+calcDtmMargin.toFixed(1)+'px" id="'+map.reqId+'">'
							+'<div class="req_top_box">'
							+reqViewStr
							+'</div>'
							+'<div class="req_bottom_box">'
							+new Date(map.chgDtm).format('yyyy-MM-dd HH:mm:ss')
							+'</div>'
						+'</div></div>';
			$('.select_process .process_top_box').append(viewStr);
			topIndex++;
		}else if(map.type == "CHK"){	//결재 정보
			reqViewStr = "";
			//날짜 계산
			var today = new Date(map.signDtm).format('yyyy-MM-dd HH:mm:ss');
			
			calcDtmMargin = 5;
			//상단 요구사항에 맞게 margin-left 조절
			if(topIndex > 0){
				calcDtmMargin = calcDtmMargin+((topIndex)*reqBoxWidth)+calcDtm+topMargin;

				allMargin += calcDtm;
				bottomMargin += calcDtm;
				topIndex = 0;
				topMargin = 0;
			}
			
			//결재 요청
			if(map.signCd == '01'){
				//요청자 img
				var regUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.regUsrImg;
				
				//결재자 img
				var signUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.signUsrImg;
				
				reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 요청]</div>' 
							+'<div class="req_box_main regUsrImg">'
								+'<img src="'+regUsrImgSrc+'">'
								+'<div>[요청자]</div>'
								/* +'<div class="historyUsrDiv" title="'+map.regUsrNm+'" onclick="gfnAlarmOpen(\''+map.regUsrId+'\',\''+map.reqId+'\',\''+map.regUsrNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-edit"></i>&nbsp;'+map.regUsrNm+'</div>' */
								+'<div class="historyUsrDiv" title="'+map.regUsrNm+'"><i class="fa fa-user-edit"></i>&nbsp;'+map.regUsrNm+'</div>'
							+'</div>'
							+'<div class="req_box_main arrow" style="height: 10px;"><li class="fa fa-angle-double-down fa-lg"></li></div>'
							+'<div class="req_box_main regUsrImg">'
								+'<img src="'+signUsrImgSrc+'">'
								+'<div>[결재자]</div>'
								/* +'<div class="historyUsrDiv" title="'+map.signUsrNm+'" onclick="gfnAlarmOpen(\''+map.signUsrId+'\',\''+map.reqId+'\',\''+map.signUsrNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-check"></i>&nbsp;'+map.signUsrNm+'</div>' */
								+'<div class="historyUsrDiv" title="'+map.signUsrNm+'"><i class="fa fa-user-check"></i>&nbsp;'+map.signUsrNm+'</div>'
							+'</div>';
			}
			//결재 승인
			else if(map.signCd == '02'){
				//결재자 img
				var signUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.signUsrImg;
				
				reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 승인]</div>' 
							+'<div class="req_box_main signAcceptDiv">'
								+'<img src="'+signUsrImgSrc+'">'
								/* +'<div class="historyUsrDiv" title="'+map.regUsrNm+'" onclick="gfnAlarmOpen(\''+map.regUsrId+'\',\''+map.reqId+'\',\''+map.regUsrNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-file-signature"></i>&nbsp;'+map.regUsrNm+'</div>' */
								+'<div class="historyUsrDiv" title="'+map.regUsrNm+'" ><i class="fa fa-file-signature"></i>&nbsp;'+map.regUsrNm+'</div>'
							+'</div>'
			}
			//결재 반려
			else if(map.signCd == '03'){
				reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 반려]</div>'
							+'<div class="req_box_main bottom signRejectDiv" readonly="readonly" title="'+map.signRejectCmnt+'" onclick="signRejectPopupOpen(this)">사유: '+signRejectCmnt+'</div>'
							/* +'<div class="req_box_main bottom historyUsrDiv signRejectUsrDiv" onclick="gfnAlarmOpen(\''+map.signUsrId+'\',\''+map.reqId+'\',\''+map.regUsrNm+'\',\'${prjId}\',\'${prjGrpId}\')"><i class="fa fa-user-times"></i>'+map.signUsrNm+'</div>'; */
							+'<div class="req_box_main bottom historyUsrDiv signRejectUsrDiv"><i class="fa fa-user-times"></i>'+map.signUsrNm+'</div>';
			}
			//하단 요구사항 그리기
			viewStr =	'<div class="reqFrameBox"><div class="req_main_box req_bottom reqSeq_'+map.signId+'" style="margin-left:'+calcDtmMargin.toFixed(1)+'px" id="'+map.reqId+'">'
							+'<div class="req_top_box">'
							+today
							+'</div>'
							+'<div class="req_bottom_box">'
							+reqViewStr
							+'</div>'
						+'</div></div>';
			$('.select_process  .process_bottom_box').append(viewStr);
						
			bottomIndex++;
		} else{
			console.log('error');
			//오류있는 데이터, skip
			return true;
		}
		//이전 요구사항 시간 값 저장
		prev_reqDtm = new Date(map.regDtm).getTime();
		
		//전체 요구사항 수
		allIndex++;
	});
	//스크롤 우측 끝으로 이동
	$('#reqChgViewDiv').stop().animate( { scrollLeft : 9999999 },1 );
}

//작업흐름 icon 구하는 함수
function fnFlowOptionIcon(type,flowData){
	 var flowIcon = "";
	 
      //flowNextId 없는경우
     if(gfnIsNull(flowData[type+"FlowNextId"]) || flowData[type+"FlowNextId"] == "null"){
     	flowIcon += "<li class='far fa-stop-circle' title='[종료]최종 완료'></li>";
     } 
     //필수
     if(flowData[type+"FlowEssentialCd"] == "01"){
     	flowIcon += "<li class='fa fa-key' title='필수'></li>";
     }
     //결재
     if(flowData[type+"FlowSignCd"] == "01"){
     	flowIcon += "<li class='fa fa-file-signature' title='결재'></li>";
     }
     //종료
     if(flowData[type+"FlowEndCd"] == "01"){
     	flowIcon += "<li class='fa fa-sign-out-alt' title='종료 분기'></li>";
     }
     //작업 체크
	if(flowData[type+"FlowWorkCd"] == "01"){
		flowIcon += '<li class="fa fa-code-branch" title="작업 분기"></li>';
	}
	//리비전 체크
	if(flowData[type+"FlowRevisionCd"] == "01"){
		flowIcon += '<li class="fa fa-code" title="리비전 저장 유무"></li>';
	}
	//배포계획 체크
	if(flowData[type+"FlowDplCd"] == "01"){
		flowIcon += '<li class="fa fa-puzzle-piece" title="배포계획 저장 유무"></li>';
	}
	//허용 역할
	if(flowData[type+"FlowAuthCd"] == "01"){
		flowIcon += '<li class="fa fa-user-shield" title="허용 역할그룹 제한유무"></li>';
	}
     //추가 항목
     if(!gfnIsNull(flowData[type+"FlowOptCnt"]) && flowData[type+"FlowOptCnt"] > 0){
     	flowIcon += "<li class='fa fa-list' title='추가 항목'></li>+"+flowData[type+"FlowOptCnt"];
     }
     
     if(!gfnIsNull(flowIcon)){
     	flowIcon = "<div class='flowOptionDiv hideOptDiv'>"+flowIcon+"</div>";
     }
     
     return flowIcon;
}

/**********
  *  요구사항 수정 이력
 ***********/
function fnReqChgDetailView(reqChgDetailList){
	//$(".reqHistory .middle_box")
	
	var reqChgDetailStr = "";
	var chgDetailContent = "";
	var prevChgDetailId = "";
	
	//수정 이력 loop
	$.each(reqChgDetailList,function(idx, map){
		//수정이력 내용 구하기
		var chgDetailType = map.chgDetailType;
		
		//요구사항 수정+ 요구사항 추가 항목 수정
		if(chgDetailType == "01" || chgDetailType == "02"){
			chgDetailContent += "<span class='chgDetailDiff' num='"+idx+"' onclick='fnOptDiff(this)'><b>"+map.chgDetailNm+"</b>(이)가 수정되었습니다.</span>";
		}
		//첨부파일 추가
		else if(chgDetailType == "03"){
			chgDetailContent += "<span><li class='fas fa-file-upload'></li>&nbsp;첨부파일 <b>"+map.chgDetailVal+"</b>(이)가 추가되었습니다.</span>";
		}
		//첨부파일 삭제
		else if(chgDetailType == "04"){
			chgDetailContent += "<span class='chgDetailFileDel'>첨부파일 <b>"+map.chgDetailVal+"</b>(이)가 삭제되었습니다.</span>";
		}
		
		
		//CHG_DETAIL_ID값 다른 경우 내용 보여주기
		if((reqChgDetailList.length-1) == idx || map.chgDetailId != reqChgDetailList[idx+1].chgDetailId){
			//ago Time구하기
			var agoTime = gfnDtmAgoStr(new Date(map.regDtm).getTime());

			//요구사항 명
			var reqNm = $("#reqNm").val();
			
			//사용자 img
			var regUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.regUsrImg;
			
			//문자열 넣기
			reqChgDetailStr += '<div class="chgDetailMainDiv">'
									+'<div class="chgDetailLeftDiv">'
										+'<img src="'+regUsrImgSrc+'">'
									+'</div>'
									+'<div class="chgDetailRightDiv">'
										+'<div class="chgDetailRightTopDiv">'
											+'<div class="chgDetailUsrDiv"><i class="fa fa-user"></i>&nbsp;'+map.regUsrNm+' <small>[ '+map.regUsrEmail+' ]</small></div>'
											+'<div class="chgDetailAgoDiv"><li class="far fa-clock"></li>&nbsp;'+agoTime+'</div>'
										+'</div>'
										+'<div class="chgDetailRightBottomDiv">'
											+chgDetailContent
										+'</div>'
									+'</div>'
								+'</div>';
			chgDetailContent = "";
		}
		
		prevChgDetailId = map.chgDetailId;
	});

	//수정 이력 있는경우
	if(reqChgDetailList.length > 0){
		$(".reqHistory .middle_box .chgDetailMainDiv").remove();
		$(".reqHistory .middle_box").append(reqChgDetailStr);
	}
	//수정 이력 없는경우 diff 숨김
	else{
		$(".diffDiv").hide();
	}
}

//diff - 변경 정보 보여주기
function fnOptDiff(thisObj){
	var chgDetail_idx = thisObj.getAttribute("num");
	
	var chgDetailInfo = reqChgDetailList[chgDetail_idx];
	
	//수정이력 정보
	var chgDetailNm = chgDetailInfo.chgDetailNm;
	var chgDetailId = chgDetailInfo.chgDetailId;
	var preDetailVal = chgDetailInfo.preDetailVal;
	var chgDetailVal = chgDetailInfo.chgDetailVal;
	
	//빈값인경우 공백 처리
	if(gfnIsNull(preDetailVal)){preDetailVal = "";}
	if(gfnIsNull(chgDetailVal)){chgDetailVal = "";}
	
	//항목 명
	$("div.diffOptInfo > span#diffOptNm").text(chgDetailNm);
	
	//비교 후 결과 값
	var oldVal = "";
	var newVal = "";
	
	//문자열 비교
	var dmp = new diff_match_patch();
	var diff = dmp.diff_main(preDetailVal, chgDetailVal);
	dmp.diff_cleanupEfficiency(diff);
	
	//문자열 비교 값 loop돌면서 class 입히기
	for (var i = 0, j = diff.length; i < j; i++) {
        var arr = diff[i];
        if (arr[0] == 0) {	//변화 없음
            oldVal += arr[1];
            newVal += arr[1];
        } else if (arr[0] == -1) { //이전 값에서 제거
            oldVal += "<span class='text-del'>" + arr[1] + "</span>";
        } else { //변경 값에서 추가
            newVal += "<span class='text-add'>" + arr[1] + "</span>";
        }
    }
	
	//값 출력
	$("div.diffOldDiv > div#diffOldStr").html(oldVal);
	$("div.diffNewDiv > div#diffNewStr").html(newVal);
}

function fnReqWorkView(workList){
	work_grid = new ax5.ui.grid();
				
	//그리드 프레임 호출
	work_grid.setConfig({
		target: $('[data-ax5grid="work-grid"]'),
		showLineNumber: true,
		sortable:true,
		header: {align:"center"},
		columns: [
			{key: "flowNm", label: "작업흐름", width: 80, align: "center"},
			{key: "workStatusNm", label: "작업상태", width: 80, align: "center"},
			{key: "workChargerNm", label: "담당자", width: 80, align: "center"},
			{key: "workAdmContent", label: "작업 지시내용", width: 370, align: "left"},
			{key: "workAdmStDtm", label: "작업 시작예정일자", width: 140, align: "center"},
			{key: "workAdmEdDtm", label: "작업 종료예정일자", width: 140, align: "center"},
			{key: "workContent", label: "작업 내용", width: 370, align: "left"},
			{key: "workStDtm", label: "작업 시작일자", width: 140, align: "center"},
			{key: "workEdDtm", label: "작업 종료일자", width: 140, align: "center"},
			{key: "regDtm", label: "생성일자", width: 140, align: "center"},
			{key: "regUsrNm", label: "생성자", width: 80, align: "center"},
			{key: "modifyDtm", label: "수정일자", width: 140, align: "center"},
			{key: "modifyUsrNm", label: "수정자", width: 80, align: "center"},
		],
		body: {
			align: "center",
			columnHeight: 30
		}
	});
	work_grid.setData(workList);
}

var btnFoldFlag = false;
function fnOnFolding(){
	
	if(btnFoldFlag){
		$(".req_top_div_fold1").css('height','227px');
		$('#foldSpan').attr('class','reqFoldingCursor down');
		$(".req_fold_title_box").css('border-radius','0 0 5px 5px');
		$('#sub_req_div_view').hide();
		$(".req_sub_fold_contents").animate({scrollTop:0},0);	// 스크롤 최상단으로 이동
		$(".req_sub_fold_contents").css('overflow-y','hidden'); // 세로 스크롤 숨기기
		
		btnFoldFlag = false;
	}else{
		$(".req_top_div_fold1").css('height','690px');
		$('#foldSpan').attr('class','reqFoldingCursor up');
		$(".req_fold_title_box").css('border-radius','0 0 5px 5px');
		$('#sub_req_div_view').show();
		$(".req_sub_fold_contents").css('overflow-y','auto');	// 글자가 많을 경우 세로 스크롤 활성화
		
		btnFoldFlag = true;
	}
}

function fnReq4105DataLoad(reqId){
	var popupPrjId = "${prjId}";
	
	//AJAX 설정 
	var ajaxObj = new gfnAjaxRequestAction({"url":"<c:url value='/req/req4000/req4100/selectReq4105DataLoad.do'/>"}
	,{'reqId': reqId, 'dshType':"dsh1000", popupPrjId:popupPrjId});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
    	if(data.errorYN == 'Y'){
    		toast.push(data.message);
    		return false;
    	} 
    	else{
    		processId = data.processId;
    		
    		fnRightFlowDivSetData(data.reqInfoMap.flowId,data.flowList);
    		
    		//'01' 접수가 아닌경우 현재 요구사항의 이전 작업흐름 이력 세팅
    		var reqTopData = fnTopReqChgDivSetData(data.reqChgList);
			
    		//상단 내용 채우기
    		$("#req4105_reqTopDataList").html(reqTopData);
    		
    		reqUsrId = data.reqInfoMap.reqChargerId;
    		if(!gfnIsNull(data.optList)){
    			//추가항목 세팅
    			fnFlowOptDivSetData(data.optList);
    			
	    		//첨부파일 세팅하기 (추가 항목 다음에 실행)
	    		if(!gfnIsNull(data.fileList)){
	    			if(data.fileList.length > 0){
			        	//info 정보 show
			        	$('#dndCancel').show();
			        }
	    			
					//첨부파일 리스트 세팅
				 	$.each(data.fileList, function(idx, fileVo){
				 		fileVo['reqId'] = reqId;
				 		
				 		//파일 atchFileId
				 		var fileAtchFileId = fileVo.atchFileId;
				 		
				 		//삭제버튼 숨기기
				 		var delChk = false;
				 		gfnFileListReadDiv(fileVo,"#fileDiv_"+fileAtchFileId, "req",delChk);
				    });
				}
	    		
    		}
    		//배포 목록 세팅
    		var reqDplList = data.reqDplList;
    		fnReqDplSetting(reqDplList);
    		
    		//결재 목록
    		var reqChkList = data.reqChkList;
    		//결재 목록 있는경우 이력 세팅
    		if(!gfnIsNull(reqChkList)){
    			fnReqChkSetting(reqChkList);
    		}else{
    			
    			$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").hide();
    		}
    		
    		if(!gfnIsNull(flowInfo)){
	    		
   				if(flowInfo.flowWorkCd == "01"){
   	    			//작업 관리 영역 보이기
   	    			$("#req4105_work").show();
   	    			
   	    			add_work_grid = new ax5.ui.grid();
   					
   					//그리드 프레임 호출
   					add_work_grid.setConfig({
   						target: $('[data-ax5grid="add-work-grid"]'),
   						showLineNumber: true,
   						sortable:true,
   						header: {align:"center"},
   						columns: [
   							{key: "flowNm", label: "작업흐름", width: 80, align: "center"},
   							{key: "workStatusNm", label: "작업상태", width: 80, align: "center"},
   							{key: "workChargerNm", label: "담당자", width: 80, align: "center"},
   							{key: "workAdmContent", label: "작업 지시내용", width: 370, align: "left"},
   							{key: "workAdmStDtm", label: "작업 시작예정일자", width: 140, align: "center"},
   							{key: "workAdmEdDtm", label: "작업 종료예정일자", width: 140, align: "center"},
   							{key: "workContent", label: "작업 내용", width: 370, align: "left"},
   							{key: "workStDtm", label: "작업 시작일자", width: 140, align: "center"},
   							{key: "workEdDtm", label: "작업 종료일자", width: 140, align: "center"},
   							{key: "regDtm", label: "생성일자", width: 140, align: "center"},
   							{key: "regUsrNm", label: "생성자", width: 80, align: "center"},
   							{key: "modifyDtm", label: "수정일자", width: 140, align: "center"},
   							{key: "modifyUsrNm", label: "수정자", width: 80, align: "center"},
   						],
   						body: {
   							align: "center",
   							columnHeight: 30
   						}
   					});
   					add_work_grid.setData(data.workList);
   	    		}
    		}
    	}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		toast.push("ERROR STATUS("+status+")<br>"+err);
 	});
	
	//AJAX 전송
	ajaxObj.send();
}

function fnTopReqChgDivSetData(reqChgList){
	var reqTopData = "";
	if(!gfnIsNull(reqChgList)){
		
	//상단 레이아웃에 이전 작업흐름 목록 세팅
		$.each(reqChgList,function(idx, map){
			//각 옵션 체크
			var flowOptionStr = "";
 			//flowNextId 없는경우
			if(gfnIsNull(map.chgFlowNextId) || map.chgFlowNextId == "null"){
				flowOptionStr += "<li class='far fa-stop-circle' title='[종료]최종 완료'></li>";
			} 
			//필수 체크
			if(map.chgFlowEssentialCd == "01"){
				flowOptionStr += '<li class="fa fa-key" title="필수"></li>';
			}
			//결재 체크
			if(map.chgFlowSignCd == "01"){
				flowOptionStr += '<li class="fa fa-file-signature" title="결재"></li>';
			}
			//종료분기 체크
			if(map.chgFlowEndCd == "01"){
				flowOptionStr += '<li class="fa fa-sign-out-alt" title="종료 분기"></li>';
			}
			//작업 체크
			if(map.chgFlowWorkCd == "01"){
				flowOptionStr += '<li class="fa fa-code-branch" title="작업 분기"></li>';
			}
			//리비전 체크
			if(map.chgFlowRevisionCd == "01"){
				flowOptionStr += '<li class="fa fa-code" title="리비전 저장 유무"></li>';
			}
			//배포계획 체크
			if(map.chgFlowDplCd == "01"){
				flowOptionStr += '<li class="fa fa-puzzle-piece" title="배포계획 저장 유무"></li>';
				
				//배포계획 div 만들기
				$("#req4105_dplDivFrame").append(
						'<div class="req4105_dplBox" flowid="'+map.chgFlowId+'">'
							+'<div class="req4105_option_title">배포 계획</div>'
							+'<div class="req4105_option_all">'
							+'	<input type="hidden" name="dplId_'+map.chgFlowId+'" id="dplId_'+map.chgFlowId+'" title="배포 계획" opttype="04" opttarget="03" optflowid="'+map.chgFlowId+'"/>'
							+'	<input type="text" title="배포 계획" class="req4105_input_text req4105_deploy" name="dplNm_'+map.chgFlowId+'" id="dplNm_'+map.chgFlowId+'" modifyset="02" flowid="'+map.chgFlowId+'" readonly="readonly"/>'
							+'</div>'
						+'</div>');
			}
			//허용 역할
			if(map.chgFlowAuthCd == "01"){
				flowOptionStr += '<li class="fa fa-user-shield" title="허용 역할그룹 제한유무"></li>';
			}
			//추가항목 체크
			if(map.chgFlowOptCnt > 0){
				flowOptionStr += '<li class="fa fa-list" title="추가 항목"></li>+'+map.chgFlowOptCnt;
			}
		
			//옵션이 있는경우 div세팅
			//if(!gfnIsNull(flowOptionStr)){
				flowOptionStr = '<div class="req4105_flowOptionDiv">'+flowOptionStr+'</div>';
			//}
			
			//우측 화살표
			var topArrowBox = '';
			
			//최종완료가 아닌경우 
			if(!gfnIsNull(flowInfo.flowNextId) || flowInfo.flowId != map.chgFlowId){
				//화살표 표시
				topArrowBox = '<div class="req4105_flow_topArrowBox"></div>';
			}
			
			//active
			var flowActive = "";
			
			//마지막 idx인경우 active처리
			if(reqChgList.length-1 == idx){
				flowActive = " flowActive";
				
				//배포계획 있는경우 활성화
				if($(".req4105_dplBox[flowid="+map.chgFlowId+"]").length > 0){
					$(".req4105_dplBox[flowid="+map.chgFlowId+"]").show();
				}
			}
			
			reqTopData += 
					'<div class="req4105_flowBox topFlowBox'+flowActive+'" flowid="'+map.chgFlowId+'" workcd="'+map.chgFlowWorkCd+'" revisioncd="'+map.chgFlowRevisionCd+'" authcd="'+map.chgFlowAuthCd+'" dplcd="'+map.chgFlowDplCd+'" style="background-color: '+map.chgFlowTitleBgColor+';color: '+map.chgFlowTitleColor+';" onclick="fnSelectTopFlow(this)">'
					+flowOptionStr
					+'	<div class="req4105_flowBox_title">'+map.chgFlowNm+'</div>'
					+'	<div class="req4105_flowBox_contents" flowid="'+map.chgFlowId+'" style="background-color: '+map.chgFlowContentBgColor+';color: '+map.chgFlowContentColor+';">'
					+new Date(map.chgDtm).format('yyyy-MM-dd HH:mm:ss')
					+'</div>'
					+'</div>'
					+topArrowBox;
		});
	}
	
	return reqTopData;
}

//결재 이력 세팅
function fnReqChkSetting(reqChkList){
	
	$.each(reqChkList,function(idx, map){
		var signRejectCmnt = map.signRejectCmnt;
		if(!gfnIsNull(signRejectCmnt)){
			signRejectCmnt = signRejectCmnt.replace(/&nbsp;&nbsp;&nbsp;&nbsp;/gi,"</br>");
		}
		
		reqViewStr = "";
		//날짜 계산
		var today = new Date(map.signDtm).format('yyyy-MM-dd HH:mm:ss');
		
		//요구사항명
		var reqNm = $("#reqNm").val();
		
		//결재 요청
		if(map.signCd == '01'){
			//요청자 img
			var regUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.regUsrImg;
			
			//결재자 img
			var signUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.signUsrImg;
			
			reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 요청]</div>' 
						+'<div class="req_box_main regUsrImg">'
							+'<img src="'+regUsrImgSrc+'">'
							+'<div>[요청자]</div>'
							+'<div class="historyUsrDiv" title="'+map.regUsrNm+'"><i class="fa fa-user-edit"></i>&nbsp;'+map.regUsrNm+'</div>'
						+'</div>'
						+'<div class="req_box_main arrow" style="height: 10px;"><li class="fa fa-angle-double-down fa-lg"></li></div>'
						+'<div class="req_box_main regUsrImg">'
							+'<img src="'+signUsrImgSrc+'">'
							+'<div>[결재자]</div>'
							+'<div class="historyUsrDiv" title="'+map.signUsrNm+'"><i class="fa fa-user-check"></i>&nbsp;'+map.signUsrNm+'</div>'
						+'</div>';
		}
		//결재 승인
		else if(map.signCd == '02'){
			//결재자 img
			var signUsrImgSrc = "/cmm/fms/getImage.do?fileSn=0&atchFileId="+map.signUsrImg;
			
			reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 승인]</div>' 
						+'<div class="req_box_main signAcceptDiv">'
							+'<img src="'+signUsrImgSrc+'">'
							+'<div class="historyUsrDiv" title="'+map.regUsrNm+'"><i class="fa fa-file-signature"></i>&nbsp;'+map.regUsrNm+'</div>'
						+'</div>'
		}
		//결재 반려
		else if(map.signCd == '03'){
			reqViewStr = '<div class="req_box_title signDiv" style="background-color:'+map.signFlowTitleBgColor+';color:'+map.signFlowTitleColor+'">[결재 반려]</div>'
						+'<div class="req_box_main bottom signRejectDiv" readonly="readonly" title="'+map.signRejectCmnt+'" onclick="signRejectPopupOpen(this)">사유: '+signRejectCmnt+'</div>'
						+'<div class="req_box_main bottom historyUsrDiv signRejectUsrDiv"><i class="fa fa-user-times"></i>'+map.signUsrNm+'</div>';
		}
		
		//하단 요구사항 그리기
		viewStr =	'<div class="req4105_signFrame"><div class="req_main_box req_bottom reqSeq_'+map.signId+'" id="'+map.reqId+'" signflowid="'+map.signFlowId+'" prevflowid="'+map.prevFlowId+'">'
						+'<div class="req_top_box">'
						+today
						+'</div>'
						+'<div class="req_bottom_box">'
						+reqViewStr
						+'</div>'
					+'</div></div>';
					
		//마지막 이력 제외하고 화살표 추가
		if(idx < reqChkList.length-1){
			viewStr += '<div class="req4105_flow_topArrowBox signArrow" signflowid="'+map.signFlowId+'" prevflowid="'+map.prevFlowId+'"></div>';
		}
		$('#req4105_signHistoryDiv').append(viewStr);
	});
	
	//현재 작업흐름과 작업흐름 Id가 다른 결재 이력 감추기
	$("#req4105_signHistoryDiv .req_main_box.req_bottom[prevflowid!="+flowInfo.flowId+"]").hide();
	$("#req4105_signHistoryDiv .req4105_flow_topArrowBox.signArrow[prevflowid!="+flowInfo.flowId+"]").hide();
	
	//결재 이력이 0개인경우 레이아웃 닫기
	if($("#req4105_signHistoryDiv .req_main_box.req_bottom[prevflowid="+flowInfo.flowId+"]").length == 0){
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").hide();
	}else{
		//레이아웃 열기
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").show();
	}
}

//작업흐름 목록 데이터 받아와서 우측 세팅
function fnRightFlowDivSetData(nowFlowId, flowList){
	//우측 내용 세팅 변수
	var reqRightData = "";
	
	//종료 분기 무조건 표시인지 확인
	var flowEndShowChk = false;
	
	//필수인지 체크
	var flowEssentialShowChk = false;
	
	//현재 작업흐름 nextId부터 세팅
	var flowCheck = false; 
	
	//화면에 그린 작업흐름 갯수
	var drawFlowCnt = 0;
	
	//우측 레이아웃에 작업흐름 목록 세팅
	if(!gfnIsNull(flowList)){
		$.each(flowList,function(idx, map){
			//flow Id 체크
			if(!flowCheck){
				if(map.flowId == nowFlowId){
					flowCheck = true;
					
					//작업흐름 정보 담기
					flowInfo = map;
				}
				return true;
			}else{
				//화면에 그린 작업흐름 갯수 추가
				drawFlowCnt++;
			}
			
			
			//필수인경우 loop stop
			if(flowEssentialShowChk){
				//종료분기 존재하는지 체크
				if(flowInfo.flowEndCd == "01"){
					//종료 분기 나타날때까지 loop
					if(!gfnIsNull(map.flowNextId) && map.flowNextId != "null"){
						return true;	
					}
				}else{
	 				return false;
				}
			}
			
			//작업흐름 화살표
			var flowTopArrowBox = '';
			
			//화살표
			if(drawFlowCnt > 1){
				flowTopArrowBox = '<div class="req4105_flowTopArrowBox"></div>';
			}
			
	 		//각 옵션 체크
			var flowOptionStr = "";
 			//flowNextId 없는경우
			if(gfnIsNull(map.flowNextId) || map.flowNextId == "null"){
				flowOptionStr += "<li class='far fa-stop-circle' title='[종료]최종 완료'></li>&nbsp;";
			} 
			//필수 체크
			if(map.flowEssentialCd == "01"){
				flowOptionStr += '<li class="fa fa-key" title="필수"></li>&nbsp;';
			}
			//결재 체크
			if(map.flowSignCd == "01"){
				flowOptionStr += '<li class="fa fa-file-signature" title="결재"></li>&nbsp;';
			}
			//종료분기 체크
			if(map.flowEndCd == "01"){
				flowOptionStr += '<li class="fa fa-sign-out-alt" title="종료 분기"></li>&nbsp;';
			}
			//작업 체크
			if(map.flowWorkCd == "01"){
				flowOptionStr += '<li class="fa fa-code-branch" title="작업 분기"></li>&nbsp;';
			}
			//리비전 체크
			if(map.flowRevisionCd == "01"){
				flowOptionStr += '<li class="fa fa-code" title="리비전 저장 유무"></li>';
			}
			//배포계획 체크
			if(map.flowDplCd == "01"){
				flowOptionStr += '<li class="fa fa-puzzle-piece" title="배포계획 저장 유무"></li>';
			}
			//허용 역할
			if(map.flowAuthCd == "01"){
				flowOptionStr += '<li class="fa fa-user-shield" title="허용 역할그룹 제한유무"></li>';
			}
			//추가항목 체크
			if(map.optCnt > 0){
				flowOptionStr += '<li class="fa fa-list" title="추가 항목"></li>+'+map.optCnt;
			}
		
			//옵션이 있는경우 div세팅
			//if(!gfnIsNull(flowOptionStr)){
				flowOptionStr = '<div class="req4105_flowOptionDiv">'+flowOptionStr+'</div>';
			//}
			
			
	 		reqRightData += '<div class="req4105_flowFrameBox" id="'+map.flowId+'" nextid="'+map.flowNextId+'" flownm="'+map.flowNm+'">'
								+flowTopArrowBox
								+'<div class="req4105_flowBox" style="background-color: '+map.flowTitleBgColor+';color: '+map.flowTitleColor+';">'
								+flowOptionStr
								+'	<div class="req4105_flowBox_title">'+map.flowNm+'</div>'
								+'	<div class="req4105_flowBox_contents" style="background-color: '+map.flowContentBgColor+';color: '+map.flowContentColor+';">'
								+'<span>요구사항: '+map.reqTotalCnt+'</span>'
								+'</div>'
								+'</div>'
							+'</div>';
							
			//작업흐름 종료분기 있는경우 종료 무조건 표시 스위치 온
			if(map.flowEndCd == "01"){
				flowEndShowChk = true;
			}
			
			//작업흐름 필수인경우 다음 작업흐름 skip (종료 분기까지 loop)
			if(map.flowEssentialCd == "01"){
				flowEssentialShowChk = true;
			}
		});
	}
	
	//우측 내용 채우기
  $("#req4105_reqRightDataList").html(reqRightData);
	return true;
}

//작업흐름 Id로 이력 보이기&감추기
function fnSelFlowObjHidden(flowId){

	//현재 작업흐름 결재 이력 보이기
	$("#req4105_signHistoryDiv .req_main_box.req_bottom[prevflowid="+flowId+"]").show();
	$("#req4105_signHistoryDiv .req4105_flow_topArrowBox.signArrow[prevflowid="+flowId+"]").show();
	
	//현재 작업흐름과 작업흐름 Id가 다른 결재 이력 감추기
	$("#req4105_signHistoryDiv .req_main_box.req_bottom[prevflowid!="+flowId+"]").hide();
	$("#req4105_signHistoryDiv .req4105_flow_topArrowBox.signArrow[prevflowid!="+flowId+"]").hide();
	
	//결재 이력이 0개인경우 레이아웃 닫기
	if($("#req4105_signHistoryDiv .req_main_box.req_bottom[prevflowid="+flowId+"]").length == 0){
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").hide();
	}else{
		//레이아웃 열기
		$(".req4105_sign_TitleBox, #req4105_signHistoryDiv").show();
	}

	//현재 작업흐름 추가 항목 보이기
	$("#req4105_add_option .req4105_option_title[optflowid="+flowId+"]").show();
	$("#req4105_add_option .req4105_option_half[optflowid="+flowId+"]").show();
	$("#req4105_add_option .req4105_option_all[optflowid="+flowId+"]").show();
	
	//현재 작업흐름과 작업흐름 Id가 다른 항목 감추기
	$("#req4105_add_option .req4105_option_title[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_half[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_all[optflowid!="+flowId+"]").hide();
	
	//마지막 항목이 hlaf이고 홀수인경우 항목 채우기_마지막 작업흐름 Id
	if($("#req4105_add_option > div").eq($("#req4105_add_option > div").length-1).hasClass("req4105_option_half")){
		if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").length%2 == 1){
			var descClass = '';
			var descStyle  = '';
			//desc인경우 class 추가
			if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").hasClass("req4105_desc")){
				descClass = " req4105_desc";
				descStyle = ' style="height:100px"   ';
			}
			
			$("#req4105_add_option").append('<div class="req4105_option_title'+descClass+'" optflowid="'+flowId+'"  '+descStyle+' ></div><div class="req4105_option_half'+descClass+'" optflowid="'+flowId+'"  '+descStyle+' ></div>');
		}
	}

	
}

function fnSelectTopFlow(thisObj){
	/*
	if(signWaitChk){
		return true;
	}
*/
	//active
	$(".req4105_flowBox.topFlowBox.flowActive").removeClass("flowActive");
	$(thisObj).addClass("flowActive");
	
	var flowId = $(thisObj).attr("flowid");
	var workCd = $(thisObj).attr("workcd");
	var revisionCd = $(thisObj).attr("revisioncd");
	var dplcd = $(thisObj).attr("dplcd");
	
	fnSelFlowObjHidden(flowId);


	//클릭 작업흐름에 작업이 있는 경우 show
	if(workCd == "01"){
		$("#req4105_work").show('fast',function(){
			//선택 작업흐름과 현재 작업흐름이 다른 경우 버튼 감추기
			if(flowId != flowInfo.flowId){
				$(".req4105_work_btn").hide();
			}
			
			add_work_grid = new ax5.ui.grid();
			
			//그리드 프레임 호출
			add_work_grid.setConfig({
				target: $('[data-ax5grid="add-work-grid"]'),
				showLineNumber: true,
				sortable:true,
				header: {align:"center"},
				columns: [
					{key: "flowNm", label: "작업흐름", width: 80, align: "center"},
					{key: "workStatusNm", label: "작업상태", width: 80, align: "center"},
					{key: "workChargerNm", label: "담당자", width: 80, align: "center"},
					{key: "workAdmContent", label: "작업 지시내용", width: 370, align: "left"},
					{key: "workAdmStDtm", label: "작업 시작예정일자", width: 140, align: "center"},
					{key: "workAdmEdDtm", label: "작업 종료예정일자", width: 140, align: "center"},
					{key: "workContent", label: "작업 내용", width: 370, align: "left"},
					{key: "workStDtm", label: "작업 시작일자", width: 140, align: "center"},
					{key: "workEdDtm", label: "작업 종료일자", width: 140, align: "center"},
					{key: "regDtm", label: "생성일자", width: 140, align: "center"},
					{key: "regUsrNm", label: "생성자", width: 80, align: "center"},
					{key: "modifyDtm", label: "수정일자", width: 140, align: "center"},
					{key: "modifyUsrNm", label: "수정자", width: 80, align: "center"},
				],
				body: {
					align: "center",
					columnHeight: 30
				}
			});

			 fnWorkRefresh(flowId);
		});
	}else{
		$("#req4105_work").hide();
	}
	
	//클릭 작업흐름에 배포계획이 있는 경우 show
	if(dplcd == "01"){
		$(".req4105_dplBox").hide();
		$(".req4105_dplBox[flowid="+flowId+"]").show('fast');
	}else{
		$(".req4105_dplBox").hide();
	}
}


//작업 그리드 조회
function fnWorkRefresh(flowId){
	//flowId 없는경우 현재 flowId넣기
	if(!gfnIsNull(flowInfo)){
		if(gfnIsNull(flowId)){
			flowId = flowInfo.flowId;
		}
	
		var param = { 'reqId': reqId,'processId': processId, 'flowId': flowId} ; 
	
		//AJAX 설정
		var ajaxObj = new gfnAjaxRequestAction(
				{"url":"<c:url value='/req/req4000/req4400/selectReq4400ReqWorkListAjax.do'/>","loadingShow":false},
				param );
		//AJAX 전송 성공 함수
		ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			
			//에러 없는경우
			if(data.errorYN != "Y"){
				add_work_grid.setData(data.workList);
			}else{
				toast.push(data.message);
			}
		});
		
		//AJAX 전송 오류 함수
		ajaxObj.setFnError(function(xhr, status, err){
			data = JSON.parse(data);
			jAlert(data.message, "알림");
		});
		//AJAX 전송
		ajaxObj.send();
	}
}

//선택 작업흐름 추가 항목 세팅
function fnFlowOptDivSetData(optList){
	var flowOptData = '';
	if(!gfnIsNull(optList)){
		//date 세팅 array
		var optDateDataArr = [];
		
		//공통코드 세팅을 위한 값
		var mstCdStrArr = '';
		var selectObjList = [];
		var arrComboType = [];
		
		//half 갯수 체크
		var halfCnt = 0;
		//hlaf 갯수 및 desc 체크
		var halfDivDesc = false;
		//hlaf 마지막 flowId
		var hlafFlowId = "";
		
		//마지막 작업흐름 Id
		var lastFlowId = null;
		
		//항목 세팅
		$.each(optList,function(idx, map){
			//열 넓이
			var optionWidthSize = '';
			//전체인경우 title에 clear:both
			var optionTitleClass = '';
			
			//열 넓이에 따른 class 세팅
			if(map.itemRowNum == "01"){ //부분
				optionWidthSize = "req4105_option_half";
				//현재 항목이 textarea이고 이전 항목이 부분인경우 빈항목 추가
				if(map.itemType == "02" && halfCnt%2 == 1){
					flowOptData += '<div class="req4105_option_title" optflowid="'+hlafFlowId+'"></div>'
							+'<div class="req4105_option_half" optflowid="'+hlafFlowId+'"></div>';
					halfCnt++;
				}
				
				halfCnt++;
				hlafFlowId = map.flowId;
			}else if(map.itemRowNum == "02"){ //전체
				//전체 추가 전에 hlaf가 홀수라면 강제 영역 추가
				if(halfCnt%2 == 1){
					//이전 항목이 desc인경우 desc추가
					if(halfDivDesc){
						optionWidthSize += " req4105_desc";
						optionTitleClass += " req4105_desc";
					}
					
					flowOptData += '<div class="req4105_option_title'+optionTitleClass+'" optflowid="'+hlafFlowId+'"></div>'
								+'<div class="req4105_option_half'+optionWidthSize+'" optflowid="'+hlafFlowId+'"></div>';
					
					halfCnt++;
				}
				optionWidthSize = "req4105_option_all";
				optionTitleClass = " req4105_clear";
			}
			
			//데이터 내용
			var flowOptContentData = '';
			
			var itemValue = '';
			var optTarget = "02";
			var optReadOnlyChk = true;
			var optReadOnly = '';
			var optAddClass = '';
			halfDivDesc = false;
			
			//값 null처리
			if(!gfnIsNull(map.itemValue)){
				itemValue = map.itemValue;
			}
			
			//추가 항목 분류에 따른 데이터 내용 처리
			if(map.itemCode == "01"){ //기타
				if(map.itemType == "01"){ //text
					if(optReadOnlyChk){
						optReadOnly = 'readonly="readonly"';
						optAddClass += ' req4105_readonly';
					}
				
					flowOptContentData = '<input type="text" class="req4105_input_text'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" maxlength="'+map.itemLength+'" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" value="'+itemValue+'" '+optReadOnly+'/>';
					
				}else if(map.itemType == "02"){ //textarea
					if(optReadOnlyChk){
						optReadOnly = 'readonly="readonly"';
						optAddClass += ' req4105_readonly';
					}
				
					//<br>, </br>바꾸기
					itemValue = itemValue.replace(/<br>/gi,"\n").replace(/<\/br>/gi,"\n");
					flowOptContentData = '<textarea class="req4105_textarea'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" '+optReadOnly+'>'+itemValue+'</textarea>';
					
					//textarea height높게
					optionWidthSize += " req4105_desc";
					optionTitleClass += " req4105_desc";
					
					halfDivDesc = true;
				}else if(map.itemType == "03"){ //checkbox
					if(optReadOnlyChk){
						optReadOnly = 'disabled="disabled"';
						optAddClass += ' req4105_readonly';
					}
					var optChkVal = ""
					//체크박스 값 체크
					if(map.itemValue == "01"){
						optChkVal = " checked";
					}
					
					flowOptContentData = '<div class="req4105_chk"><input type="checkbox" class="req4105_input_check'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" opttarget="'+optTarget+'"'+optChkVal+' '+optReadOnly+'/><label></label></div>';
				}else if(map.itemType == "04" || map.itemType == "05"){ //date
					if(optReadOnlyChk){
						optReadOnly = 'disabled="disabled"';
						optAddClass += ' req4105_readonly';
					}
					flowOptContentData = '<input type="text" class="req4105_input_date'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" readonly="readonly" opttarget="'+optTarget+'" value="'+itemValue+'" '+optReadOnly+'/>';
					
				}
			}else if(map.itemCode == "02"){ //공통코드 (selectBox)
				if(optReadOnlyChk){
						optReadOnly = 'disabled="disabled"';
						optAddClass += ' req4105_readonly';
				}
				flowOptContentData = '<select type="text" class="req4105_select'+optAddClass+'" title="'+map.itemNm+'" id="'+map.itemId+'" name="'+map.itemId+'" optflowid="'+map.flowId+'" opttype="02" cmmcode="'+map.itemCommonCode+'" opttarget="'+optTarget+'" OS="'+itemValue+'" '+optReadOnly+'></select>';
				
				//이미 추가된 공통코드가 있는 경우 '|' 붙임
				if(mstCdStrArr.length > 0){
					mstCdStrArr += "|"
				}
				
				//공통코드 세팅
				mstCdStrArr += map.itemCommonCode;
				selectObjList.push(map.itemId);
				arrComboType.push("OS");
			}else if(map.itemCode == "03"){ //첨부파일
				//첨부파일 height높게
				optionWidthSize += " req4105_file";
				optionTitleClass += " req4105_file";
				
				var fileUploadBtnStr = '';
				var fileUploadWidth = '';
				var fileUploadDesc = '';
				
				//수정 불가능한경우 width100%
				fileUploadWidth = ' widthAll';
				
				//배열에 수정불가 추가
				readonlyFileIdList.push(map.itemValue);
					
				flowOptContentData = '<div class="uploadOverFlow optFileDiv'+fileUploadWidth+'" id="fileDiv_'+map.itemValue+'" fileid="'+map.itemValue+'" onclick="fnOptFileUpload(this)">'
										+fileUploadDesc+'</div>'
										+fileUploadBtnStr;
				
			}else if(map.itemCode == "04"){ //담당자
				if(optReadOnlyChk){
					optReadOnly = 'readonly="readonly"';
					optAddClass += ' req4105_readonly';
				}
				flowOptContentData = '<input type="text" name="'+map.itemId+'" id="'+map.itemId+'" title="'+map.itemNm+'" opttype="03" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" value="'+itemValue+'" style="display:none;"/>'
									+'<input type="text" class="req4105_input_text req4105_optCharger'+optAddClass+'" title="'+map.itemNm+'" name="'+map.itemId+'Nm" id="'+map.itemId+'Nm" modifyset="02" value="'+$.trim(map.itemValueNm)+'"/>';
									
				
			}else if(map.itemCode == "05"){ //분류
				if(optReadOnlyChk){
					optReadOnly = 'readonly="readonly"';
					optAddClass += ' req4105_readonly';
				}
				flowOptContentData = '<input type="text" name="'+map.itemId+'" id="'+map.itemId+'" title="'+map.itemNm+'" opttype="04" optflowid="'+map.flowId+'" opttarget="'+optTarget+'" value="'+itemValue+'" style="display:none;"/>'
									+'<input type="text" class="req4105_input_text req4105_cls'+optAddClass+'" title="'+map.itemNm+'" name="'+map.itemId+'Nm" id="'+map.itemId+'Nm" modifyset="02" value="'+$.trim(map.itemValueNm)+'" readonly="readonly"/>';
									
				
			}
			
			//항목명
			var optNm = map.itemNm;
			
			//필수 값인경우 '*' 추가
			if(map.itemEssentialCd == '01'){
				optNm += '&nbsp;(*)';
			}
			
			flowOptData += '<div class="req4105_option_title'+optionTitleClass+'" optflowid="'+map.flowId+'">'
								+optNm
								+'</div>'
								+'<div class="'+optionWidthSize+'" optflowid="'+map.flowId+'">'
								+flowOptContentData
								+'</div>';
								
			//마지막 작업흐름 Id 세팅
			lastFlowId = map.flowId
		});
	}

	//추가 항목 내용 채우기
	$("#req4105_add_option").html(flowOptData);
	
	//현재 작업흐름과 작업흐름 Id가 다른 항목 가리기
	var flowId = flowInfo.flowId; 
	$("#req4105_add_option .req4105_option_title[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_half[optflowid!="+flowId+"]").hide();
	$("#req4105_add_option .req4105_option_all[optflowid!="+flowId+"]").hide();
	
	
	//마지막 항목이 hlaf이고 홀수인경우 항목 채우기_마지막 작업흐름 Id
	if($("#req4105_add_option > div").eq($("#req4105_add_option > div").length-1).hasClass("req4105_option_half")){
		if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").length%2 == 1){
			var descClass = '';
			//desc인경우 class 추가
			if($("#req4105_add_option > div.req4105_option_half[optflowid="+flowId+"]").hasClass("req4105_desc")){
				descClass = " req4105_desc";
			}
			$("#req4105_add_option").append('<div class="req4105_option_title'+descClass+'" optflowid="'+lastFlowId+'"></div><div class="req4105_option_half'+descClass+'" optflowid="'+lastFlowId+'"></div>');
		}
	}
	
	//date 캘린더 세팅
	if(!gfnIsNull(optDateDataArr)){
		$.each(optDateDataArr,function(idx, map){
			gfnCalSet('YYYY-MM-DD',map);
		});
	}
	
	/* 	
	*	공통코드 가져올때 한번 트랜잭션으로 여러 코드 가져와서 셀렉트박스에 세팅하는 함수(사용 권장)
	* 	1. 공통 대분류 코드를 순서대로 배열 담기(문자열)
	*	2. 사용구분 저장(Y: 사용중인 코드만, N: 비사용중인 코드만, 그 외: 전체)
	*	3. 공통코드 적용할 select 객체 직접 배열로 저장
	* 	4. 공통코드 가져와 적용할 콤보타입 객체 배열 ( S:선택, A:전체(코드값 A 세팅한 조회조건용), N:전체, E:공백추가, 그 외:없음 )
	*	5. 동기 비동기모드 선택 (true:비동기 통신, false:동기 통신)
	*	마스터 코드 = REQ00001:요구사항 타입, REQ00002:중요도 , CMM00001:
	*/
	if(!gfnIsNull(selectObjList)){
		var arrObj = [];
		
		//공통코드 object 세팅
		$.each(selectObjList,function(idx, map){
			arrObj.push($("#"+map+""));
		});
		
		
		var strUseYn = 'Y';
		gfnGetMultiCommonCodeDataForm(mstCdStrArr, strUseYn, arrObj, arrComboType , false);
	}
}



//리비전 그리드 목록 세팅
function fnReqSvnRevisionViewGridSet(){
	revisionGrid = new ax5.ui.grid();
	 
	revisionGrid.setConfig({
        target: $('[data-ax5grid="svn-revision-grid"]'),
        sortable:false,
        showRowSelector: false,
        multipleSelect: false  ,
        header: {align:"center" },
        columns: [
			{key: "svnRepNm", label: "Repository", width: 100, align: "left"},
			{key: "revisionNum", label: "Revision", width: 80, align: "right"},
			{key: "revisionComment", label: "리비젼 내용", width: 200, align: "left"},
			{key: "svnRepUrl", label: "Repository Url", width: 200, align: "left"}
        ],
        body: {
            align: "center",
            columnHeight: 30,
            onClick: function () {
            	var selItem = revisionGrid.list[this.doindex];  
            	svnRepId = selItem.svnRepId;
            	revisionNum = selItem.revisionNum;
            	fnSearchFileDirTree(revisionNum , svnRepId);
            }
        }
	
	});
	fnSelectReq4100RevisionList();
	fnFileView();
}

//배포목록 그리드 목록 세팅
function fnReqDeployViewGridSet(){
	reqDplGrid = new ax5.ui.grid();
	 
	reqDplGrid.setConfig({
        target: $('[data-ax5grid="reqDeploy-grid"]'),
        sortable:false,
        showRowSelector: false,
        multipleSelect: false  ,
        header: {align:"center" },
        columns: [
            {key: "dplStsNm", label: "배포 상태", width: 120, align: "center"}
            ,{key: "dplNm", label: "배포 명", width: 180, align: "center"}
            ,{key: "processNm", label: "프로세스 명", width: 180, align: "center"}
            ,{key: "flowNm", label: "작업흐름 명", width: 180, align: "center"}
        ],
        body: {
            align: "center",
            columnHeight: 30,
            onClick: function () {
            	var selItem = this.item;
            	
            	//선택 요구사항 정보
            	reqDplSelPrjId = selItem.prjId;
            	reqDplSelDplId = selItem.dplId;
            	fnInJenkinsGridListSet(reqDplSelPrjId, reqDplSelDplId);
            	
            	$(".req4104_reqDep_maskBox").hide();
            }
        }
	
	});
	
	//Job 목록 Grid
	reqDplJobGrid = new ax5.ui.grid();
	 
    reqDplJobGrid.setConfig({
        target: $('[data-ax5grid="reqDeployJob-grid"]'),
        sortable:false,
        showRowSelector: false,
        multipleSelect: false  ,
        header: {align:"center"},
        columns: [
            {key: "jenNm", label: "JOB 명", width: 150, align: "center"},
			{key: "bldNum", label: "빌드 번호", width: 80, align: "right"},
			{key: "bldSts", label: "빌드 결과", width: 100, align: "center"}
        ],
        body: {
                align: "center",
                columnHeight: 30,
                onClick: function () {
                	fnSearchJobList(this.item.jenNm,reqDplSelPrjId, reqDplSelDplId);
                	reqDplJobGrid.select(this.doindex, {selected: true});
                },
				onDBLClick: function () {
					//빌드번호가 있는 경우에만
					if(!gfnIsNull(this.item.bldNum)){
						var data = { "name": this.item.jenNm , "number" : this.item.bldNum, "prjId": reqDplSelPrjId, "dplId": reqDplSelDplId}; 
						gfnLayerPopupOpen("/dpl/dpl2000/dpl2000/selectDpl2004BulidConsoleView.do", data, "1000", "800",'auto');
					}
				}
            }
    });
    //빌드목록 세팅
    fnInJenkinsBuildListSetting();
    
	reqDplGrid.setData(reqDplList);
	$(".req4104_reqDep_maskBox").show();
}

//빌드 목록 세팅
function fnInJenkinsBuildListSetting(){
	reqDplNumGrid = new ax5.ui.grid();
 
	reqDplNumGrid.setConfig({
           target: $('[data-ax5grid="reqDeployJobNum-grid"]'),
           sortable:false,
           showRowSelector: false,
           multipleSelect: false  ,
        	header: {align:"center", selector : false  },
           columns: [
			{key: "number", label: "빌드 번호", width: 195, align: "right"},
           ],
           body: {
               align: "center",
               columnHeight: 30,
               onDBLClick: function () {
               	var data = { "name": this.item.jobname , "number" : this.item.number, "prjId": reqDplSelPrjId, "dplId": reqDplSelDplId}; 
        		gfnLayerPopupOpen("/dpl/dpl2000/dpl2000/selectDpl2004BulidConsoleView.do", data, "1000", "800",'auto');
               }
           }
       });
}
//그리드 데이터 넣는 함수
function fnInJenkinsGridListSet(prjId,dplId){
	
    //AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JobBuildList.do'/>","loadingShow":false,async:false}
			, {"prjId": prjId, "dplId": dplId });
	//AJAX 전송 성공 함수
	var list = [];
	ajaxObj.setFnSuccess(function(data){
			data = JSON.parse(data);
			var list = data.jobList;
		
		   	reqDplJobGrid.setData(list);
		   	if(list.length>0){
		   		fnSearchJobList(list[0].jenNm,prjId,dplId);
		   		reqDplJobGrid.select(0, {selected: true});
		   	}
		  
		});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		//세션이 만료된 경우 로그인 페이지로 이동
          	if(status == "999"){
          		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
       		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
       		return;
          	}
	});
	
	//AJAX 전송
	ajaxObj.send();
}
function fnSearchJobList(jobname,prjId,dplId){
	gfnShowLoadingBar(true);
	//AJAX 설정
	var ajaxJobObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/dpl/dpl2000/dpl2000/selectDpl2000JenkinsBuildListAjax.do'/>","loadingShow":false}
			,{"jobname" : jobname , "prjId": prjId, "dplId": dplId});
	//AJAX 전송 성공 함수
	ajaxJobObj.async = false;
	ajaxJobObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		if(data.errorYn == "Y"){
				$("#req4104_dpl_mask_frmae").html(data.consoleText);
				$("div#req4104_dpl_mask_frmae").show();
				//빌드목록 세팅
    			fnInJenkinsBuildListSetting();
		}
		else{
			$("div#req4104_dpl_mask_frmae").hide();
			$(data.buildlist).each(function(index, item){
				item['jobname'] = jobname;
			})
			
			reqDplNumGrid.setData(data.buildlist);
		}
		gfnShowLoadingBar(false);
	});
	
	//AJAX 전송 오류 함수
	ajaxJobObj.setFnError(function(xhr, status, err){
		gfnShowLoadingBar(false);
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
 	});
	//AJAX 전송
	ajaxJobObj.send();
}
function fnSelectReq4100RevisionList(){
	//AJAX 설정 
	var reqId = '${reqId}';
	//AJAX 설정
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/req/req4000/req4100/selectReq4100RevisionList.do'/>","loadingShow":true}
			,{'reqId': reqId});
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		data = JSON.parse(data);
		revisionGrid.setData(data.list);
	
   	    
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		//세션이 만료된 경우 로그인 페이지로 이동
       	if(status == "999"){
       		alert('세션이 만료되어 로그인 페이지로 이동합니다.');
    		document.location.href="<c:url value='/cmm/cmm4000/cmm4000/selectCmm4000View.do'/>";
    		return;
       	}
	});
	
	//AJAX 전송
	ajaxObj.send();
}

function fnSearchFileDirTree(revision,svnRepId){
	//AJAX 설정
	
	var ajaxObj = new gfnAjaxRequestAction(
			{"url":"<c:url value='/cmm/cmm1000/cmm1400/selectCmm1400FileDirAjaxList.do'/>","loadingShow":false}
			,{"revision" : revision , "svnRepId" :svnRepId});
	
	
	//AJAX 전송 성공 함수
	ajaxObj.setFnSuccess(function(data){
		
		data = JSON.parse(data);
    
		//오류
		if(data.errorYn == "Y"){
			$("#svnMaskBox").html(data.consoleText);
			$("#svnMaskBox").show();
		}else{
	    	$('.ok_document').html('');
	    	var beforeFileDiv = $('.ok_document').children('div');    	
	    	$('.ok_document').html('');
	    	
	    	toast.push(data.message);
	    	
	    	// zTree 설정 
		    var setting = {
		        data: {
		        	key: {
						name: "name"
					},
		            simpleData: {
		                enable: true,
		                idKey: "currentKey",
						pIdKey: "parentKey",
						rootPId: "Root"
		            }
		        },
				callback: {
					onClick: function(event, treeId, treeNode){
						//우측 메뉴 정보
						getSVNRevisionFileList(treeNode,treeList);
					}
				},
				view : {
					fontCss: function(treeId, treeNode){
						return {};
					},
					showIcon : function(treeId, treeNode) {
						if(typeof zTree != "undefined" && treeNode.level != 3 && !treeNode.isParent){
							treeNode.isParent = true;
							zTree.refresh();	
						}
						return true;
					}
				}
		    };
	    	
		    treeList = data.baseDocList;
		    if(data.MSG_CD == "SVN_OK" ){
		    	var dirList = getDirectory(treeList);
			    // zTree 초기화
			    zTree = $.fn.zTree.init($("#fileDirJson"), setting, dirList);
			    
			    if(dirList.length >0){
			    	getSVNRevisionFileList(null,treeList);
			    	zTree.expandAll(true);
			    }	
			    $(".req4104_svn_maskBox").hide();
		    }else{
		    	$('#svnMaskBox').html("Repository에 접근할수 없습니다.");
		    	$(".req4104_svn_maskBox").show();
		    }
		    
		    
		  //폴더의 계층구조가 3단계가 아니면  tree전체 펼침 시에 일회적 동작 안함(좋은방법같진않고 임시방편 추후개선)
			//zTree.expandAll(false);
		}
	});
	
	//AJAX 전송 오류 함수
	ajaxObj.setFnError(function(xhr, status, err){
		data = JSON.parse(data);
		jAlert(data.message, "알림창");
 	});
	//AJAX 전송
	ajaxObj.send();
}

function getDirectory(dataList){
	var returnList = [];

	$(dataList).each(function(index, item){
		if(item.kind=="dir"){
			returnList.push(item);
		}	
	})
	
	return returnList;
}

	
function getSVNRevisionFileList(node,dataList){
	var returnList = [];
	
	if(node == null){
		$(dataList).each(function(index, item){
			if(item.kind=="file"){
				returnList.push(item);	
			}	
		})
	}else{
		var selectKey = node.currentKey;
		$(dataList).each(function(index, item){
			if(item.kind=="file"){
				if(item.currentKey.indexOf(selectKey)==0){
					returnList.push(item);	
				}				
			}	
		})
	}
	
	secondGrid.setData(returnList);
	return returnList;
}


function fnFileView(){
	secondGrid = new ax5.ui.grid();

	secondGrid.setConfig({
        target: $('[data-ax5grid="second-svnpop-grid"]'),
        sortable:false,
        header: {align:"center"},
        columns: [
			{key: "type", label: "type", width: 100, align: "left"},
			{key: "name", label: "name", width: 300, align: "left"},
			{key: "path", label: "path", width: 600, align: "left"}
			
        ],
        body: {
            align: "center",
            columnHeight: 30,
            onDBLClick:function(){
               	
            	var data = {"revision" : revisionNum ,"path": this.item.path,"name": this.item.name
            			, "svnRepId" : svnRepId}; 
				gfnLayerPopupOpen("/cmm/cmm1000/cmm1400/selectCmm1401FileContentView.do", data, "1200", "780",'auto');
            }
        }
       
    });
}

/**
 * 배포
 */
 //배포 데이터 세팅
function fnReqDplSetting(reqDplList){
	$.each(reqDplList,function(idx, map){
		$('#dplId_'+map.flowId).val(map.dplId);
		$('#dplNm_'+map.flowId).val(map.dplNm);
	});
}
</script>

<div id="reqDetailDiv">
	<div class="popup_title_box"><span>[${targetprjGrpNm} > ${targetPrjNm}]</span> : <span id="upupReqClsNm"></span><span id="upReqClsNm"></span><span id="reqClsNm"></span></div>
	<input type="hidden" name="atchFileId" id="atchFileId"/>
	<input type="hidden" name="cbAtchFileId" id="cbAtchFileId"/>
	<input type="hidden" name="reqChargerId" id="reqChargerId"/>
	<div class="top_box">
	
		<div class="req_top_div_fold1_line">
			<div class="req_top_div_fold1 req_left_div" style="position: absolute; margin: 0px; z-index: 90000;">
	
				<!-- 좌측 요구사항 요청 정보 DIV -->
				<div class="req_sub_title">
					요청 정보
				</div>
				<div class="req_sub_fold_contents">
					<div class="sub_line">
						<div class="req_sub_left">접수유형</div>
						<div class="req_sub_right" id="reqNewTypeNm"></div>
					</div>
					<div class="sub_line">
						<div class="req_sub_left">요청 제목</div>
						<div class="req_sub_right" id="reqNm"></div>
					</div>
					<div class="sub_line">
						<div class="req_sub_left">요청일</div>
						<div class="req_sub_right" id="reqDtm"></div>
					</div>
					<div class="sub_line">
						<div class="req_sub_left">요청자</div>
						<div class="req_sub_right" id="reqUsrNm"></div>
					</div>
					<div class="sub_line">
						<div class="req_sub_left">소속</div>
						<div class="req_sub_right" id="reqUsrDeptNm"></div>
					</div>
					<div class="sub_line">
						<div class="req_sub_left">E-mail</div>
						<div class="req_sub_right" id="reqUsrEmail"></div>
					</div>
					<div id="sub_req_div_view" >
						<div class="sub_line">
							<div class="req_sub_left">시작일자</div>
							<div class="req_sub_right" id="reqStDtm"></div>
						</div>
						
						<div class="sub_line">
							<div class="req_sub_left">작업 시작 예정 일자</div>
							<div class="req_sub_right" id="reqStDuDtm"></div>
						</div>
						
						<div class="sub_line">
							<div class="req_sub_left">종료일자</div>
							<div class="req_sub_right" id="reqEdDtm"></div>
						</div>
						
						<div class="sub_line">
							<div class="req_sub_left">작업 종료 예정 일자</div>
							<div class="req_sub_right" id="reqEdDuDtm"></div>
						</div>
		
						<div class="sub_line">
							<div class="req_sub_left">진척률(%)</div>
							<div class="req_sub_right" id="reqCompleteRatio"></div>
						</div>
		
						<div class="sub_line">
							<div class="req_sub_left">담당자</div>
							<div class="req_sub_right" id="reqChargerNm"></div>
						</div>
		
						<div class="sub_line">
							<div class="req_sub_left">예상 FP</div>
							<div class="req_sub_right" id="reqExFp"></div>
						</div>
		
						<div class="sub_line">
							<div class="req_sub_left">최종 FP</div>
							<div class="req_sub_right" id="reqFp"></div>
						</div>
		
						<div class="sub_line">
							<div class="req_sub_left">요구사항 분류</div>
							<div class="req_sub_right" id="reqClsNm"></div>
						</div>
						<div class="sub_line">
							<div class="req_sub_left">시스템 구분</div>
							<div class="req_sub_right" id="sclNm"></div>
						</div>
						<div class="sub_line">
							<div class="req_sub_left">요구사항 유형</div>
							<div class="req_sub_right" id="reqTypeNm"></div>
						</div>
						<div class="sub_line">
							<div class="req_sub_left">성능 개선 활동 여부</div>
							<div class="req_sub_right" id="piaNm"></div>
						</div>
						<div class="sub_line">
							<div class="req_sub_left">투입인력</div>
							<div class="req_sub_right" id="labInp"></div>
						</div>
					</div>					
									
				</div>
				
				<div class="req_fold_title_box" id="btnFolding" > 
					<div class="reqFoldingBtn"><span id="foldSpan" class="reqFoldingCursor down" folding="reqDetail"></span></div>
				</div>
				
			</div>
		</div>		
		
		<div class="req_top_div req_middle_div">
			<!-- 중간 요구사항 설명, 첨부파일 DIV -->
			<div class="req_sub_title">
				요청 내용
			</div>
			<div class="req_sub_desc">
				<textarea id="reqDesc" readonly="readonly"></textarea>
			</div>
			<div class="req_sub_title">
				<div class="req_sub_left_title">
						<span onclick="fnZipDownload()" id="btn_select_zipDown">전체 다운로드</span>
				</div>
				<div class="req_sub_middle_title">
					첨부파일
				</div>
				<!-- <div class="req_sub_right_title">
						<span onclick="fnOslUploadClick()" id="btn_insert_fileSelect">
							<input type="file" style="display: none" id="oslDocFileUpload" name="oslDocFileUpload"  multiple="multiple"/>첨부파일 업로드
						</span>
				</div> -->
			</div>
			<div class="req_sub_fileList">
				<div id="fileListDiv" class="uploadOverFlow">
				</div>
			</div>
		</div>
		<div class="req_top_div req_right_div">
			<!-- 우측 요구사항 코멘트 DIV -->
			<div class="req_sub_title">
				 코멘트 목록 <span id="cmntAllCnt"></span>
			</div>
			<div class="req_sub_cmtList" id="reqCmntListDiv_Detail">
			</div>
			<c:if test="${(viewer ne 'Y')}">
			<div class="req_sub_inCmt">
				<form id="reqCmntFrm" name="reqCmntFrm" method="post">
					<textarea class="cmntInsert" id="reqCmnt" tabindex="1"></textarea>
						<div class="button_normal2 btn_nm" id="btn_insert_reqCmnt"
							onclick="fnPopInsertReqCmntInfo()" tabindex=2
							onkeydown="gfnEnterAction('fnPopInsertReqCmntInfo()')"
							style="height: 52px; line-height: 46px;">등록</div>
						<!-- 코멘트 저장시 필요한 key 값 히든 -->
					<input type="hidden" id="prjId" name="prjId" value="${prjId}"/>
					<input type="hidden" id="reqId" name="reqId" value="${reqId}" />
				</form>
			</div>
			</c:if>
		</div>
	</div>
	<div class="footerTab_content">
		<div class="content_child reqAddOpt ">
			<div class="slide_box_title">추가항목 정보 </div>
			<div class="middle_box">
				<div class="slideChg_box reqAddOptFrame">
					<div class=" req4105_req_topBox" id="req4105_reqTopDataList" title="작업흐름 변경이력">
					</div>
					<div id="req4105_dplDivFrame"></div>
					<div class="req4105_sign_TitleBox">
						결재 이력
					</div>
					<div class="border req4105_signHistoryDiv" id="req4105_signHistoryDiv">
					</div>
					<div class="req4105_optionDiv" id="req4105_work">
						<div class="req4105_work_btnBox">
						작업 관리
						</div>
						<div class="border req4105_work_frame">
							<div data-ax5grid="add-work-grid" data-ax5grid-config="{}" style="height: 200px;"></div>	
						</div>
					</div>
					<div class="req4105_optionDiv" id="req4105_add_option">
						<!-- 작업흐름 입력해야하는 추가 항목 -->
					</div>
					
				</div>
			</div>
		</div>
		<div class="content_child reqwork">
			<div class="slide_box_title">작업 내용 </div>
			<div class="middle_box">
				<div class="slideChg_box" id="reqWork">
					<div class="req4104_work_frame">
						<div data-ax5grid="work-grid" data-ax5grid-config="{}" style="height: 360px;"></div>	
					</div>
				</div>
			</div>
		</div>
		<div class="content_child flowChg footerTab_active">
			<div class="slide_box_title">
				작업흐름 변경이력 <span id="sprEndDtStr"></span></div>
			<div class="middle_box">
				<div class="slideChg_box" id="reqChgViewDiv">
				</div>
			</div>
		</div>
		<div class="content_child reqHistory">
			<div class="slide_box_title">요구사항 수정이력</div>
			<div class="middle_box">
				<!-- 요구사항 수정 이력 내용 -->
				<div class="diffDiv">
					<div class="diffOptInfo">
						<li class="fa fa-list"></li>&nbsp;<span id="diffOptNm"></span>
					</div>
					<div class="diffOldDiv">
						<div class="diffTitleDiv diffAdd">변경 <span class="text-del">전</span> 내용</div>
						<div id="diffOldStr" class="diffContentDiv"></div>
					</div>
					<div class="diffNewDiv">
						<div class="diffTitleDiv diffDel">변경 <span class="text-add">후</span> 내용</div>
						<div id="diffNewStr" class="diffContentDiv"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="content_child clipBoard">
			<div class="slide_box_title">메모장</div>
			<div class="middle_box" id="clipBoardContents" contentEditable="true">

			</div>
		</div>
		<div class="content_child svnRevision">
			<div class="slide_box_title">리비젼 정보</div>
			<div class="req4104_svn_maskBox" id="svnMaskBox">리비젼 정보를 선택하세요</div>
			<table>
				<tr style="height:10px;"  >
					<td rowspan="6"  >
					</td>
				</tr>
				<tr>
					<td style="width:20px;" ></td>
					<td style="width:300px;" >
						<div data-ax5grid="svn-revision-grid" data-ax5grid-config="{}" style="height: 360px;"></div>
					</td>
					<td style="width:20px;" ></td>
					<td style="width:300px">
						<div class="menu_ctrl_btn_wrap" style="border: 1px solid #ccc" >
							<div class="menu_all_wrap">
								<span class="menu_expand_all" title="전체 열기"></span>
								<span class="menu_collapse_all" title="전체 닫기"></span>
							</div>
						</div>
				
						<div class="menu_lists_wrap" style="border: 1px solid #ccc;overflow-y:scroll;overflow-x:scroll;height: 310px;width:300px;border-top:none;" >
								<ul id="fileDirJson" class="ztree"></ul>
						</div>
					</td>
					<td style="width:20px;" ></td>
					<td style="width:615px">
						<div data-ax5grid="second-svnpop-grid" data-ax5grid-config="{}" style="height: 360px;width:615px;"></div>
					</td>
				</tr>
			</table>
		</div>
		<div class="content_child reqDep">
			<div class="slide_box_title">배포 정보 </div>
			<div class="middle_box">
				<div class="slideChg_box" id="reqDep">
					<div class="req4104_reqDep_leftFrame">
						<div class="req4104_reqDep_titleBox">작업흐름별 배정된 배포계획 목록</div>
						<div data-ax5grid="reqDeploy-grid" data-ax5grid-config="{}" style="height: 330px;"></div>
					</div>
					<div class="req4104_reqDep_rightFrame">
						<div class="req4104_reqDep_maskBox" id="reqDplMaskBox">배포 정보를 선택하세요</div>
						<div class="req4104_reqDep_jobGrid">
							<div class="req4104_reqDep_titleBox">선택된 배포계획별 최종 빌드 결과 정보</div>
							<div data-ax5grid="reqDeployJob-grid" data-ax5grid-config="{}" style="height: 330px;"></div>
						</div>
						<div class="req4104_reqDep_jobHistoryGrid">
							<div class="req4104_dpl_mask_frmae" id="req4104_dpl_mask_frmae"></div>
							<div class="req4104_reqDep_titleBox">선택된 JOB 전체 빌드 목록</div>
							<div data-ax5grid="reqDeployJobNum-grid" data-ax5grid-config="{}" style="height: 330px;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="footerTab">
		
		<div class="footerTab_child flowChg footerTab_active" data-tab="flowChg">
			<i class="fa fa-th-list" aria-hidden="true"></i>
			작업흐름 변경이력
		</div>
		
		<div class="footerTab_child reqHistory" data-tab="reqHistory">
			<i class="fa fa-history" aria-hidden="true"></i>
			요구사항 수정이력
		</div>
		
		<div class="footerTab_child reqAddOpt " data-tab="reqAddOpt">
			<i class="fa fa-list" aria-hidden="true"></i>
			추가항목 정보
		</div>
		<div class="footerTab_child reqwork" data-tab="reqwork">
			<i class="fa fa-code-branch" aria-hidden="true"></i>
			작업 내용
		</div>
<!-- 
		<div class="footerTab_child clipBoard" data-tab="clipBoard">
			<i class="fa fa-clipboard" aria-hidden="true"></i>
			메모장
		</div>
		<div id="clipBoard_help"> 메모장 활성화  <span class="addComment"> CTRL + V </span> </div> -->
		
		<div class="footerTab_child svnRevision" data-tab="svnRevision">
			<i class="fa fa-code-branch" aria-hidden="true"></i>
			리비젼 정보
		</div>
		<div class="footerTab_child reqDep" data-tab="reqDep">
			<i class="fa fa-puzzle-piece" aria-hidden="true"></i>
			배포 정보
		</div>
	</div>
	
	<div class="btn_box">
		<div class="button_complete pop_close" style="margin-top:3px !important;display: inline-block;">닫기</div>
	</div>
</div>
<div class="notepad_input">
	<div class="notepad_title">메모장 입력</div>
	<div class="notepad_contents" contentEditable="true" id="notepad_contents"></div>
	<div class="notepad_footer">
		<div class="notepad_footer_btn" id="btn_input">
			<div class="button_normal2" id="notepad_content_insert" style="width: 150px;">등록</div>
		</div>		
		<div class="notepad_footer_btn">
			<div class="button_normal2" id="notepad_cancel" style="width: 150px;">취소</div>
		</div>
	</div>
</div>
<div class="notepad_img_resize_main">
	<div class="notepad_img_download">
		<div class="button_normal" id="notepad_img_download">다운</div>
	</div>
	<div class="notepad_img_resize_div">
		<div class="button_normal" id="notepad_img_delete">삭제</div>
	</div>
	<div class="notepad_img_resize_div">
		<div class="button_normal">취소</div>
	</div>
</div>
<div class="notepad_update">
	<div class="notepad_title">메모 상세정보</div>
	<div class="notepad_detailInfo">
		<div class="notepad_detailInfo_left" id="notepad_detailInfo">
			<input type="hidden" name="reqCbSeq" id="reqCbSeq"/>
			<div class="sub_line">
				<span class="req_sub_left">등록자 :</span>
				<span class="req_sub_right" id="usrNm"></span>
			</div>
			<div class="sub_line">
				<span class="req_sub_left">등록 날짜 :</span>
				<span class="req_sub_right" id="regDtm"></span>
			</div>
			<div class="sub_line">
				<span class="req_sub_left">수정자 :</span>
				<span class="req_sub_right" id="modifyUsrNm"></span>
			</div>
			<div class="sub_line">
				<span class="req_sub_left">수정 일자 :</span>
				<span class="req_sub_right" id="modifyDtm"></span>
			</div>
		</div>
		<div class="notepad_detailInfo_right">
			<div class="button_normal2 detail_btn" id="notepad_detail_update" style="margin-left:125px;">수정완료</div>
			<div class="button_normal2 detail_btn" id="notepad_detail_delete" style="margin-left:125px;">삭제</div>
			<div class="button_normal2 detail_btn" id="notepad_detail_cancel" style="margin-left:125px;">취소</div>
		</div>
	</div>
	<div class="notepad_contents" contentEditable="true" id="notepad_contents_update" style="height:580px;"></div>
</div>
</html>