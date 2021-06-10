package gui.controller.Project;

import dao.DaoProject;
import gui.BudgetApp;
import gui.CompanyApp;
import gui.ProjectApp;
import gui.ReceiptApp;
import gui.model.Project;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.math.BigDecimal;
import java.sql.SQLException;

public class BaseContrProject {
    @FXML
    private TableView<Project> projects;
    @FXML
    private TableColumn<Project,String> projectList;
    @FXML
    private TableColumn<Project, BigDecimal> receipts;
    @FXML
    private TableColumn<Project, BigDecimal> sum_pay_true;
    @FXML
    private TableColumn<Project, BigDecimal> sum_pay_false;

    private ProjectApp ProjectApp;
    @FXML
    private void initialize(){
        projectList.setCellValueFactory(
                cellData -> cellData.getValue().getNameProperty());
        projectList.setCellFactory(TextFieldTableCell.forTableColumn());

        receipts.setCellValueFactory(
                cellData -> cellData.getValue().sumReceiptProperty());
        sum_pay_true.setCellValueFactory(
                cellData -> cellData.getValue().sumPayTrueProperty());
        sum_pay_false.setCellValueFactory(
                cellData -> cellData.getValue().sumPayFalseProperty());
    }

    public void setMainApp(ProjectApp ProjectApp){
        this.ProjectApp = ProjectApp;
        projects.setItems(ProjectApp.getList());
        projects.setEditable(true);
    }

    @FXML
    private void showReceipts(){
        int selectedIndex = projects.getSelectionModel().getSelectedIndex();
        Project mark = projects.getItems().get(selectedIndex);
        ReceiptApp app = new ReceiptApp(mark);

    }
    @FXML
    private void showBudgets(){
        int selectedIndex = projects.getSelectionModel().getSelectedIndex();
        Project mark = projects.getItems().get(selectedIndex);
        BudgetApp budgetApp = new BudgetApp(mark);

    }
    @FXML
    private void delete(){
        int selectedIndex = projects.getSelectionModel().getSelectedIndex();
        Project markToRemove = projects.getItems().get(selectedIndex);
        try {
            DaoProject.delete(markToRemove.getId());
            projects.getItems().remove(selectedIndex);
        } catch (SQLException throwables) {
            CompanyApp.deleteInfo(throwables.getMessage());
        }
    }
    @FXML
    private void edit(){
        try {
            int selectedIndex = projects.getSelectionModel().getSelectedIndex();
            Project markToEdit = projects.getItems().get(selectedIndex);
            projects.getItems().set(selectedIndex, new Project(markToEdit.getCompanyId(), markToEdit.getId(), markToEdit.getName()));
            DaoProject.update(projects.getItems().get(selectedIndex));
            CompanyApp.editInfo();
        }catch (Exception e){
            CompanyApp.info(e.getMessage());
        }
    }

    @FXML
    private void create(){
        try {
            Project project = new Project();
            project.setCompanyId(ProjectApp.COMPANY.getId());
            ProjectApp.showCreateWindow(project);
            if (project.getNameProperty() != null) {
                long resId = DaoProject.insert(project);
                if (resId != -1) {
                    project.setId(resId);
                    ProjectApp.getList().add(project);
                }
            }
        }catch (Exception e){
            CompanyApp.info(e.getMessage());
        }
    }
    @FXML
    public void update(){
        ProjectApp.updateList();
        projects.setItems(ProjectApp.getList());
    }
}
