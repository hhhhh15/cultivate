package com.example.cultivate


import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.cultivate.databinding.MainviewBinding
import com.example.cultivate.ui.fragment.TaskSubmitFragment
import com.example.cultivate.ui.fragment.forumModule
import com.example.cultivate.ui.fragment.grownModule
import com.example.cultivate.ui.fragment.taskCalendar
import com.example.cultivate.ui.fragment.taskModule
import com.example.cultivate.viewmodel.TopBarViewModel

class MainActivity: AppCompatActivity() {
    private lateinit var binding: MainviewBinding
    private val vm by viewModels<TopBarViewModel>()
    private val TAG = "mainactivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

         vm.topBarState.observe(this){topbar->
             if (topbar != null) {
                 binding.titleBar.text = topbar.title
                 topbar.id?.let { iconId ->
                     binding.titleRightContainer.setImageResource(iconId)
                     // 点击事件调用 lambda
                     binding.titleRightContainer.setOnClickListener {
                         topbar.clickAction?.invoke()
                     }
                 } ?: run {
                     binding.titleRightContainer.setImageDrawable(null)
                     binding.titleRightContainer.setOnClickListener(null)
                 }
             }
         }

        jumpToMainPage()

        //就是这句代码打开了taskmodule页面
        binding.bottomNavigation.selectedItemId = R.id.Task
        popMenu()

        supportFragmentManager.addOnBackStackChangedListener {
            checkMainFragment()
        }
    }

    private fun jumpToMainPage() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment: Fragment? = when (item.itemId) {
                R.id.forum -> forumModule()
                R.id.Task -> taskModule()
                R.id.Grown -> grownModule()
                else -> null
            }

            if (fragment != null) {
                changeFragment(fragment)
                true
            } else {
                Log.e(TAG, "jumpToMainPage: 该死跳转页面为空")
                false
            }
        }
    }
    //为啥这个mainactivity重启就是咋跳转到taskModule()啊
     fun changeFragment(targetFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentContainer, targetFragment)
            .addToBackStack(null)
            .commit()
    }

    //与popmeun方法配套使用
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
            Log.d("返回键", "像系统一样返回")
        }
    }
 //与popmeun方法配套使用
    private fun checkMainFragment() {
        val fragmentManager = supportFragmentManager
        val count = fragmentManager.backStackEntryCount
        Log.d("返回栈数量", "当前数量: $count")

        if (count == 0) {
            Log.d("返回栈空了", "finish activity")
            finish()
            return
        }
    }


    private fun popMenu() {
        binding.btnMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_time_study -> changeFragment(taskCalendar())
                R.id.menu_analysis_page -> {
                    Log.d("进入数据分析页面", "Button clicked")
                    changeFragment(taskCalendar())
                }
                R.id.menu_submit_page -> {
                    Log.d("进入提交页面", "Button clicked")
                    changeFragment(TaskSubmitFragment())
                }
                // 添加其他 menu case，如养成模块、论坛等
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}