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
public class Receipt {
    private static final Logger logger = LogManager.getLogger(Receipt.class);


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
        return "Receipt{" +
                "companyId='" + companyId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", budgetItemId='" + budgetId + '\'' +
                ", contractorId='" + contractorId + '\'' +
                ", date=" + date + '\'' +
                ", sum=" + sum + '\'' +
                ", purpose='" + purpose + '\'' +
                ", source='" + source + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public Receipt(){

    }

    public Receipt(Long companyId, Long projectId, Long budgetId, Long contractorId, Long id,
                   String date, String sum, String purpose, String source, String comment){

        this.companyId = new SimpleLongProperty(companyId);
        this.projectId = new SimpleLongProperty(projectId);
        this.budgetId = new SimpleLongProperty(budgetId);
        this.contractorId = new SimpleLongProperty(contractorId);

        this.id = new SimpleLongProperty(id);
        this.purpose = new SimpleStringProperty(purpose);
        this.date = new SimpleObjectProperty<>(LocalDate.parse(date,dateFormatter));
        this.source = new SimpleStringProperty(source);
        this.sum = new SimpleObjectProperty<>(new BigDecimal(sum));
        this.comment = new SimpleStringProperty(comment);
    }

    public Receipt fillReceipt(ResultSet rs) {
        try{
            this.setCompanyId(rs.getLong("company_id"));
            this.setProjectId(rs.getLong("project_id"));
            this.setBudgetId(rs.getLong("budget_id"));
            this.setContractorId(rs.getLong("contractor_id"));
            this.setId(rs.getLong("id"));
            this.setDateDB(rs.getDate("date").toLocalDate());
            this.setSumDB(rs.getBigDecimal("sum"));
            this.setPurpose(rs.getString("purpose"));
            this.setSource(rs.getString("source"));
            this.setComment(rs.getString("comment"));

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return this;
    }

    public int compareTo(Receipt p1){
        return  -this.getDate().compareTo(p1.getDate());
    }


}
