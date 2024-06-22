// Imports

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class TradeStock {

    public static int minIndex = 0;
    public static int maxIndex = 0;

    public static int callCounter = 0;


    public static void main(String[] args) throws IOException {

        // Checking if the file in the command line is valid
        File f = new File(args[0]);
        if (f.isFile() && Integer.parseInt(args[1]) == 1) {
            System.out.println("Russell Coon\n" + args[0] + "\nTheta(nlog(n)) Divide and Conquer");

            // Getting the bytes from the .bin file
            FileInputStream fis = new FileInputStream(f);
            byte[] arr = new byte[(int) f.length()];
            fis.read(arr);
            ByteBuffer bb = ByteBuffer.wrap(arr);

            try {
                // Getting the correct length (first int) and other floats
                int stockPriceLength = bb.getInt();
                int numberOfValues = bb.limit() / 4;
                float[] stockPrices = new float[stockPriceLength];

                // Putting all the float values into a float[]
                for (int i = 1; i < numberOfValues; i++)
                    stockPrices[i - 1] = bb.getFloat();


                // Calling the maxProfit function that will return the max profit
                float goodProfit = maxProfit(stockPrices, 0, stockPriceLength-1);
                System.out.printf("%d, %d, %.4f", minIndex, maxIndex, goodProfit);
            } catch (BufferUnderflowException e) {
                System.out.println("\nException Thrown!! : " + e);
            }
        } else {
            // Printing out an invalid input statement and exiting the program.
            System.out.println("\nInvalid input. Ending Program.");
            System.exit(0);
        }
    } // main

    public static float maxProfit(float[] binValues, int low, int high) {

        // Base case - returns 0 because low and high are equal, no profit can be made
        if (low == high) {
            return 0;
        }

        // Getting the middle of the section
        int mid = (low + high) / 2;

        // Finding the maximum profit for the left half, right half, and
        // when the minimum price is on the left, and maximum is on the right
        float leftProfit = maxProfit(binValues, low, mid);                       // 1 T(n/2)

        float rightProfit = maxProfit(binValues, mid + 1, high);            // 1 T(n/2)

        float overMidProfit = maxProfitOverMid(binValues, low, mid, high);      // f(n) = n


        // Returning the max of the 3 profits
        float maxProfit = Math.max(overMidProfit, Math.max(leftProfit, rightProfit));

        return maxProfit;
    }

    public static float maxProfitOverMid(float[] binValues, int low, int mid, int high) {

        // Setting the minimum price to the "low"est value
        float minimumPrice = binValues[low];

        // Looping through the lower half (low-->mid) in the binValues array
        // to find the minimum price (the smallest value)
        for (int min = low; min < mid; min++) {
            if (binValues[min] < minimumPrice) {
                minimumPrice = binValues[min];
                minIndex = min;
            }
        }

        // Setting the maximum profit (value at mid - minimum price)
        float maximumProfit = binValues[mid] - minimumPrice;

        // Looping through the upper half (mid-->high) in the binValues array
        // to find the maximum profit
        for (int max = mid; max < high; max++) {
            if (binValues[max] - minimumPrice > maximumProfit) {
                maximumProfit = binValues[max] - minimumPrice;
                maxIndex = max;
            }
        }

        // Return the maximum profit found
        return maximumProfit;
    }
} // TradeStock
