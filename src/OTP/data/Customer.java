package OTP.data;

public class Customer {
    private String webshopId;
    //Egyedi a webshopon belül
    private String customerId;

    private String name;

    private String Address;

    public Customer(String webshopId, String customerId, String name, String address) {
        this.webshopId = webshopId;
        this.customerId = customerId;
        this.name = name;
        Address = address;
    }

    public String getWebshopId() {
        return webshopId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return Address;
    }
}
