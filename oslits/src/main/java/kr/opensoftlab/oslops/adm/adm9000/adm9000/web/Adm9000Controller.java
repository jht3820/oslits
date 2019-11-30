package kr.opensoftlab.oslops.adm.adm9000.adm9000.web;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sun.management.OperatingSystemMXBean;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;


/**
 * @Class Name : Adm9000Controller.java
 * @Description : 시스템 정보(Adm9000) 컨트롤러 클래스
 * @Modification Information
 * <pre>
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2019.01.09  진주영          최초 생성
 *  
 * </pre>
 *  @author 진주영
 *  @version 1.0
 *  @see 
 *  
 *  Copyright (C) Open Soft Lab Corp. All Rights Reserved
 */
@Controller
public class Adm9000Controller {
	
	/**
     * Logging 을 위한 선언
     * Log는 반드시 가이드에 맞춰서 작성한다. 개발용으로는 debug 카테고리를 사용한다.
     */
	protected Logger Log = Logger.getLogger(this.getClass());
	
	/** System Property 를 사용하기 위한 Bean Injection */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
    /**
	 * Adm9000 시스템 정보 화면이동
	 * @param 
	 * @return 
	 * @exception Exception
	 */
	@RequestMapping(value="/adm/adm9000/adm9000/selectAdm9000SysInfoView.do")
    public String selectAdm9000View(HttpServletRequest request, HttpServletResponse response, ModelMap model ) throws Exception {
		return "/adm/adm9000/adm9000/adm9000";
    }
	
	/**
	 * Adm9000 시스템 정보 조회
	 * @throws Exception
	 * @author 진주영
	 * @since 2019.01.09.
	 */
	@SuppressWarnings({ "restriction", "static-access", "rawtypes", "unchecked"})
	@RequestMapping(value="/adm/adm9000/adm9000/selectAdm9000SysInfoAjax.do")
	public ModelAndView selectAdm9000SysInfoAjax(HttpServletRequest request, ModelMap model ) throws Exception {
    	try{
    		// request 파라미터를 map으로 변환
    		//Map<String, String> paramMap = RequestConvertor.requestParamToMap(request);
    		
    		
    		//디스크 용량
			File[] roots = File.listRoots();
			List<Map> driveSpace = new ArrayList<Map>();
			
			for (File root : roots) {
				Map newMap = new HashMap<>();
				
				newMap.put("driveNm", root.getPath());
				newMap.put("diskTotalSpace", root.getTotalSpace());
				newMap.put("diskUsableSpace", root.getUsableSpace());
				
				driveSpace.add(newMap);
			}

			//디스크 용량
		    model.addAttribute("driveSpace", driveSpace);
		    
		    /* OS 및 CPU */
		    //OS 정보
		    OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		    
		    //os명
		    model.addAttribute("osVersion", osbean.getName());
		    
		    //CPU 프로세서 수
		    model.addAttribute("cpuProcessor", osbean.getAvailableProcessors());
		    
		    //현재 CPU 사용률
		    model.addAttribute("cpuUsage", osbean.getSystemCpuLoad());
		    
		    /* 호스트명, ip알아내기 */
		    InetAddress ip = InetAddress.getLocalHost();
		    
		    List<String> list_ipv4 = new ArrayList<String>();
		    List<String> list_ipv6 = new ArrayList<String>();
		    
		    NetworkInterface iface = null;
		    try {
		        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
		            iface = (NetworkInterface) ifaces.nextElement();
		            InetAddress ia = null;

		            for (Enumeration ips = iface.getInetAddresses(); ips.hasMoreElements();) {
		                ia = (InetAddress) ips.nextElement();
		                String address = ia.getHostAddress();
		                if ("127.0.0.1".compareTo(address) != 0 && "0:0:0:0:0:0:0:1".compareTo(address) != 0) {
		                	//ipv4일경우
		                	if(address.indexOf('.') != -1){
		                		list_ipv4.add(address);
		                	}else{
		                		list_ipv6.add(address);
		                	}
		                }
		            }
		        }
		    }
		    catch (SocketException e) {
		        e.printStackTrace();
		    }
		    //ipv4,6 목록
		    model.addAttribute("listIpv4", list_ipv4);
		    model.addAttribute("listIpv6", list_ipv6);
		    
		    //호스트 명
		    model.addAttribute("clientHostNm", ip.getLocalHost().getHostName());
		    
		    //클라이언트 ip
		    model.addAttribute("clientIp4", ip.getLocalHost().getHostAddress());
		    
		    /* 메모리 */
		    //최대 사용 가능 메모리 
		    model.addAttribute("totalMem", osbean.getTotalPhysicalMemorySize());
		    
		    //사용 가능 메모리
		    model.addAttribute("freeMem", osbean.getFreePhysicalMemorySize());
		    
		    MemoryMXBean membean = (MemoryMXBean) ManagementFactory.getMemoryMXBean();

		    //힙 메모리
		  	MemoryUsage heap = membean.getHeapMemoryUsage();
		  	Map<String,Long> heapInfo = new HashMap<String, Long>();
		  	heapInfo.put("init", heap.getInit());
		  	heapInfo.put("used", heap.getUsed());
		  	heapInfo.put("committed", heap.getCommitted());
		  	heapInfo.put("max", heap.getMax());
		  	
		  	//perm 메모리
		  	MemoryUsage nonheap = membean.getNonHeapMemoryUsage();
		  	Map<String,Long> nonHeapInfo = new HashMap<String, Long>();
		  	nonHeapInfo.put("init", nonheap.getInit());
		  	nonHeapInfo.put("used", nonheap.getUsed());
		  	nonHeapInfo.put("committed", nonheap.getCommitted());
		  	nonHeapInfo.put("max", nonheap.getMax());

		    //최대 사용 가능 메모리 
		    model.addAttribute("heapInfo", heapInfo);
		    
		    //사용 가능 메모리
		    model.addAttribute("nonHeapInfo", nonHeapInfo);

		    //서버시간
		    Date today = new Date();
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)",Locale.KOREAN);
		    
		    model.addAttribute("serverDate", format.format(today));
		    model.addAttribute("timeZone", TimeZone.getTimeZone("Asia/Seoul").getDisplayName());
		    
			model.addAttribute("errorYn", "N");
			
			return new ModelAndView("jsonView");
    	}catch(Exception ex){
			Log.error("selectAdm9000SysInfoAjax()", ex);

			//조회실패 메시지 세팅 및 저장 성공여부 세팅
			model.addAttribute("errorYn", "Y");
			model.addAttribute("message", egovMessageSource.getMessage("fail.common.select"));
			return new ModelAndView("jsonView");
		}
	}
}
