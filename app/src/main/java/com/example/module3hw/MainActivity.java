package com.example.module3hw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setText("0");
    }

    //region HELPERS
    private void inputDigit(String digit){
        if (textView.getText().equals("0")){
            textView.setText(digit);
        }
        else if (result){
            textView.setText(digit);
            result = false;
        }
        else {
            textView.setText(textView.getText() + digit);
        }
    }

    private void inputOperator(String operator){
        if ("+*/-".contains(textView.getText().subSequence(textView.getText().length() - 1, textView.getText().length()))){
            if ("+*/".contains(textView.getText().subSequence(textView.getText().length() - 2, textView.getText().length() - 1))){
                textView.setText(textView.getText().subSequence(0, textView.getText().length() - 2) + operator);
            }
            else {
                textView.setText(textView.getText().subSequence(0, textView.getText().length() - 1) + operator);
            }
        }
        else {
            textView.setText(textView.getText() + operator);
        }
        result = false;
    }

    private String getFormula() {
        if ("+-*/".contains(textView.getText().subSequence(textView.getText().length() - 1, textView.getText().length()))){
            if ("+*/".contains(textView.getText().subSequence(textView.getText().length() - 2, textView.getText().length() - 1))){
                return textView.getText().subSequence(0, textView.getText().length() - 2).toString();
            }
            else {
                return textView.getText().subSequence(0, textView.getText().length() - 1).toString();
            }
        }
        return textView.getText().subSequence(0, textView.getText().length()).toString();
    }

    private String[] parseFormula(String[] formula, String request){
        List<String> operands = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        String currentToken = "";
        int length = formula.length;
        for(int i = 1; i < length; i++){
            if (currentToken.equals("")){
                currentToken = formula[i];
            }
            else if ("+-*/".contains(formula[i])){
                if (formula[i].equals("-") && "*/".contains(currentToken)){
                    operators.add(currentToken);
                    currentToken = formula[i];
                }
                else {
                    operands.add(currentToken);
                    currentToken = formula[i];
                }
            }
            else if (".0123456789".contains(formula[i])){
                if ("+-*/".contains(currentToken)){
                    if (currentToken.equals("-")){
                        if (i - 2 < 0 || "*/".contains(formula[i - 2])){
                            currentToken = currentToken + formula[i];
                        }
                        else {
                            operators.add(currentToken);
                            currentToken = formula[i];
                        }
                    }
                    else {
                        operators.add(currentToken);
                        currentToken = formula[i];
                    }
                }
                else {
                    currentToken = currentToken + formula[i];
                }
            }
        }

        operands.add(currentToken);

        if (request.equals("getOperands")){
            return operands.toArray(new String[operands.size()]);
        }
        else {
            return operators.toArray(new String[operators.size()]);
        }
    }

    private String[] getOperands(String formula){
        return parseFormula(formula.split(""), "getOperands");
    }

    private String[] getOperators(String formula){
        return parseFormula(formula.split(""), "getOperators");
    }

    private Float calculate(Float operand1, Float operand2, String operator) throws ArithmeticException {
        Float result = null;

        switch (operator){
            case "+":
                result = operand1 + operand2;
                break;

            case "-":
                result = operand1 - operand2;
                break;

            case "*":
                result = operand1 * operand2;
                break;

            case "/":
                if (operand2 == 0){
                    throw new ArithmeticException();
                }
                result = operand1 / operand2;
        }

        return result;
    }

    private String formatAnswer(String answer) {
        String[] answerParts = answer.split("\\.");

        if (answerParts[1].equals("0")){
            return answerParts[0];
        }
        else {
            if (answerParts[1].length() > 4 && !answerParts[1].contains("E")){
                return answerParts[0] + "." + answerParts[1].subSequence(0, 4);
            }
        }

        return answer;
    }
    //endregion

    //region DIGITS
    public void zero(View view) {
        if (textView.getText() == "0"){
            return;
        }
        if (result){
            textView.setText("0");
            result = false;
            return;
        }
        textView.setText(textView.getText() + "0");
    }

    public void one(View view) {
        inputDigit("1");
    }

    public void two(View view) {
        inputDigit("2");
    }

    public void three(View view) {
        inputDigit("3");
    }

    public void four(View view) {
        inputDigit("4");
    }

    public void five(View view) {
        inputDigit("5");
    }

    public void six(View view) {
        inputDigit("6");
    }

    public void seven(View view) {
        inputDigit("7");
    }

    public void eight(View view) {
        inputDigit("8");
    }

    public void nine(View view) {
        inputDigit("9");
    }
    //endregion

    //region OPERATORS
    public void plus(View view) {
        inputOperator("+");
    }

    public void minus(View view) {
        if ("+-".contains(textView.getText().subSequence(textView.getText().length() - 1, textView.getText().length()))){
            textView.setText(textView.getText().subSequence(0, textView.getText().length() - 1) + "-");
        }
        else {
            textView.setText(textView.getText() + "-");
        }
        result = false;
    }

    public void mult(View view) {
        inputOperator("*");
    }

    public void div(View view) {
        inputOperator("/");
    }
    //endregion

    public void point(View view) {
        if (textView.getText().equals("0")){
            textView.setText(textView.getText() + ".");
        }
        else if (result){
            textView.setText("0.");
            result = false;
        }
        else if ("+-*/".contains(textView.getText().subSequence(textView.getText().length() - 1, textView.getText().length()))){
            textView.setText(textView.getText() + "0.");
        }
        else {
            String currentToken = getOperands(textView.getText().toString())[getOperands(textView.getText().toString()).length - 1];
            if (!currentToken.contains(".")){
                textView.setText(textView.getText() + ".");
            }
        }
    }

    public void result(View view) {
        if (textView.getText().equals("0")){
            return;
        }

        String formula = getFormula();
        String[] operands = getOperands(formula);
        String[] operators = getOperators(formula);

        if (operands.length == 1){
            if (operands[0].subSequence(operands[0].length() - 1, operands[0].length()).equals(".")){
                textView.setText(operands[0].subSequence(0, operands[0].length() - 1));
            }
            else {
                textView.setText(formula);
            }
            return;
        }

        Stack<Float> values = new Stack<>();
        Stack<String> actions = new Stack<>();

        try {
            for(int i = 0; i < operators.length; i++){
                if (actions.empty()){
                    values.push(Float.parseFloat(operands[i]));
                    actions.push(operators[i]);
                }
                else {
                    if (actions.peek().equals("*") || actions.peek().equals("/")){
                        values.push(calculate(values.pop(), Float.parseFloat(operands[i]), actions.pop()));
                        actions.push(operators[i]);
                    }
                    else {
                        if (operators[i].equals("+") || operators[i].equals("-")){
                            values.push(calculate(values.pop(), Float.parseFloat(operands[i]), actions.pop()));
                            actions.push(operators[i]);
                        }
                        else {
                            operands[i + 1] = calculate(Float.parseFloat(operands[i]), Float.parseFloat(operands[i + 1]), operators[i]).toString();
                        }
                    }
                }
            }

            textView.setText(formatAnswer(calculate(values.pop(), Float.parseFloat(operands[operands.length - 1]), actions.pop()).toString()));
        } catch (ArithmeticException e){
            textView.setText("Infinity");
        }

        result = true;
    }

    public void clear(View view) {
        textView.setText("0");
        result = false;
    }

    public void del(View view) {
        result = false;
        if (textView.getText().equals("0")){
            return;
        }
        else {
            if (textView.getText().length() == 1){
                textView.setText("0");
            }
            else {
                textView.setText(textView.getText().subSequence(0, textView.getText().length() - 1));
            }
        }
    }
}
