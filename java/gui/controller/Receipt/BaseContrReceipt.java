package gui.controller.Receipt;

import dao.DaoCompany;
import dao.DaoProject;
import dao.DaoReceipt;
import gui.CompanyApp;
import gui.ReceiptApp;
import gui.model.Receipt;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
@Log4j2
public class BaseContrReceipt {
    static DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @FXML
    private TableView<Receipt> receipts;
    @FXML
    private TableColumn<Receipt,String> purposeList;
    @FXML
    private TableColumn<Receipt, LocalDate> dateList;
    @FXML
    private TextField date;
    @FXML
    private TextField sum;
    @FXML
    private TextField purpose;
    @FXML
    private TextField source;
    @FXML
    private TextField comment;

    private ReceiptApp receiptApp;
    @FXML
    private void initialize(){
        purposeList.setCellValueFactory(
                cellData -> cellData.getValue().getPurposeProperty());
        dateList.setCellValueFactory(
                cellData -> (cellData.getValue().getDateProperty()));
        receipts.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showReceiptsInformation(newValue));
    }

    public void setMainApp(ReceiptApp receiptApp){
        this.receiptApp = receiptApp;
        receipts.setItems(receiptApp.getList());
    }

    private void showReceiptsInformation(Receipt receipt){
        if(receipt != null){
            date.setText(receipt.getDate() != null ? receipt.getDateString() : null);
            sum.setText(receipt.getSum() != null ? receipt.getSum().toString() : null);
            purpose.setText(receipt.getPurpose() != null ? receipt.getPurpose() : null);
            source.setText(receipt.getSource() != null ? receipt.getSource() : null);
            comment.setText(receipt.getComment() != null ? receipt.getComment() : null);

        }else{
            date.setText("");
            sum.setText("");
            purpose.setText("");
            source.setText("");
            comment.setText("");
        }
    }
    @FXML
    private void delete(){
        int selectedIndex = receipts.getSelectionModel().getSelectedIndex();
        Receipt markToRemove = receipts.getItems().get(selectedIndex);
        try {
            DaoReceipt.delete(markToRemove.getId());
            receipts.getItems().remove(selectedIndex);
            //обновить сумму в проекте и в компании
            DaoProject.updateSumReceipts(receiptApp.CONTRACTOR.getProjectId());
            DaoCompany.updateSumReceipts(receiptApp.CONTRACTOR.getCompanyId());
        } catch (SQLException throwables) {
            CompanyApp.deleteInfo(throwables.getMessage());
        }
    }
    @FXML
    private void edit(){
        int selectedIndex = receipts.getSelectionModel().getSelectedIndex();
        Receipt markToEdit = receipts.getItems().get(selectedIndex);
        BigDecimal oldSum = markToEdit.getSum();
        Receipt newReceipt = new Receipt(markToEdit.getCompanyId(),markToEdit.getProjectId(),markToEdit.getBudgetId(),markToEdit.getContractorId(),markToEdit.getId(),date.getText(),sum.getText(),
                purpose.getText(),source.getText(),comment.getText());
        try {
            receipts.getItems().set(selectedIndex, newReceipt);
            DaoReceipt.update(newReceipt);
            //если сумма изменилась - обновить сумму поступлений в проекте и в компании
            if (!oldSum.equals(newReceipt.getSum())) {
                log.info("сумма изменилась");
                DaoProject.updateSumReceipts(newReceipt.getProjectId());
                DaoCompany.updateSumReceipts(newReceipt.getCompanyId());
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }

    }

    @FXML
    private void create() {
        try {
            Receipt receipt = new Receipt();
            receipt.setCompanyId(receiptApp.CONTRACTOR.getCompanyId());
            receipt.setProjectId(receiptApp.CONTRACTOR.getProjectId());
            receipt.setBudgetId(receiptApp.CONTRACTOR.getBudgetId());
            receipt.setContractorId(receiptApp.CONTRACTOR.getId());
            receiptApp.showCreateWindow(receipt);
            if (receipt.getDateProperty() != null) {
                long resId = DaoReceipt.insert(receipt);
                if (resId != -1) {
                    receipt.setId(resId);
                    receiptApp.getList().add(receipt);
                    Collections.sort(receiptApp.getList(), (p1, p2) -> p1.compareTo(p2));
                }
                //обновить сумму в проекте и в компании
                DaoProject.updateSumReceipts(receiptApp.CONTRACTOR.getProjectId());
                DaoCompany.updateSumReceipts(receiptApp.CONTRACTOR.getCompanyId());
            }
        }catch(SQLException e) {
            CompanyApp.info(e.getMessage());
        }catch (Exception ex){
            CompanyApp.info("Нельзя создать поступление без статьи бжджета и контрагента");
        }
    }
}
