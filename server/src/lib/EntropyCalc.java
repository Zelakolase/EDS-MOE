package lib;

import java.util.function.IntPredicate;

public class EntropyCalc {
    public static double calculate(String passphrase) {
        double entropy = 0;
        int P = 0; // pool
        int L = passphrase.length(); // length of the passphrase
        if(containsLowerCase(passphrase)) P += 26;
        if(containsUpperCase(passphrase)) P += 26;
        if(containsNumber(passphrase)) P += 10;
        entropy = Math.log(power(P, L)) / Math.log(2);
        return entropy;
    }
    private static boolean containsLowerCase(String value) {
        return contains(value, i -> Character.isLetter(i) && Character.isLowerCase(i));
    }
    
    private static boolean containsUpperCase(String value) {
        return contains(value, i -> Character.isLetter(i) && Character.isUpperCase(i));
    }
    
    private static boolean containsNumber(String value) {
        return contains(value, Character::isDigit);
    }
    
    private static boolean contains(String value, IntPredicate predicate) {
        return value.chars().anyMatch(predicate);
    }
    public static long power(int x, int n) { // x ^ n
        long pow = 1L;
         while (n > 0) {
            if ((n & 1) == 1) {
                pow *= x;
            }
            n = n >> 1;
            x = x * x;
        }
        return pow;
    }
}
