package xxl.Operators;

import java.util.List;

import xxl.Content;
import xxl.Types.Cell;
import xxl.Types.LiteralInt;

/**
 * Returns the value of the successive multiplication of 
 * an unknown amount of numbers in a given ArrayList.
 * 
 * @param list the list containing the numbers to be used.
 */
public class Product implements Content{
    @Override
    public String operator(List<Cell> list){
        int result = 1;
        for(Cell cell : list){
            try {
                LiteralInt value = (LiteralInt) cell.getValue();
                result *= value.getIntValue();
            } catch (ClassCastException e) {
                return "#VALUE";
            }
        }
        return String.valueOf(result);
    }
}