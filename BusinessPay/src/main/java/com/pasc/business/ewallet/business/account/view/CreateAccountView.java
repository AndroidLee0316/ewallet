package com.pasc.business.ewallet.business.account.view;


import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;

/**
 * @date 2019/6/26
 * @des
 * @modify
 **/
public interface CreateAccountView extends CommonBaseView {

    void queryQueryMemberSuccess(QueryMemberResp memberResp);
    void queryQueryMemberError(String code,String msg);

}
