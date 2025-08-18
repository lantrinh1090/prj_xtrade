package Model;

public class OrderDataModel {

        private String symbol;
        private String orderMode;
        private Float volume;
        private Float price;
        private String fillPolicy;
        private String orderType;
        private boolean expectSuccess;

        public OrderDataModel(String symbol, String orderMode, Float volume,
                              Float price, String fillPolicy, String orderType, boolean expectSuccess) {
            this.symbol = symbol;
            this.orderMode = orderMode;
            this.volume = volume;
            this.price = price;
            this.fillPolicy = fillPolicy;
            this.orderType = orderType;
            this.expectSuccess = expectSuccess;
        }

        public String getSymbol() { return symbol; }
        public String getOrderMode() { return orderMode; }
        public Float getVolume() { return volume; }
        public Float getPrice() { return price; }
        public String getFillPolicy() { return fillPolicy; }
        public String getOrderType() { return orderType; }
        public boolean isExpectSuccess() { return expectSuccess; }
    }

