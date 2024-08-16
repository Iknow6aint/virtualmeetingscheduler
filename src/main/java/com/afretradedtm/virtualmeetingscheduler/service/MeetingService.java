package com.afretradedtm.virtualmeetingscheduler.service;

import com.afretradedtm.virtualmeetingscheduler.model.Meeting;
import com.afretradedtm.virtualmeetingscheduler.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final CalendarIntegrationService calendarIntegrationService;

    public Meeting scheduleMeeting(Meeting meeting) {
        // Integrate with the platform to generate a meeting link
        String meetingLink = calendarIntegrationService.scheduleMeeting(
                meeting.getPlatform(),
                meeting.getTitle(), // Use the new field
                meeting.getStartTime(), // Use the updated field name
                meeting.getEndTime(), // Use the updated field name
                meeting.getDescription(), // Use the new field
                meeting.getAttendeeEmail() // Use the new field
        );

        meeting.setMeetingLink(meetingLink);
        return meetingRepository.save(meeting);
    }

    public Optional<Meeting> findById(Long id) {
        return meetingRepository.findById(id);
    }

    public List<Meeting> findByUserId(Long userId) {
        return meetingRepository.findByUserId(userId);
    }

    public void cancelMeeting(Long id) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(id);
        if (optionalMeeting.isPresent()) {
            Meeting meeting = optionalMeeting.get();
            calendarIntegrationService.cancelMeeting(meeting.getPlatform(), meeting.getId().toString());
            meetingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Meeting not found with id: " + id);
        }
    }

    public Meeting rescheduleMeeting(Long id, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(id);
        if (optionalMeeting.isPresent()) {
            Meeting meeting = optionalMeeting.get();
            String updatedMeetingLink = calendarIntegrationService.rescheduleMeeting(
                    meeting.getPlatform(),
                    meeting.getId().toString(),
                    newStartTime,
                    newEndTime
            );

            meeting.setStartTime(newStartTime);
            meeting.setEndTime(newEndTime);
            meeting.setMeetingLink(updatedMeetingLink);
            return meetingRepository.save(meeting);
        } else {
            throw new RuntimeException("Meeting not found with id: " + id);
        }
    }
}
