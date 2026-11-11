package org.firstinspires.ftc.teamcode;

//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/**
 * Created by Tom on 9/26/17.  Updated 9/24/2021 for PIDF.
 * This assumes that you are using a REV Robotics Control Hub or REV Robotics Expansion Hub
 * as your DC motor controller.  This OpMode uses the extended/enhanced
 * PIDF-related functions of the DcMotorEx class.
 */

//@Config
@TeleOp
public class Catapult_tuning extends LinearOpMode {

    // our DC motor
    DcMotorEx catapultL;
    DcMotorEx catapultR;
    DcMotorEx intake;
    public double NEW_P = 10.5;
    public double NEW_PP = 0.84;
    public double NEW_I = 0;
    public double NEW_D = -1.7;
    public double NEW_F = 252;

    public double lowering_PP = 0.84;

     public double lowering_F = 252;

     //rainbow basket changes
     public double Higher_F = 400;
     public double Higher_PP = 0.9;
    // These values are for illustration only; they must be set
    // and adjusted for each motor based on its planned usage.
    public double set_angle_degrees = 30;
    public final double TICKS_PER_DEGREE= 537.7/360;
    public boolean IntakeOn = true;
    public int setposition =  (int)(set_angle_degrees * TICKS_PER_DEGREE);

    public void runOpMode() {
        // Get reference to DC motor.
        // Since we are using the Control Hub or Expansion Hub,
        // cast this motor to a DcMotorEx object.
        catapultL = hardwareMap.get(DcMotorEx.class, "catapult_left");
        catapultR = hardwareMap.get(DcMotorEx.class, "catapult_right");

        intake = (DcMotorEx)hardwareMap.get(DcMotor.class, "intake");
//        FtcDashboard dashboard = FtcDashboard.getInstance();
//        telemetry = dashboard.getTelemetry();

        // wait for start command
        waitForStart();
        catapultL.setPower(0.1);
        catapultL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapultL.setDirection(DcMotor.Direction.FORWARD);
        catapultL.setTargetPosition(setposition);
        catapultL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        catapultR.setPower(0.1);
        catapultR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        catapultR.setDirection(DcMotor.Direction.REVERSE); // direrction good
        catapultR.setTargetPosition(setposition);
        catapultR.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        intake.setPower(0);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        // Get the PIDF coefficients for the RUN_TO_POSITION RunMode.
        PIDFCoefficients pidfOrig = catapultL.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);


        // Change coefficients using methods included with DcMotorEx class.
        PIDFCoefficients pidfNew = new PIDFCoefficients(NEW_P, NEW_I, NEW_D, NEW_F);
        catapultL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        catapultR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);


        catapultL.setPositionPIDFCoefficients(NEW_PP);
        catapultR.setPositionPIDFCoefficients(NEW_PP);

        // Re-read coefficients and verify change.
        PIDFCoefficients pidfModified = catapultL.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
        catapultL.setTargetPosition(setposition);
        catapultR.setTargetPosition(setposition);

        // display info to user
        while(opModeIsActive()) {

            if (gamepad1.aWasPressed()) {
                if (set_angle_degrees < -50) {
                    set_angle_degrees = 30;
                    catapultL.setPower(0.1);
                    catapultR.setPower(0.1);
                    NEW_F = lowering_F;
                    NEW_PP = lowering_PP;
                } else if (set_angle_degrees > -50) {
                    catapultL.setPower(1);
                    catapultR.setPower(1);
                    //rainbow basket changes
                    set_angle_degrees = -115;
                    NEW_F = Higher_F;
                    NEW_PP = Higher_PP;

                }

            }

            if (gamepad1.bWasPressed()) {
                if (IntakeOn){
                    intake.setPower(0);
                    IntakeOn = false;
                } else {
                    intake.setPower(1);
                    IntakeOn = true;
                }
            }

            if (gamepad1.left_bumper){
                NEW_F -= 1;
            }
            if (gamepad1.right_bumper){
                NEW_F += 1;
            }

            if (gamepad1.right_trigger > 0.5){
                NEW_PP += 0.01;
            }
            if (gamepad1.left_trigger > 0.5){
                NEW_PP -= 0.01;
            }

            if (gamepad1.dpad_left){
                NEW_P -= 0.01;
            }
            if (gamepad1.dpad_right){
                NEW_P += 0.01;
            }

            if (gamepad1.dpad_up){
                NEW_D += 0.01;
            }
            if (gamepad1.dpad_down){
                NEW_D -= 0.01;
            }


            setposition =  (int)(set_angle_degrees * TICKS_PER_DEGREE);

            pidfNew = new PIDFCoefficients(NEW_P, NEW_I, NEW_D, NEW_F);
            catapultL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

            catapultR.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

            catapultL.setPositionPIDFCoefficients(NEW_PP);
            catapultL.setTargetPosition(setposition);

            catapultR.setPositionPIDFCoefficients(NEW_PP);
            catapultR.setTargetPosition(setposition);

            pidfModified = catapultL.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION);

            telemetry.addData("Runtime (sec)", "%.01f", getRuntime());
            telemetry.addData("P,I,D,F (orig)", "%.04f, %.04f, %.04f, %.04f",
                    pidfOrig.p, pidfOrig.i, pidfOrig.d, pidfOrig.f);
            telemetry.addData("P,I,D,F (modified)", "%.04f, %.04f, %.04f, %.04f",
                    pidfModified.p, pidfModified.i, pidfModified.d, pidfModified.f);
            telemetry.addData("New PP",  NEW_PP);
            telemetry.addData("New F",  NEW_F);
            telemetry.addData("New D",  NEW_D);
            telemetry.addData("New P",  NEW_P);
            telemetry.addData("catapultL position",  catapultL.getCurrentPosition());
            telemetry.addData("catapultR position",  catapultR.getCurrentPosition());
            telemetry.addData("setangledegrees", set_angle_degrees);
            telemetry.update();
        }
    }
}