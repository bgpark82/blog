package com.bgpark.quadkey.domain.place;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceDocumentRepository extends ElasticsearchRepository<PlaceDocument, String> {
}
