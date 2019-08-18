package sales;

import org.junit.Test;

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
}
