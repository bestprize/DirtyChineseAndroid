package com.fengxingshifang.dirtychineseandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fengxingshifang.dirtychineseandroid.db.InfoDao;
import com.fengxingshifang.dirtychineseandroid.domain.Info;
import com.fengxingshifang.dirtychineseandroid.domain.InfoListData;
import com.fengxingshifang.dirtychineseandroid.utils.CommonUtil;
import com.fengxingshifang.dirtychineseandroid.utils.ImageUtils;
import com.fengxingshifang.dirtychineseandroid.utils.SDCardUtil;
import com.fengxingshifang.dirtychineseandroid.utils.ScreenUtils;
import com.sendtion.xrichtext.RichTextEditor;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by git on 2017/12/7.
 */

public class PublishcommentActivity extends AppCompatActivity {

//    private EditText et_comment_new_title;
    private RichTextEditor et_comment_new_content;
    private static final int cutTitleLength = 20;//截取的标题长度
    private int flag;//区分是新建笔记还是编辑笔记
    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private Subscription subsLoading;
    private Subscription subsInsert;
    private InfoDao infoDao;
    private InfoListData.Info fatherinfo;
    private InfoListData.Info info;
    private long fatherInfoid;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_publishcomment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_comment_new);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_dialog_info);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_comment_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealwithExit();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        fatherinfo = (InfoListData.Info) bundle.getSerializable("info");
        fatherInfoid = fatherinfo.getInfoid();

//        et_comment_new_title = (EditText) findViewById(R.id.et_comment_new_title);
        et_comment_new_content = (RichTextEditor) findViewById(R.id.et_comment_new_content);

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("图片解析中...");
        loadingDialog.setCanceledOnTouchOutside(false);

        info = new InfoListData.Info();
        infoDao = new InfoDao(this);


    }




    @Override
    protected void onResume() {
        super.onResume();

//        refreshNoteList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert_image:
                callGallery();
                break;
            case R.id.action_new_save:
                saveNoteData(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     */
    private void saveNoteData(boolean isBackground) {
//        String title = et_comment_new_title.getText().toString();
//        Log.i("","title---------------------------------------------------:"+title);
        String content = getEditData();

//        if (title.length() == 0 ){//如果标题为空，则截取内容为标题
//            if (content.length() > cutTitleLength){
//                title = content.substring(0,cutTitleLength);
//            } else if (content.length() > 0 && content.length() <= cutTitleLength){
//                title = content;
//            }
//        }
//        info.setTitle(title);
        info.setContent(content);
//        flag = 0;
//        if (flag == 0 ) {//新建笔记
//            if (title.length() == 0 && content.length() == 0) {
//                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
//            } else {
                long infoId = infoDao.insertInfo(info);
        //TODO
//                    //Log.i("", "noteId: "+noteId);
//                    //查询新建笔记id，防止重复插入
//                    note.setId((int) noteId);
//                    flag = 1;//插入以后只能是编辑
//                    if (!isBackground){
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//            }
//        }else if (flag == 1) {//编辑笔记
//                if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                        || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                    noteDao.updateNote(note);
//                }
//                    finish();if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                        || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                    noteDao.updateNote(note);
//                }
//                    finish();
//        }
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private String getEditData() {
        List<RichTextEditor.EditData> editList = et_comment_new_content.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
                //Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
                //Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);
                //imageList.add(itemData.imagePath);
            }
        }
        return content.toString();
    }

    /**
     * 调用图库选择
     */
    private void callGallery(){
//        //调用系统图库
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 相片类型
//        startActivityForResult(intent, 1);

        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == PhotoPicker.REQUEST_CODE){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    /**
     * 异步方式插入图片
     * @param data
     */
    private void insertImagesSync(final Intent data){
        insertDialog.show();

        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    et_comment_new_content.measure(0, 0);
                    int width = ScreenUtils.getScreenWidth(PublishcommentActivity.this);
                    int height = ScreenUtils.getScreenHeight(PublishcommentActivity.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        //Log.i("NewActivity", "###path=" + imagePath);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
                        //bitmap = BitmapFactory.decodeFile(imagePath);
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        //Log.i("NewActivity", "###imagePath="+imagePath);
                        subscriber.onNext(imagePath);
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        insertDialog.dismiss();
                        et_comment_new_content.addEditTextAtIndex(et_comment_new_content.getLastIndex(), " ");
                        showToast("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        insertDialog.dismiss();
                        showToast("图片插入失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        et_comment_new_content.insertImage(imagePath, et_comment_new_content.getMeasuredWidth());
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //如果APP处于后台，或者手机锁屏，则启用密码锁
        if (CommonUtil.isAppOnBackground(getApplicationContext()) ||
                CommonUtil.isLockScreeen(getApplicationContext())){
            saveNoteData(true);//处于后台时保存数据
        }
    }

    /**
     * 退出处理
     */
    private void dealwithExit(){
//        String noteTitle = et_new_title.getText().toString();
//        String noteContent = getEditData();
//        String groupName = tv_new_group.getText().toString();
//        String noteTime = tv_new_time.getText().toString();
//        if (flag == 0) {//新建笔记
//            if (noteTitle.length() > 0 || noteContent.length() > 0) {
//                saveNoteData(false);
//            }
//        }else if (flag == 1) {//编辑笔记
//            if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                    || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)) {
//                saveNoteData(false);
//            }
//        }
        finish();
    }

    @Override
    public void onBackPressed() {
        dealwithExit();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


}
