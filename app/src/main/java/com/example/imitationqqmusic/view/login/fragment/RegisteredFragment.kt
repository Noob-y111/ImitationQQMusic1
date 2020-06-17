package com.example.imitationqqmusic.view.login.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.imitationqqmusic.databinding.FragmentRegisteredBinding
import com.example.imitationqqmusic.model.room_bean.User
import com.example.imitationqqmusic.view.login.LoginActivity
import com.example.imitationqqmusic.view.login.LoginViewModel

class RegisteredFragment: Fragment(), LoginActivity.OnRegisteredClick {

    private lateinit var binding: FragmentRegisteredBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisteredBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = LoginViewModel.newInstance(requireActivity())!!
        viewModel.registeredListener = this

        viewModel.cleanEt.observe(viewLifecycleOwner, Observer {
            for (i in 0 until binding.linear.childCount){
                (binding.linear[i] as EditText).setText("")
            }
        })

    }

    override fun getRegisteredUser(): User? {
        val id = binding.etUser.text.toString().trim()
        val pwdOnce = binding.etPassword.text.toString().trim()
        val pwdTwice = binding.etPasswordTwice.text.toString().trim()
        val nickname = binding.etNickname.text.toString().trim()

        if (TextUtils.isEmpty(id)){
            viewModel.toastMessage("账号不能为空")
        }

        if (TextUtils.isEmpty(pwdOnce)){
            viewModel.toastMessage("密码不能为空")
        }

        if (pwdOnce != pwdTwice){
            viewModel.toastMessage("两次输入的密码不一致")
        }

        val user = User(userId = id, name = nickname, passWord = pwdOnce, userHead = null)
        viewModel.isHaveThisUser(user)
        return user
    }
}