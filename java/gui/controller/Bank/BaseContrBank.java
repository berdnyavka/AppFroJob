package gui.controller.Bank;

import dao.DaoBank;
import gui.BankApp;
import gui.CompanyApp;
import gui.model.Bank;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;
import java.sql.SQLException;

public class BaseContrBank {
    @FXML
    private TableView<Bank> banks;
    @FXML
    private TableColumn<Bank,String> name;
    @FXML
    private TableColumn<Bank, BigDecimal> account;

    private BankApp bankApp;
    @FXML
    private void initialize(){
        name.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty());
        name.setCellFactory(TextFieldTableCell.forTableColumn());

        account.setCellValueFactory(
                cellData -> cellData.getValue().accountProperty());
        account.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));

    }

    public void setMainApp(BankApp bankApp){
        this.bankApp = bankApp;
        banks.setItems(bankApp.getList());
        banks.setEditable(true);
    }
    @FXML
    private void delete(){
        int selectedIndex = banks.getSelectionModel().getSelectedIndex();
        Bank markToRemove = banks.getItems().get(selectedIndex);
        try {
            DaoBank.delete(markToRemove.getId());
            banks.getItems().remove(selectedIndex);
        } catch (SQLException throwables) {
            CompanyApp.deleteInfo(throwables.getMessage());
        }
    }
    @FXML
    private void edit(){
        try {
            int selectedIndex = banks.getSelectionModel().getSelectedIndex();
            Bank markToEdit = banks.getItems().get(selectedIndex);
            banks.getItems().set(selectedIndex, new Bank(markToEdit.getCompanyId(),markToEdit.getId(),markToEdit.getName(),markToEdit.getAccount()));
            DaoBank.update(banks.getItems().get(selectedIndex));
            CompanyApp.editInfo();
        }catch (Exception e){
            CompanyApp.info(e.getMessage());
        }
    }
    @FXML
    private void create() {
        try {
            Bank bank = new Bank();
            bank.setCompanyId(bankApp.COMPANY.getId());
            bankApp.showCreateWindow(bank);
            if (bank.nameProperty() != null) {
                long resId = DaoBank.insert(bank);
                if (resId != -1) {
                    bank.setId(resId);
                    bankApp.getList().add(bank);
                }
            }
        }catch (Exception e){
            CompanyApp.info(e.getMessage());
        }
    }
}
