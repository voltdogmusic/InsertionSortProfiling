import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/*
Program Flow:

//This Class
1. Main method
2. manualClassLoader calls load method to warm up JVM with dummy classes
3. call makeRandomIntegerArrays with different array sizes n and add random arrays to randomArrayAL
4. cycle thru randomArrayAL and send the arrays to the BenchmarkSorts class runSorts method

//BenchMarkSorts Class
5. runSorts method within the BenchmarkSorts class, call iterativeSort from the YourSorts class on the current
array to sort
6. ensure the array is sorted within a try catch block using arraySortedOrNot method

//YourSorts Class
7. call the displayReport method which calls the getCount and getTime method

//BenchMarkSorts Class
8. call recursive sort on the current array to sort, ensure array is sorted, call the displayReport method

//This Class
9. back within SortMain, convert the critical operations and execution times to doubles for variance calculations
10. call the calculateAverage methods and the getCoefVariance with the statistics object
11. print the results to the console
12. write the data to an excel spreadsheet
 */
public class SortMain {

    private static SortMain sortMainObj = new SortMain();
    private static Statistics statObj = new Statistics();
    private static BenchmarkSorts benchmarkSortsObj = new BenchmarkSorts();

    //int[] sizes = {10, 50, 100, 200, 300, 400, 500, 750, 1000, 1500};
    private static int[] sizes = {10, 20}; //sizes of each individual array, n
    static final int numberOfArrays = 50; //the number of arrays to create for each size n

    public ArrayList<int[]> randomArrayAL = new ArrayList<>();//array list to store random arrays

    //results converted to doubles, used to calculate variance
    private static ArrayList<Double> doublesCriticalOpAL = new ArrayList<>();
    private static ArrayList<Double> doublesCriticalOpRec = new ArrayList<>();
    private static ArrayList<Double> doublesExecTimeALIt = new ArrayList<>();
    private static ArrayList<Double> doublesExecTimeALRec = new ArrayList<>();


    public void makeRandomIntegerArrays(int lengthOfArray) {

        Random randomObj = new Random();

        for (int a = 0; a < this.numberOfArrays; ++a) {//first loop to create multiple arrays
            int randomArray[] = new int[lengthOfArray];
            for (int i = 0; i < lengthOfArray; ++i) {//second loop to add values to the arrays
                randomArray[i] = randomObj.nextInt(1000);
            }
            randomArrayAL.add(randomArray);//add the arrays to an array list
        }
    }
    private static void convertToDoubles(){

        for (Integer i : YourSort.sumCriticalOperationAL) {

            doublesCriticalOpAL.add(Double.valueOf(i));
        }

        for (Integer i : YourSort.sumCriticalOperationALRec) {
            doublesCriticalOpRec.add(Double.valueOf(i));
        }

        for (Long i : YourSort.sumExecTimeAL) {
            doublesExecTimeALIt.add(Double.valueOf(i));
        }

        for (Long i : YourSort.sumExecTimeALRec) {
            doublesExecTimeALRec.add(Double.valueOf(i));
        }

    }
    private static double calculateAverage(ArrayList<Integer> average) {
        ArrayList<Double> doubles = new ArrayList<>();
        for (Integer i : average) {

            doubles.add(Double.valueOf(i));
        }

        double sum = 0;

        for (double d : doubles) {
            sum += d;
        }
        return sum / average.size();
    }
    private static long calculateAverageLong(ArrayList<Long> average) {
        long sum = 0;
        for (long d : average) {
            sum += d;
        }
        return sum / average.size();
    }
    private static class Statistics {
        double getCoefVariance(double mean, ArrayList<Double> data) {
            double temp = 0;
            for (double a : data) {
                temp += (a - mean) * (a - mean);    //sum each (data point - mean)^2
            }
            return (temp / (data.size() - 1)) / mean;   //divide by n-1.....divide by mean (n-1 b/c incomplete sample)
        }
    }//used to calculate variance

    public static void main(String[] args) {

        //1. warm up the JVM by creating a bunch of dummy classes
        ManualClassLoader.load(); System.out.println("Warming Up Complete");


        //2. Header of console output
        System.out.println("Size (n)[                           Iterative                    ][                     Recursive]");
        System.out.println("         Avg Critical   |     Coef       |   Avg     |    Coef    |     Avg Critical  |      Coef      |    Avg     |   Coef ");
        System.out.println("         operation count|    Var Count   |  Exec Time|   Var Time |    operation count|     Var Count  |   Exec Time|   Var Time ");


       //3. creating a print writer and a csv file on the desktop to write the output to
        PrintWriter pw = null;
        try {

            String userHome = System.getProperty("user.home") + "/Desktop";
            pw = new PrintWriter(new File(userHome + "/InsertionSortIterativeVsRecursive.csv"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //4. StringBuilder to write the output to a csv file
        StringBuilder builder = new StringBuilder();

        //4.5 these are the titles of the csv file columns
        builder.append(" Data Set Size n,,,,Iterative,,,,Recursive " + "\n");
        builder.append(",Average Critical Operation Count, " + " Coefficient of Variation Count, " + " Average Execution Time, "  + " Coefficient of Variance of Time (end of iterative row), " + " Average Critical Operation Count, " + " Coefficient of Variation Count, " + " Average Execution Time, "  + " Coefficient of Variance of Time, " + "\n");

        //5. creating the correct number of random integer arrays and using the BenchMarkSorts object to sort them
        int x;
        for (x = 0; x < sizes.length; ++x) {
            sortMainObj.makeRandomIntegerArrays(sizes[x]);//make random arrays with sizes from sizes array
            for (int i = 0; i < sortMainObj.randomArrayAL.size(); ++i) {
                int[] arrayFromAL = sortMainObj.randomArrayAL.get(i); //get random arrays into an array of arrays
                int[] arrayFromALCopy = arrayFromAL.clone();//second array for recursive, first for iterative
                benchmarkSortsObj.runSorts(arrayFromAL, arrayFromALCopy);//send random arrays to runSort method
            }

            //6. converts all the results to doubles for variance and average calculations
            convertToDoubles();

            //7. printing the results to console output
            int numberOfSpaces = 12;
            String space = String.format("%"+ numberOfSpaces +"s", " ");

System.out.println(
              sizes[x]
              + space +
String.valueOf((int)calculateAverage(YourSort.sumCriticalOperationAL))
              + space +
String.valueOf((int)statObj.getCoefVariance(calculateAverage(YourSort.sumCriticalOperationAL), doublesCriticalOpAL)
              + space +
String.valueOf((int)calculateAverageLong(YourSort.sumExecTimeAL))
              + space +
String.valueOf((int)statObj.getCoefVariance((double) calculateAverageLong(YourSort.sumExecTimeAL), doublesExecTimeALIt))
              + space +
String.valueOf((int)calculateAverage(YourSort.sumCriticalOperationALRec))
              + space +
String.valueOf((int)statObj.getCoefVariance(calculateAverage(YourSort.sumCriticalOperationALRec), doublesCriticalOpRec))
              + space +
String.valueOf((int)calculateAverageLong(YourSort.sumExecTimeALRec))
              + space +
String.valueOf((int)statObj.getCoefVariance((double)calculateAverageLong(YourSort.sumExecTimeALRec), doublesExecTimeALRec))));


//8. append the results to the StringBuilder
builder.append(

        sizes[x] + "," +

        (int)calculateAverage(YourSort.sumCriticalOperationAL) + "," +

        (int)statObj.getCoefVariance(calculateAverage(YourSort.sumCriticalOperationAL), doublesCriticalOpAL) + "," +

        (int)calculateAverageLong(YourSort.sumExecTimeAL) + "," +

        (int)statObj.getCoefVariance((double) calculateAverageLong(YourSort.sumExecTimeAL), doublesExecTimeALIt) + "," +

        (int)calculateAverage(YourSort.sumCriticalOperationALRec) + "," +

        (int)statObj.getCoefVariance(calculateAverage(YourSort.sumCriticalOperationALRec), doublesCriticalOpRec) + "," +

        (int)calculateAverageLong(YourSort.sumExecTimeALRec) + "," +

        (int)statObj.getCoefVariance((double)calculateAverageLong(YourSort.sumExecTimeALRec), doublesExecTimeALRec) + "," +

        "\n");

        }//end "master" for loop

        //8.5 send the StringBuilder to the PrintWriter and write to the CSV file
        pw.write(builder.toString());
        pw.close();

        System.out.println("Csv file created and placed on desktop");
    }//end main
}//end SortMain

//the Dummy class and the ManualClassLoader are used to create many Dummy classes and warm up the JVM
 class Dummy {
    public void m() {
    }
}
 class ManualClassLoader {
    protected static void load() {
        for (int i = 0; i < 100000; i++) {
            Dummy dummy = new Dummy();
            dummy.m();
        }
    }
}



