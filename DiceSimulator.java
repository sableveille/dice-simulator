import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.System.*;

/*
 * Plan:
 *   Senarios:
 *       average of choosing highest/lowest
 *       average of dice rerolled at different numbers
 *       probability of rolling <number> (or higher/lower) on multiple dice
 * */

public class DiceSimulator {
    static int bigNum = 100000;
    public static void main(String[] args) throws IOException {

        boolean running = true;

        while(running) {
            Scanner scan = new Scanner(System.in);
            out.println("Select type of rolling: ");
            out.println(" 1.  Roll a single die\n 2.  Get highest/lowest of multiple dice\n 3.  Get the total of multiple dice\n 4.  Get list of multiple dice rolls\n 5.  Average of a single die\n 6.  Average of multiple dice\n 7.  Average of multiple dice if the highest/lowest is chosen\n 8.  Average of multiple dice rerolling numbers lower than X once\n 9.  Average of multiple dice rerolling the lowest X dice if they are below average\n 10. Probability to roll more or less than X on Y dice\n 11. Probabilities of rolling the same number any number of times on X dice\n");
            //set up input
            int rollType = Integer.parseInt(scan.nextLine());
            out.println("Enter dice size: ");
            int size = Integer.parseInt(scan.nextLine());

            out.println("Enter number of dice");
            int dice = Integer.parseInt(scan.nextLine());

            boolean high;
            int highestRerolled;
            int reroll;
            int number;

            switch (rollType) {
                case 1:
                    out.println(roll(size));
                    break;
                case 2:
                    out.println("High(true) or low(false)?");
                    high = Boolean.parseBoolean(scan.nextLine());
                    out.println(rollMultiHighLow(size, dice, high));
                    break;
                case 3:
                    System.out.println(rollMultiTotal(size, dice));
                    break;
                case 4:
                    printArr(rollMultiList(size, dice));
                    break;
                case 5:
                    System.out.println(averageSingle(size));
                    break;
                case 6:
                    System.out.println(averageRollMultiple(size, dice));
                    break;
                case 7:
                    out.println("High(true) or low(false)?");
                    high = Boolean.parseBoolean(scan.nextLine());
                    System.out.println(averageMultiHighLow(size, dice, high));
                    break;
                case 8:
                    out.println("Enter the highest number re-rolled");
                    highestRerolled = Integer.parseInt(scan.nextLine());
                    System.out.println(averageRerollLessThan(size, dice, highestRerolled));
                    break;
                case 9:
                    out.println("Enter the number of dice re-rolled");
                    reroll = Integer.parseInt(scan.nextLine());
                    System.out.println(averageTotalRerollNum(size, dice, reroll));
                    break;
                case 10:
                    out.println("What number higher/lower than?");
                    number = Integer.parseInt(scan.nextLine());
                    out.println("High(true) or low(false)?");
                    high = Boolean.parseBoolean(scan.nextLine());
                    System.out.println(probRollMoreLessXonYDice(size, dice, high, number));
                    break;
                case 11:
                    System.out.println(probOfXNuminDice(size, dice));
                    break;
            }

            out.println("Run again? Y N");
            String ans = scan.nextLine();
            if(!(ans.equals("Y") || ans.equals("y"))){running = false;}
        }


        printMat(rollMultiDiffSizesList(20, 3, 8, 3 ));
        printMat(rollMultiDiffSizesList(10, 1, 8, 2));

        out.println(averageMultiHighLow(20, 2, false));
        out.println(expectedVal2HighLow(20, false));

        out.println(averageSingle(20));
        out.println(averageMultiHighLow(20,2, true));
        out.println(averageMultiHighLow(20,2, false));
        out.println(averageMultiHighLow(20,3, true));
        out.println(averageMultiHighLow(20,3, false));
        out.println();

        out.println(averageMultiHighLow2(20,2, true));
        out.println(averageMultiHighLow2(20,2, false));
        out.println();

        out.println(averageRollMultiple(6, 8));
        out.println(averageRerollLessThan(12, 1, 3));
        out.println(averageTotalRerollNum(6, 8, 5));

        out.println();
        out.println(probRollMoreLessXonYDice(20, 1, true, 20));
        out.println(probRollMoreLessXonYDice(20, 2, true, 20));
        out.println(probRollMoreLessXonYDice(20, 3, true, 20));
        out.println();
        printArr(probOfXNuminDice(20, 9));//now working
        printArr(rollMultiList(20,3));
    }
    public static int roll(int size){
        return 1 + (int)(Math.random() * size);
    }
    public static int rollMultiHighLow(int size, int dice, boolean high){
        int highest = 0;
        int lowest = size + 1;
        int[] rolls = new int[dice];
        for (int i = 0; i < dice; i++) {
            rolls[i] = roll(size);
            //out.println("Running loop, roll is: " + rolls[i]);
            if(high){
                if(highest < rolls[i]){highest = rolls[i];}
            }
            else{
                if(lowest > rolls[i]){lowest = rolls[i];}
            }
        }
        if(high){return highest;}
        return lowest;
    }
    public static int rollMultiTotal(int size, int dice){
        int total = 0;
        for (int i = 0; i < dice; i++) {
            total+=roll(size);
        }
        return total;
    }
    public static int rollMultiDiffSizesTotal(int size1, int dice1, int size2, int dice2){
        return rollMultiTotal(size1, dice1) + rollMultiTotal(size2, dice2);
    }
    public static int[] rollMultiList(int size, int dice){
        int[] list = new int[dice];
        for (int i = 0; i < dice; i++) {
            list[i] = roll(size);
        }
        return list;
    }
    public static int[][] rollMultiDiffSizesList(int size1, int dice1, int size2, int dice2){
        int[][] mat = new int[2][Math.max(dice1, dice2)];
        mat[0] = rollMultiList(size1, dice1);
        mat[1] = rollMultiList(size2, dice2);
        return mat;
    }
    public static double averageSingle(int size){
        int total = 0;
        for(int i=0; i<bigNum; i++){
            total += roll(size);
        }
        return ((double)total)/bigNum;
    }
    public static double averageMultiHighLow(int size, int dice, boolean high){
        int total = 0;
        for(int i=0; i<bigNum; i++){
            total += rollMultiHighLow(size, dice, high);
        }
        return ((double)total)/bigNum;
    }
//    public static double averageMultiHighLow2(int size, int dice, boolean high){
//        int total = 0;
//        for(int i=0; i<bigNum; i++){
//            int rollOne = roll(size);
//            int rollTwo = roll(size);
//            if(high){total += Math.max(rollOne, rollTwo);}
//            else{total += Math.min(rollOne, rollTwo);}
//            //total += rollMultiHighLow(size, dice, high);
//        }
//        return ((double)total)/bigNum;
//    }
    public static double averageRollMultiple(int size, int dice){
        int total = 0;
        for(int i=0; i<bigNum; i++){
            total += rollMultiTotal(size, dice);
        }
        return ((double)total)/bigNum;
    }
    public static double averageRerollLessThan(int size, int dice, int highestRerolled){
        int total = 0;
        for(int i=0; i<bigNum; i++){
            for (int k = 0; k < dice; k++) {
                int roll = roll(size);
                if(roll > highestRerolled) {total += roll;}
                else{total += roll(size);}
            }
        }
        return ((double)total)/bigNum;
    }
    public static double averageTotalRerollNum(int size, int dice, int reroll){
        int total = 0;
        for(int i=0; i<bigNum; i++){
            ArrayList<Integer> nums = new ArrayList<>();
            if(reroll > dice){
                out.println("Error");
                break;
            }
            for (int k = 0; k < dice; k++) {
                nums.add(roll(size));
            }
            Collections.sort(nums);
            for (int m = 0; m < reroll; m++) {
                //this is just rerolling the lowest x dice, but if all rolls are above average
                // this may not be best
                if(nums.get(m) < ((double)size/2 + 0.5)){
                    nums.set(m, roll(size));
                }
            }
            for (int n = 0; n < dice; n++) {
                total += nums.get(n);
            }
        }
        return ((double)total)/bigNum;
    }
    public static double probRollMoreLessXonYDice(int size, int dice, boolean more, int number){
        int total = 0;
        for (int i = 0; i < bigNum; i++) {
            int roll = rollMultiHighLow(size, dice, more);
            if(more){
                if(roll >= number){total+=1;}
            }
            else{
                if(roll <= number){total+=1;}
            }
        }
        return ((double)total/bigNum) * 100;
    }
    public static double[] probOfXNuminDice(int size, int dice){ //Fixed :)
        double[] probCount = new double[dice+1];
        int count = 0;
        for (int i = 0; i < bigNum; i++) {
            int[] rolls = rollMultiList(size, dice);
            count = 0;
            for (int k = 0; k < dice; k++) {
                if(rolls[k] == size){count += 1;}
            }
            if(count > 0){
                probCount[count] += 1;
            }

        }
        int total = 0;
        for (int i = 0; i < dice; i++) {
            total += probCount[i+1];
        }
        probCount[0] = bigNum-(total);
        for (int i = 0; i < dice; i++) {
            probCount[i] = (probCount[i]/bigNum) * 100;
        }
        return probCount;
    }


    public static void printArr(int[] arr){
        out.print("[");
        for (int i = 0; i < arr.length-1; i++) {
            out.print(arr[i]+", ");
        }
        out.println(arr[arr.length-1] + "]");
    }
    public static void printArr(double[] arr){
        out.print("[");
        for (int i = 0; i < arr.length-1; i++) {
            out.print(arr[i]+", ");
        }
        out.println(arr[arr.length-1] + "]");
    }
    public static void printMat(int[][] mat){
        for (int i = 0; i < mat.length; i++) {
            printArr(mat[i]);
        }
    }
    
    public static double averageSingleStats(int size){
        double tot = 0;
        for (int i = 1; i < size+1; i++) {
            tot += i;
        }
        return tot/size;
    }
    public static double standardDeviationSingle(int size){
        double sum = 0;
        double mean = averageSingleStats(size);
        for (int i = 1; i < size+1; i++) {
            sum += (i-mean) * (i-mean);
        }
        return Math.sqrt(sum/size);
    }
    public static double averageMultiStats(int size, int dice){
        return averageSingleStats(size) * dice;
    }
    public static double standardDeviationMulti(int size, int dice){
        return Math.sqrt(standardDeviationSingle(size) * standardDeviationSingle(size) * dice);
    }
    public static double expectedVal2HighLow(int size, boolean high){
        if(high){
            return (1.0/3) * ((2 * size) + 1);
        }
        return (1.0/3) * (1 + (size/10.0)); // FIX MEEEEE!!!! figure out the weird math stuff. size shouldn't be there, but I don't know what should
    }


}

