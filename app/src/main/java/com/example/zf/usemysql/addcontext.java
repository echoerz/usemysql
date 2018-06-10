package com.example.zf.usemysql;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zf.usemysql.login.BaseActivity;
import com.example.zf.usemysql.tools.DBUtils;

import java.io.ByteArrayOutputStream;

public class addcontext extends BaseActivity {
    private ImageView picture;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO = 2;
    private static final String TAG = "addcontext";
    public byte[] picadd;
    private SharedPreferences pref2;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
           // ((TextView)findViewById(R.id.tv_result)).setText((String)message.obj);
            String str = "添加失败";
            if(message.what == 1) str = "添加成功";
            Toast.makeText(addcontext.this, str, Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontext);

        picture =(ImageView)findViewById(R.id.picture);
        final EditText add_title = (EditText)findViewById(R.id.add_title);
        final EditText add_context = (EditText)findViewById(R.id.add_data);
        pref2 = PreferenceManager.getDefaultSharedPreferences(this);
        final String user = pref2.getString("user","");
        Button add_create = (Button)findViewById(R.id.add_create);
        add_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = add_title.getText().toString().trim();
                final String context = add_context.getText().toString().trim();
                Log.e(TAG,title);
                Log.e(TAG,context);
                if(title == null || title.equals("") ||context == null || context.equals("")) {
                    Toast.makeText(addcontext.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DBUtils.AddData(title,context,user);
                            Message msg = new Message();
                          //  msg.what = 0;
                         //   msg.obj =  "查询失败，请检查网络是否连接成功";
                            msg.what = 1;
                                //非UI线程不要试着去操作界面
                            handler.sendMessage(msg);
                        }
                    }).start();
                    add_title.setText("");
                    add_context.setText("");
                }
            }
        });

        Button chooseFromAlbum = (Button)findViewById(R.id.choose_from_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(addcontext.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager
                        .PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(addcontext.this,new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"你好像没有打开相应权限",
                            Toast.LENGTH_LONG).show();
                }break;
                default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    handleImageOnKitKat(data);
                }else {
                    handleImageBeforeKiKat(data);
                }break;
                default:break;
        }
    }

    private void handleImageBeforeKiKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];  // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.document".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public_downloads"
                ),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath!=null){
            Bitmap mypic = BitmapFactory.decodeFile(imagePath);
            picadd = convertIconToString(mypic);
            //  org.hibernate.Hibernate.Hibernate.createBlob(new byte[1024]);
            Bitmap a = convertStringToIcon(picadd);
            picture.setImageBitmap(a);
        }else {
            Toast.makeText(this,"主人我无法偷到照片嘤嘤嘤",Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取真实图片的路径
        Cursor cursor = getContentResolver().query(uri,
                null,selection,null,null);
        if (cursor !=null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }





    //图片处理

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static byte[] convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();// 转为byte数组
        return bytes;
    }

    /**
     * string转成bitmap
     *
     * @param bytes
     */
    public static Bitmap convertStringToIcon(byte[] bytes) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
