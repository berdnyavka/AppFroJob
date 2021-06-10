package gui.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Company {

    Long id;
    StringProperty name;
    LocalDate date;
    ObjectProperty<BigDecimal> sumReceipt;
    ObjectProperty<BigDecimal> sumPayTrue;
    ObjectProperty<BigDecimal> sumPayFalse;

    public Company(){

    }

    public Company(Long id, String name, LocalDate date, BigDecimal sumReceipt, BigDecimal sumPayT, BigDecimal sumPayF) {
        this.name = new SimpleStringProperty(name);
        this.id = id;
        this.date = date;
        this.sumReceipt = new SimpleObjectProperty<>(sumReceipt);
        this.sumPayTrue = new SimpleObjectProperty<>(sumPayT);
        this.sumPayFalse = new SimpleObjectProperty<>(sumPayF);

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getSumReceipt() {
        return sumReceipt.get();
    }

    public ObjectProperty<BigDecimal> sumReceiptProperty() {
        return sumReceipt;
    }

    public void setSumReceipt(BigDecimal sumReceipt) {
        this.sumReceipt = new SimpleObjectProperty<>(sumReceipt);
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

    public String getName() {
        return name.get();
    }


    public StringProperty getNameProperty() {
        return name;
    }


    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Company fillCompany(ResultSet rs){
        Company cmp = new Company();
        try{
            cmp.setId(rs.getLong("id"));
            cmp.setDate(rs.getDate("date").toLocalDate());
            cmp.setName(rs.getString("name"));
            cmp.setSumReceipt(rs.getBigDecimal("sum_receipt"));
            cmp.setSumPayTrue(rs.getBigDecimal("sum_pay_true"));
            cmp.setSumPayFalse(rs.getBigDecimal("sum_pay_false"));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return cmp;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name=" + name +
                ", date=" + date +
                ", sum_receipt=" + sumReceipt +
                ", sum_pay_true=" + sumPayTrue +
                ", sum_pay_false=" + sumPayFalse +
                '}';
    }
}
