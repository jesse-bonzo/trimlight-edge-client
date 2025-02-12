package cc.jessebonzo;


import cc.jessebonzo.trimlight.model.CalendarSchedule;
import cc.jessebonzo.trimlight.model.Devices;
import cc.jessebonzo.trimlight.model.SwitchState;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.time.Duration;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws ParseException {
        var clientIdOption = Option.builder("client-id").longOpt("client-id").required().hasArg().type(String.class).build();
        var clientSecretOption = Option.builder("client-secret").longOpt("client-secret").hasArg().required().type(String.class).build();
        var cmd = new DefaultParser().parse(new Options().addOption(clientIdOption).addOption(clientSecretOption), args);

        String clientId = cmd.getOptionValue(clientIdOption);
        String secret = cmd.getOptionValue(clientSecretOption);

        try (TrimlightClient trimlightClient = new TrimlightClient(clientId, secret)) {
            var devices = trimlightClient.getDevices();
            System.out.println("Devices: " + devices);

            var device = devices.payload().data().getFirst();
            var details = trimlightClient.getDeviceDetail(device.deviceId());
            System.out.println(details);
            var selectedEffect = details.payload().effects().stream().filter(effect -> "VALENTINE'S DAY".equals(effect.name())).findFirst().orElseThrow();
            var calendarId = details.payload().calendar().stream().mapToInt(CalendarSchedule::id).max().orElse(0) + 1;
            var calendarResponse = trimlightClient.saveCalendarSchedule(device.deviceId(), calendarId, selectedEffect.id(), LocalDate.of(2025, 2, 13).atTime(18, 0), LocalDate.of(2025, 2, 15).atTime(23, 0));
            System.out.println(calendarResponse);
            System.out.println(trimlightClient.updateShadowData(device.deviceId()));

        }
    }

    static void cycleEffects(TrimlightClient trimlightClient, Devices devices) {
        for (Devices.Payload.Data data : devices.payload().data()) {
            var deviceDetail = trimlightClient.getDeviceDetail(data.deviceId());
            trimlightClient.setDeviceState(data.deviceId(), SwitchState.MANUAL_MODE);

            deviceDetail.payload().effects().forEach(effect -> {
                System.out.println("Effect: " + effect);
                var response = trimlightClient.previewBuiltInEffect(data.deviceId(), effect);
                System.out.println(response);
                try {
                    Thread.sleep(Duration.ofSeconds(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            trimlightClient.setDeviceState(data.deviceId(), SwitchState.TIMER_MODE);
        }
    }
}