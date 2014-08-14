/* $Header$ */

/*
 * @(#)MsgHistoryDTO.java
 *
 * Copyright by ObjectFrontier, Inc.,
 * 12225 Broadleaf Lane, Alpharetta, GA 30005, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of ObjectFrontier, Inc. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with ObjectFrontier.
 */
package com.objectfrontier.rtgs.dto;

import com.objectfrontier.arch.server.dto.ClientInfo;

import com.objectfrontier.rhs.meta.client.vo.MsgHistoryValueObject;

/**
 * This class implements <code>ClientInfo</code> of framework which 
 * inturn is implementing <code>java.io.Serializable</code> as this object will 
 * be transferred across network. The MsgHistoryDTO transfers message history 
 * in the form of value object.
 * 
 * @author Malar; Aug 16, 2004
 * @date   Aug 16, 2004
 * @since  RHS API 1.0; Aug 16, 2004
 * @see    
 */
public class MsgHistoryDTO
implements ClientInfo {
    
    public MsgHistoryValueObject msgHistoryVO;
    
}

