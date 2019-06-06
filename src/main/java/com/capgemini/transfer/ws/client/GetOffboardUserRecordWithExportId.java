
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
 *         &lt;element name="Ticket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HRDataID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExportID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "ticket",
    "hrDataID",
    "exportID"
})
@XmlRootElement(name = "GetOffboardUserRecordWithExportId")
public class GetOffboardUserRecordWithExportId {

    @XmlElement(name = "Ticket")
    protected String ticket;
    @XmlElement(name = "HRDataID")
    protected String hrDataID;
    @XmlElement(name = "ExportID")
    protected String exportID;

    /**
     * Gets the value of the ticket property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * Sets the value of the ticket property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicket(String value) {
        this.ticket = value;
    }

    /**
     * Gets the value of the hrDataID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHRDataID() {
        return hrDataID;
    }

    /**
     * Sets the value of the hrDataID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHRDataID(String value) {
        this.hrDataID = value;
    }

    /**
     * Gets the value of the exportID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExportID() {
        return exportID;
    }

    /**
     * Sets the value of the exportID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExportID(String value) {
        this.exportID = value;
    }

}
