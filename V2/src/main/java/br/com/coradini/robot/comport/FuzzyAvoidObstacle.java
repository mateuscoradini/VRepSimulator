package br.com.coradini.robot.comport;

import java.io.File;
import java.io.IOException;

import com.fuzzylite.Engine;
import com.fuzzylite.activation.General;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.imex.FclExporter;
import com.fuzzylite.imex.FllExporter;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.AlgebraicProduct;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.term.Triangle;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;

public class FuzzyAvoidObstacle {

	private Engine engine;

	public Engine createEngineFuzzyAvoidObstacle() throws IOException {
		engine = new Engine();
		engine.setName("AvoidObstacle");
		engine.setDescription("");

		String atecendentSideSensorName = "side_sensor";
		String atecendentDiagSensorName = "diag_sensor";
		String atecendentRightFrontSensorName = "right_front_sensor";
		String consequentWheelLinearName = "whell_linear_speed";
		String consequentWheelAngularName = "whell_angular_speed";

		InputVariable sideSensor = createProximityUltrassonicSensorFuzzyAtecendent(atecendentSideSensorName);
		InputVariable diagSensor = createProximityUltrassonicSensorFuzzyAtecendent(atecendentDiagSensorName);
		InputVariable rightFrontSensor = createProximityUltrassonicSensorFuzzyAtecendent(atecendentRightFrontSensorName);
		OutputVariable whellLinearSpeedOutput = createWhellLinearSpeedSensorFuzzyConsequent();
		OutputVariable whellAngularSpeedOutput = createWhellAngularSpeedSensorFuzzyConsequent();

		engine.addInputVariable(sideSensor);
		engine.addInputVariable(diagSensor);
		engine.addInputVariable(rightFrontSensor);
		engine.addOutputVariable(whellLinearSpeedOutput);
		engine.addOutputVariable(whellAngularSpeedOutput);

		RuleBlock wallFollowerRule = new RuleBlock();

		wallFollowerRule.setName("WallFollowerRule");
		wallFollowerRule.setDescription("");
		wallFollowerRule.setEnabled(true);
		wallFollowerRule.setConjunction(null);
		wallFollowerRule.setDisjunction(null);
		wallFollowerRule.setImplication(new AlgebraicProduct());
		wallFollowerRule.setActivation(new General());

		// Right Front Sensor Rules
		rightFrontSensorRules(atecendentRightFrontSensorName, consequentWheelLinearName, consequentWheelAngularName, wallFollowerRule);

		// Side Sensor Rules
		sideSensorRules(atecendentSideSensorName, consequentWheelLinearName, consequentWheelAngularName, wallFollowerRule);

		// VERY FAR
		veryFarRules(atecendentSideSensorName, atecendentRightFrontSensorName, consequentWheelLinearName, consequentWheelAngularName, wallFollowerRule);
		
		wallFollowerRule.addRule(Rule.parse("if " + atecendentDiagSensorName + "  is in_range then " + consequentWheelAngularName + " is left", engine));

		engine.addRuleBlock(wallFollowerRule);

		StringBuilder status = new StringBuilder();
		if (!engine.isReady(status))
			throw new RuntimeException("[engine error] engine is not ready:n" + status);
		
		FclExporter exporter = new FclExporter();
		FllExporter exporter2 = new FllExporter();
		exporter.toFile(new File("wallfollower"), engine);
		exporter2.toFile(new File("wallfollwerfll"), engine);

		return engine;

	}

	private void veryFarRules(String atecendentSideSensorName, String atecendentRightFrontSensorName, String consequentWheelLinearName, String consequentWheelAngularName, RuleBlock wallFollowerRule) {
		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is far then " + consequentWheelAngularName + " is verySharpRight", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is far then " + consequentWheelLinearName + " is stop", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + "  is far then " + consequentWheelAngularName + " is verySharpRight", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + "  is far then " + consequentWheelLinearName + " is stop", engine));
	}

	private void sideSensorRules(String atecendentSideSensorName, String consequentWheelLinearName, String consequentWheelAngularName, RuleBlock wallFollowerRule) {
		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is almost_close then " + consequentWheelAngularName + " is right", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is almost_close then " + consequentWheelLinearName + " is slow", engine));

		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is close then " + consequentWheelAngularName + " is straight", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is close then " + consequentWheelLinearName + " is fast", engine));

		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is very_close then " + consequentWheelAngularName + " is left", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is very_close then " + consequentWheelLinearName + " is slow", engine));

		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is in_range then " + consequentWheelAngularName + " is sharpRight", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentSideSensorName + "  is very_close then " + consequentWheelLinearName + " is fast", engine));
	}

	private void rightFrontSensorRules(String atecendentRightFrontSensorName, String consequentWheelLinearName, String consequentWheelAngularName, RuleBlock wallFollowerRule) {
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + " is in_range then " + consequentWheelLinearName + " is slow", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + " is in_range then " + consequentWheelAngularName + " is straight", engine));

		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + "  is close then " + consequentWheelLinearName + " is stop", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + "  is almost_close then " + consequentWheelLinearName + " is stop", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + "  is close then " + consequentWheelAngularName + " is verySharpLeft", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + "  is almost_close then " + consequentWheelAngularName + " is verySharpLeft", engine));

		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + " is very_close then " + consequentWheelLinearName + " is back", engine));
		wallFollowerRule.addRule(Rule.parse("if " + atecendentRightFrontSensorName + " is very_close then " + consequentWheelAngularName + " is verySharpLeft", engine));
	}

	private InputVariable createProximityUltrassonicSensorFuzzyAtecendent(String atecendentName) {
		InputVariable sensorAtecendent = new InputVariable();
		sensorAtecendent.setName(atecendentName);
		sensorAtecendent.setDescription("");
		sensorAtecendent.setEnabled(true);

		sensorAtecendent.setRange(0.000, 2.000);
		sensorAtecendent.setLockValueInRange(false);
		sensorAtecendent.addTerm(new Trapezoid("very_close", 0, 0, 0.15, 0.27));
		sensorAtecendent.addTerm(new Triangle("close", 0.25, 0.3, 0.35));
		sensorAtecendent.addTerm(new Triangle("almost_close", 0.33, 0.40, 0.45));
		sensorAtecendent.addTerm(new Triangle("in_range", 0.43, 0.55, 0.75));
		sensorAtecendent.addTerm(new Trapezoid("far", 0.7, 0.9, 2, 2));

		return sensorAtecendent;
	}

	private OutputVariable createWhellLinearSpeedSensorFuzzyConsequent() {
		OutputVariable sensorConsequent = new OutputVariable();
		sensorConsequent.setName("whell_linear_speed");
		sensorConsequent.setDescription("");
		sensorConsequent.setEnabled(true);
		sensorConsequent.setRange(-0.100, 0.45);
		sensorConsequent.setLockValueInRange(false);
		sensorConsequent.addTerm(new Triangle("back", -0.1, -0.03, 0));
		sensorConsequent.addTerm(new Triangle("stop", -0.01, 0, 0.01));
		sensorConsequent.addTerm(new Triangle("slow", 0.03, 0.09, 0.15));
		sensorConsequent.addTerm(new Triangle("fast", 0.2, 0.3, 0.4));
		sensorConsequent.setAggregation(new Maximum());
		sensorConsequent.setDefuzzifier(new Centroid(100));
		return sensorConsequent;
	}

	private OutputVariable createWhellAngularSpeedSensorFuzzyConsequent() {
		OutputVariable sensorConsequent = new OutputVariable();
		sensorConsequent.setName("whell_angular_speed");
		sensorConsequent.setDescription("");
		sensorConsequent.setEnabled(true);
		sensorConsequent.setRange(-0.9, 0.91);
		sensorConsequent.setLockValueInRange(false);
		sensorConsequent.addTerm(new Trapezoid("verySharpRight", -0.9, -0.9, -0.5, -0.45));
		sensorConsequent.addTerm(new Triangle("sharpRight", -0.5, -0.35, -0.2));
		sensorConsequent.addTerm(new Triangle("right", -0.3, -0.01, 0.0));
		sensorConsequent.addTerm(new Triangle("straight", -0.1, 0.0, 0.1));
		sensorConsequent.addTerm(new Triangle("left", 0.0, 0.1, 0.3));
		sensorConsequent.addTerm(new Triangle("sharpLeft", 0.2, 0.35, 0.5));
		sensorConsequent.addTerm(new Trapezoid("verySharpLeft", 0.45, 0.5, 0.9, 0.9));
		sensorConsequent.setAggregation(new Maximum());
		sensorConsequent.setDefuzzifier(new Centroid(100));
		return sensorConsequent;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

}
