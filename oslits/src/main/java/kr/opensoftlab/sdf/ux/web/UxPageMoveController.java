package kr.opensoftlab.sdf.ux.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.opensoftlab.sdf.ux.service.UxPageMoveService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 공통유틸리티성 작업을 위한 Controller 클래스
 * @author 공통 서비스 정형택
 * @since 2016.01.08
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2016.01.08  정형택                최초 생성
 *  
 *  </pre>
 */
@Controller
public class UxPageMoveController {
    
    @Resource(name = "uxPageMoveService")
    private UxPageMoveService uxPageMoveService;
    
	/**
     * jsp.do로 온 요청 jsp 파일 화면으로 넘겨주는 메서드 
     * @return
     * @throws Exception
     */
    @RequestMapping(value="**/*.jsp.do")
    public String jspFileResolve(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	String str = request.getRequestURI();
    	System.out.println(str);
    	String resURI = str.replaceAll(".jsp.do", "");
    	resURI = resURI.replaceAll("/oslits/oslits/", "/");
    	System.out.println("responseURI = " + resURI);
    	return resURI;
    }
}