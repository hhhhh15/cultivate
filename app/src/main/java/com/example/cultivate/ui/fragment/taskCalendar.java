package com.example.cultivate.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cultivate.R;
import com.example.cultivate.databinding.CalendarItemBinding;
import com.example.cultivate.databinding.ShowcompletedTaskBinding;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;


import java.time.DayOfWeek;
import java.time.YearMonth;

import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class taskCalendar extends Fragment {
    private ShowcompletedTaskBinding binding;
    private List<DayOfWeek> daysOfWeek;

    public taskCalendar(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding=ShowcompletedTaskBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setcalendar();

    }

    private void setcalendar(){
        //1.获取Jav自带的枚举类DayOfWeek中的Monsday...
        daysOfWeek = Arrays.asList(DayOfWeek.values());

        // daysOfWeek = listOf(SUNDAY, MONDAY, TUESDAY, ..., SATURDAY);

        binding.calendarView.setup(
                YearMonth.now().minusMonths(12),
                YearMonth.now().plusMonths(12),
                daysOfWeek.get(0));//DayOfWeek.MONDAY
        binding.calendarView.scrollToMonth(YearMonth.now());//打开日历默认为现在这个月份

        //设置每cell的内容
        binding.calendarView.setDayBinder(new MonthDayBinder<simpleContainer>() {
            @Override
            public simpleContainer  create(View view) {
                return new simpleContainer (view);  // 创建 simpleContainer，把view包起来

            }
            @Override
            public void bind(simpleContainer  container, CalendarDay day) {
                //就可以用container调用布局cellbinding
//                container.cellbinding.textViewDate
                container.cellbinding.textViewDate.setText(day.getDate().getDayOfMonth()+"");
            }

        });
//5. 设置星期标题,就是将已经确定好的一星期的排序，然后填充
        //这里获取的日历标题组件应该是calendar_title里面的，而不是showcompeted里面的这个include的linelayout布局id
        ViewGroup titlesContainer = requireView().findViewById(R.id.titlesContainer);
        Log.d("Debug", "titlesContainer: " + titlesContainer);

        if (titlesContainer != null) {
            for (int i = 0; i < titlesContainer.getChildCount(); i++) {
                TextView textView = (TextView) titlesContainer.getChildAt(i);
                DayOfWeek dayOfWeek = daysOfWeek.get(i);//获取到dayOfWeek里面的标题数据monsday,然后转换名字，
                String title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());//然后设置到对应的7个textview里面
                textView.setText(title);
            }
        } else {
            Log.e("Error", "titlesContainer is null!");
        }
    }
     public class simpleContainer  extends ViewContainer{
        private CalendarItemBinding cellbinding;
       public  simpleContainer (View view){
           super(view);//调用父类的构造器传参数
           cellbinding=CalendarItemBinding.bind(view);//
        }
    }

}
