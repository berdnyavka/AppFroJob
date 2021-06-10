package gui;

import dao.DaoReceipt;
import gui.controller.Receipt.BaseContrReceipt;
import gui.controller.Receipt.CreateContrReceipt;
import gui.model.Contractor;
import gui.model.Project;
import gui.model.Receipt;
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
public class ReceiptApp {

    private final Stage primaryStage = new Stage();
    private final ObservableList<Receipt> receiptList;
    public Contractor CONTRACTOR;
    public Project PROJECT;

    public ObservableList<Receipt> getList() {
        return receiptList;
    }

    public ReceiptApp(Contractor contractor){
        CONTRACTOR = contractor;

        ArrayList<Receipt> list = DaoReceipt.selectFromContractor(CONTRACTOR.getId());
        receiptList = FXCollections.observableArrayList(list);

        primaryStage.setTitle("Поступления от контрагента"+CONTRACTOR.getName());
        showBaseWindow();
    }

    public ReceiptApp(Project project){
        PROJECT = project;

        ArrayList<Receipt> list = DaoReceipt.selectFromProject(PROJECT.getId());
        receiptList = FXCollections.observableArrayList(list);

        primaryStage.setTitle("Поступления по проекту "+PROJECT.getName());
        showBaseWindow();
    }

    public void showBaseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketReceipt/rootWindow.fxml"));
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            BaseContrReceipt controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            log.error("receiptApp: error in showBaseWindow");
            e.printStackTrace();
        }
    }

    public void showCreateWindow(Receipt receipt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketReceipt/new.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Создать новое поступление");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            CreateContrReceipt controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setReceipt(receipt);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}