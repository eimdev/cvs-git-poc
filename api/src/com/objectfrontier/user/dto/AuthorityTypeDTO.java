// FrontEnd Plus GUI for JAD
// DeCompiled : AuthorityTypeDTO.class

package com.objectfrontier.user.dto;

import com.objectfrontier.arch.server.dto.ClientInfo;
import com.objectfrontier.user.client.vo.AuthorityTypeValueObject;

// Referenced classes of package com.objectfrontier.user.dto:
//            UserDTO

public class AuthorityTypeDTO
    implements ClientInfo
{

    public AuthorityTypeValueObject authorityTypeVO;
    public UserDTO userDTO;
    public UserDTO reportingAuthorityDTO;

    public AuthorityTypeDTO()
    {
    }
}
