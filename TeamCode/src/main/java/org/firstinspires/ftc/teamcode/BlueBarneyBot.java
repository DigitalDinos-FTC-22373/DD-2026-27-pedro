package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class BlueBarneyBot {
    private DcMotor intake;

    private SparkFunOTOS sparkFunOTOS;
    private DcMotor frontLeftDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;
    private DcMotor frontRightDrive;
    private DcMotor Catapult0;
    IMU imu;

    public void init(HardwareMap hardwareMap){
        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));

        sparkFunOTOS = hardwareMap.get(SparkFunOTOS.class, "otos");
        configureOTOS();


        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.

        frontLeftDrive = hardwareMap.get(DcMotor.class, "DriveFL");
        backLeftDrive = hardwareMap.get(DcMotor.class, "DriveBL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "DriveFR");
        backRightDrive = hardwareMap.get(DcMotor.class, "DriveBR");

        intake = hardwareMap.get(DcMotor.class, "intake");
        //Vineeth made the change
        Catapult0 = hardwareMap.get(DcMotorEx.class, "Catapult0");

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        intake.setDirection(DcMotor.Direction.FORWARD);
//        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // shooter.setMode(DcMotor.RunMode.);
    }
    private  void configureOTOS() {
        sparkFunOTOS.setLinearUnit(DistanceUnit.INCH);
        sparkFunOTOS.setAngularUnit(AngleUnit.DEGREES);
        sparkFunOTOS.setOffset(new SparkFunOTOS.Pose2D(-0.125, -3.75, 0));
        sparkFunOTOS.setLinearScalar(1.0);
        sparkFunOTOS.setAngularScalar(0.993);
        sparkFunOTOS.resetTracking();
        sparkFunOTOS.setPosition(new SparkFunOTOS.Pose2D(0,0,0));
        sparkFunOTOS.calibrateImu(255, false);
    }
    }
