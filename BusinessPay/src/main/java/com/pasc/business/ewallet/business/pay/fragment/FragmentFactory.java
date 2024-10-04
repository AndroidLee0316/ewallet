package com.pasc.business.ewallet.business.pay.fragment;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2019/7/26
 * @des
 * @modify
 **/
public class FragmentFactory {
//    public static final int FragmentSize=5;
    public static final int FragmentSize=4;

    public static final int MainPosition = 0;
    public static final int PwdPosition = 1;
    public static final int VerifyCodePosition = 2;
    public static final int PwdWxPosition = 3;

//    public static final int PayPosition = 4;
//    public static final int SignPosition = 5;

    private FragmentFactory() {

    }

    private final static class Single {
        private final static FragmentFactory instance = new FragmentFactory ();
    }

    public static FragmentFactory instance() {
        return Single.instance;
    }


    private Map<Integer, BasePayFragment> fragmentMap = new HashMap<> ();

    public <T extends BasePayFragment> T getFragment(int pos) {
        return (T) fragmentMap.get (pos);
    }

    public BasePayFragment newInstance(int pos) {
        BasePayFragment fragment = null;
        switch (pos) {
            case MainPosition:
                fragment = new PayMainFragment ();
                break;
            case PwdPosition:
                fragment = new PayPwdFragment ();
                break;
            case VerifyCodePosition:
                fragment = new PaySendVCodeFragment ();
                break;
            case PwdWxPosition:
                fragment = new PayWxPwdFragment ();
                break;
//            case PayPosition:
//                fragment = new PayingFragment ();
//                break;
//            case SignPosition:
//                fragment = new PaySignSendVcodeFragment ();
//                break;

            default:
                pos = MainPosition;
                fragment = new PayMainFragment ();
                break;
        }
        fragmentMap.put (pos, fragment);
        return fragment;
    }

    public void clearAll() {
        fragmentMap.clear ();
    }
}
