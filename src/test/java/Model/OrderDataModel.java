package Model;

public class OrderDataModel {

    public String symbol;
    public String orderType;
    public String volume;
    public float price;
    public float SL;
    public float TP;
    public boolean expectedSuccess;

    public OrderDataModel(String symbol, String orderType, String volume,float price, float SL,float TP, boolean expectedSuccess) {
        this.symbol = symbol;
        this.orderType = orderType;
        this.volume = volume;
        this.price = price;
        this.SL = SL;
        this.TP = TP;
        this.expectedSuccess = expectedSuccess;
    }
}
