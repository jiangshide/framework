package com.zd112.framework.view.jsbridge;

import com.zd112.framework.view.jsbridge.interfaces.BridgeHandler;
import com.zd112.framework.view.jsbridge.interfaces.CallBackFunction;

public class DefaultHandler implements BridgeHandler {

	@Override
	public void handler(String data, CallBackFunction function) {
		if(function != null){
			function.onCallBack("DefaultHandler response data");
		}
	}

}
