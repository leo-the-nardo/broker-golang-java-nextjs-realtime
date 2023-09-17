package com.leothenardo.homebroker.assets.infra;


import com.leothenardo.homebroker.assets.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface AssetRepository extends MongoRepository<Asset, String> {
	@Query("{ '_id' : { $nin : ?0 } }")
	Page<Asset> findAllNotIn(List<String> ids, Pageable pageable);

//	@Query("{ '_id' : { $in : ?0 } }")
//	Page<Asset> findAllIn(List<String> ids, Pageable pageable);

	List<Asset> findBySymbolIn(List<String> ids);

}
