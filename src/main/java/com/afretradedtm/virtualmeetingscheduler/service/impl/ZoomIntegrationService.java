package com.afretradedtm.virtualmeetingscheduler.service.impl;

import com.afretradedtm.virtualmeetingscheduler.service.CalendarIntegrationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class ZoomIntegrationService implements CalendarIntegrationService {

    private static final String ZOOM_API_URL = "https://api.zoom.us/v2";
    private static final String ZOOM_API_KEY = "your_zoom_api_key";
    private static final String ZOOM_API_SECRET = "your_zoom_api_secret";
    private static final String JWT_TOKEN = "your_jwt_token";

    @Override
    public String scheduleMeeting(String platform, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String attendeeEmail) {
        try {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonRequest = objectMapper.createObjectNode()
                    .put("topic", title)
                    .put("start_time", convertToZoomDateTime(startTime))
                    .put("duration", calculateDurationInMinutes(startTime, endTime))
                    .put("agenda", description)
                    .put("type", 2);

            RequestBody body = RequestBody.create(
                    jsonRequest.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(ZOOM_API_URL + "/users/me/meetings")
                    .addHeader("Authorization", "Bearer " + JWT_TOKEN)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode responseBody = objectMapper.readTree(response.body().string());
            return responseBody.get("join_url").asText();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void cancelMeeting(String platform, String meetingId) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(ZOOM_API_URL + "/meetings/" + meetingId)
                    .addHeader("Authorization", "Bearer " + JWT_TOKEN)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String rescheduleMeeting(String platform, String meetingId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        try {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonRequest = objectMapper.createObjectNode()
                    .put("start_time", convertToZoomDateTime(newStartTime))
                    .put("duration", calculateDurationInMinutes(newStartTime, newEndTime));

            RequestBody body = RequestBody.create(
                    jsonRequest.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(ZOOM_API_URL + "/meetings/" + meetingId)
                    .addHeader("Authorization", "Bearer " + JWT_TOKEN)
                    .patch(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JsonNode responseBody = objectMapper.readTree(response.body().string());
            return responseBody.get("join_url").asText();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertToZoomDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private int calculateDurationInMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        return (int) java.time.Duration.between(startTime, endTime).toMinutes();
    }

    private String generateDummyId() {
        return "dummy-zoom-id";
    }
}
