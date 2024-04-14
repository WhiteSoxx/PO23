package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("local", Prompt.address());
        addStringField("content", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        String range = stringField("local");
        String content = stringField("content");
        
        try {
            _receiver.changeRangeContent(range, content);
        } 
        catch(IndexOutOfBoundsException | IllegalArgumentException e){
            throw new InvalidCellRangeException(range);
        }
        catch(IllegalCallerException e){
            throw new UnknownFunctionException(content);
        }

    }

}
