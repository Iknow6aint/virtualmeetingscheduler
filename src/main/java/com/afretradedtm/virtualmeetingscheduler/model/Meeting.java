package com.afretradedtm.virtualmeetingscheduler.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platform;
    private String title; // Added field
    private LocalDateTime startTime; // Updated field name
    private LocalDateTime endTime; // Updated field name
    private String description; // Added field
    private String attendeeEmail; // Added field
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    private InterviewLocation location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
