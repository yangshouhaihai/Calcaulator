package com.example.calcaulator2;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
    author:Tony
    date:Feb 3rd,2023
 */
/*
    editText:输入框
    text:错误提示框
    str:参与运算的式子
    indexYN:是否出错的标志
    infixExpression:中缀表达式
    suffixExpression:后缀表达式
 */
public class calcaulateActivty extends AppCompatActivity {

    private EditText editText;
    private TextView text;
    private TextView title;
    private final StringBuilder str = new StringBuilder();
    private int indexYN = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcaulate);
        editText = (EditText) findViewById(R.id.editView);
        text = (TextView) findViewById(R.id.tv_title);
        Intent intent = getIntent();
        String titles = intent.getStringExtra("username")+"のcalcaulate";
        title = findViewById(R.id.title);
        title.setText(titles);
    }

    //1234567890+-.()
    public void clickButton(View view) {
        Button button = (Button) view;
        editText.append(button.getText());
        str.append(button.getText());
    }

    //除
    public void div(View view) {
        editText.append("/");
        str.append("/");
    }

    //乘
    public void mul(View view) {
        editText.append("*");
        str.append("*");
    }

    //清空
    public void empty(View view) {
        editText.setText(null);
        text.setText(null);
        str.delete(0, str.length());
    }

    //删除
    public void delete(View view) {
        String nowText = editText.getText().toString();
        if (nowText.length() != 0 && str.length() != 0) {
            editText.setText(nowText.substring(0, nowText.length() - 1));
            str.deleteCharAt(str.length() - 1);
        }
        text.setText(null);
    }

    //等于
    public void equal(View view) {
        indexYN = 0;
        /*
            System.out.println("str:\t" + str);
            System.out.println("length:\t" + str.length());
         */
        estimate();
        if (indexYN == 0) {
            List<String> infixExpression = turnIntoInfixExpression(str.toString());
            System.out.println(infixExpression);
            List<String> suffixExpression = turnIntoSuffixExpression(infixExpression);
            System.out.println(suffixExpression);
            editText.append("\n" + math(suffixExpression));
            str.delete(0, str.length());
            str.append(math(suffixExpression));
        }
    }

    //倒数
    public void reciprocal(View view) {
        editText.append("1/");
        str.append("1/");
    }

    //阶乘
    public void factorial(View view) {
        editText.append("!");
        str.append("!");
    }

    //平方
    public void square(View view) {
        editText.append("^2");
        str.append("^2");
    }
    //幂
    public void power(View view) {
        editText.append("^");
        str.append("^");
    }

    //开方
    public void squareRoot(View view) {
        editText.append("√");
        str.append("g");
    }

    //欧拉数e
    public void eulerNumber(View view) {
        editText.append("e");
        str.append("e");
    }

    //百分数
    public void percentage(View view) {
        editText.append("%");
        str.append("*0.01");
    }

    //圆周率
    public void pi(View view) {
        editText.append("π");
        str.append("p");
    }

    //sin
    public void sin(View view) {
        editText.append("sin");
        str.append("s");
    }

    //cos
    public void cos(View view) {
        editText.append("cos");
        str.append("c");
    }

    //tan
    public void tan(View view) {
        editText.append("tan");
        str.append("t");
    }

    //ln
    public void ln(View view) {
        editText.append("ln");
        str.append("l");
    }

    //log
    public void log(View view) {
        editText.append("log");
        str.append("o");
    }

    private List<String> turnIntoInfixExpression(String str) {
        //把输入的字符串转换成中缀表达式。存入list中
        int index = 0;
        List<String> list = new ArrayList<>();
        do {
            char ch = str.charAt(index);
            if ("+-*/^!logsct()".indexOf(str.charAt(index)) >= 0) {
                //是操作符，直接添加至list中
                index++;
                list.add(ch + "");
            } else if (str.charAt(index) == 'e' || str.charAt(index) == 'p') {
                index++;
                list.add(ch + "");
            } else if ("0123456789".indexOf(str.charAt(index)) >= 0) {
                //是数字,判断多位数的情况
                StringBuilder str1 = new StringBuilder();
                while (index < str.length()
                        && "0123456789.".indexOf(str.charAt(index)) >= 0) {
                    str1.append(str.charAt(index));
                    index++;
                }
                list.add(str1.toString());

            }
        } while (index < str.length());
        return list;
    }

    //中缀表达式转换称后缀表达式
    public List<String> turnIntoSuffixExpression(List<String> list) {
        Stack<String> fuZhan = new Stack<>();
        List<String> list2 = new ArrayList<>();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (isNumber(list.get(i))) {
                    list2.add(list.get(i));
                } else if (list.get(i).charAt(0) == '(') {
                    fuZhan.push(list.get(i));
                } else if (isOperator(list.get(i)) && list.get(i).charAt(0) != '(') {
                    if (fuZhan.isEmpty()) {
                        fuZhan.push(list.get(i));
                    } else {//符栈不为空
                        if (list.get(i).charAt(0) != ')') {
                            if (adv(fuZhan.peek()) <= adv(list.get(i))) {
                                //入栈
                                fuZhan.push(list.get(i));
                            } else {
                                //出栈
                                while (!fuZhan.isEmpty() && !"(".equals(fuZhan.peek())) {
                                    if (adv(list.get(i)) <= adv(fuZhan.peek())) {
                                        list2.add(fuZhan.pop());
                                    }
                                }
                                if (fuZhan.isEmpty() || fuZhan.peek().charAt(0) == '(') {
                                    fuZhan.push(list.get(i));
                                }
                            }
                        } else if (list.get(i).charAt(0) == ')') {
                            while (fuZhan.peek().charAt(0) != '(') {
                                list2.add(fuZhan.pop());
                            }
                            fuZhan.pop();
                        }
                    }
                }
            }
            while (!fuZhan.isEmpty()) {
                list2.add(fuZhan.pop());
            }
        } else {
            editText.setText("");
        }
        return list2;
    }

    //判断是否为操作符
    public static boolean isOperator(String op) {
        return "0123456789.ep".indexOf(op.charAt(0)) == -1;
    }

    //判断是否为操作数
    public static boolean isNumber(String num) {
        return "0123456789ep".indexOf(num.charAt(0)) >= 0;
    }

    //判断操作符的优先级
    public static int adv(String f) {
        int result = 0;
        switch (f) {
            case "+":
            case "-":
                result = 1;
                break;
            case "*":
            case "/":
                result = 2;
                break;
            case "^":
                result = 3;
                break;
            case "!":
            case "g":
            case "l":
            case "o":
            case "s":
            case "c":
            case "t":
                result = 4;
                break;
        }
        return result;
    }
    //通过后缀表达式进行计算
    public double math(List<String> list2) {
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < list2.size(); i++) {
            if (isNumber(list2.get(i))) {
                if (list2.get(i).charAt(0) == 'e') {
                    stack.push(String.valueOf(Math.E));
                } else if (list2.get(i).charAt(0) == 'p') {
                    stack.push(String.valueOf(Math.PI));
                } else {
                    stack.push(list2.get(i));
                }
            } else if (isOperator(list2.get(i))) {
                double res = 0;
                switch (list2.get(i)) {
                    case "+": {
                        double num2 = Double.parseDouble(stack.pop());
                        double num1 = Double.parseDouble(stack.pop());
                        res = num1 + num2;
                        break;
                    }
                    case "-": {
                        double num2 = Double.parseDouble(stack.pop());
                        double num1 = Double.parseDouble(stack.pop());
                        res = num1 - num2;
                        break;
                    }
                    case "*": {
                        double num2 = Double.parseDouble(stack.pop());
                        double num1 = Double.parseDouble(stack.pop());
                        res = num1 * num2;
                        break;
                    }
                    case "/": {
                        double num2 = Double.parseDouble(stack.pop());
                        double num1 = Double.parseDouble(stack.pop());
                        if (num2 != 0) {
                            res = num1 / num2;
                        } else {
                            text.setText("Can't be divided by 0!");
                            indexYN = 1;
                        }
                        break;
                    }
                    case "^": {
                        double num2 = Double.parseDouble(stack.pop());
                        double num1 = Double.parseDouble(stack.pop());
                        res = Math.pow(num1, num2);
                        break;
                    }
                    case "!": {
                        double num1 = Double.parseDouble(stack.pop());
                        if (num1 == 0 || num1 == 1) {
                            res = 1;
                        } else if (num1 == (int) num1 && num1 > 1) {
                            int d = 1;
                            for (int j = (int) num1; j > 0; j--) {
                                d *= j;
                            }
                            res = d;
                        } else {
                            text.setText("The factorial must be a natural number!");
                            indexYN = 1;
                        }
                        break;
                    }
                    case "g": {
                        double num1 = Double.parseDouble(stack.pop());
                        res = Math.sqrt(num1);
                        break;
                    }
                    case "l": {
                        double num1 = Double.parseDouble(stack.pop());
                        if (num1 > 0) {
                            res = Math.log(num1);
                        } else {
                            text.setText("The variable x in ln must be greater than 0!");
                            indexYN = 1;
                        }
                        break;
                    }
                    case "o": {
                        double num1 = Double.parseDouble(stack.pop());
                        if (num1 > 0) {
                            res = Math.log(num1) / Math.log(2);
                        } else {
                            text.setText("The variable x in function log must be greater than 0!");
                            indexYN = 1;
                        }
                        break;
                    }
                    case "s": {
                        double num1 = Double.parseDouble(stack.pop());
                        res = Math.sin(num1);
                        break;
                    }
                    case "c": {
                        double num1 = Double.parseDouble(stack.pop());
                        res = Math.cos(num1);
                        break;
                    }
                    case "t": {
                        double num1 = Double.parseDouble(stack.pop());
                        if (Math.cos(num1) != 0) {
                            res = Math.tan(num1);
                        } else {
                            text.setText("The variable in function tan can't be +-(π/2 + kπ)!");
                            indexYN = 1;
                        }
                        break;
                    }
                }
                stack.push("" + res);
            }
        }
        if (indexYN == 0) {
            if (!stack.isEmpty()) {
                return Double.parseDouble(stack.pop());
            } else {
                return 0;
            }
        } else {
            return -999999;
        }
    }

    //判断输入是否错误
    public void estimate() {
        int i;
        if (str.length() == 0) {
            text.setText("Error!");
            indexYN = 1;
        }
        if (str.length() == 1) {
            //当只有一位字符时，只能是“0123456789ep”中的一个
            if ("0123456789ep".indexOf(str.charAt(0)) == -1) {
                text.setText("Error!");
                indexYN = 1;
            }
        }
        if (str.length() > 1) {
            for (i = 0; i < str.length() - 1; i++) {
                //1.第一个字符只能为"losctg(0123456789ep"中的一个
                if ("losctg(0123456789ep".indexOf(str.charAt(0)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //2.“+-*/”后面只能是"0123456789losctg(ep"中的一个
                if ("+-*/".indexOf(str.charAt(i)) >= 0
                        && "0123456789losctg(ep".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //3."."后面只能是“0123456789”中的一个
                if (str.charAt(i) == '.' && "0123456789".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //4."!"后面只能是“+-*/^)”中的一个
                if (str.charAt(i) == '!' && "+-*/^)".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //5."losctg"后面只能是“0123456789(ep”中的一个
                if ("losctg".indexOf(str.charAt(i)) >= 0 && "0123456789(ep".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //6."0"的判断操作
                if (str.charAt(0) == '0' && str.charAt(1) == '0') {
                    text.setText("Error!");
                    indexYN = 1;
                }
                if (i >= 1 && str.charAt(i) == '0') {
                    //&& str.charAt(0) == '0' && str.charAt(1) == '0'
                    int n = i;
                    int is = 0;
                    //1.当0的上一个字符不为"0123456789."时，后一位只能是“+-*/.!^)”中的一个
                    if ("0123456789.".indexOf(str.charAt(i - 1)) == -1 && "+-*/.!^)".indexOf(str.charAt(i + 1)) == -1) {
                        text.setText("Error!");
                        indexYN = 1;
                    }
                    //2.如果0的上一位为“.”,则后一位只能是“0123456789+-*/.^)”中的一个
                    if (str.charAt(i - 1) == '.' && "0123456789+-*/.^)".indexOf(str.charAt(i + 1)) == -1) {
                        text.setText("Error!");
                        indexYN = 1;
                    }
                    n -= 1;
                    while (n > 0) {
                        if ("(+-*/^glosct".indexOf(str.charAt(n)) >= 0) {
                            break;
                        }
                        if (str.charAt(n) == '.') {
                            is++;
                        }
                        n--;
                    }

                    //3.如果0到上一个运算符之间没有“.”的话，整数位第一个只能是“123456789”，
                    //  且后一位只能是“0123456789+-*/.!^)”中的一个。
                    if ((is == 0 && str.charAt(n) == '0') || "0123456789+-*/.!^)".indexOf(str.charAt(i + 1)) == -1) {
                        text.setText("Error!");
                        indexYN = 1;
                    }
                    //4.如果0到上一个运算符之间有一个“.”的话,则后一位只能是“0123456789+-*/.^)”中的一个
                    if (is == 1 && "0123456789+-*/.^)".indexOf(str.charAt(i + 1)) == -1) {
                        text.setText("Error!");
                        indexYN = 1;
                    }
                    if (is > 1) {
                        text.setText("Error!");
                        indexYN = 1;
                    }

                }
                //7."123456789"后面只能是“0123456789+-*/.!^)”中的一个
                if ("123456789".indexOf(str.charAt(i)) >= 0 && "0123456789+-*/.!^)".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //8."("后面只能是“0123456789locstg()ep”中的一个
                if (str.charAt(i) == '(' && "0123456789locstg()ep".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //9.")"后面只能是“+-*/!^)”中的一个
                if (str.charAt(i) == ')' && "+-*/!^)".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //10.最后一位字符只能是“0123456789!)ep”中的一个
                if ("0123456789!)ep".indexOf(str.charAt(str.length() - 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
                //11.不能有多个“.”
                if (i > 2 && str.charAt(i) == '.') {
                    int n = i - 1;
                    int is = 0;
                    while (n > 0) {
                        if ("(+-*/^glosct".indexOf(str.charAt(n)) >= 0) {
                            break;
                        }
                        if (str.charAt(n) == '.') {
                            is++;
                        }
                        n--;
                    }
                    if (is > 0) {
                        text.setText("Error!");
                        indexYN = 1;
                    }
                }
                //12."ep"后面只能是“+-*/^)”中的一个
                if ("ep".indexOf(str.charAt(i)) >= 0 && "+-*/^)".indexOf(str.charAt(i + 1)) == -1) {
                    text.setText("Error!");
                    indexYN = 1;
                }
            }
        }
    }
}