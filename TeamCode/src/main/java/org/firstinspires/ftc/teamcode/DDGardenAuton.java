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
public class DDGardenAuton extends OpMode {
    private Follower follower;
    private BlueBarneyBot bbb;
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(59, 8, 270);
    private final Pose shootPose = p.of(59, 20, 270);
    private final Pose gardenPose = p.of(10, 10, 180);
    private final Pose gardenControlPose = p.of(64, 23, 0);
    private final Pose flowerPose = p.of(13, 55, 180);
    private final Pose flowerControlPose = p.of(25, 56, 0);
    private final Pose parkControlPose = p.of(8, 48, 0);
    private final Pose parkPose = p.of(10, 93, 0);
    private Path StartToShoot() {
        return line(startPose, shootPose).linear(startPose, shootPose);
    }
    private Path ShootToGarden() {
        return curve(shootPose, gardenControlPose, gardenPose).linear(shootPose, gardenPose);
    }
    private Path GardenToShoot() {
        return curve(gardenPose, gardenControlPose, startPose).linear(gardenPose, startPose);
    }
    private Path ShootToFlower() {
        return curve(shootPose, flowerControlPose, flowerPose).linear(shootPose, flowerPose);
    }
    private Path FlowerToPark() {
        return curve(flowerPose, parkControlPose, parkPose).linear(flowerPose, parkPose);
    }

    private Path ShootToPark(){
        return curve(shootPose, parkControlPose, parkPose).linear(shootPose,parkPose);
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
                instant(() -> {bbb.intakeOn();}),
                shoot,
                instant(() -> {bbb.intakeOn();}),
                follow(follower, StartToShoot()),
                follow(follower,ShootToPark())
//                race(follow(follower, ShootToGarden()),
//                        waitMs(3000)),
//                waitMs(2000),
//                race(follow(follower, GardenToShoot()),
//                        waitMs(3000)),
//                shoot
////                instant(() -> {bbb.intakeOn();}),
////                instant(() -> {bbb.arm_down();}),
////                race(follow(follower, ShootToFlower()),
////                        waitMs(3000)),
////                waitMs(3000),
////                instant(() -> {bbb.arm_up();}),
////                instant(() -> {bbb.intakeOff();}),
////                follow(follower, FlowerToPark())
        );
    }

    @Override
    public void init() {
        bbb = new BlueBarneyBot();
        bbb.init(hardwareMap);
        bbb.arm_up();
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
        telemetry.addLine("Init Complete. Good Luck Drivers!!");
        telemetry.update();
    }

    @Override
    public void start() {schedule(autoRoutine());}

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
        bbb.catapultUpdate();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }

    @Override
    public void stop() {OpModeStorage.autonomousEndPose = follower.pose();}
}