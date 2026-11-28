package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
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
        c.xPodOffset.set(4.207987147053396);
        c.yPodOffset.set(-3.7308706448772764);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.34323177555310075);
                Controller secondaryTranslationalForward = Controller.proportional(0.1268149697449173);
                Controller primaryTranslationalLateral = Controller.proportional(0.5253364013127495);
                Controller secondaryTranslationalLateral = Controller.proportional(0.19409776303789017);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.020155985000063024));
                c.brake.set(Controller.proportionalFeedforward(0.01713258725005357));

                c.headingFeedback.set(Controller.proportional(4.833101451556971));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04011421526251482, 0.011482849237814115));

                c.linearBrakeCoefficients.set(Matrix.diag(0.09777011450286777, 0.06433031413178993));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.99731544088856E-4, 0.0017360397717952093));

                c.maxAchievableForwardVelocity.set(55.77244840971023);
                c.maxAchievableStrafeVelocity.set(39.78733193631518);
                c.naturalForwardDeceleration.set(47.11117799010889);
                c.naturalStrafeDeceleration.set(79.19995360274979);
            }
    );
}