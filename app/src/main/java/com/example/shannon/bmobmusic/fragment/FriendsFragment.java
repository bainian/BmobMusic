package com.example.shannon.bmobmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emokit.sdk.InitListener;
import com.emokit.sdk.senseface.ExpressionDetect;
import com.emokit.sdk.senseface.ExpressionListener;
import com.emokit.sdk.util.SDKAppInit;
import com.emokit.sdk.util.SDKConstant;
import com.example.shannon.bmobmusic.R;
import com.example.shannon.bmobmusic.bean.EmojiResult;
import com.google.gson.Gson;


public class FriendsFragment extends Fragment {




    private TextView ff_tv_test;
    private ListView ff_lv_recommened;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        System.out.println("friendsfragment创建");
        View view = inflater.inflate(R.layout.fragment_friends , null);
        ff_tv_test = (TextView) view.findViewById(R.id.ff_tv_test);
        ff_lv_recommened = (ListView) view.findViewById(R.id.ff_lv_recommened);


        //初始化情绪识别
        SDKAppInit.createInstance(getActivity());

        final ExpressionListener expressionListener = new ExpressionListener() {
            @Override
            public void beginDetect() {


                Toast.makeText(getContext(),"正在分析，请稍等",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void endDetect(String s) {

                System.out.println("测试结果：" + s);
                Gson gson = new Gson();
                EmojiResult emojiResult = gson.fromJson(s, EmojiResult.class);
                System.out.println(emojiResult.toString());
                if(emojiResult.getResultcode().equals("200")){

                    Toast.makeText(getContext(),"识别成功",Toast.LENGTH_SHORT).show();
                    ff_tv_test.setVisibility(View.INVISIBLE);



                }else{

                    Toast.makeText(getContext(),"识别失败",Toast.LENGTH_SHORT).show();

                }



            }
        };

        InitListener initListener = new InitListener(){

            @Override
            public void onInit(int i) {
                SDKAppInit.registerforuid("BmobMusic","shannonc@163.com","w2133707703");
            }
        };


        final ExpressionDetect expressionDetect = ExpressionDetect.createRecognizer(getContext(),initListener);


        expressionDetect.setParameter(SDKConstant.FACING,
                SDKConstant.CAMERA_FACING_FRONT);



        ff_tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                expressionDetect.startRateListening(expressionListener);


            }
        });


        return view;
    }









}
