package gui.model;

import javafx.beans.property.*;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
@Log4j2
public class Payment {
    private static final Logger logger = LogManager.getLogger(Payment.class);


    static DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private LongProperty companyId;
    private LongProperty projectId;
    private LongProperty budgetId;

    private LongProperty contractorId;
    private StringProperty contractorName;


    private LongProperty id;

    private ObjectProperty<LocalDate> date;

    private ObjectProperty<BigDecimal> sum;

    private StringProperty purpose;
    private StringProperty source;

    private Confirmation confirmation;

    private StringProperty advice;
    private StringProperty comment;



    public Long getContractorId() {
        return contractorId.get();
    }

    public LongProperty getContractorIdProperty() {
        return contractorId;
    }

    public Long getProjectId() {
        return projectId.get();
    }

    public void setProjectId(Long projectId) {
        this.projectId = new SimpleLongProperty(projectId);
    }
    public LongProperty getProjectIdProperty() {
        return projectId;
    }

    public String getAdvice() {
        return advice.get();
    }

    public void setAdvice(String advice) {
        this.advice = new SimpleStringProperty(advice);
    }

    public StringProperty getAdviceProperty() {
        return advice;
    }

    public String getContractorName() {
        return contractorName.get();
    }

    public void setContractorName(String contractorName) {
        this.contractorName = new SimpleStringProperty(contractorName);
    }

    public StringProperty getContractorNameProperty() {
        return contractorName;
    }

    public String getSource() {
        return source.get();
    }

    public void setSource(String source) {
        this.source = new SimpleStringProperty(source);
    }

    public StringProperty getSourceProperty() {
        return source;
    }

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id = new SimpleLongProperty(id);
    }

    public LongProperty getIdProperty() {
        return id;
    }

    public Long getCompanyId() {
        return companyId.get();
    }

    public void setCompanyId(Long companyId) {
        this.companyId = new SimpleLongProperty(companyId);
    }

    public LongProperty getCompanyIdProperty() {
        return companyId;
    }

    public Long getBudgetId() {
        return budgetId.get();
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = new SimpleLongProperty(budgetId);
    }

    public LongProperty getBudgetIdProperty() {
        return budgetId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = new SimpleLongProperty(contractorId);
    }

    public Boolean getConfirmationBool() {
        return confirmation.bool;
    }

    public StringProperty getConfirmationProperty() {
        return confirmation.string;
    }
    public void setConfirmationBool(boolean con) {
        this.confirmation = new Confirmation(con);
    }

    public void setConfirmationString(String str) {
        this.confirmation = new Confirmation(str);
    }

    public String getConfirmationString() {
        return confirmation.string.get();
    }



    public class Confirmation {
        private StringProperty string = new SimpleStringProperty("Запланирован");
        private boolean bool = false;

        public Confirmation(boolean c){
            if (c) {
                string = new SimpleStringProperty("Проведен") ;
                bool = true;
            }
        }

        public Confirmation(String c){
            if (c.equalsIgnoreCase("Проведен")) {
                bool = true;
                string = new SimpleStringProperty(c.toUpperCase());
            }
            else{
                if (!c.equalsIgnoreCase("Запланирован"))
                    log.error("error! string in Confirmation(String c) doesn't recognized");
            }
        }
    }


    public String getDateString() {
        return dateFormatter.format(date.get());
    }
    public LocalDate getDate() {
        return date.get();
    }
    public ObjectProperty<LocalDate> getDateProperty() {
        return date;
    }



    public void setDate(String date){
        LocalDate ld = LocalDate.parse("1990-01-01");
        try {
            ld = LocalDate.parse(date,dateFormatter);
        }catch(DateTimeParseException ex){
            log.error("format for date - yyyy-mm-dd");
            ex.printStackTrace();
        }
        this.date = new SimpleObjectProperty<>(ld);
    }

    public void setDateDB(LocalDate ld){
        this.date = new SimpleObjectProperty<>(ld);
    }


    public void setSum(String sum){
        this.sum = new SimpleObjectProperty<>(Budget.manualMoney(sum));
    }

    public void setSumDB(BigDecimal sum){
        this.sum = new SimpleObjectProperty<>(Budget.checkNull(sum));
    }





    public BigDecimal getSum(){
        return sum.get();
    }

    public ObjectProperty<BigDecimal> getSumProperty(){
        return sum;
    }

    public void setPurpose(String purpose){
        this.purpose = new SimpleStringProperty(purpose);
    }

    public String getPurpose(){
        return purpose.get();
    }

    public StringProperty getPurposeProperty(){
        return purpose;
    }

    public void setComment(String comment){
        this.comment = new SimpleStringProperty(comment);
    }

    public String getComment(){
        return comment.get();
    }

    public StringProperty getCommentProperty(){
        return comment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", budgetItemId='" + budgetId + '\'' +
                ", contractorId='" + contractorId + '\'' +
                ", date=" + date + '\'' +
                ", sum=" + sum + '\'' +
                ", advice=" + advice +
                ", purpose='" + purpose + '\'' +
                ", source='" + source + '\'' +
                ", confirmation=" + confirmation +
                ", comment='" + comment + '\'' +
                '}';
    }

    public Payment(){

    }

    public Payment(Long companyId, Long projectId, Long budgetId, Long contractorId, Long id,
                   String date, String sum, String purpose, String source, String confirmation, String advice, String comment){

        this.companyId = new SimpleLongProperty(companyId);
        this.projectId = new SimpleLongProperty(projectId);
        this.budgetId = new SimpleLongProperty(budgetId);
        this.contractorId = new SimpleLongProperty(contractorId);

        this.id = new SimpleLongProperty(id);
        this.purpose = new SimpleStringProperty(purpose);
        this.advice = new SimpleStringProperty(advice);
        this.date = new SimpleObjectProperty<>(LocalDate.parse(date,dateFormatter));
        this.source = new SimpleStringProperty(source);
        this.sum = new SimpleObjectProperty<>(new BigDecimal(sum));
        this.confirmation = new Confirmation(confirmation);
        this.comment = new SimpleStringProperty(comment);
    }

    public Payment fillPayment(ResultSet rs) {
        try{
            this.setCompanyId(rs.getLong("company_id"));
            this.setProjectId(rs.getLong("project_id"));
            this.setBudgetId(rs.getLong("budget_id"));
            this.setContractorId(rs.getLong("contractor_id"));
            this.setId(rs.getLong("id"));
            this.setDateDB(rs.getDate("date").toLocalDate());
            this.setSumDB(rs.getBigDecimal("sum"));
            this.setPurpose(rs.getString("purpose"));
            this.setAdvice(rs.getString("advice"));
            this.setSource(rs.getString("source"));
            this.setConfirmationBool(rs.getBoolean("confirmation"));
            this.setComment(rs.getString("comment"));

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return this;
    }

    public int compareTo(Payment p1){
        return  -this.getDate().compareTo(p1.getDate());
    }


}
