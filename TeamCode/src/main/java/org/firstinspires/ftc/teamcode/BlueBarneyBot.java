package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class BlueBarneyBot {

    // CONSTANTS
    public static int CATAPULT_DOWN_POS = -30;
    public static int CATAPULT_SHOOT_POS = 180;
    public static int CATAPULT_BLOCK_POS = 250;
    public static int CATAPULT_FLOWER_POS = 230; //220
    public static double DEFAULT_P = 0.7;  // Lowering and blocking
    public static double DEFAULT_F = 300;
    public static double FAST_F = 400; // Shooting
    public static double FAST_P = 1.9;
    public static double SLOW_F = 550;  // Flower
    public static double SLOW_P = 0.15;
    public static double ARM_UP_POS = 0.95;
    public static double ARM_DOWN_POS = 0.27;

    public static int AUTON_DOWN_POS = -300;

    public static double FLOWER_F = 505;  // Flower 550
    public static double FLOWER_P = 0.12;



    private DcMotor intake;
    private DcMotorEx catapult_left;
    private DcMotorEx catapult_right;
    private Servo flower_arm;
    private int setPosition;
    private int setPosTolerance;
    private int setFlowerPos;

    private double setPower = 1.0;
    private double set_P;
    private double set_F;
    private boolean blocking;
    private boolean arm_is_up;
    private boolean flower_up;

    private boolean catapult_init = false;

    public void init(HardwareMap hardwareMap) {

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.REVERSE);
        intake.setPower(0);

        flower_arm = hardwareMap.get(Servo.class, "flower_arm");
        flower_arm.setDirection(Servo.Direction.FORWARD);
        flower_arm.setPosition(0);

        //Vineeth made the change
        catapult_left = hardwareMap.get(DcMotorEx.class, "catapult_left");
        catapult_right = hardwareMap.get(DcMotorEx.class, "catapult_right");

        catapult_left.setPower(1.0);
        catapult_left.setDirection(DcMotor.Direction.REVERSE);
        catapult_left.setTargetPosition(CATAPULT_DOWN_POS);
        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        catapult_right.setPower(1.0);
        catapult_right.setDirection(DcMotor.Direction.FORWARD); // direrction good
        catapult_right.setTargetPosition(CATAPULT_DOWN_POS);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        set_F = DEFAULT_F;
        set_P = DEFAULT_P;
        setPosition = CATAPULT_DOWN_POS;
    }

    public void start(){
        intake.setPower(1);
    }

    public void shoot() {
        set_F = FAST_F;
        set_P = FAST_P;
        setPosition = CATAPULT_SHOOT_POS;
        setPosTolerance = 10;
        intake.setPower(0);
        catapult_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        blocking = false;

    }

    public void block() {
        set_F = DEFAULT_F;
        set_P = DEFAULT_P;
        setPosition = CATAPULT_BLOCK_POS;
        setPosTolerance = 5;
        intake.setPower(0);
        blocking = true;
    }

    public void toggleflower() {
        if (!flower_up) {
            set_F = FLOWER_F;
            set_P = FLOWER_P;
            setPosition = CATAPULT_FLOWER_POS;
            setPosTolerance = 2;
            intake.setPower(0);
            blocking = false;
            flower_up = true;
        } else {
            lower();
            flower_up = false;
        }
    }


    public void lower() {
        set_F = DEFAULT_F;
        set_P = DEFAULT_P;
        setPosition = CATAPULT_DOWN_POS;
        setPosTolerance = 2;
        intake.setPower(1);
        blocking = false;
    }

    public void arm_up(){
        flower_arm.setPosition(ARM_UP_POS);
        arm_is_up = true;
    }

    public void arm_down(){
        flower_arm.setPosition(ARM_DOWN_POS);
        arm_is_up = false;
    }

    public void toggle_arm(){
        if (arm_is_up){arm_down();}
        else {arm_up();        }
    }

    public boolean isBlocking(){
        return blocking;
    }

    public void resetCatapultEncoders(){
        catapult_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapult_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void adjustCatapultpos(int increment){
        setPosition = setPosition + increment;
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

    public void shooterPoweradjust(double inc){
        setPower += inc;
        catapult_left.setPower(setPower);
        catapult_right.setPower(setPower);

    }
    public double getShooterPow(){
        return setPower;
    }

    public void setAutonDownPos(){
            if (catapult_init = false) {
                set_P = FAST_P;
                set_F = FAST_F;
                catapult_init = true;
            }
            else{
                catapult_left.setTargetPosition(AUTON_DOWN_POS);
                catapult_right.setTargetPosition(AUTON_DOWN_POS);

                catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                catapult_init = false;

            }






        //        catapult_left.setTargetPosition(AUTON_DOWN_POS);
//        catapult_right.setTargetPosition(AUTON_DOWN_POS);
//        set_P =  FAST_P;
//        set_F = FAST_F;
//        resetCatapultEncoders();
    }

    public void catapultUpdate(){
        PIDFCoefficients pidfNew = new PIDFCoefficients(10.5, 0, 0, set_F);
        catapult_left.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapult_right.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

        if (flower_up) {
            if (catapult_left.getCurrentPosition() > 0.5 * CATAPULT_FLOWER_POS) {
                if (set_P < 0.15) {
                    set_P = 0.18;
                }
            }
            if (catapult_left.getCurrentPosition() > 0.82 * CATAPULT_FLOWER_POS) {
                if (set_P < 0.25) {
                    set_P = 0.32;
                }
            }
        }

        catapult_left.setPositionPIDFCoefficients(set_P);
        catapult_right.setPositionPIDFCoefficients(set_P);

        catapult_left.setTargetPositionTolerance(setPosTolerance);
        catapult_right.setTargetPositionTolerance(setPosTolerance);

        catapult_left.setTargetPosition(setPosition);
        catapult_right.setTargetPosition(setPosition);

        catapult_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        catapult_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public double getCatapultLPos(){
        return catapult_left.getCurrentPosition();
     }
    public double getCatapultRPos(){
        return catapult_right.getCurrentPosition();
    }
}

