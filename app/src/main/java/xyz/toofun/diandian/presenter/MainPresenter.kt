package xyz.toofun.diandian.presenter

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import xyz.toofun.diandian.R
import xyz.toofun.diandian.fragment.EditArticleFragment
import xyz.toofun.diandian.fragment.MenuFragment
import xyz.toofun.diandian.uitl.ToastUtil
import xyz.toofun.diandian.uitl.map.ILocationManager
import xyz.toofun.diandian.view.MainView
import xyz.toofun.diandian.widget.sideslip.SideslipLayout

/**
 * 主要的activity的逻辑
 * Created by bear on 2017/9/30.
 */
class MainPresenter(context: Context, view: MainView) : BasePresenter<MainView>(context, view), View.OnClickListener {
    private val TAG = "MainPresenter"

    private lateinit var mMenuFragment: MenuFragment //左边菜单
    private lateinit var mEditArticleFragment: EditArticleFragment //写文章的fragment

    var mOnSideLayoutClickListener = object : SideslipLayout.OnSideLayoutClickListener {
        override fun onClickHide() {
            mView?.sideslipLayout?.hideSideView()

        }

        override fun onClickShow(gravity: Int) {
            mView?.sideslipLayout?.showSideView(gravity)
        }
    }

    fun initSideslipLayout() {
        mMenuFragment = MenuFragment()

        mEditArticleFragment = EditArticleFragment()
        mEditArticleFragment.setOnBottonSideLayoutClickListener(mOnSideLayoutClickListener)




        mView?.sideslipLayout?.setFragmentManager(mView?.getFragemntManagerr())
//
        //加入左边侧滑菜单
        mView?.sideslipLayout?.setLeftFragment(mMenuFragment, 0.7f)

        //加入底部侧滑写故事
        mView?.sideslipLayout?.setBottomFragment(mEditArticleFragment, 1f)

    }


    /**
     * 初始化地图
     */
    fun initMap() {
        ILocationManager.instance.init(mContext?.applicationContext!!, mView?.getMapView()?.map)
        //地图的触摸事件
        //
        mView?.getMapView()?.map?.setOnMapTouchListener {
            if (mView?.sideslipLayout?.isShowSideLayout() ?: false && mView?.sideslipLayout?.getShowSide() ?: Gravity.LEFT == Gravity.LEFT) {
                mView?.sideslipLayout?.hideSideView()
            }
        }
    }

    fun initClickEvent() {
        mView?.sideslipLayout?.getHomeView()?.findViewById(R.id.person)?.setOnClickListener(this)
        mView?.sideslipLayout?.getHomeView()?.findViewById(R.id.message)?.setOnClickListener(this)
        mView?.sideslipLayout?.getHomeView()?.findViewById(R.id.animate_position)?.setOnClickListener(this)
    }

    fun startLocation() {
        ILocationManager.instance.start()
    }

    fun destoryLocation() {
        ILocationManager.instance.destroy()
    }

    /**
     * 移动到用户所在的位置
     */
    fun animateUserPosition() {
        ILocationManager.instance.animate2CurrentPosition()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.person -> {
                mView?.sideslipLayout?.showSideView(Gravity.LEFT)
            }

            R.id.message -> {
                Log.i(TAG, "消息")
                ToastUtil.showToastR(mContext, R.string.message)
            }

            R.id.animate_position -> {
                Log.i(TAG, "定位")
                animateUserPosition()
            }
        }
    }

}