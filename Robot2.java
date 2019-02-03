import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;

public class Robot2 {
	EV3MediumRegulatedMotor motorArm;
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
	EV3ColorSensor colorSensorLeft;
	EV3ColorSensor colorSensorRight;
	EV3ColorSensor colorSensorFront;
	EV3ColorSensor colorSensorSide;
	static int[] orientations = { 0, 0, 0, 0 }; // 0: east, 1: south, 2: west, 3: north

	Robot2() {
		this.motorArm = new EV3MediumRegulatedMotor(MotorPort.A);
		this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
		this.motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
		this.colorSensorLeft = new EV3ColorSensor(SensorPort.S1);
		this.colorSensorRight = new EV3ColorSensor(SensorPort.S2);
		this.colorSensorFront = new EV3ColorSensor(SensorPort.S3);
		this.colorSensorSide = new EV3ColorSensor(SensorPort.S4);
	}

	public static void main(String[] args) {
		Robot2 robot = new Robot2();
		LCD.drawString("running", 0, 0);
		Button.ENTER.waitForPress();
		while (orientations[3] == 0) {
			robot.followLine();
			robot.findOrientations();
		}
		LCD.drawString( "East " + orientations[0], 0, 1);
		LCD.drawString( "South " + orientations[1], 0, 2);
		LCD.drawString( "West " + orientations[2], 0, 3);
		LCD.drawString( "North " + orientations[3], 0, 4);
	}

	public void followLine() {
		motorLeft.setSpeed(100);
		motorRight.setSpeed(100);
		if (colorSensorLeft.getColorID() == Color.BLACK) {
			motorLeft.backward();
			motorRight.forward();
		} else if (colorSensorRight.getColorID() == Color.BLACK) {
			motorRight.backward();
			motorLeft.forward();
		} else {
			motorRight.forward();
			motorLeft.forward();
		}

	}

	public void findOrientations() {
		int color = colorSensorSide.getColorID();
		if (orientations[0] == 0 && color != Color.NONE  ) {
			orientations[0] = color;
		} else if (orientations[1] == 0 && color != Color.NONE && color != orientations[0]) {
			orientations[1] = color;
		} else if (orientations[2] == 0 && color != Color.NONE && color != orientations[0] && color != orientations[1]) {
			orientations[2] = color;
		} else if (orientations[3] == 0 && color != Color.NONE && color != orientations[0] && color != orientations[1] && color != orientations[2]) {
			orientations[3] = color;
		}
	}
}