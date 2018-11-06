package kr.opensoftlab.sdf.util;

import java.io.UnsupportedEncodingException;

import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;

import egovframework.rte.fdl.cryptography.EgovPasswordEncoder;
import egovframework.rte.fdl.cryptography.impl.EgovARIACryptoServiceImpl;

public class CommonScrty {
	
	public static final String encryptedAria(String value, String key) throws UnsupportedEncodingException{
		return new String(Base64.encodeBase64(CommonScrty.encrypted(value, key)));
	}
	
	public static final String decryptedAria(String value, String key) throws UnsupportedEncodingException{
		return new String(Base64.decodeBase64(CommonScrty.decrypted(value, key)));
	}
	
	//복호화
    public static byte[] decrypted(String value, String key) throws UnsupportedEncodingException{
        
        EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
        EgovARIACryptoServiceImpl egovARIACryptoServiceImpl = new EgovARIACryptoServiceImpl();
        
        String hasedPassword = egovPasswordEncoder.encryptPassword(key);
        egovPasswordEncoder.setHashedPassword(hasedPassword);
        egovPasswordEncoder.setAlgorithm("SHA-256");
        egovARIACryptoServiceImpl.setPasswordEncoder(egovPasswordEncoder);
        egovARIACryptoServiceImpl.setBlockSize(1025);
        
        byte[] decrypted = egovARIACryptoServiceImpl.decrypt(Base64.decodeBase64(value.getBytes("UTF-8")), key);
        return decrypted;
    }
    
    //암호화
    public static byte[] encrypted(String value, String key) throws UnsupportedEncodingException{
        EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
        EgovARIACryptoServiceImpl egovARIACryptoServiceImpl = new EgovARIACryptoServiceImpl();
        
        String hasedPassword = egovPasswordEncoder.encryptPassword(key);
        egovPasswordEncoder.setHashedPassword(hasedPassword);
        egovPasswordEncoder.setAlgorithm("SHA-256");
        egovARIACryptoServiceImpl.setPasswordEncoder(egovPasswordEncoder);
        egovARIACryptoServiceImpl.setBlockSize(1025);
        
        byte[] encrypted = egovARIACryptoServiceImpl.encrypt(Base64.encodeBase64(value.getBytes("UTF-8")), key);
        return encrypted;
    }

}
