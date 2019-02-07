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
	static String[] orientations = { "", "", "", "" }; //index: 0: east, 1: south, 2: west, 3: north

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
		LCD.drawString("running, press enter...", 0, 0);
		Button.ENTER.waitForPress();
		while (orientations[3] == "") {
			robot.followLine();
			robot.findOrientations();
		}
		LCD.drawString( "East " + orientations[0] + " to turn: " + toTurn(orientations[0], 0), 0, 1);
		LCD.drawString( "South " + orientations[1] + " to turn: " + toTurn(orientations[1], 1), 0, 2);
		LCD.drawString( "West " + orientations[2] + " to turn: " + toTurn(orientations[2], 2), 0, 3);
		LCD.drawString( "North " + orientations[3] + " to turn: " + toTurn(orientations[3], 3), 0, 4);
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
	
	public int getColorId(String color) {
		int id = -1;
		switch(color) {
		case "RED":
			id =  Color.RED;
			break;
		case "GREEN":
			id =  Color.GREEN;
			break;
		case "BLUE":
			id =  Color.BLUE;
			break;
		case "YELLOW":
			id =  Color.YELLOW;
			break;
		}	
		return id;
	}
	public String getColorName(int id) {
		String name = "";
		switch(id)  {
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
	
	public static boolean toTurn(String color, int orientation) {
		// first index: orientation East, South, West, North
		// second index: color red, green, blue, yellow
		int colorIndex = 0;
		switch(color) {
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
		boolean[][] turnmatrix = {{true, false, true, false}, {true, true, false, false}, {false, true, true, false}, {true, true, false, false}};
		return turnmatrix[orientation][colorIndex];
	}

	public void findOrientations() {
		int color = colorSensorSide.getColorID();
		if (color != Color.NONE) {
			if (orientations[0] == "") {
				orientations[0] = getColorName(color);
			}
			else if (color != getColorId(orientations[0])) {
				if (orientations[1] == "") {
					orientations[1] = getColorName(color);
				}
				else if (color != getColorId(orientations[1])) {
					if (orientations[2] == "") {
						orientations[2] = getColorName(color);
					}
					else if (color != getColorId(orientations[2])) {
						if (orientations[3] == "") {
							orientations[3] = getColorName(color);
						}
					}
				}
			}
		}
	}
}