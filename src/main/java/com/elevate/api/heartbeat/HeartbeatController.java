package com.elevate.api.heartbeat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/heartbeat")
public class HeartbeatController {
    private static final String TAG = "HeartbeatController";
    private static final Logger logger = Logger.getLogger(HeartbeatController.class.getName());

    @GetMapping("")
    public ResponseEntity<?> heartbeat() {
        logger.info(TAG + " - heartbeat: OK");
        return new ResponseEntity<>(new HeartBeat("OK"), org.springframework.http.HttpStatus.OK);
    }
}
