package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null;
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("DriveFL");
        c.frontRightName.set("DriveFR");
        c.backLeftName.set("DriveBL");
        c.backRightName.set("DriveBR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.manualBrakeMode.set(true);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("Pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(4.417217885415385);
        c.yPodOffset.set(-3.7413193109467278);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.28147935271184127);
                Controller secondaryTranslationalForward = Controller.proportional(0.10399909956019975);
                Controller primaryTranslationalLateral = Controller.proportional(0.43118000552641295);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1593094907000703);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.020171198504676326));
                c.brake.set(Controller.proportionalFeedforward(0.017145518728974875));

                c.headingFeedback.set(Controller.proportional(3.2140609257802275));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.011953821554098623, 0.017969049960442594));

                c.linearBrakeCoefficients.set(Matrix.diag(0.024697683185217246, 0.029989491152184643));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.003761753450868197, 0.003326456562562127));

                c.maxAchievableForwardVelocity.set(52.52037203889897);
                c.maxAchievableStrafeVelocity.set(42.4789769330869);
                c.naturalForwardDeceleration.set(52.52074337298585);
                c.naturalStrafeDeceleration.set(72.9083915693218);
            }
    );
}