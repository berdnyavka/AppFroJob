package gui.controller.Receipt;


import gui.model.Receipt;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateContrReceipt {

    private Stage dialogStage;
    private Receipt receipt;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
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
    private TextField comment;
    @FXML
    private void ok(){
        if(receipt.getContractorId() != null){
            receipt.setDate(date.getText());
            receipt.setSum(sum.getText());
            receipt.setPurpose(purpose.getText());
            receipt.setSource(source.getText());
            receipt.setComment(comment.getText());

            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
