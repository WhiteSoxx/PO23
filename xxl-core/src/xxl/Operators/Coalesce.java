package xxl.Operators;

import java.util.List;

import xxl.Content;
import xxl.Types.Cell;
import xxl.Types.LiteralString;

/**
 * Returns the first value that isn't null in a given ArrayList.
 * 
 * @param list the list containing the values to be used.
 */
public class Coalesce implements Content{
    @Override
    public String operator(List<Cell> list){
        String result = "'";
        for(Cell cell : list){
            try {
                LiteralString valor = (LiteralString) cell.getValue();
                result += valor.getValue().replace("'", "");
                break;
            } catch (ClassCastException e) {}
        }
        return result;
    }
}
