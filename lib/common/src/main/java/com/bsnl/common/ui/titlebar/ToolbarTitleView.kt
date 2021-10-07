package com.bsnl.common.ui.titlebar

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar
import com.bsnl.base.utils.KeyboardUtils
import com.bsnl.common.R
import com.bsnl.common.iface.ITitleView
import com.bsnl.common.utils.dp

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class ToolbarTitleView : LinearLayout, ITitleView {
    private var mTopBarLayout: RelativeLayout? = null
    private var mToolbarLeftLayout: LinearLayout? = null
    private var mToolbarLeftImgv: ImageView? = null
    private var mToolbarLeftTv: TextView? = null
    private var mToolbarRightLayout: LinearLayout? = null
    private var mTitleTv: TextView? = null

    constructor(context: Context?) : super(context)
    constructor(
        context: Context?,
        @Nullable attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context?,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopBarLayout = findViewById<View>(R.id.top_bar_layout) as RelativeLayout
        mToolbarLeftLayout =
            findViewById<View>(R.id.toolbar_left_layout) as LinearLayout
        mToolbarLeftImgv =
            findViewById<View>(R.id.toolbar_left_imgv) as ImageView
        mToolbarLeftTv =
            findViewById<TextView>(R.id.toolbar_left_tv) as TextView
        mToolbarRightLayout = findViewById(R.id.toolbar_right_layout)
        mTitleTv = findViewById<View>(R.id.title_tv) as TextView
        mTitleTv!!.isSelected = true
    }

    override fun setImmersionBarEnable(isImmersionBarEnable: Boolean) {
        try {
            if (isImmersionBarEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val lp =
                    mTopBarLayout!!.layoutParams as LayoutParams
                lp.topMargin = KeyboardUtils.getStatusBarHeight()
            } else {
                val lp =
                    mTopBarLayout!!.layoutParams as LayoutParams
                lp.topMargin = 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setNavIconOnClickListener(onClickListener: OnClickListener?): ToolbarTitleView {
        mToolbarLeftImgv!!.setOnClickListener(onClickListener)
        return this
    }

    override fun setNavIcon(@Nullable icon: Drawable?): ToolbarTitleView {
        mToolbarLeftImgv!!.setImageDrawable(icon)
        return this
    }

    override fun setNavIcon(@DrawableRes resId: Int): ToolbarTitleView {
        mToolbarLeftImgv!!.setImageResource(resId)
        return this
    }

    override fun setNavIconVisibility(isVisible: Boolean): ToolbarTitleView {
        mToolbarLeftImgv!!.visibility = if (isVisible) View.VISIBLE else View.GONE
        return this
    }

    override fun setLeftText(
        leftTxt: String?,
        onClickListener: OnClickListener?
    ): ToolbarTitleView {
        mToolbarLeftTv!!.visibility = View.VISIBLE
        mToolbarLeftTv!!.text = leftTxt
        mToolbarLeftTv!!.setOnClickListener(onClickListener)
        return this
    }

    override fun setLeftText(leftTxt: String?): ToolbarTitleView {
        return setLeftText(leftTxt, null)
    }

    override fun setLeftTextColor(@ColorInt color: Int): ToolbarTitleView {
        mToolbarLeftTv!!.setTextColor(color)
        return this
    }

    override fun setLeftTextSize(sizeOfSp: Int): ToolbarTitleView {
        mToolbarLeftTv!!.textSize = sizeOfSp.toFloat()
        return this
    }

    override fun getLeftText(): TextView? {
        return mToolbarLeftTv
    }

    override fun setTitleText(title: CharSequence?): ToolbarTitleView {
        mTitleTv!!.text = title
        return this
    }

    override fun setTitleText(@StringRes title: Int): ToolbarTitleView {
        mTitleTv!!.setText(title)
        return this
    }

    override fun setTitleTextColor(@ColorInt color: Int): ToolbarTitleView {
        mTitleTv!!.setTextColor(color)
        return this
    }

    override fun setTitleTextSize(sizeOfSp: Int): ToolbarTitleView {
        mTitleTv!!.textSize = sizeOfSp.toFloat()
        return this
    }

    override fun setTitleGravityLeft(): ToolbarTitleView {
        val params =
            mTitleTv!!.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.RIGHT_OF, R.id.toolbar_left_layout)
        params.addRule(RelativeLayout.LEFT_OF, R.id.toolbar_right_layout)
        return this
    }

    override fun getTitleText(): TextView? {
        return mTitleTv
    }

    override fun getToolbar(): Toolbar? {
        //用于后期扩展，暂时没用
        return null
    }

    override fun getView(): View {
        //提供布局控件，外部可能需要扩展如：顶部栏渐变
        return this
    }

    override fun <T : View?> setCenterView(@LayoutRes layoutId: Int): T? {
        val viewStub = findViewById<View>(R.id.stub_center) as ViewStub
        if (viewStub != null) {
            viewStub.layoutResource = layoutId
            return viewStub.inflate() as T
        }
        return null
    }

    override fun <T : View?> setLayoutView(layoutId: Int): T? {
        val viewStub = findViewById<View>(R.id.stub_layout) as ViewStub
        if (viewStub != null) {
            viewStub.layoutResource = layoutId
            return viewStub.inflate() as T
        }
        return null
    }

    override fun addMenuItem(view: View?, onClickListener: OnClickListener?): ITitleView? {
        val param = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT
        )
        param.rightMargin = 12.dp.toInt()
        view?.setOnClickListener(onClickListener)
        mToolbarRightLayout!!.addView(view, param)
        return this
    }


    override fun addMenuItem(viewGroup: ViewGroup?): ToolbarTitleView {
        val param = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.MATCH_PARENT
        )
        mToolbarRightLayout!!.addView(viewGroup, param)
        return this
    }

    override fun addMenuItem(
        text: String?,
        onClickListener: OnClickListener?
    ): TextView {
        val root: View =
            LayoutInflater.from(context).inflate(R.layout.common_top_bar_btn, null)
        val btnTxt =
            root.findViewById<View>(R.id.text) as TextView
        btnTxt.setTextColor(resources.getColor(R.color.color_white))
        btnTxt.tag = context.getString(R.string.lib_common_component_dispatch)
        btnTxt.text = text
        this.addMenuItem(root, onClickListener)
        return btnTxt
    }

    override fun clearMenuItems() {
        mToolbarRightLayout!!.removeAllViews()
    }
}