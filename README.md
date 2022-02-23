

### Gradle管理

在以前的项目中大部分是用ext 管理方式，但在 gradle 中没有智能提示，也不能跳转到这些变量，局限性太高是个硬伤，那么现在有更好的解决方案吗？答案是肯定的，就是利用 `buildSrc` 来更好地管理 Gradle。



那么具体怎么操作呢，现在来揭晓其真面目，写过插件的肯定知道 `buildSrc` 这个目录，这个目录存在于项目根目录下，例如我这里就是 `scaffold/buildSrc`，提醒一下，这个是要**自己创建**的哦，它默认是直接会参与编译，所以不用加入到 `setting.gradle` 中，具体如下图所示：



<img src="https://upload-images.jianshu.io/upload_images/1634465-30a48ff9cd905714.png?imageMogr2/auto-orient/strip|imageView2/2/w/424/format/webp" alt="gradle03"  />



<img src="https://upload-images.jianshu.io/upload_images/1634465-db1ff87c680fb65d.png?imageMogr2/auto-orient/strip|imageView2/2/w/729/format/webp" alt="gradle04"  />



使用buildSrc管理gradle依赖：在dependencies中输入依赖库时可以智能提示，支持点击跳转到具体的依赖配置信息。

### 组件化架构图



![](https://upload-images.jianshu.io/upload_images/1634465-2d57640f5408e9d1.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

### 组件单独调试

在 AndroidStudio 开发 Android 项目时，使用的是 Gradle 来构建，具体来说使用的是 Android Gradle 插件来构建，Android Gradle 中提供了三种插件，在开发中可以通过配置不同的插件来配置不同的工程。

- App 插件，id: com.android.application
- Library 插件，id: com.android.libraay
- Test 插件，id: com.android.test

区别比较简单， App 插件来配置一个 Android App 工程，项目构建后输出一个 APK 安装包，Library 插件来配置一个 Android Library 工程，构建后输出 aar 包，Test 插件来配置一个 Android Test 工程。

再看一遍架构图，我们可以发现 feature 中每个 feature 中都存有 app、pkg（、export） 模块，我们自底向上来一步步操作，分别创建 `:feature:sample:export` 和 `:feature:sample:pkg 模块 `，最后创建`:feature:sample:app` 模块。建立完成后的setting.gradle文件如下所示：

```
include ':feature:sample:pkg'
include ':feature:sample:export'
include ':feature:sample:app'
```



<img src="https://upload-images.jianshu.io/upload_images/1634465-68d1cdc133da409e.png?imageMogr2/auto-orient/strip|imageView2/2/w/477/format/webp" alt="gradle02" style="zoom:100%;" />

如上图所示，我有多个 feature,在gradle同步完毕后，便能看如下的运行选项，每个feature都可以独立运行

<img src="https://upload-images.jianshu.io/upload_images/1634465-cad902a51877d1fb.png?imageMogr2/auto-orient/strip|imageView2/2/w/306/format/webp" alt="gradle01" style="zoom:100%;" />



### 组件间数据传递与方法的互相调用

由于主项目与组件，组件与组件之间都是不可以直接使用类的相互引用来进行数据传递的，那么在开发过程中如果有组件间的数据传递时应该如何解决呢，这里我直接采用Arouter来解决。

在架构图中说了：export层包含要对外导出的api和bean。新建一个对外暴露的接口（继承IProvide），同时将所需要序列化的对象写到一起，同时对外提供路由的路径。具体如下图所示：

<img src="https://upload-images.jianshu.io/upload_images/1634465-d38d6f40258b40a8.png?imageMogr2/auto-orient/strip|imageView2/2/w/1038/format/webp" alt="arout01" style="zoom:100%;" />

在所属的feature#pkg层完成路由跳转的具体实现：

<img src="https://upload-images.jianshu.io/upload_images/1634465-b0681e81a04fd29d.png?imageMogr2/auto-orient/strip|imageView2/2/w/885/format/webp" alt="arout02" style="zoom:100%;" />

在集成全部组件一起调试之前，如果组件与组件之间有相互调用关系（例如featureA依赖FeatureB），可以先借助mock层实现一些简单的回调，如下所示：

<img src="https://upload-images.jianshu.io/upload_images/1634465-28fc3c72802c3adc.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp" alt="arout03" style="zoom:100%;" />

然后在pkg模块依赖FeatureB#export模块，如下所示：

<img src="https://upload-images.jianshu.io/upload_images/1634465-e3366aa420c90f77.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp" alt="arout04" style="zoom:100%;" />

### 组件内第三方库初始化

Java SPI全称Service Provider Interface，是Java提供的一套用来被第三方实现或者扩展的API，它可以用来启用框架扩展和替换组件。实际上是“基于接口的编程＋策略模式＋配置文件”组合实现的动态加载机制.

我们可以借助ServiceLoader + @AutoService实现动态加载。

添加依赖：

```bash
implementation 'com.google.auto.service:auto-service:1.0'
annotationProcessor 'com.google.auto.service:auto-service:1.0'
```

在base里定义接口：

```
interface BaseAppInit {

    /**需要在第一时间被初始化的*/
    fun onInitSpeed(app:Application)

    /**低优先级初始化*/
    fun onInitLow(app:Application)
}
```

在组件内完成具体实现，实现类上面加上一个@AutoService注解，参数则为接口的class类：

```
@AutoService(BaseAppInit::class)
class SampleModuleInit : BaseAppInit {

    override fun onInitSpeed(app: Application) {
        Log.d("@@","SampleInit#onInitSpeed")
    }

    override fun onInitLow(app: Application) {
        Log.d("@@","SampleInit#onInitLow")
    }
}
```

然后在base中创建一个代理类：

```
/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/3
 * @desc   : 组件初始化的工作将由该代理类代理实现
 */
class LoadModuleProxy : BaseAppInit {

    private var mLoader: ServiceLoader<BaseAppInit> =
        ServiceLoader.load(BaseAppInit::class.java)

    override fun onInitSpeed(app: Application) {
        mLoader.forEach {
            it.onInitSpeed(app)
        }
    }

    override fun onInitLow(app: Application) {
        doOnMainThreadIdle({
            mLoader.forEach {
                it.onInitLow(app)
            }
        },0)
    }
}
```

最后在app中调用方法即可。

```
private val loadModuleProxy by lazy { LoadModuleProxy() }
...
loadModuleProxy.onInitSpeed(application)
loadModuleProxy.onInitLow(application)
```

### 组件集成调试

在壳App中将所有feature#pkg集成起来即可。

```
implementation project(':feature:feature0:pkg')
implementation project(':feature:feature1:pkg')
```



### 基础架构

#### 页面状态管理

使用LoadSir来实现页面状态管理。

```
open fun initLoadSir() {
    LoadSir.beginBuilder()
        .addCallback(ErrorLayoutCallback())
        .addCallback(EmptyLayoutCallback())
        .addCallback(LoadingLayoutCallback())
        .addCallback(TransparentLoadingLayoutCallback())
        .commit()
}
```

baseApp中只添加了几种常用的callback,如果你即想保留全局配置，又想在某个特殊页面加点不同的配置，例如用骨架图代替Loading，在Activity或者Fragment中可采用该方式：

```
override fun getCallbackConfig(): CallbackConfig? {
    return CallbackConfig(callbackLoading = PlaceholderCallback())
}
```

LoadSir是根据ViewState做对应的展示，ViewState目前有6种状态:

```
enum class ViewState {
    STATE_LOADING, STATE_COMPLETED, STATE_ERROR, STATE_EMPTY, STATE_COMMIT, STATE_NETWORK_ERROR
}
```

初始化LoadSir配置的代码如下（WrapLayoutDelegateImpl#initLoadServiceConfig()）：

```（
private fun initLoadServiceConfig(){
    childView?.let {
        loadService = getLoadSir().register(it, null, Convertor<ViewState> { v ->
            val resultCode: Class<out Callback?> = when (v) {
                ViewState.STATE_LOADING -> getLoadingClass()
                ViewState.STATE_COMMIT -> getTransparentLoadingClass()
                ViewState.STATE_ERROR -> getErrorClass()
                ViewState.STATE_EMPTY -> getEmptyClass()
                else -> SuccessCallback::class.java
            }
            resultCode
        }) as LoadService<ViewState>?
    }
}
```

#### 事件总线

使用UnPeekLiveData和ViewModel实现事件总线。

ViewModel可以根据职能再细分为Event-ViewModel和State-ViewModel。

- State-ViewModel，**职责仅限于状态托管和恢复**，也即 State-ViewModel 中主要包含 ObservableField、LiveData、Request 以及它们的初始化操作，**除此之外不包含任何逻辑**。

- Event-ViewModel，**基于application作用域实现整个App内的状态共享**。通过UnPeekLiveData分发到所有订阅者页面。UnPeekLiveData通过 “访问控制权限” 做了 “读写分离”，即，这些 UnPeekLiveData 对 Activity/Fragment 只读，对数据的修改只能来自于唯一可信源内部，通过这样的设计理念来确保 “**消息的一致性和同步性**”。

用法如下：

```
class ShareViewModel : ViewModel() {    

    val countLiveData = UnPeekLiveData<Int>()
    
 }
 
class AActivity : XXX {        
    private val event: ShareViewModel by lazy {        
        getApplicationScopeViewModel()}
        ...  
        //发送事件        event.countLiveData.value = 1234
}

class BActivity : XXX {        
        private var testEventObserver: Observer<Int>? = null
        private val event: ShareViewModel by lazy {        
        getApplicationScopeViewModel()}
        
        override fun onCreate() {                
            ...                
            event.countLiveData.observeForever(Observer<Int> {        
                L.d("event value:$it") }.apply {
                    testEventObserver = this
                 })       
         }
             
        override fun onDestroy() {       
             super.onDestroy()        
             if (testEventObserver != null) {            
                 event.countLiveData.removeObserver(testEventObserver!!)      
              }    
        }
 }
```

#### 普通页面

继承BaseActivity或BaseFragment, 在泛型中写上对应的ViewModel和ViewBinding的具体实例即可完成ViewModel和ViewBinding的初始化，实现原理就是反射。

```
class XxxActivity : BaseActivity<XxxViewModel, XxxBinding>() {  
   override fun initView() {          
   
   }
}
```

其中ViewModel需要继承BaseViewModel。BaseViewModel封装了多种请求数据及处理请求结果的方法：

```
abstract class BaseViewModel : ViewModel() {

    /** 切换页面状态 */
    val viewState = MutableLiveData(ViewStateWithMsg(msg = null, state = ViewState.STATE_COMPLETED))


    /**
     * 请求数据显示带白色背景的Loading
     */
    fun <T> fetchData(
        flow: Flow<DataResult<T>>,
        viewState: MutableLiveData<ViewStateWithMsg>,
    ): LiveData<T?> = liveData {
        flow.onStart {
            viewState.postValue(
                ViewStateWithMsg(state = ViewState.STATE_LOADING)
            )
        }.catch {
            handleTheCatchException(it.message, viewState)
        }.collectLatest {
            handleCollections(it, viewState)
        }
    }

    /**
     * 只请求数据，不处理页面状态
     */
    @ExperimentalCoroutinesApi
    fun <T> fetchDataWithoutState(
        flow: Flow<DataResult<T>>,
    ): LiveData<T?> = liveData {
        flow.catch {
            handleTheCatchException(it.message, null)
        }.collectLatest {
            handleCollections(it, null)
        }
    }

    /**
     * 提交数据过程中显示透明背景的loading
     */
    fun <T> commitDataWithLoading(
        flow: Flow<DataResult<T>>,
        viewState: MutableLiveData<ViewStateWithMsg>,
    ): LiveData<T?> = liveData {
        flow.onStart {
            viewState.postValue(
                ViewStateWithMsg(state = ViewState.STATE_COMMIT)
            )
        }.catch {
            viewState.postValue(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
            if (!NetworkUtils.isConnected()) {
                "无法联网，请检查网络".showToast()
            } else {
                it.message?.showToast()
            }
        }.collectLatest {
            viewState.postValue(
                ViewStateWithMsg(state = ViewState.STATE_COMPLETED)
            )
            if (it.isSuccessFul) {
                emit(it.data)
            } else {
                it.msg?.showToast()
            }
        }
    }


    private fun handleTheCatchException(
        message: String?,
        viewState: MutableLiveData<ViewStateWithMsg>?
    ) {
        if (!NetworkUtils.isConnected()) {
            "网络出问题了".showToast()
            viewState?.postValue(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
            return
        }
        message?.let {
            viewState?.postValue(
                ViewStateWithMsg(
                    msg = "网络似乎出现了问题",
                    state = ViewState.STATE_ERROR
                )
            )
        }
    }

    private suspend fun <T> LiveDataScope<T?>.handleCollections(
        it: DataResult<T>,
        viewState: MutableLiveData<ViewStateWithMsg>?
    ) {
        if (it.isSuccessFul) {
            viewState?.postValue(
                ViewStateWithMsg(
                    msg = it.msg,
                    state = ViewState.STATE_COMPLETED
                )
            )
            emit(it.data)
        } else {
            it.msg?.let { it.showToast() }
            viewState?.postValue(
                ViewStateWithMsg(
                    msg = it.msg,
                    state = ViewState.STATE_ERROR
                )
            )
        }
    }

}
```

#### 列表页

如果使用一个recyclerView就能搞定，请继承**SimpleListActivity < VM:ViewModel > **，实现registerItem方法以及实现XxxViewBinder( )就能完成一个列表页，是不是很简单呢？

```
class SampleActivity : SimpleListActivity<SampleViewModel>() {       
 override fun registerItem(adapter: MultiTypeAdapter?) {        
     adapter?.apply {    register(XxxViewBinder()) }    
 }
 
 override fun initView() {        
     super.initView()        
     getTitleView()?.setTitleText(TAG)    
 }}
```

然后让Viewmodel继承BaseListViewModel()，BaseListViewModel这个类集中处理了下拉刷新、加载更多、空数据以及是否没有更多数据等逻辑：

```
abstract class BaseListViewModel : BaseViewModel(), IList, IBaseListViewModel {

    @RequestType.Val
    var mRequestType = RequestType.INIT
    protected open var pageNo = DEFAULT_START_PAGE_INDEX
    protected open var pageSize: Int = DEFAULT_PAGE_SIZE
    protected var mData: MutableList<Any> = mutableListOf()


    /**
     * 结束刷新动作
     */
    private val _finishRefresh = MutableLiveData(false)
    val finishRefresh: LiveData<Boolean> = _finishRefresh

    /**
     * 结束加载更多动作
     */
    private val _finishLoadMore = MutableLiveData(false)
    val finishLoadMore: LiveData<Boolean> = _finishLoadMore

    /**
     * adapter更新数据
     */
    private val _notifyDataChange = MutableLiveData(0)
    val notifyDataChange: LiveData<Int> = _notifyDataChange

    /**
     * 是否可以加载更多
     */
    private val _enableLoadMore = MutableLiveData(true)
    val enableLoadMore: LiveData<Boolean> = _enableLoadMore

    /**
     * 没有更多数据
     */
    private val _noMoreData = MutableLiveData(false)
    val noMoreData: LiveData<Boolean> = _noMoreData

    var resultLiveData: LiveData<DataResult<out Any>?>? = null

    init {
        viewModelScope.launch {
            resultLiveData =  fetchListData(mRequestType)
        }
    }

    /**
     * 请求入口
     */
    @ExperimentalCoroutinesApi
    override fun fetchListData(requestType: Int): LiveData<DataResult<out Any>?>? {
        mRequestType = requestType
        when (requestType) {
            RequestType.INIT -> processPreInitData()
            RequestType.REFRESH -> pageNo = DEFAULT_START_PAGE_INDEX
            RequestType.LOAD_MORE -> { }
        }

        return liveData {
            getList()?.
            catch {
                processError("网络似乎出现了问题")
            }?.collectLatest {
                if (it?.isSuccessFul!!) {
                    processData(it)
                    emit(it as DataResult<Any>?)
                } else {
                    processError(it.msg)
                }
            }
        }
    }

    /**
     * 处理错误
     */
    private fun processError(errMsg: String?) {
        when (mRequestType) {
            RequestType.REFRESH -> {
                _finishRefresh.postValue(true)
            }
            RequestType.LOAD_MORE -> {
                pageNo--
                if (pageNo < 0) pageNo = DEFAULT_START_PAGE_INDEX
                _finishLoadMore.postValue(true)
            }
        }
        processInitDataError(errMsg)
    }


    /**
     * 处理数据
     */
    protected open fun processData(t: DataResult<out Any>?) {
        if (t == null) {
            setState(value = ViewState.STATE_ERROR)
            return
        }
        when (mRequestType) {
            RequestType.INIT -> {
                setState(value = ViewState.STATE_COMPLETED)
                initView(t, mRequestType)
            }
            RequestType.REFRESH -> {
                _finishRefresh.postValue(true)
                initView(t, mRequestType)
            }
            RequestType.LOAD_MORE -> {
                _finishLoadMore.postValue(true)
                processLoadMoreData(t)
            }
            RequestType.SILENT_REFRESH -> initView(t, mRequestType)
        }
    }

    /**
     * 初始化页面数据（子类可重写）
     */
    protected open fun initView(
        listResponseBean: DataResult<out Any>?,
        @RequestType.Val requestType: Int
    ) {
        if (listResponseBean?.data is IBaseList) {
            val data: IBaseList = listResponseBean.data as IBaseList
            if (!data.getDataList().isNullOrEmpty()) {
                mData.clear()
                mData.addAll(data.getDataList()!!)
                processInitLoadMoreState(data)
                pageNo++
                _notifyDataChange.postValue(0)
            } else {
                processEmptyState()
            }
        } else {
            throw  RuntimeException("列表数据必须实现IBaseList接口")
        }
    }

    /**
     * Desc:设置初始化页面或刷新页面时是否可以加载更多，如果页面默认没有加载更多，
     * 可重写，默认空实现
     * @param baseList
     */
    protected open fun processInitLoadMoreState(baseList: IBaseList?) {
        if (isLoadMoreNoData(baseList)) {
            finishLoadMoreWithNoMoreData()
        } else {
            _enableLoadMore.postValue(true)
        }
    }

    /**
     * 处理没有更多数据
     */
    protected open fun finishLoadMoreWithNoMoreData() {
        _enableLoadMore.postValue(false)
        _noMoreData.postValue(true)

    }


    /**
     * Desc:加载更多没有后续数据
     *
     * @param baseList
     * @return boolean true:没有后续数据
     */
    protected open fun isLoadMoreNoData(baseList: IBaseList?): Boolean {
        if (baseList?.getTotals()!! > 0) {
            return mData.size >= baseList.getTotals()
        } else {
            return judgeLoadMoreNoDataByPageSize(baseList)
        }
    }

    /**
     * Desc:处理加载更多的数据（子类可重写）
     *
     * @param listResponseBean
     */
    protected open fun processLoadMoreData(listResponseBean: DataResult<out Any>?) {
        if (listResponseBean?.data is IBaseList) {
            val data: IBaseList = listResponseBean?.getData() as IBaseList
            val insertP = mData.size
            if (!data.getDataList().isNullOrEmpty()) {
                mData.addAll(data.getDataList()!!)
                if (isLoadMoreNoData(data)) {
                    finishLoadMoreWithNoMoreData()
                }
                pageNo++
            } else {
                finishLoadMoreWithNoMoreData()
            }
            _notifyDataChange.postValue(insertP)
        } else {
            throw  RuntimeException("列表数据必须实现IBaseList接口")
        }
    }

    /**
     *  通过pageSize判断更多没有后续数据
     */
    private fun judgeLoadMoreNoDataByPageSize(baseList: IBaseList?): Boolean {
        return baseList?.getDataList()?.size!! < pageSize
    }

    /**
     * 初始化页面数据获取失败后的处理(子类可重写)
     */
    protected open fun processInitDataError(errMsg: String?) {
        if (mData.size == 0) {
            if (errMsg.isNullOrBlank()) {
                setState("网络似乎出现了问题", ViewState.STATE_NETWORK_ERROR)
            } else {
                setState(errMsg, ViewState.STATE_ERROR)
            }
            return
        }
        if (!NetworkUtils.isConnected()) {
            "当前无网路连接，请检查网络".showToast()
            setState("网络似乎出现了问题", ViewState.STATE_NETWORK_ERROR)
        }
    }


    /**
     * 初始化数据前的逻辑处理(子类可重写)
     */
    protected open fun processPreInitData() {
        pageNo = DEFAULT_START_PAGE_INDEX
        setState("", ViewState.STATE_LOADING)
    }

    private fun setState(msg: String? = "", value: ViewState) {
        viewState.postValue(ViewStateWithMsg(msg = msg, state = value))
    }

    override fun providerData(): MutableList<Any> {
        return mData
    }

    /**
     * 处理空状态
     */
    protected open fun processEmptyState() {
        setState("空空如也～", ViewState.STATE_EMPTY)
    }

}
```

反之，请继承 **BaseListActivity<VM : BaseViewModel,VB : ViewBinding>，** 且布局中的recyclerView的Id必须叫rv。

Adapter使用了MultiType，一个class对应一个ItemViewBinder。新建ItemViewBinder请继承**BindingViewDelegate<T, VB : ViewBinding>, 简化创建ViewHolder的模版代码以及封装了点击事件相关代码。**

添加子view的点击事件监听：**holder.addOnChildClickListener(R.id.xxx)**。然后在activity/fragment里添加如下代码：

```
ItemClickSupport.addTo(getRecyclerView()).setOnItemClickListener(object : ItemClickSupport.OnItemClick<MultiTypeAdapter>() {
        override fun onItemChildClick(            
        adapter: MultiTypeAdapter?,view: View?, position: Int) {    
                 
        }    
 })
```

如果还需要子View长按事件或itemView长按事件，实现对于的方法即可。

```
public static abstract class OnItemClick<T extends MultiTypeAdapter> {    
		public void onItemClick(T adapter, View view, int position) { }
    public void onItemLongClick(T adapter, View view, int position) { }
    public void onItemChildClick(T adapter, View view, int pos) { }
    public void onItemChildLongClick(T adapter, View view, int pos) { }
}
```



#### 网络请求（Retrofit+flow+Moshi）

BaseNetworkApi是个抽象类，构造函数的参数为 baseUrl，实现类可以穿入自己需要的baseUrl。

我们利用了泛型擦除的特性，在创建一个带泛型参数的Interface也就是IService，那么在BaseNetworkApi里面就可以通过getServiceClass函数来获取子类传进来的泛型参数。

然后就是对外提供了service的实现，service是通过lazy来延迟加载，具体就是Retrofit+OkHttp那一套东西，相信大家都烂熟于心了。其中defaultOkHttpClient笔者放到了伴生对象里面，目的是保证defaultOkHttpClient有且只有一个。

```
private const val TIME_OUT = 20L

abstract class BaseNetworkApi<I>(private val baseUrl: String = "https://www.wanandroid.com/") :
    IService<I> {
    val moshi = Moshi.Builder().add(BigDecimalAdapter)
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(DefaultOnDataMismatchAdapter.newFactory(Any::class.java, null))
        .add(FilterNullValuesFromListAdapter.newFactory(Any::class.java))
        .build()

    protected val service: I by lazy {
        getRetrofit().create(getServiceClass())
    }

    open fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun getServiceClass(): Class<I> {
        val genType = javaClass.genericSuperclass as ParameterizedType
        return genType.actualTypeArguments[0] as Class<I>
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClient = getCustomOkHttpClient()
        if (null != okHttpClient) {
            return okHttpClient
        }
        return defaultOkHttpClient
    }

    open fun getCustomOkHttpClient(): OkHttpClient {
        return defaultOkHttpClient
    }

    protected open fun getCustomInterceptor(): Interceptor? {
        return null
    }


    companion object {
        const val TAG = "BaseNetworkApi"
        private const val RETRY_COUNT = 2
        private val defaultOkHttpClient by lazy {
            val builder = OkHttpClient.Builder()
                .callTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)


            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }

            builder.build()
        }
    }
}
```

然后新建一个单例的仓库类去继承BaseNetworkApi，泛型参数传入Service接口，具体代码如下：

```
object WanAndroidRepository : BaseNetworkApi<WanAndroidService>() {

    @ExperimentalCoroutinesApi
     fun getTopArticles(): Flow<DataResult<List<Article>>> {
        return simpleSlow {
            emit(service.getTopArticles())
        }
    }
 }
```

```
interface WanAndroidService {
    @GET("/article/top/json")
    suspend fun getTopArticles(): DataResult<List<Article>>

    @GET("/banner/json")
    suspend fun getBanners(): DataResult<List<Banner>>

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResult<UserInfo>
}
```

最后，在ViewModel里使用flow调用接口，就能拿到网络的请求结果了：

```
viewModelScope.launch {
            WanAndroidRepository.getTopArticles().collect {
                _uiState.value = it.data
            }
        }
```

#### 序列化（moshi）

Gson很好用，但是跟kotlin结合起来就有些问题：Gson无法解析设置Kotlin默认参数。

如果给data class的属性都赋予默认值：

```kotlin
data class SomeData(
    val intValue: Int = 0,
    val strValue: String = "default value"
)
```

服务端返回的json数据是：

```
{"intValue"=2,"strValue"=null}
```

解析json,这时候strValue覆盖默认值为null，最终导致了非空类型指向了null。

所以使用Gson解析时，我们能信任的data class只有下面这种，将所有字段设为`可空类型`

```kotlin
data class SomeData(
    val intValue: Int?,
    val strValue: String?
)
```

但我希望在属性没有对应的值{ }或者为空就抛出异常，除非我们将它定义为可空的。

而Moshi就能满足这个要求。

#### 轻量级的本地持久化

```
object MMKVUtil {
    private val mKv by lazy {
        MMKV.defaultMMKV()
    }

    fun put(key: String?, value: Any) {
        when {
            value is Int -> mKv.encode(key, value)
            value is String -> mKv.encode(key, value)
            value is Boolean -> mKv.encode(key, value)
            value is Float -> mKv.encode(key, value)
            value is Long -> mKv.encode(key, value)
            value is Double -> mKv.encode(key, value)
            value is Parcelable -> mKv.encode(key, value)
        }
    }

    fun getString(k: String?): String {
        return getString(k, "")
    }

    fun getString(k: String?, def: String?): String {
        return mKv.decodeString(k, def) ?: ""
    }

    fun getInt(k: String?): Int {
        return mKv.decodeInt(k, 0)
    }

    fun getInt(k: String?, v: Int): Int {
        return mKv.decodeInt(k, v)
    }

    fun getBoolean(k: String?): Boolean {
        return mKv.decodeBool(k, false)
    }

    fun getFloat(k: String?): Float {
        return mKv.decodeFloat(k, 0.0f)
    }

    fun getLong(k: String?): Long {
        return mKv.decodeLong(k, 0L)
    }

    fun getDouble(k: String?): Double {
        return mKv.decodeDouble(k)
    }

    fun <T : Parcelable> getParcelable(k: String?, clz: Class<T>): T? {
        return mKv.decodeParcelable(k, clz)
    }
}
```

