package dao;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import gui.model.Company;
import gui.model.Payment;
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
public class DaoCompany{
    static final LocalDate ld = LocalDate.now();
    static final int month = ld.getMonthValue();
    static final int year = ld.getYear();

    private static final String GET_ALL_COMPANY = "select id, date, name, sum_receipt, sum_pay_true," +
            " sum_pay_false from company ORDER BY date DESC ";

    public static ArrayList<Company> selectAll() {
        ArrayList<Company> list = new ArrayList<>();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_ALL_COMPANY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(Company.fillCompany(rs));
            }
            log.info("DaoCompany: selectAll");
        }catch(SQLException ex){
            log.error(ex.getMessage());
        }
        return list;
    }

    private static final String INSERT_COMPANY ="insert into company (name) values (?)";

    public static long insert(Company company) throws SQLException {
        long result = -1L;
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_COMPANY, new String[]{"id"})){
            stmt.setString(1,company.getName());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }
            rs.close();
            log.info("DaoCompany: insert company "+company);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        company.setId(result);
        return result;
    }

    private static final String UPDATE_COMPANY ="update company set name = (?) where id = (?) ";

    public static void update(Company company) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_COMPANY)){
            stmt.setString(1,company.getName());
            stmt.setLong(2,company.getId());
            stmt.executeUpdate();
            log.info("DaoCompany: update "+company);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }

    }


    private static final String DELETE_COMPANY = "delete from company where id = ?";

    public static void delete(long id) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_COMPANY)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
            log.info("DaoCompany: delete where id ="+id);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }

    }


    private static final String UPDATE_SUM_PAY_T ="update company set sum_pay_true = ? where id = (?)";

    public static BigDecimal updateSumPayTrue(long id){

        ArrayList<Payment> listPayment = DaoPayment.selectFromCompany(id);
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
            log.info("DaoCompany: update sum payment true (проведенный), where sum = "+sumOfSum+" for month = "+month);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return sumOfSum;
    }

    private static final String UPDATE_SUM_PAY_F ="update company set sum_pay_false = ? where id = (?)";

    public static BigDecimal updateSumPayFalse(long id){

        ArrayList<Payment> listPayment = DaoPayment.selectFromCompany(id);
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
            log.info("DaoCompany: update sum payment false (запланированный), where sum = "+sumOfSum+" for month = "+month);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return sumOfSum;
    }

    private static final String UPDATE_SUM_RECEIPT ="update company set sum_receipt = ? where id = (?)";

    public static BigDecimal updateSumReceipts(long id){

        ArrayList<Receipt> listReceipt = DaoReceipt.selectFromCompany(id);
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
            log.info("DaoCompany: update sum receipt , where sum = "+sumOfSum+" for month = "+month);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        return sumOfSum;
    }

}
