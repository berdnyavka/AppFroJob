package charts;

import dao.DaoProject;
import gui.model.Company;
import gui.model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.stage.Stage;

import java.util.ArrayList;

public class PieChartReceipt {

    public PieChartReceipt(Company company) {
        Stage stage = new Stage();
        Scene scene = new Scene(new Group());
        stage.setTitle("Получение средств по проектам");
        stage.setWidth(1000);
        stage.setHeight(800);

        ArrayList<Project> list = DaoProject.selectFromCompany(company.getId());
        ObservableList<Data> pie =
                FXCollections.observableArrayList();
        for(Project p : list){

            if(p.getSumReciept() != null) {
                pie.add(new Data(p.getName(), p.getSumReciept().doubleValue()));
                System.out.println(p.getName()+" "+p.getSumReciept().doubleValue());
            }
        }

        final PieChart chart = new PieChart(pie);
        chart.setTitle("Компания "+company.getName());


        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        stage.show();
    }

}
