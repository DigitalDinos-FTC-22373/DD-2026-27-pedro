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
    private final Pose shootToGardenControlPose = p.of(50, 10, 0);
    private final Pose gardenToShootControlPose = p.of(20, 20, 180);
    private final Pose flowerPose = p.of(13, 52, 180);

    private final Pose flowerControlPose = p.of(25, 56, 0);
    private final Pose parkControlPose = p.of(8, 48, 0);
    private final Pose parkPose = p.of(10, 93, 0);
    private Path StartToShoot() {
        return line(startPose, shootPose).linear(startPose, shootPose);
    }
    private Path ShootToGarden() {
        return curve(shootPose, shootToGardenControlPose, gardenPose).constant(gardenPose);
    }
    private Path GardenToShoot() {
        return curve(gardenPose, gardenToShootControlPose, shootPose).linear(gardenPose, shootPose);
    }
    private Path ShootToFlower() {
        return curve(shootPose, flowerControlPose, flowerPose).constant(flowerPose);
    }
    private Path FlowerToPark() {
        return curve(flowerPose, parkControlPose, parkPose).linear(flowerPose, parkPose);
    }

    private Path ShootToPark(){
        return curve(shootPose, parkControlPose, parkPose).linear(shootPose,parkPose);
    }

    private Path FlowerToShoot(){
        return line(flowerPose,shootPose).linear(flowerPose,shootPose);
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
                //Move from start and shoot
                follow(follower, StartToShoot()),
                shoot,

//                //Go to garden, pick up pollen and shoot
//                instant(() -> {bbb.intakeOn();}),
//
//                instant(() -> {bbb.intakeOn();}),
//                race(follow(follower, ShootToGarden()),
//                        waitMs(4000)),
//                waitMs(3000),
//                race(follow(follower, GardenToShoot()),
//                        waitMs(3000)),
//                shoot,
//
//                //Go to flower, pick up pollen and shoot
//                instant(() -> {bbb.intakeOn();}),
//                instant(() -> {bbb.arm_down();}),
//                waitMs(500),
//                race(follow(follower, ShootToFlower()),
//                        waitMs(3000)),
//                waitMs(3000),
//                race(follow(follower, FlowerToShoot()),
//                        waitMs(2000)),
//
//                instant(() -> {bbb.arm_up();}),
//                shoot,

                //Park
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