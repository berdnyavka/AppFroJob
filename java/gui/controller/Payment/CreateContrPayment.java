package gui.controller.Payment;

import gui.model.Payment;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateContrPayment {

    private Stage dialogStage;
    private Payment pay;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPayment(Payment pay) {
        this.pay = pay;
    }
    @FXML
    private TextField date;
    @FXML
    private TextField sum;
    @FXML
    private TextField purpose;
    @FXML
    private TextField source;
    @FXML
    private TextField confirmation;
    @FXML
    private TextField advice;
    @FXML
    private TextField comment;

    @FXML
    private void ok(){
        if(pay.getContractorId() != null){
            pay.setDate(date.getText());
            pay.setSum(sum.getText());
            pay.setPurpose(purpose.getText());
            pay.setSource(source.getText());
            pay.setConfirmationString(confirmation.getText());
            pay.setAdvice(advice.getText());
            pay.setComment(comment.getText());

            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
