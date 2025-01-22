package org.firstinspires.ftc.teamcode.Autonomous;


import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Systems.BotTelemetry;
import org.firstinspires.ftc.teamcode.Systems.IMU;
import org.firstinspires.ftc.teamcode.Systems.Input;
import org.firstinspires.ftc.teamcode.Systems.Motors;

@Autonomous(name="Auto-Main")

public class aUtOnOmOuSv2 extends LinearOpMode {


    int step = 1;



    public void runOpMode() throws InterruptedException {
        Telemetry dashboardTelemetry = FtcDashboard.getInstance().getTelemetry(); //AND THIS BEFORE COMPETITION also line 109
        BotTelemetry.setTelemetry(telemetry, dashboardTelemetry);

        Input input = new Input(hardwareMap);
        Motors motors = new Motors(hardwareMap);
        IMU imu = new IMU(hardwareMap);

        ElapsedTime time = new ElapsedTime();

        waitForStart();


        while (opModeIsActive()) {

            // Step 1
            Actions.driveToBarFromInitialPositionForSpecimen(0);
            Actions.hangAndReleaseSpecimen();

            // Step 2
            Actions.driveBehindSampleFromLocation(1, "bar");
            Actions.pushOrReverseSampleToHumanPlayer(1, "push");

            // Step 3
            Actions.pushOrReverseSampleToHumanPlayer(1, "reverse");
            Actions.driveRightUntilBehindSample(2);

            // Step 4
            Actions.pushOrReverseSampleToHumanPlayer(2, "push");

            // Step 5
            Actions.grabSpecimen(1);
            Actions.driveToInitialPositionFromHumanPlayerPitStopX(2);
            Actions.driveToBarFromInitialPositionForSpecimen(1);
            Actions.hangAndReleaseSpecimen();

            //Step 6
            Actions.driveBehindSampleFromLocation(3, "bar");
            Actions.pushOrReverseSampleToHumanPlayer(3, "push");

            //Step 7
            Actions.grabSpecimen(2);
            Actions.driveToInitialPositionFromHumanPlayerPitStopX(3);
            Actions.driveToBarFromInitialPositionForSpecimen(2);
            Actions.hangAndReleaseSpecimen();

            //Step 8
            Actions.driveToHumanPlayerFromBarForFinalSpecimen();
            Actions.grabSpecimen(3);
            Actions.driveToInitialPositionFromHumanPlayerPitStopX(1);
            Actions.driveToBarFromInitialPositionForSpecimen(3);
            Actions.hangAndReleaseSpecimen();

            //Step 9
            Actions.levelOneAsension();



            while (opModeIsActive());


            Actions.driveBehindSampleFromLocation(1, "human player");







            BotTelemetry.addData("Step:", step);
            BotTelemetry.addData("Encoder Distance (in inches)", input.getDistance());
            BotTelemetry.update();
        }


    }
}
