package com.example.androidcalculator;

import java.util.Stack;

public class CalculatorParser {
    private boolean isError;
    private String errorMessage;

    public CalculatorParser() {
        isError = false;
    }

    public String parseToRPN(String expression){
        StringBuilder output = new StringBuilder();
        Stack<String> stack = new Stack<>();
        for(String item : expression.split(" ")){
            int priority = getPriority(item);
            switch (priority){
                case 0:
                    if(output.length() > 0)
                        output.append(" " + item);
                    else
                        output.append(item);
                    break;
                case 1:
                    stack.push(item);
                    break;
                case -1:
                    while(getPriority(stack.peek()) != 1)
                        output.append(" " + stack.pop());
                    stack.pop();
                    break;
                default:
                    while (!stack.empty()){
                        if(getPriority(stack.peek()) >= priority)
                            output.append(" " + stack.pop());
                        else break;
                    }
                    stack.push(item);
                    break;
            }
        }
        while(!stack.empty())
            output.append(" " + stack.pop());
        return String.valueOf(output);
    }

    public double calculateRPN(String expression){
        int priority;
        Stack<Double> stack = new Stack<>();
        for(String token : expression.split(" ")){
            priority = getPriority(token);
            if(priority == 0)
                stack.push(Double.parseDouble(token));
            if(priority > 1){
                double first = stack.pop();
                double second = stack.pop();
                switch(token){
                    case"+":
                        stack.push(second+first);
                        break;
                    case"-":
                        stack.push(second-first);
                        break;
                    case"*":
                        stack.push(second*first);
                        break;
                    case"/":
                        if((second/first) == 0){
                            isError = true;
                            errorMessage = "Cannot divide by zero";
                            stack.push((double) 1);
                        }
                        else{
                            stack.push(second / first);
                            isError = false;
                        }
                        break;
                }
            }
        }
        return stack.pop();
    }

    public String showResult(String expression){
        String rpn = this.parseToRPN(expression);
        double result = this.calculateRPN(rpn);
        if(!isError){
            return String.valueOf(result);
        }
        else{
            return errorMessage;
        }
    }

    private int getPriority(String token) {
        if (token.equals("*") || token.equals("/")) return 3;
        else if (token.equals("+") || token.equals("-")) return 2;
        else if (token.equals("(")) return 1;
        else if (token.equals(")")) return -1;
        else return 0;
    }

    public boolean isFunction(String expression){
        return (expression.equals("+") || expression.equals("-") || expression.equals("*") || expression.equals("/"));
    }

    public boolean isNumberExceptZero(String expression){
        return (expression.equals("1") || expression.equals("2") || expression.equals("3") || expression.equals("4") || expression.equals("5") || expression.equals("6") || expression.equals("7") || expression.equals("8") || expression.equals("9"));
    }

    public boolean isZero(String expression){
        return (expression.equals("0"));
    }

    public boolean isCLoseBracket(String expression){
        return (expression.equals(")"));
    }

    public String toDenominator(String expression){
        return String.valueOf((1/Double.parseDouble(expression)));
    }

    public String Sqrt(String expression){
        return String.valueOf(Math.sqrt(Double.valueOf(expression)));
    }

    public boolean isContainNumber(String expression){
        return (expression.contains("1") || expression.contains("2") || expression.contains("3") || expression.contains("4") || expression.contains("5") || expression.contains("6") || expression.contains("7") || expression.contains("8") || expression.contains("9"));
    }

    public boolean isNegativeNumber(String expression){
        return (expression.substring(0,1).equals("-"));
    }

}
