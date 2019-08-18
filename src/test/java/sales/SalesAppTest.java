package sales;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

    @Mock
    SalesDao salesDao;

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

    @Test
    public void testGetSales_givenNull_thenReturnNull() {
        Sales sales = mock(SalesApp.class).getSales(null);
        assertNull(sales);
    }

    @Test
    public void testGetSales_givenSalesIdAndTodayIsAfterLastDate_thenReturnNull() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        Sales sales = mock(Sales.class);
        when(sales.getEffectiveTo()).thenReturn(calendar.getTime());
        when(sales.getEffectiveFrom()).thenReturn(calendar.getTime());
        when(salesDao.getSalesBySalesId(anyString())).thenReturn(sales);

        Sales result = mock(SalesApp.class).getSales(anyString());

        Assert.assertNull(result);
    }

    @Test
    public void testGetSales_givenSalesIdAndTodayIsBeforeLastDate_thenReturnNull() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Sales sales = mock(Sales.class);
        when(sales.getEffectiveTo()).thenReturn(calendar.getTime());
        when(sales.getEffectiveFrom()).thenReturn(calendar.getTime());
        when(salesDao.getSalesBySalesId(anyString())).thenReturn(sales);

        Sales result = mock(SalesApp.class).getSales(anyString());

        Assert.assertNull(result);
    }

    @Test
    public void testGetSales_givenSalesIdAndLegalDate_thenReturnSales() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, +1);

        Sales sales = mock(Sales.class);
        when(sales.getEffectiveTo()).thenReturn(tomorrow.getTime());
        when(sales.getEffectiveFrom()).thenReturn(yesterday.getTime());
        when(salesDao.getSalesBySalesId(anyString())).thenReturn(sales);

        Sales result = mock(SalesApp.class).getSales(anyString());

        Assert.assertNotNull(result);
    }

    @Test
    public void testgetReportDataList_givenSales_thenReturnReportDataList() {
        SalesApp salesApp = mock(SalesApp.class);
        SalesReportDao salesReportDao = mock(SalesReportDao.class);
        List<SalesReportData> reportDataList = Arrays.asList(new SalesReportData());
        when(salesReportDao.getReportData(any())).thenReturn(reportDataList);

        List<SalesReportData> result = salesApp.getReportDataList(any());

        Assert.assertEquals(1, result.size());
        Mockito.verify(salesReportDao, times(1)).getReportData(any());
    }
}
