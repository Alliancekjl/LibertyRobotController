package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.util.Range;

public class SwerveModule {
    // drive gears, steering gears, drive motor, azimuth motor, absolute encoder

    private DcMotorEx motor;
    private CRServo servo;
    private AbsoluteAnalogEncoder enc;
    private PIDFController scontroller;
    private PIDFController.PIDCoefficients scoeffs;
    private double P = 1;
    private double I = 1;
    private double D = 1;
    private final double WHEEL_RAD = 2.67717; //inches might change irl due to wheel squish
    private final double DRIVE_RATIO = (52 * 2 * 2)/18.0; //208/18
    private final double AZIMUTH_RATIO = 1.0; //for now
    private final double TPR = 28*DRIVE_RATIO; //ticks per 1 wheel irl rotation;


    public SwerveModule(DcMotorEx m, CRServo s, AbsoluteAnalogEncoder e, PIDFController.PIDCoefficients scoef) {
            motor = m;
            motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

            servo = s;

            enc = e;


    }

    public void azimtuh(double pos){ //in RADianz

    }
    public void drive(){

    }
}