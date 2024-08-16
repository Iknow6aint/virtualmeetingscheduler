package com.afretradedtm.virtualmeetingscheduler.service;

import java.time.LocalDateTime;

public interface CalendarIntegrationService {
    
    /**
     * Schedules a meeting on the specified platform.
     *
     * @param platform The platform to schedule the meeting on (e.g., Google Meet, Zoom, Teams).
     * @param title The title of the meeting.
     * @param startTime The start time of the meeting.
     * @param endTime The end time of the meeting.
     * @param description The description of the meeting.
     * @param attendeeEmail The email address of the attendee.
     * @return The URL of the meeting if successful.
     */
    String scheduleMeeting(String platform, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String attendeeEmail);

    /**
     * Cancels a meeting on the specified platform.
     *
     * @param platform The platform where the meeting was scheduled (e.g., Google Meet, Zoom, Teams).
     * @param meetingId The ID of the meeting to cancel.
     */
    void cancelMeeting(String platform, String meetingId);

    /**
     * Reschedules a meeting on the specified platform.
     *
     * @param platform The platform where the meeting is scheduled (e.g., Google Meet, Zoom, Teams).
     * @param meetingId The ID of the meeting to reschedule.
     * @param newStartTime The new start time of the meeting.
     * @param newEndTime The new end time of the meeting.
     * @return The updated URL of the meeting if successful.
     */
    String rescheduleMeeting(String platform, String meetingId, LocalDateTime newStartTime, LocalDateTime newEndTime);
}
