package com.bgpark.quadkey.domain.place;

import java.util.List;

public interface PlaceDocumentQuery {

    List<PlaceDocument> findBySearch(PlaceObj.Search search);
}
