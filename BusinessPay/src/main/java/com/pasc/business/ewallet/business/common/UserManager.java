package com.pasc.business.ewallet.business.common;

import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.inner.Location;

/**
 * 用户相关信息管理类
 * Created by ex-huangzhiyi001 on 2019/2/27.
 */
@NotProguard
public class UserManager {
    private String mchOrderNo;//商户订单号
    private String merchantNo;//商户号
    private String memberNo;//会员号
    private String token = "";//用户登陆凭证
    private String accessUserId = "";//接入方的用户标识accessUserId
    private String inChannelId = "";//支付平台分配的渠道号
    private Location payLocation = new Location ();//位置信息
    private String name = ""; //用户名称
    private String idCard = "";//身份证15或者18位
    private String addressCity = "";// 城市地址 province city area
    private String addressDetail = "";//详细地址
    private String occupation = "";//职业id
    private String cardNum = "";//银行卡
    private String phoneNum = "";//手机号
    private String unionOrderId = "";//银联订单号
    private String unionOrderTime = "";//银联下单时间

    private String setPwdTag= StatusTable.PassWordTag.fromDefaultPwdTag;

    public void setDefaultPwdTag(){
        setSetPwdTag (StatusTable.PassWordTag.fromDefaultPwdTag);
    }
    public void setSetPwdTag(String setPwdTag) {
        if (Util.isEmpty (setPwdTag)) {
            return;
        }
        this.setPwdTag = setPwdTag;
    }

    public String getSetPwdTag() {
        return setPwdTag;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getCompleteAddress(){
        return getAddressCity ()+getAddressDetail ();
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressCity(String addressCity) {
        if (Util.isEmpty (addressCity)) {
            return;
        }
        this.addressCity = addressCity;
    }
    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        if (Util.isEmpty (memberNo)) {
            return;
        }
        this.memberNo = memberNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        if (Util.isEmpty (mchOrderNo)) {
            return;
        }
        this.mchOrderNo = mchOrderNo;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMerchantNo(String merchantNo) {
        if (Util.isEmpty (merchantNo)) {
            return;
        }
        this.merchantNo = merchantNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        if (Util.isEmpty (phoneNum)) {
            return;
        }
        this.phoneNum = phoneNum;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        if (Util.isEmpty (idCard)) {
            return;
        }
        this.idCard = idCard;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        if (Util.isEmpty (cardNum)) {
            return;
        }
        this.cardNum = cardNum;
    }

    public String getUnionOrderId() {
        return unionOrderId;
    }

    public void setUnionOrderId(String unionOrderId) {
        if (Util.isEmpty (unionOrderId)) {
            return;
        }
        this.unionOrderId = unionOrderId;
    }

    public String getUnionOrderTime() {
        return unionOrderTime;
    }

    public void setUnionOrderTime(String unionOrderTime) {
        if (Util.isEmpty (unionOrderTime)) {
            return;
        }
        this.unionOrderTime = unionOrderTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Util.isEmpty (name)) {
            return;
        }
        this.name = name;
    }
    public String getSafeName() {
        return Util.formatName (name);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (Util.isEmpty (token)) {
            return;
        }
        this.token = token;
    }

    public String getAccessUserId() {
        return accessUserId;
    }

    public void setAccessUserId(String accessUserId) {
        if (Util.isEmpty (accessUserId)) {
            return;
        }
        this.accessUserId = accessUserId;
    }

    public String getInChannelId() {
        return inChannelId;
    }

    public void setInChannelId(String inChannelId) {
        if (Util.isEmpty (inChannelId)) {
            return;
        }
        this.inChannelId = inChannelId;
    }

    public void setPayLocation(Location payLocation) {
        if (payLocation != null) {
            Location p = new Location ();
            p.setLatitude (payLocation.getLatitude ());
            p.setLongitude (payLocation.getLongitude ());
            this.payLocation = p;
        }
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    public Location getPayLocation() {
        return payLocation;
    }


    public static UserManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private UserManager() {
    }

    private static class SingletonHolder {
        private static final UserManager INSTANCE = new UserManager ();
    }
}
