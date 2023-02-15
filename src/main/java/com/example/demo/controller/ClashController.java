package com.example.demo.controller;

import com.example.demo.model.data.Clash;
import com.example.demo.model.data.Profile;
import com.example.demo.model.dto.ClashDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.ClashService;
import com.example.demo.utils.AuthUtils;
import com.example.demo.utils.ClashEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/clash")
public class ClashController {
    @Autowired
    private ClashService clashService;

    @GetMapping("get/{id}")
    private ResponseEntity<?> getClash(@PathVariable String id) {
        ClashDTO clash = clashService.getClash(id);
        if (clash != null)
            return ResponseEntity.status(HttpStatus.OK).body(clash);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("create")
    private ResponseEntity<?> createClash(@RequestBody ClashDTO clashDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(clashService.createClash(username, clashDTO));
    }

    @PatchMapping("update")
    private ResponseEntity<?> updateClash(@RequestBody ClashDTO requestDTO) {

        ClashDTO clashDTO = clashService.updateClash(AuthUtils.getLoggedUsername(), requestDTO);

        if (clashDTO != null)
            return ResponseEntity.status(HttpStatus.OK).body(clashDTO);

        MessageResponse messageResponse = MessageResponse.builder().hasError(true).message("Given clash is not in db").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponse);
    }

    @GetMapping("list")
    private ResponseEntity<?> listClash(@RequestParam(required = false, defaultValue = "") String ownerId,
                                        @RequestParam(required = false, defaultValue = "false") boolean restricted,
                                        @RequestParam(required = false, defaultValue = "9") int size,
                                        @RequestParam(required = false, defaultValue = "0") int page) {
        Page<Clash> clashes = clashService.listAllFilteredClash(ownerId, restricted, PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(clashes);
    }

    @GetMapping("delete/{id}")
    private ResponseEntity<?> deleteClash(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.deleteClash(id) ? "Clash with id : " + id + " deleted." : "Could not delete clash. (doesn't exist)");
    }

    @PostMapping("join/{clashId}")
    private ResponseEntity<?> join(@PathVariable String clashId, @RequestParam(required = false) String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.join(AuthUtils.getLoggedUsername(), clashId, userId));
    }

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping("dummy/join/{clashId}")
    private ResponseEntity<?> test(@PathVariable String clashId) {
        String loggedUsername = AuthUtils.getLoggedUsername();
        Profile profile = profileRepository.findByUsername(loggedUsername).get();
        String loggedId = profile.getId();
        String[] ids = {loggedId,
                "63dda6093c57167920880428",
                "63dda60e3c57167920880429",
                "63dda6193c5716792088042a",
                "63dda6273c5716792088042b",
                "63dda5f13c57167920880426"};
        for (String id : ids) {
            Profile save = profileRepository.save(Profile.builder().username(id.replace("-", "").substring(0, ids.length)).password(new BCryptPasswordEncoder().encode(id)).email("email@yopmail.com").build());
            clashService.join(loggedUsername, clashId, save.getId());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(true);
    }

    @PostMapping("exit/{clashId}")
    private ResponseEntity<?> exit(@PathVariable String clashId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.exit(AuthUtils.getLoggedUsername(), clashId));
    }

    @PostMapping("close/{clashId}")
    private ResponseEntity<?> close(@PathVariable String clashId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.close(clashId));
    }

    @PostMapping("finish/{clashId}")
    private ResponseEntity<?> finish(@PathVariable String clashId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.finishClash(clashId));
    }

    @PostMapping("start/{clashId}")
    private ResponseEntity<?> start(@PathVariable String clashId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.start(clashId));
    }

    @PostMapping("next/{clashId}")
    private ResponseEntity<?> nextRound(@PathVariable String clashId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.nextRound(clashId));
    }

    @PostMapping("uploadMedia/{clashId}")
    private ResponseEntity<?> nextRound(@PathVariable String clashId, @RequestBody MultipartFile file, @RequestParam String mediaDescription) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.uploadMedia(AuthUtils.getLoggedUsername(), clashId, file, mediaDescription));
    }

}
