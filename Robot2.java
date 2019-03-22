import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class Robot2 {
	EV3MediumRegulatedMotor motorArm;
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
	EV3ColorSensor colorSensorLeft;
	EV3ColorSensor colorSensorRight;
	EV3ColorSensor colorSensorSide;
	String[] orientations;

	Robot2() {
		this.motorArm = new EV3MediumRegulatedMotor(MotorPort.A);
		this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
		this.motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
		this.colorSensorLeft = new EV3ColorSensor(SensorPort.S1);
		this.colorSensorRight = new EV3ColorSensor(SensorPort.S2);
		this.colorSensorSide = new EV3ColorSensor(SensorPort.S4);
		this.orientations = new String[] { "", "", "", "" }; // index: 0: east, 1: south, 2: west, 3: north
	}

	public static void main(String[] args) {
		Robot2 robot = new Robot2();
		robot.colorSensorSide.setFloodlight(Color.WHITE);
		LCD.drawString("running, press enter...", 0, 0);
		Button.ENTER.waitForPress();
		/* 1. fahre bis zum ersten Block
		 * 2. lies Farbe des ersten Blocks
		 * 3. fahre zum zweiten Block
		 * 4. lies Farbe des zweiten Blocks ...
		 */
		LCD.clear();
		robot.drive(5000);
		robot.orientations[0] = robot.getColorName(robot.colorSensorSide.getColorID());
		//robot.findOrientations();
		LCD.drawString("East " + robot.orientations[0], 0, 0);
		robot.drive(3000);
		robot.orientations[1] = robot.getColorName(robot.colorSensorSide.getColorID());
		//robot.findOrientations();
		LCD.drawString("South " + robot.orientations[1], 0, 1);
		robot.drive(3000);
		robot.orientations[2] = robot.getColorName(robot.colorSensorSide.getColorID());
		//robot.findOrientations();
		LCD.drawString("West " + robot.orientations[2], 0, 2);
		robot.drive(3000);
	
		robot.orientations[3] = robot.getColorName(robot.colorSensorSide.getColorID());
		//robot.findOrientations();
		LCD.drawString("North " + robot.orientations[3], 0, 3);
		LCD.drawString("Turn Red:  " + robot.toTurn("RED"), 0, 4);
		LCD.drawString("Turn Green:  " + robot.toTurn("GREEN"), 0, 5);
		LCD.drawString("Turn Blue:  " + robot.toTurn("BLUE"), 0, 6);
		LCD.drawString("Turn Yellow:  " + robot.toTurn("YELLOW"), 0, 7);
		Button.ENTER.waitForPress();
	}
	
	public void drive(int time) {
		motorLeft.setSpeed(100);
		motorRight.setSpeed(100);
		motorRight.forward();
		motorLeft.forward();
		Delay.msDelay(time);
		stop();
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
	
	public void stop() {
		motorRight.stop(true);
		motorLeft.stop(true);
	}

	public int getColorId(String color) {
		int id = -1;
		switch (color) {
		case "RED":
			id = Color.RED;
			break;
		case "GREEN":
			id = Color.GREEN;
			break;
		case "BLUE":
			id = Color.BLUE;
			break;
		case "YELLOW":
			id = Color.YELLOW;
			break;
		}
		return id;
	}

	public String getColorName(int id) {
		String name = "";
		switch (id) {
		case Color.RED:
			name = "RED";
			break;
		case Color.GREEN:
			name = "GREEN";
			break;
		case Color.BLUE:
			name = "BLUE";
			break;
		case Color.YELLOW:
			name = "YELLOW";
			break;
		}
		return name;
	}

	public void findOrientations() {
		int color = colorSensorSide.getColorID();
		if (color != Color.NONE) {
			if (orientations[0] == "") {
				orientations[0] = getColorName(color);
			} else if (color != getColorId(orientations[0])) {
				if (orientations[1] == "") {
					orientations[1] = getColorName(color);
				} else if (color != getColorId(orientations[1])) {
					if (orientations[2] == "") {
						orientations[2] = getColorName(color);
					} else if (color != getColorId(orientations[2])) {
						if (orientations[3] == "") {
							orientations[3] = getColorName(color);
						}
					}
				}
			}
		}
	}

	public boolean toTurn(String color) {
		int colorIndex = 0;
		int orientationIndex = 0;
		for (int i = 0; i < 4; i++) {
			if (color == this.orientations[i]) {
				orientationIndex = i;
			}
		}
		switch (color) {
		case "RED":
			colorIndex = 0;
			break;
		case "GREEN":
			colorIndex = 1;
			break;
		case "BLUE":
			colorIndex = 2;
			break;
		case "YELLOW":
			colorIndex = 3;
			break;
		}
		// first index: orientation East, South, West, North
		// second index: color red, green, blue, yellow
		boolean[][] turnmatrix = { { true, false, true, false }, { true, true, false, false }, { false, true, false, true }, { false, false, true, true } };
		return turnmatrix[orientationIndex][colorIndex];
	}
}