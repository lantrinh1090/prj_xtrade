package Dataprovider;

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
}
