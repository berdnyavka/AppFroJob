package dao;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import gui.model.Payment;
import gui.model.Project;
import gui.model.Receipt;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@Log4j2
public class DaoProject {
    static final LocalDate ld = LocalDate.now();
    static final int month = ld.getMonthValue();
    static final int year = ld.getYear();

    private static final String GET_FROM_COMPANY = "select company_id, id, sum_receipt, sum_pay_true, sum_pay_false," +
            "name from Project where company_id = (?) ORDER BY date DESC";

    public static ArrayList<Project> selectFromCompany(long id) {
        ArrayList<Project> list = new ArrayList();
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_FROM_COMPANY)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Project.fillProject(rs));
            }
            log.info("DaoProject: select from company,where company.id = "+id);
        }catch(SQLException e){
           log.error(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }


    private static final String UPDATE_SUM_PAY_T ="update project set sum_pay_true = ? where id = (?)";

    public static BigDecimal updateSumPayTrue(long id){

        ArrayList<Payment> listPayment = DaoPayment.selectFromProject(id);
        BigDecimal sumOfSum = new BigDecimal("0");
        for(Payment p: listPayment){
            if(((p.getDate().getMonthValue() == month)&&(p.getConfirmationBool())&&(p.getDate().getYear() == year))){
                sumOfSum = sumOfSum.add(p.getSum());
            }
        }
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_SUM_PAY_T);){
            stmt.setBigDecimal(1,sumOfSum);
            stmt.setLong(2,id);
            stmt.executeUpdate();
            log.info("DaoProject: update sum payment true - проведен, where project.id = "+id+" and sum ="+sumOfSum);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return sumOfSum;
    }

    private static final String UPDATE_SUM_PAY_F ="update project set sum_pay_false = ? where id = (?)";

    public static BigDecimal updateSumPayFalse(long id){

        ArrayList<Payment> listPayment = DaoPayment.selectFromProject(id);
        BigDecimal sumOfSum = new BigDecimal("0");
        for(Payment p: listPayment){
            if((p.getDate().getMonthValue() == month)&&(!p.getConfirmationBool())&&(p.getDate().getYear() == year)){
                sumOfSum = sumOfSum.add(p.getSum());
            }
        }

        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_SUM_PAY_F);){
            stmt.setBigDecimal(1,sumOfSum);
            stmt.setLong(2,id);
            stmt.executeUpdate();
            log.info("DaoProject: update sum payment false - запланирован, where project.id = "+id+" and sum ="+sumOfSum);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return sumOfSum;
    }

    private static final String UPDATE_SUM_RECEIPT ="update project set sum_receipt = ? where id = (?)";

    public static BigDecimal updateSumReceipts(long id){

        ArrayList<Receipt> listReceipt = DaoReceipt.selectFromProject(id);

        BigDecimal sumOfSum = new BigDecimal("0");
        for(Receipt r: listReceipt){
            if((r.getDate().getMonthValue() == month)&&(r.getDate().getYear() == year)){
                sumOfSum = sumOfSum.add(r.getSum());
            }
        }

        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_SUM_RECEIPT);){
            stmt.setBigDecimal(1,sumOfSum);
            stmt.setLong(2,id);
            stmt.executeUpdate();
            log.info("DaoProject: update sum receipt, where project.id = "+id+" and sum ="+sumOfSum);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return sumOfSum;
    }

    private static final String INSERT_PROJECT ="insert into Project (company_id, name)" +
            " values (?, ?)";

    public static long insert(Project project) throws SQLException {
        long result = -1L;
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_PROJECT, new String[]{"id"})){
            stmt.setLong(1,project.getCompanyId());
            stmt.setString(2,project.getName());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }
            rs.close();
           log.info("DaoProject: insert "+project);
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    private static final String UPDATE_PROJECT ="update Project set name = ?" +
            "where id = ?";

    public static void update(Project project) throws SQLException {
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_PROJECT)){
            stmt.setString(1,project.getName());
            stmt.setLong(2,project.getId());
            stmt.executeUpdate();
           log.info("DaoProject: update "+project);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }

    }

    private static final String DELETE_PROJECT = "delete from Project where id = ?";

    public static void delete(long id) throws SQLException {
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_PROJECT)){
            stmt.setLong(1, (id));
            stmt.executeUpdate();
            log.info("DaoProject: delete, where id = "+id);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

}



