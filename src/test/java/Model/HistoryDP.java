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
                  { new OrderDataModel("BTCUSD", "Market Execution", null, null, "Fill or Kill", "Buy", false) },

                // TC003: Volume < 0.01
                  { new OrderDataModel("BTCUSD", "Market Execution", (float) 0.0001f, null, "Fill or Kill", "Buy", true)},

                // TC004: Volume > max
              // { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "Fill or Kill", "Buy", false) },


                // TC006: Không chọn Symbol
                  { new OrderDataModel("", "Market Execution", 0.1f, null, "Fill or Kill", "Buy", false) },

                // TC007: Không chọn Order Type
                  { new OrderDataModel("BTCUSD", "Market Execution", 1.0f, null, "Fill or Kill", null, false) },

                // TC008: Pending Order thành công
                  { new OrderDataModel("BTCUSD", "Pending Order", 1.0f, (float) 0, "", "Buy Limit", true) },
                // TC002: Volume trống
                  { new OrderDataModel("BTCUSD", "Pending Order", null, (float) 0, "", "Buy Limit", false) },

                // TC003: Volume < 0.01
                  { new OrderDataModel("BTCUSD", "Pending Order", 0.005f, 115254.82f, "", "Buy Limit", false) },

                // TC004: Volume > max
                // { new OrderDataModel("EURUSD", "Market Execution", 0.005f, null, "Fill or Kill", "Buy Limit", false) },
              //   TC005: Fill Price Empty
                  { new OrderDataModel("BTCUSD", "Pending Order", 0.01f, null, "", "Buy Limit", false) },

                // TC006: Không chọn Symbol
                  { new OrderDataModel("", "Pending Order", 0.1f,(float) 0, "", "Buy Limit", false) },

                // TC007: Không chọn Order Type
              //    { new OrderDataModel("BTCUSD", "Pending Order", 1.0f, (float) 0, "", null, false) }

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
