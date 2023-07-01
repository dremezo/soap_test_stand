package com.technokratos.studysoapstand.dto;

import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRegistry;

@NoArgsConstructor
@XmlRegistry
public class ObjectFactory {

    public SimpleRequest getSimpleRequest() {
        return new SimpleRequest();
    }

    public CheckedRequest getCheckedRequest() {
        return new CheckedRequest();
    }

    public SimpleResponse getSimpleResponse() {
        return new SimpleResponse();
    }

}
