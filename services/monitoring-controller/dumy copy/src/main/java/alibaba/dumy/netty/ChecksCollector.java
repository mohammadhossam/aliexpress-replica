package alibaba.dumy.netty;

import alibaba.dumy.metrics.ResourceMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ChecksCollector implements ApplicationRunner {


    private final ResourceMetrics resourceMetrics;
    private final NettyClient nettyClient;

    @Value("${netty.collector-period}")
    private int period;
    @Autowired
    public ChecksCollector(ResourceMetrics resourceMetrics, NettyClient nettyClient) {
        this.resourceMetrics = resourceMetrics;
        this.nettyClient = nettyClient;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                // Get the system health data
                double memoryUsage = resourceMetrics.getMemoryUsage();
                double cpuUtilization = resourceMetrics.getCpuUtilization();
                String healthStatus = resourceMetrics.getHealthStatus();

                // TODO the data is send as comma separated values to the controller
                // This should be changed to a JSON object or other decoding format
                nettyClient.sendMessage(memoryUsage + "," + cpuUtilization + "," + healthStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }
}
