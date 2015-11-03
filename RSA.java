import java.math.BigInteger;
import gnu.getopt.Getopt;

public class RSA {
    
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
    
    public static BigInteger RSAEncrpyt(String M, int e, int n) {
        //int arr[] = splitMessage(M); // split message into array of
        String ret[];
        // split the long value and include padding
        ret = splitMessage(M);
        // the cipher chunks
        int c[] = new int[ret.length];
        StringBuffer buff = new StringBuffer();
        for (int i=0; i < ret.length; i++) {
            c[i] = computeMod(Integer.parseInt(ret[i]), e, n);
            buff.append(c[i]);
        }
        BigInteger retVal = new BigInteger(buff.toString());
        System.out.println(retVal.toString(16));
        return retVal;
    }
    
    public static int computeMod(int m, int e, int n) {
        BigInteger mess = new BigInteger(m+"");
        BigInteger np = new BigInteger(n+"");
        BigInteger ret = mess.pow(e).mod(np);
        return ret.intValue();
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
    public static String RSADecrypt(String C, int d, int n) {
        System.out.println("DECRYPT: ");
        System.out.println(C);
        // convert from Hex to BigInteger
        BigInteger c = new BigInteger(C, 16);
        System.out.println(c);
        String blocks[] = (c+"").split("(?<=\\G.{4})");
        
        int dp[] = new int[blocks.length];
        StringBuffer buff = new StringBuffer();
        for (int i=0; i < blocks.length; i++) {
            dp[i] = computeMod(Integer.parseInt(blocks[i]), d, n);
            buff.append(dp[i]);
        }
        
        BigInteger retVal = new BigInteger(buff.toString());
        System.out.println(retVal);
        return "";
    }
    
    private static void RSAencrypt(StringBuilder m, StringBuilder nStr, StringBuilder eStr) {
        // TODO Auto-generated method stub
        RSAEncrpyt(m.toString(), Integer.parseInt(eStr.toString()), Integer.parseInt(nStr.toString()));
    }
    
    private static void RSAdecrypt(StringBuilder cStr, StringBuilder nStr,
                                   StringBuilder dStr){
        // TODO Auto-generated method stub
        RSADecrypt(cStr.toString(), Integer.parseInt(dStr.toString()), Integer.parseInt(nStr.toString()));
    }
    
    private static void genRSAkey(StringBuilder bitSizeStr) {
        // TODO Auto-generated method stub
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