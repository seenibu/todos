package sn.ept.git.seminaire.cicd.demo;

import sn.ept.git.seminaire.cicd.demo.exception.DivisionByZeroException;

public class Calculator implements ICalculator {

    public static final String DIVIDE_BY_ZERO = "Can not divide by zero";

    @Override
    public double add(double a, double b) {
        return a+b;
    }

    @Override
    public double subtract(double a, double b) {
        return  a-b;
    }

    @Override
    public double multiply(double a, double b) {
        return  a*b;
    }

    @Override
    public double divide(double a, double b) throws DivisionByZeroException {
        if(Double.compare(b,0)==0){
            throw new DivisionByZeroException(DIVIDE_BY_ZERO);
        }
        return a/b;
    }
}
