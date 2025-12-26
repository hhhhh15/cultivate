package com.example.cultivate.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class StudyTimerView @JvmOverloads constructor(
    context:Context,
    attrs: AttributeSet? = null
):View(context,attrs){

    //抗锯齿化的
    val paint1=Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.BLACK
        style=Paint.Style.STROKE
        strokeWidth=2f

    }
    val paint2=Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.parseColor("#98D551")
        style=Paint.Style.FILL
        strokeWidth=5f
    }

    var totalTime: Int = 1        // 避免除零
    var timeRemaining: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width= resolveSize(200,widthMeasureSpec)
        val height= resolveSize(200,heightMeasureSpec)
        setMeasuredDimension(width,height)
    }

    //通过获取到的view的高和宽，设置画笔的属性textSize文字大小
    override fun onSizeChanged(w:Int,h:Int,oldw:Int,oldh:Int){
        super.onSizeChanged(w, h, oldw, oldh)
        paint1.textSize= minOf(w,h)/6f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//圆心
        val Ox=width/2f
        val Oy=height/2f
        val radius=300f
//时间格式转换
        val minutes=timeRemaining / 60
        val seconds=timeRemaining % 60
        val timeStr=String.format("%02d:%02d",minutes,seconds)

        //画环道
//        val path=Path()
//        path.addCircle(Ox,Oy,radius,Path.Direction.CW)
//        path.addCircle(Ox,Oy,radius-30f,Path.Direction.CCW)
//        canvas.drawPath(path,paint2)
//画的两个描边圆
        canvas.drawCircle(Ox,Oy,radius,paint1)
        canvas.drawCircle(Ox,Oy,radius-30f,paint1)
//绘制的文字显示
        val textY = Oy - (paint1.ascent() + paint1.descent()) / 2
        paint1.textAlign = Paint.Align.CENTER
        canvas.drawText(timeStr,Ox,textY,paint1)
        //
        changeCircle(Ox,Oy,radius,canvas)
    }

    fun changeCircle(Ox:Float,Oy:Float,radius:Float,canvas: Canvas){

        val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#98D551") // 绿色
            style = Paint.Style.STROKE
            strokeWidth = 30f // 环道的厚度
            strokeCap = Paint.Cap.ROUND // 让圆弧端点是圆滑的
        }
        // 预留一半的 strokeWidth，防止画笔外溢,没看懂还有这个起始点为啥是-90f，这个view的起始点（0，0）到底在哪里啊
        val halfStroke = progressPaint.strokeWidth / 2
        val sweepAngle=(timeRemaining.toFloat() /totalTime)*360f
        val rect= RectF(Ox-radius+ halfStroke, Oy - radius+ halfStroke, Ox + radius- halfStroke, Oy + radius- halfStroke)
        canvas.drawArc(rect,-90f,sweepAngle,false,progressPaint)
    }
    fun setFixedTime(time: Int){

        totalTime=time
        Log.d("9.23", "5.1.setFixedTime中，也就是自定义绘制的页面$totalTime")
        invalidate()
    }
    fun updateTimeRemaining(time:Int){

        timeRemaining=time
        invalidate()
    }
}