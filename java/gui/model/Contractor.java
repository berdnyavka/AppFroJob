package gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.ResultSet;
import java.time.LocalDate;

public class Contractor {

    Long companyId;
    Long projectId;
    Long budgetId;

    Long id;
    StringProperty name;

    LocalDate date;

    public Contractor(){

    }

    public Contractor(Long comp_id,Long projectId, Long bug_id, Long id, String name){
        this.companyId = comp_id;
        this.projectId = projectId;
        this.budgetId = bug_id;
        this.id = id;
        this.name = new SimpleStringProperty(name);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Contractor fillContractor(ResultSet rs) {
        Contractor c = new Contractor();
        try{
            c.setCompanyId(rs.getLong("company_id"));
            c.setProjectId(rs.getLong("project_id"));
            c.setBudgetId(rs.getLong("budget_id"));
            c.setId(rs.getLong("id"));
            c.setName(rs.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }


}
