package dao;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import gui.model.Receipt;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Log4j2
public class DaoReceipt {
    private static final String GET_FROM_COMPANY = "select company_id, project_id, budget_id, " +
            "contractor_id, id, date, source, sum, purpose, comment " +
            "from receipt where company_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Receipt> selectFromCompany(long id){
        ArrayList<Receipt> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_COMPANY)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Receipt p = new Receipt();
                p.setDateDB(rs.getDate("date").toLocalDate());
                p.setSumDB(rs.getBigDecimal("sum"));
                list.add(p);
            }
            log.info("DaoReceipt: select from company where company.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }


    public static ArrayList<Receipt> selectFromCompanyForMonth(long id, int month){
        ArrayList<Receipt> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_COMPANY)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Receipt p = new Receipt();
                p.setDateDB(rs.getDate("date").toLocalDate());
                p.setSumDB(rs.getBigDecimal("sum"));
                p.setPurpose(rs.getString("purpose"));
                list.add(p);
            }
            log.info("DaoReceipt: select from company where company.id = "+id+" month = "+month);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }

        log.debug("list of receipt before filtering by month"+list);
        LocalDate ld = LocalDate.now();
        int year = ld.getYear();
        ArrayList<Receipt> newList = new ArrayList();
        for(Receipt p: list){
            if((p.getDate().getMonthValue() == month)&&(p.getDate().getYear() == year)){
                newList.add(p);
            }
        }
        log.debug("list of receipt after filtering by month"+newList);
        return newList;
    }

    private static final String GET_FROM_BUDGET = "select company_id, project_id, budget_id, " +
            "contractor_id, id, date, source, sum, purpose, comment  " +
            "from Receipt where budget_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Receipt> selectFromBudget(long id){
        ArrayList<Receipt> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_BUDGET)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Receipt p = new Receipt();
                list.add(p.fillReceipt(rs));
            }
            log.info("DaoReceipt: select from budget where budget.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }

    private static final String GET_FROM_PROJECT = "select company_id, project_id, budget_id, " +
            "contractor_id, id, date, source, sum, purpose, comment " +
            "from Receipt where project_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Receipt> selectFromProject(long id){
        ArrayList<Receipt> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_PROJECT)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Receipt p = new Receipt();
                list.add(p.fillReceipt(rs));
            }
            log.info("DaoReceipt: select from project where project.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }

    private static final String GET_FROM_CONTRACTOR = "select company_id, project_id, budget_id, contractor_id, id, date,\n" +
            "source, sum, purpose, comment from Receipt\n" +
            "where contractor_id = (?)" +
            "ORDER BY date DESC";

    public static ArrayList<Receipt> selectFromContractor(long id) {
        ArrayList<Receipt> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_CONTRACTOR)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Receipt p = new Receipt();
                list.add(p.fillReceipt(rs));
            }
            log.info("DaoReceipt: select from contractor where contractor.id = "+id);
        }catch (SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }

    private static final String INSERT_RECEIPT = "INSERT INTO Receipt(\n" +
            "\t company_id, project_id, budget_id, contractor_id, date, sum," +
            "purpose, source, comment)\n" +
            "\t VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static long insert(Receipt p) throws SQLException {
        long result = -1L;
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_RECEIPT, new String[]{"id"})){
            stmt.setLong(1,p.getCompanyId());
            stmt.setLong(2,p.getProjectId());
            stmt.setLong(3,p.getBudgetId());
            stmt.setLong(4,p.getContractorId());
            stmt.setDate(5, Date.valueOf(p.getDate()));
            stmt.setBigDecimal(6,p.getSum());
            stmt.setString(7, p.getPurpose());
            stmt.setString(8, p.getSource());
            stmt.setString(9,p.getComment());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }
            rs.close();
            log.info("DaoReceipt: insert "+p);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        return result;
    }

    private static final String UPDATE_RECEIPT = "update Receipt set\n" +
            "\t company_id = ?, project_id = ?, budget_id = ?, contractor_id = ?, date = ?, sum = ?," +
            "\t purpose = ?, source = ?, comment = ? \n" +
            "\t where id = ?";


    public static void update(Receipt receipt) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_RECEIPT)){
            stmt.setLong(1,receipt.getCompanyId());
            stmt.setLong(2,receipt.getProjectId());
            stmt.setLong(3,receipt.getBudgetId());
            stmt.setLong(4,receipt.getContractorId());
            stmt.setDate(5, Date.valueOf(receipt.getDate()));
            stmt.setBigDecimal(6,receipt.getSum());
            stmt.setString(7,receipt.getPurpose());
            stmt.setString(8,receipt.getSource());
            stmt.setString(9,receipt.getComment());
            stmt.setLong(10,receipt.getId());
            stmt.executeUpdate();
            log.info("DaoReceipt: update "+receipt);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }


    private static final String DELETE_RECEIPT = "delete from Receipt\n" +
            "where id = ?";

    public static void delete(Long id) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_RECEIPT)){
            stmt.setLong(1, (id));
            stmt.executeUpdate();
            log.info("DaoReceipt: delete receipt where receipt.id = "+id);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

}
