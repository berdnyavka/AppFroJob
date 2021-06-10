package gui;

import dao.DaoContractor;
import gui.controller.Contractor.BaseContrContractor;
import gui.controller.Contractor.CreateContrContractor;
import gui.model.Budget;
import gui.model.Contractor;
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
public class ContractorApp {

    private final Stage primaryStage = new Stage();
    private final ObservableList<Contractor> contrList;
    public final Budget BUDGET;

    public ObservableList<Contractor> getList() {
        return contrList;
    }

    public ContractorApp(Budget budget){
        this.BUDGET = budget;

        ArrayList<Contractor> list = DaoContractor.selectFromBudget(BUDGET.getId());
        contrList = FXCollections.observableArrayList(list);

        this.primaryStage.setTitle("Контрагенты по статье бюджета "+ BUDGET.getName());
        showBaseWindow();
       }

    public void showBaseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketContractor/rootWindow.fxml"));
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            BaseContrContractor controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            log.error("ContractorApp: error in showBaseWindow");
            e.printStackTrace();
        }
    }

    public void showCreateWindow(Contractor contractor) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketContractor/new.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Создание нового контрагента");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            CreateContrContractor controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setContractor(contractor);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}