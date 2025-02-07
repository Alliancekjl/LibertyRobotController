package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class RobotHardware {
    private LinearOpMode myOpmode = null;
    public RobotHardware(LinearOpMode opMode)
    {
        myOpmode = opMode;
    }

    //Constants
    private final double SERVO1_INITIAl = 0;
    private final double SERVO_FINAL = 1;
    private final double SPEED_RATIO = 0.8;

    //Moters
    private DcMotor fLeft;
    private DcMotor fRight;
    private DcMotor bLeft;
    private DcMotor bRight;

    //Servos
    public Servo servo1;

    public void init()
    {
        fLeft = myOpmode.hardwareMap.get(DcMotor.class, "fLeft");
        fRight = myOpmode.hardwareMap.get(DcMotor.class, "fRight");
        bLeft = myOpmode.hardwareMap.get(DcMotor.class, "bLeft");
        bRight = myOpmode.hardwareMap.get(DcMotor.class, "bRight");

        servo1 = myOpmode.hardwareMap.get(Servo.class, "sweeper");

        fLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        fRight.setDirection(DcMotorSimple.Direction.REVERSE);
        bLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        bRight.setDirection(DcMotorSimple.Direction.REVERSE);

        fLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        servo1.setPosition(SERVO1_INITIAl);

    }

    //mecnum code
    public void driveRobot(Gamepad gamepad1){
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = -gamepad1.right_stick_x;

        double frLeft = y + x + rx;
        double frRight = y - x - rx  ;
        double baLeft = y - x + rx;
        double baRight = y + x - rx;

        setDrivePower(frLeft, frRight, baLeft, baRight, SPEED_RATIO);

    }

    //Sets driving speed
    public void setDrivePower(double v1, double v2, double v3, double v4, double s) {
        double n = 1 / s;
        fLeft.setPower(v1/n);
        fRight.setPower(v2/n);
        bLeft.setPower(v3/n);
        bRight.setPower(v4/n);
    }

//    public void servoUp()
//    {
//        servo1.setPosition(SERVO_FINAL);
//    }
}
