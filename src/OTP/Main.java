package OTP;

import OTP.data.Customer;
import OTP.data.Payment;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {

    private static final String customerData="customer.csv";
    private static final String paymentData="payments.csv";
    private static final String log = "application.log";
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");


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
        System.out.println(payments.size());
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
    /*
        Amennyiben jól értem a feladatot itt két dolgot kell validálnunk 1. dátum formátuma megfelelő-e mint az egyik adatsorban látható nincs januárnak 182.napja
        Másik dolog amit fontos csekkolni hogy az adott bolt vevő páros létezik-e
        éppen emiatt kötelező előbb beolvasni a customer.csv-t ha tudni szeretnénk hogy van-e létező vevő a rendeléshez

     */
    private static void readPaymentData() {
        try (BufferedReader br = new BufferedReader(new FileReader(paymentData))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length == 7) {
                    String webshopId = values[0].trim();
                    String customerId = values[1].trim();
                    String paymentMethod = values[2].trim();
                    int amount = Integer.parseInt(values[3].trim());
                    String bankAccount = values[4].trim();
                    String cardNumber = values[5].trim();
                    String paymentDateStr = values[6].trim();
                    //Validálja hogy a dátum formátuma helyes-e
                    if (!isCustomerValid(values[0],values[1])) {
                        logger.log(Level.WARNING, "Invalid userid and webshopid pair "+webshopId+" "+customerId);
                        continue;
                    }
                    if (!isDateValid(paymentDateStr)) {
                        logger.log(Level.WARNING, "Invalid payment date: " + paymentDateStr);
                        continue;
                    }
                    Date paymentDate;
                    try {
                        paymentDate = dateFormat.parse(values[6].trim());

                    } catch (ParseException e) {
                        logger.log(Level.WARNING, "Invalid payment date format: " + values[6]);
                        continue;
                    }

                    payments.add(new Payment(webshopId, customerId, paymentMethod, amount, bankAccount, cardNumber, paymentDate));
                } else {
                    logger.log(Level.WARNING, "Invalid payment data: " + line);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read payment data", e);
        }
    }



    private static boolean isDateValid(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        dateFormat.setLenient(false); // Engedélyezzük a szigorú dátumellenőrzést

        try {
            dateFormat.parse(dateString);
            return true; // A dátum formátuma helyes
        } catch (ParseException e) {
            return false; // A dátum formátuma érvénytelen
        }
    }
    private static boolean isCustomerValid(String wId,String cId){
        for(int i=0;i< customers.size();i++){
            if(customers.get(i).getCustomerId().equals(cId) && customers.get(i).getWebshopId().equals(wId)) {
                return true;
            }
        }
        return false;
    }

}