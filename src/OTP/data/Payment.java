package OTP.data;

public class Payment {


    private String webshopId;
    private String customerId;

    //Lehetne akár Enumot is létrehozni erre, de mivel nem az volt kikötés elég lesz későbbi részekben lecsekkolni egy if-el hogy a két érték
    private String paymentMethod;

    //esetleg double is lehetne , de a bevitt adatok alapján itt nincs olyan hogy X.678 Forintot költöttünk .
    private int amount;

    private String bankAccount;
    private String cardNumber;

    //Kiiratáshoz könnyebb lesz ezzel dolgozni
    private String paymentDate;

    public Payment(String webshopId, String customerId, String paymentMethod, int amount, String bankAccount, String cardNumber, String paymentDate) {
        this.webshopId = webshopId;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.bankAccount = bankAccount;
        this.cardNumber = cardNumber;
        this.paymentDate = paymentDate;
    }

    public String getWebshopId() {
        return webshopId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public int getAmount() {
        return amount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
}
/*
A payments.csv mezői:
- Webshop azonosító
- Ügyfél azonosító (Webshop-on belül egyedi!)
- Fizetés módja ( 'card' | 'transfer' ) Kártyás fizetés, vagy banki átutalás
- Összeg (HUF)
- Bankszámlaszám, amennyiben banki átutalás történt
- Kártyaszám, amennyiben kártyás fizetés történt
- Fizetés dátuma

 */