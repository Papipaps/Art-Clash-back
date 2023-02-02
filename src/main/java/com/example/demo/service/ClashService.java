package com.example.demo.service;

import com.example.demo.model.data.Clash;
import com.example.demo.model.dto.ClashDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ClashService {

    ClashDTO createClash(String username, ClashDTO clashDTO);
    ClashDTO getClash(String id);
    Page<Clash> listClash(String id, Pageable pageable);
    ClashDTO updateClash(String username, ClashDTO clashDTO);
    boolean deleteClash(String id);


    Page<Clash> listAllFilteredClash(String ownerId,boolean finished ,Pageable pageable);

    ClashDTO finishClash(String clashId);

    ClashDTO nextRound(String clashId);
}
