package quickSort;

public class QuickSortable implements Sortable{
    private int[] arr;
    private int low;
    private int high;
    private QuickSortable(int[] arr, int low, int high){
        this.arr = arr;
        this.low = low;
        this.high = high;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }
    public static QuickSortable createInstance(int[] arr, int low, int high){
        if (arr == null || arr.length == 0 || low < 0 || high >= arr.length )
            throw new RuntimeException("Geçersiz işlem!!! ");
        return new QuickSortable(arr,low,high);
    }

}
