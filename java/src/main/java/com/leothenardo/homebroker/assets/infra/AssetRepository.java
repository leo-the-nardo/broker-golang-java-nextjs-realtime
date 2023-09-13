package com.leothenardo.homebroker.assets.infra;


import com.leothenardo.homebroker.assets.model.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssetRepository extends MongoRepository<Asset, String> {
}
