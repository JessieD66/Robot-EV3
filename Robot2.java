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
	EV3ColorSensor colorSensorMiddle;
	EV3ColorSensor colorSensorRight;
	EV3ColorSensor colorSensorLeft;
	EV3ColorSensor colorSensorSide;
	String[] orientations;

	Robot2() {
		this.motorUp = new EV3MediumRegulatedMotor(MotorPort.C);
		this.motorTurn = new EV3MediumRegulatedMotor(MotorPort.D);
		this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
		this.motorRight = new EV3LargeRegulatedMotor(MotorPort.B);
		this.colorSensorMiddle = new EV3ColorSensor(SensorPort.S2);
		this.colorSensorRight = new EV3ColorSensor(SensorPort.S4);
		this.colorSensorLeft = new EV3ColorSensor(SensorPort.S1);
		this.colorSensorSide = new EV3ColorSensor(SensorPort.S3);
		this.orientations = new String[] { "", "", "", "" }; // index: 0: east, 1: south, 2: west, 3: north
	}

	public static void main(String[] args) {
		Robot2 robot = new Robot2();
		LCD.drawString("running, press enter...", 0, 0);
		Button.ENTER.waitForPress();
		LCD.clear();
		robot.drive(200);
		robot.measureOrientations();
		Sound.buzz();
		LCD.drawString("Turn Red:  " + robot.toTurn("RED"), 0, 4);
		LCD.drawString("Turn Green:  " + robot.toTurn("GREEN"), 0, 5);
		LCD.drawString("Turn Blue:  " + robot.toTurn("BLUE"), 0, 6);
		LCD.drawString("Turn Yellow:  " + robot.toTurn("YELLOW"), 0, 7);
		robot.stop();
		Sound.beep();
		Delay.msDelay(500);
		robot.turn('l');
		Sound.beep();
		robot.drive(600);
		Delay.msDelay(5300);
		robot.stop();
		robot.turn('l');
		robot.drive(200);
		LCD.clear();
		robot.toLine();
		if (robot.routerColour(robot.colorSensorSide.getColorID()) == "BLACK") {
			Sound.buzz();
			robot.liftRouter();
			robot.turn('b');
			robot.distance(360);
			robot.turnAndDrop(robot.toTurn("RED"));
		} 
		else {
			robot.drive(200);
			Delay.msDelay(500);
			robot.toLine();
			robot.liftRouter();
			robot.turn('l');
			robot.driveback(200);
			robot.toLine();
			robot.turn('l');
			robot.distance(360);
			robot.turnAndDrop(robot.toTurn("RED"));
		}
		Button.ENTER.waitForPress();
	}
	
	public void distance(int angle) {
		motorRight.rotate(angle, true);
		motorLeft.rotate(angle);
	}

	public void toLine() {
		while (true) {
			if (colorSensorLeft.getColorID() != Color.WHITE && colorSensorMiddle.getColorID() != Color.WHITE
					&& colorSensorRight.getColorID() != Color.WHITE) {
				stop();
				break;
			}
		}
	}

	public void liftRouter() {
		motorLeft.rotate(270, true);
		motorRight.rotate(270);
		Delay.msDelay(100);
		motorRight.rotate(-380);
		motorRight.rotate(-120, true);
		motorLeft.rotate(-120);
		motorUp.setSpeed(100);
    	motorUp.rotate(110);
    	Delay.msDelay(400);
    	motorLeft.setSpeed(130);
    	motorRight.setSpeed(130);
    	motorRight.rotate(200, true);
    	motorLeft.rotate(200);
    	Delay.msDelay(360);
    	motorUp.rotate(-50);
	}
	
	public void turnAndDrop(int angle)
	{
    	motorTurn.setSpeed(100);
    	motorTurn.rotate(angle);
    	Delay.msDelay(600);
    	motorUp.setSpeed(100);
    	if (angle == 90) {
        	motorUp.rotate(50);
        	motorTurn.setSpeed(600);
        	motorTurn.rotate(20);
        	motorTurn.rotate(-20);
        	motorTurn.rotate(20);
        	motorTurn.rotate(-20);
        	Delay.msDelay(200);
        	motorLeft.rotate(50);
        	motorUp.rotate(-110);
        	motorLeft.rotate(-50);
    	}
    	else if (angle == -90) {
        	motorUp.rotate(50);
        	motorTurn.setSpeed(600);
        	motorTurn.rotate(20);
        	motorTurn.rotate(-20);
        	motorTurn.rotate(20);
        	motorTurn.rotate(-20);
        	Delay.msDelay(200);
        	motorRight.rotate(50);
        	motorUp.rotate(-110);
        	motorRight.rotate(-50);
    	}
    	else if (angle == 180) {
        	motorUp.rotate(40);
        	motorTurn.setSpeed(600);
        	motorTurn.rotate(-20);
        	motorTurn.rotate(20);
        	motorTurn.rotate(-20);
        	motorTurn.rotate(20);
        	Delay.msDelay(200);
        	motorLeft.rotate(50);
        	motorRight.rotate(50);
        	motorUp.rotate(-110);
    	}
    	else if (angle == 0) {
        	motorUp.rotate(40);
        	motorTurn.setSpeed(600);
        	motorTurn.rotate(200);
        	motorTurn.rotate(20);
        	motorTurn.rotate(-20);
        	motorTurn.rotate(20);
        	motorTurn.rotate(-20);
        	motorUp.rotate(-110);
    	}
    	motorTurn.rotate(-angle);
    	motorRight.rotate(-200, true);
    	motorLeft.rotate(-200);
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
		while (colorSensorLeft.getColorID() == Color.WHITE || System.currentTimeMillis() - startTime < 7000) {
			if (System.currentTimeMillis() - startTime > 3050 && System.currentTimeMillis() - startTime < 3150) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[0] && getColorName(color) != "") {
					orientations[0] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[0], 0, 0);
				}
			} else if (System.currentTimeMillis() - startTime > 3900 && System.currentTimeMillis() - startTime < 4000) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[1] && getColorName(color) != ""
						&& getColorName(color) != orientations[0]) {
					orientations[1] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[1], 0, 1);
				}
			} else if (System.currentTimeMillis() - startTime > 4700 && System.currentTimeMillis() - startTime < 4800) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[2] && getColorName(color) != ""
						&& getColorName(color) != orientations[0] && getColorName(color) != orientations[1]) {
					orientations[2] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[2], 0, 2);
				}
			}
			if (System.currentTimeMillis() - startTime > 5650 && System.currentTimeMillis() - startTime < 5750) {
				int color = colorSensorSide.getColorID();
				if (getColorName(color) != orientations[3] && getColorName(color) != ""
						&& getColorName(color) != orientations[0] && getColorName(color) != orientations[1]
						&& getColorName(color) != orientations[2]) {
					orientations[3] = getColorName(color);
					Sound.beep();
					LCD.drawString(orientations[3], 0, 3);
				}
			}
			Delay.msDelay(5);
		}
		distance(200);
	}

	public void drive(int speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		motorRight.forward();
		motorLeft.forward();
	}

	public void driveback(int speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
		motorRight.backward();
		motorLeft.backward();
	}

	public void turn(char direction) {
		motorLeft.setSpeed(200);
		motorRight.setSpeed(200);
		if (direction == 'l') {
			motorLeft.rotate(-195, true);
			motorRight.rotate(195);
		} else if (direction == 'r') {
			motorLeft.rotate(195, true);
			motorRight.rotate(-195);
		} else if (direction == 'b') {
			motorLeft.rotate(370, true);
			motorRight.rotate(-370);
		}
	}

	public void stop() {
		motorLeft.stop(true);
		motorRight.stop(true);
	}

	public void findLine() {
		drive(200);
		while (colorSensorMiddle.getColorID() != Color.BLACK || colorSensorLeft.getColorID() == Color.BLACK
				|| colorSensorRight.getColorID() == Color.BLACK) {
			if (colorSensorLeft.getColorID() == Color.BLACK) {
				motorRight.setSpeed(300);
			} else if (colorSensorRight.getColorID() == Color.BLACK) {
				motorLeft.setSpeed(300);
			}
		}
		motorLeft.setSpeed(200);
		motorRight.setSpeed(200);
	}

	public void followLine(int crossleft) {
		drive(300);
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 10100) {
			/*
			 * } else if (colorSensorLeft.getColorID() == Color.BLACK &&
			 * colorSensorMiddle.getColorID() == Color.BLACK) { count++; Sound.buzz(); if
			 * (count < crossleft) { Delay.msDelay(1000); } else { stop(); break; } }
			 */
			if (colorSensorRight.getColorID() == Color.BLACK && colorSensorMiddle.getColorID() != Color.BLACK) {
				motorLeft.setSpeed(400);
				Delay.msDelay(20);
			}
			if (colorSensorLeft.getColorID() == Color.BLACK && colorSensorMiddle.getColorID() != Color.BLACK) {
				motorRight.setSpeed(400);
				Delay.msDelay(20);
			} else if (colorSensorMiddle.getColorID() == Color.BLACK && colorSensorRight.getColorID() != Color.BLACK
					&& colorSensorLeft.getColorID() != Color.BLACK) {
				motorLeft.setSpeed(300);
				motorRight.setSpeed(300);
			}

		}
		stop();
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

	public void findOrientations() { // currently not used
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
		int[][] turnmatrix = { { 180, -90, 180, -90 }, { 90, 180, 90, 0 }, { 0, 90, 0, -90 }, { -90, 0, -90, 180 } };
		return turnmatrix[orientationIndex][colorIndex];
	}
}