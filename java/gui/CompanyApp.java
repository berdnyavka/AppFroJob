package gui;

import dao.DaoCompany;
import gui.controller.Company.BaseContrCompany;
import gui.controller.Company.CreateContrCompany;
import gui.controller.Company.DateContrCompany;
import gui.controller.Company.MonthContrCompany;
import gui.model.Company;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;

@Log4j2
public class CompanyApp extends Application {

    private Stage primaryStage;
    private ObservableList<Company> companiesList;

    public ObservableList<Company> getList() {
        return companiesList;
    }

    public void updateList() {
        ArrayList<Company> list = DaoCompany.selectAll();
        companiesList = FXCollections.observableArrayList(list);
    }

    public CompanyApp(){
        updateList();
       }


    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Компании");
        showBaseWindow();
    }

    public void showBaseWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketCompany/rootWindow.fxml"));
            Parent rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            BaseContrCompany controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            log.error("CompanyApp: error in showBaseWindow");
            e.printStackTrace();
        }
    }

    public void showCreateWindow(Company company) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketCompany/new.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Создание новой компании");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            CreateContrCompany controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCompany(company);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDateWindow() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketCompany/dateForReport.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ввод даты");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            DateContrCompany controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int showMonthWindow(Company c) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/maketCompany/monthForCharts.fxml"));
            AnchorPane personOverview = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ввод месяца");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(personOverview));

            MonthContrCompany controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCompany(c);
            dialogStage.showAndWait();

            return controller.monthForCharts;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteInfo(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Удаление");
        alert.setHeaderText("Oшибка: не удалено!");
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void info(String message){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Описание ошибки:");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void editInfo(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Редактирование");
        alert.setHeaderText("Статус:");
        alert.setContentText("записано!");
        alert.showAndWait();
    }


}