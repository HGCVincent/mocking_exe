package sales;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

    @Mock
    SalesReportDao salesReportDao;

    @Mock
    EcmService ecmService;

    @InjectMocks
    SalesApp mockSalesApp;

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
    public void testGetReportDataList_givenSale_thenReturnReportDataList() {
        Sales sales = new Sales();
        SalesReportData salesReportData = mock(SalesReportData.class);
        List<SalesReportData> reportDatas = Arrays.asList(salesReportData);
        when(salesReportDao.getReportData(sales)).thenReturn(reportDatas);

        List<SalesReportData> result = mockSalesApp.getReportDataList(sales);

        Assert.assertEquals(1,result.size());
        verify(salesReportDao,times(1)).getReportData(sales);
    }

    @Test
    public void testGetHeader_givenTrue_thenReturnHeaders() {
        SalesApp salesApp = new SalesApp();
        boolean isNatTrade = true;

        List<String> result = salesApp.getHeader(isNatTrade);

        assertEquals("Time",result.get(3));
    }

    @Test
    public void testGetHeader_givenFalse_thenReturnHeaders() {
        SalesApp salesApp = new SalesApp();
        boolean isNatTrade = false;

        List<String> result = salesApp.getHeader(isNatTrade);

        assertEquals("Local Time",result.get(3));
    }

    @Test
    public void testUploadReportDocument_givenSalesActivityReport() {
        SalesActivityReport salesActivityReport = new SalesActivityReport();
        mockSalesApp.uploadReportDocument(salesActivityReport);
        Mockito.verify(ecmService, times(1)).uploadDocument(anyString());
    }
}
