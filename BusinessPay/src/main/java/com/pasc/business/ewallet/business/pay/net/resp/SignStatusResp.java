package com.pasc.business.ewallet.business.pay.net.resp;

import com.google.gson.annotations.SerializedName;

/**
 * @date 2019/12/15
 * @des
 * @modify
 **/
public class SignStatusResp {

    @SerializedName ("status")
    public int status=0; //0-未签约、1-已签约、2-已解约

   public boolean hasSign(){
       return status==1;
   }

   public void updateHasSign(){
       status=1;
   }
}
