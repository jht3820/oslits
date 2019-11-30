package kr.opensoftlab.oslops.cmm.cmm3000.cmm3000.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Class Name : Cmm3000Controller.java
 * @Description : Cmm3000Controller Controller class
 * @Modification Information
 *
 * @author 김정환
 * @since 2016.01.24
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class Cmm3000Controller {

	/** 로그4j 로거 로딩 */
	@SuppressWarnings("unused")
	private static final Logger Log = Logger.getLogger(Cmm3000Controller.class);
	
	
    /**
	 * Cmm3000 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @RequestMapping(value="/cmm/cmm3000/cmm3000/selectCmm3000View.do")
    public String selectCmm3000View() throws Exception {
    	return "/cmm/cmm3000/cmm3000/cmm3000";
    }
    
    /**
	 * Cmm3100 화면 이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
    @RequestMapping(value="/cmm/cmm3000/cmm3100/selectCmm3100View.do")
    public String selectCmm3100View() throws Exception {
    	return "/cmm/cmm3000/cmm3100/cmm3100";
    }
    
    

}
