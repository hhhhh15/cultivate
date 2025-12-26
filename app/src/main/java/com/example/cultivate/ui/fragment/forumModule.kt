package com.example.cultivate.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import com.example.cultivate.data.retrofit.RetrofitInstance
import com.example.cultivate.data.retrofit.model.test
import com.example.cultivate.databinding.ForumModuleBinding
import com.example.cultivate.ui.base.BaseViewBindingFragment
import com.example.cultivate.viewmodel.TopBarViewModel
import kotlinx.coroutines.launch
import androidx.media3.exoplayer.ExoPlayer


class forumModule: BaseViewBindingFragment<ForumModuleBinding>(){
    private val vm: TopBarViewModel by activityViewModels()
    private  val TAG="forum Module"
    private lateinit var player: ExoPlayer

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ForumModuleBinding {
        return ForumModuleBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.setTopBar("论坛模块",null)

        binding.postSave.setOnClickListener{
            val postContext=binding.editPostContent.text.toString()
            // 创建 test 对象
            val testObj = test(json = postContext)
            Log.d(TAG, "onViewCreated: 上传的内容是$postContext")

            //在fragment中启动协程，调用的协程域是lifecycleScope
            lifecycleScope.launch{
                try {
                    val receive=RetrofitInstance.testApi.testPost(testObj)
                    binding.setTestRetrofit.text = receive.toString()
                }catch (e: Exception){
                    e.printStackTrace()
                    Log.e("TAG", "网络请求失败", e)
                    binding.setTestRetrofit.text = "请求失败：${e.message}"
                }

            }
        }

        fun initPlayer(){
             player = ExoPlayer.Builder(requireContext()).build()
            //准备视频播放源
            val mediaItem = MediaItem.fromUri("https://vimeo.com/1084537")
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play() //播放视频
        }

    }


}