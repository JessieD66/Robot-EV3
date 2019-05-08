import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.utility.Delay;

public class DistanceTester {
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
	
	DistanceTester() {
		this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
		this.motorRight = new EV3LargeRegulatedMotor(MotorPort.B);
	}

	public static void main(String[] args) {
		DistanceTester robot = new DistanceTester();
		LCD.drawString("Press enter!", 0, 0);
		Button.ENTER.waitForPress();
		LCD.clear();
		long start = System.currentTimeMillis();
		robot.drive(200); //first parameter: speed
		Button.ENTER.waitForPress();
		robot.stop();
		int time = (int)(System.currentTimeMillis()-start);
		LCD.drawInt(time, 0, 0);
		Button.ENTER.waitForPress();

	}
	
	public void drive(int speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		motorLeft.forward();
		motorRight.forward();
	}
	
	public void stop() {
		motorLeft.stop(true);
		motorRight.stop(true);
	}

}