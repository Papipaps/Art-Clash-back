package com.example.demo.controller;

import com.example.demo.model.data.Battle;
import com.example.demo.model.data.Clash;
import com.example.demo.model.dto.ClashDTO;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.service.ClashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ClashDTO clashDTO = clashService.updateClash(username, requestDTO);

        if (clashDTO != null)
            return ResponseEntity.status(HttpStatus.OK).body(clashDTO);

        MessageResponse messageResponse = MessageResponse.builder().hasError(true).message("Given clash is not in db").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageResponse);
    }

    @GetMapping("list")
    private ResponseEntity<?> listClash(@RequestParam(required = false, defaultValue = "") String ownerId,
                                        @RequestParam(required = false, defaultValue = "false") boolean finished,
                                        @RequestParam(required = false, defaultValue = "9") int size,
                                        @RequestParam(required = false, defaultValue = "0") int page) {
        Page<Clash> clashes = clashService.listAllFilteredClash(ownerId, finished, PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(clashes);
    }

    @GetMapping("delete/{id}")
    private ResponseEntity<?> deleteClash(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.deleteClash(id) ? "Clash with id : " + id + " deleted." : "Could not delete clash. (doesn't exist)");
    }

    @PostMapping("terminate/{clashId}")
    private ResponseEntity<?> finishClash(@PathVariable String clashId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clashService.finishClash(clashId));
    }


}
