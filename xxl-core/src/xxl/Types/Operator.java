package xxl.Types;

import xxl.Content;
import xxl.Operators.*;

public class Operator extends Value{
    /*Operator that was created*/
    private Content _operator;

    /** This class' constructor. */
    public Operator(){}

    /**
     * This class' constructor.
     * @param operator saves the content that was given
     * @param function creates the operation
     */
    public Operator(String operator, String function){
        super.setValue(operator);
        switch (function){
            case "ADD":
                _operator = new Add();
                break;
            case "SUB" : 
                _operator = new Sub();
                break;
            case "MUL" : 
                _operator = new Mul();
                break;
            case "DIV"  : 
                _operator = new Div();
                break;
            case "AVERAGE" : 
                _operator = new Average();
                break;
            case "PRODUCT" : 
                _operator = new Product();
                break;
            case "CONCAT" : 
                _operator = new Concat();
                break;
            case "COALESCE" : 
                _operator = new Coalesce();
                break;
        }
    }

    /**
     * @return the content of the cell
     */
    public String getValue(){
        return super.getValue();
    }

    /** 
     * @param operator sets a new content for the cell
    */
    public void setValue(String value){
        super.setValue(value);
    }

    /**
     * @return the operator
     */
    public Content getOperator(){
        return _operator;
    }
}
