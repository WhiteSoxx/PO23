package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Paste command.
 */
class DoPaste extends Command<Spreadsheet> {

    DoPaste(Spreadsheet receiver) {
        super(Label.PASTE, receiver);
        addStringField("colar", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("colar");
        
        try {
            _receiver.paste(range);
        } 
        catch(IndexOutOfBoundsException | IllegalArgumentException e){
            throw new InvalidCellRangeException(range);
        }
    }

}
