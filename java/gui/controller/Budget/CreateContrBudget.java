package gui.controller.Budget;

import gui.model.Budget;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateContrBudget {

    private Stage dialogStage;
    private Budget budget1;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setBudget(Budget budget) {
        this.budget1 = budget;
    }
    @FXML
    private TextField name;
    @FXML
    private TextField planPay;
    @FXML
    private void ok(){
        if(budget1.getProjectId() != null){
            budget1.setName(name.getText());
            budget1.setPlanForMonth(planPay.getText());
            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
