package com.bgpark.quadkey.domain.place.document;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PlaceDocumentRepository extends ElasticsearchRepository<PlaceDocument, String>, PlaceDocumentQuery  {

}
