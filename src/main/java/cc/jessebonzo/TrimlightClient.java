package cc.jessebonzo;

import cc.jessebonzo.trimlight.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.StandardException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

public class TrimlightClient implements AutoCloseable {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    private static final String baseUrl = "https://trimlight.ledhue.com/trimlight";

    private final String clientId;
    private final String clientSecret;
    private final HttpClient client = HttpClient.newHttpClient();

    public TrimlightClient(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private static <T> T read(InputStream is, Class<T> type) throws IOException {
        return objectMapper.readValue(is, type);
    }

    private static <T> T read(String str, Class<T> type) throws IOException {
        return objectMapper.readValue(str, type);
    }

    public Devices getDevices() {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/devices")).build(),
                    HttpResponse.BodyHandlers.ofInputStream());
            return read(response.body(), Devices.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    public DeviceDetail getDeviceDetail(String deviceId) {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(new DeviceDetailRequest(deviceId))))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/get")).build(),
                    HttpResponse.BodyHandlers.ofString());
            return read(response.body(), DeviceDetail.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    public Response setDeviceState(String deviceId, SwitchState state) {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(new DeviceStateRequest(deviceId, state))))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/update")).build(),
                    HttpResponse.BodyHandlers.ofString());
            return read(response.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    // very similar to setDeviceState
    public Response setDeviceName(String deviceId, String name) {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(new DeviceStateRequest(deviceId, name))))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/update")).build(),
                    HttpResponse.BodyHandlers.ofString());
            return read(response.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    // very similar to setDeviceState
    // not sure why'd you want to do this
    public Response setDeviceColorOrder(String deviceId, ColorOrder colorOrder) {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(new DeviceStateRequest(deviceId, colorOrder))))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/update")).build(),
                    HttpResponse.BodyHandlers.ofString());
            return read(response.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    // TODO: Set device ic
    // TODO: Set device port

    public Response previewBuiltInEffect(String deviceId, Effect effect) {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(new PreviewEffectRequest(deviceId, effect))))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/effect/preview")).build(),
                    HttpResponse.BodyHandlers.ofString());
            return read(response.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    public Response previewCustomEffect(String deviceId, Effect effect) {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(new PreviewEffectRequest(deviceId, effect))))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/effect/preview")).build(),
                    HttpResponse.BodyHandlers.ofString());
            return read(response.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    public CalendarResponse saveCalendarSchedule(String deviceId, int calendarId, int effectId, LocalDateTime start, LocalDateTime end) {
        try {
            final SaveCalendarRequest request = new SaveCalendarRequest(deviceId,
                    new CalendarSchedule(1,
                            effectId,
                            new ScheduleDate(start.toLocalDate()),
                            new ScheduleDate(end.toLocalDate()),
                            new ScheduleTime(start.toLocalTime()),
                            new ScheduleTime(end.toLocalTime())));
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(request)))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/calendar/save")).build(),
                    HttpResponse.BodyHandlers.ofInputStream());
            return read(response.body(), CalendarResponse.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    public Response updateShadowData(String deviceId) {
        try {
            var response = client.send(newRequest(clientId, clientSecret)
                            .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(new DeviceDetailRequest(deviceId))))//
                            .uri(URI.create(baseUrl + "/v1/oauth/resources/device/notify-update-shadow")).build(),
                    HttpResponse.BodyHandlers.ofInputStream());
            return read(response.body(), Response.class);
        } catch (IOException | InterruptedException e) {
            throw handle(e);
        }
    }

    static HttpRequest.Builder newRequest(String clientId, String clientSecret) {
        var accessToken = accessToken(clientId, clientSecret);
        return HttpRequest.newBuilder()//
                .header("Content-Type", "application/json")//
                .header("Authorization", accessToken.token())//
                .header("S-ClientId", clientId)//
                .header("S-Timestamp", String.valueOf(accessToken.timestamp()));
    }

    static AccessToken accessToken(String clientId, String clientSecret) {
        try {
            var timestamp = System.currentTimeMillis();
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] signature = mac.doFinal(("Trimlight|" + clientId + "|" + timestamp).getBytes(StandardCharsets.UTF_8));
            return new AccessToken(timestamp, Base64.getEncoder().encodeToString(signature));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw handle(e);
        }
    }

    static TrimlightClientException handle(Exception e) {
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        throw new TrimlightClientException(e);
    }

    @Override
    public void close() {
        client.close();
    }

    record AccessToken(long timestamp, String token) {
    }

    @StandardException
    public static class TrimlightClientException extends RuntimeException {
    }
}
