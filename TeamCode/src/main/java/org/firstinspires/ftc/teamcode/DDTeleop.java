package org.firstinspires.ftc.teamcode;
import com.pedropathing.controllers.PIDController;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.seattlesolvers.solverslib.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class DDTeleop extends OpMode {
    //CONSTANTS
    private static Pose2d NORTH_HIVE_POS = new Pose2d(59,94,0);
    private static Pose2d SOUTH_HIVE_POS = new Pose2d(59,50,0);
    private Follower follower;
    private BlueBarneyBot bbb;
    private final PIDController headingController = new PIDController(2.0, 0.0, 0.1);

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        bbb = new BlueBarneyBot();
        bbb.init(hardwareMap);
        bbb.arm_up();
    }

    @Override
    public void init_loop(){
        bbb.catapultUpdate();
    }


    @Override
    public void start(){
        bbb.start();
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.update();
    }


    @Override
    public void loop() {
        Pose2d targetHivePos;
        if (follower.pose().y() < 72) {
            targetHivePos = SOUTH_HIVE_POS;
        } else {
            targetHivePos = NORTH_HIVE_POS;
        }
        double targetHeading = Math.atan2(targetHivePos.getY() - follower.pose().y(),
                targetHivePos.getX() - follower.pose().x()) + Math.PI;
        if (Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y) > 0.4) {
            targetHeading = Math.atan2(-gamepad1.right_stick_x, -gamepad1.right_stick_y);
        }

        DrivePowers powers = new DrivePowers(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x);

        powers = ManualDrive.headingLock(follower, headingController, powers, targetHeading);
        powers = ManualDrive.fieldCentric(
                powers,
                follower.pose().heading()
        );

        follower.manual(powers);
        follower.update();

        if (gamepad1.aWasPressed()) {bbb.shoot();}
        if (gamepad1.aWasReleased()) {bbb.lower();}

        if (gamepad1.bWasPressed()) {bbb.block();}
        if (gamepad1.bWasReleased()) {bbb.lower();}

        if (gamepad1.xWasPressed()) {bbb.flower();}
        if (gamepad1.xWasReleased()) {bbb.lower();}

        if (gamepad1.dpadDownWasPressed()) {bbb.intakeOn();}
        if (gamepad1.dpadUpWasPressed()) {bbb.intakeOut();}
        if (gamepad1.dpadRightWasPressed()) {bbb.intakeOff();}
        if (gamepad1.dpadLeftWasPressed()) {bbb.intakeOff();}

        if (gamepad1.rightBumperWasPressed()){
           bbb.adjustCatapultpos(5);
        }
        if (gamepad1.leftBumperWasPressed()){
            bbb.adjustCatapultpos(-5);
        }

        if(gamepad1.backWasPressed()){
            follower.setHeading(0);
        }

        if (gamepad1.yWasPressed()){
            bbb.toggle_arm();
        }



        bbb.catapultUpdate();

        telemetry.addData("catapult Left Position", bbb.getCatapultLPos());
        telemetry.addData("catapult Right Position", bbb.getCatapultRPos());
        telemetry.update();
    }
}
