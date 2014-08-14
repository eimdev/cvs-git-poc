/* $Header$ */

/*
 * @(#)ListMap.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 2050 Marconi Drive, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */
package com.objectfrontier.rtgs.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author pRasanna
 * @date   Aug 7, 2004
 * @since  Fetrem 1.0; Aug 7, 2004
 */
public class ListMap
extends java.util.HashMap {

    public ListMap() {
        super();
    }

    public ListMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ListMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ListMap(Map m) {
        super(m);
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(Object listID, Object listObject) {

        List list = (List)super.get(listID);
        if (list == null) {
            list = new ArrayList();
        }
        list.add(listObject);

        return super.put(listID, list);
    }
}