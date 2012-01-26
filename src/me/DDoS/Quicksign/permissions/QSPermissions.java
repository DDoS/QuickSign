package me.DDoS.Quicksign.permissions;

/**
 *
 * @author DDoS
 */
public enum QSPermissions {
    
    USE("quicksign.use"),
    ALLOW_ICS("quicksign.allowics"),
    FS("quicksign.fastsign"),
    FS_NO_INV("quicksign.fsnoinv"),    
    WG_MEMBER("quicksign.wgmember"),   
    WG_OWNER("quicksign.wgowner"),    
    WG_CAN_BUILD("quicksign.wgcanbuild"),
    RE_CAN_BUILD("quicksign.recanbuild"),
    RE_CAN_BUILD_FP("quicksign.recanbuildfp"),
    RS_CAN_BUILD("quicksign.rscanbuild"),    
    RS_CAN_BUILD_FP("quicksign.rscanbuildfp"),    
    LWC_CAN_ACCESS("quicksign.lwccanaccess"),    
    LWC_CAN_ACCESS_FP("quicksign.lwccanaccessfp"),    
    FREE_USE("quicksign.freeuse"),   
    NO_REACH_LIMIT("quicksign.noreachlimit"),
    COLOR_CMD("quicksign.colorcmd"),   
    COLOR_DYE("quicksign.colordyes"),   
    COLOR_SIGN_CHANGE("quicksign.colorsignchange"),    
    COLOR_SPOUT("quicksign.colorspout"),   
    CHAT_SIGNS("quicksign.usechatsigns"),   
    COMMAND_SIGNS("quicksign.usecommandsigns"),    
    USE_SPOUT("quicksign.usespout"),
    IGNORE_BLACK_LIST("quicksign.ignoreblacklist");
    
    private final String permString;

    private QSPermissions(String name) {
        
        this.permString = name;
    
    }

    public String getPermissionString() {
    
        return permString;
    
    }
}
