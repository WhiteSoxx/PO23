package xxl;

import java.util.ArrayList;
import java.util.List;

import xxl.Types.Cell;

import java.io.Serializable;


public class Storage implements Serializable{
    /** Matrix of Cells */
    private ArrayList<ArrayList<Cell>> _table;
    /** Number of lines of the matrix*/
    private int _lines;
    /** Number of colums of the matrix*/
    private int _columns;

    /**
     * This class' constructor. 
     * Creates a matrix-like spreadsheet. Each list has their size determined
     * by the value of columns, while the value of lines determines how many
     * lists there are. 
     * It then procedes to add an instance of Cell to each spot in each list.
     * 
     * @param lines intended amount of lines.
     * @param columns intended amount of columns.
     */
    public Storage(int lines, int columns){
        _lines = lines;
        _columns = columns;
        _table = new ArrayList<ArrayList<Cell>>(lines);
        for (int ix = 0; ix < lines; ix++){
            _table.add(new ArrayList<Cell>(columns));
            for (int jx = 0; jx < columns ; jx++){
                Cell cell = new Cell();
                _table.get(ix).add(jx, cell);
            }         
        }
    }
    
    /**
    * @return the number of lines.
    */
    public int getLines(){
        return _lines;
    }

    /**
    * @return the number of columns.
    */
    public int getColumns(){
        return _columns;
    }

    /**
    * @param columns sets the number of columns
    */
    public void setColumns(int columns){
        _columns = columns;
    }

    /**
    * @param lines sets the number of lines
    */
    public void setLine(int lines){
        _lines = lines;
    }

    /** 
    * Finds the cell in a specified line-column position.
    *
    * @param lines the line the desired cell is placed in.
    * @param columns the column the desired cell is placed in.
    * @return the cell stored in a specified cell.
    */
    public Cell getCell(String lines, String columns){
        return _table.get(Integer.valueOf(lines)-1).
            get(Integer.valueOf(columns)-1);
    }

    /** 
    * Gets all the values stored in range(distance).
    *
    * @param distance the range to consider.
    * @return list with the value each cell in the specified range holds.
    */
    public List<Cell> getValues(String distance){
        List<Cell> list = new ArrayList<Cell>(); 
        if (distance.contains(",")){
            String[] range = distance.split("\\,");
            // (a;b,c;d)
            if(range[0].contains(";") && range[1].contains(";")){
                String[] coordenada1 = range[1].split("\\;");
                String[] coordenada2 = range[0].split("\\;");
                list.add(0, getCell(coordenada1[0], coordenada1[1]));
                list.add(1, getCell(coordenada2[0], coordenada2[1]));
            }
            // (a,b)
            else if (!range[0].contains(";") && !range[1].contains(";")){
                list.add(0, new Cell(range[0]));
                list.add(1, new Cell(range[1]));
            }
            // (c,a;b)
            else if (!range[0].contains(";") && range[1].contains(";")) {
                list.add(0, new Cell(range[0]));
                String[] position = range[1].split("\\;");
                list.add(1, getCell(position[0], position[1]));
            }
            // (a;b,c)
            else if (range[0].contains(";") && !range[1].contains(";")) {
                String[] position = range[0].split("\\;");
                list.add(0, getCell(position[0], position[1]));
                list.add(1, new Cell(range[1]));
            }
        }
        // (a;b:c;d)
        else if(distance.contains(":")){
            String[] range = distance.split(":");
            String[] position1 = range[0].split("\\;");
            String[] position2 = range[1].split("\\;");
            if (position1[0].equals(position2[0]))
                list = parseLine(position1[0], 
                    position1[1], position2[1]);
            else
                list = parseColumn(position1[1], 
                    position1[0], position2[0]);
        }
        // (a,b)
        else{
            String[] position = distance.split("\\;");
            list.add(getCell(position[0], position[1]));
        }
        return list;
    }

    /**
     * Being given a specified column, gets the cells held between two specified lines.
     * This method differs from getValues in the sense that this one absolutely
     * requires a range input, rejecting single-cell inputs.
     * 
     * @param column the intended column.
     * @param first the first line in range.
     * @param last the last line in range.
     * @return Returns an array list with the value each cell holds in the specified column 
     * and specified range.
     */
    public List<Cell> parseColumn(String column, String first , String last){
        List<Cell> list = new ArrayList<Cell>();
        int start = Integer.valueOf(first)-1;
        int end = Integer.valueOf(last)-1;
        int i = 0;
        for(int ix = start; ix <= end; ix++){
            Cell cell = _table.get(ix).
                get(Integer.valueOf(column)-1);
            list.add(i++, cell);
        }
        return list;
    }

    /**
     * Being given a specified line, gets the cells held between two specified columns.
     * This method differs from getValues in the sense that this one absolutely
     * requires a range input, rejecting single-cell inputs.
     * 
     * @param line the intended line.
     * @param first the first column in range.
     * @param last the last column in range.
     * @return Returns an array list with the value each cell holds in the specified column 
     * and specified range.
     */
    public List<Cell> parseLine(String line, String first , String last){
        List<Cell> list = new ArrayList<Cell>();
        int start = Integer.valueOf(first)-1;
        int end = Integer.valueOf(last)-1;
        int i = 0;
        for(int ix = start; ix <= end; ix++){
            Cell cell = _table.get(Integer.valueOf(line)-1).
                get(ix);
            list.add(i++, cell);
        }
        return list;
    }


    /**
     * Effectively changes the content a cell in a specified spot holds.
     * 
     * @param position where the cell is.
     * @param content the content to insert. 
    */
    public void store(String[] position, String content){
        for(int ix = 0; ix < _table.size(); ix++){
            if(ix == Integer.parseInt(position[0])-1){
                for(int jx = 0; jx < _table.get(ix).size(); jx++){
                    if(jx == Integer.parseInt(position[1])-1){
                        _table.get(ix).get(jx).saveValue(content);
                        break;
                    }
                }
                break;
            }
        }
    }
    
}