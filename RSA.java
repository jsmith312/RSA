/**
 * @authors Jordan Smith, Chris Pawlik
 * CSc 466
 * Assignment 5
 * 
 */
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.BitSet;
import java.util.Random;

import gnu.getopt.Getopt;

public class RSA {
    
    static private int bit_size = 1024;
    static private BigInteger p;
    static private BigInteger q;
    static private BigInteger n;
    static private BigInteger phi_n;
    static private BigInteger e;
    static private BigInteger d;
    static private BitSet pSet;
    static private BitSet qSet;
    static private boolean simp = false;
    
    public static void main(String[] args) {
        StringBuilder bitSizeStr = new StringBuilder();
        StringBuilder nStr = new StringBuilder();
        StringBuilder dStr = new StringBuilder();
        StringBuilder eStr = new StringBuilder();
        StringBuilder m = new StringBuilder();
        
        pcl(args, bitSizeStr, nStr, dStr, eStr,m);
        
        if(simp == false && !bitSizeStr.toString().equalsIgnoreCase("")){
            //This means you want to create a new key
            genRSAkey(bitSizeStr);
        }else if(simp == true){
        	//"100" will let genKey() know to set as 1024
        	genRSAkey(bitSizeStr.append("100"));
        }
        
        if(!eStr.toString().equalsIgnoreCase("")){
            RSAencrypt(m, nStr, eStr);
        }
        
        if(!dStr.toString().equalsIgnoreCase("")){
            RSAdecrypt(m, nStr, dStr);
        }
        
    }
    
    /**
     * RSA Encryption
     * @param M, e, n
     */
    public static BigInteger RSAEncrpyt(String M, String e, String n) {
        String ret[];
        BigInteger N = new BigInteger(n+"", 16);
        BigInteger E = new BigInteger(e+"", 16);
        String buff = computeMod(M.toString(), E.toString(), N.toString());
        BigInteger retVal = new BigInteger(buff.toString());
        System.out.println("Cipher Text: " + retVal.toString(16));
        return retVal;
    }
    
    /**
     * Helper Method for computing the modulus of e/d
     * @param M, e, n
     **/
    public static String computeMod(String m, String e, String n) {
        BigInteger mess = new BigInteger(m+"");
        BigInteger np = new BigInteger(n);
        BigInteger E = new BigInteger(e);
        BigInteger retV = mess.modPow(E, np);
        return retV.toString();
    }
    
    /**
     * RSA decryption
     * @param C, d, n
     */
    public static String RSADecrypt(String C, String d, String n) {
        BigInteger D = new BigInteger(d, 16);
        // convert from Hex to BigInteger
        BigInteger N = new BigInteger(n+"", 16);
        BigInteger c = new BigInteger(C, 16);
        String buff = computeMod(c.toString(), D.toString(), N.toString());
        BigInteger retVal = new BigInteger(buff.toString());
        System.out.println("Plain Text: " + retVal);
        return "";
    }
    
    /**
     * RSAencrypt()
     * 
     * @param m, nStrm, eStr
     */
    private static void RSAencrypt(StringBuilder m, StringBuilder nStr, StringBuilder eStr) {
        //System.out.println("E (hex): " + eStr.toString());
        BigInteger e = new BigInteger(eStr.toString(), 16);
        //System.out.println("E (int): " + e);
        RSAEncrpyt(m.toString(), eStr.toString(), nStr.toString());
    }
    
    private static void RSAdecrypt(StringBuilder cStr, StringBuilder nStr,
                                   StringBuilder dStr){
        // TODO Auto-generated method stub
        RSADecrypt(cStr.toString(), dStr.toString(), nStr.toString());
    }
    
    /**
     * genRSAkey()
     *
     * @param bitSizeStr
     *
     * generates session key. Takes in a StringBuilder and if valid, uses that as bit_size. Otherwise, use default 1024 bits.
     */
    private static void genRSAkey(StringBuilder bitSizeStr) {
        if(Integer.parseInt(bitSizeStr.toString()) > 1024 && Integer.parseInt(bitSizeStr.toString()) < 10000){
            bit_size = Integer.parseInt(bitSizeStr.toString());
            setSize(bit_size);
        }
        //System.out.println("bit_size: " + bit_size);
        genPrimes();
        //System.out.println(p.bitLength());
        //System.out.println(q.bitLength());
        n = p.multiply(q);
        phi_n = (p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1))));
        genE();
        genD();
        
        System.out.println(n.bitLength());
        System.out.println("e: " + e.toString(16));
        System.out.println();
        System.out.println("d: " + d.toString(16));
        System.out.println();
        System.out.println("n: " + n.toString(16));
        
    }
    /**
     * setSize()
     *
     * @param size
     * Ensure bit_size is divisible by 8
     */
    private static void setSize(int size){
        while(size % 8 != 0) {
            size+=1;
        }
        bit_size = size;
    }
    
    
    /**
     * getPrimes()
     *
     * generate two prime numbers
     */
    private static void genPrimes(){
        SecureRandom rnd = new SecureRandom();
        pSet = new BitSet(bit_size/2);
        qSet = new BitSet(bit_size/2);
        int i = 0;
        for(;;){
            pSet = new BitSet(bit_size/2);
            i = 0;
            while(i < (bit_size/2)){
                pSet.set(i, rnd.nextBoolean());
                i++;
            }
            p = new BigInteger(pSet.toByteArray()).abs();
            
            if(p.isProbablePrime(100)){
                break;
            }

        }
        for(;;){
            qSet = new BitSet(bit_size/2);
            i = 0;
            while(i < (bit_size/2)){
                qSet.set(i, rnd.nextBoolean());
                i++;
            }
            q = new BigInteger(qSet.toByteArray()).abs();
            
            if(q.isProbablePrime(100)){
                break;
            }
        }
    }
    /**
     * testPrime()
     *
     * @param val
     * @return boolean
     *
     * Tests if long representation of p and q are prime values.
     */
    /*private static boolean testPrime(long val){
     if(val <= 1 || val % 2 == 0){
     return false;
     }else if(val == 2){
     return true;
     }
     for(int i = 3; i*i <= val; i+= 2){
     if(val % i == 0){
     return false;
     }
     }
     return true;
     }*/
    
    /**
     * genE()
     *
     * generate e such that it is relatively prime to phi_n and odd
     */
    private static void genE(){
        int i;
        SecureRandom rand = new SecureRandom();
        i = rand.nextInt((1000-3)+1)+1;
        BigInteger j = BigInteger.valueOf(i);
        for(;;){
            if(j.isProbablePrime(100) && phi_n.longValue() % i != 0 && i % 2 != 0 && phi_n.gcd(j).compareTo(BigInteger.valueOf(1)) == 0){
                e = BigInteger.valueOf(i).abs();
                return;
            }else{
                i = rand.nextInt((1000-3)+1)+1;
                j = BigInteger.valueOf(i);
            }
        }
    }
    /**
     * genD()
     *
     * compute d
     */
    private static void genD(){
        d = e.modInverse(phi_n);
    }
    
    /**
     * This function Processes the Command Line Arguments.
     */
    private static void pcl(String[] args, StringBuilder bitSizeStr,
                            StringBuilder nStr, StringBuilder dStr, StringBuilder eStr,
                            StringBuilder m) {
        /*
         * http://www.urbanophile.com/arenn/hacking/getopt/gnu.getopt.Getopt.html
         */
        Getopt g = new Getopt("Chat Program", args, "hke:d:b:n:i:");
        int c;
        boolean flag = false;
        String arg;
        while ((c = g.getopt()) != -1){
            switch(c){
                case 'i':
                    arg = g.getOptarg();
                    m.append(arg);
                    break;
                case 'e':
                    arg = g.getOptarg();
                    eStr.append(arg);
                    break;
                case 'n':
                    arg = g.getOptarg();
                    nStr.append(arg);
                    break;
                case 'd':
                    arg = g.getOptarg();
                    dStr.append(arg);
                    break;
                case 'k':
                	simp = true;
                    break;
                case 'b':
                	simp = false;
                    arg = g.getOptarg();
                    bitSizeStr.append(arg);
                    break;
                case 'h':
                    callUsage(0);
                case '?':
                    break; // getopt() already printed an error
                default:
                    break;
            }
        }
    }
    
    private static void callUsage(int exitStatus) {
        
        String useage = "\'i\' - designates plaintext value for encrypt and ciphertext value for decrypt\n"
            + "\'e\' - designates public key\n"
            + "\'n\' - designates n value for both encrypt and decrypt\n"
            + "\'d\' - designates private key\n"
            + "\'k\' - generates key\n"
            + "\'b\' - designates bit size for key generation\n"
            + "\'h\' - lists cammand line options for this program\n";
        
        System.err.println(useage);
        System.exit(exitStatus);
        
    }
    
}