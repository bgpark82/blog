package com.bgpark.quadkey.domain.place;

import lombok.Data;
import lombok.NoArgsConstructor;

public class PlaceDto {

    @Data
    public static class FindReq {

        private String quadkey;
    }

    @Data
    @NoArgsConstructor
    public static class Req {
        private String id;
        private String quadkey;

        public PlaceDocument toEntity() {
            return new PlaceDocument(id, quadkey);
        }
    }
}
