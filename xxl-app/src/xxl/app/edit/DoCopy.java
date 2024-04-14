package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Copy command.
 */
class DoCopy extends Command<Spreadsheet> {

    DoCopy(Spreadsheet receiver) {
        super(Label.COPY, receiver);
        addStringField("copiar", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("copiar");
        
        try{
            _receiver.copy(range);
        }
        catch(IndexOutOfBoundsException | IllegalArgumentException e ){
            throw new InvalidCellRangeException(range);
        }
    }

}
