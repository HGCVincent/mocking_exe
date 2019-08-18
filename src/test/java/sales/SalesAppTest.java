package sales;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SalesAppTest {

    @Test
    public void testGenerateReport_givenNotFoundSale_thenReturnNull() {

        SalesApp salesApp = spy(new SalesApp());
        doReturn(null).when(salesApp).getSales(any());
        salesApp.generateSalesActivityReport("DUMMY", false);
        verify(salesApp, times(1)).getSales(any());
    }

    @Test
    public void testGenerateReport_givenSalesIdAndIsNatTrade() {
        SalesApp salesApp = spy(new SalesApp());

        doReturn(new Sales()).when(salesApp).getSales(any());
        doReturn(new ArrayList<SalesReportData>()).when(salesApp).getReportDataList(any());
        doReturn(new ArrayList<String>()).when(salesApp).getHeader(anyBoolean());
        doReturn(new SalesActivityReport()).when(salesApp).generateReport(anyList(), anyList());
        doNothing().when(salesApp).uploadReportDocument(any());
        salesApp.generateSalesActivityReport("DUMMY", false);

        verify(salesApp, times(1)).getSales(any());
        verify(salesApp, times(1)).getReportDataList(any());
        verify(salesApp, times(1)).getHeader(anyBoolean());
        verify(salesApp, times(1)).generateReport(anyList(),anyList());
        verify(salesApp, times(1)).uploadReportDocument(any());
    }
}
