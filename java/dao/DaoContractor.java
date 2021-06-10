package dao;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import gui.model.Contractor;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Log4j2
public class DaoContractor {

    private static final String GET_FROM_BUDGET = "select company_id, project_id, budget_id,id," +
            "name from contractor where budget_id = (?) ORDER BY date DESC";

    public static ArrayList<Contractor> selectFromBudget(long id) {
        ArrayList<Contractor> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_BUDGET)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Contractor.fillContractor(rs));
            }
            log.info("DaoContractor: select from budget where budget.id = "+id);
        }catch (SQLException e){
            log.error(e.getMessage());
        }
        return list;
    }

    private static final String GET_FROM_COMPANY = "select company_id, project_id, budget_id, contractor_id," +
            "name from contractor where company_id = (?)";
    public ArrayList<Contractor> selectFromCompany(long id) {
        ArrayList<Contractor> list = new ArrayList();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(GET_FROM_COMPANY)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Contractor.fillContractor(rs));
            }
            log.info("DaoContractor: select from company where company.id = "+id);
        }catch(SQLException e){
            log.error(e.getMessage());
        }
        return list;
    }

    private static final String INSERT_CONTRACTOR ="insert into contractor (company_id, project_id, budget_id, name)" +
            " values (?, ?, ?, ?)";

    public static long insert(Contractor contr) throws SQLException {
        long result = -1L;
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_CONTRACTOR, new String[]{"id"})){
            stmt.setLong(1,contr.getCompanyId());
            stmt.setLong(2,contr.getProjectId());
            stmt.setLong(3,contr.getBudgetId());
            stmt.setString(4,contr.getName());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }
            rs.close();
            log.info("DaoContractor: insert "+contr);
            return result;
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    private static final String UPDATE_CONTRACTOR ="update contractor set name = ?" +
            "where id = ?";

    public static void update(Contractor contr) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(UPDATE_CONTRACTOR)){
            stmt.setString(1,contr.getName());
            stmt.setLong(2,contr.getId());
            stmt.executeUpdate();
            log.info("DaoContractor: update "+contr);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    private static final String DELETE_CONTRACTOR = "delete from contractor\n" +
            "where id = ?";

    public static void delete(long id) throws SQLException {
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(DELETE_CONTRACTOR)){
            stmt.setLong(1, (id));
            stmt.executeUpdate();
            log.info("DaoContractor: delete where contractor.id = "+id);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
    }

}



