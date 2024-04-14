package xxl.Operators;

import xxl.Content;
import xxl.Types.Cell;
import xxl.Types.LiteralInt;

import java.util.List;

/**
 * Returns the sum of numbers in a given ArrayList.
 * 
 * @param list the list containing the numbers to be used.
 */
public class Add implements Content{
    @Override
    public String operator(List<Cell> list){
        try {
            LiteralInt first = (LiteralInt) list.get(0).getValue();
            LiteralInt second = (LiteralInt) list.get(1).getValue();
            int sum = first.getIntValue() + second.getIntValue();
            return String.valueOf(sum);
        } catch (ClassCastException e) {
            return "#VALUE";
        }
    }
}