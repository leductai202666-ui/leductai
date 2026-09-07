package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled

@TeleOp

public class overall extends OpMode {
    double[] stepsize = { 10.0, 1.0, 0.1,0.001};
    double target = 0;
    backend bench1 = new backend();
     double P = 0;
     double F = 0;
     double P1 = P;
     double ballcount = 0;
     boolean ball1 = bench1.distancesensor.getDistance(DistanceUnit.CM) < 5;
    boolean ball2 = bench1.distancesensor.getDistance(DistanceUnit.CM) < 5;
    boolean ball3 = bench1.distancesensor.getDistance(DistanceUnit.CM) < 5;
    int ballCount = 0;
    boolean lastBallState = false;
     double F1 = F;
     double target1 = target;


    int step = 1;
    ElapsedTime timer = new ElapsedTime();

    public void init(){
       bench1.init(hardwareMap, telemetry);
    }
    public void loop(){
        boolean pidfchanged = false;
        boolean velocitychanged = false;
        double forward = gamepad1.left_stick_y;
        double strafe  = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;
        bench1.fieldrelative(forward,strafe,rotate);
         if(gamepad1.a) {
             bench1.setup(-1);
             timer.reset();
         }
             if(timer.seconds() > 1.5) {
                 bench1.setup(0);
                 bench1.setup1(-1);
                 bench1.setup2(-1);
                 bench1.setup3(-1);
                 bench1.setup4(-1);
                 timer.reset();
                 bench1.position1(0);
             }
                 if(timer.seconds() > 1){
                     bench1.setup1(0);
                     bench1.setup2(0);
                     bench1.setup3(0);
                     bench1.setup4(0);
                     timer.reset();


                 }
              if(gamepad1.xWasPressed()){
                 target1 -= 100;
                 velocitychanged = true;
              }
              if(gamepad1.yWasPressed()){
                  target1 += 100;
                  velocitychanged = true;
              }
              if(gamepad1.bWasPressed()) {
                  if (velocitychanged) {
                      bench1.flywheel.setVelocity(target1);
                  } else {
                      bench1.flywheel.setVelocity(target);
                  }
              }
              if(gamepad1.right_bumper){
                  bench1.servo.setPosition(90);
                  timer.reset();
                  if(timer.seconds() > 1){
                      bench1.setup1(-1);
                      bench1.setup2(-1);
                      bench1.setup3(-1);
                      bench1.setup4(-1);
                  }
              }
               if (gamepad1.bWasPressed()) {
                   step = (step + 1) % stepsize.length;
               }
               if (gamepad1.dpadLeftWasPressed()) {
                  F1 += stepsize[step];
                  pidfchanged = true;
               }
               if (gamepad1.dpadRightWasPressed()) {
                   F1 -= stepsize[step];
                   pidfchanged = true;
               }
               if (gamepad1.dpadUpWasPressed()) {
                   P1 += stepsize[step];
                   pidfchanged = true;
               }
               if (gamepad1.dpadDownWasPressed()) {
                   P1 -= stepsize[step];
                   pidfchanged = true;
               }
                    if(pidfchanged) {
                        bench1.flywheel.setVelocityPIDFCoefficients(P1, 0, 0, F1);
                    }
                    else{
                        bench1.flywheel.setVelocityPIDFCoefficients(P, 0 , 0 , F);
                    }
        // Biến lưu trạng thái đếm (Khai báo bên ngoài loop)
        // Biến lưu trạng thái đếm (Khai báo bên ngoài loop)

   if(gamepad1.right_bumper) {

       // 1. Đọc khoảng cách từ cảm biến cuối dốc
       double distance = bench1.distancesensor.getDistance(DistanceUnit.CM);

       // Kiểm tra có bóng đi qua (Khoảng cách < 5cm và loại bỏ lỗi 0cm)
       boolean currentBallState = (distance > 1.0 && distance < 5.0);

       // 2. BẮT SƯỜN LÊN (RISING EDGE): Chỉ cộng 1 khi bóng VỪA MỚI CHẠM cảm biến
       if (currentBallState && !lastBallState) {
           ballCount++;
       }

       // Cập nhật trạng thái cho vòng lặp sau
       lastBallState = currentBallState;

       // 3. TỰ ĐỘNG THỰC HIỆN KHI ĐỦ 3 BÓNG
       if (ballCount == 3) {
           bench1.servo.setPosition(90); // Quay Servo góc 90 độ (0.5 = 90 độ)
           bench1.setup1(-1);
           bench1.setup2(-1);
           bench1.setup3(-1);
           bench1.setup4(-1);

       }
       else if( ballcount >= 1 ){
           bench1.servo.setPosition(90); // Quay Servo góc 90 độ (0.5 = 90 độ)
           bench1.setup1(-1);
           bench1.setup2(-1);
           bench1.setup3(-1);
           bench1.setup4(-1);
           timer.reset();


       }
       if(timer.seconds() > 4){
           ballcount = 0;
           bench1.flywheel.setVelocity(0);
           bench1.servo.setPosition(0);
           bench1.setup1(0);
           bench1.setup2(0);
           bench1.setup3(0);
           bench1.setup4(0);
           timer.reset();

       }







       // Hiển thị số bóng lên màn hình Driver Station
       telemetry.addData("Số bóng trong máy", ballCount);


   }

    }
}
