package OTP;

import OTP.data.Customer;
import OTP.data.Payment;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;



public class Main {

    private static final String customerData="customer.csv";
    private static final String paymentData="payments.csv";
    private static final String log = "application.log";


    //Logger müködtetéséhez itt kell létrehozni!
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static List<Customer> customers = new ArrayList<>();
    private static List<Payment> payments = new ArrayList<>();

    public static void main(String[] args) {

        //Logger();
        readCustomerData();
        readPaymentData();
        System.out.println("Hello world!");
        System.out.println(customers.size());
    }
    private static void Logger() {

        try {
            FileHandler fileHandler = new FileHandler(log);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }


    private static void readCustomerData() {
        try (BufferedReader br = new BufferedReader(new FileReader(customerData))) {
            String line;
            while ((line = br.readLine()) != null) {
                //Fontos hogy a CSV nem vesszővel hanem pontosvesszővel választotta el az adatokat egymástól
                String[] values = line.split(";");
                //System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]);
                if (values.length == 4) {
                    String webshopId = values[0].trim();

                    String customerId = values[1].trim();
                    String name = values[2].trim();
                    String address = values[3].trim();
                    customers.add(new Customer(webshopId, customerId, name, address));
                } else {
                    logger.log(Level.WARNING, "Invalid customer data: " + line);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read customer data", e);
        }
    }
    private static void readPaymentData() {
        try (BufferedReader br = new BufferedReader(new FileReader(paymentData))) {
            String line;
            while ((line = br.readLine()) != null) {
                //Fontos hogy a CSV nem vesszővel hanem pontosvesszővel választotta el az adatokat egymástól
                String[] values = line.split(";");
                //System.out.println(values[0]+" "+values[1]+" "+values[2]+" "+values[3]);
                if (values.length == 4) {
                    String webshopId = values[0].trim();

                    String customerId = values[1].trim();
                    String name = values[2].trim();
                    String address = values[3].trim();
                    customers.add(new Customer(webshopId, customerId, name, address));
                } else {
                    logger.log(Level.WARNING, "Invalid customer data: " + line);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read customer data", e);
        }
    }



}