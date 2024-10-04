package com.pasc.business.ewallet.business.traderecord.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.lib.adapter.listview.BaseHolder;
import com.pasc.lib.adapter.listview.ListBaseAdapter;

import java.util.List;

/**
 * @date 2019/7/8
 * @des
 * @modify
 **/
public class TypeDialog extends Dialog {
    private GridView ewalletPayTypeGrid;
    private TextView ewalletTvCancel;
    private  View view;

    private void initView() {
        ewalletPayTypeGrid = view.findViewById(R.id.ewallet_pay_type_grid);
        ewalletTvCancel = view.findViewById(R.id.ewallet_tv_cancel);
    }

    public TypeDialog(@NonNull Context context, List<String> data) {
        super (context,R.style.EwalletBottomDialogStyle);
         view= LayoutInflater.from (context).inflate (R.layout.ewallet_pay_bill_type_dialog,null);
        initView ();
        setContentView (view);
        TypeAdapter typeAdapter=new TypeAdapter (context,data);
        ewalletPayTypeGrid.setAdapter (typeAdapter);

        ewalletPayTypeGrid.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (typeListener!=null){
                    typeListener.typeClick (data.get (position));
                }
                dismiss ();
            }
        });

        ewalletTvCancel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dismiss ();
            }
        });
        WindowManager.LayoutParams attributes = getWindow ().getAttributes ();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow ().setAttributes (attributes);
    }
    private TypeListener typeListener;

    public void setTypeListener(TypeListener typeListener) {
        this.typeListener = typeListener;
    }
    public  interface TypeListener{
        void typeClick(String item);
    }
    public static class TypeAdapter extends ListBaseAdapter<String,TypeAdapter.TypeHolder> {


        public TypeAdapter(Context context, List<String> data) {
            super (context, data);
        }

        @Override
        public int layout() {
            return R.layout.ewallet_pay_bill_type_item;
        }

        @Override
        public TypeAdapter.TypeHolder createBaseHolder(View rootView) {
            return new TypeAdapter.TypeHolder (rootView);
        }

        @Override
        public void setData(TypeAdapter.TypeHolder holder, String data,int position) {
            holder.tvType.setText (data);
        }

        public static class TypeHolder extends BaseHolder {
            TextView tvType;
            public TypeHolder(View rootView) {
                super (rootView);
                tvType=rootView.findViewById (R.id.ewallet_pay_tv_type);
            }
        }
    }
}
