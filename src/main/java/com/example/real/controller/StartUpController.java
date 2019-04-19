package com.example.real.controller;

import com.example.real.dto.TagsHeader;
import com.example.real.dto.user.SetttingsHeader;
import com.example.real.service.StartUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class StartUpController {

    @Autowired
    StartUpService startUpService;

    @GetMapping("/api/tags")
    TagsHeader tags() {
        return startUpService.fillTags();
    }

    @GetMapping("/api/profiles/{username}")
    public ResponseEntity getAtriclesAuthor(@RequestHeader HttpHeaders headers, @PathVariable("username") String username) {
        return startUpService.returnUserRequest(headers, username);
    }

    @PutMapping("/api/user")
    public ResponseEntity putSettings(@RequestHeader HttpHeaders headers, @RequestBody SetttingsHeader settingsHeader) {
        return startUpService.examineSettings(headers,settingsHeader);
    }

    @PostMapping("/api/profiles/{username}/follow")
    public ResponseEntity postFollow(@RequestHeader HttpHeaders headers, @PathVariable("username") String username) {
        return startUpService.addFollow(headers, username);
    }

    @DeleteMapping("/api/profiles/{username}/follow")
    public ResponseEntity delFollow(@RequestHeader HttpHeaders headers,@PathVariable("username") String username) {
        return startUpService.deleteFollow(headers, username);
    }
}
