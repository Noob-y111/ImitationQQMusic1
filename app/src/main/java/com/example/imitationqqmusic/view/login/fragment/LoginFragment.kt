package com.example.imitationqqmusic.view.login.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.imitationqqmusic.databinding.FragmentLoginBinding
import com.example.imitationqqmusic.model.room_bean.User
import com.example.imitationqqmusic.view.login.LoginActivity
import com.example.imitationqqmusic.view.login.LoginViewModel


class LoginFragment: Fragment(), LoginActivity.OnLoginClick {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = LoginViewModel.newInstance(requireActivity())!!
        viewModel.loginListener = this

    }

    override fun getLoginUser(): User? {
        val userId = binding.etUser.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (TextUtils.isEmpty(userId)){
            viewModel.toastMessage("用户名为空")
            return null
        }

        if (TextUtils.isEmpty(password)){
            viewModel.toastMessage("密码为空")
            return null
        }
        val user = User(name = null, userId = userId, passWord = password, userHead = null)
        viewModel.userExist(user)
        return user
    }
}