package gui.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Project {

    Long companyId;
    Long id;
    StringProperty name;

    ObjectProperty<BigDecimal> sumReceipt;
    ObjectProperty<BigDecimal> sumPayTrue;
    ObjectProperty<BigDecimal> sumPayFalse;

    LocalDate date;
    public Project() {

    }

    public Project(Long companyId, Long id, String name) {
        this.companyId = companyId;
        this.id = id;
        this.name = new SimpleStringProperty(name);
    }



    public void setSumReceipt(BigDecimal sumReceipt) {
        this.sumReceipt = new SimpleObjectProperty<>(sumReceipt) {
        };
    }

    public BigDecimal getSumReciept() {
        return sumReceipt.get();
    }

    public ObjectProperty<BigDecimal> sumReceiptProperty() {
        return sumReceipt;
    }

    public BigDecimal getSumPayTrue() {
        return sumPayTrue.get();
    }

    public ObjectProperty<BigDecimal> sumPayTrueProperty() {
        return sumPayTrue;
    }

    public void setSumPayTrue(BigDecimal sumPayTrue) {
        this.sumPayTrue = new SimpleObjectProperty<>(sumPayTrue);
    }

    public BigDecimal getSumPayFalse() {
        return sumPayFalse.get();
    }

    public ObjectProperty<BigDecimal> sumPayFalseProperty() {
        return sumPayFalse;
    }

    public void setSumPayFalse(BigDecimal sumPayFalse) {
        this.sumPayFalse = new SimpleObjectProperty<>(sumPayFalse);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public StringProperty getNameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    @Override
    public String toString() {
        return "Project{" +
                "companyId=" + companyId +
                ", id=" + id +
                ", name=" + name +
                ", sum_receipt=" + sumReceipt +
                ", sum_pay_true=" + sumPayTrue +
                ", sum_pay_false=" + sumPayFalse +
                ", date=" + date +
                '}';
    }

    public static Project fillProject(ResultSet rs) {
        Project p = new Project();
        try{
            p.setCompanyId(rs.getLong("company_id"));
            p.setId(rs.getLong("id"));
            p.setName(rs.getString("name"));
            p.setSumReceipt(rs.getBigDecimal("sum_receipt"));
            p.setSumPayTrue(rs.getBigDecimal("sum_pay_true"));
            p.setSumPayFalse(rs.getBigDecimal("sum_pay_false"));

        } catch (Exception e) {
            //logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return p;
    }
}
