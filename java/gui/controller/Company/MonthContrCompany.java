package gui.controller.Company;

import gui.model.Company;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MonthContrCompany {

    private Stage dialogStage;
    private Company company;
    public int monthForCharts;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCompany(Company pay) {
        this.company = pay;
    }
    @FXML
    private TextField month;

    @FXML
    private void ok(){
        if(month.getText() != null){
            try {
                monthForCharts = Integer.parseInt(month.getText());
                if((monthForCharts<1) || (monthForCharts>12)){
                    throw new Exception();
                }

            }catch (Exception e){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Statistic");
                alert.setHeaderText("Oшибка: направильный ввод месяца!");
                alert.showAndWait();

                monthForCharts = -1;
            }

            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
