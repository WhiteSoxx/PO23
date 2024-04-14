package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Delete command.
 */
class DoDelete extends Command<Spreadsheet> {

    DoDelete(Spreadsheet receiver) {
        super(Label.DELETE, receiver);
        addStringField("delete", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("delete");
        
        try {
            //Changing held content to "" effectively erases the held content. 
            _receiver.changeRangeContent(range,"");
        }
        catch(IndexOutOfBoundsException | IllegalArgumentException | IllegalCallerException e){
            throw new InvalidCellRangeException(range);
        }
    }

}
