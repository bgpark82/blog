package com.bgpark.quadkey.domain.place;

import com.bgpark.quadkey.domain.util.GeoUtils;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;


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

    private double lat;

    private double lon;

    private double distance;

    @GeoPointField
    private LatLon location;

    public PlaceDocument distanceTo(double lat, double lon) {
        distance = GeoUtils.distance(location, new LatLon(lat, lon));
        return this;
    }
}
