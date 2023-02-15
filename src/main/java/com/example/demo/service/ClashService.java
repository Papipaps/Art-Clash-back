package com.example.demo.service;

import com.example.demo.model.data.Clash;
import com.example.demo.model.dto.ClashDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ClashService {

    ClashDTO createClash(String username, ClashDTO clashDTO);
    ClashDTO getClash(String id);
    Page<Clash> listClash(String id, Pageable pageable);
    ClashDTO updateClash(String username, ClashDTO clashDTO);
    boolean deleteClash(String id);


    Page<Clash> listAllFilteredClash(String ownerId,boolean restricted ,Pageable pageable);

    ClashDTO finishClash(String clashId);

    ClashDTO nextRound(String clashId);

    ClashDTO join(String loggedUsername, String clashId, String userId);

    boolean exit(String loggedUsername, String clashId);

    boolean close(String clashId);

    ClashDTO start(String clashId);

    boolean uploadMedia(String loggedUsername, String clashId, MultipartFile file, String mediaDescription) throws IOException;
}
