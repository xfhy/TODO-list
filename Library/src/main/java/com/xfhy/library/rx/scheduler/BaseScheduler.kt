package com.xfhy.library.rx.scheduler

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * @author xfhy
 * time create at 2018/1/27 19:26
 * description RxJava2.x 5种基础相应类型
 */
abstract class BaseScheduler<T> protected constructor(private val subscribeOnScheduler: Scheduler,
                                                      private val observeOnScheduler: Scheduler)
    : ObservableTransformer<T, T>, SingleTransformer<T, T>,
        MaybeTransformer<T, T>, CompletableTransformer, FlowableTransformer<T, T> {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }
}