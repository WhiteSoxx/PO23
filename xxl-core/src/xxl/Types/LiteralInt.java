package xxl.Types;

public class LiteralInt extends Value{
    /*value in the format of a number */
    private int _value;

    /**
     * This class' constructor.
     * @param value initiates the value(_value)
     */
    public LiteralInt(String value){
        super.setValue(value);
        _value = Integer.parseInt(value);
    }

    /** This class' constructor.*/
    public LiteralInt(){
        super();
    }

    /**
     * @return the number 
     */
    public int getIntValue(){
        return _value;
    }

    /**
     * @param value sets a new number
     */
    public void setValue(int value){
        _value = value;
    }
}
