package lib;

import java.util.function.IntPredicate;

/**
 * Calculation of Passphrase Entropy
 * @Morad A.
 */
public class EntropyCalc {
    public static double calculate(String passphrase) {
        double entropy = 0;
        int P = 0; // pool
        int L = passphrase.length(); // length of the passphrase
        if(containsLowerCase(passphrase)) P += 26;
        if(containsUpperCase(passphrase)) P += 26;
        if(containsNumber(passphrase)) P += 10;
        if(containsSpecialCharacter(passphrase)) P += 32;
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

    private static boolean containsSpecialCharacter(String value) {
        return contains(value, i-> !Character.isLetter(i) && !Character.isDigit(i) && !Character.isWhitespace(i));
    }
    
    private static boolean contains(String value, IntPredicate predicate) {
        return value.chars().anyMatch(predicate);
    }

    /**
     * Square and multiply algorithm to compute x^n
     * @param x Base
     * @param n Power
     * @return Result of x^n
     */
    private static double power(double x, long n) {
        if( n < 0 ) return power( (1 / x), -n);
        else if ( n == 0) return 1.0; 
        else if( n == 1) return x;
        else if( n % 2 == 0) return power(x * x, n / 2);
        else return x * power( x * x, n / 2);
    }
}
