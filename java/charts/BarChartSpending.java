package charts;

import dao.DaoPayment;
import gui.model.Company;
import gui.model.Payment;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class BarChartSpending {

    private final HashMap<Integer,BigDecimal> payTrue;
    private final HashMap<Integer,BigDecimal> payFalse;
    private final int month;
    private final Company company;


    public BarChartSpending(Company c, int m){

        company = c;
        month = m;

        ArrayList<Payment> listPayMonth = DaoPayment.selectFromCompanyForMonth(c.getId(),m);
        payTrue = new HashMap<>();
        payFalse = new HashMap<>();
        for(Payment p: listPayMonth) {
            int dayOfPay = p.getDate().getDayOfMonth();
            BigDecimal sumOfPay = p.getSum();
            if(p.getConfirmationBool()){
                BigDecimal bd1 = payTrue.get(dayOfPay);
                payTrue.put(dayOfPay,bd1 == null ? sumOfPay : bd1.add(sumOfPay));
            }
            else{
                BigDecimal bd1 = payFalse.get(dayOfPay);
                payFalse.put(dayOfPay,bd1 == null ? sumOfPay:bd1.add(sumOfPay));
            }
        }
        start();
    }

    public static String getMonth(int month) {
        return switch (month) {
            case 1 -> "январь";
            case 2 -> "февраль";
            case 3 -> "март";
            case 4 -> "апрель";
            case 5 -> "май";
            case 6 -> "июнь";
            case 7 -> "июль";
            case 8 -> "август";
            case 9 -> "сентябрь";
            case 10 -> "октябрь";
            case 11 -> "ноябрь";
            case 12 -> "декабрь";
            default -> "none";
        };
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

        linePayment.setTitle("Статистика по платежам за "+ getMonth(month));
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("Проведенные платежи");
        series2.setName("Запланированные платежи");

        for(Integer date: payTrue.keySet()){
            series1.getData().add(new XYChart.Data(date,payTrue.get(date)));
        }

        for(Integer date: payFalse.keySet()){
            series2.getData().add(new XYChart.Data(date,payFalse.get(date)));
        }

        Scene scene  = new Scene(linePayment,1000,800);
        linePayment.getData().add(series1);
        linePayment.getData().add(series2);

        stage.setScene(scene);
        stage.show();
    }

}
