package xxl.Operators;

import java.util.List;

import xxl.Content;
import xxl.Types.Cell;
import xxl.Types.LiteralInt;

/**
 * Returns the value of the subtraction of numbers in a given ArrayList.
 * 
 * @param list the list containing the numbers to be used.
 */
public class Sub implements Content{
    @Override
    public String operator(List<Cell> list){
        try {
            LiteralInt primeiro = (LiteralInt) list.get(0).getValue();
            LiteralInt segundo = (LiteralInt) list.get(1).getValue();
            int result = primeiro.getIntValue() - segundo.getIntValue();
            return String.valueOf(result);
        } catch (ClassCastException e) {
            return "#VALUE";
        }
    }
}