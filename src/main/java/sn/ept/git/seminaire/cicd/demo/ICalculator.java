package sn.ept.git.seminaire.cicd.demo;

import sn.ept.git.seminaire.cicd.demo.exception.DivisionByZeroException;

public interface ICalculator {

    double add (double a,double b);
    double subtract(double a, double b);
    double multiply (double a,double b);
    double divide (double a,double b) throws DivisionByZeroException;

}
