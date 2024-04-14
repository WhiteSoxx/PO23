package xxl.Operators;

import java.util.List;

import xxl.Content;
import xxl.Types.Cell;
import xxl.Types.LiteralString;

/**
 * Returns a String that consists of all Strings in a given ArrayList.
 * 
 * @param list the list containing the Strings to be used.
 */
public class Concat implements Content{
    @Override
    public String operator(List<Cell> list){
        String result = "'";
        for(Cell cell : list){
            try {
                LiteralString value = (LiteralString) cell.getValue();
                result += value.getValue().replace("'", "");
            } catch (ClassCastException e) {}
        }
        return result;
    }
}