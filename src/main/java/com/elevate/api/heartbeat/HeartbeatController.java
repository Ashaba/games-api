package com.elevate.api.heartbeat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heartbeat")
public class HeartbeatController {

    @GetMapping("")
    public ResponseEntity<?> heartbeat() {
        return new ResponseEntity<>(new HeartBeat("OK"), org.springframework.http.HttpStatus.OK);
    }
}
