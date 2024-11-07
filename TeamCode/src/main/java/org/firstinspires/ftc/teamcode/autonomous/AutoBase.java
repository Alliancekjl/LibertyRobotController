package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.BaseRobot;
import org.firstinspires.ftc.teamcode.MainAuto;
import org.firstinspires.ftc.teamcode.systems.ShutdownManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.systems.DynamicInput;
import org.firstinspires.ftc.teamcode.Settings;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Autonomous(name = "Main Autonomous", group = "Autonomous")
public abstract class AutoBase extends LinearOpMode {
    protected String color;
    protected String position;
    private final String defaultColor;
    private final String defaultPosition;

    private static final String[] MENU_OPTIONS = {
            "Red Left", "Red Right", "Blue Left", "Blue Right", "Confirm"
    };

    protected AutoBase(String defaultColor, String defaultPosition) {
        this.defaultColor = defaultColor;
        this.defaultPosition = defaultPosition;
        this.color = defaultColor;
        this.position = defaultPosition;
    }

    @Override
    public void runOpMode() {
        // Menu initialization
        AtomicBoolean menuActive = new AtomicBoolean(true);
        AtomicInteger currentSelection = new AtomicInteger();

        while (!isStarted() && !isStopRequested() && menuActive.get()) {
            // Display menu header
            telemetry.addLine("=== Autonomous Configuration ===");
            telemetry.addLine("\nSelect Starting Position:");

            // Display options with cursor
            for (int i = 0; i < MENU_OPTIONS.length; i++) {
                telemetry.addData(i == currentSelection.get() ? ">" : " ", MENU_OPTIONS[i]);
            }

            // Handle controller input with debounce
            handleControllerInput(gamepad1, true, () -> {
                if (gamepad1.dpad_up) {
                    currentSelection.set((currentSelection.get() - 1 + MENU_OPTIONS.length) % MENU_OPTIONS.length);
                } else if (gamepad1.dpad_down) {
                    currentSelection.set((currentSelection.get() + 1) % MENU_OPTIONS.length);
                } else if (gamepad1.a) {
                    if (currentSelection.get() < MENU_OPTIONS.length - 1) {
                        color = MENU_OPTIONS[currentSelection.get()].split(" ")[1];
                        position = MENU_OPTIONS[currentSelection.get()].split(" ")[2];
                    } else {
                        menuActive.set(false);
                    }
                }
            });

            // Display current selection
            telemetry.addLine("\nSelected Configuration:");
            telemetry.addData("Position", color + " " + position);
            telemetry.update();
        }

        // Initialize robot systems
        DynamicInput dynamicInput = new DynamicInput(gamepad1, gamepad2,
                Settings.DEFAULT_PROFILE, Settings.DEFAULT_PROFILE);
        BaseRobot baseRobot = new BaseRobot(hardwareMap, dynamicInput, this, telemetry);
        MainAuto auto = new MainAuto(baseRobot, color);
        ShutdownManager shutdownManager = new ShutdownManager(this, baseRobot, auto);

        // Run autonomous
        shutdownManager.scheduleShutdownCheck();
        try {
            if (opModeIsActive()) {
                auto.run(color + " " + position);
            }
        } catch (RuntimeException e) {
            // Shutdown handled by ShutdownManager
        }
    }

    private void handleControllerInput(Gamepad gamepad, boolean active, Runnable handler) {
        if (active && (gamepad.dpad_up || gamepad.dpad_down || gamepad.a)) {
            handler.run();
            sleep(250); // Debounce
        }
    }
}