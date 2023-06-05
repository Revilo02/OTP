package OTP.data;

public class Customer {
    private int webshopId;
    //Egyedi a webshopon belül
    private int customerId;

    private String name;

    private String Address;

    public Customer(int webshopId, int customerId, String name, String address) {
        this.webshopId = webshopId;
        this.customerId = customerId;
        this.name = name;
        Address = address;
    }

    //Gettereket elég generáltatni mivel a feladat nem kéri hogy módosítsunk adatokat ezért nem tettem bele settert

    public int getWebshopId() {
        return webshopId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return Address;
    }
}
