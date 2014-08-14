/* $Header$ */

/*
 * @(#)MsgIFSCMasterHistoryDTO.java
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
package com.objectfrontier.rtgs.dto;

import com.objectfrontier.arch.server.dto.ClientInfo;

import com.objectfrontier.rhs.meta.client.vo.MsgIFSCMasterHistoryValueObject;

/**
 * @author anbus
 * @date   Jan 4, 2005
 * @since  RTGS 1.0; Jan 4, 2005
 */
public class MsgIFSCMasterHistoryDTO
implements ClientInfo {

    public MsgIFSCMasterHistoryValueObject ifscMsgVO;
}
