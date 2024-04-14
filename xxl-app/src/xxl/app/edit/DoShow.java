package xxl.app.edit;


import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("visualizar", Prompt.address());
        
    }

    @Override
    protected final void execute() throws CommandException {
        String s = stringField("visualizar");

        try{
            for(String value: _receiver.printValues(s)){
                _display.popup(value);
            }
        }
        catch(IndexOutOfBoundsException | IllegalArgumentException e ){
            throw new InvalidCellRangeException(s);
        }
    }

}
