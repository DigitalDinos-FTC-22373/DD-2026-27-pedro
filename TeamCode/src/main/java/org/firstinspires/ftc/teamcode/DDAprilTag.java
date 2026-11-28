

package org.firstinspires.ftc.teamcode;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;
import java.util.concurrent.TimeUnit;


//@Disabled
public class DDAprilTag{
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    // Declare OpMode members for each of the 4 motors.

    public void initAprilTag(HardwareMap hardwareMap) {


        // 1. Define the camera's physical offset from the center of your robot
        Position cameraPosition = new Position(
                DistanceUnit.INCH,
                -0.75,  // X: Right (+) / Left (-) offset from robot center
                7.5,  // Y: Forward (+) / Backward (-) offset from robot center
                13,  // Z: Up (+) / Down (-) height from the ground
                0     // Acquisition time (leave as 0)
        );

        // 2. Define the camera's physical rotation
        YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(
                AngleUnit.DEGREES,
                0.0,   // Yaw: Counter-clockwise (+) / Clockwise (-) turn
                0.0,   // Pitch: Tilted up (+) / Tilted down (-)
                180.0, // Roll: 180 for upside down, 90 or -90 for sideways
                0      // Acquisition time (leave as 0)
        );

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                .setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
            builder.setCameraResolution(new Size(1920, 1080));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }


        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()


    /**
     * Add telemetry about AprilTag detections.
     */
    public void detectionAprilTag(Telemetry telemetry) {

        // Get list of detections from AprilTag Processor
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            // Check if the object is an AprilTagSingleDetection or AprilTagClusterDetection
            if ( detection instanceof AprilTagClusterDetection)
            {
                // Create local copy of detection but casting to AprilTagSingleDetection type
                // so that we can properly interpret the metadata and id information.
                AprilTagClusterDetection det = (AprilTagClusterDetection) detection;
                // Now that we have singleDet as an AprilTagSingleDetection object, we can
                // read the metadata and id information from it.
                if (det.metadata != null) {
                    // Notice this telemetry is using singleDet to get metadata and id.
                    telemetry.addLine(String.format("\n==== (PCT %d) %s", det.percentClusterFound, det.metadata.name));
                    // Notice we can use the raw detection variable or our new singleDet
                    // variable to get Pose information because the ftcPose is
                    // inherited from the superclass and so it lives in both places.
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                }
            }
        }
        telemetry.update();
    }   // end method telemetryAprilTag()

    public boolean hiveIsUp(boolean garden_side) {
        if (garden_side) {
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                // Check if the object is an AprilTagSingleDetection or AprilTagClusterDetection
                if (detection instanceof AprilTagClusterDetection) {
                    AprilTagClusterDetection det = (AprilTagClusterDetection) detection;
                }
            }
        }
        return false;
    }


    /*
           Manually set the camera gain and exposure.
           Can only be called AFTER calling initAprilTag();
           Returns true if controls are set.
        */
    public boolean setManualExposure(int exposureMS, int gain, Telemetry telemetry) {
        // Ensure Vision Portal has been set up.
        if (visionPortal == null) {
            telemetry.addData("Camera", "No visionPortal");
            telemetry.update();
            return false;
        }

        // Wait for the camera to be open
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            return false;
        }
        else{
            telemetry.addData("Camera", "streaming");
            telemetry.update();
        }

        // Set exposure.  Make sure we are in Manual Mode for these values to take effect.
        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
            telemetry.addData("Camera", "Waiting for manual exposure");
            telemetry.update();
            return false;
        }
        exposureControl.setExposure((long) exposureMS, TimeUnit.MILLISECONDS);
        // Set Gain.
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
        telemetry.addData("Camera", "READY");
        telemetry.update();
        return true;
    }
}


// hello