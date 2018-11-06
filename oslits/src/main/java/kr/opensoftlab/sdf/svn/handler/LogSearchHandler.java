package kr.opensoftlab.sdf.svn.handler;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import kr.opensoftlab.sdf.svn.vo.SVNFileVO;
import kr.opensoftlab.sdf.svn.vo.SVNLogVO;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

/**
 * @Class Name : LogSearchHandler.java
 * @Description : LogSearchHandler LogSearchHandler class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */


public class LogSearchHandler implements ISVNLogEntryHandler{

	private List<SVNLogVO> svnFLogList = new ArrayList<SVNLogVO>();


	public List<SVNLogVO> getSvnFLogList() {
		return svnFLogList;
	}

	
	private Map<String,Map> pathMap = new HashMap<String,Map>();

	public void handleLogEntry(SVNLogEntry svnLog) throws SVNException {
		// TODO Auto-generated method stub
		long revisionNum = 0;
		String author ="";
		Date logDate = null;
		String comment = "";
		Map<String, SVNLogEntryPath> svnPathMap = null;

		List<SVNFileVO> svnFileList = new ArrayList<SVNFileVO>();

		SVNLogVO logVo = new SVNLogVO();

		SVNLogEntryPath  svnLogEntryPath =null;


		revisionNum= svnLog.getRevision();
		author = svnLog.getAuthor();
		logDate = svnLog.getDate();
		comment  = svnLog.getMessage();

		logVo.setAuthor(author);
		logVo.setComment(comment);
		logVo.setLogDate(logDate);
		logVo.setRevision(revisionNum);

		svnPathMap = svnLog.getChangedPaths();

		for( String key : svnPathMap.keySet() ){
			svnLogEntryPath=svnPathMap.get(key);

			SVNFileVO svnFileVO = new SVNFileVO();

			svnFileVO.setPath(svnLogEntryPath.getPath());
			svnFileVO.setType(svnLogEntryPath.getType());		
			svnFileVO.setKind(svnLogEntryPath.getKind().toString());


			svnFileList.add( svnFileVO);
		}


		logVo.setSvnFileList(svnFileList);

		svnFLogList.add(logVo);

	}



	public Map setDirectoryMap(String path,Map<String,Map> pathMap){

		String paths[] =  path.split("/");
		if(pathMap==null){
			pathMap = new HashMap<String,Map>();
		}
		
		int indexOf = path.indexOf("/");

		if(indexOf>-1){
			if(path.substring(1).indexOf("/") >-1){
				String dir = path.substring(1 , path.substring(1).indexOf("/") );
				if( ! pathMap.containsKey(dir)){
					pathMap.put(dir, setDirectoryMap( path.substring( path.substring(1).indexOf("/")+1) ,null ) );
				}
			}

		}

		return pathMap;


	}

}

