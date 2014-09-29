package com.tarena.fashionmusic.lrc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.List;


import com.yuri.uplayer.service.ApolloService;
import com.yuri.uplayer.ui.fragments.LrcShowFragment;
import com.tarena.fashionmusic.lrc.Lyric;
import com.tarena.fashionmusic.lrc.Sentence;

public class LyricView extends TextView
{
  private Paint NotCurrentPaint;
  private Paint CurrentPaint;
  private int notCurrentPaintColor = -12303292;
  private int CurrentPaintColor = -16777216;

  private Typeface Texttypeface = Typeface.SERIF;
  private Typeface CurrentTexttypeface = Typeface.DEFAULT_BOLD;
  private float width;
  private static Lyric mLyric;
  private int brackgroundcolor = 251658240;
  private float lrcTextSize = 22.0F;
  private float CurrentTextSize = 24.0F;

  String nowsen = null;
  public float mTouchHistoryY;
  private int height;
  private long currentDunringTime;
  private int TextHeight = 50;
  private boolean lrcInitDone = false;
  public int index = 0;
  private List<Sentence> Sentencelist;
  private long currentTime;
  private long sentenctTime;

  public Paint getNotCurrentPaint()
  {
    return this.NotCurrentPaint;
  }

  public void setNotCurrentPaint(Paint notCurrentPaint) {
    this.NotCurrentPaint = notCurrentPaint;
  }

  public boolean isLrcInitDone() {
    return this.lrcInitDone;
  }

  public Typeface getCurrentTexttypeface() {
    return this.CurrentTexttypeface;
  }

  public void setCurrentTexttypeface(Typeface currrentTexttypeface) {
    this.CurrentTexttypeface = currrentTexttypeface;
  }

  public void setLrcInitDone(boolean lrcInitDone) {
    this.lrcInitDone = lrcInitDone;
  }

  public float getLrcTextSize() {
    return this.lrcTextSize;
  }

  public void setLrcTextSize(float lrcTextSize) {
    this.lrcTextSize = lrcTextSize;
  }

  public float getCurrentTextSize() {
    return this.CurrentTextSize;
  }

  public void setCurrentTextSize(float currentTextSize) {
    this.CurrentTextSize = currentTextSize;
  }

  public static Lyric getmLyric() {
    return mLyric;
  }

  public void setmLyric(Lyric tomLyric) {
    mLyric = tomLyric;
  }

  public Paint getCurrentPaint() {
    return this.CurrentPaint;
  }

  public void setCurrentPaint(Paint currentPaint) {
    this.CurrentPaint = currentPaint;
  }

  public List<Sentence> getSentencelist() {
    return this.Sentencelist;
  }

  public void setSentencelist(List<Sentence> sentencelist) {
    this.Sentencelist = sentencelist;
  }

  public int getNotCurrentPaintColor() {
    return this.notCurrentPaintColor;
  }

  public void setNotCurrentPaintColor(int notCurrentPaintColor) {
    this.notCurrentPaintColor = notCurrentPaintColor;
  }

  public int getCurrentPaintColor() {
    return this.CurrentPaintColor;
  }

  public void setCurrentPaintColor(int currrentPaintColor) {
    this.CurrentPaintColor = currrentPaintColor;
  }

  public Typeface getTexttypeface() {
    return this.Texttypeface;
  }

  public void setTexttypeface(Typeface texttypeface) {
    this.Texttypeface = texttypeface;
  }

  public int getBrackgroundcolor() {
    return this.brackgroundcolor;
  }

  public void setBrackgroundcolor(int brackgroundcolor) {
    this.brackgroundcolor = brackgroundcolor;
  }

  public int getTextHeight() {
    return this.TextHeight;
  }

  public void setTextHeight(int textHeight) {
    this.TextHeight = textHeight;
  }

  public LyricView(Context context) {
    super(context);
    init();
  }

  public LyricView(Context context, AttributeSet attr) {
    super(context, attr);
    init();
  }

  public LyricView(Context context, AttributeSet attr, int i) {
    super(context, attr, i);
    init();
  }

  private void init() {
    setFocusable(true);

    this.NotCurrentPaint = new Paint();
    this.NotCurrentPaint.setAntiAlias(true);

    this.NotCurrentPaint.setTextAlign(Paint.Align.CENTER);

    this.CurrentPaint = new Paint();
    this.CurrentPaint.setAntiAlias(true);

    this.CurrentPaint.setTextAlign(Paint.Align.CENTER);
  }

  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);

    canvas.drawColor(this.brackgroundcolor);
    this.NotCurrentPaint.setColor(this.notCurrentPaintColor);
    this.CurrentPaint.setColor(this.CurrentPaintColor);

    this.NotCurrentPaint.setTextSize(this.lrcTextSize);

    this.NotCurrentPaint.setTypeface(this.Texttypeface);

    this.CurrentPaint.setTextSize(this.lrcTextSize);
    this.CurrentPaint.setTypeface(this.CurrentTexttypeface);

    if (this.index == -1) {
      return;
    }
    float plus = this.currentDunringTime == 0L ? 30.0F : 
      30.0F + 
      ((float)this.currentTime - (float)this.sentenctTime) / (float)this.currentDunringTime * 
      30.0F;

    canvas.translate(0.0F, -plus);
    try
    {
      canvas.drawText(((Sentence)this.Sentencelist.get(this.index)).getContent(), this.width / 2.0F, 
        this.height / 2, this.CurrentPaint);

      float tempY = this.height / 2;

      for (int i = this.index - 1; i >= 0; i--)
      {
        tempY -= this.TextHeight;
        if (tempY < 0.0F) {
          break;
        }
        canvas.drawText(((Sentence)this.Sentencelist.get(i)).getContent(), this.width / 2.0F, 
          tempY, this.NotCurrentPaint);
      }

      tempY = this.height / 2;

      for (int i = this.index + 1; i < this.Sentencelist.size(); i++)
      {
        tempY += this.TextHeight;
        if (tempY > this.height) {
          break;
        }
        canvas.drawText(((Sentence)this.Sentencelist.get(i)).getContent(), this.width / 2.0F, 
          tempY, this.NotCurrentPaint);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  protected void onSizeChanged(int w, int h, int ow, int oh)
  {
    super.onSizeChanged(w, h, ow, oh);
    this.width = w;
    this.height = h;
  }

	private final BroadcastReceiver StatusListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	intent = new Intent(ApolloService.UPDATE_LRC_ACTION);
        	context.sendBroadcast(intent);
             
        }
    };
    
  public void updateIndex(long time)
  {
    this.currentTime = time;

    this.index = mLyric.getNowSentenceIndex(time);
    if (this.index != -1) {
      Sentence sen = (Sentence)this.Sentencelist.get(this.index);

      String nowlrcsen = sen.getContent();
      if (("".equals(this.nowsen)) || (!nowlrcsen.equals(this.nowsen))) {
    	IntentFilter f = new IntentFilter();
    	f.addAction(ApolloService.PLAYSTATE_CHANGED);
	    f.addAction(ApolloService.META_CHANGED);
	    f.addAction(ApolloService.UPDATE_LRC_ACTION);
    	LrcShowFragment .intent.putExtra("lrccurr", sen.getContent());
    	LrcShowFragment.Lrccontext.registerReceiver(StatusListener,new IntentFilter(f));
        this.nowsen = nowlrcsen;
      }
      this.sentenctTime = sen.getFromTime();
      this.currentDunringTime = sen.getDuring();
    }
  }
}