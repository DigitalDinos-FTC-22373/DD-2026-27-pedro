package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class BlueBarneyBot {
    // CONSTANTS

    private static int CATAPULT_L_DOWN_POS = 50;
    private static int CATAPULT_R_DOWN_POS = 50;

    private static int CATAPULT_L_SHOOT_POS = -80;
    private static int CATAPULT_R_SHOOT_POS = -80;

    private static int CATAPULT_L_BLOCK_POS = -100;
    private static int CATAPULT_R_BLOCK_POS = -100;

    private static int CATAPULT_L_FLOWER_POS = -85;
    private static int CATAPULT_R_FLOWER_POS = -85;


    public double DEFAULT_P = 0.84;
    public double DEFAULT_F = 252;
    public double FAST_F = 579;
    public double FAST_P = 1.1;
    public double SLOW_F = 100;
    public double SLOW_P = 0.7;

    private DcMotor intake;
    private DcMotor catapult_left;
    private DcMotor catapult_right;

    public final double TICKS_PER_DEGREE = 537.7 / 360;


    public void init(HardwareMap hardwareMap) {

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.REVERSE);

        //Vineeth made the change
        catapult_left = hardwareMap.get(DcMotorEx.class, "catapult_left");
        catapult_right = hardwareMap.get(DcMotorEx.class, "catapult_right");

        catapult_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_left.setDirection(DcMotor.Direction.FORWARD);
        catapult_left.setTargetPosition(CATAPULT_L_DOWN_POS);
        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        catapult_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_right.setDirection(DcMotor.Direction.REVERSE); // direrction good
        catapult_right.setTargetPosition(CATAPULT_R_DOWN_POS);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void shoot() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(FAST_P, 0, 0, FAST_F);
        catapult_left.setTargetPosition(CATAPULT_L_SHOOT_POS);
        catapult_right.setTargetPosition(CATAPULT_R_SHOOT_POS);
        intake.setPower(0);
    }

    public void block() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(DEFAULT_P, 0, 0, DEFAULT_F);
        catapult_left.setTargetPosition(CATAPULT_L_BLOCK_POS);
        catapult_right.setTargetPosition(CATAPULT_R_BLOCK_POS);
        intake.setPower(0);
    }

    public void flower() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(SLOW_P, 0, 0, SLOW_F);
        catapult_left.setTargetPosition(CATAPULT_L_FLOWER_POS);
        catapult_right.setTargetPosition(CATAPULT_R_FLOWER_POS);
        intake.setPower(0);
    }

    public void lower() {
        PIDFCoefficients pidfNew = new PIDFCoefficients(DEFAULT_P, 0, 0, DEFAULT_F);
        catapult_left.setTargetPosition(CATAPULT_L_DOWN_POS);
        catapult_right.setTargetPosition(CATAPULT_R_DOWN_POS);
        intake.setPower(1);
    }
}

