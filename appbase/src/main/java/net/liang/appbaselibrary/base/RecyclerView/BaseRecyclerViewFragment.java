package net.liang.appbaselibrary.base.RecyclerView;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import net.liang.appbaselibrary.R;
import net.liang.appbaselibrary.base.BaseFragment;
import net.liang.appbaselibrary.data.RecyclerDataRepository;
import net.liang.appbaselibrary.data.RecyclerDataSource;
import net.liang.appbaselibrary.data.local.LocalRecyclerDataSource;


/**
 * Created on 2016/10/23.
 * By lianghuiyong@outlook.com
 *
 * @param <T> 是获取过来的数据类型
 */

public abstract class BaseRecyclerViewFragment<T> extends BaseFragment implements BaseRecyclerViewContract.View<T>, RecyclerDataSource<T>, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    protected BaseRecyclerAdapter adapter;
    protected SwipeRefreshLayout swipeRefresh;
    protected RecyclerView recyclerView;
    private BaseRecyclerViewContract.Presenter recyclerPresenter;
    protected LinearLayoutManager mLinearLayoutManager;
    protected boolean refreshEveryTimes = true;

    @Override
    public void initRecyclerView() {
        recyclerPresenter = new BaseRecyclerViewPresenter(this,
                new RecyclerDataRepository(this, LocalRecyclerDataSource.getInstance()));

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh);

        adapter = adapter == null ? addListAdapter() : adapter;

        swipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefresh.setOnRefreshListener(this);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this);
        adapter.addOnRecyclerAdapterListener(() -> onRefresh());
    }

    @Override
    public void setListRefresh(boolean isShow) {
        swipeRefresh.setRefreshing(isShow);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (refreshEveryTimes) {
            recyclerPresenter.onListRefresh();
        }
    }

    @Override
    public void onRefresh() {
        recyclerPresenter.onListRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        recyclerPresenter.onListLoadMore();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerPresenter.unSubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshEveryTimes = isRefreshEvertTimes();
    }

    public boolean isRefreshEvertTimes() {
        return true;
    }

    @Override
    public void showNetworkFail() {
        adapter.showNetWorkErrorView();
    }

    @Override
    public void onListError(Throwable error) {

    }
}
