import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import service.core.ClientInfo;
import service.core.Quotation;
@WebService
public interface BrokerService {
    @WebMethod
    public ArrayList<Quotation> getQuotations(ClientInfo info);
}
 