package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Cut command.
 */
class DoCut extends Command<Spreadsheet> {

    DoCut(Spreadsheet receiver) {
        super(Label.CUT, receiver);
        addStringField("cut", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("cut");

        try {
            _receiver.copy(range);
            //Changing held content to "" effectively erases the held content. 
            _receiver.changeRangeContent(range, "");
        } 
        catch(IndexOutOfBoundsException | IllegalArgumentException | IllegalCallerException e){
            throw new InvalidCellRangeException(range);
        }
    }

}
