package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class calculatorController extends controller {

    @FXML
    private transient AnchorPane anchorPane;

    @FXML private transient Button sin;
    @FXML private transient Button cos;
    @FXML private transient Button tan;
    @FXML private transient Button Exp;
    @FXML private transient Button exponent;
    @FXML private transient Button log;
    @FXML private transient Button ln;

    @FXML
    private transient TextField calculation;
    @FXML
    private transient Label pastCalculation;
    @FXML
    private transient Label warning;
    @FXML
    private transient Label solution;
    @FXML
    private transient Label mode;
    @FXML
    private transient ListView listView;

    double x;
    double result;
    String operator;

    //Basic operator symbols
    private static final Set<String> BasicOps = Set.of("+","-","×","÷", "mod", "*", "/", "%");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (pref.getBoolean("darkmode", true)) {
            anchorPane.getStylesheets().add(dark);

        }
        else {
            anchorPane.getStylesheets().add(light);
        }
        if (pref.getBoolean("rad", true)) {
            mode.setText("RAD");
        }
        else{
            mode.setText("DEG");
        }
        setTextFormatter();
    }

    /*
    User input parser, default is 0.
     */
    public String parseInput() {
        String text = calculation.getText();
        if (text.length() == 0 || text.equals("-")) {
            return "0";
        }
        else {
            return text;
        }
    }

    /*
    Radiant/ degrees switching.
     */
    public void switchMode() {
        pref.putBoolean("rad", !pref.getBoolean("rad", true));
        if (pref.getBoolean("rad", true)) {
            mode.setText("RAD");
        }
        else{
            mode.setText("DEG");
        }
    }

    /*
    Number Button input handler
     */
    public void putNumber(ActionEvent actionEvent) {
        String putNumber = ((Button)actionEvent.getSource()).getText();
        calculation.setText(calculation.getText() + putNumber);
    }

    /*
    Item in listView clicked handler
    Put history result as current calculation value
     */
    public void putHistory() {
        try {
            String item = listView.getSelectionModel().getSelectedItem().toString();
            calculation.setText(item.substring(item.indexOf("\n=") + 3));
        }
        catch (NullPointerException ignored) {

        }
    }

    /*
    Backspace button input handler
     */
    public void backspace() {
        String text = calculation.getText();
        if (text.length() > 0) {
            calculation.setText(text.substring(0, text.length() - 1));
        }
    }

    /*
    E, pi, lightspeed button input handlers
     */
    public void putE() {
        double e = Math.E;
        calculation.setText(String.valueOf(e));
    }

    public void putPi() {
        double pi = Math.PI;
        calculation.setText(String.valueOf(pi));
    }

    public void putLight() {
        double c = 299792458;
        calculation.setText(String.valueOf(c));
    }

    /*
    Sign switch button handler
     */
    public void signSwitch() {
        String text = calculation.getText();
        if (text.length() > 0 && text.charAt(0) == '-') {
            calculation.setText(text.substring(1));
        }
        else {
            calculation.setText("-" + text);
        }
    }

    /*
    C/CE, clear/clear entry button handler
     */
    public void clear() {
        if (calculation.getText().equals("")) {
            pastCalculation.setText("");
            x = 0;
            operator = null;
            solution.setText("");
        }
        else {
            calculation.setText("");
        }
    }

    /*
    Second options button handler
    Switches some buttons functionality.
     */
    public void secondSet() {
        if (ln.getText().equals("ln")) {
            exponent.setText("2ˣ");
            log.setText("logᵧx");
            log.setOnAction(this::twoInput);
            Exp.setText("|x|");
            Exp.setOnAction(this::oneInput);
            ln.setText("log₂x");
            sin.setText("arcsin");
            cos.setText("arccos");
            tan.setText("arctan");
        }
        else {
            exponent.setText("10ˣ");
            log.setText("log");
            log.setOnAction(this::oneInput);
            Exp.setText("E");
            Exp.setOnAction(this::putNumber);
            ln.setText("ln");
            sin.setText("sin");
            cos.setText("cos");
            tan.setText("tan");
        }
    }

    /*
    Put history item into listView
     */
    public void history(String calc, String result) {
        listView.getItems().add(calc + "\n= " + result);
    }

    /*
    All button with one input required handler
     */
    public void oneInput(ActionEvent actionEvent) {
        operator = ((Button)actionEvent.getSource()).getText();
        x = Double.parseDouble(parseInput());
        switch (operator) {
            case "ln": result = Math.log(x);
                pastCalculation.setText("ln(" + parseInput() + ")");
                break;
            case "log": result = Math.log10(x);
                pastCalculation.setText("log(" + parseInput() + ")");
                break;
            case "10ˣ": result = Math.pow(10, x);
                pastCalculation.setText("10^(" + parseInput() + ")");
                break;
            case "eˣ": result = Math.exp(x);
                pastCalculation.setText("e^(" + parseInput() + ")");
                break;
            case "²√x": result = Math.sqrt(x);
                pastCalculation.setText("²√" + parseInput());
                break;
            case "³√x": result = Math.cbrt(x);
                pastCalculation.setText("³√" + parseInput());
                break;
            case "x²": result = Math.pow(x, 2);
                pastCalculation.setText(parseInput() + "²");
                break;
            case "x³": result = Math.pow(x, 3);
                pastCalculation.setText(parseInput() + "³");
                break;
            case "x!":
                break;
            case "|x|": result = Math.abs(x);
                pastCalculation.setText("|" + parseInput() + "|");
                break;
            case "1/x": result = 1/x;
                pastCalculation.setText("1/" + parseInput());
                break;
            case "2ˣ": result = Math.pow(2, x);
                pastCalculation.setText("2^(" + parseInput() + ")");
                break;
            case "log₂x": result = Math.log(x) / Math.log(2);
                pastCalculation.setText("log₂(" + parseInput() + ")");
                break;
            case "sin":
                if(pref.getBoolean("rad", true)) {
                    result = Math.sin(x);
                }
                else {
                    result = Math.sin(Math.toRadians(x));
                }
                pastCalculation.setText("sin(" + parseInput() + ")");
                break;
            case "cos":
                if(pref.getBoolean("rad", true)) {
                    result = Math.cos(x);
                }
                else {
                    result = Math.cos(Math.toRadians(x));
                }
                pastCalculation.setText("cos(" + parseInput() + ")");
                break;
            case "tan":
                if(pref.getBoolean("rad", true)) {
                    result = Math.tan(x);
                }
                else {
                    result = Math.tan(Math.toRadians(x));
                }
                pastCalculation.setText("tan(" + parseInput() + ")");
                break;
            case "arcsin":
                if(pref.getBoolean("rad", true)) {
                    result = Math.asin(x);
                }
                else {
                    result = Math.toDegrees(Math.asin(x));
                }
                pastCalculation.setText("arcsin(" + parseInput() + ")");
                break;
            case "arccos":
                if(pref.getBoolean("rad", true)) {
                    result = Math.acos(x);
                }
                else {
                    result = Math.toDegrees(Math.acos(x));
                }
                pastCalculation.setText("arccos(" + parseInput() + ")");
                break;
            case "arctan":
                if(pref.getBoolean("rad", true)) {
                    result = Math.atan(x);
                }
                else {
                    result = Math.toDegrees(Math.atan(x));
                }
                pastCalculation.setText("arctan(" + parseInput() + ")");
                break;
            default:
                warning.setText("Something went wrong");
                break;
        }
        calculation.setText(String.valueOf(result));
        solution.setText(String.valueOf(result));
        history(pastCalculation.getText(), String.valueOf(result));
        operator = null;
    }

    /*
    Equals button handler, also used in between 2 consecutive two input actions
     */
    public void solve() {
        double y = Double.parseDouble(parseInput());
        String past = pastCalculation.getText();
        if(operator != null) {
            switch (operator) {
                case "+": result = x + y;
                    past = past + parseInput();
                    break;
                case "-": result = x - y;
                    past = past + parseInput();
                    break;
                case "*":
                case "×": result = x * y;
                    past = past + parseInput();
                    break;
                case "/":
                case "÷": result = x / y;
                    past = past + parseInput();
                    break;
                case "%":
                case "mod": result = x % y;
                    past = past + parseInput();
                    break;
                case "xʸ":
                    result = Math.pow(x, y);
                    past = past.replace("ʸ", "^(" + parseInput() + ")");
                    break;
                case "ʸ√x":
                    result = Math.pow(x, 1 / y);
                    past = past.replace("ʸ", parseInput());
                    break;
                case "logᵧx":
                    result = Math.log(x) / Math.log(y);
                    past = past.replace("ᵧ", parseInput() + "(") + ")";
                    break;
                default:
                    warning.setText("Something broke");
                    break;
            }
            operator = null;
        }
        else {
            result = y;
            past = parseInput();
        }
        pastCalculation.setText(past);
        calculation.setText(String.valueOf(result));
        solution.setText(String.valueOf(result));
        history(past, String.valueOf(result));
    }

    /*
    All button with two inputs required handler
     */
    public void twoInput(ActionEvent actionEvent) {
        String checkInput = calculation.getText();
        if (checkInput.length() == 0 && operator != null) {
            operator = ((Button) actionEvent.getSource()).getText();
            if (BasicOps.contains(operator)) {
                String op = parseBasicOp();
                pastCalculation.setText(x + op);
            } else {
                pastCalculation.setText(((Button) actionEvent.getSource()).getText().replace("x", Double.toString(x)));
            }
        }
        else {
            if (operator != null) {
                solve();
            }
            x = Double.parseDouble(parseInput());
            operator = ((Button) actionEvent.getSource()).getText();
            if (BasicOps.contains(operator)) {
                String op = parseBasicOp();
                pastCalculation.setText(parseInput() + op);
            } else {
                pastCalculation.setText(((Button) actionEvent.getSource()).getText().replace("x", parseInput()));
            }
            calculation.setText("");
        }
    }

    public String parseBasicOp() {
        String bop = operator;
        switch (operator) {
            case "×":
                bop = "*";
                break;
            case "÷":
                bop = "/";
                break;
            case "mod":
                bop = "%";
                break;
            default:
                warning.setText("Operator broke");
                break;
        }
        return bop;
    }

    /*
    WIP thread conflicts, Platform.runLater(runnable).
    Textformatter for checking math symbol typing user input.
     */
    private void setTextFormatter() {
        final TextFormatter<Object> formatter = new TextFormatter<>(change -> {
            if(BasicOps.contains(change.getText())) {
                if (operator != null) {
                    solve();
                }
                operator = change.getText();
                x = Double.parseDouble(parseInput());
                pastCalculation.setText(parseInput() + operator);
                calculation.setText("");
                change.setText("");
            }
            return change;
        });

        calculation.setTextFormatter(formatter);
    }
}
