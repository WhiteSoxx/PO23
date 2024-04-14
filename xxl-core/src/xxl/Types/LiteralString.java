package xxl.Types;

public class LiteralString extends Value{

    /**
     * This class' constructor.
     * @param value initiates the content of the cell
     */
    public LiteralString(String value){
        super.setValue(value);
    }

    /**
     * @return the content of the cell
     */
    public String getValue(){
        return super.getValue();
    }

    /**
     * @param value sets a new content
     */
    public void setValue(String value){
        super.setValue(value);
    }
}
