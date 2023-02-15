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
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
                .contestants(new ArrayList<>())
                .createdDate(LocalDateTime.now())
                .status(ClashEnum.OPEN.name())
                .build();

        return clashMapper.toDTO(clashRepository.save(clashMapper.updateClashFromDTO(clashDTO, save)));
    }

    @Override
    public ClashDTO getClash(String clashId) {
        if (clashId == null || clashId.isBlank()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
        Clash clash = clashRepository.findById(clashId).orElse(null);
        if (clash == null) {
            throw new ObjectNotFoundException(clashId, "clash");
        }
        ClashDTO res = clashMapper.toDTO(clash);

        Optional<Profile> profile = profileRepository.findById(clash.getOwnerId());

        res.setOwnerName(profile.isPresent() ? profile.get().getUsername() : "Unknowed user");
        return res;
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
//            clash.setArtists((String[]) contestants.stream().map(Contestant::getProfileId()).toArray());
            contestants.forEach(contestant -> {
                contestant.setCurrentRound(1);
                contestantRepository.save(contestant);
            });
            clash.setCurrentRound(1);
            return clashMapper.toDTO(clashRepository.save(clash));
        } else {
            throw new IllegalArgumentException("CAN'T START FINISHED CLASH");
        }
    }

    @Override
    public boolean uploadMedia(String loggedUsername, String clashId, MultipartFile file, String mediaDescription) throws IOException {

        Clash clash = clashCheckHandler(clashId);

        Profile profile = profileRepository.findByUsername(loggedUsername).get();
        Optional<Contestant> optionalContestant = contestantRepository.findByClashIdAndProfileId(clashId, profile.getId());
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
    public ClashDTO join(String username, String clashid, String userId) {
        Clash clash = clashCheckHandler(clashid);

        Profile profile = userId == null || userId.isEmpty() ? profileRepository.findByUsername(username).get() : profileRepository.findById(userId).get();

        Optional<Contestant> optionalContestant = contestantRepository.findByClashIdAndProfileId(clashid, profile.getId());

        if (optionalContestant.isPresent()) {
            throw new IllegalArgumentException("DEJA REJOINT");
        }

        List<Contestant> contestants =  new ArrayList<>(clash.getContestants());

        if (contestants.size() >= clash.getSlot()) {
            throw new IllegalArgumentException("CLASH COMPLET");
        }

        if (!clash.getStatus().equals(ClashEnum.OPEN.name())) {
            throw new IllegalArgumentException("PAS OUVERT");
        }

        contestants.add(contestantRepository.save(Contestant.builder()
                .clashId(clashid)
                .profileId(profile.getId())
                .mediaId("")
                .score((int) (Math.random() * 100))
                .build()));

        clash.setContestants(contestants);
        return clashMapper.toDTO(clashRepository.save(clash));
    }

    @Override
    public boolean exit(String username, String clashid) {
        Clash clash = clashCheckHandler(clashid);
        Profile profile = profileRepository.findByUsername(username).get();

        Optional<Contestant> optionalContestant = contestantRepository.findByClashIdAndProfileId(clashid, profile.getId());

        if (optionalContestant.isEmpty()) {
            throw new IllegalArgumentException("pas dedans");
        }
        Contestant contestant = optionalContestant.get();

        contestantRepository.deleteById(contestant.getId());

        List<Contestant> contestants = new ArrayList<>(clash.getContestants());

        contestants.removeIf(c -> c.getProfileId().equals(profile.getId()));

        clash.setContestants(contestants);

        clashRepository.save(clash);



        return true;
    }


    @Override
    public ClashDTO nextRound(String clashId) {
        Clash clash = clashCheckHandler(clashId);
        if (clash.getStatus().equals(ClashEnum.FINISHED.name())) {
            throw new IllegalArgumentException("THIS CLASH IS FINISH");
        }
        int currentRound = clash.getCurrentRound();
        int totalRound = clash.getRound();
        List<Contestant> currentContestants = new ArrayList<>(clash.getContestants());
        int nbContestans = currentContestants.size();
        int skip = nbContestans > 6 ? 2 : 1;

        int nextRound = currentRound + 1;

        currentContestants.sort(Comparator.comparingInt(Contestant::getScore));

        if (currentRound == totalRound || nbContestans == 3) {

            currentContestants.forEach(contestant -> {
                contestant.setHasWin(true);
                contestant.setCurrentRound(currentRound);
                contestant.setGlobalScore(contestant.getGlobalScore() + contestant.getScore());
                contestantRepository.save(contestant);
            });

            clash.setStatus(ClashEnum.FINISHED.name());
            clash.setCurrentRound(totalRound);
            clash.setFirst(currentContestants.get(nbContestans - 1).getProfileId());
            clash.setSecond(currentContestants.get(nbContestans - 2).getProfileId());
            clash.setThird(currentContestants.get(nbContestans - 3).getProfileId());
            clashRepository.save(clash);


        } else {

            List<Contestant> losers = currentContestants.subList(0, skip);
            losers.forEach(contestant -> {
                contestant.setCurrentRound(currentRound);
                contestantRepository.save(contestant);
            });
            List<Contestant> winners = currentContestants.stream().skip(skip)
                    .map(battle -> {
                        battle.setHasWin(true);
                        battle.setCurrentRound(currentRound);
                        contestantRepository.save(battle);
                        return contestantRepository.save(Contestant.builder()
                                .globalScore(battle.getGlobalScore() + battle.getScore())
                                .score((int) (Math.random() * 100))
                                .profileId(battle.getProfileId())
                                .clashId(battle.getClashId())
                                .mediaId("")
                                .mediaDescription("")
                                .currentRound(nextRound)
                                .build());
                    }).collect(Collectors.toList());

            clash.setContestants(winners);
            clash.setCurrentRound(nextRound);
        }
        return clashMapper.toDTO(clashRepository.save(clash));
     }
}
