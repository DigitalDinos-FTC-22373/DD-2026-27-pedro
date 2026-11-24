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

    private static Pose2d NORTH_HIVE_POS_OPP = new Pose2d(85,94,0);
    private static Pose2d SOUTH_HIVE_POS_OPP = new Pose2d(85,50,0);
    private Follower follower;
    private BlueBarneyBot bbb;

    double locked_heading;
    private boolean heading_locked;
    private boolean auto_aim;
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

        if (bbb.isBlocking()) {
            if (follower.pose().y() < 72) {
                targetHivePos = SOUTH_HIVE_POS_OPP;
            } else {
                targetHivePos = NORTH_HIVE_POS_OPP;
            }
            targetHeading = Math.atan2(targetHivePos.getY() - follower.pose().y(),
                    targetHivePos.getX() - follower.pose().x());

        }

        if (Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y) > 0.4) {
            targetHeading = Math.atan2(-gamepad1.right_stick_x, -gamepad1.right_stick_y);
        }

        if (heading_locked){
            targetHeading = locked_heading;
        }

        DrivePowers powers = new DrivePowers(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x);

        if (auto_aim) {
            powers = ManualDrive.headingLock(follower, headingController, powers, targetHeading);
        }
        powers = ManualDrive.fieldCentric(
                powers,
                follower.pose().heading()
        );

        follower.manual(powers);
        follower.update();

        if (gamepad2.right_stick_y > 0.5){
            bbb.shooterPoweradjust(-0.01);
        }
        if (gamepad2.right_stick_y < -0.5 ){
            bbb.shooterPoweradjust(0.01);
        }

        if (gamepad1.aWasPressed()) {bbb.shoot();}
        if (gamepad1.aWasReleased()) {bbb.lower();}

        if (gamepad1.bWasPressed()) {bbb.block();}
        if (gamepad1.bWasReleased()) {bbb.lower();}

        if (gamepad2.bWasPressed()) {auto_aim = !auto_aim;};

        if (gamepad1.xWasPressed() || gamepad2.xWasPressed()) {bbb.toggleflower();}

        if (gamepad1.dpadDownWasPressed() || gamepad2.dpadDownWasPressed()) {bbb.intakeOn();}
        if (gamepad1.dpadUpWasPressed() || gamepad2.dpadUpWasPressed()) {bbb.intakeOut();}
        if (gamepad1.dpadRightWasPressed() || gamepad2.dpadRightWasPressed()) {bbb.intakeOff();}
        if (gamepad1.dpadLeftWasPressed() || gamepad2.dpadLeftWasPressed()) {bbb.intakeOff();}

        if (gamepad1.right_bumper || gamepad2.right_bumper){bbb.adjustCatapultpos(2);}
        if (gamepad1.left_bumper || gamepad2.left_bumper){bbb.adjustCatapultpos(-2);}

        if(gamepad1.backWasPressed() || gamepad2.backWasPressed()){follower.setHeading(0);}

        if (gamepad1.yWasPressed() || gamepad2.yWasPressed()){bbb.toggle_arm();}

        if (gamepad1.leftTriggerWasPressed()){
            locked_heading = follower.pose().heading();
            heading_locked = !heading_locked;
        }

        if (gamepad1.startWasPressed() || gamepad2.startWasPressed()){
            bbb.resetCatapultEncoders();
        }

        bbb.catapultUpdate();

        telemetry.addData("catapult Left Position", bbb.getCatapultLPos());
        telemetry.addData("catapult Right Position", bbb.getCatapultRPos());
        telemetry.addData("Follower X", follower.pose().x());
        telemetry.addData("Follower Y", follower.pose().y());
        telemetry.addData("Heading", follower.pose().heading());
        telemetry.addData("Heading locked", heading_locked);
        telemetry.addData("shooter Power",bbb.getShooterPow());
        telemetry.update();
    }
}
