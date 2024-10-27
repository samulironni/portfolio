package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.CpuMonitor;
import test.MemoryMonitor;
import test.NvidiaGpuMonitor;

public class Main extends Application {

    private CpuMonitor cpuUsageMonitor;
    private MemoryMonitor memoryUsageMonitor;
    private NvidiaGpuMonitor gpuUsageMonitor;

    @Override
    public void start(Stage primaryStage) {
        // Luo CpuMonitor-olio ProgressBar-parametrilla
        ProgressBar cpuProgressBar = new ProgressBar();
        cpuUsageMonitor = new CpuMonitor(cpuProgressBar);

        // Luo MemoryMonitor-olio ProgressBar-parametrilla
        ProgressBar memoryProgressBar = new ProgressBar();
        memoryUsageMonitor = new MemoryMonitor(memoryProgressBar);

        // Luo NvidiaGpuMonitor-olio ProgressBar-parametrilla
        ProgressBar gpuProgressBar = new ProgressBar();
        gpuUsageMonitor = new NvidiaGpuMonitor(gpuProgressBar);

        // Luodaan käyttöliittymä
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        HBox cpuBox = new HBox(10);
        // Print the usage of CPU
        Label cpuUsageLabel = new Label("0%");
        cpuBox.getChildren().addAll(cpuUsageLabel);

        HBox memoryBox = new HBox(10);
        // Print the usage of memory
        Label memoryUsageLabel = new Label("0%");
        memoryBox.getChildren().addAll(memoryUsageLabel);

        HBox gpuBox = new HBox(10);
        // Print the usage of GPU
        Label gpuUsageLabel = new Label("0%");
        gpuBox.getChildren().addAll(gpuUsageLabel);

        root.getChildren().addAll(
                cpuBox,
                cpuProgressBar,
                memoryBox,
                memoryProgressBar,
                gpuBox,
                gpuProgressBar
        );

        // Näytä käyttöliittymä
        Scene scene = new Scene(root, 250, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Resource Monitor");
        primaryStage.show();

        // Liitetään CPU:n monitoriin label näyttämään prosenttiosuutta
        cpuUsageMonitor.setCpuLabel(cpuUsageLabel);

        // Aloita CPU-monitorointi
        Thread cpuThread = new Thread(cpuUsageMonitor);
        cpuThread.start();

        // Liitetään muistin monitoriin label näyttämään prosenttiosuutta
        memoryUsageMonitor.setMemoryLabel(memoryUsageLabel);

        // Aloita muistin monitorointi
        Thread memoryThread = new Thread(memoryUsageMonitor);
        memoryThread.start();

        // Liitetään GPU:n monitoriin label näyttämään prosenttiosuutta
        gpuUsageMonitor.setGpuLabel(gpuUsageLabel);

        // Aloita GPU-monitorointi
        Thread gpuThread = new Thread(gpuUsageMonitor);
        gpuThread.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        cpuUsageMonitor.stop();
        memoryUsageMonitor.stop();
        gpuUsageMonitor.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
