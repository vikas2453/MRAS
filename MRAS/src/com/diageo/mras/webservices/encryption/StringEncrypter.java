package com.diageo.mras.webservices.encryption;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.diageo.mras.webservices.services.ValidationDateEmail;
import com.sun.jersey.core.util.Base64;
public class StringEncrypter {
	  Cipher ecipher;
	    Cipher dcipher;
	  private  static Logger logger = Logger
				.getLogger(StringEncrypter.class.getName());



	    /**
	     * Constructor used to create this object.  Responsible for setting
	     * and initializing this object's encrypter and decrypter Chipher instances
	     * given a Pass Phrase and algorithm.
	     * @param passPhrase Pass Phrase used to initialize both the encrypter and
	     *                   decrypter instances.
	     */
	 public  StringEncrypter(String passPhrase) {
            // 8-bytes Salt
	        byte[] salt = {
	            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
	        };

	        // Iteration count
	        int iterationCount = 19;

	        try {

	            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
	            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

	            ecipher = Cipher.getInstance(key.getAlgorithm());
	            dcipher = Cipher.getInstance(key.getAlgorithm());

	            // Prepare the parameters to the cipthers
	            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

	            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

	        } catch (InvalidAlgorithmParameterException e) {
	        	logger.info("EXCEPTION: InvalidAlgorithmParameterException");	           
	            e.printStackTrace();
	        } catch (InvalidKeySpecException e) {
	        	logger.info("EXCEPTION: InvalidKeySpecException");	           
	            e.printStackTrace();
	           
	        } catch (NoSuchPaddingException e) {
	        	logger.info("EXCEPTION: NoSuchPaddingException");	           
	            e.printStackTrace();
	   
	        } catch (NoSuchAlgorithmException e) {
	        	logger.info("EXCEPTION: NoSuchAlgorithmException");	           
	            e.printStackTrace();
	            
	        } catch (InvalidKeyException e) {
	        	logger.info("EXCEPTION: InvalidKeyException");	           
	            e.printStackTrace();
	           
	        }
	    }


	    /**
	     * Takes a single String as an argument and returns an Encrypted version
	     * of that String.
	     * @param str String to be encrypted
	     * @return <code>String</code> Encrypted version of the provided String
	     */
	    public String encrypt(String str) {
	        try {
	            // Encode the string into bytes using utf-8
	            byte[] utf8 = str.getBytes("UTF8");

	            // Encrypt
	            byte[] enc = ecipher.doFinal(utf8);

	            // Encode bytes to base64 to get a string
	           // DatatypeConverter.printBase64Binary(enc);

	            return org.jboss.util.Base64.encodeBytes(enc);

	        } catch (BadPaddingException e) {
	        	logger.info("EXCEPTION: BadPaddingException");	           
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	        	logger.info("EXCEPTION: IllegalBlockSizeException");	           
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	        	logger.info("EXCEPTION: UnsupportedEncodingException");	           
	            e.printStackTrace();
	        } catch (IOException e) {
	        	logger.info("EXCEPTION: IOException");	           
	            e.printStackTrace();
	        }
	        return null;
	    }


	    /**
	     * Takes a encrypted String as an argument, decrypts and returns the 
	     * decrypted String.
	     * @param str Encrypted String to be decrypted
	     * @return <code>String</code> Decrypted version of the provided String
	     */
	    public String decrypt(String str) {

	        try {
	        	
	            // Decode base64 to get bytes
	            byte[] dec =Base64.decode(str);
	            	
	            	//DatatypeConverter.parseBase64Binary(str);

	            // Decrypt
	            byte[] utf8 = dcipher.doFinal(dec);

	            // Decode using utf-8
	            return new String(utf8, "UTF8");

	        } catch (BadPaddingException e) {
	        	logger.info("EXCEPTION: BadPaddingException");	           
	            e.printStackTrace();
	        } catch (ArithmeticException e) {
	        	logger.info("EXCEPTION: ArithmeticException");	           
	            e.printStackTrace();
	        } catch (ArrayIndexOutOfBoundsException e ){
	        	logger.info("EXCEPTION: ArrayIndexOutOfBoundsException");	           
	            e.printStackTrace();
	        } catch (IllegalBlockSizeException e) {
	        	logger.info("EXCEPTION: IllegalBlockSizeException");	           
	            e.printStackTrace();
	        } catch (UnsupportedEncodingException e) {
	        	logger.info("EXCEPTION: UnsupportedEncodingException");	           
	            e.printStackTrace();
	        } catch (IOException e) {
	        	logger.info("EXCEPTION: IOException");	           
	            e.printStackTrace();
	        }
	        return "Mismatch";
	    }
	    
	    
	  /*  public static String  Decryption(String Encyption) {

	        System.out.println();
	        System.out.println("+----------------------------------------+");
	        System.out.println("|  -- Test Using Pass Phrase Method --   |");
	        System.out.println("+----------------------------------------+");
	        System.out.println();

	       String passPhrase   = "Diageo@123";

	        // Create encrypter/decrypter class
	        StringEncrypter desEncrypter = new StringEncrypter(passPhrase);

	        // Encrypt the string
	       // String desEncrypted       = desEncrypter.encrypt(Encyption);

	        // Decrypt the string
	        String desDecrypted       = desEncrypter.decrypt(Encyption);

	        // Print out values
	        System.out.println("PBEWithMD5AndDES Encryption algorithm");
	        System.out.println("    Original String  : " + Encyption);
	        System.out.println("    Decrypted String : " + desDecrypted);
	        System.out.println();
		
	        return desDecrypted;

	    }*/


}
