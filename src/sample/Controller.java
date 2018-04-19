package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Controller {

    private Scene scene;
    private double x0_value;
    private double y0_value;
    private double xmax_value;
    private int steps_value;
    private double step_size;

    public void setScene(Scene scene){
        this.scene = scene;
    }

    /**
     * Standard function, called when a request for Functions and Errors graphs are requested
     * @param event
     */
    @FXML
    private void onRender1ButtonClicked(ActionEvent event)
    {
        try {
            TextField x0 = (TextField) scene.lookup("#x0");
            TextField y0 = (TextField) scene.lookup("#x0");
            TextField xmax = (TextField) scene.lookup("#xmax");
            TextField steps = (TextField) scene.lookup("#steps");

            boolean show_exact = ((CheckBox) scene.lookup("#exact")).isSelected();
            boolean show_euler = ((CheckBox) scene.lookup("#euler")).isSelected();
            boolean show_ei = ((CheckBox) scene.lookup("#ei")).isSelected();
            boolean show_rk = ((CheckBox) scene.lookup("#rk")).isSelected();

            x0_value = Double.parseDouble(x0.getText());
            y0_value = Double.parseDouble(y0.getText());
            xmax_value = Double.parseDouble(xmax.getText());
            steps_value = Integer.parseInt(steps.getText());
            step_size = (xmax_value - x0_value) / steps_value;

            LineChart chart1 = initiallizeChart("#chart1", "x", "y", x0_value, xmax_value, step_size);

            LineChart chart2 = initiallizeChart("#chart2", "x", "Error", x0_value, xmax_value, step_size);

            chart1.setTitle("Function");
            chart2.setTitle("Error for every step");

            Computer computer = new Computer((x, y) -> (y * y) / (x * x) - 2, steps_value, y0_value, x0_value, xmax_value);
            double[] exactData = computer.ComputeExactFunction((x)-> - x * (x * x * x - 4) / (x * x * x + 2));
            //defining a series
            if(show_euler){
                double[] data = computer.EulerMethod();

                chart1.getData().add(addDataToSeries(data, x0_value, step_size, "Euler"));

                for (int i = 0; i < data.length; i++)
                    data[i] = Math.abs(exactData[i] - data[i]);

                chart2.getData().add(addDataToSeries(data, x0_value, step_size, "Euler"));
            }

            if(show_ei){
                double[] data = computer.UpgradedEulerMethod();

                chart1.getData().add(addDataToSeries(data, x0_value, step_size, "Improved Euler"));

                for (int i = 0; i < data.length; i++)
                    data[i] = Math.abs(exactData[i] - data[i]);

                chart2.getData().add(addDataToSeries(data, x0_value, step_size, "Improved Euler"));
            }

            if(show_rk){
                double[] data = computer.RungeKuttaMethod();

                chart1.getData().add(addDataToSeries(data, x0_value, step_size, "Runge-Kutta"));

                for (int i = 0; i < data.length; i++)
                    data[i] = Math.abs(exactData[i] - data[i]);

                chart2.getData().add(addDataToSeries(data, x0_value, step_size, "Runge-Kutta"));
            }

            if(show_exact){
                chart1.getData().add(addDataToSeries(exactData, x0_value, step_size, "Exact"));
            }

        } catch(Exception e){
            e.printStackTrace();
            System.out.println("parse error!");
        }
    }

    /**
     * Standard function, called when a request for Maximum Error graph is requested
     * @param event
     */
    @FXML
    private void onErrorButtonClick(ActionEvent event) {
        TextField n = (TextField) scene.lookup("#n");
        TextField N = (TextField) scene.lookup("#N");

        boolean show_error_Euler = ((CheckBox) scene.lookup("#errore")).isSelected();
        boolean show_error_Improved = ((CheckBox) scene.lookup("#errorie")).isSelected();
        boolean show_error_RoungeKouta = ((CheckBox) scene.lookup("#errorrk")).isSelected();

        int nStart = Integer.parseInt(n.getText());
        int nEnd = Integer.parseInt(N.getText());

        LineChart chart3 = initiallizeChart("#chart3", "steps", "maxError", (double)nStart, (double)nEnd, (double)1);

        double[] maxEulerError = new double[nEnd - nStart + 1];
        double[] maxImprovedEulerError = new double[nEnd - nStart + 1];
        double[] maxRoungeKoutaError = new double[nEnd - nStart + 1];

        for (int i = nStart; i <= nEnd; i++) {
            Computer c = new Computer((x, y) -> (y * y) / (x * x) - 2, i, y0_value, x0_value, xmax_value);

            double[] data = c.ComputeExactFunction((x)-> - x * (x * x * x - 4) / (x * x * x + 2));
            double[] dataEuler = c.EulerMethod();
            double[] dataImproved = c.UpgradedEulerMethod();
            double[] dataRoungeKouta = c.RungeKuttaMethod();

            double max1 = 0;
            double max2 = 0;
            double max3 = 0;

            for (int j = 0; j < data.length; j++) {
                if (max1 < Math.abs(data[j] - dataEuler[j]))
                    max1 = Math.abs(data[j] - dataEuler[j]);

                if (max2 < Math.abs(data[j] - dataImproved[j]))
                    max2 = Math.abs(data[j] - dataImproved[j]);

                if (max3 < Math.abs(data[j] - dataRoungeKouta[j]))
                    max3 = Math.abs(data[j] - dataRoungeKouta[j]);
            }


                maxEulerError[i - nStart] = max1;
                maxImprovedEulerError[i - nStart] = max2;
                maxRoungeKoutaError[i - nStart] = max3;
        }

        if (show_error_Euler)
            chart3.getData().add(addDataToSeries(maxEulerError, nStart, 1, "Euler"));

        if (show_error_Improved)
            chart3.getData().add(addDataToSeries(maxImprovedEulerError, nStart, 1, "Improved Euler"));

        if (show_error_RoungeKouta)
            chart3.getData().add(addDataToSeries(maxRoungeKoutaError, nStart,1, "Runge-Kutta"));
    }

    /**
     * adds data to series to insert in a chart in future
     */
    private XYChart.Series addDataToSeries(double[] data, double start_value, double step_size, String name) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        double x = start_value;

        for (int i = 0; i < data.length; i++) {
            series.getData().add(new XYChart.Data(x, data[i]));
            x += step_size;
        }

        return series;
    }

    /**
     * Initiallizes one particular chart
     */
    private LineChart initiallizeChart(String selector, String xAxis, String yAxis, double lowerBound, double upperBound, double step_size) {
        LineChart chart = (LineChart) scene.lookup(selector);
        chart.getData().clear();

        NumberAxis xAxisFirst = (NumberAxis) chart.getXAxis();
        xAxisFirst.setLabel(xAxis);

        NumberAxis yAxisFirst = (NumberAxis) chart.getYAxis();
        yAxisFirst.setLabel(yAxis);

        xAxisFirst.setAutoRanging(false);

        xAxisFirst.setLowerBound(lowerBound);
        xAxisFirst.setUpperBound(upperBound);

        xAxisFirst.setTickUnit(step_size);

        return chart;
    }
}
