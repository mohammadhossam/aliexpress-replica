package com.msa.services;

import com.msa.models.Property;
import com.msa.models.RunningResource;
import com.msa.models.ServiceConfiguration;
import com.msa.repos.RunningResourcesRepo;
import com.msa.repos.ServiceConfigurationRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ServiceConfigManager {
    private final ServiceConfigurationRepo serviceConfigurationRepo;
    private final RunningResourcesRepo runningResourcesRepo;

    private static final String externalResourcePattern = "\\{([^}]+)\\}"; // "{users-service-cache.port}

    public ServiceConfiguration getServiceConfiguration(String serviceName) {
        ServiceConfiguration serviceConfiguration = serviceConfigurationRepo.findByServiceName(serviceName);
        ArrayList<Property> newProperties = new ArrayList<>();
        ArrayList<Property> applicationProperties = serviceConfiguration.getApplicationProperties();
        for (Property property : applicationProperties) {
            String val = property.getVal();
            if (isValNeedsExternalResource(val)) {
                String newVal = addResourcesToPlaceholders(val);
                newProperties.add(new Property(property.getPropertyName(), newVal));
            } else {
                newProperties.add(new Property(property.getPropertyName(), val));
            }
        }
        return new ServiceConfiguration(serviceName, newProperties);
    }


    private boolean isValNeedsExternalResource(String val) {
        Pattern pattern = Pattern.compile(externalResourcePattern);
        Matcher matcher = pattern.matcher(val);
        return matcher.find();
    }

    private HashMap<String, String> queryResourcesAttributes(ArrayList<String> neededResources) {
        HashMap<String, String> resourcesAttributes = new HashMap<>();
        for (String resource : neededResources) {
            String[] resourceTokens = resource.split("\\.");
            String resourceName = resourceTokens[0];
            String resourceProperty = resourceTokens[1];
            RunningResource runningResource = runningResourcesRepo.findByResourceName(resourceName);
            if (runningResource == null) {
                throw new RuntimeException("Could not find resource: " + resourceName);
            }
            resourcesAttributes.put(resource, runningResource.getAttributes().get(resourceProperty));
        }
        return resourcesAttributes;
    }
    private String addResourcesToPlaceholders(String val) {
        HashMap<String, String> resourcesAttributes = queryResourcesAttributes(getAllNeededResources(val));
        return StringSubstitutor.replace(val, resourcesAttributes);
    }

    private ArrayList<String> getAllNeededResources(String val) {
        ArrayList<String> neededResources = new ArrayList<>();
        Pattern pattern = Pattern.compile(externalResourcePattern);
        Matcher matcher = pattern.matcher(val);
        while (matcher.find()) {
            neededResources.add(matcher.group(1));
        }
        return neededResources;
    }

}
