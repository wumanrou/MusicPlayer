package com.example.musicplayer;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;


public class MusicService extends Service{
	//声明广播接收者
	ServiceReceiver serviceReceiver;
	//资源管理器
	AssetManager am;
	//定义几首歌曲
	String[] musics =new String[]{"Funny Day.mp3","Reset.mp3","Unity.mp3"};
	MediaPlayer mPlayer;
	//当前的状态，0x11:未播放；0x12：正在播放；0x13：暂停
	int status=0x11;
	//记录当前正在播放的音乐的序号
	int current=0;
	public IBinder onBind(Intent intent){
		return null;
	}
	public void onCreate(){
		//调用Context里的方法
		am=getAssets();
		//创建广播接收者对象
		serviceReceiver=new ServiceReceiver();
		//创建IntentFilter
		IntentFilter filter=new IntentFilter(MainActivity.CONTROL);
		//注册广播接收者
		registerReceiver(serviceReceiver,filter);
		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(new OnCompletionListener(){
			public void onCompletion(MediaPlayer mp){
			current++;
				//判断是否超出范围，如果超出，又从第一首开始
				if(current>=3){
					current=0;
			    }
				Intent sendIntent=new Intent(MainActivity.UPDATE);
				sendIntent.putExtra("current",current);
				//发送广播，将被Activity的广播接收器接收到
				sendBroadcast(sendIntent);
				//准备并播放音乐
				prepareAndPlay(musics [current]);
			 }
		});
		//创建媒体播放器
		super.onCreate();
	}
	public class ServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(final Context context, Intent intent) {
			// TODO Auto-generated method stub
			int control=intent.getIntExtra("control", -1);
			switch(control){
			//单机了“播放”或“暂停”按钮
			case 1:
			//原来处于没有播放状态
				if (status==0x11){
					//准备并播放音乐
					prepareAndPlay(musics [current]);
					status=0x12;
			}
				//原来处于播放状态
				else if (status==0x12){
					//暂停
					mPlayer.pause();
					//改变状态
					status=0x13;
				}
				//原来处于暂停状态
				else if (status==0x13){
					//播放
					mPlayer.start();
					//改变状态
					status=0x12;
			}
				break;
				//停止音乐
			case 2:
				//如果原来正在播放或暂停
				if (status==0x12||status==0x13){
					//停止播放
					mPlayer.stop();
					status=0x11;
				}
		}
			Intent sendIntent=new Intent(MainActivity.UPDATE);
			sendIntent.putExtra("update", status);
			sendIntent.putExtra("current", current);
			//发送广播，将被Acitvity组件中的广播接收器接收到
			sendBroadcast(sendIntent);
		}
	}
	private void prepareAndPlay(String music){
		try{
			//打开指定音乐文件
			AssetFileDescriptor afd=am.openFd(music);

			mPlayer.reset();
			//使用MediaPlayer加载指定的声音文件
			mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			//准备声音
			mPlayer.prepare();
			//播放
			mPlayer.start();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


}

