import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class TurnTester {
	EV3MediumRegulatedMotor motorGreif;
	EV3MediumRegulatedMotor motorDrehe;
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
    
	TurnTester()
	{
    	this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
    	this.motorRight = new EV3LargeRegulatedMotor(MotorPort.B);
    	}
    
	public static void main(String[] args) {
    	TurnTester robot = new TurnTester();
    	LCD.drawString("running, press enter...", 0, 0);
    	Button.ENTER.waitForPress();
    	robot.turn('l');
    	Delay.msDelay(2000);
    	robot.turn('b');
    	Delay.msDelay(2000);
    	robot.turn('r');
   	 
	}
	public void turn(char direction) {
		motorLeft.setSpeed(200);
		motorRight.setSpeed(200);
		if (direction == 'l') {
			motorLeft.rotate(-200, true);
			motorRight.rotate(200);
		} else if (direction == 'r') {
			motorLeft.rotate(200, true);
			motorRight.rotate(-200);
		} else if (direction == 'b') {
			motorLeft.rotate(390, true);
			motorRight.rotate(-390);
		}
	}
}