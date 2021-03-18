package com.bgpark.quadkey.domain.place;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PlaceDocumentRepository extends ElasticsearchRepository<PlaceDocument, String>, PlaceDocumentQuery  {

}
