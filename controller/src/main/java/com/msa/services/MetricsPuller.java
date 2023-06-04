package com.msa.services;

import com.msa.models.Query;
import com.msa.models.RunningInstance;
import com.msa.models.responses.MetricResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;


@Component
public class MetricsPuller {

    private final WebClient webClient;
    private final Hashtable<String, Query> queries;

    @Value("${prometheus.host}")
    private String prometheusHost;

    @Value("${prometheus.port}")
    private String prometheusPort;

    @Value("${health-checker.time-window}")
    private String period;

    @Autowired
    public MetricsPuller(ResourceLoader resourceLoader) {
        WebClient.Builder builder = WebClient.builder();
        builder.uriBuilderFactory(customUriBuilderFactory());
        webClient = builder.build();

        queries = new Hashtable<>();

        // pull the metrics from the metric.txt file
        try {
            Resource resource = resourceLoader.getResource("classpath:metrics.txt");
            InputStream inputStream = resource.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Process each query
                String[] lineSplit = line.split("#");
                int[] paramIndexSplit = Arrays.stream(lineSplit[2].split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();

                ArrayList<Integer> parameterIndexes = new ArrayList<>();
                for (int i : paramIndexSplit) parameterIndexes.add(i);

                queries.put(lineSplit[0], new Query(lineSplit[1], parameterIndexes));
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public Hashtable<String, String> pullInstanceMetrics(RunningInstance runningInstance) throws UnsupportedEncodingException {

        Hashtable<String, String> results = new Hashtable<>();

        String[] params = new String[4];
        params[0] = runningInstance.getServiceType().getDirectory();
        params[1] = runningInstance.getHost().getIp();
        params[2] = runningInstance.getPort();
        params[3] = period;

        for (Map.Entry<String, Query> query : queries.entrySet()) {

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host(prometheusHost)
                    .port(prometheusPort)
                    .path("/api/v1/query")
                    .queryParam("query", URLEncoder.encode(
                            convertQueryToString(query.getValue().getQuery(), query.getValue().getParams(), params),
                            "UTF-8")
                    );

            String uriString = uriBuilder.build().toUriString();
            MetricResponse metricResponse = webClient.get()
                    .uri(uriString)
                    .retrieve()
                    .bodyToMono(MetricResponse.class)
                    .block();
            System.out.println(metricResponse);

            if (metricResponse != null &&
                    metricResponse.getData() != null &&
                    metricResponse.getData().getResult() != null &&
                    metricResponse.getData().getResult().size() > 0
            ) {
                results.put(
                        query.getKey(),
                        metricResponse.getData().getResult().get(0).getValue().get(1).toString()
                );
            }
        }

        return results;
    }

    public void reloadYaml() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(prometheusHost)
                .port(prometheusPort)
                .path("/-/reload");

        String uriString = uriBuilder.build().toUriString();
        webClient.post().uri(uriString).retrieve().bodyToMono(String.class).block();

    }

    private String convertQueryToString(String placeholder, List<Integer> paramIndexes, String[] params) {
        String[] selectedParams = new String[paramIndexes.size()];

        for (int i = 0; i < selectedParams.length; i++) {
            selectedParams[i] = params[paramIndexes.get(i) - 1];
        }
        return String.format(placeholder, (Object[]) selectedParams);
    }

    private UriBuilderFactory customUriBuilderFactory() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return factory;
    }
}
