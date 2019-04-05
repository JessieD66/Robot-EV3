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
	//EV3ColorSensor colorSensorLeft;
	//EV3ColorSensor colorSensorRight;
	EV3ColorSensor colorSensorSide;
	String[] orientations;

	Robot2() {
		this.motorUp = new EV3MediumRegulatedMotor(MotorPort.A);
		this.motorTurn = new EV3MediumRegulatedMotor(MotorPort.D);
		this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
		this.motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
		//this.colorSensorLeft = new EV3ColorSensor(SensorPort.S1);
		//this.colorSensorRight = new EV3ColorSensor(SensorPort.S2);
		this.colorSensorSide = new EV3ColorSensor(SensorPort.S4);
		this.orientations = new String[] { "", "", "", "" }; // index: 0: east, 1: south, 2: west, 3: north
	}

	public static void main(String[] args) {
		Robot2 robot = new Robot2();
		robot.colorSensorSide.setFloodlight(Color.WHITE);
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
		Delay.msDelay(2000);
		robot.drive(-200, 500);
		robot.turn('l', 950);
		robot.stop();
		Button.ENTER.waitForPress();
		
	}
	
	public void measureOrientations () {
		for (int i = 0; i < 4; i++) {
			while (true) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != "") {
					Delay.msDelay(20);
					color = colorSensorSide.getColorID();
					if (getColorName(color) != "") {
						break;
					}
				}
				Delay.msDelay(10);
			}
			int[] colors = new int[4]; // red, green, blue, yellow
			for (int j = 0; j < 5; j++) {
				int color = colorSensorSide.getColorID();
				switch(color) {
				case Color.RED:
					colors[0]++;
					break;
				case Color.GREEN: 
					colors[1]++;
					break;
				case Color.BLUE:
					colors[2]++;
					break;
				case Color.YELLOW:
					colors[3]++;
					break;
				}
				Delay.msDelay(40);
			}
			int index = 0;
			for (int k = 1; k < 4; k++) {
				int max = colors[0];
				if (colors[k] > max) {
					max = colors[k];
					index = k;
				}
			}
			switch(index) {
			case 0:
				orientations[i] = "RED";
				break;
			case 1:
				orientations[i] = "GREEN";
				break;
			case 2:
				orientations[i] = "BLUE";
				break;
			case 3:
				orientations[i] = "YELLOW";
				break;
			}
			LCD.drawString(orientations[i], 0, i); 
			Sound.beep();
			Delay.msDelay(250);
		}
	}
	
	public void drive(int speed, int time) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		motorRight.forward();
		motorLeft.forward();
		Delay.msDelay(time);
		}
	
	public void turn(char direction, int time) {
		motorLeft.setSpeed(200);
		motorRight.setSpeed(200);
		if (direction == 'l') {
			motorLeft.backward();
			motorRight.forward();
		}
		else if (direction == 'r') {
			motorLeft.forward();
			motorRight.backward();
		}
		Delay.msDelay(time);
		//motorLeft.stop(true);
		//motorRight.stop(true);
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