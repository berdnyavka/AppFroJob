package gui.controller.Bank;


import gui.model.Bank;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateContrBank {

    private Stage dialogStage;
    private Bank bank;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
    @FXML
    private TextField name;
    @FXML
    private TextField account;
    @FXML
    private void ok(){
        if(bank.getCompanyId() != null){
            bank.setName(name.getText());
            bank.setAccount(account.getText());
            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
