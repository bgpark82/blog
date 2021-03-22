package com.bgpark.quadkey.domain.place.document;

import java.util.List;

public interface PlaceDocumentQuery {

    List<PlaceDocument> findBySearch(PlaceObj.Search search);
}
