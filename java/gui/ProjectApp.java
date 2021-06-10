package gui;

import dao.DaoProject;
import gui.controller.Project.BaseContrProject;
import gui.controller.Project.CreateContrProject;
import gui.model.Company;
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
public class ProjectApp {

    private Stage primaryStage = new Stage();
    private ObservableList<Project> projectList;
    public final Company COMPANY;

    public ObservableList<Project> getList() {
        return projectList;
    }

    public void updateList() {

        ArrayList<Project> list = DaoProject.selectFromCompany(COMPANY.getId());
        projectList = FXCollections.observableArrayList(list);
    }

    public ProjectApp(Company company){
        COMPANY = company;
        updateList();
        primaryStage.setTitle("Проекты компании "+COMPANY.getName());
        showBaseWindow();
    }

    public void showBaseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketProject/rootWindow.fxml"));
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            BaseContrProject controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();

        } catch (Exception e) {
            log.error("ProjectApp: error in showBaseWindow");
            e.printStackTrace();
        }
    }

    public void showCreateWindow(Project project) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketProject/new.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Создание нового проекта");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            CreateContrProject controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProject(project);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}