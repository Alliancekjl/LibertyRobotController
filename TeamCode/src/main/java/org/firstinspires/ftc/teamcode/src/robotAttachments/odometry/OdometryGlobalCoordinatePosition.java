package org.firstinspires.ftc.teamcode.src.robotAttachments.odometry;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.src.Utills.Executable;
import org.firstinspires.ftc.teamcode.src.Utills.ThreadedSubsystemTemplate;

import java.io.File;


/**
 * A class to do position triangulation based on dead wheel odometry
 *
 * @author Sarthak
 * @since 6/1/2019
 */
public class OdometryGlobalCoordinatePosition extends ThreadedSubsystemTemplate {
    private static final Object lock = new Object();
    private final DcMotor verticalEncoderLeft;
    private final DcMotor verticalEncoderRight;
    private final DcMotor horizontalEncoder;
    private final double COUNTS_PER_INCH = 1892.3724283364;
    //Algorithm constants
    private final double robotEncoderWheelDistance;
    private final double horizontalEncoderTickPerDegreeOffset;
    //Files to access the algorithm constants
    private final File wheelBaseSeparationFile = AppUtil.getInstance().getSettingsFile("wheelBaseSeparation.txt");
    private final File horizontalTickOffsetFile = AppUtil.getInstance().getSettingsFile("horizontalTickOffset.txt");
    //Position variables used for storage and calculations
    private double verticalRightEncoderWheelPosition = 0, verticalLeftEncoderWheelPosition = 0, normalEncoderWheelPosition = 0, changeInRobotOrientation = 0;
    private BNO055IMU imu = null;
    private volatile boolean isActive = false;
    private volatile double robotGlobalXCoordinatePosition = 0, robotGlobalYCoordinatePosition = 0, robotOrientationRadians = 0;
    private double previousVerticalRightEncoderWheelPosition = 0, previousVerticalLeftEncoderWheelPosition = 0, prevNormalEncoderWheelPosition = 0;
    private int verticalLeftEncoderPositionMultiplier = 1;
    private int verticalRightEncoderPositionMultiplier = 1;
    private int normalEncoderPositionMultiplier = 1;
    private double angleOffset = 0;

    /**
     * Constructor for GlobalCoordinatePosition Thread
     *
     * @param verticalEncoderLeft  left odometry encoder, facing the vertical direction
     * @param verticalEncoderRight right odometry encoder, facing the vertical direction
     * @param horizontalEncoder    horizontal odometry encoder, perpendicular to the other two odometry encoder wheels
     * @param threadSleepDelay     delay in milliseconds for the GlobalPositionUpdate thread (50-75 milliseconds is suggested)
     */
    public OdometryGlobalCoordinatePosition(DcMotor verticalEncoderLeft, DcMotor verticalEncoderRight, DcMotor horizontalEncoder, int threadSleepDelay, Executable<Boolean> _isOpmodeActive, Executable<Boolean> _isStopRequested) {
        super(_isOpmodeActive, _isStopRequested);
        this.verticalEncoderLeft = verticalEncoderLeft;
        this.verticalEncoderRight = verticalEncoderRight;
        this.horizontalEncoder = horizontalEncoder;
        sleepTime = threadSleepDelay;

        robotEncoderWheelDistance = Double.parseDouble(ReadWriteFile.readFile(wheelBaseSeparationFile).trim()) * COUNTS_PER_INCH;
        this.horizontalEncoderTickPerDegreeOffset = Double.parseDouble(ReadWriteFile.readFile(horizontalTickOffsetFile).trim());

        this.isStopRequested = _isStopRequested;
        this.opModeIsActive = _isOpmodeActive;

    }


    /**
     * Returns the ports the encoders are plugged into for debug purposes
     *
     * @return the ports in the form Vertical Right, Vertical Left, Horizontal
     */
    public Integer[] getPorts() {
        Integer[] ports = new Integer[3];
        ports[0] = verticalEncoderRight.getPortNumber();
        ports[1] = verticalEncoderLeft.getPortNumber();
        ports[2] = horizontalEncoder.getPortNumber();
        return ports;
    }

    /**
     * Outputs the location to the telemetry
     *
     * @param telemetry The OpMode telemetry
     */
    public void showPosition(Telemetry telemetry) {
        telemetry.addData("X: ", this.returnRelativeXPosition());
        telemetry.addData("Y: ", this.returnRelativeYPosition());
        telemetry.addData("Rotation: ", this.returnOrientation());
    }

    /**
     * Getter for the COUNTS_PER_INCH variable
     *
     * @return COUNTS_PER_INCH in ticks per inch
     */
    public double getCOUNTS_PER_INCH() {
        return COUNTS_PER_INCH;
    }

    /**
     * Getter for the right encoder position
     *
     * @return The right encoder position in ticks
     */
    public int returnRightEncoderPosition() {
        return verticalRightEncoderPositionMultiplier * verticalEncoderRight.getCurrentPosition();
    }

    /**
     * Getter for the left encoder position
     *
     * @return The left encoder position in ticks
     */
    public int returnLeftEncoderPosition() {
        return verticalLeftEncoderPositionMultiplier * verticalEncoderLeft.getCurrentPosition();
    }

    /**
     * Getter for the horizontal encoder position
     *
     * @return The horizontal encoder position in ticks
     */
    public int returnHorizontalEncoderPosition() {
        return normalEncoderPositionMultiplier * horizontalEncoder.getCurrentPosition();
    }

    /**
     * Returns the tick values of the encoders
     *
     * @return Returns in this order: Vertical Right, Vertical Left, Horizontal
     */
    public int[] returnRaw() {
        int[] positions = new int[3];
        positions[0] = verticalEncoderRight.getCurrentPosition();
        positions[1] = verticalEncoderLeft.getCurrentPosition();
        positions[2] = horizontalEncoder.getCurrentPosition();
        return positions;
    }

    /**
     * Sets the orientation of the odometry calibration system
     *
     * @param angle the angle to be at
     */
    public void setOrientation(double angle) {
        synchronized (lock) {
            if (imu != null) {
                angleOffset = Math.toRadians(angle);
            } else {
                robotOrientationRadians = Math.toRadians(angle);
            }
        }
    }

    /**
     * Sets the position of the robot
     *
     * @param x     The x-position in inches
     * @param y     The y-position in inches
     * @param angle The angle in degrees
     */
    public void setPosition(double x, double y, double angle) {
        synchronized (lock) {
            robotGlobalXCoordinatePosition = x * COUNTS_PER_INCH;
            robotGlobalYCoordinatePosition = y * COUNTS_PER_INCH;
            setOrientation(angle);
        }

    }

    /**
     * Returns if the Thread Main is currently executing
     *
     * @return True if Thread Main is currently executing, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets the current angle of the IMU
     *
     * @return The angle parallel to the floor in degrees
     */
    protected double getImuAngle() {
        double returnVal = 0;
        if (imu.getAngularOrientation().firstAngle < 0) {
            returnVal = Math.abs(imu.getAngularOrientation().firstAngle);
        } else {
            returnVal = Math.abs(imu.getAngularOrientation().firstAngle - 360);
        }
        return returnVal % 360;

    }

    /**
     * Sets the IMU
     *
     * @param imu The imu to set to
     */
    public void setImu(BNO055IMU imu) {
        synchronized (lock) {
            this.imu = imu;
        }
    }

    /**
     * Updates the global (x, y, theta) coordinate position of the robot using the odometry encoders
     */
    protected void threadMain() {
        isActive = true;
        synchronized (lock) {
            if (imu != null) {
                robotOrientationRadians = Math.toRadians(getImuAngle()) + angleOffset;
                robotOrientationRadians = robotOrientationRadians % 360;
            }
            //Get Current Positions
            verticalLeftEncoderWheelPosition = (verticalEncoderLeft.getCurrentPosition() * verticalLeftEncoderPositionMultiplier);
            verticalRightEncoderWheelPosition = (verticalEncoderRight.getCurrentPosition() * verticalRightEncoderPositionMultiplier);

            double leftChange = verticalLeftEncoderWheelPosition - previousVerticalLeftEncoderWheelPosition;
            double rightChange = verticalRightEncoderWheelPosition - previousVerticalRightEncoderWheelPosition;

            //Calculate Angle
            changeInRobotOrientation = (leftChange - rightChange) / (robotEncoderWheelDistance);
            robotOrientationRadians = ((robotOrientationRadians + changeInRobotOrientation));

            //Get the components of the motion
            normalEncoderWheelPosition = (horizontalEncoder.getCurrentPosition() * normalEncoderPositionMultiplier);
            double rawHorizontalChange = normalEncoderWheelPosition - prevNormalEncoderWheelPosition;
            double horizontalChange = rawHorizontalChange - (changeInRobotOrientation * horizontalEncoderTickPerDegreeOffset);

            double p = ((rightChange + leftChange) / 2);
            double n = horizontalChange;

            //Calculate and update the position values
            robotGlobalXCoordinatePosition = robotGlobalXCoordinatePosition + (p * Math.sin(robotOrientationRadians) + n * Math.cos(robotOrientationRadians));
            robotGlobalYCoordinatePosition = robotGlobalYCoordinatePosition + (p * Math.cos(robotOrientationRadians) - n * Math.sin(robotOrientationRadians));

            previousVerticalLeftEncoderWheelPosition = verticalLeftEncoderWheelPosition;
            previousVerticalRightEncoderWheelPosition = verticalRightEncoderWheelPosition;
            prevNormalEncoderWheelPosition = normalEncoderWheelPosition;
        }
        isActive = false;
    }

    /**
     * Returns the robot's global x coordinate
     *
     * @return global x coordinate
     */
    public double returnRawXCoordinate() {
        return robotGlobalXCoordinatePosition;
    }

    /**
     * Returns the robot's global y coordinate
     *
     * @return global y coordinate
     */
    public double returnRawYCoordinate() {
        return robotGlobalYCoordinatePosition;
    }

    /**
     * Returns the robot's global orientation
     *
     * @return global orientation, in degrees
     */
    public double returnOrientation() {
        return Math.toDegrees(robotOrientationRadians) % 360;
    }

    /**
     * Returns the robot's global X Position in inches
     *
     * @return global X Position in inches
     */
    public double returnRelativeXPosition() {
        return robotGlobalXCoordinatePosition / COUNTS_PER_INCH;
    }

    /**
     * Returns the robot's global Y Position in inches
     *
     * @return global Y Position in inches
     */
    public double returnRelativeYPosition() {
        return robotGlobalYCoordinatePosition / COUNTS_PER_INCH;
    }

    /**
     * Returns the string name of the horizontal encoder
     *
     * @return string name of the horizontal encoder
     */
    protected String getHorizontalMotorName() {
        return horizontalEncoder.getDeviceName();
    }

    /**
     * Returns the string name of the Right encoder
     *
     * @return string name of the Right encoder
     */
    protected String getVerticalRightMotorName() {
        return verticalEncoderRight.getDeviceName();
    }

    /**
     * Returns the string name of the Left encoder
     *
     * @return string name of the Left encoder
     */
    protected String getVerticalLeftMotorName() {
        return verticalEncoderLeft.getDeviceName();
    }

    /**
     * Reverses the left encoder
     */
    public void reverseLeftEncoder() {
        if (verticalLeftEncoderPositionMultiplier == 1) {
            verticalLeftEncoderPositionMultiplier = -1;
        } else {
            verticalLeftEncoderPositionMultiplier = 1;
        }
    }

    /**
     * Reverses the right encoder
     */
    public void reverseRightEncoder() {
        if (verticalRightEncoderPositionMultiplier == 1) {
            verticalRightEncoderPositionMultiplier = -1;
        } else {
            verticalRightEncoderPositionMultiplier = 1;
        }
    }

    /**
     * Reverses the Horizontal encoder
     */
    public void reverseNormalEncoder() {
        if (normalEncoderPositionMultiplier == 1) {
            normalEncoderPositionMultiplier = -1;
        } else {
            normalEncoderPositionMultiplier = 1;
        }
    }

}
