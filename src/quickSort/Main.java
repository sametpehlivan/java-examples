package quickSort;

import quickSort.QuickSort;
import quickSort.QuickSortable;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {


        int[] arr = { 123,23,12,4,12,3,4,4};
        int N = arr.length;
        QuickSort quickSort = QuickSort.createInstance();
        QuickSortable sortable = QuickSortable.createInstance(arr,0,N-1);
        quickSort.sort(sortable);
        Arrays.stream(arr).forEach(s -> System.out.print(s + " "));
        System.out.println("\n");
    }
}