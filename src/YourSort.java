import java.util.ArrayList;
import java.util.Arrays;

//This class implements the SortInterface and contains the relevant iterative and recursive code for InsertionSort
public class YourSort implements SortInterface {

    private long elapsedTime;
    private long elapsedTimeRec;

    private ArrayList<Integer> criticalOperationAL = new ArrayList<>();
    private ArrayList<Integer> criticalOperationALRec = new ArrayList<>();

    public static ArrayList<Integer> sumCriticalOperationAL = new ArrayList<>();
    public static ArrayList<Integer> sumCriticalOperationALRec = new ArrayList<>();
    public static ArrayList<Long> sumExecTimeAL = new ArrayList<>();
    public static ArrayList<Long> sumExecTimeALRec = new ArrayList<>();

    @Override
    public void iterativeSort(int[] inputArray) {

        long startTime = System.nanoTime();
        int lengthOfArray = inputArray.length;

        //start at 1 b/c 0th element is sorted
        for (int arrayIndexLoop = 1; arrayIndexLoop < lengthOfArray; arrayIndexLoop++) {

            //get the index at the arrayIndexLoop
            int indexToSort = arrayIndexLoop;
            //get the value at that index
            int valueToSort = inputArray[indexToSort];
            //we decrement the index down as we are comparing the value to insert
            //to values on the left of it within the array
            //if the value to the left of the value to sort is bigger than our value
            //of interest, swap them
            while (indexToSort > 0 && valueToSort < inputArray[indexToSort - 1]) {
                criticalOperationAL.add(0);
                //this operation will change based on how sorted the incoming array is
                inputArray[indexToSort] = inputArray[indexToSort - 1];
                indexToSort--;
            }
            //when we exit the loop, the value is no longer smaller than
            //the value to the left of it (indexToSort-1) and we insert it at indexToSort
            inputArray[indexToSort] = valueToSort;
        }
        long stopTime = System.nanoTime();
        elapsedTime = stopTime - startTime;
        Arrays.toString(criticalOperationAL.toArray());
    }

    public void recursiveSort(int arr[]) {

        long startTime2 = System.nanoTime();
        int n = arr.length;
        recursiveSortHelper(arr, n);
        long stopTime2 = System.nanoTime();
        elapsedTimeRec = stopTime2 - startTime2;
    }

    private void recursiveSortHelper(int arr[], int n) {

        // Base case
        if (n <= 1)
            return;
        // Sort first n-1 elements
        recursiveSortHelper(arr, n - 1);
        criticalOperationALRec.add(0);
        // Insert last element at its correct position
        // in sorted array.
        int valueToMove = arr[n - 1];
        int indexOfValueToLeftOfMover =n - 2;

        /* Move elements of arr[0..i-1], that are
          greater than key, to one position ahead
          of their current position */
        while (indexOfValueToLeftOfMover >=0 && arr[indexOfValueToLeftOfMover] > valueToMove){
            arr[indexOfValueToLeftOfMover+1] = arr[indexOfValueToLeftOfMover];
            indexOfValueToLeftOfMover--;
        }
        arr[indexOfValueToLeftOfMover+1] = valueToMove;
    }

    //This method gets "critical operations" (criticalOperationsAl/criticalOperationsAlRec) incremented within each sorting method respectively
    @Override
    public int getCount(char a) {
        if (a == 'i') {
            sumCriticalOperationAL.add(criticalOperationAL.size());
            return criticalOperationAL.size();
        } else if (a == 'r') {
            sumCriticalOperationALRec.add(criticalOperationALRec.size());
            return criticalOperationALRec.size();
        } else {
            System.out.println("Please input lowercase i or r");
            return 0;
        }
    }

    //Time for execution of each sorting method
    @Override
    public long getTime(char a) {
        if (a == 'i') {

            sumExecTimeAL.add(elapsedTime);
            return elapsedTime;
        } else if (a == 'r') {
            sumExecTimeALRec.add(elapsedTimeRec);
            return elapsedTimeRec;
        } else {
            System.out.println("Please input lowercase i or r");
            return 0;
        }

    }
}
