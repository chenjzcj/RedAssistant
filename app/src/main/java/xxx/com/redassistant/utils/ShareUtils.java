package xxx.com.redassistant.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import xxx.com.redassistant.R;

/**
 * Created by MZIA(527633405@qq.com) on 2017/11/6 0006 18:02
 */
public class ShareUtils {
    public static void share(Context context) {
        //http://www.jianshu.com/p/0a0e2258b3d6
        //MyTextUtils.copy("https://fir.im/1ed9",this);
                /*Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, "https://fir.im/1ed9");
                startActivity(Intent.createChooser(textIntent, "分享"));*/
        BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.share_pic);
        assert drawable != null;
        Bitmap bitmap = drawable.getBitmap();
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/*");
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", ""));

        imageIntent.putExtra(Intent.EXTRA_TEXT, "https://fir.im/1ed9");
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(imageIntent, "分享"));
    }
}
