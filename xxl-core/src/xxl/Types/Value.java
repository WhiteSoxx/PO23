package xxl.Types;

import java.io.Serializable;

public class Value implements Serializable{
    private String _value = "";

    public Value(){}

    public Value saveValue(String newvalue){
        if(newvalue.contains("=") && newvalue.contains("(") && newvalue.indexOf("=") == 0){
            String[] operator = newvalue.split("\\(");
            return new Operator(newvalue, operator[0].replace("=", ""));
        }
        else if(newvalue.contains("=") && !newvalue.contains("(") && newvalue.indexOf("=") == 0){
            return new Reference(newvalue);
        }
        else if(newvalue.contains("'")){
            return new LiteralString(newvalue);
        }
        try {
            Integer.parseInt(newvalue);
            return new LiteralInt(newvalue);
        } catch (NumberFormatException e) {}
        
        Value value = new Value();
        value.setValue(newvalue);
        return value;
    }

    public void setValue(String value){
        _value = value;
    }

    public String getValue(){
        return _value;
    }

}
