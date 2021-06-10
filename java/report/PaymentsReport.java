package report;

import com.zaxxer.hikari.HikariDataSource;
import connection.MyConnection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;



public class PaymentsReport {
 final LocalDate locald;

    public PaymentsReport(LocalDate date){
        locald = date;
        MyConnection mc = new MyConnection();
        try (HikariDataSource ds = MyConnection.getDataSource();
             Connection con = ds.getConnection()){
            showReport(con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void showReport(Connection con){

        //Path to your .jasper file in your package
        String reportName = "report/report.jasper";

        //Get a stream to read the file
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(reportName);

        try {
            HashMap<String, Object> params = new HashMap<>();


            Date date = Date.valueOf(locald);

            params.put( "DATE",date );
            //Fill the report with parameter, connection and the stream reader
            JasperPrint jp = JasperFillManager.fillReport(is, params, con);

            //Viewer for JasperReport
            JRViewer jv = new JRViewer(jp);

            //Insert viewer to a JFrame to make it showable
            JFrame jf = new JFrame();
            jf.getContentPane().add(jv);
            jf.validate();
            jf.setVisible(true);
            jf.setSize(new Dimension(1000,800));
            jf.setLocation(200,200);
            jf.setAlwaysOnTop(true);
            jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
    }

}