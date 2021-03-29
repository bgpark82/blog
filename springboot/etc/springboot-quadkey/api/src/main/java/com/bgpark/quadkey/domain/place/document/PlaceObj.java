package com.bgpark.quadkey.domain.place.document;

import com.bgpark.quadkey.domain.place.Category;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static com.bgpark.quadkey.domain.place.document.PlaceObj.Search.SortOrder.*;
import static org.elasticsearch.search.sort.SortOrder.ASC;
import static org.elasticsearch.search.sort.SortOrder.DESC;

public class PlaceObj {

    @Data
    @NoArgsConstructor
    public static class Search {
        private String id;
        private String quadkey;
        private Double lat;
        private Double lon;
        private Double lat2;
        private Double lon2;
        private Double kilometer;

        private Integer minRate = 0;
        private Integer maxRate = 10;
        private Set<Category> categories;
        private Integer page;
        private Integer size;
        private SortOrder sortOrder = RATING;

        public Search(String quadkey, Double lat, Double lon, Double kilometer, Integer page, Integer size) {
            this.quadkey = quadkey;
            this.lat = lat;
            this.lon = lon;
            this.kilometer = kilometer;
            this.page = page;
            this.size = size;
        }

        @Builder
        public Search(String id, String quadkey, Double lat, Double lon, Double lat2, Double lon2, Double kilometer, Integer minRate, Integer maxRate, Set<Category> categories, Integer page, Integer size, SortOrder sortOrder) {
            this.id = id;
            this.quadkey = quadkey;
            this.lat = lat;
            this.lon = lon;
            this.lat2 = lat2;
            this.lon2 = lon2;
            this.kilometer = kilometer;
            this.minRate = minRate;
            this.maxRate = maxRate;
            this.categories = categories;
            this.page = page;
            this.size = size;
            this.sortOrder = sortOrder;
        }

        public Pageable getPagable() {
            return PageRequest.of(page, size);
        }

        public SortBuilder getSort() {

            if(sortOrder.equals(RATING)) {
                return SortBuilders
                        .fieldSort("rating")
                        .order(DESC);
            } else if (sortOrder.equals(LOCATION)) {
                return SortBuilders
                        .geoDistanceSort("location",lat, lon)
                        .order(ASC);
            } else if (sortOrder.equals(ROUTE)) {
                return SortBuilders
                        .geoDistanceSort("location",
                                new GeoPoint(lat, lon),
                                new GeoPoint(lat2, lon2))
                        .order(ASC);
            }
            return SortBuilders
                    .fieldSort("rating")
                    .order(DESC);
        }

        public enum SortOrder {
            RATING, LOCATION, ROUTE;
        }
    }


}
