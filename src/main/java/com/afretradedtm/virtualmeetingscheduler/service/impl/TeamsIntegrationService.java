package com.afretradedtm.virtualmeetingscheduler.service.impl;

import com.afretradedtm.virtualmeetingscheduler.service.CalendarIntegrationService;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.models.OnlineMeeting;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.requests.GraphServiceClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class TeamsIntegrationService implements CalendarIntegrationService {

    // Replace with actual method to get authorized Graph client
    private GraphServiceClient<?> getGraphServiceClient() {
        // TODO: Implement OAuth2 authentication to get GraphServiceClient
        return null;
    }

    @Override
    public String scheduleMeeting(String platform, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String attendeeEmail) {
        try {
            Event event = new Event();
            event.subject = title;
            event.body = new ItemBody();
            event.body.content = description;
            event.start = new DateTimeTimeZone();
            event.start.dateTime = convertToMicrosoftDateTime(startTime);
            event.start.timeZone = ZoneId.systemDefault().getId();
            event.end = new DateTimeTimeZone();
            event.end.dateTime = convertToMicrosoftDateTime(endTime);
            event.end.timeZone = ZoneId.systemDefault().getId();

            // Add attendee
            Recipient attendee = new Recipient();
            attendee.emailAddress = new EmailAddress();
            attendee.emailAddress.address = attendeeEmail;
            event.attendees = Collections.singletonList(attendee);

            // Create online meeting
            OnlineMeeting onlineMeeting = new OnlineMeeting();
            onlineMeeting.subject = title;
            onlineMeeting.startDateTime = convertToMicrosoftDateTime(startTime);
            onlineMeeting.endDateTime = convertToMicrosoftDateTime(endTime);
            onlineMeeting.participants = new MeetingParticipants();
            onlineMeeting.participants.attendees = Collections.singletonList(attendee);

            GraphServiceClient<?> graphClient = getGraphServiceClient();
            OnlineMeeting createdMeeting = graphClient.me.onlineMeetings()
                    .buildRequest()
                    .post(onlineMeeting);

            return createdMeeting.joinWebUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void cancelMeeting(String platform, String meetingId) {
        try {
            GraphServiceClient<?> graphClient = getGraphServiceClient();
            graphClient.me.events(meetingId)
                    .buildRequest()
                    .delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String rescheduleMeeting(String platform, String meetingId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        try {
            GraphServiceClient<?> graphClient = getGraphServiceClient();
            Event event = graphClient.me.events(meetingId)
                    .buildRequest()
                    .get();

            event.start.dateTime = convertToMicrosoftDateTime(newStartTime);
            event.end.dateTime = convertToMicrosoftDateTime(newEndTime);

            Event updatedEvent = graphClient.me.events(event.id)
                    .buildRequest()
                    .patch(event);

            return updatedEvent.onlineMeeting.joinWebUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertToMicrosoftDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toString();
    }

    private String generateDummyId() {
        return UUID.randomUUID().toString();
    }
}
