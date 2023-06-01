package com.msa.searchservice.repository;

import com.msa.searchservice.model.Product;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface SearchRepository extends MongoRepository<Product, String> {
    @Aggregation(pipeline = {
            "{$search: {index: \"search_index\", text:  {query:  ?0, path:  {wildcard:  \"*\"}, fuzzy:  {maxEdits:  2}}}}",
            "{$match: {\"price\":  {\"$gte\":  ?1, \"$lte\":  ?2}}}"
    })
    List<Product> searchProduct(String text, double minPrice, double maxPrice);
}
