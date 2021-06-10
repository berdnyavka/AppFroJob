package gui.controller.Contractor;


import gui.model.Contractor;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateContrContractor {

    private Stage dialogStage;
    private Contractor contractor;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }
    @FXML
    private TextField name;
    @FXML
    private void ok(){
        if(contractor.getCompanyId() != null){
            contractor.setName(name.getText());

            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
