Ito
============

Field and method binding for creating static Intent and Fragment returning functions used in navigation.

 * Eliminate `public static Intent createActivityIntent(args) {...}` for getting Intents required to launch activity.
 * Eliminate `public static Bundle getInstance(args) {...}` for getting Fragment instance required to inflate fragment.

For example,
```java
class SecondActivity extends Activity {

  private static final String KEY_ARG_1 = "arg_1";
  private static final String KEY_ARG_2 = "arg_2";

  @IntentVar(KEY_ARG_1) String arg1;
  @IntentVar(KEY_ARG_2) int arg2;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.simple_activity);
    
    arg1 = getIntent().getStringExtra(KEY_ARG_1);
    arg2 = getIntent().getStringExtra(KEY_ARG_2, 0);
  }
}
```

```java
class SampleFragment extends Fragment {

  private static final String KEY_ARG_1 = "arg_1";
  private static final String KEY_ARG_2 = "arg_2";
  
  @BundleVar(KEY_ARG_1) String arg1;
  @BundleVar(KEY_ARG_2) int arg2;
  
  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strVal1 = getArguments().getString(KEY_ARG_1);
        intVal2 = getArguments().getInt(KEY_ARG_2);
    }
}
```
Now, rebuild the project for processing the annotations. Two functions will be generated as follows which can be used for easy navigation.

```java

Intent intent = Ito.createSecondActivityIntent(this, val1, val2));

SecondFragment intent = Ito.createSecondFragmentInstance(val1, val2);

```


Download
--------

```groovy

repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  compile 'com.github.surajsau:ito:0.1'
  annotationProcessor 'com.github.surajsau.ito:ito-processor:0.1'
}
```

License
-------

    Copyright 2017 Suraj Kumar Sau

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: http://square.github.com/dagger/
 [2]: https://search.maven.org/remote_content?g=com.jakewharton&a=butterknife&v=LATEST
 [3]: http://jakewharton.github.com/butterknife/
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
