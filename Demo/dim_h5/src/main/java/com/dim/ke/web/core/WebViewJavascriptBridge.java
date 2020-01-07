package com.dim.ke.web.core;


interface WebViewJavascriptBridge {
	
//	void sendToWeb(Object data);
//
//	void sendToWeb(Object data, OnBridgeCallback responseCallback);
//
//	void sendToWeb(String function, Object... values);

	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);

}
