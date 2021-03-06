package com.mkch.youshi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ChartListAdapter;
import com.mkch.youshi.adapter.ExpressionGridAdapter;
import com.mkch.youshi.bean.GroupFriend;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.ChatBean;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.Group;
import com.mkch.youshi.model.MessageBox;
import com.mkch.youshi.receiver.ChatReceiver;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.view.Expression;
import com.mkch.youshi.view.RecordButton;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMFaceElem;
import com.tencent.TIMFileElem;
import com.tencent.TIMImageElem;
import com.tencent.TIMLocationElem;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    @ViewInject(R.id.iv_chat_topbar_back)
    private ImageView mIvBack;//返回按钮

    @ViewInject(R.id.tv_chat_topbar_title)
    private TextView mTvTitle;//标题

    @ViewInject(R.id.iv_chat_topbar_ps)
    private ImageView mIvPsInfo;//查看用户

    @ViewInject(R.id.iv_chat_go_voice)
    private ImageView mIvGoVoice;//去使用语音

    @ViewInject(R.id.line_keyboard_comptonts_use)
    private LinearLayout mLineKeybordBlock;//键盘输入框和表情选择

    @ViewInject(R.id.btn_chat_voice_use)
    private RecordButton mBtnUseVoice;

    @ViewInject(R.id.iv_chat_go_keyboard)
    private ImageView mIvGoKeyboard;//去使用文字

    @ViewInject(R.id.et_chat_input)
    private EditText mEtChatInput;//聊天框

    @ViewInject(R.id.iv_chat_go_expression)
    private ImageView mIvGoExpression;//表情

    @ViewInject(R.id.line_chat_expression)
    private LinearLayout mLineExpression;//表情面板

    @ViewInject(R.id.gv_chat_expression)
    private GridView mGvExpression;//表情视图

    @ViewInject(R.id.iv_chat_go_more_action)
    private ImageView mIvGoMoreAction;//更多操作

    @ViewInject(R.id.btn_chat_send)
    private Button mBtnChatSend;//发送

    @ViewInject(R.id.line_chat_more_action_block)
    private LinearLayout mLineMoreAction;//更多操作-面板弹出

    @ViewInject(R.id.gv_chat_more_action)
    private GridView mGvMoreAction;//更多操作-网格视图
    // 聊天
    @ViewInject(R.id.rv_chart_list)
    private RecyclerView mRvList;
    private DbManager dbManager;//数据库管理对象
    //数据
    private List<ChatBean> m_chart_list;
    private ChartListAdapter m_adapter;
    private String _openfirename, _groupID;
    private static ProgressDialog mProgressDialog = null;//加载
    private ChatReceiver mChatReceiver;
    private User mUser;
    private List<Map<String, Object>> _maps = new ArrayList<>();
    private SimpleAdapter _adapter;
    //播放语音
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //发送图片相关
    private File mFile;
    private Uri imageUri;
    private String mPicPath = Environment.getExternalStorageDirectory().getPath() + "/";
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int FILE_CODE = 4;//选择文件
    private String[] expressionMessages = new String[]{"[微笑]", "[抓狂]", "[大哭]", "[拜托]", "[鄙视]", "[委屈]", "[发呆]", "[晕]", "[傲慢]", "[色]", "[大笑]", "[偷笑]", "[可怜]", "[傻笑]", "[飞吻]", "[困]", "[难过]", "[咒骂]", "[亲亲]", "[流汗]", "[惊吓]", "[害羞]", "[快哭了]", "[流泪]", "[调皮]", "[闭嘴]", "[撇嘴]"};
    //定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private final int SDK_PERMISSION_REQUEST = 127;
    private final int CHOOSE_MEM_CODE = 100;
    private String permissionInfo;
    TIMConversation conversation;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int _what = msg.what;
            switch (_what) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(ChatActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case ChatReceiver.RECEIVE_CHAT_MSG:
                    Bundle b = msg.getData();
                    int _chat_id = b.getInt("chat_id");
                    int _member_id = b.getInt("member_id");//最新的一条消息-写在广播接收处
                    Log.d("zzz----RECEIVE_CHAT_MSG", String.valueOf(_chat_id));
                    //查询该消息内容并刷新到UI
                    try {
                        ChatBean _chat_bean = dbManager.findById(ChatBean.class, _chat_id);
                        GroupFriend _group_friend = dbManager.findById(GroupFriend.class, _member_id);
                        //更新UI界面，获取最新的用户列表
                        updateUIfromReceiver(_chat_bean, _group_friend);
                    } catch (DbException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CommonConstants.SEND_MSG_SUCCESS:
                    Log.d("zzz----SEND_MSG_SUCCESS", "");
                    m_adapter.notifyDataSetChanged();
                    mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private int mMessageBoxId;
    private Friend mFriend;
    private Group mGroup;
    private List<GroupFriend> mGroupFriends = new ArrayList<>();
    private MessageBox m_messageBox;
    private String friendId, selfId;

    /**
     * 更新UI界面，获取最新的聊天记录
     *
     * @param _chat_bean
     */
    private void updateUIfromReceiver(ChatBean _chat_bean, GroupFriend _group_friend) {
        m_chart_list.add(_chat_bean);
        mGroupFriends.add(_group_friend);
        m_adapter.notifyDataSetChanged();
        mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = DBHelper.getDbManager();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    private void updateData() {
        if (_groupID != null) {
            try {
                mGroup = dbManager.selector(Group.class).where("group_id", "=", _groupID).and("user_id", "=", selfId).findFirst();
                if (mGroup.getGroupName() != null && !mGroup.getGroupName().equals("")) {
                    mTvTitle.setText(mGroup.getGroupName());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    private void setListener() {
        //语音消息发送
        mBtnUseVoice.setOnRecordFinishedListener(new RecordButton.OnRecordFinishedListener() {
            @Override
            public void onFinished(File audioFile, int duration) {
                TIMMessage msg = new TIMMessage();
                //添加语音
                TIMSoundElem elem = new TIMSoundElem();
                final String soundFile = audioFile.getAbsolutePath();
                final int soundDuration = duration / 1000;
                elem.setPath(soundFile); //填写语音文件路径
                elem.setDuration(duration);  //填写语音时长
                //将elem添加到消息
                if (msg.addElement(elem) != 0) {
                    Log.d("zzz-------", "addSoundElement failed");
                    return;
                }
                //发送消息
                conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                    @Override
                    public void onError(int code, String desc) {//发送消息失败
                        Log.d("zzz---sendMessage sound", code + "Error:" + desc);
                    }

                    @Override
                    public void onSuccess(TIMMessage msg) {//发送消息成功
                        Log.d("zzz---sendMessage sound", "sendMessage is success");
                        ChatBean _local_message = new ChatBean(selfId, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_OUT, soundDuration, soundFile, "[语音]");
                        saveChatBean(_local_message);
                    }
                });
            }
        });
        //消息列表按类型的点击事件
        m_adapter.setOnItemClickListener(new ChartListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws IOException {
                if (m_chart_list.get(position).getMsgModel() == 2) {
                    if (mediaPlayer.isPlaying()) {
                        Log.d("zzz---------mediaPlayer", "mediaPlayer is Playing");
                        mediaPlayer.stop();
                    } else {
                        try {
                            Log.d("zzz---------mediaPlayer", "mediaPlayer is stop");
                            File audio = new File(m_chart_list.get(position).getFileName());
                            FileInputStream fis = new FileInputStream(audio);
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(fis.getFD());
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (m_chart_list.get(position).getMsgModel() == 3) {
                    if (m_chart_list.get(position).getFileOriginal() == null) {
                        Toast.makeText(ChatActivity.this, "正在下载图片...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(ChatActivity.this, ChatPicShowActivity.class);
                        intent.putExtra("image", m_chart_list.get(position).getFileOriginal());
                        startActivity(intent);
                    }
                } else if (m_chart_list.get(position).getMsgModel() == 4) {
                    if (m_chart_list.get(position).getFilePath() == null) {
                        Toast.makeText(ChatActivity.this, "正在下载文件...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "下载已完成", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //输入框的点击操作
        mEtChatInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当获取焦点时，只要有其他面板在打开状态，隐藏这些面板
                if (hasFocus) {
                    mLineMoreAction.setVisibility(View.GONE);
                    mLineExpression.setVisibility(View.GONE);
                    mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
                }
            }
        });
        //输入框文字变化的监听器
        mEtChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String _input_text = s.toString();
                if (_input_text != null && !_input_text.equals("")) {
                    mIvGoMoreAction.setVisibility(View.GONE);
                    mBtnChatSend.setVisibility(View.VISIBLE);
                } else {
                    mIvGoMoreAction.setVisibility(View.VISIBLE);
                    mBtnChatSend.setVisibility(View.GONE);
                }
            }
        });
        //更多操作的选择监听器
        mGvMoreAction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        mFile = new File(mPicPath, TimesUtils.getNow() + mUser.getOpenFireUserName() + ".jpg");
                        imageUri = Uri.fromFile(mFile);
                        intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                        break;
                    case 1:
                        mFile = new File(mPicPath, TimesUtils.getNow() + mUser.getOpenFireUserName() + ".jpg");
                        imageUri = Uri.fromFile(mFile);
                        intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                        break;
                    case 2:
                        mLocationClient.start();
                        break;
                    case 3:
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, FILE_CODE);
                        break;
                    default:
                        break;
                }
            }
        });
        //表情消息发送
        mGvExpression.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendExpressionMsg(position);
            }
        });
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(this);
        selfId = mUser.getOpenFireUserName();
        //获取会话
        Intent _intent = getIntent();
        if (_intent != null) {
            if (_intent.getStringExtra("chatType").equals("C2C")) {
                _openfirename = _intent.getStringExtra("_openfirename");
                conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, _openfirename);
                if (_openfirename != null && !_openfirename.equals("")) {
                    try {
                        mFriend = dbManager.selector(Friend.class)
                                .where("friendid", "=", _openfirename)
                                .and("status", "=", "1")
                                .findFirst();
                        //标题
                        if (mFriend.getNickname() == null || mFriend.getNickname().equals("")) {
                            mTvTitle.setText(mFriend.getPhone());
                        } else {
                            mTvTitle.setText(mFriend.getNickname());
                        }
                        mIvPsInfo.setImageResource(R.drawable.chat_ps);
                        friendId = mFriend.getFriendid() + "@" + "TIMConversationType.C2C";
                        queryChatsInfoFromDB(friendId, selfId);
                        //注册聊天监听的Receiver
                        mChatReceiver = new ChatReceiver(mHandler);
                        IntentFilter filter = new IntentFilter("yoshi.action.chatsbroadcast");
                        registerReceiver(mChatReceiver, filter);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            } else if (_intent.getStringExtra("chatType").equals("Group")) {
                _groupID = _intent.getStringExtra("groupID");
//                _openfirename = _intent.getStringExtra("_openfirename");
                conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, _groupID);
                if (_groupID != null && !_groupID.equals("")) {
                    try {
                        mGroup = dbManager.selector(Group.class).where("group_id", "=", _groupID).and("user_id", "=", selfId).findFirst();
                        if (mGroup.getGroupName() != null && !mGroup.getGroupName().equals("")) {
                            mTvTitle.setText(mGroup.getGroupName());
                        }
                        mIvPsInfo.setImageResource(R.drawable.downmenu_group);
                        friendId = _groupID + "@" + "TIMConversationType.Group";
                        queryChatsInfoFromDB(friendId, selfId);
                        //注册聊天监听的Receiver
                        mChatReceiver = new ChatReceiver(mHandler);
                        IntentFilter filter = new IntentFilter("yoshi.action.chatsbroadcast");
                        registerReceiver(mChatReceiver, filter);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //gridview网格视图-更多动作
        //数据源
        int[] _pic_reses = new int[]{R.drawable.chat_photo,
                R.drawable.chat_shot,
                R.drawable.chat_location,
                R.drawable.chat_file};
        String[] _str_names = new String[]{"照片", "拍摄", "位置", "文件"};
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> _map1 = new HashMap<>();
            _map1.put("pic_res", _pic_reses[i]);
            _map1.put("str_name", _str_names[i]);
            _maps.add(_map1);
        }
        //适配器
        _adapter = new SimpleAdapter(this, _maps, R.layout.gv_item_chat_more_action, new String[]{"pic_res", "str_name"}, new int[]{R.id.iv_item_chat_more_action, R.id.tv_item_chat_more_action});
        //设置适配器
        mGvMoreAction.setAdapter(_adapter);
        //表情视图
        ExpressionGridAdapter expressionGridAdapter = new ExpressionGridAdapter(this, Expression.expressions);
        mGvExpression.setAdapter(expressionGridAdapter);
        //定位初始化
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                if (location.getProvince() == null || location.getProvince().equals("") || location.getProvince().equals("null")) {
                    sb = null;
                    UIUtils.showTip("定位失败，请点击重新获取");
                } else {
                    sb.append("当前位置：" + location.getAddrStr());
                    sb.append(",------" + location.getLocationDescribe());
                    Log.d("zzz-------address", sb.toString());
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("服务端网络定位失败");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("无法获取有效定位依据，请重启手机");
                }
                if (sb != null) {
                    sendAddressMsg(sb.toString());
                }
            }
        }
    }

    /**
     * 处理图片的剪辑
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                startPhotoZoom(imageUri);
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null)
                    startPhotoZoom(data.getData());
                break;
            case PHOTO_REQUEST_CUT:// 返回的结果
                try {
                    if (resultCode == 0) {
                    } else {
                        sentPicToNext();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case FILE_CODE://选择文件后
                if (resultCode == RESULT_OK) {
                    sendFile(data.getData().getPath());
                }
                break;
            case CHOOSE_MEM_CODE://删除群聊
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //剪裁图片
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 720);
        intent.putExtra("outputY", 1080);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//將剪切的文件输入到imageUri中
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片传递到下一个界面上
    private void sentPicToNext() throws FileNotFoundException {
        Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
        saveBitmap(photo);  //保存BitMap到本地
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存bitmap为File文件
     */
    public void saveBitmap(Bitmap bm) {
        File f = new File(mPicPath, TimesUtils.getNow() + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            sendPicMsg(f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库查询聊天信息
     *
     * @param
     */
    private void queryChatsInfoFromDB(final String friendId, final String selfId) {
        //聊天内容列表
        //布局管理器
        LinearLayoutManager _layout_manager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(_layout_manager);
        //初始化list聊天数据
        try {
            //查询
            dbManager = DBHelper.getDbManager();
            m_messageBox = dbManager.selector(MessageBox.class)
                    .where("friend_id", "=", friendId)
                    .and("self_id", "=", selfId)
                    .findFirst();
            //若消息盒子存在，则获取消息盒子的ID以及该消息盒子的所有消息
            if (m_messageBox != null) {
                mMessageBoxId = m_messageBox.getId();
                m_chart_list = m_messageBox.getChatBeans(dbManager);
                //判断该消息盒子是否属于群聊，是则插入消息的所发群成员
                if (m_messageBox.getType() == 2) {
                    for (int i = 0; i < m_chart_list.size(); i++) {
                        String memberId = m_chart_list.get(i).getUsername();
                        GroupFriend _groupFriend = dbManager.selector(GroupFriend.class).where("group_id", "=", _groupID).and("member_identifier", "=", memberId).findFirst();
                        if (_groupFriend != null) {
                            mGroupFriends.add(_groupFriend);
                        }
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (m_chart_list == null) {
            m_chart_list = new ArrayList<>();
        }
        //适配器
        if (mFriend != null) {
            m_adapter = new ChartListAdapter(m_chart_list, mFriend, mUser, this);
            mRvList.setAdapter(m_adapter);
            mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
        } else {
            m_adapter = new ChartListAdapter(m_chart_list, mGroupFriends, mUser, this);
            mRvList.setAdapter(m_adapter);
            mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
        }
    }

    @Event({R.id.iv_chat_topbar_back, R.id.iv_chat_topbar_ps, R.id.iv_chat_go_voice, R.id.iv_chat_go_keyboard, R.id.iv_chat_go_expression, R.id.iv_chat_go_more_action
            , R.id.et_chat_input, R.id.btn_chat_send})
    private void clickOneButton(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_send://发送
                sendInfoToFriend();
                break;
            case R.id.iv_chat_topbar_back://返回按钮
                this.finish();
                break;
            case R.id.iv_chat_topbar_ps://查看用户
                if (_groupID != null) {
                    Intent intent = new Intent(ChatActivity.this, GroupDetailActivity.class);
                    intent.putExtra("groupID", _groupID);
                    startActivityForResult(intent, CHOOSE_MEM_CODE);
                }
                break;
            case R.id.iv_chat_go_voice://切换到语音
                changeKeyboardAndVoice();
                //隐藏发送按钮和显示更多
                mIvGoMoreAction.setVisibility(View.VISIBLE);
                mBtnChatSend.setVisibility(View.GONE);
                break;
            case R.id.iv_chat_go_keyboard://切换到键盘
                changeKeyboardAndVoice();
                //判断是否有文字在输入框
                String _input_text_content = mEtChatInput.getText().toString();
                if (_input_text_content != null && !_input_text_content.equals("")) {
                    //隐藏更多和显示发送按钮
                    mIvGoMoreAction.setVisibility(View.GONE);
                    mBtnChatSend.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_chat_input://点击输入框
                //隐藏面板
                mLineMoreAction.setVisibility(View.GONE);
                mLineExpression.setVisibility(View.GONE);
                mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
                break;
            case R.id.iv_chat_go_expression://弹出表情
                if (mLineExpression.getVisibility() == View.VISIBLE) {
                    mLineExpression.setVisibility(View.GONE);
                } else {
                    CommonUtil.hideInput(this, mEtChatInput);//隐藏输入法
                    mLineMoreAction.setVisibility(View.GONE);
                    mLineExpression.setVisibility(View.VISIBLE);
                }
                if (m_chart_list != null) {
                    mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
                }
                break;
            case R.id.iv_chat_go_more_action://切换弹出更多操作
                if (mLineMoreAction.getVisibility() == View.VISIBLE) {
                    mLineMoreAction.setVisibility(View.GONE);
                } else {
                    CommonUtil.hideInput(this, mEtChatInput);//隐藏输入法
                    mLineExpression.setVisibility(View.GONE);
                    mLineMoreAction.setVisibility(View.VISIBLE);
                }
                if (m_chart_list != null) {
                    mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
                }
                break;
        }
    }

    /**
     * 发送信息给好友
     */
    private void sendInfoToFriend() {
        final String _msg = mEtChatInput.getText().toString();
        if (TextUtils.isEmpty(_msg)) {
            Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_LONG).show();
            mEtChatInput.setText("");
            return;
        }
        //发出去后立马清空edittext
        mEtChatInput.setText("");
        //开启副线程-发送消息
        sendTextMsg(_msg);
    }

    //发送文本信息
    private void sendTextMsg(final String _msg) {
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(_msg);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d("zzz-------", "addTextElement failed");
            return;
        }
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz----sendMessage text", i + "Error:" + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Log.d("zzz----sendMessage text", "sendMessage is success");
                if (mFriend != null) {
                    ChatBean _local_message = new ChatBean(selfId, _msg, ChatBean.MESSAGE_TYPE_OUT, TimesUtils.getNow());
                    saveChatBean(_local_message);
                } else if (mGroup != null) {
                    ChatBean _local_message = new ChatBean(mGroup.getGroupID(), selfId, _msg, ChatBean.MESSAGE_TYPE_OUT, TimesUtils.getNow());
                    saveChatBean(_local_message);
                }
            }
        });
    }

    //发送图片信息
    private void sendPicMsg(final String path) {
        TIMMessage msg = new TIMMessage();
        //添加图片
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(path);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d("zzz-------", "addImageElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {
                Log.d("zzz---sendMessage image", code + "Error:" + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {
                Log.d("zzz---sendMessage image", "sendMessage is success");
                if (mFriend != null) {
                    ChatBean _local_message = new ChatBean(selfId, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_OUT, path, "[图片]");
                    _local_message.setFileOriginal(path);
                    saveChatBean(_local_message);
                } else if (mGroup != null) {
                    ChatBean _local_message = new ChatBean(mGroup.getGroupID(), selfId, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_OUT, path, "[图片]");
                    _local_message.setFileOriginal(path);
                    saveChatBean(_local_message);
                }
            }
        });
    }

    //发送文件信息
    private void sendFile(final String path) {
        if (path == null) return;
        File file = new File(path);
        if (file.exists()) {
            if (file.length() > 1024 * 1024 * 10) {
                Toast.makeText(this, "文件过大，发送失败！", Toast.LENGTH_SHORT).show();
            } else {
                TIMMessage msg = new TIMMessage();
                //添加文件内容
                TIMFileElem elem = new TIMFileElem();
                elem.setPath(path); //设置文件路径
                final String fileName = path.substring(path.lastIndexOf("/") + 1);
                elem.setFileName(fileName); //设置消息展示用的文件名称
                //将elem添加到消息
                if (msg.addElement(elem) != 0) {
                    Log.d("zzz-------", "addFileElem failed");
                    return;
                }
                //发送消息
                conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                    @Override
                    public void onError(int code, String desc) {
                        Log.d("zzz---sendMessage file", code + "Error:" + desc);
                    }

                    @Override
                    public void onSuccess(TIMMessage msg) {//发送消息成功
                        Log.d("zzz---sendMessage file", "sendMessage is success");
                        ChatBean _local_message = new ChatBean(selfId, TimesUtils.getNow(), path, fileName, "[文件]", ChatBean.MESSAGE_TYPE_OUT);
                        saveChatBean(_local_message);
                    }
                });
            }
        } else {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    //发送表情信息
    private void sendExpressionMsg(final int position) {
        TIMMessage msg = new TIMMessage();
        //添加表情index
        TIMFaceElem elem = new TIMFaceElem();
        elem.setIndex(position);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d("zzz-------", "addFaceElement failed");
            return;
        }
        //发送消息
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {
                Log.d("zzz---sendMessage Face", code + "Error:" + desc);
            }

            @Override
            public void onSuccess(TIMMessage msg) {
                Log.d("zzz---sendMessage Face", "sendMessage is success");
                ChatBean _local_message = new ChatBean(selfId, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_OUT, position, expressionMessages[position]);
                saveChatBean(_local_message);
            }
        });
    }

    //发送位置信息
    private void sendAddressMsg(final String _msg) {
        mLocationClient.stop();
        TIMMessage msg = new TIMMessage();
        //添加位置信息
        TIMLocationElem elem = new TIMLocationElem();
        elem.setDesc(_msg);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d("zzz-------", "addLocationElement failed");
            return;
        }
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zz-sendMessage Location", i + "Error:" + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Log.d("zz-sendMessage Location", "sendMessage is success");
                ChatBean _local_message = new ChatBean(selfId, _msg, ChatBean.MESSAGE_TYPE_OUT, TimesUtils.getNow());
                saveChatBean(_local_message);
            }
        });
    }

    //保存已发送信息
    private void saveChatBean(final ChatBean _local_message) {
        try {
            m_chart_list.add(_local_message);
            if (mMessageBoxId == 0) {
                //新增该消息盒子
                if (mFriend != null) {
                    m_messageBox = new MessageBox(mFriend.getHead_pic(), mFriend.getNickname(), _local_message.getContent(), 0, TimesUtils.getNow(), 0, MessageBox.MB_TYPE_CHAT, friendId, selfId);
                } else {
                    m_messageBox = new MessageBox(mGroup.getGroupHead(), mGroup.getGroupName(), _local_message.getContent(), 0, TimesUtils.getNow(), 0, MessageBox.MB_TYPE_MUL_CHAT, friendId, selfId);
                }
                dbManager.saveBindingId(m_messageBox);
                mMessageBoxId = m_messageBox.getId();
            } else {
                //更新消息盒子
                if (mFriend != null) {
                    m_messageBox.setBoxLogo(mFriend.getHead_pic());
                    m_messageBox.setTitle(mFriend.getNickname());
                } else {
                    m_messageBox.setBoxLogo(mGroup.getGroupHead());
                    m_messageBox.setTitle(mGroup.getGroupName());
                }
                m_messageBox.setInfo(_local_message.getContent());
                m_messageBox.setLasttime(TimesUtils.getNow());
                dbManager.saveOrUpdate(m_messageBox);
            }
            _local_message.setMsgboxid(mMessageBoxId);//設置消息盒子id
            dbManager.save(_local_message);//保存一条消息到数据库
            mHandler.sendEmptyMessage(CommonConstants.SEND_MSG_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换键盘和语音
     */
    private void changeKeyboardAndVoice() {
        if (mLineKeybordBlock.getVisibility() == View.VISIBLE) {
            //点击了语音
            CommonUtil.hideInput(this, mEtChatInput);//隐藏输入法
            mIvGoVoice.setVisibility(View.GONE);
            mIvGoKeyboard.setVisibility(View.VISIBLE);
            mLineKeybordBlock.setVisibility(View.GONE);
            mBtnUseVoice.setVisibility(View.VISIBLE);
        } else {
            //点击了键盘
            mIvGoVoice.setVisibility(View.VISIBLE);
            mIvGoKeyboard.setVisibility(View.GONE);
            mLineKeybordBlock.setVisibility(View.VISIBLE);
            mBtnUseVoice.setVisibility(View.GONE);
        }
        //隐藏面板
        mLineMoreAction.setVisibility(View.GONE);
        mLineExpression.setVisibility(View.GONE);
    }
}
