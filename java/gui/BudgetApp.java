package gui;

import dao.DaoBudget;
import gui.controller.Budget.BaseContrBudget;
import gui.controller.Budget.CreateContrBudget;
import gui.model.Budget;
import gui.model.Project;
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
public class BudgetApp {

    private final Stage primaryStage;
    private  ObservableList<Budget> BudgetList;
    public final Project PROJECT;

    public ObservableList<Budget> getList() {
        return BudgetList;
    }

    public BudgetApp(Project project){
        this.PROJECT = project;

        updateList();

        primaryStage = new Stage();
        primaryStage.setTitle("Статьи бюджета по проекту "+PROJECT.getName());
        showBaseWindow();
       }


    public void showBaseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketBudget/rootWindow.fxml"));
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            BaseContrBudget controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            log.error("BudgetApp: error in showBaseWindow");
            e.printStackTrace();
        }
    }

    public void showCreateWindow(Budget budget) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketBudget/new.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create new Budget");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            CreateContrBudget controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBudget(budget);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateList() {
        ArrayList<Budget> list = DaoBudget.selectFromProject(PROJECT.getId());
        BudgetList = FXCollections.observableArrayList(list);
    }
}