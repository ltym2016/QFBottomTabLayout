## QFBottomTabLayout
- 支持选中和未选中的图标设置和颜色设置
- 支持选中Tab之后，Tab上覆盖图片
- 支持显示纯文字或者纯图片
- 最少2个tab 最多6个tab
- 支持在中间设置单独按钮，点击不做切换
- 支持选中（没有覆盖图片的情况下）双击操作
- 支持为每个tab右上角设置红点或者数字显示

### 示例展示
<img src="https://github.com/ltym2016/QFBottomTabLayout/blob/master/untitled.gif" width="350"/> <img src="https://github.com/ltym2016/QFBottomTabLayout/blob/fd0c189cb8669faa3a3eae2c2239fd26bd5ce057/device-2019-03-18-113907.png" width="350"/>


### 如何使用
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	implementation 'com.github.ltym2016:QFBottomTabLayout:Tag'
}
```

#### XML
    <com.samluys.tablib.QFBottomTabLayout
                android:id="@+id/tl_2"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:qf_textSelectColor="#2C97DE"
                app:qf_textUnselectColor="#66000000"
                app:qf_background="@color/color_fafafa"
                app:qf_publishMode="COVER"/>
#### 属性说明
| 自定义属性  | 说明  |
| ------------ | ------------ |
| qf_tab_padding  | TAB间的Padding  |
|  qf_tab_space_equal |  设置tab大小等分 |
|  qf_tab_width |  设置Tab的宽度 |
| qf_background  |  设置tab的背景色 |
| qf_textsize  | 设置文字的大小  |
| qf_textSelectColor  | 设置选中文字的颜色  |
| qf_textUnselectColor  | 设置未选中文字的颜色  |
| qf_textVisible  |  是否纯文字显示 |
| qf_iconWidth  |  图片的宽度 |
| qf_iconHeight  |图片的高度   |
|qf_iconMargin| 图片与文字间的间距   |
| qf_centerPublishIcon  | 设置发布按钮固定在中间的图片  |
|  qf_publishMode | 设置发布按钮的状态 （COVER：表示覆盖在tab上  CENTER：表示发布按钮固定在中间）|

### Java代码中使用
```java
// 底部文字
private String[] mTitles = {"首页", "社区", "本地圈", "消息", "发现"};
// 未选中图片
private int[] mIconUnselectIds = {
	R.mipmap.tab1, R.mipmap.tab2,
	R.mipmap.tab3, R.mipmap.tab4, R.mipmap.tab5};
// 选中图片
private int[] mIconSelectIds = {
	R.mipmap.tab1_selected, R.mipmap.tab2_selected,
	R.mipmap.tab3_selected, R.mipmap.tab4_selected, R.mipmap.tab5_selected};
// 覆盖的图片以及对应的位置
private int[] mCoverImageIds = {0, R.mipmap.icon_cover,0, R.mipmap.icon_cover1, 0};

@Override
protected void onCreate(Bundle savedInstanceState) {
	QFBottomTabLayout mTabLayout_1 = findViewById(R.id.tl_1);
	private ArrayList<QFTabEntity> mTabEntities = new ArrayList<>();
	for (int i = 0; i < mTitles.length; i++) {
		 mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
	}
// 设置数据
mTabLayout_1.setTabData(mTabEntities);
// 设置显示数字提醒
mTanLayout_2.showMsg(3, 99);
// 设置红点
mTanLayout_2.showDot(2);
// 设置覆盖的图片以及对应的位置
mTanLayout_2.setCoverImages(mCoverImageIds);
```
#### 部分说明
- 数据实例需要实现QFTabEntity接口
```
public interface QFTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}
```
- setTabData(ArrayList<QFTabEntity> tabEntitys) 设置TAB的数据源
- setCoverImages(int[] coverImages) 在qf_publishMode="COVER" 情况下，设置覆盖图片的资源以及对应的位置信息
- showMsg(position, num)
  - position tab位置
  - num 未读消息的数字 超过3位显示99+
- showDot(position) ： position tab位置
- hideMsg(position) ： 隐藏未读消息提示
- OnTabSelectListener ：TAB切换以及点击事件的监听
```
public interface OnTabSelectListener {
    // 选中
    void onTabSelect(int position);
    // 未选中
	void onTabReselect(int position);
    // 点击覆盖图片
	void onTabPublish(int position);
	// 双击事件
	void onDoubleClick(int position);
}
```
### Thanks
[FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout)
