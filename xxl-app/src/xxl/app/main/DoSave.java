package xxl.app.main;

import java.io.IOException;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import xxl.Calculator;

import xxl.exceptions.MissingFileAssociationException;

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSave extends Command<Calculator> {

    DoSave(Calculator receiver) {
        super(Label.SAVE, receiver, xxl -> xxl.getSpreadsheet() != null);
    }

    @Override
    protected final void execute() {
        try {
            //if the file has never been saved before.
            if(!_receiver.currentSpreadsheetWasSavedBefore()) {
                _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
            }
            else {
                //if the file has been saved before but has been changed since.
                if(_receiver.currentSpreadsheetWasChanged()) {
                    _receiver.save();
                }
            }
            _receiver.getSpreadsheet().setChangedStatus(false);
        }
        catch(MissingFileAssociationException|IOException e){
            e.printStackTrace();
        }
    }
}

