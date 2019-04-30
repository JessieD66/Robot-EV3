import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class Robot2 {
	EV3MediumRegulatedMotor motorUp;
	EV3MediumRegulatedMotor motorTurn;
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
	//EV3ColorSensor colorSensorDown;
	//EV3ColorSensor colorSensorRight;
	EV3ColorSensor colorSensorSide;
	String[] orientations;

	Robot2() {
		this.motorUp = new EV3MediumRegulatedMotor(MotorPort.D);
		this.motorTurn = new EV3MediumRegulatedMotor(MotorPort.A);
		this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
		this.motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
		//this.colorSensorDown = new EV3ColorSensor(SensorPort.S1);
		//this.colorSensorRight = new EV3ColorSensor(SensorPort.S2);
		this.colorSensorSide = new EV3ColorSensor(SensorPort.S1);
		this.orientations = new String[] { "", "", "", "" }; // index: 0: east, 1: south, 2: west, 3: north
	}

	public static void main(String[] args) {
		Robot2 robot = new Robot2();
		LCD.drawString("running, press enter...", 0, 0);
		Button.ENTER.waitForPress();
		LCD.clear();
		robot.drive(200, 0);
		robot.measureOrientations();
		Sound.buzz();
		LCD.drawString("Turn Red:  " + robot.toTurn("RED"), 0, 4);
		LCD.drawString("Turn Green:  " + robot.toTurn("GREEN"), 0, 5);
		LCD.drawString("Turn Blue:  " + robot.toTurn("BLUE"), 0, 6);
		LCD.drawString("Turn Yellow:  " + robot.toTurn("YELLOW"), 0, 7);
		robot.stop();
		robot.turn('l');
		robot.drive(300, 10200);
		robot.stop();
		robot.turn('l');
		robot.drive(200, 1600);
		robot.stop();
		LCD.clear();
		LCD.drawString(robot.routerColour(robot.colorSensorSide.getColorID()), 0, 0);
		Sound.buzz();
		Button.ENTER.waitForPress();
		
	}
	
	public String routerColour(int id) {
		String name = "";
		switch (id) {
		case Color.BLACK:
			name = "BLACK";
			break;
		case Color.WHITE:
			name = "WHITE";
			break;
		default:
			name = "NONE";
			break;
		}
		return name;
	}
	
	
	public void measureOrientations() {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 8100) {
			if (System.currentTimeMillis() - startTime > 3050 && System.currentTimeMillis() - startTime < 3150) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[0] && getColorName(color) != "") {
					orientations[0] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[0], 0, 0); 
				}
			}
			else if (System.currentTimeMillis() - startTime > 3900 && System.currentTimeMillis() - startTime < 4000) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[1] && getColorName(color) != "" && getColorName(color) != orientations[0]) {
					orientations[1] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[1], 0, 1); 
				}
			}
			else if (System.currentTimeMillis() - startTime > 4700 && System.currentTimeMillis() - startTime < 4800) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[2] && getColorName(color) != "" && getColorName(color) != orientations[0] && getColorName(color) != orientations[1]) {
					orientations[2] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[2], 0, 2); 
				}
			}
			if (System.currentTimeMillis() - startTime > 5650 && System.currentTimeMillis() - startTime < 5750) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[3] && getColorName(color) != "" && getColorName(color) != orientations[0] && getColorName(color) != orientations[1] && getColorName(color) != orientations[2]) {
					orientations[3] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[3], 0, 3); 
				}
			}
			Delay.msDelay(5);
		}
	}

	
	public void drive(int speed, int time) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		motorRight.forward();
		motorLeft.forward();
		Delay.msDelay(time);
		}
	
	public void driveback(int speed, int time) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		motorRight.backward();
		motorLeft.backward();
		Delay.msDelay(time);
		}
	
	public void turn(char direction) {
		motorLeft.setSpeed(200);
		motorRight.setSpeed(200);
		if (direction == 'l') {
			motorLeft.rotate(-180, true);
			motorRight.rotate(180);
		}
		else if (direction == 'r') {
			motorLeft.rotate(180, true);
			motorRight.rotate(-180);
		}
	}
	
	public void stop() {
		motorLeft.stop(true);
		motorRight.stop(true);
	}
	/*
	public void followLine() { //currently not used
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
*/
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

	public void findOrientations() { //currently not used
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

	public int toTurn(String color) {
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
		int[][] turnmatrix = { { 180, 90, 180, 90 }, { -90, 180, -90, 0 }, { 0, -90, 0, 90 }, { 90, 0, 90, 180 } };
		return turnmatrix[orientationIndex][colorIndex];
	}
}