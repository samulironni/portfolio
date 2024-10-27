package test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NvidiaGpuMonitor implements Runnable {
    private volatile boolean running = true;
    private ProgressBar progressBar;
    private Label gpuLabel; // Lisätään Label-kenttä

    public NvidiaGpuMonitor(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    // Lisätään setteri gpuLabel-kentälle
    public void setGpuLabel(Label gpuLabel) {
        this.gpuLabel = gpuLabel;
    }

    @Override
    public void run() {
        while (running) {
            double gpuUsage = getGpuUsage();
            Platform.runLater(() -> {
                progressBar.setProgress(gpuUsage);
                gpuLabel.setText("GPU Usage: " + (int)(gpuUsage * 100) + "%"); // Päivitetään GPU-käyttöprosentti tekstikomponenttiin
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private double getGpuUsage() {
        double gpuUsage = 0.0;
        try {
            Process process = Runtime.getRuntime().exec("nvidia-smi");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Default")) { // Oletusyksikössä
                    String[] parts = line.split("\\s+");
                    gpuUsage = Double.parseDouble(parts[12].replace("%", "")) / 100.0;
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpuUsage;
    }

    public void stop() {
        running = false;
    }
}
