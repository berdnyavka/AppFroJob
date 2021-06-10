package charts;

import dao.DaoReceipt;
import gui.model.Company;
import gui.model.Receipt;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static charts.BarChartSpending.getMonth;

public class BarChartReceipt {

    private HashMap<Integer,BigDecimal> receipts;
    private final int month;
    private final Company company;


    public BarChartReceipt(Company c, int m){
        receipts = new HashMap<>();

        company = c;
        month = m;

        ArrayList<Receipt> listReceipt = DaoReceipt.selectFromCompanyForMonth(c.getId(),m);

        for(Receipt r: listReceipt) {
            int day = r.getDate().getDayOfMonth();
            BigDecimal sum = r.getSum();

            BigDecimal bd = receipts.get(day);
            receipts.put(day, bd == null ? sum : bd.add(sum));
        }
        if(receipts != null) {
            start();
        }else{
            notFound();
        }

    }

    public static void notFound(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Статистика");
        alert.setHeaderText("Статус:");
        alert.setContentText("поступлений на данную дату нет.");
        alert.showAndWait();
    }

    public void start() {
        Stage stage = new Stage();
        stage.setTitle("Статистика: "+ company.getName());

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("День");
        yAxis.setLabel("Сумма");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(1);
        switch (month) {
            case 2 -> xAxis.setUpperBound(29);
            case 1, 3, 5, 7, 8, 10, 12 -> xAxis.setUpperBound(31);
            case 4, 6, 9, 11 -> xAxis.setUpperBound(30);
        }
        xAxis.setTickUnit(1);

        final LineChart<Integer, BigDecimal> linePayment =
                new LineChart(xAxis,yAxis);

        linePayment.setTitle("Статистика по поступлениям за "+getMonth(month));
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Поступления по проектам");

        for(Integer date: receipts.keySet()){
            series2.getData().add(new XYChart.Data(date,receipts.get(date)));
        }

        Scene scene  = new Scene(linePayment,1000,800);
        linePayment.getData().add(series2);

        stage.setScene(scene);
        stage.show();
    }

}
