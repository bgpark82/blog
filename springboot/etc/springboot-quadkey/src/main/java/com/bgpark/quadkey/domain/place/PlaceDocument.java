package com.bgpark.quadkey.domain.place;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "travel_v6", type = "places")
@ToString
public class PlaceDocument {

    @Id
    private String id;

    private String quadkey;
}
