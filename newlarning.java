package org.firstinspires.ftc.teamcode;





public class newlarning  {
    double angle;
    double x;
    double y;
    public  newlarning(double angle) {
        this.angle = angle;
    }

 public double getheading(){
        double angle = this.angle;
        while(angle > 180){
            angle -= 360;
        }
        while (angle <= -180){
            angle += 360;
        }
        return angle;

 }
  public void turnrobot(double anglechange){
        angle += anglechange;
  }
  public void setAngle(double angle){
        this.angle = angle;
  }

public void changeX ( double Changeamount) {
        this.x += Changeamount;
}
public double getx(){
 return this.x;
}
public void setX(double x){
        this.x = x;
}

public void changY(double amountchange){
        this.y += amountchange;

}
public double gety(){
        return this.y;
}
public void Sety(double y){
        this.y = y;
}
}




