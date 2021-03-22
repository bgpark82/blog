package com.bgpark.quadkey.domain.place.document;

import com.bgpark.quadkey.domain.place.Category;
import com.bgpark.quadkey.domain.place.LatLon;
import com.bgpark.quadkey.domain.util.GeoUtils;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.util.Date;
import java.util.Set;


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

    private Double rating;

    private String name;
    private String url;
    private String original_name;
    private String name_suffix;
    private String marker;
    private String thumbnail_url;
    private Set<Category> categories;
    private Date createdAt;
    private Date modifiedAt;
    private String address;
    private String img_url;

    public PlaceDocument(String id, String quadkey) {
        this.id = id;
        this.quadkey = quadkey;
    }

    public PlaceDocument distanceTo(double lat, double lon) {
        distance = GeoUtils.distance(location, new LatLon(lat, lon));
        return this;
    }
}
