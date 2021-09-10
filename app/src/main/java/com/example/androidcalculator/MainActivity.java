package com.example.androidcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView currentScreen, mainScreen;
    String lastFunction, lastNumber;
    int openedBracketsCounter;
    CalculatorParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentScreen = (TextView) findViewById(R.id.currentScreen);
        mainScreen = (TextView) findViewById(R.id.mainScreen);
        lastFunction = "";
        lastNumber = "";
        openedBracketsCounter = 0;
        parser = new CalculatorParser();
    }

    /*ивент на цифры*/
    public void onNumberClick(View view) {
        Button button = (Button) view;
        /* преривает текущее выражение*/
        if(mainScreen.getText().toString().length() > 0 &&
                parser.isCLoseBracket(mainScreen.getText().toString().substring(mainScreen.getText().toString().length()-1))){
            mainScreen.setText("");
            lastFunction = "=";
            lastNumber = "";
            currentScreen.append(button.getText().toString());
        }
        /* просто вводит число*/
        else{
            /* если это 0*/
            if (button.getText().toString().equals("0")) {
                /* если в current пусто*/
                if (currentScreen.getText().toString().length() == 0) {
                    currentScreen.append(button.getText().toString());
                }
                /* если в current не пусто*/
                else {
                    /* если первый символ не 0*/
                    if(parser.isNumberExceptZero(currentScreen.getText().toString().substring(0,1))){
                        currentScreen.append(button.getText().toString());
                    }
                    /* первый символ = 0*/
                    else{
                        /* там ввобще есть точка?*/
                        if(currentScreen.getText().toString().contains(".")){
                            currentScreen.append(button.getText().toString());
                        }
                    }
                }
                /* общий случай*/
            } else {
                currentScreen.append(button.getText().toString());
            }
        }
    }

    /*ивент на нажатие точки*/
    public void onDotClick(View view) {
        Button button = (Button) view;
        if (currentScreen.getText().toString().length() > 0) {
            if (!currentScreen.getText().toString().contains(".")) {
                currentScreen.append(button.getText());
            }
        } else {
            currentScreen.append("0" + button.getText());
        }
    }

    /*ивент на функции плюс, минус, умножение, деление*/
    public void onFunctionClick(View view) {
        Button button = (Button) view;
        if(lastFunction.equals("=")){
            mainScreen.setText("");
        }
        /*в main есть данные*/
        if (mainScreen.getText().toString().length() > 0 ) {
            /* в current есть данные*/
            if (currentScreen.getText().toString().length() > 0) {
                /*если в current .*/
                if (currentScreen.getText().toString().substring(currentScreen.getText().toString().length() - 1).equals(".")) {
                    lastNumber = currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1);
                    if (!(lastNumber.equals("0") && lastFunction.equals("/"))) {
                        lastFunction = button.getText().toString();
                        mainScreen.append(" " + lastNumber + " " + lastFunction);
                        currentScreen.setText("");
                    }
                }
                /*если нет .*/
                else {
                    lastNumber = currentScreen.getText().toString();
                    if (!(lastNumber.equals("0") && lastFunction.equals("/"))) {
                        lastFunction = button.getText().toString();
                        mainScreen.append(" " + lastNumber + " " + lastFunction);
                        currentScreen.setText("");
                    }
                }
            }
            /* в current нет данных*/
            else {
                switch (mainScreen.getText().toString().substring(mainScreen.getText().toString().length() - 1)) {
                    case "(":
                        lastNumber = "0";
                        lastFunction = button.getText().toString();
                        mainScreen.append(" " + lastNumber + " " + lastFunction);
                        break;
                    case ")":
                        lastNumber = "";
                        lastFunction = button.getText().toString();
                        mainScreen.append(" " + lastFunction);
                        break;
                    default:
                        if (!this.equals(button.getText().toString())) {
                            lastFunction = button.getText().toString();
                            mainScreen.setText(mainScreen.getText().toString().substring(0, mainScreen.getText().toString().length() - 1) + lastFunction);
                        }
                        break;
                }
            }
        }
        /*в main нет данных*/
        else {
            /* в current есть данные*/
            if (currentScreen.getText().toString().length() > 0) {
                /* они оканчиваются на .*/
                if (currentScreen.getText().toString().substring(currentScreen.getText().toString().length() - 1).equals(".")) {
                    lastFunction = button.getText().toString();
                    lastNumber = currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1);
                    mainScreen.append(lastNumber + " " + lastFunction);
                    currentScreen.setText("");
                }
                /* они не оканчиваются на .*/
                else {
                    lastFunction = button.getText().toString();
                    lastNumber = currentScreen.getText().toString();
                    mainScreen.append(lastNumber + " " + lastFunction);
                    currentScreen.setText("");
                }
            }
            /* в current нет данных*/
            else {
                lastFunction = button.getText().toString();
                lastNumber = "0";
                mainScreen.append(lastNumber + " " + lastFunction);
            }
        }
    }

    /*ивент на удаление последнего символа в currentScreen*/
    public void onBackspaceCLick(View view) {
        /*currentScreen.setText(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1));*/
        if(currentScreen.getText().toString().length() > 0){
            switch (currentScreen.getText().toString().length()){
                case 1:
                    currentScreen.setText("");
                    break;
                case 2:
                    if(parser.isNegativeNumber(currentScreen.getText().toString())){
                        currentScreen.setText("");
                    }
                    else{
                        currentScreen.setText(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1));
                    }
                    break;
                default:
                    if(currentScreen.getText().toString().substring(currentScreen.getText().toString().length()-2,currentScreen.getText().toString().length()-1).equals(".")){
                        if(!parser.isContainNumber(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 2)) && parser.isNegativeNumber(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 2))){
                            currentScreen.setText(currentScreen.getText().toString().substring(1, currentScreen.getText().toString().length() - 2));
                        }
                        else {
                            currentScreen.setText(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 2));
                        }
                    }
                    else{
                        if(!parser.isContainNumber(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1)) && parser.isNegativeNumber(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1))){
                            currentScreen.setText(currentScreen.getText().toString().substring(1, currentScreen.getText().toString().length() - 1));
                        }
                        else {
                            currentScreen.setText(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1));
                        }
                        /*currentScreen.setText(currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1));*/
                    }
                    break;
            }
        }
    }

    /*ивент на удаление всего из скринов*/
    public void onClearCLick(View view) {
        currentScreen.setText("");
        mainScreen.setText("");
        lastFunction = "";
        lastNumber = "";
    }

    /*ивент на открытие скобки*/
    public void onOpenBracketClick(View view) {
        Button button = (Button) view;
        if (mainScreen.getText().toString().length() == 0) {
            mainScreen.append("(");
            openedBracketsCounter++;
        } else {
            mainScreen.append(" " + "(");
            openedBracketsCounter++;
        }
    }

    /*ивент на закрытие скобки*/
    public void onCLoseBracketClick(View view) {
        Button button = (Button) view;
        if (openedBracketsCounter > 0) {


            switch (mainScreen.getText().toString().substring(mainScreen.getText().toString().length() - 1)) {
                case "(":
                    /* в current есть данные*/
                    if (currentScreen.getText().toString().length() > 0) {
                        /*если заканчивается на .*/
                        if (currentScreen.getText().toString().substring(currentScreen.getText().toString().length() - 1).equals(".")) {
                            lastNumber = currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1);
                            mainScreen.append(" " + lastNumber + " " + button.getText().toString());
                            currentScreen.setText("");
                            openedBracketsCounter--;
                        }
                        /*если нету .*/
                        else {
                            lastNumber = currentScreen.getText().toString();
                            mainScreen.append(" " + lastNumber + " " + button.getText().toString());
                            currentScreen.setText("");
                            openedBracketsCounter--;
                        }
                    }
                    /* в current пусто*/
                    else {
                        mainScreen.setText(mainScreen.getText().toString().substring(0, mainScreen.getText().toString().length() - 1));
                        openedBracketsCounter--;
                    }
                    break;

                case ")":
                    mainScreen.append(" " + button.getText());
                    openedBracketsCounter--;
                    break;

                default:
                    /*в current есть данные*/
                    if (currentScreen.getText().toString().length() > 0) {

                        /*если заканчивается на .*/
                        if (currentScreen.getText().toString().substring(currentScreen.getText().toString().length() - 1).equals(".")) {
                            lastNumber = currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1);
                            if (lastNumber.equals("0") && lastFunction.equals("/")) {
                                /*error*/
                            } else {
                                mainScreen.append(" " + lastNumber + " " + button.getText().toString());
                                currentScreen.setText("");
                                openedBracketsCounter--;
                            }
                        }
                        /*если нету .*/
                        else {
                            lastNumber = currentScreen.getText().toString();
                            if (lastNumber.equals("0") && lastFunction.equals("/")) {
                                /*error*/
                            } else {
                                mainScreen.append(" " + lastNumber + " " + button.getText().toString());
                                currentScreen.setText("");
                                openedBracketsCounter--;
                            }
                        }
                    }
                    /*в current нет данных*/
                    else {
                        /*последним числом перед функцией был 0*/
                        if (lastNumber.equals("0")) {
                            mainScreen.setText(mainScreen.getText().toString().substring(0, mainScreen.getText().toString().length() - 6));
                            openedBracketsCounter--;
                        }
                        /*это был не 0*/
                        else {
                            mainScreen.append(" " + lastNumber + " " + button.getText().toString());
                            openedBracketsCounter--;
                        }
                    }
                    break;
            }
        }
    }

    /* ивент на вычисление выражения*/
    public void onEqualsClick(View view) {
        /*currentScreen.setText(parser.showResult(mainScreen.getText().toString()));*/
        if(lastFunction.equals("=")){
            mainScreen.setText("");
        }
        /* если все скобки сам закрыл и основное выражение не пустое*/
        if (openedBracketsCounter == 0 && mainScreen.getText().toString().length() > 0) {
            /* если в current что-то осталось*/
            if (currentScreen.getText().toString().length() > 0) {
                /*если в current .*/
                if (currentScreen.getText().toString().substring(currentScreen.getText().toString().length() - 1).equals(".")) {
                    lastNumber = currentScreen.getText().toString().substring(0, currentScreen.getText().toString().length() - 1);
                    if (!(lastNumber.equals("0") && lastFunction.equals("/"))) {
                        mainScreen.append(" " + lastNumber);
                        currentScreen.setText(parser.showResult(mainScreen.getText().toString()));
                        lastFunction = "=";
                        lastNumber = "";
                    }
                }
                /*если нет .*/
                else {
                    lastNumber = currentScreen.getText().toString();
                    if (!(lastNumber.equals("0") && !lastFunction.equals("/"))) {
                        mainScreen.append(" " + lastNumber);
                        currentScreen.setText(parser.showResult(mainScreen.getText().toString()));
                        lastFunction = "=";
                        lastNumber = "";
                    }
                }
            }
            /*в current пусто*/
            else {
                /* последний символ = функция => обрезаем*/
                if (parser.isFunction(mainScreen.getText().toString().substring(mainScreen.getText().toString().length() - 1))) {
                    currentScreen.setText(parser.showResult(mainScreen.getText().toString().substring(0, mainScreen.getText().toString().length() - 2)));
                    mainScreen.setText(mainScreen.getText().toString().substring(0, mainScreen.getText().toString().length() - 2));
                    lastFunction = "=";
                    lastNumber = "";
                }
                else{
                    currentScreen.setText(parser.showResult(mainScreen.getText().toString()));
                    lastFunction = "=";
                    lastNumber = "";
                }
            }
        }
    }

    /*ивент на приведение к знаменателю*/
    public void onDenominatorClick(View view){
        if(currentScreen.getText().toString().length() > 0){
            if(!parser.isZero(currentScreen.getText().toString())){
                if(currentScreen.getText().toString().substring(currentScreen.getText().toString().length() - 1).equals(".")){
                    currentScreen.setText(parser.toDenominator(currentScreen.getText().toString().substring(0,currentScreen.getText().toString().length()-1)));
                }
                else {
                    currentScreen.setText(parser.toDenominator(currentScreen.getText().toString()));
                }
            }
        }
    }

    /*ивент на вычисление квадратного корня*/
    public void onSqrt(View view){
        if(currentScreen.getText().toString().length() > 0){
            if(!currentScreen.getText().toString().contains("-")){
                if(currentScreen.getText().toString().substring(currentScreen.getText().toString().length() - 1).equals(".")){
                    currentScreen.setText(parser.Sqrt(currentScreen.getText().toString().substring(0,currentScreen.getText().toString().length()-1)));
                }
                else {
                    currentScreen.setText(parser.Sqrt(currentScreen.getText().toString()));
                }
            }
            else{
                Toast.makeText(this, "Невозможно вычислить корень из отрицательного числа", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*ивент на смену знака*/
    public void onSignChangeClick(View view){
        if(currentScreen.getText().toString().length() > 0){
            if(!parser.isZero(currentScreen.getText().toString()) && parser.isContainNumber(currentScreen.getText().toString())){
                if(currentScreen.getText().toString().contains("-")){
                    currentScreen.setText(currentScreen.getText().toString().substring(1));
                }
                else{
                    currentScreen.setText("-" + currentScreen.getText().toString());
                }
            }
        }
    }
}