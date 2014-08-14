package com.objectfrontier.common.xml.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.objectfrontier.arch.xml.XMLWorker;
import com.objectfrontier.arch.xml.XMLWorkerException;
import com.objectfrontier.arch.xml.descriptor.InvalidDescriptorException;
import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * This class contains the utility methods that is responsible for
 * loading and saving the XML file.
 *
 * @author arvind
 * @date   Jul 15, 2004
 * @since  IMS 1.0; Jul 15, 2004
 * @see    XMLWorker
 *
 * @author Gunaseelan
 * @date   Jun 30, 2006
 * @since  AML Jul 01, 2006 from framework
 */
public class XMLUtil {


    /**
     * This method is used to load the xml descriptor information and
     * store the same in corresponding descripotr object and returns
     * the descriptor containing xml information
     *
     * @param   inputStream
     * @param   descriptor
     * @throws  InvalidDescriptorException
     */
    public static Element loadXML (InputStream inputStream)
    throws InvalidDescriptorException  {

        try {
            XMLWorker xmlWorker = new XMLWorker();
            Document descriptorDocument = xmlWorker.read(inputStream);
            return descriptorDocument.getDocumentElement();
        } catch ( Throwable  e ) {
            throw new InvalidDescriptorException(e.getMessage());
        }
    }

    public static Document loadDocument(InputStream inputStream)
    throws InvalidDescriptorException  {

        try {
            XMLWorker xmlWorker = new XMLWorker();
            return xmlWorker.read(inputStream);
        } catch ( Throwable  e ) {
            throw new InvalidDescriptorException(e.getMessage());
        }
    }
    
    /**
     * This method is used to load the xml descriptor information and
     * store the same in corresponding descripotr object and returns
     * the descriptor containing xml information
     */
    public static Element loadXML(String xmlFile)
    throws InvalidDescriptorException , FileNotFoundException {
        return loadXML(new FileInputStream(xmlFile));
    }

    public static Document loadDocument(String xmlFile)
    throws InvalidDescriptorException , FileNotFoundException {
        return loadDocument(new FileInputStream(xmlFile));
    }
    
    /**
     * This method is used to save the node information as xml file
     * given the node object and fileName. This inturn invokes the
     * overloaded method for saveXML with node and InputStream
     *
     * @param node
     * @param fileName
     * @throws XMLWorkerException
     */
    public static void saveXML(Node node, String fileName)
    throws XMLWorkerException {

        try {
        saveXML(node, new FileOutputStream(fileName));
        } catch (FileNotFoundException e ) {
            throw new XMLWorkerException(e.getMessage());
        }
    }

    /**
     * This method saves the node object into xml file with the
     * help of outputstream. This uses the XMLWorker to save
     * the xml file
     *
     * @param node
     * @param outputStream
     * @throws XMLWorkerException
     */
    public static void saveXML(Node node, OutputStream outputStream)
    throws XMLWorkerException {
        XMLWorker worker = new XMLWorker();
        worker.write(node, outputStream);
    }


    public static String getAttribute(Element e, String name, String defaultValue) {
        return (e.hasAttribute(name)) ? e.getAttribute(name) : defaultValue;
    }

    public static String getStringValue(Element e, String xPath) {

        return getStringValue(e, xPath, null);
    }


    /**
     * This method returns the text node of the specified xPath
     *      i.e., get the node specified to the xPath from the element and
     *          return the text node from that extracted xPath node
     *
     * if the xPath is null or empty,
     *      this will return the text node of the inputted element directly
     *
     * @param e
     * @param xPath
     * @param defaultValue
     * @return
     */
    public static String getStringValue(Element e, String xPath, String defaultValue) {

        Node node = getNode(e, xPath + (xPath != null && xPath.length() > 0 ? "/" : "" ) + "text()");
        return (node == null) ? defaultValue : node.getNodeValue();
    }

    public static Integer getIntegerValue(Element e, String xPath)
    throws NumberFormatException {

        String value = getStringValue(e, xPath);
        return (value == null) ? null : Integer.valueOf(value);
    }

    public static Integer getIntegerValue(Element e, String xPath, Integer defaultValue) {

        String value = getStringValue(e, xPath);
        try {
            return (value == null) ? defaultValue : Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static Long getLongValue(Element e, String xPath)
    throws NumberFormatException {

        String value = getStringValue(e, xPath);
        return (value == null) ? null : Long.valueOf(value);
    }

    public static Long getLongValue(Element e, String xPath, Long defaultValue) {

        String value = getStringValue(e, xPath);
        try {
            return (value == null) ? defaultValue : Long.valueOf(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static Element getElement(Node parent, String xPath) {

        try {
            Node node = XPathAPI.selectSingleNode(parent, xPath);
            return (node instanceof Element) ? (Element)node : null;
        } catch (TransformerException e) {
            return null;
        }
    }

    public static Node getNode(Node parent, String xPath) {

        try {
            return XPathAPI.selectSingleNode(parent, xPath);
        } catch (TransformerException e) {
            return null;
        }
    }

    public static NodeList processXPath(Node parent, String xPath) {

        try {
            return XPathAPI.selectNodeList(parent, xPath);
        } catch (TransformerException e) {
            return null;
        }
    }

}
