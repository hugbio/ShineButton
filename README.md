# ShineButton
[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

在[ShineButton](https://github.com/ChadCSong/ShineButton)的基础上修改的。因为原来的ShineButton库直接导入现有项目中编译时会跟现有项目的编译SDK版本冲突，
而且原来的ShineButton库只能修改图标的形状不能直接使用一张图片当图标。所以只能自己修改和编译这个库了。修改的不多。不喜勿喷。嘿嘿！

![preview](https://github.com/hugbio/ShineButton-master/blob/master/demo_shine_others.gif)

除了上面的效果。也可以直接只用一张图片当图标。

## Usage
原来的ShineButton库的那些用法都不变。只是我扩展了一个ShineButton接口。所以将原来的ShineButton类的名字改为ShineShapeButton，
并增加类ShineImageButton用于支持直接只用一张图片当图标，增加两个属性：btn_img和btn_fill_Img，当然也就不支持btn_color、btn_fill_color、siShape属性了。
```java
 shineShapeButton = (ShineShapeButton) findViewById(R.id.shine_button);
 shineShapeButton.init(activity);
 shineImageButton = (ShineImageButton) findViewById(R.id.shine_img_button);
 shineImageButton.init(activity);
```
or

```java
 shineImageButton shineButtonJava = new shineImageButton(this);
 shineButtonJava.setImageResource(imgId,checkImgId);
 shineButtonJava.setAllowRandomColor(true);
 LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
 shineButtonJava.setLayoutParams(layoutParams);
 if (linearLayout != null) {
     linearLayout.addView(shineButtonJava);
 }
```

```xml
 <com.hugbio.shinebuttonlib.shineImageButton
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:layout_centerInParent="true"
                app:btn_img=""
                app:btn_fill_Img=""
                app:allow_random_color="false"/>
 ```
 
## Requirements

- Android 4.0+

## Code Reference
[ShineButton](https://github.com/ChadCSong/ShineButton)
[android-shape-imageview](https://github.com/siyamed/android-shape-imageview)
[EasingInterpolator](https://github.com/MasayukiSuda/EasingInterpolator)

##
