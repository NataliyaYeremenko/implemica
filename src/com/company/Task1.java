package com.company;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Created by Nataliya on 19.05.2017.
 */
public class Task1 {
    // use BigInteger, since already at N = 20 obtain a value 6564120420
    private BigInteger Catalan(int n){
        //Array for storing the number of ways to compose the correct bracket sequence
        BigInteger[] catArray = new BigInteger[n+1]; // массив для хранения количества способов составления правильной скобочной последовательности
        /*
        рекуррентное соотношение для вычисления количества правильных скобочных последовательностей:
        recurrence relation for calculating the number of regular bracketed sequences:
            catArray[N] = catArray[0]*catArray[N - 1] + catArray[1]catArray[N - 2] +
                        +catArray[2]catArray[N - 3] +...+ catArray[N - 2]catArray[1] + catArray[N - 1]catArray[0]
         */
        //The correct bracket sequence of length 0 is exactly one - empty
        catArray[0] = BigInteger.ONE; //правильная скобочная последовательность длины 0 ровно одна - пустая
        for (int m = 1; m <= n; ++m) {
            catArray[m] = BigInteger.ZERO;
            for (int k = 0; k < m; ++k)
                catArray[m] = catArray[m].add(catArray[k].multiply(catArray[m-1-k]));
        }
        return catArray[n];
    }

    public static void main(String[] args) {
        System.out.print("Input N: ");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println("The number of regular bracketed sequences (Catalan number): " + new Task1().Catalan(n));
    }
}
