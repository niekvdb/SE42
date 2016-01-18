/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorws_client_application;

import webservice.NegativeNumberException_Exception;

/**
 *
 * @author Niek
 */
public class CalculatorWS_Client_Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NegativeNumberException_Exception {
        int i = 3;
        int j = 4;
        int result = add(i, j);
        System.out.println("Add = " + result);
        result = minus(j, i);
        System.out.println("Minus = " + result);
        result = times(i, j);
        System.out.println("Times = " + result);
    }

    private static int add(int arg0, int arg1) throws NegativeNumberException_Exception {
        webservice.WebCalculatorService service = new webservice.WebCalculatorService();
        webservice.WebCalculator port = service.getWebCalculatorPort();
        return port.add(arg0, arg1);
    }

    private static int minus(int arg0, int arg1) throws NegativeNumberException_Exception {
        webservice.WebCalculatorService service = new webservice.WebCalculatorService();
        webservice.WebCalculator port = service.getWebCalculatorPort();
        return port.minus(arg0, arg1);
    }

    private static int times(int arg0, int arg1) throws NegativeNumberException_Exception {
        webservice.WebCalculatorService service = new webservice.WebCalculatorService();
        webservice.WebCalculator port = service.getWebCalculatorPort();
        return port.times(arg0, arg1);
    }

}
