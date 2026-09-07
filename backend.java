package org.firstinspires.ftc.robotcontroller.internal;


import android.graphics.Canvas;
import android.util.Size;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;


public class backend {
    private DcMotor frontleftmotor;
    private DcMotor frontrightmotor;
    private DcMotor backrightmotor;


    private DcMotor backleftmotor;
    private CRServo crservo;
    private CRServo crservo1;
    private CRServo crservo2;
    private CRServo crservo3;
    private CRServo crservo4;
    public Servo servo;
   public DistanceSensor distancesensor;

    private ColorSensor colorsensor;
    public DcMotorEx flywheel;
    private IMU imu;
    private DigitalChannel touchsensor;
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> detectedtags = new ArrayList<>();
    private Telemetry telemetry;
    public void init(HardwareMap hw, Telemetry telemetry){
        this.telemetry = telemetry;
        crservo = hw.get(CRServo.class, "crservo");
        crservo1 = hw.get(CRServo.class, "crservo1");
        crservo2 = hw.get(CRServo.class, "crservo2");
        crservo3 = hw.get(CRServo.class, "crservo3");
        crservo4 = hw.get(CRServo.class, "crservo4");

        servo = hw.get(Servo.class, "servo");
        frontleftmotor = hw.get(DcMotor.class, "frontleftmotor");
        frontleftmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontrightmotor = hw.get(DcMotor.class, "frontrightmotor");
        frontrightmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleftmotor = hw.get(DcMotor.class, "backleftmotor");
        backleftmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       backrightmotor = hw.get(DcMotor.class, "backrightmotor");
        backrightmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        distancesensor = hw.get(DistanceSensor.class, "distancesensor");

        touchsensor = hw.get(DigitalChannel.class, "touchsensor");
        touchsensor.setMode(DigitalChannel.Mode.INPUT);
        imu = hw.get(IMU.class, "imu");
        RevHubOrientationOnRobot position = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
        );
        aprilTagProcessor = hw.get(AprilTagProcessor.class, "apriltagprocessor");
        visionPortal = hw.get(VisionPortal.class,"visionprotal");
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();
        VisionPortal.Builder bench = new VisionPortal.Builder();
        bench.setCamera(hw.get(WebcamName.class, "webcam1"));
        bench.setCameraResolution(new Size(640,360));
        bench.addProcessor(aprilTagProcessor);
        visionPortal = bench.build();




        colorsensor =hw.get(ColorSensor.class,"colorsensor");
        flywheel = hw.get(DcMotorEx.class, "flywheel");
      flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(0,0,0,0);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);



    }

    public void setspeed(double strafe, double forward, double rotate){
        double fl = forward + strafe + rotate;
        double bl  = forward - strafe + rotate;
        double fr  = forward - strafe - rotate;
        double br = forward + strafe - rotate;
        double maxspeed = 1.0;
        double maxpower = 1.0;
        maxpower = Math.max(maxpower, fl);
        maxpower = Math.max(maxpower, bl);
        maxpower = Math.max(maxpower, fr);
        maxpower = Math.max(maxpower, br);
        frontleftmotor.setPower(maxspeed * (fl / maxpower));
        frontrightmotor.setPower(maxspeed * (fr / maxpower));
        backrightmotor.setPower(maxspeed * (br / maxpower));
        backleftmotor.setPower(maxspeed * (bl / maxpower));





    }
    public void fieldrelative(double strafe, double rotate, double forward){
        double theta = Math.atan2(forward,strafe);
        double r = Math.hypot(forward,strafe);
        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        double newforward = r * Math.sin(theta);
        double newstrafe = r * Math.cos(theta);
        this.setspeed(newforward,newstrafe,rotate);
    }
     public boolean ispressed(){
        return !touchsensor.getState();
     }
     public double curdistance(){
        return distancesensor.getDistance(DistanceUnit.CM);
     }

     public void update(){
        detectedtags = aprilTagProcessor.getDetections();
     }
    public List<AprilTagDetection> curtags(){
        return detectedtags;
    }
     public AprilTagDetection gettags(int id){
        for(AprilTagDetection detection : detectedtags){
            if( detection.id == id ){
                return detection;
            }

        }
        return null;
     }
     public void stop(){
        if(visionPortal != null){
            visionPortal.close();
        }
     }
     public void setup(double speed){
         crservo.setPower(speed);
     }
    public void setup1(double speed1){
        crservo1.setPower(speed1);
    }
    public void setup2(double speed2){
        crservo2.setPower(speed2);
    }
    public void setup3(double speed3){
        crservo3.setPower(speed3);
    }
    public void setup4(double speed4){
        crservo4.setPower(speed4);
    }
    public void position1(double position){
        servo.setPosition(position);
    }

}
