package com.bgpark.quadkey.domain.place;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PlaceObj {

    @Data
    public static class Search {
        private String id;
        private String quadkey;
        private Double lat;
        private Double lon;
        private Double kilometer;

        private Integer page;
        private Integer size;

        public Search(String quadkey, Double lat, Double lon, Double kilometer, Integer page, Integer size) {
            this.quadkey = quadkey;
            this.lat = lat;
            this.lon = lon;
            this.kilometer = kilometer;
            this.page = page;
            this.size = size;
        }

        public Pageable getPagable() {
            return PageRequest.of(page, size);
        }
    }
}
