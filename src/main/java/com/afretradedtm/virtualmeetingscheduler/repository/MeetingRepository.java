package com.afretradedtm.virtualmeetingscheduler.repository;

import com.afretradedtm.virtualmeetingscheduler.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    
    /**
     * Finds meetings by the user ID.
     *
     * @param userId The ID of the user.
     * @return A list of meetings associated with the given user ID.
     */
    List<Meeting> findByUserId(Long userId);

    /**
     * Finds a meeting by its meeting ID.
     *
     * @param meetingId The ID of the meeting.
     * @return An Optional containing the meeting if found, or empty if not.
     */
    Optional<Meeting> findByMeetingId(String meetingId);

    /**
     * Finds meetings by the platform.
     *
     * @param platform The platform where the meetings were scheduled (e.g., Google Meet, Zoom, Teams).
     * @return A list of meetings associated with the given platform.
     */
    List<Meeting> findByPlatform(String platform);
}
