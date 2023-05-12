package com.msa.searchservice.repository;

import com.msa.searchservice.model.Product;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface SearchRepository extends MongoRepository<Product, String> {
//    @Query("[\n" +
//            "  {\n" +
//            "    $search: {\n" +
//            "      index: \"search_index\",\n" +
//            "      text: {\n" +
//            "        query: ?0,\n" +
//            "        path: {\n" +
//            "          wildcard: \"*\"\n" +
//            "        },\n" +
//            "        fuzzy: {\n" +
//            "          maxEdits: 2\n" +
//            "        }\n" +
//            "      }\n" +
//            "    }\n" +
//            "  }\n" +
//            "]")
    @Aggregation(pipeline = {"\n" +
            "  {\n" +
            "    $search: {\n" +
            "      index: \"search_index\",\n" +
            "      text: {\n" +
            "        query: ?0,\n" +
            "        path: {\n" +
            "          wildcard: \"*\"\n" +
            "        },\n" +
            "        fuzzy: {\n" +
            "          maxEdits: 2\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" })
    List<Product> searchProduct(String text);
}
