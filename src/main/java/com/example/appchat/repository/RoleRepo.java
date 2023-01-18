package com.example.appchat.repository;

import com.example.appchat.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends MongoRepository<RoleEntity, Long> {
    RoleEntity findByName(String role);
}
