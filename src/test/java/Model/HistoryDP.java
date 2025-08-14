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
                // TC001: Market Execution thành công
                { new OrderDataModel("BTCUSD", "Market Execution", 1.0f, null, "Fill or Kill", "Buy", true) },

                // TC002: Volume trống
                { new OrderDataModel("EURUSD", "Market Execution", null, null, "Fill or Kill", "Buy", false) },

                // TC003: Volume < 0.01
                { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "Fill or Kill", "Buy", false) },

                // TC004: Volume > max
               // { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "Fill or Kill", "Buy", false) },
                // TC005: Fill Policy Empty
                // { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "", "Buy", false) },

                // TC006: Không chọn Symbol
                { new OrderDataModel("", "Market Execution", 0.1f, null, "Fill or Kill", "Buy", false) },

                // TC007: Không chọn Order Type
                { new OrderDataModel("EURUSD", "Market Execution", 1.0f, null, "Fill or Kill", "", false) },

                // TC008: Pending Order thành công
                { new OrderDataModel("BTCUSD", "Pending Order", 1.0f, null, "", "Buy Limit", true) },
                // TC002: Volume trống
                { new OrderDataModel("EURUSD", "Pending Order", null, null, "", "Buy", false) },

                // TC003: Volume < 0.01
                { new OrderDataModel("EURUSD", "Pending Order", 0.005f, null, "", "Buy", false) },

                // TC004: Volume > max
                // { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "Fill or Kill", "Buy", false) },
              //   TC005: Fill Price Empty
                 { new OrderDataModel("EURUSD", "Pending Order", 0.005f, null, "", "Buy", false) },

                // TC006: Không chọn Symbol
                { new OrderDataModel("", "Pending Order", 0.1f, null, "", "Buy", false) },

                // TC007: Không chọn Order Type
                { new OrderDataModel("EURUSD", "Pending Order", 1.0f, null, "", "", false) }

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
