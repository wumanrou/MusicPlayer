package com.example.musicplayer;



import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {
	//歌曲标题，作者文本框
	TextView title ,author;
	//播放/暂停、停止按钮
	ImageButton play,stop;
	//定义广播接收器
	ActivityReceiver activityReceiver;
	//控制播放、暂停
	public static final String CONTROL="android.control";
    //更新界面显示
	public static final String UPDATE="android.update";
	//定义播放状态，0x11:未播放；0x12：正在播放；0x13：暂停
	int status=0x11;
	//歌曲名
	String [] titleStrs=new String[]{"Funny Day","Reset","Unity"};
	//演唱者
	String [] authorStrs=new String[]{"Nichole Alden","theFatRat","Tiger JK、jinsi"};

	@Override
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//指定布局文件
		setContentView(R.layout.activity_main);
		//获取程序界面中的两个按钮以及两个文本显示框
		play=(ImageButton)this.findViewById(R.id.play);
		stop=(ImageButton)this.findViewById(R.id.stop);
		title=(TextView)this.findViewById(R.id.title);
		author=(TextView)this.findViewById(R.id.author);
		//为两个按钮添加监听器,MainActivity实现了OnClickListener接口
		play.setOnClickListener(this);
		stop.setOnClickListener(this);
		//创建广播接收者对象
		activityReceiver=new ActivityReceiver();
		//创建IntentFilter
		IntentFilter filter=new IntentFilter(UPDATE);
		registerReceiver(activityReceiver,filter);
		Intent intent=new Intent(this,MusicService.class);
		//启动后台Service
		startService(intent);
	}
		public class ActivityReceiver extends BroadcastReceiver {
        public void onReceiver(Context context,Intent intent){
				//获取Intent中的update消息
				int update=intent.getIntExtra("update", -1);
				//获取当前播放音乐的序号
				int current=intent.getIntExtra("current", -1);
			  //如果current 不为-1，则显示正在播放的音乐名和演唱者
				if(current>=0){
					title.setText(titleStrs[current]);
					     author.setText(authorStrs[current]);
				}
				//未播放状态显示播放按钮

				switch(update){
				case 0x11:
					play.setImageResource(R.drawable.selector_btn);
					//播放状态下设置使用暂停图标
				   case 0x12:
						play.setImageResource(R.drawable.stop);
					status=0x12;break;
					//暂停状态下设置使用播放图标
				   case 0x13:
			 play.setImageResource(R.drawable.stop);
						status=0x13;
						break;
				}
			}

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
       }
		
	

	@Override
	public void onClick(View source) {
		// TODO Auto-generated method stub
		//创建Intent
		Intent intent=new Intent (CONTROL);
		switch(source.getId()){
			//按下"播放"/"暂停"按钮
			case R.id.play:
				intent.putExtra("control",1);break;
			//按下"停止"按钮
			case R.id.stop:
				intent.putExtra("control",2);break;	
		}
		//发送广播，将被Service中的广播接收者收到
		sendBroadcast(intent);
    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
}

