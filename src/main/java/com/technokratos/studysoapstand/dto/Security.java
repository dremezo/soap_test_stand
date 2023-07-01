package com.technokratos.studysoapstand.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.technokratos.studysoapstand.controller.TokenControllerSoap.SECURITY_NS;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = SECURITY_NS, name = "security")

public class Security {

    @XmlElement(namespace = SECURITY_NS, name = "token")
    private String token;

}