package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class BlueBarneyBot {

    // CONSTANTS
    public static int CATAPULT_DOWN_POS = 0;
    public static int CATAPULT_SHOOT_POS = -120;
    public static int CATAPULT_BLOCK_POS = -230;
    public static int CATAPULT_FLOWER_POS = -200;
    public static double DEFAULT_P = 0.7;
    public static double DEFAULT_F = 200;
    public static double FAST_F = 510;
    public static double FAST_P = 1;
    public static double SLOW_F = 100;
    public static double SLOW_P = 0.5;


    private DcMotor intake;
    private DcMotorEx catapult_left;
    private DcMotorEx catapult_right;


    public void init(HardwareMap hardwareMap) {

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.REVERSE);

        //Vineeth made the change
        catapult_left = hardwareMap.get(DcMotorEx.class, "catapult_left");
        catapult_right = hardwareMap.get(DcMotorEx.class, "catapult_right");

        catapult_left.setPower(0.1);
        catapult_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_left.setDirection(DcMotor.Direction.FORWARD);
        catapult_left.setTargetPosition(CATAPULT_DOWN_POS);
        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        catapult_right.setPower(0.1);
        catapult_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_right.setDirection(DcMotor.Direction.REVERSE); // direrction good
        catapult_right.setTargetPosition(CATAPULT_DOWN_POS);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void shoot() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(10.5, 0, 0, FAST_F);
        catapult_left.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_right.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_left.setPositionPIDFCoefficients(FAST_P);
        catapult_right.setPositionPIDFCoefficients(FAST_P);

        catapult_left.setTargetPosition(CATAPULT_SHOOT_POS);
        catapult_right.setTargetPosition(CATAPULT_SHOOT_POS);

        catapult_left.setPower(1);
        catapult_right.setPower(1);

        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        intake.setPower(0);
    }

    public void block() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(10.5, 0, 0, DEFAULT_F);
        catapult_left.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_right.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_left.setPositionPIDFCoefficients(FAST_P);
        catapult_right.setPositionPIDFCoefficients(FAST_P);

        catapult_left.setTargetPosition(CATAPULT_BLOCK_POS);
        catapult_right.setTargetPosition(CATAPULT_BLOCK_POS);

        catapult_left.setPower(0.75);
        catapult_right.setPower(0.75);

        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        intake.setPower(0);
    }

    public void flower() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(10.5, 0, 0, SLOW_F);
        catapult_left.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_right.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_left.setPositionPIDFCoefficients(FAST_P);
        catapult_right.setPositionPIDFCoefficients(FAST_P);

        catapult_left.setTargetPosition(CATAPULT_FLOWER_POS);
        catapult_right.setTargetPosition(CATAPULT_FLOWER_POS);

        catapult_left.setPower(0.1);
        catapult_right.setPower(0.1);

        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        intake.setPower(0);
    }

    public void lower() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(10.5, 0, 0, DEFAULT_F);
        catapult_left.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_right.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

        catapult_left.setTargetPosition(CATAPULT_DOWN_POS);
        catapult_right.setTargetPosition(CATAPULT_DOWN_POS);

        catapult_left.setPositionPIDFCoefficients(DEFAULT_P);
        catapult_right.setPositionPIDFCoefficients(DEFAULT_P);

        catapult_left.setPower(0.1);
        catapult_right.setPower(0.1);

        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        intake.setPower(1);


    }

     public double getCatapultLPos(){
        return catapult_left.getCurrentPosition();
     }

    public double getCatapultRPos(){
        return catapult_right.getCurrentPosition();
    }





}

