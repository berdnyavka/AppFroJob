package gui.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Budget {

    private static final Logger logger = LogManager.getLogger(Budget.class);

    private Long companyId;
    private Long projectId;

    private Long id;

    private StringProperty name;
    private ObjectProperty<BigDecimal> planForMonth;
    private ObjectProperty<BigDecimal> factForMonth;

    LocalDate date;

    public Budget(){
        planForMonth = new SimpleObjectProperty<>(new BigDecimal("0"));
        factForMonth = new SimpleObjectProperty<>(new BigDecimal("0"));

    }

    public Budget(Long companyId,Long projectId, Long id, String name, String plan){
        this.companyId = companyId;
        this.projectId = projectId;
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.planForMonth = new SimpleObjectProperty<>(manualMoney(plan));
        this.factForMonth = new SimpleObjectProperty<>(manualMoney("0"));
    }

    protected static BigDecimal manualMoney(String sum){
        try{
            BigDecimal bd = new BigDecimal(sum);
            bd = bd.setScale(2, RoundingMode.HALF_DOWN);
            return bd;
        }catch (Exception ex){
            return new BigDecimal("0.00");

        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    protected static BigDecimal checkNull(BigDecimal bd){
        if(bd == null){
            return new BigDecimal("0.00");
        }
        else {
            return bd;
        }
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public BigDecimal getPlanForMonth() {
        return planForMonth.get();
    }

    public ObjectProperty<BigDecimal> getPlanForMonthProperty() {
        return planForMonth;
    }

    public void setPlanForMonth(String planForMonth) {

        this.planForMonth = new SimpleObjectProperty<>(manualMoney(planForMonth));
    }

    public void setPlanForMonthDB(BigDecimal planForMonth) {

        this.planForMonth = new SimpleObjectProperty<>(checkNull(planForMonth));
    }

    public BigDecimal getFactForMonth() {
        return factForMonth.get();
    }

    public ObjectProperty<BigDecimal> getFactForMonthProperty() {
        return factForMonth;
    }

    public void setFactForMonth(String factForMonth) {
        this.factForMonth = new SimpleObjectProperty<>(manualMoney(factForMonth));
    }

    public void setFactForMonthDB(BigDecimal factForMonth) {
        this.factForMonth = new SimpleObjectProperty<>(checkNull(factForMonth));
    }

    @Override
    public String toString() {
        return "BudgetItem{" +
                "companyId='" + companyId + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", planForMonth=" + planForMonth + '\'' +
                ", factForMonth=" + factForMonth + '\'' +
                '}';
    }

    public static Budget fillBudget(ResultSet rs) {
        Budget budget = new Budget();
        try{
            budget.setCompanyId(rs.getLong("company_id"));
            budget.setProjectId(rs.getLong("project_id"));
            budget.setId(rs.getLong("id"));
            budget.setName(rs.getString("name"));
            budget.setPlanForMonthDB(rs.getBigDecimal("plan"));
            budget.setFactForMonthDB(rs.getBigDecimal("fact"));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return budget;
    }
}
