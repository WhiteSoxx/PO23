package xxl.Types;

public class Reference extends Value{

    /** This class' constructor.
     * @param value saves the content that was given
     */
    public Reference(String value){
        super.setValue(value);
    }

    /** This class' constructor. */
    public Reference(){
        super();
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
