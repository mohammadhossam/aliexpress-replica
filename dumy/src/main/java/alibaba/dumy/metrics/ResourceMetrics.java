//package alibaba.dumy.metrics;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.health.HealthEndpoint;
//import org.springframework.boot.actuate.metrics.MetricsEndpoint;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ResourceMetrics {
//
//    private final MetricsEndpoint metricsEndpoint;
//    private final HealthEndpoint healthEndpoint;
//
//    @Autowired
//    public ResourceMetrics(MetricsEndpoint metricsEndpoint, HealthEndpoint healthEndpoint) {
//        this.metricsEndpoint = metricsEndpoint;
//        this.healthEndpoint = healthEndpoint;
//    }
//
//    public double getMemoryUsage() {
//        return metricsEndpoint.metric("jvm.memory.used", null).getMeasurements().get(0).getValue();
//    }
//
//    public double getCpuUtilization() {
//        return metricsEndpoint.metric("process.cpu.usage", null).getMeasurements().get(0).getValue();
//    }
//
//    public String getHealthStatus() {
//        return healthEndpoint.health().getStatus().toString();
//    }
//}
//
