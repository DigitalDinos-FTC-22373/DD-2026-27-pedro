package org.firstinspires.ftc.teamcode;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class DigitalDinos2627Teleop extends OpMode {
    private Follower follower;
    private BlueBarneyBot bbb;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        bbb = new BlueBarneyBot();
        bbb.init(hardwareMap);
    }

    @Override
    public void init_loop(){
        bbb.catapultUpdate();
    }

    @Override
    public void start(){
        bbb.start();
    }

    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
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

        bbb.catapultUpdate();

        telemetry.addData("catapult Left Position", bbb.getCatapultLPos());
        telemetry.addData("catapult Right Position", bbb.getCatapultRPos());
        telemetry.update();
    }
}
