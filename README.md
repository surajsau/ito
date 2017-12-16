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
MIT License

Copyright (c) [2017] [Suraj Kumar Sau]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
