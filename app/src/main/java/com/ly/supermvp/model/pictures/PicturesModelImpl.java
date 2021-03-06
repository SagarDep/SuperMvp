package com.ly.supermvp.model.pictures;

import android.text.TextUtils;

import com.ly.supermvp.model.OnNetRequestListener;
import com.ly.supermvp.model.entity.ShowApiResponse;
import com.ly.supermvp.server.RetrofitService;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <Pre>
 *     图片大全数据实现类
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p/>
 *          Create by 2016/3/21 16:04
 */
public class PicturesModelImpl implements PicturesModel{
    @Override
    public void netLoadPictures(String type, int page, final OnNetRequestListener<List<PictureBody>> listener) {
        Observable<ShowApiResponse<ShowApiPictures>> observable = RetrofitService.getInstance().
                createBaiduAPI().getPictures(RetrofitService.getCacheControl(), type, page);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.onStart();
                    }
                })
                .subscribe(new Observer<ShowApiResponse<ShowApiPictures>>() {
                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure(e);
                        listener.onFinish();
                    }

                    @Override
                    public void onComplete() {
                        listener.onFinish();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ShowApiResponse<ShowApiPictures> showApiPicturesShowApiResponse) {
                        if (showApiPicturesShowApiResponse.showapi_res_body != null && TextUtils.equals("0", showApiPicturesShowApiResponse.showapi_res_code)) {
                            listener.onSuccess(showApiPicturesShowApiResponse.showapi_res_body.pagebean.contentlist);
                        } else {
                            listener.onFailure(new Exception());
                        }
                    }
                });
    }
}
