package com.leothenardo.homebroker.wallets.repositories;


import com.leothenardo.homebroker.wallets.entities.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletRepository extends MongoRepository<Wallet, String> {
}
