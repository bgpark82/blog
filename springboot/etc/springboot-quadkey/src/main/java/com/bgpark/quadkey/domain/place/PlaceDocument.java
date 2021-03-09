package com.bgpark.quadkey.domain.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Builder
@Document(indexName = "travel", type = "places")
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDocument {

    @Id
    private String id;

    private String quadkey;
}
