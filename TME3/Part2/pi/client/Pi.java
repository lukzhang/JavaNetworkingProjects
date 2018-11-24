package client;

import compute.Task;
import java.io.Serializable;
import java.math.BigDecimal;

public class Pi implements Task<BigDecimal>, Serializable {

    private static final long serialVersionUID = 227L;

    /** Range for largest prime number to be found */
    private final int digits;
    private final int secondDigit;      
    /**
     * Construct a task to calculate pi to the specified
     * precision.
     */
    public Pi(int digits, int secondDigit) {
        this.digits = digits;
        this.secondDigit = secondDigit;
    }

    /**
     * Calculate the largest prime number found within the range
     */
    public BigDecimal execute() {
        return computePi(digits, secondDigit);
    }

    /**
     *Compute the largest prime number of a range between two numbers 'digit' 
     *and 'secondDigit'. The largest prime number is set to 0. Then, the range
     *is looped through and updated the largest prime number. A return of 0 (or
     *less or equal to 1) on the client side signals no prime number was found
     */
    public static BigDecimal computePi(int digits, int secondDigit) {

        //Largest prime number initialized
        int largestNumber = 0;

        //If the second entry is less than first entry, swap
        if(digits > secondDigit){
            int temp = digits;
            digits = secondDigit;
            secondDigit = temp;
        }

        //Checks if current number is prime
        boolean isPrime = true;

        //Loop through each number in range
        for(int i=digits; i<=secondDigit; i++){
            //If number divided gives a remainder of 0, it is not prime
            for(int j=2; j<i; j++){
                if(i%j == 0){
                    isPrime = false;
                    break;
                }
            }
            //If the number is prime and is larger than current largest prime
            //number, than update
            if(isPrime && i > largestNumber){
                largestNumber = i;
            }
            //Reset isPrime boolean
            isPrime = true;
        }
        
        //Return the largest prime number as a BigDecimal
        BigDecimal value = BigDecimal.valueOf(largestNumber);

        return value;
    
    }


}
