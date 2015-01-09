package sahli.michel.generator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.tomcat.util.codec.binary.Base64;

public class PasswordGenerator {
    
    public static void main(String[] args) {
        String username = "admin";
        String usernamePepper = ".+.";
        String password = "admin";
        String passwordPepper = "*+*";
        String hashingAlgorithm = "sha-512";
        String hashingEncoding = "utf-8";
        int saltLength = 32;
        String salt = "";
        
        if(salt.equals("")){
            salt = getUniqueSalt(saltLength);
        }
        
        String hashedPassword = hash(username + usernamePepper + password + passwordPepper + salt, hashingAlgorithm, hashingEncoding);
        
        System.out.println("username: " + username);
        System.out.println("usernamePepper: " + usernamePepper);
        System.out.println("password: " + password);
        System.out.println("passwordPepper: " + passwordPepper);
        System.out.println("hashingAlgorithm: " + hashingAlgorithm);
        System.out.println("hashingEncoding: " + hashingEncoding);
        System.out.println("saltLength: " + saltLength);
        System.out.println();
        System.out.println("*_______________GENERATED_______________*");
        System.out.println();
        System.out.println("salt: " + salt);
        System.out.println("hashedPassword: " + hashedPassword);
        
    }
    
    public static String getUniqueSalt(int saltLength) {
        Random r = new SecureRandom();
        byte[] salt = new byte[saltLength];
        r.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }
    
    public static String hash(String passwordToHash, String hashAlgorithm, String hashEncoding) {
        String hashedPassword = "";
        
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            
            if(hashEncoding == null) {
                md.update(passwordToHash.getBytes());
            }
            else {
                md.update(passwordToHash.getBytes(hashEncoding));
            }
            
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } 
        catch(NoSuchAlgorithmException e) {
            System.out.println("Your hashing algorithm isn't supported");
        }
        catch(UnsupportedEncodingException e) {
            System.out.println("Your hashing encoding isn't supported");
        }
        return hashedPassword;
    }
}
