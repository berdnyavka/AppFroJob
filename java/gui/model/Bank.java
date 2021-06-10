package gui.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.ResultSet;

@Log4j2
public class Bank {

    private static final Logger logger = LogManager.getLogger(Bank.class);


    private Long companyId;
    private Long id;
    private StringProperty name;
    private ObjectProperty<BigDecimal> account;


    public Bank(){

    }

    public Bank(Long companyId, Long id, String name, BigDecimal acc){
        this.companyId = companyId;
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.account = new SimpleObjectProperty<>(acc);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public BigDecimal getAccount() {
        return account.get();
    }

    public void setAccount(String number) {
        this.account = new SimpleObjectProperty<>(new BigDecimal(number));
    }

    public ObjectProperty<BigDecimal> accountProperty() {
        return account;
    }

    public void setAccountDB(BigDecimal number) {
        this.account = new SimpleObjectProperty<>(number);
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

    public static Bank fillBank(ResultSet rs) {
        Bank b = new Bank();
        try{
            b.setCompanyId(rs.getLong("company_id"));
            b.setId(rs.getLong("id"));
            b.setName(rs.getString("name"));
            b.setAccountDB(rs.getBigDecimal("account"));

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            e.printStackTrace();
        }
        return b;
    }
}
