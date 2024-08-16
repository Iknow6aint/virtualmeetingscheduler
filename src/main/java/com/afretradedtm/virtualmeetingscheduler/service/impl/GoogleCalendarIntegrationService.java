package com.afretradedtm.virtualmeetingscheduler.service.impl;

import com.afretradedtm.virtualmeetingscheduler.service.CalendarIntegrationService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.UUID;

@Service
public class GoogleCalendarIntegrationService implements CalendarIntegrationService {

    private static final String APPLICATION_NAME = "Virtual Meeting Scheduler";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Replace with actual method to get authorized credentials
    private Credential getCredentials() {
        // TODO: Implement OAuth2 credential retrieval
        return null;
    }

    private Calendar getCalendarService() throws GeneralSecurityException, IOException {
        return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, getCredentials())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public String scheduleMeeting(String platform, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String attendeeEmail) {
        try {
            Event event = new Event()
                    .setSummary(title)
                    .setDescription(description)
                    .setStart(new EventDateTime()
                            .setDateTime(convertToGoogleDateTime(startTime))
                            .setTimeZone(ZoneId.systemDefault().getId()))
                    .setEnd(new EventDateTime()
                            .setDateTime(convertToGoogleDateTime(endTime))
                            .setTimeZone(ZoneId.systemDefault().getId()))
                    .setAttendees(Collections.singletonList(
                            new Event.Attendee().setEmail(attendeeEmail)
                    ));

            Calendar service = getCalendarService();
            event = service.events().insert("primary", event).execute();
            return event.getHtmlLink();

        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void cancelMeeting(String platform, String meetingId) {
        try {
            Calendar service = getCalendarService();
            service.events().delete("primary", meetingId).execute();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String rescheduleMeeting(String platform, String meetingId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        try {
            Calendar service = getCalendarService();
            Event event = service.events().get("primary", meetingId).execute();

            event.setStart(new EventDateTime()
                    .setDateTime(convertToGoogleDateTime(newStartTime))
                    .setTimeZone(ZoneId.systemDefault().getId()));
            event.setEnd(new EventDateTime()
                    .setDateTime(convertToGoogleDateTime(newEndTime))
                    .setTimeZone(ZoneId.systemDefault().getId()));

            event = service.events().update("primary", event.getId(), event).execute();
            return event.getHtmlLink();

        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    private com.google.api.client.util.DateTime convertToGoogleDateTime(LocalDateTime localDateTime) {
        return new com.google.api.client.util.DateTime(java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private String generateDummyId() {
        return UUID.randomUUID().toString();
    }
}
