#### 一、图片加载组件

框架内默认使用Glide作为图片加载框架，考虑后期有可能使用其他库，封装成【ImageLoader】，工具类【ImageLoaderUtil】，示例用法如下：

```
//写法1
ivAvatar.load {
    this.url = coinUrl
    this.isCircle = true
}

//写法2
ImageLoader.loadImage(this,ImageConfigImpl().also {
    it.url = coinUrl
    it.isCircle = true
    it.imageView=ivAvatar
})

//写法3:要处理内存泄漏
private var callback: ILoadBitmapListener? = null
getBitmapByGlide(ImageConfigImpl().also {
    it.url = coinUrl
    it.isCircle = true
    it.imageView=ivAvatar
    },callback = object :ILoadBitmapListener{
    override fun onGetBitmap(bitmap: Bitmap) {
        ivAvatar.setImageBitmap(bitmap)
        //do others
    }
})


override fun onDestroy() {
    callback = null
    clearLoadBitmapListener()
    super.onDestroy()
 }


```

#### 二、更高性能的构建布局方案（在需要极致性能的场景选用）

大概能缩短20倍的构建耗时，示例用法如下：

```
private val rootView by lazy {
    ConstraintLayout {
        layout_width = match_parent
        layout_height = match_parent

        TextView {
            margin_top = 10
            layout_height = 40
            layout_width = match_parent
            textSize = 16f
            textStyle = bold
            text = "请求权限"
            gravity = gravity_center
            layout_id = "tv_permission"
            onClick = {
                PermissionHelper.showInAty(
                    weakReference!!,
                    mutableListOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    needExplainReason = true
                )
            }
            top_toTopOf = parent_id
            start_toStartOf = parent_id
            background_color = "#eeeeee"

        }


        TextView {
            layout_height = 40
            layout_width = match_parent
            margin_top = 10
            textSize = 16f
            textStyle = bold
            text = "显示图片"
            gravity = gravity_center
            layout_id = "tv_img"
            onClick = {
                ivAvatar.visibility = android.view.View.VISIBLE
            }
            top_toBottomOf = "tv_permission"
            start_toStartOf = parent_id
            background_color = "#eeeeee"
        }


        ivAvatar = ImageView {
            layout_id = "iv_show"
            layout_width = 180
            layout_height = 180
            align_horizontal_to = parent_id
            top_toBottomOf = "tv_img"
            visibility = gone
            margin_top = 10
        }

        TextView {
            layout_height = 40
            layout_width = match_parent
            margin_top = 10
            textSize = 16f
            textStyle = bold
            text = "请求网络"
            gravity = gravity_center
            layout_id = "tv_retry_when_error"
            top_toBottomOf = "iv_show"
            start_toStartOf = parent_id
            background_color = "#eeeeee"
            onClick = {
                //使用协程切换线程
                CoroutineScope(Dispatchers.IO).launch {
                    wanAndroidService.login("lizhao", "123457")
                }
            }
        }


        TextView {
            layout_height = 40
            layout_width = match_parent
            margin_top = 10
            textSize = 16f
            textStyle = bold
            text = "请求网络"
            gravity = gravity_center
            layout_id = "tv_retry_when_error"
            top_toBottomOf = "iv_show"
            start_toStartOf = parent_id
            background_color = "#eeeeee"
            onClick = {
                //使用协程切换线程
                CoroutineScope(Dispatchers.IO).launch {
                    wanAndroidService.login("lizhao", "123457")
                }
            }
        }

        TextView {
            layout_height = 40
            layout_width = match_parent
            margin_top = 10
            textSize = 16f
            textStyle = bold
            text = "跳转到其他页面"
            gravity = gravity_center
            layout_id = "iv_start_activity"
            onClick = {
                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            }
            top_toBottomOf = "tv_retry_when_error"
            start_toStartOf = parent_id
            background_color = "#eeeeee"

        }

    }
}


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(rootView)
}
```

[详见这里->](https://juejin.im/post/6844904153068601357#heading-4)

#### 三、权限申请

采用郭霖的PermissionX，同其他开源权限申请框架原理一致：在这个隐藏Fragment中对运行时权限的API进行封装。使用详见帮助类【PermissionHelper】，示例用法如下：

```
PermissionHelper.showInAty( weakReference!!, mutableListOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),  needExplainReason = true)
```



#### 四、MMKV

示例用法如下：

```
MMKVUtil.put("lzx",123)
...
MMKVUtil.getString("lzx")
```



#### 五、网络请求

Retrofit简单封装，一般用法如下：

```
interface TestService {
    @POST("user/login")
    suspend fun login(@Query("username") username: String,@Query("password") password: String):Response<User>
}

...

ServiceCreator.create<TestService>().login("lizhao", "123457")
```

有需要添加拦截器，在Application#onCreate( )里面执行：

```
ServiceCreator.interceptors.add(RetryInterceptor())
ServiceCreator.initRetrofit()
```

#### 六、事件总线

为了减少包大小，使用LiveData实现的事件总线，liveEventBus，示例用法如下：

```
//定义消息
class DemoEvent(val content: String) : LiveEvent {}

//订阅消息
 LiveEventBus.get(DemoEvent::class.java).observe(this, Observer {
            it.content.showToast()
 })

//发送消息
 LiveEventBus.get(DemoEvent::class.java).post(DemoEvent("ssr"))
```

App内发送信息，跨进程使用、App之间发送消息、进程内延迟发送消息等进阶用法可以 [查看源码](https://github.com/JeremyLiao/LiveEventBus)

#### 七、日志组件

Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印，并且支持添加多个日志框架(打印策略)。做到外部调用一次 Api,内部却可以做到同时使用多个策略。比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器。[查看源码](https://github.com/JakeWharton/timber)



基于一般用法，提供二次封装类 【L】：

```
object L {

    private val TAG = "TAG"

    /**
     * 在[Application#onCreate()]里调用该方法
     */
    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }


    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        Timber.tag(tag).d(msg)
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        Timber.tag(tag).e(msg)
    }

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        Timber.tag(tag).i(msg)
    }

    fun v(msg: String) {
        v(TAG, msg)
    }

    fun v(tag: String, msg: String) {
        Timber.tag(tag).v(msg)
    }

    fun w(msg: String) {
        w(TAG, msg)
    }

    fun w(tag: String, msg: String) {
        Timber.tag(tag).w(msg)
    }
}
```

还可以直接Timber进行打印，好处是使用简单，不用输入tag（tag自动为打印位置类的类名）：

```
Timber.v("${activity::class.java.name}#onActivityCreated ")
```

```
2020-10-15 16:46:30.457 22030-22030/com.bsnl.scaffold V/BaseApp$registerActivityLifecycleCallbacks: com.bsnl.scaffold.MainActivity#onActivityCreated
```


