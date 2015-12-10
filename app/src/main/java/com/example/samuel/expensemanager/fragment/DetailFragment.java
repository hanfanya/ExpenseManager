package com.example.samuel.expensemanager.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.expensemanager.ExpenseApplication;
import com.example.samuel.expensemanager.R;
import com.example.samuel.expensemanager.activity.AddRecordActivity;
import com.example.samuel.expensemanager.model.DaoSession;
import com.example.samuel.expensemanager.model.Expense;
import com.example.samuel.expensemanager.model.ExpenseDao;
import com.example.samuel.expensemanager.utils.DatesUtils;
import com.example.samuel.expensemanager.utils.SPUtils;
import com.example.samuel.expensemanager.view.MyListView;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by WSY on 2015-12-05-0005.
 * 明细页面viewpager的fragment基类
 */
public class DetailFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    public List<Expense> mLists;
    public double sum = 0; //支出总额
    private int[] colorArray;
    private int position;
    private View view;
    private MyListView lv_detail_fragment;
    private TextView tv_notice;
    private MyAdapater myAdapater;
    private Menu mMenu;
    private MenuItem mDateItem;
    private boolean hasInited = false;
    private String sumString;
    private ExpenseDao mExpenseDao;

    public DetailFragment() {
        System.out.println("构造方法------------------------------");
    }

    /**
     * 静态方法，在对象创建之前就存在了
     */
    public static DetailFragment newInstance(int position) {
//        System.out.println("newInstance-------------------------");
        /**
         * 将传来的date当做一个参数实体封装到bunlde中
         */
        Bundle args = new Bundle();
        args.putSerializable("position", position);
        /**
         * fragment中可以存放这样的参数实体
         */
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        System.out.println("onCreateView---------------------");
        setHasOptionsMenu(true);
        colorArray = getActivity().getResources().getIntArray(R.array.colorType);
        return initViews();
    }

    /**
     * 初始化view对象
     */
    private View initViews() {
//        System.out.println("initViews------------------------");
        view = View.inflate(getContext(), R.layout.fragment_detail, null);

        lv_detail_fragment = (MyListView) view.findViewById(R.id.lv_detail_fragment);
        tv_notice = (TextView) view.findViewById(R.id.tv_notice);
        return view;
    }

    /**
     * 每次得焦的时候call到
     */
    @Override
    public void onResume() {
//        System.out.println("onResume----------------------------");
        super.onResume();
        initData();
        //判断menu是否已经初始化！
        if (hasInited) {
            mDateItem.setTitle(sumString);
        }
    }

    /**
     * 给view上数据
     */
    private void initData() {
//        System.out.println("initData--------------------------");
        //通过key拿到传来的page的位置
        position = (int) getArguments().getSerializable("position");
        //通过position得到日期
        String date = DatesUtils.getDate(position);
        //取出该天的数据，封装到集合中
        getData(date);
        refreshSum();
        myAdapater = new MyAdapater();
        lv_detail_fragment.setAdapter(myAdapater);
        lv_detail_fragment.setOnItemLongClickListener(this);
    }

    /**
     * fragment的回调，控制上面的菜单栏
     * 这里的代码想要生效，前面需要写这句代码：
     * setHasOptionsMenu(true);
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        System.out.println("onCreateOptionsMenu----------------------");
        inflater.inflate(R.menu.menu_detail_fragment, menu);
        this.mMenu = menu;
        mDateItem = mMenu.findItem(R.id.action_date_title);
        mDateItem.setTitle(sumString);

        hasInited = true; //表明菜单栏初始化完毕
    }

    public void refreshSum() {
        getSum();
//        for (Expense e : mLists) {
//            System.out.println("日期： " + e.getDate() + " 金额: " + e.getFigure()
//                    + " 类别: " + e.getTypeName()
//                    + " 颜色： " + e.getTypeColor() + " 收入/支出： " + e.getTypeFlag()
//                    + " 是否删除：" + e.getIsDeleted());
//        }
        sumString = "总支出：￥" + sum;

    }

    public void getSum() {
        sum = 0;
        for (Expense e : mLists) {
            if (e.getTypeFlag() == 1) { //支出
                Double figure = e.getFigure();
                sum = sum + figure;
            }
        }
    }

    /**
     * 获取当前日期的数据库数据
     */
    public void getData(String date) {
        DaoSession daoSession = ((ExpenseApplication) getActivity().getApplicationContext()).getDaoSession();
        mExpenseDao = daoSession.getExpenseDao();

        QueryBuilder qb = mExpenseDao.queryBuilder();
        qb.where(ExpenseDao.Properties.Date.eq(date), ExpenseDao.Properties.UploadFlag.in(0, 1, 5, 8)).orderDesc(ExpenseDao.Properties.Time);
        mLists = qb.list();
        isEmpty();

    }

    public void isEmpty() {
        if (mLists.size() == 0) {
            //如果没有记录，提示下
            tv_notice.setVisibility(View.VISIBLE);
            lv_detail_fragment.setVisibility(View.GONE);
            tv_notice.setText("没有记录...");
        } else {
            tv_notice.setVisibility(View.GONE);
            lv_detail_fragment.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 长按删除
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showActionDialog(position);
        return false;
    }

    /**
     * 弹出操作对话框
     */
    private void showActionDialog(final int position) {
        String[] items = {"删除记录", "编辑记录"};
        new AlertDialog.Builder(getActivity()).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        deleteRecord(position);
                        break;
                    case 1:
                        editRecord(position);
                        break;
                }
                dialog.dismiss();   //点击后，关闭对话框
            }
        }).show();
    }

    //编辑选中的记录
    private void editRecord(int position) {
        Expense expense = mLists.get(position);

        Long id = expense.getId();
        Integer typeFlag = expense.getTypeFlag();

        Intent intent = new Intent(getActivity(), AddRecordActivity.class);
        intent.putExtra("edit_record", id);
        intent.putExtra("type_flag", typeFlag);
        intent.putExtra("isCreated", false);
        startActivity(intent);
    }

    //删除选中的记录
    private void deleteRecord(int position) {
        Expense expense = mLists.get(position);
        Integer uploadFlag = expense.getUploadFlag();
        switch (uploadFlag) {
            case 0:
            case 1:
                mExpenseDao.delete(expense);
                break;
            case 5:
                expense.setUploadFlag(7);
                mExpenseDao.update(expense);
                break;
            case 8:
                expense.setUploadFlag(6);
                mExpenseDao.update(expense);
                break;
            default:
                break;

        }
        mLists.remove(position);
        myAdapater.notifyDataSetChanged();
        refreshSum();
        isEmpty();
        mDateItem.setTitle(sumString);
        Toast.makeText(getActivity(), "记录已删除", Toast.LENGTH_SHORT).show();
        SPUtils.saveBoolean(getActivity(), "isSame", false);

    }

    static class ViewHolder {
        ImageView iv_type;
        TextView tv_circle;
        TextView tv_typename;
        TextView tv_figure;
    }

    /**
     * listview的adapter
     */
    class MyAdapater extends BaseAdapter {

        @Override
        public int getCount() {
            return mLists.size();
        }

        @Override
        public Expense getItem(int position) {
            return mLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null) {
                view = View.inflate(getContext(), R.layout.item_detail_list, null);
                holder = new ViewHolder();
                holder.iv_type = (ImageView) view.findViewById(R.id.iv_type);
                holder.tv_circle = (TextView) view.findViewById(R.id.tv_circle);
                holder.tv_typename = (TextView) view.findViewById(R.id.tv_typename);
                holder.tv_figure = (TextView) view.findViewById(R.id.tv_figure);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            Expense item = getItem(position);
            if (item != null) {
                String typeName = item.getTypeName();
                String substring = typeName.substring(0, 1);

                holder.tv_circle.setText(substring);
                holder.tv_typename.setText(typeName);
                holder.iv_type.setColorFilter(colorArray[item.getTypeColor()]);
                holder.tv_figure.setText("￥ " + item.getFigure());
            }
            return view;
        }
    }

}
