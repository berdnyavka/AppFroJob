package dao;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import gui.model.Payment;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Log4j2
public class DaoPayment {
    private static final String GET_FROM_COMPANY = "select company_id, project_id, budget_id, " +
            "contractor_id, id, date, source,sum,purpose, confirmation, comment, advice " +
            "from Payment where company_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Payment> selectFromCompany(long id){
        ArrayList<Payment> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_COMPANY)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                p.setDateDB(rs.getDate("date").toLocalDate());
                p.setSumDB(rs.getBigDecimal("sum"));
                p.setConfirmationBool(rs.getBoolean("confirmation"));
                list.add(p);
            }
            log.info("DaoPayment: select from company where company.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }


    public static ArrayList<Payment> selectFromCompanyForMonth(long id, int month){
        ArrayList<Payment> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_COMPANY)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                p.setDateDB(rs.getDate("date").toLocalDate());
                p.setSumDB(rs.getBigDecimal("sum"));
                p.setPurpose(rs.getString("purpose"));
                p.setConfirmationBool(rs.getBoolean("confirmation"));
                list.add(p);
            }
            log.info("DaoPayment: select from company for month where company.id = "+id+"month = "+month);
            log.debug("list before filtering by month"+list);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        LocalDate ld = LocalDate.now();
        int year = ld.getYear();
        ArrayList<Payment> newList = new ArrayList();
        for(Payment p: list){
            if((p.getDate().getMonthValue() == month)&&(p.getDate().getYear() == year)){
                newList.add(p);
            }
        }
        log.debug("list after filtering by month"+newList);
        return newList;
    }

    private static final String GET_FROM_PROJECT = "select company_id, project_id, budget_id, " +
            "contractor_id, id, date, source, sum, purpose, comment, advice, confirmation  " +
            "from payment where project_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Payment> selectFromProject(long id){
        ArrayList<Payment> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_PROJECT)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                list.add(p.fillPayment(rs));
            }
            log.info("DaoPayment: select from project where project.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }

    private static final String GET_FROM_BUDGET = "select company_id, project_id, budget_id, " +
            "contractor_id, id, date, source,sum,purpose, confirmation, comment, advice " +
            "from Payment where budget_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Payment> selectFromBudget(long id){
        ArrayList<Payment> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_BUDGET)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                list.add(p.fillPayment(rs));
            }
            log.info("DaoPayment: select from budget item where budget.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }

    private static final String GET_FROM_CONTRACTOR = "select company_id, project_id, budget_id, contractor_id, id, date,\n" +
            "source, sum, purpose, confirmation, comment, advice from Payment\n" +
            "where contractor_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Payment> selectFromContractor(long id) {
        ArrayList<Payment> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_CONTRACTOR)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Payment p = new Payment();
                list.add(p.fillPayment(rs));
            }
            log.info("DaoPayment: select from contractor where contractor.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }

    private static final String INSERT_PAYMENT = "INSERT INTO payment(" +
            "company_id, project_id, budget_id, contractor_id, date, sum," +
            "purpose, source, confirmation, comment, advice)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static long insert(Payment p) throws SQLException {
        long result = -1L;
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_PAYMENT, new String[]{"id"})){
            stmt.setLong(1,p.getCompanyId());
            stmt.setLong(2,p.getProjectId());
            stmt.setLong(3,p.getBudgetId());
            stmt.setLong(4,p.getContractorId());
            stmt.setDate(5, Date.valueOf(p.getDate()));
            stmt.setBigDecimal(6,p.getSum());
            stmt.setString(7, p.getPurpose());
            stmt.setString(8, p.getSource());
            stmt.setBoolean(9,p.getConfirmationBool());
            stmt.setString(10,p.getComment());
            stmt.setString(11,p.getAdvice());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }
            rs.close();

            log.info("DaoPayment: insert "+p);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        return result;
    }

    private static final String UPDATE_PAYMENT = "update payment set" +
            "company_id = ?, project_id = ?, budget_id = ?, contractor_id = ?, date = ?, sum = ?," +
            "purpose = ?, source = ?, Confirmation = ?, comment = ?, advice = ?" +
            "where id = ?";


    public static void update(Payment pay) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_PAYMENT)){
            stmt.setLong(1,pay.getCompanyId());
            stmt.setLong(2,pay.getProjectId());
            stmt.setLong(3,pay.getBudgetId());
            stmt.setLong(4,pay.getContractorId());
            stmt.setDate(5, Date.valueOf(pay.getDate()));
            stmt.setBigDecimal(6,pay.getSum());
            stmt.setString(7,pay.getPurpose());
            stmt.setString(8,pay.getSource());
            stmt.setBoolean(9,pay.getConfirmationBool());
            stmt.setString(10,pay.getComment());
            stmt.setString(11,pay.getAdvice());
            stmt.setLong(12,pay.getId());
            stmt.executeUpdate();
            log.info("DaoPayment: update "+pay);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }


    private static final String DELETE_PAYMENT = "delete from Payment\n" +
            "where id = ?";

    public static void delete(long id) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_PAYMENT)){
            stmt.setLong(1, (id));
            stmt.executeUpdate();
            log.info("DaoPayment: delete where payment.id = "+id);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

}
