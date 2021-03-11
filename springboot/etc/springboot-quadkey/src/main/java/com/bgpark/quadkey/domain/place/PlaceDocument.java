package com.bgpark.quadkey.domain.place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "travel", type = "places")
public class PlaceDocument {

    @Id
    private String id;

    private String quadkey;
}
