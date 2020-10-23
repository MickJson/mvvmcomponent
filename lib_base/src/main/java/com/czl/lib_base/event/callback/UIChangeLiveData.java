package com.czl.lib_base.event.callback;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.Map;

import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.yokeyword.fragmentation.SupportFragment;

public class UIChangeLiveData extends SingleLiveEvent {
    public SingleLiveEvent<String> showLoadingEvent;
    public SingleLiveEvent<Void> dismissDialogEvent;
    public SingleLiveEvent<Map<String, Object>> startActivityEvent;
    public SingleLiveEvent<SupportFragment> startFragmentEvent;
    public SingleLiveEvent<Map<String, Object>> startContainerActivityEvent;
    public SingleLiveEvent<Void> finishEvent;
    public SingleLiveEvent<Void> onBackPressedEvent;

    public SingleLiveEvent<String> getShowLoadingEvent() {
        return showLoadingEvent = createLiveData(showLoadingEvent);
    }

    public SingleLiveEvent<Void> getDismissDialogEvent() {
        return dismissDialogEvent = createLiveData(dismissDialogEvent);
    }

    public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
        return startActivityEvent = createLiveData(startActivityEvent);
    }

    public SingleLiveEvent<SupportFragment> getStartFragmentEvent() {
        return startFragmentEvent = createLiveData(startFragmentEvent);
    }

    public SingleLiveEvent<Map<String, Object>> getStartContainerActivityEvent() {
        return startContainerActivityEvent = createLiveData(startContainerActivityEvent);
    }

    public SingleLiveEvent<Void> getFinishEvent() {
        return finishEvent = createLiveData(finishEvent);
    }

    public SingleLiveEvent<Void> getOnBackPressedEvent() {
        return onBackPressedEvent = createLiveData(onBackPressedEvent);
    }

    private <T> SingleLiveEvent<T> createLiveData(SingleLiveEvent<T> liveData) {
        if (liveData == null) {
            liveData = new SingleLiveEvent<>();
        }
        return liveData;
    }

    @Override
    public void observe(LifecycleOwner owner, Observer observer) {
        super.observe(owner, observer);
    }
}