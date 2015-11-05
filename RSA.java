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
    
    public static void main(String[] args) {
        StringBuilder bitSizeStr = new StringBuilder();
        StringBuilder nStr = new StringBuilder();
        StringBuilder dStr = new StringBuilder();
        StringBuilder eStr = new StringBuilder();
        StringBuilder m = new StringBuilder();
        
        pcl(args, bitSizeStr, nStr, dStr, eStr,m);
        
        if(!bitSizeStr.toString().equalsIgnoreCase("")){
            //This means you want to create a new key
            genRSAkey(bitSizeStr);
        }
        
        if(!eStr.toString().equalsIgnoreCase("")){
            RSAencrypt(m, nStr, eStr);
        }
        
        if(!dStr.toString().equalsIgnoreCase("")){
            RSAdecrypt(m, nStr, dStr);
        }
        
        /*BigInteger j = RSAEncrpyt("6882326879666683",79,3337);
         System.out.println(j);
         System.out.println(j.toString(16)); // report as BigInt;
         String k = RSADecrypt(j.toString(16), 1019, 3337);
         System.out.println(k); // report as BigInt;*/
    }
    
    public static BigInteger RSAEncrpyt(String M, int e, String n) {
        //int arr[] = splitMessage(M); // split message into array of
        String ret[];
        BigInteger N = new BigInteger(n+"", 16);
        BigInteger E = new BigInteger(e+"", 16);
        // split the long value and include padding
        ret = splitMessage(M);
        // the cipher chunks
        String c[] = new String[ret.length];
        StringBuffer buff = new StringBuffer();
        for (int i=0; i < ret.length; i++) {
            c[i] = computeMod(ret[i], e, N.toString());
            // System.out.println(c[i]);
            buff.append(c[i]);
            //modExp(new BigInteger(ret[i]), E, N);
            //System.out.println(c[i]);
        }
        BigInteger retVal = new BigInteger(buff.toString());
        System.out.println(retVal.toString(16));
        return retVal;
    }
    
    private static void modExp(BigInteger big, BigInteger exp, BigInteger mod){
        BigInteger q = BigInteger.valueOf(1), m = exp, square = big;
        while(m.compareTo(BigInteger.valueOf(1)) >= 0){
            if(m.mod(BigInteger.valueOf(2)).compareTo(BigInteger.valueOf(0)) != 0){
                big = q.multiply(square).mod(mod);
            }
            square = square.multiply(square).mod(mod);
            m = m.divide(BigInteger.valueOf(2));
        }
        System.out.println(q);
        System.out.println(big);
        System.out.println(mod);
        System.out.println(m);
        System.out.println(square);
        
    }
    
    public static String computeMod(String m, int e, String n) {
        BigInteger mess = new BigInteger(m+"");
        BigInteger np = new BigInteger(n);
        BigInteger E = new BigInteger(e+"");
        //BigInteger ret = mess.pow(e);
        //BigInteger retV = ret.mod(np);
        
        BigInteger retV = mess.modPow(E, np);
        //System.out.println(retV.toString());
        return retV.toString();
    }
    
    /**
     * Splits the message into 3 chunks
     * @param M (Message to encrypt)
     * @return blocks, String[] of every 3 chunks of the M, with padding
     */
    private static String[] splitMessage(String M) {
        String blocks[] = (M+"").split("(?<=\\G.{3})");
        int ret[] = new int[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i].length() < 3) {
                // pad the Long
                if (blocks[i].length() == 1) {
                    blocks[i] = "00"+blocks[i];
                } else {
                    blocks[i] = "0"+blocks[i];
                }
            }
        }
        return blocks;
    }
    
    /**
     * TODO: You need to write the DES decryption here.
     * @param C, d, n
     */
    public static String RSADecrypt(String C, String d, String n) {
        System.out.println("DECRYPT: ");
        //System.out.println(C);
        BigInteger D = new BigInteger(d,16);
        // convert from Hex to BigInteger
        BigInteger N = new BigInteger(n+"", 16);
        BigInteger c = new BigInteger(C, 16);
        //System.out.println(c);
        String blocks[] = (c+"").split("(?<=\\G.{4})");
        
        String dp[] = new String[blocks.length];
        StringBuffer buff = new StringBuffer();
        for (int i=0; i < blocks.length; i++) {
            dp[i] = computeMod(blocks[i].toString(), D.intValue(), N.toString());
            buff.append(dp[i]);
        }
        BigInteger retVal = new BigInteger(buff.toString());
        System.out.println(retVal);
        return "";
    }
    
    private static void RSAencrypt(StringBuilder m, StringBuilder nStr, StringBuilder eStr) {
        //System.out.println("E (hex): " + eStr.toString());
        BigInteger e = new BigInteger(eStr.toString(), 16);
        //System.out.println("E (int): " + e);
        RSAEncrpyt(m.toString(), e.intValue(), nStr.toString());
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
        
        //System.out.println(n.bitLength());
        System.out.println("e (hex): " + e.toString(16));
        System.out.println("e (int): " + e.toString(10));
        System.out.println();
        System.out.println("d (hex): " + d.toString(16));
        System.out.println("d (int): " + d.toString(10));
        System.out.println();
        System.out.println("n (hex): " + n.toString(16));
        System.out.println("n (int): " + n.toString(10));
        
    }
    /**
     * setSize()
     *
     * @param size
     * Ensure bit_size is divisible by 8
     */
    private static void setSize(int size){
        while(size % 8 != 0){
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
            
            if(p.isProbablePrime(10)){
                break;
            }
            
            //test prime and assign p, q
            //if(testPrime(p.longValue())){
            //	if(testPrime(q.longValue())){
            //		break;
            //	}
            //}
        }
        for(;;){
            qSet = new BitSet(bit_size/2);
            i = 0;
            while(i < (bit_size/2)){
                qSet.set(i, rnd.nextBoolean());
                i++;
            }
            q = new BigInteger(qSet.toByteArray()).abs();
            
            if(q.isProbablePrime(10)){
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
            if(j.isProbablePrime(10) && phi_n.longValue() % i != 0 && i % 2 != 0 && phi_n.gcd(j).compareTo(BigInteger.valueOf(1)) == 0){
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
        System.out.println(phi_n);
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
                    break;
                case 'b':
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
        
        String useage = "";
        
        System.err.println(useage);
        System.exit(exitStatus);
        
    }
    
}