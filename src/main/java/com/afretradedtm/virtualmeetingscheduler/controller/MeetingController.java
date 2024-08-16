package com.afretradedtm.virtualmeetingscheduler.controller;

import com.afretradedtm.virtualmeetingscheduler.model.Meeting;
import com.afretradedtm.virtualmeetingscheduler.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping("/schedule")
    public ResponseEntity<Meeting> scheduleMeeting(@RequestBody Meeting meeting) {
        return ResponseEntity.ok(meetingService.scheduleMeeting(meeting));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Meeting>> getMeetingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(meetingService.findByUserId(userId));
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<Meeting> rescheduleMeeting(@PathVariable Long id, @RequestBody LocalDateTime newDateTime) {
        return ResponseEntity.ok(meetingService.rescheduleMeeting(id, newDateTime));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelMeeting(@PathVariable Long id) {
        meetingService.cancelMeeting(id);
        return ResponseEntity.noContent().build();
    }
}

