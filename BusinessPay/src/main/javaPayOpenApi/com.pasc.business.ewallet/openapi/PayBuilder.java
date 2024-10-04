package com.pasc.business.ewallet.openapi;

import android.app.Application;
import android.support.annotation.NonNull;

import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019-11-28
 * @des
 * @modify
 **/

@NotProguard
public class PayBuilder {


    @NonNull  public Application application;
    @NonNull  public String publicKey;
    @NonNull  public String AppId;
    @NonNull  public String secretKey;
    @NonNull  public String wxPayAppId;
    @NonNull  public boolean isDebug;
    @NonNull  public String hostAndGateWay;

    private PayBuilder(@NonNull Application application,
                       @NonNull String publicKey,
                       @NonNull String AppId,
                       @NonNull String secretKey,
                       @NonNull String wxPayAppId,
                       @NonNull boolean isDebug,String hostAndGateWay){
        this.application=application;
        this.publicKey=publicKey;
        this.AppId=AppId;
        this.secretKey=secretKey;
        this.wxPayAppId=wxPayAppId;
        this.isDebug=isDebug;
        this.hostAndGateWay=hostAndGateWay;

    }

    @NotProguard
    public static class Build{
        @NonNull private Application application;
        @NonNull private String publicKey;
        @NonNull private String AppId;
        @NonNull private String secretKey;
        @NonNull private String wxPayAppId;
        @NonNull private boolean isDebug;
        @NonNull private String hostAndGateWay;
        public Build(Application application){
            this.application=application;

        }
        public Build publicKey(String publicKey){
            this.publicKey=publicKey;
            return this;
        }
        public Build appId(String appId){
            this.AppId=appId;
            return this;
        }

        public Build secretKey(String secretKey){
            this.secretKey=secretKey;
            return this;
        }

        public Build wxPayAppId(String wxPayAppId){
            this.wxPayAppId=wxPayAppId;
            return this;
        }
        public Build hostAndGateWay(String hostAndGateWay){
            this.hostAndGateWay=hostAndGateWay;
            return this;
        }
        public Build isDebug(boolean isDebug){
            this.isDebug=isDebug;
            return this;
        }

        public PayBuilder build(){
            return new PayBuilder (application,
                    publicKey,
                    AppId,
                    secretKey,
                    wxPayAppId,
                    isDebug,
                    hostAndGateWay);
        }
    }

}
