import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.utility.Delay;

public class ColorTester {
	EV3MediumRegulatedMotor motorArm;
	EV3LargeRegulatedMotor motorLeft;
	EV3LargeRegulatedMotor motorRight;
	EV3ColorSensor colorSensorLeft;
	EV3ColorSensor colorSensorRight;
	EV3ColorSensor colorSensorSide;
	
	ColorTester() {
		this.motorArm = new EV3MediumRegulatedMotor(MotorPort.A);
		this.motorLeft = new EV3LargeRegulatedMotor(MotorPort.B);
		this.motorRight = new EV3LargeRegulatedMotor(MotorPort.C);
		this.colorSensorLeft = new EV3ColorSensor(SensorPort.S1);
		this.colorSensorRight = new EV3ColorSensor(SensorPort.S2);
		this.colorSensorSide = new EV3ColorSensor(SensorPort.S4);
	}

	public static void main(String[] args) {
		ColorTester robot = new ColorTester();
		Button.ENTER.waitForPress();
		while(Button.ESCAPE.isUp()) {
			LCD.drawString(robot.getColorName(robot.colorSensorSide.getColorID()), 0, 0);
			Delay.msDelay(10);
		}

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

}
