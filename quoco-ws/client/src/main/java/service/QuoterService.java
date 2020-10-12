package service.core;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import service.core.ClientInfo;
import service.core.Quotation;

@WebService
@SOAPBinding(style = Style.RPC, use = Use.LITERAL)

public interface QuoterService {
    @WebMethod public Quotation generateQuotation(ClientInfo info);
 }