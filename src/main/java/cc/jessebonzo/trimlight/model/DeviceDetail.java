package cc.jessebonzo.trimlight.model;

import java.util.List;

public record DeviceDetail(String code, String desc, Payload payload) {
    public record Payload(String name,
                          SwitchState switchState,
                          Connectivity connectivity,
                          DeviceState state,
                          ColorOrder colorOrder,
                          int ic,
                          /*
                            The pixel setting for each port.
                            id: Port ID, range from 0 to 3, correspond to port1, port2, port3,
                            port4.
                            start: The start pixel of the port. range: [1, 2048]
                            end: The end pixel of the port. range: [1, 2048].
                            "start" should not greater than "end".
                           */
                          List<Port> ports,
                          /*
                            Device firmware version name.
                           */
                          String fwVersionName,
                          /*
                            All the effects stored in the device.
                           */
                          List<Effect> effects,
                          CombinedEffect combinedEffect,
                          /*
                            Daily schedules.
                            Each device has two daily schedules.
                           */
                          List<DailySchedule> daily,
                          List<CalendarSchedule> calendar,
                          /*
                            Device current running effect.
                            (If the device's switch state is timer mode, although the light is off at
                            this time, it will return the last running effect data.)
                            Note: The effect ID will be -1, when the controller is running a
                            preview effect (not yet saved).
                           */
                          Effect currentEffect) {
        record Port(int id, int start, int end) {
        }

        record CombinedEffect(
                /*
                  A list of each effects' ID in the combined effect.
                 */
                List<Integer> effectIds,
                /*
                  The interval between switching to the next effect. (Unit: minute.)
                 */
                int interval) {
        }
    }
}
