package test;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class CpuMonitor implements Runnable {
    private volatile boolean running = true;
    private ProgressBar progressBar;
    private Label cpuLabel; 

    public CpuMonitor(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setCpuLabel(Label cpuLabel) {
        this.cpuLabel = cpuLabel;
    }

    @Override
    public void run() {
        while (running) {
            double cpuLoad = getCPULoad();
            Platform.runLater(() -> {
                progressBar.setProgress(cpuLoad);
                // Tarkistetaan, ettei cpuLabel ole null
                if (cpuLabel != null) {
                    cpuLabel.setText("CPU Usage: " + (int)(cpuLoad * 100) + "%"); 
                }        
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private double getCPULoad() {
        OperatingSystemMXBean osMxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = osMxBean.getSystemCpuLoad();
        return cpuLoad;
    }

    public void stop() {
        running = false;
    }
}
