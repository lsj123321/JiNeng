package com.bawei.liushaojie111.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bawei.liushaojie111.R;
import com.bawei.liushaojie111.model.bean.ShoppingCartBean;

public class ShoppingCartAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ShoppingCartBean mShoppingCartBean;
    private float price_All=0;
    public ShoppingCartAdapter(Context context){
        this.mContext = context;
    }

    public void setData(ShoppingCartBean shoppingCartBean){
        this.mShoppingCartBean = shoppingCartBean;
    }
    CheckBox mCheckBox;
    public void setCheckBox(final CheckBox checkBox) {
        this.mCheckBox = checkBox;
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                sumPrice(checkBox.isChecked());
                selectAll(checkBox.isChecked());
            }
        });
    }
    private TextView mtextview;
    public void setTextView(TextView textView){
        this.mtextview=textView;
    }
    @Override
    public int getGroupCount() {
        if(mShoppingCartBean==null){
            return 0;
        }
        return mShoppingCartBean.getData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(mShoppingCartBean==null){
            return 0;
        }
        return mShoppingCartBean.getData().get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
            parentViewHolder = new ParentViewHolder();
            parentViewHolder.shopName = convertView.findViewById(R.id.shopName);
            parentViewHolder.checkBox = convertView.findViewById(R.id.checkbox_parent);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }

        parentViewHolder.checkBox.setChecked(mShoppingCartBean.getData().get(groupPosition).isCheck());
        parentViewHolder.shopName.setText(mShoppingCartBean.getData().get(groupPosition).getSellerName());
        parentViewHolder.checkBox.setTag(groupPosition);
        parentViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                boolean checked = checkBox.isChecked();
                int groupPosition = Integer.parseInt(checkBox.getTag().toString());
                for (int j = 0; j < mShoppingCartBean.getData().get(groupPosition).getList().size(); j++) {
                    String praise_num1 = mShoppingCartBean.getData().get(groupPosition).getList().get(j).getPrice();
                    int i2 = Integer.parseInt(praise_num1);
                    if(checked) {
                        price_All += i2;
                    }else{
                        price_All -= i2;
                    }
                }
                mtextview.setText("总价:￥"+price_All);
                mShoppingCartBean.getData().get(groupPosition).setCheck(checked);
                selectGroup(groupPosition,checked);
                boolean selectAllGroup = isSelectAllGroup();
                mCheckBox.setChecked(selectAllGroup);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.goodsName = convertView.findViewById(R.id.goodsName);
            childViewHolder.checkBox = convertView.findViewById(R.id.checkbox_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.checkBox.setChecked(mShoppingCartBean.getData().get(groupPosition).getList().get(childPosition).isCheck());
        childViewHolder.goodsName.setText(mShoppingCartBean.getData().get(groupPosition).getList().get(childPosition).getTitle());
        childViewHolder.checkBox.setTag(groupPosition+"#"+childPosition);
        childViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox= (CheckBox) v;
                String tag= (String) v.getTag();
                int groupPosition = Integer.parseInt(tag.split("#")[0]) ;
                int childPosition = Integer.parseInt(tag.split("#")[1]) ;
                ShoppingCartBean.DataBean dataBean = mShoppingCartBean.getData().get(groupPosition);
                ShoppingCartBean.DataBean.ListBean goodsBean = dataBean.getList().get(childPosition);
                goodsBean.setCheck(checkBox.isChecked());
                String price = goodsBean.getPrice();
                int price1=Integer.parseInt(price);
                if (checkBox.isChecked()){
                    price_All+=price1;
                }else {
                    price_All-=price1;
                }
                mtextview.setText("总价:￥"+price_All);
                boolean group = isSelectGroup(groupPosition);
                dataBean.setCheck(group);
                boolean allGroup = isSelectAllGroup();
                mCheckBox.setChecked(allGroup);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    class ParentViewHolder {
        TextView shopName;
        CheckBox checkBox;
    }

    class ChildViewHolder {
        TextView goodsName;
        CheckBox checkBox;
    }
    private boolean isSelectGroup(int groupPosition){
        for (int i = 0; i < mShoppingCartBean.getData().get(groupPosition).getList().size(); i++) {
            ShoppingCartBean.DataBean.ListBean listBean = mShoppingCartBean.getData().get(groupPosition).getList().get(i);
            boolean check = listBean.isCheck();
            if (!check){
                return false;
            }
        }
        return true;
    }
    private boolean isSelectAllGroup(){
        for (int i = 0; i < mShoppingCartBean.getData().size(); i++) {
            ShoppingCartBean.DataBean dataBean = mShoppingCartBean.getData().get(i);
            boolean check = dataBean.isCheck();
            if (!check) {
                return false;
            }
        }
        return true;
    }
    private void selectAll(boolean checked) {
        for (int i = 0; i < mShoppingCartBean.getData().size(); i++) {
            ShoppingCartBean.DataBean dataBean = mShoppingCartBean.getData().get(i);
            dataBean.setCheck(checked);
            for (int j = 0; j < dataBean.getList().size(); j++) {
                ShoppingCartBean.DataBean.ListBean listBean = dataBean.getList().get(j);
                listBean.setCheck(checked);
            }
        }
        notifyDataSetChanged();
    }
    public void selectGroup(int groupPosition,boolean isChecked){
        for (int i = 0; i < mShoppingCartBean.getData().get(groupPosition).getList().size(); i++) {
            ShoppingCartBean.DataBean.ListBean listBean = mShoppingCartBean.getData().get(groupPosition).getList().get(i);
            listBean.setCheck(isChecked);
        }

    }
    private void sumPrice(boolean checked){
        price_All=0;
        for (int i = 0; i < mShoppingCartBean.getData().size(); i++) {
            ShoppingCartBean.DataBean dataBean = mShoppingCartBean.getData().get(i);
            for (int j = 0; j < dataBean.getList().size(); j++) {
                String price = dataBean.getList().get(j).getPrice();
                int price1=Integer.parseInt(price);

                if (checked){
                    price_All+=price1;
                }else {
                    price_All=0;
                }
            }
        }
        mtextview.setText("总价:￥"+price_All);
    }
}
