package gui.controller.Contractor;

import dao.DaoContractor;
import gui.CompanyApp;
import gui.ContractorApp;
import gui.PaymentApp;
import gui.ReceiptApp;
import gui.model.Contractor;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.SQLException;

public class BaseContrContractor {
    @FXML
    private TableView<Contractor> contractors;
    @FXML
    private TableColumn<Contractor,String> contrList;

    private ContractorApp contractorApp;
    @FXML
    private void initialize(){
        contrList.setCellValueFactory(
                cellData -> cellData.getValue().getNameProperty());
        contrList.setCellFactory(TextFieldTableCell.forTableColumn());
        // contractors.getSelectionModel().selectedItemProperty().addListener(
        //       (observable, oldValue, newValue) -> showContractorsInformation(newValue));
    }

    public void setMainApp(ContractorApp contractorApp){
        this.contractorApp = contractorApp;
        contractors.setItems(contractorApp.getList());
        contractors.setEditable(true);

    }
    @FXML
    private void showReceipts(){
        int selectedIndex = contractors.getSelectionModel().getSelectedIndex();
        Contractor mark = contractors.getItems().get(selectedIndex);
        ReceiptApp app = new ReceiptApp(mark);

    }
    @FXML
    private void showPayments(){
        int selectedIndex = contractors.getSelectionModel().getSelectedIndex();
        Contractor mark = contractors.getItems().get(selectedIndex);
        PaymentApp paymentApp = new PaymentApp(mark);

    }
    @FXML
    private void delete(){
        int selectedIndex = contractors.getSelectionModel().getSelectedIndex();
        //delete from DB
        Contractor markToRemove = contractors.getItems().get(selectedIndex);
        try {
            DaoContractor.delete(markToRemove.getId());
            contractors.getItems().remove(selectedIndex);
        } catch (SQLException throwables) {
            CompanyApp.deleteInfo(throwables.getMessage());
        }
        //delete from list

    }
    @FXML
    private void edit(){
        try {
            int selectedIndex = contractors.getSelectionModel().getSelectedIndex();
            //получить платеж со старыми полями(нужны все id)
            Contractor markToEdit = contractors.getItems().get(selectedIndex);
            //получить новые значения полей и записать в список
            contractors.getItems().set(selectedIndex, new Contractor(markToEdit.getCompanyId(),markToEdit.getProjectId(),markToEdit.getBudgetId(),markToEdit.getId(),markToEdit.getName()));
            //из списка записать платеж в бд
            DaoContractor.update(contractors.getItems().get(selectedIndex));
        } catch (SQLException e) {
            CompanyApp.info(e.getMessage());
        }
        //edit in list
        CompanyApp.editInfo();
    }

    @FXML
    private void create(){
        try {
            Contractor contractor = new Contractor();

            contractor.setCompanyId(contractorApp.BUDGET.getCompanyId());
            contractor.setProjectId(contractorApp.BUDGET.getProjectId());
            contractor.setBudgetId(contractorApp.BUDGET.getId());
            contractorApp.showCreateWindow(contractor);
            //add to DB
            long resId = DaoContractor.insert(contractor);
            //add to list if ID is getting
            if (resId != -1) {
                contractor.setId(resId);
                contractorApp.getList().add(contractor);
            }
        }catch (Exception ex){
            CompanyApp.info(ex.getMessage());
        }
    }
}
