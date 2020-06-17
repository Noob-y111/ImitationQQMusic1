package com.example.imitationqqmusic.view.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.imitationqqmusic.R
import com.example.imitationqqmusic.databinding.ActivityLoginBinding
import com.example.imitationqqmusic.model.room_bean.User
import com.example.imitationqqmusic.model.tools.Rotate3DAnimation
import com.example.imitationqqmusic.model.tools.ToastUtil
import com.example.imitationqqmusic.view.login.fragment.LoginFragment
import com.example.imitationqqmusic.view.login.fragment.RegisteredFragment
import com.example.imitationqqmusic.view.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginToolbar.title = ""
        setSupportActionBar(binding.loginToolbar)

        var localConfig = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            localConfig = localConfig or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            binding.title.setTextColor(resources.getColor(R.color.blackTextColor, null))
        } else {
            binding.title.setTextColor(Color.WHITE)
            binding.loginToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }

        val view = window.decorView
        view.systemUiVisibility = localConfig
        window.statusBarColor = Color.TRANSPARENT

        viewModel = LoginViewModel.newInstance(this)!!
        viewModel.setUserDatabase(this)

        val fragmentLogin = LoginFragment()
        val fragmentRegistered = RegisteredFragment()

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_container, fragmentLogin, null)
                .commitAllowingStateLoss()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_container, fragmentRegistered, null)
                .commitAllowingStateLoss()

        viewModel.isLoginOrRegistered.observe(this, Observer {
            val y = binding.flContainer.y
            val x = binding.flContainer.height
            val point = Point()
            window.windowManager.defaultDisplay.getSize(point)
            val centerX = point.x.toFloat() / 2
            val centerY = binding.flContainer.height.toFloat() / 2 + y
            if (it) {
                if (viewModel.isFirst) {
                    val title = getString(R.string.login)
                    binding.title.text = title
                    binding.btnAction.text = title
                    supportFragmentManager.beginTransaction().hide(fragmentRegistered).commitAllowingStateLoss()
                    supportFragmentManager.beginTransaction().show(fragmentLogin).commitAllowingStateLoss()
                    viewModel.isFirst = false
                    return@Observer
                }

                val animation = Rotate3DAnimation(
                        0.0f, 90.0f, centerX, centerY, (x/2).toFloat(), true
                )
                animation.duration = 300
                animation.fillAfter = true
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val title = getString(R.string.login)
                        binding.title.text = title
                        binding.btnAction.text = title
                        supportFragmentManager.beginTransaction().hide(fragmentRegistered).commitAllowingStateLoss()
                        supportFragmentManager.beginTransaction().show(fragmentLogin).commitAllowingStateLoss()

                        val animation2 = Rotate3DAnimation(
                                270.0f, 360.0f, centerX, centerY, (x/2).toFloat(), false
                        )
                        animation2.duration = 300
                        animation2.fillAfter = true
                        animation2.setAnimationListener(object : Animation.AnimationListener{
                            override fun onAnimationRepeat(animation: Animation?) {

                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                binding.btnAction.isEnabled = true
                            }

                            override fun onAnimationStart(animation: Animation?) {

                            }

                        })
                        binding.flContainer.startAnimation(animation2)
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        binding.btnAction.isEnabled = false
                    }

                })
                binding.flContainer.startAnimation(animation)
            } else {
                if (viewModel.isFirst) {
                    val title = getString(R.string.register)
                    binding.title.text = title
                    binding.btnAction.text = title
                    supportFragmentManager.beginTransaction().hide(fragmentLogin).commitAllowingStateLoss()
                    supportFragmentManager.beginTransaction().show(fragmentRegistered).commitAllowingStateLoss()
                    viewModel.isFirst = false
                    return@Observer
                }

                val animation = Rotate3DAnimation(
                        360.0f, 270.0f, centerX, centerY, (x/2).toFloat(), true
                )
                animation.duration = 300
                animation.fillAfter = true
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val title = getString(R.string.register)
                        binding.title.text = title
                        binding.btnAction.text = title
                        supportFragmentManager.beginTransaction().hide(fragmentLogin).commitAllowingStateLoss()
                        supportFragmentManager.beginTransaction().show(fragmentRegistered).commitAllowingStateLoss()

                        val animation2 = Rotate3DAnimation(
                                90.0f, 0.0f, centerX, centerY, (x/2).toFloat(), false
                        )
                        animation2.duration = 300
                        animation2.fillAfter = true
                        animation2.setAnimationListener(object : Animation.AnimationListener{
                            override fun onAnimationRepeat(animation: Animation?) {

                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                binding.btnAction.isEnabled = true
                            }

                            override fun onAnimationStart(animation: Animation?) {

                            }

                        })
                        binding.flContainer.startAnimation(animation2)
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        binding.btnAction.isEnabled = false
                    }

                })
                binding.flContainer.startAnimation(animation)
            }
        })

        viewModel.toastString.observe(this, Observer {
            ToastUtil.showToast(this, it)
        })

        viewModel.loginEnable.observe(this, Observer {
            if (it){
                val bundle = Bundle()
                bundle.putParcelable("user", viewModel.user)

                val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("user_id", viewModel.user?.userId)
                editor.apply()

                toMainActivity(bundle)
            }
        })

        binding.tvTourists.setOnClickListener {
            val bundle = Bundle()
            val user = User(userId = "-1", name = "游客", userHead = null, passWord = "-1")
            bundle.putParcelable("user", user)
            toMainActivity(bundle)
        }

        binding.loginToolbar.setNavigationIcon(R.drawable.login_close)
        binding.loginToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnAction.setOnClickListener {
            if (viewModel.isLoginOrRegistered.value!!){
                if (!binding.checkbox.isChecked) {
                    viewModel.toastMessage("请同意用户协议，才能继续使用本软件")
                    return@setOnClickListener
                }
                viewModel.loginListener?.getLoginUser()
            }else{
                viewModel.registeredListener?.getRegisteredUser()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.add(0, 0, 1, "注册/登录")
        val item1 = menu?.add(0, 1, 2, "删除全部用户")
        item?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        item?.setIcon(R.drawable.rotate)

        item1?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        item1?.setIcon(R.drawable.clear_list)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            0 -> {
                viewModel.changeMode(!viewModel.isLoginOrRegistered.value!!)
            }

            1 -> {
                viewModel.clearAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toMainActivity(bundle: Bundle?){
        Intent(this, MainActivity::class.java).apply {
            putExtra("bundle", bundle)
            startActivity(this)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isFirst = true
    }

    interface OnLoginClick{
        fun getLoginUser(): User?
    }

    interface OnRegisteredClick{
        fun getRegisteredUser(): User?

    }
}