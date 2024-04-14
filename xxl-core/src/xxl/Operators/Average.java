package xxl.Operators;

import java.util.List;

import xxl.Content;
import xxl.Types.Cell;
import xxl.Types.LiteralInt;

/**
 * Returns the value of the mean average of an unknown 
 * amount of numbers in a given ArrayList.
 * 
 * @param list the list containing the numbers to be used.
 */
public class Average implements Content{
    @Override
    public String operator(List<Cell> list){
        int samplesize = 0;
        int sum = 0;
        for(Cell cell : list){
            try {
                LiteralInt valor = (LiteralInt) cell.getValue();
                sum += valor.getIntValue();
                samplesize++;
            } catch (ClassCastException e) {
                return "#VALUE";
            }
        }
        return String.valueOf(sum/samplesize);
    }
}
