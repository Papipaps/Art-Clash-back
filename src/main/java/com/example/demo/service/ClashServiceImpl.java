package com.example.demo.service;

import com.example.demo.model.data.Battle;
import com.example.demo.model.data.Clash;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.ClashDTO;
import com.example.demo.repository.BattleRepository;
import com.example.demo.repository.ClashRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.utils.mapper.ClashMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClashServiceImpl implements ClashService {

    @Autowired
    private ClashRepository clashRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BattleRepository battleRepository;
    @Autowired
    private ClashMapper clashMapper;

    @Autowired
    private SocialService socialService;

    @Override
    public ClashDTO createClash(String username, ClashDTO clashDTO) {

        if (clashRepository.existsByTitle(clashDTO.getTitle())) {
            throw new IllegalArgumentException("Un clash avec ce nom existe déjà, changez de nom.");
        }

        Profile profile = profileRepository.findByUsername(username).get();
        Clash save = Clash.builder()
                .ownerId(profile.getId())
                .contestants(new ArrayList<>())
                .createdDate(LocalDateTime.now())
                .build();

        return clashMapper.toDTO(clashRepository.save(clashMapper.updateClashFromDTO(clashDTO, save)));
    }

    @Override
    public ClashDTO getClash(String clashId) {
        if (clashId == null || clashId.isBlank()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
        Clash clash = clashRepository.findById(clashId).orElse(null);
        if (clash != null) {
            Optional<Profile> profile = profileRepository.findById(clash.getOwnerId());
            if (profile.isPresent()) {
                ClashDTO res = clashMapper.toDTO(clash);
                res.setOwnerName(profile.get().getUsername());
                return res;
            }
        }
        return null;
    }

    @Override
    public Page<Clash> listClash(String id, Pageable pageable) {
        Page<Clash> res = clashRepository.findAllByOwnerId(id, pageable);

        return res;
    }

    @Override
    public ClashDTO updateClash(String username, ClashDTO clashDTO) {
        Optional<Clash> oldClashOpt = clashRepository.findById(clashDTO.getId());

        if (oldClashOpt.isPresent()) {
            Clash save = oldClashOpt.get();
            save = clashRepository.save(clashMapper.updateClashFromDTO(clashDTO, save));
            return clashMapper.toDTO(save);
        }

        return null;
    }

    @Override
    public boolean deleteClash(String id) {
        Optional<Clash> clashOpt = clashRepository.findById(id);
        boolean res = false;
        if (clashOpt.isPresent()) {
            clashRepository.delete(clashOpt.get());
            res = true;
        }
        return res;
    }

    @Override
    public Page<Clash> listAllFilteredClash(String ownerId, boolean finished, Pageable pageable) {
        String id = ownerId.isEmpty() ? null : ownerId;
        return id == null ? clashRepository.findAllByIsFinished(finished, pageable) : clashRepository.findAllByOwnerIdAndIsFinished(id, finished, pageable);
    }

    @Override
    public ClashDTO finishClash(String clashId) {
        return null;
    }


    public void updateResult(Battle node, String winningContestant) {
        if (node == null) {
            return;
        }
        if (winningContestant.equals(node.getContestantA())) {
            updateResult(node.getLeft(), winningContestant);
        } else {
            updateResult(node.getRight(), winningContestant);
        }
    }

    public void saveTournamentToDB(Battle node) {
        // Save node to MongoDB
    }

}
