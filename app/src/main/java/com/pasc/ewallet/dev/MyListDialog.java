package com.pasc.ewallet.dev;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pasc.business.ewallet.config.Constants;
import com.pasc.ewallet.dev.utils.AppConstant;
import com.pasc.ewallet.dev.utils.SharePreUtil;
import com.pasc.lib.adapter.listview.BaseHolder;
import com.pasc.lib.adapter.listview.ListBaseAdapter;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/7/4
 * @des
 * @modify
 **/
public class MyListDialog extends Dialog {
    final static int preNnv = SharePreUtil.getInt (AppConstant.key_pre_env, Constants.PAY_STG2_ENV);
    ListView listView;
     List<EnvBean> envs = new ArrayList<> ();

     {
        envs.add (new EnvBean (Constants.PAY_TEST_ENV, "Purse测试环境"));
        envs.add (new EnvBean (Constants.PAY_STG2_ENV, "Stg2环境"));
        envs.add (new EnvBean (Constants.PAY_RELEASE_ENV, "正式环境"));

    }

    public MyListDialog(@NonNull Context context) {
        super (context);
        View view = View.inflate (context, R.layout.dialog_env_layout, null);
        setContentView (view);
        listView = findViewById (R.id.listView);
        listView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int env = envs.get (position).env;
                dismiss ();
                if (preNnv == env) {
                    return;
                }
                ToastUtils.toastMsg ("切换环境后重新进入");
                SharePreUtil.setInt (AppConstant.key_pre_env, env);
                killAllOtherProcess (context);
                android.os.Process.killProcess (android.os.Process.myPid ());
            }
        });

        ListAdapter myAdapter = new ListAdapter (context, envs);
        listView.setAdapter (myAdapter);
    }

    public static void killAllOtherProcess(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService (Context.ACTIVITY_SERVICE);
        if (am == null) {
            return;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcessList = am
                .getRunningAppProcesses ();

        if (appProcessList == null) {
            return;
        }
        for (ActivityManager.RunningAppProcessInfo ai : appProcessList) {
            // KILL OTHER PROCESS OF MINE
            if (ai.uid == android.os.Process.myUid () && ai.pid != android.os.Process.myPid ()) {
                android.os.Process.killProcess (ai.pid);
            }
        }

    }

    public static class EnvBean {
        public int env;
        public String name;

        public EnvBean(int env, String name) {
            this.env = env;
            this.name = name;
        }
    }


    public static class MyHolder extends BaseHolder {
        TextView text1;

        public MyHolder(View rootView) {
            super (rootView);
            text1 = rootView.findViewById (android.R.id.text1);
        }
    }

    public static class ListAdapter extends ListBaseAdapter<EnvBean, MyHolder> {

        public ListAdapter(Context context, List<EnvBean> data) {
            super (context, data);
        }

        @Override
        public int layout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public MyHolder createBaseHolder(View rootView) {
            return new MyHolder (rootView);
        }

        @Override
        public void setData(MyHolder holder, EnvBean data,int position) {
            boolean isCurrent = data.env == preNnv;
            if (isCurrent) {
                holder.text1.setTextColor (Color.parseColor ("#ff0000"));
            } else {
                holder.text1.setTextColor (Color.parseColor ("#333333"));
            }
            if (isCurrent) {
                holder.text1.setText ("当前环境："+data.name);
            } else {
                holder.text1.setText (data.name);
            }
        }
    }


}
