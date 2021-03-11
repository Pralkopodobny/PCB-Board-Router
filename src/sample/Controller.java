package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import javafx.event.ActionEvent;

public class Controller {
    @FXML
    LineChart<String, Number> mychart;
    public void btn(ActionEvent event){
        if (mychart == null) System.out.println("DEBIL");
        else {
            mychart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<String, Number>("Marceli", 2137));
            series.getData().add(new XYChart.Data<String, Number>("Ja", 2222));
            mychart.getData().add(series);
        }
    }

}
