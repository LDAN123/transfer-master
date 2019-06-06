
package com.capgemini.transfer.ws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetActivityStatusExResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getActivityStatusExResult"
})
@XmlRootElement(name = "GetActivityStatusExResponse")
public class GetActivityStatusExResponse {

    @XmlElement(name = "GetActivityStatusExResult")
    protected String getActivityStatusExResult;

    /**
     * Gets the value of the getActivityStatusExResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetActivityStatusExResult() {
        return getActivityStatusExResult;
    }

    /**
     * Sets the value of the getActivityStatusExResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetActivityStatusExResult(String value) {
        this.getActivityStatusExResult = value;
    }

}
