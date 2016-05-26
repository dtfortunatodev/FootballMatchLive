package com.formobile.projectlivestream.configs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PTECH on 10-02-2015.
 */
public class CompactIpsEntity implements Serializable{

    private List<IpCatchEntity> ipCatchEntities;

    public CompactIpsEntity(){

    }

    public List<IpCatchEntity> getIpCatchEntities() {
        return ipCatchEntities;
    }

    public void setIpCatchEntities(List<IpCatchEntity> ipCatchEntities) {
        this.ipCatchEntities = ipCatchEntities;
    }
}
