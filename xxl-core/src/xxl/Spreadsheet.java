package xxl;

import xxl.Types.*;
import xxl.exceptions.UnrecognizedEntryException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;
import java.lang.NumberFormatException;
import java.util.Collections;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202308312359L;
    /** A list of SpreadSheet users. */
    private List<User> _users = new ArrayList<>();
    /** SpreadSheet name*/
    private String _filename = null;
    /** SpreadSheet has been changed(True) or not(False) */
    private boolean _changed = true;
    /** Memory Bank of the SpreadSheet */
    private Storage _storage;
    /** SpreadSheet operation. */
    private Spreadsheet _cutBuffer = null;

    /**
     * This class' constructor. 
     * Creates the memory of the SpreadSheet 
     * 
     * @param lines intended amount of lines.
     * @param columns intended amount of columns.
     */
    public Spreadsheet(int line, int column){
        _storage = new Storage(line, column);
    }

    /**
    * @return the current name of the SpreadSheet.
    */
    public String getFileName(){
        return _filename;
    }

    /**
    * @param name sets a new name for the spreadsheet
    */
    public void setFileName(String name){
        _filename = name;
    }

    public Storage getStorage(){
        return _storage;
    }

    /**
    * @return if the SpreadSheet has been altered or not.
    */
    public boolean wasChanged(){
        return _changed;
    }

    /**
    * @param b sets a different state for spreadsheet.
    */
    public void setChangedStatus(boolean b){
        _changed = b;
    }

    /**
    * @return the operation cutBuffer.
    */
    public Spreadsheet getCutBuffer(){
        return _cutBuffer;
    }

    /**
    * @param cutbuffer the new cutBuffer.
    */
    public void setCutBuffer(Spreadsheet cutbuffer){
        _cutBuffer = cutbuffer;
    }

    /**
   * Adds a new user.
   * 
   * @param user the user to be added.
   */
    public void addUser(User user){
        if(!_users.contains(user)) {
            _users.add(user);
            user.addSpreadsheet(this);
        }
    }

    /**
     * Sees if there is any anomaly with the string s.
     * In order, it confirms if the number is negative, a integer or if it is 
     * inside the dimension of the matrix.
     *
     * @param s string be confirmed 
     * @throws IllegalArgumentException if there is something wrong with the string s.
     * @throws IndexOutOfBoundsException if the string s is trying to access memory that doesn't exist.
     */
    public void checkPosition(String s) throws IllegalArgumentException, IndexOutOfBoundsException{
        List<String> values = new ArrayList<>();
        int i = 0;
        String position = ""; 
        for(int ix = 0; ix < s.length() ; ix++){
            String value = s.charAt(ix) + "";
            if(!value.equals(";") && !value.equals(":") && !value.equals(",")) 
                position += value;
            else{
                values.add(i++, position);
                position = "";
            }
        }
        values.add(i, position);
        for(int ix = 0; ix < values.size() ; ix++){
            if(values.get(ix).contains("-"))
                throw new IllegalArgumentException();
            try{
                Integer.parseInt((values.get(ix)));
            }
            catch(NumberFormatException e){
                throw new IllegalArgumentException();
            }
            if(ix % 2 == 0 && 
                (Integer.valueOf(values.get(ix)) > _storage.getLines() 
                    || Integer.valueOf(values.get(ix)) == 0))
                throw new IndexOutOfBoundsException();
            else if(ix % 2 == 1 && 
                (Integer.valueOf(values.get(ix)) > _storage.getColumns()
                    || Integer.valueOf(values.get(ix)) == 0))
                throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Redirects the content inside the string s to check if there is any problem 
     * with the content.
     *
     * @param s string to be confirmed.
     * @throws IllegalArgumentException if there is something wrong with the string s.
     * @throws IndexOutOfBoundsException if the string s is trying to access memory that doesn't exist.
     */
    public void checkInput(String s) throws IllegalArgumentException, IndexOutOfBoundsException{
        if(s.contains(",")){
            String[] range = s.split("\\,");
            // (a;b,c;d)
            if(range[0].contains(";") && range[1].contains(";")){
                checkPosition(range[0]);
                checkPosition(range[1]);
            }
            // (a,b)
            else if (!range[0].contains(";") && !range[1].contains(";")){
                return;
            }
            // (c,a;b)
            else if (!range[0].contains(";") && range[1].contains(";")) {
                checkPosition(range[1]);
            }
            // (a;b,c)
            else if (range[0].contains(";") && !range[1].contains(";")) {
                checkPosition(range[0]);
            }
        }
        else{
            checkPosition(s);
        }
    }

     /**
     * Checks if it the operation exists.
     *
     * @param operation the operation to check.
     * @throws UnrecognizedEntryException if the operation doesn't exist.
     */
    public void checkOperator(String operation)throws UnrecognizedEntryException{
        switch (operation){
            case "ADD" : break;
            case "SUB" : break;
            case "MUL" : break;
            case "DIV" : break;
            case "AVERAGE" : break;
            case "PRODUCT" : break;
            case "CONCAT" : break;
            case "COALESCE" :break;
            default : throw new 
                UnrecognizedEntryException(operation);
        }
    }

    /**
     * Insert specified content in specified range.
     *
     * @param rangeSpecification the specified range.
     * @param contentSpecification the specified content.
     * @throws UnrecognizedEntryException if there is something wrong with the position.
     * @throws IllegalArgumentException if there is something wrong with the position.
     * @throws IndexOutOfBoundsException if the position are try to access memory that doesnt exist.
     */
    public void insertContents(String[] position, String contentSpecification) 
    throws UnrecognizedEntryException , IllegalArgumentException, IndexOutOfBoundsException {
        try{
            if(contentSpecification.contains("=")){
                String content = contentSpecification.replace("=", "");
                if(!content.contains(",") && content.contains(";") && 
                    !content.contains(":") && !content.contains(")") 
                        && !content.contains("("))
                    checkInput(content);
                else{
                    String[] operation = content.split("\\(");
                    operation[1] = operation[1].replace(")", "");
                    checkInput(operation[1]);
                    checkOperator(operation[0]);
                }
            }
            _storage.store(position, contentSpecification);
            this.setChangedStatus(true);
        }
        catch(IndexOutOfBoundsException |IllegalArgumentException | UnrecognizedEntryException e){
            throw new UnrecognizedEntryException(contentSpecification);
        }
    }

    /**
     * Handles a succession/chain of operators/references.
     * 
     * @param list a list containing the operators and/or references.
     * @return list with
     */
    public List<Cell> Chain(List<Cell> list){
        for(int ix = 0; ix < list.size() ; ix++){
            //will use class cast method to determine object's class.
            try {
                Operator c = (Operator) list.get(ix).getValue();
                String[] operation = c.getValue().split("\\(");
                List<Cell> value = _storage.getValues(operation[1].replace(")", ""));
                value = Chain(value);
                String s = c.getOperator().operator(value);
                list.remove(list.get(ix));
                list.add(ix--,new Cell(s));
                continue;
            }
            catch (ClassCastException e) {}
            try {
                Reference c = (Reference) list.get(ix).getValue();
                List<Cell> value = _storage.getValues(c.getValue().replace("=", ""));
                list.remove(list.get(ix--));
                for(Cell celula: value)
                    list.add(celula);
                continue;
            } 
            catch (ClassCastException e) {}
        }
        return list;
    }   


    /**
     * It receives position and it goes and gets the values of those position 
     * saving it in a list
     *
     * @param position
     * @return A list with the elements asked from the position
     */
    public List<String> getValues(String position){
        int i = 0;
        List<String> values = new ArrayList<>();
        
        List<Cell> content = _storage.getValues(position);
        for(Cell cell: content){
            String s = "";
            boolean in = false;
            String contentSpecification = cell.getValue().getValue();
            // |a
            try{
                LiteralInt c = (LiteralInt) cell.getValue();
                s += c.getValue();
                in = true;
            }
            catch(ClassCastException e){}
            // |'a
            try{
                LiteralString c = (LiteralString) cell.getValue();
                s += c.getValue();
                in = true;
            }
            catch(ClassCastException e){}
            // = a;b
            try{
                Reference c = (Reference) cell.getValue();
                List<Cell> list = _storage.getValues(c.getValue().replace("=", ""));
                list = Chain(list);
                s = list.get(0).getValue().getValue();
                if(s.equals("")){
                    s += "#VALUE";
                }
                s += c.getValue();
                in = true;
            }
            catch(ClassCastException e){}
            // =operator()
            try{
                Operator c = (Operator) cell.getValue();
                String[] operation = contentSpecification.split("\\(");
                List<Cell> list = _storage.getValues(operation[1].replace(")", ""));
                list = Chain(list);
                s = c.getOperator().operator(list) + c.getValue();
                in = true;
            }
            catch(ClassCastException e){}
            if(!in)
                s = contentSpecification;
            values.add(i++,s);
        }
        return values;
    }

    /**
     * Receives a range, confirms it, and asks for the values of that range
     * And then gives a string to present to the user in the right format:
     * a;b|content
     *
     * @param range
     * @throws IllegalArgumentException if there is something wrong with the position.
     * @throws IndexOutOfBoundsException if the position are try to access memory that doesnt exist.
     * @return A list with the elements asked from the position with the right format
     */
    public List<String> printValues(String range) throws IllegalArgumentException, IndexOutOfBoundsException{
        checkInput(range);
        List<String> values = new ArrayList<>();
        List<String> content = getValues(range);
        String send = "";
        if(range.contains(":")){
            String[] position = range.split(":");
            String[] start = position[0].split(";");
            String[] end = position[1].split(";");
            int i = 0;

            if (!start[0].equals(end[0]) && !start[1].equals(end[1]))
                throw new IllegalArgumentException();

            if (start[0].equals(end[0])){
                for(int ix = Integer.valueOf(start[1]); 
                    ix <= Integer.valueOf(end[1]); ix++)
                {
                    send = start[0] + ";" + String.valueOf(ix) 
                        + "|" + content.get(i);
                    values.add(i++,send);
                }
            }
            else{
                for(int ix = Integer.valueOf(start[0]); 
                ix <= Integer.valueOf(end[0]); ix++)
                {
                    send = String.valueOf(ix) + ";" + 
                        start[1] + "|" + content.get(i);

                    values.add(i++,send);
                }
            }   
        }
        else{
            send = range + "|" + content.get(0);
            values.add(0,send);
        }
        values.add(String.valueOf(content.size()));
        return values;
    }

    /**
     * Copies the cells in the specified range, adding them to a new
     * cutbuffer.
     * 
     * @param range the specified range.
     * @throws IllegalArgumentException if the given range is absurd.
     * @throws IndexOutOfBoundsException if the given range exceeds the spreadsheet's bounds.
    */
    public void copy(String range) throws IllegalArgumentException, IndexOutOfBoundsException{
        checkInput(range);
        List<String> content = getValues(range);

        if(range.contains(":")){
            String[] position = range.split(":");
            String[] start = position[0].split(";");
            String[] end = position[1].split(";");
            
            if (!start[0].equals(end[0]) && !start[1].equals(end[1]))
                throw new IllegalArgumentException();

            //lines iguais
            if (start[0].equals(end[0])){
                int n_lines_cb = 1;
                int n_columns_cb = Integer.valueOf(end[1]) - Integer.valueOf(start[1]) + 1;
                setCutBuffer(new Spreadsheet(n_lines_cb, n_columns_cb));
                int line = 1;
                for(int column = 1, i = 0; column <= n_columns_cb; column++, i++){
                    String[] coord = new String[2];
                    coord[0] = String.valueOf(line);
                    coord[1] = String.valueOf(column);
                    _cutBuffer.getStorage().store(coord, content.get(i));
                }
            }
            //columns iguais
            else{
                int n_lines_cb = Integer.valueOf(end[0]) - Integer.valueOf(start[0]) + 1;
                int n_columns_cb = 1;
                setCutBuffer(new Spreadsheet(n_lines_cb, n_columns_cb));
                int column = 1;
                for(int line = 1, i = 0; line <= n_lines_cb; line++, i++){
                    String[] coord = new String[2];
                    coord[0] = String.valueOf(line);
                    coord[1] = String.valueOf(column);
                    _cutBuffer.getStorage().store(coord, content.get(i));
                }
            }   
        }
        else{
            setCutBuffer(new Spreadsheet(1, 1));
            String[] coord = new String[2];
            coord[0] = String.valueOf(1);
            coord[1] = String.valueOf(1);
            _cutBuffer.getStorage().store(coord, content.get(0));
        }
    }
   

    /**
     * Advances the given line, adding the content in s as it progresses.
     * 
     * @param line the given line.
     * @param column the given column.
     * @param values the list containing the values to add.
     */
    public void nextLine(int line, String column, List<String> values){
        for(String s: values){
            String[] place = (String.valueOf(line++) + ";" + 
                column).split("\\;");
            try{
                insertContents(place, s);
            }
            catch(UnrecognizedEntryException e){
                throw new IllegalCallerException();
            }
        }
    }

    /**
     * Advances the given column, adding the content in s as it progresses.
     * 
     * @param line the given line.
     * @param column the given column.
     * @param values the list containing the values to add.
     */
    public void nextColumn(String line, int column, List<String> values){
        for(String s: values){
            String[] place = (line + ";" 
                + String.valueOf(column++)).split("\\;");
            try{
                insertContents(place, s);
            }
            catch(UnrecognizedEntryException e){
                throw new IllegalCallerException();
            }
        }
    }
    
    /**
     * Pastes the content in the current cutbuffer to a specified range.
     * This range can be the position to a single cell.
     * 
     * @param range the given range.
     */
    public void paste(String range){
        checkInput(range);
        int line = _cutBuffer.getStorage().getLines();
        int column = _cutBuffer.getStorage().getColumns();
        String cb = "1;1:" + String.valueOf(line) + ";" + String.valueOf(column);
        List<String> values = _cutBuffer.getValues(cb);
        for(int ix = 0; ix < values.size(); ix++){
            if(values.get(ix).contains("=")){
                int jx = values.get(ix).indexOf("=");
                values.set(ix,values.get(ix).split(
                    values.get(ix).charAt(jx-1) + "")[1]);
            }
        }
        if(range.contains(":")){
            String[] position = range.split(":");
            String[] start = position[0].split(";");
            String[] end = position[1].split(";");
            int i;

            if (start[0].equals(end[0])){
                i = Integer.valueOf(start[1]);
                nextColumn(start[0], i, values);
            }
            else{
                i = Integer.valueOf(start[0]);
                nextLine(i, start[1], values);
            }   
        }
        else{
            String[] place = range.split(";");
            if(line == 1){
                int i = Integer.valueOf(place[1]);
                nextColumn(place[0], i, values);

            }
            else{
                int i = Integer.valueOf(place[0]);
                nextLine(i, place[1], values);
            }
        }
    }

    /**
     * Changes the content in a specified range to a specified content.
     * 
     * @param range the specified range.
     * @param content the specified content.
     */
    public void changeRangeContent(String range, String content){
        checkPosition(range);
        if(range.contains(":")){
            String[] position = range.split(":");
            String[] start = position[0].split(";");
            String[] end = position[1].split(";");

            if (!start[0].equals(end[0]) && !start[1].equals(end[1]))
                throw new IllegalArgumentException();

            if (start[0].equals(end[0])){
                for(int ix = Integer.valueOf(start[1]); 
                    ix <= Integer.valueOf(end[1]); ix++) {
                    String[] place = (String.valueOf(start[0]) + ";" 
                        + String.valueOf(ix)).split("\\;");
                    try{
                        insertContents(place, content);
                    }
                    catch(UnrecognizedEntryException e){
                        throw new IllegalCallerException();
                    }
                }
            }
            else{
                for(int ix = Integer.valueOf(start[0]); 
                ix <= Integer.valueOf(end[0]); ix++) {
                    String[] place = (String.valueOf(ix) + ";" + 
                        String.valueOf(start[1])).split("\\;");
                    try{
                        insertContents(place, content);
                    }
                    catch(UnrecognizedEntryException e){
                        throw new IllegalCallerException();
                    }
                }
            }   
        }
        else{
            String[] place = range.split(";");
            try{
                insertContents(place, content);
            }
            catch(UnrecognizedEntryException e){
                throw new IllegalCallerException();
            }
        }
    }

    /**
     * Looks for all cells that hold a specified value in the spreadsheet.
     * 
     * @param search the value to look for.
     * @return the cells that hold the specified value.
     */
    public List<String> showValue(String search){
        List<String> content = new ArrayList<>();

        for(int ix = 1; ix <= _storage.getLines(); ix++){
            for(int jx = 1; jx <= _storage.getColumns(); jx++){
                List<String> values = getValues(
                        String.valueOf(ix) + ";" + String.valueOf(jx));
                String[] value = values.get(0).split("=");
                if (values.get(0).equals(search) || value[0].equals(search)) {
                    content.add(String.valueOf(ix) + ";" + 
                        String.valueOf(jx) + "|" + values.get(0));
                }
            }
        }
        return content;
    }

    /**
     * Looks for all cells that hold a specified function in the spreadsheet.
     * 
     * @param operator the function to look for.
     * @return the cells that use the specified function.
     */
    public List<String> showFunction(String operator){

        HashMap<String,ArrayList<String>> values = new HashMap<String,ArrayList<String>>();

        for(int ix = 1; ix <= _storage.getLines(); ix++){
            for(int jx = 1; jx <= _storage.getColumns(); jx++){
                Cell value = _storage.getCell(String.valueOf(ix), 
                    String.valueOf(jx));
                try{
                    Operator op = (Operator) value.getValue();
                    if (op.getValue().contains(operator)) {
                        String coord = String.valueOf(ix) + ";" + 
                            String.valueOf(jx);
                        String name = op.getValue().replace("=", "").split("\\(")[0];
                        if(values.get(name) == null) {
                            ArrayList<String> values1 = new ArrayList<String>();
                            values1.add(coord + "|" + getValues(coord).get(0));
                            values.put(name, values1);
                        }
                        else{
                            ArrayList<String> values1 = values.get(name);
                            values1.add(coord + "|" + getValues(coord).get(0));
                            values.put(name, values1);
                        }
                    }
                }
                catch(ClassCastException e){}
            }
        }
        ArrayList<String> sortedKeys = new ArrayList<String>(values.keySet());
        Collections.sort(sortedKeys);
        List<String> list = new ArrayList<>();
        for(String s : sortedKeys){
            for(String value : values.get(s))
                list.add(value);
        }
        return list;
    }

    /**
     * Gives a list of all values the cutbuffer is currently holding.
     * Does nothing if nothing has been copied yet.
     * 
     * @return a list of all values currently in the cutbuffer.
     */
    public List<String> showCutBuffer() {
        if(_cutBuffer != null) {
            int line = _cutBuffer.getStorage().getLines();
            int column = _cutBuffer.getStorage().getColumns();
            String position = "1;1:" + String.valueOf(line) + ";" + String.valueOf(column);
            return _cutBuffer.printValues(position);
        }
        return new ArrayList<String>();
    }

}
