import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SfComLuna {
	
	public static String sfReplaceAll(String pData, String pRegex, String pReplace) {
		
		if(pData == null || pData.length() == 0) {
			return pData;
		}
		
		if(pRegex == null || pRegex.length() == 0) { 
			return " ";
		}
		
		Pattern ptrn = null;
		Matcher mtch = null;
		String rtnValue = "";
		
		ptrn = Pattern.compile(pRegex);
		
		try {
			mtch = ptrn.matcher(pData);
			
			if(pReplace != null) {
				rtnValue = mtch.replaceAll(pReplace);
				
			} else {
				rtnValue = mtch.replaceAll("");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			rtnValue = e.getMessage();
		}

		return rtnValue;
	}
}
