package gui.controller.Company;

import gui.model.Company;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateContrCompany {

    private Stage dialogStage;
    private Company company;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCompany(Company pay) {
        this.company = pay;
    }

    @FXML
    private TextField name;

    @FXML
    private void ok(){
        if(company != null){
            company.setName(name.getText());
            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
