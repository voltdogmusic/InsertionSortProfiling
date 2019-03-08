public class BenchmarkSorts {

    private YourSort yourSortObj = new YourSort();

    public void runSorts(int[] arrayToSort, int[] arrayToSortCopy) {

        //call iterative sort
        yourSortObj.iterativeSort(arrayToSort);

        //convert to string and print out
        //String currentArrayToSort = Arrays.toString(arrayToSort);

        //ensure array is sorted
        try {
            arraySortedOrNot(arrayToSort);
        } catch (UnsortedException e) {
            e.printStackTrace();
        }

        //display report with iterative option selected
        char i = 'i';
        displayReport(i);

        yourSortObj.recursiveSort(arrayToSortCopy);

        //ensure array is sorted after rec sort
        try {
            arraySortedOrNot(arrayToSortCopy);
        } catch (UnsortedException e) {
            e.printStackTrace();
        }

        //call rec sort display report
        char r = 'r';
        displayReport(r);
    }

    //this method is used to get the critical operations count and execution time for each sorting method
    //it was also used to display results to the console during debugging
    private void displayReport(char a) {

        if (a == 'i') {
            yourSortObj.getCount(a);
            yourSortObj.getTime(a);
        } else if (a == 'r') {
            yourSortObj.getCount(a);
            yourSortObj.getTime(a);
        } else {
            System.out.println("Please input lowercase i or r");
            return;
        }
    }

    private static boolean arraySortedOrNot(int arr[]) throws UnsortedException {
        int arrLength = arr.length;
        if (arrLength == 0 || arrLength == 1)
            return true;
        for (int i = 1; i < arrLength; i++)
            if (arr[i - 1] > arr[i]) {
                throw new UnsortedException();
            }
        return true;
    }
}
