
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.Quoter;
import service.QuoterService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public class Broker {
    static ArrayList<QuoterService> qServices = null;

    public static void main(String[] args) throws MalformedURLException {
        for (String url : args) {
            System.out.println("url=" + url);
        }
        try {

            Endpoint endpoint = Endpoint.create(new Broker());
            HttpServer server = HttpServer.create(new InetSocketAddress(9000), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/broker");
            endpoint.publish(context);
            
            server.start();
            
        } catch (Exception e) {
            System.out.println("error " + e);
        }
                        for (String url : args) {
                            System.out.println("url=" + url);
                            URL wsdlUrl = new URL(url);
                            QName serviceName = new QName("http://core.service/", "QuoterService");
                            Service service = Service.create(wsdlUrl, serviceName);
                            QName portName = new QName("http://core.service/", "QuoterPort");
                            QuoterService quoter=service.getPort(portName, QuoterService.class);
                            System.out.println("Quoter="+quoter);
                            qServices.add( quoter );
                        }
    }

    @WebMethod
    public ArrayList<Quotation> getQuotations(ClientInfo info) {

        ArrayList<Quotation> quotations = new ArrayList<Quotation>();
        for (Quoter q : qServices) {
            Quotation quotation = q.generateQuotation(info);
            quotations.add(quotation);
        }

        return quotations;
    }

}
