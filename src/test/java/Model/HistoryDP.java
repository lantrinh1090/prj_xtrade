package Model;

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.testng.annotations.DataProvider;
import Page.NewOrderPage;

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
                // TC001: Market Execution thành công
                { "TC001: Market Execution thành công(Buy)",new OrderDataModel("BTCUSD", "Market Execution", 1.0f, null, "Fill or Kill", "Buy", true) },
                  { "TC001: Market Execution thành công(Sell)",new OrderDataModel("BTCUSD", "Market Execution", 1.0f, null, "Fill or Kill", "Sell", true) },

                // TC002: Volume trống
                  { "TC002: Volume trống(Market Execution) ",new OrderDataModel("BTCUSD", "Market Execution", null, null, "Fill or Kill", "Buy", false) },

                // TC003: Volume < 0.01
                  { "TC003: Volume < 0.01(Market Execution)",new OrderDataModel("BTCUSD", "Market Execution", (float) 0.0001f, null, "Fill or Kill", "Buy", false)},

                // TC004: Volume > max
              // { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "Fill or Kill", "Buy", false) },


                // TC006: Không chọn Symbol
                  {"TC007: Không chọn Order Type(Market Execution)", new OrderDataModel("", "Market Execution", 0.1f, null, "Fill or Kill", "Buy", false) },

                // TC007: Không chọn Order Type
                  { "TC007: Không chọn Order Type(Market Execution)",new OrderDataModel("BTCUSD", "Market Execution", 1.0f, null, "Fill or Kill", null, false) },

                // TC008: Pending Order thành công
                  {"TC0081: Pending Order thành công(Buy Limit)", new OrderDataModel("BTCUSD", "Pending Order", 1.0f,  0f, "", "Buy Limit", true) },
                  {"TC0082: Pending Order thành công(Sell Limit ETHUSD)",new OrderDataModel("ETHUSD", "Pending Order", 1.0f, 0f, "", "Sell Limit", true) },
                  {"TC0083: Pending Order thành công(Sell Limit XAUAUD)",new OrderDataModel("XAUAUD", "Pending Order", 1.0f,  0f, "", "Sell Limit", true) },
                  {"TC0081: Pending Order thành công(Buy Stop)", new OrderDataModel("BTCUSD", "Pending Order", 1.0f,  0f, "", "Buy Stop", true) },
                  {"TC0082: Pending Order thành công(Sell Stop ETHUSD)",new OrderDataModel("ETHUSD", "Pending Order", 1.0f, 0f, "", "Sell Stop", true) },
                  {"TC0083: Pending Order thành công(Sell Stop Limit XAUAUD)",new OrderDataModel("XAUAUD", "Pending Order", 1.0f, 0f, "", "Sell Limit", true) },
                  {"TC0081: Pending Order thành công(Buy Stop)", new OrderDataModel("BTCUSD", "Pending Order", 1.0f, 0f, "", "Buy Stop", true) },
                // TC002: Volume trống
                  { "TC002: Volume trống(Pending Order)",new OrderDataModel("BTCUSD", "Pending Order", null, 0f, "", "Buy Limit", false) },

                // TC003: Volume < 0.01
                  { "TC003: Volume < 0.01(Pending Order)", new OrderDataModel("BTCUSD", "Pending Order", 0.005f, 115254.82f, "", "Buy Limit", false) },

                // TC004: Volume > max
                // { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "Fill or Kill", "Buy Limit", false) },
              //   TC005: Fill Price Empty
                  {"TC005: Fill Price Empty(Pending Order)", new OrderDataModel("BTCUSD", "Pending Order", 0.01f, null, "", "Buy Limit", false) },

                // TC006: Không chọn Symbol
                  { "TC006: Không chọn Symbol(Pending Order)",new OrderDataModel("", "Pending Order", 0.1f,0f, "", "Buy Limit", false) },

                // TC007: Không chọn Order Type
                  {"TC007: Không chọn Order Type(Pending Order)", new OrderDataModel("BTCUSD", "Pending Order", 1.0f, 0f, "", null, false) }


        };
    }
    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][] {
                { "valid_user", "wrong_pass", "serverA" },
                { "wrong_user", "valid_pass", "serverA" },
                { "", "valid_pass", "serverA" },
                { "valid_user", "", "serverA" },
                { "valid_user", "valid_pass", "" },
                { "", "", "" }
        };
    }
}
