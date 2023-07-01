package com.technokratos.studysoapstand.controller;

import com.technokratos.studysoapstand.dto.CheckedRequest;
import com.technokratos.studysoapstand.dto.Security;
import com.technokratos.studysoapstand.dto.SimpleRequest;
import com.technokratos.studysoapstand.dto.SimpleResponse;
import com.technokratos.studysoapstand.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapFaultException;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static com.technokratos.studysoapstand.jwt.JwtProvider.StatusJwt.MALFORMEDJWT;
import static com.technokratos.studysoapstand.jwt.JwtProvider.StatusJwt.SIGNATURE;

@Endpoint
@Slf4j
@RequiredArgsConstructor
public class TokenControllerSoap {
    public static final String NAMESPACE_URI = "http://www.technokratos.com/soap";
    public static final String SECURITY_NS = "http://www.technokratos.com/soap/security";

    private final JwtProvider provider;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDataRequest")
    @ResponsePayload
    public SimpleResponse getSimpleRequest(@RequestPayload SimpleRequest request,
                                           @SoapHeader(value = "{" + SECURITY_NS + "}security")
                                           SoapHeaderElement securityHeader) {

        Security security = unmarshallSecurityFromSoapHeader(securityHeader);
        String token = security.getToken();

        checkToken(token);

        String subject = provider.getDataFromToken(token);
        log.info("Subject from token: " + subject);

        SimpleResponse response = new SimpleResponse();
        response.setResponse(subject);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createDataRequest")
    @ResponsePayload
    public SimpleResponse createDataRequest(@RequestPayload CheckedRequest request,
                                            @SoapHeader(value = "{" + SECURITY_NS + "}security")
                                            SoapHeaderElement securityHeader) {

        Security security = unmarshallSecurityFromSoapHeader(securityHeader);
        String token = security.getToken();

        checkToken(token);

        String subject = provider.getDataFromToken(token);
        log.info("Subject from token: " + subject);

        String value = request.getRequest();

        SimpleResponse response = new SimpleResponse();

        if(!subject.equals(value)) {
            throw new SoapFaultException(HttpStatus.BAD_REQUEST.toString());
        }

        response.setResponse("Success");
        return response;
    }

    private Security unmarshallSecurityFromSoapHeader(SoapHeaderElement soapHeaderElement) {
        Security security = null;
        try {

            JAXBContext context = JAXBContext.newInstance(Security.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            security = (Security) unmarshaller.unmarshal(soapHeaderElement.getSource());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return security;
    }

    private void checkToken(String token){
        HttpStatus status = HttpStatus.OK;

        if (token.isEmpty())
            status = HttpStatus.UNAUTHORIZED;

        JwtProvider.StatusJwt statusJwt = provider.validateToken(token);

        if (statusJwt == SIGNATURE || statusJwt == MALFORMEDJWT)
            status = HttpStatus.FORBIDDEN;

        if(status != HttpStatus.OK) {
            throw new SoapFaultException(status.toString());
        }
    }

}
