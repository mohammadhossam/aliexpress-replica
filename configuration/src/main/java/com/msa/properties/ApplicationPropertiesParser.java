package com.msa.properties;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationPropertiesParser {

    private String properties;
    /**
     * input example:
     *          spring.datasource.url=jdbc:{users-service-db.db}://{users-service-db.host}:{users-service-db.port}/aliexpress
     *          spring.datasource.username=postgres
     *          spring.datasource.password=postgres
     */


    public ArrayList<Property> parse() throws ParseException {
        ArrayList<Property> properties = new ArrayList<>();
        String[] lines = this.properties.split("\n");
        for (String prop : lines) {
            if (prop.isEmpty())
                continue;
            String [] propParts = prop.split("=");
            if (propParts.length < 2) {
                throw new ParseException("Invalid property: " + prop, 0);
            }
            String propertyName = propParts[0];
            StringBuilder val = new StringBuilder();
            for (int i = 1; i < propParts.length; i++) {
                val.append(propParts[i]);
            }

            HashMap<String, String> placeHolders = new HashMap<>();
            Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher matcher = pattern.matcher(val.toString());

            while (matcher.find()) {
                String match = matcher.group(1);
                System.out.println(match);
                String[] matchParts = match.split("\\.");
                System.out.println(Arrays.toString(matchParts));
                if (matchParts.length != 2) {
                    throw new ParseException("Invalid placeholder: " + match, 0);
                }
                String placeHolderName = matchParts[0];
                String placeHolderProperty = matchParts[1];
                placeHolders.put(placeHolderName, placeHolderProperty);
            }
            properties.add(new Property(propertyName, val.toString(), placeHolders));
        }
        System.out.println(properties);
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

}
