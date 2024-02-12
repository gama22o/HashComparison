import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.BigInteger; //added
import java.math.RoundingMode;

public class Assignment1 {
 
    //MD5 hash function
    public static String MD5(String str){
        try{ 
            MessageDigest md = MessageDigest.getInstance("MD5"); //creates the MD5 instance
            
            //get the MD5 hash of the input string
            md.update(str.getBytes());
            byte[] md5bytes = md.digest();

            //convert bytes to hexadecimal
            StringBuilder hexString = new StringBuilder();
            for(int i=0; i < md5bytes.length; i++) {
                byte b = md5bytes[i];
                String hex = Integer.toHexString(0xff & b); //each singular byte converted to singular hex
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Error.");
            return null;
        }
    }

    //SHA hash function
    public static String SHA(String str) {
        try{ 
            MessageDigest sha = MessageDigest.getInstance("SHA-256");

            //get SHA hash of the input string 
            sha.update(str.getBytes());
            byte[] shabytes = sha.digest();

            //convert bytes to hexadecimal 
            StringBuilder hexString = new StringBuilder();
            for(int i=0; i < shabytes.length; i++) {
                byte b = shabytes[i];
                String hex = Integer.toHexString(0xff & b); //each singular byte converted to singular hex
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Error.");
            return null;
        }

    }

    //calculates the difference between hashes
    public static List<BigInteger> difference(List<String> hashes) {
        List<BigInteger> differences = new ArrayList<>(); 

        for(int i=1; i < hashes.size(); i++) {
            BigInteger diff = hexDiff(hashes.get(i-1), hashes.get(i));
            differences.add(diff);
        }

        return differences; 
    }

    //calculates the hexadecimal difference
    public static BigInteger hexDiff(String str1, String str2) {
        BigInteger val1 = new BigInteger(str1, 16);
        BigInteger val2 = new BigInteger(str2, 16);

        BigInteger result = val2.subtract(val1).abs(); 
        return result;
    }

    //calculates the average
    public static double average(List<BigInteger> values) {
        BigInteger sum = BigInteger.ZERO;

        for(int i=0; i < values.size(); i++) {
            BigInteger val = values.get(i);
            sum = sum.add(val);
        }

        double result = sum.divide(BigInteger.valueOf(values.size())).doubleValue();;
        return result;
    }

    //calculates the standard deviation 
    public static double stdev(List<BigInteger> values, double avg) {
        BigDecimal sumSquared = BigDecimal.ZERO;
        
        for(BigInteger val : values) {
            //BigDecimal val = values.get(i);
            BigDecimal diff = new BigDecimal(val.subtract(BigInteger.valueOf((long) avg)));
            sumSquared = sumSquared.add(diff.multiply(diff));
        }

        double result = Math.sqrt(sumSquared.divide(BigDecimal.valueOf(values.size()), 10, RoundingMode.HALF_UP).doubleValue());
        return result;
    }

    //converts hexadecimal to binary
    public static String hexToBinary(String hexString) {
        StringBuilder binaryString = new StringBuilder();

        //converting each hex character to binary 
        for(int i=0; i < hexString.length(); i++) {
            char hexChar = hexString.charAt(i);
            int decimalVal = Integer.parseInt(String.valueOf(hexChar), 16);
            String binaryVal = Integer.toBinaryString(decimalVal);

            while(binaryVal.length() < 4) {
                binaryVal = "0" + binaryVal;
            }

            binaryString.append(binaryVal);
        }

        return binaryString.toString();
        
    }

   
    //main function
    public static void main(String[] args) {

        String binString = "10000000";
        List<String> MD5hashes = new ArrayList<>();
        List<String> SHAhashes = new ArrayList<>();

        //300 binary strings starting from 10000000 -- loop
        for(int i=0; i<300; i++) {

            System.out.println("Original binary string: " + binString);

            //hash string using MD5 
            String md5hash = MD5(binString);
            System.out.println("MD5 Hash: " + md5hash);

            //hash string using SHA
            String shahash = SHA(binString);
            System.out.println("SHA Hash: " + shahash);

            //adding the MD5 and SHA hashes to an array list
            MD5hashes.add(md5hash);
            SHAhashes.add(shahash);

            //convert hex to binary
            String binaryMD5 = hexToBinary(md5hash);
            String binarySHA = hexToBinary(shahash);
            System.out.println("MD5 Binary result: " + binaryMD5);
            System.out.println("SHA Binary result: " + binarySHA);

            //increment binary string
            int decimalVal = Integer.parseInt(binString, 2);
            decimalVal++;
            binString = Integer.toBinaryString(decimalVal);

            System.out.println();

            
        }

        //MD5 hashes
        List<BigInteger> md5diff = difference(MD5hashes);
        double MD5avg = average(md5diff);
        double MD5stdev = stdev(md5diff, MD5avg);
        System.out.println("MD5 average difference: " + MD5avg);
        System.out.println("MD5 standard deviation: " + MD5stdev);

        System.out.println(); 

        //SHA hashes
        List<BigInteger> SHAdiff = difference(SHAhashes);
        double SHAavg = average(SHAdiff);
        double SHAstdev = stdev(SHAdiff, SHAavg);
        System.out.println("SHA average difference: " + SHAavg);
        System.out.println("SHA standard deviation: " + SHAstdev);

        System.out.println();
    }
}