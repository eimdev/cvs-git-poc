package com.objectfrontier.common.xml.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.objectfrontier.arch.xml.descriptor.InvalidDescriptorException;

/**
 * This class contains utility method on Node object
 * Currently this class contains method for following operation on nodes
 *     1. get the  node of text node contained within an element
 *     2. get the text-value of the text node contained with in an element
 *     3. find an element of particular tag from the the given NodeList
 *     4. add a text to a node, whose tag and parentNode element is given
 *
 * @author Gunaseelan
 * @date   Jun 30, 2006
 * @since  AML Jul 01, 2006 from framework
 */
public class NodeUtil {

    /**
     * Utility method to get node of the text node contained with in
     * an element.
     *
     * @param parent Parent node from where the specified node required to be
     * retrieved
     * @param tag  tag name in given parent for which node to be returned
     * @return string the value
     */
    public static Node getNonMandatoryNode(Node parent, String tag) {

        return findFirstNode(parent.getChildNodes(), tag);
    }

    /**
     * Utility method to get text-value of the text node contained with in
     * an element.
     *
     * @param parent
     * @param tag
     * @return string the value
     */
    public static String getMandatoryNodeValue(Node parent, String tag)
    throws InvalidDescriptorException {

        Element child = (Element) findFirstNode(parent.getChildNodes(), tag);
        if (child == null ) {
            throw new InvalidDescriptorException("Could not find element for:" +
                                                  tag);
        }
        return getValue(child);
    }

    /**
     * Utility method to get text-value of tha text node contained with in
     * an element.
     *
     * @param parent
     * @param tag
     *
     * @return string the value
     */
    public static String getNonMandatoryNodeValue(Node parent, String tag) {

        Element child = (Element)findFirstNode(parent.getChildNodes(), tag);
        if (child == null ) {
            return null;
        }
        return getValue(child);
    }

    /**
     * Utility method to get int-value of tha text node contained with in
     * an element.
     *
     * @param parent
     * @param tag
     *
     * @return string the value
     */
    public static int getNonMandatoryNodeIntValue(Node parent, String tag) {
        return Integer.parseInt(getNonMandatoryNodeValue(parent, tag));
    }


    /**
     * Utility method to get long-value of tha text node contained with in
     * an element.
     *
     * @param parent
     * @param tag
     *
     * @return string the value
     */
    public static long getNonMandatoryNodeLongValue(Node parent, String tag) {
        return Long.parseLong(getNonMandatoryNodeValue(parent, tag));
    }

    /**
     * Utility method to get long-value of tha text node contained with in
     * an element.
     *
     * @param parent
     * @param tag
     *
     * @return string the value
     */
    public static double getNonMandatoryNodeDoubleValue(Node parent, String tag) {
        return Double.parseDouble(getNonMandatoryNodeValue(parent, tag));
    }


    /**
     * Utility method to get long-value of tha text node contained with in
     * an element.
     *
     * @param parent
     * @param tag
     *
     * @return string the value
     */
    public static float getNonMandatoryNodeFloatValue(Node parent, String tag) {
        return Float.parseFloat(getNonMandatoryNodeValue(parent, tag));
    }


    /**
     * Utility method to get long-value of tha text node contained with in
     * an element.
     *
     * @param parent
     * @param tag
     *
     * @return string the value
     */
    public static boolean getNonMandatoryNodeBooleanValue(Node parent, String tag) {
        return Boolean.valueOf(getNonMandatoryNodeValue(parent, tag)).booleanValue();
    }

    /**
     * Utility method to get text-value of the text node contained with in
     * an element.
     *
     * @param child
     * @return string the value
     */
    public static String getValue(Node child) {

        Node text = child.getFirstChild();
        return text == null ? null : text.getNodeValue();
    }

    /**
     * Utility method to find an element of particular tag from the the given
     * NodeList
     *
     * @param list contains the list of nodes
     * @param tag name
     * @return Node
     */
    public static Node findFirstNode(NodeList list, String tag) {

        int size = list.getLength();
        for (int i=0; i < size; i++ ) {
            Node node = list.item(i);
            if (node.getNodeName().equals(tag)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Utility mehtod to add a text to a node, whose tag and parentNode
     * element is given.
     * Just to ease of use, else we need to repeat this code all over the place.
     *
     * @param document
     * @param parent node
     * @param tag
     * @param va;ie
     *
     * @return Noder
     */
    public static Node addDataNode(Document document, Node parent,
                                   String tag, String value) {

         Element child = document.createElement(tag);
         if (value != null ) {
            Node text = document.createTextNode(value);
            child.appendChild(text);
         }
         parent.appendChild(child);
         return child;
    }

    /**
     * This utility method is to get the node based on the tag and
     * child node name
     *
     * @param parent node
     * @param tag of the node
     * @param childNodeName based on this node will be return
     * @param childNodeValue
     *
     * @return
     *
     * @throws InvalidDescriptorException
     */
    public static Node getMandatoryNode(Node parent,
                                        String tag,
                                        String childNodeName,
                                        String childNodeValue)
    throws InvalidDescriptorException {

        return findFirstNode(parent.getChildNodes(), tag,
                childNodeName, childNodeValue);
    }

    /**
     * Utility method to find an element of particular tag from the the given
     * NodeList
     *
     * @param list
     * @param tag name
     * @param childNodeName
     * @param childNodeValue
     *
     * @return Node
     */
    public static Node findFirstNode(NodeList list,
                                     String tag,
                                     String childNodeName,
                                     String childNodeValue)
    throws InvalidDescriptorException {

        int size = list.getLength();
        for (int i=0; i < size; i++ ) {
            Node node = list.item(i);
            if (node.getNodeName().equals(tag)) {
                String nodeName = getMandatoryNodeValue(node, childNodeName);
                if (childNodeValue.equals(nodeName)) {
                    return node;
                }
            }
        }
        return null;
    }

    public static String LISTDELIMITTER = "^";

    /**
     * Utility method to get the list of values from a given node separated by ^ symbol
     *
     * @return
     */
    public static List getStringList(Element parent, String tag) {

        return getStringList(parent, tag, LISTDELIMITTER);
    }


    /**
     * Utility method to get the list of values from a given node separated by ^ symbol
     *
     * @return
     */
    public static List getStringList(Element parent, String tag, String delimiter) {

        String value = NodeUtil.getNonMandatoryNodeValue(parent, tag);

        return getStringList(value, delimiter);
    }


    @SuppressWarnings("unchecked")
    public static List getStringList(String value, String delimiter) {
        List values = new ArrayList();

        if (value == null) return values;
        StringTokenizer tokenizer = new StringTokenizer(value, delimiter);

        while (tokenizer.hasMoreTokens())
            values.add(tokenizer.nextToken().trim());

        return values;
    }

    public static String getListString(List data) {

        return getListString(data, LISTDELIMITTER);
    }

    public static String getListString(List data, String delimiter) {

        StringBuffer sb = new StringBuffer();
        if (data == null) return sb.toString();
        for (Iterator i = data.iterator(); i.hasNext(); ) {
            sb.append(i.next());
            if (i.hasNext()) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }


    /**
     * Method used to get the delimitted String from String array
     *
     * @param data
     * @param delimiter
     * @return
     */
    public static String getStringArrayAsString(String[] data, String delimiter) {

        StringBuffer sb = new StringBuffer();
        if (data == null) return sb.toString();
        for (int i = 0; i< data.length; i++) {
            sb.append(data[i]);
            sb.append(delimiter);
        }
        sb.deleteCharAt(sb.lastIndexOf(delimiter));

        return sb.toString();
    }

    public static String getStringArrayAsString(String[] data) {

        return getStringArrayAsString(data, LISTDELIMITTER);
    }

    /**
     * Method used to get the string array from the given delimitted string
     * @param data
     * @param delimiter
     * @return
     */
    public static String[] getStringArrayFromString(String data, String delimiter) {

        if (data == null) return null;
        StringTokenizer tokenizer = new StringTokenizer(data, delimiter);

        String[] resultData = new String[tokenizer.countTokens()];
        int i = 0;

        while (tokenizer.hasMoreTokens())
            resultData[i++] = tokenizer.nextToken().trim();

        return resultData;
    }

    public static String[] getStringArrayFromString(Element parent, String tag, String delimiter) {

        String value = NodeUtil.getNonMandatoryNodeValue(parent, tag);
        return getStringArrayFromString(value, delimiter);
    }

    public static String[] getStringArrayFromString(Element parent, String tag) {

        return getStringArrayFromString(parent, tag, LISTDELIMITTER);
    }

}