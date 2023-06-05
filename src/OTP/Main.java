package OTP;

import OTP.data.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;


public class Main {

    private static final String customerData="customer.csv";
    private static final String paymentData="payments.csv";
    private static final String log = "application.log";
    private static final String report1="Report01.csv";
    private static final String report2="Report02.csv";
    private static final String reportTop="Top.csv";
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");


    //Logger müködtetéséhez itt kell inicializálni
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static List<Customer> customers = new ArrayList<>();
    private static List<Payment> payments = new ArrayList<>();

    public static void main(String[] args) {

        Logger();
        readCustomerData();
        readPaymentData();
        generateCustomerReport();
        generateTopCustomersReport();
        generateWebShopReport();

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


    /*
        Validálni kell azt is hogy egy bolton belül csak egy Id-t állítottak ki

     */
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
                    boolean addAble=true;
                    for(int i=0;i< customers.size();i++){
                        if(customers.get(i).getWebshopId().equals(webshopId) && customers.get(i).getCustomerId().equals(customerId)){
                            logger.log(Level.WARNING, "Invalid userid and webshopid pair "+webshopId+" "+customerId);
                            addAble=false;
                                continue;
                        }
                    }
                    if(addAble)
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
        Bankszámla illetve bankártya szám hosszát is validálni kell !

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
                   if (!isCustomerValid(values[0],values[1]) || !isDateValid(paymentDateStr ) ) {
                        logger.log(Level.SEVERE, "Invalid data: " + line);
                        continue;
                    }
                    //Validálja hogy a cardnumber vagy a bankaccount hossza nem egyenlő-e 16-al
                    if ((bankAccount.length() != 16 && cardNumber.equals("")) || (bankAccount.equals("") && cardNumber.length() != 16)) {
                        logger.log(Level.SEVERE, "Invalid bank account or card number length: " + line);
                        continue;
                    }

                    Date paymentDate;
                    try {
                        paymentDate = dateFormat.parse(values[6].trim());

                    } catch (ParseException e) {
                        logger.log(Level.SEVERE, "Invalid payment date format: " + values[6]);
                        continue;
                    }

                    payments.add(new Payment(webshopId, customerId, paymentMethod, amount, bankAccount, cardNumber, paymentDate));
                } else {
                    logger.log(Level.SEVERE, "Invalid payment data: " + line);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read payment data", e);
        }
    }
    //Validálások
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
    private static boolean isCardNumberValid(String cardnumber, String bankaccount){
        if(cardnumber.length()!=16  )
            return false;
        if(bankaccount.length()!=16)
            return false;
        return true;
    }


    //Reportok létrehozása
    private static void generateCustomerReport() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(report1))) {
            writer.println("NAME;ADDRESS;vásárlás összesen");
            for (Customer customer : customers) {
                int totalAmount = getTotalAmountForCustomer(customer.getCustomerId(), customer.getWebshopId());
                writer.println(customer.getName() + ";" + customer.getAddress() + ";" + totalAmount);
            }
            logger.log(Level.INFO, "Customer report generated successfully.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to generate customer report.", e);
        }
    }

    private static int getTotalAmountForCustomer(String customerId, String webshopID ) {
        int totalAmount = 0;
        for (Payment payment : payments) {
            for(Customer customer: customers){
                if (payment.getCustomerId().equals(customerId) && payment.getWebshopId().equals(webshopID)) {
                    totalAmount += payment.getAmount();
                    break;
                }
            }
        }
        return totalAmount;
    }
    private static void generateTopCustomersReport() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportTop))) {
            writer.println("NAME;ADDRESS;vásárlás összesen");
            List<Customer> sortedCustomers = new ArrayList<>(customers);
            sortedCustomers.sort(Comparator.comparingInt((Customer c) -> getTotalAmountForCustomer(c.getCustomerId(),c.getWebshopId())).reversed());
            int count = Math.min(sortedCustomers.size(), 2);
            for (int i = 0; i < count; i++) {
                Customer customer = sortedCustomers.get(i);
                int totalAmount = getTotalAmountForCustomer(customer.getCustomerId(),customer.getWebshopId());
                writer.println(customer.getName() + ";" + customer.getAddress() + ";" + totalAmount);
            }
            logger.log(Level.INFO, "Top customers report generated successfully.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to generate top customers report.", e);
        }
    }
    private static void generateWebShopReport(){
        ArrayList<String> webshopNames = new ArrayList<>();


        // Az egyes webshopok bevételeinek összesítése
        for (Payment payment : payments) {
            String webshopId = payment.getWebshopId();


            int index = webshopNames.indexOf(webshopId);
            if (index == -1) {
                // A webshop már szerepel a listában, frissítsük a bevételeket
                webshopNames.add(webshopId);

            }
        }
        int [] cardpayments=new int [webshopNames.size()];
        int [] bankaccountpayments=new int[webshopNames.size()];

        for(int i=0;i< webshopNames.size();i++){
            cardpayments[i]=0;

            for (int j=0; j<payments.size();j++){

                if(webshopNames.get(i).equals(payments.get(j).getWebshopId())){
                    if(payments.get(j).getPaymentMethod().equals("transfer")) {
                        cardpayments[i] += payments.get(j).getAmount();

                    }
                    else{
                        bankaccountpayments[i]+=payments.get(j).getAmount();

                    }
                }
            }
        }




        try (PrintWriter writer = new PrintWriter(new FileWriter(report2))) {
            writer.println("WEBSHOP;KÁRTYÁSVÁSÁRLÁSOKOSSZEGE;ÁTUTALÁSOSVÁSÁRLÁSOKOSSZEGE");
            for(int i=0;i<webshopNames.size();i++){
                writer.println(webshopNames.get(i)+";"+cardpayments[i]+";"+bankaccountpayments[i]);
            }

            logger.log(Level.INFO, "Webshop report generated successfully.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to generate Webshop report.", e);
        }
    }


}