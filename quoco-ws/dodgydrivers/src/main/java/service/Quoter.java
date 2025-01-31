package service;
import service.core.AbstractQuotationService;
import service.core.ClientInfo;
import service.core.Quotation;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.Endpoint;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style = Style.RPC, use = Use.LITERAL)
public class Quoter extends AbstractQuotationService {
	// All references are to be prefixed with an AF (e.g. AF001000)
	public static final String PREFIX = "DD";
	public static final String COMPANY = "Dodgy Drivers Corp.";;

	public static void main(String[] args) {
		try {
			Endpoint endpoint = Endpoint.create(new Quoter());
			HttpServer server = HttpServer.create(new InetSocketAddress(9001), 5);
			server.setExecutor(Executors.newFixedThreadPool(5));
			HttpContext context = server.createContext("/quotation");
			endpoint.publish(context);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Quote generation: 30% discount for being male 2% discount per year over 60
	 * 20% discount for less than 3 penalty points 50% penalty (i.e. reduction in
	 * discount) for more than 60 penalty points
	 */
	@WebMethod
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 800 and 1000
		double price = generatePrice(800, 200);
		
		// 5% discount per penalty point (3 points required for qualification)
		int discount = (info.points > 3) ? 5*info.points:-50;
		
		// Add a no claims discount
		discount += getNoClaimsDiscount(info);
		
		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 10*info.noClaims;
	}


}
