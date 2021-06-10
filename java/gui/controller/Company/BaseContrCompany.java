package gui.controller.Company;

import charts.BarChartReceipt;
import charts.BarChartSpending;
import charts.PieChartReceipt;
import dao.DaoCompany;
import gui.BankApp;
import gui.CompanyApp;
import gui.ProjectApp;
import gui.model.Company;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.math.BigDecimal;
import java.sql.SQLException;

import static gui.CompanyApp.editInfo;

public class BaseContrCompany {

    @FXML
    private TableView<Company> companies;
    @FXML
    private TableColumn<Company,String> companiesList;
    @FXML
    private TableColumn<Company, BigDecimal> sum_receipt;
    @FXML
    private TableColumn<Company,BigDecimal> sum_pay_true;
    @FXML
    private TableColumn<Company,BigDecimal> sum_pay_false;

    private CompanyApp CompanyApp;

    @FXML
    private void initialize(){
        companiesList.setCellValueFactory(
                cellData -> cellData.getValue().getNameProperty());
        companiesList.setCellFactory(TextFieldTableCell.forTableColumn());

        sum_receipt.setCellValueFactory(
                cellData -> cellData.getValue().sumReceiptProperty());
        sum_pay_true.setCellValueFactory(
                cellData -> cellData.getValue().sumPayTrueProperty());
        sum_pay_false.setCellValueFactory(
                cellData -> cellData.getValue().sumPayFalseProperty());
    }

    public void setMainApp(CompanyApp CompanyApp){
        this.CompanyApp = CompanyApp;
        companies.setItems(CompanyApp.getList());
        companies.setEditable(true);
    }
    @FXML
    private void showStatistic(){
        try{
            int selectedIndex = companies.getSelectionModel().getSelectedIndex();
            Company mark = companies.getItems().get(selectedIndex);
            int month = CompanyApp.showMonthWindow(mark);

            new BarChartSpending(mark, month);
            new BarChartReceipt(mark, month);
            new PieChartReceipt(mark);

        }catch (Exception e){
            gui.CompanyApp.info(e.getMessage());
        }
    }
    @FXML
    private void showBanks(){
        int selectedIndex = companies.getSelectionModel().getSelectedIndex();
        Company mark = companies.getItems().get(selectedIndex);
        new BankApp(mark);
    }
    @FXML
    private void showProjects(){
        int selectedIndex = companies.getSelectionModel().getSelectedIndex();
        Company mark = companies.getItems().get(selectedIndex);
        new ProjectApp(mark);
    }
    @FXML
    private void update(){
        CompanyApp.updateList();
        companies.setItems(CompanyApp.getList());
    }
    @FXML
    private void delete(){
        int selectedIndex = companies.getSelectionModel().getSelectedIndex();
        Company markToRemove = companies.getItems().get(selectedIndex);
        try {
            DaoCompany.delete(markToRemove.getId());
            companies.getItems().remove(selectedIndex);
        } catch (SQLException throwables) {
            CompanyApp.deleteInfo(throwables.getMessage());
        }
    }
    @FXML
    private void edit(){
        try {
            int selectedIndex = companies.getSelectionModel().getSelectedIndex();
            //получить платеж со старыми полями(нужны все id) (или новые поля тоже если редактируем через тейблвью)
            Company mark = companies.getItems().get(selectedIndex);
            //получить новые значения полей и записать в список
            companies.getItems().set(selectedIndex, new Company(mark.getId(), mark.getName(), mark.getDate(), mark.getSumReceipt(), mark.getSumPayTrue(), mark.getSumPayFalse()));
            //из списка записать платеж в бд
            DaoCompany.update(companies.getItems().get(selectedIndex));
            //edit in list
            editInfo();
        }catch (Exception e){
            gui.CompanyApp.info(e.getMessage());
        }
    }

    @FXML
    private void create(){
        try {
            Company company = new Company();
            CompanyApp.showCreateWindow(company);
            if (company.getNameProperty() != null) {
                //add to DB
                long resId = DaoCompany.insert(company);
                //add to list if ID is getting
                if (resId != -1) {
                    company.setId(resId);
                    CompanyApp.getList().add(company);
                    //Collections.sort(companyApp.getList(),(p1, p2) -> p1.compareTo(p2));
                }
            }
        }catch (Exception e){
            gui.CompanyApp.info(e.getMessage());
        }
    }
    @FXML
    private void getReport(){
        CompanyApp.showDateWindow();
    }
}
