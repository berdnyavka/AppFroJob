package dao;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import gui.model.Budget;
import gui.model.Payment;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@Log4j2
public class DaoBudget {

    private static final String GET_FROM_PROJECT = "select company_id, project_id, id, " +
            "name, plan, fact from Budget where project_id = ? ORDER BY date DESC";

    public static ArrayList<Budget> selectFromProject(long project_id) {
        ArrayList<Budget> list = new ArrayList();
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_FROM_PROJECT)) {
            stmt.setLong(1, project_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Budget.fillBudget(rs));
            }
           log.info("DaoBudget: select from project where project.id = "+project_id);
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }
        return list;
    }

    private static final String INSERT_BUDGET ="insert into budget (company_id, project_id, name, plan) " +
            "values (?, ?, ?, ?)";
    public static long insert(Budget budget) throws SQLException {
        long result = -1L;
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(INSERT_BUDGET, new String[]{"id"})){
            stmt.setLong(1,budget.getCompanyId());
            stmt.setLong(2,budget.getProjectId());
            stmt.setString(3,budget.getName());
            stmt.setBigDecimal(4,budget.getPlanForMonth());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }
            rs.close();
           log.info("DaoBudget: insert budget "+budget);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        return result;

    }

    private static final String UPDATE_BUDGET ="update budget set name = ?,plan = ?" +
            " where id = (?)";

    public static void update(Budget budget) throws SQLException {
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_BUDGET)){
            stmt.setString(1,budget.getName());
            stmt.setBigDecimal(2,budget.getPlanForMonth());
            stmt.setLong(3,budget.getId());
            stmt.executeUpdate();
            log.info("DaoBudget: update budget "+budget);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    private static final String UPDATE_FACT ="update budget set fact = ?" +
            " where id = (?)";

    public static BigDecimal updateFact(long id){

        BigDecimal sumOfSum = new BigDecimal("0");
        ArrayList<Payment> listPayment = DaoPayment.selectFromBudget(id);

        LocalDate ld = LocalDate.now();
        int month = ld.getMonthValue();
        int year = ld.getYear();

        for(Payment p: listPayment){
            if((p.getDate().getMonthValue() == month)&&(p.getDate().getYear() == year)){
                sumOfSum = sumOfSum.add(p.getSum());
            }
        }

       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_FACT)){
            stmt.setBigDecimal(1,sumOfSum);
            stmt.setLong(2,id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        }
        log.info("DaoBudget: summa of payment for current month = "+sumOfSum+" where budget.id = "+id);
        return sumOfSum;
    }


    private static final String DELETE_BUDGET = "delete from Budget where id = ?";

    public static void delete(long id) throws SQLException {
       try (HikariDataSource ds = MyConnection.getDataSource();
            Connection con = ds.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_BUDGET)){
            stmt.setLong(1, (id));
            stmt.executeUpdate();
            log.info("DaoBudget: deleted budget where id = "+id);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }
}
