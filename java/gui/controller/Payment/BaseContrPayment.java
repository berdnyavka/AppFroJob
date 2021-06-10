package gui.controller.Payment;

import dao.DaoBudget;
import dao.DaoCompany;
import dao.DaoPayment;
import dao.DaoProject;
import gui.CompanyApp;
import gui.PaymentApp;
import gui.model.Payment;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
@Log4j2
public class BaseContrPayment {
    @FXML
    private TableView<Payment> payments;
    @FXML
    private TableColumn<Payment,String> purposeList;
    @FXML
    private TableColumn<Payment, LocalDate> dateList;
    @FXML
    private TextField date;
    @FXML
    private TextField sum;
    @FXML
    private TextField purpose;
    @FXML
    private TextField source;
    @FXML
    private TextField confirmation;
    @FXML
    private TextField advice;
    @FXML
    private TextField comment;


    private PaymentApp paymentApp;

    @FXML
    private void initialize(){
        purposeList.setCellValueFactory(
                cellData -> cellData.getValue().getPurposeProperty());
        dateList.setCellValueFactory(
                cellData -> cellData.getValue().getDateProperty());
        payments.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPaymentsInformation(newValue));
    }

    public void setMainApp(PaymentApp paymentApp){
        this.paymentApp = paymentApp;
        payments.setItems(paymentApp.getList());

    }

    private void showPaymentsInformation(Payment payment){
        if(payment != null){
            date.setText(payment.getDate() != null ? payment.getDateString() : null);
            sum.setText(payment.getSum() != null ? payment.getSum().toString() : null);
            purpose.setText(payment.getPurpose() != null ? payment.getPurpose() : null);
            source.setText(payment.getSource() != null ? payment.getSource() : null);
            confirmation.setText(payment.getConfirmationString() != null ? payment.getConfirmationString() : null);
            advice.setText(payment.getAdvice() != null ? payment.getAdvice() : null);
            comment.setText(payment.getComment() != null ? payment.getComment() : null);

        }else{
            date.setText("");
            sum.setText("");
            purpose.setText("");
            source.setText("");
            confirmation.setText("");
            advice.setText("");
            comment.setText("");
        }
    }

    @FXML
    private void delete(){
        int selectedIndex = payments.getSelectionModel().getSelectedIndex();
        Payment mark = payments.getItems().get(selectedIndex);
        try {
            DaoPayment.delete(mark.getId());
            payments.getItems().remove(selectedIndex);
            //обновить сумму у компании и факт сумму у бюджета
            if(mark.getConfirmationBool()){
                DaoCompany.updateSumPayTrue(paymentApp.CONTRACTOR.getCompanyId());
                DaoProject.updateSumPayTrue(paymentApp.CONTRACTOR.getProjectId());
            }else{
                DaoCompany.updateSumPayFalse(paymentApp.CONTRACTOR.getCompanyId());
                DaoProject.updateSumPayFalse(paymentApp.CONTRACTOR.getProjectId());
            }
            DaoBudget.updateFact(paymentApp.CONTRACTOR.getBudgetId());
        } catch (SQLException throwables) {
            CompanyApp.deleteInfo(throwables.getMessage());
        }
    }
    @FXML
    private void edit(){
        int selectedIndex = payments.getSelectionModel().getSelectedIndex();
        Payment markToEdit = payments.getItems().get(selectedIndex);
        BigDecimal oldSum = markToEdit.getSum();
        Payment newPayment = new Payment(markToEdit.getCompanyId(), markToEdit.getProjectId(), markToEdit.getBudgetId(), markToEdit.getContractorId(), markToEdit.getId(), date.getText(), sum.getText(),
                purpose.getText(), source.getText(), confirmation.getText(), advice.getText(), comment.getText());
        try {
            payments.getItems().set(selectedIndex,newPayment);
            DaoPayment.update(payments.getItems().get(selectedIndex));
            //обновить сумму у компании и факт сумму у бюджета
            if(markToEdit.getConfirmationBool()){
                DaoCompany.updateSumPayTrue(paymentApp.CONTRACTOR.getCompanyId());
                DaoProject.updateSumPayTrue(paymentApp.CONTRACTOR.getProjectId());
            }else{
                DaoCompany.updateSumPayFalse(paymentApp.CONTRACTOR.getCompanyId());
                DaoProject.updateSumPayFalse(paymentApp.CONTRACTOR.getProjectId());
            }
            DaoBudget.updateFact(paymentApp.CONTRACTOR.getBudgetId());
        }catch(Exception ex){
            log.error(ex.getMessage());
        }

    }

    @FXML
    private void create() {
        try{
            if (paymentApp.CONTRACTOR != null) {
                Payment pay = new Payment();
                pay.setCompanyId(paymentApp.CONTRACTOR.getCompanyId());
                pay.setProjectId(paymentApp.CONTRACTOR.getProjectId());
                pay.setBudgetId(paymentApp.CONTRACTOR.getBudgetId());
                pay.setContractorId(paymentApp.CONTRACTOR.getId());
                paymentApp.showCreateWindow(pay);
                if (pay.getDateProperty() != null) {
                    long resId = DaoPayment.insert(pay);
                    if (resId != -1) {
                        pay.setId(resId);
                        paymentApp.getList().add(pay);
                        Collections.sort(paymentApp.getList(), (p1, p2) -> p1.compareTo(p2));
                    }
                    //обновить сумму у компании и факт сумму у бюджета
                    if (pay.getConfirmationBool()) {
                        DaoCompany.updateSumPayTrue(paymentApp.CONTRACTOR.getCompanyId());
                        DaoProject.updateSumPayTrue(paymentApp.CONTRACTOR.getProjectId());
                    } else {
                        DaoCompany.updateSumPayFalse(paymentApp.CONTRACTOR.getCompanyId());
                        DaoProject.updateSumPayFalse(paymentApp.CONTRACTOR.getProjectId());
                    }
                    DaoBudget.updateFact(paymentApp.CONTRACTOR.getBudgetId());
                }
            }
        }catch (Exception e){
            CompanyApp.info(e.getMessage());

        }
    }
}
