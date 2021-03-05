package org.firstinspires.ftc.teamcode.robots.UGBot.utils;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Constants {
    //misc

    public static int visionView = 0;
    public static final double LAUNCH_HEIGHT = 0.41;
    public static final int ENCODER_TICKS_PER_REVOLUTION = 28;
    public static final double FLYWHEEL_RADIUS = 0.0765;
    public static final double GRAVITY = 9.80665;
    public static final double INCHES_PER_METER = 39.3701;


    //BEGIN Proteus Kinematics
    public static final double ROBOT_RADIUS_INCHES = 8.75;
    public static double ITERATIONS = 1;
    public static final double LAUNCHER_LENGTH = 0.24;
    public static final double LAUNCHER_VERTICAL_OFFSET = 0.085;
    public static final double BASE_LAUNCH_ANGLE = 19.50244851;

    //MUZZLE Corrections
    //keep these X and Y muzzle offsets for reference
    //public static double MUZZLE_X_OFFSET = 0.115;
    //public static double MUZZLE_Y_OFFSET = 0.09;
    //converting to polar we get:
    public static double MUZZLE_DEG_OFFSET = 90-38.0470;
    public static double MUZZLE_RAD_OFFSET = 0.906750906;
    public static double MUZZLE_RADIUS = .146;
    public static double TURRET_AXIS_OFFSET = 0.114;
    public static double TURRET_RADIUS = 0.1500;

    //END Kinematics

    public static double kpFlywheel = 0.6; //proportional constant multiplier goodish
    public static  double kiFlywheel = 1.0; //integral constant multiplier
    public static  double kdFlywheel= 0.0; //derivative constant multiplier

    // Vision
    public static int TOP_LEFT_X = 184;
    public static int TOP_LEFT_Y = 303;
    public static int BOTTOM_RIGHT_X = 484;
    public static int BOTTOM_RIGHT_Y = 448;

    public static double NORMALIZE_ALPHA = 51.0;
    public static double NORMALIZE_BETA = 261.0;

    public static double BLUR_RADIUS = 8.558558558558557;

    public static double HSV_THRESHOLD_HUE_MIN = 0.4668065215846204;
    public static double HSV_THRESHOLD_HUE_MAX = 1000;
    public static double HSV_THRESHOLD_SATURATION_MIN = 40.13039568345324;
    public static double HSV_THRESHOLD_SATURATION_MAX = 255.0;
    public static double HSV_THRESHOLD_VALUE_MIN = 109.84730100784292;
    public static double HSV_THRESHOLD_VALUE_MAX = 255.0;

    public static double MIN_CONTOUR_AREA = .1;

    //odometry positions all in meters
    public static double goalX = 0.9144;
    public static double goalY = 3.6576;
    public static double GOAL_RADIUS = 8;
    public static double POWER_SHOT_RADIUS = 1;
    public static double startingXOffset = 1.2192;
    public static double startingYOffset = .24765;
    public static double HEIGHT_MULTIPLIER = 1.2;
    public static double RPS_MULTIPLIER = 1.1;

    public static int WOBBLE_GRIPPER_CLOSED = 900;
    public static int WOBBLE_GRIPPER_STOWED = 1920;

    public static int overrideTPS = 0;

    public static double  DUMMYVAL = 0.0;

    public enum Target {
        NONE(0, 0, 0),
        HIGH_GOAL(0.9144, 3.6576, 0.88),
        MID_GOAL(0.9144,3.6576,.6858),
        MID_GOAL_CLASSIC(-0.9144,3.6576,.6858),
        LOW_GOAL(0.9144,3.6576,.4318),
        FIRST_POWER_SHOT(.1016,3.6576,.8),
        SECOND_POWER_SHOT(.2921,3.6576,.8),
        THIRD_POWER_SHOT(.4826,3.6576,.8);



        public double x, y, height;

        private Target(double x, double y, double height) {
            this.x = x;
            this.y = y;
            this.height = height;
        }
    }

    public enum Position {
        //headings and elevations that are negative means don't apply them to ending position - let other behaviors control
        START(48/INCHES_PER_METER, ROBOT_RADIUS_INCHES/INCHES_PER_METER,0,0,0),
        WOBBLE_ONE_GRAB(48/INCHES_PER_METER, (ROBOT_RADIUS_INCHES+3)/INCHES_PER_METER,0,340,0),
        CALIBRATION_RESET(48/INCHES_PER_METER, 11*12/INCHES_PER_METER, 0,170,-1),
        //turret needs to rotate counter clockwise to deposit wobble goals A and C - use intermediate turret heading of 170
        TARGET_C_1(48/INCHES_PER_METER, 11*12/INCHES_PER_METER, 0,60+45,0),
        TARGET_C_2((48+7)/INCHES_PER_METER, 10.5*12/INCHES_PER_METER, 0,45+45,0),
        TARGET_B_1((48-7)/INCHES_PER_METER, 8.5*12/INCHES_PER_METER, 0,0,0),
        TARGET_B_2((48+7)/INCHES_PER_METER, 8*12/INCHES_PER_METER, 0,0,0),
        TARGET_A_1((48+7)/INCHES_PER_METER, 7.75*12/INCHES_PER_METER, 0,45+45,0),
        TARGET_A_2((48-7)/INCHES_PER_METER, 7*12/INCHES_PER_METER, 0,45+45,0),
        RING_STACK(36/INCHES_PER_METER, 48/INCHES_PER_METER,-1,-1, -1),
        RING_STACK_APPROACH(36/INCHES_PER_METER, (48+6+ ROBOT_RADIUS_INCHES)/INCHES_PER_METER, 180, 270,0),
        //sweep needs to be very slow
        RING_STACK_SWEEPTO(36/INCHES_PER_METER, (48-10+ ROBOT_RADIUS_INCHES)/INCHES_PER_METER, 180, 270,0),
        WOBBLE_TWO(24/INCHES_PER_METER, 23/INCHES_PER_METER,-1,-1, -1),
        WOBBLE_TWO_APPROACH(35/INCHES_PER_METER, (23+6+ ROBOT_RADIUS_INCHES)/INCHES_PER_METER, 0, 0,0),
        WOBBLE_TWO_GRAB (35/INCHES_PER_METER, (23-6+ ROBOT_RADIUS_INCHES)/INCHES_PER_METER, 0, 0,0),
        NAVIGATE(3*12/INCHES_PER_METER, 7*12/INCHES_PER_METER,-1,-1, -1),
        LAUNCH_PREFERRED(3*12/INCHES_PER_METER, 5.5*12/INCHES_PER_METER,270,-1, -1);

        public double x, y, baseHeading, launchHeading, launchElevation;

        private Position(double x, double y, double baseHeading, double launchHeading, double launchElevation) {
            this.x = x;
            this.y = y;
            this.baseHeading=baseHeading;
            this.launchHeading=launchHeading;
            this.launchElevation=launchElevation;

        }
    }
}
