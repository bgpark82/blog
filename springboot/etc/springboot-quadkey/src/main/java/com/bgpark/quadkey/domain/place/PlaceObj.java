package com.bgpark.quadkey.domain.place;

import lombok.Data;

public class PlaceObj {

    @Data
    public static class Search {
        private String id;
        private String quadkey;
        private Double lat;
        private Double lon;
        private Double kilometer;

        public Search(String quadkey, Double lat, Double lon, Double kilometer) {
            this.quadkey = quadkey;
            this.lat = lat;
            this.lon = lon;
            this.kilometer = kilometer;
        }
    }
}
