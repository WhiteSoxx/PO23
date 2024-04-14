package xxl.Types;

import java.io.Serializable;

/**
 * Class representing each slot in the spreadsheet's table.
 */
public class Cell implements Serializable{
    /**Content that each cell has */
    private Value _value = new Value();

    /**
     * This class' constructor.
     */
    public Cell(){}

    /**
     * This class' constructor.
     * @param value new content of the cell
     */
    public Cell(String value){
        saveValue(value);
    }

    /**
    * @return the Content.
    */
    public Value getValue() {
        return _value;
    }
    /**
    * @param value sets new content.
    */
    public void setValue(Value value) {
        _value = value;
    }

    /**
     * @param key saves the key in a cell value
     */
    public void saveValue(String key){
        _value = _value.saveValue(key);
    }

}
