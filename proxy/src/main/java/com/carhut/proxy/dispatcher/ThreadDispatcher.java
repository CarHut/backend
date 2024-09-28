package com.carhut.proxy.dispatcher;

public interface ThreadDispatcher<FUN, RES> {
     RES dispatchThread(FUN function);
}
