package xxl;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /** A list of Users of the spreadsheet */
    private List<User> _users = new ArrayList<>();

    private User _activeUser;
    
    /**
     * This class' constructor. Creates the default user "root" 
     * and adds it to the user list.
     * 
     */
    public Calculator() {
        _activeUser = new User("root");
        addUser(_activeUser);
    }

    /**
     * Creates a new spreadsheet with the specified number
     * of lines and columns. Additionally adds the current active
     * user to the new spreadsheet's user list.
     * 
     * @param lines
     * @param columns
     */
    public void newSpreadsheet(int lines, int columns) {
        checkLinesAndColumns(lines, columns);
        setSpreadsheet(new Spreadsheet(lines, columns));
        _spreadsheet.addUser(_activeUser);
    }

    /**
    * @return the current spreadsheet.
    */
    public Spreadsheet getSpreadsheet(){
        return _spreadsheet;
    }

    /**
    * @param spreadsheet sets the current spreadsheet.
    */
    public void setSpreadsheet(Spreadsheet spreadsheet){
        _spreadsheet = spreadsheet;
        _spreadsheet.addUser(_activeUser);
    }

    /**
    * Checks whether or not the current spreadsheet exists and
    * has been saved.
    *
    * @return the answer.
    */
    public boolean currentSpreadsheetWasSavedBefore(){
        return _spreadsheet != null && _spreadsheet.getFileName() != null;
    }

    /**
    * Checks whether or not the current spreadsheet exists and
    * has been saved.
    *
    * @return the answer.
    */
    public boolean currentSpreadsheetWasChanged(){
        return _spreadsheet.wasChanged();
    }


    /**
    * @return the current active user.
    */
    public User getActiveUser(){
        return _activeUser;
    }

    /**
    * Changes the current active user. If the user isn't on the
    * users list yet, adds them. If a spreadsheet is also open,
    * adds them to the spreadsheet's user list too.
    * 
    * @param user the new active user.
    */
    public void setActiveUser(User user){
        _activeUser = user;
        if(_spreadsheet != null)
            _spreadsheet.addUser(user);
        if(!_users.contains(user))
            addUser(user);
    }

    /**
    * Adds a new user to the users list. By defalt, they're simply
    * added but not considered the new active user.
    * 
    * @param user the new user.
    */
    public void addUser(User user){
        _users.add(user);
    }  

    /**
     * Assures the values given are valid. Lines and columns can't
     * be negative.
     *
     * @param line the line value.
     * @param column the column value.
     */
    public void checkLinesAndColumns(int lines,int columns){
        if(lines < 0 || columns < 0){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        try( ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream(_spreadsheet.getFileName())))){
            oos.writeObject(_spreadsheet);
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        if(filename == null || filename == ""){
            throw new MissingFileAssociationException();
        }
        _spreadsheet.setFileName(filename);
        save();
    }


    /**
     * Loads a previous application state from an existing file.
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        try(ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(
                    new FileInputStream(filename)))){
            setSpreadsheet((Spreadsheet)ois.readObject());
            _spreadsheet.setChangedStatus(false);
            _spreadsheet.setFileName(filename);
        }
        catch(ClassNotFoundException | IOException e1){
            throw new UnavailableFileException(filename);
        }
    }


    /**
     * Reads a text input file and creates a spreadhseet with the values it reads.
     *
     * @param filename name of the text input file.
     * @throws ImportFileException should something go wrong during this process.
     */
    public void importFile(String filename) throws ImportFileException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String s;
            int i = 0;
            while ((s = reader.readLine()) != null){
                if(i == 0){
                    String[] n_lines = s.split("\\=");
                    s = reader.readLine();
                    String[] n_col = s.split("\\=");
                    _spreadsheet = new Spreadsheet(Integer.valueOf(
                        n_lines[1]), Integer.valueOf(n_col[1]));
                    i++;
                }
                else{
                    String[] line = s.split("\\|");
                    try{
                        _spreadsheet.checkInput(line[0]);
                    }
                    catch(IllegalArgumentException | IndexOutOfBoundsException e){
                        throw new UnrecognizedEntryException(line[0]);
                    }
                    try{
                        String[] coordenadas = line[0].split("\\;");
                        _spreadsheet.insertContents(coordenadas, line[1]);
                    }
                    catch(IllegalArgumentException | IndexOutOfBoundsException
                            | UnrecognizedEntryException e){
                        throw new UnrecognizedEntryException(line[1]);
                    }
                }
            }
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename, e);
        }
    }

}
