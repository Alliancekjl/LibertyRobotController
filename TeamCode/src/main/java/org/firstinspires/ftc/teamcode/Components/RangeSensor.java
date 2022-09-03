package org.firstinspires.ftc.teamcode.Components;

import static org.firstinspires.ftc.teamcode.BasicRobot.op;
import static org.firstinspires.ftc.teamcode.Components.EncoderChassis.angle;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.LED;


public class RangeSensor {

    private AnalogInput ultrasonicFront;
    private AnalogInput ultrasonicRight;
    private AnalogInput ultrasonicLeft;
    private LED ultraFront;


    public RangeSensor() {
        ultrasonicFront =  op.hardwareMap.get(AnalogInput.class, "ultrasonicFront");
        ultrasonicLeft =  op.hardwareMap.get(AnalogInput.class, "ultrasonicLeft");
        ultrasonicRight =  op.hardwareMap.get(AnalogInput.class, "ultrasonicRight");
        ultraFront=op.hardwareMap.get(LED.class, "ultraFront");
        ultraFront.enable(true);
    }

    public double getDistance(boolean front) {
        AnalogInput ultrasonic;
        double rawValue = 0;
        if(front){
            ultrasonic = ultrasonicFront;
            rawValue = ultrasonic.getVoltage();
            ultraFront.enable(false);
        }
        else{
            ultrasonic = ultrasonicLeft;
            rawValue = ultrasonic.getVoltage();
        }
        return  90.48337*rawValue  - 13.12465;
    }
    public void setState(boolean front, boolean state){
        if(front){
            ultraFront.enable(!state);
        }
    }
    public boolean getState(boolean front){
        return ultraFront.isLightOn();
    }
    public double getVoltage(boolean front) {
        AnalogInput ultrasonic;
        if(front){
            ultrasonic = ultrasonicFront;
        }
        else{
            ultrasonic = ultrasonicLeft;
        }
        return ultrasonic.getVoltage();
    }
    public double[] getLocation(){
        double[] pos = {0,0};
            pos[1] = 53.5-getDistance(true);
            pos[0] = getDistance(false)-0.5;
            //hypot
        double hypot =0;
        if(abs(angle)<5&&angle!=0){
            hypot = 10000*angle/abs(angle);
        }else if(angle!=0) {
            hypot = pos[0] / sin(angle * PI / 180);
        }
        else{
            hypot =10000;
        }
            pos[0]*=(hypot-6)/hypot;

        return pos;
    }
}