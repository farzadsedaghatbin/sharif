package com.isc.npsd.sharif.model.entities;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * Created by a_jankeh on 2/5/2017.
 */
public enum FileType {

    TRANSACTION("com.isc.npsd.sharif.model.entities.schemaobjects.trx", "trx.xsd");

    private String schemaContext;
    private String xsdName;
    private Schema xsdSchema;

    FileType(String schemaContext, String xsdName) {
        this.schemaContext = schemaContext;
        this.xsdName = xsdName;
    }

    public String getSchemaContext() {
        return schemaContext;
    }

    public String getXsdName() {
        return xsdName;
    }

    public Schema getXSDSchema() throws SAXException {
        if (xsdSchema == null) {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            xsdSchema = schemaFactory.newSchema(new StreamSource(this.getClass().getClassLoader().getResourceAsStream("/schema/" + this.xsdName)));
        }
        return xsdSchema;
    }
}
