/*
 * @(#)AbstractUserDTO.java
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
package com.objectfrontier.user.dto;

import com.objectfrontier.arch.context.Context;

/**
 * @author gunaseelanv
 * @date   Aug 29, 2006
 * @since  AML 1.0; Aug 29, 2006
 */
public abstract class AbstractUserDTO
implements com.objectfrontier.arch.server.dto.ClientInfo,
           com.objectfrontier.common.dto.Auditable {

    public Context context;
}
