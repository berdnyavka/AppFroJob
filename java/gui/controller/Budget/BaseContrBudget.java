package gui.controller.Budget;

import dao.DaoBudget;
import gui.BudgetApp;
import gui.CompanyApp;
import gui.ContractorApp;
import gui.model.Budget;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;
import java.sql.SQLException;

public class BaseContrBudget {
    @FXML
    private TableView<Budget> budgets;
    @FXML
    private TableColumn<Budget,String> budgetsList;
    @FXML
    private TableColumn<Budget,BigDecimal> planPayList;
    @FXML
    private TableColumn<Budget,BigDecimal> factPayList;

    private BudgetApp BudgetApp;

    @FXML
    private void initialize(){
        budgetsList.setCellValueFactory(
                cellData -> cellData.getValue().getNameProperty());
        budgetsList.setCellFactory(TextFieldTableCell.forTableColumn());

        planPayList.setCellValueFactory(
                cellData -> cellData.getValue().getPlanForMonthProperty());
        planPayList.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

        factPayList.setCellValueFactory(
                cellData -> cellData.getValue().getFactForMonthProperty());

    }

    public void setMainApp(BudgetApp app){
        this.BudgetApp = app;
        budgets.setItems(app.getList());
        budgets.setEditable(true);
    }
    @FXML
    private void showContractors(){
        int selectedIndex = budgets.getSelectionModel().getSelectedIndex();
        Budget mark = budgets.getItems().get(selectedIndex);
        ContractorApp contractorApp = new ContractorApp(mark);

    }
    @FXML
    private void delete(){
        int selectedIndex = budgets.getSelectionModel().getSelectedIndex();
        //delete from DB
        Budget markToRemove = budgets.getItems().get(selectedIndex);
        try {
            DaoBudget.delete(markToRemove.getId());
            budgets.getItems().remove(selectedIndex);
        } catch (SQLException throwables) {
            CompanyApp.deleteInfo(throwables.getMessage());
        }
        //delete from list

    }
    @FXML
    private void edit(){
        try {
            int selectedIndex = budgets.getSelectionModel().getSelectedIndex();
            //получить платеж со старыми полями(нужны все id) (или новые поля тоже если редактируем через тейблвью)
            Budget markToEdit = budgets.getItems().get(selectedIndex);
            //получить новые значения полей и записать в список
            budgets.getItems().set(selectedIndex, new Budget(markToEdit.getCompanyId(), markToEdit.getProjectId(), markToEdit.getId(),
                    markToEdit.getName(), markToEdit.getPlanForMonth().toString()));
            //из списка записать платеж в бд
            DaoBudget.update(budgets.getItems().get(selectedIndex));
            //edit in list

            CompanyApp.editInfo();
        }catch (Exception e){
            CompanyApp.info(e.getMessage());
        }
    }

    @FXML
    private void create(){
        try {
            Budget budget = new Budget();

            budget.setProjectId(BudgetApp.PROJECT.getId());
            budget.setCompanyId(BudgetApp.PROJECT.getCompanyId());

            BudgetApp.showCreateWindow(budget);
            if (budget.getNameProperty() != null) {
                //add to DB
                long resId = DaoBudget.insert(budget);
                //add to list if ID is getting
                if (resId != -1) {
                    budget.setId(resId);
                    BudgetApp.getList().add(budget);
                    //Collections.sort(app.getList(),(p1, p2) -> p1.compareTo(p2));
                }
            }
        }catch (Exception e){
            CompanyApp.info(e.getMessage());
        }
    }

    @FXML
    public void update(){
        BudgetApp.updateList();
        budgets.setItems(BudgetApp.getList());
    }
}
