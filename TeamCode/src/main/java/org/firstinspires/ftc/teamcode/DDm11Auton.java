package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class DDm11Auton extends OpMode {
    private Follower follower;
    private BlueBarneyBot bbb;
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(8.6, 83, 90);
    private final Pose shootPose = p.of(59, 124, 90);
    private final Pose flowerPose = p.of(48, 131.5, 90);
    private final Pose flowerControlPose = p.of(59, 100, 0);
    private final Pose parkControlPose = p.of(50, 97, 0);
    private final Pose parkPose = p.of(10, 96, 90);
    private Path StartToShoot() {
        return line(startPose, shootPose).linear(startPose, shootPose);
    }
    private Path ShootToFlower() {
        return curve(shootPose, flowerControlPose, flowerPose).linear(shootPose, flowerPose);
    }

    private Path FlowerToShoot() {
        return curve(flowerPose, flowerControlPose, shootPose).linear(flowerPose, shootPose);
    }
    private Path ShootToPark() {
        return curve(shootPose, parkControlPose, parkPose).linear(shootPose, parkPose);
    }

    private Path StartToPark(){
        return curve(startPose, parkPose).linear(startPose,parkPose);
    }

    Command shoot = sequential(
            instant(() -> {bbb.intakeOff();}),
            waitMs(500),
            instant(() -> {bbb.shoot();}),
            waitMs(1000),
            instant(() -> {bbb.lower();})
    );

    private Command autoRoutine() {
        return sequential(
//                follow(follower, StartToShoot()),
//                shoot,
//                instant(() -> {bbb.intakeOn();}),
//                instant(() -> {bbb.arm_down();}),
//                race(follow(follower, ShootToFlower()),
//                        waitMs(3000)),
//                waitMs(3000),
//                instant(() -> {bbb.intakeOff();}),
//                race(follow(follower, FlowerToShoot()),
//                        waitMs(3000)),
//                instant(() -> {bbb.arm_up();}),
//                shoot,
//                follow(follower, ShootToPark())
                follow(follower,ShootToPark())
        );
    }

    @Override
    public void init() {
        bbb = new BlueBarneyBot();
        bbb.init(hardwareMap);
        bbb.arm_up();
        bbb.resetCatapultEncoders();
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
        telemetry.addLine("Init Complete. Good Luck Drivers!!");
        telemetry.update();
    }

    @Override
    public void init_loop(){
        bbb.catapultUpdate();
    }

    @Override
    public void start() {schedule(autoRoutine());}

    @Override
    public void loop() {
        follower.update();
        bbb.catapultUpdate();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }

    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}