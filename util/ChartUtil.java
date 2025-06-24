package util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class ChartUtil {

    public static void showLineChart(String title, String xLabel, String yLabel, double[] xData, double[] yData) {
        XYSeries series = new XYSeries("Результат");

        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i], yData[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        createXYLineChart(title, xLabel, yLabel, dataset);
    }

    public static void showMultipleLineChart(String title, String xLabel, String yLabel,
                                             String[] seriesNames, double[] xData, double[][] yDataMatrix) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int s = 0; s < seriesNames.length; s++) {
            XYSeries series = new XYSeries(seriesNames[s]);
            for (int i = 0; i < xData.length; i++) {
                series.add(xData[i], yDataMatrix[s][i]);
            }
            dataset.addSeries(series);
        }

        createXYLineChart(title, xLabel, yLabel, dataset);
    }

    private static void createXYLineChart(String title, String xLabel, String yLabel, XYSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset,
                PlotOrientation.VERTICAL, true, true, false);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new ChartPanel(chart));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

