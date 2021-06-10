package gui;

import dao.DaoBank;
import gui.controller.Bank.BaseContrBank;
import gui.controller.Bank.CreateContrBank;
import gui.model.Bank;
import gui.model.Company;
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
public class BankApp {

    private final Stage primaryStage = new Stage();
    private final ObservableList<Bank> BankList;
    public final Company COMPANY;

    public ObservableList<Bank> getList() {
        return BankList;
    }

    public BankApp(Company comp){
        COMPANY = comp;
        ArrayList<Bank> list = DaoBank.selectFromCompany(COMPANY.getId());
        BankList = FXCollections.observableArrayList(list);

        primaryStage.setTitle("Банки компании "+COMPANY.getName());
        showBaseWindow();
       }


    public void showBaseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketBank/rootWindow.fxml"));
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            BaseContrBank controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            log.error("BankApp: error in showBaseWindow");
            e.printStackTrace();
        }
    }

    public void showCreateWindow(Bank bank) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketBank/new.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create new Bank");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            CreateContrBank controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBank(bank);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}