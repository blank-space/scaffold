package com.dawn.base.ui.callback

import com.kingja.loadsir.callback.Callback

data class CallbackConfig(var callbackEmpty: Callback = EmptyLayoutCallback(),
                          var callbackLoading: Callback = LoadingLayoutCallback(),
                          var callbackTransparentLoading: Callback = TransparentLoadingLayoutCallback(),
                          var callbackError: Callback = ErrorLayoutCallback())