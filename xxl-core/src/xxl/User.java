package xxl;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

  /** The user's name. */
  private String _name;
 /** A list of spreadsheets the user has at least opened. */
  private List<Spreadsheet> _spreadsheets = new ArrayList<>();

  /**
   * This class' constructor. 
   * Creates a new user.
   * @param name the name of the user.
   */
  public User(String name){
    _name = name;
  }
  
  /**
    * @return the name of the user.
    */
  public String getName() {
    return _name;
  }

  /**
    * @param name sets the name of the user
    */
  public void setName(String name) {
    _name = name;
  }

  /**
    * Adds a new spreadsheet to the list of spreadsheets the user
    * has at least opened.
    *
    * @param spreadsheet the spreadsheet to be added.
    */
  public void addSpreadsheet(Spreadsheet spreadsheet) {
    if(!_spreadsheets.contains(spreadsheet))
      _spreadsheets.add(spreadsheet);
  }

}

