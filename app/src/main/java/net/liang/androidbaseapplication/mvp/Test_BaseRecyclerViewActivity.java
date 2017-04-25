package net.liang.androidbaseapplication.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import net.liang.androidbaseapplication.R;
import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerViewActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class Test_BaseRecyclerViewActivity extends BaseRecyclerViewActivity<List<String>> {

    @Override
    public void init() {
        super.init();
        setToolbarCentel(true, "Activity列表示例");

        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemClick(adapter, view, position);
                String string = (String) baseQuickAdapter.getData().get(position);
                ToastUtils.showSnackbar(getView(), string);
                ToastUtils.showToast(string);
            }

            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_recycler_view;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    /**
     * 列表适配器
     * 默认一页页数为10，可使用setPageSize(int pageSize)修改
     */
    @Override
    public BaseRecyclerAdapter addListAdapter() {
        return new RecyclerAdapter(recyclerView, null);
    }

    /**
     * 请求成功时的回调方法;
     * onSuccess返回的数据为完整的请求数据，需要自己拆解列表数据，添加到适配器里
     */
    @Override
    public void onListSuccess(List<String> strings, int pageNo) {

        //单页使用
        //adapter.showList(strings);

        //多页使用
        adapter.showList(strings, pageNo);
    }


    /**
     * 获取网络数据接口，注意返回的是被观察者对象
     */
    @Override
    public Observable<List<String>> onListGetData(int pageNo) {

        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < 10; i++) {
            String string = sdf.format(new Date());
            list.add(string);
        }
        return Observable.just(list);
    }

    /**
     * 带bind的适配器
     */
    class RecyclerAdapter extends BaseRecyclerAdapter<String> {

        public RecyclerAdapter(RecyclerView recyclerView, List data) {
            super(recyclerView, R.layout.item_base_recyclerview_layout, data);
        }

        @Override
        protected void convert(BindingViewHolder bindingViewHolder, String s) {
            bindingViewHolder.setText(R.id.data, s);
        }
    }

}
