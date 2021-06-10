package gui.controller.Company;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import report.PaymentsReport;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateContrCompany {

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    @FXML
    private TextField date;

    @FXML
    private void ok(){
        if(date.getText() != null){
            try {
                DateTimeFormatter dateFormatter =
                        DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate locald = LocalDate.parse(date.getText(),dateFormatter);
                new PaymentsReport(locald);
            }catch (Exception e){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Report");
                alert.setHeaderText("Oшибка: направильная дата!");
                alert.showAndWait();
            }

            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
