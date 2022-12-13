package com.example.demo.service;

import com.example.demo.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationshipServiceImpl implements RelationshipService{
    @Autowired
    private RelationshipRepository relationshipRepository;


}
