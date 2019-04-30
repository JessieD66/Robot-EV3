import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class Hochheben {
	EV3MediumRegulatedMotor motorGreif;
	EV3MediumRegulatedMotor motorDrehe;
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
    
	Hochheben()
	{
    	this.motorGreif = new EV3MediumRegulatedMotor(MotorPort.A);
    	this.motorDrehe = new EV3MediumRegulatedMotor(MotorPort.D);
    	this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
    	this.motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
    	}
    
	public static void main(String[] args) {
    	Hochheben robot = new Hochheben();
    	LCD.drawString("running, press enter...", 0, 0);
    	Button.ENTER.waitForPress();
    	robot.RouterHochheben();
    	robot.RouterDrehen(100, 90);
    	robot.RouterAbsetzen();
   	 
	}
    
	public void RouterDrehen(int speed, int angle)
	{
   	 
    	motorDrehe.setSpeed(speed);
    	motorDrehe.rotate(angle);
    	Delay.msDelay(600);
    	motorLeft.setSpeed(100);
    	motorRight.setSpeed(100);
    	motorRight.rotate(200, true);
    	motorLeft.rotate(200);
   	 
	}
	public void RouterHochheben()
	{
   	 
    	motorGreif.setSpeed(100);
    	motorGreif.rotate(90);
    	Delay.msDelay(400);
    	motorLeft.setSpeed(100);
    	motorRight.setSpeed(100);
    	motorRight.rotate(200, true);
    	motorLeft.rotate(200);
    	Delay.msDelay(400);
    	motorGreif.rotate(-50);
	}
	public void RouterAbsetzen()
	{
   	 
    	motorGreif.setSpeed(100);
    	motorGreif.rotate(50);
    	Delay.msDelay(200);
    	motorRight.rotate(-50);
    	motorGreif.rotate(-90);
	}
}
