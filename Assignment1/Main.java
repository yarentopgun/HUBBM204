import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class Main {

    public static final int[] sizes = {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};


    public static void main(String args[]) throws IOException {

        String filepath = args[0];

        List<Integer> flowDurations = DatasetProcessor.readFlowDurations(filepath);

        int[][] subsets = DatasetProcessor.createSubsets(flowDurations, sizes);

        DatasetProcessor.InsertionSortWithRandomData(subsets);
        DatasetProcessor.MergeSortWithRandomData(subsets);
        DatasetProcessor.CountingSortWithRandomData(subsets);

        DatasetProcessor.InsertionSortWithSortedData();
        DatasetProcessor.MergeSortWithSortedData();
        DatasetProcessor.CountingSortWithSortedData();

        DatasetProcessor.InsertionSortWithReversedData();
        DatasetProcessor.MergeSortWithReversedData();
        DatasetProcessor.CountingSortWithReversedData();

        DatasetProcessor.LinearSearchWithUnsorted(subsets);
        DatasetProcessor.LinearSearchWithSorted();
        DatasetProcessor.BinarySearchWithSorted();


        double[][] yAxis = new double[3][10];
        yAxis[0] =  InsertionSort.averageTimesRandom ;// Insertion Sort
        yAxis[1] = MergeSort.averageTimesRandom;  // Merge Sort
        yAxis[2] = CountingSort.averageTimesRandom;  // Counting Sort

        showAndSaveChartForSorting("Sorting Tests on Random Data", sizes, yAxis);


        double[][] yAxis2 = new double[3][10];
        yAxis2[0] =  InsertionSort.averageTimesSorted ;// Insertion Sort
        yAxis2[1] = MergeSort.averageTimesSorted;  // Merge Sort
        yAxis2[2] = CountingSort.averageTimesSorted;  // Counting Sort

        showAndSaveChartForSorting("Sorting Tests on Sorted Data", sizes, yAxis2);


        double[][] yAxis3 = new double[3][10];
        yAxis3[0] =  InsertionSort.averageTimesReversed ;// Insertion Sort
        yAxis3[1] = MergeSort.averageTimesReversed;  // Merge Sort
        yAxis3[2] = CountingSort.averageTimesReversed;  // Counting Sort

        showAndSaveChartForSorting("Sorting Tests on Reversed Data", sizes, yAxis3);


        double[][] yAxis4 = new double[3][10];
        yAxis4[0] = LinearSearch.averageTimesRandom ;// Linear Search
        yAxis4[1] = LinearSearch.averageTimesSorted;  // Linear Search
        yAxis4[2] = BinarySearch.averageTimesSorted;  // Binary Search

        showAndSaveChartForSearching("Searching Tests", sizes, yAxis4);

    }

    public static void showAndSaveChartForSorting(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Milliseconds").xAxisTitle("Input Size").build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart

        // Add a plot for a sorting algorithm
        chart.addSeries("Insertion Sort", doubleX, yAxis[0]);
        chart.addSeries("Merge Sort", doubleX, yAxis[1]);
        chart.addSeries("Counting Sort", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();


    }

    public static void showAndSaveChartForSearching(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Nanoseconds").xAxisTitle("Input Size").build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Add a plot for a sorting algorithm
        chart.addSeries("Linear Search(Random Data)", doubleX, yAxis[0]);
        chart.addSeries("Linear Search(Sorted Data)", doubleX, yAxis[1]);
        chart.addSeries("Binary Search(Sorted Data)", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }

}
