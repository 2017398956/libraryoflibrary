package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView工具：用于WebView的配置和加载 ; 注意：必须在初始化该类前，初始化构造参数mWebView
 * 
 * @author nfl
 * @date 2015年12月31日
 */
public class WebViewHelper {

	private Context context;
	private WebView mWebView;

	public WebViewHelper(Context context, WebView mWebView) {
		super();
		this.context = context;
		this.mWebView = mWebView;
		initSettings();
	}

	/*
	 * 初始化WebView参数
	 */
	private void initSettings() {
		// 　　setDefaultFontSize(int size); 　　　　　　　　 //设置默认的字体大小
		// 　　setSupportZoom(boolean support); 　　　　　　//设置是否支持变焦
		WebSettings webSettings = mWebView.getSettings();
		// 自适应屏幕
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true); // 设置是否支持缩放
		webSettings.setJavaScriptEnabled(true); // 设置是否支持JavaScript
		webSettings.setDomStorageEnabled(true); // 是否支持文件对象存储
		webSettings.setAppCacheEnabled(true); // 是否支持cache
		String appCacheDir = context.getDir("cache", Context.MODE_PRIVATE)
				.getPath();
		webSettings.setAppCachePath(appCacheDir);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 1);// 设置cache最大值
		webSettings.setAllowFileAccess(true);// 设置启用或禁止访问文件数据
		webSettings.setRenderPriority(RenderPriority.HIGH); // 设置GPL渲染优先权
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		mWebView.setWebChromeClient(mChromeClient);
		mWebView.setWebViewClient(mWebViewClient);
	}

	// WebViewClient主要帮助WebView处理各种通知、请求事件
	private WebViewClient mWebViewClient = new WebViewClient() {
		/**
		 * 其他未复写方法： 1.onReceiveError 接收出错 ; 2.onReceivedHttpAuthRequest ;
		 * 3.doUpdateVisitedHistory(WebView view, String url, boolean isReload)
		 * 更新历史记录 ; 4.onFormResubmission(WebView view, Message dontResend,
		 * Message resend) 重新请求网页数据 ; ...
		 */

		// 处理页面导航，在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			mWebView.loadUrl(url);
			return true;
		}

		// 网页加载完毕
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		// 网页开始加载
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		// 重写此方法可以让webview处理https请求。
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			super.onReceivedSslError(view, handler, error);
			handler.proceed();
		}

		// 重写此方法才能够处理在浏览器中的按键事件
		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			return super.shouldOverrideKeyEvent(view, event);
		}

		// 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
		}

	};

	// WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
	private WebChromeClient mChromeClient = new WebChromeClient() {

		/**
		 * 其他未复写方法： 1.onCloseWindow(关闭WebView) ; 2.onCreateWindow() ;
		 * 3.onJsAlert (WebView上alert无效，需要定制WebChromeClient处理弹出) ; 4.onJsPrompt
		 * ; 5.onJsConfirm ; 6.onProgressChanged ; 7.onReceivedIcon ;
		 * 8.onReceivedTitle ; ...
		 */

		private View myView = null;
		private CustomViewCallback myCallback = null;

		// 获取GPS权限
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		// 设置数据溢出的阀值
		@Override
		public void onExceededDatabaseQuota(String url,
				String databaseIdentifier, long currentQuota,
				long estimatedSize, long totalUsedQuota,
				WebStorage.QuotaUpdater quotaUpdater) {

			quotaUpdater.updateQuota(estimatedSize * 2);
		}

		// 摄者cache大小
		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded,
				long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {

			quotaUpdater.updateQuota(spaceNeeded * 2);
		}

		// 设置默认样式
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			if (myCallback != null) {
				myCallback.onCustomViewHidden();
				myCallback = null;
				return;
			}
			ViewGroup parent = (ViewGroup) mWebView.getParent();
			parent.removeView(mWebView);
			parent.addView(view);
			myView = view;
			myCallback = callback;
			mChromeClient = this;
		}

		@Override
		public void onHideCustomView() {
			if (myView != null) {
				if (myCallback != null) {
					myCallback.onCustomViewHidden();
					myCallback = null;
				}

				ViewGroup parent = (ViewGroup) myView.getParent();
				parent.removeView(myView);
				parent.addView(mWebView);
				myView = null;
			}
		}
	};

	/*
	 * 将网页转换成字符串加载
	 */
	public void loadWithHtmlContent(String content) {
		mWebView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
	}

	/**
	 * 判断是否能够从该网页返回上一个打开的网页; 可用在activity中
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	// mWebView.goBack();
	// // mWebView.goBackOrForward(steps);
	// // mWebView.goForward();
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }
	/**
	 * 以下复写activity的方法用于解决WebView可放大缩小时的崩溃问题
	 */
	// @Override
	// protected void onPause() {
	// // TODO Auto-generated method stub
	// if (null != mWebView) {
	// mWebView.onPause();
	// mWebView.setVisibility(View.GONE);
	// }
	// super.onPause();
	// }
	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// mWebView.setVisibility(View.GONE);
	// if (null != mWebView) {
	// mWebView.removeAllViews();
	// }
	// mWebView.destroy();
	// }
}
