package com.pasc.business.ewallet.business.bankcard.presenter;

import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.bankcard.model.CardModel;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickCardBean;
import com.pasc.business.ewallet.business.bankcard.net.resp.SafeCardBean;
import com.pasc.business.ewallet.business.bankcard.view.BankCardListView;
import com.pasc.lib.netpay.resp.BaseRespThrowableObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class BankCardListPresenter extends EwalletBasePresenter<BankCardListView> {
    CompositeDisposable compositeDisposable = new CompositeDisposable ();

    public static class BankCardResp {
        public List<SafeCardBean> safeCardBeans;
        public List<QuickCardBean> quickCardBeans;

        public BankCardResp(List<SafeCardBean> safeCardBeans, List<QuickCardBean> quickCardBeans) {
            if (safeCardBeans == null) {
                safeCardBeans = new ArrayList<> ();
            }
            if (quickCardBeans == null) {
                quickCardBeans = new ArrayList<> ();
            }

            this.safeCardBeans = safeCardBeans;
            this.quickCardBeans = quickCardBeans;
        }
    }

    Single<List<QuickCardBean>> quickSingle() {
        return Single.create (new SingleOnSubscribe<List<QuickCardBean>> () {
            @Override
            public void subscribe(SingleEmitter<List<QuickCardBean>> emitter) {
                emitter.onSuccess (new ArrayList<> ());
            }
        }).subscribeOn (Schedulers.io ());
    }

    public void getBankCardList(String memberNo) {
        getView ().showLoading ("");
        compositeDisposable.add (Single.zip (CardModel.listSafeBank (memberNo),
                StatusTable.enableQuickCard ? CardModel.listQuickBank (memberNo) : quickSingle (),
                new BiFunction<List<SafeCardBean>, List<QuickCardBean>, BankCardResp> () {
            @Override
            public BankCardResp apply(List<SafeCardBean> safeCardBeans, List<QuickCardBean> quickCardBeans) throws Exception {
                List<SafeCardBean> cardBeans = new ArrayList<> ();
                //注意，目前只取第一个
                if (!StatusTable.enableMultiSafeCard) {
                    if (safeCardBeans.size () > 0) {
                        cardBeans.add (safeCardBeans.get (0));
                    }
                }else {
                    cardBeans.addAll (safeCardBeans);
                }
                return new BankCardResp (cardBeans, quickCardBeans);
            }
        }).observeOn (AndroidSchedulers.mainThread ()).subscribe (new Consumer<BankCardResp> () {
            @Override
            public void accept(BankCardResp cardResp) {
                getView ().dismissLoading ();
                getView ().queryCardListSuccess (cardResp.safeCardBeans, cardResp.quickCardBeans);

            }
        }, new BaseRespThrowableObserver () {
            @Override
            public void onV2Error(String code, String msg) {
                getView ().dismissLoading ();
                getView ().queryCardListError (code, msg);
            }
        }));


    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
