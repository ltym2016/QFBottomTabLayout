package com.samluys.tablib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @author luys
 * @describe
 * @date 2019/3/13
 * @email samluys@foxmail.com
 */
public class QFBottomTabLayout extends FrameLayout {

    private Context mContext;
    private ArrayList<QFTabEntity> mTabEntitys = new ArrayList<>();
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private int mTabCount;


    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;
    private int mBackgroundColor;

    /** title */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
    private float mTextsize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;
    private boolean mtextVisible;

    /** icon */
    private boolean mIconVisible;
    private float mIconWidth;
    private float mIconHeight;
    private float mIconMargin;
    private int mHeight;
    private FragmentChangeManager mFragmentChangeManager;
    private int mPublishMode;
    private int mCenterPublishIcon;
    private long firstTab = 0;
    private long secondTab = 0;
    /**
     * 主题色
     */
    private int mThemeColor;

    public QFBottomTabLayout(Context context) {
        this(context, null, 0);
    }

    public QFBottomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QFBottomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 因为需要重写onDraw方法，所以这里需要去掉WILL_NOT_DRAW flag
        setWillNotDraw(false);
        // 允许进行扩展配置
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        mTabsContainer = new LinearLayout(context);
        addView(mTabsContainer);

        obtainAttributes(context, attrs);

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.QFBottomTabLayout);

        mTextsize = ta.getDimension(R.styleable.QFBottomTabLayout_qf_textsize, sp2px(13f));
        mTextSelectColor = ta.getColor(R.styleable.QFBottomTabLayout_qf_textSelectColor, Color.parseColor("#2C97DE"));
        mTextUnselectColor = ta.getColor(R.styleable.QFBottomTabLayout_qf_textUnselectColor, Color.parseColor("#66000000"));
        mTextBold = ta.getInt(R.styleable.QFBottomTabLayout_qf_textBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.QFBottomTabLayout_qf_textAllCaps, false);

        mIconVisible = ta.getBoolean(R.styleable.QFBottomTabLayout_qf_iconVisible, true);
        mIconWidth = ta.getDimension(R.styleable.QFBottomTabLayout_qf_iconWidth, dp2px(0));
        mIconHeight = ta.getDimension(R.styleable.QFBottomTabLayout_qf_iconHeight, dp2px(0));
        mIconMargin = ta.getDimension(R.styleable.QFBottomTabLayout_qf_iconMargin, dp2px(2.5f));

        mTabSpaceEqual = ta.getBoolean(R.styleable.QFBottomTabLayout_qf_tab_space_equal, true);
        mTabWidth = ta.getDimension(R.styleable.QFBottomTabLayout_qf_tab_width, dp2px(-1));
        mTabPadding = ta.getDimension(R.styleable.QFBottomTabLayout_qf_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(10));
        mBackgroundColor = ta.getColor(R.styleable.QFBottomTabLayout_qf_background, Color.parseColor("#fafafa"));
        mPublishMode = ta.getInt(R.styleable.QFBottomTabLayout_qf_publishMode, 0);
        mtextVisible = ta.getBoolean(R.styleable.QFBottomTabLayout_qf_textVisible, true);
        mCenterPublishIcon = ta.getResourceId(R.styleable.QFBottomTabLayout_qf_centerPublishIcon, R.mipmap.icon_center_publish);
        mThemeColor = ta.getColor(R.styleable.QFBottomTabLayout_qf_themeColor, 0);

        ta.recycle();
    }

    public void setTabData(ArrayList<QFTabEntity> tabEntitys) {
        if (tabEntitys == null || tabEntitys.size() == 0) {
            throw new IllegalStateException("TAB data can not be NULL or EMPTY !");
        } else if (tabEntitys.size()< 2 || tabEntitys.size() > 6) {
            throw new IllegalStateException("The number of TAB cannot be less than 2 and cannot exceed 6！");
        }

        this.mTabEntitys.clear();

        if (mPublishMode == 2 && tabEntitys.size() % 2 == 0) {
            QFTabEntity qfTabEntity = new QFTabEntity() {
                @Override
                public String getTabTitle() {
                    return null;
                }

                @Override
                public int getTabSelectedIcon() {
                    return mCenterPublishIcon;
                }

                @Override
                public int getTabUnselectedIcon() {
                    return mCenterPublishIcon;
                }

                @Override
                public int getTabCoverIcon() {
                    return 0;
                }
            };

            int index = tabEntitys.size()/2;
            tabEntitys.add(index, qfTabEntity);
        }

        this.mTabEntitys.addAll(tabEntitys);

        notifyDataSetChanged();
    }

    /**
     * 关联数据支持同时切换fragments
     */
    public void setTabData(ArrayList<QFTabEntity> tabEntitys, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        setTabData(tabEntitys);
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        this.mTabCount = mTabEntitys.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.layout_tab_top, null);
            tabView.setTag(i);
            addTab(i, tabView);
        }

        updateTabStyles();
    }

    /**
     * 创建并添加tab
     * @param position
     * @param tabView
     */
    private void addTab(final int position, View tabView) {
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        if (TextUtils.isEmpty(mTabEntitys.get(position).getTabTitle())) {
            tv_tab_title.setVisibility(GONE);
        } else {
            tv_tab_title.setVisibility(VISIBLE);
            tv_tab_title.setText(mTabEntitys.get(position).getTabTitle());
        }
        ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(mTabEntitys.get(position).getTabUnselectedIcon());
        FrameLayout rl_tab = tabView.findViewById(R.id.rl_tab);
        rl_tab.setBackgroundColor(mBackgroundColor);

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                ImageView publish = v.findViewById(R.id.publish);
                firstTab = secondTab;
                secondTab = System.currentTimeMillis();
                // 双击操作
                if (secondTab - firstTab < 1000 && position == mCurrentTab) {
                    firstTab = 0;
                    secondTab = 0;

                    if (mListener != null) {
                        // 只有在覆图片不显示的情况下才会有双击效果
                        if (mPublishMode != 1 || publish.getVisibility() != VISIBLE) {
                            mListener.onDoubleClick(position);
                        }
                    }
                }

                // 点击了发布按钮固定在中间的情况
                if (mPublishMode == 2 && position == mTabCount/2) {
                    if (mListener != null) {
                        mListener.onTabPublish(position);
                    }
                    return;
                }

                // 点击了覆盖图片的情况
                if (mPublishMode == 1 && publish.getVisibility() == VISIBLE) {
                    if (mListener != null) {
                        mListener.onTabPublish(position);
                    }
                }

                // 切换tab的监听
                if (mCurrentTab != position) {
                    setCurrentTab(position);
                    if (mListener != null) {
                        mListener.onTabSelect(position);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onTabReselect(position);
                    }
                }
            }
        });

        // 每一个Tab的布局参数
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }
        mTabsContainer.addView(tabView, position, lp_tab);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            if (mtextVisible) {
                if (!TextUtils.isEmpty(tv_tab_title.getText().toString())) {
                    tv_tab_title.setVisibility(VISIBLE);
                }

                // 设置主题色的情况下优先使用主题色
                if(mThemeColor != 0) {
                    mTextSelectColor = mThemeColor;
                }

                tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize);
                if (mTextAllCaps) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }

                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tv_tab_title.getPaint().setFakeBoldText(false);
                }
            } else {
                tv_tab_title.setVisibility(GONE);
            }

            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);

            ImageView publish = tabView.findViewById(R.id.publish);
            // 是否现在覆盖图片
            if (mPublishMode == 1 && i == mCurrentTab && mTabEntitys.get(i).getTabCoverIcon() != 0) {
                publish.setVisibility(VISIBLE);
                publish.setImageResource(mTabEntitys.get(i).getTabCoverIcon());
            } else {
                publish.setVisibility(GONE);
            }

            // 显示图片
            if (mIconVisible) {
                iv_tab_icon.setVisibility(View.VISIBLE);
                QFTabEntity tabEntity = mTabEntitys.get(i);
                // 设置Tab的图片 选择和未选择
                setTabIcon(i, i == mCurrentTab, iv_tab_icon, tabEntity);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        mIconWidth <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconWidth,
                        mIconHeight <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconHeight);
                lp.bottomMargin = (int) mIconMargin;

                iv_tab_icon.setLayoutParams(lp);
            } else {
                // 纯文字显示情况
                if (mPublishMode == 2 && i == mTabCount/2) {
                    iv_tab_icon.setVisibility(View.VISIBLE);
                } else {
                    iv_tab_icon.setVisibility(View.GONE);
                }

            }
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;

            ImageView publish = tabView.findViewById(R.id.publish);


            if (mPublishMode == 1 && isSelect && mTabEntitys.get(i).getTabCoverIcon() != 0) {
                publish.setImageResource(mTabEntitys.get(i).getTabCoverIcon());
                publish.setVisibility(VISIBLE);
            } else {
                publish.setVisibility(GONE);
            }
            TextView tab_title = tabView.findViewById(R.id.tv_tab_title);
            if(mThemeColor != 0) {
                mTextSelectColor = mThemeColor;
            }
            tab_title.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            QFTabEntity tabEntity = mTabEntitys.get(i);
            // 设置Tab的图片 选择和未选择
            setTabIcon(i, isSelect, iv_tab_icon, tabEntity);

            if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                tab_title.getPaint().setFakeBoldText(isSelect);
            }
        }
    }

    /**
     * 设置Tab的图片
     * @param i
     * @param isSelect
     * @param iv_tab_icon
     * @param tabEntity
     */
    private void setTabIcon(int i, boolean isSelect, ImageView iv_tab_icon, QFTabEntity tabEntity) {
        if (mThemeColor != 0) {
            if (i == mCurrentTab) {
                iv_tab_icon.setImageDrawable(
                        getTintDrawable(mContext, tabEntity.getTabSelectedIcon(),
                                mThemeColor));
            } else {
                iv_tab_icon.setImageResource(tabEntity.getTabUnselectedIcon());
            }
        } else {
            iv_tab_icon.setImageResource(isSelect ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
        }
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        this.mCurrentTab = currentTab;
        updateTabSelection(currentTab);
        if (mFragmentChangeManager != null) {
            if (mPublishMode == 2) {
                // 中间添加按钮的位置
                int centerAddPosition = mTabEntitys.size() / 2;
                if (currentTab > centerAddPosition) {
                    mFragmentChangeManager.setFragments(currentTab - 1);
                } else {
                    mFragmentChangeManager.setFragments(currentTab);
                }
            } else {
                mFragmentChangeManager.setFragments(currentTab);
            }
        }
        invalidate();
    }


    public void setTabPadding(float tabPadding) {
        this.mTabPadding = dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.mTabWidth = dp2px(tabWidth);
        updateTabStyles();
    }

    public void setTextsize(float textsize) {
        this.mTextsize = sp2px(textsize);
        updateTabStyles();
    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    public void setTextBold(int textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
    }

    public void setIconVisible(boolean iconVisible) {
        this.mIconVisible = iconVisible;
        updateTabStyles();
    }

    public void setIconWidth(float iconWidth) {
        this.mIconWidth = dp2px(iconWidth);
        updateTabStyles();
    }

    public void setIconHeight(float iconHeight) {
        this.mIconHeight = dp2px(iconHeight);
        updateTabStyles();
    }

    public void setIconMargin(float iconMargin) {
        this.mIconMargin = dp2px(iconMargin);
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        updateTabStyles();
    }

    public void setPublishMode(int publishMode) {
        this.mPublishMode = publishMode;
        updateTabStyles();
    }

    public void setTextVisible(boolean textVisible) {
        this.mtextVisible = textVisible;
        updateTabStyles();
    }

    public void setCenterPublishIcon(int centerPublishIcon) {
        this.mCenterPublishIcon = centerPublishIcon;
        updateTabStyles();
    }

    public void setThemeColor(int themeColor) {
        this.mThemeColor = themeColor;
        updateTabStyles();
    }

    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public float getTextsize() {
        return mTextsize;
    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return mTextUnselectColor;
    }

    public int getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public float getIconWidth() {
        return mIconWidth;
    }

    public float getIconHeight() {
        return mIconHeight;
    }

    public float getIconMargin() {
        return mIconMargin;
    }

    public boolean isIconVisible() {
        return mIconVisible;
    }


    public ImageView getIconView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        ImageView iv_tab_icon = (ImageView) tabView.findViewById(R.id.iv_tab_icon);
        return iv_tab_icon;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int position, int num) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num);

            if (mInitSetMap.get(position) != null && mInitSetMap.get(position)) {
                return;
            }

            setMsgMargin(position, num);

            mInitSetMap.put(position, true);
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        showMsg(position, 0);
    }

    public void hideMsg(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = (MsgView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     */
    public void setMsgMargin(int position, int num) {
        int bottomPadding = 12;
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextsize);
            float textWidth = mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();

            float iconH = mIconHeight;
            float margin = 0;
            if (mIconVisible) {
                if (iconH <= 0) {
                    iconH = mContext.getResources().getDrawable(mTabEntitys.get(position).getTabSelectedIcon()).getIntrinsicHeight();
                }
                margin = mIconMargin;
            }


            if (num > 0) {
                if (!mIconVisible) {
                    bottomPadding = 6;
                } else {
                    bottomPadding = 2;
                }
            } else {
                if (mIconVisible && mtextVisible) {
                    bottomPadding = 4;
                }
            }
            lp.leftMargin = (int) textWidth/2;
            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight - iconH - margin) / 2 - dp2px(bottomPadding) : dp2px(bottomPadding);

            tipView.setLayoutParams(lp);
        }
    }

    /** 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置 */
    public MsgView getMsgView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = (MsgView) tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    public static Drawable getTintDrawable(Drawable drawable, @ColorInt int color) {
        Drawable.ConstantState state = drawable.getConstantState();
        Drawable drawable1 = DrawableCompat.wrap(state == null ? drawable : state.newDrawable()).mutate();
        drawable1.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        DrawableCompat.setTint(drawable1, color);
        return drawable1;
    }

    public static Drawable getTintDrawable(Context context, int id, int color) {
        if (id != 0) {
            Drawable originDrawable = ContextCompat.getDrawable(context, id);
            if (originDrawable != null) {
                return getTintDrawable(originDrawable, color);
            }
        }

        return null;
    }
}
