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
    public static int CATAPULT_DOWN_POS = 80;// it was 50
    public static int CATAPULT_SHOOT_POS = -165;
    public static int CATAPULT_BLOCK_POS = -230;
    public static int CATAPULT_FLOWER_POS = -200;

    public static int CATAPULT_INIT_POS = 100;
    public static double DEFAULT_P = 0.7;
    public static double DEFAULT_F = 300;// 200 previous
    public static double FAST_F = 510;
    public static double FAST_P = 1;
    public static double SLOW_F = 100;
    public static double SLOW_P = 0.5;


    private DcMotor intake;
    private DcMotorEx catapult_left;
    private DcMotorEx catapult_right;

    private  double setPower;
    private int setPosition;
    private double set_P;
    private double set_F;

    private boolean bottomFound = false;


    public void init(HardwareMap hardwareMap) {

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.REVERSE);

        //Vineeth made the change
        catapult_left = hardwareMap.get(DcMotorEx.class, "catapult_left");
        catapult_right = hardwareMap.get(DcMotorEx.class, "catapult_right");

        catapult_left.setPower(0.5);
        catapult_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_left.setDirection(DcMotor.Direction.FORWARD);
        catapult_left.setTargetPosition(CATAPULT_DOWN_POS);
        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        catapult_right.setPower(0.5);
        catapult_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_right.setDirection(DcMotor.Direction.REVERSE); // direrction good
        catapult_right.setTargetPosition(CATAPULT_DOWN_POS);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        set_F = FAST_F;
        set_P = FAST_P;
        setPosition = 0;
        setPower = 1;
    }


    public int init_loop() {
        if (!bottomFound) {
            int curPos = catapult_right.getCurrentPosition();
            int diff  = Math.abs(curPos - setPosition);
            if (diff < 20 ){
                setPosition += 20;
            }

            if (diff > 30 ){
                catapult_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                catapult_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                setPosition = 0;
                bottomFound = true;
            }
        }

        catapultUpdate();
    return setPosition;
    }



    public void shoot() {
        set_F = FAST_F;
        set_P = FAST_P;
        setPosition = CATAPULT_SHOOT_POS;
        setPower = 1;
        intake.setPower(0);
    }

    public void block() {

        set_F = DEFAULT_F;
        set_P = DEFAULT_P;
        setPosition = CATAPULT_BLOCK_POS;
        setPower = 0.75;
        intake.setPower(0);
    }

    public void flower(){
        set_F = SLOW_F;
        set_P = SLOW_P;
        setPosition = CATAPULT_FLOWER_POS;
        setPower = 0.1;
        intake.setPower(0);
    }

    public void lower() {
        set_F = SLOW_F;
        set_P = SLOW_P;
        setPosition = CATAPULT_DOWN_POS;
        setPower = 0.5;
        intake.setPower(1);
    }

    public void intakeOn(){
        intake.setPower(1);
    }

    public void intakeOut(){
        intake.setPower(-1);
    }
    public void intakeOff(){
        intake.setPower(0);
    }


    public void catapultUpdate(){
        PIDFCoefficients pidfNew = new PIDFCoefficients(10.5, 0, 0, set_F);
        catapult_left.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_right.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_left.setPositionPIDFCoefficients(set_P);
        catapult_right.setPositionPIDFCoefficients(set_P);

        catapult_left.setTargetPosition(setPosition);
        catapult_right.setTargetPosition(setPosition);

        catapult_left.setPower(setPower);
        catapult_right.setPower(setPower);

        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void positionInit(){
        set_P = DEFAULT_P;
        set_F = DEFAULT_F;
        setPosition = CATAPULT_INIT_POS;
        setPower = 0.75;
        intake.setPower(1);
    }

     public double getCatapultLPos(){
        return catapult_left.getCurrentPosition();
     }

    public double getCatapultRPos(){
        return catapult_right.getCurrentPosition();
    }

    public void setBottom(){
        catapult_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }





}

