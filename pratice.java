package org.firstinspires.ftc.teamcode;

import android.os.DropBoxManager;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class pratice extends OpMode{
    newlarning newlearning = new newlarning(0);

     @Override
    public void init(){
         newlearning.setAngle(0);
         newlearning.setX(0);
         newlearning.Sety(0);

     }
     @Override
     public void loop(){
            if (gamepad1.a){
                newlearning.turnrobot(1);
            }
            else if (gamepad1.b){
                newlearning.turnrobot(-1);
            }




            if (gamepad1.dpad_left){
                newlearning.changeX(1);
            }
            else if (gamepad1.dpad_right){
                newlearning.changeX(-1);
            }

            if (gamepad1.dpad_up){
                newlearning.changY(1);
            }
            else if (gamepad1.dpad_down){
                newlearning.changY(-1);
            }

         telemetry.addData("Heading", newlearning.getheading());
            telemetry.addDate("X", newlearning.getx());
            telemetry.addData("Y", newlearning.gety());

     }
}




