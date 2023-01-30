package com.example.demo.repository;

import com.example.demo.model.data.Podium;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PodiumRepository extends MongoRepository<Podium,String> {
 }
