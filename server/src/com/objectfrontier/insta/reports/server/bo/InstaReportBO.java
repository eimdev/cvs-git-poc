package com.objectfrontier.insta.reports.server.bo;

import java.util.Map;

import com.objectfrontier.arch.server.bo.BOException;
import com.objectfrontier.insta.server.bo.InstaBO;

/**
 * 
 * @author jinuj
 * @date   Jul 29, 2008
 * @since  insta.reports; Jul 29, 2008
 */
public class InstaReportBO
extends InstaBO {

    private final String RTGS_CHANNEL = "RTGS";
    
    public void initialize(Map props)
    throws BOException {
        super.initialize(props);
    }
    
    protected String appendFieldString(String fieldNo, String channel, int isAlternate, int isCompound) {
        
        StringBuffer sb = new StringBuffer("");
        
        try {
            
            if(RTGS_CHANNEL.equalsIgnoreCase(channel)) {
                
                if(isCompound == 1 && isAlternate == 0) {
                    
                    sb.append(" DECODE (INSTR(UPPER (concat7495field (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )), 'VIJBH') , 0, '', " ) 
                    .append(" SUBSTR(UPPER (concat7495field (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )),                         " )    
                    .append("   INSTR(UPPER (concat7495field (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )), 'VIJBH'), 16 ) )  R_" + fieldNo );
                } else if(isCompound == 1 && isAlternate == 1) {
                    
                    sb.append(" DECODE (INSTR(UPPER (concatCompoundAltfield (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )), 'VIJBH') , 0, '', " ) 
                    .append(" SUBSTR(UPPER (concatCompoundAltfield (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )),                         " )    
                    .append("   INSTR(UPPER (concatCompoundAltfield (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )), 'VIJBH'), 16 ) )  R_" + fieldNo );
                } else {
                    
                    sb.append(" DECODE (SUBSTR (getfieldvalue (rm.msg_id, '" + fieldNo + "', m.msg_sub_type ), 0, 4 ), 'VIJBH', " )
                    .append(" getfieldvalue (rm.msg_id, '" + fieldNo + "', m.msg_sub_type ), '' ) R_" + fieldNo                     );
                }
            } else {
             
                if(isCompound == 1 && isAlternate == 0) {
                    
                    sb.append(" DECODE (INSTR(UPPER (NNN_" + fieldNo + " ), 'VIJB') , 0, '', " ) 
                    .append(" SUBSTR(UPPER (NNN_" + fieldNo + " ),                         " )    
                    .append("   INSTR(UPPER (NNN_" + fieldNo + " ), 'VIJB'), 16 ) )  N_" + fieldNo );
                } else if(isCompound == 1 && isAlternate == 1) {
                    
                    sb.append(" DECODE (INSTR(UPPER (NNN_" + fieldNo + " ), 'VIJB') , 0, '', " ) 
                    .append(" SUBSTR(UPPER (NNN_" + fieldNo + " ),                         " )    
                    .append("   INSTR(UPPER (NNN_" + fieldNo + " ), 'VIJB'), 16 ) )  N_" + fieldNo );
                } else {
                    
                    sb.append(" DECODE (SUBSTR (NNN_" + fieldNo + " , 0, 4 ), 'VIJB', " )
                    .append(" NNN_" + fieldNo + " , '' ) N_" + fieldNo                     );
                }
            }
            return sb.toString();
        } catch (Exception e) {
            
            throw new BOException("Getting Exception While Appending the String", e);
        }
        
    }
    
    protected String appendWhereString(String fieldNo, String channel, int isAlternte, int isCompound) {
        
        StringBuffer sb = new StringBuffer("");
        
        try {
            
            if(RTGS_CHANNEL.equalsIgnoreCase(channel)) {
                
                if(isCompound == 1 && isAlternte == 0) {
                    
                    sb.append(" ( UPPER(Concat7495field (rm.msg_id, '" + fieldNo + "', m.msg_sub_type)) LIKE '%VIJBH%' ")
                    .append(" AND LENGTH( SUBSTR(UPPER (concat7495field (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )),                " )    
                    .append("   INSTR(UPPER (concat7495field (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )), 'VIJBH'), 16 ) ) = 16 )    " );
                } else if(isCompound == 1 && isAlternte == 1) {
                    
                    sb.append(" ( UPPER(concatCompoundAltfield (rm.msg_id, '" + fieldNo + "', m.msg_sub_type)) LIKE '%VIJBH%' ")
                    .append(" AND LENGTH( SUBSTR(UPPER (concatCompoundAltfield (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )),                " )    
                    .append("   INSTR(UPPER (concatCompoundAltfield (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )), 'VIJBH'), 16 ) ) = 16 )    " );
                } else {    
                    
                    sb.append(" ( SUBSTR(UPPER(getfieldvalue (rm.msg_id, '" + fieldNo + "', m.msg_sub_type)), 0, 4) = 'VIJBH' ")
                    .append(" AND LENGTH(getfieldvalue (rm.msg_id, '" + fieldNo + "', m.msg_sub_type )) = 16 )               ");
                }
            } else {
                
                if(isCompound == 1 && isAlternte == 0) {
                    
                    sb.append(" ( UPPER(NNN_" + fieldNo + " ) LIKE '%VIJB%' ")
                    .append(" AND LENGTH( SUBSTR(UPPER (NNN_" + fieldNo + " ),                " )    
                    .append("   INSTR(UPPER (NNN_" + fieldNo + " ), 'VIJB'), 16 ) ) = 16 )    " );
                } else if(isCompound == 1 && isAlternte == 1) {
                    
                    sb.append(" ( UPPER(NNN_" + fieldNo + " ) LIKE '%VIJB%' ")
                    .append(" AND LENGTH( SUBSTR(UPPER (NNN_" + fieldNo + " ),                " )    
                    .append("   INSTR(UPPER (NNN_" + fieldNo + " ), 'VIJB'), 16 ) ) = 16 )    " );
                } else {    
                    
                    sb.append(" ( SUBSTR(UPPER(NNN_" + fieldNo + " ), 0, 4) = 'VIJB' ")
                    .append(" AND LENGTH(NNN_" + fieldNo + " ) = 16 )               ");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            
            throw new BOException("Getting Exception While Appending the String", e);
        }
        
    }
    
    protected String appendFinalString(String fieldNo, String channel) {
        
        StringBuffer sb = new StringBuffer("");
        
        try {
            
            if(RTGS_CHANNEL.equalsIgnoreCase(channel)) {
                
                sb.append(" DECODE(LENGTH(R_" + fieldNo + " ), 16, R_" + fieldNo + ", '') RR_" + fieldNo + "");
            } else {
             
                sb.append(" DECODE(LENGTH(N_" + fieldNo + " ), 16, N_" + fieldNo + ", '') NN_" + fieldNo + "");
            }
            return sb.toString();
        } catch (Exception e) {
            
            throw new BOException("Getting Exception While Appending the String", e);
        }
        
    }
    
    protected String appendSelectString(String fieldNo) {
        
        StringBuffer sb = new StringBuffer("");
        
        try {
            //Commented the below line for linespace and carriage return in the UTR field, 20100930
            //sb.append(" ( SELECT mfs.VALUE                               ")
            sb.append(" ( SELECT replace(replace(mfs.VALUE,CHR(10),''),CHR(13),'')    ")
              .append("   FROM msgdefn md,                               ")
              .append("        msgblockdefn mbd,                         ")
              .append("        msgfieldblockdefn mfb,                    ")
              .append("        msgfielddefn mfd,                         ")
              .append("        msgfieldtype mft,                         ")
              .append("        msgfield_stage mfs                        ")
              .append(" WHERE md.ID = mbd.msg_defn_id                    ")
              .append("   AND mbd.ID = mfb.block_id                      ")
              .append("   AND mfb.ID = mfd.field_block_id                ")
              .append("   AND mfd.default_field_type_id = mft.ID         ")
              .append("   AND mft.ID = mfs.msg_field_type_id             ")
              .append("   AND mft.NO = '"+ fieldNo + "'                  ")
              .append("   AND mfs.msg_id = m.msg_id                      ")
              .append("   AND md.ID = (SELECT MAX (ID)                   ")    
              .append("                  FROM msgdefn                    ")
              .append("                 WHERE sub_type = m.msg_sub_type))");
            return sb.toString();
        } catch (Exception e) {
            
            throw new BOException("Getting Exception While Appending the String", e);
        }
        
    }
    
    

}
