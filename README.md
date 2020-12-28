# SimpleConcurrency
[![License: Apache 2](https://img.shields.io/badge/License-Apache%202-blue.svg)](https://github.com/mardous/SimpleConcurrency/blob/master/LICENSE)
[![](https://jitpack.io/v/mardous/SimpleConcurrency.svg)](https://jitpack.io/#mardous/SimpleConcurrency)

# Table of contents

- [Deprecated](#deprecated)
- [Description](#description)
- [Download](#download)
  - [Using Bintray OSS](#using-bintray-oss)
  - [Using JitPack](#using-jitpack)
- [Implementation](#implementation)
- [License](#license)

## Deprecated
This library has been deprecated. You should use Kotlin Coroutines or RxJava, they are more complete solutions and the Kotlin alternative is certainly very amazing!

This repository will continue to be available.

## Description
An easy-to-use library for Android designed to be the perfect replacement for AsyncTask

## Download
Downloads for this library are no longer available.

## Implementation
Executing a simple task (with a [Runnable]):

```java
AsyncWorker.simpleTask(() -> longRunningTask()).execute();
```

You can also run tasks and get a result:

```java
AsyncWorker.forResult(new AsyncCallable<Something>() {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            // Do something useful here!
        }

        @Override
        public void onSuccess(Something result) {
            super.onSuccess(result);
            // Perfect! We got a result.
        }

        @Override
        public Something call() throws Exception {
            // The heavy work gets done here!
        }
    })
    .acceptsNull(false) // If you don't want a null result.
    .execute();
```

When it comes to strings, integers or other special cases, you can extend the API even further:

```java
AsyncWorker.forString(new AsyncCallable<String>() {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            // Do something useful here!
        }

        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            // Perfect! We got a result.
        }

        @Override
        public void onError(Exception error) {
            super.onError(error);
            // We got an error.
        }

        @Override
        public String call() throws Exception {
            // The heavy work gets done here!
        }
    })
    .acceptsMaximumLength(10) // The maximum length we want
    .acceptsEmpty(false) // If you don't want an empty string
    .acceptsNull(false) // If you don't want a null result.
    .execute();
```

You can attach your task to a lifecycle:

```java
AsyncWorker.simpleTask(() -> longRunningTask())
    .attachLifecycle(getLifecycle())
    .execute();
```

Also, you can cancel a task:

```java
// Supposing we have:
Task task = AsyncWorker.forResult(new AsyncCallable<Something>() {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            // Do something useful here!
        }

        @Override
        public void onSuccess(Something result) {
            super.onSuccess(result);
            // Perfect! We got a result.
        }

        @Override
        public Something call() throws Exception {
            // The heavy work gets done here!
        }
    })
    .acceptsNull(false) // If you don't want a null result.
    .execute();

// If you want to cancel the task, your just need to call:
task.cancel(false);
```

## License
```
Copyright 2020 Christians Martínez Alvarado

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[Runnable]:https://developer.android.com/reference/java/lang/Runnable.html