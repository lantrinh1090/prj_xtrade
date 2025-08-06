package Model;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.testng.annotations.DataProvider;

public class HistoryDP {

    @DataProvider(name = "filterData")
    public static Object[][] getFilterData() {
        return new Object[][]{
                {"XAUUSD.s", "", "", "Profit", "", "",true},
                {"XAUUSD.s", "BUY", "In", "Profit", "2025-01-01", "2025-12-31",false},
                {"", "SELL", "", "", "", "",true},
                //  {"XAUUSD.s", "", "", "Profit", "", "",true},
                {"XAUUSD.s", "", "", "", "", "",true},
                {"", "", "", "Loss", "", "",true},
                {"", "", "Out", "", "", "",true}, // invalid date range
                {"", "", "", "", "2025-01-01", "2025-12-01", true} // invalid symbol
        };
    }
    @DataProvider(name = "orderData")
    public Object[][] orderData() {
        return new Object[][]{
                // symbol, orderType, volume, expectedSuccess
          //      { new OrderDataModel("ETHUSD", "BUY_LIMIT", "0.01", 3633.01F, 3630.03F, 3639.3F,false) },//Khối lượng không hợp lệ
         //      { new OrderDataModel("ETHUSD", "BUY_LIMIT", "0.1", 3633.01F, 3636.03F, 3639.3F,false) },//Giá Stop Loss phải thấp hơn giá đặt.
          //     { new OrderDataModel("ETHUSD", "BUY_LIMIT", "0.1", 3633.01F, 3630.03F, 3631.3F,false) },//Giá Take Profit phải cao hơn giá đặt.
                { new OrderDataModel("ETHUSD", "BUY_LIMIT", "0.1", 3617.885F, 0, 0,true) },
          //     { new OrderDataModel("ETHUSD", "BUY_LIMIT", "0.1", 36330.01F, 36300.03F, 36390.3F,false) },//Giá đặt cao hơn giá thị trường
                //   { new OrderDataModel("ETHUSD", "", "2",  111.01F, 111.03F, 112.3F,false) },
              //  { new OrderDataModel("ETHUSD", "SELL", "", 111.01F, 111.03F, 112.3F, false) },
              //  { new OrderDataModel("ETHUSD", "BUY", "0",  111.01F, 111.03F, 112.3F,false) }
        };
    }
}
