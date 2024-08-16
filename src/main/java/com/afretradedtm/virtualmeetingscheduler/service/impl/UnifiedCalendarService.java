package com.afretradedtm.virtualmeetingscheduler.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UnifiedCalendarService {

    private final GoogleCalendarIntegrationService googleService;
    private final ZoomIntegrationService zoomService;
    private final TeamsIntegrationService teamsService;

    public String scheduleMeeting(String platform, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String attendeeEmail) {
        switch (platform.toLowerCase()) {
            case "google":
                return googleService.scheduleMeeting(platform, title, startTime, endTime, description, attendeeEmail);
            case "zoom":
                return zoomService.scheduleMeeting(platform, title, startTime, endTime, description, attendeeEmail);
            case "teams":
                return teamsService.scheduleMeeting(platform, title, startTime, endTime, description, attendeeEmail);
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    public void cancelMeeting(String platform, String meetingId) {
        switch (platform.toLowerCase()) {
            case "google":
                googleService.cancelMeeting(platform, meetingId);
                break;
            case "zoom":
                zoomService.cancelMeeting(platform, meetingId);
                break;
            case "teams":
                teamsService.cancelMeeting(platform, meetingId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    public String rescheduleMeeting(String platform, String meetingId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        switch (platform.toLowerCase()) {
            case "google":
                return googleService.rescheduleMeeting(platform, meetingId, newStartTime, newEndTime);
            case "zoom":
                return zoomService.rescheduleMeeting(platform, meetingId, newStartTime, newEndTime);
            case "teams":
                return teamsService.rescheduleMeeting(platform, meetingId, newStartTime, newEndTime);
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
}
