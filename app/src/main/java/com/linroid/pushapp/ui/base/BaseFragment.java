/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linroid.pushapp.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by linroid on 8/23/15.
 */
public class BaseFragment extends Fragment {
    protected CompositeSubscription subscriptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.subscriptions = new CompositeSubscription();
    }

    protected void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
    }
}
