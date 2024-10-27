package test;

import com.sun.management.OperatingSystemMXBean;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.lang.management.ManagementFactory;

public class MemoryMonitor implements Runnable {
    private volatile boolean running = true;
    private ProgressBar progressBar;
    private Label memoryLabel;

    public MemoryMonitor(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setMemoryLabel(Label memoryLabel) {
        this.memoryLabel = memoryLabel;
    }

    @Override
    public void run() {
        while (running) {
            double memoryUsage = getMemoryUsage();
            Platform.runLater(() -> {
                progressBar.setProgress(memoryUsage);
                // Tarkistetaan, ettei memoryLabel ole null
                if (memoryLabel != null) {
                    memoryLabel.setText("Memory usage: " + (int)(memoryUsage * 100) + "%"); 
                }        
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private double getMemoryUsage() {
        OperatingSystemMXBean osMxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double freeMemory = osMxBean.getFreePhysicalMemorySize();
        double totalMemory = osMxBean.getTotalPhysicalMemorySize();
        double memoryUsage = (totalMemory - freeMemory) / totalMemory;
        return memoryUsage;
    }

    public void stop() {
        running = false;
    }
}
