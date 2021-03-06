package com.bgpark.quadkey.domain.place;

import lombok.Data;

public class PlaceDto {

    @Data
    public static class FindReq {

        private String quadkey;
    }
}
