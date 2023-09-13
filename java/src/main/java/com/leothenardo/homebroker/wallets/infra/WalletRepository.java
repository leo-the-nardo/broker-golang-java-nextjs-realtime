package com.leothenardo.homebroker.wallets.infra;


import com.leothenardo.homebroker.wallets.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletRepository extends MongoRepository<Wallet, String> {
}
