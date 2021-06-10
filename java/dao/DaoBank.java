package dao;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import gui.model.Bank;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Log4j2
public class DaoBank {
    private static final String GET_FROM_COMPANY = "select company_id, id, " +
            "name, account, date from bank where company_id = ? ORDER BY date DESC";

    public static ArrayList<Bank> selectFromCompany(long id) {
        ArrayList<Bank> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_COMPANY)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Bank.fillBank(rs));
            }
            log.info("DaoBank: selectFromCompany where id = "+id);
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }
        return list;
    }

    private static final String INSERT_BANK ="insert into Bank (company_id, name, account) " +
            "values (?, ?, ?)";
    public static long insert(Bank bank) throws SQLException {
        long result = -1L;
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_BANK, new String[]{"id"})){
            stmt.setLong(1,bank.getCompanyId());
            stmt.setString(2,bank.getName());
            stmt.setBigDecimal(3,bank.getAccount());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }
            rs.close();
           log.info("DaoBank: insert bank "+bank);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        return result;

    }

    private static final String UPDATE_BANK ="update Bank set name = ?, account = ?" +
            " where id = (?)";

    public static void update(Bank bank) throws SQLException {
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_BANK)){
            stmt.setString(1,bank.getName());
            stmt.setBigDecimal(2,bank.getAccount());
            stmt.setLong(3,bank.getId());
            stmt.executeUpdate();
           log.info("DaoBank: update bank "+bank);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    private static final String DELETE_BANK = "delete from Bank where id = ?";

    public static void delete(long id) throws SQLException {
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_BANK)){
            stmt.setLong(1, (id));
            stmt.executeUpdate();
           log.info("DaoBank: delete bank where id = "+id);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

}
