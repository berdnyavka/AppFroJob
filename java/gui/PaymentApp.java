package gui;

import dao.DaoPayment;
import gui.controller.Payment.BaseContrPayment;
import gui.controller.Payment.CreateContrPayment;
import gui.model.Contractor;
import gui.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
@Log4j2
public class PaymentApp {

    private final Stage primaryStage = new Stage();
    private final ObservableList<Payment> paymentList;
    public final Contractor CONTRACTOR;

    public ObservableList<Payment> getList() {
        return paymentList;
    }

    public PaymentApp(Contractor contractor){
        CONTRACTOR = contractor;
        ArrayList<Payment> list = DaoPayment.selectFromContractor(CONTRACTOR.getId());
        paymentList = FXCollections.observableArrayList(list);

        primaryStage.setTitle("Платежи контрагента: "+CONTRACTOR.getName());
        showBaseWindow();
       }


    public void showBaseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketPayment/rootWindow.fxml"));
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            BaseContrPayment controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            log.error("PaymentApp: error in showBaseWindow");
            e.printStackTrace();
        }
    }

    public void showCreateWindow(Payment pay) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketPayment/new.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Создать ");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            CreateContrPayment controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPayment(pay);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}