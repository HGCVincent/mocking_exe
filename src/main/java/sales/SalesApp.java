package sales;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

	SalesDao salesDao;
	SalesReportDao salesReportDao;
	EcmService ecmService;

	public SalesApp() {
		salesDao = new SalesDao();
		salesReportDao = new SalesReportDao();
		ecmService = new EcmService();
	}

	public void generateSalesActivityReport(String salesId, boolean isNatTrade) {
		Sales sales = getSales(salesId);
		if (sales == null) return;

		List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);

		List<String> headers = getHeader(isNatTrade);

		SalesActivityReport report = this.generateReport(headers, reportDataList);

		uploadReportDocument(report);
	}

	public Sales getSales(String salesId) {
		if (salesId == null) {
			return null;
		}
		Sales sales = salesDao.getSalesBySalesId(salesId);

		Date today = new Date();
		if (today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom())){
			return null;
		}
		return sales;
	}

	public void uploadReportDocument(SalesActivityReport report) {
		ecmService.uploadDocument(report.toXml());
	}

	public List<String> getHeader(boolean isNatTrade) {
		List<String> headers = null;
		if (isNatTrade) {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		} else {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		}
		return headers;
	}

	private SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}
