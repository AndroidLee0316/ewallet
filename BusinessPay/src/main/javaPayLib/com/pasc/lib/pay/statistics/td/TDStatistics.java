package com.pasc.lib.pay.statistics.td;

//import android.app.Activity;

import android.content.Context;

import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.lib.pay.statistics.IPascStatistics;

import java.util.Map;

//import com.tendcloud.tenddata.TCAgent;
//import java.util.HashMap;

/**
 *  Created by huangtebian535 on 2018/09/03.
 *
 *  天眼埋点
 */
public class TDStatistics implements IPascStatistics {
    private Context context;

    public TDStatistics(Context context) {
        this.context = context;
    }

    public void init(String appID, String channel, boolean testOn, boolean logOn) {
//        //开启天眼测试环境开关，必须放在init之前，线上环境要关闭！
//        TCAgent.TEST_ON = testOn;
//        //日志开关
//        TCAgent.LOG_ON = logOn;
//        try {
//            //防止Crash :
//            //java.lang.RuntimeException: Package manager has died
//            //Caused by: android.os.DeadObjectException
//            //            TCAgent.init(this);
//            TCAgent.init(context, appID, channel);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override public void onEvent(String eventID) {
//        TCAgent.onEvent(context, eventID);
        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onEvent (eventID,"",null);
        }
    }

    @Override public void onEvent(String eventID, String label) {
//        TCAgent.onEvent(context, eventID, label);
        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onEvent (eventID,label,null);
        }
    }

    @Override public void onEvent(String eventID, Map<String, String> map) {
//        Map<String, Object> oMap = new HashMap<>();
//
//        for (String key : map.keySet()) {
//            oMap.put(key, map.get(key));
//        }
//
//        TCAgent.onEvent(context, eventID, "", oMap);
        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onEvent (eventID,"",map);
        }
    }

    @Override public void onEvent(String eventID, String label, Map<String, String> map) {
//        Map<String, Object> oMap = new HashMap<>();
//
//        for (String key : map.keySet()) {
//            oMap.put(key, map.get(key));
//        }
//
//        TCAgent.onEvent(context, eventID, label, oMap);

        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onEvent (eventID,label,map);
        }
    }

    @Override public void onPageStart(String pageName) {
//        TCAgent.onPageStart(context, pageName);

        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onPageStart (pageName);
        }
    }

    @Override public void onPageEnd(String pageName) {
//        TCAgent.onPageEnd(context, pageName);
        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onPageEnd (pageName);
        }
    }

    @Override
    public void onResume(Context context) {
//       if(context instanceof Activity){
//           TCAgent.onResume((Activity) context);
//       }
        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onResume (context);
        }
    }

    @Override
    public void onPause(Context context) {
//        if(context instanceof Activity){
//            TCAgent.onPause((Activity) context);
//        }
        if (PayManager.getInstance ().getOnEventListener ()!=null){
            PayManager.getInstance ().getOnEventListener ().onPause (context);
        }
    }
}
