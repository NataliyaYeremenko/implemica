package com.company;

import java.math.BigInteger;

/**
 * Created by Nataliya on 19.05.2017.
 */
public class Task3 {

    // A value of 100! is more than an integer or long, so to calculate the factorial, we need to use BigInteger

    static BigInteger bigFactorial (int number) {
        BigInteger factorialRes = BigInteger.ONE;
        for (int i = 1; i <= number; i++) {
            // Factorial calculation by successively multiplying numbers from 1 to n
            factorialRes = factorialRes.multiply(BigInteger.valueOf(i));
        }return factorialRes;
    }

    static long getSum(BigInteger factorialRes) {
        long sum = 0;
        while (factorialRes.compareTo(BigInteger.ZERO) > 0) { //Check that the number is 0, i.e. the first digit is reached
            //The allocation of the last digit as a deletion from the division of the number by 10
            //and adding it to the previous result
            sum += factorialRes.mod(BigInteger.TEN).longValue();
            factorialRes = factorialRes.divide(BigInteger.TEN); //Remove the last digit by dividing the number by 10
        }
        return sum;
    }

    public static void main(String[] args) {
        BigInteger fact100 = bigFactorial(100);
        System.out.println("100! = " + fact100);
        System.out.println("The sum of the digits in the number 100! : " + getSum(fact100));
    }
}
