package com.example.demo.service;

import com.example.demo.model.data.Contestant;
import com.example.demo.model.data.Clash;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.ClashDTO;
import com.example.demo.repository.ContestantRepository;
import com.example.demo.repository.ClashRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.utils.ClashEnum;
import com.example.demo.utils.ContestantEnum;
import com.example.demo.utils.mapper.ClashMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClashServiceImpl implements ClashService {

    @Autowired
    private ClashRepository clashRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ContestantRepository contestantRepository;
    @Autowired
    private ClashMapper clashMapper;
    @Autowired
    private MediaService mediaService;
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
//                .contestants(new ArrayList<>())
                .createdDate(LocalDateTime.now())
                .status(ClashEnum.OPEN.name())
                .restricted(true)
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
    public Page<Clash> listAllFilteredClash(String ownerId, boolean restricted, Pageable pageable) {
        String id = ownerId.isEmpty() ? null : ownerId;
        return id == null ? clashRepository.findAllByRestricted(restricted, pageable) : clashRepository.findAllByOwnerIdAndRestricted(id, restricted, pageable);
    }

    @Override
    public ClashDTO finishClash(String clashId) {
        return null;
    }

    @Override
    public boolean close(String clashId) {
        return false;
    }

    private Clash clashCheckHandler(String clashId) {
        if (clashId == null || clashId.length() == 0) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }

        Optional<Clash> optionalClash = clashRepository.findById(clashId);

        if (optionalClash.isEmpty()) {
            throw new IllegalArgumentException("Clash doesn't exist");
        }
        return optionalClash.get();
    }

    @Override
    public ClashDTO start(String clashId) {
        Clash clash = clashCheckHandler(clashId);
        if (clash.getStatus().equals(ClashEnum.OPEN.name())) {
            clash.setStatus(ClashEnum.ONGOING.name());
            List<Contestant> contestants = contestantRepository.findAllByClashId(clashId);
//            clash.setArtists((String[]) contestants.stream().map(Contestant::getContestantId).toArray());
            contestants.forEach(contestant -> {
                contestant.setCurrentRound(1);
                contestantRepository.save(contestant);
            });
            clash.setCurrentRound(1);
            return clashMapper.toDTO(clashRepository.save(clash));
        }else {
            throw new IllegalArgumentException("CAN'T START FINISHED CLASH");
        }
    }

    @Override
    public boolean uploadMedia(String loggedUsername, String clashId, MultipartFile file, String mediaDescription) throws IOException {

        Clash clash = clashCheckHandler(clashId);

        Profile profile = profileRepository.findByUsername(loggedUsername).get();
        Optional<Contestant> optionalContestant = contestantRepository.findByClashIdAndContestantId(clashId, profile.getId());
        if (optionalContestant.isEmpty()) {
            throw new IllegalArgumentException("");
        }
        Contestant contestant = optionalContestant.get();
        String mediaId = mediaService.uploadImageToDB(file, loggedUsername).getPayload().toString();

        contestant.setMediaId(mediaId);
        contestant.setMediaDescription(mediaDescription);
        contestant.setStatus(ContestantEnum.SUBMITTED.name());

        contestantRepository.save(contestant);

        return true;
    }

    @Override
    public boolean join(String username, String clashid, String userId) {
//        Clash clash = clashCheckHandler(clashid);
//
//        Profile profile = userId == null || userId.isEmpty() ? profileRepository.findByUsername(username).get() : profileRepository.findById(userId).get();
//
//        Optional<Contestant> optionalContestant = contestantRepository.findByClashIdAndContestantId(clashid, profile.getId());
//
//        if (optionalContestant.isPresent()) {
//            throw new IllegalArgumentException("DEJA REJOINT");
//        }
//
//        List<String> contestants = clash.getContestants();
//
//        if (contestants == null) {
//            contestants = new ArrayList<>();
//        }
//
//        if (contestants.size() >= clash.getSlot()) {
//            throw new IllegalArgumentException("CLASH COMPLET");
//        }
//
//        if (!clash.getStatus().equals(ClashEnum.OPEN.name())) {
//            throw new IllegalArgumentException("PAS OUVERT");
//        }
//
//        contestants.add(contestantRepository.save(Contestant.builder()
//                .clashId(clashid)
//                .contestantId(profile.getId())
//                .mediaId("")
//                .score((int) (Math.random() * 100))
//                .build()).getId());
//
//        clash.setContestants(contestants);
//        clashRepository.save(clash);
        return true;
    }

    @Override
    public boolean exit(String username, String clashid) {
//        Clash clash = clashCheckHandler(clashid);
//        Profile profile = profileRepository.findByUsername(username).get();
//
//        Optional<Contestant> optionalContestant = contestantRepository.findByClashIdAndContestantId(clashid, profile.getId());
//
//        if (optionalContestant.isEmpty()) {
//            throw new IllegalArgumentException("pas dedans");
//        }
//
//        List<String> contestants = clash.getContestants();
//
//        contestants.remove(optionalContestant.get().getId());
//
//        contestantRepository.deleteById(optionalContestant.get().getId());
//
//        clash.setContestants(contestants);
//
//        clashRepository.save(clash);
        return true;
    }


    @Override
    public ClashDTO nextRound(String clashId) {
//        Clash clash = clashCheckHandler(clashId);
//        if (clash.getStatus().equals(ClashEnum.FINISHED.name())) {
//            throw new IllegalArgumentException("THIS CLASH IS FINISH");
//        }
//        int currentRound = clash.getCurrentRound();
//        int totalRound = clash.getRound();
//        List<String> contestantsIds = clash.getContestants();
//        List<Contestant> currentContestants = contestantsIds.stream()
//                .map(id -> contestantRepository.findById(id).orElse(null))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//        int nbContestans = currentContestants.size();
//        int skip = nbContestans > 6 ? 2 : 1;
//
//        int nextRound = currentRound + 1;
//
//        currentContestants.sort(Comparator.comparingInt(Contestant::getScore));
//
//        if (currentRound == totalRound || nbContestans == 3) {
//
//            Podium podium = Podium.builder()
//                    .first(currentContestants.get(nbContestans - 1).getContestantId())
//                    .second(currentContestants.get(nbContestans - 2).getContestantId())
//                    .third(currentContestants.get(nbContestans - 3).getContestantId())
//                    .build();
//
//            currentContestants.forEach(contestant -> {
//                contestant.setHasWin(true);
//                contestant.setCurrentRound(currentRound);
//                contestant.setGlobalScore(contestant.getGlobalScore() + contestant.getScore());
//                contestantRepository.save(contestant);
//            });
//
//            clash.setStatus(ClashEnum.FINISHED.name());
//            clash.setCurrentRound(totalRound);
//            clash.setPodium(podium);
//            clashRepository.save(clash);
//
//
//        } else {
//
//            List<Contestant> losers = currentContestants.subList(0, skip);
//            losers.forEach(contestant -> {
//                contestant.setCurrentRound(currentRound);
//                contestantRepository.save(contestant);
//            });
//            List<String> winners = currentContestants.stream().skip(skip)
//                    .map(battle -> {
//                        battle.setHasWin(true);
//                        battle.setCurrentRound(currentRound);
//                        contestantRepository.save(battle);
//                        return contestantRepository.save(Contestant.builder()
//                                .globalScore(battle.getGlobalScore() + battle.getScore())
//                                .score((int) (Math.random() * 100))
//                                .contestantId(battle.getContestantId())
//                                .clashId(battle.getClashId())
//                                .mediaId("")
//                                .mediaDescription("")
//                                .currentRound(nextRound)
//                                .build()).getId();
//                    }).collect(Collectors.toList());
//
//            clash.setContestants(winners);
//            clash.setCurrentRound(nextRound);
//        }
//        return clashMapper.toDTO(clashRepository.save(clash));
        return null;
    }
}
