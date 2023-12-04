package simpleCalculate;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Math {
    public static int add(int ... values){
        BiFunction<Integer,Integer,Integer> function = (a,b)->a+b;
        return operations(function,values);
    }

    public static int sub(int ... values){
        BiFunction<Integer,Integer,Integer> function = (a,b)->a-b;
        return operations(function,values);
    }
    public static int mul(int ... values){
        BiFunction<Integer,Integer,Integer> function = (a,b)->a*b;
        return operations(function,values);
    }
    public static int div(int ... values){
        BiFunction<Integer,Integer,Integer> function = (a,b)->{
            if (b == 0) throw new RuntimeException("Illegal Argument");
            return a/b;
        };
        return operations(function,values);
    }
    public static int fact(int value){
        int total = 1;
        if ( value == 0 || value == 1) return total;
        for (int i = 0; i < value+1;i++ ){
            total *= i;
        }
        return total;
    }
    public static int abs(int value){

        return value < 0 ? value*(-1) : value;
    }
    public static int operations(BiFunction<Integer, Integer,Integer> function, int... values){

        int total = values[0];
        for (int i = 1; i< values.length ; i++){
            total = function.apply(total,values[i]);
        }
        return total;
    }
}
