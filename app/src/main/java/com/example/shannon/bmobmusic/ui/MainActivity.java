package com.example.shannon.bmobmusic.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.example.shannon.bmobmusic.MyApplication;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.Account;
import com.example.shannon.bmobmusic.bean.PlayState;
import com.example.shannon.bmobmusic.fragment.FriendsFragment;
import com.example.shannon.bmobmusic.fragment.HomeFragment;
import com.example.shannon.bmobmusic.fragment.NetMusicFragment;
import com.example.shannon.bmobmusic.receiver.MyReceiver;
import com.example.shannon.bmobmusic.service.PlayService;
import com.example.shannon.bmobmusic.utils.MediaUtils;
import com.example.shannon.bmobmusic.utils.SpUtils;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.getbase.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class MainActivity extends BaseActivity{

    public static final int REQUEST_LOGIN = 0x0;
    private FloatingActionButton am_fab_next,am_fab_play,am_fab_pre;
    private FloatingActionsMenu am_fam_container;
    private Toolbar tl_custom;
    private DrawerLayout am_dl;
    private ViewPager am_vp_container;
    private ArrayList<Fragment> al_fragments = new ArrayList<>();
    private TableLayout vs_tl_container;
    private TextView vs_tv_home,vs_tv_net_music,vs_tv_friends,vn_tv_name,vn_text,vn_tv_login;
    private ImageView vs_iv_more,va_user_img;
    private PlayService ps;
    private NavigationView am_nv;
    private View view_nav;
    private LinearLayout vn_ll_info;
    private Account account;
    private AnimationDrawable ad;
    public PlayService getPs() {
        return ps;
    }
    public TableLayout getVs_tl_container() {
        return vs_tl_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("MainActivity创建");
        initView();
        setListener();
        loadData();


        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        System.out.println("下载缓存的路径：" +path);
    }


    @Override
    public void setListener() {
        vs_iv_more.setOnClickListener(this);
        va_user_img.setOnClickListener(this);
        vn_tv_login.setOnClickListener(this);
        vs_tv_home.setOnClickListener(this);
        vs_tv_net_music.setOnClickListener(this);
        vs_tv_friends.setOnClickListener(this);


        //action
        am_fab_next.setOnClickListener(this);
        am_fab_play.setOnClickListener(this);
        am_fab_pre.setOnClickListener(this);

    }

    @Override
    public void loadData() {


        setToolbar();


        setAm_vp_container();

        //设置fab的播放按钮
        am_fab_play.setTag(false);


        //绑定服务
        bindService(new Intent(this, PlayService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println("绑定播放服务成功'");
                PlayService.PlayBinder pb = (PlayService.PlayBinder)service;
                ps = pb.getPlayService();


            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                System.out.println("绑定播放服务失败'");

            }
        }, Context.BIND_AUTO_CREATE);


        //设置最右边的那个按钮
        vs_iv_more.setImageResource(R.drawable.anim_lay);
        ad = (AnimationDrawable) vs_iv_more.getDrawable();

        //用户信息初始化
        String username = SpUtils.getStringContent(this , Account.SP_USERNAME);
        String password = SpUtils.getStringContent(this , Account.SP_PASSWORD);

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){

            //System.out.println("sp中没有缓存用户");
            MyApplication.isComfirm = false;


        }else{

            account = MyApplication.account;
            if(account == null){


                account = Account.getAccountFromLoal();

                if(account == null){

                    //本地获取失败，联网验证
                    account = new Account.Builder().build();
                    account.setUsername(username);
                    account.setPassword(password);
                    BmobUser.loginByAccount(this, username, password, new LogInListener<Account>() {

                        @Override
                        public void done(Account account, BmobException e) {

                            if(account!=null){

                                MyApplication.account = account;
                                //System.out.println("账户验证成功");
                                MyApplication.isComfirm = true;
                                setNvUser(account);

                                Account.saveAccountToLocal(account);
                                //加载用户的搜藏列表
                                PlayService.al_music_collection = MediaUtils.getUserCollection(MainActivity.this , MyApplication.account.getUsername());



                            }

                        }

                    });


                }else{

                    setNvUser(account);

                }



            }else{

                setNvUser(account);


            }



        }



        //注册广播
        //动态注册广播
        MyReceiver mr = new MyReceiver() {
            @Override
            public void doAction(Context context, Intent intent) {
                //System.out.println("PlayActivity接收到广播");
                updateUI();

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.shannon.bmplayer.progress");
        registerReceiver(mr , intentFilter);



    }

    private void updateUI() {

        PlayState ps = PlayService.playState;
        boolean isPlaying = (boolean) am_fab_play.getTag();
        if(ps!=null && ps.isPlaying()){

            ad.start();

            if(isPlaying == false){

                am_fab_play.setTag(true);
                am_fab_play.setIconDrawable(getResources().getDrawable(R.mipmap.desklrc_btn_pause));


            }

        }else{

            ad.stop();


            if(isPlaying == true){

                am_fab_play.setTag(false);
                am_fab_play.setIconDrawable(getResources().getDrawable(R.mipmap.desklrc_btn_play));

            }
        }



    }



    //根据当前的用户控制显示的界面
    private void setNvUser(Account account){



        if(account!=null){
            vn_tv_login.setVisibility(View.INVISIBLE);
            vn_ll_info.setVisibility(View.VISIBLE);
            String name = account.getName();
            String title = account.getTitle();
            String url_img = account.getUrl_img();

            //设置用户名
            if(!TextUtils.isEmpty(name)){

                vn_tv_name.setText(name);
            }

            //设置签名
            if(!TextUtils.isEmpty(title)){

                vn_text.setText(title);

            }


            //设置头像
            if(account.getUrl_img()!=null){

                ImageLoader.getInstance().displayImage(url_img,va_user_img);

            }

        }else{

            //当前为空则显示登录的界面
            vn_tv_login.setVisibility(View.VISIBLE);
            vn_ll_info.setVisibility(View.INVISIBLE);


        }

    }


    private void setAm_vp_container(){


        //添加fragment到集合
        al_fragments.add(new HomeFragment());
        al_fragments.add(new NetMusicFragment());
        al_fragments.add(new FriendsFragment());

        am_vp_container.setOffscreenPageLimit(2);
        am_vp_container.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        am_vp_container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                changeSelect(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void changeSelect(int postition){

        if(postition>=0 && postition <=2){


            switch (postition){

                case 0:

                    vs_tv_home.setTextColor(getResources().getColor(R.color.colorAccent));
                    vs_tv_net_music.setTextColor(getResources().getColor(R.color.font_black));
                    vs_tv_friends.setTextColor(getResources().getColor(R.color.font_black));
                    break;

                case 1:

                    vs_tv_home.setTextColor(getResources().getColor(R.color.font_black));
                    vs_tv_net_music.setTextColor(getResources().getColor(R.color.colorAccent));
                    vs_tv_friends.setTextColor(getResources().getColor(R.color.font_black));
                    break;


                case 2:
                    vs_tv_home.setTextColor(getResources().getColor(R.color.font_black));
                    vs_tv_net_music.setTextColor(getResources().getColor(R.color.font_black));
                    vs_tv_friends.setTextColor(getResources().getColor(R.color.colorAccent));

                    FriendsFragment ff = (FriendsFragment) al_fragments.get(2);

                    break;



            }


        }



    }

    private void setToolbar(){

        setSupportActionBar(tl_custom);
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle abdt = new ActionBarDrawerToggle(this,am_dl,tl_custom,R.string.app_name,R.string.app_name);
        abdt.syncState();
        am_dl.setDrawerListener(abdt);

    }

    @Override
    public void initView() {

        //actionbar
        am_fam_container = (FloatingActionsMenu) findViewById(R.id.am_fam_container);
        am_fab_next = (FloatingActionButton) findViewById(R.id.am_fab_next);
        am_fab_play = (FloatingActionButton) findViewById(R.id.am_fab_play);
        am_fab_pre = (FloatingActionButton) findViewById(R.id.am_fab_pre);

        tl_custom = (Toolbar) findViewById(R.id.tl_custom);
        tl_custom.setTitleTextColor(getResources().getColor(R.color.font_white));
        am_dl = (DrawerLayout) findViewById(R.id.am_dl);
        am_vp_container = (ViewPager) findViewById(R.id.am_vp_container);
        vs_tl_container = (TableLayout) findViewById(R.id.vs_tl_container);

        vs_tv_home = (TextView) findViewById(R.id.vs_tv_home);
        vs_tv_net_music = (TextView) findViewById(R.id.vs_tv_net_music);
        vs_tv_friends = (TextView) findViewById(R.id.vs_tv_friends);
        vs_iv_more = (ImageView) findViewById(R.id.vs_iv_more);

        setVsTvMorePop();

        changeSelect(0);




        am_nv = (NavigationView) findViewById(R.id.am_nv);
        view_nav = getLayoutInflater().inflate(R.layout.view_nav,am_nv);
        va_user_img = (ImageView) view_nav.findViewById(R.id.va_user_img);
        vn_tv_login = (TextView) view_nav.findViewById(R.id.vn_tv_login);
        vn_ll_info = (LinearLayout) view_nav.findViewById(R.id.vn_ll_info);

        //System.out.println("vn_tv_login是否为空："  + vn_tv_login);
        vn_tv_name = (TextView) am_nv.findViewById(R.id.vn_tv_name);
        vn_text = (TextView) am_nv.findViewById(R.id.vn_text);



        //nv中菜单的点击事件
        am_nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch(item.getItemId()){

                    case R.id.mn_message:
                        showToast("无消息");
                        break;

                    case R.id.mn_download:
                        startActivity(new Intent(MainActivity.this , TransferActivity.class));
                        break;

                    case R.id.mn_about:
                        startActivity(new Intent(MainActivity.this , AboutActivity.class));
                        break;

                    case R.id.mn_feedback:
                        startActivity(new Intent(MainActivity.this , FeedbackActivity.class));
                        break;

                    case R.id.mn_back:
                        finish();
                        break;

                    case R.id.mn_logout:
                        //注销操作
                        MyApplication.account = null;
                        MyApplication.isComfirm = false;
                        setNvUser(null);
                        SpUtils.saveStringContent(MainActivity.this,Account.SP_USERNAME,null);
                        SpUtils.saveStringContent(MainActivity.this,Account.SP_PASSWORD,null);
                        break;


                }

                return false;
            }
        });



    }

    private void setVsTvMorePop() {




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem mi = menu.findItem(R.id.mt_it_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(mi);
        MenuItemCompat.setOnActionExpandListener(mi, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.vs_iv_more:

                startActivity(new Intent(this , PlayActivity.class));
                break;

            case R.id.va_user_img:
                //startActivity(new Intent(this , UserActivity.class));
                break;

            case R.id.vn_tv_login:

                System.out.println("点击登录");
                startActivityForResult(new Intent(this , LoginActivity.class),REQUEST_LOGIN);
                break;


            case R.id.vs_tv_home:
                am_vp_container.setCurrentItem(0);
                break;

            case R.id.vs_tv_net_music:
                am_vp_container.setCurrentItem(1);

                break;

            case R.id.vs_tv_friends:
                am_vp_container.setCurrentItem(2);
                break;


            case R.id.am_fab_next:
                if(ps!=null){

                    ps.next();

                }

                am_fam_container.collapse();
                break;

            case R.id.am_fab_play:

                if(ps!=null){


                    boolean isPlaying = (boolean) am_fab_play.getTag();

                    if(isPlaying){

                        ps.pause();

                    }else{

                        ps.start();

                    }


                }

                am_fam_container.collapse();


                break;

            case R.id.am_fab_pre:
                if(ps!=null){

                    ps.pre();

                }

                am_fam_container.collapse();
                break;




        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case REQUEST_LOGIN:
                //登录返回之后的结果
                setNvUser(MyApplication.account);
                break;


        }


    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {


        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return al_fragments.get(position);
        }

        @Override
        public int getCount() {
            if(al_fragments!=null){

                return al_fragments.size();
            }
            return 0;

        }
    }




}
