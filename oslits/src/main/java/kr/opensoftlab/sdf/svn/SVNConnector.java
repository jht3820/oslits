package kr.opensoftlab.sdf.svn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.opensoftlab.sdf.svn.handler.LogSearchHandler;
import kr.opensoftlab.sdf.svn.vo.SVNLogVO;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @Class Name : SVNConnector.java
 * @Description : SVNConnector SVNConnector class
 * @Modification Information
 *
 * @author 공대영
 * @since 2018.07.02
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

public class SVNConnector {

	
	/**
	 * 
	 * SVNRepository 으로 접속하여 SVNRepository 객체를 리턴해준다.
	 *
	 * @param svnURL 리파지토리 url
	 * @param userId 접속 유저id
	 * @param password 접속유저 PW
	 * 
	 * @return SVNRepository Class
	 * @throws SVNException
	 */
	
	public SVNRepository svnConnect(String svnURL,String userId ,String password ) throws SVNException {
		
		SVNRepository repository = null;

		SVNRepositoryFactoryImpl.setup();

		repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnURL));

		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userId, password);

		repository.setAuthenticationManager(authManager);

		return repository;
	}
	
	
	/**
	 * 
	 * 해당 SVNRepository의 전체 경로에서 startRevision에서 lastRevision까지 리비젼정보를 목록으로 가져온다.
	 * 
	 * @param repository 접속한 대상의 리파지토리
	 * @param startRevision 조회범위의 시작 리비젼
	 * @param lastRevision 조회범위의 종료 리비젼
	 * @return 리비전 정보 목록
	 * @throws SVNException
	 */
	public List<SVNLogVO> selectSVNLogList(SVNRepository repository,long startRevision,long lastRevision) throws SVNException {

		long limit = 0;
		LogSearchHandler handler = new LogSearchHandler();

		if(lastRevision == 0){
			lastRevision = repository.getLatestRevision();
		}

		repository.log(new String[]{}, lastRevision, startRevision, true, true, limit, true, new String[]{}, handler);

		return handler.getSvnFLogList();

	}

	/**
	 * 
	 * 현재 최종버젼의 리비젼의 파일 디렉토리 정보를 출력한다.
	 * (사용되지 않음)
	 * 
	 * @param repository
	 * @param path
	 * @throws SVNException
	 */
	public void listEntries( SVNRepository repository, String path ) throws SVNException {

		Collection entries = repository.getDir( path, -1 , null , (Collection) null );
		Iterator iterator = entries.iterator( );

		while ( iterator.hasNext( ) ) {

			SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );
			System.out.println( "/" + (path.equals( "" ) ? "" : path + "/" ) + entry.getName( ) + 
					" ( author: '" + entry.getAuthor( ) + "'; revision: " + entry.getRevision( ) + 
					"; date: " + entry.getDate( ) + ")" );



			if ( entry.getKind() == SVNNodeKind.DIR ) {			
				listEntries( repository, ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ) );
			}
		}


	}

	/**
	 * 
	 * 디렉토리의 전체 경로를 가져와 존재하는 디렉토리 정보를 맵에 정리한다.  
	 * 디렉토리 정보맵은 트리컴포넌트의 사용을 위해 계층 구조로 정보를 가진다.
	 * 
	 * 
	 * @param path 디렉토리 전체 경로
	 * @param type SVN 파일 변경 
	 *  상태 A : 추가 , M : 수정 , D : 삭제
	 * @param pathMap 정리된 리렉토리 정보멥
	 *  kind : 파일인지 디렉토리인지 정보 file,dir
	 *  type : 파일의 SVN 리비젼 당시의 수정상태 A,M,D
	 *  path : 전체파일포함 디렉토리경로
	 *  key : 현재 디렉토리까지의 경로
	 *  parentKey : 상위 디렉토리까지의 경로 
	 *  currentKey : 현재 디렉토리까지의 경로
	 *  name : 현재 디렉토리또는 파일명
	 * @return
	 */
	public Map setDirectoryMap(String path,char type,Map<String,Map> pathMap){

		String paths[] =  path.split("/");

		int keylength = 0;

		for (int i = 0; i < paths.length; i++) {
			String key = "";
			String parentKey = "";
			String currentKey = "";

			keylength+=paths[i].length() +1 ;
			if(i != paths.length-1){
				key =path.substring(0 ,keylength);
			}else{
				key =path;
			}

			if(i!=0){
				for (int j = 0; j < i; j++) {

					parentKey += paths[j]+"/";

				}

			}


			for (int k = 0; k <= i; k++) {
				currentKey += paths[k]+"/";
			}

			if( ! pathMap.containsKey( key  )){
				Map<String,String> detailMap = new HashMap<String,String>();
				if(i != paths.length-1){
					detailMap.put("kind", "dir");
				}else{
					detailMap.put("kind", "file");
				}
				String typeName = "";
				if( 'A'==type ) {
					typeName = "Added";
				}else if( 'M'==type ) {
					typeName = "Modified";
				}else if( 'D'==type ) {
					typeName = "Deleted";
				}
				detailMap.put("type",  typeName );
				detailMap.put("path", path);
				detailMap.put("key", key);		
				detailMap.put("parentKey", parentKey);
				detailMap.put("currentKey", currentKey);
				if("".equals(paths[i])){
					detailMap.put("name", "Root");
				}else{
					detailMap.put("name", paths[i]);
				}
				
				pathMap.put(key, detailMap);
			}

		}

		return pathMap;

	}

	/**
	 * 정리된 디렉토리 맵 정보를 목록화한다.
	 * @param pathMap
	 * @return 디렉토리 정보 리스트
	 */
	public List<Map> setDirectoryList(Map<String,Map> pathMap){
		List<Map> dirList = new ArrayList<Map>();
		Set<String> keySet=pathMap.keySet();

		for(String key : keySet){
			Map<String,String> keyMap= pathMap.get(key);
			dirList.add(keyMap);
		}


		return dirList;
	}
	
	/**
	 * 선택한 파일의 선택 리비젼의 파일정보를 리턴한다.
	 * 
	 * @param repository 
	 * @param path 대상 파일경로
	 * @param revision 대상 리비젼
	 * @return 선택한 파일의 선택리비젼의 파일정보
	 * @throws SVNException
	 * @throws IOException
	 */
	public String getFileContent(SVNRepository repository, String path, long revision ) throws SVNException, IOException{
		String content = "";
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream( );

		long file = repository.getFile(path, revision ,null, baos);
		
		content = baos.toString("UTF-8");

		baos.close();
		
		return content;
		
	}

	/**
	 * SVNRepository class의 접속 세션을 종료한다.
	 * 
	 * @param repository
	 */
	public void close(SVNRepository repository){
		repository.closeSession();
	}




}
