package ar.edu.unq.epersgeist.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.stereotype.Service;

@Service
public class IndexadoDominar {
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void indexCreator() {
        mongoTemplate.indexOps("coordenadasIndex")
                .ensureIndex(new GeospatialIndex("punto").typed(GeoSpatialIndexType.GEO_2DSPHERE));

    }
}